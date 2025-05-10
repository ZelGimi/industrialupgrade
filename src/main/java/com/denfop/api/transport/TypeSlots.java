package com.denfop.api.transport;


import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TypeSlots {

    private final EnumTypeList enumTypeList;
    private final List<ItemStack> list;

    public TypeSlots(List<ItemStack> list, EnumTypeList enumTypeList) {
        this.list = list;
        this.enumTypeList = enumTypeList;
    }

    public List<ItemStack> getList() {
        return list;
    }

    public EnumTypeList getEnumTypeList() {
        return enumTypeList;
    }

}
