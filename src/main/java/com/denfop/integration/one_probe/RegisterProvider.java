package com.denfop.integration.one_probe;

import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

import java.util.function.Function;

public class RegisterProvider {

    @SubscribeEvent
    public void registerIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", () ->
                (Function<ITheOneProbe, Void>) theOneProbe -> {
                    theOneProbe.registerProvider(new MyProbeInfoProvider());
                    return null;
                }
        );
    }
}
