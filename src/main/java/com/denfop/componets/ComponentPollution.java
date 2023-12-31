package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ComponentPollution extends AbstractComponent {

    boolean active = false;
    private ComponentTimer timer;
    private ItemStack stack;

    public ComponentPollution(final TileEntityInventory parent) {
        super(parent);
    }

    public void setTimer(final ComponentTimer timer) {
        this.timer = timer;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.stack = new ItemStack(IUItem.module7, 1, 9);
        if (this.active) {
            this.timer.setCanWork(false);
        }

    }

    public ItemStack getStack() {
        return stack;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean onBlockActivated(final EntityPlayer player, final EnumHand hand) {
        super.onBlockActivated(player, hand);
        final ItemStack stack = player.getHeldItem(hand);
        if (!this.active && !stack.isEmpty()) {
            if (stack.isItemEqual(this.stack)) {
                this.active = true;
                stack.shrink(1);
                this.timer.setCanWork(false);
                return true;
            }
        }
        return false;
    }

    public void onContainerUpdate(EntityPlayerMP player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
        buffer.writeBoolean(this.active);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }
    public CustomPacketBuffer updateComponent() {
        final CustomPacketBuffer packet = super.updateComponent();
        packet.writeBoolean(this.active);
        return packet;
    }
    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {

        this.active = is.readBoolean();
    }

    public int getAllTime() {
        final AtomicInteger time = new AtomicInteger();
        this.timer.getDefaultTimers().forEach(timer -> time.addAndGet(timer.getTime()));
        return time.get();
    }

    public int getTime() {
        final AtomicInteger time = new AtomicInteger();
        this.timer.getTimers().forEach(timer -> time.addAndGet(timer.getTime()));
        return time.get();
    }

    @Override
    public boolean canUsePurifier(final EntityPlayer player) {
        return this.active;
    }

    @Override
    public NBTTagCompound writeToNbt() {
        NBTTagCompound nbt = super.writeToNbt();
        nbt.setBoolean("active", active);
        return nbt;
    }

    @Override
    public void readFromNbt(final NBTTagCompound nbt) {
        super.readFromNbt(nbt);
        this.active = nbt.getBoolean("active");
    }

    @Override
    public TypePurifierJob getPurifierJob() {
        return TypePurifierJob.ItemStack;
    }

    @Override
    public List<ItemStack> getDrops() {
        final List<ItemStack> ret = super.getDrops();
        if (this.active) {
            ret.add(this.stack.copy());
        }
        return ret;
    }

    public ItemStack getItemStackUpgrade() {
        this.active = false;
        this.timer.setCanWork(true);
        return this.stack.copy();
    }

}
