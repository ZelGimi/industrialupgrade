package com.denfop.componets.pressure;

import com.denfop.api.pressure.IPressureEmitter;
import com.denfop.api.pressure.IPressureSink;
import com.denfop.api.pressure.IPressureSource;
import com.denfop.componets.PressureComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class EnergyNetDelegateSink extends EnergyNetDelegate implements IPressureSink {

    public EnergyNetDelegateSink(PressureComponent block) {
        super(block);
    }

    public int getSinkTier() {
        return this.buffer.sinkTier;
    }

    public boolean acceptsPressureFrom(IPressureEmitter emitter, Direction dir) {
        return this.sinkDirections.contains(dir);
    }

    public double getDemandedPressure() {

        return this.buffer.capacity;
    }

    public void receivedPressure(double amount) {
        this.setPressureStored(amount);

    }
    List<IPressureSource> systemTicks = new LinkedList<>();

    @Override
    public boolean needTemperature() {
        return this.buffer.need;
    }
    @Override
    public List<IPressureSource> getEnergyTickList() {
        return systemTicks;
    }
    public void setPressureStored(double amount) {
        if (this.buffer.storage < amount) {
            this.buffer.storage = amount;
        }
    }



}
