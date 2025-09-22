package com.denfop.componets;

import com.denfop.invslot.InventoryUpgrade;
import com.denfop.tiles.base.TileEntityInventory;

public class ComponentUpgradeSlots extends AbstractComponent {

    protected final InventoryUpgrade invSlot;
    public boolean update = false;

    protected ComponentProcess componentProcess;
    protected Energy energy;

    public ComponentUpgradeSlots(final TileEntityInventory parent, InventoryUpgrade invSlotUpgrade) {
        super(parent);
        this.invSlot = invSlotUpgrade;
    }

    public boolean condition() {
        return true;
    }

    public void setOverclockRates(InventoryUpgrade invSlotUpgrade) {
        invSlotUpgrade.isUpdate = true;
        if (this.parent instanceof IComponentUpdate) {
            ((IComponentUpdate) parent).setOverclockRates();
        }
        if (this.componentProcess != null) {
            this.componentProcess.setOverclockRates(this.invSlot);
        }
        if (this.energy != null) {
            this.energy.setOverclockRates(this.invSlot);
        }
        invSlotUpgrade.isUpdate = false;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.componentProcess = this.getParent().getComp(ComponentProcess.class);
        this.energy = this.getParent().getComp(Energy.class);
        this.setOverclockRates(this.invSlot);
    }

    @Override
    public boolean isServer() {
        return true;
    }

    public void markDirty() {
        update = true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (update) {
            this.setOverclockRates(this.invSlot);
            update = false;
        }
        if (condition()) {
            invSlot.tickNoMark();
        }

    }

}
