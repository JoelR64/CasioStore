package com.example.casiostore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.concurrent.Executors;

public class HistorialComprasFragment extends Fragment {

    private RecyclerView recyclerHistorialCompras;

    public HistorialComprasFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_historial_compras, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerHistorialCompras = view.findViewById(R.id.recyclerHistorialCompras);
        recyclerHistorialCompras.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarHistorialDesdeBaseDeDatos();
    }

    private void cargarHistorialDesdeBaseDeDatos() {
        Executors.newSingleThreadExecutor().execute(() -> {
            CasioDatabase db = CasioDatabase.getDatabase(getContext());
            List<HistorialEntity> listaHistorial = db.historialDao().obtenerHistorial();

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (listaHistorial != null && !listaHistorial.isEmpty()) {
                        HistorialAdapter adapter = new HistorialAdapter(listaHistorial);
                        recyclerHistorialCompras.setAdapter(adapter);
                    }
                });
            }
        });
    }
}