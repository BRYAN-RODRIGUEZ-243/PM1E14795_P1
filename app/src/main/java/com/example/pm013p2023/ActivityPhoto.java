package com.example.pm013p2023;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.pm013p2023.configuracion.SQLiteConexion;
import com.example.pm013p2023.configuracion.Transacciones;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityPhoto extends AppCompatActivity {

    static final int peticion_acceso_camara = 101;
    static final int peticion_toma_fotografica = 102;
    String currentPhotoPath;
    ImageView imageView;
    Button btntakefoto;
    String pathfoto;
    EditText nombre, telefono, nota;
    Button btn_salvar,btn_vercontactos;
    Spinner spinner2;
    String seleccion ;
    String group;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        nombre = (EditText) findViewById(R.id.txtnombre);
        telefono = (EditText)findViewById(R.id.txttelefono);
        nota = (EditText)findViewById(R.id.txtnota);
        spinner2 = findViewById(R.id.spinner2);

        String[] paises = {"Honduras(+504)", "Panama (+507)", "Costa Rica (+506)", "Salvador (+503)", "Guatemala (+502)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paises);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);



        btn_salvar = (Button) findViewById(R.id.btnsalvar);
        btn_vercontactos = (Button) findViewById(R.id.btnlista);

        imageView = (ImageView)findViewById(R.id.imageView);
        btntakefoto = (Button) findViewById(R.id.btntakefoto);



        btntakefoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String validar_nombre = nombre.getText().toString();
                String validar_telefono = telefono.getText().toString();
                String validar_nota = nota.getText().toString();
                if(validar_nombre.isEmpty() || validar_telefono.isEmpty() || imageView == null || spinner2 == null || validar_nota.isEmpty()){
                    showAlertDialog();
                } else{
                AddPerson();}
            }
        });


        btn_vercontactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(ActivityPhoto.this, ActivityList.class);

                // Iniciar la nueva actividad
                startActivity(intent);
            }
        });

        View.OnClickListener butonclick;

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Aquí puedes obtener el elemento seleccionado
                seleccion = spinner2.getSelectedItem().toString();
                String validar_nombre;
                String regex = "\\+(\\d{3})";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(seleccion);
                if (matcher.find()) {
                    // Get the matched group
                    group = matcher.group(1);
                    // Print the matched group
                    System.out.println(group);
                }

                telefono.setText(group);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        }


    private void permisos()
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},peticion_acceso_camara);
        }
        else
        {
            //TomarFoto();
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == peticion_acceso_camara)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //TomarFoto();
                dispatchTakePictureIntent();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Permiso denegado", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void TomarFoto()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!= null)
        {
            startActivityForResult(intent, peticion_toma_fotografica);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.pm013p2023.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, peticion_toma_fotografica);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == peticion_toma_fotografica && resultCode == RESULT_OK)
        {

          /*  Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            imageView.setImageBitmap(image);
             */

            try
            {
                File foto = new File(currentPhotoPath);
                imageView.setImageURI(Uri.fromFile(foto));
            }
            catch (Exception ex)
            {
                ex.toString();
            }
        }

    }


    @SuppressLint("ResourceType")
    private void AddPerson()

    {
        try {
            Integer ttelefono = Integer.valueOf(telefono.getText().toString());
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.namedb, null,1);
            SQLiteDatabase db =  conexion.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put(Transacciones.nombre, nombre.getText().toString());
            valores.put(Transacciones.telefono, telefono.getText().toString());
            valores.put(Transacciones.nota, nota.getText().toString());

            Long Result = db.insert(Transacciones.Tabla, Transacciones.id, valores);
            Toast.makeText(this, getString(R.id.txttelefono), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, getString(R.string.Respuesta), Toast.LENGTH_SHORT).show();
            db.close();
        }
        catch (Exception exception)
        {
            Toast.makeText(this, getString(R.string.ErrorResp), Toast.LENGTH_SHORT).show();
        }

    }

    private void NoveActivity(Class<?> actividad)
    {
        Intent intent = new Intent(getApplicationContext(),actividad);
        startActivity(intent);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Diálogo de Alerta");
        builder.setMessage("Este es un ejemplo de un diálogo de alerta en Android.");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Acción a realizar cuando se hace clic en el botón "Aceptar"
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Acción a realizar cuando se hace clic en el botón "Cancelar"
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


