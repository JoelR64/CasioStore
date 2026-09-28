package com.example.casiostore; // <--- ¡ESTA LÍNEA DEBE IR HASTA ARRIBA DEL TODO!

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GhostFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflamos el layout de la pantalla fantasma
        return inflater.inflate(R.layout.fragment_ghost, container, false);
    }
}