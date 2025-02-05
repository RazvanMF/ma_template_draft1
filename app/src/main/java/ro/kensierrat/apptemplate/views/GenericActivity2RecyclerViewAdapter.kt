package ro.kensierrat.apptemplate.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ro.kensierrat.apptemplate.R
import ro.kensierrat.apptemplate.domain.GenericGrouping
import ro.kensierrat.apptemplate.domain.GenericGrouping2

class GenericActivity2RecyclerViewAdapter(private val dataSet: MutableList<GenericGrouping2>) : RecyclerView.Adapter<GenericActivity2RecyclerViewAdapter.ViewHolder>() {

    // in order to create new views for the recycler
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericActivity2RecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity2_listelement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenericActivity2RecyclerViewAdapter.ViewHolder, position: Int) {
        val currentItem = dataSet[position]
        holder.monthTextView.text = currentItem.genericPrefix.toString()
        holder.expenseTextView.text = currentItem.genericInt.toString()

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val monthTextView: TextView = this.itemView.findViewById(R.id.prefixTextActivity2)
        val expenseTextView: TextView = this.itemView.findViewById(R.id.intTextActivity2)
    }

}