package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.pollution.IPollutionMechanism;
import com.denfop.api.pollution.PollutionAirLoadEvent;
import com.denfop.api.pollution.PollutionAirUnLoadEvent;
import com.denfop.api.windsystem.WindSystem;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

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

    @OnlyIn(Dist.CLIENT)
    public static void spawnCustomPollutionParticle(Level world, double x, double y, double z, double windSpeed) {
        if (world != null) {
            Vec3 windDirection = WindSystem.windSystem.getWindSide().getDirectionVector();


            double motionX = windDirection.x * windSpeed * 0.5;
            double motionY = 0.01D;
            double motionZ = windDirection.z * windSpeed * 0.5;

            world.addParticle(
                    ParticleTypes.SMOKE,
                    x, y, z,
                    motionX, motionY, motionZ
            );
        }
    }

    @Override
    public CompoundTag writeToNbt() {
        CompoundTag tagCompound = super.writeToNbt();
        tagCompound.putDouble("percent", percent);
        return tagCompound;
    }

    @Override
    public boolean isClient() {
        return true;
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
            return new ItemStack(IUItem.antiairpollution1.getItem());
        } else {
            this.percent = 1;
            return new ItemStack(IUItem.antiairpollution.getItem());
        }
    }

    @Override
    public List<ItemStack> getDrops() {
        final List<ItemStack> ret = super.getDrops();
        if (this.percent == 0) {
            ret.add(new ItemStack(IUItem.antiairpollution1.getItem()));
            ret.add(new ItemStack(IUItem.antiairpollution.getItem()));
        } else if (percent == 0.5) {
            ret.add(new ItemStack(IUItem.antiairpollution.getItem()));
        }
        return ret;
    }

    @Override
    public boolean onBlockActivated(final Player player, final InteractionHand hand) {
        if (!this.parent.getLevel().isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            Item item = stack.getItem();
            if (percent == 1 && item == IUItem.antiairpollution.getItem()) {
                stack.shrink(1);
                percent = 0.5;
                if (this.parent.getActive()) {
                    this.setPollution(this.default_pollution * this.percent);
                } else {
                    this.setPollution(0);
                }
            } else if (percent == 0.5 && item == IUItem.antiairpollution1.getItem()) {
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

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
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


        if (!this.parent.getLevel().isClientSide && this.parent.getLevel().dimension() == Level.OVERWORLD) {

            MinecraftForge.EVENT_BUS.post(new PollutionAirLoadEvent(this.parent.getLevel(), this));


        }

    }

    public double getDefault_pollution() {
        return default_pollution;
    }

    @Override
    public void onUnloaded() {
        if (!this.parent.getLevel().isClientSide && this.parent.getLevel().dimension() == Level.OVERWORLD) {

            MinecraftForge.EVENT_BUS.post(new PollutionAirUnLoadEvent(this.parent.getLevel(), this));


        }
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

    public void setPollution(double pollution) {
        if (this.pollution != pollution * percent) {
            if (!this.parent.getLevel().isClientSide && this.parent.getLevel().dimension() == Level.OVERWORLD) {
                MinecraftForge.EVENT_BUS.post(new PollutionAirUnLoadEvent(this.parent.getLevel(), this));
                this.pollution = pollution * percent;
                MinecraftForge.EVENT_BUS.post(new PollutionAirLoadEvent(this.parent.getLevel(), this));

            }
        }
    }

}
