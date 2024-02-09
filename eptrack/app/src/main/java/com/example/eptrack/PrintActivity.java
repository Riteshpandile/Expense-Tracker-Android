package com.example.eptrack;


import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import android.os.*;

import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class PrintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        // Use the same layout as TotalExpenseActivity
         // Retrieve total value from Intent
        double totalValue = getIntent().getDoubleExtra("totalValue", 0.0);

        // Call the displayTotalValue method to update the TextView
        displayTotalValue(totalValue);
        // Get records from the intent
        ArrayList<String> records = getIntent().getStringArrayListExtra("records");

        // Display records in the table layout
        displayRecordsInTable(records);
    }

    private void displayRecordsInTable(ArrayList<String> records) {
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        // Clear existing rows
        tableLayout.removeAllViews();

        // Add table header row
        addHeaderRow(tableLayout);

        // Add data rows
        for (String record : records) {
            TableRow dataRow = new TableRow(this);
            dataRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            // Split the record into individual values
            String[] values = record.split(", ");

            // Add data to the row
            for (String value : values) {
                addTextViewToRow(dataRow, value);
            }

            // Add the row to the table
            tableLayout.addView(dataRow);
        }
    }
    private void addTextViewToRow(TableRow row, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setBackgroundResource(R.drawable.cell_shape); // Customize as needed
        textView.setPadding(8, 8, 8, 8);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(params);

        // Set text size to the same value for both TextView and Button
        float textSize = getResources().getDimension(R.dimen.text_size);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        row.addView(textView);
    }
    private int measureTextWidth(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }
    private void addHeaderRow(TableLayout tableLayout) {
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        String[] headers = {"Sr. No", "Items", "Value", "Date",};

        for (String header : headers) {
            TextView headerTextView = new TextView(this);
            headerTextView.setText(header);
            headerTextView.setBackgroundResource(R.drawable.cell_shape); // Customize as needed
            headerTextView.setPadding(8, 8, 8, 8);

            // Set text size to the same value for both TextView and Button
            float textSize = getResources().getDimension(R.dimen.text_size);
            headerTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            // Measure text width and set column width
            int textWidth = measureTextWidth(headerTextView.getText().toString(), headerTextView.getPaint());
            headerRow.addView(headerTextView, new TableRow.LayoutParams(textWidth, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        tableLayout.addView(headerRow);
    }
    private void displayTotalValue(double totalValue) {
        TextView totalValueTextView = findViewById(R.id.totalValueTextView);
        totalValueTextView.setText("Total Value: " + totalValue);
    }
    // Method to handle the "Save" button click
    public void onTotalValueButtonClick(View view) {
        // Implement the action you want to perform when the "Save" button is clicked
        // For example, save data to a file or database
        // You can add your logic here
        showToast("Not Working Use Screenshot");
    }

    // Method to handle the "Back" button click
    public void onPrintTableButtonClick(View view) {
        // Finish the current activity to go back to the previous one
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

