package com.example.medivol_1 // Asegúrate de que este sea el nombre correcto de tu paquete

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {



    // Define la duración del splash screen en milisegundos (ej: 3000 ms = 3 segundos)
    private val SPLASH_SCREEN_DELAY: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Esta línea enlaza el XML con esta Activity

        // No necesitas código Kotlin adicional para esta pantalla si solo mostrará el logo
        // y esperará un tiempo (por ejemplo, para un SplashScreen).
        // Si quieres que después de unos segundos se vaya a otra pantalla,
        // podrías añadir un Handler o un Coroutine aquí.


        // Usamos Handler para ejecutar código después de un retraso
        Handler(Looper.getMainLooper()).postDelayed({
            // Crea un Intent para iniciar MenuPrincipalActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // Finaliza MainActivity para que el usuario no pueda volver a ella con el botón "atrás"
            finish()
        }, SPLASH_SCREEN_DELAY) // El retraso en milisegundos
    }
}