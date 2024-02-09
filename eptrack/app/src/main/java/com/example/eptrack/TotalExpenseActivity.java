package com.example.eptrack; // Add or correct this line

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.graphics.Color;

import android.os.Bundle;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Paint;
import android.graphics.Rect;
import java.util.ArrayList;

public class TotalExpenseActivity extends AppCompatActivity {

    private ExpenseViewModel expenseViewModel;
    private ExpenseDatabaseHelper dbHelper;
    private TextView totalValueTextView;
    private TableLayout tableLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_expense);


        dbHelper = new ExpenseDatabaseHelper(this);
        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        totalValueTextView = findViewById(R.id.totalValueTextView);
        tableLayout = findViewById(R.id.tableLayout);

        expenseViewModel.getRefreshTrigger().observe(this, refresh -> {
            if (refresh != null && refresh) {
                updateTableData();
                updateTotalValue();
                showToast("Data refreshed");
            }
        });

        updateTableData();
        updateTotalValue();

        Cursor cursor = dbHelper.getAllExpenses();
        displayDataInTable(cursor);
        updateTotalValue(); // Update total value initially
        cursor.close();
    }
    // Handle click events for the layout buttons
    public void onTotalValueButtonClick(View view) {
        // Delete all items in the database
        deleteAllExpenses();
    }

    private void deleteAllExpenses() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete all expenses?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform the deletion of all expenses
                        boolean success = dbHelper.deleteAllExpenses();

                        if (success) {
                            showToast("All expenses deleted successfully");

                            // Trigger a refresh after deleting all expenses
                            expenseViewModel.triggerRefresh();
                        } else {
                            showToast("Failed to delete all expenses");
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    private void updateTableData() {
        Cursor cursor = dbHelper.getAllExpenses();
        displayDataInTable(cursor);
        cursor.close();
    }
    private void updateTotalValue() {
        double totalValue = dbHelper.getTotalExpense();
        totalValueTextView.setText("Total Value: " + totalValue);
    }
    // Handle click events for the layout buttons

    public void onPrintTableButtonClick(View view) {
        // Get all records from the database
        double totalValue = dbHelper.getTotalExpense();
        Cursor cursor = dbHelper.getAllExpenses();

        // Create an intent to start the PrintActivity
        Intent intent = new Intent(this, PrintActivity.class);
        intent.putExtra("totalValue", totalValue);
        // Pass the records to PrintActivity
        ArrayList<String> records = getRecordsFromCursor(cursor);
        intent.putStringArrayListExtra("records", records);

        // Start PrintActivity
        startActivity(intent);
    }


    private ArrayList<String> getRecordsFromCursor(Cursor cursor) {
        ArrayList<String> records = new ArrayList<>();

        int srNo = 1;
        int nameIndex = cursor.getColumnIndex("name");
        int amountIndex = cursor.getColumnIndex("amount");
        int dateIndex = cursor.getColumnIndex("date");

        // Check if the column indices are valid
        if (nameIndex != -1 && amountIndex != -1 && dateIndex != -1) {
            while (cursor.moveToNext()) {
                // Extract data from the cursor and format it as needed
                String items = cursor.getString(nameIndex);
                double value = cursor.getDouble(amountIndex);
                String date = formatDate(cursor.getString(dateIndex));
                String record = String.format("Sr. No: %d, Items: %s, Value: %.2f, Date: %s", srNo, items, value, date);
                records.add(record);
                srNo++;
            }
        } else {
            // Handle the case where one or more columns are not found
            // You may want to log a warning or take appropriate action
        }

        return records;
    }


    private String formatDate(String inputDate) {
        // Your date formatting logic
        return inputDate;
    }

    private void displayDataInTable(Cursor cursor) {
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        // Clear existing rows
        tableLayout.removeAllViews();

        // Add table header row
        addHeaderRow(tableLayout);

        // Add data rows
        int srNo = 1;

        while (cursor.moveToNext()) {
            TableRow dataRow = new TableRow(this);
            dataRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            String items = null;
            String value = null; // Initialize with a default value
            String date = null;

            int nameIndex = cursor.getColumnIndex("name");
            int amountIndex = cursor.getColumnIndex("amount");
            int dateIndex = cursor.getColumnIndex("date");
            int idIndex = cursor.getColumnIndex("_id"); // Add this line

            // Check if the column exists in the cursor
            if (nameIndex != -1) {
                items = cursor.getString(nameIndex);
            }

            if (amountIndex != -1) {
                value = cursor.getString(amountIndex);
            }

            if (dateIndex != -1) {
                date = cursor.getString(dateIndex);
            }

            // Format date to dd/mm/yyyy
            String formattedDate = formatDate(date);

            // Add data to the row
            addDataToRow(dataRow, String.valueOf(srNo), items, value, formattedDate);

            // Add delete button if id column exists
            if (idIndex != -1) {
                addDeleteButtonToRow(dataRow, cursor.getLong(idIndex));
            }

            // Add the row to the table
            tableLayout.addView(dataRow);
            srNo++;
        }
    }



    // Add delete button to the data row
    private void addDeleteButtonToRow(TableRow row, final long expenseId) {
        Button deleteButton = new Button(this);
        deleteButton.setText("Delete");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click
                deleteExpense(expenseId);
            }
        });

        // Customize the delete button as needed
        deleteButton.setBackgroundResource(R.drawable.button_shape);
        deleteButton.setTextColor(Color.WHITE);

        // Set text size to the same value for both TextView and Button
        float textSize = getResources().getDimension(R.dimen.text_size);
        deleteButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        // Add the delete button to the row
        row.addView(deleteButton);
    }

    private void addHeaderRow(TableLayout tableLayout) {
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        String[] headers = {"Sr. No", "Items", "Value", "Date", "Action"};

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

    private void addDataToRow(TableRow row, String srNo, String items, String value, String date) {
        addTextViewToRow(row, srNo);
        addTextViewToRow(row, items);
        addTextViewToRow(row, value);
        addTextViewToRow(row, date);
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

    private void deleteExpense(long expenseId) {
        boolean success = dbHelper.deleteExpense(expenseId);
        expenseViewModel.triggerRefresh();
        if (success) {
            // Reload the data after deletion
            Cursor cursor = dbHelper.getAllExpenses();
            displayDataInTable(cursor);
            cursor.close();
            showToast("Expense deleted successfully");
        } else {
            showToast("Failed to delete expense");
        }
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private int measureTextWidth(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }
}
