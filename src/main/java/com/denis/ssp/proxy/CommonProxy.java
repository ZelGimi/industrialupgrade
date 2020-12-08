package com.Denfop.ssp.proxy;

import com.Denfop.ssp.BlocksRegister;
import com.Denfop.ssp.SSPWorldDecorator;
import com.Denfop.ssp.fluid.Neutron.BlockRegister;
import com.Denfop.ssp.fluid.Neutron.FluidRegister;

import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
  public void preInit(FMLPreInitializationEvent event) {
	  BlocksRegister.register();

	  
  }
  
  public void init(FMLInitializationEvent event) {
    GameRegistry.registerWorldGenerator((IWorldGenerator)new SSPWorldDecorator(), 0);
  }
  
  public void postInit(FMLPostInitializationEvent event) {}

}
