package com.denfop.tiles.geothermalpump;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockGeothermalPump;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerGeothermalController;
import com.denfop.gui.GuiGeothermalController;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileEntityGeothermalController extends TileMultiBlockBase implements IController {

    public boolean work = false;
    List<IGenerator> generatorList = new ArrayList<>();
    List<IWaste> wasteList = new ArrayList<>();
    IExchanger exchanger;

    public TileEntityGeothermalController() {
        super(InitMultiBlockSystem.GeoThermalPumpMultiBlock);
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        this.work = !work;
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
    public ContainerGeothermalController getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerGeothermalController(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiGeothermalController(getGuiContainer(var1));
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        work = customPacketBuffer.readBoolean();
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.work && this.isFull()) {
            Fluids.InternalFluidTank fluidTank = this.exchanger.getFluidTank();
            for (IGenerator generator : this.generatorList) {
                if (fluidTank.getFluidAmount() < 3) {
                    break;
                }
                ComponentBaseEnergy baseEnergy = generator.getEnergy();
                final int canAdd = generator.getFluidTank().getCapacity() - generator.getFluidTank().getFluidAmount();
                if (baseEnergy.getEnergy() >= 10) {
                    if (canAdd >= 1) {
                        generator.getFluidTank().fill(new FluidStack(FluidName.fluidneft.getInstance(), 1), true);
                        baseEnergy.useEnergy(10);
                        fluidTank.drain(3, true);
                        if (IUCore.random.nextInt(100) >= 98) {
                            ItemStack stack = new ItemStack(IUItem.crafting_elements, 1, 457);
                            for (IWaste waste : this.wasteList) {
                                if (waste.getSlot().add(stack)) {
                                    break;
                                }
                            }
                        }

                        if (IUCore.random.nextInt(100) == 99 && IUCore.random.nextInt(2) == 0) {
                            ItemStack stack = new ItemStack(IUItem.crafting_elements, 1, 461);
                            for (IWaste waste : this.wasteList) {
                                if (waste.getSlot().add(stack)) {
                                    break;
                                }
                            }
                        }
                        if (IUCore.random.nextInt(100) == 99 && IUCore.random.nextInt(2) == 0) {
                            ItemStack stack = new ItemStack(IUItem.crafting_elements, 1, 462);
                            for (IWaste waste : this.wasteList) {
                                if (waste.getSlot().add(stack)) {
                                    break;
                                }
                            }
                        }

                        if (IUCore.random.nextInt(100) == 99 && IUCore.random.nextInt(2) == 0) {
                            ItemStack stack = new ItemStack(IUItem.crafting_elements, 1, 463);
                            for (IWaste waste : this.wasteList) {
                                if (waste.getSlot().add(stack)) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        nbttagcompound.setBoolean("work", work);
        return super.writeToNBT(nbttagcompound);
    }

    @Override
    public void setFull(final boolean full) {
        super.setFull(full);
        if (!full) {
            this.generatorList.clear();
            this.wasteList.clear();
            this.exchanger = null;
        }


    }

    @Override
    public void updateAfterAssembly() {

        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IExchanger.class
                );
        this.exchanger = (IExchanger) this.getWorld().getTileEntity(pos1.get(0));
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
                        IWaste.class
                );
        for (BlockPos pos3 : pos2) {
            this.wasteList.add((IWaste) this.getWorld().getTileEntity(pos3));
        }
    }

    @Override
    public void usingBeforeGUI() {

    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGeothermalPump.geothermal_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.geothermalpump;
    }

}
