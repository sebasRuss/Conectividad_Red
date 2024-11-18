package com.example.conectividad_red

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {
            //creamos el singleton
            //aqui recibiremos un objeto pero lo casteamos para que sea concretamente un objeto tipo ConnectivityManager
            //objetoConectorManager guardara el servicio del sistema que gestiona la conectividad a la red
            val objetoConectorManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            //creamos esta variable para acceder a la informacion de la red y asi poder alamacenarla para
            //luego cotejarla
            val infoRed = objetoConectorManager.activeNetwork

            //usando un condicional podremos saber si la el dispositivo esta conectado ala red
            val situacion= (if (infoRed != null){
                //si hemos entrado en este condicional significa que estamos conectados a una red
                //vamos a ver si es al wifi o a los datos
                //DEBO USAR getNetworkCapabilities(aqui va la info de la red) PORQUE TYPE_WIFE
                //ESTA EN DESHUSO
                //Por lo que este metodo nos va a dar la informacion sobre la red y por lo tanto
                //sabremos si es wifi o red movil
                objetoConectorManager.getNetworkCapabilities(infoRed)?.run{
                    //si entra en este condicinal la conexion es por wifi
                    if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                        estadoConexion.setTextColor(Color.BLUE)
                        estadoConexion.setText("Dispositivo conectado a la red Wifi")
                    }
                    //si entra por este condicional es que el movil esta usando los datos moviles
                    if(hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                        estadoConexion.setTextColor(Color.GREEN)
                        estadoConexion.setText("Dispositivo conectado a los datos")
                    }
                }
            } else {
                estadoConexion.setText("El dispositivo no esta conectado a ninguna red")
            })

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        estadoConexion = findViewById(R.id.textViewEstadoConexion)

        //Hay que REGISTRAR el BroadcastReceiver para que este atento a los cambios
        //solo de conectividad.
        //IntentFilter sirve para espeficar el tipo de eventos a los que tiene que estar
        //pendiendte, en este caso, a los cambios de red.
        //ACTION_MANAGE_NETWORK_USAGE es el intent relacionado con la red en general
        registerReceiver(tipoConexion, IntentFilter(Intent.ACTION_MANAGE_NETWORK_USAGE),
            RECEIVER_NOT_EXPORTED
        )
    }

    /**
     * Funcion sobreescrita que finalizara las notificaciones del sistema
     * en cuanto al estado de red
     */
    override fun onDestroy() {
        super.onDestroy()
        //Tenemos que anular el REGISTRO del BroadcastReceiver
        //Esto se hace para decirl al sistema que ya no necesitamos mas notificaciones
        //sobre el estado de red
        unregisterReceiver(tipoConexion)
    }


}

