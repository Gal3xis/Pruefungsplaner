package com.fachhochschulebib.fhb.pruefungsplaner.view.fragments;

import java.lang.System;

/**
 * Class to maintain the Options-Fragment.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0002J\b\u0010\u000e\u001a\u00020\fH\u0002J\b\u0010\u000f\u001a\u00020\fH\u0002J\b\u0010\u0010\u001a\u00020\fH\u0002J\b\u0010\u0011\u001a\u00020\fH\u0002J\b\u0010\u0012\u001a\u00020\fH\u0002J\b\u0010\u0013\u001a\u00020\fH\u0002J\b\u0010\u0014\u001a\u00020\fH\u0002J\b\u0010\u0015\u001a\u00020\fH\u0002J\b\u0010\u0016\u001a\u00020\fH\u0002J\b\u0010\u0017\u001a\u00020\fH\u0002J\b\u0010\u0018\u001a\u00020\fH\u0002J\b\u0010\u0019\u001a\u00020\fH\u0002J\b\u0010\u001a\u001a\u00020\fH\u0002J\b\u0010\u001b\u001a\u00020\fH\u0002J\u0012\u0010\u001c\u001a\u00020\f2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\u0018\u0010\u001f\u001a\u00020\f2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0016J&\u0010$\u001a\u0004\u0018\u00010%2\u0006\u0010\"\u001a\u00020&2\b\u0010\'\u001a\u0004\u0018\u00010(2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\u0010\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020,H\u0016J\u001a\u0010-\u001a\u00020\f2\u0006\u0010.\u001a\u00020%2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\b\u0010/\u001a\u00020\fH\u0002J\u001c\u00100\u001a\u00020\f2\b\b\u0002\u00101\u001a\u0002022\b\b\u0002\u00103\u001a\u000202H\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0096\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u00064"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/view/fragments/SettingsFragment;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/MainActivityFragment;", "()V", "name", "", "getName", "()Ljava/lang/String;", "setName", "(Ljava/lang/String;)V", "viewModel", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/SettingsViewModel;", "initBackgroundUpdateIntervalButton", "", "initBackgroundUpdateSwitch", "initCalendarIdSpinner", "initCalendarInsertionTypeSpinner", "initCalendarSynchronizationSwitch", "initDarkModeSwitch", "initDeleteCalendarEntriesButton", "initDeleteDatabaseButton", "initDeleteFavoritesButton", "initImpressumButton", "initPrivacyDeclarationButton", "initSaveButton", "initThemeSpinner", "initUpdateCalendarButton", "initUpdateDatabaseButton", "initView", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "inflater", "Landroid/view/MenuInflater;", "onCreateView", "Landroid/view/View;", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "onViewCreated", "view", "save", "setIntervallTime", "hour", "", "minute", "app_debug"})
public final class SettingsFragment extends com.fachhochschulebib.fhb.pruefungsplaner.view.helper.MainActivityFragment {
    
    /**
     * ViewModel for the SettingsFragment. Is set in [onViewCreated].
     * @see SettingsViewModel
     */
    private com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.SettingsViewModel viewModel;
    
    /**
     * Sets the name of that fragment to "Datenschutzerklärung"
     */
    @org.jetbrains.annotations.NotNull()
    private java.lang.String name = "Einstellungen";
    private java.util.HashMap _$_findViewCache;
    
    public SettingsFragment() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String getName() {
        return null;
    }
    
    @java.lang.Override()
    public void setName(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    /**
     * Overrides the onCreate()-Method, which is called first in the Fragment-LifeCycle.
     * In this Method, the global parameter which are independent of the UI get initialized,
     * like the App-SharedPreferences and the reference to the Room-Database
     *
     * @since 1.6
     *
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     *
     * @see Fragment.onCreate
     */
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    /**
     * Overrides the onCreateView()-Method. It sets the current view to the optionfragmnet-layout.
     *
     * @return Returns the initialized view of this Fragment
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onCreateView
     */
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    /**
     * Overrides the onViewCreated()-Method, which is called in the Fragment LifeCycle right after the onCreateView()-Method.
     * In this Method, the UI-Elements choose_course.xml-Layout are being initialized. This cannot be done in the onCreate()-Method,
     * because the UI-Elements, which are directly accessed via synthetic imports
     * are no instantiated in the onCreate()-Method yet.
     *
     * @since 1.6
     * @author Alexander Lange (E-Mail:alexander.lange@fh-bielefeld.de)
     * @see Fragment.onViewCreated
     */
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    /**
     * Overrides the [onCreateOptionsMenu]-Method from the [Fragment]-Superclass.
     * Initializes the items for the actionmenu. In this case the save-button.
     * @param[menu] The menu where the items should be displayed.
     * @param[inflater] The [MenuInflater] to inflate the actionmenu.
     * @author Alexander Lange
     * @since 1.6
     * @see Fragment.onCreateOptionsMenu
     * @see Menu
     */
    @java.lang.Override()
    public void onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu, @org.jetbrains.annotations.NotNull()
    android.view.MenuInflater inflater) {
    }
    
    /**
     * Overrides the [onOptionsItemSelected]-Method from the [Fragment]-Superclass.
     * Called when the user clicks an item in the actionmenu.
     * Determines what to do for each item.
     * @param[item] The item, which was clicked by the user.
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     * @author Alexander Lange
     * @since 1.6
     * @see Fragment.onOptionsItemSelected
     * @see MenuItem
     */
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    /**
     * Initialized the UI.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initView() {
    }
    
    /**
     * Initializes the CalendarId Spinner. The Spinner lets the user pick, in which calendar the exams shall bes synced.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initCalendarIdSpinner() {
    }
    
    /**
     * Initializes the save button. Saves all remaining changes to the shared preferences, and if necessary, asks the user to restart the app.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initSaveButton() {
    }
    
    /**
     * Initializes the button to delete all favorites.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initDeleteFavoritesButton() {
    }
    
    /**
     * Initializes the button to update the calendar. Inserts every favorite into the calendar, despite a deactivated synchronization.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initUpdateCalendarButton() {
    }
    
    /**
     * Initializes the button to delete all calendar entries. Deletes every entry in the calendar that is saved in the shared preferences.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initDeleteCalendarEntriesButton() {
    }
    
    /**
     * Initializes the button to delete all [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry]-Objects from the local room database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initDeleteDatabaseButton() {
    }
    
    /**
     * Initializes the button to show the impressum.
     * Redirects the user to the [ImpressumFragment].
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initImpressumButton() {
    }
    
    /**
     * Initializes the button to show the privacy declaration.
     * Redirects the user to the [PrivacyDeclarationFragment].
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initPrivacyDeclarationButton() {
    }
    
    /**
     * Initializes the switch to activate/deactivate the calendar synchronization.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initCalendarSynchronizationSwitch() {
    }
    
    /**
     * Initializes the spinner that lets the user pick an [CalendarIO.InsertionType].
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initCalendarInsertionTypeSpinner() {
    }
    
    /**
     * Initializes the button to update the database.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initUpdateDatabaseButton() {
    }
    
    /**
     * Initializes the darkmode-[Switch]. Get the previous selection from sharedpreferences and
     * pass them to the darkmode-[Switch].
     * @author Alexander Lange
     * @since 1.6
     * @see Switch
     */
    private final void initDarkModeSwitch() {
    }
    
    /**
     * Initializes the theme-[Spinner]. Create a [Theme] for every implemented theme in
     * the styles.xml and passes them to a custom [ThemeAdapter], which is then passed to
     * the theme-[Spinner].
     * @author Alexander Lange
     * @since 1.6
     * @see Theme
     * @see ThemeAdapter
     * @see Spinner
     */
    private final void initThemeSpinner() {
    }
    
    /**
     * Initializes the switch to activate/deactivate the search for new database updates in the background.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initBackgroundUpdateSwitch() {
    }
    
    /**
     * Initializes the button to change the interval time of the background worker.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void initBackgroundUpdateIntervalButton() {
    }
    
    /**
     * Helperfunction for the TimePicker to change the background interval time.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see initBackgroundUpdateIntervalButton
     */
    private final void setIntervallTime(int hour, int minute) {
    }
    
    /**
     * Save the current selected options and recreate the activity to change the theme and the darkmmode.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final void save() {
    }
}