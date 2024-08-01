package com.example.b07demosummer2024.report;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.b07demosummer2024.firebase.FirebaseCallback;
import com.example.b07demosummer2024.Item;
import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.SearchFragment;
import com.example.b07demosummer2024.firebase.ImageFetcher;
import com.google.android.gms.common.images.ImageManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class ReportFragment extends SearchFragment {
    private CheckBox confirmOnlyIncludeDescriptionAndPicture;

    public ReportFragment() {
        super("Generate report", "Generate");
        setSubmissionListener(generateAndSaveReport());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // creating "show only description and picture" checkbox
        LinearLayout extraLayout = view.findViewById(R.id.extraLayout);

        LinearLayout.LayoutParams checkBoxConstraint = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        checkBoxConstraint.setMargins(32, 10, 32, 10);
        checkBoxConstraint.gravity = Gravity.CENTER;

        confirmOnlyIncludeDescriptionAndPicture = new CheckBox(getContext());
        confirmOnlyIncludeDescriptionAndPicture.setText(
                "Show only Item description and picture in generated report");
        extraLayout.addView(confirmOnlyIncludeDescriptionAndPicture, checkBoxConstraint);
        extraLayout.setVisibility(View.VISIBLE);
    }

    class Test implements Target, ImageManager.OnImageLoadedListener {
        long initialTime;
        long totalTime;

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            System.out.println((System.nanoTime() - initialTime) / 1000000);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

        public void setTotalTime(long time) {
            this.totalTime = time;
        }

        public void setInitialTime(long initialTime) {
            this.initialTime = initialTime;
        }

        @Override
        public void onImageLoaded(@NonNull Uri uri, @Nullable Drawable drawable, boolean b) {
            System.out.println((System.nanoTime() - initialTime) / 1000000);
        }
    }

    // takes about 1 second to get uri and use picasso to load into imageview
    private FirebaseCallback<List<Item>> generateAndSaveReport() {
        return new FirebaseCallback<List<Item>>() {
            @Override
            public void onSuccess(List<Item> results) {
//                Report report = new Report(results, getContext());
//                try {
//                    report.generateAndSavePdf();
//                } catch (IOException e) {
//                    Toast.makeText(getContext(), "Report could not be saved, try again.",
//                            Toast.LENGTH_LONG).show();
//                }

                Test t = new Test();
                long startTime = System.nanoTime();
                ImageFetcher.requestImageUriFromId("Item Name").addOnCompleteListener(
                        uriTask -> {
                            t.setInitialTime(startTime);
                            System.out.println((System.nanoTime() - startTime) / 1000000);
                            //ImageManager m = ImageManager.create(getContext());
                            //m.loadImage(t, uriTask.getResult());
                            Picasso.get().load(uriTask.getResult()).into(t);
                        });
            }

            @Override
            public void onFailure(String message) {
                // show error dialog
            }
        };
    }

}
