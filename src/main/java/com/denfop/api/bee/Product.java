package com.denfop.api.bee;

import com.denfop.api.crop.Crop;

import java.util.Objects;

public class Product {

    private final Crop crop;
    private double chance;

    public Product(double chance, Crop crop) {
        this.chance = chance;
        this.crop = crop;
    }

    public boolean equals(Crop crop) {
        return crop.getId() == crop.getId();
    }

    public double getChance() {
        return chance;
    }

    public void addChance(double chance) {
        this.chance += chance;
    }

    public Crop getCrop() {
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
