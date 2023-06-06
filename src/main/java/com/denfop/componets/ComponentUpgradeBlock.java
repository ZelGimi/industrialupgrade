package com.denfop.componets;

import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.nbt.NBTTagCompound;

public class ComponentUpgradeBlock extends AbstractComponent {

    private final Type type;
    public int level = 0;

    public ComponentUpgradeBlock(final TileEntityInventory parent, Type type) {
        super(parent);
        this.type = type;
    }

    public boolean canUpgradeBlock() {
        return true;
    }

    @Override
    public void readFromNbt(final NBTTagCompound nbt) {
        super.readFromNbt(nbt);
        this.level = nbt.getInteger("level");
    }

    @Override
    public NBTTagCompound writeToNbt() {
        final NBTTagCompound nbt = super.writeToNbt();
        nbt.setInteger("level", this.level);
        return nbt;
    }

}

enum Type {
    DEFAULT,
    ADVANCED,
    IMPROVED,
    PERFECT;

    Type() {

    }
}
