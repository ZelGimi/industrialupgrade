package com.Denfop.ssp.proxy;

import com.Denfop.ssp.BlocksRegister;
import com.Denfop.ssp.fluid.Neutron.BlockRegister;
import com.Denfop.ssp.fluid.Neutron.FluidRegister;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
  public void preInit(FMLPreInitializationEvent event) {
    super.preInit(event);
  }
  
  public void init(FMLInitializationEvent event) {
    super.init(event);
    BlocksRegister.registerRender();
    BlockRegister.registerRender();
  }
  
  public void postInit(FMLPostInitializationEvent event) {
    super.postInit(event);
  }
}
