package com.denfop.componets;

import com.denfop.IUCore;
import ic2.core.block.TileEntityBlock;

public class ComponentButton {

    private final TileEntityBlock entityBlock;
    private final int event;
    private final String text;

    public ComponentButton(TileEntityBlock entityBlock, int event) {
        this.entityBlock = entityBlock;
        this.event = event;
        this.text = "";
    }

    public ComponentButton(TileEntityBlock entityBlock, int event, String text) {
        this.entityBlock = entityBlock;
        this.event = event;
        this.text = text;
    }

    public int getEvent() {
        return event;
    }

    public TileEntityBlock getEntityBlock() {
        return entityBlock;
    }

    public void ClickEvent() {
        IUCore.network.get(false).initiateClientTileEntityEvent(entityBlock, event);
    }

    public String getText() {
        return text;
    }

}
