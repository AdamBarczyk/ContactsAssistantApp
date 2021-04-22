package com.adambarczyk.contactsassistant;

import android.content.Intent;
import android.os.Bundle;

import com.adambarczyk.contactsassistant.constant.Constant;
import com.adambarczyk.contactsassistant.datamodels.ContactModel;
import com.adambarczyk.contactsassistant.datamodels.ServiceModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ContactDetailActivity extends AppCompatActivity {

    private TextView contactNameTextView;
    private TextView contactPhoneTextView;
    private TextView contactEmailTextView;
    private TextView contactAddressTextView;
    private TextView contactNotesTextView;

    private DataBaseHelper dataBaseHelper;

    private ContactModel contactModel;
    private List<ServiceModel> servicesList; // list of services for this contact



    private void buildUI() {
        // Initialize text views
        this.contactNameTextView = findViewById(R.id.contactDetailName);
        this.contactPhoneTextView = findViewById(R.id.contactDetailPhone);
        this.contactEmailTextView = findViewById(R.id.contactDetailEmail);
        this.contactAddressTextView = findViewById(R.id.contactDetailAddress);
        this.contactNotesTextView = findViewById(R.id.contactDetailNotes);

        // get contact using contact ID from intent
        int contactId = getIntent().getIntExtra("contactId", Constant.ERROR);
        contactModel = dataBaseHelper.getContactById(contactId);

        // load content on the screen
        if (!loadContactData()) {
            Toast.makeText(this, R.string.unable_to_load_data_message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get DataBaseHelper
        dataBaseHelper = new DataBaseHelper(ContactDetailActivity.this);

        // load content
        buildUI();

        // get services list
        servicesList = loadServicesFromDatabase();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // load content
        buildUI();
    }

    private boolean loadContactData() {

        if (contactModel != null) {

            // show contact data on screen
            contactNameTextView.setText(contactModel.getName());
            contactPhoneTextView.setText(String.valueOf(contactModel.getPhone()));
            contactEmailTextView.setText(contactModel.getEmail());
            contactAddressTextView.setText(contactModel.getAddress());
            contactNotesTextView.setText(contactModel.getNotes());

        } else {
            return false;
        }
        return true;
    }

    private List<ServiceModel> loadServicesFromDatabase() {
        return dataBaseHelper.getListOfAllServicesForOneContactFromDatabase(contactModel);
    }

    public void openContactNotesActivity(View view) {

        // pass contact data to notes activity
        Intent intent = new Intent(ContactDetailActivity.this, NotesActivity.class);
        intent.putExtra("oldContactModel", contactModel);
        startActivity(intent);
    }

    public void openEditContactDetailsActivity(View view) {

        // pass contact data to editing activity
        Intent intent = new Intent(ContactDetailActivity.this, EditContactDetailsActivity.class);
        intent.putExtra("oldContactModel", contactModel);
        intent.putExtra("addOrEdit", Constant.TO_EDIT);
        startActivity(intent);
    }

    public void openServicesPanelActivity(View view) {
        Intent intent = new Intent(ContactDetailActivity.this, ServicesPanelActivity.class);
        intent.putExtra("contactModel", contactModel);
        startActivity(intent);
    }
}