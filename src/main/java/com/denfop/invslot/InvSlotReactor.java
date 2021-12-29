package com.denfop.invslot;

import com.denfop.tiles.reactors.TileEntityBaseNuclearReactorElectric;
import ic2.api.reactor.IBaseReactorComponent;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotReactor extends InvSlot {


    public InvSlotReactor(TileEntityBaseNuclearReactorElectric base1, String name1, int count) {
        super(base1, name1, Access.IO, count);
        this.setStackSizeLimit(1);
    }

    public boolean accepts(ItemStack itemStack) {

        return itemStack.getItem() instanceof IBaseReactorComponent;
    }

    public int size() {
        return ((TileEntityBaseNuclearReactorElectric) this.base).getReactorSize() * ((TileEntityBaseNuclearReactorElectric) this.base).sizeY;
    }

    public ItemStack get(int index) {
        return super.get(this.mapIndex(index));
    }

    public ItemStack get(int x, int y) {
        return super.get(y * ((TileEntityBaseNuclearReactorElectric) this.base).sizeX + x);
    }

    public void put(int index, ItemStack content) {
        super.put(this.mapIndex(index), content);
    }

    public void put(int x, int y, ItemStack content) {
        super.put(y * ((TileEntityBaseNuclearReactorElectric) this.base).sizeX + x, content);
    }

    private int mapIndex(int index) {
        int size = this.size();
        int cols = size / ((TileEntityBaseNuclearReactorElectric) this.base).sizeY;
        int remCols;
        int row;
        if (index < size) {
            remCols = index / cols;
            row = index % cols;
            return remCols * ((TileEntityBaseNuclearReactorElectric) this.base).sizeX + row;
        } else {
            index -= size;
            remCols = ((TileEntityBaseNuclearReactorElectric) this.base).sizeX - cols;
            row = index / remCols;
            int col = cols + index % remCols;
            return row * ((TileEntityBaseNuclearReactorElectric) this.base).sizeX + col;
        }
    }

}
