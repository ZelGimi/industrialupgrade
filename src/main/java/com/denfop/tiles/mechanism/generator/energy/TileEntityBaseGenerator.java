package com.denfop.tiles.mechanism.generator.energy;

import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlotCharge;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public abstract class TileEntityBaseGenerator extends TileEntityInventory {

    public final InvSlotCharge chargeSlot;
    public final Energy energy;

    public int fuel = 0;
    protected double production;
    private int ticksSinceLastActiveUpdate;
    private int activityMeter = 0;

    public TileEntityBaseGenerator(double production, int tier, int maxStorage, IMultiTileBlock multiTileBlock, BlockPos pos, BlockState state) {
        super(multiTileBlock, pos, state);
        this.production = production;
        this.ticksSinceLastActiveUpdate = IUCore.random.nextInt(256);
        this.chargeSlot = new InvSlotCharge(this, 1);
        this.energy =
                this.addComponent(Energy.asBasicSource(this, maxStorage, tier).addManagedSlot(this.chargeSlot));
    }


    public void addInformation(ItemStack stack, List<String> tooltip) {

        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }
        super.addInformation(stack, tooltip);
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.fuel = nbttagcompound.getInt("fuel");
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putInt("fuel", this.fuel);
        return nbt;
    }


    public double charge(double amount, ItemStack stack, boolean simulate, boolean ignore) {
        if (amount < 0.0) {
            throw new IllegalArgumentException("Amount must be > 0.");
        }
        if (amount == 0.0) {
            return 0;
        }

        return ElectricItem.manager.charge(stack, amount, 2147483647, ignore, simulate);
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.needsFuel()) {
            this.gainFuel();
        }
        if (!this.chargeSlot.isEmpty()) {
            if (this.charge(
                    this.energy.getEnergy() > 1D ? this.energy.getEnergy() : 0,
                    chargeSlot.get(0),
                    true, false
            ) != 0) {
                this.energy.useEnergy(this.charge(
                        this.energy.getEnergy() > 1D ? this.energy.getEnergy() : 0,
                        chargeSlot.get(0),
                        false, false
                ));
            }
        }
        boolean newActive = this.gainEnergy();

        if (!this.delayActiveUpdate()) {
            this.setActive(newActive);
        } else {
            if (this.ticksSinceLastActiveUpdate % 256 == 0) {
                this.setActive(this.activityMeter > 0);
                this.activityMeter = 0;
            }

            if (newActive) {
                ++this.activityMeter;
            } else {
                --this.activityMeter;
            }

            ++this.ticksSinceLastActiveUpdate;
        }

    }

    public boolean gainEnergy() {
        if (this.isConverting()) {
            this.energy.addEnergy(this.production);
            --this.fuel;
            return true;
        } else {
            return false;
        }
    }

    public boolean isConverting() {
        return !this.needsFuel() && this.energy.getFreeEnergy() >= this.production;
    }

    public boolean needsFuel() {
        return this.fuel <= 0 && this.energy.getFreeEnergy() >= this.production;
    }

    public abstract boolean gainFuel();

    protected boolean delayActiveUpdate() {
        return false;
    }


    public ContainerBase<? extends TileEntityBaseGenerator> getGuiContainer(Player player) {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player player, ContainerBase<? extends IAdvInventory> menu) {
        return null;
    }


}
