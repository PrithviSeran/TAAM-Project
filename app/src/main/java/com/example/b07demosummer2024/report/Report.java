/*
 * Report.java     1.0     2024/08/07
 */

package com.example.b07demosummer2024.report;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.view.View;

import androidx.core.content.FileProvider;

import com.example.b07demosummer2024.Item;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.squareup.picasso.Picasso;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class used to generate report pdf, generate view for pdf, and
 * save pdf to app files.
 */
public class Report {
    public static final int PAGE_WIDTH = 1500;
    public static final int PAGE_HEIGHT = 2000;

    private List<Item> items;
    private boolean showDetailedInfo;
    private Context context;
    private PdfDocument report;
    private String fileSaveName;

    private int pagesGenerated = 0;
    private List<PropertyChangeListener> onPageGeneratedListeners = new ArrayList<>();

    private class TrackPdfCompletionTask implements PropertyChangeListener {
        TaskCompletionSource<Void> trackPdfCompletionTaskProvider = new TaskCompletionSource<>();

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            int generatedPages = (Integer) evt.getNewValue();
            if (generatedPages == getTotalNumberOfPages()) {
                trackPdfCompletionTaskProvider.setResult(null);
            }
        }

        public Task<Void> getTask() {
            return trackPdfCompletionTaskProvider.getTask();
        }
    }

    public Report(List<Item> items, Context context, String fileSaveName, boolean showDetailedInfo) {
        this.items = items;
        this.context = context;
        this.showDetailedInfo = showDetailedInfo;
        this.fileSaveName = fileSaveName;
    }

    /**
     * Generates a pdf and creates a task to complete it. The task being complete and successful
     * indicates all the pages have generated correctly.
     * @return A task tracking the generation of the pdf/report
     */
    protected Task<Void> generatePdf() {
        report = new PdfDocument();

        for (Item item : items) {
            ReportDataPage dataPage = new ReportDataPage(item, context, showDetailedInfo);
            dataPage.asyncGetAssociatedView(addPageToDocument());
        }

        TrackPdfCompletionTask trackPdfCompletion = new TrackPdfCompletionTask();
        addPageGeneratedListener(trackPdfCompletion);
        return trackPdfCompletion.getTask();
    }

    /**
     * Returns a callback that add a page to the PDFDocument global instance.
     * @return a callback that adds the view as the next page
     */
    private AbstractReportPage.ViewCompletedCallback addPageToDocument() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        return completedView -> {
            getPictureFromView(completedView).addOnSuccessListener(service, (pic) -> {
                PdfDocument.PageInfo info = getPageInfoForPageNum(preIncrementPagesGeneratedCounter());
                PdfDocument.Page newPage = report.startPage(info);
                Canvas canvas = newPage.getCanvas();
                canvas.drawPicture(pic);
                report.finishPage(newPage);
                service.shutdown();
            });
        };
    }

    private PdfDocument.PageInfo getPageInfoForPageNum(int pageNum) {
        return new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNum).create();
    }

    /**
     * Saves the pdf in a default location which under the app directory.
     */
    protected Task<Void> savePDF() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        TaskCompletionSource<Void> savePdfTaskProvider = new TaskCompletionSource<>();
        service.submit(() -> {
            File saveFile = new File(context.getFilesDir(), fileSaveName);
            try (FileOutputStream fileStream = new FileOutputStream(saveFile)) {
                report.writeTo(fileStream);
                savePdfTaskProvider.setResult(null);
            } catch (IOException e) {
                savePdfTaskProvider.setException(e);
            }
            finally {
                report.close();
                service.shutdown();
            }
        });
        return savePdfTaskProvider.getTask();
    }

    public Task<Uri> getUriOfSavePath() {
        TaskCompletionSource<Uri> getUriTaskProvider = new TaskCompletionSource<>();
        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(() -> {
            File saveFile = new File(context.getFilesDir(), fileSaveName);
            String authority = context.getPackageName() + ".provider";
            Uri fileUri = FileProvider.getUriForFile(context, authority, saveFile);
            getUriTaskProvider.setResult(fileUri);
            service.shutdown();
        });
        return getUriTaskProvider.getTask();
    }

    private Task<Picture> getPictureFromView(View v) {
        TaskCompletionSource<Picture> getPictureTaskProvider = new TaskCompletionSource<>();
        v.measure(
                View.MeasureSpec.makeMeasureSpec(
                        PAGE_WIDTH, View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(
                        PAGE_HEIGHT, View.MeasureSpec.EXACTLY
                )
        );

        Picture pic = new Picture();
        Canvas c = pic.beginRecording(v.getMeasuredWidth(), v.getMeasuredHeight());
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        pic.endRecording();
        getPictureTaskProvider.setResult(pic);
        return getPictureTaskProvider.getTask();
    }

    public void addPageGeneratedListener(PropertyChangeListener listener) {
        if (listener != null) {
            onPageGeneratedListeners.add(listener);
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
    private int preIncrementPagesGeneratedCounter() {
        firePropertyChange(pagesGenerated, pagesGenerated + 1);
        return ++pagesGenerated;
    }

    private void firePropertyChange(Object oldValue, Object newValue) {
        for (PropertyChangeListener listener : onPageGeneratedListeners) {
            listener.propertyChange(new PropertyChangeEvent(this,
                    "pagesGenerated", oldValue, newValue));

        }
    }
}
