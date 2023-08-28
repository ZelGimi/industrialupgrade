package com.powerutils.handler;


import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

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

    public static boolean isTeslaCapability(Capability<?> capability) {
        if (!isLoaded()) {
            return false;
        } else {
            return capability == TESLA_CONSUMER || capability == TESLA_HOLDER || capability == TESLA_PRODUCER;
        }
    }

}
