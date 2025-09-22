package com.denfop.componets;

import com.denfop.invslot.Inventory;

import java.util.ArrayList;
import java.util.List;

public class ComponentRenderInventory {

    private final EnumTypeComponentSlot typeComponentSlot;
    private final List<Inventory> slotList;
    private final List<Inventory> blackSlotList;

    public ComponentRenderInventory(
            EnumTypeComponentSlot typeComponentSlot,
            List<Inventory> slotList,
            List<Inventory> blackSlotList
    ) {
        this.typeComponentSlot = typeComponentSlot;
        this.slotList = slotList;
        this.blackSlotList = blackSlotList;
    }

    public ComponentRenderInventory(EnumTypeComponentSlot typeComponentSlot, List<Inventory> slotList) {
        this.typeComponentSlot = typeComponentSlot;
        this.slotList = slotList;
        this.blackSlotList = new ArrayList<>();
    }

    public ComponentRenderInventory(EnumTypeComponentSlot typeComponentSlot) {
        this.typeComponentSlot = typeComponentSlot;
        this.slotList = new ArrayList<>();
        this.blackSlotList = new ArrayList<>();
    }

    public EnumTypeComponentSlot getTypeComponentSlot() {
        return typeComponentSlot;
    }

    public List<Inventory> getSlotList() {
        return slotList;
    }

    public List<Inventory> getBlackSlotList() {
        return blackSlotList;
    }


}
