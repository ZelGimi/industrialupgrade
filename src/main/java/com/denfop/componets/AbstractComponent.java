package com.denfop.componets;

import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketAbstractComponent;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

enum TypePurifierJob {
    ItemStack,
    Actions,
    None
}

public abstract class AbstractComponent {

    protected TileEntityBlock parent;

    public AbstractComponent(final TileEntityBlock parent) {
        this.parent = parent;
    }

    public TileEntityBlock getParent() {
        return this.parent;
    }

    public void setParent(final TileEntityBlock parent) {
        this.parent = parent;
    }

    public boolean isClient() {
        return false;
    }

    public boolean isServer() {
        return false;
    }

    public void setOverclockRates(InvSlotUpgrade invSlotUpgrade) {

    }

    public boolean canEntityDestroy(Entity entity) {
        return true;
    }

    public boolean wrenchCanRemove(final EntityPlayer player) {
        return true;
    }

    public void ActionPurifier() {

    }

    public boolean canUpgradeBlock() {
        return false;
    }

    public void markDirty() {

    }

    public boolean needWriteNBTToDrops() {
        return false;
    }

    public NBTTagCompound writeNBTToDrops(NBTTagCompound tagCompound) {
        return tagCompound;
    }

    public void workPurifier() {
        if (this.getPurifierJob() == TypePurifierJob.Actions) {
            this.ActionPurifier();
        } else {
            this.actionGetDrop();
        }
    }

    public void actionGetDrop() {
        if (!this.getParent().getWorld().isRemote) {
            final ItemStack itemstack = getItemStackUpgrade();
            if (itemstack.isEmpty()) {
                return;
            }
            final EntityItem item = new EntityItem(this.getParent().getWorld());
            item.setItem(itemstack);
            item.setLocationAndAngles(this.getParent().getPos().getX(), this.getParent().getPos().getY(),
                    this.getParent().getPos().getZ(), 0.0F,
                    0.0F
            );
            item.setPickupDelay(0);
            this.getParent().getWorld().spawnEntity(item);
        }
    }

    public TypePurifierJob getPurifierJob() {
        return TypePurifierJob.None;
    }

    public boolean canUsePurifier(final EntityPlayer player) {
        return getPurifierJob() != TypePurifierJob.None;
    }

    public ItemStack getItemStackUpgrade() {
        return ItemStack.EMPTY;
    }

    public void updateEntityServer() {
    }

    public List<ItemStack> getDrops() {
        return new ArrayList<>();
    }

    public void updateEntityClient() {
    }

    public boolean onBlockActivated(EntityPlayer player, EnumHand hand) {
        return false;
    }

    protected void setNetworkUpdate(EntityPlayerMP player, CustomPacketBuffer data) {
        new PacketAbstractComponent(this.parent, this.toString(), player, data);
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }

    public void readFromNbt(NBTTagCompound nbt) {
    }

    public NBTTagCompound writeToNbt() {
        return new NBTTagCompound();
    }

    public void onLoaded() {
    }

    public void onUnloaded() {
    }

    public void onNeighborChange(Block srcBlock, BlockPos srcPos) {
    }

    public void onContainerUpdate(EntityPlayerMP player) {
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {
    }

    public Collection<? extends Capability<?>> getProvidedCapabilities(EnumFacing side) {
        return Collections.emptySet();
    }

    public <T> T getCapability(Capability<T> cap, EnumFacing side) {
        return null;
    }

    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
    }

}
