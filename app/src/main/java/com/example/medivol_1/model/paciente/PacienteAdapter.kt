package com.example.medivol_1.model.paciente

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medivol_1.R
// Importa el Medico del paquete correcto, si lo moviste a 'model'
import com.example.medivol_1.model.paciente.Paciente
import com.example.medivol_1.model.Direccion // Importa también el modelo Direccion
import com.example.medivol_1.model.paciente.PacienteAdapter.PacienteViewHolder


class PacienteAdapter(private var pacientes: List<Paciente>) : RecyclerView.Adapter<PacienteAdapter.PacienteViewHolder>() {

    // Listener para los clics en los ítems (puedes usarlo para expandir/colapsar o para acciones generales)
    private var onItemClickListener: ((Paciente) -> Unit)? = null
    // Para manejar la expansión/colapso, una posición a la vez
    private var expandedPosition: Int = -1

    fun setOnItemClickListener(listener: (Paciente) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacienteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_paciente, parent, false)
        return PacienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: PacienteViewHolder, position: Int) {
        val paciente = pacientes[position]
        holder.bind(paciente)

        // Lógica para el encabezado de la letra (si la lista no está vacía)
        if (pacientes.isNotEmpty()) { // Añadir esta comprobación para evitar IndexOutOfBounds si la lista está vacía
            if (position == 0 || pacientes[position - 1].nombre[0] != paciente.nombre[0]) {
                holder.tvLetterHeader.visibility = View.VISIBLE
                holder.tvLetterHeader.text = paciente.nombre[0].uppercaseChar().toString() // Asegurar mayúscula
            } else {
                holder.tvLetterHeader.visibility = View.GONE
            }
        } else {
            holder.tvLetterHeader.visibility = View.GONE // Si la lista está vacía, ocultar encabezados
        }


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

            onItemClickListener?.invoke(paciente) // Llamar al listener externo si se desea acción al expandir/colapsar
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

    override fun getItemCount(): Int = pacientes.size

    // Esta función es correcta y la usas bien en MedicoActivity
    fun updateList(newList: List<Paciente>) {
        pacientes = newList
        notifyDataSetChanged() // Notificar que los datos han cambiado para refrescar la lista
        expandedPosition = -1 // Reiniciar la posición expandida al actualizar la lista
    }

    class PacienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLetterHeader: TextView = itemView.findViewById(R.id.tvLetterHeader)
        val tvPacienteName: TextView = itemView.findViewById(R.id.tvPacienteName)
        val expandableLayout: LinearLayout = itemView.findViewById(R.id.expandableLayout) // El layout que se expande
        val tvPacienteEmail: TextView = itemView.findViewById(R.id.tvPacienteEmail)
        val tvPacienteDocumento: TextView = itemView.findViewById(R.id.tvPacienteDocumento)
        val tvPacientePhone: TextView = itemView.findViewById(R.id.tvPacientePhone)
        val  tvPacienteAddress: TextView = itemView.findViewById(R.id.tvPacienteAddress) // Aquí estaba el error
         val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
         val btnDesactivar: Button = itemView.findViewById(R.id.btnDesactivar)

        fun bind(paciente: Paciente) {
            tvPacienteName.text = paciente.nombre
            // Combina especialidad y documento
           // tvMedicoSpecialty.text = "${medico.especialidad} | Documento ${medico.documento}"
            tvPacienteEmail.text = paciente.email
            tvPacienteDocumento.text = paciente.documento_identidad

            // El teléfono y el email no pueden ser nulos según tu JSON, pero si fueran, un "N/A" es bueno
            tvPacientePhone.text = paciente.telefono

            // Corregir la dirección, ya que ahora es un objeto Direccion
            tvPacienteAddress.text = formatAddress(paciente.direccion)

            // Si tienes un campo 'activo' en tu modelo Medico y tu API lo devuelve, puedes descomentar esto:
            /*
            if (medico.activo == false) { // Asumiendo que 'activo' es un Boolean en tu Medico.kt
                tvMedicoName.append(" (Inactivo)")
                // Puedes cambiar colores, etc. por ejemplo:
                // tvMedicoName.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.darker_gray))
            } else {
                // Restaurar color si estaba inactivo y ahora es activo (si la lista se recicla)
                // tvMedicoName.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.black))
            }
            */
        }

        // Función auxiliar para formatear la dirección
        private fun formatAddress(direccion: Direccion): String {
            return "${direccion.calle} ${direccion.numero}, ${direccion.barrio}, ${direccion.ciudad}, ${direccion.estado}, CP: ${direccion.codigo_postal}" +
                    (if (!direccion.complemento.isNullOrEmpty()) " (${direccion.complemento})" else "")
        }
    }
}