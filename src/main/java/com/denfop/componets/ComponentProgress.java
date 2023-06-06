package com.denfop.componets;

import com.denfop.tiles.base.TileEntityInventory;
import ic2.core.network.GrowingBuffer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.DataInput;
import java.io.IOException;

public class ComponentProgress extends AbstractComponent {

    private final short[] progress;
    private short maxValue;

    public ComponentProgress(final TileEntityInventory parent, int col, short max) {
        super(parent);
        this.progress = new short[col];
        this.maxValue = max;
    }

    public short getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(final short maxValue) {
        this.maxValue = maxValue;
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

    public short getProgress(int index) {
        return progress[index];
    }

    public short getProgress() {
        return getProgress(0);
    }

    public void setProgress(short col) {
        this.progress[0] = col;
    }

    public void cancellationProgress() {
        cancellationProgress(0);
    }

    public void setProgress(int index, short col) {
        this.progress[index] = col;
    }

    public void cancellationProgress(int index) {
        this.progress[index] = 0;
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
        buffer.writeShort(this.maxValue);
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
        this.maxValue = is.readShort();
    }

}
