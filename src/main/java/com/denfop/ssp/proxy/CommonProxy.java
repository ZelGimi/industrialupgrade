package com.denfop.ssp.proxy;

import com.denfop.ssp.common.BlocksRegister;
import com.denfop.ssp.common.SSPWorldDecorator;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        BlocksRegister.register();
    }

    public void init(FMLInitializationEvent event) {
        GameRegistry.registerWorldGenerator(new SSPWorldDecorator(), 0);
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

}
