package com.example.casiostore;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class PagoTarjetaFragment extends Fragment {

    private EditText etNumeroTarjeta, etExpiracion, etCvv;
    private Button btnTerminarPago;
    private String correoUsuarioActual;

    public PagoTarjetaFragment() {
        // Constructor público requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pago_tarjeta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNumeroTarjeta = view.findViewById(R.id.etNumeroTarjeta);
        etExpiracion = view.findViewById(R.id.etExpiracion);
        etCvv = view.findViewById(R.id.etCvv);
        btnTerminarPago = view.findViewById(R.id.btnTerminarPago);

        // Obtener el correo del usuario logueado actualmente (desde SharedPreferences)
        SharedPreferences prefs = requireActivity().getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE);
        correoUsuarioActual = prefs.getString("correo_usuario", "");

        // Cargar los datos de tarjeta si ya los guardó antes
        cargarTarjetaGuardada();

        // Formateador automático para la fecha MM/AA
        etExpiracion.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) return;

                isUpdating = true;
                String clean = s.toString().replaceAll("[^\\d]", "");

                if (clean.length() >= 2) {
                    String mm = clean.substring(0, 2);
                    String aa = clean.length() > 2 ? clean.substring(2, Math.min(clean.length(), 4)) : "";

                    int mesInt = Integer.parseInt(mm);
                    if (mesInt > 12) mm = "12";
                    if (mesInt == 0) mm = "01";

                    String formatted = mm + "/" + aa;
                    s.replace(0, s.length(), formatted);
                }

                isUpdating = false;
            }
        });

        btnTerminarPago.setOnClickListener(v -> procesarPagoTarjeta());
    }

    private void cargarTarjetaGuardada() {
        if (TextUtils.isEmpty(correoUsuarioActual)) return;

        Executors.newSingleThreadExecutor().execute(() -> {
            CasioDatabase db = CasioDatabase.getDatabase(getContext());
            UsuarioEntity usuario = db.usuarioDao().obtenerUsuarioPorCorreo(correoUsuarioActual);

            if (usuario != null && usuario.numeroTarjeta != null && !usuario.numeroTarjeta.isEmpty()) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        etNumeroTarjeta.setText(usuario.numeroTarjeta);
                        etExpiracion.setText(usuario.expiracionTarjeta);
                        etCvv.setText(usuario.cvvTarjeta);
                    });
                }
            }
        });
    }

    private void procesarPagoTarjeta() {
        String numTarjeta = etNumeroTarjeta.getText().toString().trim();
        String expiracion = etExpiracion.getText().toString().trim();
        String cvv = etCvv.getText().toString().trim();

        if (TextUtils.isEmpty(numTarjeta) || numTarjeta.length() < 13) {
            etNumeroTarjeta.setError("Ingrese un número de tarjeta válido");
            return;
        }

        if (expiracion.length() == 4 && !expiracion.contains("/")) {
            String mm = expiracion.substring(0, 2);
            String aa = expiracion.substring(2, 4);
            expiracion = mm + "/" + aa;
            etExpiracion.setText(expiracion);
        }

        if (TextUtils.isEmpty(expiracion) || !expiracion.contains("/") || expiracion.length() < 5) {
            etExpiracion.setError("Formato MM/AA requerido (ej. 12/26)");
            return;
        }

        if (TextUtils.isEmpty(cvv) || cvv.length() < 3) {
            etCvv.setError("Ingrese CVV válido");
            return;
        }

        Toast.makeText(getContext(), "¡Pago realizado con éxito!", Toast.LENGTH_SHORT).show();

        // Creamos copias finales para usarlas dentro del hilo de ejecución (lambda)
        final String tarjetaFinal = numTarjeta;
        final String expiracionFinal = expiracion;
        final String cvvFinal = cvv;

        Executors.newSingleThreadExecutor().execute(() -> {
            CasioDatabase db = CasioDatabase.getDatabase(getContext());

            // 1. Guardar o actualizar la tarjeta del usuario para que no tenga que volver a escribirla
            if (!TextUtils.isEmpty(correoUsuarioActual)) {
                UsuarioEntity usuario = db.usuarioDao().obtenerUsuarioPorCorreo(correoUsuarioActual);
                if (usuario != null) {
                    usuario.numeroTarjeta = tarjetaFinal;
                    usuario.expiracionTarjeta = expiracionFinal;
                    usuario.cvvTarjeta = cvvFinal;
                    db.usuarioDao().actualizar(usuario);
                }
            }

            // 2. Procesar la compra normal (Historial, Tabla Temporal y Vaciar Carrito)
            List<CarritoEntity> listaCarrito = db.carritoDao().obtenerCarrito();

            if (listaCarrito != null && !listaCarrito.isEmpty()) {
                String fechaActual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                db.ultimaCompraDao().vaciarUltimaCompra();

                for (CarritoEntity item : listaCarrito) {
                    double totalItem = item.precio * item.cantidad;
                    db.historialDao().insertarCompra(new HistorialEntity(item.nombre, totalItem, fechaActual));

                    db.ultimaCompraDao().insertar(new UltimaCompraEntity(
                            item.nombre, item.precio, item.cantidad, item.imagenRes, item.descripcion
                    ));
                }
            }

            db.carritoDao().vaciarCarrito();

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.contenedorprincipal, new DetalleCompraFragment())
                            .commit();
                });
            }
        });
    }
}