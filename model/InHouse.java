package model;

/**
 *
 *
 * QKM2
 * Jack Compton
 */

public class InHouse extends Part {

    // LOCAL VARIABLES
    private int machineId;

    // CONSTRUCTOR
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    // METHODS
    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
