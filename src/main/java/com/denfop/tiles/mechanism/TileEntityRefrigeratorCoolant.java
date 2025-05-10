package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerRefrigerator;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiRefrigerator;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotTank;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.items.reactors.ItemReactorCoolant;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityRefrigeratorCoolant extends TileElectricMachine implements IUpgradableBlock, IUpdatableTileEvent {

    public final InvSlotUpgrade upgradeSlot;
    public final Fluids fluids;
    public final Fluids.InternalFluidTank tank;
    public final InvSlot slot;
    public final InvSlotTank fluidSlot;

    public TileEntityRefrigeratorCoolant(BlockPos pos, BlockState state) {
        super(400, 14, 1,BlockBaseMachine3.refrigerator_coolant,pos,state);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = fluids.addTankInsert("input", 10000, Fluids.fluidPredicate(
                FluidName.fluidHelium.getInstance().get(),
                FluidName.fluidhyd.getInstance().get(),
                FluidName.fluidazot.getInstance().get()
        ));
        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemReactorCoolant;
            }
        };
        this.slot.setStackSizeLimit(1);
        this.fluidSlot = new InvSlotTank(this, InvSlot.TypeItemSlot.INPUT, 1,
                InvSlotFluid.TypeFluidSlot.INPUT, this.tank
        );
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.refrigerator_coolant;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public ContainerRefrigerator getGuiContainer(final Player var1) {
        return new ContainerRefrigerator(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiRefrigerator((ContainerRefrigerator) menu);
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
            if (coolant == IUItem.coolant.getItem() && this.tank.getFluid().getFluid() == FluidName.fluidhyd.getInstance().get() && need > 0) {
                coolant.fill(this.slot.get(0));
                this.tank.drain(1, IFluidHandler.FluidAction.EXECUTE);
                this.setActive(true);
                this.energy.useEnergy(50);
            } else if (coolant == IUItem.adv_coolant.getItem() && this.tank
                    .getFluid()
                    .getFluid() == FluidName.fluidazot.getInstance().get() && need > 0) {
                coolant.fill(this.slot.get(0));
                this.tank.drain(1, IFluidHandler.FluidAction.EXECUTE);
                this.setActive(true);
                this.energy.useEnergy(50);
            } else if (coolant == IUItem.imp_coolant.getItem() && this.tank
                    .getFluid()
                    .getFluid() == FluidName.fluidHelium.getInstance().get() && need > 0) {
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
