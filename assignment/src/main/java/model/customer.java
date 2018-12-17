package model;

import java.io.Serializable;

/**
 * Created by rvohra on 12/10/18.
 */
public class customer  implements Serializable {
    private static final long serialVersionUID = 10L;

    private String customerId;
    private String name;
    private String street;
    private String city;
    private String state;
    private String zip;

    public customer(String customerId, String name, String street, String city, String state, String zip) {
        this.customerId = customerId;
        this.name = name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public static customer parseRecord(String row) {
        String[] cust = row.split("#");
        // get last two chars from address field
        String[] add = cust[2].split(",");
        return new customer(cust[0], cust[1], add[0] ,add[1], add[2], cust[3]);
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}

