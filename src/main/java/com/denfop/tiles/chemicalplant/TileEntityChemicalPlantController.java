package com.denfop.tiles.chemicalplant;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockChemicalPlant;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerChemicalController;
import com.denfop.gui.GuiChemicalController;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileEntityChemicalPlantController extends TileMultiBlockBase implements IController {

    public boolean work = false;
    List<IGenerator> generatorList = new ArrayList<>();
    IWaste waste;
    List<ISeparate> separateList = new ArrayList<>();
    List<IExchanger> exchangerList = new ArrayList<>();

    public TileEntityChemicalPlantController() {
        super(InitMultiBlockSystem.ChemicalPlantMultiBlock);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockChemicalPlant.chemical_plant_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.chemicalPlant;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        this.work = !work;
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        nbttagcompound.setBoolean("work", work);
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
                    fluidTankWaste.fill(new FluidStack(FluidName.fluidcryogen.getInstance(), canAdd1), true);
                    fluidTank1.drain(canAdd1 * 2, true);
                    fluidTank.drain(canAdd1 * 10, true);
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
    public ContainerChemicalController getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerChemicalController(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiChemicalController(getGuiContainer(var1));
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
        this.waste = (IWaste) this.getWorld().getTileEntity(pos1.get(0));
        List<BlockPos> pos2 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IGenerator.class
                );
        for (BlockPos pos3 : pos2) {
            this.generatorList.add((IGenerator) this.getWorld().getTileEntity(pos3));
        }
        pos2 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ISeparate.class
                );
        for (BlockPos pos3 : pos2) {
            this.separateList.add((ISeparate) this.getWorld().getTileEntity(pos3));
        }
        pos2 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IExchanger.class
                );
        for (BlockPos pos3 : pos2) {
            this.exchangerList.add((IExchanger) this.getWorld().getTileEntity(pos3));
        }
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
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
