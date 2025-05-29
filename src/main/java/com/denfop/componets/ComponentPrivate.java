package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.proxy.CommonProxy;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.base.TileEntityTesseract;
import com.denfop.tiles.mechanism.TileEntitySafe;
import com.denfop.utils.ModUtils;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ComponentPrivate extends AbstractComponent {

    private final List<String> players = new ArrayList<>();
    private final List<UUID> playersUUID = new ArrayList<>();
    boolean activate = false;

    public ComponentPrivate(final TileEntityInventory parent) {
        super(parent);
    }

    public Player getPlayerByUUID(Level level, String name) {
        for (int i = 0; i < level.players().size(); ++i) {
            Player player = level.players().get(i);
            if (name.equals(player.getName().getString())) {
                return player;
            }
        }

        return null;
    }

    @Override
    public boolean onBlockActivated(final Player player, final InteractionHand hand) {
        super.onBlockActivated(player, hand);
        final ItemStack stack = player.getItemInHand(hand);
        if (!this.activate && !stack.isEmpty() && this.players.contains(player.getName().getString())) {
            if (stack.getItem() == IUItem.module7.getItemFromMeta(0)) {
                this.activate = true;
                for (int m = 0; m < 9; m++) {
                    CompoundTag nbt = ModUtils.nbt(stack);
                    String name = "player_" + m;
                    if (!nbt.getString(name).isEmpty()) {
                        this.players.add(nbt.getString(name));
                        this.playersUUID.add(getPlayerByUUID(player.level(), nbt.getString(name)).getUUID());
                    }
                }
                stack.shrink(1);
                return true;
            }
        }
        if (this.activate && !this.getParent().getLevel().isClientSide) {
            if (!(this.players.contains(player.getName().getString()) || player.isCreative())) {
                CommonProxy.sendPlayerMessage(player, Localization.translate("iu.error"));
                return true;
            }
        }
        return false;
    }

    public void setActivate(final boolean activate) {
        this.activate = activate;
    }

    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        if (placer instanceof Player) {
            players.add(placer.getName().getString());
            playersUUID.add(placer.getUUID());
        }
    }

    public boolean canEntityDestroy(Entity entity) {
        return !this.activate || (entity instanceof Player && (this.players.contains(entity.getName().getString()) || ((Player) entity).isCreative()));
    }

    @Override
    public boolean wrenchCanRemove(final Player player) {
        return !this.activate || (player != null && (this.players.contains(player.getName().getString()) || player.isCreative()));

    }

    public TypePurifierJob getPurifierJob() {
        return TypePurifierJob.ItemStack;
    }

    @Override
    public boolean canUsePurifier(final Player player) {
        return
              !(this.parent instanceof TileEntityTesseract || parent instanceof TileEntitySafe) &&
                this.activate && (this.players.contains(
                        player.getName().getString()) || player.isCreative());
    }

    public ItemStack getItemStackUpgrade() {
        this.activate = false;
        final String player = this.players.get(0);
        final UUID playerUUID = this.playersUUID.get(0);
        this.players.clear();
        this.playersUUID.clear();
        this.players.add(player);
        this.playersUUID.add(playerUUID);
        return new ItemStack(IUItem.module7.getItemFromMeta(0));
    }

    @Override
    public List<ItemStack> getDrops() {
        final List<ItemStack> ret = super.getDrops();
        if (this.activate) {
             if (!(this.parent instanceof TileEntityTesseract || parent instanceof TileEntitySafe)) {
            ret.add(new ItemStack(IUItem.module7.getItemFromMeta(0)));
              }
        }
        return ret;
    }

    @Override
    public CompoundTag writeToNbt() {
        CompoundTag nbt = super.writeToNbt();
        nbt.putInt("size", this.players.size());
        for (int i = 0; i < this.players.size(); i++) {
            nbt.putString("player_" + i, this.players.get(i));
        }
        nbt.putInt("size1", this.playersUUID.size());
        for (int i = 0; i < this.playersUUID.size(); i++) {
            nbt.putUUID("player_" + i, this.playersUUID.get(i));
        }
        nbt.putBoolean("activate", activate);
        return nbt;
    }

    @Override
    public void readFromNbt(final CompoundTag nbt) {
        super.readFromNbt(nbt);
        final int size = nbt.getInt("size");
        for (int i = 0; i < size; i++) {
            this.players.add(nbt.getString("player_" + i));
        }
        final int size1 = nbt.getInt("size1");
        for (int i = 0; i < size1; i++) {
            this.playersUUID.add(nbt.getUUID("player_" + i));
        }
        this.activate = nbt.getBoolean("activate");
    }

    @Override
    public void onContainerUpdate(ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeInt(this.players.size());
        buffer.writeBoolean(this.activate);
        this.players.forEach(buffer::writeString);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public CustomPacketBuffer updateComponent() {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeInt(this.players.size());
        buffer.writeBoolean(this.activate);
        this.players.forEach(buffer::writeString);
        return buffer;
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {
        this.players.clear();
        int size = is.readInt();
        this.activate = is.readBoolean();
        for (int i = 0; i < size; i++) {
            this.players.add(is.readString());
        }
    }

    public List<UUID> getPlayersUUID() {
        return playersUUID;
    }

    public List<String> getPlayers() {
        return players;
    }

}
