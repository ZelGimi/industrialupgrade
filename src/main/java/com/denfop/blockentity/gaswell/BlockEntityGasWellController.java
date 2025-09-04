package com.denfop.blockentity.gaswell;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.vein.gas.GasBaseVein;
import com.denfop.api.vein.gas.GasVeinSystem;
import com.denfop.api.vein.gas.TypeGas;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockBase;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockGasWellEntity;
import com.denfop.componets.Energy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuGasWellController;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.screen.ScreenGasWellController;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.io.IOException;
import java.util.List;

public class BlockEntityGasWellController extends BlockEntityMultiBlockBase implements IController, IUpdatableTileEvent {

    public boolean work;
    public ITank tank;
    public ISocket socket;
    public GasBaseVein vein;

    public BlockEntityGasWellController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.GasWellMultiBlock, BlockGasWellEntity.gas_well_controller, pos, state);

    }

    @Override
    public ContainerMenuGasWellController getGuiContainer(final Player var1) {
        return new ContainerMenuGasWellController(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenGasWellController((ContainerMenuGasWellController) menu);
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
            final LevelChunk chunk = this.getWorld().getChunkAt(this.pos);
            final ChunkPos chunkpos = chunk.getPos();
            if (!GasVeinSystem.system.getChunkPos().contains(chunkpos)) {
                GasVeinSystem.system.addVein(chunk);
            }
            this.vein = GasVeinSystem.system.getVein(chunkpos);
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.full) {
            if (this.work && this.vein != null && vein.isFind() && this.getEnergy().canUseEnergy(2)) {
                int amount = vein.getCol();
                amount = Math.min(Math.min(1, amount), tank.getTank().getCapacity() - tank.getTank().getFluidAmount());
                Fluid fluid = null;
                if (vein.getType() == TypeGas.IODINE) {
                    fluid = FluidName.fluidiodine.getInstance().get();
                }


                if (vein.getType() == TypeGas.BROMIDE) {
                    fluid = FluidName.fluidbromine.getInstance().get();
                }

                if (vein.getType() == TypeGas.CHLORINE) {
                    fluid = FluidName.fluidchlorum.getInstance().get();
                }
                if (vein.getType() == TypeGas.FLORINE) {
                    fluid = FluidName.fluidfluor.getInstance().get();
                }

                if (fluid == null) {
                    return;
                }
                this.getEnergy().useEnergy(2);
                vein.removeCol(amount);
                tank.getTank().fill(new FluidStack(fluid, amount), IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_well.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasWellEntity.gas_well_controller;
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        work = !work;
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBytes(socket.getEnergy().updateComponent());
        customPacketBuffer.writeBytes(tank.getTank().writePacket());
        customPacketBuffer.writeBoolean(vein != null);
        customPacketBuffer.writeBytes(vein.writePacket());
        customPacketBuffer.writeBoolean(work);
        return customPacketBuffer;
    }


    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);

        try {
            socket.getEnergy().onNetworkUpdate(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tank.getTank().readPacket(customPacketBuffer);
        final boolean hasVein = customPacketBuffer.readBoolean();
        if (hasVein) {
            vein = new GasBaseVein(customPacketBuffer);
        }
        work = customPacketBuffer.readBoolean();
    }

    @Override
    public void setFull(final boolean full) {
        super.setFull(full);
        if (!full) {
            this.socket = null;
            this.tank = null;
        }


    }

    @Override
    public Energy getEnergy() {
        return socket.getEnergy();
    }

    @Override
    public void updateAfterAssembly() {

        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ISocket.class
                );
        this.socket = (ISocket) this.getWorld().getBlockEntity(pos1.get(0));

        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ITank.class
                );
        this.tank = (ITank) this.getWorld().getBlockEntity(pos1.get(0));
    }

    @Override
    public void usingBeforeGUI() {

    }

}
