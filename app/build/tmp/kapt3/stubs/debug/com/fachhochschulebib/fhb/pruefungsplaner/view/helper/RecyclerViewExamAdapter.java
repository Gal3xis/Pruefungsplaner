package com.fachhochschulebib.fhb.pruefungsplaner.view.helper;

import java.lang.System;

/**
 * The [RecyclerView.Adapter] for the [RecyclerView] that holds information about all exams.
 * The information is stored in multiple [List]-Objects, each holding one kind of information for every exam that
 * needs to be displayed. E.g. the exam at position 1 gets his information from every list at index 1.
 *
 * @author Alexander Lange
 * @since 1.6
 * @see RecyclerView.Adapter
 * @see RecyclerView
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\u001cB#\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0010H\u0016J\u001c\u0010\u0013\u001a\u00020\u00142\n\u0010\u0015\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u0010H\u0016J\u001c\u0010\u0016\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0010H\u0016J\u0014\u0010\u001a\u001a\u00020\u00142\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u001bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/RecyclerViewExamAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/RecyclerViewExamAdapter$ViewHolder;", "context", "Landroid/content/Context;", "entryList", "", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;", "viewModel", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/BaseViewModel;", "(Landroid/content/Context;Ljava/util/List;Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/BaseViewModel;)V", "getEntryList", "()Ljava/util/List;", "setEntryList", "(Ljava/util/List;)V", "getItemCount", "", "getItemViewType", "position", "onBindViewHolder", "", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "updateContent", "", "ViewHolder", "app_debug"})
public final class RecyclerViewExamAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.fachhochschulebib.fhb.pruefungsplaner.view.helper.RecyclerViewExamAdapter.ViewHolder> {
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry> entryList;
    private final com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel viewModel = null;
    
    public RecyclerViewExamAdapter(@org.jetbrains.annotations.NotNull()
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
    public com.fachhochschulebib.fhb.pruefungsplaner.view.helper.RecyclerViewExamAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
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
    public final void updateContent(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry> entryList) {
    }
    
    /**
     * Initializes the [ViewHolder] with information of the viewtype. In this case,
     * passes the examinformation to the UI-Elements.
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
    com.fachhochschulebib.fhb.pruefungsplaner.view.helper.RecyclerViewExamAdapter.ViewHolder holder, int position) {
    }
    
    /**
     * Returns the amount of items in the recyclerview, based on the size of the moduleslist.
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
     * Return the view type of the item at position for the purposes of view recycling.
     * The default implementation of this method returns 0, making the assumption of a single view type for the adapter.
     * Unlike ListView adapters, types need not be contiguous.
     * Consider using id resources to uniquely identify item view types.
     *
     * @param[position] Position to query
     *
     * @return integer value identifying the type of the view needed to represent the item at position.
     * Type codes need not be contiguous.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see RecyclerView.Adapter.getItemViewType
     */
    @java.lang.Override()
    public int getItemViewType(int position) {
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
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001b\u0010\u0019\u001a\u00020\u001a2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cH\u0002\u00a2\u0006\u0002\u0010\u001eJ\u000e\u0010\u001f\u001a\u00020\u001a2\u0006\u0010 \u001a\u00020!J\u0010\u0010\"\u001a\u00020\u001a2\u0006\u0010 \u001a\u00020!H\u0002J\u001b\u0010#\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001c2\u0006\u0010 \u001a\u00020!H\u0002\u00a2\u0006\u0002\u0010$R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u0011\u0010\u0013\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\fR\u0011\u0010\u0015\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\bR\u0011\u0010\u0017\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\b\u00a8\u0006%"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/RecyclerViewExamAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "v", "Landroid/view/View;", "(Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/RecyclerViewExamAdapter;Landroid/view/View;)V", "dateTextView", "Landroid/widget/TextView;", "getDateTextView", "()Landroid/widget/TextView;", "ivicon", "Landroid/widget/ImageView;", "getIvicon", "()Landroid/widget/ImageView;", "layout", "Landroid/widget/LinearLayout;", "getLayout", "()Landroid/widget/LinearLayout;", "secondScreen", "getSecondScreen", "statusIcon", "getStatusIcon", "txtModule", "getTxtModule", "txtSecondScreen", "getTxtSecondScreen", "initFooter", "", "splitDay", "", "", "([Ljava/lang/String;)V", "set", "entry", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;", "setIcons", "splitInDays", "(Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;)[Ljava/lang/String;", "app_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        
        /**
         * TextView that shows the name of the module
         */
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView txtModule = null;
        
        /**
         * Layout that can be clicked on to show/hide the detailed information.
         */
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout layout = null;
        
        /**
         * Icon that shows if the [TestPlanEntry] is a favorite or not
         */
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView ivicon = null;
        
        /**
         * Icon that shows the status of the [TestPlanEntry]
         */
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView statusIcon = null;
        
        /**
         * TextView that shows the date of the exam
         */
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView dateTextView = null;
        
        /**
         * Layout of the second screen, that shows the detailed information. Can be made visible or gone to show/hide the deatils.
         */
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout secondScreen = null;
        
        /**
         * TextView ont the second screen that shows the datiled information.
         */
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView txtSecondScreen = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View v) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTxtModule() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getLayout() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getIvicon() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getStatusIcon() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getDateTextView() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getSecondScreen() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTxtSecondScreen() {
            return null;
        }
        
        /**
         * Function to initialize the UI-Elements for a specific [TestPlanEntry].
         *
         * @param entry The [TestPlanEntry] that contains the data to be displayed in this viewholder.
         *
         * @author Alexander Lange
         * @since 1.6
         */
        public final void set(@org.jetbrains.annotations.NotNull()
        com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry entry) {
        }
        
        /**
         * Tests if the exam before that takes place at the same time as itself.
         * If that is the case, it removes the date-indicator, so its placed under the indicator
         * of the first exam that day.
         *
         * @param[holder] The [ViewHolder] that holds the UI-Elements.
         * @param[position] The position of the item in the Recyclerview.
         * @return The day the exam takes place.
         * @author Alexander Lange
         * @since 1.6
         */
        private final java.lang.String[] splitInDays(com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry entry) {
            return null;
        }
        
        /**
         * Sets the icons for the items. Determines, the status of the exam an whether it is a favorit
         * or not and sets the corresponding colors.
         *
         * @param[holder] The [ViewHolder] that holds the UI-Elements.
         * @param[position] The position of the item in the Recyclerview.
         *
         * @author Alexander Lange
         * @since 1.6
         */
        private final void setIcons(com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry entry) {
        }
        
        /**
         * Initializes the UI-Elements, that hold deeper information about the exam.
         *
         * @param[splitDay] The day, the exam takes place.
         * @param[holder] The [ViewHolder] that holds the UI-Elements.
         * @param[position] The position of the item in the Recyclerview.
         *
         * @author Alexander Lange
         * @since 1.6
         */
        private final void initFooter(java.lang.String[] splitDay) {
        }
    }
}