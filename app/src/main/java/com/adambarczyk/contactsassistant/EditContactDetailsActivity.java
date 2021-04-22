package com.adambarczyk.contactsassistant;

import android.os.Bundle;

import com.adambarczyk.contactsassistant.constant.Constant;
import com.adambarczyk.contactsassistant.datamodels.ContactModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditContactDetailsActivity extends AppCompatActivity {

    // references to buttons and other controls on the layout
    EditText etContactName, etContactPhone, etContactEmail, etContactAddress, etContactNotes;
    Button btnSave, btnCancel;



    private void buildUI() {

        //Initialize Buttons and EditTexts on the layout
        etContactName = findViewById(R.id.editContactName);
        etContactPhone = findViewById(R.id.editPhoneNumber);
        etContactEmail = findViewById(R.id.editEmailAddress);
        etContactAddress = findViewById(R.id.editAddress);
        etContactNotes = findViewById(R.id.editContactNotes);
        btnSave = findViewById(R.id.btnSaveContact);
        btnCancel = findViewById(R.id.btnCancelContact);


        // If this activity is used to edit existing contact,
        // fill all views on the screen with existing contact data
        if (getIntent().getIntExtra("addOrEdit", Constant.ERROR) == Constant.TO_EDIT) {
            ContactModel oldContactModel = (ContactModel) getIntent().
                    getSerializableExtra("oldContactModel");

            etContactName.setText(oldContactModel.getName());
            etContactEmail.setText(oldContactModel.getEmail());
            etContactPhone.setText(String.valueOf(oldContactModel.getPhone()));
            etContactAddress.setText(oldContactModel.getAddress());
            etContactNotes.setText(oldContactModel.getNotes());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // load content
        buildUI();


        // button listeners

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getIntent().getIntExtra("addOrEdit", Constant.ERROR) == Constant.TO_ADD) {
                    addContact();
                } else if (getIntent().getIntExtra("addOrEdit", Constant.ERROR) == Constant.TO_EDIT) {
                    updateContact((ContactModel) getIntent().getSerializableExtra("oldContactModel"));
                } else {
                    Toast.makeText(EditContactDetailsActivity.this,
                            "couldn't neither add or edit the contact", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // going back to previous activity
            }
        });
    }

    private void addContact() {
        ContactModel contactModel;

        try {
            // get data
            contactModel = new ContactModel(
                    -1,
                    etContactName.getText().toString(),
                    etContactEmail.getText().toString(),
                    Integer.parseInt(etContactPhone.getText().toString()),
                    etContactAddress.getText().toString(),
                    etContactNotes.getText().toString());

            // save the contact to the database
            DataBaseHelper dataBaseHelper = new DataBaseHelper(EditContactDetailsActivity.this);
            boolean success = dataBaseHelper.addContact(contactModel);

            // show result notification to the user
            if (success) {
                Toast.makeText(this, "Contact has been added",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Couldn't add the contact",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // show result notification to the user
            Toast.makeText(this, R.string.too_large_number_message, Toast.LENGTH_SHORT).show();
        }

        finish(); // going back to previous activity
    }

    private void updateContact(ContactModel oldContactModel) {
        ContactModel updatedContactModel;

        try {
            // get data to update
            updatedContactModel = new ContactModel(
                    oldContactModel.getContactId(), // passing the ID of the contact being updating
                    etContactName.getText().toString(),
                    etContactEmail.getText().toString(),
                    Integer.parseInt(etContactPhone.getText().toString()),
                    etContactAddress.getText().toString(),
                    etContactNotes.getText().toString()
            );

            // update contact in the database
            DataBaseHelper dataBaseHelper = new DataBaseHelper(EditContactDetailsActivity.this);
            boolean success = dataBaseHelper.updateContact(updatedContactModel);
            Toast.makeText(this, updatedContactModel.toString(), Toast.LENGTH_SHORT).show();

            // show result notification to the user
            if (success) {
                Toast.makeText(this, "Contact has been updated",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Couldn't update the contact",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        finish(); // going back to previous activity
    }
}