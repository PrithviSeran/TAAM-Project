package com.example.b07demosummer2024;

import androidx.annotation.NonNull;

public class Item {

    private String LotNum;
    private String Name;
    private String Category;
    private String Period;
    private String Description;

    public Item() {}

    public Item(String lotNum, String name, String category, String period, String description, String picURL) {
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
        return String.format("Lot Num: %s, Name: %s, Category: %s," +
                " Period: %s, Description: %s, Pic: %s",
                LotNum, Name, Category, Period, Description);
    }
}
