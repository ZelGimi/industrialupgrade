package com.denfop.ssp.common;

import com.denfop.ssp.SuperSolarPanels;
import com.denfop.ssp.molecular.MTRecipe;
import com.denfop.ssp.molecular.MolecularTransformerRecipeManager;
import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;
import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;
import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;
import net.minecraftforge.common.config.Configuration;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public final class Configs {
	private static final String NEW_LINE = System.getProperty("line.separator");
	private static final String COMMON_KEY = "Settings";

	private static void loadNormalConfig(final File configFile, final boolean client) {
		final Configuration config = new Configuration(configFile);
		try {
			config.load();
			Panel.solar[ Panel.Advanced.ordinal() ] = new TileEntitySolarPanel.SolarConfig(config.get("Advanced solar panels", "AdvancedSPGenDay", 4).getInt(4), config.get("Advanced solar panels", "AdvancedSPGenNight", 4).getInt(4), config.get("Advanced solar panels", "AdvancedSPStorage", 16000).getInt(16000), config.get("Advanced solar panels", "AdvancedSPTier", 1).getInt(1));
			Panel.solar[ Panel.Hybrid.ordinal() ] = new TileEntitySolarPanel.SolarConfig(config.get("Hybrid solar panels", "HybrydSPGenDay", 32).getInt(32), config.get("Hybrid solar panels", "HybrydSPGenNight", 8).getInt(8), config.get("Hybrid solar panels", "HybrydSPStorage", 50000).getInt(50000), config.get("Hybrid solar panels", "HybrydSPTier", 2).getInt(2));
			Panel.solar[ Panel.Ultimate.ordinal() ] = new TileEntitySolarPanel.SolarConfig(config.get("Ultimate solar panels", "UltimateHSPGenDay", 256).getInt(256), config.get("Ultimate solar panels", "UltimateHSPGenNight", 64).getInt(64), config.get("Ultimate solar panels", "UltimateHSPStorage", 500000).getInt(500000), config.get("Ultimate solar panels", "UltimateHSPTier", 3).getInt(3));
			Panel.solar[ Panel.Quantum.ordinal() ] = new TileEntitySolarPanel.SolarConfig(config.get("Quantum solar panels", "QuantumSPGenDay", 2048).getInt(2048), config.get("Quantum solar panels", "QuantumSPGenNight", 1024).getInt(1024), config.get("Quantum solar panels", "QuantumSPStorage", 5000000).getInt(5000000), config.get("Quantum solar panels", "QuantumSPTier", 4).getInt(4));
			Panel.solar[ Panel.Spectral.ordinal() ] = new TileEntitySolarPanel.SolarConfig(config.get("Spectral solar panels", "SpecrtalGenDay", 8192).getInt(8192), config.get("Spectral solar panels", "SpecrtalGenNight", 5000).getInt(5000), config.get("Spectral solar panels", "SpecrtalStorage", 10000000).getInt(10000000), config.get("Spectral solar panels", "SpecrtalTier", 5).getInt(5));
			Panel.solar[ Panel.Proton.ordinal() ] = new TileEntitySolarPanel.SolarConfig(config.get("Proton solar panels", "ProtonGenDay", 32768).getInt(32768), config.get("Proton solar panels", "ProtonGenNight", 20000).getInt(20000), config.get("Proton solar panels", "ProtonStorage", 999999999).getInt(999999999), config.get("Proton solar panels", "ProtonTier", 6).getInt(6));
			Panel.solar[ Panel.Singular.ordinal() ] = new TileEntitySolarPanel.SolarConfig(config.get("Singular solar panels", "SingularGenDay", 131072).getInt(131072), config.get("Singular solar panels", "SingularGenNight", 104857).getInt(104857), config.get("Singular solar panels", "SingularStorage", 50000000).getInt(50000000), config.get("Singular solar panels", "SingularTier", 7).getInt(7));
			Panel.solar[ Panel.LightAbsord.ordinal() ] = new TileEntitySolarPanel.SolarConfig(config.get("Light-Absord solar panels", "Light-AbsordGenDay", 554288).getInt(554288), config.get("Light-Absord solar panels", "Light-AbsordGenNight", 554288).getInt(554288), config.get("Light-Absord solar panels", "Light-AbsordStorage", 100000000).getInt(100000000), config.get("Light-Absord solar panels", "Light-AbsordTier", 8).getInt(8));
			Panel.solar[ Panel.Photonic.ordinal() ] = new TileEntitySolarPanel.SolarConfig(config.get("Photonic solar panels", "PhotonicGenDay", 2621440).getInt(2621440), config.get("Photonic solar panels", "PhotonicGenNight", 2621440).getInt(2621440), config.get("Photonic solar panels", "PhotonicStorage", 400000000).getInt(400000000), config.get("Photonic solar panels", "PhotonicTier", 9).getInt(9));
			Panel.solar[ Panel.Neutron.ordinal() ] = new TileEntitySolarPanel.SolarConfig(config.get("Neutron solar panels", "NeutronGenDay", 10485760).getInt(10485760), config.get("Neutron solar panels", "NeutronGenNight", 10485760).getInt(10485760), config.get("Neutron solar panels", "NeutronStorage", 800000000).getInt(800000000), config.get("Neutron solar panels", "NeutronTier", 10).getInt(10));


			Panel.rain[ Panel.Advanced.ordinal() ] = new TileEntityRainPanel.SolarConfig(config.get("Advanced Rain panels", "AdvancedSPGenRain", 4).getInt(4), config.get("Advanced Rain panels", "AdvancedSPStorage", 16000).getInt(16000), config.get("Advanced Rain panels", "AdvancedSPTier", 1).getInt(1));
			Panel.rain[ Panel.Hybrid.ordinal() ] = new TileEntityRainPanel.SolarConfig(config.get("Hybrid Rain panels", "HybrydSPGenRain", 8).getInt(8), config.get("Hybrid Rain panels", "HybrydSPStorage", 50000).getInt(50000), config.get("Hybrid Rain panels", "HybrydSPTier", 2).getInt(2));
			Panel.rain[ Panel.Ultimate.ordinal() ] = new TileEntityRainPanel.SolarConfig(config.get("Ultimate Rain panels", "UltimateHSPGenRain", 64).getInt(64), config.get("Ultimate Rain panels", "UltimateHSPStorage", 500000).getInt(500000), config.get("Ultimate Rain panels", "UltimateHSPTier", 3).getInt(3));
			Panel.rain[ Panel.Quantum.ordinal() ] = new TileEntityRainPanel.SolarConfig(config.get("Quantum Rain panels", "QuantumSPGenRain", 1024).getInt(1024), config.get("Quantum Rain panels", "QuantumSPStorage", 5000000).getInt(5000000), config.get("Quantum Rain panels", "QuantumSPTier", 4).getInt(4));
			Panel.rain[ Panel.Spectral.ordinal() ] = new TileEntityRainPanel.SolarConfig(config.get("Spectral Rain panels", "SpecrtalGenRain", 5000).getInt(5000), config.get("Spectral solar panels", "SpecrtalStorage", 10000000).getInt(10000000), config.get("Spectral solar panels", "SpecrtalTier", 5).getInt(5));
			Panel.rain[ Panel.Proton.ordinal() ] = new TileEntityRainPanel.SolarConfig(config.get("Proton Rain panels", "ProtonGenRain", 20000).getInt(20000), config.get("Proton solar Rain", "ProtonStorage", 999999999).getInt(999999999), config.get("Proton Rain panels", "ProtonTier", 6).getInt(6));
			Panel.rain[ Panel.Singular.ordinal() ] = new TileEntityRainPanel.SolarConfig(config.get("Singular Rain panels", "SingularGenRain", 104857).getInt(104857), config.get("Singular solar panels", "SingularStorage", 50000000).getInt(50000000), config.get("Singular solar panels", "SingularTier", 7).getInt(7));
			Panel.rain[ Panel.LightAbsord.ordinal() ] = new TileEntityRainPanel.SolarConfig(config.get("Light-Absord Rain panels", "Light-AbsordGenRain", 554288).getInt(554288), config.get("Light-Absord solar panels", "Light-AbsordStorage", 100000000).getInt(100000000), config.get("Light-Absord solar panels", "Light-AbsordTier", 8).getInt(8));
			Panel.rain[ Panel.Photonic.ordinal() ] = new TileEntityRainPanel.SolarConfig(config.get("Photonic Rain panels", "PhotonicGenRain", 2621440).getInt(2621440), config.get("Photonic solar panels", "PhotonicStorage", 400000000).getInt(400000000), config.get("Photonic solar panels", "PhotonicTier", 9).getInt(9));
			Panel.rain[ Panel.Neutron.ordinal() ] = new TileEntityRainPanel.SolarConfig(config.get("Neutron Rain panels", "NeutronGenRain", 10485760).getInt(10485760), config.get("Neutron Rain panels", "NeutronStorage", 800000000).getInt(800000000), config.get("Neutron Rain panels", "NeutronTier", 10).getInt(10));

			Panel.sun[ Panel.Advanced.ordinal() ] = new TileEntitySunPanel.SolarConfig(config.get("Advanced Sun panels", "AdvancedSPGenSun", 8).getInt(8), config.get("Advanced Sun panels", "AdvancedSPStorage", 16000).getInt(16000), config.get("Advanced Sun panels", "AdvancedSPTier", 1).getInt(1));
			Panel.sun[ Panel.Hybrid.ordinal() ] = new TileEntitySunPanel.SolarConfig(config.get("Hybrid Sun panels", "HybrydSPGenSun", 64).getInt(64), config.get("Hybrid Sun panels", "HybrydSPStorage", 50000).getInt(50000), config.get("Hybrid Sun panels", "HybrydSPTier", 2).getInt(2));
			Panel.sun[ Panel.Ultimate.ordinal() ] = new TileEntitySunPanel.SolarConfig(config.get("Ultimate Sun panels", "UltimateHSPGenSun", 512).getInt(512), config.get("Ultimate Sun panels", "UltimateHSPStorage", 500000).getInt(500000), config.get("Ultimate Sun panels", "UltimateHSPTier", 3).getInt(3));
			Panel.sun[ Panel.Quantum.ordinal() ] = new TileEntitySunPanel.SolarConfig(config.get("Quantum Sun panels", "QuantumSPGenSun", 4096).getInt(4096), config.get("Quantum Sun panels", "QuantumSPStorage", 5000000).getInt(5000000), config.get("Quantum Sun panels", "QuantumSPTier", 4).getInt(4));
			Panel.sun[ Panel.Spectral.ordinal() ] = new TileEntitySunPanel.SolarConfig(config.get("Spectral Sun panels", "SpecrtalGenSun", 16384).getInt(16384), config.get("Spectral Sun panels", "SpecrtalStorage", 10000000).getInt(10000000), config.get("Spectral Sun panels", "SpecrtalTier", 5).getInt(5));
			Panel.sun[ Panel.Proton.ordinal() ] = new TileEntitySunPanel.SolarConfig(config.get("Proton Sun panels", "ProtonGenSun", 65536).getInt(65536), config.get("Proton Sun panels", "ProtonStorage", 999999999).getInt(999999999), config.get("Proton Sun panels", "ProtonTier", 6).getInt(6));
			Panel.sun[ Panel.Singular.ordinal() ] = new TileEntitySunPanel.SolarConfig(config.get("Singular Sun panels", "SingularGenSun", 262144).getInt(262144), config.get("Singular Sun panels", "SingularStorage", 50000000).getInt(50000000), config.get("Singular Sun panels", "SingularTier", 7).getInt(7));
			Panel.sun[ Panel.LightAbsord.ordinal() ] = new TileEntitySunPanel.SolarConfig(config.get("Light-Absord Sun panels", "Light-AbsordGenSun", 1108576).getInt(1108576), config.get("Light-Absord Sun panels", "Light-AbsordStorage", 100000000).getInt(100000000), config.get("Light-Absord Sun panels", "Light-AbsordTier", 8).getInt(8));
			Panel.sun[ Panel.Photonic.ordinal() ] = new TileEntitySunPanel.SolarConfig(config.get("Photonic Sun panels", "PhotonicGenSun", 5242880).getInt(5242880), config.get("Photonic Sun panels", "PhotonicStorage", 400000000).getInt(400000000), config.get("Photonic Sun panels", "PhotonicTier", 9).getInt(9));
			Panel.sun[ Panel.Neutron.ordinal() ] = new TileEntitySunPanel.SolarConfig(config.get("Neutron Sun panels", "NeutronGenSun", 20971520).getInt(20971520), config.get("Neutron Sun panels", "NeutronStorage", 800000000).getInt(800000000), config.get("Neutron Sun panels", "NeutronTier", 10).getInt(10));

			//  canCraftMT = !config.get("settings Quantum Boosts", "Disable Effect WATER BREATHING ", false).getBoolean(false);
			Weapon.Saber.Quantum.damageInactive = config.get("Settings quantum saber", "Damage quantum saber(not right click)+1 ", 11).getInt(11);
			Weapon.Saber.Quantum.damageActive = config.get("Settings quantum saber", "Damage quantum saber(right click)+1", 29).getInt(29);
			Weapon.Saber.Spectral.damageInactive = config.get("Settings spectral saber", "Damage quantum saber(not right click)+1 ", 14).getInt(14);
			Weapon.Saber.Spectral.damageActive = config.get("Settings spectral saber", "Damage quantum saber(right click)+1 ", 39).getInt(39);
			Weapon.Saber.Quantum.maxCharge = config.get("Settings quantum saber", "maxCharge", 300000).getInt(300000);
			Weapon.Saber.Spectral.maxCharge = config.get("Settings spectral saber", "maxCharge", 600000).getInt(600000);
			Weapon.Saber.Quantum.transferLimit = config.get("Settings quantum saber", "transferLimit", 2000).getInt(2000);
			Weapon.Saber.Spectral.transferLimit = config.get("Settings spectral saber", "transferLimit ", 2000).getInt(2000);
			Weapon.Saber.Quantum.tier = config.get("Settings quantum saber", "tier ", 4).getInt(4);
			Weapon.Saber.Spectral.tier = config.get("Settings spectral saber", "tier", 5).getInt(5);
			//
			Armor.Quantum.boots.maxCharge = config.get("Settings Quantum Boosts", "maxCharge", 100000000).getInt(100000000);
			Armor.Quantum.helmet.maxCharge = config.get("Settings Quantum Helmet", "maxCharge", 100000000).getInt(100000000);
			Armor.Quantum.leggings.maxCharge = config.get("Settings Quantum Leggings", "maxCharge", 100000000).getInt(100000000);
			Armor.Quantum.chestplate.maxCharge = config.get("Settings Quantum Chestplate", "maxCharge ", 100000000).getInt(100000000);
			Weapon.Saber.Quantum.maxCharge = config.get("Settings quantum saber", "maxCharge", 300000).getInt(300000);
			Weapon.Saber.Spectral.maxCharge = config.get("Settings spectral saber", "maxCharge", 600000).getInt(600000);
			Armor.Quantum.boots.transferLimit = config.get("Settings Quantum Boosts", "transferLimit", 100000).getInt(100000);
			Armor.Quantum.helmet.transferLimit = config.get("Settings Quantum Helmet", "transferLimit ", 100000).getInt(100000);
			Armor.Quantum.leggings.transferLimit = config.get("Settings Quantum Leggings", "transferLimit ", 100000).getInt(100000);
			Armor.Quantum.chestplate.transferLimit = config.get("Settings Quantum Chestplater", "transferLimit", 100000).getInt(100000);
			Permitting.canCraftDoubleSlabs = !config.get("settings Quantum chestplate", "Disable Effect FIRE RESISTANCE ", false).getBoolean(false);
			Permitting.canCraftMT = !config.get("settings Quantum Boosts", "Disable Effect WATER BREATHING ", false).getBoolean(false);
			Permitting.canCraftASP = !config.get("settings Quantum Boosts", "Disable Effect JUMP BOOST", false).getBoolean(false);
			Permitting.canCraftASH = !config.get("settings Quantum Boosts", "Disable Effect REGENERATION ", false).getBoolean(false);
			Permitting.canCraftHSP = !config.get("settings Quantum Leggins", "Disable Effect SPEED ", false).getBoolean(false);
			Permitting.canCraftHSH = !config.get("settings Quantum Leggins", "Disable Effect LUCK", false).getBoolean(false);
			//
			Armor.Quantum.boots.tier = config.get("Settings Quantum Boosts", "tier", 8).getInt(8);
			Armor.Quantum.helmet.tier = config.get("Settings Quantum Helmet", "tier ", 8).getInt(8);
			Armor.Quantum.leggings.tier = config.get("Settings Quantum Leggings", "tier ", 8).getInt(8);
			Armor.Quantum.chestplate.tier = config.get("Settings Quantum Chestplater", "tier", 8).getInt(8);
			//
			//terrasteel_fuel_rod = config.get("Settings terrasteel fuel rods", "maxDamage(times, 1=1 seconds) ", 15000).getInt(15000);
			Battery.Spectral.maxCharge = config.get("Settings Spectral Battery", "maxCharge", 100000000).getInt(100000000);
			Rod.CoolingRod.Twelve.storage = config.get("Settings twelve heat storage", "heatStorage", 120000).getInt(120000);
			Rod.CoolingRod.Max.storage = config.get("Settings max heat storage", "heatStorage", 240000).getInt(240000);
			Rod.FuelRod.Proton.storage = config.get("Settings proton fuel rods", "maxDamage(times, 1=1 seconds) ", 30000).getInt(30000);
			//maxCharge10 = config.get("Settings Advanced Solar Nano Helmet", "maxCharge", 300000).getInt(300000);
			//maxCharge11 = config.get("Settings Ultimate and Hybrid Solar Quantum Helmet", "maxCharge", 600000).getInt(600000);
			Battery.Spectral.transferLimit = config.get("Settings Spectral Battery", "transferLimit", 30000).getInt(30000);
			Rotor.Iridium.efficient = config.get("Settings Iridium rotor", "efficiency ", 2.0F).getInt((int) 2.0F);
			Rotor.CompressIridium.efficient = config.get("Settings Compress iridium rotor", "efficiency ", 3.0F).getInt((int) 3.0F);
			Rotor.Spectral.efficient = config.get("Settings Spectral rotor", "efficiency", 5.0F).getInt((int) 5.0F);
			Battery.Spectral.tier = config.get("Settings Spectral Battery", "tier", 4).getInt(4);
			Rotor.Iridium.durability = config.get("Settings Iridium rotor", "durability(times, 1=1 seconds) ", 648000).getInt(648000);
			Rotor.CompressIridium.durability = config.get("Settings Compress iridium rotor", " durability(times, 1=1 seconds)", 720000).getInt(720000);
			Rotor.Spectral.durability = config.get("Settings Spectral rotor", "durability(times, 1=1 seconds)", 720000).getInt(720000);
			Rotor.Iridium.maxWind = config.get("Settings Iridium rotor", "maxWindStrength", 110).getInt(110);
			Rotor.CompressIridium.maxWind = config.get("Settings Compress iridium rotor", "maxWindStrength", 110).getInt(110);
			Rotor.Spectral.maxWind = config.get("Settings Spectral rotor", "maxWindStrength", 110).getInt(110);
			Rotor.Iridium.minWind = config.get("Settings Iridium rotor", "minWindStrength", 25).getInt(25);
			Rotor.CompressIridium.minWind = config.get("Settings Compress iridium rotor", "minWindStrength", 25).getInt(25);
			Rotor.Spectral.minWind = config.get("Settings Spectral rotor", "minWindStrength", 25).getInt(25);
			Rotor.Iridium.rotorRadius = config.get("Settings Iridium rotor", "Radius", 11).getInt(11);
			Rotor.CompressIridium.rotorRadius = config.get("Settings Compress iridium rotor", "Radius", 11).getInt(11);
			Rotor.Spectral.rotorRadius = config.get("Settings Spectral rotor", "Radius", 11).getInt(11);
//
			MFSU.Advanced.transferLimit = config.get("Settings Advanced mfsu", "output", 16384).getInt(16384);
			MFSU.Advanced.tier = config.get("Settings Advanced mfsu", "tier ", 5).getInt(5);
			MFSU.Advanced.maxCharge = config.get("Settings Advanced mfsu", "maxCharge ", 100000000).getInt(100000000);
			MFSU.Ultimate.transferLimit = config.get("Settings Ultimate mfsu", "output", 65536).getInt(65536);
			MFSU.Ultimate.tier = config.get("Settings Ultimate mfsu", "tier", 6).getInt(6);
			MFSU.Ultimate.maxCharge = config.get("Settings Ultimate mfsu", "maxCharge ", 400000000).getInt(400000000);
			MFSU.Quantum.transferLimit = config.get("Settings Quantum mfsu", "output ", 262144).getInt(262144);
			MFSU.Quantum.tier = config.get("Settings Quantum mfsu", "tier", 7).getInt(7);
			MFSU.Quantum.maxCharge = config.get("Settings Quantum mfsu", "maxCharge", 1600000000).getInt(1600000000);
//
			Drill.Spectral.maxCharge = config.get("Settings Spectral Drill", "MaxCharge ", 75000).getInt(75000);
			Drill.Spectral.transferLimit = config.get("Settings Spectral Drill", " transferLimit ", 5000).getInt(5000);
			Drill.Spectral.tier = config.get("Settings Spectral Drill", "tier", 2).getInt(2);
			//
			Fabricator.Neutron.cost1mb = config.get("Settings Neutron fabricator", "1 mb = 15625000.0F (default)", 15625000.0F).getInt((int) 15625000.0F);
			Fabricator.Neutron.tier = config.get("Settings Neutron fabricator", "tier", 14).getInt(14);

			//
			Drill.Mode.Normal.speed = config.get("Settings Perfect Drill(Normal Mode)", "drillSpeed", 35).getInt(35);
			Drill.Mode.BigHoles.speed = config.get("Settings Perfect Drill(BIG_HOLES(3x3) Mode)", "drillSpeed", 12).getInt(12);
			Drill.Mode.BigBigHoles.speed = config.get("Settings Perfect Drill(BIG_HOLES(5x5) Mode)", "drillSpeed ", 16).getInt(16);
			Drill.Mode.Normal.cost = config.get("Settings Perfect Drill(Normal Mode)", "energyCost", 160).getInt(160);
			Drill.Mode.BigHoles.cost = config.get("Settings Perfect Drill(BIG_HOLES(3x3) Mode)", "energyCost", 160).getInt(160);
			Drill.Mode.BigBigHoles.cost = config.get("Settings Perfect Drill(BIG_HOLES(5x5) Mode)", "energyCost", 160).getInt(160);

			//
		} catch (Exception e) {
			SuperSolarPanels.log.fatal("Fatal error reading config file.", e);
			throw new RuntimeException(e);
		} finally {
			if (config.hasChanged()) {
				config.save();
			}
		}
	}

	public enum Panel {
		Advanced, Hybrid, Ultimate, Quantum, Spectral, Proton, Singular, LightAbsord, Photonic, Neutron;

		public static TileEntitySolarPanel.SolarConfig[] solar = new TileEntitySolarPanel.SolarConfig[ values().length ];
		public static TileEntityRainPanel.SolarConfig[] rain = new TileEntityRainPanel.SolarConfig[ values().length ];
		public static TileEntitySunPanel.SolarConfig[] sun = new TileEntitySunPanel.SolarConfig[ values().length ];
	}

	public static class Electric {
		static int tier;

		public static int getTier() {
			return tier;
		}

		public static class ElectricItem extends Electric {

			static int transferLimit;
			static int maxCharge;

			public static int getTransferLimit() {
				return transferLimit;
			}

			public static int getMaxCharge() {
				return maxCharge;
			}

		}
	}

	public static class Permitting {
		static boolean canCraftDoubleSlabs;
		static boolean canCraftMT;
		static boolean canCraftASP;
		static boolean canCraftHSP;
		static boolean canCraftASH;
		static boolean canCraftHSH;

		public static boolean isCanCraftDoubleSlabs() {
			return canCraftDoubleSlabs;
		}

		public static boolean isCanCraftMT() {
			return canCraftMT;
		}

		public static boolean isCanCraftASP() {
			return canCraftASP;
		}

		public static boolean isCanCraftHSP() {
			return canCraftHSP;
		}

		public static boolean isCanCraftASH() {
			return canCraftASH;
		}

		public static boolean isCanCraftHSH() {
			return canCraftHSH;
		}
	}

	public static class Weapon {
		public static class Saber extends Electric.ElectricItem {
			static int damageInactive;
			static int damageActive;

			public static int getDamageInactive() {
				return damageInactive;
			}

			public static int getDamageActive() {
				return damageActive;
			}

			public static class Quantum extends Saber {
			}

			public static class Spectral extends Saber {
			}
		}
	}

	public static class Armor {
		public static class helmet extends Electric.ElectricItem {
		}

		public static class chestplate extends Electric.ElectricItem {
		}

		public static class leggings extends Electric.ElectricItem {
		}

		public static class boots extends Electric.ElectricItem {
		}

		public static class Quantum extends Armor {
		}
	}

	public static class Battery extends Electric {
		static int transferLimit;
		static int tier;
		static int maxCharge;

		public static int getTransferLimit() {
			return transferLimit;
		}

		public static int getTier() {
			return tier;
		}

		public static int getMaxCharge() {
			return maxCharge;
		}

		public static class Spectral extends Battery {
		}
	}

	public static class Rod {
		static int storage;

		public static int getStorage() {
			return storage;
		}

		public static class CoolingRod extends Rod {
			public static class Twelve extends CoolingRod {
			}

			public static class Max extends CoolingRod {
			}
		}

		public static class FuelRod extends Rod {
			public static class Proton extends FuelRod {
			}
		}
	}

	public static class Rotor extends Electric.ElectricItem {
		static int durability;
		static int efficient;
		static int maxWind;
		static int minWind;
		static int rotorRadius;

		public static int getDurability() {
			return durability;
		}

		public static int getEfficient() {
			return efficient;
		}

		public static int getMaxWind() {
			return maxWind;
		}

		public static int getMinWind() {
			return minWind;
		}

		public static int getRotorRadius() {
			return rotorRadius;
		}

		public static class Iridium extends Rotor {
		}

		public static class CompressIridium extends Rotor {
		}

		public static class Spectral extends Rotor {
		}
	}

	public static class Drill extends Electric.ElectricItem {
		public static class Mode {
			static int speed;
			static int cost;

			public static int getSpeed() {
				return speed;
			}

			public static int getCost() {
				return cost;
			}

			public static class Normal extends Mode {
			}

			public static class BigHoles extends Mode {
			}

			public static class BigBigHoles extends Mode {
			}
		}

		public static class Spectral extends Drill {
		}
	}

	public static class Fabricator extends Electric {
		static int cost1mb;

		public static int getCost1mb() {
			return cost1mb;
		}

		public static class Neutron extends Fabricator {
		}
	}

	public static MTRecipe[] MTRecipes;

	public static void loadConfig(File config, boolean client) {
		SuperSolarPanels.log.info("Loading SSP Config from " + config.getAbsolutePath());
		loadNormalConfig(config, client);
		try {
			loadMolecularTransformerConfig(config.getParentFile(), config.getName());
		} catch (ParseException e) {
			MolecularTransformerRecipeManager.showError("Error reading Molecular Transformer recipes file:" + NEW_LINE + e.toString());
		}
	}

	public static class MFSU extends Electric.ElectricItem {
		public static class Advanced extends MFSU {
		}

		public static class Ultimate extends MFSU {
		}

		public static class Quantum extends MFSU {
		}
	}

	private static void loadMolecularTransformerConfig(File configFolder, String configFile) throws ParseException {
		int fileExtensionMarker = configFile.lastIndexOf('.');
		File config = new File(configFolder, configFile.substring(0, fileExtensionMarker) + "_MTRecipes" + configFile.substring(fileExtensionMarker));
		SuperSolarPanels.log.info("Loading MT Recipes from " + config.getAbsolutePath());
		if (!config.exists())
			fillDefault(config);

		List<MTRecipe> recipes = new ArrayList<>(20);
		try (FileInputStream stream = new FileInputStream(config);
		     BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			int lineNumber = 0;
			String line;
			while ((line = reader.readLine()) != null) {
				lineNumber++;
				line = line.trim();
				if (line.startsWith("#") || line.isEmpty())
					continue;
				if (line.startsWith("version=")) {
					String version = line.substring(line.indexOf('=') + 1);
					if (!"2.0".equals(version))
						throw new ParseException("Advanced Solars expected a file version of 2.0, but the config is " + version, line.indexOf('=') + 1);
					continue;
				}
				MTRecipe recipe = new MTRecipe(lineNumber, line);
				if (recipe.isValid()) {
					recipes.add(recipe);
					continue;
				}
				SuperSolarPanels.log.warn("Skipping line {} as it is has the wrong format (expected length 3, found {})", lineNumber, recipe.parts.length);
			}
		} catch (IOException e) {
			SuperSolarPanels.log.fatal("RIP MT Config!", e);
			throw new RuntimeException("Fatal error reading Molecular Transformer recipe file", e);
		}
		MTRecipes = recipes.toArray(new MTRecipe[0]);
	}

	private static void fillDefault(File config) {
		try {
			if (!config.createNewFile()) {
				throw new IOException("Couldn't create config file");
			}
		} catch (IOException e) {
			SuperSolarPanels.log.fatal("RIP MT Config!", e);
			throw new RuntimeException("Fatal error writing Molecular Transformer recipe file", e);
		}

		try (FileOutputStream stream = new FileOutputStream(config);
		     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream))) {
			write(writer, "##################################################################################################");
			write(writer, "#                        AdvancedSolarPanels Molecular Transformer Recipes                       #");
			write(writer, "##################################################################################################");
			write(writer, "# Format of recipe: \"inputItem*stackSize;outputItem*outputStackSize;energy\"                      #");
			write(writer, "# InputItem (and outputItem) format:                                                             #");
			write(writer, "# \"OreDict:forgeOreDictName\" or \"minecraft:item_name@meta\" or \"modID:item_name@meta\"             #");
			write(writer, "# New line = new recipe.                                                                         #");
			write(writer, "# Add \"#\" before line to skip parsing line/recipe                                                #");
			write(writer, "##################################################################################################");
			writer.write("version=2.0" + NEW_LINE);
			write(writer, "##################################################################################################");
			write(writer, "minecraft:skull@1; minecraft:nether_star; 280000000");
			write(writer, "minecraft:iron_ingot@*; ic2:misc_resource#iridium_ore; 18000000");
			write(writer, "minecraft:netherrack@*; minecraft:gunpowder*2; 80000");
			write(writer, "minecraft:sand@*; minecraft:gravel; 30000");
			write(writer, "minecraft:dirt@*; minecraft:clay; 30000");
			write(writer, "minecraft:coal@1; minecraft:coal@0; 40000");
			write(writer, "minecraft:glowstone_dust@*; super_solar_panels:crafting@2; 800000");
			write(writer, "minecraft:glowstone@*; super_solar_panels:crafting@1; 4500000");
			write(writer, "minecraft:wool@4; minecraft:glowstone; 350000");
			write(writer, "minecraft:wool@11; minecraft:lapis_block; 350000");
			write(writer, "minecraft:wool@14; minecraft:redstone_block; 350000");
			write(writer, "minecraft:dye@4; OreDict:gemSapphire; 4000000");
			write(writer, "minecraft:redstone@*; OreDict:gemRuby; 4000000");
			write(writer, "minecraft:coal@0; ic2:crafting#industrial_diamond; 7000000");
			write(writer, "ic2:crafting#industrial_diamond; minecraft:diamond; 800000");
			write(writer, "OreDict:dustTitanium; OreDict:dustChrome; 300000");
			write(writer, "OreDict:ingotTitanium; OreDict:ingotChrome; 300000");
			write(writer, "OreDict:gemNetherQuartz; OreDict:gemCertusQuartz; 300000");
			write(writer, "OreDict:ingotCopper; OreDict:ingotNickel; 150000");
			write(writer, "OreDict:ingotTin; OreDict:ingotSilver; 350000");
			write(writer, "OreDict:ingotSilver; OreDict:ingotGold; 350000");
			write(writer, "OreDict:ingotGold; OreDict:ingotPlatinum; 4000000");
			write(writer, "super_solar_panels:crafting@5; super_solar_panels:crafting@48; 15000000");
			write(writer, "super_solar_panels:crafting@6; super_solar_panels:crafting@49; 15000000");
			write(writer, "ic2:misc_resource#iridium_ore; super_solar_panels:crafting@52; 25000000");
		} catch (IOException e) {
			SuperSolarPanels.log.fatal("RIP MT Config!", e);
			throw new RuntimeException("Fatal error writing Molecular Transformer recipe file", e);
		}
	}

	private static void write(BufferedWriter writer, String line) throws IOException {
		writer.write(line);
		writer.newLine();
	}
}
