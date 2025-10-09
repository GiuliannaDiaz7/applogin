package com.devst.app;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class HomeActivity extends AppCompatActivity {

    private String emailUsuario = "";
    private TextView tvBienvenida;

    private Button btnLinterna;
    private CameraManager camara;
    private String camaraID = null;
    private boolean luz = false;


    private final ActivityResultLauncher<Intent> editarPerfilLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
                if(result.getResultCode() == RESULT_OK && result.getData() != null){
                    String nombre = result.getData().getStringExtra("nombre-editado");
                    if (nombre != null){
                        tvBienvenida.setText("Hola, " + nombre);
                    }
                }
            });

    private final ActivityResultLauncher<String>permisoCamaraLauncher=
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted ->{
                if(granted){
                    alternarluz();
                }else{
                    Toast.makeText(this, "Permiso de camara denegado" , Toast.LENGTH_SHORT).show();
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
        Button btnLinterna = findViewById(R.id.btnLinterna);
        Button btnCamara = findViewById(R.id.btnCamara);
        Button btnMaps = findViewById(R.id.btnMaps);
        Button btnLlamar = findViewById(R.id.btnLlamar);
        Button btnGaleria = findViewById(R.id.btnGaleria);
        Button btnWifi = findViewById(R.id.btnWifi);

        btnIrPerfil.setOnClickListener(v -> {
            Intent perfil = new Intent(HomeActivity.this, PerfilActivity.class);
            perfil.putExtra("email_usuario", emailUsuario);
            perfil.putExtra("nombre_usuario", getIntent().getStringExtra("nombre_usuario"));
            perfil.putExtra("pass_usuario", getIntent().getStringExtra("pass_usuario"));
            editarPerfilLauncher.launch(perfil);
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
            email.putExtra(Intent.EXTRA_TEXT, "OLA ESTAMOS REALIZANDO PRUEBAS ");
            startActivity(Intent.createChooser(email, "Enviar informacion de prueba"));
        });
        btnCompartir.setOnClickListener((View ->{
            Intent compartir = new Intent(Intent.ACTION_SEND);
            compartir.setType("text/plain");
            compartir.putExtra(Intent.EXTRA_TEXT, "Hola mundo desde el btn compartir ");
            startActivity(Intent.createChooser(compartir, "Saludo"));
        }));

        btnCamara.setOnClickListener(v ->
                startActivity(new Intent(this, CamaraActivity.class))
        );
        btnMaps.setOnClickListener(v -> {
            Uri ubicacion = Uri.parse("geo:-33.0472,-71.6127");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, ubicacion);
            startActivity(mapIntent);
        });


        btnLlamar.setOnClickListener(v -> {
            Uri telefono = Uri.parse("tel:+56992034061");
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, telefono);
            startActivity(dialIntent);
        });


        btnGaleria.setOnClickListener(v -> {
            Intent galeria = new Intent(Intent.ACTION_GET_CONTENT);
            galeria.setType("image/*");
            startActivity(Intent.createChooser(galeria, "Selecciona una imagen"));
        });


        btnWifi.setOnClickListener(v -> {
            Intent wifiSettings = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
            startActivity(wifiSettings);
        });

        camara = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
           for(String id : camara.getCameraIdList()){
               CameraCharacteristics cc = camara.getCameraCharacteristics(id);
               Boolean disponibleFlash = cc.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
               Integer lensFacing = cc.get(CameraCharacteristics.LENS_FACING);
               if(Boolean.TRUE.equals(disponibleFlash)
                   && lensFacing != null
                   && lensFacing == CameraCharacteristics.LENS_FACING_BACK){
                   camaraID= id;
                   break;
               }
           }
        }catch (CameraAccessException e){
            Toast.makeText(this, "No se puede acceder a la camara", Toast.LENGTH_SHORT).show();
        }
        btnLinterna.setOnClickListener(v -> {
            if (camaraID == null){
                Toast.makeText(this,"Este dispositivo no tiene flash disponible",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            boolean camGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED;

            if (camGranted) {
                alternarluz();
            }else{
                permisoCamaraLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        emailUsuario = getIntent().getStringExtra("email_usuario");
        if(emailUsuario == null) emailUsuario = "";
        tvBienvenida.setText("Bienvenida: " + emailUsuario);


    }
    private void alternarluz(){
        try{
            luz = !luz;
            camara.setTorchMode(camaraID, luz);
            btnLinterna.setText(luz ? "Apagar Linterna" : "Encender Linterna");
        }catch (CameraAccessException e){
            Toast.makeText(this , "Error al controlar la linterna",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected  void onPause(){
        super.onPause();
        if(camaraID != null && luz){
            try {
                camara.setTorchMode(camaraID,false);
                luz = false;
                if (btnLinterna != null) btnLinterna.setText("Encender Linterna");
            }catch ( CameraAccessException ignored){}
        }
    }


}