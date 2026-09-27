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

public class NotificacionesFragment extends Fragment {

    private RecyclerView rvNotificaciones;

    public NotificacionesFragment() {
        // Constructor público requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notificaciones, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvNotificaciones = view.findViewById(R.id.rvNotificaciones);
        rvNotificaciones.setLayoutManager(new LinearLayoutManager(getContext()));

        // Cargar las notificaciones de la base de datos
        cargarNotificaciones();
    }

    private void cargarNotificaciones() {
        Executors.newSingleThreadExecutor().execute(() -> {
            CasioDatabase db = CasioDatabase.getDatabase(getContext());

            // Verificamos si la tabla está vacía y agregamos una notificación de prueba si es necesario
            List<NotificacionEntity> lista = db.notificacionDao().obtenerNotificaciones();
            if (lista == null || lista.isEmpty()) {
                db.notificacionDao().insertar(new NotificacionEntity("25/09/2026", "¡Bienvenido a Casio Store! Revisa nuestros nuevos modelos."));
                db.notificacionDao().insertar(new NotificacionEntity("24/09/2026", "Tu compra ha sido procesada con éxito."));
                lista = db.notificacionDao().obtenerNotificaciones();
            }

            final List<NotificacionEntity> listaFinal = lista;

            // Actualizar la UI en el hilo principal
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    NotificacionAdapter adapter = new NotificacionAdapter(listaFinal);
                    rvNotificaciones.setAdapter(adapter);
                });
            }
        });
    }
}