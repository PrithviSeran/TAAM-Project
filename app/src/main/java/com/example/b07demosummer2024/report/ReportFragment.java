package com.example.b07demosummer2024.report;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.b07demosummer2024.CommonUtils;
import com.example.b07demosummer2024.firebase.FirebaseCallback;
import com.example.b07demosummer2024.Item;
import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.SearchFragment;
import com.google.android.material.snackbar.Snackbar;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

public class ReportFragment extends SearchFragment {
    private CheckBox confirmOnlyIncludeDescriptionAndPicture;
    private ProgressBar pdfGenerationProgressBar;
    private Snackbar openFilePopup = null;

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

        pdfGenerationProgressBar = view.findViewById(R.id.extraProgressBar);
    }

    private FirebaseCallback<List<Item>> generateAndSaveReport() {
        return new FirebaseCallback<List<Item>>() {
            @Override
            public void onSuccess(List<Item> results) {
                boolean showDetailedInfo = !confirmOnlyIncludeDescriptionAndPicture.isChecked();
                Report report = new Report(results, getContext(), "Report.pdf", showDetailedInfo);
                int totalPages = report.getTotalNumberOfPages();
                pdfGenerationProgressBar.setVisibility(View.VISIBLE);
                submitButton.setEnabled(false);

                report.addPageGeneratedListener(updateOnPageGenerated(totalPages));
                report.generatePdf().addOnSuccessListener((Void) -> {
                    try {
                        report.savePDF();
                        getOpenGeneratedPdfPopup(report.getUriOfSavePath()).show();
                    } catch (IOException e) {
                        Toast.makeText(getContext(), "Could not save report, try again",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                submitButton.postDelayed(() -> submitButton.setEnabled(true), 5000);
            }

            @Override
            public void onFailure(String message) {
                // show error dialog
            }
        };
    }

    private PropertyChangeListener updateOnPageGenerated(int totalPages) {
        return evt -> {
            int generatedPages = (Integer) evt.getNewValue();
            if (generatedPages <= totalPages) { // the last page increments to a higher total
                double proportion = generatedPages / (double) totalPages;
                pdfGenerationProgressBar.setProgress((int) (proportion * 100));
                if (generatedPages == totalPages) {
                    pdfGenerationProgressBar.postDelayed(() -> {
                        pdfGenerationProgressBar.setVisibility(View.GONE);
                        pdfGenerationProgressBar.setProgress(0);
                    }, 50);
                }
            }
        };
    }

    private Snackbar getOpenGeneratedPdfPopup(Uri linkToGeneratedFile) {
        if (openFilePopup == null) {
            openFilePopup = Snackbar.make(submitButton, "Here is the generated PDF ", Snackbar.LENGTH_SHORT)
                    .setAction("Open", v -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(linkToGeneratedFile, "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(intent);
                    });
        }
        return openFilePopup;
    }
}
