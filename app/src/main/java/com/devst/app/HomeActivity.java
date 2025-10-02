package com.devst.app;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;



public class HomeActivity extends AppCompatActivity {

    private String emailUsuario = "";
    private TextView tvBienvenida;

    private final ActivityResultLauncher<Intent> editarPerfilLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
                if(result.getResultCode() == RESULT_OK && result.getData() != null){
                    String nombre = result.getData().getStringExtra("nombre-editado");
                    if (nombre != null){
                        tvBienvenida.setText("Hola, " + nombre);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        tvBienvenida = findViewById(R.id.tvBienvenida);
        Button btnIrPerfil = findViewById(R.id.btnIrPerfil);
        Button btnAbrirWeb = findViewById(R.id.btnAbrirWeb);
        Button btnEnviarCorreo = findViewById(R.id.btnEnviarCorreo);
        Button btnCompartir = findViewById(R.id.btnCompartir);

        btnIrPerfil.setOnClickListener(v -> {
            Intent perfil = new Intent(HomeActivity.this, PerfilActivity.class);
            perfil.putExtra("email_usuario", emailUsuario);
            perfil.putExtra("nombre_usuario", getIntent().getStringExtra("nombre_usuario"));
            perfil.putExtra("pass_usuario", getIntent().getStringExtra("pass_usuario"));
            editarPerfilLauncher.launch(perfil); // en vez de startActivity
        });


        btnAbrirWeb.setOnClickListener(View ->{
            Uri url = Uri.parse("http://www.santotomas.cl");
            Intent viewWeb = new Intent(Intent.ACTION_VIEW, url);
            startActivity(viewWeb);
        });

        btnEnviarCorreo.setOnClickListener(View ->{
            Intent email = new Intent(Intent.ACTION_SENDTO);
            email.setData((Uri.parse("mailto:")));
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailUsuario});
            email.putExtra(Intent.EXTRA_SUBJECT, "Prueba desde la aplicacion");
            email.putExtra(Intent.EXTRA_TEXT, "OLA ESTAMOS REALIZANDO PRUEBAS ðŸ˜Ž");
            startActivity(Intent.createChooser(email, "Enviar informacion de prueba"));
        });
        btnCompartir.setOnClickListener((View ->{
            Intent compartir = new Intent(Intent.ACTION_SEND);
            compartir.setType("text/plain");
            compartir.putExtra(Intent.EXTRA_TEXT, "Hola mundo desde el btn compartir ðŸ˜Ž");
            startActivity(Intent.createChooser(compartir, "Saludo"));
        }));


        emailUsuario = getIntent().getStringExtra("email_usuario");
        if(emailUsuario == null) emailUsuario = "";
        tvBienvenida.setText("Bienvenida: " + emailUsuario);

    }

}