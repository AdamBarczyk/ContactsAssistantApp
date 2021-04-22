package com.adambarczyk.contactsassistant.datamodels;

import java.io.Serializable;

public class ServiceModel implements Serializable {
    private int serviceId;
    private int contactId;
    private String serviceInfo;
    private int serviceCost;
    private String serviceName;
    private int serviceTime;

    // constructors

    public ServiceModel(int serviceId, int contactId, String serviceInfo,
                        int serviceCost, String serviceName, int serviceTime) {
        this.serviceId = serviceId;
        this.contactId = contactId;
        this.serviceInfo = serviceInfo;
        this.serviceCost = serviceCost;
        this.serviceName = serviceName;
        this.serviceTime = serviceTime;
    }

    public ServiceModel(int errorServiceId) {
        this.serviceId = errorServiceId;
    }


    // toString() for printing the contents of a class object

    @Override
    public String toString() {
        return "ServiceModel{" +
                "serviceId=" + serviceId +
                ", contactId=" + contactId +
                ", serviceInfo='" + serviceInfo + '\'' +
                ", serviceCost=" + serviceCost +
                ", serviceName='" + serviceName + '\'' +
                ", serviceTime=" + serviceTime +
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }
}
