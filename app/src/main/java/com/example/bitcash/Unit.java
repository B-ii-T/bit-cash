package com.example.bitcash;

public class Unit {
    //variables declaration
    private int unitValue, unitQnt;
    //constructor
    public Unit(int unitValue, int unitQnt) {
        this.unitValue = unitValue;
        this.unitQnt = unitQnt;
    }
    //getter and setters
    public void setUnitValue(int unitValue) {
        this.unitValue = unitValue;
    }
    public void setUnitQnt(int unitQnt) {
        this.unitQnt = unitQnt;
    }
    public int getUnitValue() {
        return unitValue;
    }
    public int getUnitQnt() {
        return unitQnt;
    }
}
