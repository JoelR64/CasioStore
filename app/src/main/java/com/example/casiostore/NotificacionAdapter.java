package com.example.casiostore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.ViewHolder> {

    private final List<NotificacionEntity> listaNotificaciones;

    public NotificacionAdapter(List<NotificacionEntity> listaNotificaciones) {
        this.listaNotificaciones = listaNotificaciones;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Corregido al nombre estándar item_notificaciones
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificacionEntity noti = listaNotificaciones.get(position);
        holder.tvFecha.setText(noti.fecha);
        holder.tvMensaje.setText(noti.mensaje);
    }

    @Override
    public int getItemCount() {
        return listaNotificaciones.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvMensaje;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFechaNotificacion);
            tvMensaje = itemView.findViewById(R.id.tvMensajeNotificacion);
        }
    }
}