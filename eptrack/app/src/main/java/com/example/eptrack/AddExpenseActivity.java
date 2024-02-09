package com.example.eptrack;

// AddExpenseActivity.java
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText expenseNameEditText, amountEditText;
    private DatePicker datePicker;
    private Button saveButton;

    private ExpenseDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        expenseNameEditText = findViewById(R.id.expenseNameEditText);
        amountEditText = findViewById(R.id.amountEditText);
        datePicker = findViewById(R.id.datePicker);
        saveButton = findViewById(R.id.saveButton);

        dbHelper = new ExpenseDatabaseHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });
    }

    private void saveExpense() {
        try {
            String expenseName = expenseNameEditText.getText().toString();
            String amountStr = amountEditText.getText().toString();
            double amount = Double.parseDouble(amountStr);

            // Get the selected date from the DatePicker
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1; // Month is zero-based
            int year = datePicker.getYear();
            String date = year + "-" + month + "-" + day;

            // Insert the data into the database
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", expenseName);
            values.put("amount", amount);
            values.put("date", date);

            long newRowId = db.insert("expenses", null, values);

            if (newRowId != -1) {
                Toast.makeText(this, "Expense saved successfully", Toast.LENGTH_SHORT).show();
                // You can add further actions here if needed
            } else {
                Toast.makeText(this, "Error saving expense", Toast.LENGTH_SHORT).show();
                Log.e("Database", "Error saving expense. Row ID: " + newRowId);
            }

            // Close the database connection
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving expense", Toast.LENGTH_SHORT).show();
            Log.e("Database", "Exception during expense saving", e);
        }
    }
}