package com.drejkim.androidwearmotionsensors;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by julian on 6/5/15.
 */
public class DatabaseHelper {
    public static final String DATABASE_FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();
    public static final String DATABASE_NAME = "motionData.db";

    String TAG="DB";
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name


    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_DATA = "data";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TIME = "timestamp";
    private static final String KEY_X = "dataX";
    private static final String KEY_Y = "dataY";
    private static final String KEY_Z = "dataZ";
    private static final String KEY_PH_NO = "phone_number";

    String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_PH_NO + " TEXT" + ")";

    String CREATE_DATA_TABLE = "CREATE TABLE " + TABLE_DATA + "("
            +KEY_TIME+" REAL,"
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_X + " REAL,"
            + KEY_Y + " REAL," + KEY_Z + " REAL" + ")";
    String insertTest="insert into data (dataX,dataY,dataZ) values (1,2,3)";



    private SQLiteDatabase database;

    public DatabaseHelper() {
        try {
            Log.d(TAG,DATABASE_FILE_PATH
                    + File.separator + DATABASE_NAME);
            database= SQLiteDatabase.openOrCreateDatabase(DATABASE_FILE_PATH
                    + File.separator + DATABASE_NAME, null);
            Log.d(TAG,"creating");
            createTables();

//            database = SQLiteDatabase.openDatabase(DATABASE_FILE_PATH
//                    + File.separator + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException ex) {
            Log.e(TAG, "Could not create the database " + ex.getMessage(), ex);
            // error means tables does not exits

        }
//        finally {

//        }
    }

    private void createTables() {
        database.execSQL(CREATE_CONTACTS_TABLE);
        Log.d(TAG,"Created contacts table");
        database.execSQL(CREATE_DATA_TABLE);
        Log.d(TAG,"Created data table");

        //Add some way to verify that the tables were created
//        database.execSQL("SELECT name FROM sqlite_master WHERE type = 'table'\n");
//        database.execSQL(insertTest);
        database.close();
    }

    public void close() {
        database.close();
    }

    public SQLiteDatabase getReadableDatabase() {
        database = SQLiteDatabase.openDatabase(DATABASE_FILE_PATH
                        + File.separator + DATABASE_NAME, null,
                SQLiteDatabase.OPEN_READONLY);
        return database;
    }

    public SQLiteDatabase getWritableDatabase() {
        database = SQLiteDatabase.openDatabase(DATABASE_FILE_PATH
                        + File.separator + DATABASE_NAME, null,
                SQLiteDatabase.OPEN_READWRITE);
        return database;
    }
    void addData(Data data) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG,db.getPath());

        ContentValues values = new ContentValues();
        long unixTime = System.currentTimeMillis() / 1000L;
        values.put(KEY_TIME,data.getTime());
        values.put(KEY_X, data.getX());
        values.put(KEY_Y, data.getY());
        values.put(KEY_Z, data.getZ());



        // Inserting Row
        if (db.insert(TABLE_DATA, null, values)==-1){
            Log.d(TAG,"The data insertion failed");
        }
        db.close(); // Closing database connection
    }

    // Getting single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        db.close();
        return contact;
    }

    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        db.close();
        return contactList;
    }

    public List<Data> getAllData() {
        List<Data> dataList = new ArrayList<Data>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DATA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Data data = new Data();
                data.setID(Integer.parseInt(cursor.getString(0)));
                data.setX(cursor.getString(1));
                data.setY(cursor.getString(2));
                data.setZ(cursor.getString(3));
                // Adding contact to list
                dataList.add(data);
            } while (cursor.moveToNext());
        }

        // return contact list
        db.close();
        return dataList;

    }

    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        // updating row

        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    void update(){
        Log.d(TAG,"Hard update");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);

        // Create tables again
        createTables();
        db.close();

    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }
}
