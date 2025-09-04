package com.denfop.api.bee;

import com.denfop.api.crop.ICrop;

import java.util.Objects;

public class Product {

    private final ICrop crop;
    private double chance;

    public Product(double chance, ICrop crop) {
        this.chance = chance;
        this.crop = crop;
    }

    public boolean equals(ICrop crop) {
        return crop.getId() == crop.getId();
    }

    public double getChance() {
        return chance;
    }

    public void addChance(double chance) {
        this.chance += chance;
    }

    public ICrop getCrop() {
        return crop;
    }

    public void removeChance(double chance) {
        this.chance -= chance;
        if (this.chance < 0)
            this.chance = 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return crop.getId() == product.crop.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(crop.getId());
    }

}
