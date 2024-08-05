package com.example.b07demosummer2024.report;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.core.content.FileProvider;

import com.example.b07demosummer2024.CommonUtils;
import com.example.b07demosummer2024.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.squareup.picasso.Picasso;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Report {
    public static final int PAGE_WIDTH = 2000;
    public static final int PAGE_HEIGHT = 2000;

    private List<Item> items;
    private boolean showDetailedInfo;
    private Context context;
    private PdfDocument report;
    private final File saveFile;
    private final File saveFolder;

    private int pagesGenerated = 1;
    private List<PropertyChangeListener> pageGeneratedListeners = new ArrayList<>();
    private AbstractReportPage.ViewCompletedCallback addPageToDocument = null;

    public Report(List<Item> items, Context context, String fileSaveName, boolean showDetailedInfo) {
        this.items = items;
        this.context = context;
        this.showDetailedInfo = showDetailedInfo;

        this.saveFolder = context.getFilesDir();
        this.saveFile = new File(saveFolder, fileSaveName);
    }

//    protected Task<Task<Void>> generatePdf() {
//        report = new PdfDocument();
//
//        // idea is to generate each page, then wait until the page is done, then ...
//        ReportIntroduction introPage = new ReportIntroduction(context);
//        Task<Void> getIntroTask = introPage.asyncGetAssociatedView(addPageToDocument());
//        TaskCompletionSource<Task<Void>> finishAllDataPagesTaskProvider = new TaskCompletionSource<>();
//        getIntroTask.addOnSuccessListener((Void) -> {
//            List<Task<Void>> dataPageFinishTasks = new ArrayList<>();
//            for (Item item : items) {
//                ReportDataPage dataPage = new ReportDataPage(item, context, showDetailedInfo);
//                Task<Void> pageFinishTask = dataPage.asyncGetAssociatedView(addPageToDocument());
//                dataPageFinishTasks.add(pageFinishTask);
//            }
//            finishAllDataPagesTaskProvider.setResult(Tasks.whenAll(dataPageFinishTasks));
//        });
//        return finishAllDataPagesTaskProvider.getTask();
//    }

    /**
     * Generates a pdf and creates a task to complete it. The task being complete and successful
     * indicates all the pages have generated correctly.
     * @return A task tracking the generation of the pdf/report
     */
    protected Task<Void> generatePdf() {
        report = new PdfDocument();

        List<Task<Void>> dataPageFinishTasks = new ArrayList<>();
        for (Item item : items) {
            ReportDataPage dataPage = new ReportDataPage(item, context, showDetailedInfo);
            Task<Void> pageFinishTask = dataPage.asyncGetAssociatedView(addPageToDocument());
            dataPageFinishTasks.add(pageFinishTask);
        }

        return Tasks.whenAll(dataPageFinishTasks);
    }

    /**
     * Returns a callback that add a page to the PDFDocument global instance.
     * @return a callback that adds the view as the next page
     */
    private AbstractReportPage.ViewCompletedCallback addPageToDocument() {
        if (addPageToDocument == null) {
            addPageToDocument = completedView -> {
                PdfDocument.PageInfo info = getPageInfoForPageNum(postIncrementPagesGeneratedCounter());
                PdfDocument.Page newPage = report.startPage(info);
                Bitmap bit = getBitmapFromView(completedView);
                newPage.getCanvas().drawBitmap(bit, 0f, 0f, null);
                report.finishPage(newPage);
            };
        }
        return addPageToDocument;
    }

    /**
     * Saves the pdf in a default location which under the app directory.
     * @throws IOException if there is an error that occurs while writing
     */
    protected void savePDF() throws IOException {
        try (FileOutputStream fileStream = new FileOutputStream(saveFile)) {
            report.writeTo(fileStream);
        } finally {
            report.close();
        }
    }

    public Uri getUriOfSavePath() {
        String authority = context.getPackageName() + ".provider";
        return FileProvider.getUriForFile(context, authority, saveFile);
    }

    private Bitmap getBitmapFromView(View v) {
        v.measure(
                View.MeasureSpec.makeMeasureSpec(
                        PAGE_WIDTH, View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(
                        PAGE_HEIGHT, View.MeasureSpec.EXACTLY
                )
        );

        Bitmap bit = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bit);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);

        return bit;
    }

    private PdfDocument.PageInfo getPageInfoForPageNum(int pageNum) {
        return new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNum).create();
    }

    public void addPageGeneratedListener(PropertyChangeListener listener) {
        if (listener != null) {
            pageGeneratedListeners.add(listener);
        } else {
            throw new NullPointerException("Listener cannot be null");
        }
    }

    public int getTotalNumberOfPages() {
        return items.size(); // one page is intro, the other is (maybe) summary
    }

    /**
     * Increments the pages generated counter and notifies all listeners
     * about the change.
     * @return the initial value of the field pagesGenerated
     */
    private int postIncrementPagesGeneratedCounter() {
        firePropertyChange(pagesGenerated, pagesGenerated + 1);
        return pagesGenerated++;
    }

    private void firePropertyChange(Object oldValue, Object newValue) {
        for (PropertyChangeListener listener : pageGeneratedListeners) {
            listener.propertyChange(new PropertyChangeEvent(this,
                    "pagesGenerated", oldValue, newValue));

        }
    }
}
