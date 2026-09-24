package com.example.casiostore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executors;

public class InicioFragment extends Fragment {

    public InicioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Encontramos los botones y el RecyclerView por su ID
        Button btnRelojes = view.findViewById(R.id.btnRelojes);
        Button btnCalculadoras = view.findViewById(R.id.btnCalculadoras);
        Button btnTeclados = view.findViewById(R.id.btnTeclados);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerProductos);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Cargar o insertar productos en segundo plano de manera segura
        Executors.newSingleThreadExecutor().execute(() -> {
            CasioDatabase db = CasioDatabase.getDatabase(getContext());
            List<ProductoEntity> listaCompleta = db.productoDao().obtenerTodos();

            // Si la base de datos está vacía, insertamos los productos iniciales automáticamente
            if (listaCompleta == null || listaCompleta.isEmpty()) {
                db.productoDao().insertarProducto(new ProductoEntity("Piano Casio AP-550BK", "piano digital casio 88 teclas...", 2000.0, "ap_550bk", "Teclados"));
                db.productoDao().insertarProducto(new ProductoEntity("Piano Casio AP-300BK", "piano digital casio 88 teclas...", 2330.0, "ap_300bk", "Teclados"));
                db.productoDao().insertarProducto(new ProductoEntity("Piano Casio AP-750BK", "piano digital casio 88 teclas...", 2330.0, "ap_750", "Teclados"));
                db.productoDao().insertarProducto(new ProductoEntity("Reloj Casio A-168WA", "reloj digital casio clásico...", 1234.0, "a168", "Relojes"));
                db.productoDao().insertarProducto(new ProductoEntity("Reloj Casio MTP-1302D", "reloj análogo elegante...", 1234.0, "mtp_1302d", "Relojes"));
                db.productoDao().insertarProducto(new ProductoEntity("Reloj Casio MTP-1314D", "reloj análogo con fechador...", 900.0, "mtp_1314d", "Relojes"));

                // Volvemos a consultar para obtener la lista completa ya con los datos insertados
                listaCompleta = db.productoDao().obtenerTodos();
            }

            List<ProductoEntity> finalListaCompleta = listaCompleta;

            // Verificamos que el fragmento siga activo antes de actualizar la UI
            if (isAdded() && getActivity() != null) {
                requireActivity().runOnUiThread(() -> {
                    ProductoAdapter adapter = new ProductoAdapter(finalListaCompleta, productoSeleccionado -> {
                        Intent intent = new Intent(getActivity(), DetalleProductoActivity.class);
                        intent.putExtra("NOMBRE", productoSeleccionado.nombre);
                        intent.putExtra("PRECIO", productoSeleccionado.precio);
                        intent.putExtra("DESCRIPCION", productoSeleccionado.descripcion);
                        intent.putExtra("IMAGEN", productoSeleccionado.imagenRes);
                        startActivity(intent);
                    });
                    recyclerView.setAdapter(adapter);
                });
            }
        });

        // 2. Programar el botón RELOJES
        if (btnRelojes != null) {
            btnRelojes.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), PantallaRelojes.class);
                intent.putExtra("CATEGORIA", "Relojes");
                startActivity(intent);
            });
        }

        // 3. Programar el botón CALCULADORAS
        if (btnCalculadoras != null) {
            btnCalculadoras.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), PantallaRelojes.class);
                intent.putExtra("CATEGORIA", "Calculadoras");
                startActivity(intent);
            });
        }

        // 4. Programar el botón TECLADOS
        if (btnTeclados != null) {
            btnTeclados.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), PantallaRelojes.class);
                intent.putExtra("CATEGORIA", "Teclados");
                startActivity(intent);
            });
        }
    }
}