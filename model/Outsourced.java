package model;

/**
 *
 *
 * QKM2
 * Jack Compton
 */

public class Outsourced extends Part{

    // LOCAL VARIABLES
    private String companyName;

    // CONSTRUCTOR
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    // METHODS
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
