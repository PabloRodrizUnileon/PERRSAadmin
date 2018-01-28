package com.example.pablo.perrsa_admin.Objetos;

import java.io.Serializable;

/**
 * Created by Pablo on 24/01/2018.
 */

public class ProductoItem implements Serializable{
    private String title;
    private int quantity;
    private boolean checked;

    public ProductoItem(){

    }

    public ProductoItem(String title, int quantity, boolean checked) {
        this.title = title;
        this.quantity = quantity;
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return title + ": " +  quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        ProductoItem itemCompare = (ProductoItem) obj;
        if(itemCompare.getTitle().equals(this.getTitle()))
            return true;
        return false;
    }
}
