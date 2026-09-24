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

        // Recibir los datos enviados por el Intent (La imagen ahora es int)
        String nombre = getIntent().getStringExtra("NOMBRE");
        double precio = getIntent().getDoubleExtra("PRECIO", 0.0);
        String descripcion = getIntent().getStringExtra("DESCRIPCION");
        int imagenRes = getIntent().getIntExtra("IMAGEN", 0); // <-- CORREGIDO A INT

        // Asignar los datos a las vistas correspondientes
        if (nombre != null) {
            txtDetalleNombre.setText(nombre);
        }

        txtDetallePrecio.setText("Bs. " + precio);

        if (descripcion != null) {
            txtDetalleDescripcion.setText(descripcion);
        }

        // Cargar la imagen directamente con el entero del recurso (R.drawable.xxx)
        if (imagenRes != 0) {
            imgDetalleProducto.setImageResource(imagenRes);
        } else {
            imgDetalleProducto.setImageResource(R.drawable.ic_launcher_foreground);
        }

        Button btnAgregarCarrito = findViewById(R.id.btnAgregarCarrito);

        btnAgregarCarrito.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                CasioDatabase db = CasioDatabase.getDatabase(this);

                // Creamos el ítem para el carrito con la imagen en formato int
                CarritoEntity item = new CarritoEntity(nombre, precio, 1, imagenRes, descripcion);
                db.carritoDao().insertarAlCarrito(item);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Agregado al carrito exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }
}