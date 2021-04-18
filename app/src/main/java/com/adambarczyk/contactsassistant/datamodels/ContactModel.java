package com.adambarczyk.contactsassistant.datamodels;

public class ContactModel {

    private int contactId;
    private String name;
    private String email;
    private int phone;
    private String address;
    private String notes;


    // constructors

    public ContactModel(int contactId, String name, String email,
                        int phone, String address, String notes) {
        this.contactId = contactId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.notes = notes;
    }

    public ContactModel(int errorContactId) {
        this.contactId = errorContactId;
    }

    // toString() for printing the contents of a class object

    @Override
    public String toString() {
        return "ContactsModel{" +
                "contact_id=" + contactId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }


    // getters and setters

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
