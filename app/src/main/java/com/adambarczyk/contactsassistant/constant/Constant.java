package com.adambarczyk.contactsassistant.constant;

public class Constant {
    // Constants to choose if app should edit or add the contact/service
    public static final int TO_ADD = 0;
    public static final int TO_EDIT = 1;
    public static final int ERROR = -1;

    // Constant names to use with intents
    public static final String ADD_OR_EDIT = "addOrEdit";
    public static final String CONTACT_ID = "contactId";
    public static final String SERVICE_ID = "serviceId";
    public static final String CONTACT_MODEL = "contactModel";
    public static final String OLD_CONTACT_MODEL = "oldContactModel";
    public static final String OLD_SERVICE_MODEL = "oldServiceModel";
    public static final String PARENT_CONTACT_MODEL = "parentContactModel";

    // Constant tags and names for UI elements
    public static final String CONTACT_BUTTON = "contactButton";
    public static final String SERVICE_BUTTON = "serviceButton";

    // Constants for area code telephone numbers
    public static final String PL_PHONE_CODE = "+48";

    // Constants for sorting contacts
    public static final int SORT_IN_ALPHABETICAL_ORDER = 0;
    public static final int SORT_BY_SERVICES_COUNT_ASCENDING_ORDER = 1;
    public static final int SORT_BY_SERVICES_COUNT_DESCENDING_ORDER = 2;
    public static final int SORT_BY_SERVICES_ALL_SERVICES_COST_ASCENDING_ORDER = 3;
    public static final int SORT_BY_SERVICES_COST_DESCENDING_ORDER = 4;
    public static final int SORT_BY_SERVICE_COST_PER_HOUR_ASCENDING_ORDER = 5;
    public static final int SORT_BY_SERVICE_COST_PER_HOUR_DESCENDING_ORDER = 6;
}