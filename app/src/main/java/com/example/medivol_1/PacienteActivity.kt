package com.example.medivol_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medivol_1.model.paciente.Paciente
import com.example.medivol_1.model.paciente.PacienteAdapter
import com.example.medivol_1.service.PacienteService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class PacienteActivity : AppCompatActivity() {
    private lateinit var pacienteService: PacienteService

    private lateinit var pacienteAdapter: PacienteAdapter
    private var pacienteList: MutableList<Paciente> = mutableListOf() // Lista mutable para los datos
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_paciente)


        //
        // Configurar la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbarPaciente)
        setSupportActionBar(toolbar) // Necesario para usar la toolbar como ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Habilita el botón de retroceso
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Vuelve a la actividad anterior
        }

        // Configurar RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewPacientes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        pacienteAdapter = PacienteAdapter(pacienteList)
        recyclerView.adapter = pacienteAdapter



        // Configurar la barra de búsqueda
        searchView = findViewById(R.id.searchMedico)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Se llama cuando el usuario presiona enter o el botón de búsqueda
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Se llama cada vez que el texto cambia en la barra de búsqueda
                filterList(newText.orEmpty()) // Filtra la lista
                return true
            }
        })

        // consumiendo api

        setupRetrofit()
        loadPacientes()



        //
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.paciente)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        pacienteService = retrofit.create(PacienteService::class.java)
    }
    private fun loadPacientes() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val  pagedResponse = pacienteService.getPacientes()
                pacienteList= pagedResponse.content.toMutableList()
              pacienteList.sortBy { it.nombre }
                withContext(Dispatchers.Main){
                   pacienteAdapter.updateList(pacienteList)
                }

                Log.d("MedicoServicio", "Datos recibidos: $pacienteList")
            } catch (e: Exception) {
                Log.e("MedicoServicio", "Error al obtener médicos", e)
                runOnUiThread {
                    Toast.makeText(this@PacienteActivity, "Error al cargar Pacientes", Toast.LENGTH_LONG).show()
                }
            }

        }
    }


    // Método para inflar el menú en la Toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_toolbar, menu) // Infla tu archivo de menú
        return true
    }

    // Método para manejar los clics en los ítems del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_medicos -> {
                Toast.makeText(this, "Navegar a Médicos", Toast.LENGTH_SHORT).show()
                // Ejemplo de navegación
                startActivity(Intent(this, MedicoActivity::class.java))
                true
            }
            R.id.action_pacientes -> {
                Toast.makeText(this, "Navegar a Pacientes", Toast.LENGTH_SHORT).show()
                // Ejemplo de navegación
                 startActivity(Intent(this, PacienteActivity::class.java))
                true
            }
            R.id.action_consultas -> {
                Toast.makeText(this, "Navegar a Consultas", Toast.LENGTH_SHORT).show()
                // Ejemplo de navegación
                 startActivity(Intent(this, ConsultaActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun filterList(query: String) {
        val filteredList = if (query.isEmpty()) {
            pacienteList // Si la búsqueda está vacía, mostrar la lista original
        } else {
            pacienteList.filter {
                it.nombre.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault())) ||
                        it.email.lowercase(Locale.getDefault()).contains(query.lowercase(
                            Locale.getDefault())) ||
                        it.documento_identidad.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }.toMutableList() // Convertir a mutableList si necesitas modificarla después
        }
        pacienteAdapter.updateList(filteredList)
    }
}