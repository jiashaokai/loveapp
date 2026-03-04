import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jk.lovediary.fragment.MonthFragment
import java.time.YearMonth

class CalendarPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private val startYearMonth = YearMonth.of(2020, 1)

    override fun getItemCount(): Int = 240  // 20年范围

    override fun createFragment(position: Int): Fragment {
        val yearMonth = startYearMonth.plusMonths(position.toLong())
        return MonthFragment.newInstance(
            yearMonth.year,
            yearMonth.monthValue
        )
    }
}