package com.fachhochschulebib.fhb.pruefungsplaner.view.helper;

import java.lang.System;

/**
 * The [RecyclerView.Adapter] for the [RecyclerView] that holds information about the favorite exams.
 * The information is stored in multiple [List]-Objects, each holding one kind of information for every exam that
 * needs to be displayed. E.g. the exam at position 1 gets his information from every list at index 1.
 *
 * @author Alexander Lange
 * @since 1.6
 * @see RecyclerView.Adapter
 * @see RecyclerView
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\u001eB#\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u001d\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0013\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\u0014J\b\u0010\u0015\u001a\u00020\u0012H\u0016J\u001c\u0010\u0016\u001a\u00020\u00102\n\u0010\u0017\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u001c\u0010\u0018\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0012H\u0016J\u0016\u0010\u001c\u001a\u00020\u00102\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u001dH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/RecyclerViewFavoriteAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/RecyclerViewFavoriteAdapter$ViewHolder;", "context", "Landroid/content/Context;", "entryList", "", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;", "viewModel", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/BaseViewModel;", "(Landroid/content/Context;Ljava/util/List;Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/BaseViewModel;)V", "getEntryList", "()Ljava/util/List;", "setEntryList", "(Ljava/util/List;)V", "add", "", "position", "", "entry", "(Ljava/lang/Integer;Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;)V", "getItemCount", "onBindViewHolder", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "updateContent", "", "ViewHolder", "app_debug"})
public final class RecyclerViewFavoriteAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.fachhochschulebib.fhb.pruefungsplaner.view.helper.RecyclerViewFavoriteAdapter.ViewHolder> {
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry> entryList;
    private final com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel viewModel = null;
    
    public RecyclerViewFavoriteAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry> entryList, @org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel viewModel) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry> getEntryList() {
        return null;
    }
    
    public final void setEntryList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry> p0) {
    }
    
    /**
     * Adds an item to the recyclerview.
     *
     * @param[position] The position, where the item needs to be inserted.
     * @param[entry] The [TestPlanEntry] of the item, that needs to be inserted.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void add(@org.jetbrains.annotations.Nullable()
    java.lang.Integer position, @org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry entry) {
    }
    
    /**
     * Inflates the view that shows the information for the passed viewType. In this case the information
     * about the exam.
     *
     * @param[parent] The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param[viewType] The view type of the new View.
     * @return The [ViewHolder], that shows the information.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see RecyclerView.Adapter.onCreateViewHolder
     */
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.fachhochschulebib.fhb.pruefungsplaner.view.helper.RecyclerViewFavoriteAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    /**
     * Initializes the [ViewHolder] with information of the view type. In this case,
     * passes the exam information to the UI-Elements.
     *
     * @param[holder] The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param[position] The position of the item within the adapter's data set.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see RecyclerView.Adapter.onBindViewHolder
     */
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.view.helper.RecyclerViewFavoriteAdapter.ViewHolder holder, int position) {
    }
    
    /**
     * Updates the content of the recyclerview with a new List of courses.
     * Replaces the current list with the new list.
     *
     * @param entryList The list of [TestPlanEntry]-Objects to be shown by the recyclerview.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @android.annotation.SuppressLint(value = {"NotifyDataSetChanged"})
    public final void updateContent(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry> entryList) {
    }
    
    /**
     * Returns the amount of items in the recyclerview
     *
     * @return The amount of items in the recyclerview.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see RecyclerView.Adapter.getItemCount
     */
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    /**
     * Inner class [ViewHolder], that contains the references to the UI-Elements.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see RecyclerView.ViewHolder
     */
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0007J\u0010\u0010\u001a\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0002R\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \u0007*\u0004\u0018\u00010\t0\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n \u0007*\u0004\u0018\u00010\u000b0\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\n \u0007*\u0004\u0018\u00010\t0\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\n \u0007*\u0004\u0018\u00010\t0\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n \u0007*\u0004\u0018\u00010\t0\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n \u0007*\u0004\u0018\u00010\t0\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0010\u001a\n \u0007*\u0004\u0018\u00010\u00110\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015\u00a8\u0006\u001b"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/RecyclerViewFavoriteAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "layout", "Landroid/view/View;", "(Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/RecyclerViewFavoriteAdapter;Landroid/view/View;)V", "card", "Lcom/google/android/material/card/MaterialCardView;", "kotlin.jvm.PlatformType", "expandedTextView", "Landroid/widget/TextView;", "expansion", "Landroid/widget/RelativeLayout;", "headerCourse", "headerDate", "headerModule", "headerTime", "iconInCalendar", "Landroid/widget/ImageView;", "getLayout", "()Landroid/view/View;", "setLayout", "(Landroid/view/View;)V", "set", "", "entry", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;", "showContextMenu", "app_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private android.view.View layout;
        
        /**
         * Card that shows all information. Can be clicked on to show/hide detailed information.
         */
        private com.google.android.material.card.MaterialCardView card;
        
        /**
         * TextView that shows the name of the module.
         */
        private android.widget.TextView headerModule;
        
        /**
         * TextView that shows the name of the course.
         */
        private android.widget.TextView headerCourse;
        
        /**
         * TextView that shows the date of the exam.
         */
        private android.widget.TextView headerDate;
        
        /**
         * TextView that shows the timespan of the exam (eg. 08:00-09:30)
         */
        private android.widget.TextView headerTime;
        
        /**
         * TextView that shows the detailed information
         */
        private android.widget.TextView expandedTextView;
        
        /**
         * Layout that can be expanded to show the detailed information
         */
        private android.widget.RelativeLayout expansion;
        
        /**
         * Icon that shows, if the [TestPlanEntry] is currently placed in the calendar
         */
        private android.widget.ImageView iconInCalendar;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View layout) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.view.View getLayout() {
            return null;
        }
        
        public final void setLayout(@org.jetbrains.annotations.NotNull()
        android.view.View p0) {
        }
        
        /**
         * Function to initialize the UI-Elements for a specific [TestPlanEntry].
         *
         * @param entry The [TestPlanEntry] that contains the data to be displayed in this view holder.
         *
         * @author Alexander Lange
         * @since 1.6
         */
        @android.annotation.SuppressLint(value = {"SetTextI18n"})
        public final void set(@org.jetbrains.annotations.NotNull()
        com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry entry) {
        }
        
        /**
         * Displays a menu, that lets the user remove this [TestPlanEntry] from his favorites or delete this [TestPlanEntry] from the calendar.
         *
         * @author Alexander Lange
         * @since 1.6
         */
        private final void showContextMenu(com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry entry) {
        }
    }
}