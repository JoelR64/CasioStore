package com.example.casiostore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class InicioFragment extends Fragment {

    private List<ProductoEntity> listaCompletaProductos = new ArrayList<>();
    private ProductoAdapter adapter;
    private RecyclerView recyclerView;

    // Variables para el Carrusel Dinámico
    private ImageView bannerCarousel;
    private int[] imagenesBanner = {
            R.drawable.carrusel, // Imagen principal actual
            R.drawable.carrusel_2, // Puedes cambiar estas por tus banners diseñados
            R.drawable.carrusel_3     // Tercera imagen del carrusel
    };
    private int indiceBanner = 0;
    private final Handler handlerBanner = new Handler(Looper.getMainLooper());
    private Runnable runnableBanner;

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

        // 1. Encontramos los componentes por su ID
        Button btnRelojes = view.findViewById(R.id.btnRelojes);
        Button btnCalculadoras = view.findViewById(R.id.btnCalculadoras);
        Button btnTeclados = view.findViewById(R.id.btnTeclados);
        EditText inputBuscar = view.findViewById(R.id.inputBuscar);
        bannerCarousel = view.findViewById(R.id.bannerCarousel); // <--- Referencia al Banner

        recyclerView = view.findViewById(R.id.recyclerProductos);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Iniciar el Carrusel Automático (Cambia de imagen cada 3.5 segundos)
        iniciarCarrusel();

        // Cargar productos en segundo plano
        Executors.newSingleThreadExecutor().execute(() -> {
            CasioDatabase db = CasioDatabase.getDatabase(getContext());
            listaCompletaProductos = db.productoDao().obtenerTodos();

            // Si la base de datos está vacía, insertamos los productos iniciales
            if (listaCompletaProductos == null || listaCompletaProductos.isEmpty()) {
                db.productoDao().insertar(new ProductoEntity("Piano Casio AP-550BK", 2000.0, "piano digital casio 88 teclas...", R.drawable.ap_550bk, "Teclados", 1));
                db.productoDao().insertar(new ProductoEntity("Piano Casio AP-300BK", 2330.0, "piano digital casio 88 teclas...", R.drawable.ap_300bk, "Teclados", 1));
                db.productoDao().insertar(new ProductoEntity("Piano Casio AP-750BK", 2330.0, "piano digital casio 88 teclas...", R.drawable.ap_750, "Teclados", 1));
                db.productoDao().insertar(new ProductoEntity("Reloj Casio A-168WA", 1234.0, "reloj digital casio clásico...", R.drawable.a168, "Relojes", 1));
                db.productoDao().insertar(new ProductoEntity("Reloj Casio MTP-1302D", 1234.0, "reloj análogo elegante...", R.drawable.mtp_1302d, "Relojes", 1));
                db.productoDao().insertar(new ProductoEntity("Reloj Casio MTP-1314D", 900.0, "reloj análogo con fechador...", R.drawable.mtp_1314d, "Relojes", 1));

                listaCompletaProductos = db.productoDao().obtenerTodos();
            }

            List<ProductoEntity> finalListaCompleta = listaCompletaProductos;

            // Verificamos que el fragmento siga activo antes de actualizar la UI
            if (isAdded() && getActivity() != null) {
                requireActivity().runOnUiThread(() -> configurarAdapter(finalListaCompleta));
            }
        });

        // 2. FILTRADO EN TIEMPO REAL (Estilo Reactivo / Vue)
        if (inputBuscar != null) {
            inputBuscar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filtrarProductos(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        // 3. Programar el botón RELOJES
        if (btnRelojes != null) {
            btnRelojes.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), PantallaRelojes.class);
                intent.putExtra("CATEGORIA", "RELOJES");
                startActivity(intent);
            });
        }

        // 4. Programar el botón CALCULADORAS
        if (btnCalculadoras != null) {
            btnCalculadoras.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), PantallaRelojes.class);
                intent.putExtra("CATEGORIA", "CALCULADORAS");
                startActivity(intent);
            });
        }

        // 5. Programar el botón TECLADOS
        if (btnTeclados != null) {
            btnTeclados.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), PantallaRelojes.class);
                intent.putExtra("CATEGORIA", "TECLADOS");
                startActivity(intent);
            });
        }
    }

    // --- MÉTODO PARA INICIAR EL CARRUSEL AUTOMÁTICO ---
    private void iniciarCarrusel() {
        runnableBanner = new Runnable() {
            @Override
            public void run() {
                if (bannerCarousel != null) {
                    // Cambia la imagen del banner usando el índice actual
                    bannerCarousel.setImageResource(imagenesBanner[indiceBanner]);

                    // Avanza al siguiente índice de forma circular
                    indiceBanner = (indiceBanner + 1) % imagenesBanner.length;
                }
                // Repite la ejecución cada 3500 milisegundos (3.5 segundos)
                handlerBanner.postDelayed(this, 3500);
            }
        };
        handlerBanner.postDelayed(runnableBanner, 3500);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Detenemos el carrusel cuando se destruye la vista para evitar consumo de memoria
        if (handlerBanner != null && runnableBanner != null) {
            handlerBanner.removeCallbacks(runnableBanner);
        }
    }

    // Método para inicializar el adaptador del RecyclerView
    private void configurarAdapter(List<ProductoEntity> lista) {
        adapter = new ProductoAdapter(lista, productoSeleccionado -> {
            Intent intent = new Intent(getActivity(), DetalleProductoActivity.class);
            intent.putExtra("NOMBRE", productoSeleccionado.nombre);
            intent.putExtra("PRECIO", productoSeleccionado.precio);
            intent.putExtra("DESCRIPCION", productoSeleccionado.descripcion);
            intent.putExtra("IMAGEN", productoSeleccionado.imagenRes);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    // Método de filtrado rápido en tiempo real
    private void filtrarProductos(String textoBusqueda) {
        List<ProductoEntity> listaFiltrada = new ArrayList<>();
        String textoFiltro = textoBusqueda.toLowerCase().trim();

        for (ProductoEntity producto : listaCompletaProductos) {
            if (producto.nombre.toLowerCase().contains(textoFiltro) ||
                    producto.descripcion.toLowerCase().contains(textoFiltro)) {
                listaFiltrada.add(producto);
            }
        }

        if (adapter != null) {
            adapter.actualizarLista(listaFiltrada);
        }
    }
}