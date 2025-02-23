package com.denfop.network.packet;

import net.minecraft.item.ItemStack;

public interface IUpdatableItemStackEvent {

    void updateField(String name, CustomPacketBuffer buffer, ItemStack stack);

    void updateEvent(int event, ItemStack stack);


}
