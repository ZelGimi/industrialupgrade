package com.denfop.blockentity.geothermalpump;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockBase;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockGeothermalPumpEntity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuGeothermalController;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.screen.ScreenGeothermalController;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityGeothermalController extends BlockEntityMultiBlockBase implements IController {

    public boolean work = false;
    List<IGenerator> generatorList = new ArrayList<>();
    List<IWaste> wasteList = new ArrayList<>();
    IExchanger exchanger;

    public BlockEntityGeothermalController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.GeoThermalPumpMultiBlock, BlockGeothermalPumpEntity.geothermal_controller, pos, state);
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        this.work = !work;
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
    public ContainerMenuGeothermalController getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuGeothermalController(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenGeothermalController((ContainerMenuGeothermalController) menu);
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
                        generator.getFluidTank().fill(new FluidStack(FluidName.fluidpetroleum.getInstance().get(), 1), IFluidHandler.FluidAction.EXECUTE);
                        baseEnergy.useEnergy(10);
                        fluidTank.drain(3, IFluidHandler.FluidAction.EXECUTE);
                        if (IUCore.random.nextInt(100) >= 98) {
                            ItemStack stack = new ItemStack(IUItem.crafting_elements.getStack(457), 1);
                            for (IWaste waste : this.wasteList) {
                                if (waste.getSlot().add(stack)) {
                                    break;
                                }
                            }
                        }

                        if (IUCore.random.nextInt(100) == 99 && IUCore.random.nextInt(2) == 0) {
                            ItemStack stack = new ItemStack(IUItem.crafting_elements.getStack(461), 1);
                            for (IWaste waste : this.wasteList) {
                                if (waste.getSlot().add(stack)) {
                                    break;
                                }
                            }
                        }
                        if (IUCore.random.nextInt(100) == 99 && IUCore.random.nextInt(2) == 0) {
                            ItemStack stack = new ItemStack(IUItem.crafting_elements.getStack(462), 1);
                            for (IWaste waste : this.wasteList) {
                                if (waste.getSlot().add(stack)) {
                                    break;
                                }
                            }
                        }

                        if (IUCore.random.nextInt(100) == 99 && IUCore.random.nextInt(2) == 0) {
                            ItemStack stack = new ItemStack(IUItem.crafting_elements.getStack(463), 1);
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
    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        nbttagcompound.putBoolean("work", work);
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
        this.exchanger = (IExchanger) this.getWorld().getBlockEntity(pos1.get(0));
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
                        IWaste.class
                );
        for (BlockPos pos3 : pos2) {
            this.wasteList.add((IWaste) this.getWorld().getBlockEntity(pos3));
        }
    }

    @Override
    public void usingBeforeGUI() {

    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGeothermalPumpEntity.geothermal_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.geothermalpump.getBlock(getTeBlock());
    }

}
