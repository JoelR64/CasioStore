package com.example.casiostore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private List<CarritoEntity> listaCarrito;

    public CarritoAdapter(List<CarritoEntity> listaCarrito) {
        this.listaCarrito = listaCarrito;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        CarritoEntity item = listaCarrito.get(position);
        holder.txtNombre.setText(item.nombre);
        holder.txtPrecio.setText("Bs. " + item.precio);
        holder.txtCantidad.setText(String.valueOf(item.cantidad));

        double subtotal = item.precio * item.cantidad;
        holder.txtSubtotal.setText("Bs. " + subtotal);

        // Cargar imagen directamente usando el entero del recurso (R.drawable.xxx)
        holder.imgProducto.setImageResource(item.imagenRes);
    }

    @Override
    public int getItemCount() {
        return listaCarrito.size();
    }

    public static class CarritoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProducto;
        TextView txtNombre, txtPrecio, txtCantidad, txtSubtotal;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProducto = itemView.findViewById(R.id.imgItemCarrito);
            txtNombre = itemView.findViewById(R.id.txtNombreItemCarrito);
            txtPrecio = itemView.findViewById(R.id.txtPrecioItemCarrito);
            txtCantidad = itemView.findViewById(R.id.txtCantidadItemCarrito);
            txtSubtotal = itemView.findViewById(R.id.txtSubtotalItemCarrito);
        }
    }
}