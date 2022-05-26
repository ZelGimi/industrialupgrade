package com.quantumgenerators.proxy;

import com.quantumgenerators.RenderQG;
import com.quantumgenerators.TileEntityQuantumGenerator;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy{
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityQuantumGenerator.class, new RenderQG());

    }
    public void preinit(FMLPreInitializationEvent event) {
        super.preinit(event);
    }
    public void postinit(FMLPostInitializationEvent event) {
        super.postinit(event);
    }
}
