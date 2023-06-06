package com.denfop.componets;

import com.denfop.tiles.base.TileEntityInventory;

import java.util.function.IntSupplier;

public abstract class BasicRedstoneComponent extends AbstractComponent {

    private int level;
    private IntSupplier update;

    public BasicRedstoneComponent(TileEntityInventory parent) {
        super(parent);
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int newLevel) {
        if (newLevel != this.level) {
            this.level = newLevel;
            this.onChange();
        }
    }

    public abstract void onChange();

    public boolean isServer() {
        return this.update != null;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();

    }


    public void setUpdate(IntSupplier update) {
        this.update = update;
    }

}
