//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.powerutils.handler;


import javax.annotation.Nullable;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Optional.Method;

public class TeslaHelper {
    @Nullable
    @CapabilityInject(ITeslaConsumer.class)
    public static Capability<ITeslaConsumer> TESLA_CONSUMER = null;
    @Nullable
    @CapabilityInject(ITeslaProducer.class)
    public static Capability<ITeslaProducer> TESLA_PRODUCER = null;
    @Nullable
    @CapabilityInject(ITeslaHolder.class)
    public static Capability<ITeslaHolder> TESLA_HOLDER = null;

    public TeslaHelper() {
    }

    public static boolean isLoaded() {
        return TESLA_CONSUMER != null && TESLA_PRODUCER != null && TESLA_HOLDER != null;
    }

    public static boolean isEnergyReceiver(TileEntity tile, EnumFacing side) {
        return isLoaded() && _isEnergyReceiver(tile, side);
    }

    public static int sendEnergy(TileEntity tile, EnumFacing side, int amount, boolean simulate) {
        return isLoaded() ? _sendEnergy(tile, side, amount, simulate) : 0;
    }

    @Method(
            modid = "tesla"
    )
    private static boolean _isEnergyReceiver(TileEntity tile, EnumFacing side) {
        return TESLA_CONSUMER != null && tile.hasCapability(TESLA_CONSUMER, side);
    }

    @Method(
            modid = "tesla"
    )
    private static int _sendEnergy(TileEntity tile, EnumFacing side, int amount, boolean simulate) {
        ITeslaConsumer consumer = tile.getCapability(TESLA_CONSUMER, side);
        if (consumer == null) {
            if (tile.hasCapability(TESLA_CONSUMER, side)) {
             }

            return 0;
        } else {
            return (int)consumer.givePower((long)amount, simulate);
        }
    }

    public static boolean isTeslaCapability(Capability<?> capability) {
        if (!isLoaded()) {
            return false;
        } else {
            return capability == TESLA_CONSUMER || capability == TESLA_HOLDER || capability == TESLA_PRODUCER;
        }
    }
}
