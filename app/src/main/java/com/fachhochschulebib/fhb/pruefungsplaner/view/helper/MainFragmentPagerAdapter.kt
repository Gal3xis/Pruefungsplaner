package com.fachhochschulebib.fhb.pruefungsplaner.view.helper

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fachhochschulebib.fhb.pruefungsplaner.view.fragments.ExamOverviewFragment
import com.fachhochschulebib.fhb.pruefungsplaner.view.fragments.FavoriteOverviewFragment

/**
 * Defines the viewpager, which is responsible for sliding between the [ExamOverviewFragment] and the [FavoriteOverviewFragment].
 *
 * @author Alexander Lange
 * @since 1.6
 *
 * **See Also**[Documentation](https://developer.android.com/reference/androidx/viewpager2/adapter/FragmentStateAdapter)
 */
class MainFragmentPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    /**
     * Returns the number of pages in the viewpager. In this case it is fixed to two ([ExamOverviewFragment] & [FavoriteOverviewFragment])
     *
     * @return The total number of items in this adapter.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see FragmentStateAdapter.getItemCount
     */
    override fun getItemCount(): Int {
        return 2
    }

    /**
     * Called to create a new Fragment when the position int the tablayout changes.
     * In this case it is fixed, on the first position it creates the [ExamOverviewFragment] and on the second the [FavoriteOverviewFragment]
     *
     * @param position The current position in the tab layout
     *
     * @return The fragment that is to be displayed
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see FragmentStateAdapter.createFragment
     */
    override fun createFragment(position: Int): Fragment {
        return when(position){
            1-> FavoriteOverviewFragment()
            else-> ExamOverviewFragment()
        }
    }
}