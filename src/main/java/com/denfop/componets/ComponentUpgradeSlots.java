package com.denfop.componets;

import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;

public class ComponentUpgradeSlots extends AbstractComponent {

    protected final InvSlotUpgrade invSlot;
    public boolean update = false;

    protected ComponentProcess componentProcess;
    private AdvEnergy advEnergy;

    public ComponentUpgradeSlots(final TileEntityInventory parent, InvSlotUpgrade invSlotUpgrade) {
        super(parent);
        this.invSlot = invSlotUpgrade;
    }

    public boolean condition() {
        return true;
    }

    public void setOverclockRates(InvSlotUpgrade invSlotUpgrade) {
        invSlotUpgrade.isUpdate = true;
        if (this.parent instanceof IComponentUpdate) {
            ((IComponentUpdate) parent).setOverclockRates();
        }
        if (this.componentProcess != null) {
            this.componentProcess.setOverclockRates(this.invSlot);
        }
        if (this.advEnergy != null) {
            this.advEnergy.setOverclockRates(this.invSlot);
        }
        invSlotUpgrade.isUpdate = false;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.componentProcess = this.getParent().getComp(ComponentProcess.class);
        this.advEnergy = this.getParent().getComp(AdvEnergy.class);
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
