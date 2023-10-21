package com.example.pm013p2023;

import static com.example.pm013p2023.R.id.btn_compartir;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pm013p2023.Models.Personas;
import com.example.pm013p2023.configuracion.SQLiteConexion;
import com.example.pm013p2023.configuracion.Transacciones;

import java.util.ArrayList;

public class ActivityList extends AppCompatActivity {
    SQLiteConexion conexion;
    ListView listView;
    ArrayList<Personas> listcontactos;

    ArrayList<String> ArregloPersonas;
    String Itemcontacto;
    String nombre;
    Button btn_eliminar,btn_actualizar,btn_compartir;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        btn_eliminar = (Button) findViewById(R.id.btn_eliminar);
        btn_actualizar = (Button) findViewById(R.id.btn_actualizar);
        btn_compartir = (Button) findViewById(R.id.btn_compartir);
        try {
            // Establecemos una conxion a base de datos
            conexion = new SQLiteConexion(this, Transacciones.namedb, null, 1);
            listView = (ListView) findViewById(R.id.listpersonas);
            GetContactos();

            ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ArregloPersonas);
            listView.setAdapter(adp);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Itemcontacto = listcontactos.get(i).getNombre();
                    Toast.makeText(ActivityList.this, "Nombre" + Itemcontacto, Toast.LENGTH_LONG).show();



                }
            });


        } catch (Exception ex) {
            ex.toString();
        }
        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                eliminarPersona();
            }
        });
        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetContactos();

            }
        });
        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compartirContacto();
            }
        });
    }

    private void GetContactos() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Personas contacts = null;
        listcontactos = new ArrayList<Personas>();

        Cursor cursor = db.rawQuery(Transacciones.SelectTableContactos, null);
        while (cursor.moveToNext()) {
            contacts = new Personas();
            contacts.setId(cursor.getInt(0));
            contacts.setNombre(cursor.getString(1));
            contacts.setTelefono(cursor.getInt(2));
            contacts.setNota(cursor.getString(3));

            listcontactos.add(contacts);
        }

        cursor.close();
        FillList();
    }

    private void FillList() {
        ArregloPersonas = new ArrayList<String>();

        for (int i = 0; i < listcontactos.size(); i++) {
            ArregloPersonas.add(listcontactos.get(i).getId() + " - " +
                    listcontactos.get(i).getNombre() + " - " +
                    listcontactos.get(i).getTelefono());
        }
    }



    private void eliminarPersona() {
        try {
            // Establecer una conexión con la base de datos SQLite
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.namedb, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();

            // Definir un criterio para eliminar un registro, por ejemplo, basado en un nombre
            String whereClause = Transacciones.nombre + " = ?";
            String[] whereArgs = new String[]{Itemcontacto};

            // Eliminar el registro que cumple con el criterio
            int filasEliminadas = db.delete(Transacciones.Tabla, whereClause, whereArgs);

            if (filasEliminadas > 0) {
                // Mostrar un mensaje de éxito si se eliminaron registros
                Toast.makeText(this, getString(R.string.Respuesta_eliminar), Toast.LENGTH_SHORT).show();
            }

            // Cerrar la conexión con la base de datos
            db.close();
        } catch (Exception exception) {
            // Manejar cualquier excepción que ocurra y mostrar un mensaje de error
            Toast.makeText(this, getString(R.string.ErrorResp_eliminar), Toast.LENGTH_SHORT).show();
        }
    }
    private void compartirContacto() {
        try {
            // Crear un Intent para compartir
            Intent intent = new Intent(Intent.ACTION_SEND);

            // Especificar el tipo de contenido que deseas compartir
            intent.setType("text/plain");

            // Agregar el contenido que deseas compartir (en este caso, el nombre del contacto)

            intent.putExtra(Intent.EXTRA_TEXT, Itemcontacto);

            // Iniciar la actividad de compartir
            startActivity(Intent.createChooser(intent, "Compartir contacto"));

        } catch (Exception exception) {
            // Manejar cualquier excepción que ocurra y mostrar un mensaje de error
            Toast.makeText(this, getString(R.string.ErrorResp), Toast.LENGTH_SHORT).show();
        }
    }

}


