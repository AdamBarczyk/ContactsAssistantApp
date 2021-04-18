package com.adambarczyk.contactsassistant;

import android.os.Bundle;

import com.adambarczyk.contactsassistant.datamodels.ContactModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Parcelable;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditContactDetailsActivity extends AppCompatActivity {

    // references to buttons and other controls on the layout
    EditText et_contactName, et_phoneNumber, et_emailAddress, et_address, et_contactNotes;
    Button btn_save, btn_cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et_contactName = findViewById(R.id.editContactName);
        et_phoneNumber = findViewById(R.id.editPhoneNumber);
        et_emailAddress = findViewById(R.id.editEmailAddress);
        et_address = findViewById(R.id.editAddress);
        et_contactNotes = findViewById(R.id.editContactNotes);
        btn_save = findViewById(R.id.btnSave);
        btn_cancel = findViewById(R.id.btnCancel);

        // button listeners

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContactModel contactModel;

                try {
                    // get data
                    contactModel = new ContactModel(
                            -1,
                            et_contactName.getText().toString(),
                            et_emailAddress.getText().toString(),
                            Integer.parseInt(et_phoneNumber.getText().toString()),
                            et_address.getText().toString(),
                            et_contactNotes.getText().toString());

                    // save to database
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(EditContactDetailsActivity.this);
                    boolean success = dataBaseHelper.addContact(contactModel);

                    // show result notification to the user
                    Toast.makeText(EditContactDetailsActivity.this, "Success=" + success, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // show result notification to the user
                    Toast.makeText(EditContactDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                finish(); // going back to previous activity

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // going back to previous activity
            }
        });
    }
}