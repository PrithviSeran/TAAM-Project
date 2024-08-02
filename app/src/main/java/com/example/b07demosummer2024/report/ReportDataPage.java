package com.example.b07demosummer2024.report;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.b07demosummer2024.Item;
import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.firebase.ImageFetcher;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ReportDataPage {

    private Item item;
    private Context context;
    private ImageManager manager;

    public interface PageCompletedCallback {
        void onComplete(View completedPage);
    }

    /**
     * Create a new instance of the TAAM report data page. Note an item report page will take up the
     * entire page.
     * @param item the Item that will be written to this page
     * @param context context object
     */
    protected ReportDataPage(Item item, Context context) {
        this.item = item;
        this.context = context;
        this.manager = ImageManager.create(context);
    }

    protected Task<Boolean> asyncSetAssociatedView(PageCompletedCallback callback) {
        LayoutInflater inf = LayoutInflater.from(context);

        View itemView = inf.inflate(R.layout.pdf_report_data_page, null);
        ImageView itemImage = itemView.findViewById(R.id.itemImage);
        getAndSetTextView(itemView, R.id.itemLotNum, item.getLotNum());
        getAndSetTextView(itemView, R.id.itemName, item.getName());
        getAndSetTextView(itemView, R.id.itemCategory, item.getCategory());
        getAndSetTextView(itemView, R.id.itemPeriod, item.getPeriod());
        getAndSetTextView(itemView, R.id.itemDescription, item.getDescription());

        TaskCompletionSource<Boolean> statusOfTaskAfterPageCompletion = new TaskCompletionSource<>();
        ImageFetcher.requestImage(item.getLotNum(), Picasso.get(), new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                itemImage.setImageBitmap(bitmap);
                notifyListeners();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                itemImage.setImageDrawable(errorDrawable);
                notifyListeners();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}

            public void notifyListeners() {
                try {
                    callback.onComplete(itemView);
                    statusOfTaskAfterPageCompletion.setResult(true);
                } catch (RuntimeException pageCallbackException) {
                    statusOfTaskAfterPageCompletion.setException(pageCallbackException);
                }
            }
        }, R.drawable.image_not_found);
        return statusOfTaskAfterPageCompletion.getTask();
    }

    private void getAndSetTextView(View parent, int id, String text) {
        TextView result = parent.findViewById(id);
        result.setText(text);
    }
}
