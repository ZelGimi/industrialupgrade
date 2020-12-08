package com.denfop.ssp.proxy;

import com.denfop.ssp.BlocksRegister;
import com.denfop.ssp.fluid.Neutron.BlockRegister;
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
