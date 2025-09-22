package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.pollution.IPollutionMechanism;
import com.denfop.api.pollution.PollutionSoilLoadEvent;
import com.denfop.api.pollution.PollutionSoilUnLoadEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.ChunkPos;
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
    public NBTTagCompound writeToNbt() {
        NBTTagCompound tagCompound = super.writeToNbt();
        tagCompound.setDouble("percent", percent);
        return tagCompound;
    }

    @Override
    public void readFromNbt(final NBTTagCompound nbt) {
        super.readFromNbt(nbt);
        percent = nbt.getDouble("percent");
    }

    public TypePurifierJob getPurifierJob() {
        return TypePurifierJob.ItemStack;
    }

    @Override
    public boolean canUsePurifier(final EntityPlayer player) {
        return this.percent != 1;
    }

    public ItemStack getItemStackUpgrade() {
        if (this.percent == 0) {
            this.percent = 0.5;
            return new ItemStack(IUItem.antisoilpollution1);
        } else {
            this.percent = 1;
            return new ItemStack(IUItem.antisoilpollution);
        }
    }

    @Override
    public List<ItemStack> getDrops() {
        final List<ItemStack> ret = super.getDrops();
        if (this.percent == 0) {
            ret.add(new ItemStack(IUItem.antisoilpollution1));
            ret.add(new ItemStack(IUItem.antisoilpollution));
        } else if (percent == 0.5) {
            ret.add(new ItemStack(IUItem.antisoilpollution));
        }
        return ret;
    }

    @Override
    public boolean onBlockActivated(final EntityPlayer player, final EnumHand hand) {
        if (!this.parent.getWorld().isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            Item item = stack.getItem();
            if (percent == 1 && item == IUItem.antisoilpollution) {
                stack.shrink(1);
                percent = 0.5;
                if (this.parent.getActive()) {
                    this.setPollution(this.default_pollution * this.percent);
                } else {
                    this.setPollution(0);
                }
            } else if (percent == 0.5 && item == IUItem.antisoilpollution1) {
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


        if (!this.parent.getWorld().isRemote && this.parent.getWorld().provider.getDimension() == 0) {

            MinecraftForge.EVENT_BUS.post(new PollutionSoilLoadEvent(this.parent.getWorld(), this));


        }

    }

    public void onContainerUpdate(EntityPlayerMP player) {
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
        if (!this.parent.getWorld().isRemote && this.parent.getWorld().provider.getDimension() == 0) {

            MinecraftForge.EVENT_BUS.post(new PollutionSoilUnLoadEvent(this.parent.getWorld(), this));


        }
    }

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public ChunkPos getChunkPos() {
        if (this.chunkPos == null) {
            chunkPos = new ChunkPos(this.getParent().getPos());
        }
        return chunkPos;
    }

    @Override
    public double getPollution() {
        return pollution;
    }

    public void setPollution(double pollution) {
        if (this.pollution != pollution) {
            if (!this.parent.getWorld().isRemote && this.parent.getWorld().provider.getDimension() == 0) {
                MinecraftForge.EVENT_BUS.post(new PollutionSoilUnLoadEvent(this.parent.getWorld(), this));
                this.pollution = pollution * percent;
                MinecraftForge.EVENT_BUS.post(new PollutionSoilLoadEvent(this.parent.getWorld(), this));

            }
        }
    }

}
