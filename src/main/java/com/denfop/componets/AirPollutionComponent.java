package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.pollution.IPollutionMechanism;
import com.denfop.api.pollution.PollutionAirLoadEvent;
import com.denfop.api.pollution.PollutionAirUnLoadEvent;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

public class AirPollutionComponent extends AbstractComponent implements IPollutionMechanism {

    private double pollution;
    private double default_pollution;
    private ChunkPos chunkPos;
    private double percent = 1;

    public AirPollutionComponent(final TileEntityInventory parent, double pollution) {
        super(parent);
        this.pollution = pollution;
        this.default_pollution = pollution;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        if (this.parent != null && this.parent.getWorld() == null) {
            tooltip.add(Localization.translate("iu.pollution.air.info") + " " + String.format(
                    "%.2f",
                    default_pollution
            ) + Localization.translate("iu" +
                    ".pollution.air.info1"));
        }
    }

    @Override
    public NBTTagCompound writeToNbt() {
        NBTTagCompound tagCompound = super.writeToNbt();
        tagCompound.setDouble("percent", percent);
        return tagCompound;
    }

    @Override
    public boolean isClient() {
        return true;
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
            return new ItemStack(IUItem.antiairpollution1);
        } else {
            this.percent = 1;
            return new ItemStack(IUItem.antiairpollution);
        }
    }

    @Override
    public List<ItemStack> getDrops() {
        final List<ItemStack> ret = super.getDrops();
        if (this.percent == 0) {
            ret.add(new ItemStack(IUItem.antiairpollution1));
            ret.add(new ItemStack(IUItem.antiairpollution));
        } else if (percent == 0.5) {
            ret.add(new ItemStack(IUItem.antiairpollution));
        }
        return ret;
    }

    @Override
    public boolean onBlockActivated(final EntityPlayer player, final EnumHand hand) {
        if (!this.parent.getWorld().isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            Item item = stack.getItem();
            if (percent == 1 && item == IUItem.antiairpollution) {
                stack.shrink(1);
                percent = 0.5;
                if (this.parent.getActive()) {
                    this.setPollution(this.default_pollution * this.percent);
                } else {
                    this.setPollution(0);
                }
            } else if (percent == 0.5 && item == IUItem.antiairpollution1) {
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

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.parent.getActive()) {

           /*    if (this.parent.getWorld().getWorldTime() % 4 == 0) {
                Vec3d windDirection = WindSystem.windSystem.getWindSide().getDirectionVector();
                double windSpeed = WindSystem.windSystem.getSpeed() / 32F;
                double motionX = windDirection.x * windSpeed;
                double motionY = 0.02D;
                double motionZ = windDirection.z * windSpeed;
                Minecraft.getMinecraft().effectRenderer.addEffect(new CustomPollutionParticle(this.parent.getWorld(),
                        this.parent.getPos().getX(),
                        this.parent.getPos().getY() + 1,
                        this.parent.getPos().getZ(),motionX,motionY,motionZ
                ));
            }*/
        } else {

        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.parent.getActive()) {
            this.setPollution(this.default_pollution * this.percent);
        } else {
            this.setPollution(0);
        }
    }

    public void onLoaded() {


        if (!this.parent.getWorld().isRemote && this.parent.getWorld().provider.getDimension() == 0) {

            MinecraftForge.EVENT_BUS.post(new PollutionAirLoadEvent(this.parent.getWorld(), this));


        }

    }

    public double getDefault_pollution() {
        return default_pollution;
    }

    @Override
    public void onUnloaded() {
        if (!this.parent.getWorld().isRemote && this.parent.getWorld().provider.getDimension() == 0) {

            MinecraftForge.EVENT_BUS.post(new PollutionAirUnLoadEvent(this.parent.getWorld(), this));


        }
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
        if (this.pollution != pollution * percent) {
            if (!this.parent.getWorld().isRemote && this.parent.getWorld().provider.getDimension() == 0) {
                MinecraftForge.EVENT_BUS.post(new PollutionAirUnLoadEvent(this.parent.getWorld(), this));
                this.pollution = pollution * percent;
                MinecraftForge.EVENT_BUS.post(new PollutionAirLoadEvent(this.parent.getWorld(), this));

            }
        }
    }

}
