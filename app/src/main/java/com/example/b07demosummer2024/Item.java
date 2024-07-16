package com.example.b07demosummer2024;

public class Item {

    private String lotNum;
    private String name;
    private String category;
    private String period;
    private String description;
    private String picURL;

    public Item() {}

    public Item(String lotNum, String name, String category, String period, String description, String picURL) {
        this.lotNum = lotNum;
        this.name = name;
        this.category = category;
        this.period = period;
        this.description = description;
        this.picURL = picURL;
    }

    // Getters and setters
    public String getLotNum() { return lotNum; }
    public void setLotNum(String lotNum) { this.lotNum= lotNum; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPeriod() { return period; }
    public void setGenre(String period) { this.period = period; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPic() { return picURL; }
    public void setPic(String picURL) { this.picURL= picURL; }
}
