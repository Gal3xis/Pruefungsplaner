package com.fachhochschulebib.fhb.pruefungsplaner.utils;

import java.lang.System;

/**
 * Utility-Function for the application.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\u000b\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J,\u0010\u0010\u001a\u00020\u00062\b\b\u0001\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u0005J\u0010\u0010\u0017\u001a\u00020\u00062\b\u0010\u0018\u001a\u0004\u0018\u00010\nJ\u0010\u0010\u0019\u001a\u00020\n2\b\u0010\u0018\u001a\u0004\u0018\u00010\nJ\u0018\u0010\u001a\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u00132\b\b\u0001\u0010\u001b\u001a\u00020\u0006R\u001d\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001d\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00060\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\bR\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00060\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u001c"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/utils/Utils;", "", "()V", "favoriteIcons", "", "", "", "getFavoriteIcons", "()Ljava/util/Map;", "statusColors", "", "getStatusColors", "themeList", "", "getThemeList", "()Ljava/util/List;", "getColorFromAttr", "attrColor", "context", "Landroid/content/Context;", "typedValue", "Landroid/util/TypedValue;", "resolveRes", "getExamDuration", "examForm", "getExamForm", "readTextFile", "textResource", "app_debug"})
public final class Utils {
    @org.jetbrains.annotations.NotNull()
    public static final com.fachhochschulebib.fhb.pruefungsplaner.utils.Utils INSTANCE = null;
    
    /**
     * A map, where an Entry-Status is mapped to a specific color
     */
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Map<java.lang.String, java.lang.Integer> statusColors = null;
    
    /**
     * A map, where the icons for favorite/not favorite entry are defined
     */
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Map<java.lang.Boolean, java.lang.Integer> favoriteIcons = null;
    
    /**
     * A list of all available themes
     */
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.Integer> themeList = null;
    
    private Utils() {
        super();
    }
    
    /**
     * Takes an Attribute-ID for a color and the current theme and returns the color for that id.
     * Provides access to the colorscheme, so they can be assigned dynamically in code.
     *
     * @param[attrColor] The id of the Color (R.attr.*)
     * @param[context] The context (not applicationcontext!!!)
     * @param[typedValue] Stores the data of the resolved attribute, does not need to be given
     * @param[resolveRes] If true, resource references will be walked; if false, outValue may be a TYPE_REFERENCE. In either case, it will never be a TYPE_ATTRIBUTE.
     *
     * @return The color as integer, that is associated with the given id.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final int getColorFromAttr(@androidx.annotation.AttrRes()
    int attrColor, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.util.TypedValue typedValue, boolean resolveRes) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Integer> getStatusColors() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.Boolean, java.lang.Integer> getFavoriteIcons() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Integer> getThemeList() {
        return null;
    }
    
    /**
     * Reads a textfile in the Raw-Folder and returns the content as a string.
     *
     * @param[context] The context of the application
     * @param[textResource] The id of the textfile (R.raw.*)
     *
     * @return The content of the textfile as string
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String readTextFile(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @androidx.annotation.RawRes()
    int textResource) {
        return null;
    }
    
    /**
     * Returns the duration for a given exam. Necessary because the duration information
     * is transferred via the examForm.
     *
     * @param[examForm] The exam form that holds information about the duration.
     *
     * @return The duration for the exam in minutes. Returns zero if it doesn't find the information in the input.
     *
     * @author Alexander Lange
     */
    public final int getExamDuration(@org.jetbrains.annotations.Nullable()
    java.lang.String examForm) {
        return 0;
    }
    
    /**
     * Returns the form of the exam, separated from the duration.
     *
     * @param examForm The exam form from the [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry]
     *
     * @return The exam form, separated from the duration
     *
     * @author Alexander Lange
     * @since 1.6
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getExamForm(@org.jetbrains.annotations.Nullable()
    java.lang.String examForm) {
        return null;
    }
}