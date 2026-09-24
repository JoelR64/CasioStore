package com.example.casiostore;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;
import java.util.concurrent.Executors;

public class DetalleProductoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        // Referencias de los elementos de la vista
        ImageView btnDetalleAtras = findViewById(R.id.btnDetalleAtras);
        ImageView imgDetalleProducto = findViewById(R.id.imgDetalleProducto);
        TextView txtDetalleNombre = findViewById(R.id.txtDetalleNombre);
        TextView txtDetallePrecio = findViewById(R.id.txtDetallePrecio);
        TextView txtDetalleDescripcion = findViewById(R.id.txtDetalleDescripcion);

        // Botón para regresar a la pantalla anterior
        btnDetalleAtras.setOnClickListener(v -> finish());

        // Recibir los datos enviados por el Intent desde el adaptador o la lista
        String nombre = getIntent().getStringExtra("NOMBRE");
        double precio = getIntent().getDoubleExtra("PRECIO", 0.0);
        String descripcion = getIntent().getStringExtra("DESCRIPCION");
        String imagenRes = getIntent().getStringExtra("IMAGEN");

        // Asignar los datos a las vistas correspondientes
        if (nombre != null) {
            txtDetalleNombre.setText(nombre);
        }

        txtDetallePrecio.setText("$ " + precio);

        if (descripcion != null) {
            txtDetalleDescripcion.setText(descripcion);
        }

        // Cargar la imagen dinámicamente según el nombre del recurso guardado en la base de datos
        if (imagenRes != null && !imagenRes.isEmpty()) {
            int imageResourceID = getResources().getIdentifier(imagenRes, "drawable", getPackageName());
            if (imageResourceID != 0) {
                imgDetalleProducto.setImageResource(imageResourceID);
            } else {
                // Imagen por defecto si no se encuentra el recurso
                imgDetalleProducto.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }

        Button btnAgregarCarrito = findViewById(R.id.btnAgregarCarrito); // Asegúrate de darle este ID en el XML de detalle

        btnAgregarCarrito.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                CasioDatabase db = CasioDatabase.getDatabase(this);

                // Creamos el ítem para el carrito con los datos recibidos del Intent
                CarritoEntity item = new CarritoEntity(nombre, precio, 1, imagenRes, descripcion);
                db.carritoDao().insertarAlCarrito(item);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Agregado al carrito exitosamente", Toast.LENGTH_SHORT).show();
                    // Opcional: Redirigir al carrito o cerrar detalle
                    finish();
                });
            });
        });
    }
}