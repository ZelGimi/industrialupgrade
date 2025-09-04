package com.denfop.blockentity.mechanism.steamboiler;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockBase;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockSteamBoilerEntity;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.inventory.Inventory;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

public class BlockEntitySteamControllerBoiler extends BlockEntityMultiBlockBase implements IController {

    ITank waterTank;
    ITank steamTank;

    IHeater heater;

    IExchanger exchanger;

    public BlockEntitySteamControllerBoiler(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.SteamBoilerMultiBlock, BlockSteamBoilerEntity.steam_boiler_controller, pos, state);
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.steam_boiler.info"));
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.isFull()) {

            if (this.exchanger.isWork() && this.heater.isWork() && this.waterTank.getTank().getFluidAmount() >= 4 && this.steamTank
                    .getTank()
                    .getFluidAmount() + 8 <= this.steamTank.getTank().getCapacity()) {
                this.steamTank.getSteam().addEnergy(8);
                this.waterTank.getTank().drain(4, IFluidHandler.FluidAction.EXECUTE);
                this.setActive(true);
            } else {
                this.setActive(false);
            }
        }
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSteamBoilerEntity.steam_boiler_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_boiler.getBlock(getTeBlock());
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {

    }

    @Override
    public void setFull(final boolean full) {
        super.setFull(full);
        if (!full) {
            if (steamTank != null) {
                this.steamTank.setMainMultiElement(null);
                this.steamTank.getTank().setAcceptedFluids(Fluids.fluidPredicate());
                this.steamTank.getTank().setTypeItemSlot(Inventory.TypeItemSlot.NONE);
                this.steamTank = null;
            }
            if (waterTank != null) {
                this.waterTank.getTank().setAcceptedFluids(Fluids.fluidPredicate());
                this.waterTank.getTank().setTypeItemSlot(Inventory.TypeItemSlot.NONE);
                this.waterTank = null;
            }
            this.exchanger = null;
            this.heater = null;
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


        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IHeater.class
                );
        this.heater = (IHeater) this.getWorld().getBlockEntity(pos1.get(0));

        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ITank.class
                );
        this.waterTank = (ITank) this.getWorld().getBlockEntity(pos1.get(0));
        this.waterTank.getTank().setTypeItemSlot(Inventory.TypeItemSlot.INPUT);
        this.waterTank.getTank().setAcceptedFluids(Fluids.fluidPredicate(net.minecraft.world.level.material.Fluids.WATER));
        if (this.waterTank.getTank().getFluidAmount() > 0 && this.waterTank
                .getTank()
                .getFluid()
                .getFluid() != net.minecraft.world.level.material.Fluids.WATER) {
            this.waterTank.getTank().drain(this.waterTank.getTank().getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
        }
        this.steamTank = (ITank) this.getWorld().getBlockEntity(pos1.get(1));
        if (this.steamTank.getTank().getFluidAmount() > 0 && this.steamTank
                .getTank()
                .getFluid()
                .getFluid() != FluidName.fluidsteam.getInstance().get()) {
            this.steamTank.getTank().drain(this.steamTank.getTank().getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
        }
        this.steamTank.getTank().setTypeItemSlot(Inventory.TypeItemSlot.OUTPUT);
        this.steamTank.getTank().setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidsteam.getInstance().get()));
        this.waterTank.setUnloaded();
        this.steamTank.setSteam();
    }

    @Override
    public void usingBeforeGUI() {

    }

}
