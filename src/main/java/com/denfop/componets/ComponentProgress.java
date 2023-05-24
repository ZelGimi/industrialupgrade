package com.denfop.componets;

import com.denfop.tiles.base.TileEntityInventory;
import ic2.core.network.GrowingBuffer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.DataInput;
import java.io.IOException;

public class ComponentProgress extends TileEntityAdvComponent {

    private final short[] progress;
    private final short maxValue;

    public ComponentProgress(final TileEntityInventory parent, int col, short max) {
        super(parent);
        this.progress = new short[col];
        this.maxValue = max;
    }

    public void addProgress() {
        this.addProgress(0);
    }

    public void addProgress(int col) {
        this.progress[col] += 1;
    }

    public void addProgress(int col, short value) {
        this.progress[col] += value;
    }

    public double getBar(int col) {
        return this.progress[col] * 1D / this.maxValue;
    }

    public double getBar() {
        return this.getBar(0);
    }

    @Override
    public void onContainerUpdate(final EntityPlayerMP player) {
        GrowingBuffer buffer = new GrowingBuffer(16);
        buffer.writeShort(progress.length);
        for (final short value : progress) {
            buffer.writeShort(value);
        }
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    @Override
    public void onNetworkUpdate(final DataInput is) throws IOException {
        super.onNetworkUpdate(is);
        int size = is.readShort();
        for (int i = 0; i < size; i++) {
            this.progress[i] = is.readShort();
        }
    }

}
