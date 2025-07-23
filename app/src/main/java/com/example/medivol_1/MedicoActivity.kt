package com.example.medivol_1
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar // Asegúrate de importar Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.medivol_1.service.MedicoService
import com.example.medivol_1.model.medico.Medico


class MedicoActivity : AppCompatActivity() {
    private lateinit var medicoService: MedicoService

    private lateinit var medicoAdapter: MedicoAdapter
    private var medicosList: MutableList<Medico> = mutableListOf() // Lista mutable para los datos
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medico)
        // llamando a setup retrofit


        //
        // Configurar la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbarMedico)
        setSupportActionBar(toolbar) // Necesario para usar la toolbar como ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Habilita el botón de retroceso
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Vuelve a la actividad anterior
        }


        // Obtener referencias de los botones del menú superior
     //   val btnAddMedico: Button = findViewById(R.id.btnAddMedico)
      //  val btnEditMedico: Button = findViewById(R.id.btnEditMedico)
      //  val btnDeleteMedico: Button = findViewById(R.id.btnDeleteMedico)
      //  val fabAddMedico: Button = findViewById(R.id.fabAddMedico) // El FAB de la parte inferior

        // Configurar RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewMedicos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        medicoAdapter = MedicoAdapter(medicosList)
        recyclerView.adapter = medicoAdapter

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

        // Cargar datos de ejemplo (esto vendría de tu API REST real)
        //loadSampleMedicos()
        setupRetrofit()
        getDoctors()
        // codigo de android estudio
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.medico)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // fin  codigo de android estudio
    }

  private fun  setupRetrofit(){
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
      medicoService = retrofit.create(MedicoService::class.java)
    }
    private fun getDoctors() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
               val  pagedResponse = medicoService.getMedicos()
               medicosList = pagedResponse.content.toMutableList()
                medicosList.sortBy { it.nombre }
                    withContext(Dispatchers.Main){
                        medicoAdapter.updateList(medicosList)
                    }
              /*  // Configurar el listener del adaptador para acciones en el ítem (opcional)
                medicoAdapter.setOnItemClickListener { medico ->
                    Toast.makeText(this, "Click en: ${medico.nombre}", Toast.LENGTH_SHORT).show()
                    // Aquí puedes decidir qué hacer cuando se expande/contrae un médico
                }*/
                Log.d("MedicoServicio", "Datos recibidos: $medicosList")
            } catch (e: Exception) {
                Log.e("MedicoServicio", "Error al obtener médicos", e)
                runOnUiThread {
                    Toast.makeText(this@MedicoActivity, "Error al cargar médicos", Toast.LENGTH_LONG).show()
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
   /* private fun loadSampleMedicos() {
        medicosList.add(Medico(1, "Adriano Moreira Sales", "Ginecologista", "15.879-SP", "adriano@med.com", "(11) 1234-5678", "Rua A, 123 - SP"))
        medicosList.add(Medico(2, "Amanda Siqueira", "Oftalmologista", "65.789-SP", "amanda@med.com", "(21) 9876-5432", "Av B, 456 - RJ"))
        medicosList.add(Medico(3, "Antônio Santana", "Clínica General", "37.124-SP", "antonio@med.com", "(31) 2345-6789", "Praça C, 789 - MG"))
        medicosList.add(Medico(4, "Barbara Aparecida", "Pediatra", "15.879-SP", "barbara@med.com", "(41) 3456-7890", "Rua D, 101 - PR", activo = false)) // Ejemplo de inactivo
        medicosList.add(Medico(5, "Bernardo Oliveira", "Pediatra", "15.879-SP", "bernardo@med.com", "(51) 4567-8901", "Av E, 202 - RS"))
        medicosList.add(Medico(6, "Brenda de Almeida", "Ortopedista", "47.889-PR", "brenda.almeida@med.com.br", "(51) 99999-8888", "Av. Altas Gracias, 633 -Miramar/PR\nPostal: 66.777-100"))
        medicosList.add(Medico(7, "Bruno de Souza", "Oftalmologista", "95.612-MG", "bruno@med.com", "(61) 5678-9012", "Rua F, 303 - DF"))
        medicosList.add(Medico(1, "Pedro Moreira Sales", "Ginecologista", "15.879-SP", "adriano@med.com", "(11) 1234-5678", "Rua A, 123 - SP"))
        medicosList.add(Medico(2, "Javier Ziqueira", "Oftalmologista", "65.789-SP", "amanda@med.com", "(21) 9876-5432", "Av B, 456 - RJ"))
        medicosList.add(Medico(3, "Izmael Cantana", "Clínica General", "37.124-SP", "antonio@med.com", "(31) 2345-6789", "Praça C, 789 - MG"))
        medicosList.add(Medico(4, "Mateto Lopez", "Pediatra", "15.879-SP", "barbara@med.com", "(41) 3456-7890", "Rua D, 101 - PR", activo = false)) // Ejemplo de inactivo
        medicosList.add(Medico(5, "Bernardo Oliveira", "Pediatra", "15.879-SP", "bernardo@med.com", "(51) 4567-8901", "Av E, 202 - RS"))
        medicosList.add(Medico(6, "Hipolito de Almeida", "Ortopedista", "47.889-PR", "brenda.almeida@med.com.br", "(51) 99999-8888", "Av. Altas Gracias, 633 -Miramar/PR\nPostal: 66.777-100"))
        medicosList.add(Medico(7, "Ximena Hoyos", "Oftalmologista", "95.612-MG", "bruno@med.com", "(61) 5678-9012", "Rua F, 303 - DF"))
        // Agrega más datos de ejemplo aquí

        medicosList.sortBy { it.nombre } // Ordena la lista por nombre al cargarla
        medicoAdapter.updateList(medicosList)

        // Configurar el listener del adaptador para acciones en el ítem (opcional)
        medicoAdapter.setOnItemClickListener { medico ->
            Toast.makeText(this, "Click en: ${medico.nombre}", Toast.LENGTH_SHORT).show()
            // Aquí puedes decidir qué hacer cuando se expande/contrae un médico
        }
    }*/

    private fun filterList(query: String) {
        val filteredList = if (query.isEmpty()) {
            medicosList // Si la búsqueda está vacía, mostrar la lista original
        } else {
            medicosList.filter {
                it.nombre.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault())) ||
                        it.especialidad.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault())) ||
                        it.documento.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }.toMutableList() // Convertir a mutableList si necesitas modificarla después
        }
        medicoAdapter.updateList(filteredList)
    }


}


