package com.example.medivol_1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

