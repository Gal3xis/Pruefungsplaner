package com.fachhochschulebib.fhb.pruefungsplaner.view.activities;

import java.lang.System;

/**
 * Main-Class, Controls the main part of the app except the start page, where the user picks his faculty and courses in the MainActivity.kt.
 * The MainWindow is the [ExamOverviewFragment], where the user can view and pick exams.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0002J\u0010\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0018\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0018\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u0019\u001a\u00020\fH\u0002J\u0018\u0010\u001a\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0010\u0010\u001b\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0018\u0010\u001c\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0010\u0010\u001d\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0010\u0010\u001e\u001a\u00020\f2\u0006\u0010\u001f\u001a\u00020 H\u0002J\b\u0010!\u001a\u00020\fH\u0002J\b\u0010\"\u001a\u00020\fH\u0002J\u0010\u0010#\u001a\u00020\f2\u0006\u0010\u001f\u001a\u00020 H\u0002J\b\u0010$\u001a\u00020\fH\u0002J\b\u0010%\u001a\u00020\fH\u0002J\b\u0010&\u001a\u00020\fH\u0016J\u0012\u0010\'\u001a\u00020\f2\b\u0010(\u001a\u0004\u0018\u00010)H\u0014J\u0010\u0010*\u001a\u00020\b2\u0006\u0010\u001f\u001a\u00020 H\u0016J\b\u0010+\u001a\u00020\fH\u0002J\u0010\u0010,\u001a\u00020\b2\u0006\u0010-\u001a\u00020.H\u0002J\u0010\u0010/\u001a\u00020\b2\u0006\u0010-\u001a\u00020.H\u0016J\b\u00100\u001a\u00020\fH\u0002J\u0006\u00101\u001a\u00020\fR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u00062"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/view/activities/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "filterDialog", "Landroid/app/AlertDialog;", "viewModel", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/MainViewModel;", "changeFragment", "", "fragment", "Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/MainActivityFragment;", "closeKeyboard", "", "initActionBar", "initFilterCalendar", "filterView", "Landroid/view/View;", "initFilterCheckbox", "c", "Landroid/widget/CheckBox;", "semester", "", "initFilterCourse", "context", "Landroid/content/Context;", "initFilterDialog", "initFilterExaminer", "initFilterHeader", "initFilterModule", "initFilterSemesterBoxes", "initMenu", "menu", "Landroid/view/Menu;", "initNavigationDrawer", "initPeriodTimeSpan", "initSearchView", "initTabLayout", "initViewPager", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "onNavigationDrawerButtonClicked", "onNavigationDrawerItemClicked", "item", "Landroid/view/MenuItem;", "onOptionsItemSelected", "openFilterMenu", "userFilter", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    
    /**
     * The dialog that presents the filter to the user.
     */
    private android.app.AlertDialog filterDialog;
    
    /**
     * The ViewModel for the MainActivity. Is set in [onCreate].
     * @see MainViewModel
     */
    private com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.MainViewModel viewModel;
    private java.util.HashMap _$_findViewCache;
    
    public MainActivity() {
        super();
    }
    
    /**
     * Overrides the onCreate()-Method, which is called first in the Fragment-LifeCycle.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onCreate
     */
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    /**
     * Initializes the Tab Layout. The tab layout presents the user multiple fragments as tabs with a navigation bar.
     * In this case, the tabs are [ExamOverviewFragment] and [FavoriteOverviewFragment].
     * With help of the viewPager, the user can switch between the fragments with sliding the page to the left or right.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see TabLayout
     * @see MainFragmentPagerAdapter
     */
    private final void initTabLayout() {
    }
    
    /**
     * Initializes the ViewPager that is attached to the TabLayout.
     * Sets the adapter to the [MainFragmentPagerAdapter]
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see MainFragmentPagerAdapter
     */
    private final void initViewPager() {
    }
    
    /**
     * Initialized the Actionbar
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initActionBar() {
    }
    
    /**
     * Overrides the onCreateOptionsMenu()-Method, used to create the action menu in the top-right corner.
     * The menu contains the Filter-button and the search-button.
     *
     * @param[menu] The menu from this fragment. The action-menu is after inflation assigned to this.
     * @return Return true to show the menu, if it returns false, the menu is hidden
     * @author Alexander Lange
     * @since 1.6
     * @see AppCompatActivity.onCreateOptionsMenu
     * @see SearchView.autofill
     */
    @java.lang.Override()
    public boolean onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu) {
        return false;
    }
    
    /**
     * Initializes the menu in the actionbar, shows the filter icon and hides the save button.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initMenu(android.view.Menu menu) {
    }
    
    /**
     * Initializes the searchview. The searchview is located in the actionbar, where the user
     * can search a specific module from everywhere in the Application except the start page.
     *
     * @param menu The menu in which the searchview is embedded.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initSearchView(android.view.Menu menu) {
    }
    
    /**
     * Overrides the onOptionsItemSelected()-Method, used to declare what happens if the user clicked on an menu-item.
     *
     * @param[item] The item which was selected from the user. Use item.itemId to identify the item.
     * @return return false for normal processing.
     * @author Alexander Lange
     * @since 1.6
     * @see AppCompatActivity.onOptionsItemSelected
     */
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    /**
     * Overrides the onBackPressed()-Method. Is Called when the user presses the back-button on his smartphone.
     * Ending the App in a controlled manner. That means if the [ExamOverviewFragment] is open, then the user is asked if he wants to exit the Application.
     * Else he is directed to the [ExamOverviewFragment]
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    /**
     * Initializes the NavigationDrawer. The navigation drawer is the main tool for the user to switch between fragments.
     * It can be accessed via the hamburger-icon from the actionbar.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initNavigationDrawer() {
    }
    
    /**
     * Handler for when the user clicked the hamburger-icon in the actionbar to open or close the navigationdrawer.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void onNavigationDrawerButtonClicked() {
    }
    
    /**
     * Handler for when the user clicked an icon in the navigationdrawer. Directs him to the selected fragment.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final boolean onNavigationDrawerItemClicked(android.view.MenuItem item) {
        return false;
    }
    
    /**
     * Closes the Keyboard of the smartphone.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void closeKeyboard() {
    }
    
    /**
     * Directs the user to a new Fragment.
     *
     * @param[fragment] The fragment which shall be shown.
     *
     * @return Returns always true, needed for the listener.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final boolean changeFragment(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment fragment) {
        return false;
    }
    
    /**
     * Initializes the filter dialog. With the filter dialog, the user can filter through the shown exams.
     * The Filter for the [ExamOverviewFragment] and the [FavoriteOverviewFragment] are the same, so
     * a change in one Fragment is also a change in the other.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initFilterDialog() {
    }
    
    /**
     * Sets the Filter for the users default configuration.
     * Reads the faculty and maincourse from shared preferences and sets the filter to this values.
     *
     * @author Alexander Lange
     * @since 1.6
     * @see Filter
     */
    public final void userFilter() {
    }
    
    /**
     * Sets the text for the current period with content from shared preferences
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initPeriodTimeSpan() {
    }
    
    /**
     * Creates and opens the Filter-Dialog.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void openFilterMenu() {
    }
    
    /**
     * Initializes the header of the filter. The header shows the faculty, the user has chosen.
     *
     * @param filterView The view of the filter.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initFilterHeader(android.view.View filterView) {
    }
    
    /**
     * Initializes the checkboxes of the filter, where the user can pick specific semester.
     *
     * @param filterView The view of the filter.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initFilterSemesterBoxes(android.view.View filterView) {
    }
    
    /**
     * Initializes a checkbox of the filter-menu.
     *
     * @param[c] The checkbox to be initialized.
     * @param[semester] The semester, the checkbox is representing
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initFilterCheckbox(android.widget.CheckBox c, int semester) {
    }
    
    /**
     * Initializes the examiner-filter in the filter menu.
     * Creates an adapter with all first-examiners to implement an autocompletion.
     *
     * @param context The applicationcontext.
     * @param filterView The view of the filter.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initFilterExaminer(android.content.Context context, android.view.View filterView) {
    }
    
    /**
     * Updates the Course-Filter-Spinner in the Filter-dialog.
     * Creates a list of course names from the room-database and passes them to the spinner.
     *
     * @param[context] the current context
     * @param[filterView] the view of the filter
     * @author Alexander Lange
     * @since 1.6
     * @see openFilterMenu
     * @see initFilterModule
     * @see Filter
     */
    private final void initFilterCourse(android.content.Context context, android.view.View filterView) {
    }
    
    /**
     * Updates the module-Filter-Spinner in the Filter-dialog.
     * Creates a list of faculty names from the room-database and passes them to the spinner.
     *
     * @param[context] the current context
     * @param[filterView] the view of the filter
     * @author Alexander Lange
     * @since 1.6
     * @see openFilterMenu
     * @see initFilterCourse
     * @see initFilterModule
     * @see Filter
     */
    private final void initFilterModule(android.content.Context context, android.view.View filterView) {
    }
    
    /**
     * Initializes the Calendar-Button of the menu. If the user clicks the Button,
     * he is navigated to a DatePicker-Dialog where he can set a day to filter
     * the modules.
     *
     * @param[filterView] the view of the filter
     * @author Alexander Lange
     * @since 1.6
     * @see Calendar
     * @see DatePickerDialog
     * @see Filter
     */
    private final void initFilterCalendar(android.view.View filterView) {
    }
}