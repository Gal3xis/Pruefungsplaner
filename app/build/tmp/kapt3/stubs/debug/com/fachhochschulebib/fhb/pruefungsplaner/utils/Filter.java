package com.fachhochschulebib.fhb.pruefungsplaner.utils;

import java.lang.System;

/**
 * Inner Class to filter the table of moduls. Used by TermineFragment-fragment and FavoritenFragment-fragment.
 *
 * @author Alexander Lange
 * @since 1.6
 * @see Terminefragment
 * @see Favoritenfragment
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010&\u001a\u00020\u0019H\u0002J\u0006\u0010\'\u001a\u00020\u0019J\u0016\u0010#\u001a\u00020\u00192\u0006\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\u001fJ\u0010\u0010+\u001a\u00020\u001f2\b\u0010,\u001a\u0004\u0018\u00010-J\u001a\u0010.\u001a\b\u0012\u0004\u0012\u00020-0/2\f\u00100\u001a\b\u0012\u0004\u0012\u00020-0/R(\u0010\u0005\u001a\u0004\u0018\u00010\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR(\u0010\u000b\u001a\u0004\u0018\u00010\n2\b\u0010\u0003\u001a\u0004\u0018\u00010\n@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR(\u0010\u0010\u001a\u0004\u0018\u00010\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0007\"\u0004\b\u0012\u0010\tR(\u0010\u0013\u001a\u0004\u0018\u00010\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0007\"\u0004\b\u0015\u0010\tR&\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00190\u00180\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR2\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001f0\u001e2\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u001f0\u001e@FX\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010%\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$\u00a8\u00061"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/utils/Filter;", "", "()V", "value", "", "courseName", "getCourseName", "()Ljava/lang/String;", "setCourseName", "(Ljava/lang/String;)V", "Ljava/util/Date;", "datum", "getDatum", "()Ljava/util/Date;", "setDatum", "(Ljava/util/Date;)V", "examiner", "getExaminer", "setExaminer", "modulName", "getModulName", "setModulName", "onFilterChangedListener", "", "Lkotlin/Function0;", "", "getOnFilterChangedListener", "()Ljava/util/List;", "setOnFilterChangedListener", "(Ljava/util/List;)V", "", "", "semester", "getSemester", "()[Ljava/lang/Boolean;", "setSemester", "([Ljava/lang/Boolean;)V", "[Ljava/lang/Boolean;", "filterChanged", "reset", "pSemester", "", "active", "validateFilter", "entry", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;", "validateList", "", "list", "app_debug"})
public final class Filter {
    @org.jetbrains.annotations.NotNull()
    public static final com.fachhochschulebib.fhb.pruefungsplaner.utils.Filter INSTANCE = null;
    
    /**
     * Parameter to Filter with the Modulename.
     * Calls the [onModuleNameChangedListener] and the [onFilterChangedListener].
     *
     * @author Alexander Lange
     * @since 1.6
     * @see onModulNameChangedListener
     * @see onFilterChangedListener
     */
    @org.jetbrains.annotations.Nullable()
    private static java.lang.String modulName;
    
    /**
     * Parameter to Filter with the Coursename.
     * Calls the [onCourseNameChangedListener] and the [onFilterChangedListener].
     *
     * @author Alexander Lange
     * @since 1.6
     * @see onCourseNameChangedListener
     * @see onFilterChangedListener
     */
    @org.jetbrains.annotations.Nullable()
    private static java.lang.String courseName;
    
    /**
     * Parameter to Filter with a specific date.
     * Calls the  [onFilterChangedListener].
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see onFilterChangedListener
     */
    @org.jetbrains.annotations.Nullable()
    private static java.util.Date datum;
    
    /**
     * Parameter to filter with a specific examiner.
     * Calls the [onExaminerChangedListener] and [onFilterChangedListener].
     *
     * @author Alexander Lange
     * @since 1.6
     *
     * @see onFilterChangedListener
     */
    @org.jetbrains.annotations.Nullable()
    private static java.lang.String examiner;
    
    /**
     * Array of 6 semester, where each field contains a boolean, if the semester is selected (true), or not (false)
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    private static java.lang.Boolean[] semester = {true, true, true, true, true, true};
    
    /**
     * List of functions, that shall be invoked, when the filter changes.
     */
    @org.jetbrains.annotations.NotNull()
    private static java.util.List<kotlin.jvm.functions.Function0<kotlin.Unit>> onFilterChangedListener;
    
    private Filter() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getModulName() {
        return null;
    }
    
    public final void setModulName(@org.jetbrains.annotations.Nullable()
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCourseName() {
        return null;
    }
    
    public final void setCourseName(@org.jetbrains.annotations.Nullable()
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.Date getDatum() {
        return null;
    }
    
    public final void setDatum(@org.jetbrains.annotations.Nullable()
    java.util.Date value) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getExaminer() {
        return null;
    }
    
    public final void setExaminer(@org.jetbrains.annotations.Nullable()
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.Boolean[] getSemester() {
        return null;
    }
    
    public final void setSemester(@org.jetbrains.annotations.NotNull()
    java.lang.Boolean[] value) {
    }
    
    /**
     * Public method to set the value for a specific semester.
     * Calls the [onSemesterChangedListener] and the [onFilterChangedListener]
     * @param[pSemester] The semester to set the value.
     * @param[active] If the semester is checked or not.
     * @author Alexander Lange
     * @since 1.6
     * @see onSemesterChangedListener
     * @see onFilterChangedListener
     */
    public final void setSemester(int pSemester, boolean active) {
    }
    
    /**
     * Invokes every Method, appended to the onFilterChangedListener.
     *
     * @author Alexander Lange
     * @since 1.6
     * @see onFilterChangedListener
     */
    private final void filterChanged() {
    }
    
    /**
     * Validates a testplanentry-Object. Checks if all Filter-values agree with the given entry.
     *
     * @param[context] Current context
     * @param[entry] The Entry that needs to be validated
     * @return true->The entry agrees with the filter,false->the entry does not agree with the filter
     * @author Alexander Lange
     * @since 1.6
     */
    public final boolean validateFilter(@org.jetbrains.annotations.Nullable()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry entry) {
        return false;
    }
    
    /**
     * Validates a list of [TestPlanEntry]-Objects. Checks if all Filter-values agree with the given entry.
     *
     * @param list The list, that needs to be checked
     *
     * @return A Filtered list with only [TestPlanEntry]-Objects, that fit to the current filter
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry> validateList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry> list) {
        return null;
    }
    
    /**
     * Resets the Filter, sets every value to null.
     * Calls the onResetListener.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void reset() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<kotlin.jvm.functions.Function0<kotlin.Unit>> getOnFilterChangedListener() {
        return null;
    }
    
    public final void setOnFilterChangedListener(@org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.jvm.functions.Function0<kotlin.Unit>> p0) {
    }
}