package com.denfop.ssp.items;

import com.denfop.ssp.SuperSolarPanels;
import com.denfop.ssp.common.Configs;
import com.denfop.ssp.items.armor.*;
import com.denfop.ssp.items.battery.ItemBattery;
import com.denfop.ssp.items.reactors.ItemReactorHeatStorage;
import com.denfop.ssp.items.reactors.ItemReactorProton;
import com.denfop.ssp.items.resource.CraftingThings;
import com.denfop.ssp.items.resource.CraftingThings.CraftingTypes;
import com.denfop.ssp.items.tools.ItemNanoSaber;
import com.denfop.ssp.items.tools.ItemUltDrill;
import ic2.core.block.state.IIdProvider;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ToolClass;
import ic2.core.ref.IItemModelProvider;
import ic2.core.ref.IMultiItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public enum SSPItems {
	SPECTRAL_SOLAR_HELMET, SINGULAR_SOLAR_HELMET, QUANTUM_LEGGINGS, QUANTUM_BOOSTS, CRAFTING, GRAVI_CHESTPLATE, ADVANCED_CRYSTAL, QUANTUM_SABER, SPECTRAL_SABER, QUANTUM_HELMET, TWELVE_HEAT_STORAGE, MAX_HEAT_STORAGE, PROTON_FUEL_ROD, QUAD_PROTON_FUEL_ROD, EIT_PROTON_FUEL_ROD, DUAL_PROTON_FUEL_ROD, HYBRID_SOLAR_HELMET, ULTIMATE_HYBRID_SOLAR_HELMET, ADVANCED_SOLAR_HELMET,
	DRILL,
	IRIDIUM, COMPRESSIRIDIUM, SPECTRAL;
	//battery_su

	public Item instance;

	public static void buildItems(Side side) {
		SINGULAR_SOLAR_HELMET.setInstance(new ItemArmourSolarHelmet(ItemArmourSolarHelmet.SolarHelmetTypes.Singular));
		SPECTRAL_SOLAR_HELMET.setInstance(new ItemArmourSolarHelmet(ItemArmourSolarHelmet.SolarHelmetTypes.Spectral));
		CRAFTING.setInstance(new CraftingThings());
		GRAVI_CHESTPLATE.setInstance(new ItemGraviChestplate());
		QUANTUM_LEGGINGS.setInstance(new ItemArmorQuantumLeggins());
		QUANTUM_BOOSTS.setInstance(new ItemArmorQuantumBoosts());
		ADVANCED_CRYSTAL.setInstance(new ItemBattery("spectral_battery", Configs.maxCharge8, Configs.transferLimit8, Configs.tier8));
		QUANTUM_SABER.setInstance(new ItemNanoSaber("quantumsaber", 10, HarvestLevel.Diamond, ToolClass.Sword, Configs.maxCharge1, Configs.transferLimit1, Configs.tier1, Configs.damage1, Configs.damage2));
		SPECTRAL_SABER.setInstance(new ItemNanoSaber("spectralsaber", 10, HarvestLevel.Diamond, ToolClass.Sword, Configs.maxCharge2, Configs.transferLimit2, Configs.tier2, Configs.damage3, Configs.damage4));
		QUANTUM_HELMET.setInstance(new ItemArmorQuantumHelmet(ItemArmorQuantumHelmet.SolarHelmetTypes.Helmet));
		TWELVE_HEAT_STORAGE.setInstance(new ItemReactorHeatStorage("twelve_heat_storage", Configs.twelve_heat_storage));
		MAX_HEAT_STORAGE.setInstance(new ItemReactorHeatStorage("max_heat_storage", Configs.max_heat_storage));
		PROTON_FUEL_ROD.setInstance(new ItemReactorProton("proton_fuel_rod", 1));
		DUAL_PROTON_FUEL_ROD.setInstance(new ItemReactorProton("dual_proton_fuel_rod", 2));
		QUAD_PROTON_FUEL_ROD.setInstance(new ItemReactorProton("quad_proton_fuel_rod", 4));
		EIT_PROTON_FUEL_ROD.setInstance(new ItemReactorProton("eit_proton_fuel_rod", 8));
		ADVANCED_SOLAR_HELMET.setInstance(new ItemArmourSolarHelmet(ItemArmourSolarHelmet.SolarHelmetTypes.ADVANCED));
		HYBRID_SOLAR_HELMET.setInstance(new ItemArmourSolarHelmet(ItemArmourSolarHelmet.SolarHelmetTypes.HYBRID));
		ULTIMATE_HYBRID_SOLAR_HELMET.setInstance(new ItemArmourSolarHelmet(ItemArmourSolarHelmet.SolarHelmetTypes.ULTIMATE));
		DRILL.setInstance(new ItemUltDrill());
		IRIDIUM.setInstance(new ItemWindRotor("rotor_carbon1", Configs.iridiumRotorRadius, Configs.rotor_carbon1, Configs.coefficient, Configs.minWindStrength, Configs.maxWindStrength, SuperSolarPanels.getIdentifier("textures/items/carbon_rotor_model1.png")));
		COMPRESSIRIDIUM.setInstance(new ItemWindRotor("rotor_carbon2", Configs.compressIridiumRotorRadius, Configs.rotor_carbon2, Configs.coefficient1, Configs.minWindStrength1, Configs.maxWindStrength1, SuperSolarPanels.getIdentifier("textures/items/carbon_rotor_model2.png")));
		SPECTRAL.setInstance(new ItemWindRotor("rotor_carbon3", Configs.spectralRotorRadius, Configs.rotor_carbon3, Configs.coefficient2, Configs.minWindStrength2, Configs.maxWindStrength2, SuperSolarPanels.getIdentifier("textures/items/carbon_rotor_model3.png")));
		if (side == Side.CLIENT)
			doModelGuf();
	}

	@SideOnly(Side.CLIENT)
	public static void doModelGuf() {
		for (SSPItems item : values())
			((IItemModelProvider) item.getInstance()).registerModels(null);
	}

	@SuppressWarnings("unchecked")
	public <T extends Item> T getInstance() {
		return (T) this.instance;
	}

	public <T extends Item> void setInstance(T instance) {
		if (this.instance != null)
			throw new IllegalStateException("Duplicate instances!");
		this.instance = instance;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public <T extends Enum<?>> ItemStack getItemStack(T variant) {
		if (this.instance == null)
			throw new IllegalArgumentException("Not applicable. instance == null");
		if (this.instance instanceof IMultiItem) {
			IMultiItem<IIdProvider> multiItem = (IMultiItem<IIdProvider>) this.instance;
			return multiItem.getItemStack((IIdProvider) variant);
		}
		if (variant == null)
			return new ItemStack(this.instance);
		throw new IllegalArgumentException("Not applicable");
	}

	public ItemStack getItemStack(CraftingTypes proton, int i) {
		// TODO Auto-generated method stub
		return null;
	}
}
