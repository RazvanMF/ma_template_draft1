package ro.kensierrat.apptemplate.views

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import ro.kensierrat.apptemplate.R
import ro.kensierrat.apptemplate.domain.GenericGrouping
import ro.kensierrat.apptemplate.domain.GenericModel

class GenericActivity1RecyclerViewAdapter(private val dataSet: MutableList<GenericGrouping>) : RecyclerView.Adapter<GenericActivity1RecyclerViewAdapter.ViewHolder>() {

    // in order to create new views for the recycler
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericActivity1RecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity1_listelement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenericActivity1RecyclerViewAdapter.ViewHolder, position: Int) {
        val currentItem = dataSet[position]
        holder.monthTextView.text = currentItem.genericMonth.toString()
        holder.expenseTextView.text = currentItem.genericInt.toString()

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val monthTextView: TextView = this.itemView.findViewById(R.id.monthTextActivity1)
        val expenseTextView: TextView = this.itemView.findViewById(R.id.expensesTextActivity1)
    }

}