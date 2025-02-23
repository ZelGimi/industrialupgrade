package com.denfop.tiles.gaswell;

import com.denfop.IUItem;
import com.denfop.api.gasvein.GasVein;
import com.denfop.api.gasvein.GasVeinSystem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.vein.IVein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEarthQuarry;
import com.denfop.blocks.mechanism.BlockGasWell;
import com.denfop.container.ContainerEarthAnalyzer;
import com.denfop.container.ContainerGasWellAnalyzer;
import com.denfop.gui.GuiEarthAnalyzer;
import com.denfop.gui.GuiGasWellAnalyzer;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileEntityGasWellAnalyzer extends TileEntityMultiBlockElement implements IAnalyzer, IUpdatableTileEvent {

    public int progress;
    public GasVein vein;
    public int col;
    public boolean work;

    public TileEntityGasWellAnalyzer() {

    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_well;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasWell.gas_well_analyzer;
    }

    @Override
    public ContainerGasWellAnalyzer getGuiContainer(final EntityPlayer var1) {
        return new ContainerGasWellAnalyzer(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiGasWellAnalyzer(getGuiContainer(var1));
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(work);
        customPacketBuffer.writeInt(progress);
        customPacketBuffer.writeBoolean(vein != null);
        customPacketBuffer.writeBytes(vein.writePacket());
        return customPacketBuffer;
    }

    @Override
    public boolean hasOwnInventory() {
        return getMain() != null;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        work = customPacketBuffer.readBoolean();
        progress = customPacketBuffer.readInt();
        final boolean hasVein = customPacketBuffer.readBoolean();
        if (hasVein){
            vein = new GasVein(customPacketBuffer);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        nbt.setBoolean("work", this.work);
        nbt.setInteger("progress", this.progress);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.work = nbtTagCompound.getBoolean("analyzer");
        this.progress = nbtTagCompound.getInteger("progress");
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.world.isRemote) {
            return;
        }
        if (getWorld().provider.getDimension() != 0) {
            this.vein = GasVeinSystem.system.getEMPTY();
        } else {
            final Chunk chunk = this.getWorld().getChunkFromBlockCoords(this.pos);
            final ChunkPos chunkpos = chunk.getPos();
            if (!GasVeinSystem.system.getChunkPos().contains(chunkpos)) {
                GasVeinSystem.system.addVein(chunk);
            }
            this.vein = GasVeinSystem.system.getVein(chunkpos);


            if (this.vein.isFind()) {
                this.progress = 1200;
            }
            if (this.progress >= 1200) {
                this.vein.setFind(true);
            }
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.vein == null) {
            return;
        }



        if (work && this.progress < 1200 && ((IController)this.getMain()).getEnergy().getEnergy() >= 5 && !this.vein.isFind()) {
            progress++;
            ((IController)this.getMain()).getEnergy().useEnergy(5);
            if (progress >= 1200) {
                this.progress = 1200;
                this.vein.setFind(true);
            }


        } else {
            if (work) {
                work = false;
            }

        }
    }


    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if (var2 == 0 && this.getMain() != null && !vein.isFind() && progress < 1200) {
            work = !work;
        }
    }

}
