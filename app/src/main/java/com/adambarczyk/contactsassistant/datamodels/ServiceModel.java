package com.adambarczyk.contactsassistant.datamodels;

public class ServiceModel {
    private int serviceId;
    private int contactId;
    private String serviceInfo;
    private int serviceCost;

    // constructors

    public ServiceModel(int serviceId, int contactId, String serviceInfo, int serviceCost) {
        this.serviceId = serviceId;
        this.contactId = contactId;
        this.serviceInfo = serviceInfo;
        this.serviceCost = serviceCost;
    }

    public ServiceModel(int errorServiceId) {
        this.serviceId = errorServiceId;
    }


    // toString() for printing the contents of a class object

    @Override
    public String toString() {
        return "ServicesModel{" +
                "serviceId=" + serviceId +
                ", contactId=" + contactId +
                ", serviceInfo=" + serviceInfo +
                ", serviceCost=" + serviceCost +
                '}';
    }


    // getters and setters

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(String serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public int getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(int serviceCost) {
        this.serviceCost = serviceCost;
    }
}
