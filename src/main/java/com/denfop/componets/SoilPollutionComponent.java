package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.api.pollution.soil.PollutionSoilLoadEvent;
import com.denfop.api.pollution.soil.PollutionSoilUnLoadEvent;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.Localization;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.List;

public class SoilPollutionComponent extends AbstractComponent {

    private PollutionMechanism pollution;
    private double default_pollution;
    private ChunkPos chunkPos;
    private double percent = 1;

    public SoilPollutionComponent(final BlockEntityInventory parent, double pollution) {
        super(parent);
        this.pollution = new PollutionMechanism(new ChunkPos(parent.pos));
        this.default_pollution = pollution;
    }

    public static void spawnSoilPollution(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.1;
        double z = pos.getZ() + 0.5;


        if (random.nextFloat() < 0.3f) {
            Vector3f dirtyColor = new Vector3f(0.2f, 0.4f, 0.1f);
            server.sendParticles(new DustParticleOptions(dirtyColor, 0.8f),
                    x, y, z, 2, 0.02, 0.005, 0.02, 0.001);
        }
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
        if (this.parent.getWorld().getGameTime() % 20 == 0 && this.parent.getActive()) {
            spawnSoilPollution(parent.getWorld(), parent.getPos(), parent.getWorld().random);
        }
        if (this.parent.getActive()) {
            this.setPollution(this.default_pollution * percent);
        } else {
            this.setPollution(0);
        }
    }

    public void onLoaded() {


        if (!this.parent.getLevel().isClientSide && this.parent.getLevel().dimension() == Level.OVERWORLD) {

            MinecraftForge.EVENT_BUS.post(new PollutionSoilLoadEvent(this.parent.getLevel(), pollution));


        }

    }

    public void onContainerUpdate(ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
        buffer.writeDouble(this.pollution.pollution);
        buffer.writeDouble(this.default_pollution);
        buffer.writeDouble(this.percent);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public CustomPacketBuffer updateComponent() {
        final CustomPacketBuffer buffer = super.updateComponent();
        buffer.writeDouble(this.pollution.pollution);
        buffer.writeDouble(this.default_pollution);
        buffer.writeDouble(this.percent);
        return buffer;
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {

        this.pollution.pollution = is.readDouble();
        this.default_pollution = is.readDouble();
        this.percent = is.readDouble();

    }

    public double getDefault_pollution() {
        return default_pollution;
    }

    @Override
    public void onUnloaded() {
        if (!this.parent.getLevel().isClientSide && this.parent.getLevel().dimension() == Level.OVERWORLD) {

            MinecraftForge.EVENT_BUS.post(new PollutionSoilUnLoadEvent(this.parent.getLevel(), pollution));


        }
    }

    @Override
    public boolean isServer() {
        return true;
    }


    public void setPollution(double pollution) {
        if (this.pollution.pollution != pollution) {
            if (!this.parent.getLevel().isClientSide && this.parent.getLevel().dimension() == Level.OVERWORLD) {
                MinecraftForge.EVENT_BUS.post(new PollutionSoilUnLoadEvent(this.parent.getLevel(), this.pollution));
                this.pollution.pollution = pollution * percent;
                MinecraftForge.EVENT_BUS.post(new PollutionSoilLoadEvent(this.parent.getLevel(), this.pollution));

            }
        }
    }

}
