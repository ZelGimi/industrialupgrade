package com.denfop.componets;

import com.denfop.blockentity.base.BlockEntityBase;

public class LightComponent extends AbstractComponent {
    private boolean active;

    public LightComponent(BlockEntityBase parent) {
        super(parent);
    }

    public boolean canExtractLightLevel() {
        return parent.getActive();
    }

    @Override
    public int getLightValue() {
        return 15;
    }


    public int getLightOpacity() {
        return 15;
    }

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.active != parent.getActive()) {
            this.active = parent.getActive();
        }
    }
}
