package com.denfop.componets;

import com.denfop.IUCore;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.tiles.base.TileEntityInventory;

public class Action {

    private final int tick;
    private final TypeAction typeAction;
    private final TypeLoad typeLoad;
    private final Object[] param;
    private final TileEntityInventory inventory;

    public Action(TileEntityInventory inventory, int tick, TypeAction typeAction, TypeLoad typeLoad, Object... param) {
        this.tick = tick;
        this.typeAction = typeAction;
        this.typeLoad = typeLoad;
        this.param = param;
        this.inventory = inventory;
    }

    public void doAction() {
        if (this.inventory.getWorld().provider.getWorldTime() % tick == 0) {
            if (typeAction == TypeAction.AUDIO && this.param.length > 0) {
                Object o = param[0];

                if (!((Boolean) o)) {
                    IUCore.network.get(true).initiateTileEntityEvent(this.inventory, (Integer) param[1], true);
                } else {
                    ((IAudioFixer) this.inventory).initiate((Integer) param[1]);
                }
            }
        }
    }

    public boolean needAction(TypeLoad typeLoad) {
        return typeLoad == this.typeLoad;
    }

}

