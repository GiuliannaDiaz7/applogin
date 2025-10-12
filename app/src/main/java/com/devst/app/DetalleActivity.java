package com.devst.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetalleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        // Referencias a los elementos del XML
        TextView tvTitulo = findViewById(R.id.tvTitulo);
        TextView tvDescripcion = findViewById(R.id.tvDescripcion);
        Button btnVolver = findViewById(R.id.btnVolver);

        // Obtener los datos enviados desde HomeActivity
        String titulo = getIntent().getStringExtra("titulo");
        String descripcion = getIntent().getStringExtra("descripcion");

        // Mostrar los datos
        tvTitulo.setText(titulo);
        tvDescripcion.setText(descripcion);

        // btn Volver
        btnVolver.setOnClickListener(v -> finish());
    }
}
