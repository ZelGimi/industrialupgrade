package com.denfop.blockentity.gaswell;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.vein.gas.GasBaseVein;
import com.denfop.api.vein.gas.GasVeinSystem;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasWellEntity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuGasWellAnalyzer;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenGasWellAnalyzer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEntityGasWellAnalyzer extends BlockEntityMultiBlockElement implements IAnalyzer, IUpdatableTileEvent {

    public int progress;
    public GasBaseVein vein;
    public int col;
    public boolean work;

    public BlockEntityGasWellAnalyzer(BlockPos pos, BlockState state) {
        super(BlockGasWellEntity.gas_well_analyzer, pos, state);
    }


    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_well.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasWellEntity.gas_well_analyzer;
    }

    @Override
    public ContainerMenuGasWellAnalyzer getGuiContainer(final Player var1) {
        return new ContainerMenuGasWellAnalyzer(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenGasWellAnalyzer((ContainerMenuGasWellAnalyzer) menu);
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
        if (hasVein) {
            vein = new GasBaseVein(customPacketBuffer);
        }
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        nbt.putBoolean("work", this.work);
        nbt.putInt("progress", this.progress);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.work = nbtTagCompound.getBoolean("analyzer");
        this.progress = nbtTagCompound.getInt("progress");
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.level.isClientSide) {
            return;
        }
        if (getWorld().dimension() != Level.OVERWORLD) {
            this.vein = GasVeinSystem.system.getEMPTY();
        } else {
            LevelChunk chunk = this.getWorld().getChunkAt(this.pos);
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


        if (work && this.progress < 1200 && ((IController) this.getMain()).getEnergy().getEnergy() >= 5 && !this.vein.isFind()) {
            progress++;
            ((IController) this.getMain()).getEnergy().useEnergy(5);
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
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 == 0 && this.getMain() != null && !vein.isFind() && progress < 1200) {
            work = !work;
        }
    }

}
