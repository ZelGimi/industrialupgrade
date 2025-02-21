package com.denfop.componets;

import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;

enum Type {
    DEFAULT,
    ADVANCED,
    IMPROVED,
    PERFECT;

    Type() {

    }
}

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

    public CustomPacketBuffer updateComponent() {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeInt(this.level);
        return buffer;
    }

    @Override
    public void onNetworkUpdate(final CustomPacketBuffer is) throws IOException {
        super.onNetworkUpdate(is);
        this.level = is.readInt();


    }

}
