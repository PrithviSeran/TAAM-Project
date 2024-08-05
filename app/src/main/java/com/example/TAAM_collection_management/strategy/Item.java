/*
 * Item.java     1.0     2024/08/04
 */

package com.example.TAAM_collection_management.strategy;

import androidx.annotation.NonNull;


/**
 * <code>Item</code> class translates and allows the usage and manipulation
 * of display data for items stored in Firebase database.
 *
 */
public class Item {

    private String lotNum;
    private String name;
    private String category;
    private String period;
    private String description;

    /**
     * Default constructor of class <code>Item</code>
     */
    public Item() {}

    /**
     * Constructor of <code>Item</code> class.
     * Sets values of <code>Item</code> to given parameters.
     *
     *
     * @param lotNum        Lot number of database object.
     * @param name          Name of database object.
     * @param category      Category of database object.
     * @param period        Period of database object.
     * @param description   Description of database object.
     */
    public Item(String lotNum, String name, String category, String period, String description) {
        this.lotNum = lotNum;
        this.name = name;
        this.category = category;
        this.period = period;
        this.description = description;
    }

    /**
     * Returns Complied information from <code>Item</code> in the form of a string.
     *
     * @return      String with all <code>Item</code> information appropriately formatted.
     */
    //Why does toString() have zero documentation on intelliJ
    @NonNull
    @Override
    public String toString() {
        return String.format("Lot Num: %s, Name: %s, Category: %s," +
                " Period: %s, Description: %s, Pic: %s",
                lotNum, name, category, period, description);
    }

    public String getLotNum() { return lotNum; }

    public void setLotNum(String lotNum) { this.lotNum= lotNum; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public String getPeriod() { return period; }

    public void setPeriod(String period) { this.period = period; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

}
