package com.example.b07demosummer2024.report;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.example.b07demosummer2024.Item;
import com.example.b07demosummer2024.R;

public class ReportDataPage {

    private Item item;
    private Context context;
    private View associatedView = null;

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

    protected void setAssociatedView(Bitmap image) {
        LayoutInflater inf = LayoutInflater.from(context);

        associatedView = inf.inflate(R.layout.pdf_report_data_page, null);
        ImageView itemImage = associatedView.findViewById(R.id.itemImage);
        if (image != null) {
            itemImage.setImageBitmap(image);
        } else {
            itemImage.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.image_not_found));
        }
        getAndSetTextView(associatedView, R.id.itemLotNum, item.getLotNum());
        getAndSetTextView(associatedView, R.id.itemName, item.getName());
        getAndSetTextView(associatedView, R.id.itemCategory, item.getCategory());
        getAndSetTextView(associatedView, R.id.itemPeriod, item.getPeriod());
        getAndSetTextView(associatedView, R.id.itemDescription, item.getDescription());
    }

    private void getAndSetTextView(View parent, int id, String text) {
        TextView result = parent.findViewById(id);
        result.setText(text);
    }

    public View getAssociatedView() {
        return associatedView;
    }
}
