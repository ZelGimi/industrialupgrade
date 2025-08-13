package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.pollution.IPollutionMechanism;
import com.denfop.api.pollution.PollutionAirLoadEvent;
import com.denfop.api.pollution.PollutionAirUnLoadEvent;
import com.denfop.api.windsystem.WindSystem;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.List;

public class AirPollutionComponent extends AbstractComponent  {


    private final PollutionMechanism pollution;
    private double default_pollution;

    private double percent = 1;

    public AirPollutionComponent(final TileEntityInventory parent, double pollution) {
        super(parent);
        this.pollution = new PollutionMechanism(new ChunkPos(parent.pos));
        this.default_pollution = pollution;
    }
    public static void spawnAirPollutionDirected(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        Vec3 Vec3 = WindSystem.windSystem.getWindSide().getDirectionVector();
        double dx = 0, dz = 0;
        dx += Vec3.x;
        dz += Vec3.z;


        double magnitude = Math.sqrt(dx * dx + dz * dz);
        if (magnitude != 0) {
            dx /= magnitude;
            dz /= magnitude;
        }

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.2;
        double z = pos.getZ() + 0.5;


        if (random.nextFloat() < 0.5f) {
            server.sendParticles(ParticleTypes.CLOUD, x, y, z, 0,
                    0.05, 0.1, 0.05, 0.1);


            for (int i = 0; i < 2; i++) {
                double ox = x + (random.nextDouble() - 0.5) * 0.4;
                double oy = y + random.nextDouble() * 0.2;
                double oz = z + (random.nextDouble() - 0.5) * 0.4;
                double vx = dx * 0.05 + (random.nextDouble() - 0.5) * 0.01;
                double vz = dz * 0.05 + (random.nextDouble() - 0.5) * 0.01;

                server.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, ox, oy, oz, 0, vx, 0.1, vz, 1);
            }
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

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.parent.getWorld().getGameTime() % 20 == 0 && this.parent.getActive()) {
            spawnAirPollutionDirected(parent.getWorld(),parent.getPos(),parent.getWorld().random);
        }
        if (this.parent.getActive()) {
            this.setPollution(this.default_pollution * this.percent);
        } else {
            this.setPollution(0);
        }
    }

    public void onLoaded() {


        if (!this.parent.getLevel().isClientSide && this.parent.getLevel().dimension() == Level.OVERWORLD) {

            MinecraftForge.EVENT_BUS.post(new PollutionAirLoadEvent(this.parent.getLevel(), pollution));


        }

    }

    public double getDefault_pollution() {
        return default_pollution;
    }

    @Override
    public void onUnloaded() {
        if (!this.parent.getLevel().isClientSide && this.parent.getLevel().dimension() == Level.OVERWORLD) {

            MinecraftForge.EVENT_BUS.post(new PollutionAirUnLoadEvent(this.parent.getLevel(), pollution));


        }
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
        if (this.pollution.pollution != pollution * percent) {
            if (!this.parent.getLevel().isClientSide && this.parent.getLevel().dimension() == Level.OVERWORLD) {
                MinecraftForge.EVENT_BUS.post(new PollutionAirUnLoadEvent(this.parent.getLevel(), this.pollution));
                this.pollution.pollution = pollution * percent;
                MinecraftForge.EVENT_BUS.post(new PollutionAirLoadEvent(this.parent.getLevel(), this.pollution));

            }
        }
    }

}
