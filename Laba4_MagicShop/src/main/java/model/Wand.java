package model;


/**
 *
 * @author elenagoncarova
 */
public class Wand {
    private int id;
    private int woodId;
    private int coreId;
    private boolean isSold; // false если не продана
    private double price;
    
    public Wand() {}

    public Wand(int id, int woodId, int coreId, boolean isSold, double price) {
        this.id = id;
        this.woodId = woodId;
        this.coreId = coreId;
        this.price = price;
        this.isSold = isSold;
    }
    
    public int getId() {
        return id;
    }

    public int getWoodId() {
        return woodId;
    }

    public int getCoreId() {
        return coreId;
    }

    public boolean isSold() {
        return isSold;
    }

    public double getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWoodId(int woodId) {
        this.woodId = woodId;
    }

    public void setCoreId(int coreId) {
        this.coreId = coreId;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
