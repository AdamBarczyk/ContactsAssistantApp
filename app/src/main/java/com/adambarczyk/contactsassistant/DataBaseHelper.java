package com.adambarczyk.contactsassistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.adambarczyk.contactsassistant.datamodels.ContactModel;
import com.adambarczyk.contactsassistant.datamodels.ServiceModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String CONTACTS = "Contacts";
    public static final String SERVICES = "Services";

    public static final String COLUMN_CONTACT_ID = "contact_id";
    public static final String COLUMN_CONTACT_NAME = "contact_name";
    public static final String COLUMN_CONTACT_EMAIL = "contact_email";
    public static final String COLUMN_CONTACT_PHONE = "contact_phone";
    public static final String COLUMN_CONTACT_ADDRESS = "contact_address";
    public static final String COLUMN_CONTACT_NOTES = "contact_notes";

    public static final String COLUMN_SERVICE_ID = "service_id";
    public static final String COLUMN_SERVICE_INFO = "service_info";
    public static final String COLUMN_SERVICE_COST = "service_cost";
    public static final String COLUMN_SERVICE_NAME = "service_name";
    public static final String COLUMN_SERVICE_TIME = "service_time";


    // constructor

    public DataBaseHelper(@Nullable Context context) {
        super(context, "Database.db", null, 3);
    }


    //  this is called the first time a database is accessed. There is the code to create a new database

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CONTACTS + " (" +
                COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CONTACT_NAME + " TEXT NOT NULL, " +
                COLUMN_CONTACT_EMAIL + " TEXT, " +
                COLUMN_CONTACT_PHONE + " INTEGER, " +
                COLUMN_CONTACT_ADDRESS + " TEXT, " +
                COLUMN_CONTACT_NOTES + " TEXT)");

        db.execSQL("CREATE TABLE " + SERVICES + " (" +
                COLUMN_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CONTACT_ID + " INTEGER NOT NULL, " +
                COLUMN_SERVICE_INFO + " TEXT, " +
                COLUMN_SERVICE_COST + " INTEGER, " +
                COLUMN_SERVICE_NAME + "TEXT, " +
                COLUMN_SERVICE_TIME + "FLOAT, " +
                "FOREIGN KEY (" + COLUMN_CONTACT_ID + ") REFERENCES " +
                CONTACTS + " (" + COLUMN_CONTACT_ID + "))");
    }


    // this is called if the database version number changes. It prevents previous users apps from breaking when database's design changes.

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + SERVICES);
        onCreate(db);
    }




    // There are methods for CRUD operations on the database


    // Methods for CONTACTS TABLE

    public boolean addContact(ContactModel contactModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // There is no need to put contact_id, because it self-increments in the database
        cv.put(COLUMN_CONTACT_NAME, contactModel.getName());
        cv.put(COLUMN_CONTACT_EMAIL, contactModel.getEmail());
        cv.put(COLUMN_CONTACT_PHONE, contactModel.getPhone());
        cv.put(COLUMN_CONTACT_ADDRESS, contactModel.getAddress());
        cv.put(COLUMN_CONTACT_NOTES, contactModel.getNotes());

        long result = db.insert(CONTACTS, null, cv);

        // close the database
        db.close();

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateContact(ContactModel contactModel) {
        // Initialize boolean to return
        boolean success;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // There is no need to put contact_id because it shouldn't change when updating
        cv.put(COLUMN_CONTACT_NAME, contactModel.getName());
        cv.put(COLUMN_CONTACT_EMAIL, contactModel.getEmail());
        cv.put(COLUMN_CONTACT_PHONE, contactModel.getPhone());
        cv.put(COLUMN_CONTACT_ADDRESS, contactModel.getAddress());
        cv.put(COLUMN_CONTACT_NOTES, contactModel.getNotes());


        String queryString = "SELECT * FROM " + CONTACTS + " WHERE " + COLUMN_CONTACT_ID + " = ?";
        String[] queryArgs = new String[]{String.valueOf(contactModel.getContactId())};

        Cursor cursor = db.rawQuery(queryString, queryArgs);

        if (cursor.moveToFirst()) {
            long result = db.update(CONTACTS, cv, COLUMN_CONTACT_ID + " = ?",
                    new String[] {String.valueOf(contactModel.getContactId())});
            if (result == 0) {
                success = false;
            } else {
                success = true;
            }
        } else {
            success = false;
        }

        // close both the cursor and the database
        cursor.close();
        db.close();

        return success;
    }

    public ContactModel getContactById(int contactId) {
        ContactModel contactModel;

        SQLiteDatabase db = this.getReadableDatabase();

        String queryString = "SELECT * FROM " + CONTACTS + " WHERE " + COLUMN_CONTACT_ID + " = ?";
        String[] queryArgs = new String[] {String.valueOf(contactId)};

        // find row with the given contactId in the database
        Cursor cursor = db.rawQuery(queryString, queryArgs);

        if (cursor.moveToFirst()) {

            // load data from found row
            String name = cursor.getString(1);
            String email = cursor.getString(2);
            int phone = cursor.getInt(3);
            String address = cursor.getString(4);
            String notes = cursor.getString(5);

            // save loaded data (and contactId) as object of ContactModel class
            contactModel = new ContactModel(contactId, name, email, phone, address, notes);

        } else { // if record not found
            // return object of ContactModel class with field contactId set to -1
            contactModel = new ContactModel(-1);
        }

        // close both the cursor and the database
        cursor.close();
        db.close();

        // return contactModel with data of found service or serviceID set to -1 as an error code
        return contactModel;
    }

    public boolean deleteContact(ContactModel contactModel) {
        // Initialize boolean to return
        boolean success;

        // find contact model in the database. If it found, delete it and return true.
        // if it is not found, return false

        SQLiteDatabase db = this.getWritableDatabase();
        //String queryString = "DELETE FROM " + CONTACTS + "WHERE " + COLUMN_CONTACT_ID + " = " +
        //        contactModel.getContact_id();

        String queryString = "SELECT * FROM " + CONTACTS + " WHERE " + COLUMN_CONTACT_ID + " = ?";
        String[] queryArgs = new String[] {String.valueOf(contactModel.getContactId())};

        Cursor cursor = db.rawQuery(queryString, queryArgs);

        if (cursor.moveToFirst()) {
            int deleted = db.delete(CONTACTS, COLUMN_CONTACT_ID + " = ?",
                    new String[]{String.valueOf(contactModel.getContactId())});
            if (deleted > 0) { // deleted variable contains number of rows deleted
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }

        // close both the cursor and the database
        cursor.close();
        db.close();

        return success;

    }

    public List<ContactModel> getListOfContactsFromDatabase() {
        List<ContactModel> returnList = new ArrayList<>();

        // set the query to get data from database
        String queryString = "SELECT * FROM " + CONTACTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null); // Cursor contains result returned by database as an answer for the query

        if (cursor.moveToFirst()) { // cursor.moveToFirst() returns a true if there were items selected
            // loop through the cursor(results from DB) and create new ContactModel objects
            // put them into the returnList

            do {
                int contactID = cursor.getInt(0);
                String contactName = cursor.getString(1);
                String contactEmail = cursor.getString(2);
                int contactPhone = cursor.getInt(3);
                String contactAddress = cursor.getString(4);
                String contactNotes = cursor.getString(5);

                ContactModel newContact = new ContactModel(contactID, contactName, contactEmail,
                        contactPhone, contactAddress, contactNotes);
                returnList.add(newContact);
            } while (cursor.moveToNext());
        } else {
            // do nothing, there are no contacts to add
        }

        // close both the cursor and the database
        cursor.close();
        db.close();

        return returnList;
    }




    // Methods for SERVICE TABLE

    public boolean addService(ServiceModel serviceModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // There is no need to put service_id, because it self-increments in the database
        cv.put(COLUMN_CONTACT_ID, serviceModel.getContactId());
        cv.put(COLUMN_SERVICE_INFO, serviceModel.getServiceInfo());
        cv.put(COLUMN_SERVICE_COST, serviceModel.getServiceCost());
        cv.put(COLUMN_SERVICE_NAME, serviceModel.getServiceName());
        cv.put(COLUMN_SERVICE_TIME, serviceModel.getServiceTime());

        long result = db.insert(SERVICES, null, cv);

        // close both the cursor and the database
        db.close();

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateService(ServiceModel serviceModel) {
        // Initialize boolean to return
        boolean success;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // There is no need to put service_id and contact_id because it shouldn't change when updating
        cv.put(COLUMN_SERVICE_INFO, serviceModel.getServiceInfo());
        cv.put(COLUMN_SERVICE_COST, serviceModel.getServiceCost());
        cv.put(COLUMN_SERVICE_NAME, serviceModel.getServiceName());
        cv.put(COLUMN_SERVICE_TIME, serviceModel.getServiceTime());


        String queryString = "SELECT * FROM " + SERVICES + " WHERE " + COLUMN_SERVICE_ID + " = ?";
        String[] queryArgs = new String[]{String.valueOf(serviceModel.getServiceId())};

        Cursor cursor = db.rawQuery(queryString, queryArgs);

        if (cursor.moveToFirst()) {
            long result = db.update(SERVICES, cv, COLUMN_SERVICE_ID + " = ?",
                    new String[] {String.valueOf(serviceModel.getServiceId())});
            if (result == 0) {
                success = false;
            } else {
                success = true;
            }
        } else {
            success = false;
        }

        // close both the cursor and the database
        cursor.close();
        db.close();

        return success;
    }

    public ServiceModel getServiceById(int serviceId) {
        ServiceModel serviceModel;

        SQLiteDatabase db = this.getReadableDatabase();

        String queryString = "SELECT * FROM " + SERVICES + " WHERE " + COLUMN_SERVICE_ID + " = ?";
        String[] queryArgs = new String[] {String.valueOf(serviceId)};

        // find row with the given serviceId in the database
        Cursor cursor = db.rawQuery(queryString, queryArgs);

        if (cursor.moveToFirst()) {

            // load data from found row
            int contactId = cursor.getInt(1);
            String serviceInfo = cursor.getString(2);
            int serviceCost = cursor.getInt(3);
            String serviceName = cursor.getString(4);
            float serviceTime = cursor.getFloat(5);

            // save loaded data (and serviceId) as object of ServiceModel class
            serviceModel = new ServiceModel(serviceId, contactId, serviceInfo,
                    serviceCost, serviceName, serviceTime);

        } else { // if record not found
            // create object of ServiceModel class with serviceId set to -1
            serviceModel = new ServiceModel(-1);
        }

        // close both the cursor and the database
        cursor.close();
        db.close();

        // return serviceModel with data of found service or serviceID set to -1 as an error code
        return serviceModel;
    }

    public boolean deleteService(ServiceModel serviceModel) {
        // Initialize boolean to return
        boolean success;

        // find service in the database. If it found, delete it and return true.
        // if it is not found, return false

        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "SELECT * FROM " + SERVICES + " WHERE " + COLUMN_SERVICE_ID + " = ?";
        String[] queryArgs = new String[] {String.valueOf(serviceModel.getServiceId())};

        Cursor cursor = db.rawQuery(queryString, queryArgs);

        if (cursor.moveToFirst()) {
            int deleted = db.delete(SERVICES, COLUMN_SERVICE_ID + " = ?",
                    new String[]{String.valueOf(serviceModel.getServiceId())});
            if (deleted > 0) { // deleted variable contains number of rows deleted
                success = true;
            } else {
                success = false;
            }
        } else {
            success = false;
        }

        // close both the cursor and the database
        cursor.close();
        db.close();

        return success;
    }

    public boolean deleteAllServicesForOneContact(int contactId) {
        // Initialize boolean to return
        boolean success;

        // find services in the database. If they are found, delete them and return true.
        // if they are not found, return false

        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "SELECT * FROM " + SERVICES + " WHERE " + COLUMN_CONTACT_ID + " = ?";
        String[] queryArgs = new String[] {String.valueOf(contactId)};

        Cursor cursor = db.rawQuery(queryString, queryArgs);

        if (cursor.moveToFirst()) {

            // delete all services with this contactId
            int deleted = db.delete(SERVICES, COLUMN_CONTACT_ID + " = ?",
                    new String[] {String.valueOf(contactId)});

            if (deleted == 0) { // if 0 rows were deleted it means that something went wrong
                success = false;
            } else {
                success = true;
            }
        } else { // when there are no rows found with contactId
            success = false;
        }

        // close both the cursor and the database
        cursor.close();
        db.close();

        return success;
    }

    public List<ServiceModel> getListOfAllServicesForOneContactFromDatabase(ContactModel contactModel) {
        List<ServiceModel> returnList = new ArrayList<>();

        // set the query to get data from database
        String queryString = "SELECT * FROM " + SERVICES + " WHERE " + COLUMN_CONTACT_ID + " = ?";
        String[] queryArgs = new String[] {String.valueOf(contactModel.getContactId())};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, queryArgs);

        if (cursor.moveToFirst()) { // cursor.moveToFirst() returns a true if there were items selected
            // loop through the cursor(results from DB) and create new ContactModel objects
            // put them into the returnList

            do {
                int serviceID = cursor.getInt(0);
                int contactID = cursor.getInt(1);
                String serviceInfo = cursor.getString(2);
                int serviceCost = cursor.getInt(3);
                String serviceName = cursor.getString(4);
                float serviceTime = cursor.getFloat(5);

                ServiceModel newService = new ServiceModel(serviceID, contactID, serviceInfo,
                        serviceCost, serviceName, serviceTime);
                returnList.add(newService);
            } while (cursor.moveToNext());
        } else {
            // do nothing, there are no services to add
        }

        // close both the cursor and the database
        cursor.close();
        db.close();

        return returnList;
    }
}
