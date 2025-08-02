package com.example.medivol_1.model.consulta

import android.annotation.SuppressLint
import com.example.medivol_1.R

import android.util.Log // Importa para Log.e
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medivol_1.MedicoAdapter.MedicoViewHolder
// Importa el Consulta del paquete correcto, si lo moviste a 'model'
import com.example.medivol_1.model.consulta.Consulta
import com.example.medivol_1.model.Direccion // Importa también el modelo Direccion
import com.example.medivol_1.service.ConsultaService
import java.util.Locale

class ConsultaAdapter(private var consultas: List<Consulta>) : RecyclerView.Adapter<ConsultaAdapter.ConsultaViewHolder>() {

    // Listener para los clics en los ítems (puedes usarlo para expandir/colapsar o para acciones generales)
    private var onItemClickListener: ((Consulta) -> Unit)? = null
    // Para manejar la expansión/colapso, una posición a la vez
    private var expandedPosition: Int = -1

    // Define los formateadores de fecha una vez para eficiencia
    // Ajusta el formato 'yyyy-MM-dd'T'HH:mm:ss' para que coincida exactamente con tu string 'fecha' del backend
    private val apiDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    private val displayDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())


    fun setOnItemClickListener(listener: (Consulta) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_consulta, parent, false)
        return ConsultaViewHolder(view)
    }



    override fun onBindViewHolder(holder: ConsultaViewHolder, position: Int) {
        val consulta = consultas[position]
        holder.bind(consulta)

        // --- INICIO DE LA LÓGICA PARA tvLetterHeader ---
        // 1. Parsear la fecha de la consulta actual a LocalDate
        val currentConsultaDate: LocalDate? = try {
            // Intenta parsear como LocalDateTime y luego convertir a LocalDate
            LocalDateTime.parse(consulta.fecha, apiDateTimeFormatter).toLocalDate()
        } catch (e: Exception) {
            // Si falla el formato completo, intenta parsear como LocalDate directamente (ej. si la cadena es solo "yyyy-MM-dd")
            try {
                LocalDate.parse(consulta.fecha)
            } catch (e2: Exception) {
                Log.e("ConsultaAdapter", "Error al parsear fecha '${consulta.fecha}': ${e2.message}")
                null // Si no se puede parsear ninguno de los formatos, la fecha es nula
            }
        }

        // Variable para el texto que se mostrará en el encabezado
        var headerText: String? = null

        // 2. Determinar el texto del encabezado ("HOY" o la fecha formateada)
        val today = LocalDate.now()
        if (currentConsultaDate != null) {
            if (currentConsultaDate.isEqual(today)) {
                headerText = "HOY"
            } else {
                // Formatear la fecha para mostrar (ej. "22 Jul 2025")
                headerText = currentConsultaDate.format(displayDateFormatter)
            }
        }

        // 3. Lógica para mostrar/ocultar tvLetterHeader (agrupación por fecha/HOY)
        if (position == 0) {
            // Siempre mostrar el encabezado para el primer elemento de la lista
            holder.tvLetterHeader.visibility = View.VISIBLE
            holder.tvLetterHeader.text = headerText ?: "" // Usa cadena vacía si headerText es nulo (error de parseo)
        } else {
            // Obtener la fecha del elemento anterior para comparar
            val prevConsulta = consultas[position - 1]
            val prevConsultaDate: LocalDate? = try {
                LocalDateTime.parse(prevConsulta.fecha, apiDateTimeFormatter).toLocalDate()
            } catch (e: Exception) {
                try {
                    LocalDate.parse(prevConsulta.fecha)
                } catch (e2: Exception) {
                    null
                }
            }

            // Determinar el texto del encabezado para el elemento anterior
            var prevHeaderText: String? = null
            if (prevConsultaDate != null) {
                if (prevConsultaDate.isEqual(today)) {
                    prevHeaderText = "HOY"
                } else {
                    prevHeaderText = prevConsultaDate.format(displayDateFormatter)
                }
            }

            // Mostrar el encabezado solo si el texto del encabezado actual es diferente al del anterior
            // Esto asegura que "HOY" solo aparezca una vez para el bloque de hoy, y las fechas una vez por día.
            if (headerText != null && headerText != prevHeaderText) {
                holder.tvLetterHeader.visibility = View.VISIBLE
                holder.tvLetterHeader.text = headerText
            } else {
                holder.tvLetterHeader.visibility = View.GONE
            }
        }
        // --- FIN DE LA LÓGICA PARA tvLetterHeader ---

        // Lógica de expansión/colapso
        val isExpanded = position == expandedPosition
        holder.expandableLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener {
            val previousExpandedPosition = expandedPosition
            if (isExpanded) {
                expandedPosition = -1 // Colapsar
            } else {
                expandedPosition = position // Expandir
            }

            // Notificar los cambios para que RecyclerView actualice las vistas
            if (previousExpandedPosition != -1) {
                notifyItemChanged(previousExpandedPosition) // Colapsa el anterior
            }
            if (expandedPosition != -1) {
                notifyItemChanged(expandedPosition) // Expande el actual
            }

            onItemClickListener?.invoke(consulta) // Llamar al listener externo si se desea acción al expandir/colapsar
        }

        /*// Clis para los botones dentro del item (Editar/Desactivar)
         holder.btnEditar.setOnClickListener {
             // Lógica para editar el médico. Podrías pasar el 'medico' a una función en la Activity.
             // Ejemplo: (holder.itemView.context as MedicoActivity).onEditMedicoClicked(medico)
         }
         holder.btnDesactivar.setOnClickListener {
             // Lógica para desactivar el médico.
             // Ejemplo: (holder.itemView.context as MedicoActivity).onDesactivarMedicoClicked(medico)
         }*/
    }

    override fun getItemCount(): Int = consultas.size

    // Esta función es correcta y la usas bien en MedicoActivity
    fun updateList(newList: List<Consulta>) {
        consultas = newList
        notifyDataSetChanged() // Notificar que los datos han cambiado para refrescar la lista
        expandedPosition = -1 // Reiniciar la posición expandida al actualizar la lista
    }

    class ConsultaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLetterHeader: TextView = itemView.findViewById(R.id.tvLetterHeader)
        val tvConsultaFecha: TextView = itemView.findViewById(R.id.tvConsultaFecha)
        val tvMedicoName: TextView = itemView.findViewById(R.id.tvMedicoName)
        val tvMedicoSpecialty: TextView = itemView.findViewById(R.id.tvMedicoSpecialty)
        val tvPacienteName: TextView = itemView.findViewById(R.id.tvPacienteName)
        val expandableLayout: LinearLayout = itemView.findViewById(R.id.expandableLayout) // El layout que se expande
        val tvPacienteEmail: TextView = itemView.findViewById(R.id.tvPacienteEmail)
        val tvPacientePhone: TextView = itemView.findViewById(R.id.tvPacientePhone)
        val tvPacienteDocumento: TextView = itemView.findViewById(R.id.tvPacienteDocumento)
        val tvPacienteAddress: TextView = itemView.findViewById(R.id.tvPacienteAddress) // Aquí estaba el error
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnDesactivar: Button = itemView.findViewById(R.id.btnDesactivar)

        fun bind(consulta: Consulta) {
            tvConsultaFecha.text = consulta.fecha
            tvMedicoName.text = consulta.nombre
            // Combina especialidad y documento
            tvMedicoSpecialty.text = "${consulta.especialidad} | Documento ${consulta.documento_medico}"
            tvPacienteName.text = consulta.nombre_paciente
            tvPacienteEmail.text = consulta.email_paciente

            // El teléfono y el email no pueden ser nulos según tu JSON, pero si fueran, un "N/A" es bueno
            tvPacientePhone.text = consulta.phone_paciente
            tvPacienteDocumento.text = consulta.documento_paciente

            // Corregir la dirección, ya que ahora es un objeto Direccion
            tvPacienteAddress.text = formatAddress(consulta.direccion)


        }

        // Función auxiliar para formatear la dirección
        private fun formatAddress(direccion: Direccion): String {
            return "${direccion.calle} ${direccion.numero}, ${direccion.distrito}, ${direccion.provincia}, ${direccion.departamento}, CP: ${direccion.codigo_postal}" +
                    (if (!direccion.complemento.isNullOrEmpty()) " (${direccion.complemento})" else "")
        }
    }
}