package com.example.casiostore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UsuarioFragment extends Fragment {

    private TextView txtNombrePerfil, txtCorreoPerfil, txtEmailDetalle;
    private Button btnIrHistorial, btnCerrarSesion, btnEliminarCuenta, btnLoginDesdePerfil;
    private LinearLayout layoutUsuarioLogueado, layoutInvitado;

    public UsuarioFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_usuario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Enlazar vistas
        layoutUsuarioLogueado = view.findViewById(R.id.layoutUsuarioLogueado);
        layoutInvitado = view.findViewById(R.id.layoutInvitado);
        txtNombrePerfil = view.findViewById(R.id.txtNombrePerfil);
        txtCorreoPerfil = view.findViewById(R.id.txtCorreoPerfil);
        txtEmailDetalle = view.findViewById(R.id.txtEmailDetalle);
        btnIrHistorial = view.findViewById(R.id.btnIrHistorial);
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        btnEliminarCuenta = view.findViewById(R.id.btnEliminarCuenta);
        btnLoginDesdePerfil = view.findViewById(R.id.btnLoginDesdePerfil);

        // Verificar el tipo de usuario recibido
        boolean esInvitado = false;
        if (getActivity() != null && getActivity().getIntent() != null) {
            String usuarioExtra = getActivity().getIntent().getStringExtra("Usuario");
            if ("Invitado".equals(usuarioExtra) || usuarioExtra == null) {
                esInvitado = true;
            } else {
                txtNombrePerfil.setText(usuarioExtra);
                txtCorreoPerfil.setText(usuarioExtra.contains("@") ? usuarioExtra : usuarioExtra.toLowerCase() + "@casiostore.com");
                txtEmailDetalle.setText(usuarioExtra.contains("@") ? usuarioExtra : usuarioExtra.toLowerCase() + "@casiostore.com");
            }
        } else {
            esInvitado = true;
        }

        // Alternar vistas según el estado
        if (esInvitado) {
            layoutUsuarioLogueado.setVisibility(View.GONE);
            layoutInvitado.setVisibility(View.VISIBLE);
        } else {
            layoutUsuarioLogueado.setVisibility(View.VISIBLE);
            layoutInvitado.setVisibility(View.GONE);
        }

        // Botón para iniciar sesión desde el perfil de invitado
        btnLoginDesdePerfil.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Botón para ir al Historial de Compras
        btnIrHistorial.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contenedorprincipal, new HistorialComprasFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Botón Cerrar Sesión
        btnCerrarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Botón Eliminar Cuenta
        btnEliminarCuenta.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Función de eliminar cuenta no disponible por el momento", Toast.LENGTH_SHORT).show();
        });
    }
}