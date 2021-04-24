package com.adambarczyk.contactsassistant;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.adambarczyk.contactsassistant.constant.Constant;
import com.adambarczyk.contactsassistant.datamodels.ContactModel;
import com.adambarczyk.contactsassistant.utilities.SortFunctions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity implements Filterable {

    LinearLayout linearLayoutForContacts;
    SearchView contactsSearchView;

    // id of long-clicked contact button (when user tries to open floating context menu)
    private int contactId;
    private List<ContactModel> contactsList;
    private List<ContactModel> contactsListFiltered;



    private void buildUI() {
        linearLayoutForContacts = findViewById(R.id.linearLayoutForContacts);
        linearLayoutForContacts.removeAllViews(); // clear layout before loading all contacts again

        // load contacts from database
        contactsList = loadContactsFromDatabase();

        // sort contacts alphabetically by contact name
        SortFunctions.sortContactsListInAlphabeticalOrder(contactsList);

        // create list for contacts filtered in SearchView
        contactsListFiltered = new ArrayList<>(contactsList);

        // show contacts on the screen
        if (!addButtonsForEachContact(contactsListFiltered, linearLayoutForContacts)) {
            Toast.makeText(this, R.string.unable_to_load_contacts, Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshUI() {
        // refreshes contacts on the screen to match with SearchView

        linearLayoutForContacts.removeAllViews();

        // show contacts on the screen
        if (!addButtonsForEachContact(contactsListFiltered, linearLayoutForContacts)) {
            Toast.makeText(this, R.string.unable_to_load_contacts, Toast.LENGTH_SHORT).show();
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
                intent.putExtra(Constant.ADD_OR_EDIT, Constant.TO_ADD);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getTag() == Constant.CONTACT_BUTTON) {
            getMenuInflater().inflate(R.menu.contact_context_menu, menu);

            // v.getId() in this case returns ID of long-clicked button before menu show up
            // Each button has got the same ID as the contact "inside" this button,
            // so buttonId can be used as contactId
            contactId = v.getId();
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(ContactsActivity.this);
        ContactModel contactModel;

        switch (item.getItemId()) {

            // editing contact using context menu

            case R.id.edit_contact_context_menu:
                Intent intent = new Intent(ContactsActivity.this, EditContactDetailsActivity.class);

                // get ContactModel of contact to edit and pass it to the EditContactDetailsActivity
                contactModel = dataBaseHelper.getContactById(contactId);

                intent.putExtra(Constant.OLD_CONTACT_MODEL, contactModel);
                intent.putExtra(Constant.ADD_OR_EDIT, Constant.TO_EDIT);
                startActivity(intent);
                return true;


            // deleting contact using context menu

            case R.id.delete_contact_context_menu:

                contactModel = dataBaseHelper.getContactById(contactId);

                if(dataBaseHelper.deleteContact(contactModel)) {
                    if (dataBaseHelper.deleteAllServicesForOneContact(contactId)) {
                        Toast.makeText(this, R.string.contact_removed_successfully,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, R.string.contact_removed_but_services_not,
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.contact_and_services_not_removed,
                            Toast.LENGTH_SHORT).show();
                }

                // refresh UI after deleting
                onRestart();

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void openContactDetailActivity(int contactId) {
        Intent intent = new Intent(ContactsActivity.this, ContactDetailActivity.class);
        intent.putExtra(Constant.CONTACT_ID, contactId);
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
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 20, 0, 0);

            // drawable icon
            Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_account_circle_24, getTheme());
            icon.setBounds(0, 0, 200, 200);

            //drawable button background
            Drawable background = getResources().getDrawable(R.drawable.button_background);

            button.setLayoutParams(layoutParams);
            button.setCompoundDrawables(icon, null, null, null);
            button.setId(contact.getContactId());
            button.setTag(Constant.CONTACT_BUTTON);
            button.setText(contact.getName());
            button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            button.setTextSize(24);
            button.setBackground(background);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openContactDetailActivity(contact.getContactId());
                }
            });

            // add button to layout
            linearLayout.addView(button);

            // add context menu for button
            registerForContextMenu(button);
        }
        return true;
    }


    // Methods to handle contacts SearchView

    @Override
    public Filter getFilter() {
        return contactsFilter;
    }

    private Filter contactsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // Invoked in the worker thread, so application won't freeze

            List<ContactModel> filteredList = new ArrayList<>();

            // constraint is a phrase written by user in the SearchView (as a CharSequence, not a String)
            if (constraint == null || constraint.length() == 0) {

                // add all contacts to the filteredList, because user didn't typed anything
                filteredList.addAll(contactsList);
            } else {

                // .trim() removes white spaces at the start and at the end of the String
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ContactModel contact : contactsList) {
                    if (contact.getName().toLowerCase().startsWith(filterPattern)) {
                        filteredList.add(contact);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // Invoked in the UI thread to publish the filtering results in the user interface.

            contactsListFiltered.clear();
            contactsListFiltered.addAll((List) results.values);
            refreshUI();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();  // now can use searchView like normal text field

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
}