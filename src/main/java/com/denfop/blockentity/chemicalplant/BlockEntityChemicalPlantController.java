package com.denfop.blockentity.chemicalplant;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockBase;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockChemicalPlantEntity;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuChemicalController;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.screen.ScreenChemicalController;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityChemicalPlantController extends BlockEntityMultiBlockBase implements IController {

    public boolean work = false;
    List<IGenerator> generatorList = new ArrayList<>();
    IWaste waste;
    List<ISeparate> separateList = new ArrayList<>();
    List<IExchanger> exchangerList = new ArrayList<>();

    public BlockEntityChemicalPlantController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.ChemicalPlantMultiBlock, BlockChemicalPlantEntity.chemical_plant_controller, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockChemicalPlantEntity.chemical_plant_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.chemicalPlant.getBlock(getTeBlock().getId());
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        this.work = !work;
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        nbttagcompound.putBoolean("work", work);
        return super.writeToNBT(nbttagcompound);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.work && this.isFull()) {

            Fluids.InternalFluidTank fluidTankWaste = waste.getFluidTank();
            int canAdd = fluidTankWaste.getCapacity() - fluidTankWaste.getFluidAmount();
            if (canAdd == 0) {
                return;
            }
            int defaultAdd = canAdd;
            for (ISeparate separate : this.separateList) {
                if (defaultAdd == 0) {
                    return;
                }

                Fluids.InternalFluidTank fluidTank = separate.getFluidTank();
                if (fluidTank.getFluidAmount() == 0) {
                    continue;
                }
                canAdd = Math.min(Math.min(defaultAdd, fluidTank.getFluidAmount() / 10), 10);
                if (canAdd == 0) {
                    return;
                }
                double energy = 0;
                for (IGenerator generator : this.generatorList) {
                    energy += generator.getEnergy().getEnergy();
                }
                canAdd = (int) Math.min(energy / 5, canAdd);
                for (IExchanger exchanger : this.exchangerList) {
                    if (canAdd == 0) {
                        return;
                    }
                    Fluids.InternalFluidTank fluidTank1 = exchanger.getFluidTank();
                    final int canAdd1 = (int) Math.min(canAdd, fluidTank1.getFluidAmount() / 2D);
                    defaultAdd -= canAdd1;
                    canAdd -= canAdd1;
                    fluidTankWaste.fill(new FluidStack(FluidName.fluidcryogen.getInstance().get(), canAdd1), IFluidHandler.FluidAction.EXECUTE);
                    fluidTank1.drain(canAdd1 * 2, IFluidHandler.FluidAction.EXECUTE);
                    fluidTank.drain(canAdd1 * 10, IFluidHandler.FluidAction.EXECUTE);
                    double energy1 = canAdd1 * 5;
                    for (IGenerator generator : this.generatorList) {
                        if (generator.getEnergy().getEnergy() > energy1) {
                            generator.getEnergy().useEnergy(energy1);
                            break;
                        } else {
                            energy1 -= generator.getEnergy().getEnergy();
                            generator.getEnergy().useEnergy(generator.getEnergy().getEnergy());
                        }
                    }
                }

            }


        }
    }

    @Override
    public ContainerMenuChemicalController getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuChemicalController(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(final Player var1, final ContainerMenuBase<?> var2) {
        return new ScreenChemicalController((ContainerMenuChemicalController) var2);
    }

    @Override
    public void setFull(final boolean full) {
        super.setFull(full);
        if (!full) {
            this.generatorList.clear();
            this.exchangerList.clear();
            this.separateList.clear();
            this.waste = null;
        }


    }

    @Override
    public void updateAfterAssembly() {

        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IWaste.class
                );
        this.waste = (IWaste) this.getWorld().getBlockEntity(pos1.get(0));
        List<BlockPos> pos2 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IGenerator.class
                );
        for (BlockPos pos3 : pos2) {
            this.generatorList.add((IGenerator) this.getWorld().getBlockEntity(pos3));
        }
        pos2 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ISeparate.class
                );
        for (BlockPos pos3 : pos2) {
            this.separateList.add((ISeparate) this.getWorld().getBlockEntity(pos3));
        }
        pos2 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IExchanger.class
                );
        for (BlockPos pos3 : pos2) {
            this.exchangerList.add((IExchanger) this.getWorld().getBlockEntity(pos3));
        }
    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        work = nbttagcompound.getBoolean("work");
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.work);
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        work = customPacketBuffer.readBoolean();
    }

    @Override
    public void usingBeforeGUI() {

    }

}
