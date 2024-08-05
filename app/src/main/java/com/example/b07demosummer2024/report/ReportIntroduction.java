package com.example.b07demosummer2024.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.example.b07demosummer2024.R;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

public class ReportIntroduction extends AbstractReportPage {
    protected ReportIntroduction(Context context) {
        super(context);
    }

    @Override
    protected Task<Void> asyncGetAssociatedView(ViewCompletedCallback callback) {
        LayoutInflater inf = LayoutInflater.from(context);

        View introView = inf.inflate(R.layout.pdf_intro_page, null);
        TaskCompletionSource<Void> pageCompletionTaskProvider = new TaskCompletionSource<>();
        notifyListenersAndPropagateFeedback(callback, pageCompletionTaskProvider, introView);

        return pageCompletionTaskProvider.getTask();
    }

}
