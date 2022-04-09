package com.example.bitcash;

public class Unit {
    private int unitValue, unitQnt;
    public Unit(int unitValue, int unitQnt) {
        this.unitValue = unitValue;
        this.unitQnt = unitQnt;
    }
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
