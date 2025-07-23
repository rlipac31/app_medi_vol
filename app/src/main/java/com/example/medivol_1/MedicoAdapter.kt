package com.example.medivol_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
// Importa el Medico del paquete correcto, si lo moviste a 'model'
import com.example.medivol_1.model.medico.Medico
import com.example.medivol_1.model.Direccion // Importa también el modelo Direccion

class MedicoAdapter(private var medicos: List<Medico>) : RecyclerView.Adapter<MedicoAdapter.MedicoViewHolder>() {

    // Listener para los clics en los ítems (puedes usarlo para expandir/colapsar o para acciones generales)
    private var onItemClickListener: ((Medico) -> Unit)? = null
    // Para manejar la expansión/colapso, una posición a la vez
    private var expandedPosition: Int = -1

    fun setOnItemClickListener(listener: (Medico) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medico, parent, false)
        return MedicoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicoViewHolder, position: Int) {
        val medico = medicos[position]
        holder.bind(medico)

        // Lógica para el encabezado de la letra (si la lista no está vacía)
        if (medicos.isNotEmpty()) { // Añadir esta comprobación para evitar IndexOutOfBounds si la lista está vacía
            if (position == 0 || medicos[position - 1].nombre[0] != medico.nombre[0]) {
                holder.tvLetterHeader.visibility = View.VISIBLE
                holder.tvLetterHeader.text = medico.nombre[0].uppercaseChar().toString() // Asegurar mayúscula
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

            onItemClickListener?.invoke(medico) // Llamar al listener externo si se desea acción al expandir/colapsar
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

    override fun getItemCount(): Int = medicos.size

    // Esta función es correcta y la usas bien en MedicoActivity
    fun updateList(newList: List<Medico>) {
        medicos = newList
        notifyDataSetChanged() // Notificar que los datos han cambiado para refrescar la lista
        expandedPosition = -1 // Reiniciar la posición expandida al actualizar la lista
    }

    class MedicoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLetterHeader: TextView = itemView.findViewById(R.id.tvLetterHeader)
        val tvMedicoName: TextView = itemView.findViewById(R.id.tvMedicoName)
        val tvMedicoSpecialty: TextView = itemView.findViewById(R.id.tvMedicoSpecialty)
        val expandableLayout: LinearLayout = itemView.findViewById(R.id.expandableLayout) // El layout que se expande
        val tvMedicoEmail: TextView = itemView.findViewById(R.id.tvMedicoEmail)
        val tvMedicoPhone: TextView = itemView.findViewById(R.id.tvMedicoPhone)
        val tvMedicoAddress: TextView = itemView.findViewById(R.id.tvMedicoAddress) // Aquí estaba el error
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnDesactivar: Button = itemView.findViewById(R.id.btnDesactivar)

        fun bind(medico: Medico) {
            tvMedicoName.text = medico.nombre
            // Combina especialidad y documento
            tvMedicoSpecialty.text = "${medico.especialidad} | Documento ${medico.documento}"
            tvMedicoEmail.text = medico.email

            // El teléfono y el email no pueden ser nulos según tu JSON, pero si fueran, un "N/A" es bueno
            tvMedicoPhone.text = medico.telefono

            // Corregir la dirección, ya que ahora es un objeto Direccion
            tvMedicoAddress.text = formatAddress(medico.direccion)

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