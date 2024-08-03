package com.example.b07demosummer2024.report;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.b07demosummer2024.Item;
import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.firebase.ImageFetcher;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ReportDataPage {

    private Item item;
    private Context context;

    public interface ViewCompletedCallback {
        void onComplete(View completedView);
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
    }

    /**
     * Gets the View associated with this page with "callback" being called when
     * the view is completed. The View is completed when all data fields are populated
     * and the image has been inserted.
     * @param callback the listener that operates on the completed View
     * @return the Task that when completed means the provided "callback" has completed its work
     * @see ViewCompletedCallback
     */
    protected Task<Void> asyncSetAssociatedView(ViewCompletedCallback callback) {
        LayoutInflater inf = LayoutInflater.from(context);

        View itemView = inf.inflate(R.layout.pdf_report_data_page, null);
        ImageView itemImage = itemView.findViewById(R.id.itemImage);
        getAndSetTextView(itemView, R.id.itemLotNum, item.getLotNum());
        getAndSetTextView(itemView, R.id.itemName, item.getName());
        getAndSetTextView(itemView, R.id.itemCategory, item.getCategory());
        getAndSetTextView(itemView, R.id.itemPeriod, item.getPeriod());
        getAndSetTextView(itemView, R.id.itemDescription, item.getDescription());

        TaskCompletionSource<Void> statusOfTaskAfterPageCompletion = new TaskCompletionSource<>();
        ImageFetcher.requestImage(item.getLotNum(), Picasso.get(), new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ViewGroup.LayoutParams imageLayout = itemImage.getLayoutParams();
                Bitmap scaledToFitBitmap = Bitmap.createScaledBitmap(bitmap, imageLayout.width,
                        imageLayout.height, true);
                itemImage.setImageBitmap(scaledToFitBitmap);

                notifyListenersAndPropagateFeedback();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                itemImage.setImageDrawable(errorDrawable);
                notifyListenersAndPropagateFeedback();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}

            public void notifyListenersAndPropagateFeedback() {
                try {
                    callback.onComplete(itemView);
                    statusOfTaskAfterPageCompletion.setResult(null);
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
