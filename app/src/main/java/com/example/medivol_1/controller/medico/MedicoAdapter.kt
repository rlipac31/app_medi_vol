package com.example.medivol_1.controller.medico

// Importa el Medico del paquete correcto, si lo moviste a 'model'
import TokenManager
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medivol_1.Constants
import com.example.medivol_1.R
import com.example.medivol_1.model.Direccion
import com.example.medivol_1.model.medico.Medico

class MedicoAdapter(
    private var medicos: List<Medico>,
    private val onEditClick: (Medico) -> Unit,      // Callback para editar
    private val onDeleteClick: (Medico) -> Unit

) : RecyclerView.Adapter<MedicoAdapter.MedicoViewHolder>() {

    // Listener para los clics en los ítems (puedes usarlo para expandir/colapsar o para acciones generales)
    private var onItemClickListener: ((Medico) -> Unit)? = null
    // Para manejar la expansión/colapso, una posición a la vez
    private var expandedPosition: Int = -1

    fun setOnItemClickListener(listener: (Medico) -> Unit) {
        onItemClickListener = listener
    }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medico, parent, false)
        return MedicoViewHolder(view, onEditClick, onDeleteClick)

    }

    override fun onBindViewHolder(holder: MedicoViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val medico = medicos[position]
        holder.bind(medico)
        // Obtén el Context desde la vista del holder.
        val context = holder.itemView.context
    //obteniendo role del usuario
        // Pasa el context a la función getUserRole()
         var userRole = TokenManager.getUserRole(context)
        Log.e("UserRole", "rol de usuario de usuario: $userRole")

        // Lógica para el encabezado de la letra (si la lista no está vacía)
        if (medicos.isNotEmpty()) { // Añadir esta comprobación para evitar IndexOutOfBounds si la lista está vacía
            if (position == 0 || medicos[position - 1].nombre[0] != medico.nombre[0]) {
                holder.tvLetterHeader.visibility = View.VISIBLE
                holder.tvLetterHeader.text = medico.nombre[0].uppercaseChar().toString() // Asegurar mayúscula
                if (userRole == Constants.ADMIN_ROLE){
                    holder.btnDesactivar.visibility = View.VISIBLE
                    holder.btnEditar.visibility = View.VISIBLE
                } else {
                    holder.btnDesactivar.visibility= View.GONE
                    holder.btnEditar.visibility= View.GONE
                }

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


    }

    override fun getItemCount(): Int = medicos.size

    // Esta función es correcta y la usas bien en MedicoActivity
    fun updateList(newList: List<Medico>) {
        medicos = newList
        notifyDataSetChanged() // Notificar que los datos han cambiado para refrescar la lista
        expandedPosition = -1 // Reiniciar la posición expandida al actualizar la lista
    }

    class MedicoViewHolder(
        itemView: View,
        private val onEditClick: (Medico) -> Unit,
        private val onDeleteClick: (Medico) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {


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
           //    medicoId= medico.id
            tvMedicoName.text = medico.nombre
            // Combina especialidad y documento
            tvMedicoSpecialty.text = "${medico.especialidad} | Documento ${medico.documento}"
            tvMedicoEmail.text = medico.email
            // El teléfono y el email no pueden ser nulos según tu JSON, pero si fueran, un "N/A" es bueno
            tvMedicoPhone.text = medico.telefono
            // Corregir la dirección, ya que ahora es un objeto Direccion
            tvMedicoAddress.text = formatAddress(medico.direccion)
            // 4. Asigna los listeners de los botones aquí en el bind()
            btnEditar.setOnClickListener {
                onEditClick(medico) // Llama al callback de editar
            }

            btnDesactivar.setOnClickListener {
                onDeleteClick(medico) // Llama al callback de eliminar
            }

        }

        // Función auxiliar para formatear la dirección
        private fun formatAddress(direccion: Direccion): String {
            return "${direccion.calle} ${direccion.numero}, ${direccion.departamento}, ${direccion.provincia}, ${direccion.distrito}" +
                    (if (!direccion.codigo_postal.isNullOrEmpty()) " (${direccion.codigo_postal})" else "") +
                    (if (!direccion.complemento.isNullOrEmpty()) " (${direccion.complemento})" else "")
        }
    }
}