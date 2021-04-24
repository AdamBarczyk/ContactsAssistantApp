package com.adambarczyk.contactsassistant.utilities;

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
                return contact1.getName().compareTo(contact2.getName());
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
                return contact1.getServiceName().compareTo(contact2.getServiceName());
            }
        });
    }
}
