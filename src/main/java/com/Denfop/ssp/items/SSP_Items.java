package com.Denfop.ssp.items;

import java.util.EnumSet;

import com.Denfop.ssp.Configs;
import com.Denfop.ssp.SuperSolarPanels;
import com.Denfop.ssp.items.armor.ItemArmorQuantumBoosts;
import com.Denfop.ssp.items.armor.ItemArmorQuantumHelmet;
import com.Denfop.ssp.items.armor.ItemArmorQuantumLeggins;
import com.Denfop.ssp.items.armor.ItemArmourSolarHelmet;
import com.Denfop.ssp.items.armor.ItemGraviChestplate;
import com.Denfop.ssp.items.battery.ItemBattery;
import com.Denfop.ssp.items.reactors.ItemReactorHeatStorage;
import com.Denfop.ssp.items.reactors.ItemReactorProton;
import com.Denfop.ssp.items.resource.CraftingThings.CraftingTypes;
import com.Denfop.ssp.items.tools.ItemNanoSaber;
import com.Denfop.ssp.items.tools.ItemUltDrill;
import com.Denfop.ssp.items.tools.ItemNanoSaber;

import ic2.core.block.state.IIdProvider;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ToolClass;
import ic2.core.ref.IItemModelProvider;
import ic2.core.ref.IMultiItem;
import ic2.core.ref.ItemName;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum SSP_Items {
  Spectral_SOLAR_HELMET, Singular_SOLAR_HELMET,  Quantum_leggins, Quantum_boosts, CRAFTING, GRAVI_CHESTPLATE, ADVANCED_crystal, quantumsaber, spectralsaber, Quantum_HELMET, twelve_heat_storage, max_heat_storage, proton_fuel_rod, quad_proton_fuel_rod, eit_proton_fuel_rod, dual_proton_fuel_rod, HYBRID_SOLAR_HELMET, ULTIMATE_HYBRID_SOLAR_HELMET, ADVANCED_SOLAR_HELMET, 
drill,
  iridium, compressiridium, spectral, compressiridium1, iridium1, spectral1;
  //battery_su
  ;
  
  public Item instance;
  
  public <T extends Item> T getInstance() {
    return (T)this.instance;
  }
  
  public <T extends Enum> ItemStack getItemStack(T variant) {
    if (this.instance == null)
      return null; 
    if (this.instance instanceof IMultiItem) {
      IMultiItem<IIdProvider> multiItem = (IMultiItem<IIdProvider>)this.instance;
      return multiItem.getItemStack((IIdProvider)variant);
    } 
    if (variant == null)
      return new ItemStack(this.instance); 
    throw new IllegalArgumentException("Not applicable");
  }
  
  public <T extends Item> void setInstance(T instance) {
    if (this.instance != null)
      throw new IllegalStateException("Duplicate instances!"); 
    this.instance = (Item)instance;
  }
  
  public static void buildItems(Side side) {
    Singular_SOLAR_HELMET.setInstance(new ItemArmourSolarHelmet(ItemArmourSolarHelmet.SolarHelmetTypes.Singular));
    Spectral_SOLAR_HELMET.setInstance(new ItemArmourSolarHelmet(ItemArmourSolarHelmet.SolarHelmetTypes.Spectral));
    CRAFTING.setInstance(new com.Denfop.ssp.items.resource.CraftingThings());
    GRAVI_CHESTPLATE.setInstance(new ItemGraviChestplate());
    Quantum_leggins.setInstance(new ItemArmorQuantumLeggins());
    Quantum_boosts.setInstance(new ItemArmorQuantumBoosts());
    ADVANCED_crystal.setInstance(new ItemBattery("spectral_battery", Configs.maxCharge8, Configs.transferLimit8, Configs.tier8));
    quantumsaber.setInstance(new ItemNanoSaber("quantumsaber", 10, HarvestLevel.Diamond, ToolClass.Sword, Configs.maxCharge1,Configs.transferLimit1,Configs.tier1, Configs.damage1, Configs.damage2));
    spectralsaber.setInstance(new ItemNanoSaber("spectralsaber", 10, HarvestLevel.Diamond, ToolClass.Sword,Configs.maxCharge2,Configs.transferLimit2,Configs.tier2,Configs.damage3,Configs.damage4));
    Quantum_HELMET.setInstance(new ItemArmorQuantumHelmet(ItemArmorQuantumHelmet.SolarHelmetTypes.Helmet));
    twelve_heat_storage.setInstance(new ItemReactorHeatStorage("twelve_heat_storage",Configs.twelve_heat_storage));
    max_heat_storage.setInstance(new ItemReactorHeatStorage("max_heat_storage", Configs.max_heat_storage));
    proton_fuel_rod.setInstance(new ItemReactorProton("proton_fuel_rod", 1));
    dual_proton_fuel_rod.setInstance(new ItemReactorProton("dual_proton_fuel_rod", 2));
    quad_proton_fuel_rod.setInstance(new ItemReactorProton("quad_proton_fuel_rod", 4));
    eit_proton_fuel_rod.setInstance(new ItemReactorProton("eit_proton_fuel_rod", 8));
    ADVANCED_SOLAR_HELMET.setInstance(new ItemArmourSolarHelmet(ItemArmourSolarHelmet.SolarHelmetTypes.ADVANCED));
    HYBRID_SOLAR_HELMET.setInstance(new ItemArmourSolarHelmet(ItemArmourSolarHelmet.SolarHelmetTypes.HYBRID));
    ULTIMATE_HYBRID_SOLAR_HELMET.setInstance(new ItemArmourSolarHelmet(ItemArmourSolarHelmet.SolarHelmetTypes.ULTIMATE));
   drill.setInstance(new ItemUltDrill());
   iridium.setInstance(new ItemWindRotor("rotor_carbon1",Configs.Radius, Configs.rotor_carbon1, Configs.coefficient, Configs.minWindStrength, Configs.maxWindStrength ,new ResourceLocation("super_solar_panels", "textures/items/carbon_rotor_model1.png")));
   compressiridium.setInstance(new ItemWindRotor("rotor_carbon2", Configs.Radius1, Configs.rotor_carbon2, Configs.coefficient1, Configs.minWindStrength1, Configs.maxWindStrength1,  new ResourceLocation("super_solar_panels", "textures/items/carbon_rotor_model2.png")));
   spectral.setInstance(new ItemWindRotor("rotor_carbon3", Configs.Radius2, Configs.rotor_carbon3, Configs.coefficient2, Configs.minWindStrength2, Configs.maxWindStrength2,  new ResourceLocation("super_solar_panels", "textures/items/carbon_rotor_model3.png")));
   //
   iridium1.setInstance(new ItemWindRotor("rotor_carbon4",Configs.Radius, Configs.rotor_carbon1, Configs.coefficient, Configs.minWindStrength, Configs.maxWindStrength ,new ResourceLocation("super_solar_panels", "textures/items/carbon_rotor_model1.png")));
   compressiridium1.setInstance(new ItemWindRotor("rotor_carbon5", Configs.Radius1, Configs.rotor_carbon2, Configs.coefficient1, Configs.minWindStrength1, Configs.maxWindStrength1,  new ResourceLocation("super_solar_panels", "textures/items/carbon_rotor_model2.png")));
   spectral1.setInstance(new ItemWindRotor("rotor_carbon6", Configs.Radius2, Configs.rotor_carbon3, Configs.coefficient2, Configs.minWindStrength2, Configs.maxWindStrength2,  new ResourceLocation("super_solar_panels", "textures/items/carbon_rotor_model3.png")));
   
   
   
   // new ItemReactorHeatStorage(ItemName.hex_heat_storage, 60000);
    //new ItemReactorMOX(ItemName.mox_fuel_rod, 1);
  //  new ItemReactorMOX(ItemName.dual_mox_fuel_rod, 2);
 //   new ItemReactorMOX(ItemName.quad_mox_fuel_rod, 4);
    
  //  battery_su.setInstance(new ItemBatteryChargeHotbar("charging_energy_crystal", 4000000.0D, 8192.0D, 3));
   // new ItemBatteryChargeHotbar(ItemName.charging_energy_crystal, 4000000.0D, 8192.0D, 3);
    if (side == Side.CLIENT)
      doModelGuf(); 
  }
  
  @SideOnly(Side.CLIENT)
  public static void doModelGuf() {
    for (SSP_Items item : values())
      ((IItemModelProvider)item.getInstance()).registerModels((ItemName)null); 
  }

public ItemStack getItemStack(CraftingTypes proton, int i) {
	// TODO Auto-generated method stub
	return null;
}
}
