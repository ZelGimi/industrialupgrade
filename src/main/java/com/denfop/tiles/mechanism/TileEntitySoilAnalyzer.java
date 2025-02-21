package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerSoilAnalyzer;
import com.denfop.gui.GuiSoilAnalyzer;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TileEntitySoilAnalyzer extends TileElectricMachine {

    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(-0.2, 0.0D, -0.2, 1.2, 2.0D,
            1.2
    ));
    public final ComponentProgress progress;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public boolean analyzed;
    public Radiation radiation;

    public TileEntitySoilAnalyzer() {
        super(5000, 14, 0);
        this.progress = this.addComponent(new ComponentProgress(this, 1, (short) 400));
        this.analyzed = false;
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.05));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound = super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("analyzed", analyzed);
        return nbttagcompound;
    }

    @Override
    public ContainerSoilAnalyzer getGuiContainer(final EntityPlayer var1) {
        return new ContainerSoilAnalyzer(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiSoilAnalyzer(getGuiContainer(var1));
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
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
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
                        .getChunkFromBlockCoords(this.pos)
                        .getPos());
            }
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            if (this.analyzed) {
                this.radiation = RadiationSystem.rad_system.getMap().get(this
                        .getWorld()
                        .getChunkFromBlockCoords(this.pos)
                        .getPos());
            }

        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.soil_analyzer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return aabbs;
    }

}
