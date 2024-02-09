package com.example.eptrack;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExpenseDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expenses.db";
    private static final int DATABASE_VERSION = 7; // Updated version

    public static final String TABLE_NAME = "expenses";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DESCRIPTION = "description"; // Added new column
    public static final String COLUMN_CATEGORY = "category"; // Added new column

    private static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_AMOUNT + " REAL, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " + // New column
                    COLUMN_CATEGORY + " TEXT);"; // New column
    // Method to delete all expenses from the database
    public boolean deleteAllExpenses() {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_NAME, null, null);
        db.close();
        return deletedRows > 0;
    }
    public ExpenseDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to insert a new expense into the database

    // Method to get all expenses from the database, ordered by date
    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_ID, // Include _id in the projection
                COLUMN_NAME,
                COLUMN_AMOUNT,
                COLUMN_DATE
        };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                "strftime('%Y-%m-%d', " + COLUMN_DATE + ") ASC"// Order by date and then by _id
        );

        return cursor;
    }

    // Method to get the total expense from the database
    public double getTotalExpense() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalExpense = 0;

        try {
            Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_NAME, null);

            if (cursor.moveToFirst()) {
                totalExpense = cursor.getDouble(0);
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return totalExpense;
    }

    // Method to get the number of items in the database

    // Method to delete an expense from the database
    public boolean deleteExpense(long expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(expenseId)};

        int deletedRows = db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();

        return deletedRows > 0;
    }
}
