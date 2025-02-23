package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerRefrigerator;
import com.denfop.gui.GuiRefrigerator;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotTank;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.items.reactors.ItemReactorCoolant;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityRefrigeratorCoolant extends TileElectricMachine implements IUpgradableBlock, IUpdatableTileEvent {

    public final InvSlotUpgrade upgradeSlot;
    public final Fluids fluids;
    public final Fluids.InternalFluidTank tank;
    public final InvSlot slot;
    public final InvSlotTank fluidSlot;

    public TileEntityRefrigeratorCoolant() {
        super(400, 14, 1);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = fluids.addTankInsert("input", 10000, Fluids.fluidPredicate(
                FluidName.fluidHelium.getInstance(),
                FluidName.fluidhyd.getInstance(),
                FluidName.fluidazot.getInstance()
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
        return IUItem.basemachine2;
    }

    @Override
    public ContainerRefrigerator getGuiContainer(final EntityPlayer var1) {
        return new ContainerRefrigerator(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiRefrigerator(getGuiContainer(var1));
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (!fluidSlot.isEmpty()) {
            if (tank.getFluidAmount() + 1000 <= tank.getCapacity() && !fluidSlot
                    .get()
                    .isEmpty()) {
                if (fluidSlot.processIntoTank(tank, this.outputSlot)) {
                    this.upgradeSlot.tickNoMark();
                }
            }
        }


        if (this.energy.getEnergy() >= 50 && !this.slot.isEmpty() && this.tank.getFluidAmount() > 1) {
            ItemReactorCoolant coolant = (ItemReactorCoolant) this.slot.get().getItem();
            int need = coolant.needFill(this.slot.get());
            if (coolant == IUItem.coolant && this.tank.getFluid().getFluid() == FluidName.fluidhyd.getInstance() && need > 0) {
                coolant.fill(this.slot.get());
                this.tank.drain(1, true);
                this.setActive(true);
                this.energy.useEnergy(50);
            } else if (coolant == IUItem.adv_coolant && this.tank
                    .getFluid()
                    .getFluid() == FluidName.fluidazot.getInstance() && need > 0) {
                coolant.fill(this.slot.get());
                this.tank.drain(1, true);
                this.setActive(true);
                this.energy.useEnergy(50);
            } else if (coolant == IUItem.imp_coolant && this.tank
                    .getFluid()
                    .getFluid() == FluidName.fluidHelium.getInstance() && need > 0) {
                coolant.fill(this.slot.get());
                this.tank.drain(1, true);
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
        if (IUCore.proxy.isSimulating()) {


        }


    }

}
