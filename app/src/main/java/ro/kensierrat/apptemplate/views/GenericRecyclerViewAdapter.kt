package ro.kensierrat.apptemplate.views

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import ro.kensierrat.apptemplate.R
import ro.kensierrat.apptemplate.domain.GenericModel

class GenericRecyclerViewAdapter(private val dataSet: MutableList<GenericModel>) : RecyclerView.Adapter<GenericRecyclerViewAdapter.ViewHolder>() {

    private var onDeleteButtonPress: OnClickListener? = null
    private var onEditButtonPress: OnClickListener? = null
    private var onInfoButtonPress: OnClickListener? = null

    // in order to create new views for the recycler
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_element, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenericRecyclerViewAdapter.ViewHolder, position: Int) {
        val currentItem = dataSet[position]
        holder.idTextView.text = "GENERIC ITEM #" + currentItem.id.toString()
        holder.dateTextView.text = "GENERIC DATE: " + currentItem.genericDate
        holder.intTextView.text = "GENERIC INT: " + currentItem.genericInt.toString()
        holder.stringTextView.text = "GENERIC STRING: " + currentItem.genericString


        holder.deleteButton.setOnClickListener {
            onDeleteButtonPress?.onClick(holder.bindingAdapterPosition, currentItem, currentItem.id)
        }
        holder.editButton.setOnClickListener {
            onEditButtonPress?.onClick(holder.bindingAdapterPosition, currentItem, currentItem.id)
        }
        holder.infoButton.setOnClickListener {
            onInfoButtonPress?.onClick(holder.bindingAdapterPosition, currentItem, currentItem.id)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setOnDeleteButtonPress(listener: OnClickListener?) {
        this.onDeleteButtonPress = listener
    }

    fun setOnEditButtonPress(listener: OnClickListener?) {
        this.onEditButtonPress = listener
    }

    fun setOnInfoButtonPress(listener: OnClickListener?) {
        this.onInfoButtonPress = listener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: GenericModel, id: Int)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idTextView: TextView = this.itemView.findViewById(R.id.genericElement_Id)
        val dateTextView: TextView = this.itemView.findViewById(R.id.genericElement_Date)
        val intTextView: TextView = this.itemView.findViewById(R.id.genericElement_Int)
        val stringTextView: TextView = this.itemView.findViewById(R.id.genericElement_String)

        val editButton: Button = this.itemView.findViewById(R.id.genericElement_EditButton)
        val deleteButton: Button = this.itemView.findViewById(R.id.genericElement_DeleteButton)
        val infoButton: Button = this.itemView.findViewById(R.id.genericElement_InfoButton)
    }

}