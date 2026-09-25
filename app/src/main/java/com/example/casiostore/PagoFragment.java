package com.example.casiostore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.List;
import java.util.concurrent.Executors;

public class PagoFragment extends Fragment {

    private RecyclerView recyclerResumenPago;
    private EditText etNitCi, etRazonSocial, etTelefono;
    private Button btnQr, btnTarjeta;

    // Lanzador para iniciar la cámara y recibir el resultado del QR
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(getContext(), "Lectura cancelada", Toast.LENGTH_SHORT).show();
                } else {
                    String qrContenido = result.getContents();
                    Toast.makeText(getContext(), "Pago exitoso por QR: " + qrContenido, Toast.LENGTH_LONG).show();

                    // Vaciar carrito tras pago completado con QR
                    Executors.newSingleThreadExecutor().execute(() -> {
                        CasioDatabase db = CasioDatabase.getDatabase(getContext());
                        db.carritoDao().vaciarCarrito();

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                getParentFragmentManager().popBackStack();
                            });
                        }
                    });
                }
            }
    );

    public PagoFragment() {
        // Constructor público vacío
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pago, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar vistas
        recyclerResumenPago = view.findViewById(R.id.recyclerResumenPago);
        etNitCi = view.findViewById(R.id.etNitCi);
        etRazonSocial = view.findViewById(R.id.etRazonSocial);
        etTelefono = view.findViewById(R.id.etTelefono);
        btnQr = view.findViewById(R.id.btnQr);
        btnTarjeta = view.findViewById(R.id.btnTarjeta);

        // Configurar RecyclerView
        recyclerResumenPago.setLayoutManager(new LinearLayoutManager(getContext()));

        // Cargar productos guardados en el carrito
        cargarProductosResumen();

        // Al presionar el botón QR se abre la cámara para escanear
        btnQr.setOnClickListener(v -> abrirCamaraEscaner());

        // Al presionar el botón TARJETA se navega a PagoTarjetaFragment
        btnTarjeta.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contenedorprincipal, new PagoTarjetaFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void abrirCamaraEscaner() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Escanea el código QR de pago");
        options.setCameraId(0); // Cámara trasera por defecto
        options.setBeepEnabled(true); // Sonido al detectar el QR
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(false);

        // Iniciar la cámara
        barcodeLauncher.launch(options);
    }

    private void cargarProductosResumen() {
        Executors.newSingleThreadExecutor().execute(() -> {
            CasioDatabase db = CasioDatabase.getDatabase(getContext());
            List<CarritoEntity> listaCarrito = db.carritoDao().obtenerCarrito();

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (listaCarrito != null && !listaCarrito.isEmpty()) {
                        CarritoAdapter adapter = new CarritoAdapter(listaCarrito);
                        recyclerResumenPago.setAdapter(adapter);
                    }
                });
            }
        });
    }
}