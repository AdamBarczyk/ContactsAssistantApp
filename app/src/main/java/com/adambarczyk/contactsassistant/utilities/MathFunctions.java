package com.adambarczyk.contactsassistant.utilities;

import com.adambarczyk.contactsassistant.datamodels.ServiceModel;

import java.util.List;

public class MathFunctions {


    /**
     * Function returns cost of all services in servicesList
     * @param servicesList list containing objects of ServiceModel class
     * @return cost of all services in the servicesList
     */
    public static long getCostOfAllServices(List<ServiceModel> servicesList) {
        long sum = 0;
        for (ServiceModel serviceModel : servicesList) {
            sum += serviceModel.getServiceCost();
        }
        return sum;
    }


    /**
     * Function returns time of all services in servicesList
     * @param servicesList the list containing objects of ServiceModel class
     * @return time in hours of all services in servicesList
     */
    public static double getTimeOfAllServices(List<ServiceModel> servicesList) {

        long minutes = 0;
        for (ServiceModel serviceModel : servicesList) {
            minutes += serviceModel.getServiceTime();
        }

        return (double) minutes / 60;
    }


    /**
     * Function gets cost of all services and divides by time of all services in servicesList
     * @param servicesList the list containing objects of ServiceModel class
     * @return rounded AVERAGE cost per hour for one service in servicesList
     */
    public static long getAverageCostPerHourOfAllServices(List<ServiceModel> servicesList) {
        double result = getCostOfAllServices(servicesList) / getTimeOfAllServices(servicesList);
        return Math.round(result);
    }


    /**
     * Function gets cost of all services and divides by number of services in servicesList
     * @param servicesList the list containing objects of ServiceModel class
     * @return rounded AVERAGE cost per service for one service in servicesList
     */
    public static long getAverageCostPerServiceOfAllServices(List<ServiceModel> servicesList) {
        double result = (double) getCostOfAllServices(servicesList) / servicesList.size();
        return Math.round(result);
    }
}
