package com.denfop.network.packet;

public interface IUpdatableItemStack {

    void readContainer(CustomPacketBuffer buffer);

    CustomPacketBuffer writeContainer();

}
