package com.denfop.componets;

import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;

public class ComponentUpgradeSlots extends AbstractComponent {

    private final InvSlotUpgrade invSlot;

    ComponentProcess componentProcess;
    private AdvEnergy advEnergy;

    public ComponentUpgradeSlots(final TileEntityInventory parent, InvSlotUpgrade invSlotUpgrade) {
        super(parent);
        this.invSlot = invSlotUpgrade;
    }

    public boolean condition() {
        return true;
    }

    public void setOverclockRates(InvSlotUpgrade invSlotUpgrade) {
        if (this.parent instanceof IComponentUpdate) {
            ((IComponentUpdate) parent).setOverclockRates();
        }
        if (this.componentProcess != null) {
            this.componentProcess.setOverclockRates(this.invSlot);
        }
        if (this.advEnergy != null) {
            this.advEnergy.setOverclockRates(this.invSlot);
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.componentProcess = this.getParent().getComp(ComponentProcess.class);
        this.advEnergy = this.getParent().getComp(AdvEnergy.class);
    }

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (condition()) {
            if (invSlot.tickNoMark()) {
                if (this.parent instanceof IComponentUpdate) {
                    ((IComponentUpdate) parent).setOverclockRates();
                }
                if (this.componentProcess != null) {
                    this.componentProcess.setOverclockRates(this.invSlot);
                }
                if (this.advEnergy != null) {
                    this.advEnergy.setOverclockRates(this.invSlot);
                }
            }
        }

    }

}
