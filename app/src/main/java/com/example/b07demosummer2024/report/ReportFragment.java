package com.example.b07demosummer2024.report;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.b07demosummer2024.firebase.FirebaseCallback;
import com.example.b07demosummer2024.Item;
import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.SearchFragment;

import java.io.IOException;
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

    private FirebaseCallback<List<Item>> generateAndSaveReport() {
        return new FirebaseCallback<List<Item>>() {
            @Override
            public void onSuccess(List<Item> results) {
                Report report = new Report(results, getContext());
                report.generatePdf().addOnCompleteListener((pdfGenerationStatus) -> {
                    if (pdfGenerationStatus.isSuccessful()) {
                        try {
                            report.savePDF("Report.pdf");
                        } catch (IOException e) {
                            Toast.makeText(getContext(), "Could not save report, try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w("ReportError", "Error occurred in callback after view was generated");
                        Toast.makeText(getContext(), "Error occurred with pdf generation, could not save",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                // show error dialog
            }
        };
    }
}
