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
        if (getIntent().getIntExtra(Constant.ADD_OR_EDIT, Constant.ERROR) == Constant.TO_EDIT) {
            ServiceModel serviceModel = (ServiceModel) getIntent().
                    getSerializableExtra(Constant.OLD_SERVICE_MODEL);

            etServiceName.setText(serviceModel.getServiceName());
            etServiceCost.setText(String.valueOf(serviceModel.getServiceCost()));
            etServiceTime.setText(String.valueOf(serviceModel.getServiceTime()));
            etServiceInfo.setText(serviceModel.getServiceInfo());
        } else {
            setTitle(R.string.title_activity_add_service_details);
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
        parentContactModel = (ContactModel) getIntent().getSerializableExtra(Constant.PARENT_CONTACT_MODEL);

        // load content
        buildUI();

        // button listeners

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getIntent().getIntExtra(Constant.ADD_OR_EDIT, Constant.ERROR) == Constant.TO_ADD) {
                    if (checkConditions()) {
                        addService();
                    }

                } else if (getIntent().getIntExtra(Constant.ADD_OR_EDIT, Constant.ERROR) == Constant.TO_EDIT) {
                    if (checkConditions()) {
                        updateService((ServiceModel) getIntent().
                                getSerializableExtra(Constant.OLD_SERVICE_MODEL));
                    }

                } else {
                    Toast.makeText(EditServiceDetailsActivity.this,
                            R.string.unable_to_add_or_edit_service, Toast.LENGTH_SHORT).show();
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

    private boolean checkConditions() {
        // Checks if user provided new contact name or left it empty.
        // If user provided the contact name, function returns true. Otherwise returns false.

        if (etServiceName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.empty_service_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (etServiceCost.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.empty_service_cost, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (etServiceTime.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.empty_service_time, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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
                Toast.makeText(this, R.string.service_added_successfully,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.unable_to_add_service,
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

            // show result notification to the user
            if (success) {
                Toast.makeText(this, R.string.service_updated,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.unable_to_update_service,
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Exception:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        finish(); // going back to previous activity
    }
}