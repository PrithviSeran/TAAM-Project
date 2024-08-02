package com.example.b07demosummer2024.report;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.view.View;

import com.example.b07demosummer2024.Item;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Report {
    public static final int pageWidth = 2000;
    public static final int pageHeight = 2000;
    private List<Item> items;
    private Context context;
    private PdfDocument report;
    private int currentPage = 1;

    public Report(List<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public Task<Void> generatePdf() {
        report = new PdfDocument();

        List<Task<Boolean>> pageFinishTasks = new ArrayList<>();
        for (Item item : items) {
            ReportDataPage dataPage = new ReportDataPage(item, context);
            Task<Boolean> pageFinishTask = dataPage.asyncSetAssociatedView(addPageToDocument());
            pageFinishTasks.add(pageFinishTask);
        }
        return Tasks.whenAll(pageFinishTasks);
    }

    private ReportDataPage.PageCompletedCallback addPageToDocument() {
        return completedPage -> {
            PdfDocument.PageInfo info = getPageInfoForPageNum(currentPage++);
            PdfDocument.Page newPage = report.startPage(info);
            Bitmap bit = getBitmapFromView(completedPage);
            newPage.getCanvas().drawBitmap(bit, 0f, 0f, null);
            report.finishPage(newPage);
        };
    }

    /** @noinspection IOStreamConstructor*/
    public void savePDF(String fileName) throws IOException {
        File f = new File(context.getFilesDir(), "report");
        if (!f.exists()) {
            f.mkdir();
        }
        File created = new File(f, fileName);
        report.writeTo(new FileOutputStream(created));
        report.close();
    }

    private Bitmap getBitmapFromView(View v) {
        v.measure(
                View.MeasureSpec.makeMeasureSpec(
                        pageWidth, View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(
                        pageHeight, View.MeasureSpec.EXACTLY
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
        return new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum).create();
    }
}
