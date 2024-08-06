/*
 * AbstractReportPage.java     1.0     2024/08/07
 */

package com.example.b07demosummer2024.report;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.b07demosummer2024.CommonUtils;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

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

    protected abstract Task<Void> asyncGetAssociatedView(ViewCompletedCallback callback);

    /**
     * Calls the specified callback and sets the task result to be null if successful, or to be an
     * exception if a RuntimeException is thrown in the callback. This method should only
     * be called by subclasses when it is sure that the view is completed.
     *
     * @param callback the callback that handles page creation with the given view
     * @param taskProvider the task builder whose task indicates that the page is completed
     * @param createdView the created view built by subclasses of AbstractReportPage
     */
    protected void notifyListenersAndPropagateFeedback(ViewCompletedCallback callback,
                                                       TaskCompletionSource<Void> taskProvider,
                                                       View createdView) {
        try {
            callback.finishPage(createdView);
            taskProvider.setResult(null);
        } catch (RuntimeException pageCallbackException) {
            CommonUtils.logError("RuntimeException", pageCallbackException.getMessage());
            taskProvider.setException(pageCallbackException);
        }
    }

    protected void getAndSetTextView(View parent, int id, String text) {
        TextView result = parent.findViewById(id);
        result.setText(text);
    }
}
