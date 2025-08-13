package com.denfop.componets.cold;

import com.denfop.api.cool.ICoolSource;
import com.denfop.componets.CoolComponent;
import net.minecraft.world.level.block.entity.BlockEntity;

public  class EnergyNetDelegateSource extends EnergyNetDelegate implements ICoolSource {



    public EnergyNetDelegateSource(CoolComponent coolComponent) {
        super(coolComponent);
    }


    public double getOfferedCool() {
        assert !this.sourceDirections.isEmpty();

        return !this.sendingSidabled ? this.getSourceEnergy() : 0.0D;
    }

    @Override
    public boolean isAllowed() {
        return this.buffer.allow;
    }

    @Override
    public void setAllowed(final boolean allowed) {
        this.buffer.allow = allowed;
    }



}
