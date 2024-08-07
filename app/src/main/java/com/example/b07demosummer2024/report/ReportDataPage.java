/*
 * ReportDataPage.java     1.0     2024/08/07
 */

package com.example.b07demosummer2024.report;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.b07demosummer2024.CommonUtils;
import com.example.b07demosummer2024.Item;
import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.firebase.ImageFetcher;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Class used to generate 1 page and associated view for
 * the report pdf. Extends <code>AbstractReportPage</code> for
 * <code>ViewCompletedCallback</code> checks.
 */
public class ReportDataPage extends AbstractReportPage {

    private Item item;
    private boolean showDetailedInfo;

    /**
     * Create a new instance of the TAAM report data page. Note an item report page will take up the
     * entire page.
     * @param item the Item that will be written to this page
     * @param context context object
     */
    protected ReportDataPage(Item item, Context context, boolean showDetailedInfo) {
        super(context);
        this.item = item;
        this.showDetailedInfo = showDetailedInfo;
    }

    /**
     * Gets asynchronously the View associated with this page, with "callback" being called when
     * the View is completed. The View is completed when all data fields are populated
     * and the image has been inserted.
     * @param callback the listener that operates on the completed View
     * @see ViewCompletedCallback
     */
    protected void asyncGetAssociatedView(ViewCompletedCallback callback) {
        LayoutInflater inflator = LayoutInflater.from(context);
        View itemView;

        if (showDetailedInfo) {
            itemView = inflator.inflate(R.layout.pdf_report_data_page, null);
            addAdditionalInfoToView(itemView);
        } else {
            itemView = inflator.inflate(R.layout.pdf_report_lite_data_page, null);
        }
        getAndSetTextView(itemView, R.id.itemDescription, item.getDescription());

        ImageView itemImage = itemView.findViewById(R.id.itemImage);
        Drawable errorDrawable = AppCompatResources.getDrawable(super.context, R.drawable.image_not_found);
        ImageFetcher.requestImage(item.getLotNum(), Picasso.get(), new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                itemImage.setImageBitmap(bitmap);

                notifyListenersOfCreatedView(callback, itemView);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable picassoErrorDrawable) {
                CommonUtils.logError("InvalidBitmapError", e.getMessage());
                itemImage.setImageDrawable(picassoErrorDrawable);

                notifyListenersOfCreatedView(callback, itemView);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}

        }, errorDrawable);
    }

    private void addAdditionalInfoToView(View itemView) {
        getAndSetTextView(itemView, R.id.itemLotNum, item.getLotNum());
        getAndSetTextView(itemView, R.id.itemName, item.getName());
        getAndSetTextView(itemView, R.id.itemCategory, item.getCategory());
        getAndSetTextView(itemView, R.id.itemPeriod, item.getPeriod());
    }
}
