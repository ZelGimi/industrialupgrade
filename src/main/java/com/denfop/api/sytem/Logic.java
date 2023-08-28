package com.denfop.api.sytem;

import java.util.ArrayList;
import java.util.List;

public class Logic<T> {

    public final List<T> tiles;

    public Logic() {
        this.tiles = new ArrayList<>();
    }

    public boolean contains(final T par1) {
        return this.tiles.contains(par1);
    }

    public void add(final T par1) {
        this.tiles.add(par1);
    }

    public void remove(final T par1) {
        this.tiles.remove(par1);
    }

    public void clear() {
        this.tiles.clear();
    }

    public T getRepresentingTile() {
        if (this.tiles.isEmpty()) {
            return null;
        }
        return this.tiles.get(0);
    }

}
