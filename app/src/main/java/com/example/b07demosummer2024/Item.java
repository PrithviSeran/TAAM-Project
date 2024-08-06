/*
 * Item.java     1.0     2024/08/07
 */

package com.example.b07demosummer2024;

import androidx.annotation.NonNull;

/**
 * <code>Item</code> class translates and allows the usage and manipulation
 * of display data for items stored in Firebase database.
 */
public class Item {

    /**
     * LotNum of the Item. This is also the image ID.
     */
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

    @NonNull
    @Override
    public String toString() {
        return String.format("Lot Num: %s, Name: %s, Category: %s, Period: %s, Description: %s",
                (Object[]) toArray());
    }

    /**
     * Converts the contents of <code>Item</code> into an array
     * <p>
     * <b>Note:</b> Item order cannot be changed, however new items
     * can be added to the end of the array.
     * @return      Returns the array containing all <code>Item</code> information.
     */
    public final String[] toArray() {
        return new String[] {LotNum, Name, Category, Period, Description};
    }
}
