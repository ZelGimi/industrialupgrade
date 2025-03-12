package com.denfop.integration.ae;

import appeng.tile.powersink.AEBasePoweredTile;

import java.util.Objects;

public class DataAE {

    private final AEBasePoweredTile tile;
    private boolean isLoaded;
    boolean remove = false;
    public DataAE(AEBasePoweredTile tile){
        this.tile = tile;
        isLoaded = false;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(final boolean loaded) {
        isLoaded = loaded;
    }

    public DataAE(AEBasePoweredTile tile, boolean remove){
        this.tile = tile;
        this.isLoaded = false;
    }
    public void setRemove(final boolean remove) {
        this.remove = remove;
    }

    public boolean isRemove() {
        return remove;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o instanceof AEBasePoweredTile){
            return Objects.equals(tile.getPos(),((AEBasePoweredTile) o).getPos());
        }
        if (o == null || getClass() != o.getClass()) return false;
        DataAE dataAE = (DataAE) o;
        return Objects.equals(tile.getPos(), dataAE.tile.getPos());
    }

    public AEBasePoweredTile getTile() {
        return tile;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tile);
    }

}
