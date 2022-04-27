package com.fachhochschulebib.fhb.pruefungsplaner.view.helper

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fachhochschulebib.fhb.pruefungsplaner.view.fragments.ExamOverviewFragment
import com.fachhochschulebib.fhb.pruefungsplaner.view.fragments.FavoriteOverviewFragment


class MainFragmentPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            1-> FavoriteOverviewFragment()
            else-> ExamOverviewFragment()
        }
    }
}