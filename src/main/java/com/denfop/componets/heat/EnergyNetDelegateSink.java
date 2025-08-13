package com.denfop.componets.heat;

import com.denfop.api.heat.IHeatEmitter;
import com.denfop.api.heat.IHeatSink;
import com.denfop.api.heat.IHeatSource;
import com.denfop.api.heat.IHeatTile;
import com.denfop.api.sytem.InfoTile;
import com.denfop.componets.HeatComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EnergyNetDelegateSink extends EnergyNetDelegate implements IHeatSink {

    List<IHeatSource> list = new LinkedList<>();


    public EnergyNetDelegateSink(HeatComponent block) {
        super(block);
    }




    @Override
    public List<IHeatSource> getEnergyTickList() {
        return list;
    }


    public double getDemandedHeat() {

        return this.buffer.capacity;
    }





    public void receivedHeat(double amount) {
        this.setHeatStored(amount);

    }

    @Override
    public boolean needTemperature() {
        return this.buffer.need;
    }

    public void setHeatStored(double amount) {
        if (this.buffer.storage < amount) {
            this.buffer.storage = amount;
        }
    }


}