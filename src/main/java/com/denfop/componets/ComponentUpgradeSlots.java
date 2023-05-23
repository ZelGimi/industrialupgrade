package com.denfop.componets;

import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;

public class ComponentUpgradeSlots extends AbstractComponent{

    private final InvSlotUpgrade invSlot;
    private final boolean needUpdate;

    public ComponentUpgradeSlots(final TileEntityInventory parent, InvSlotUpgrade invSlotUpgrade) {
        super(parent);
        this.invSlot = invSlotUpgrade;
        this.needUpdate = parent instanceof IComponentUpdate;
    }
    public boolean condition(){
        return true;
    }

    @Override
    public boolean isServer() {
        return needUpdate;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if(condition()){
            if(invSlot.tickNoMark())
                ((IComponentUpdate)parent).setOverclockRates();
        }

    }

}
