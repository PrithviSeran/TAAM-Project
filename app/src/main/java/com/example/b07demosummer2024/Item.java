package com.example.b07demosummer2024;

import androidx.annotation.NonNull;


/**
 * <code>Item</code> class translates and allows the usage and manipulation
 * of display data for items stored in Firebase database.
 *
 */
public class Item {

    private String LotNum;
    private String Name;
    private String Category;
    private String Period;
    private String Description;

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
        this.LotNum = lotNum;
        this.Name = name;
        this.Category = category;
        this.Period = period;
        this.Description = description;
    }

    //Why are there so many unused getters and setters?
    public String getLotNum() { return LotNum; }
    public void setLotNum(String lotNum) { this.LotNum= lotNum; }
    public String getName() { return Name; }
    public void setName(String name) { this.Name = name; }
    public String getCategory() { return Category; }
    public void setCategory(String category) { this.Category = category; }
    public String getPeriod() { return Period; }
    public void setPeriod(String period) { this.Period = period; }
    public String getDescription() { return Description; }
    public void setDescription(String description) { this.Description = description; }

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
                LotNum, Name, Category, Period, Description);
    }
}
