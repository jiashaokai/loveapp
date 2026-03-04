import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jk.lovediary.R
import com.jk.lovediary.model.MonthStatVO

class MonthStatAdapter(
    private val list: List<MonthStatVO>
) : RecyclerView.Adapter<MonthStatAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMonth: TextView = view.findViewById(R.id.tvMonth)
        val tvMyCount: TextView = view.findViewById(R.id.tvMyCount)
        val tvRelatedCount: TextView = view.findViewById(R.id.tvRelatedCount)
        val tvCommonCount: TextView = view.findViewById(R.id.tvCommonCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_month_stat, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.tvMonth.text = item.yearMonth
        holder.tvMyCount.text = "我：${item.myCount} 天"
        holder.tvRelatedCount.text = "TA：${item.relatedCount} 天"
        holder.tvCommonCount.text = "共同：${item.togetherCount} 天"
    }
}