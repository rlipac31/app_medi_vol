package com.example.medivol_1



import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar // Asegúrate de importar Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.medivol_1.controller.medico.MedicoActivity
import com.example.medivol_1.controller.paciente.PacienteActivity
import com.example.medivol_1.model.consulta.Consulta
import com.example.medivol_1.model.consulta.ConsultaAdapter
import com.example.medivol_1.model.consulta.PageConsultaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ConsultaActivity : AppCompatActivity() {
   // private lateinit var consultaService: ConsultaService

    private lateinit var consultaAdapter: ConsultaAdapter
    private var consultasList: MutableList<Consulta> = mutableListOf() // Lista mutable para los datos
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulta)
        // llamando a setup retrofit


        //
        // Configurar la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbarConsulta)
        setSupportActionBar(toolbar) // Necesario para usar la toolbar como ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Habilita el botón de retroceso
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Vuelve a la actividad anterior
        }



        // Configurar RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewMedicos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        consultaAdapter = ConsultaAdapter(consultasList)
        recyclerView.adapter = consultaAdapter

        /*   // Configurar listeners para los botones de CRUD superior
           btnAddMedico.setOnClickListener {
               Toast.makeText(this, "Agregar Médico (superior)", Toast.LENGTH_SHORT).show()
               // Aquí iría la lógica para agregar un nuevo médico (ej. abrir un formulario)
           }
           btnEditMedico.setOnClickListener {
               Toast.makeText(this, "Editar Médico (superior)", Toast.LENGTH_SHORT).show()
               // Aquí iría la lógica para editar un médico (ej. abrir un formulario de edición)
           }
           btnDeleteMedico.setOnClickListener {
               Toast.makeText(this, "Eliminar Médico (superior)", Toast.LENGTH_SHORT).show()
               // Aquí iría la lógica para eliminar médico(s)
           }*/

        // Configurar listener para el FAB inferior
        /* fabAddMedico.setOnClickListener {
             Toast.makeText(this, "Registrar Nuevo Perfil (FAB)", Toast.LENGTH_SHORT).show()
             // Este es probablemente el que usaremos para agregar un médico.
             // Aquí podrías iniciar una nueva Activity para el formulario de registro.
         }*/


        // Configurar la barra de búsqueda
        searchView = findViewById(R.id.searchConsulta)
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

        val  servicio = ApiClient.getConsultaService(this)// llando al servicio
        servicio.getConsultas().enqueue(object : Callback<PageConsultaResponse> {
            override fun onResponse(
                call: Call<PageConsultaResponse>,
                response: Response<PageConsultaResponse>
            ) {
                if (response.isSuccessful) {
                    val pageResponse = response.body()
                    consultasList = pageResponse?.content?.toMutableList() ?: mutableListOf()
                    consultasList.sortBy { it.fecha }
                    consultaAdapter.updateList(consultasList)
                    Log.d("ConsultaServicio", "Datos recibidos: $consultasList")
                } else {
                    Toast.makeText(this@ConsultaActivity, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<PageConsultaResponse>, t: Throwable) {
                Toast.makeText(this@ConsultaActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("ConsultaServicio", "Error en la llamada", t)
            }
        })


        // codigo de android estudio
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.consulta)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // fin  codigo de android estudio


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
            R.id.action_Logout -> {
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
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun filterList(query: String) {
        val filteredList = if (query.isEmpty()) {
            consultasList // Si la búsqueda está vacía, mostrar la lista original
        } else {
            consultasList.filter {
                it.nombre.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault())) ||
                        it.nombre_paciente.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault())) ||
                        it.especialidad.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault())) ||
                        it.documento_medico.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault())) ||
                        it.documento_paciente.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }.toMutableList() // Convertir a mutableList si necesitas modificarla después
        }
        consultaAdapter.updateList(filteredList)
    }


}




