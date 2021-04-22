package com.adambarczyk.contactsassistant;

import android.os.Bundle;

import com.adambarczyk.contactsassistant.constant.Constant;
import com.adambarczyk.contactsassistant.datamodels.ContactModel;
import com.adambarczyk.contactsassistant.datamodels.ServiceModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditServiceDetailsActivity extends AppCompatActivity {

    // references to buttons and other controls on the layout
    EditText etServiceName, etServiceCost, etServiceTime, etServiceInfo;
    Button btnSave, btnCancel;

    // parent contact for this service
    ContactModel parentContactModel;



    private void buildUI() {

        //Initialize Buttons and EditTexts on the layout
        etServiceName = findViewById(R.id.editServiceName);
        etServiceCost = findViewById(R.id.editServiceCost);
        etServiceTime = findViewById(R.id.editServiceTime);
        etServiceInfo = findViewById(R.id.editServiceInfo);
        btnSave = findViewById(R.id.btnSaveService);
        btnCancel = findViewById(R.id.btnCancelService);

        // If this activity is used to edit existing service,
        // fill all views on the screen with existing service data
        if (getIntent().getIntExtra("addOrEdit", Constant.ERROR) == Constant.TO_EDIT) {
            ServiceModel serviceModel = (ServiceModel) getIntent().
                    getSerializableExtra("oldServiceModel");

            etServiceName.setText(serviceModel.getServiceName());
            etServiceCost.setText(String.valueOf(serviceModel.getServiceCost()));
            etServiceTime.setText(String.valueOf(serviceModel.getServiceTime()));
            etServiceInfo.setText(serviceModel.getServiceInfo());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // load parent contact. There is no need to load it with UI refresh (buildUI() method),
        // because parent contact for this service won't change
        parentContactModel = (ContactModel) getIntent().getSerializableExtra("parentContactModel");

        // load content
        buildUI();

        // button listeners

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getIntent().getIntExtra("addOrEdit", Constant.ERROR) == Constant.TO_ADD) {
                    addService();
                } else if (getIntent().getIntExtra("addOrEdit", Constant.ERROR) == Constant.TO_EDIT) {
                    updateService((ServiceModel) getIntent().getSerializableExtra("oldServiceModel"));
                } else {
                    Toast.makeText(EditServiceDetailsActivity.this,
                            "Couldn't neither add or edit he service", Toast.LENGTH_SHORT).show();
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

    private void addService() {
        ServiceModel serviceModel;

        try {

            // get data about service
            serviceModel = new ServiceModel(
                    -1,
                    parentContactModel.getContactId(),
                    etServiceInfo.getText().toString(),
                    Integer.parseInt(etServiceCost.getText().toString()),
                    etServiceName.getText().toString(),
                    Integer.parseInt(etServiceTime.getText().toString())
            );

            // save the service to the database
            DataBaseHelper dataBaseHelper = new DataBaseHelper(EditServiceDetailsActivity.this);
            boolean success = dataBaseHelper.addService(serviceModel);

            // show result notification to the user
            if (success) {
                Toast.makeText(this, "Service has been added",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Couldn't add the service",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // show result notification to the user
            Toast.makeText(this, R.string.too_large_number_message, Toast.LENGTH_SHORT).show();
        }

        finish(); // going back to previous activity
    }

    private void updateService(ServiceModel oldServiceModel) {
        ServiceModel updatedServiceModel;

        try {
            // get data to update
            updatedServiceModel = new ServiceModel(
                    oldServiceModel.getServiceId(), // passing the ID of the service being updating
                    parentContactModel.getContactId(),
                    etServiceInfo.getText().toString(),
                    Integer.parseInt(etServiceCost.getText().toString()),
                    etServiceName.getText().toString(),
                    Integer.parseInt(etServiceTime.getText().toString())
            );

            // update contact in the database
            DataBaseHelper dataBaseHelper = new DataBaseHelper(EditServiceDetailsActivity.this);
            boolean success = dataBaseHelper.updateService(updatedServiceModel);
            Toast.makeText(this, updatedServiceModel.toString(), Toast.LENGTH_SHORT).show();

            // show result notification to the user
            if (success) {
                Toast.makeText(this, "Service has been updated",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Couldn't update the service",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        finish(); // going back to previous activity
    }
}