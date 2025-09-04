package com.denfop.componets;

import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.server.level.ServerPlayer;

import java.io.IOException;

public class ComponentProgress extends AbstractComponent {

    private final short[] progress;
    private short maxValue;

    public ComponentProgress(final BlockEntityInventory parent, int col, short max) {
        super(parent);
        this.progress = new short[col];
        this.maxValue = max;
    }

    public ComponentProgress(final BlockEntityInventory parent, int col, int max) {
        super(parent);
        this.progress = new short[col];
        this.maxValue = (short) max;
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
    public void onContainerUpdate(final ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16, player.registryAccess());
        buffer.writeShort(progress.length);
        for (final short value : progress) {
            buffer.writeShort(value);
        }
        buffer.writeShort(this.maxValue);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public CustomPacketBuffer updateComponent() {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16, parent.registryAccess());
        buffer.writeShort(progress.length);
        for (final short value : progress) {
            buffer.writeShort(value);
        }
        buffer.writeShort(this.maxValue);
        return buffer;
    }

    @Override
    public void onNetworkUpdate(final CustomPacketBuffer is) throws IOException {
        super.onNetworkUpdate(is);
        int size = is.readShort();
        for (int i = 0; i < size; i++) {
            this.progress[i] = is.readShort();
        }
        this.maxValue = is.readShort();
    }

}
