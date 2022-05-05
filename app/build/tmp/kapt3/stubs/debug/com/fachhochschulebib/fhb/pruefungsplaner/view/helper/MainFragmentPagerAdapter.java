package com.fachhochschulebib.fhb.pruefungsplaner.view.helper;

import java.lang.System;

/**
 * Defines the viewpager, which is responsible for sliding between the [ExamOverviewFragment] and the [FavoriteOverviewFragment].
 *
 * @author Alexander Lange
 * @since 1.6
 *
 * **See Also**[Documentation](https://developer.android.com/reference/androidx/viewpager2/adapter/FragmentStateAdapter)
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\bH\u0016\u00a8\u0006\n"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/MainFragmentPagerAdapter;", "Landroidx/viewpager2/adapter/FragmentStateAdapter;", "fragmentActivity", "Landroidx/fragment/app/FragmentActivity;", "(Landroidx/fragment/app/FragmentActivity;)V", "createFragment", "Landroidx/fragment/app/Fragment;", "position", "", "getItemCount", "app_debug"})
public final class MainFragmentPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter {
    
    public MainFragmentPagerAdapter(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.FragmentActivity fragmentActivity) {
        super(null);
    }
    
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
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    /**
     * Called to create a new Fragment when the position int the tab layout changes.
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
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public androidx.fragment.app.Fragment createFragment(int position) {
        return null;
    }
}