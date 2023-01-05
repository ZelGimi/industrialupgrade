package com.denfop.invslot;

import com.denfop.tiles.reactors.ReactorsItem;
import com.denfop.tiles.reactors.TileEntityBaseNuclearReactorElectric;
import ic2.api.reactor.IBaseReactorComponent;
import ic2.api.reactor.IReactorComponent;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

import java.util.List;

public class InvSlotReactor extends InvSlot {


    private final TileEntityBaseNuclearReactorElectric tile;

    public InvSlotReactor(TileEntityBaseNuclearReactorElectric base1, String name1, int count) {
        super(base1, name1, Access.IO, count);
        this.setStackSizeLimit(1);
        this.tile = base1;
    }
    //       IReactorComponent comp = (IReactorComponent) stack.getItem();
    //                        comp.processChamber(stack, this, x, y, pass == 0);
    //

    public void update() {
        final List<ReactorsItem> list = tile.getReactorsItems();
        list.clear();
        int size = this.tile.getReactorSize();

        for (int y = 0; y < this.tile.sizeY; ++y) {
            for (int x = 0; x < size; ++x) {
                ItemStack stack = this.get(x, y);
                if (!stack.isEmpty() && stack.getItem() instanceof IReactorComponent) {
                    list.add(new ReactorsItem(stack, x, y, this.tile));
                }
            }

        }
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
        final List<ReactorsItem> list = tile.getReactorsItems();
        list.clear();
        int size = this.tile.getReactorSize();

        for (int y = 0; y < this.tile.sizeY; ++y) {
            for (int x = 0; x < size; ++x) {
                ItemStack stack = this.get(x, y);
                if (!stack.isEmpty() && stack.getItem() instanceof IReactorComponent) {
                    list.add(new ReactorsItem(stack, x, y, this.tile));
                }
            }
        }


    }

    public void put(int x, int y, ItemStack content) {
        super.put(y * ((TileEntityBaseNuclearReactorElectric) this.base).sizeX + x, content);
        final List<ReactorsItem> list = this.tile.getReactorsItems();
        list.clear();
        int size = this.tile.getReactorSize();
        for (int pass = 0; pass < 2; ++pass) {
            for (int y1 = 0; y1 < this.tile.sizeY; ++y1) {
                for (int x1 = 0; x1 < size; ++x1) {
                    ItemStack stack = this.get(x1, y1);
                    if (!stack.isEmpty() && stack.getItem() instanceof IReactorComponent && ((IReactorComponent) stack.getItem()).canBePlacedIn(
                            stack,
                            this.tile
                    )) {
                        list.add(new ReactorsItem(stack, x1, y1, this.tile));
                    }
                }
            }
        }
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
