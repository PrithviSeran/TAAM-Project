/*
 * ReportFragment.java     1.0     2024/08/07
 */

package com.example.b07demosummer2024.report;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Class used to display report entry page and compile proper information
 * for view.
 * <p>
 * ReportFragment creates a view for the xml file and displays the report
 * item page. The Fragment allows user to enter information on an entry to
 * be reported, and allows user to generate report.
 * <p>
 * Extends <code>SearchFragment</code> to use search methods to
 * find items to generate report on, and fragment methods to display view.
 */
public class ReportFragment extends SearchFragment {
    private CheckBox confirmOnlyIncludeDescriptionAndPicture;
    private ProgressBar pdfGenerationProgressBar;
    private Snackbar openFilePopup = null;

    /**
     * Default constructor of <code>ReportFragment</code>. Creates
     * an <code>activityTitle</code> "Generate report" and <code>submitText</code>
     * "Generate".
     */
    public ReportFragment() {
        super("Generate report", "Generate");
        setSubmissionListener(generateAndSaveReport());
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This allows <code>ReportFragment</code> to create another view for "show only description
     * and picture" <code>Checkbox</code> and a progress bar. Also sets <code>keywordSearch</code>
     * to "gone" so that user cannot interact with the button.
     *
     * @param view                  The View returned by
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} from <code>SearchFragment</code>.
     * @param savedInstanceState    If non-null, this fragment is being re-constructed
     * from a previous saved state as given here. savedInstanceState is not used in this
     * method.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        keywordSearch.setVisibility(View.GONE);

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

    /**
     * Generates a report with the list of items queried by user specifications
     * for generating report and saves it in app directory.
     *
     * @return      <code>FirebaseCallback</code> which attempts to generates and save
     *              a pdf of the report.
     */
    private FirebaseCallback<List<Item>> generateAndSaveReport() {
        return new FirebaseCallback<List<Item>>() {
            @Override
            public void onFirebaseSuccess(List<Item> results) {
                if (!results.isEmpty()) {
                    boolean showDetailedInfo = !confirmOnlyIncludeDescriptionAndPicture.isChecked();
                    Report report = new Report(results, getContext(), "Report.pdf", showDetailedInfo);
                    int totalPages = report.getTotalNumberOfPages();
                    pdfGenerationProgressBar.setVisibility(View.VISIBLE);
                    submitButton.setEnabled(false);

                    report.addPageGeneratedListener(updateOnPageGenerated(totalPages));
                    report.generatePdf().addOnSuccessListener((generateCompleted) -> {
                        report.savePDF().addOnCompleteListener((saveCompleted) -> {
                            Task<Uri> getUriTask = report.getUriOfSavePath();
                            getUriTask.addOnSuccessListener((uri -> {
                                getOpenGeneratedPdfPopup(uri).show();
                            }));
                        });
                    });
                    submitButton.postDelayed(() -> submitButton.setEnabled(true), 4500);
                } else {
                    CommonUtils.logError("SearchError", "No items found");
                    Toast.makeText(getContext(), "No items found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFirebaseFailure(String message) {
                // show error dialog
            }
        };
    }

    private Snackbar getOpenGeneratedPdfPopup(Uri linkToGeneratedFile) {
        if (openFilePopup == null) {
            openFilePopup = Snackbar.make(submitButton, "Here is the generated PDF ", Snackbar.LENGTH_LONG)
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

    private PropertyChangeListener updateOnPageGenerated(int totalPages) {
        return evt -> {
            int generatedPages = (Integer) evt.getNewValue();
            double proportion = generatedPages / (double) totalPages;
            pdfGenerationProgressBar.setProgress((int) (proportion * 100));
            if (generatedPages == totalPages || totalPages == 1) {
                pdfGenerationProgressBar.postDelayed(() -> {
                    pdfGenerationProgressBar.setVisibility(View.GONE);
                    pdfGenerationProgressBar.setProgress(0);
                }, 50);
            }
        };
    }
}
