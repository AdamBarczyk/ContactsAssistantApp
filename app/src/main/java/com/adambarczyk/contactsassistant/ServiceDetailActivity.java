package com.adambarczyk.contactsassistant;

import android.content.Intent;
import android.os.Bundle;

import com.adambarczyk.contactsassistant.constant.Constant;
import com.adambarczyk.contactsassistant.datamodels.ContactModel;
import com.adambarczyk.contactsassistant.datamodels.ServiceModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceDetailActivity extends AppCompatActivity {

    private TextView tvServiceName, tvServiceCost, tvServiceTime, tvServiceInfo;

    DataBaseHelper dataBaseHelper;
    ServiceModel serviceModel;
    ContactModel parentContactModel;


    private void buildUI() {

        // initialize TextFields on the screen
        this.tvServiceName = findViewById(R.id.service_detail_name);
        this.tvServiceCost = findViewById(R.id.service_detail_cost);
        this.tvServiceTime = findViewById(R.id.service_detail_time);
        this.tvServiceInfo = findViewById(R.id.service_detail_info);

        // get service using service ID from ServicesPanelActivity sent via intent
        int serviceId = getIntent().getIntExtra(Constant.SERVICE_ID, Constant.ERROR);
        serviceModel = dataBaseHelper.getServiceById(serviceId);

        // get parentContact for service from ServicesPanelActivity sent via intent
        parentContactModel = (ContactModel) getIntent().getSerializableExtra(Constant.PARENT_CONTACT_MODEL);

        //load content on the screen
        if (!loadServiceData()) {
            Toast.makeText(this, R.string.unable_to_load_data_message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get DataBaseHelper
        dataBaseHelper = new DataBaseHelper(this);

        // load content
        buildUI();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // load content
        buildUI();
    }

    private boolean loadServiceData() {

        // show service data on the screen
        if (serviceModel != null) {
            tvServiceName.setText(serviceModel.getServiceName());
            String value = serviceModel.getServiceCost() + " zł";
            tvServiceCost.setText(value);
            tvServiceTime.setText(String.valueOf(serviceModel.getServiceTime()));
            tvServiceInfo.setText(serviceModel.getServiceInfo());
        } else {
            return false;
        }
        return true;
    }

    public void openEditServiceDetailActivity(View view) {

        // pass service data to editing activity
        Intent intent = new Intent(this, EditServiceDetailsActivity.class);
        intent.putExtra(Constant.OLD_SERVICE_MODEL, serviceModel);
        intent.putExtra(Constant.PARENT_CONTACT_MODEL, parentContactModel);
        intent.putExtra(Constant.ADD_OR_EDIT, Constant.TO_EDIT);
        startActivity(intent);
    }
}