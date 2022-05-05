package com.fachhochschulebib.fhb.pruefungsplaner.utils;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 2, d1 = {"\u0000H\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a$\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0001H\u0002\u001a-\u0010\u0007\u001a\u00020\u0005\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\t2\b\u0010\n\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u0002H\b\u00a2\u0006\u0002\u0010\f\u001a\u0012\u0010\r\u001a\u00020\u000e*\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011\u001a\u0012\u0010\u0012\u001a\u00020\u0013*\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0014\u001a\n\u0010\u0016\u001a\u00020\u000e*\u00020\u000f\u001a\u0012\u0010\u0017\u001a\u00020\u0001*\u00020\u00182\u0006\u0010\u0002\u001a\u00020\u0003\u001a\u0014\u0010\u0019\u001a\u00020\u000e*\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0001\u00a8\u0006\u001c"}, d2 = {"createStringWithLabel", "", "context", "Landroid/content/Context;", "label", "", "info", "add", "E", "", "position", "item", "(Ljava/util/List;Ljava/lang/Integer;Ljava/lang/Object;)I", "applySettings", "", "Landroidx/appcompat/app/AppCompatActivity;", "viewModel", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/BaseViewModel;", "atDay", "", "Ljava/util/Date;", "date", "closeApp", "getString", "Lcom/fachhochschulebib/fhb/pruefungsplaner/model/room/TestPlanEntry;", "setSelection", "Landroid/widget/Spinner;", "value", "app_debug"})
public final class ExtensionsKt {
    
    /**
     * Extension-Function for the [Spinner]. Lets the user set the selection with the text-content of the spinner
     * instead of the position.
     *
     * @param[value] The text, the spinner is showing at the wanted position.
     * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
     * @since 1.6
     * @see Spinner
     */
    public static final void setSelection(@org.jetbrains.annotations.NotNull()
    android.widget.Spinner $this$setSelection, @org.jetbrains.annotations.Nullable()
    java.lang.String value) {
    }
    
    /**
     * Returns formatted details for a [TestPlanEntry].
     *
     * @param[context] The application context
     *
     * @return A Formatted String, including the details for the exam.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String getString(@org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry $this$getString, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    /**
     * Puts together a label from the strings-resources and an associated value. (E.g. Modul:Datenbankanwendungen)
     *
     * @param[context] The application context.
     * @param[label] The Resources-Id of the label.
     * @param[info] The value, associated with the label.
     *
     * @return A String, that displays the label and the info.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    private static final java.lang.String createStringWithLabel(android.content.Context context, @androidx.annotation.StringRes()
    int label, java.lang.String info) {
        return null;
    }
    
    /**
     * Extension-function for the [Date]-Class, which checks if a given date is at the same day as the current date.
     *
     * @param[date] The date which should be compared.
     *
     * @return true-> both dates are at the same day,false->both dates are not at the same day.
     *
     * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
     * @since 1.6
     * @see Date
     */
    @kotlin.Suppress(names = {"DEPRECATION"})
    public static final boolean atDay(@org.jetbrains.annotations.NotNull()
    java.util.Date $this$atDay, @org.jetbrains.annotations.NotNull()
    java.util.Date date) {
        return false;
    }
    
    /**
     * Applies Settings from sharedPreferences to the activity.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public static final void applySettings(@org.jetbrains.annotations.NotNull()
    androidx.appcompat.app.AppCompatActivity $this$applySettings, @org.jetbrains.annotations.NotNull()
    com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel viewModel) {
    }
    
    /**
     * Closes the Application. First start a dialog to ask the user if the app shall be closed.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public static final void closeApp(@org.jetbrains.annotations.NotNull()
    androidx.appcompat.app.AppCompatActivity $this$closeApp) {
    }
    
    /**
     * Adds an item to a given position, or, if the position is null, appends the item at the end of the list.
     *
     * @param[position] The position, where the item shall be inserted. Can be null
     * @param[item] The item to be inserted
     *
     * @return The position, where the item was inserted.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public static final <E extends java.lang.Object>int add(@org.jetbrains.annotations.NotNull()
    java.util.List<E> $this$add, @org.jetbrains.annotations.Nullable()
    java.lang.Integer position, E item) {
        return 0;
    }
}