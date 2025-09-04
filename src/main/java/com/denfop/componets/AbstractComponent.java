package com.denfop.componets;

import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketAbstractComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
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

    protected BlockEntityBase parent;

    public AbstractComponent(final BlockEntityBase parent) {
        this.parent = parent;
    }

    public BlockEntityBase getParent() {
        return this.parent;
    }

    public void setParent(final BlockEntityBase parent) {
        this.parent = parent;
    }

    public boolean isClient() {
        return false;
    }

    public boolean isServer() {
        return false;
    }

    //  public void setOverclockRates(InvSlotUpgrade invSlotUpgrade) {}

    public boolean canEntityDestroy(Entity entity) {
        return true;
    }

    public boolean wrenchCanRemove(final Player player) {
        return true;
    }

    public void ActionPurifier() {

    }

    public boolean onSneakingActivated(Player player, InteractionHand hand) {
        return false;
    }

    public boolean canUpgradeBlock() {
        return false;
    }

    public void markDirty() {

    }

    public boolean needWriteNBTToDrops() {
        return false;
    }

    public CompoundTag writeNBTToDrops(CompoundTag tagCompound) {
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
        if (!this.getParent().getLevel().isClientSide) {
            final ItemStack itemstack = getItemStackUpgrade();
            if (itemstack.isEmpty()) {
                return;
            }
            final ItemEntity item = new ItemEntity(EntityType.ITEM, this.getParent().getLevel());
            item.setItem(itemstack);
            item.setPos(this.getParent().getBlockPos().getX(), this.getParent().getBlockPos().getY(),
                    this.getParent().getBlockPos().getZ()
            );
            item.setPickUpDelay(0);
            this.getParent().getLevel().addFreshEntity(item);
        }
    }

    public TypePurifierJob getPurifierJob() {
        return TypePurifierJob.None;
    }

    public boolean canUsePurifier(final Player player) {
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

    public boolean onBlockActivated(Player player, InteractionHand hand) {
        return false;
    }

    protected void setNetworkUpdate(ServerPlayer player, CustomPacketBuffer data) {
        new PacketAbstractComponent(this.parent, this.toString(), player, data);
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }

    public void readFromNbt(CompoundTag nbt) {
    }

    public CompoundTag writeToNbt() {
        return new CompoundTag();
    }

    public void onLoaded() {
    }

    public void onUnloaded() {
    }

    public void onNeighborChange(BlockState srcBlock, BlockPos srcPos) {
    }

    public void onContainerUpdate(ServerPlayer player) {
    }

    public CustomPacketBuffer updateComponent() {
        return new CustomPacketBuffer();
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {
    }

    public Collection<? extends Capability<?>> getProvidedCapabilities(Direction side) {
        return Collections.emptySet();
    }

    public <T> T getCapability(Capability<T> cap, Direction side) {
        return null;
    }

    public void onPlaced(ItemStack stack, LivingEntity placer, Direction facing) {
    }

    public void blockBreak() {
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
    }
}
