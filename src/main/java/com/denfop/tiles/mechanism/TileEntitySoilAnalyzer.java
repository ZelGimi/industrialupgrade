package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSoilAnalyzer;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSoilAnalyzer;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TileEntitySoilAnalyzer extends TileElectricMachine {

    private static final List<AABB> aabbs = Collections.singletonList(new AABB(-0.2, 0.0D, -0.2, 1.2, 2.0D,
            1.2
    ));
    public final ComponentProgress progress;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public boolean analyzed;
    public Radiation radiation;

    public TileEntitySoilAnalyzer(BlockPos pos, BlockState state) {
        super(5000, 14, 0,BlockBaseMachine3.soil_analyzer,pos,state);
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
    public ContainerSoilAnalyzer getGuiContainer(final Player var1) {
        return new ContainerSoilAnalyzer(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiSoilAnalyzer((ContainerSoilAnalyzer) menu);
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

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.soil_analyzer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }



    public List<AABB> getAabbs(boolean forCollision) {
        return aabbs;
    }

}
