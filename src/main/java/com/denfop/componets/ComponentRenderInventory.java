package com.denfop.componets;

import ic2.core.block.invslot.InvSlot;

import java.util.ArrayList;
import java.util.List;

public class ComponentRenderInventory {

    private final EnumTypeComponentSlot typeComponentSlot;
    private final List<InvSlot> slotList;
    private final List<InvSlot> blackSlotList;

    public ComponentRenderInventory(
            EnumTypeComponentSlot typeComponentSlot,
            List<InvSlot> slotList,
            List<InvSlot> blackSlotList
    ) {
        this.typeComponentSlot = typeComponentSlot;
        this.slotList = slotList;
        this.blackSlotList = blackSlotList;
    }

    public ComponentRenderInventory(EnumTypeComponentSlot typeComponentSlot, List<InvSlot> slotList) {
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

    public List<InvSlot> getSlotList() {
        return slotList;
    }

    public List<InvSlot> getBlackSlotList() {
        return blackSlotList;
    }


}
