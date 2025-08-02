package com.example.medivol_1.formularios

import com.google.gson.reflect.TypeToken
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medivol_1.R
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.medivol_1.ConsultaActivity
import com.example.medivol_1.MedicoActivity
import com.example.medivol_1.PacienteActivity
import com.example.medivol_1.databinding.ActivityRegistroMedicoBinding // Generado automáticamente por ViewBinding
import com.example.medivol_1.data.Departamento
import com.example.medivol_1.data.Distrito
import com.example.medivol_1.data.Provincia
import com.example.medivol_1.model.Direccion
import com.example.medivol_1.model.medico.Especialidad
import com.example.medivol_1.model.medico.RegistroMedicoRequest
import com.example.medivol_1.model.medico.RegistroMedicoResponse
import com.example.medivol_1.utils.AssetLoader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroMedicoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroMedicoBinding

    // Listas completas de todos los datos
    private var allDepartamentos: List<Departamento> = emptyList()
    private var allProvincias: List<Provincia> = emptyList()
    private var allDistritos: List<Distrito> = emptyList()

    // Variable para almacenar el ID del departamento seleccionado
    private var selectedDepartamentoId: String? = null
    // Variable para almacenar el ID de la provincia seleccionada
    private var selectedProvinciaId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    //    setContentView(R.layout.activity_registro_medico)
        //
        binding = ActivityRegistroMedicoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbarMedico)
        setSupportActionBar(toolbar) // Necesario para usar la toolbar como ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Habilita el botón de retroceso
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Vuelve a la actividad anterior
        }

        // Configurar Spinner/Dropdown de Especialidad

        val especialidadesParaSpinner = listOf(
            "ORTOPEDIA",
            "CARDIOLOGIA",
            "DERMATOLOGIA",
            "PEDIATRIA",
            "GINECOLOGIA",
            "MEDICINA_GENERAL"
        )
        // Asegúrate de incluir la especialidad actual "Ortopedista"
      /*  val especialidadAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, especialidades)
        (binding.tilEspecialidad.editText as? AutoCompleteTextView)?.setAdapter(especialidadAdapter)*/

       val  especialidadesParaSpinner2 = Especialidad.values().map { it.nombreAmigable }
        val especialidadAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, especialidadesParaSpinner)
        (binding.tilEspecialidad.editText as? AutoCompleteTextView)?.setAdapter(especialidadAdapter)

        // Pre-seleccionar la especialidad si estás en modo edición
        // Asegúrate de que "Ortopedista" esté en tu lista de 'especialidades'
        binding.atCEspecialidad.setText(
            "ESPECIALIDAD",
            false
        ) // 'false' para no mostrar el dropdown al establecer texto

        // --- Carga inicial de todas las listas desde JSON ---
        loadAllLocationData()

        // --- Configuración de AutoCompleteTextViews para Ubicación ---

        // 1. Departamentos
        val nombresDepartamentos = allDepartamentos.map { it.name }
        val departamentoAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombresDepartamentos)
        (binding.tilEstado.editText as? AutoCompleteTextView)?.setAdapter(departamentoAdapter)

        // Listener para la selección de Departamento
        (binding.tilEstado.editText as? AutoCompleteTextView)?.setOnItemClickListener { parent, view, position, id ->
            val selectedName = parent.getItemAtPosition(position).toString()
            val departamento = allDepartamentos.find { it.name.equals(selectedName, ignoreCase = true) }
            selectedDepartamentoId = departamento?.id // Guarda el ID del departamento seleccionado

            // Limpia y habilita el campo de provincia
            (binding.tilPostal.editText as? AutoCompleteTextView)?.setText("")//provincia
            binding.tilPostal.isEnabled = true
            selectedProvinciaId = null // Restablece el ID de provincia

            // Filtra y actualiza el adaptador de Provincias
            updateProvinciasAdapter()

            // Limpia y deshabilita el campo de distrito
            (binding.tilCiudad.editText as? AutoCompleteTextView)?.setText("")
            binding.tilCiudad.isEnabled = false // Deshabilita hasta que se seleccione una provincia
        }

        // 2. Provincias (inicialmente deshabilitado)
        // El adaptador se actualizará dinámicamente
        binding.tilPostal.isEnabled = false // Deshabilitar inicialmente

        // Listener para la selección de Provincia
        (binding.tilPostal.editText as? AutoCompleteTextView)?.setOnItemClickListener { parent, view, position, id ->
            val selectedName = parent.getItemAtPosition(position).toString()
            val provincia = allProvincias.find { it.name.equals(selectedName, ignoreCase = true) && it.department_id == selectedDepartamentoId }
            selectedProvinciaId = provincia?.id // Guarda el ID de la provincia seleccionada

            // Limpia y habilita el campo de distrito
            (binding.tilCiudad.editText as? AutoCompleteTextView)?.setText("")
            binding.tilCiudad.isEnabled = true

            // Filtra y actualiza el adaptador de Distritos
            updateDistritosAdapter()
        }

        // 3. Distritos (inicialmente deshabilitado)
        // El adaptador se actualizará dinámicamente
        binding.tilCiudad.isEnabled = false // Deshabilitar inicialmente


        //fin de lleyendo json
       // val estados = loadStatesFromJson()
        val estadoAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombresDepartamentos)
        (binding.tilEstado.editText as? AutoCompleteTextView)?.setAdapter(estadoAdapter)


        // Manejar click del botón "Concluir registro"
        binding.btnConcluirRegistro.setOnClickListener {
            if (validarCampos()) {
                enviarDatosRegistro()

            } else {
                Toast.makeText(
                    this,
                    "Por favor, completa todos los campos obligatorios.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Manejar click del botón "Cancelar"
        binding.btnCancelar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Vuelve a la actividad anterior
        }
    }

    private fun validarCampos(): Boolean {
        var isValid = true

        /*  // Validar Nombre completo
        if (binding.etNombreMedico.text.isNullOrBlank()) {
            binding.tvObligatorioNombre.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvObligatorioNombre.visibility = View.GONE
        }*/

        // Validar Especialidad
        /* if (binding.actvEspecialidad.text.isNullOrBlank()) {
            binding.tvObligatorioEspecialidad.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvObligatorioEspecialidad.visibility = View.GONE
        }*/

        // Validar Documento
        /*  if (binding.etDocumento.text.isNullOrBlank()) {
            binding.tvObligatorioDocumento.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvObligatorioDocumento.visibility = View.GONE
        }*/

        // Validar Email
        if (binding.etEmail.text.isNullOrBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(
                binding.etEmail.text.toString()
            ).matches()
        ) {
            binding.tvObligatorioEmail.visibility = View.VISIBLE
            binding.tvObligatorioEmail.text =
                "Email inválido o campo obligatorio" // Mensaje específico para email
            isValid = false
        } else {
            binding.tvObligatorioEmail.visibility = View.GONE
        }

        // Validar Teléfono
        if (binding.etTelefono.text.isNullOrBlank()) {
            binding.tvObligatorioTelefono.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvObligatorioTelefono.visibility = View.GONE
        }

        // Validar Calle
        if (binding.etCalle.text.isNullOrBlank()) {
            binding.tvObligatorioCalle.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvObligatorioCalle.visibility = View.GONE
        }

        // Validar Número
        if (binding.etNumero.text.isNullOrBlank()) {
            binding.tvObligatorioNumero.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvObligatorioNumero.visibility = View.GONE
        }

        // Validar Ciudad
        if (binding.etCiudad.text.isNullOrBlank()) {
            binding.tvObligatorioCiudad.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvObligatorioCiudad.visibility = View.GONE
        }

        // Validar Estado
        if (binding.actvEstado.text.isNullOrBlank()) {
            binding.tvObligatorioEstado.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvObligatorioEstado.visibility = View.GONE
        }

        // Validar Postal
        if (binding.etPostal.text.isNullOrBlank()) {
            binding.tvObligatorioPostal.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvObligatorioPostal.visibility = View.GONE
        }

        return isValid


///verificar codigo eantes de descomentar
    // Puedes agregar botones de "Guardar Cambios" o "Actualizar" aquí si no están en la imagen visible
    // Por ejemplo:
  /*   binding.btnGuardarCambios.setOnClickListener {
         // Lógica para guardar los cambios en la API
         Toast.makeText(this, "Guardando cambios...", Toast.LENGTH_SHORT).show()
     }*/


    //
        // codigo de android estudio
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.RegistroMedico)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // fin  codigo de android estudi
}

    private fun loadAllLocationData() {
        allDepartamentos = AssetLoader.loadListFromAsset(
            this,
            "departamentos_peru.json", // O "estados_peru.json" si así lo llamas
            object : TypeToken<List<Departamento>>() {}
        )
        Log.d("RegistroMedico", "Departamentos cargados: ${allDepartamentos.size}")

        allProvincias = AssetLoader.loadListFromAsset(
            this,
            "provincias_peru.json",
            object : TypeToken<List<Provincia>>() {}
        )
        Log.d("RegistroMedico", "Provincias cargadas: ${allProvincias.size}")

        allDistritos = AssetLoader.loadListFromAsset(
            this,
            "distritos_peru.json",
            object : TypeToken<List<Distrito>>() {}
        )
        Log.d("RegistroMedico", "Distritos cargados: ${allDistritos.size}")
    }

    private fun updateProvinciasAdapter() {
        val filteredProvincias = allProvincias.filter { it.department_id == selectedDepartamentoId }
        val nombresProvincias = filteredProvincias.map { it.name }
        val provinciaAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombresProvincias)
        (binding.tilPostal.editText as? AutoCompleteTextView)?.setAdapter(provinciaAdapter)
    }

    private fun updateDistritosAdapter() {
        val filteredDistritos = allDistritos.filter {
            it.department_id == selectedDepartamentoId && it.province_id == selectedProvinciaId
        }
        val nombresDistritos = filteredDistritos.map { it.name }
        val distritoAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombresDistritos)
        (binding.tilCiudad.editText as? AutoCompleteTextView)?.setAdapter(distritoAdapter)
    }


    private fun enviarDatosRegistro() {
        Toast.makeText(
            this,
            "Envienado resgistro.....",
            Toast.LENGTH_SHORT
        ).show()

        // Aquí obtendrías los datos de todos los campos
        val nombre = binding.etNombreMedico.text.toString()
        val email = binding.etEmail.text.toString()
        val especialidadSeleccionadaTexto = binding.atCEspecialidad.text.toString()
       // val especialidadEnum = Especialidad.values().firstOrNull { it.nombreAmigable == especialidadSeleccionadaTexto }

        if (especialidadSeleccionadaTexto == null) {
            Toast.makeText(this, "Por favor, selecciona una especialidad válida tu selecion: $especialidadSeleccionadaTexto", Toast.LENGTH_SHORT).show()
            return
        }
        val documento = binding.etDocumentoMedico.text.toString()
        val telefono = binding.etTelefono.text.toString()
        val calle = binding.etCalle.text.toString()
        val numero = binding.etNumero.text.toString()
        val complemento = binding.etComplemento.text.toString() // Opcional
        val distritoNombre = binding.etCiudad.text.toString()
        val departamentoNombre = binding.actvEstado.text.toString()
        val provinciaNombre = binding.etPostal.text.toString()
      //  val departamentoNombre = binding.atCDepartamento.text.toString()



        // Opcional: Si necesitas los IDs, puedes buscarlos de nuevo aquí
        val departamentoId = allDepartamentos.find { it.name.equals(departamentoNombre, ignoreCase = true) }?.id
        val provinciaId = allProvincias.find { it.name.equals(provinciaNombre, ignoreCase = true) && it.department_id == departamentoId }?.id
        val distritoId = allDistritos.find { it.name.equals(distritoNombre, ignoreCase = true) && it.province_id == provinciaId }?.id


        //creando objeto a guardar
        // Ahora, agregando direccion al crear tu request:
        val direccion = Direccion(
            calle = calle,
            numero = numero,
            complemento = complemento,
            distrito = distritoNombre,
            provincia = provinciaNombre,
            departamento = departamentoNombre,
            codigo_postal = "" // <-- AÑADE EL CAMPO codigo_postal
        )
        val registroMedicoRequest = RegistroMedicoRequest(
            nombre,
            email,
            especialidadSeleccionadaTexto, // <-- Aquí va el enum
            telefono,
            documento,
            direccion
        )

        // Luego harías la llamada a tu API con Retrofit:
        val service = ApiClient.getMedicoService(this)

        service.getRegistroMedico(registroMedicoRequest).enqueue(object : Callback<RegistroMedicoResponse> {
            override fun onResponse(call: Call<RegistroMedicoResponse>, response: Response<RegistroMedicoResponse>) {
                if (response.isSuccessful) {
                    val nuevoMedico = response.body()
                    Log.d("RegistroMedico", "El médico: ${nuevoMedico?.nombre} se ha creado correctamente. ID: ${nuevoMedico?.id}")
                    Toast.makeText(this@RegistroMedicoActivity, "Médico registrado exitosamente", Toast.LENGTH_LONG).show()
                    // Inicia MedicoActivity
                    val intent = Intent(this@RegistroMedicoActivity, MedicoActivity::class.java)
                    // Opcional: Si quieres limpiar el back stack para que el usuario no pueda volver a
                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                    finish() // Cierra la actividad actual
                } else {
                    // Manejar errores de la API (ej. código 400, 401, 500)
                    val errorBody = response.errorBody()?.string()
                    Log.e("RegistroMedico", "Error al registrar médico: ${response.code()} - $errorBody")
                    Toast.makeText(this@RegistroMedicoActivity, "Error al registrar médico: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RegistroMedicoResponse>, t: Throwable) {
                // Manejar errores de conexión o red
                Log.e("RegistroMedico", "Fallo en la conexión: ${t.message}", t)
                Toast.makeText(this@RegistroMedicoActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

      //  Toast.makeText(this, "Datos listos para enviar (simulado)", Toast.LENGTH_LONG).show()
        // Después de enviar, podrías cerrar la actividad o mostrar un mensaje de éxito
        // finish()
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
}







