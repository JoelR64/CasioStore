package com.example.casiostore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<ProductoEntity> listaProductos;
    private OnItemClickListener listener; // 1. Declarar el listener de clics

    // 2. Definir la interfaz para manejar el clic en los elementos
    public interface OnItemClickListener {
        void onItemClick(ProductoEntity producto);
    }

    // 3. Constructor actualizado que recibe la lista y el listener
    public ProductoAdapter(List<ProductoEntity> listaProductos, OnItemClickListener listener) {
        this.listaProductos = listaProductos;
        this.listener = listener;
    }

    public void actualizarLista(List<ProductoEntity> nuevaLista) {
        this.listaProductos = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        ProductoEntity producto = listaProductos.get(position);
        holder.txtNombre.setText(producto.nombre);
        holder.txtDescripcion.setText(producto.descripcion);
        holder.txtPrecio.setText(producto.precio + " BS");

        // Cargar imagen dinámicamente desde drawable usando el texto guardado en la BD
        int imageId = holder.itemView.getContext().getResources().getIdentifier(
                producto.imagenRes,
                "drawable",
                holder.itemView.getContext().getPackageName()
        );
        if (imageId != 0) {
            holder.imgProducto.setImageResource(imageId);
        }

        // 4. Configurar el evento de clic en toda la tarjeta del producto
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(producto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProducto;
        TextView txtNombre, txtDescripcion, txtPrecio;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
        }
    }
}