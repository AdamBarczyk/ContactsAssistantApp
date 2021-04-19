package com.adambarczyk.contactsassistant;

import android.content.Intent;
import android.os.Bundle;

import com.adambarczyk.contactsassistant.datamodels.ContactModel;
import com.adambarczyk.contactsassistant.datamodels.ServiceModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ServicesPanelActivity extends AppCompatActivity {

    LinearLayout linearLayoutForAllServices;
    ContactModel contactModel;


    private void buildUI() {
        linearLayoutForAllServices = findViewById(R.id.linear_layout_for_all_services);
        linearLayoutForAllServices.removeAllViews(); // clear layout before loading all contacts again

        // load parent contact for services
        contactModel = (ContactModel) getIntent().getSerializableExtra("contactModel");

        // load services for parent contact
        List<ServiceModel> servicesList = loadServicesFromDatabase();

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

        // load content
        buildUI();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // load content
        buildUI();
    }

    public void openServiceDetailActivity(View view) {
        Intent intent = new Intent(ServicesPanelActivity.this, ServiceDetailActivity.class);
        startActivity(intent);
    }

    public void openServiceDetailActivity(int serviceId) {
        Intent intent = new Intent(ServicesPanelActivity.this, ServiceDetailActivity.class);
        intent.putExtra("serviceId", serviceId);
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
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    4.0f
            );
            textView.setLayoutParams(tvLayoutParams);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setText(service.getServiceCost());
            textView.setTextSize(18);

            // create button
            Button button = new Button(this);
            LinearLayout.LayoutParams btnLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            button.setId(service.getServiceId());
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
            LinearLayout.LayoutParams llLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            linearLayoutContainer.setOrientation(LinearLayout.HORIZONTAL);

            // add a TextView and a Button to the LinearLayout created above
            linearLayoutContainer.addView(button);
            linearLayoutContainer.addView(textView);

            // add LinearLayout (container) to parent LinearLayout containing all services
            linearLayoutForAllServices.addView(linearLayoutContainer);
        }
        return true;
    }
}