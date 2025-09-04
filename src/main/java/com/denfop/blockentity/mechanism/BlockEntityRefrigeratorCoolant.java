package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuRefrigerator;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryFluid;
import com.denfop.inventory.InventoryTank;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.items.reactors.ItemReactorCoolant;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenRefrigerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.EnumSet;
import java.util.Set;

public class BlockEntityRefrigeratorCoolant extends BlockEntityElectricMachine implements IUpgradableBlock, IUpdatableTileEvent {

    public final InventoryUpgrade upgradeSlot;
    public final Fluids fluids;
    public final Fluids.InternalFluidTank tank;
    public final Inventory slot;
    public final InventoryTank fluidSlot;

    public BlockEntityRefrigeratorCoolant(BlockPos pos, BlockState state) {
        super(400, 14, 1, BlockBaseMachine3Entity.refrigerator_coolant, pos, state);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = fluids.addTankInsert("input", 10000, Fluids.fluidPredicate(
                FluidName.fluidhelium.getInstance().get(),
                FluidName.fluidhydrogen.getInstance().get(),
                FluidName.fluidnitrogen.getInstance().get()
        ));
        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof ItemReactorCoolant;
            }
        };
        this.slot.setStackSizeLimit(1);
        this.fluidSlot = new InventoryTank(this, Inventory.TypeItemSlot.INPUT, 1,
                InventoryFluid.TypeFluidSlot.INPUT, this.tank
        );
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.refrigerator_coolant;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public ContainerMenuRefrigerator getGuiContainer(final Player var1) {
        return new ContainerMenuRefrigerator(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenRefrigerator((ContainerMenuRefrigerator) menu);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (!fluidSlot.isEmpty()) {
            if (tank.getFluidAmount() + 1000 <= tank.getCapacity() && !fluidSlot
                    .get(0)
                    .isEmpty()) {
                if (fluidSlot.processIntoTank(tank, this.outputSlot)) {
                    this.upgradeSlot.tickNoMark();
                }
            }
        }


        if (this.energy.getEnergy() >= 50 && !this.slot.isEmpty() && this.tank.getFluidAmount() > 1) {
            ItemReactorCoolant coolant = (ItemReactorCoolant) this.slot.get(0).getItem();
            int need = coolant.needFill(this.slot.get(0));
            if (coolant == IUItem.coolant.getItem() && this.tank.getFluid().getFluid() == FluidName.fluidhydrogen.getInstance().get() && need > 0) {
                coolant.fill(this.slot.get(0));
                this.tank.drain(1, IFluidHandler.FluidAction.EXECUTE);
                this.setActive(true);
                this.energy.useEnergy(50);
            } else if (coolant == IUItem.adv_coolant.getItem() && this.tank
                    .getFluid()
                    .getFluid() == FluidName.fluidnitrogen.getInstance().get() && need > 0) {
                coolant.fill(this.slot.get(0));
                this.tank.drain(1, IFluidHandler.FluidAction.EXECUTE);
                this.setActive(true);
                this.energy.useEnergy(50);
            } else if (coolant == IUItem.imp_coolant.getItem() && this.tank
                    .getFluid()
                    .getFluid() == FluidName.fluidhelium.getInstance().get() && need > 0) {
                coolant.fill(this.slot.get(0));
                this.tank.drain(1, IFluidHandler.FluidAction.EXECUTE);
                this.setActive(true);
                this.energy.useEnergy(50);
            } else {
                setActive(false);
            }
        } else {
            this.setActive(false);
        }
        this.upgradeSlot.tickNoMark();
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.FluidInput
        );
    }

    public void onLoaded() {
        super.onLoaded();


    }

}
