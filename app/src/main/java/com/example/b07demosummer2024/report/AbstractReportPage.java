/*
 * AbstractReportPage.java     1.0     2024/08/07
 */

package com.example.b07demosummer2024.report;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
/**
 * Abstract class dedicated to check for pdf generation
 * Runtime exceptions and extra pdf generation features.
 */
public abstract class AbstractReportPage {

    protected Context context;

    /**
     * An interface to implement finish the PDF page when a report page's view is completed.
     * Note it is assumed after the execution of the onComplete method that the PDF page is complete.
     */
    public interface ViewCompletedCallback {
        void finishPage(View completedView);
    }

    protected AbstractReportPage(Context context) {
        this.context = context;
    }


    abstract void asyncGetAssociatedView(ViewCompletedCallback callback);

    /**
     * Calls the specified callback with the newly created view. This method should only
     *      * be called by subclasses when it is sure that the view is completed.
     * @param callback the callback that handles page creation with the given view
     * @param createdView the created view built by subclasses of AbstractReportPage
     */
    protected void notifyListenersOfCreatedView(ViewCompletedCallback callback, View createdView) {
        callback.finishPage(createdView);
    }

    protected void getAndSetTextView(View parent, int id, String text) {
        TextView result = parent.findViewById(id);
        result.setText(text);
    }
}
