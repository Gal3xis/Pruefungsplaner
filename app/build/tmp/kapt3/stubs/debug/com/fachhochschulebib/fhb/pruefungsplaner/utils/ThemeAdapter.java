package com.fachhochschulebib.fhb.pruefungsplaner.utils;

import java.lang.System;

/**
 * Adapterclass for the Theme-Spinner in the options-fragment.
 * Displays the different themes with an icon, that shows the primary-and the accent color.
 *
 * @param context The Applicationcontext
 * @param resource The ResourceId
 * @param objects A list of [Theme]-Objects, that shall be displayed in the spinner
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B#\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00020\b\u00a2\u0006\u0002\u0010\tJ\"\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00062\b\u0010\r\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\"\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00062\b\u0010\r\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u000e\u001a\u00020\u000fH\u0017J\u001a\u0010\u0011\u001a\u00020\u000b2\b\u0010\u0011\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\u0006H\u0002\u00a8\u0006\u0012"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/utils/ThemeAdapter;", "Landroid/widget/ArrayAdapter;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/utils/Theme;", "context", "Landroid/content/Context;", "resource", "", "objects", "", "(Landroid/content/Context;ILjava/util/List;)V", "getDropDownView", "Landroid/view/View;", "position", "convertView", "parent", "Landroid/view/ViewGroup;", "getView", "view", "app_debug"})
public final class ThemeAdapter extends android.widget.ArrayAdapter<com.fachhochschulebib.fhb.pruefungsplaner.utils.Theme> {
    
    public ThemeAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, int resource, @org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.utils.Theme> objects) {
        super(null, 0);
    }
    
    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * [android.view.LayoutInflater#inflate]
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *       we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *       is non-null and of an appropriate type before using. If it is not possible to convert
     *       this view to display the correct data, this method can create a new view.
     *       Heterogeneous lists can specify their number of view types, so that this View is
     *       always of the right type (see [getViewTypeCount] and
     *       [getItemViewType]).
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @org.jetbrains.annotations.NotNull()
    @android.annotation.SuppressLint(value = {"ViewHolder"})
    @java.lang.Override()
    public android.view.View getView(int position, @org.jetbrains.annotations.Nullable()
    android.view.View convertView, @org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent) {
        return null;
    }
    
    /**
     * Initializes the view, passes the values to the UI-Elements.
     *
     * @param[view] The view for a Spinnerrow.
     * @param[position] The index of the item
     *
     * @return The spinnerrow, containing the initialized UI-Elements.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private final android.view.View view(android.view.View view, int position) {
        return null;
    }
    
    /**
     * Gets a [android.view.View] that displays in the drop down popup
     * the data at the specified position in the data set.
     *
     * @param position index of the item whose view we want.
     * @param convertView the old view to reuse, if possible. Note: You should
     *       check that this view is non-null and of an appropriate type before
     *       using. If it is not possible to convert this view to display the
     *       correct data, this method can create a new view.
     * @param parent the parent that this view will eventually be attached to
     * @return a [android.view.View] corresponding to the data at the
     *        specified position.
     */
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public android.view.View getDropDownView(int position, @org.jetbrains.annotations.Nullable()
    android.view.View convertView, @org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent) {
        return null;
    }
}