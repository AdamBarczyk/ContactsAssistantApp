package com.adambarczyk.contactsassistant;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.adambarczyk.contactsassistant.datamodels.ContactModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    LinearLayout linearLayoutForContacts;

    private void buildUI() {
        // load content

        linearLayoutForContacts = findViewById(R.id.linearLayoutForContacts);
        linearLayoutForContacts.removeAllViews(); // clear layout before loading all contacts again
        List<ContactModel> contactsList = loadContactsFromDatabase();
        if (!addButtonsForEachContact(contactsList, linearLayoutForContacts)) {
            Toast.makeText(this, "Couldn't load contacts", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsActivity.this, EditContactDetailsActivity.class);
                startActivity(intent);
            }
        });

        // load content
        buildUI();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // load content
        buildUI();
    }

    public void openContactDetailActivity(View view){
        Intent intent = new Intent(ContactsActivity.this, ContactDetailActivity.class);
        startActivity(intent);
    }

    public void openContactDetailActivity(int contactID) {
        Intent intent = new Intent(ContactsActivity.this, ContactDetailActivity.class);
        intent.putExtra("contactID", contactID);
        startActivity(intent);
    }

    private List<ContactModel> loadContactsFromDatabase() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(ContactsActivity.this);
        return dataBaseHelper.getListOfContactsFromDatabase();
    }

    private boolean addButtonsForEachContact(List<ContactModel> contactsList, LinearLayout linearLayout) {
        for (final ContactModel contact : contactsList) {
            Button button = new Button(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            Drawable img = getResources().getDrawable(R.drawable.ic_baseline_account_circle_24, getTheme());
            img.setBounds(0, 0, 200, 200);

            button.setLayoutParams(layoutParams);
            button.setCompoundDrawables(img, null, null, null);
            button.setId(contact.getContactId());
            button.setText(contact.getName());
            button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            button.setTextSize(24);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openContactDetailActivity(contact.getContactId());
                }
            });

            // add button to layout
            linearLayout.addView(button);
        }
        return true;
    }
}