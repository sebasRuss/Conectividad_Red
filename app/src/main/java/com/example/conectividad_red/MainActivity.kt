package com.example.conectividad_red

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    //TextView que contendra la informacion de la bateria
    private lateinit var estadoConexion: TextView

    //Creamos el broadcast
    private val tipoConexion = object : BroadcastReceiver() {
        /**
         * Le pasaremos el contexto y el intent para transmitir la informacion
         */
        override fun onReceive(context: Context, intent: Intent) {
            //en esta variable vamos a guarda un numero que correspondera al tipo de conexion en el momento
            val estadoConexion : Int = intent.getIntExtra ("estado", -1)

            //aqui tengo que recibir ese numero correspondiente a la conexion para introducirlo dentro
            //de un switch y asignarle el tipo de conexion
            //"Conectado a Wi-Fi", "Conectado a Datos Móviles", "Sin Conexión"
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}