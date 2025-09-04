package com.denfop.componets;

import com.denfop.api.sound.AudioFixer;
import com.denfop.blockentity.base.BlockEntityInventory;

public class Action {

    private final int tick;
    private final TypeAction typeAction;
    private final TypeLoad typeLoad;
    private final Object[] param;
    private final BlockEntityInventory inventory;

    public Action(BlockEntityInventory inventory, int tick, TypeAction typeAction, TypeLoad typeLoad, Object... param) {
        this.tick = tick;
        this.typeAction = typeAction;
        this.typeLoad = typeLoad;
        this.param = param;
        this.inventory = inventory;
    }

    public void doAction() {
        if (this.inventory.getLevel().getGameTime() % tick == 0) {
            if (typeAction == TypeAction.AUDIO && this.param.length > 0) {
                ((AudioFixer) this.inventory).initiate((Integer) param[0]);

            }
        }
    }

    public boolean needAction(TypeLoad typeLoad) {
        return typeLoad == this.typeLoad;
    }

}

