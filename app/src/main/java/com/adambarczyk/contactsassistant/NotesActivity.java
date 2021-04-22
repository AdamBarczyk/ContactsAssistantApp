package com.adambarczyk.contactsassistant;

import android.os.Bundle;

import com.adambarczyk.contactsassistant.constant.Constant;
import com.adambarczyk.contactsassistant.datamodels.ContactModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class NotesActivity extends AppCompatActivity {

    private TextView tvNotes;

    private ContactModel contactModel;



    private void buildUI() {
        // Initialize text views
        tvNotes = findViewById(R.id.contact_notes_activity_text_field);

        // get contact from intent
        contactModel = (ContactModel) getIntent().getSerializableExtra(Constant.OLD_CONTACT_MODEL);

        // load content on the screen
        loadNotes();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // load content
        buildUI();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // load content
        buildUI();
    }

    private void loadNotes() {
        tvNotes.setText(contactModel.getNotes());
    }
}