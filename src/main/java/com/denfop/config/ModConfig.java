package com.denfop.config;


import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModConfig {
    public static final Common COMMON;
    public static final ModConfigSpec COMMON_SPEC;


    private static class IntEntry {
        final String key;
        final int defaultValue;
        final int min;
        final int max;
        final String comment;

        IntEntry(String key, int defaultValue, int min, int max, String comment) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.min = min;
            this.max = max;
            this.comment = comment;
        }
    }

    private static class DoubleEntry {
        final String key;
        final double defaultValue;
        final double min;
        final double max;
        final String comment;

        DoubleEntry(String key, double defaultValue, double min, double max, String comment) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.min = min;
            this.max = max;
            this.comment = comment;
        }
    }

    private static final IntEntry[] MECHANISM_INT_ENTRIES = new IntEntry[]{
            new IntEntry("improved_combined_pump_operation_length", 30, 1, Integer.MAX_VALUE, "Improved Combined Pump: operation length (old value 30)"),
            new IntEntry("improved_combined_pump_radius_or_size", 80, 0, Integer.MAX_VALUE, "Improved Combined Pump: radius or size (old value 80)"),
            new IntEntry("improved_alloy_smelter_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Improved Alloy Smelter: energy per tick (old value 1)"),
            new IntEntry("improved_alloy_smelter_operation_length", 300, 1, Integer.MAX_VALUE, "Improved Alloy Smelter: operation length (old value 300)"),
            new IntEntry("improved_gas_cooling_reactor_regenerator_capacity", 7500, 1, Integer.MAX_VALUE, "Improved Gas-Cooling Reactor Regenerator: capacity (old value 7500)"),
            new IntEntry("improved_gas_cooling_reactor_socket_capacity", 20000, 1, Integer.MAX_VALUE, "Improved Gas-Cooling Reactor Socket: capacity (old value 20000)"),
            new IntEntry("improved_generator_energy_storage", 8000, 1, Integer.MAX_VALUE, "Improved Generator: energy storage (old value 8000)"),
            new IntEntry("improved_matter_fabricator_tank_capacity", 12, 1, Integer.MAX_VALUE, "Improved Matter Fabricator: tank capacity (old value 12)"),
            new IntEntry("improved_quantum_quarry_efficiency", 3, 0, Integer.MAX_VALUE, "Improved Quantum Quarry: efficiency (old value 3)"),
            new IntEntry("improved_scanner_operation_length", 2500, 1, Integer.MAX_VALUE, "Improved Scanner: operation length (old value 2500)"),
            new IntEntry("alloy_smelter_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Alloy Smelter: energy per tick (old value 1)"),
            new IntEntry("alloy_smelter_operation_length", 300, 1, Integer.MAX_VALUE, "Alloy Smelter: operation length (old value 300)"),
            new IntEntry("auto_crafter_energy_use", 1, 0, Integer.MAX_VALUE, "Auto Crafter: energy_use (old value 1)"),
            new IntEntry("excavator_chance", 0, 0, Integer.MAX_VALUE, "Excavator: chance (old value 0)"),
            new IntEntry("excavator_energy_use", 500, 0, Integer.MAX_VALUE, "Excavator: energy_use (old value 500)"),
            new IntEntry("auto_spawner_chance", 0, 0, Integer.MAX_VALUE, "Auto Spawner: chance (old value 0)"),
            new IntEntry("base_plastic_creator_tank_capacity", 12, 1, Integer.MAX_VALUE, "Base Plastic Creator: tank capacity (old value 12)"),
            new IntEntry("base_plastic_plate_creator_tank_capacity", 12, 1, Integer.MAX_VALUE, "Base Plastic Plate Creator: tank capacity (old value 12)"),
            new IntEntry("base_quantum_quarry_chance", 0, 0, Integer.MAX_VALUE, "Base Quantum Quarry: chance (old value 0)"),
            new IntEntry("base_world_collector_energy_use", 40, 0, Integer.MAX_VALUE, "Base World Collector: energy_use (old value 40)"),
            new IntEntry("electric_bio_generator_energy_use", 1, 0, Integer.MAX_VALUE, "Electric Bio-Generator: energy_use (old value 1)"),
            new IntEntry("fluid_solid_canning_machine_tank_capacity", 10, 1, Integer.MAX_VALUE, "Fluid/Solid Canning Machine: tank capacity (old value 10)"),
            new IntEntry("bee_product_centrifuge_energy_use", 1, 0, Integer.MAX_VALUE, "Bee Product Centrifuge: energy use (old value 1)"),
            new IntEntry("combined_matter_fabricator_tank_capacity", 12, 1, Integer.MAX_VALUE, "Combined Matter Fabricator: tank capacity (old value 12)"),
            new IntEntry("converter_solid_matter_energy_use", 2, 0, Integer.MAX_VALUE, "Converter Solid Matter: energy_use (old value 2)"),
            new IntEntry("cyclotron_chamber_chance", 100, 0, Integer.MAX_VALUE, "Cyclotron Chamber: chance (old value 100)"),
            new IntEntry("electric_latex_dryer_energy_use", 1, 0, Integer.MAX_VALUE, "Electric Latex Dryer: energy_use (old value 1)"),
            new IntEntry("electric_refractory_furnace_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Electric Refractory Furnace: energy per tick (old value 1)"),
            new IntEntry("electric_refractory_furnace_operation_length", 200, 1, Integer.MAX_VALUE, "Electric Refractory Furnace: operation length (old value 200)"),
            new IntEntry("electric_latex_extractor_energy_use", 1, 0, Integer.MAX_VALUE, "Electric Latex Extractor: energy_use (old value 1)"),
            new IntEntry("enrichment_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Enrichment: energy per tick (old value 1)"),
            new IntEntry("enrichment_operation_length", 300, 1, Integer.MAX_VALUE, "Enrichment: operation length (old value 300)"),
            new IntEntry("fishing_machine_energy_use", 100, 0, Integer.MAX_VALUE, "Fishing Machine: energy_use (old value 100)"),
            new IntEntry("fluid_adapter_energy_use", 1, 0, Integer.MAX_VALUE, "Fluid Adapter: energy_use (old value 1)"),
            new IntEntry("fluid_heater_energy_use", 1, 0, Integer.MAX_VALUE, "Fluid Heater: energy_use (old value 1)"),
            new IntEntry("fluid_integrator_energy_use", 1, 0, Integer.MAX_VALUE, "Fluid Integrator: energy_use (old value 1)"),
            new IntEntry("fluid_mixer_energy_use", 1, 0, Integer.MAX_VALUE, "Fluid Mixer: energy_use (old value 1)"),
            new IntEntry("fluid_separator_energy_use", 1, 0, Integer.MAX_VALUE, "Fluid Separator: energy_use (old value 1)"),
            new IntEntry("gas_mixer_energy_use", 1, 0, Integer.MAX_VALUE, "Gas Mixer: energy_use (old value 1)"),
            new IntEntry("gas_generator_tank_capacity", 24, 1, Integer.MAX_VALUE, "Gas Generator: tank capacity (old value 24)"),
            new IntEntry("gas_installation_tank_capacity", 20, 1, Integer.MAX_VALUE, "Gas Installation: tank capacity (old value 20)"),
            new IntEntry("gas_cooling_reactor_regenerator_capacity", 5000, 1, Integer.MAX_VALUE, "Gas-Cooling Reactor Regenerator: capacity (old value 5000)"),
            new IntEntry("gas_cooling_reactor_socket_capacity", 10000, 1, Integer.MAX_VALUE, "Gas-Cooling Reactor Socket: capacity (old value 10000)"),
            new IntEntry("expanded_stone_generator_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Expanded Stone Generator: energy per tick (old value 1)"),
            new IntEntry("expanded_stone_generator_operation_length", 100, 1, Integer.MAX_VALUE, "Expanded Stone Generator: operation length (old value 100)"),
            new IntEntry("biogenerator_tank_capacity", 12, 1, Integer.MAX_VALUE, "Biogenerator: tank capacity (old value 12)"),
            new IntEntry("diesel_generator_tank_capacity", 12, 1, Integer.MAX_VALUE, "Diesel Generator: tank capacity (old value 12)"),
            new IntEntry("hydrogen_generator_tank_capacity", 12, 1, Integer.MAX_VALUE, "Hydrogen Generator: tank capacity (old value 12)"),
            new IntEntry("obsidian_generator_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Obsidian Generator: energy per tick (old value 1)"),
            new IntEntry("obsidian_generator_operation_length", 300, 1, Integer.MAX_VALUE, "Obsidian Generator: operation length (old value 300)"),
            new IntEntry("gasoline_generator_tank_capacity", 12, 1, Integer.MAX_VALUE, "Gasoline Generator: tank capacity (old value 12)"),
            new IntEntry("stone_generator_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Stone Generator: energy per tick (old value 1)"),
            new IntEntry("stone_generator_operation_length", 100, 1, Integer.MAX_VALUE, "Stone Generator: operation length (old value 100)"),
            new IntEntry("solarite_transformer_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Solarite Transformer: energy per tick (old value 1)"),
            new IntEntry("solarite_transformer_operation_length", 300, 1, Integer.MAX_VALUE, "Solarite Transformer: operation length (old value 300)"),
            new IntEntry("wither_manufacturer_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Wither Manufacturer: energy per tick (old value 1)"),
            new IntEntry("wither_manufacturer_operation_length", 1500, 1, Integer.MAX_VALUE, "Wither Manufacturer: operation length (old value 1500)"),
            new IntEntry("generator_energy_storage", 4000, 1, Integer.MAX_VALUE, "Generator: energy storage (old value 4000)"),
            new IntEntry("matter_fabricator_tank_capacity", 10, 1, Integer.MAX_VALUE, "Matter Fabricator: tank capacity (old value 10)"),
            new IntEntry("circuit_manufacturer_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Circuit Manufacturer: energy per tick (old value 1)"),
            new IntEntry("circuit_manufacturer_operation_length", 300, 1, Integer.MAX_VALUE, "Circuit Manufacturer: operation length (old value 300)"),
            new IntEntry("genetic_polymerizer_tank_capacity", 12, 1, Integer.MAX_VALUE, "Genetic Polymerizer: tank capacity (old value 12)"),
            new IntEntry("genetic_replicator_energy_use", 1, 0, Integer.MAX_VALUE, "Genetic Replicator: energy_use (old value 1)"),
            new IntEntry("genetic_stabilize_energy_use", 1, 0, Integer.MAX_VALUE, "Genetic Stabilize: energy_use (old value 1)"),
            new IntEntry("genetic_transposer_tank_capacity", 12, 1, Integer.MAX_VALUE, "Genetic Transposer: tank capacity (old value 12)"),
            new IntEntry("improved_graphite_water_reactor_socket_capacity", 20000, 1, Integer.MAX_VALUE, "Improved Graphite-Water Reactor Socket: capacity (old value 20000)"),
            new IntEntry("advanced_graphite_water_reactor_socket_capacity", 40000, 1, Integer.MAX_VALUE, "Advanced Graphite-Water Reactor Socket: capacity (old value 40000)"),
            new IntEntry("perfect_graphite_water_reactor_socket_capacity", 80000, 1, Integer.MAX_VALUE, "Perfect Graphite-Water Reactor Socket: capacity (old value 80000)"),
            new IntEntry("graphite_water_reactor_socket_capacity", 10000, 1, Integer.MAX_VALUE, "Graphite-Water Reactor Socket: capacity (old value 10000)"),
            new IntEntry("improved_high_temperature_reactor_socket_capacity", 20000, 1, Integer.MAX_VALUE, "Improved High-Temperature Reactor Socket: capacity (old value 20000)"),
            new IntEntry("advanced_high_temperature_reactor_socket_capacity", 40000, 1, Integer.MAX_VALUE, "Advanced High-Temperature Reactor Socket: capacity (old value 40000)"),
            new IntEntry("perfect_high_temperature_reactor_socket_capacity", 80000, 1, Integer.MAX_VALUE, "Perfect High-Temperature Reactor Socket: capacity (old value 80000)"),
            new IntEntry("high_temperature_reactor_socket_capacity", 10000, 1, Integer.MAX_VALUE, "High-Temperature Reactor Socket: capacity (old value 10000)"),
            new IntEntry("advanced_combined_pump_operation_length", 20, 1, Integer.MAX_VALUE, "Advanced Combined Pump: operation length (old value 20)"),
            new IntEntry("advanced_combined_pump_radius_or_size", 160, 0, Integer.MAX_VALUE, "Advanced Combined Pump: radius or size (old value 160)"),
            new IntEntry("advanced_gas_cooling_reactor_regenerator_capacity", 1000, 1, Integer.MAX_VALUE, "Advanced Gas-Cooling Reactor Regenerator: capacity (old value 1000)"),
            new IntEntry("advanced_gas_cooling_reactor_socket_capacity", 40000, 1, Integer.MAX_VALUE, "Advanced Gas-Cooling Reactor Socket: capacity (old value 40000)"),
            new IntEntry("advanced_generator_energy_storage", 16000, 1, Integer.MAX_VALUE, "Advanced Generator: energy storage (old value 16000)"),
            new IntEntry("advanced_matter_fabricator_tank_capacity", 14, 1, Integer.MAX_VALUE, "Advanced Matter Fabricator: tank capacity (old value 14)"),
            new IntEntry("advanced_quantum_quarry_efficiency", 2, 0, Integer.MAX_VALUE, "Advanced Quantum Quarry: efficiency (old value 2)"),
            new IntEntry("advanced_scanner_operation_length", 2000, 1, Integer.MAX_VALUE, "Advanced Scanner: operation length (old value 2000)"),
            new IntEntry("incubator_energy_use", 1, 0, Integer.MAX_VALUE, "Incubator: energy_use (old value 1)"),
            new IntEntry("industrial_radioactive_element_purifier_energy_use", 1, 0, Integer.MAX_VALUE, "Industrial Radioactive Element Purifier: energy_use (old value 1)"),
            new IntEntry("insulator_energy_use", 1, 0, Integer.MAX_VALUE, "Insulator: energy_use (old value 1)"),
            new IntEntry("item_divider_energy_use", 1, 0, Integer.MAX_VALUE, "Item Divider: energy_use (old value 1)"),
            new IntEntry("item_divider_fluids_energy_use", 1, 0, Integer.MAX_VALUE, "Item Divider Fluids: energy_use (old value 1)"),
            new IntEntry("magnet_default_x_radius", 11, 1, 128, "Magnet default X radius"),
            new IntEntry("magnet_default_y_radius", 11, 1, 128, "Magnet default Y radius"),
            new IntEntry("magnet_default_z_radius", 11, 1, 128, "Magnet default Z radius"),
            new IntEntry("magnet_energy_per_item", 1000, 0, Integer.MAX_VALUE, "Magnet EF used when pulling one item stack"),
            new IntEntry("magnet_max_x_radius", 11, 1, 128, "Magnet maximum X radius"),
            new IntEntry("magnet_max_y_radius", 11, 1, 128, "Magnet maximum Y radius"),
            new IntEntry("magnet_max_z_radius", 11, 1, 128, "Magnet maximum Z radius"),
            new IntEntry("magnet_work_interval_ticks", 4, 1, 200, "Magnet work scan interval in ticks"),
            new IntEntry("mutatron_energy_use", 1, 0, Integer.MAX_VALUE, "Mutatron: energy_use (old value 1)"),
            new IntEntry("neutron_separator_energy_use", 50, 0, Integer.MAX_VALUE, "Neutron Separator: energy_use (old value 50)"),
            new IntEntry("oak_tank_tank_capacity", 4, 1, Integer.MAX_VALUE, "Oak Tank: tank capacity (old value 4)"),
            new IntEntry("oil_purifier_energy_use", 1, 0, Integer.MAX_VALUE, "Oil Purifier: energy_use (old value 1)"),
            new IntEntry("perfect_combined_pump_operation_length", 10, 1, Integer.MAX_VALUE, "Perfect Combined Pump: operation length (old value 10)"),
            new IntEntry("perfect_combined_pump_radius_or_size", 320, 0, Integer.MAX_VALUE, "Perfect Combined Pump: radius or size (old value 320)"),
            new IntEntry("painting_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Painting: energy per tick (old value 1)"),
            new IntEntry("painting_operation_length", 300, 1, Integer.MAX_VALUE, "Painting: operation length (old value 300)"),
            new IntEntry("perfect_gas_cooling_reactor_regenerator_capacity", 1500, 1, Integer.MAX_VALUE, "Perfect Gas-Cooling Reactor Regenerator: capacity (old value 1500)"),
            new IntEntry("perfect_gas_cooling_reactor_socket_capacity", 80000, 1, Integer.MAX_VALUE, "Perfect Gas-Cooling Reactor Socket: capacity (old value 80000)"),
            new IntEntry("perfect_generator_energy_storage", 32000, 1, Integer.MAX_VALUE, "Perfect Generator: energy storage (old value 32000)"),
            new IntEntry("perfect_matter_fabricator_tank_capacity", 16, 1, Integer.MAX_VALUE, "Perfect Matter Fabricator: tank capacity (old value 16)"),
            new IntEntry("perfect_quantum_quarry_efficiency", 1, 0, Integer.MAX_VALUE, "Perfect Quantum Quarry: efficiency (old value 1)"),
            new IntEntry("perfect_scanner_operation_length", 1500, 1, Integer.MAX_VALUE, "Perfect Scanner: operation length (old value 1500)"),
            new IntEntry("pumpjack_tank_capacity", 20, 1, Integer.MAX_VALUE, "Pumpjack: tank capacity (old value 20)"),
            new IntEntry("photonic_comb_pump_operation_length", 10, 1, Integer.MAX_VALUE, "Photonic Comb Pump: operation length (old value 10)"),
            new IntEntry("photonic_comb_pump_radius_or_size", 320, 0, Integer.MAX_VALUE, "Photonic Comb Pump: radius or size (old value 320)"),
            new IntEntry("photonic_matter_fabricator_tank_capacity", 16, 1, Integer.MAX_VALUE, "Photonic Matter Fabricator: tank capacity (old value 16)"),
            new IntEntry("photonic_generator_energy_storage", 70000, 1, Integer.MAX_VALUE, "Photonic Generator: energy storage (old value 70000)"),
            new IntEntry("photonic_scanner_operation_length", 1000, 1, Integer.MAX_VALUE, "Photonic Scanner: operation length (old value 1000)"),
            new IntEntry("chemical_plant_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Chemical Plant: energy per tick (old value 1)"),
            new IntEntry("chemical_plant_operation_length", 300, 1, Integer.MAX_VALUE, "Chemical Plant: operation length (old value 300)"),
            new IntEntry("plastic_plate_machine_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Plastic Plate Machine: energy per tick (old value 1)"),
            new IntEntry("plastic_plate_machine_operation_length", 300, 1, Integer.MAX_VALUE, "Plastic Plate Machine: operation length (old value 300)"),
            new IntEntry("polymerizer_energy_use", 1, 0, Integer.MAX_VALUE, "Polymerizer: energy_use (old value 1)"),
            new IntEntry("primal_pump_tank_capacity", 4, 1, Integer.MAX_VALUE, "Primal Pump: tank capacity (old value 4)"),
            new IntEntry("pump_energy_use", 1, 0, Integer.MAX_VALUE, "Pump: energy_use (old value 1)"),
            new IntEntry("quantum_quarry_efficiency", 4, 0, Integer.MAX_VALUE, "Quantum Quarry: efficiency (old value 4)"),
            new IntEntry("fluid_cooler_energy_use", 1, 0, Integer.MAX_VALUE, "Fluid Cooler: energy_use (old value 1)"),
            new IntEntry("reverse_transriptor_energy_use", 1, 0, Integer.MAX_VALUE, "Reverse Transriptor: energy_use (old value 1)"),
            new IntEntry("rna_collector_energy_use", 1, 0, Integer.MAX_VALUE, "RNA Collector: energy_use (old value 1)"),
            new IntEntry("rod_manufacturer_energy_use", 2, 0, Integer.MAX_VALUE, "Rod Manufacturer: energy_use (old value 2)"),
            new IntEntry("rotor_assembler_energy_use", 2, 0, Integer.MAX_VALUE, "Rotor Assembler: energy_use (old value 2)"),
            new IntEntry("southern_operation_length", 40, 1, Integer.MAX_VALUE, "Southern: operation length (old value 40)"),
            new IntEntry("southern_radius_or_size", 40, 0, Integer.MAX_VALUE, "Southern: radius or size (old value 40)"),
            new IntEntry("scanner_operation_length", 3300, 1, Integer.MAX_VALUE, "Scanner: operation length (old value 3300)"),
            new IntEntry("single_fluid_adapter_energy_use", 1, 0, Integer.MAX_VALUE, "Single Fluid Adapter: energy_use (old value 1)"),
            new IntEntry("solid_fluid_integrator_energy_use", 1, 0, Integer.MAX_VALUE, "Solid-Fluid Integrator: energy_use (old value 1)"),
            new IntEntry("solid_fluid_mixer_energy_use", 1, 0, Integer.MAX_VALUE, "Solid-Fluid Mixer: energy_use (old value 1)"),
            new IntEntry("solid_mixer_energy_use", 1, 0, Integer.MAX_VALUE, "Solid Mixer: energy_use (old value 1)"),
            new IntEntry("solid_state_electrolyzer_energy_use", 1, 0, Integer.MAX_VALUE, "Solid-State Electrolyzer: energy_use (old value 1)"),
            new IntEntry("steam_bio_generator_energy_use", 1, 0, Integer.MAX_VALUE, "Steam Bio-Generator: energy_use (old value 1)"),
            new IntEntry("steam_crystal_charger_energy_use", 1, 0, Integer.MAX_VALUE, "Steam Crystal Charger: energy_use (old value 1)"),
            new IntEntry("steam_dryer_energy_use", 2, 0, Integer.MAX_VALUE, "Steam Dryer: energy_use (old value 2)"),
            new IntEntry("steam_pump_energy_use", 2, 0, Integer.MAX_VALUE, "Steam Pump: energy_use (old value 2)"),
            new IntEntry("steam_pump_tank_capacity", 10, 1, Integer.MAX_VALUE, "Steam Pump: tank capacity (old value 10)"),
            new IntEntry("steam_sharpener_energy_use", 1, 0, Integer.MAX_VALUE, "Steam Sharpener: energy_use (old value 1)"),
            new IntEntry("steam_solid_fluid_mixer_energy_use", 1, 0, Integer.MAX_VALUE, "Steam Solid-Fluid Mixer: energy_use (old value 1)"),
            new IntEntry("steam_squeezer_energy_use", 2, 0, Integer.MAX_VALUE, "Steam Squeezer: energy_use (old value 2)"),
            new IntEntry("steam_cable_insulator_energy_use", 1, 0, Integer.MAX_VALUE, "Steam Cable Insulator: energy_use (old value 1)"),
            new IntEntry("steel_tank_tank_capacity", 10, 1, Integer.MAX_VALUE, "Steel Tank: tank capacity (old value 10)"),
            new IntEntry("sunnarium_panel_maker_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Sunnarium Panel Maker: energy per tick (old value 1)"),
            new IntEntry("sunnarium_panel_maker_operation_length", 300, 1, Integer.MAX_VALUE, "Sunnarium Panel Maker: operation length (old value 300)"),
            new IntEntry("nuclear_fusion_reactor_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Nuclear Fusion Reactor: energy per tick (old value 1)"),
            new IntEntry("nuclear_fusion_reactor_operation_length", 300, 1, Integer.MAX_VALUE, "Nuclear Fusion Reactor: operation length (old value 300)"),
            new IntEntry("liquid_tank_tank_capacity", 40, 1, Integer.MAX_VALUE, "Liquid Tank: tank capacity (old value 40)"),
            new IntEntry("teleporter_cooldown_ticks", 0, 0, Integer.MAX_VALUE, "Teleporter: cooldown_ticks (old value 0)"),
            new IntEntry("triple_solid_mixer_energy_use", 1, 0, Integer.MAX_VALUE, "Triple Solid Mixer: energy_use (old value 1)"),
            new IntEntry("update_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Update: energy per tick (old value 1)"),
            new IntEntry("update_operation_length", 300, 1, Integer.MAX_VALUE, "Update: operation length (old value 300)"),
            new IntEntry("rover_upgrade_station_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Rover Upgrade Station: energy per tick (old value 1)"),
            new IntEntry("rover_upgrade_station_operation_length", 300, 1, Integer.MAX_VALUE, "Rover Upgrade Station: operation length (old value 300)"),
            new IntEntry("improved_liquid_reactor_socket_capacity", 15000, 1, Integer.MAX_VALUE, "Improved Liquid Reactor Socket: capacity (old value 15000)"),
            new IntEntry("advanced_liquid_reactor_socket_capacity", 30000, 1, Integer.MAX_VALUE, "Advanced Liquid Reactor Socket: capacity (old value 30000)"),
            new IntEntry("perfect_liquid_reactor_socket_capacity", 80000, 1, Integer.MAX_VALUE, "Perfect Liquid Reactor Socket: capacity (old value 80000)"),
            new IntEntry("water_rotor_assembler_energy_use", 2, 0, Integer.MAX_VALUE, "Water Rotor Assembler: energy_use (old value 2)"),
            new IntEntry("liquid_reactor_socket_capacity", 10000, 1, Integer.MAX_VALUE, "Liquid Reactor Socket: capacity (old value 10000)"),
            new IntEntry("welding_machine_energy_per_tick", 1, 1, Integer.MAX_VALUE, "Welding Machine: energy per tick (old value 1)"),
            new IntEntry("welding_machine_operation_length", 140, 1, Integer.MAX_VALUE, "Welding Machine: operation length (old value 140)")
    };

    private static final DoubleEntry[] MECHANISM_DOUBLE_ENTRIES = new DoubleEntry[]{
            new DoubleEntry("improved_alloy_smelter_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Improved Alloy Smelter: air pollution amount (old value 0.15)"),
            new DoubleEntry("improved_alloy_smelter_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Improved Alloy Smelter: soil pollution amount (old value 0.075)"),
            new DoubleEntry("improved_coke_oven_controller_air_pollution_amount", 0.5D, 0.0D, 1000000.0D, "Improved Coke Oven Controller: air pollution amount (old value 0.5)"),
            new DoubleEntry("improved_coke_oven_controller_soil_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Improved Coke Oven Controller: soil pollution amount (old value 0.2)"),
            new DoubleEntry("improved_generator_efficiency", 2.2D, 0.0D, Double.MAX_VALUE, "Improved Generator: efficiency (old value 2.2)"),
            new DoubleEntry("improved_geo_generator_air_pollution_amount", 0.5D, 0.0D, 1000000.0D, "Improved Geo Generator: air pollution amount (old value 0.5)"),
            new DoubleEntry("improved_geo_generator_soil_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Improved Geo Generator: soil pollution amount (old value 0.2)"),
            new DoubleEntry("improved_handler_heavy_ore_air_pollution_amount", 0.8D, 0.0D, 1000000.0D, "Improved Handler Heavy Ore: air pollution amount (old value 0.8)"),
            new DoubleEntry("improved_handler_heavy_ore_soil_pollution_amount", 0.35D, 0.0D, 1000000.0D, "Improved Handler Heavy Ore: soil pollution amount (old value 0.35)"),
            new DoubleEntry("improved_matter_fabricator_energy_storage", 900000.0D, 0.0D, Double.MAX_VALUE, "Improved Matter Fabricator: energy storage (old value 900000F)"),
            new DoubleEntry("improved_oil_refiner_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Improved Oil Refiner: air pollution amount (old value 0.2)"),
            new DoubleEntry("improved_oil_refiner_soil_pollution_amount", 0.06D, 0.0D, 1000000.0D, "Improved Oil Refiner: soil pollution amount (old value 0.06)"),
            new DoubleEntry("improved_pump_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Improved Pump: air pollution amount (old value 0.15)"),
            new DoubleEntry("improved_pump_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Improved Pump: soil pollution amount (old value 0.05)"),
            new DoubleEntry("improved_redstone_generator_air_pollution_amount", 0.3D, 0.0D, 1000000.0D, "Improved Redstone Generator: air pollution amount (old value 0.3)"),
            new DoubleEntry("improved_redstone_generator_efficiency", 2.2D, 0.0D, Double.MAX_VALUE, "Improved Redstone Generator: efficiency (old value 2.2)"),
            new DoubleEntry("improved_redstone_generator_soil_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Improved Redstone Generator: soil pollution amount (old value 0.2)"),
            new DoubleEntry("improved_oil_refinery_energy_storage", 24000.0D, 0.0D, Double.MAX_VALUE, "Improved Oil Refinery: energy storage (old value 24000)"),
            new DoubleEntry("improved_replicator_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Improved Replicator: air pollution amount (old value 0.15)"),
            new DoubleEntry("improved_replicator_efficiency", 0.95D, 0.0D, Double.MAX_VALUE, "Improved Replicator: efficiency (old value 0.95)"),
            new DoubleEntry("improved_replicator_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Improved Replicator: soil pollution amount (old value 0.075)"),
            new DoubleEntry("improved_scanner_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Improved Scanner: air pollution amount (old value 0.2)"),
            new DoubleEntry("improved_scanner_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Improved Scanner: soil pollution amount (old value 0.075)"),
            new DoubleEntry("advanced_energy_storage", 1600000.0D, 0.0D, Double.MAX_VALUE, "Improved: energy storage (old value 1600000)"),
            new DoubleEntry("advanced_matter_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Advanced Matter: air pollution amount (old value 0.1)"),
            new DoubleEntry("advanced_matter_soil_pollution_amount", 0.02D, 0.0D, 1000000.0D, "Advanced Matter: soil pollution amount (old value 0.02)"),
            new DoubleEntry("air_collector_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Air Collector: soil pollution amount (old value 0.1)"),
            new DoubleEntry("air_separation_unit_energy_storage", 5000.0D, 0.0D, Double.MAX_VALUE, "Air Separation Unit: energy storage (old value 5000)"),
            new DoubleEntry("alkaline_earth_quarry_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Alkaline Earth Quarry: air pollution amount (old value 0.1)"),
            new DoubleEntry("alkaline_earth_quarry_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Alkaline Earth Quarry: soil pollution amount (old value 0.1)"),
            new DoubleEntry("alkaline_earth_quarry_energy_storage", 100.0D, 0.0D, Double.MAX_VALUE, "Alkaline Earth Quarry: energy storage (old value 100)"),
            new DoubleEntry("alloy_smelter_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Alloy Smelter: air pollution amount (old value 0.2)"),
            new DoubleEntry("alloy_smelter_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Alloy Smelter: soil pollution amount (old value 0.1)"),
            new DoubleEntry("current_converter_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Current Converter: energy storage (old value 0)"),
            new DoubleEntry("ampere_storage_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Ampere Storage: energy storage (old value 0)"),
            new DoubleEntry("analyze_air_pollution_amount", 1.0D, 0.0D, 1000000.0D, "Analyze: air pollution amount (old value 1)"),
            new DoubleEntry("analyze_energy_storage", 10000000.0D, 0.0D, Double.MAX_VALUE, "Analyze: energy storage (old value 10000000)"),
            new DoubleEntry("analyze_soil_pollution_amount", 0.5D, 0.0D, 1000000.0D, "Analyze: soil pollution amount (old value 0.5)"),
            new DoubleEntry("module_removal_station_energy_storage", 1000.0D, 0.0D, Double.MAX_VALUE, "Module Removal Station: energy storage (old value 1000)"),
            new DoubleEntry("assampler_scrap_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Assampler Scrap: air pollution amount (old value 0.15)"),
            new DoubleEntry("assampler_scrap_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Assampler Scrap: soil pollution amount (old value 0.1)"),
            new DoubleEntry("auto_crafter_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Auto Crafter: air pollution amount (old value 0.1)"),
            new DoubleEntry("auto_crafter_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Auto Crafter: soil pollution amount (old value 0.1)"),
            new DoubleEntry("excavator_air_pollution_amount", 0.5D, 0.0D, 1000000.0D, "Excavator: air pollution amount (old value 0.5)"),
            new DoubleEntry("excavator_soil_pollution_amount", 0.5D, 0.0D, 1000000.0D, "Excavator: soil pollution amount (old value 0.5)"),
            new DoubleEntry("box_unpacker_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Box unpacker: air pollution amount (old value 0.1)"),
            new DoubleEntry("box_unpacker_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Box unpacker: soil pollution amount (old value 0.1)"),
            new DoubleEntry("auto_spawner_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Auto Spawner: air pollution amount (old value 0.1)"),
            new DoubleEntry("auto_spawner_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Auto Spawner: soil pollution amount (old value 0.1)"),
            new DoubleEntry("automatic_crafting_table_energy_storage", 1000.0D, 0.0D, Double.MAX_VALUE, "Automatic Crafting Table: energy storage (old value 1000)"),
            new DoubleEntry("base_handler_heavy_ore_energy_storage", 300.0D, 0.0D, Double.MAX_VALUE, "Base Handler Heavy Ore: energy storage (old value 300)"),
            new DoubleEntry("base_redstone_generator_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Base Redstone Generator: energy storage (old value 0)"),
            new DoubleEntry("base_replicator_energy_storage", 2000000.0D, 0.0D, Double.MAX_VALUE, "Base Replicator: energy storage (old value 2000000)"),
            new DoubleEntry("base_wither_maker_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Base Wither Maker: air pollution amount (old value 0.2)"),
            new DoubleEntry("base_wither_maker_soil_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Base Wither Maker: soil pollution amount (old value 0.2)"),
            new DoubleEntry("base_world_collector_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Base World Collector: energy storage (old value 0)"),
            new DoubleEntry("battery_factory_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Battery Factory: air pollution amount (old value 0.1)"),
            new DoubleEntry("battery_factory_energy_storage", 400.0D, 0.0D, Double.MAX_VALUE, "Battery Factory: energy storage (old value 400)"),
            new DoubleEntry("battery_factory_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Battery Factory: soil pollution amount (old value 0.1)"),
            new DoubleEntry("bio_fuel_generator_air_pollution_amount", 0.3D, 0.0D, 1000000.0D, "Bio Fuel Generator: air pollution amount (old value 0.3)"),
            new DoubleEntry("bio_fuel_generator_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Bio Fuel Generator: soil pollution amount (old value 0.1)"),
            new DoubleEntry("electric_bio_generator_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Electric Bio-Generator: energy storage (old value 0)"),
            new DoubleEntry("blast_furnace_controller_air_pollution_amount", 0.5D, 0.0D, 1000000.0D, "Blast Furnace Controller: air pollution amount (old value 0.5)"),
            new DoubleEntry("blast_furnace_controller_soil_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Blast Furnace Controller: soil pollution amount (old value 0.2)"),
            new DoubleEntry("brewing_plant_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Brewing Plant: air pollution amount (old value 0.2)"),
            new DoubleEntry("brewing_plant_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Brewing Plant: soil pollution amount (old value 0.1)"),
            new DoubleEntry("cactus_farm_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Cactus Farm: air pollution amount (old value 0.1)"),
            new DoubleEntry("cactus_farm_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Cactus Farm: soil pollution amount (old value 0.1)"),
            new DoubleEntry("canner_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Canner: air pollution amount (old value 0.1)"),
            new DoubleEntry("fluid_solid_canning_machine_energy_storage", 300.0D, 0.0D, Double.MAX_VALUE, "Fluid/Solid Canning Machine: energy storage (old value 300)"),
            new DoubleEntry("canner_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Canner: soil pollution amount (old value 0.05)"),
            new DoubleEntry("bee_product_centrifuge_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Bee Product Centrifuge: air pollution amount (old value 0.1)"),
            new DoubleEntry("bee_product_centrifuge_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Bee Product Centrifuge: soil pollution amount (old value 0.1)"),
            new DoubleEntry("chicken_farm_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Chicken Farm: air pollution amount (old value 0.1)"),
            new DoubleEntry("chicken_farm_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Chicken Farm: soil pollution amount (old value 0.1)"),
            new DoubleEntry("coke_oven_controller_air_pollution_amount", 0.5D, 0.0D, 1000000.0D, "Coke Oven Controller: air pollution amount (old value 0.5)"),
            new DoubleEntry("coke_oven_controller_soil_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Coke Oven Controller: soil pollution amount (old value 0.2)"),
            new DoubleEntry("bee_product_collector_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Bee Product Collector: air pollution amount (old value 0.1)"),
            new DoubleEntry("bee_product_collector_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Bee Product Collector: soil pollution amount (old value 0.1)"),
            new DoubleEntry("comb_double_macerator_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Comb Double Macerator: air pollution amount (old value 0.1)"),
            new DoubleEntry("comb_double_macerator_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Comb Double Macerator: soil pollution amount (old value 0.075)"),
            new DoubleEntry("combined_macerator_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Combined Macerator: air pollution amount (old value 0.15)"),
            new DoubleEntry("combined_macerator_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Combined Macerator: soil pollution amount (old value 0.1)"),
            new DoubleEntry("comb_quad_macerator_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Comb Quad Macerator: air pollution amount (old value 0.05)"),
            new DoubleEntry("comb_quad_macerator_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Comb Quad Macerator: soil pollution amount (old value 0.025)"),
            new DoubleEntry("comb_triple_macerator_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Comb Triple Macerator: air pollution amount (old value 0.075)"),
            new DoubleEntry("comb_triple_macerator_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Comb Triple Macerator: soil pollution amount (old value 0.05)"),
            new DoubleEntry("combined_pump_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Combined Pump: energy storage (old value 0)"),
            new DoubleEntry("combined_matter_fabricator_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Combined Matter Fabricator: air pollution amount (old value 0.1)"),
            new DoubleEntry("combined_matter_fabricator_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Combined Matter Fabricator: energy storage (old value 0)"),
            new DoubleEntry("combined_matter_fabricator_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Combined Matter Fabricator: soil pollution amount (old value 0.05)"),
            new DoubleEntry("primitive_compressor_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Primitive Compressor: air pollution amount (old value 0.15)"),
            new DoubleEntry("primitive_compressor_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Primitive Compressor: soil pollution amount (old value 0.1)"),
            new DoubleEntry("solid_matter_transformer_energy_storage", 50000.0D, 0.0D, Double.MAX_VALUE, "Solid Matter Transformer: energy storage (old value 50000)"),
            new DoubleEntry("refrigerator_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Refrigerator: air pollution amount (old value 0.05)"),
            new DoubleEntry("refrigerator_energy_storage", 10000.0D, 0.0D, Double.MAX_VALUE, "Refrigerator: energy storage (old value 10000D)"),
            new DoubleEntry("refrigerator_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Refrigerator: soil pollution amount (old value 0.05)"),
            new DoubleEntry("cow_farm_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Cow Farm: air pollution amount (old value 0.1)"),
            new DoubleEntry("cow_farm_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Cow Farm: soil pollution amount (old value 0.1)"),
            new DoubleEntry("crystal_charger_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Crystal Charger: energy storage (old value 200)"),
            new DoubleEntry("crystal_charger_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Crystal Charger: air pollution amount (old value 0.05)"),
            new DoubleEntry("crystal_charger_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Crystal Charger: soil pollution amount (old value 0.05)"),
            new DoubleEntry("cutting_machine_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Cutting Machine: air pollution amount (old value 0.15)"),
            new DoubleEntry("cutting_machine_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Cutting Machine: soil pollution amount (old value 0.1)"),
            new DoubleEntry("background_radiation_is_low_energy_storage", 400000.0D, 0.0D, Double.MAX_VALUE, "Background radiation is low: energy storage (old value 400000)"),
            new DoubleEntry("diesel_generator_air_pollution_amount", 0.75D, 0.0D, 1000000.0D, "Diesel Generator: air pollution amount (old value 0.75)"),
            new DoubleEntry("diesel_generator_soil_pollution_amount", 0.45D, 0.0D, 1000000.0D, "Diesel Generator: soil pollution amount (old value 0.45)"),
            new DoubleEntry("double_assampler_scrap_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Double Assampler Scrap: air pollution amount (old value 0.1)"),
            new DoubleEntry("double_assampler_scrap_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Double Assampler Scrap: soil pollution amount (old value 0.075)"),
            new DoubleEntry("double_centrifuge_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Double Centrifuge: air pollution amount (old value 0.1)"),
            new DoubleEntry("double_centrifuge_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Double Centrifuge: soil pollution amount (old value 0.075)"),
            new DoubleEntry("improved_combined_recycler_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Improved Combined Recycler: air pollution amount (old value 0.1)"),
            new DoubleEntry("improved_combined_recycler_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Improved Combined Recycler: soil pollution amount (old value 0.075)"),
            new DoubleEntry("double_compressor_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Double Compressor: air pollution amount (old value 0.1)"),
            new DoubleEntry("double_compressor_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Double Compressor: soil pollution amount (old value 0.075)"),
            new DoubleEntry("improved_cutting_machine_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Improved Cutting Machine: air pollution amount (old value 0.1)"),
            new DoubleEntry("improved_cutting_machine_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Improved Cutting Machine: soil pollution amount (old value 0.075)"),
            new DoubleEntry("double_electric_furnace_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Double Electric Furnace: air pollution amount (old value 0.1)"),
            new DoubleEntry("double_electric_furnace_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Double Electric Furnace: soil pollution amount (old value 0.075)"),
            new DoubleEntry("improved_extractor_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Improved Extractor: air pollution amount (old value 0.1)"),
            new DoubleEntry("improved_extractor_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Improved Extractor: soil pollution amount (old value 0.075)"),
            new DoubleEntry("double_extruding_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Double Extruding: air pollution amount (old value 0.1)"),
            new DoubleEntry("double_extruding_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Double Extruding: soil pollution amount (old value 0.075)"),
            new DoubleEntry("double_fermer_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Double Fermer: air pollution amount (old value 0.1)"),
            new DoubleEntry("double_fermer_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Double Fermer: soil pollution amount (old value 0.075)"),
            new DoubleEntry("double_gear_machine_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Double Gear Machine: air pollution amount (old value 0.1)"),
            new DoubleEntry("double_gear_machine_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Double Gear Machine: soil pollution amount (old value 0.075)"),
            new DoubleEntry("improved_macerator_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Improved Macerator: air pollution amount (old value 0.1)"),
            new DoubleEntry("improved_macerator_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Improved Macerator: soil pollution amount (old value 0.075)"),
            new DoubleEntry("double_ore_washing_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Double Ore Washing: air pollution amount (old value 0.1)"),
            new DoubleEntry("double_ore_washing_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Double Ore Washing: soil pollution amount (old value 0.075)"),
            new DoubleEntry("improved_recycler_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Improved Recycler: air pollution amount (old value 0.1)"),
            new DoubleEntry("improved_recycler_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Improved Recycler: soil pollution amount (old value 0.075)"),
            new DoubleEntry("improved_rolling_machine_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Improved Rolling Machine: air pollution amount (old value 0.1)"),
            new DoubleEntry("improved_rolling_machine_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Improved Rolling Machine: soil pollution amount (old value 0.075)"),
            new DoubleEntry("advanced_molecular_transformer_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Advanced Molecular Transformer: energy storage (old value 0)"),
            new DoubleEntry("electric_brewing_machine_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Electric Brewing Machine: energy storage (old value 200)"),
            new DoubleEntry("electric_latex_dryer_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Electric Latex Dryer: air pollution amount (old value 0.1)"),
            new DoubleEntry("electric_latex_dryer_energy_storage", 100.0D, 0.0D, Double.MAX_VALUE, "Electric Latex Dryer: energy storage (old value 100)"),
            new DoubleEntry("electric_latex_dryer_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Electric Latex Dryer: soil pollution amount (old value 0.1)"),
            new DoubleEntry("electric_furnace_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Electric Furnace: air pollution amount (old value 0.15)"),
            new DoubleEntry("electric_furnace_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Electric Furnace: soil pollution amount (old value 0.1)"),
            new DoubleEntry("electric_heat_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Electric Heat: air pollution amount (old value 0.05)"),
            new DoubleEntry("electric_heat_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Electric Heat: soil pollution amount (old value 0.1)"),
            new DoubleEntry("electric_refractory_furnace_air_pollution_amount", 0.5D, 0.0D, 1000000.0D, "Electric Refractory Furnace: air pollution amount (old value 0.5)"),
            new DoubleEntry("electric_refractory_furnace_soil_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Electric Refractory Furnace: soil pollution amount (old value 0.25)"),
            new DoubleEntry("electric_latex_extractor_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Electric Latex Extractor: air pollution amount (old value 0.1)"),
            new DoubleEntry("electric_latex_extractor_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Electric Latex Extractor: energy storage (old value 200)"),
            new DoubleEntry("electric_latex_extractor_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Electric Latex Extractor: soil pollution amount (old value 0.1)"),
            new DoubleEntry("electric_cable_insulator_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Electric Cable Insulator: energy storage (old value 200)"),
            new DoubleEntry("electrolyzer_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Electrolyzer: air pollution amount (old value 0.1)"),
            new DoubleEntry("electrolyzer_energy_storage", 24000.0D, 0.0D, Double.MAX_VALUE, "Electrolyzer: energy storage (old value 24000)"),
            new DoubleEntry("electrolyzer_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Electrolyzer: soil pollution amount (old value 0.05)"),
            new DoubleEntry("electric_electronic_assembler_energy_storage", 300.0D, 0.0D, Double.MAX_VALUE, "Electric Electronic Assembler: energy storage (old value 300)"),
            new DoubleEntry("electronics_assembly_table_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Electronics Assembly Table: air pollution amount (old value 0.1)"),
            new DoubleEntry("electronics_assembly_table_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Electronics Assembly Table: energy storage (old value 0)"),
            new DoubleEntry("electronics_assembly_table_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Electronics Assembly Table: soil pollution amount (old value 0.1)"),
            new DoubleEntry("enchanting_machine_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Enchanting Machine: air pollution amount (old value 0.1)"),
            new DoubleEntry("enchanting_machine_energy_storage", 400.0D, 0.0D, Double.MAX_VALUE, "Enchanting Machine: energy storage (old value 400)"),
            new DoubleEntry("enchanting_machine_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Enchanting Machine: soil pollution amount (old value 0.1)"),
            new DoubleEntry("extractor_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Extractor: air pollution amount (old value 0.15)"),
            new DoubleEntry("extractor_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Extractor: soil pollution amount (old value 0.1)"),
            new DoubleEntry("extruding_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Extruding: air pollution amount (old value 0.15)"),
            new DoubleEntry("extruding_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Extruding: soil pollution amount (old value 0.1)"),
            new DoubleEntry("fermer_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Fermer: air pollution amount (old value 0.15)"),
            new DoubleEntry("fermer_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Fermer: soil pollution amount (old value 0.1)"),
            new DoubleEntry("field_cleaner_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Field Cleaner: air pollution amount (old value 0.1)"),
            new DoubleEntry("field_cleaner_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Field Cleaner: soil pollution amount (old value 0.1)"),
            new DoubleEntry("fishing_machine_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Fishing Machine: air pollution amount (old value 0.1)"),
            new DoubleEntry("fishing_machine_energy_storage", 10000.0D, 0.0D, Double.MAX_VALUE, "Fishing Machine: energy storage (old value 1E4)"),
            new DoubleEntry("fishing_machine_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Fishing Machine: soil pollution amount (old value 0.05)"),
            new DoubleEntry("fluid_adapter_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Fluid Adapter: air pollution amount (old value 0.1)"),
            new DoubleEntry("fluid_adapter_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Fluid Adapter: energy storage (old value 200)"),
            new DoubleEntry("fluid_adapter_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Fluid Adapter: soil pollution amount (old value 0.1)"),
            new DoubleEntry("fluid_refrigerator_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Fluid Refrigerator: energy storage (old value 0)"),
            new DoubleEntry("liquid_heater_air_pollution_amount", 0.4D, 0.0D, 1000000.0D, "Liquid Heater: air pollution amount (old value 0.4)"),
            new DoubleEntry("liquid_heater_soil_pollution_amount", 0.24D, 0.0D, 1000000.0D, "Liquid Heater: soil pollution amount (old value 0.24)"),
            new DoubleEntry("fluid_heater_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Fluid Heater: air pollution amount (old value 0.1)"),
            new DoubleEntry("fluid_heater_energy_storage", 100.0D, 0.0D, Double.MAX_VALUE, "Fluid Heater: energy storage (old value 100)"),
            new DoubleEntry("fluid_heater_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Fluid Heater: soil pollution amount (old value 0.1)"),
            new DoubleEntry("fluid_integrator_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Fluid Integrator: air pollution amount (old value 0.1)"),
            new DoubleEntry("fluid_integrator_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Fluid Integrator: energy storage (old value 200)"),
            new DoubleEntry("fluid_integrator_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Fluid Integrator: soil pollution amount (old value 0.1)"),
            new DoubleEntry("fluid_mixer_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Fluid Mixer: air pollution amount (old value 0.1)"),
            new DoubleEntry("fluid_mixer_energy_storage", 100.0D, 0.0D, Double.MAX_VALUE, "Fluid Mixer: energy storage (old value 100)"),
            new DoubleEntry("fluid_mixer_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Fluid Mixer: soil pollution amount (old value 0.1)"),
            new DoubleEntry("fluid_separator_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Fluid Separator: air pollution amount (old value 0.1)"),
            new DoubleEntry("fluid_separator_energy_storage", 100.0D, 0.0D, Double.MAX_VALUE, "Fluid Separator: energy storage (old value 100)"),
            new DoubleEntry("fluid_separator_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Fluid Separator: soil pollution amount (old value 0.1)"),
            new DoubleEntry("gas_mixer_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Gas Mixer: air pollution amount (old value 0.2)"),
            new DoubleEntry("gas_mixer_energy_storage", 100.0D, 0.0D, Double.MAX_VALUE, "Gas Mixer: energy storage (old value 100)"),
            new DoubleEntry("gas_mixer_soil_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Gas Mixer: soil pollution amount (old value 0.2)"),
            new DoubleEntry("gas_generator_air_pollution_amount", 0.75D, 0.0D, 1000000.0D, "Gas Generator: air pollution amount (old value 0.75)"),
            new DoubleEntry("gas_generator_soil_pollution_amount", 0.3D, 0.0D, 1000000.0D, "Gas Generator: soil pollution amount (old value 0.3)"),
            new DoubleEntry("gas_installation_energy_storage", 50000.0D, 0.0D, Double.MAX_VALUE, "Gas Installation: energy storage (old value 50000)"),
            new DoubleEntry("gear_machine_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Gear Machine: air pollution amount (old value 0.15)"),
            new DoubleEntry("gear_machine_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Gear Machine: soil pollution amount (old value 0.1)"),
            new DoubleEntry("generation_addition_stone_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Generation Addition Stone: air pollution amount (old value 0.075)"),
            new DoubleEntry("generation_addition_stone_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Generation Addition Stone: soil pollution amount (old value 0.05)"),
            new DoubleEntry("generation_microchip_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Generation Microchip: air pollution amount (old value 0.2)"),
            new DoubleEntry("generation_microchip_soil_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Generation Microchip: soil pollution amount (old value 0.15)"),
            new DoubleEntry("generation_stone_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Generation Stone: air pollution amount (old value 0.075)"),
            new DoubleEntry("generation_stone_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Generation Stone: soil pollution amount (old value 0.05)"),
            new DoubleEntry("generator_improved_air_pollution_amount", 0.5D, 0.0D, 1000000.0D, "Generator Improved: air pollution amount (old value 0.5)"),
            new DoubleEntry("generator_improved_soil_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Generator Improved: soil pollution amount (old value 0.2)"),
            new DoubleEntry("charging_in_the_generator_air_pollution_amount", 0.75D, 0.0D, 1000000.0D, "Charging in the Generator: air pollution amount (old value 0.75)"),
            new DoubleEntry("generator_advanced_air_pollution_amount", 0.35D, 0.0D, 1000000.0D, "Generator Advanced: air pollution amount (old value 0.35)"),
            new DoubleEntry("generator_advanced_soil_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Generator Advanced: soil pollution amount (old value 0.15)"),
            new DoubleEntry("generator_efficiency", 1.0D, 0.0D, Double.MAX_VALUE, "Generator: efficiency (old value 1)"),
            new DoubleEntry("matter_fabricator_energy_storage", 1000000.0D, 0.0D, Double.MAX_VALUE, "Matter Fabricator: energy storage (old value 1000000F)"),
            new DoubleEntry("generator_perfect_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Generator Perfect: air pollution amount (old value 0.2)"),
            new DoubleEntry("generator_perfect_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Generator Perfect: soil pollution amount (old value 0.1)"),
            new DoubleEntry("charging_in_the_generator_soil_pollution_amount", 0.35D, 0.0D, 1000000.0D, "Charging in the Generator: soil pollution amount (old value 0.35)"),
            new DoubleEntry("genetic_polymerizer_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Genetic Polymerizer: air pollution amount (old value 0.25)"),
            new DoubleEntry("genetic_polymerizer_energy_storage", 300.0D, 0.0D, Double.MAX_VALUE, "Genetic Polymerizer: energy storage (old value 300)"),
            new DoubleEntry("genetic_polymerizer_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Genetic Polymerizer: soil pollution amount (old value 0.1)"),
            new DoubleEntry("genetic_replicator_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Genetic Replicator: air pollution amount (old value 0.25)"),
            new DoubleEntry("genetic_replicator_energy_storage", 100.0D, 0.0D, Double.MAX_VALUE, "Genetic Replicator: energy storage (old value 100)"),
            new DoubleEntry("genetic_replicator_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Genetic Replicator: soil pollution amount (old value 0.1)"),
            new DoubleEntry("genetic_stabilize_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Genetic Stabilize: air pollution amount (old value 0.25)"),
            new DoubleEntry("genetic_stabilize_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Genetic Stabilize: soil pollution amount (old value 0.1)"),
            new DoubleEntry("genetic_stabilizer_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Genetic Stabilizer: energy storage (old value 200)"),
            new DoubleEntry("genetic_transposer_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Genetic Transposer: air pollution amount (old value 0.25)"),
            new DoubleEntry("genetic_transposer_energy_storage", 300.0D, 0.0D, Double.MAX_VALUE, "Genetic Transposer: energy storage (old value 300)"),
            new DoubleEntry("genetic_transposer_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Genetic Transposer: soil pollution amount (old value 0.1)"),
            new DoubleEntry("genome_extractor_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Genome Extractor: air pollution amount (old value 0.25)"),
            new DoubleEntry("genome_extractor_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Genome Extractor: soil pollution amount (old value 0.1)"),
            new DoubleEntry("geo_generator_air_pollution_amount", 0.85D, 0.0D, 1000000.0D, "Geo Generator: air pollution amount (old value 0.85)"),
            new DoubleEntry("geo_generator_soil_pollution_amount", 0.35D, 0.0D, 1000000.0D, "Geo Generator: soil pollution amount (old value 0.35)"),
            new DoubleEntry("graphite_crystallizer_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Graphite Crystallizer: air pollution amount (old value 0.1)"),
            new DoubleEntry("graphite_crystallizer_energy_storage", 1000.0D, 0.0D, Double.MAX_VALUE, "Graphite Crystallizer: energy storage (old value 1000)"),
            new DoubleEntry("graphite_crystallizer_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Graphite Crystallizer: soil pollution amount (old value 0.1)"),
            new DoubleEntry("handler_heavy_ore_air_pollution_amount", 1.0D, 0.0D, 1000000.0D, "Handler Heavy Ore: air pollution amount (old value 1)"),
            new DoubleEntry("handler_heavy_ore_soil_pollution_amount", 0.5D, 0.0D, 1000000.0D, "Handler Heavy Ore: soil pollution amount (old value 0.5)"),
            new DoubleEntry("helium_generator_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Helium Generator: air pollution amount (old value 0.1)"),
            new DoubleEntry("helium_generator_energy_storage", 50000.0D, 0.0D, Double.MAX_VALUE, "Helium Generator: energy storage (old value 50000)"),
            new DoubleEntry("helium_generator_soil_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Helium Generator: soil pollution amount (old value 0.15)"),
            new DoubleEntry("generator_without_exhaust_air_pollution_amount", 0.3D, 0.0D, 1000000.0D, "Generator without exhaust: air pollution amount (old value 0.3)"),
            new DoubleEntry("generator_without_exhaust_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Generator without exhaust: soil pollution amount (old value 0.1)"),
            new DoubleEntry("advanced_alloy_smelter_air_pollution_amount", 0.125D, 0.0D, 1000000.0D, "Advanced Alloy Smelter: air pollution amount (old value 0.125)"),
            new DoubleEntry("advanced_alloy_smelter_energy_storage", 300.0D, 0.0D, Double.MAX_VALUE, "Advanced Alloy Smelter: energy storage (old value 300)"),
            new DoubleEntry("advanced_alloy_smelter_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Advanced Alloy Smelter: soil pollution amount (old value 0.05)"),
            new DoubleEntry("advanced_generator_efficiency", 3.4D, 0.0D, Double.MAX_VALUE, "Advanced Generator: efficiency (old value 3.4)"),
            new DoubleEntry("advanced_geo_generator_air_pollution_amount", 0.35D, 0.0D, 1000000.0D, "Advanced Geo Generator: air pollution amount (old value 0.35)"),
            new DoubleEntry("advanced_geo_generator_soil_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Advanced Geo Generator: soil pollution amount (old value 0.15)"),
            new DoubleEntry("advanced_handler_heavy_ore_air_pollution_amount", 0.6D, 0.0D, 1000000.0D, "Advanced Handler Heavy Ore: air pollution amount (old value 0.6)"),
            new DoubleEntry("advanced_handler_heavy_ore_soil_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Advanced Handler Heavy Ore: soil pollution amount (old value 0.25)"),
            new DoubleEntry("advanced_matter_fabricator_energy_storage", 800000.0D, 0.0D, Double.MAX_VALUE, "Advanced Matter Fabricator: energy storage (old value 800000F)"),
            new DoubleEntry("advanced_oil_refiner_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Advanced Oil Refiner: air pollution amount (old value 0.2)"),
            new DoubleEntry("advanced_oil_refiner_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Advanced Oil Refiner: soil pollution amount (old value 0.05)"),
            new DoubleEntry("advanced_pump_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Advanced Pump: air pollution amount (old value 0.1)"),
            new DoubleEntry("advanced_pump_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Advanced Pump: soil pollution amount (old value 0.025)"),
            new DoubleEntry("advanced_redstone_generator_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Advanced Redstone Generator: air pollution amount (old value 0.2)"),
            new DoubleEntry("advanced_redstone_generator_efficiency", 3.4D, 0.0D, Double.MAX_VALUE, "Advanced Redstone Generator: efficiency (old value 3.4)"),
            new DoubleEntry("advanced_redstone_generator_soil_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Advanced Redstone Generator: soil pollution amount (old value 0.15)"),
            new DoubleEntry("advanced_oil_refinery_energy_storage", 24000.0D, 0.0D, Double.MAX_VALUE, "Advanced Oil Refinery: energy storage (old value 24000)"),
            new DoubleEntry("advanced_replicator_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Advanced Replicator: air pollution amount (old value 0.1)"),
            new DoubleEntry("advanced_replicator_efficiency", 0.85D, 0.0D, Double.MAX_VALUE, "Advanced Replicator: efficiency (old value 0.85)"),
            new DoubleEntry("advanced_replicator_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Advanced Replicator: soil pollution amount (old value 0.05)"),
            new DoubleEntry("advanced_scanner_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Advanced Scanner: air pollution amount (old value 0.15)"),
            new DoubleEntry("advanced_scanner_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Advanced Scanner: soil pollution amount (old value 0.05)"),
            new DoubleEntry("advanced_solar_energy_generator_generation_multiplier", 4.0D, 0.0D, Double.MAX_VALUE, "Advanced Solar Energy Generator: generation multiplier (old value 4)"),
            new DoubleEntry("advanced_energy_storage", 16000000.0D, 0.0D, Double.MAX_VALUE, "Advanced: energy storage (old value 16000000)"),
            new DoubleEntry("improved_matter_air_pollution_amount", 0.065D, 0.0D, 1000000.0D, "Improved Matter: air pollution amount (old value 0.065)"),
            new DoubleEntry("improved_matter_soil_pollution_amount", 0.015D, 0.0D, 1000000.0D, "Improved Matter: soil pollution amount (old value 0.015)"),
            new DoubleEntry("incubator_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Incubator: air pollution amount (old value 0.25)"),
            new DoubleEntry("incubator_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Incubator: energy storage (old value 200)"),
            new DoubleEntry("incubator_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Incubator: soil pollution amount (old value 0.1)"),
            new DoubleEntry("industrial_radioactive_element_purifier_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Industrial Radioactive Element Purifier: air pollution amount (old value 0.25)"),
            new DoubleEntry("industrial_radioactive_element_purifier_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Industrial Radioactive Element Purifier: energy storage (old value 200)"),
            new DoubleEntry("industrial_radioactive_element_purifier_soil_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Industrial Radioactive Element Purifier: soil pollution amount (old value 0.25)"),
            new DoubleEntry("inoculator_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Inoculator: air pollution amount (old value 0.25)"),
            new DoubleEntry("inoculator_energy_storage", 300.0D, 0.0D, Double.MAX_VALUE, "Inoculator: energy storage (old value 300)"),
            new DoubleEntry("inoculator_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Inoculator: soil pollution amount (old value 0.1)"),
            new DoubleEntry("insulator_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Insulator: air pollution amount (old value 0.25)"),
            new DoubleEntry("insulator_energy_storage", 100.0D, 0.0D, Double.MAX_VALUE, "Insulator: energy storage (old value 100)"),
            new DoubleEntry("insulator_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Insulator: soil pollution amount (old value 0.1)"),
            new DoubleEntry("item_divider_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Item Divider: air pollution amount (old value 0.1)"),
            new DoubleEntry("item_divider_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Item Divider: energy storage (old value 200)"),
            new DoubleEntry("item_divider_fluids_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Item Divider Fluids: air pollution amount (old value 0.1)"),
            new DoubleEntry("item_divider_fluids_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Item Divider Fluids: soil pollution amount (old value 0.1)"),
            new DoubleEntry("item_divider_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Item Divider: soil pollution amount (old value 0.1)"),
            new DoubleEntry("item_to_fluid_divider_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Item-to-Fluid Divider: energy storage (old value 200)"),
            new DoubleEntry("bag_unloader_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Bag Unloader: energy storage (old value 0)"),
            new DoubleEntry("laser_polisher_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Laser Polisher: air pollution amount (old value 0.1)"),
            new DoubleEntry("laser_polisher_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Laser Polisher: energy storage (old value 200)"),
            new DoubleEntry("laser_polisher_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Laser Polisher: soil pollution amount (old value 0.1)"),
            new DoubleEntry("lava_generator_energy_storage", 20000.0D, 0.0D, Double.MAX_VALUE, "Lava Generator: energy storage (old value 20000)"),
            new DoubleEntry("lava_generator_air_pollution_amount", 0.3D, 0.0D, 1000000.0D, "Lava Generator: air pollution amount (old value 0.3)"),
            new DoubleEntry("lava_generator_soil_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Lava Generator: soil pollution amount (old value 0.15)"),
            new DoubleEntry("maceration_tool_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Maceration Tool: air pollution amount (old value 0.15)"),
            new DoubleEntry("maceration_tool_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Maceration Tool: soil pollution amount (old value 0.1)"),
            new DoubleEntry("machine_charge_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Machine Charge: air pollution amount (old value 0.1)"),
            new DoubleEntry("machine_charge_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Machine Charge: soil pollution amount (old value 0.1)"),
            new DoubleEntry("magnet_energy_storage", 100000.0D, 0.0D, Double.MAX_VALUE, "Magnet: energy storage (old value 100000)"),
            new DoubleEntry("magnetic_generator_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Magnetic Generator: energy storage (old value 0)"),
            new DoubleEntry("active_matter_factory_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Active Matter Factory: air pollution amount (old value 0.1)"),
            new DoubleEntry("active_matter_factory_energy_storage", 2000.0D, 0.0D, Double.MAX_VALUE, "Active Matter Factory: energy storage (old value 2000)"),
            new DoubleEntry("active_matter_factory_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Active Matter Factory: soil pollution amount (old value 0.1)"),
            new DoubleEntry("mob_magnet_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Mob Magnet: air pollution amount (old value 0.1)"),
            new DoubleEntry("mob_magnet_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Mob Magnet: soil pollution amount (old value 0.1)"),
            new DoubleEntry("module_handler_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Module Handler: energy storage (old value 0)"),
            new DoubleEntry("molecular_transformer_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Molecular Transformer: energy storage (old value 0)"),
            new DoubleEntry("moonlight_infuser_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Moonlight Infuser: air pollution amount (old value 0.1)"),
            new DoubleEntry("moonlight_infuser_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Moonlight Infuser: energy storage (old value 0)"),
            new DoubleEntry("moonlight_infuser_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Moonlight Infuser: soil pollution amount (old value 0.1)"),
            new DoubleEntry("multi_crop_air_pollution_formula_base_value", 0.1D, 0.0D, 1000000.0D, "Multi Crop: air pollution formula base value (old value 0.1)"),
            new DoubleEntry("multi_crop_soil_pollution_formula_base_value", 0.1D, 0.0D, 1000000.0D, "Multi Crop: soil pollution formula base value (old value 0.1)"),
            new DoubleEntry("mutatron_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Mutatron: air pollution amount (old value 0.25)"),
            new DoubleEntry("mutatron_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Mutatron: energy storage (old value 200)"),
            new DoubleEntry("mutatron_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Mutatron: soil pollution amount (old value 0.1)"),
            new DoubleEntry("neutron_particle_generator_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Neutron Particle Generator: air pollution amount (old value 0.05)"),
            new DoubleEntry("neutron_particle_generator_soil_pollution_amount", 0.005D, 0.0D, 1000000.0D, "Neutron Particle Generator: soil pollution amount (old value 0.005)"),
            new DoubleEntry("neutronium_transformer_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Neutronium transformer: energy storage (old value 0)"),
            new DoubleEntry("night_converter_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Night Converter: energy storage (old value 0)"),
            new DoubleEntry("radioactive_waste_reprocessor_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Radioactive Waste Reprocessor: air pollution amount (old value 0.1)"),
            new DoubleEntry("radioactive_waste_reprocessor_energy_storage", 500.0D, 0.0D, Double.MAX_VALUE, "Radioactive Waste Reprocessor: energy storage (old value 500)"),
            new DoubleEntry("radioactive_waste_reprocessor_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Radioactive Waste Reprocessor: soil pollution amount (old value 0.1)"),
            new DoubleEntry("obsidian_generator_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Obsidian Generator: air pollution amount (old value 0.25)"),
            new DoubleEntry("obsidian_generator_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Obsidian Generator: soil pollution amount (old value 0.1)"),
            new DoubleEntry("oil_purifier_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Oil Purifier: air pollution amount (old value 0.1)"),
            new DoubleEntry("oil_purifier_energy_storage", 100.0D, 0.0D, Double.MAX_VALUE, "Oil Purifier: energy storage (old value 100)"),
            new DoubleEntry("oil_purifier_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Oil Purifier: soil pollution amount (old value 0.1)"),
            new DoubleEntry("oil_refiner_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Oil Refiner: air pollution amount (old value 0.25)"),
            new DoubleEntry("oil_refiner_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Oil Refiner: soil pollution amount (old value 0.075)"),
            new DoubleEntry("ore_washing_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Ore Washing: air pollution amount (old value 0.15)"),
            new DoubleEntry("ore_washing_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Ore Washing: soil pollution amount (old value 0.1)"),
            new DoubleEntry("painting_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Painting: air pollution amount (old value 0.1)"),
            new DoubleEntry("painting_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Painting: soil pollution amount (old value 0.05)"),
            new DoubleEntry("radioisotope_thermoelectric_generator_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Radioisotope Thermoelectric Generator: energy storage (old value 0)"),
            new DoubleEntry("peat_generator_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Peat Generator: air pollution amount (old value 0.2)"),
            new DoubleEntry("peat_generator_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Peat Generator: energy storage (old value 0)"),
            new DoubleEntry("peat_generator_soil_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Peat Generator: soil pollution amount (old value 0.2)"),
            new DoubleEntry("perfect_alloy_smelter_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Perfect Alloy Smelter: air pollution amount (old value 0.1)"),
            new DoubleEntry("perfect_alloy_smelter_energy_storage", 300.0D, 0.0D, Double.MAX_VALUE, "Perfect Alloy Smelter: energy storage (old value 300)"),
            new DoubleEntry("perfect_alloy_smelter_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Perfect Alloy Smelter: soil pollution amount (old value 0.025)"),
            new DoubleEntry("perfect_generator_efficiency", 4.6D, 0.0D, Double.MAX_VALUE, "Perfect Generator: efficiency (old value 4.6)"),
            new DoubleEntry("perfect_geo_generator_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Perfect Geo Generator: air pollution amount (old value 0.2)"),
            new DoubleEntry("perfect_geo_generator_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Perfect Geo Generator: soil pollution amount (old value 0.1)"),
            new DoubleEntry("perfect_handler_heavy_ore_air_pollution_amount", 0.4D, 0.0D, 1000000.0D, "Perfect Handler Heavy Ore: air pollution amount (old value 0.4)"),
            new DoubleEntry("perfect_handler_heavy_ore_soil_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Perfect Handler Heavy Ore: soil pollution amount (old value 0.15)"),
            new DoubleEntry("perfect_matter_fabricator_energy_storage", 700000.0D, 0.0D, Double.MAX_VALUE, "Perfect Matter Fabricator: energy storage (old value 700000F)"),
            new DoubleEntry("perfect_redstone_generator_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Perfect Redstone Generator: air pollution amount (old value 0.1)"),
            new DoubleEntry("perfect_redstone_generator_efficiency", 4.6D, 0.0D, Double.MAX_VALUE, "Perfect Redstone Generator: efficiency (old value 4.6)"),
            new DoubleEntry("perfect_redstone_generator_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Perfect Redstone Generator: soil pollution amount (old value 0.1)"),
            new DoubleEntry("perfect_replicator_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Perfect Replicator: air pollution amount (old value 0.05)"),
            new DoubleEntry("perfect_replicator_efficiency", 0.8D, 0.0D, Double.MAX_VALUE, "Perfect Replicator: efficiency (old value 0.8)"),
            new DoubleEntry("perfect_replicator_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Perfect Replicator: soil pollution amount (old value 0.025)"),
            new DoubleEntry("perfect_scanner_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Perfect Scanner: air pollution amount (old value 0.1)"),
            new DoubleEntry("perfect_scanner_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Perfect Scanner: soil pollution amount (old value 0.025)"),
            new DoubleEntry("perfect_energy_storage", 160000000.0D, 0.0D, Double.MAX_VALUE, "Perfect: energy storage (old value 160000000)"),
            new DoubleEntry("gasoline_generator_air_pollution_amount", 0.75D, 0.0D, 1000000.0D, "Gasoline Generator: air pollution amount (old value 0.75)"),
            new DoubleEntry("gasoline_generator_soil_pollution_amount", 0.45D, 0.0D, 1000000.0D, "Gasoline Generator: soil pollution amount (old value 0.45)"),
            new DoubleEntry("pumpjack_energy_storage", 50000.0D, 0.0D, Double.MAX_VALUE, "Pumpjack: energy storage (old value 50000)"),
            new DoubleEntry("photonic_quantum_storage_energy_storage", 5120000000.0D, 0.0D, Double.MAX_VALUE, "Photonic Quantum Storage: energy storage (old value 5120000000D)"),
            new DoubleEntry("photonic_matter_fabricator_energy_storage", 600000.0D, 0.0D, Double.MAX_VALUE, "Photonic Matter Fabricator: energy storage (old value 600000F)"),
            new DoubleEntry("photonic_generator_efficiency", 6.0D, 0.0D, Double.MAX_VALUE, "Photonic Generator: efficiency (old value 6)"),
            new DoubleEntry("photonic_redstone_generator_efficiency", 6.0D, 0.0D, Double.MAX_VALUE, "Photonic Redstone Generator: efficiency (old value 6)"),
            new DoubleEntry("photonic_replicator_efficiency", 0.7D, 0.0D, Double.MAX_VALUE, "Photonic Replicator: efficiency (old value 0.7)"),
            new DoubleEntry("pig_farm_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Pig Farm: air pollution amount (old value 0.1)"),
            new DoubleEntry("pig_farm_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Pig Farm: soil pollution amount (old value 0.1)"),
            new DoubleEntry("plant_collector_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Plant Collector: air pollution amount (old value 0.2)"),
            new DoubleEntry("plant_collector_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Plant Collector: soil pollution amount (old value 0.1)"),
            new DoubleEntry("plant_fertilizer_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Plant Fertilizer: air pollution amount (old value 0.1)"),
            new DoubleEntry("plant_fertilizer_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Plant Fertilizer: soil pollution amount (old value 0.1)"),
            new DoubleEntry("plant_gardener_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Plant Gardener: air pollution amount (old value 0.2)"),
            new DoubleEntry("plant_gardener_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Plant Gardener: soil pollution amount (old value 0.1)"),
            new DoubleEntry("chemical_plant_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Chemical Plant: air pollution amount (old value 0.25)"),
            new DoubleEntry("chemical_plant_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Chemical Plant: soil pollution amount (old value 0.1)"),
            new DoubleEntry("plastic_plate_machine_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Plastic Plate Machine: air pollution amount (old value 0.1)"),
            new DoubleEntry("plastic_plate_machine_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Plastic Plate Machine: soil pollution amount (old value 0.1)"),
            new DoubleEntry("polymerizer_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Polymerizer: air pollution amount (old value 0.1)"),
            new DoubleEntry("polymerizer_energy_storage", 100.0D, 0.0D, Double.MAX_VALUE, "Polymerizer: energy storage (old value 100)"),
            new DoubleEntry("polymerizer_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Polymerizer: soil pollution amount (old value 0.1)"),
            new DoubleEntry("electric_wilson_chamber_energy_storage", 500.0D, 0.0D, Double.MAX_VALUE, "Electric Wilson Chamber: energy storage (old value 500)"),
            new DoubleEntry("primitive_fluid_heater_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Primitive Fluid Heater: energy storage (old value 0)"),
            new DoubleEntry("primitive_fluid_integrator_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Primitive Fluid Integrator: energy storage (old value 0)"),
            new DoubleEntry("gas_chamber_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Gas Chamber: energy storage (old value 0)"),
            new DoubleEntry("primal_programming_table_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Primal Programming Table: energy storage (old value 0)"),
            new DoubleEntry("primal_pump_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Primal Pump: energy storage (old value 0)"),
            new DoubleEntry("primitive_crystal_grower_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Primitive Crystal Grower: energy storage (old value 0)"),
            new DoubleEntry("soldering_table_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Soldering Table: energy storage (old value 0)"),
            new DoubleEntry("privatizer_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Privatizer: energy storage (old value 0)"),
            new DoubleEntry("probe_assembler_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Probe Assembler: air pollution amount (old value 0.1)"),
            new DoubleEntry("probe_assembler_energy_storage", 800.0D, 0.0D, Double.MAX_VALUE, "Probe Assembler: energy storage (old value 800)"),
            new DoubleEntry("probe_assembler_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Probe Assembler: soil pollution amount (old value 0.1)"),
            new DoubleEntry("electric_programming_table_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Electric Programming Table: air pollution amount (old value 0.1)"),
            new DoubleEntry("electric_programming_table_energy_storage", 100.0D, 0.0D, Double.MAX_VALUE, "Electric Programming Table: energy storage (old value 100)"),
            new DoubleEntry("electric_programming_table_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Electric Programming Table: soil pollution amount (old value 0.1)"),
            new DoubleEntry("pump_energy_storage", 20.0D, 0.0D, Double.MAX_VALUE, "Pump: energy storage (old value 20)"),
            new DoubleEntry("quad_assampler_scrap_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Quad Assampler Scrap: air pollution amount (old value 0.05)"),
            new DoubleEntry("quad_assampler_scrap_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Quad Assampler Scrap: soil pollution amount (old value 0.025)"),
            new DoubleEntry("quad_centrifuge_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Quad Centrifuge: air pollution amount (old value 0.05)"),
            new DoubleEntry("quad_centrifuge_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Quad Centrifuge: soil pollution amount (old value 0.025)"),
            new DoubleEntry("perfect_combined_recycle_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Perfect Combined Recycle: air pollution amount (old value 0.05)"),
            new DoubleEntry("perfect_combined_recycle_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Perfect Combined Recycle: soil pollution amount (old value 0.025)"),
            new DoubleEntry("quad_compressor_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Quad Compressor: air pollution amount (old value 0.05)"),
            new DoubleEntry("quad_compressor_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Quad Compressor: soil pollution amount (old value 0.025)"),
            new DoubleEntry("perfect_cutting_machine_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Perfect Cutting Machine: air pollution amount (old value 0.05)"),
            new DoubleEntry("perfect_cutting_machine_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Perfect Cutting Machine: soil pollution amount (old value 0.025)"),
            new DoubleEntry("quad_electric_furnace_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Quad Electric Furnace: air pollution amount (old value 0.05)"),
            new DoubleEntry("quad_electric_furnace_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Quad Electric Furnace: soil pollution amount (old value 0.025)"),
            new DoubleEntry("perfect_extractor_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Perfect Extractor: air pollution amount (old value 0.05)"),
            new DoubleEntry("perfect_extractor_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Perfect Extractor: soil pollution amount (old value 0.025)"),
            new DoubleEntry("quad_extruding_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Quad Extruding: air pollution amount (old value 0.05)"),
            new DoubleEntry("quad_extruding_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Quad Extruding: soil pollution amount (old value 0.025)"),
            new DoubleEntry("quad_fermer_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Quad Fermer: air pollution amount (old value 0.05)"),
            new DoubleEntry("quad_fermer_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Quad Fermer: soil pollution amount (old value 0.025)"),
            new DoubleEntry("quad_gear_machine_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Quad Gear Machine: air pollution amount (old value 0.05)"),
            new DoubleEntry("quad_gear_machine_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Quad Gear Machine: soil pollution amount (old value 0.025)"),
            new DoubleEntry("perfect_macerator_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Perfect Macerator: air pollution amount (old value 0.05)"),
            new DoubleEntry("perfect_macerator_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Perfect Macerator: soil pollution amount (old value 0.025)"),
            new DoubleEntry("quad_ore_washing_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Quad Ore Washing: air pollution amount (old value 0.05)"),
            new DoubleEntry("quad_ore_washing_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Quad Ore Washing: soil pollution amount (old value 0.025)"),
            new DoubleEntry("perfect_recycler_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Perfect Recycler: air pollution amount (old value 0.05)"),
            new DoubleEntry("perfect_recycler_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Perfect Recycler: soil pollution amount (old value 0.025)"),
            new DoubleEntry("perfect_rolling_machine_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Perfect Rolling Machine: air pollution amount (old value 0.05)"),
            new DoubleEntry("perfect_rolling_machine_soil_pollution_amount", 0.025D, 0.0D, 1000000.0D, "Perfect Rolling Machine: soil pollution amount (old value 0.025)"),
            new DoubleEntry("quantum_transformer_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Quantum Transformer: energy storage (old value 0)"),
            new DoubleEntry("drilling_rig_energy_storage", 400.0D, 0.0D, Double.MAX_VALUE, "Drilling Rig: energy storage (old value 400)"),
            new DoubleEntry("radiation_purifier_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Radiation Purifier: air pollution amount (old value 0.15)"),
            new DoubleEntry("radiation_purifier_energy_storage", 50000.0D, 0.0D, Double.MAX_VALUE, "Radiation Purifier: energy storage (old value 50000)"),
            new DoubleEntry("radiation_purifier_soil_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Radiation Purifier: soil pollution amount (old value 0.15)"),
            new DoubleEntry("radioactive_ore_handler_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Radioactive Ore Handler: energy storage (old value 200)"),
            new DoubleEntry("radioactive_ore_handler_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Radioactive Ore Handler: air pollution amount (old value 0.25)"),
            new DoubleEntry("radioactive_ore_handler_soil_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Radioactive Ore Handler: soil pollution amount (old value 0.25)"),
            new DoubleEntry("reactor_rod_manufacturer_energy_storage", 2000.0D, 0.0D, Double.MAX_VALUE, "Reactor Rod Manufacturer: energy storage (old value 2000)"),
            new DoubleEntry("reactor_protective_dome_energy_storage", 50000.0D, 0.0D, Double.MAX_VALUE, "Reactor Protective Dome: energy storage (old value 50000)"),
            new DoubleEntry("recycler_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Recycler: air pollution amount (old value 0.15)"),
            new DoubleEntry("recycler_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Recycler: soil pollution amount (old value 0.1)"),
            new DoubleEntry("redstone_generator_air_pollution_amount", 0.4D, 0.0D, 1000000.0D, "Redstone Generator: air pollution amount (old value 0.4)"),
            new DoubleEntry("redstone_generator_efficiency", 1.0D, 0.0D, Double.MAX_VALUE, "Redstone Generator: efficiency (old value 1)"),
            new DoubleEntry("redstone_generator_soil_pollution_amount", 0.3D, 0.0D, 1000000.0D, "Redstone Generator: soil pollution amount (old value 0.3)"),
            new DoubleEntry("oil_refinery_energy_storage", 24000.0D, 0.0D, Double.MAX_VALUE, "Oil Refinery: energy storage (old value 24000)"),
            new DoubleEntry("cooling_rod_refrigerator_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Cooling Rod Refrigerator: air pollution amount (old value 0.1)"),
            new DoubleEntry("cooling_rod_refrigerator_energy_storage", 400.0D, 0.0D, Double.MAX_VALUE, "Cooling Rod Refrigerator: energy storage (old value 400)"),
            new DoubleEntry("cooling_rod_refrigerator_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Cooling Rod Refrigerator: soil pollution amount (old value 0.1)"),
            new DoubleEntry("fluid_cooler_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Fluid Cooler: air pollution amount (old value 0.1)"),
            new DoubleEntry("fluid_cooler_energy_storage", 100.0D, 0.0D, Double.MAX_VALUE, "Fluid Cooler: energy storage (old value 100)"),
            new DoubleEntry("fluid_cooler_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Fluid Cooler: soil pollution amount (old value 0.1)"),
            new DoubleEntry("replicator_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Replicator: air pollution amount (old value 0.2)"),
            new DoubleEntry("replicator_efficiency", 1.0D, 0.0D, Double.MAX_VALUE, "Replicator: efficiency (old value 1)"),
            new DoubleEntry("replicator_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Replicator: soil pollution amount (old value 0.1)"),
            new DoubleEntry("genetic_reverser_energy_storage", 100.0D, 0.0D, Double.MAX_VALUE, "Genetic Reverser: energy storage (old value 100)"),
            new DoubleEntry("reverse_transriptor_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Reverse Transriptor: air pollution amount (old value 0.25)"),
            new DoubleEntry("reverse_transriptor_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Reverse Transriptor: soil pollution amount (old value 0.1)"),
            new DoubleEntry("rna_collector_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "RNA Collector: energy storage (old value 200)"),
            new DoubleEntry("rna_collector_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "RNA Collector: air pollution amount (old value 0.25)"),
            new DoubleEntry("rna_collector_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "RNA Collector: soil pollution amount (old value 0.1)"),
            new DoubleEntry("rocket_assembler_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Rocket Assembler: air pollution amount (old value 0.1)"),
            new DoubleEntry("rocket_assembler_energy_storage", 800.0D, 0.0D, Double.MAX_VALUE, "Rocket Assembler: energy storage (old value 800)"),
            new DoubleEntry("rocket_assembler_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Rocket Assembler: soil pollution amount (old value 0.1)"),
            new DoubleEntry("rod_factory_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Rod Factory: air pollution amount (old value 0.1)"),
            new DoubleEntry("rod_factory_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Rod Factory: soil pollution amount (old value 0.1)"),
            new DoubleEntry("rod_manufacturer_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Rod Manufacturer: air pollution amount (old value 0.05)"),
            new DoubleEntry("rod_manufacturer_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Rod Manufacturer: soil pollution amount (old value 0.05)"),
            new DoubleEntry("rolling_machine_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Rolling Machine: air pollution amount (old value 0.15)"),
            new DoubleEntry("rolling_machine_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Rolling Machine: soil pollution amount (old value 0.1)"),
            new DoubleEntry("rotor_assembler_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Rotor Assembler: air pollution amount (old value 0.05)"),
            new DoubleEntry("rotor_assembler_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Rotor Assembler: soil pollution amount (old value 0.05)"),
            new DoubleEntry("rover_assembler_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Rover Assembler: air pollution amount (old value 0.1)"),
            new DoubleEntry("rover_assembler_energy_storage", 800.0D, 0.0D, Double.MAX_VALUE, "Rover Assembler: energy storage (old value 800)"),
            new DoubleEntry("rover_assembler_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Rover Assembler: soil pollution amount (old value 0.1)"),
            new DoubleEntry("sapling_gardener_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Sapling Gardener: air pollution amount (old value 0.1)"),
            new DoubleEntry("sapling_gardener_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Sapling Gardener: soil pollution amount (old value 0.1)"),
            new DoubleEntry("satellite_assembler_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Satellite Assembler: air pollution amount (old value 0.1)"),
            new DoubleEntry("satellite_assembler_energy_storage", 800.0D, 0.0D, Double.MAX_VALUE, "Satellite Assembler: energy storage (old value 800)"),
            new DoubleEntry("satellite_assembler_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Satellite Assembler: soil pollution amount (old value 0.1)"),
            new DoubleEntry("sawmill_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Sawmill: air pollution amount (old value 0.1)"),
            new DoubleEntry("sawmill_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Sawmill: energy storage (old value 200)"),
            new DoubleEntry("sawmill_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Sawmill: soil pollution amount (old value 0.1)"),
            new DoubleEntry("scanner_energy_storage", 512000.0D, 0.0D, Double.MAX_VALUE, "Scanner: energy storage (old value 512000)"),
            new DoubleEntry("sheep_farm_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Sheep Farm: air pollution amount (old value 0.1)"),
            new DoubleEntry("sheep_farm_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Sheep Farm: soil pollution amount (old value 0.1)"),
            new DoubleEntry("crystal_grower_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Crystal Grower: air pollution amount (old value 0.1)"),
            new DoubleEntry("crystal_grower_energy_storage", 1000.0D, 0.0D, Double.MAX_VALUE, "Crystal Grower: energy storage (old value 1000)"),
            new DoubleEntry("crystal_grower_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Crystal Grower: soil pollution amount (old value 0.1)"),
            new DoubleEntry("simple_pump_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Simple Pump: air pollution amount (old value 0.25)"),
            new DoubleEntry("simple_pump_soil_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Simple Pump: soil pollution amount (old value 0.075)"),
            new DoubleEntry("simple_scanner_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Simple Scanner: air pollution amount (old value 0.25)"),
            new DoubleEntry("simple_scanner_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Simple Scanner: soil pollution amount (old value 0.1)"),
            new DoubleEntry("single_fluid_adapter_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Single Fluid Adapter: air pollution amount (old value 0.1)"),
            new DoubleEntry("single_fluid_adapter_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Single Fluid Adapter: energy storage (old value 200)"),
            new DoubleEntry("single_fluid_adapter_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Single Fluid Adapter: soil pollution amount (old value 0.1)"),
            new DoubleEntry("smeltery_controller_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Smeltery Controller: air pollution amount (old value 0.25)"),
            new DoubleEntry("smeltery_controller_soil_pollution_amount", 0.4D, 0.0D, 1000000.0D, "Smeltery Controller: soil pollution amount (old value 0.4)"),
            new DoubleEntry("socket_factory_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Socket Factory: air pollution amount (old value 0.1)"),
            new DoubleEntry("socket_factory_energy_storage", 400.0D, 0.0D, Double.MAX_VALUE, "Socket Factory: energy storage (old value 400)"),
            new DoubleEntry("socket_factory_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Socket Factory: soil pollution amount (old value 0.1)"),
            new DoubleEntry("soil_analyzer_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Soil Analyzer: air pollution amount (old value 0.05)"),
            new DoubleEntry("soil_analyzer_energy_storage", 5000.0D, 0.0D, Double.MAX_VALUE, "Soil Analyzer: energy storage (old value 5000)"),
            new DoubleEntry("soil_analyzer_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Soil Analyzer: soil pollution amount (old value 0.05)"),
            new DoubleEntry("solid_refrigerator_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Solid Refrigerator: energy storage (old value 0)"),
            new DoubleEntry("solid_fluid_integrator_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Solid-Fluid Integrator: air pollution amount (old value 0.1)"),
            new DoubleEntry("solid_fluid_integrator_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Solid-Fluid Integrator: energy storage (old value 200)"),
            new DoubleEntry("solid_fluid_integrator_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Solid-Fluid Integrator: soil pollution amount (old value 0.1)"),
            new DoubleEntry("solid_fluid_mixer_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Solid-Fluid Mixer: air pollution amount (old value 0.1)"),
            new DoubleEntry("solid_fluid_mixer_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Solid-Fluid Mixer: energy storage (old value 200)"),
            new DoubleEntry("solid_fluid_mixer_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Solid-Fluid Mixer: soil pollution amount (old value 0.1)"),
            new DoubleEntry("solid_mixer_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Solid Mixer: air pollution amount (old value 0.1)"),
            new DoubleEntry("solid_mixer_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Solid Mixer: energy storage (old value 200)"),
            new DoubleEntry("solid_mixer_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Solid Mixer: soil pollution amount (old value 0.1)"),
            new DoubleEntry("solid_state_electrolyzer_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Solid-State Electrolyzer: air pollution amount (old value 0.1)"),
            new DoubleEntry("solid_state_electrolyzer_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Solid-State Electrolyzer: energy storage (old value 200)"),
            new DoubleEntry("solid_state_electrolyzer_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Solid-State Electrolyzer: soil pollution amount (old value 0.1)"),
            new DoubleEntry("automatic_hunter_energy_storage", 150000.0D, 0.0D, Double.MAX_VALUE, "Automatic Hunter: energy storage (old value 150000)"),
            new DoubleEntry("stamping_machine_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Stamping Machine: air pollution amount (old value 0.1)"),
            new DoubleEntry("stamping_machine_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Stamping Machine: energy storage (old value 200)"),
            new DoubleEntry("stamping_machine_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Stamping Machine: soil pollution amount (old value 0.1)"),
            new DoubleEntry("steam_current_converter_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Steam Current Converter: energy storage (old value 0)"),
            new DoubleEntry("steam_bio_generator_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Steam Bio-Generator: energy storage (old value 0)"),
            new DoubleEntry("steam_compressor_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Steam Compressor: air pollution amount (old value 0.2)"),
            new DoubleEntry("steam_compressor_soil_pollution_amount", 0.125D, 0.0D, 1000000.0D, "Steam Compressor: soil pollution amount (old value 0.125)"),
            new DoubleEntry("steam_converter_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Steam Converter: energy storage (old value 0)"),
            new DoubleEntry("steam_crystal_charger_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Steam Crystal Charger: energy storage (old value 0)"),
            new DoubleEntry("steam_cutting_machine_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Steam Cutting Machine: air pollution amount (old value 0.2)"),
            new DoubleEntry("steam_cutting_machine_soil_pollution_amount", 0.125D, 0.0D, 1000000.0D, "Steam Cutting Machine: soil pollution amount (old value 0.125)"),
            new DoubleEntry("steam_electrolyzer_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Steam Electrolyzer: energy storage (old value 0)"),
            new DoubleEntry("steam_extractor_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Steam Extractor: air pollution amount (old value 0.2)"),
            new DoubleEntry("steam_extractor_soil_pollution_amount", 0.125D, 0.0D, 1000000.0D, "Steam Extractor: soil pollution amount (old value 0.125)"),
            new DoubleEntry("steam_extruder_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Steam Extruder: air pollution amount (old value 0.2)"),
            new DoubleEntry("steam_extruder_soil_pollution_amount", 0.125D, 0.0D, 1000000.0D, "Steam Extruder: soil pollution amount (old value 0.125)"),
            new DoubleEntry("steam_fluid_heater_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Steam Fluid Heater: energy storage (old value 0)"),
            new DoubleEntry("electric_steam_generator_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Electric Steam Generator: air pollution amount (old value 0.1)"),
            new DoubleEntry("electric_steam_generator_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Electric Steam Generator: soil pollution amount (old value 0.1)"),
            new DoubleEntry("steam_ore_separator_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Steam Ore Separator: energy storage (old value 0)"),
            new DoubleEntry("steam_macerator_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Steam Macerator: air pollution amount (old value 0.2)"),
            new DoubleEntry("steam_macerator_soil_pollution_amount", 0.125D, 0.0D, 1000000.0D, "Steam Macerator: soil pollution amount (old value 0.125)"),
            new DoubleEntry("steam_peat_generator_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Steam Peat Generator: energy storage (old value 0)"),
            new DoubleEntry("steam_pump_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Steam Pump: energy storage (old value 0)"),
            new DoubleEntry("steam_rolling_mill_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Steam Rolling Mill: air pollution amount (old value 0.2)"),
            new DoubleEntry("steam_rolling_mill_soil_pollution_amount", 0.125D, 0.0D, 1000000.0D, "Steam Rolling Mill: soil pollution amount (old value 0.125)"),
            new DoubleEntry("steam_sharpener_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Steam Sharpener: energy storage (old value 0)"),
            new DoubleEntry("steam_solid_fluid_mixer_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Steam Solid-Fluid Mixer: energy storage (old value 0)"),
            new DoubleEntry("steam_squeezer_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Steam Squeezer: energy storage (old value 0)"),
            new DoubleEntry("steam_cable_insulator_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Steam Cable Insulator: energy storage (old value 0)"),
            new DoubleEntry("steam_boiler_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Steam Boiler: energy storage (old value 0)"),
            new DoubleEntry("steam_dryer_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Steam Dryer: energy storage (old value 0)"),
            new DoubleEntry("steam_pressure_converter_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Steam Pressure Converter: energy storage (old value 0)"),
            new DoubleEntry("teleporter_energy_storage", 500000.0D, 0.0D, Double.MAX_VALUE, "Teleporter: energy storage (old value 500000)"),
            new DoubleEntry("tree_breaker_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Tree Breaker: air pollution amount (old value 0.1)"),
            new DoubleEntry("tree_breaker_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Tree Breaker: soil pollution amount (old value 0.1)"),
            new DoubleEntry("triple_assampler_scrap_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Triple Assampler Scrap: air pollution amount (old value 0.075)"),
            new DoubleEntry("triple_assampler_scrap_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Triple Assampler Scrap: soil pollution amount (old value 0.05)"),
            new DoubleEntry("triple_centrifuge_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Triple Centrifuge: air pollution amount (old value 0.075)"),
            new DoubleEntry("triple_centrifuge_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Triple Centrifuge: soil pollution amount (old value 0.05)"),
            new DoubleEntry("advanced_combined_recycler_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Advanced Combined Recycler: air pollution amount (old value 0.075)"),
            new DoubleEntry("advanced_combined_recycler_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Advanced Combined Recycler: soil pollution amount (old value 0.05)"),
            new DoubleEntry("triple_compressor_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Triple Compressor: air pollution amount (old value 0.075)"),
            new DoubleEntry("triple_compressor_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Triple Compressor: soil pollution amount (old value 0.05)"),
            new DoubleEntry("advanced_cutting_machine_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Advanced Cutting Machine: air pollution amount (old value 0.075)"),
            new DoubleEntry("advanced_cutting_machine_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Advanced Cutting Machine: soil pollution amount (old value 0.05)"),
            new DoubleEntry("triple_electric_furnace_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Triple Electric Furnace: air pollution amount (old value 0.075)"),
            new DoubleEntry("triple_electric_furnace_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Triple Electric Furnace: soil pollution amount (old value 0.05)"),
            new DoubleEntry("advanced_extractor_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Advanced Extractor: air pollution amount (old value 0.075)"),
            new DoubleEntry("advanced_extractor_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Advanced Extractor: soil pollution amount (old value 0.05)"),
            new DoubleEntry("triple_extruding_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Triple Extruding: air pollution amount (old value 0.075)"),
            new DoubleEntry("triple_extruding_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Triple Extruding: soil pollution amount (old value 0.05)"),
            new DoubleEntry("triple_fermer_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Triple Fermer: air pollution amount (old value 0.075)"),
            new DoubleEntry("triple_fermer_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Triple Fermer: soil pollution amount (old value 0.05)"),
            new DoubleEntry("triple_gear_machine_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Triple Gear Machine: air pollution amount (old value 0.075)"),
            new DoubleEntry("triple_gear_machine_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Triple Gear Machine: soil pollution amount (old value 0.05)"),
            new DoubleEntry("advanced_macerator_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Advanced Macerator: air pollution amount (old value 0.075)"),
            new DoubleEntry("advanced_macerator_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Advanced Macerator: soil pollution amount (old value 0.05)"),
            new DoubleEntry("triple_ore_washing_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Triple Ore Washing: air pollution amount (old value 0.075)"),
            new DoubleEntry("triple_ore_washing_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Triple Ore Washing: soil pollution amount (old value 0.05)"),
            new DoubleEntry("advanced_recycler_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Advanced Recycler: air pollution amount (old value 0.075)"),
            new DoubleEntry("advanced_recycler_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Advanced Recycler: soil pollution amount (old value 0.05)"),
            new DoubleEntry("advanced_rolling_machine_air_pollution_amount", 0.075D, 0.0D, 1000000.0D, "Advanced Rolling Machine: air pollution amount (old value 0.075)"),
            new DoubleEntry("advanced_rolling_machine_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Advanced Rolling Machine: soil pollution amount (old value 0.05)"),
            new DoubleEntry("triple_solid_mixer_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Triple Solid Mixer: air pollution amount (old value 0.1)"),
            new DoubleEntry("triple_solid_mixer_energy_storage", 200.0D, 0.0D, Double.MAX_VALUE, "Triple Solid Mixer: energy storage (old value 200)"),
            new DoubleEntry("triple_solid_mixer_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Triple Solid Mixer: soil pollution amount (old value 0.1)"),
            new DoubleEntry("wireless_adjuster_energy_storage", 0.0D, 0.0D, Double.MAX_VALUE, "Wireless Adjuster: energy storage (old value 0)"),
            new DoubleEntry("ultimate_matter_air_pollution_amount", 0.045D, 0.0D, 1000000.0D, "Ultimate Matter: air pollution amount (old value 0.045)"),
            new DoubleEntry("ultimate_matter_soil_pollution_amount", 0.011D, 0.0D, 1000000.0D, "Ultimate Matter: soil pollution amount (old value 0.011)"),
            new DoubleEntry("upgrade_kit_manufacturer_energy_storage", 400.0D, 0.0D, Double.MAX_VALUE, "Upgrade Kit Manufacturer: energy storage (old value 400)"),
            new DoubleEntry("upgrade_machine_factory_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Upgrade Machine Factory: air pollution amount (old value 0.1)"),
            new DoubleEntry("upgrade_machine_factory_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Upgrade Machine Factory: soil pollution amount (old value 0.1)"),
            new DoubleEntry("rover_upgrade_station_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Rover Upgrade Station: air pollution amount (old value 0.1)"),
            new DoubleEntry("rover_upgrade_station_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Rover Upgrade Station: soil pollution amount (old value 0.1)"),
            new DoubleEntry("water_generator_air_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Water Generator: air pollution amount (old value 0.25)"),
            new DoubleEntry("water_generator_soil_pollution_amount", 0.25D, 0.0D, 1000000.0D, "Water Generator: soil pollution amount (old value 0.25)"),
            new DoubleEntry("water_rotor_assembler_air_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Water Rotor Assembler: air pollution amount (old value 0.05)"),
            new DoubleEntry("water_rotor_assembler_soil_pollution_amount", 0.05D, 0.0D, 1000000.0D, "Water Rotor Assembler: soil pollution amount (old value 0.05)"),
            new DoubleEntry("water_generator_energy_storage", 10000.0D, 0.0D, Double.MAX_VALUE, "Water Generator: energy storage (old value 10000)"),
            new DoubleEntry("weeder_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Weeder: air pollution amount (old value 0.1)"),
            new DoubleEntry("weeder_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Weeder: soil pollution amount (old value 0.1)"),
            new DoubleEntry("welding_machine_air_pollution_amount", 0.2D, 0.0D, 1000000.0D, "Welding Machine: air pollution amount (old value 0.2)"),
            new DoubleEntry("welding_machine_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Welding Machine: soil pollution amount (old value 0.1)"),
            new DoubleEntry("wire_insulator_air_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Wire Insulator: air pollution amount (old value 0.1)"),
            new DoubleEntry("wire_insulator_soil_pollution_amount", 0.1D, 0.0D, 1000000.0D, "Wire Insulator: soil pollution amount (old value 0.1)"),
            new DoubleEntry("wireless_matter_collector_air_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Wireless Matter Collector: air pollution amount (old value 0.15)"),
            new DoubleEntry("wireless_matter_collector_soil_pollution_amount", 0.15D, 0.0D, 1000000.0D, "Wireless Matter Collector: soil pollution amount (old value 0.15)")
    };

    private static final IntEntry[] ITEM_INT_ENTRIES = new IntEntry[]{
            new IntEntry("base_rod_durability", 1, 1, Integer.MAX_VALUE, "Base Rod: durability (old value 1)"),
            new IntEntry("canister_capacity", 1, 1, Integer.MAX_VALUE, "Canister: capacity (old value 1)"),
            new IntEntry("restores_nearby_durability_by_durability", 1, 1, Integer.MAX_VALUE, "Restores nearby durability by: durability (old value 1)"),
            new IntEntry("energy_tool_hoe_energy_use", 50, 0, Integer.MAX_VALUE, "Energy Tool Hoe: energy use (old value 50)"),
            new IntEntry("universal_fluid_cell_capacity", 1000, 1, Integer.MAX_VALUE, "Universal Fluid Cell: capacity (old value 1000)"),
            new IntEntry("latex_pipette_capacity", 1, 1, Integer.MAX_VALUE, "Latex Pipette: capacity (old value 1)"),
            new IntEntry("pipette_capacity", 1, 1, Integer.MAX_VALUE, "Pipette: capacity (old value 1)"),
            new IntEntry("radioprotector_capacity", 1, 1, Integer.MAX_VALUE, "Radioprotector: capacity (old value 1)"),
            new IntEntry("regular_plate_durability", 0, 1, Integer.MAX_VALUE, "Regular Plate: durability (old value 0)"),
            new IntEntry("reinforced_fluid_cell_capacity", 10000, 1, Integer.MAX_VALUE, "Reinforced Fluid Cell: capacity (old value 10000)"),
            new IntEntry("relocator_max_points", 0, 0, 10000, "Maximum saved relocator points per player, 0 = unlimited"),
            new IntEntry("relocator_max_teleport_distance", 0, 0, 1000000, "Maximum relocator teleport distance in blocks, 0 = unlimited"),
            new IntEntry("relocator_teleport_energy_cost", 100000, 0, Integer.MAX_VALUE, "Relocator EF cost per teleport"),
            new IntEntry("small_fluid_cell_capacity", 500, 1, Integer.MAX_VALUE, "Small Fluid Cell: capacity (old value 500)"),
            new IntEntry("sprayer_capacity", 1, 1, Integer.MAX_VALUE, "Sprayer: capacity (old value 1)"),
            new IntEntry("tool_cutter_durability", 60, 1, Integer.MAX_VALUE, "Tool Cutter: durability (old value 60)"),
            new IntEntry("treetap_energy_energy_use", 50, 0, Integer.MAX_VALUE, "Treetap Energy: energy use (old value 50)"),
            new IntEntry("water_rotor_radius", 4, 0, Integer.MAX_VALUE, "Water Rotor: radius (old value 4)")
    };

    private static final DoubleEntry[] ITEM_DOUBLE_ENTRIES = new DoubleEntry[]{
            new DoubleEntry("planetary_translocator_transfer_limit", 8192.0D, 0.0D, Double.MAX_VALUE, "Planetary Translocator: transfer limit (old value 8192D)"),
            new DoubleEntry("relocator_energy_capacity", 10000000.0D, 0.0D, Double.MAX_VALUE, "Relocator: energy capacity (old value 10000000)"),
            new DoubleEntry("relocator_transfer_limit", 8192.0D, 0.0D, Double.MAX_VALUE, "Relocator: transfer limit (old value 8192)"),
            new DoubleEntry("wind_meter_energy_capacity", 5000.0D, 0.0D, Double.MAX_VALUE, "Wind Meter: energy capacity (old value 5000)"),
            new DoubleEntry("wind_meter_transfer_limit", 500.0D, 0.0D, Double.MAX_VALUE, "Wind Meter: transfer limit (old value 500)"),
            new DoubleEntry("wireless_terminal_energy_capacity", 1000000.0D, 0.0D, Double.MAX_VALUE, "Wireless Terminal: energy capacity (old value 1000000)"),
            new DoubleEntry("wireless_terminal_transfer_limit", 4096.0D, 0.0D, Double.MAX_VALUE, "Wireless Terminal: transfer limit (old value 4096)")
    };

    private static final IntEntry[] MECHANIC_INT_ENTRIES = new IntEntry[]{
            new IntEntry("antimagnet_radius", 10, 1, 128, "Anti-magnet protection radius in blocks")
    };

    private static final DoubleEntry[] MECHANIC_DOUBLE_ENTRIES = new DoubleEntry[]{
    };

    public static int mechanismInt(String key, int fallback) {
        return COMMON.getInt(COMMON.mechanismInts, key, fallback);
    }

    public static double mechanismDouble(String key, double fallback) {
        return COMMON.getDouble(COMMON.mechanismDoubles, key, fallback);
    }

    public static int itemInt(String key, int fallback) {
        return COMMON.getInt(COMMON.itemInts, key, fallback);
    }

    public static double itemDouble(String key, double fallback) {
        return COMMON.getDouble(COMMON.itemDoubles, key, fallback);
    }

    public static int mechanicInt(String key, int fallback) {
        return COMMON.getInt(COMMON.mechanicInts, key, fallback);
    }

    public static double mechanicDouble(String key, double fallback) {
        return COMMON.getDouble(COMMON.mechanicDoubles, key, fallback);
    }


    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {

        public final Map<String, ModConfigSpec.IntValue> mechanismInts = new LinkedHashMap<>();
        public final Map<String, ModConfigSpec.DoubleValue> mechanismDoubles = new LinkedHashMap<>();
        public final Map<String, ModConfigSpec.IntValue> itemInts = new LinkedHashMap<>();
        public final Map<String, ModConfigSpec.DoubleValue> itemDoubles = new LinkedHashMap<>();
        public final Map<String, ModConfigSpec.IntValue> mechanicInts = new LinkedHashMap<>();
        public final Map<String, ModConfigSpec.DoubleValue> mechanicDoubles = new LinkedHashMap<>();

        private int getInt(Map<String, ModConfigSpec.IntValue> values, String key, int fallback) {
            ModConfigSpec.IntValue value = values.get(key);
            if (value == null) {
                return fallback;
            }

            try {
                return value.get();
            } catch (IllegalStateException e) {
                return fallback;
            }
        }

        private double getDouble(Map<String, ModConfigSpec.DoubleValue> values, String key, double fallback) {
            ModConfigSpec.DoubleValue value = values.get(key);
            if (value == null) {
                return fallback;
            }

            try {
                return value.get();
            } catch (IllegalStateException e) {
                return fallback;
            }
        }

        private void defineIntEntries(ModConfigSpec.Builder builder, Map<String, ModConfigSpec.IntValue> out, IntEntry[] entries) {
            for (IntEntry entry : entries) {
                out.put(entry.key, builder.comment(entry.comment).defineInRange(entry.key, entry.defaultValue, entry.min, entry.max));
            }
        }

        private void defineDoubleEntries(ModConfigSpec.Builder builder, Map<String, ModConfigSpec.DoubleValue> out, DoubleEntry[] entries) {
            for (DoubleEntry entry : entries) {
                out.put(entry.key, builder.comment(entry.comment).defineInRange(entry.key, entry.defaultValue, entry.min, entry.max));
            }
        }



        public final ModConfigSpec.BooleanValue newsystem;

        public final ModConfigSpec.BooleanValue enableEasyMode;
        public final ModConfigSpec.BooleanValue cableEasyMode;
        public final ModConfigSpec.BooleanValue enableExplosion;
        public final ModConfigSpec.BooleanValue enableLosing;

        public final ModConfigSpec.IntValue maxVein;

        public final ModConfigSpec.IntValue gasMaxVein;
        public final ModConfigSpec.IntValue gasChance;
        public final ModConfigSpec.BooleanValue airPollution;
        public final ModConfigSpec.BooleanValue soilPollution;
        public final ModConfigSpec.BooleanValue pressureWork;
        public final ModConfigSpec.BooleanValue damageRadiation;
        public final ModConfigSpec.BooleanValue informationText;
        public final ModConfigSpec.BooleanValue radiationChunksEnabled;
        public final ModConfigSpec.BooleanValue radiationDamageEnabled;
        public final ModConfigSpec.BooleanValue radiationAccumulationEnabled;
        public final ModConfigSpec.IntValue desertChance;
        public final ModConfigSpec.IntValue desertMin;
        public final ModConfigSpec.IntValue desertMax;

        public final ModConfigSpec.IntValue oceanChance;
        public final ModConfigSpec.IntValue oceanMin;
        public final ModConfigSpec.IntValue oceanMax;

        public final ModConfigSpec.IntValue deepOceanChance;
        public final ModConfigSpec.IntValue deepOceanMin;
        public final ModConfigSpec.IntValue deepOceanMax;

        public final ModConfigSpec.IntValue riverChance;
        public final ModConfigSpec.IntValue riverMin;
        public final ModConfigSpec.IntValue riverMax;

        public final ModConfigSpec.IntValue savannaChance;
        public final ModConfigSpec.IntValue savannaMin;
        public final ModConfigSpec.IntValue savannaMax;

        public final ModConfigSpec.IntValue defaultChance;
        public final ModConfigSpec.IntValue defaultMin;
        public final ModConfigSpec.IntValue defaultMax;
        public final ModConfigSpec.BooleanValue enableAllProfessions;

        public final ModConfigSpec.BooleanValue enableEngineer;
        public final ModConfigSpec.BooleanValue enableMechanic;
        public final ModConfigSpec.BooleanValue enableNuclear;
        public final ModConfigSpec.BooleanValue enableMetallurg;
        public final ModConfigSpec.BooleanValue enableChemist;
        public final ModConfigSpec.BooleanValue enableBotanist;
        public final ModConfigSpec.BooleanValue cooldownEnabled;
        public final ModConfigSpec.IntValue hillChanceBonus;
        public final ModConfigSpec.IntValue taigaSnowyChancePenalty;
        public final ModConfigSpec.IntValue baseChance;
        public final ModConfigSpec.IntValue gasRollChance;
        public final ModConfigSpec.IntValue gasVeinMaxCol;
        public final ModConfigSpec.BooleanValue defaultSpawnEnabled;

        public Common(ModConfigSpec.Builder builder) {
            builder.push("Generation ores");
            defaultSpawnEnabled = builder
                    .comment("Enable approximately default ore generation (false = vein system)")
                    .define("defaultSpawnEnabled", false);

            builder.pop();
            builder.push("Mechanisms");
            cooldownEnabled = builder
                    .comment("Enable cooldown for mechanisms (false = no cooldown)")
                    .define("cooldownEnabled", true);

            builder.pop();
            builder.push("General");
            maxVein = builder.comment("Maximum amount of ore in a vein").defineInRange("maxVein", 30000, 1, Integer.MAX_VALUE);
            gasMaxVein = builder.comment("Maximum amount of mb in a gas vein (not natural gas)").defineInRange("gasMaxVein", 200000, 1, Integer.MAX_VALUE);
            gasChance = builder.comment("Chance to get a gas vein (not natural gas)").defineInRange("gasChance", 25, 1, 100);
            hillChanceBonus = builder
                    .comment("Additional chance if biome is hill")
                    .defineInRange("hillChanceBonus", 25, 0, 100);

            taigaSnowyChancePenalty = builder
                    .comment("Chance penalty for taiga or snowy biomes")
                    .defineInRange("taigaSnowyChancePenalty", 15, 0, 100);

            baseChance = builder
                    .comment("Base chance threshold")
                    .defineInRange("baseChance", 15, 0, 100);

            gasRollChance = builder
                    .comment("Chance (in %) to NOT generate gas vein")
                    .defineInRange("gasRollChance", 85, 0, 100);

            gasVeinMaxCol = builder
                    .comment("Gas vein capacity")
                    .defineInRange("gasVeinMaxCol", 600000, 1, Integer.MAX_VALUE);
            builder.pop();
            builder.push("Information Text");
            informationText = builder.comment("Informational text when logging the game").define("enable", true);
            builder.pop();
            builder.push("Oil Vein");
            desertChance = builder.comment("Chance % for oil vein in desert")
                    .defineInRange("desertChance", 65, 0, 100);
            desertMin = builder.defineInRange("desertMin", 250000, 0, Integer.MAX_VALUE);
            desertMax = builder.defineInRange("desertMax", 1000000, 0, Integer.MAX_VALUE);

            oceanChance = builder.defineInRange("oceanChance", 60, 0, 100);
            oceanMin = builder.defineInRange("oceanMin", 250000, 0, Integer.MAX_VALUE);
            oceanMax = builder.defineInRange("oceanMax", 750000, 0, Integer.MAX_VALUE);

            deepOceanChance = builder.defineInRange("deepOceanChance", 65, 0, 100);
            deepOceanMin = builder.defineInRange("deepOceanMin", 150000, 0, Integer.MAX_VALUE);
            deepOceanMax = builder.defineInRange("deepOceanMax", 500000, 0, Integer.MAX_VALUE);

            riverChance = builder.defineInRange("riverChance", 50, 0, 100);
            riverMin = builder.defineInRange("riverMin", 100000, 0, Integer.MAX_VALUE);
            riverMax = builder.defineInRange("riverMax", 250000, 0, Integer.MAX_VALUE);

            savannaChance = builder.defineInRange("savannaChance", 50, 0, 100);
            savannaMin = builder.defineInRange("savannaMin", 150000, 0, Integer.MAX_VALUE);
            savannaMax = builder.defineInRange("savannaMax", 500000, 0, Integer.MAX_VALUE);

            defaultChance = builder.defineInRange("defaultChance", 11, 0, 100);
            defaultMin = builder.defineInRange("defaultMin", 0, 0, Integer.MAX_VALUE);
            defaultMax = builder.defineInRange("defaultMax", 300000, 0, Integer.MAX_VALUE);
            builder.pop();
            builder.push("VillagerProfessions");

            enableAllProfessions = builder
                    .comment("Enable registration of ALL custom villager professions")
                    .define("enableAllProfessions", true);

            enableEngineer = builder.define("enableEngineer", true);
            enableMechanic = builder.define("enableMechanic", true);
            enableNuclear = builder.define("enableNuclear", true);
            enableMetallurg = builder.define("enableMetallurg", true);
            enableChemist = builder.define("enableChemist", true);
            enableBotanist = builder.define("enableBotanist", true);

            builder.pop();

            builder.push("TransformerMode");
            newsystem = builder.comment("Transformer mode enabled").define("TransformerMode", true);
            enableEasyMode = builder.comment("Unchecking the tier").define("enableUnchecking", false);
            cableEasyMode = builder.comment("Unlimiting the conduction of energy in the cable").define("cableUnlimiting", false);
            enableExplosion = builder.comment("Enable explosion from mechanisms if transformer mode is on").define("enableExplosion", true);
            enableLosing = builder.comment("Enable lose energy in cables if transformer mode is on").define("enableLosing", true);
            builder.pop();

            builder.push("pollution");
            airPollution = builder.comment("Air pollution").define("airPollution", true);
            soilPollution = builder.comment("Soil pollution").define("soilPollution", true);
            builder.pop();

            builder.push("Pressure Network");
            pressureWork = builder
                    .comment("Remove the pressure level restriction in the pressure system (allows machines to work at any pressure level)")
                    .define("pressureRestriction", true);
            builder.pop();

            builder.push("Radiation");
            damageRadiation = builder
                    .comment("Enable damage from radiation")
                    .define("radiationDamageEnabled", true);
            radiationChunksEnabled = builder
                    .comment("Enable radiation chunks generation")
                    .define("radiationChunksEnabled", true);

            radiationDamageEnabled = builder
                    .comment("Enable damage from radiation chunk")
                    .define("radiationDamageEnabled", true);

            radiationAccumulationEnabled = builder
                    .comment("Enable radiation accumulation on the player")
                    .define("radiationAccumulationEnabled", true);
            builder.pop();

            builder.push("mechanisms_unique");
            defineIntEntries(builder, mechanismInts, MECHANISM_INT_ENTRIES);
            defineDoubleEntries(builder, mechanismDoubles, MECHANISM_DOUBLE_ENTRIES);
            builder.pop();

            builder.push("items_unique");
            defineIntEntries(builder, itemInts, ITEM_INT_ENTRIES);
            defineDoubleEntries(builder, itemDoubles, ITEM_DOUBLE_ENTRIES);
            builder.pop();

            builder.push("mechanics_unique");
            defineIntEntries(builder, mechanicInts, MECHANIC_INT_ENTRIES);
            defineDoubleEntries(builder, mechanicDoubles, MECHANIC_DOUBLE_ENTRIES);
            builder.pop();

        }
    }
}
