package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.pollution.radiation.Radiation;
import com.denfop.api.pollution.radiation.RadiationSystem;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSoilAnalyzer;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSoilAnalyzer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class BlockEntitySoilAnalyzer extends BlockEntityElectricMachine {

    private static final List<AABB> aabbs = Collections.singletonList(new AABB(-0.2, 0.0D, -0.2, 1.2, 2.0D,
            1.2
    ));
    public final ComponentProgress progress;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public boolean analyzed;
    public Radiation radiation;

    public BlockEntitySoilAnalyzer(BlockPos pos, BlockState state) {
        super(5000, 14, 0, BlockBaseMachine3Entity.soil_analyzer, pos, state);
        this.progress = this.addComponent(new ComponentProgress(this, 1, (short) 400));
        this.analyzed = false;
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.05));
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        nbttagcompound = super.writeToNBT(nbttagcompound);
        nbttagcompound.putBoolean("analyzed", analyzed);
        return nbttagcompound;
    }

    @Override
    public ContainerMenuSoilAnalyzer getGuiContainer(final Player var1) {
        return new ContainerMenuSoilAnalyzer(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenSoilAnalyzer((ContainerMenuSoilAnalyzer) menu);
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.analyzed);
        customPacketBuffer.writeBoolean(this.radiation != null);
        if (this.radiation != null) {
            try {
                EncoderHandler.encode(customPacketBuffer, this.radiation);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        analyzed = customPacketBuffer.readBoolean();
        boolean notNull = customPacketBuffer.readBoolean();
        if (notNull) {
            try {
                this.radiation = (Radiation) DecoderHandler.decode(customPacketBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.analyzed = nbttagcompound.getBoolean("analyzed");
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.energy.getEnergy() > 20 && this.progress.getBar() < 1) {
            this.energy.useEnergy(20);
            this.progress.addProgress();
            if (this.progress.getProgress() >= this.progress.getMaxValue()) {
                this.analyzed = true;
                this.radiation = RadiationSystem.rad_system.getMap().get(this
                        .getWorld()
                        .getChunkAt(this.pos)
                        .getPos());
            }
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            if (this.analyzed) {
                this.radiation = RadiationSystem.rad_system.getMap().get(this
                        .getWorld()
                        .getChunkAt(this.pos)
                        .getPos());
            }

        }
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.soil_analyzer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


    public List<AABB> getAabbs(boolean forCollision) {
        return aabbs;
    }

}
