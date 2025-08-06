package com.example.medivol_1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medivol_1.controller.medico.MedicoActivity
import com.example.medivol_1.controller.paciente.PacienteActivity

// Define constantes para los destinos
/*const val DESTINO_EXTRA = "destino_extra"
const val DESTINO_MEDICO = "medico"
const val DESTINO_PACIENTE = "paciente"
const val DESTINO_CONSULTA = "consulta"*/

class MenuPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)

        // Encuentra el botón por su ID
        val logoutButton: ImageView = findViewById(R.id.logout)

        // Asigna el OnClickListener
        logoutButton.setOnClickListener {
            Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show()

            // 1. Limpiar el token
            TokenManager.clearToken(this)

            // 2. Crear un nuevo Intent a la LoginActivity
            val intent = Intent(this, LoginActivity::class.java)

            // 3. Establecer los flags para limpiar la pila de actividades
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            // 4. Iniciar la nueva actividad
            startActivity(intent)

            // 5. Opcional, pero buena práctica: Cierra la actividad actual
            finish()

            true
        }

        // Obtener referencias a los CardView
        val cardMedico = findViewById<CardView>(R.id.cardMedico)
        val cardPaciente = findViewById<CardView>(R.id.cardPaciente)
        val cardConsulta = findViewById<CardView>(R.id.cardConsulta)



        // Configurar OnClickListener para cada CardView
        cardMedico.setOnClickListener {
            // Iniciar la actividad de Citas Médicas
            val intent = Intent(this, MedicoActivity::class.java)
            startActivity(intent)
        }

        cardPaciente.setOnClickListener {
            // Iniciar la actividad de Perfil del Paciente
            val intent = Intent(this, PacienteActivity::class.java)
            startActivity(intent)
        }

        cardConsulta.setOnClickListener {
            // Iniciar la actividad de Contacto / Ayuda
            val intent = Intent(this, ConsultaActivity::class.java)
            startActivity(intent)
        }


        // codigo de android estudio
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // fin  codigo de android estudio

    }


}

