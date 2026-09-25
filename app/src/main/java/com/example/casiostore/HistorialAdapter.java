package com.example.casiostore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder> {

    private List<HistorialEntity> listaHistorial;

    public HistorialAdapter(List<HistorialEntity> listaHistorial) {
        this.listaHistorial = listaHistorial;
    }

    @NonNull
    @Override
    public HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
        return new HistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder holder, int position) {
        HistorialEntity item = listaHistorial.get(position);
        holder.txtNombre.setText(item.nombreProducto);
        holder.txtFecha.setText(item.fecha);
        holder.txtMonto.setText("Bs. " + item.monto);
    }

    @Override
    public int getItemCount() {
        return listaHistorial.size();
    }

    public static class HistorialViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtFecha, txtMonto;

        public HistorialViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreHistorial);
            txtFecha = itemView.findViewById(R.id.txtFechaHistorial);
            txtMonto = itemView.findViewById(R.id.txtMontoHistorial);
        }
    }
}