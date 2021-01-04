package com.denfop.ssp.common;

import com.denfop.ssp.SuperSolarPanels;
import com.denfop.ssp.items.battery.ItemBattery;
import com.denfop.ssp.molecular.MTRecipe;
import com.denfop.ssp.molecular.MolecularTransformerRecipeManager;
import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;
import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;
import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;
import com.denfop.ssp.tiles.panels.overtime.*;
import com.denfop.ssp.tiles.panels.rain.*;
import com.denfop.ssp.tiles.panels.sun.*;
import net.minecraftforge.common.config.Configuration;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public final class Configs {
	private static final String GENERAL = "general";
	private static final String SOLARS = "solars";
	private static final String QUANTUM_GENERATOR = "quantum generator";
	private static final String CRAFTING = "recipes settings";
	private static final String NEW_LINE;
	private static final String CONFIG_VERSION = "2.0";
	public static boolean canCraftDoubleSlabs;
	public static boolean canCraftMT;
	public static boolean canCraftASP;
	public static boolean canCraftHSP;
	public static boolean canCraftUHSP;
	public static boolean canCraftQSP;
	public static boolean canCraftASH;
	public static boolean canCraftHSH;
	public static boolean canCraftUHSH;
	public static int damage1;
	public static int damage2;
	public static int damage3;
	public static int damage4;
	public static int maxCharge1;
	public static int maxCharge2;
	public static int transferLimit1;
	public static int transferLimit2;
	public static int tier1;
	public static int tier2;
	public static ItemBattery settings;
	public static ItemBattery settings1;
	public static int maxCharge3;
	public static int maxCharge4;
	public static int maxCharge5;
	public static int maxCharge6;
	public static int transferLimit3;
	public static int transferLimit4;
	public static int transferLimit5;
	public static int transferLimit6;
	public static int tier3;
	public static int tier4;
	public static int tier5;
	public static int tier6;
	public static int maxCharge7;
	public static int transferLimit8;
	public static int tier8;
	public static int maxCharge8;
	public static int twelve_heat_storage;
	public static int max_heat_storage;
	public static int maxCharge9;
	public static int maxCharge10;
	public static int maxCharge11;
	public static int transferLimit9;
	public static int transferLimit10;
	public static int transferLimit11;
	public static int proton_fuel_rod;
	public static int rotor_carbon1;
	public static int rotor_carbon2;
	public static int rotor_carbon3;
	public static int coefficient;
	public static int coefficient1;
	public static int coefficient2;
	public static int operationEnergyCost;
	public static int maxChargedrill;
	public static int transferLimitdrill;
	public static int tierdrill;
	public static int drillSpeed;
	public static int drillSpeed1;
	public static int drillSpeed2;
	public static int energyCost;
	public static int energyCost1;
	public static int energyCost2;
	public static int maxWindStrength;
	public static int maxWindStrength1;
	public static int maxWindStrength2;
	public static int minWindStrength;
	public static int minWindStrength1;
	public static int minWindStrength2;
	public static int iridiumRotorRadius;
	public static int compressIridiumRotorRadius;
	public static int spectralRotorRadius;
	public static int Neutronfabricator;
	public static int Neutronfabricator1;
	public static int advancedmfsu;
	public static int advancedmfsu1;
	public static int advancedmfsu2;
	public static int ultimatemfsu;
	public static int ultimatemfsu1;
	public static int ultimatemfsu2;
	public static int quantummfsu;
	public static int quantummfsu1;
	public static int quantummfsu2;
	public static int terrasteel_fuel_rod;
	static boolean hardRecipes;
	static boolean easyASPRecipe;
	public static MTRecipe[] MTRecipes;

	static {
		NEW_LINE = System.getProperty("line.separator");
	}

	public static void loadConfig(File config, boolean client) {
		SuperSolarPanels.log.info("Loading SSP Config from " + config.getAbsolutePath());
		loadNormalConfig(config, client);
		try {
			loadMolecularTransformerConfig(config.getParentFile(), config.getName());
		} catch (ParseException e) {
			MolecularTransformerRecipeManager.showError("Error reading Molecular Transformer recipes file:" + NEW_LINE + e.toString());
		}
	}

	private static void loadNormalConfig(final File configFile, final boolean client) {
		final Configuration config = new Configuration(configFile);
		try {
			config.load();
			TileEntityNeutronSolar.settings = new TileEntitySolarPanel.SolarConfig(config.get("Neutron solar panels", "NeutronGenDay", 10485760).getInt(10485760), config.get("Neutron solar panels", "NeutronGenNight", 10485760).getInt(10485760), config.get("Neutron solar panels", "NeutronStorage", 800000000).getInt(800000000), config.get("Neutron solar panels", "NeutronTier", 10).getInt(10));
			TileEntityProtonSolar.settings = new TileEntitySolarPanel.SolarConfig(config.get("Proton solar panels", "ProtonGenDay", 32768).getInt(32768), config.get("Proton solar panels", "ProtonGenNight", 20000).getInt(20000), config.get("Proton solar panels", "ProtonStorage", 999999999).getInt(999999999), config.get("Proton solar panels", "ProtonTier", 6).getInt(6));
			TileEntityAdvancedSolar.settings = new TileEntitySolarPanel.SolarConfig(config.get("Advanced solar panels", "AdvancedSPGenDay", 4).getInt(4), config.get("Advanced solar panels", "AdvancedSPGenNight", 4).getInt(4), config.get("Advanced solar panels", "AdvancedSPStorage", 16000).getInt(16000), config.get("Advanced solar panels", "AdvancedSPTier", 1).getInt(1));
			TileEntityHybridSolar.settings = new TileEntitySolarPanel.SolarConfig(config.get("Hybrid solar panels", "HybrydSPGenDay", 32).getInt(32), config.get("Hybrid solar panels", "HybrydSPGenNight", 8).getInt(8), config.get("Hybrid solar panels", "HybrydSPStorage", 50000).getInt(50000), config.get("Hybrid solar panels", "HybrydSPTier", 2).getInt(2));
			TileEntityUltimateHybridSolar.settings = new TileEntitySolarPanel.SolarConfig(config.get("Ultimate solar panels", "UltimateHSPGenDay", 256).getInt(256), config.get("Ultimate solar panels", "UltimateHSPGenNight", 64).getInt(64), config.get("Ultimate solar panels", "UltimateHSPStorage", 500000).getInt(500000), config.get("Ultimate solar panels", "UltimateHSPTier", 3).getInt(3));
			TileEntityQuantumSolar.settings = new TileEntitySolarPanel.SolarConfig(config.get("Quantum solar panels", "QuantumSPGenDay", 2048).getInt(2048), config.get("Quantum solar panels", "QuantumSPGenNight", 1024).getInt(1024), config.get("Quantum solar panels", "QuantumSPStorage", 5000000).getInt(5000000), config.get("Quantum solar panels", "QuantumSPTier", 4).getInt(4));

			TileEntitySpectralSolar.settings = new TileEntitySolarPanel.SolarConfig(config.get("Spectral solar panels", "SpecrtalGenDay", 8192).getInt(8192), config.get("Spectral solar panels", "SpecrtalGenNight", 5000).getInt(5000), config.get("Spectral solar panels", "SpecrtalStorage", 10000000).getInt(10000000), config.get("Spectral solar panels", "SpecrtalTier", 5).getInt(5));
			TileEntitySingularSolar.settings = new TileEntitySolarPanel.SolarConfig(config.get("Singular solar panels", "SingularGenDay", 131072).getInt(131072), config.get("Singular solar panels", "SingularGenNight", 104857).getInt(104857), config.get("Singular solar panels", "SingularStorage", 50000000).getInt(50000000), config.get("Singular solar panels", "SingularTier", 7).getInt(7));
			TileEntityAdminSolar.settings = new TileEntitySolarPanel.SolarConfig(config.get("Light-Absord solar panels", "Light-AbsordGenDay", 554288).getInt(554288), config.get("Light-Absord solar panels", "Light-AbsordGenNight", 554288).getInt(554288), config.get("Light-Absord solar panels", "Light-AbsordStorage", 100000000).getInt(100000000), config.get("Light-Absord solar panels", "Light-AbsordTier", 8).getInt(8));
			TileEntityPhotonicSolar.settings = new TileEntitySolarPanel.SolarConfig(config.get("Photonic solar panels", "PhotonicGenDay", 2621440).getInt(2621440), config.get("Photonic solar panels", "PhotonicGenNight", 2621440).getInt(2621440), config.get("Photonic solar panels", "PhotonicStorage", 400000000).getInt(400000000), config.get("Photonic solar panels", "PhotonicTier", 9).getInt(9));


			TileEntityNeutronRain.settings = new TileEntityRainPanel.SolarConfig(config.get("Neutron Rain panels", "NeutronGenRain", 10485760).getInt(10485760), config.get("Neutron Rain panels", "NeutronStorage", 800000000).getInt(800000000), config.get("Neutron Rain panels", "NeutronTier", 10).getInt(10));
			TileEntityProtonRain.settings = new TileEntityRainPanel.SolarConfig(config.get("Proton Rain panels", "ProtonGenRain", 20000).getInt(20000), config.get("Proton solar Rain", "ProtonStorage", 999999999).getInt(999999999), config.get("Proton Rain panels", "ProtonTier", 6).getInt(6));
			TileEntityAdvancedSolarRain.settings = new TileEntityRainPanel.SolarConfig(config.get("Advanced Rain panels", "AdvancedSPGenRain", 4).getInt(4), config.get("Advanced Rain panels", "AdvancedSPStorage", 16000).getInt(16000), config.get("Advanced Rain panels", "AdvancedSPTier", 1).getInt(1));
			TileEntityHybridSolarrain.settings = new TileEntityRainPanel.SolarConfig(config.get("Hybrid Rain panels", "HybrydSPGenRain", 8).getInt(8), config.get("Hybrid Rain panels", "HybrydSPStorage", 50000).getInt(50000), config.get("Hybrid Rain panels", "HybrydSPTier", 2).getInt(2));
			TileEntityUltimateHybridSolarRain.settings = new TileEntityRainPanel.SolarConfig(config.get("Ultimate Rain panels", "UltimateHSPGenRain", 64).getInt(64), config.get("Ultimate Rain panels", "UltimateHSPStorage", 500000).getInt(500000), config.get("Ultimate Rain panels", "UltimateHSPTier", 3).getInt(3));
			TileEntityQuantumSolarRain.settings = new TileEntityRainPanel.SolarConfig(config.get("Quantum Rain panels", "QuantumSPGenRain", 1024).getInt(1024), config.get("Quantum Rain panels", "QuantumSPStorage", 5000000).getInt(5000000), config.get("Quantum Rain panels", "QuantumSPTier", 4).getInt(4));
			TileEntitySpectralRain.settings = new TileEntityRainPanel.SolarConfig(config.get("Spectral Rain panels", "SpecrtalGenRain", 5000).getInt(5000), config.get("Spectral solar panels", "SpecrtalStorage", 10000000).getInt(10000000), config.get("Spectral solar panels", "SpecrtalTier", 5).getInt(5));
			TileEntitySingularrain.settings = new TileEntityRainPanel.SolarConfig(config.get("Singular Rain panels", "SingularGenRain", 104857).getInt(104857), config.get("Singular solar panels", "SingularStorage", 50000000).getInt(50000000), config.get("Singular solar panels", "SingularTier", 7).getInt(7));
			TileEntityAdminRain.settings = new TileEntityRainPanel.SolarConfig(config.get("Light-Absord Rain panels", "Light-AbsordGenRain", 554288).getInt(554288), config.get("Light-Absord solar panels", "Light-AbsordStorage", 100000000).getInt(100000000), config.get("Light-Absord solar panels", "Light-AbsordTier", 8).getInt(8));
			TileEntityPhotonicRain.settings = new TileEntityRainPanel.SolarConfig(config.get("Photonic Rain panels", "PhotonicGenRain", 2621440).getInt(2621440), config.get("Photonic solar panels", "PhotonicStorage", 400000000).getInt(400000000), config.get("Photonic solar panels", "PhotonicTier", 9).getInt(9));

			TileEntityNeutronSun.settings = new TileEntitySunPanel.SolarConfig(config.get("Neutron Sun panels", "NeutronGenSun", 20971520).getInt(20971520), config.get("Neutron Sun panels", "NeutronStorage", 800000000).getInt(800000000), config.get("Neutron Sun panels", "NeutronTier", 10).getInt(10));
			TileEntityProtonSun.settings = new TileEntitySunPanel.SolarConfig(config.get("Proton Sun panels", "ProtonGenSun", 65536).getInt(65536), config.get("Proton Sun panels", "ProtonStorage", 999999999).getInt(999999999), config.get("Proton Sun panels", "ProtonTier", 6).getInt(6));
			TileEntityAdvancedSolarSun.settings = new TileEntitySunPanel.SolarConfig(config.get("Advanced Sun panels", "AdvancedSPGenSun", 8).getInt(8), config.get("Advanced Sun panels", "AdvancedSPStorage", 16000).getInt(16000), config.get("Advanced Sun panels", "AdvancedSPTier", 1).getInt(1));
			TileEntityHybridSolarSun.settings = new TileEntitySunPanel.SolarConfig(config.get("Hybrid Sun panels", "HybrydSPGenSun", 64).getInt(64), config.get("Hybrid Sun panels", "HybrydSPStorage", 50000).getInt(50000), config.get("Hybrid Sun panels", "HybrydSPTier", 2).getInt(2));
			TileEntityUltimateHybridSolarSun.settings = new TileEntitySunPanel.SolarConfig(config.get("Ultimate Sun panels", "UltimateHSPGenSun", 512).getInt(512), config.get("Ultimate Sun panels", "UltimateHSPStorage", 500000).getInt(500000), config.get("Ultimate Sun panels", "UltimateHSPTier", 3).getInt(3));
			TileEntityQuantumSolarSun.settings = new TileEntitySunPanel.SolarConfig(config.get("Quantum Sun panels", "QuantumSPGenSun", 4096).getInt(4096), config.get("Quantum Sun panels", "QuantumSPStorage", 5000000).getInt(5000000), config.get("Quantum Sun panels", "QuantumSPTier", 4).getInt(4));
			TileEntitySpectralSun.settings = new TileEntitySunPanel.SolarConfig(config.get("Spectral Sun panels", "SpecrtalGenSun", 16384).getInt(16384), config.get("Spectral Sun panels", "SpecrtalStorage", 10000000).getInt(10000000), config.get("Spectral Sun panels", "SpecrtalTier", 5).getInt(5));
			TileEntitySingularSun.settings = new TileEntitySunPanel.SolarConfig(config.get("Singular Sun panels", "SingularGenSun", 262144).getInt(262144), config.get("Singular Sun panels", "SingularStorage", 50000000).getInt(50000000), config.get("Singular Sun panels", "SingularTier", 7).getInt(7));
			TileEntityAdminSun.settings = new TileEntitySunPanel.SolarConfig(config.get("Light-Absord Sun panels", "Light-AbsordGenSun", 1108576).getInt(1108576), config.get("Light-Absord Sun panels", "Light-AbsordStorage", 100000000).getInt(100000000), config.get("Light-Absord Sun panels", "Light-AbsordTier", 8).getInt(8));
			TileEntityPhotonicSun.settings = new TileEntitySunPanel.SolarConfig(config.get("Photonic Sun panels", "PhotonicGenSun", 5242880).getInt(5242880), config.get("Photonic Sun panels", "PhotonicStorage", 400000000).getInt(400000000), config.get("Photonic Sun panels", "PhotonicTier", 9).getInt(9));

			//  canCraftMT = !config.get("settings Quantum Boosts", "Disable Effect WATER BREATHING ", false).getBoolean(false);
			damage1 = config.get("Settings quantum saber", "Damage quantum saber(not right click)+1 ", 11).getInt(11);
			damage2 = config.get("Settings quantum saber", "Damage quantum saber(right click)+1", 29).getInt(29);
			damage3 = config.get("Settings spectral saber", "Damage quantum saber(not right click)+1 ", 14).getInt(14);
			damage4 = config.get("Settings spectral saber", "Damage quantum saber(right click)+1 ", 39).getInt(39);
			maxCharge1 = config.get("Settings quantum saber", "maxCharge", 300000).getInt(300000);
			maxCharge2 = config.get("Settings spectral saber", "maxCharge", 600000).getInt(600000);
			transferLimit1 = config.get("Settings quantum saber", "transferLimit", 2000).getInt(2000);
			transferLimit2 = config.get("Settings spectral saber", "transferLimit ", 2000).getInt(2000);
			tier1 = config.get("Settings quantum saber", "tier ", 4).getInt(4);
			tier2 = config.get("Settings spectral saber", "tier", 5).getInt(5);
			//
			maxCharge3 = config.get("Settings Quantum Boosts", "maxCharge", 100000000).getInt(100000000);
			maxCharge4 = config.get("Settings Quantum Helmet", "maxCharge", 100000000).getInt(100000000);
			maxCharge5 = config.get("Settings Quantum Leggings", "maxCharge", 100000000).getInt(100000000);
			maxCharge6 = config.get("Settings Quantum Chestplate", "maxCharge ", 100000000).getInt(100000000);
			maxCharge1 = config.get("Settings quantum saber", "maxCharge", 300000).getInt(300000);
			maxCharge2 = config.get("Settings spectral saber", "maxCharge", 600000).getInt(600000);
			transferLimit3 = config.get("Settings Quantum Boosts", "transferLimit", 100000).getInt(100000);
			transferLimit4 = config.get("Settings Quantum Helmet", "transferLimit ", 100000).getInt(100000);
			transferLimit5 = config.get("Settings Quantum Leggings", "transferLimit ", 100000).getInt(100000);
			transferLimit6 = config.get("Settings Quantum Chestplater", "transferLimit", 100000).getInt(100000);
			canCraftDoubleSlabs = !config.get("settings Quantum chestplate", "Disable Effect FIRE RESISTANCE ", false).getBoolean(false);
			canCraftMT = !config.get("settings Quantum Boosts", "Disable Effect WATER BREATHING ", false).getBoolean(false);
			canCraftASP = !config.get("settings Quantum Boosts", "Disable Effect JUMP BOOST", false).getBoolean(false);
			canCraftASH = !config.get("settings Quantum Boosts", "Disable Effect REGENERATION ", false).getBoolean(false);
			canCraftHSP = !config.get("settings Quantum Leggins", "Disable Effect SPEED ", false).getBoolean(false);
			canCraftHSH = !config.get("settings Quantum Leggins", "Disable Effect LUCK", false).getBoolean(false);
			//
			tier3 = config.get("Settings Quantum Boosts", "tier", 8).getInt(8);
			tier4 = config.get("Settings Quantum Helmet", "tier ", 8).getInt(8);
			tier5 = config.get("Settings Quantum Leggings", "tier ", 8).getInt(8);
			tier6 = config.get("Settings Quantum Chestplater", "tier", 8).getInt(8);
			//
			terrasteel_fuel_rod = config.get("Settings terrasteel fuel rods", "maxDamage(times, 1=1 seconds) ", 15000).getInt(15000);
			maxCharge8 = config.get("Settings Spectral Battery", "maxCharge", 100000000).getInt(100000000);
			twelve_heat_storage = config.get("Settings twelve heat storage", "heatStorage", 120000).getInt(120000);
			max_heat_storage = config.get("Settings max heat storage", "heatStorage", 240000).getInt(240000);
			proton_fuel_rod = config.get("Settings proton fuel rods", "maxDamage(times, 1=1 seconds) ", 30000).getInt(30000);
			maxCharge10 = config.get("Settings Advanced Solar Nano Helmet", "maxCharge", 300000).getInt(300000);
			maxCharge11 = config.get("Settings Ultimate and Hybrid Solar Quantum Helmet", "maxCharge", 600000).getInt(600000);
			transferLimit8 = config.get("Settings Spectral Battery", "transferLimit", 30000).getInt(30000);
			coefficient = config.get("Settings Iridium rotor", "efficiency ", 2.0F).getInt((int) 2.0F);
			coefficient1 = config.get("Settings Compress iridium rotor", "efficiency ", 3.0F).getInt((int) 3.0F);
			coefficient2 = config.get("Settings Spectral rotor", "efficiency", 5.0F).getInt((int) 5.0F);
			tier8 = config.get("Settings Spectral Battery", "tier", 4).getInt(4);
			rotor_carbon1 = config.get("Settings Iridium rotor", "durability(times, 1=1 seconds) ", 648000).getInt(648000);
			rotor_carbon2 = config.get("Settings Compress iridium rotor", " durability(times, 1=1 seconds)", 720000).getInt(720000);
			rotor_carbon3 = config.get("Settings Spectral rotor", "durability(times, 1=1 seconds)", 720000).getInt(720000);
			maxWindStrength = config.get("Settings Iridium rotor", "maxWindStrength", 110).getInt(110);
			maxWindStrength1 = config.get("Settings Compress iridium rotor", "maxWindStrength", 110).getInt(110);
			maxWindStrength2 = config.get("Settings Spectral rotor", "maxWindStrength", 110).getInt(110);
			minWindStrength = config.get("Settings Iridium rotor", "minWindStrength", 25).getInt(25);
			minWindStrength1 = config.get("Settings Compress iridium rotor", "minWindStrength", 25).getInt(25);
			minWindStrength2 = config.get("Settings Spectral rotor", "minWindStrength", 25).getInt(25);
			iridiumRotorRadius = config.get("Settings Iridium rotor", "Radius", 11).getInt(11);
			compressIridiumRotorRadius = config.get("Settings Compress iridium rotor", "Radius", 11).getInt(11);
			spectralRotorRadius = config.get("Settings Spectral rotor", "Radius", 11).getInt(11);
//
			advancedmfsu = config.get("Settings Advanced mfsu", "output", 16384).getInt(16384);
			advancedmfsu1 = config.get("Settings Advanced mfsu", "tier ", 5).getInt(5);
			advancedmfsu2 = config.get("Settings Advanced mfsu", "maxCharge ", 100000000).getInt(100000000);
			ultimatemfsu = config.get("Settings Ultimate mfsu", "output", 65536).getInt(65536);
			ultimatemfsu1 = config.get("Settings Ultimate mfsu", "tier", 6).getInt(6);
			ultimatemfsu2 = config.get("Settings Ultimate mfsu", "maxCharge ", 400000000).getInt(400000000);
			quantummfsu = config.get("Settings Quantum mfsu", "output ", 262144).getInt(262144);
			quantummfsu1 = config.get("Settings Quantum mfsu", "tier", 7).getInt(7);
			quantummfsu2 = config.get("Settings Quantum mfsu", "maxCharge", 1600000000).getInt(1600000000);
//
			operationEnergyCost = config.get("Settings Spectral Drill", "operationEnergyCost", 160).getInt(160);
			maxChargedrill = config.get("Settings Spectral Drill", "MaxCharge ", 75000).getInt(75000);
			transferLimitdrill = config.get("Settings Spectral Drill", " transferLimit ", 5000).getInt(5000);
			tierdrill = config.get("Settings Spectral Drill", "tier", 2).getInt(2);
			//
			Neutronfabricator = config.get("Settings Neutron fabricator", "1 mb = 15625000.0F (default)", 15625000.0F).getInt((int) 15625000.0F);
			Neutronfabricator1 = config.get("Settings Neutron fabricator", "tier", 14).getInt(14);

			//
			drillSpeed = config.get("Settings Perfect Drill(Normal Mode)", "drillSpeed", 35).getInt(35);
			drillSpeed1 = config.get("Settings Perfect Drill(BIG_HOLES(3x3) Mode)", "drillSpeed", 12).getInt(12);
			drillSpeed2 = config.get("Settings Perfect Drill(BIG_HOLES(5x5) Mode)", "drillSpeed ", 16).getInt(16);
			energyCost = config.get("Settings Perfect Drill(Normal Mode)", "energyCost", 160).getInt(160);
			energyCost1 = config.get("Settings Perfect Drill(BIG_HOLES(3x3) Mode)", "energyCost", 160).getInt(160);
			energyCost2 = config.get("Settings Perfect Drill(BIG_HOLES(5x5) Mode)", "energyCost", 160).getInt(160);

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
