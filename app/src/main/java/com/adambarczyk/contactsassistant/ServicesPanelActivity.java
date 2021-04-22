package com.adambarczyk.contactsassistant;

import android.content.Intent;
import android.os.Bundle;

import com.adambarczyk.contactsassistant.constant.Constant;
import com.adambarczyk.contactsassistant.datamodels.ContactModel;
import com.adambarczyk.contactsassistant.datamodels.ServiceModel;
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
        contactModel = (ContactModel) getIntent().getSerializableExtra("contactModel");

        // load services for parent contact
        servicesList = loadServicesFromDatabase();

        // show services on the screen
        if (!addViewForEachService(servicesList)) {
            Toast.makeText(this, "Couldn't load services", Toast.LENGTH_SHORT).show();
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
                intent.putExtra("addOrEdit", Constant.TO_ADD);
                intent.putExtra("parentContactModel", contactModel);
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
        Toast.makeText(this, "dupa", Toast.LENGTH_SHORT).show();

        if (v.getTag() == "serviceButton") {
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

                intent.putExtra("oldServiceModel", serviceModel);
                intent.putExtra("parentContactModel", contactModel);
                intent.putExtra("addOrEdit", Constant.TO_EDIT);
                startActivity(intent);
                return true;


            // deleting contact using context menu

            case R.id.delete_service_context_menu:

                serviceModel = dataBaseHelper.getServiceById(serviceId);

                if (dataBaseHelper.deleteService(serviceModel)) {
                    Toast.makeText(this, "Service removed successfully",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failure! \n Couldn't remove the service",
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
        intent.putExtra("serviceId", serviceId);
        intent.putExtra("parentContactModel", contactModel);
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
            String value = service.getServiceCost() + "zł";
            textView.setText(value);
            textView.setTextSize(18);

            // create button
            Button button = new Button(this);
            LinearLayout.LayoutParams btnLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            button.setId(service.getServiceId());
            button.setTag("serviceButton");
            button.setLayoutParams(btnLayoutParams);
            button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            button.setText(service.getServiceName());
            button.setTextSize(18);
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