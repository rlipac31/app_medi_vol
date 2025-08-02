package com.example.medivol_1
import ApiClient
import android.app.AlertDialog // Importar AlertDialog
import android.content.DialogInterface // Importar DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medivol_1.formularios.EditMedicoActivity
import com.example.medivol_1.formularios.RegistroMedicoActivity
import com.example.medivol_1.model.medico.Medico
import com.example.medivol_1.service.MedicoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale


class MedicoActivity : AppCompatActivity() {
    private lateinit var medicoService: MedicoService
    private lateinit var medicoAdapter: MedicoAdapter
    private var medicosList: MutableList<Medico> = mutableListOf() // Lista mutable para los datos
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medico)
        // Configurar la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbarMedico)
        setSupportActionBar(toolbar) // Necesario para usar la toolbar como ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Habilita el botón de retroceso
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Vuelve a la actividad anterior
        }

        // Obtener referencias de los botones del menú superior

        val fabAddMedico: Button = findViewById(R.id.fabAddMedico)
        // El FAB de la parte inferior

        // Configurar RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewMedicos)
        recyclerView.layoutManager = LinearLayoutManager(this)
      //  medicoAdapter = MedicoAdapter(medicosList)

        medicoAdapter = MedicoAdapter(
            medicos = medicosList,
            onEditClick = { medico ->
                // Lógica para editar un médico específico

                Toast.makeText(this, "Editar Médico: ${medico.nombre} ", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, EditMedicoActivity::class.java).apply {
                   // putExtra("idMedico", idMedico) // Pasa el ID del médico
                    putExtra("medico_object", medico) // <-- ¡Clave importante!
                    Log.d("PasandoMedico", "Médico : ${medico}")
                }
                startActivity(intent)
            },

            onDeleteClick = { medico ->

                // *** ¡LLAMAMOS AL DIÁLOGO DE CONFIRMACIÓN AQUÍ! ***
                showDeleteConfirmationDialog(medico)

            }
        )
        recyclerView.adapter = medicoAdapter

        // Configurar listener para el FAB inferior
        fabAddMedico.setOnClickListener {
            Toast.makeText(this, "Registrar Nuevo Medico (FAB)", Toast.LENGTH_SHORT).show()
            // Ejemplo de navegación
            // Este es probablemente el que usaremos para agregar un médico.
            // Aquí podrías iniciar una nueva Activity para el formulario de registro.
            startActivity(Intent(this, RegistroMedicoActivity::class.java))
        }


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

    // Dentro de tu MedicoActivity:
    private fun showDeleteConfirmationDialog(medicoToDelete: Medico) {
        val builder = AlertDialog.Builder(this)

        // Inflar el layout personalizado
        val customLayout = layoutInflater.inflate(R.layout.dialog_delete_medico, null)
        builder.setView(customLayout)

        // --- 1. Referenciar y cargar datos del médico en los TextViews del layout personalizado ---
        val titleTextView = customLayout.findViewById<TextView>(R.id.dialog_title)
        val nombreTextView = customLayout.findViewById<TextView>(R.id.dialog_nombre)
        val especialidadTextView = customLayout.findViewById<TextView>(R.id.dialog_especialidad)
        val phoneTextView = customLayout.findViewById<TextView>(R.id.dialog_phone)
        val documentoTextView = customLayout.findViewById<TextView>(R.id.dialog_documento)
        val messageTextView = customLayout.findViewById<TextView>(R.id.dialog_mensage)
        val messageTextView2 = customLayout.findViewById<TextView>(R.id.dialog_mensage2)

        // Asignar los valores del médico a los TextViews
        titleTextView.text = "Desea desactivar este perfil" // Título fijo según tu XML
        nombreTextView.text = medicoToDelete.nombre
        especialidadTextView.text = medicoToDelete.especialidad // Aquí pusiste especialidad, pero en el XML dice "42261160"
        phoneTextView.text = medicoToDelete.telefono
        documentoTextView.text = medicoToDelete.documento
        messageTextView.text = "Al desactivar este perfil, sus informaciones quedaran inactivas para nuevas consultas." // Mensaje fijo
        messageTextView2.text = "Certifique que no hay consultas agendadas, caso tenga, la desactivacion no podra ser concluída." // Mensaje fijo

        // --- 2. Referenciar y configurar los botones del layout personalizado ---
        val deactivateButton = customLayout.findViewById<Button>(R.id.btn_desactivar_perfil)
        val cancelButton = customLayout.findViewById<Button>(R.id.btn_cancelar_delete_perfil)
// *** DECLARA EL DIALOG ANTES DE ASIGNAR LOS LISTENERS ***
        val dialog = builder.create() // Aho
        deactivateButton.setOnClickListener {
            medicoToDelete.id?.let { id ->
                deleteMedicoFromService(id) // Llama a tu función para eliminar el médico
            } ?: run {
                Toast.makeText(this, "Error: ID del médico no disponible.", Toast.LENGTH_SHORT).show()
            }
            // Importante: Cierra el diálogo después de la acción
            (dialog as? AlertDialog)?.dismiss() // Asegúrate de que 'dialog' sea el AlertDialog instanciado
        }

        cancelButton.setOnClickListener {
            // Simplemente cierra el diálogo
            Toast.makeText(this, "Desactivación cancelada.", Toast.LENGTH_SHORT).show()
            (dialog as? AlertDialog)?.dismiss() // Asegúrate de que 'dialog' sea el AlertDialog instanciado
        }

        // --- ¡IMPORTANTE! NO uses builder.setPositiveButton / setNegativeButton
        //     si ya estás manejando los botones en tu layout personalizado.
        //     Comenta o elimina estas líneas:
        /*
        builder.setPositiveButton("Eliminar") { dialog: DialogInterface, which: Int ->
            // ... (este código ahora va en el setOnClickListener del btn_desactivar_perfil)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog: DialogInterface, which: Int ->
            // ... (este código ahora va en el setOnClickListener del btn_cancelar_delete_perfil)
            dialog.dismiss()
        }
        */



        // Opcional: Para evitar que el diálogo se cierre al tocar fuera
        dialog.setCanceledOnTouchOutside(false)
        // Opcional: Para evitar que se cierre al presionar el botón de atrás
        dialog.setCancelable(false)

        // Mostrar el AlertDialog
        dialog.show()
    }
    // --- FUNCIÓN PARA LLAMAR AL SERVICIO DE ELIMINACIÓN ---
    private fun deleteMedicoFromService(idMedico: Long) {
           // Lógica para desactivar/eliminar un médico específico
        Log.e("DeleteMedico", "idMedico:  ${idMedico}")
            val  servicio = ApiClient.getMedicoService(this)
               servicio.deleteMedico(idMedico).enqueue(object : Callback<Unit> {
                   override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                       if (response.isSuccessful) {
                           // El código 200 significa éxito
                           Toast.makeText(this@MedicoActivity, "Médico eliminado exitosamente", Toast.LENGTH_SHORT).show()
                          getDoctors()


                       } else {
                           // Hubo un error (ej. 404, 400). Puedes obtener el código de error.
                           // No tendrás acceso directo al errorBody si este endpoint realmente no envía nada.
                           Toast.makeText(this@MedicoActivity, "Error al eliminar médico: Código ${response.code()}", Toast.LENGTH_LONG).show()
                           Log.e("DeleteMedico", "Error: Código ${response.code()}")
                       }
                   }

                   override fun onFailure(call: Call<Unit>, t: Throwable) {
                       // Error de red o cualquier otra excepción
                       Toast.makeText(this@MedicoActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                       Log.e("DeleteMedico", "Fallo en la conexión", t)
                   }
               })
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


