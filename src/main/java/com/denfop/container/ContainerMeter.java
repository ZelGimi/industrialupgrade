package com.denfop.container;

import com.denfop.IUCore;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IAdvEnergyTile;
import com.denfop.api.energy.NodeStats;
import com.denfop.invslot.HandHeldMeter;
import ic2.api.network.ClientModifiable;
import ic2.core.IC2;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerMeter extends ContainerFullInv<HandHeldMeter> {

    private IAdvEnergyTile uut;
    private double resultAvg;
    private double resultMin;
    private double resultMax;
    private int resultCount = 0;
    @ClientModifiable
    private Mode mode;

    public ContainerMeter(EntityPlayer player, HandHeldMeter meter) {
        super(player, meter, 218);
        this.mode = ContainerMeter.Mode.EnergyIn;
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (this.uut != null) {
            NodeStats stats = EnergyNetGlobal.instance.getNodeStats(this.uut);
            if (stats == null) {
                this.base.closeGUI();
            } else {
                double result = 0.0;
                switch (this.mode) {
                    case EnergyIn:
                        result = stats.getEnergyIn();
                        break;
                    case EnergyOut:
                        result = stats.getEnergyOut();
                        break;
                    case EnergyGain:
                        result = stats.getEnergyIn() - stats.getEnergyOut();
                        break;
                    case Voltage:
                        result = stats.getVoltage();
                }

                if (this.resultCount == 0) {
                    this.resultAvg = this.resultMin = this.resultMax = result;
                } else {
                    if (result < this.resultMin) {
                        this.resultMin = result;
                    }

                    if (result > this.resultMax) {
                        this.resultMax = result;
                    }

                    this.resultAvg = (this.resultAvg * (double) this.resultCount + result) / (double) (this.resultCount + 1);
                }

                ++this.resultCount;
                IUCore.network.get(true).sendContainerFields(this, "resultAvg", "resultMin",
                        "resultMax", "resultCount"
                );
            }
        }
    }

    public double getResultAvg() {
        return this.resultAvg;
    }

    public double getResultMin() {
        return this.resultMin;
    }

    public double getResultMax() {
        return this.resultMax;
    }

    public int getResultCount() {
        return this.resultCount;
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        IUCore.network.get(false).sendContainerField(this, "mode");
        this.reset();
    }

    public void reset() {
        if (IC2.platform.isSimulating()) {
            this.resultCount = 0;
        } else {
            IUCore.network.get(false).sendContainerEvent(this, "reset");
        }

    }

    public void setUut(IAdvEnergyTile uut) {
        assert this.uut == null;

        this.uut = uut;
    }

    public void onContainerEvent(String event) {
        super.onContainerEvent(event);
        if ("reset".equals(event)) {
            this.reset();
        }

    }

    public enum Mode {
        EnergyIn,
        EnergyOut,
        EnergyGain,
        Voltage;

        Mode() {
        }
    }

}
