package com.denfop.ssp.integration.botania;




import com.denfop.ssp.Configs;
import com.denfop.ssp.items.SSP_Items;
import com.denfop.ssp.items.reactors.ItemReactorUranium;
import com.denfop.ssp.items.resource.CraftingThings;

import ic2.api.reactor.IReactor;
import ic2.core.init.BlocksItems;
import ic2.core.item.type.NuclearResourceType;
import ic2.core.profile.NotClassic;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@NotClassic
public class ItemReactorBotania extends ItemReactorUranium {
  public ItemReactorBotania(String name, int cells) {
    super(name, cells, Configs.terrasteel_fuel_rod);
    
  }
  
  protected int getFinalHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
    if (reactor.isFluidCooled()) {
      float breedereffectiveness = reactor.getHeat() / reactor.getMaxHeat();
      if (breedereffectiveness > 0.5D)
        heat *= 1.5; 
    } 
    return heat;
  }
  
  protected ItemStack getDepletedStack(ItemStack stack, IReactor reactor) {
    ItemStack ret;
    switch (this.numberOfCells) {
      case 1:
        ret = BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.depleted_terrasteel_fuel_rod);
        return ret.copy();
      case 2:
        ret = BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.depleted_dual_terrasteel_fuel_rod);
        return ret.copy();
      case 4:
        ret = BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.depleted_quad_terrasteel_fuel_rod);
        return ret.copy();
      case 8:
          ret = BotaniaItems.BotaniaCRAFTING.getItemStack(BotaniaCraftingThings.CraftingTypes.depleted_eit_terrasteel_fuel_rod);
          return ret.copy();
    } 
    throw new RuntimeException("invalid cell count: " + this.numberOfCells);
  }
  
  public boolean acceptUraniumPulse(ItemStack stack, IReactor reactor, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY, boolean heatrun) {
    if (!heatrun) {
      float breedereffectiveness = (reactor.getHeat() / reactor.getMaxHeat()) * 32.0F;
      float ReaktorOutput = 32.0F * breedereffectiveness + 1.0F;
      reactor.addOutput(ReaktorOutput);
    } 
    return true;
  }
}
