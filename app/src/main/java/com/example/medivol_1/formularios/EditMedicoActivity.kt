package com.example.medivol_1.formularios

import ApiClient
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medivol_1.MedicoActivity
import com.example.medivol_1.PacienteActivity
import com.example.medivol_1.R
import com.example.medivol_1.data.Departamento
import com.example.medivol_1.data.Distrito
import com.example.medivol_1.data.Provincia
import com.example.medivol_1.databinding.ActivityEditMedicoBinding
import com.example.medivol_1.databinding.ActivityRegistroMedicoBinding
import com.example.medivol_1.model.Direccion
import com.example.medivol_1.model.medico.Especialidad
import com.example.medivol_1.model.medico.Medico
import com.example.medivol_1.model.medico.MedicoUpdateRequest
import com.example.medivol_1.service.MedicoService
import com.example.medivol_1.utils.AssetLoader
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// En EditMedicoActivity.kt
class EditMedicoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditMedicoBinding
    private  lateinit var medicoService: MedicoService
    private var medicoToEdit: Medico? = null
    private  var idMedico: Long? = null
    // private lateinit var medicoToEdit: Medico // Si pasaste el objeto completo

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
        binding = ActivityEditMedicoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // --- CORRECTO: Recupera el OBJETO Medico completo ---
        medicoToEdit = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Para Android 13 (API 33) y superior
            intent.getParcelableExtra("medico_object", Medico::class.java)
        } else {
            // Para versiones anteriores de Android
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("medico_object")
        }
        Log.d("MedicoEncontrado", "Médico ID: ${medicoToEdit}")
        idMedico = medicoToEdit?.id

        // --- Verifica si el objeto se recuperó correctamente ---
        if (medicoToEdit == null) {
            Toast.makeText(this, "Error: No se pudo cargar el médico para editar.", Toast.LENGTH_LONG).show()
            finish() // Cierra la actividad si no hay datos
            return // Salir de onCreate
        }

        // O si pasaste el objeto Medico completo (si es Parcelable/Serializable):
        // medicoToEdit = intent.getParcelableExtra("medicoObjeto") ?: throw IllegalArgumentException("Médico no encontrado")


// --- Verifica si el objeto se recuperó correctamente ---
        if (medicoToEdit == null) {
            Toast.makeText(this, "Error: No se pudo cargar el médico para editar.", Toast.LENGTH_LONG).show()
            finish() // Cierra la actividad si no hay datos
            return // Salir de onCreate
        }



        val actualizarMedico: Button = binding.btnConcluirEdit

        medicoToEdit?.let{  medicoEncontrado ->
            Log.d("EditMedico", "Médico ID: ${medicoEncontrado.id}")
            // Usa binding para acceder a las vistas
            binding.etNombreMedico.setText(medicoEncontrado.nombre)
            binding.atCEspecialidad.setText(medicoEncontrado.especialidad)
            binding.etTelefono.setText(medicoEncontrado.telefono)
            binding.etDocumentoMedico.setText(medicoEncontrado.documento)
            binding.etEmail.setText(medicoEncontrado.email)
            // Cargando direccion
            binding.etCalle.setText(medicoEncontrado.direccion?.calle)
            binding.etNumero.setText(medicoEncontrado.direccion?.numero)
            // ¡Asegúrate de haber corregido el @SerializedName para 'departamento'!
            binding.etCiudad.setText(medicoEncontrado.direccion?.distrito ?: "")
            binding.actvEstado.setText(medicoEncontrado.direccion?.departamento ?: "")
            binding.etPostal.setText(medicoEncontrado.direccion?.provincia ?: "")
            binding.etComplemento.setText(medicoEncontrado.direccion?.complemento ?: "")
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



        //tolbar de android
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.editMedico)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar listener para el FAB inferior
       actualizarMedico.setOnClickListener {
            Toast.makeText(this, "Actualizando  Medico.... (FAB)", Toast.LENGTH_SHORT).show()
          saveChanges()
        }

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



    // ... en tu EditMedicoActivity o un ViewModel
    private fun saveChanges() {

        // --- Verifica si el objeto se recuperó correctamente ---
        if (medicoToEdit == null) {
            Toast.makeText(this, "Error: No se pudo cargar el médico para editar.", Toast.LENGTH_LONG).show()
            finish() // Cierra la actividad si no hay datos
            return // Salir de onCreate
        }

        // 2. Recolectar los datos actualizados del formulario
        val updatedNombre = binding.etNombreMedico.text.toString()
        val updatedEspecialidad = binding.atCEspecialidad.text.toString()
        val updatedTelefono = binding.etTelefono.text.toString()
        val updatedDocumento = binding.etDocumentoMedico.text.toString()


        // Asume que también recopilas los datos de la dirección de los campos del formulario
        val updatedDireccion = Direccion(
            calle = binding.etCalle.text.toString(),
            numero = binding.etNumero.text.toString(),
            provincia = binding.etPostal.text.toString(),
            departamento = binding.actvEstado.text.toString(),
            codigo_postal = "",
            complemento = binding.etComplemento.text.toString(),
            distrito =binding.etCiudad.text.toString()
        )

        // 3. Crear el objeto MedicoUpdateRequest
        val updateRequest = MedicoUpdateRequest(
            nombre = updatedNombre,
            especialidad = updatedEspecialidad,
            telefono = updatedTelefono,
            documento = updatedDocumento,
            direccion = updatedDireccion
        )
        Log.d("MedicoEncontrado", "Médico ID: ${idMedico}")
        val  servicio = ApiClient.getMedicoService(this)
        idMedico?.let {
            servicio.updateMedico(it, updateRequest).enqueue(object : Callback<Medico> {

                override fun onResponse(
                    call: Call<Medico>,
                    response: Response<Medico>
                ) {
                    if (response.isSuccessful) {
                        val medicoActualizado = response.body()

                        Log.d("ConsultaServicio", "Datos recibidos: $medicoActualizado")
                        Toast.makeText(this@EditMedicoActivity, "Médico actualizado exitosamente", Toast.LENGTH_LONG).show() // Mensaje de éxito

                        // Inicia MedicoActivity
                        val intent = Intent(this@EditMedicoActivity, MedicoActivity::class.java)
                        // Opcional: Si quieres limpiar el back stack para que el usuario no pueda volver a EditMedicoActivity
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)

                        finish() // Cierra la actividad actual (EditMedicoActivity)



                    } else {
                        // Manejo de errores cuando la respuesta no es exitosa
                        val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                        Toast.makeText(this@EditMedicoActivity, "Error al actualizar médico: $errorMessage", Toast.LENGTH_LONG).show()
                        Log.e("MedicoServicio", "Error en la respuesta: ${response.code()} - $errorMessage")
                    }
                }

                override fun onFailure(call: Call<Medico>, t: Throwable) {
                    Toast.makeText(this@EditMedicoActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("MedicoaServicio", "Error en la llamada", t)
                }


            })
        }


    }
}