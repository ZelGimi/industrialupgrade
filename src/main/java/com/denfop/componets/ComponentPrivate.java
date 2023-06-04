package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.proxy.CommonProxy;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import ic2.core.network.GrowingBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComponentPrivate extends AbstractComponent {

    boolean activate = false;
    private final List<String> players = new ArrayList<>();

    public ComponentPrivate(final TileEntityInventory parent) {
        super(parent);
    }

    @Override
    public boolean onBlockActivated(final EntityPlayer player, final EnumHand hand) {
        super.onBlockActivated(player, hand);
        final ItemStack stack = player.getHeldItem(hand);
        if (!this.activate && !stack.isEmpty()) {
            if (stack.getItem() == IUItem.module7 && stack.getItemDamage() == 0) {
                this.activate = true;
                for (int m = 0; m < 9; m++) {
                    NBTTagCompound nbt = ModUtils.nbt(stack);
                    String name = "player_" + m;
                    if (!nbt.getString(name).isEmpty()) {
                        this.players.add(nbt.getString(name));
                    }
                }
                stack.shrink(1);
                return true;
            }
        }
        if (this.activate && !this.getParent().getWorld().isRemote) {
            if (!(this.players.contains(player.getName()) || player.capabilities.isCreativeMode)) {
                CommonProxy.sendPlayerMessage(player, Localization.translate("iu.error"));
                return true;
            }
        }
        return false;
    }


    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (placer instanceof EntityPlayer) {
            players.add(placer.getName());
        }
    }

    public boolean canEntityDestroy(Entity entity) {
        return !this.activate || (entity instanceof EntityPlayer && (this.players.contains(entity.getName()) || ((EntityPlayer) entity).capabilities.isCreativeMode));
    }

    @Override
    public boolean wrenchCanRemove(final EntityPlayer player) {
        return !this.activate || (player != null && (this.players.contains(player.getName()) || player.capabilities.isCreativeMode));

    }

    public TypePurifierJob getPurifierJob() {
        return TypePurifierJob.ItemStack;
    }

    @Override
    public boolean canUsePurifier(final EntityPlayer player) {
        return this.activate && (this.players.contains(player.getName()) || player.capabilities.isCreativeMode);
    }

    public ItemStack getItemStackUpgrade() {
        this.activate = false;
        final String player = this.players.get(0);
        this.players.clear();
        this.players.add(player);
        return new ItemStack(IUItem.module7);
    }

    @Override
    public List<ItemStack> getDrops() {
        final List<ItemStack> ret = super.getDrops();
        if (this.activate) {
            ret.add(new ItemStack(IUItem.module7));
        }
        return ret;
    }

    @Override
    public NBTTagCompound writeToNbt() {
        NBTTagCompound nbt = super.writeToNbt();
        nbt.setInteger("size", this.players.size());
        for (int i = 0; i < this.players.size(); i++) {
            nbt.setString("player_" + i, this.players.get(i));
        }
        nbt.setBoolean("activate", activate);
        return nbt;
    }

    @Override
    public void readFromNbt(final NBTTagCompound nbt) {
        super.readFromNbt(nbt);
        final int size = nbt.getInteger("size");
        for (int i = 0; i < size; i++) {
            this.players.add(nbt.getString("player_" + i));
        }
        this.activate = nbt.getBoolean("activate");
    }

    @Override
    public void onContainerUpdate(EntityPlayerMP player) {
        GrowingBuffer buffer = new GrowingBuffer(this.players.size() + 2);
        buffer.writeShort(this.players.size());
        buffer.writeBoolean(this.activate);
        this.players.forEach(buffer::writeString);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public void onNetworkUpdate(DataInput is) throws IOException {
        this.players.clear();
        short size = is.readShort();
        this.activate = is.readBoolean();
        for (int i = 0; i < size; i++) {
            this.players.add(is.readLine());
        }
    }

}
