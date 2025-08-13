package com.denfop.componets.cold;

import com.denfop.api.cool.ICoolEmitter;
import com.denfop.api.cool.ICoolSink;
import com.denfop.api.cool.ICoolSource;
import com.denfop.api.cool.ICoolTile;
import com.denfop.api.sytem.InfoTile;
import com.denfop.componets.CoolComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EnergyNetDelegateSink extends EnergyNetDelegate implements ICoolSink {



    List<ICoolSource> list = new LinkedList<>();


    public EnergyNetDelegateSink(CoolComponent coolComponent) {
        super(coolComponent);
    }





    public double getDemandedCool() {
        if (this.buffer.storage != 0) {
            return 64;
        } else {
            return 0;
        }
    }

    public void receivedCold(double amount) {
        if (amount > 0) {
            this.buffer.storage-=0.05 * amount / 4;

        }

    }

    @Override
    public boolean needCooling() {
        return this.buffer.storage > 0;
    }

    @Override
    public List<ICoolSource> getEnergyTickList() {
        return list;
    }



}