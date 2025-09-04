package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.pollution.component.ChunkLevel;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ComponentPollution extends AbstractComponent {

    boolean active = false;
    private ComponentTimer timer;
    private ItemStack stack;
    private ChunkPos chunkPos;

    private double percent;

    public ComponentPollution(final BlockEntityInventory parent) {
        super(parent);
    }

    public void setTimer(final ComponentTimer timer) {
        this.timer = timer;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.stack = new ItemStack(IUItem.module7.getStack(9), 1);
        if (this.active) {
            this.timer.setCanWork(false);
        }

    }

    public ChunkPos getChunkPos() {
        if (this.chunkPos == null) {
            chunkPos = new ChunkPos(this.getParent().getBlockPos());
        }
        return chunkPos;
    }

    public ItemStack getStack() {
        return stack;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.percent = 1;
        if (this.parent.getWorld().getGameTime() % 60 == 0) {
            final ChunkLevel chunkLevel = PollutionManager.pollutionManager.getChunkLevelAir(this.getChunkPos());
            if (chunkLevel != null) {
                switch (chunkLevel.getLevelPollution()) {
                    case LOW:
                    case VERY_LOW:
                        break;
                    case MEDIUM:
                        this.percent = 2;
                        break;
                    case HIGH:
                        this.percent = 4;
                        break;
                    case VERY_HIGH:
                        this.percent = 8;
                        break;
                }
            } else {
                this.percent = 1;
            }

        }
        this.timer.percent = this.percent;
    }

    @Override
    public boolean onBlockActivated(final Player player, final InteractionHand hand) {
        super.onBlockActivated(player, hand);
        final ItemStack stack = player.getItemInHand(hand);
        if (!this.active && !stack.isEmpty()) {
            if (stack.is(this.stack.getItem())) {
                this.active = true;
                stack.shrink(1);
                this.timer.setCanWork(false);
                return true;
            }
        }
        return false;
    }

    public void onContainerUpdate(ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16, player.registryAccess());
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
    public boolean canUsePurifier(final Player player) {
        return this.active;
    }

    @Override
    public CompoundTag writeToNbt() {
        CompoundTag nbt = super.writeToNbt();
        nbt.putBoolean("active", active);
        return nbt;
    }

    @Override
    public void readFromNbt(final CompoundTag nbt) {
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
