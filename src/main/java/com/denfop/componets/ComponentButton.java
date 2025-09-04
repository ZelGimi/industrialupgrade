package com.denfop.componets;

import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.network.packet.PacketUpdateServerTile;

public class ComponentButton {

    private final BlockEntityBase entityBlock;
    private final int event;
    private final String text;

    public ComponentButton(BlockEntityBase entityBlock, int event) {
        this.entityBlock = entityBlock;
        this.event = event;
        this.text = "";
    }

    public ComponentButton(BlockEntityBase entityBlock, int event, String text) {
        this.entityBlock = entityBlock;
        this.event = event;
        this.text = text;
    }

    public boolean active() {
        return false;
    }

    public int getEvent() {
        return event;
    }

    public BlockEntityBase getEntityBlock() {
        return entityBlock;
    }

    public void ClickEvent() {
        new PacketUpdateServerTile(entityBlock, event);
    }

    public String getText() {
        return text;
    }

}
