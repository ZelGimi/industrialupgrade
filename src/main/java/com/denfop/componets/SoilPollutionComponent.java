package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.pollution.IPollutionMechanism;
import com.denfop.api.pollution.PollutionSoilLoadEvent;
import com.denfop.api.pollution.PollutionSoilUnLoadEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.List;

public class SoilPollutionComponent extends AbstractComponent implements IPollutionMechanism {

    private double pollution;
    private double default_pollution;
    private ChunkPos chunkPos;

    private double percent = 1;

    public SoilPollutionComponent(final TileEntityInventory parent, double pollution) {
        super(parent);
        this.pollution = pollution;
        this.default_pollution = pollution;
    }
    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        if (this.parent != null && this.parent.getWorld() == null) {
            tooltip.add(Localization.translate("iu.pollution.soil.info") + " " + String.format(
                    "%.2f",
                    default_pollution
            ) + Localization.translate("iu" +
                    ".pollution.soil.info1"));
        }
    }

    @Override
    public CompoundTag writeToNbt() {
        CompoundTag tagCompound = super.writeToNbt();
        tagCompound.putDouble("percent", percent);
        return tagCompound;
    }

    @Override
    public void readFromNbt(final CompoundTag nbt) {
        super.readFromNbt(nbt);
        percent = nbt.getDouble("percent");
    }

    public TypePurifierJob getPurifierJob() {
        return TypePurifierJob.ItemStack;
    }

    @Override
    public boolean canUsePurifier(final Player player) {
        return this.percent != 1;
    }

    public ItemStack getItemStackUpgrade() {
        if (this.percent == 0) {
            this.percent = 0.5;
            return new ItemStack(IUItem.antisoilpollution1.getItem());
        } else {
            this.percent = 1;
            return new ItemStack(IUItem.antisoilpollution.getItem());
        }
    }

    @Override
    public List<ItemStack> getDrops() {
        final List<ItemStack> ret = super.getDrops();
        if (this.percent == 0) {
            ret.add(new ItemStack(IUItem.antisoilpollution1.getItem()));
            ret.add(new ItemStack(IUItem.antisoilpollution.getItem()));
        } else if (percent == 0.5) {
            ret.add(new ItemStack(IUItem.antisoilpollution.getItem()));
        }
        return ret;
    }

    @Override
    public boolean onBlockActivated(final Player player, final InteractionHand hand) {
        if (!this.parent.getLevel().isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            Item item = stack.getItem();
            if (percent == 1 && item == IUItem.antisoilpollution.getItem()) {
                stack.shrink(1);
                percent = 0.5;
                if (this.parent.getActive()) {
                    this.setPollution(this.default_pollution * this.percent);
                } else {
                    this.setPollution(0);
                }
            } else if (percent == 0.5 && item == IUItem.antisoilpollution1.getItem()) {
                stack.shrink(1);
                percent = 0;
                if (this.parent.getActive()) {
                    this.setPollution(this.default_pollution * this.percent);
                } else {
                    this.setPollution(0);
                }
            }
        }
        return super.onBlockActivated(player, hand);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.parent.getActive()) {
            this.setPollution(this.default_pollution * percent);
        } else {
            this.setPollution(0);
        }
    }

    public void onLoaded() {


        if (!this.parent.getLevel().isClientSide && this.parent.getLevel().dimension() == Level.OVERWORLD) {

            MinecraftForge.EVENT_BUS.post(new PollutionSoilLoadEvent(this.parent.getLevel(), this));


        }

    }

    public void onContainerUpdate(ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
        buffer.writeDouble(this.pollution);
        buffer.writeDouble(this.default_pollution);
        buffer.writeDouble(this.percent);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public CustomPacketBuffer updateComponent() {
        final CustomPacketBuffer buffer = super.updateComponent();
        buffer.writeDouble(this.pollution);
        buffer.writeDouble(this.default_pollution);
        buffer.writeDouble(this.percent);
        return buffer;
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {

        this.pollution = is.readDouble();
        this.default_pollution = is.readDouble();
        this.percent = is.readDouble();

    }

    public double getDefault_pollution() {
        return default_pollution;
    }

    @Override
    public void onUnloaded() {
        if (!this.parent.getLevel().isClientSide && this.parent.getLevel().dimension() == Level.OVERWORLD) {

            MinecraftForge.EVENT_BUS.post(new PollutionSoilUnLoadEvent(this.parent.getLevel(), this));


        }
    }

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public ChunkPos getChunkPos() {
        if (this.chunkPos == null) {
            chunkPos = new ChunkPos(this.getParent().getBlockPos());
        }
        return chunkPos;
    }

    @Override
    public double getPollution() {
        return pollution;
    }

    public void setPollution(double pollution) {
        if (this.pollution != pollution) {
            if (!this.parent.getLevel().isClientSide && this.parent.getLevel().dimension() == Level.OVERWORLD) {
                MinecraftForge.EVENT_BUS.post(new PollutionSoilUnLoadEvent(this.parent.getLevel(), this));
                this.pollution = pollution * percent;
                MinecraftForge.EVENT_BUS.post(new PollutionSoilLoadEvent(this.parent.getLevel(), this));

            }
        }
    }

}
