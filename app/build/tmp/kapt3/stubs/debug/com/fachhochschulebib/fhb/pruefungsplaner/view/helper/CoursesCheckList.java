package com.fachhochschulebib.fhb.pruefungsplaner.view.helper;

import java.lang.System;

/**
 * Adapter-Class for the Recyclerview in the AddCourseFragment-Class
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 *
 * @see AddCourseFragment
 * @see RecyclerView.Adapter
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\u001eB#\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u0010\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0011H\u0016J\u001c\u0010\u0014\u001a\u00020\u00152\n\u0010\u0016\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u0011H\u0016J\u001c\u0010\u0017\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0011H\u0016J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0013\u001a\u00020\u0011H\u0002J\u0014\u0010\u001d\u001a\u00020\u00152\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/CoursesCheckList;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/CoursesCheckList$ViewHolder;", "courseList", "", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/Course;", "viewModel", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/BaseViewModel;", "context", "Landroid/content/Context;", "(Ljava/util/List;Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/BaseViewModel;Landroid/content/Context;)V", "getCourseList", "()Ljava/util/List;", "setCourseList", "(Ljava/util/List;)V", "getChosen", "getItemCount", "", "getItemViewType", "position", "onBindViewHolder", "", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "toggleFavorite", "", "updateContent", "ViewHolder", "app_debug"})
public final class CoursesCheckList extends androidx.recyclerview.widget.RecyclerView.Adapter<com.fachhochschulebib.fhb.pruefungsplaner.view.helper.CoursesCheckList.ViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course> courseList;
    private final com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel viewModel = null;
    private final android.content.Context context = null;
    
    public CoursesCheckList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course> courseList, @org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel viewModel, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course> getCourseList() {
        return null;
    }
    
    public final void setCourseList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course> p0) {
    }
    
    /**
     * Returns a list of all chosen courses in the recyclerview
     *
     * @return A list of all chosen courses.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course> getChosen() {
        return null;
    }
    
    /**
     * Updates the content of the recyclerview with a new List of courses.
     * Replaces the current list with the new list.
     *
     * @param courseList The list of courses to be shown by the recyclerview.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void updateContent(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course> courseList) {
    }
    
    /**
     * Called when Recyclerview needs a new ViewHolder.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *              an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see RecyclerView
     * @see RecyclerView.Adapter
     * @see RecyclerView.Adapter.onCreateViewHolder
     */
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.fachhochschulebib.fhb.pruefungsplaner.view.helper.CoursesCheckList.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    /**
     * Displays the data at a specific position.
     *
     * @param[holder] The ViewHolder which should be updated to represent the contents of the
     *       item at the given position in the data set.
     * @param[position] The position of the item within the adapter's data set.
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see RecyclerView
     * @see RecyclerView.Adapter
     * @see RecyclerView.Adapter.onBindViewHolder
     */
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.view.helper.CoursesCheckList.ViewHolder holder, int position) {
    }
    
    /**
     * Selects/Unselects a course as favorite.
     *
     * @param[position] The position of the course in the list.
     *
     * @return true if the course was set as favorite,false if the course is no longer a favorite.
     * @author Alexander Lange
     * @since 1.6
     */
    private final boolean toggleFavorite(int position) {
        return false;
    }
    
    /**
     * Returns the size of the courseList.
     *
     * @return the size of the courselist
     *
     * @author Alexander Lange
     * @since 1.6
     * @see RecyclerView
     * @see RecyclerView.Adapter
     * @see RecyclerView.Adapter.getItemCount
     */
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     *
     * @param[position] position to query
     * @return integer value identifying the type of the view needed to represent the item at
     *                <code>position</code>. Type codes need not be contiguous.
     * @author Alexander Lange
     * @since 1.6
     * @see RecyclerView
     * @see RecyclerView.Adapter
     * @see RecyclerView.Adapter.getItemViewType
     */
    @java.lang.Override()
    public int getItemViewType(int position) {
        return 0;
    }
    
    /**
     * Inner class to provide a reference to the views for each data item
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\r"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/CoursesCheckList$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "v", "Landroid/view/View;", "(Lcom/fachhochschulebib/fhb/pruefungsplaner/view/helper/CoursesCheckList;Landroid/view/View;)V", "checkBoxCourse", "Landroid/widget/CheckBox;", "getCheckBoxCourse", "()Landroid/widget/CheckBox;", "nameCourse", "Landroid/widget/TextView;", "getNameCourse", "()Landroid/widget/TextView;", "app_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView nameCourse = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.CheckBox checkBoxCourse = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View v) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getNameCourse() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.CheckBox getCheckBoxCourse() {
            return null;
        }
    }
}