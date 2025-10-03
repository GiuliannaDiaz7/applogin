package com.devst.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {

    private TextView tvNombre, tvCorreo, tvPass;
    private Button btnAcercaDe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        tvNombre = findViewById(R.id.tvNombre);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvPass = findViewById(R.id.tvPass);
        btnAcercaDe = findViewById(R.id.btnAcercaDe);

        // Recuperar datos enviados
        String email = getIntent().getStringExtra("email_usuario");
        String nombre = getIntent().getStringExtra("nombre_usuario");
        String pass = getIntent().getStringExtra("pass_usuario");

        // Mostrar en TextViews
        tvNombre.setText("Nombre: " + nombre);
        tvCorreo.setText("Correo: " + email);
        tvPass.setText("Contraseña: " + pass);

        // Botón Acerca de -> abre GitHub
        btnAcercaDe.setOnClickListener(v -> {
            Uri github = Uri.parse("https://github.com/GiuliannaDiaz7");
            Intent intent = new Intent(Intent.ACTION_VIEW, github);
            startActivity(intent);
        });
    }
}
