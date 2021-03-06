package com.adambarczyk.contactsassistant;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.adambarczyk.contactsassistant.constant.Constant;
import com.adambarczyk.contactsassistant.datamodels.ContactModel;
import com.adambarczyk.contactsassistant.datamodels.ServiceModel;
import com.adambarczyk.contactsassistant.utilities.SortFunctions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ServicesPanelActivity extends AppCompatActivity {

    LinearLayout linearLayoutForAllServices;

    List<ServiceModel> servicesList;
    ContactModel contactModel;

    // id of long-clicked service button (when user tries to open floating context menu)
    private int serviceId;


    private void buildUI() {
        linearLayoutForAllServices = findViewById(R.id.linear_layout_for_all_services);
        linearLayoutForAllServices.removeAllViews(); // clear layout before loading all contacts again

        // load parent contact for services
        contactModel = (ContactModel) getIntent().getSerializableExtra(Constant.CONTACT_MODEL);

        // load services for parent contact
        servicesList = loadServicesFromDatabase();

        // sort services alphabetically by service name
        SortFunctions.sortServicesListInAlphabeticalOrder(servicesList);

        // show services on the screen
        if (!addViewForEachService(servicesList)) {
            Toast.makeText(this, R.string.unable_to_load_services, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_panel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServicesPanelActivity.this, EditServiceDetailsActivity.class);
                intent.putExtra(Constant.ADD_OR_EDIT, Constant.TO_ADD);
                intent.putExtra(Constant.PARENT_CONTACT_MODEL, contactModel);
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

        if (v.getTag() == Constant.SERVICE_BUTTON) {
            getMenuInflater().inflate(R.menu.service_context_menu, menu);

            // v.getId() in this case returns ID of long-clicked button before menu show up
            // Each button has got the same ID as the service "inside" this button,
            // so buttonId can be used as serviceId
            serviceId = v.getId();
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(ServicesPanelActivity.this);
        ServiceModel serviceModel;

        switch (item.getItemId()) {

            // editing service using context menu

            case R.id.edit_service_context_menu:
                Intent intent = new Intent(ServicesPanelActivity.this, EditServiceDetailsActivity.class);

                // get ServiceModel of service to edit and pass it to the EditServiceDetailsActivity
                serviceModel = dataBaseHelper.getServiceById(serviceId);

                intent.putExtra(Constant.OLD_SERVICE_MODEL, serviceModel);
                intent.putExtra(Constant.PARENT_CONTACT_MODEL, contactModel);
                intent.putExtra(Constant.ADD_OR_EDIT, Constant.TO_EDIT);
                startActivity(intent);
                return true;


            // deleting contact using context menu

            case R.id.delete_service_context_menu:

                serviceModel = dataBaseHelper.getServiceById(serviceId);

                if (dataBaseHelper.deleteService(serviceModel)) {
                    Toast.makeText(this, R.string.service_removed_successfully,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.unable_to_remove_service,
                            Toast.LENGTH_SHORT).show();
                }

                //refresh UI after deleting
                onRestart();

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void openServiceDetailActivity(View view) {
        Intent intent = new Intent(ServicesPanelActivity.this, ServiceDetailActivity.class);
        startActivity(intent);
    }

    public void openServiceDetailActivity(int serviceId) {
        Intent intent = new Intent(ServicesPanelActivity.this, ServiceDetailActivity.class);
        intent.putExtra(Constant.SERVICE_ID, serviceId);
        intent.putExtra(Constant.PARENT_CONTACT_MODEL, contactModel);
        startActivity(intent);
    }

    private List<ServiceModel> loadServicesFromDatabase() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(ServicesPanelActivity.this);
        return dataBaseHelper.getListOfAllServicesForOneContactFromDatabase(contactModel);
    }

    private boolean addViewForEachService(List<ServiceModel> servicesList) {

        // for each service in the database belonging to this particular contact:
        // adds new LinearLayout (horizontal) with button on the left side of the LinearLayout
        // and a text view with service cos on the right side of the LinearLayout


        for (final ServiceModel service : servicesList) {

            // create TextView
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams tvLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    4.0f
            );
            textView.setLayoutParams(tvLayoutParams);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setGravity(Gravity.CENTER);
            String value = service.getServiceCost() + "z??";
            textView.setText(value);
            textView.setTextSize(18);

            // create button

            //drawable button background
            Drawable background = getResources().getDrawable(R.drawable.button_background);

            Button button = new Button(this);
            LinearLayout.LayoutParams btnLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            btnLayoutParams.setMargins(0, 20, 0, 0);
            button.setId(service.getServiceId());
            button.setTag(Constant.SERVICE_BUTTON);
            button.setLayoutParams(btnLayoutParams);
            button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            button.setText(service.getServiceName());
            button.setTextSize(18);
            button.setBackground(background);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openServiceDetailActivity(service.getServiceId());
                }
            });

            // create LinearLayout as a container for TextView and Button created above
            LinearLayout linearLayoutContainer = new LinearLayout(this);
            LinearLayout.LayoutParams llcLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            linearLayoutContainer.setLayoutParams(llcLayoutParams);
            linearLayoutContainer.setOrientation(LinearLayout.HORIZONTAL);

            // add a TextView and a Button to the LinearLayout created above
            linearLayoutContainer.addView(button);
            linearLayoutContainer.addView(textView);

            // add LinearLayout (container) to parent LinearLayout containing all services
            linearLayoutForAllServices.addView(linearLayoutContainer);

            // add context menu for button
            // context menu needs ID from button, so it must be registered to button!
            registerForContextMenu(button);
        }
        return true;
    }
}