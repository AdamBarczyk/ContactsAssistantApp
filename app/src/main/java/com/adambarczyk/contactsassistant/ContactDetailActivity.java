package com.adambarczyk.contactsassistant;

import android.content.Intent;
import android.os.Bundle;

import com.adambarczyk.contactsassistant.constant.Constant;
import com.adambarczyk.contactsassistant.datamodels.ContactModel;
import com.adambarczyk.contactsassistant.datamodels.ServiceModel;
import com.adambarczyk.contactsassistant.utilities.MathFunctions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ContactDetailActivity extends AppCompatActivity {


    // TextViews for contact's data
    private TextView contactNameTextView;
    private TextView contactPhoneTextView;
    private TextView contactEmailTextView;
    private TextView contactAddressTextView;
    private TextView contactNotesTextView;

    // TextViews for contact's services data
    private TextView servicesCountTextView;
    private TextView allServicesCostTextView;
    private TextView averageServicesCostPerHourTextView;
    private TextView averageServiceCostTextView;

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
        this.servicesCountTextView = findViewById(R.id.services_count);
        this.allServicesCostTextView = findViewById(R.id.all_services_cost);
        this.averageServiceCostTextView = findViewById(R.id.average_service_cost);
        this.averageServicesCostPerHourTextView = findViewById(R.id.average_services_cost_per_hour);

        // get contact using contact ID from intent
        int contactId = getIntent().getIntExtra(Constant.CONTACT_ID, Constant.ERROR);
        contactModel = dataBaseHelper.getContactById(contactId);

        // get services list
        servicesList = loadServicesFromDatabase();

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
            /* if phone number is equal to -1(ERROR), it means that contact hasn't got phone number,
             so there is no need to show -1 value to the user*/
            if (contactModel.getPhone() != Constant.ERROR) {
                contactPhoneTextView.setText(String.valueOf(contactModel.getPhone()));
            }
            contactEmailTextView.setText(contactModel.getEmail());
            contactAddressTextView.setText(contactModel.getAddress());
            contactNotesTextView.setText(contactModel.getNotes());


            // show contact's services data on screen

            servicesCountTextView.setText(String.valueOf(servicesList.size()));
            allServicesCostTextView.setText(
                    String.valueOf(MathFunctions.getCostOfAllServices(servicesList))
            );
            averageServiceCostTextView.setText(
                    String.valueOf(MathFunctions.getAverageCostPerServiceOfAllServices(servicesList))
            );
            averageServicesCostPerHourTextView.setText(
                    String.valueOf(MathFunctions.getAverageCostPerHourOfAllServices(servicesList))
            );

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
        intent.putExtra(Constant.OLD_CONTACT_MODEL, contactModel);
        startActivity(intent);
    }

    public void openEditContactDetailsActivity(View view) {

        // pass contact data to editing activity
        Intent intent = new Intent(ContactDetailActivity.this, EditContactDetailsActivity.class);
        intent.putExtra(Constant.OLD_CONTACT_MODEL, contactModel);
        intent.putExtra(Constant.ADD_OR_EDIT, Constant.TO_EDIT);
        startActivity(intent);
    }

    public void openServicesPanelActivity(View view) {
        Intent intent = new Intent(ContactDetailActivity.this, ServicesPanelActivity.class);
        intent.putExtra(Constant.CONTACT_MODEL, contactModel);
        startActivity(intent);
    }
}