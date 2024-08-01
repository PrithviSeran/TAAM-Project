package com.example.b07demosummer2024.report;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.view.View;

import com.example.b07demosummer2024.Item;
import com.example.b07demosummer2024.firebase.ImageFetcher;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Report {
    public static final int pageWidth = 2000;
    public static final int pageHeight = 2000;
    private List<Item> items;
    private Context context;


    private ExecutorService service = null;

    public Report(List<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public void generateAndSavePdf() throws IOException {
        List<Future<Bitmap>> pendingRequests = requestImages();
        ArrayList<ReportDataPage> dataPages = new ArrayList<>();
        ArrayList<PdfDocument.Page> pdfPages = new ArrayList<>();

        PdfDocument doc = new PdfDocument();
        PdfDocument.PageInfo b = new PdfDocument.PageInfo.Builder(
                pageWidth, pageHeight, 1).create();

        for (Item item : items) {
            PdfDocument.Page page = doc.startPage(b);
            ReportDataPage dataPage = new ReportDataPage(item, context);
            dataPages.add(dataPage);
            pdfPages.add(page);
        }

        for (int i = 0; i < pendingRequests.size(); i++) {
            Future<Bitmap> request = pendingRequests.get(i);
            while (true) {
                ReportDataPage dataPage = dataPages.get(i);
                PdfDocument.Page page = pdfPages.get(i);
                try {
                    if (request.isDone()) {
                        dataPage.setAssociatedView(request.get());
                        doc.finishPage(page);
                        break;
                    }
                } catch (ExecutionException | InterruptedException e) {
                    dataPage.setAssociatedView(null);
                    doc.finishPage(page);
                }
            }
        }

        savePDF(doc);

    }

    /** @noinspection IOStreamConstructor*/
    private void savePDF(PdfDocument pdf) throws IOException {
        File f = new File(context.getFilesDir(), "report");
        if (!f.exists()) {
            f.mkdir();
        }
        File created = new File(f, "YOOOO.pdf");
        pdf.writeTo(new FileOutputStream(created));
        pdf.close();
    }

    private List<Future<Bitmap>> requestImages() {
        startMultithreadService();

        List<Future<Bitmap>> pendingRequests = new ArrayList<>();
        for (Item item : items) {
            service.submit(() -> {
                Task<Uri> imageTask = ImageFetcher.requestImageUriFromId(item.getLotNum());
                ImageFetcher.FetchBitmapFromUri bitmapFetcher = new ImageFetcher.FetchBitmapFromUri();
                imageTask.addOnCompleteListener(service, bitmapFetcher);
                return bitmapFetcher.getResult();
            });
        }
        return pendingRequests;
    }

    private void startMultithreadService() {
        final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
        service = Executors.newFixedThreadPool(MAXIMUM_POOL_SIZE);
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
}
