package com.adambarczyk.contactsassistant;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class ContactDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void openContactNotesActivity(View view) {
        Intent intent = new Intent(ContactDetailActivity.this, NotesActivity.class);
        startActivity(intent);
    }

    public void openEditContactDetailsActivity(View view) {
        Intent intent = new Intent(ContactDetailActivity.this, EditContactDetailsActivity.class);
        startActivity(intent);
    }

    public void openServicesPanelActivity(View view) {
        Intent intent = new Intent(ContactDetailActivity.this, ServicesPanelActivity.class);
        startActivity(intent);
    }
}