package com.adambarczyk.contactsassistant.utilities;

import com.adambarczyk.contactsassistant.datamodels.ServiceModel;

import java.util.List;

public class MathFunctions {

    public static long getCostOfAllServices(List<ServiceModel> servicesList) {
        long sum = 0;
        for (ServiceModel serviceModel : servicesList) {
            sum += serviceModel.getServiceCost();
        }
        return sum;
    }

    public static double getTimeOfAllServices(List<ServiceModel> servicesList) {

        // Function returns time in hours

        long minutes = 0;
        for (ServiceModel serviceModel : servicesList) {
            minutes += serviceModel.getServiceTime();
        }

        return (double) minutes / 60;
    }

    public static long getAverageCostPerHourOfAllServices(List<ServiceModel> servicesList) {
        double result = getCostOfAllServices(servicesList) / getTimeOfAllServices(servicesList);
        return Math.round(result);
    }

    public static long getAverageCostPerServiceOfAllServices(List<ServiceModel> servicesList) {
        double result = (double) getCostOfAllServices(servicesList) / servicesList.size();
        return Math.round(result);
    }
}
