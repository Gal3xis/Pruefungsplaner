package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel;

import java.lang.System;

/**
 * ViewModel for the [com.fachhochschulebib.fhb.pruefungsplaner.view.fragments.FeedbackFragment]
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J&\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\f\u00a8\u0006\r"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/FeedbackViewModel;", "Lcom/fachhochschulebib/fhb/pruefungsplaner/viewmodel/BaseViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "sendFeedBack", "", "ratingUsability", "", "ratingFunctions", "ratingStability", "text", "", "app_debug"})
public final class FeedbackViewModel extends com.fachhochschulebib.fhb.pruefungsplaner.viewmodel.BaseViewModel {
    
    public FeedbackViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    /**
     * Send feedback to the server.
     *
     * @param ratingUsability How good is the usability of the application
     * @param ratingFunctions Are there enough functions in the application
     * @param ratingStability How stable was the application, did it crashes?
     * @param text A comment from the user.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    public final void sendFeedBack(float ratingUsability, float ratingFunctions, float ratingStability, @org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
}