package com.adambarczyk.contactsassistant.utilities;

import android.content.Context;

import com.adambarczyk.contactsassistant.DataBaseHelper;
import com.adambarczyk.contactsassistant.datamodels.ContactModel;
import com.adambarczyk.contactsassistant.datamodels.ServiceModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortFunctions {

    /**
     * Sorts contacts list by contact name in alphabetical order
     * @param contactsList list containing objects of ContactModel class
     */
    public static void sortContactsListInAlphabeticalOrder(List<ContactModel> contactsList) {

        Collections.sort(contactsList, new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel contact1, ContactModel contact2) {
                return contact1.getName().toLowerCase().compareTo(contact2.getName().toLowerCase());
            }
        });
    }


    /**
     * Sorts contacts list by number of services owned by each contact in ascending order
     * @param contactsList list containing objects of ContactModel class
     * @param context context of the current activity
     */
    public static void sortContactsListByServicesCountInAscendingOrder(
            List<ContactModel> contactsList,
            Context context
    ) {

        final DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

        Collections.sort(contactsList, new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel contact1, ContactModel contact2) {
                List<ServiceModel> servicesListForContact1 = dataBaseHelper.
                        getListOfAllServicesForOneContactFromDatabase(contact1);
                List<ServiceModel> servicesListForContact2 = dataBaseHelper.
                        getListOfAllServicesForOneContactFromDatabase(contact2);

                return servicesListForContact1.size() - servicesListForContact2.size();
            }
        });
    }

    /**
     * Sorts contacts list by number of services owned by each contact in descending order
     * @param contactsList list containing objects of ContactModel class
     * @param context context of the current activity
     */
    public static void sortContactsListByServicesCountInDescendingOrder(
            List<ContactModel> contactsList,
            Context context
    ) {

        final DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

        Collections.sort(contactsList, new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel contact1, ContactModel contact2) {
                List<ServiceModel> servicesListForContact1 = dataBaseHelper.
                        getListOfAllServicesForOneContactFromDatabase(contact1);
                List<ServiceModel> servicesListForContact2 = dataBaseHelper.
                        getListOfAllServicesForOneContactFromDatabase(contact2);

                return servicesListForContact2.size() - servicesListForContact1.size();
            }
        });
    }

    /**
     * Sorts contacts list by cost of all services owned by each contact in ascending order
     * @param contactsList list containing objects of ContactModel class
     * @param context context of the current activity
     */
    public static void sortContactsListByAllServicesCostInAscendingOrder(
            List<ContactModel> contactsList,
            Context context
    ) {

        final DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

        Collections.sort(contactsList, new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel contact1, ContactModel contact2) {
                List<ServiceModel> servicesListForContact1 = dataBaseHelper.
                        getListOfAllServicesForOneContactFromDatabase(contact1);
                List<ServiceModel> servicesListForContact2 = dataBaseHelper.
                        getListOfAllServicesForOneContactFromDatabase(contact2);

                long allServicesCostForContact1 = MathFunctions.getCostOfAllServices(servicesListForContact1);
                long allServicesCostForContact2 = MathFunctions.getCostOfAllServices(servicesListForContact2);

                return (int) (allServicesCostForContact1 -  allServicesCostForContact2);
            }
        });
    }

    /**
     * Sorts contacts list by cost of all services owned by each contact in descending order
     * @param contactsList list containing objects of ContactModel class
     * @param context context of the current activity
     */
    public static void sortContactsListByAllServicesCostInDescendingOrder(
            List<ContactModel> contactsList,
            Context context
    ) {

        final DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

        Collections.sort(contactsList, new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel contact1, ContactModel contact2) {
                List<ServiceModel> servicesListForContact1 = dataBaseHelper.
                        getListOfAllServicesForOneContactFromDatabase(contact1);
                List<ServiceModel> servicesListForContact2 = dataBaseHelper.
                        getListOfAllServicesForOneContactFromDatabase(contact2);

                long allServicesCostForContact1 = MathFunctions.getCostOfAllServices(servicesListForContact1);
                long allServicesCostForContact2 = MathFunctions.getCostOfAllServices(servicesListForContact2);

                return (int) (allServicesCostForContact2 -  allServicesCostForContact1);
            }
        });
    }

    /**
     * Sorts contacts list by cost of all services owned by each contact in ascending order
     * @param contactsList list containing objects of ContactModel class
     * @param context context of the current activity
     */
    public static void sortContactsListByServiceCostPerHourInAscendingOrder(
            List<ContactModel> contactsList,
            Context context
    ) {
        final DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

        Collections.sort(contactsList, new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel contact1, ContactModel contact2) {
                List<ServiceModel> servicesListForContact1 = dataBaseHelper.
                        getListOfAllServicesForOneContactFromDatabase(contact1);
                List<ServiceModel> servicesListForContact2 = dataBaseHelper.
                        getListOfAllServicesForOneContactFromDatabase(contact2);

                long averageCostPerHourOfAllServices1 = MathFunctions.
                        getAverageCostPerHourOfAllServices(servicesListForContact1);
                long averageCostPerHourOfAllServices2 = MathFunctions.
                        getAverageCostPerHourOfAllServices(servicesListForContact2);

                return (int) (averageCostPerHourOfAllServices1 - averageCostPerHourOfAllServices2);
            }
        });
    }

    /**
     * Sorts contacts list by cost of all services owned by each contact in descending order
     * @param contactsList list containing objects of ContactModel class
     * @param context context of the current activity
     */
    public static void sortContactsListByServiceCostPerHourInDescendingOrder(
            List<ContactModel> contactsList,
            Context context
    ) {
        final DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

        Collections.sort(contactsList, new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel contact1, ContactModel contact2) {
                List<ServiceModel> servicesListForContact1 = dataBaseHelper.
                        getListOfAllServicesForOneContactFromDatabase(contact1);
                List<ServiceModel> servicesListForContact2 = dataBaseHelper.
                        getListOfAllServicesForOneContactFromDatabase(contact2);

                long averageCostPerHourOfAllServices1 = MathFunctions.
                        getAverageCostPerHourOfAllServices(servicesListForContact1);
                long averageCostPerHourOfAllServices2 = MathFunctions.
                        getAverageCostPerHourOfAllServices(servicesListForContact2);

                return (int) (averageCostPerHourOfAllServices2 - averageCostPerHourOfAllServices1);
            }
        });
    }

    /**
     * Sorts services list by service name in alphabetical order
     * @param servicesList list containing objects of ServiceModel class
     */
    public static void sortServicesListInAlphabeticalOrder(List<ServiceModel> servicesList) {
        Collections.sort(servicesList, new Comparator<ServiceModel>() {
            @Override
            public int compare(ServiceModel contact1, ServiceModel contact2) {
                return contact1.getServiceName().toLowerCase().
                        compareTo(contact2.getServiceName().toLowerCase());
            }
        });
    }
}
