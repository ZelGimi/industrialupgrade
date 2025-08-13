package com.denfop.api.guidebook;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockAnvil;
import com.denfop.blocks.BlockStrongAnvil;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.*;
import com.denfop.network.packet.PacketUpdateInformationAboutQuestsPlayer;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.utils.ModUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.*;
import java.util.stream.Collectors;

public class GuideBookCore {

    public static GuideBookCore instance;
    public static Map<UUID, Map<String, List<String>>> uuidGuideMap = new HashMap<>();
    List<GuideTab> guideTabs = new ArrayList<>();
    Map<GuideTab, List<Quest>> guideTabListMap = new HashMap<>();

    public GuideBookCore() {
        if (instance == null) {
            instance = this;
        }
    }

    public static Map<UUID, Map<String, List<String>>> getUuidGuideMap() {
        return uuidGuideMap;
    }

    public static ItemStack getBlockStack(IMultiTileBlock block) {
        return TileBlockCreator.instance.get(block.getIDBlock()).getItemStack();
    }

    public static void init() {

        GuideTab guideTab = new GuideTab("main", ItemStackHelper.fromData(IUItem.blockadmin), "main");

        Quest.Builder.create().name("start").icon(ItemStackHelper.fromData(IUItem.book)).tab(guideTab).shape(Shape.EPIC).description(
                "start").position(0,
                0).build();
        Quest.Builder.create().name("energy").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.efReader)).shape(Shape.DEFAULT).description(
                "energy").position(35,
                0).build();
        Quest.Builder.create().name("heat").tab(guideTab).icon(getBlockStack(BlockBaseMachine3.cooling)).shape(Shape.DEFAULT).description("heat").position(35,
                35).build();
        Quest.Builder.create().name("vein").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.heavyore)).shape(Shape.DEFAULT).description("vein").position(0,
                35).build();
        Quest.Builder.create().name("energies").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.imp_se_generator)).shape(Shape.DEFAULT).description(
                "energies").position(70,
                0).build();
        Quest.Builder.create().name("radiation").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 40)).shape(Shape.DEFAULT).description(
                "radiation").position(70,
                35).build();
        Quest.Builder.create().name("volcano").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.basalts)).shape(Shape.DEFAULT).description(
                "volcano").position(-35,
                0).build();
        Quest.Builder.create().name("pollution").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.pollutionDevice)).shape(Shape.DEFAULT).description(
                "pollution").position(-35,
                -35).build();
        Quest.Builder.create().name("bee").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.jarBees)).shape(Shape.DEFAULT).description(
                "bee").position(0,
                -35).build();
        Quest.Builder.create().name("crop").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.crops)).shape(Shape.DEFAULT).description(
                "crop").position(35,
                -35).build();
        Quest.Builder.create().name("gasvein").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.gasBlock)).shape(Shape.DEFAULT).description(
                "gasvein").position(-35,
                35).build();
        Quest.Builder.create().name("mineralvein").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.mineral)).shape(Shape.DEFAULT).description(
                "mineralvein").position(70,
                -35).build();
        Quest.Builder.create().name("rubber_tree").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.rubberSapling.getItem())).shape(Shape.DEFAULT).description(
                "rubber_tree").position(-70,
                0).build();
        Quest.Builder.create().name("other_features").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.ore2, 1, 6)).shape(Shape.DEFAULT).description(
                "other_features").position(-70,
                -35).build();
        Quest.Builder.create().name("oil_vein").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.oilblock)).shape(Shape.DEFAULT).description(
                "oil_vein").position(-70,
                35).build();
        Quest.Builder.create().name("villager").tab(guideTab).icon(ItemStackHelper.fromData(Items.EMERALD)).shape(Shape.DEFAULT).description(
                "villager").position(-70,
                70).build();
        Quest.Builder.create().name("pipette").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.pipette)).shape(Shape.DEFAULT).description(
                "pipette").position(-35,
                70).build();
        Quest.Builder.create().name("recipe_schedule").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.recipe_schedule)).shape(Shape.DEFAULT).description(
                "recipe_schedule").position(-0,
                70).build();
        Quest.Builder.create().name("reactor_simulate").tab(guideTab).icon(getBlockStack(BlockBaseMachine3.simulation_reactors)).shape(Shape.DEFAULT).description(
                "reactor_simulate").position(35,
                70).build();
        Quest.Builder.create().name("reactor_logic").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.quad_mox_fuel_rod)).shape(Shape.DEFAULT).description(
                "reactor_logic").position(70,
                70).build();
        Quest.Builder.create().name("space").tab(guideTab).icon(getBlockStack(BlockBaseMachine3.research_table_space)).shape(Shape.DEFAULT).description(
                "space").position(-0,
                105).build();
        Quest.Builder.create().name("colony").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.colonial_building)).shape(Shape.DEFAULT).description(
                "colony").position(35,
                105).build();
        GuideTab primalTab = new GuideTab("primal", getBlockStack(BlockAnvil.block_anvil), "primal");
        Quest.Builder.create().name("anvil").localizationItem().useItemInform().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.anvil)).position(0, 0).build();
        Quest.Builder.create().name("forge_hammer").localizationItem().noDescription()
                .tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.ForgeHammer)).position(40, 0).prev("anvil").build();
        Quest.Builder.create().name("casings").noDescription().localizationItem().tab(primalTab).itemStack(
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 773)).position(80, 0).prev(
                "forge_hammer").build();
        Quest.Builder.create().name("smelterystart").localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.smeltery)).position(120, 0).prev(
                "casings").build();
        Quest.Builder.create().name("smelteryforms").localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 497),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 496)).position(150, 0).prev(
                "smelterystart").build();
        Quest.Builder.create().name("electrum").localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.iuingot, 1, 13)).position(170,
                -40).prev("smelteryforms").build();
        Quest.Builder.create().name("squeezer").localizationItem().useItemInform().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.squeezer)).position(200,
                -80).prev("electrum").build();
        Quest.Builder.create().name("dryer").localizationItem().useItemInform().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.dryer)).position(240,
                -80).prev("squeezer").build();
        Quest.Builder.create().name("raw_latex").localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.rawLatex)).position(280,
                -80).prev("dryer").build();
        Quest.Builder.create().name("latex").localizationItem().tab(primalTab).itemStack(IUItem.latex).position(320,
                -80).prev("raw_latex").build();
        Quest.Builder.create().name("primal_heater").useItemInform().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.primalFluidHeater)).position(220,
                -40).prev("electrum").build();
        Quest.Builder.create().name("steam").localizationItem().tab(primalTab).fluidStack(new FluidStack(FluidName.fluidsteam.getInstance().get(), 50)).position(280,
                -40).prev("primal_heater").build();
        Quest.Builder.create().name("superheated_steam").localizationItem().tab(primalTab).fluidStack(new FluidStack(FluidName.fluidsuperheated_steam.getInstance().get(), 50)).position(330,
                -40).prev("steam").build();
        Quest.Builder.create().name("ferromanganese").localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.alloysingot, 1,
                9)).position(190,
                0).prev("smelteryforms").build();
        Quest.Builder.create().name("molot").noDescription().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.molot)).position(250,
                -20).prev("ferromanganese").build();
        Quest.Builder.create().name("diamond").localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(Items.DIAMOND)).position(303,
                -20).prev("molot").build();
        Quest.Builder.create().name("compressor").useItemInform().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.blockCompressor)).position(250,
                20).prev("ferromanganese").build();
        Quest.Builder.create().name("primal_rolling").useItemInform().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.basemachine2, 1, 124)).position(290,
                20).prev("compressor").build();
        Quest.Builder.create().name("primal_wire_insulator").useItemInform().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.blockwireinsulator)).position(330,
                20).prev("primal_rolling").build();
        Quest.Builder.create().name("macerator").useItemInform().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.blockMacerator)).position(220,
                70).prev("ferromanganese").build();
        Quest.Builder.create().name("flint_dust").localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.iudust, 1, 60)).position(260,
                70).prev("macerator").build();
        Quest.Builder.create().name("silicon_handler").useItemInform().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.primalSiliconCrystal)).position(300,
                70).prev("flint_dust").build();
        Quest.Builder.create().name("primal_fluid_integrator").useItemInform().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.fluidIntegrator)).position(350,
                70).prev("silicon_handler").build();

        Quest.Builder.create().name("primal_information").icon(ItemStackHelper.fromData(IUItem.book)).tab(primalTab).position(0, -40).build();
        GuideTab steamTab = new GuideTab("steamTab", getBlockStack(BlockBaseMachine3.steamboiler), "steam");
        Quest.Builder.create().name("steam_machine_block").itemStack(ItemStackHelper.fromData(IUItem.blockResource, 1, 12)).localizationItem().tab(steamTab).position(0, 0).build();
        Quest.Builder.create().name("steamboiler").itemStack(getBlockStack(BlockBaseMachine3.steamboiler)).prev("steam_machine_block").localizationItem().tab(steamTab).position(40, 0).build();
        Quest.Builder.create().name("steampressureconverter").itemStack(getBlockStack(BlockBaseMachine3.steampressureconverter)).prev("steamboiler").localizationItem().tab(steamTab).position(80, 0).build();
        Quest.Builder.create().name("silicon_crystal").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 493)).useItemInform().prev(
                "steampressureconverter").localizationItem().tab(steamTab).position(120, 0).build();
        Quest.Builder.create().name("primal_laser_polisher").itemStack(getBlockStack(BlockPrimalLaserPolisher.primal_laser_polisher),
                ItemStackHelper.fromData(IUItem.laser)).useItemInform().prev(
                "silicon_crystal").localizationItem().tab(steamTab).position(160, 0).build();
        Quest.Builder.create().name("steam_polisher").itemStack(getBlockStack(BlockBaseMachine3.steam_sharpener)).prev(
                "primal_laser_polisher").useItemInform().localizationItem().tab(steamTab).position(200, 0).build();
        Quest.Builder.create().name("steam_ampere_generator").itemStack(getBlockStack(BlockBaseMachine3.steam_ampere_generator)).prev(
                "steam_polisher").localizationItem().tab(steamTab).position(240, 0).build();
        Quest.Builder.create().name("steam_electrolyzer").itemStack(getBlockStack(BlockBaseMachine3.steam_electrolyzer)).prev(
                "steam_ampere_generator").localizationItem().tab(steamTab).position(280, 0).build();
        Quest.Builder.create().name("oxygen").fluidStack(new FluidStack(FluidName.fluidoxy.getInstance().get(), 1000)).prev(
                "steam_electrolyzer").useItemInform().localizationItem().tab(steamTab).position(320, 0).build();
        Quest.Builder.create().name("primal_gas_chamber").itemStack(getBlockStack(BlockGasChamber.primal_gas_chamber)).prev(
                "oxygen").localizationItem().useItemInform().tab(steamTab).position(360, 0).build();
        Quest.Builder.create().name("sulfurtrioxide").fluidStack(new FluidStack(FluidName.fluidsulfurtrioxide.getInstance().get(), 1000)).prev(
                "primal_gas_chamber").useItemInform().localizationItem().tab(steamTab).position(400, 0).build();
        Quest.Builder.create().name("fluidcoppersulfate").fluidStack(new FluidStack(FluidName.fluidcoppersulfate.getInstance().get(), 1000)).prev(
                "sulfurtrioxide").useItemInform().localizationItem().tab(steamTab).position(440, 0).build();
        Quest.Builder.create().name("circuit_board").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 487)).prev(
                "fluidcoppersulfate").useItemInform().localizationItem().tab(steamTab).position(480, 0).build();
        Quest.Builder.create().name("primal_programming_table").itemStack(getBlockStack(BlockPrimalProgrammingTable.primal_programming_table)).prev(
                "circuit_board").useItemInform().localizationItem().tab(steamTab).position(520, 0).build();
        Quest.Builder.create().name("programmed_circuit_board").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 488)).prev(
                "primal_programming_table").useItemInform().localizationItem().tab(steamTab).position(560, 0).build();
        Quest.Builder.create().name("steam_handler_ore").itemStack(getBlockStack(BlockBaseMachine3.steam_handler_ore)).prev(
                "programmed_circuit_board").useItemInform().localizationItem().tab(steamTab).position(560, 40).build();
        Quest.Builder.create().name("impurity_coal_dust").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 498)).prev(
                "steam_handler_ore").useItemInform().localizationItem().tab(steamTab).position(480, 60).build();
        Quest.Builder.create().name("alloy_coal_dust").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 499)).prev(
                "impurity_coal_dust").useItemInform().localizationItem().tab(steamTab).position(440, 80).build();
        Quest.Builder.create().name("steel").itemStack(ItemStackHelper.fromData(IUItem.iuingot, 1, 23)).prev(
                "alloy_coal_dust").useItemInform().localizationItem().tab(steamTab).position(340, 80).build();
        Quest.Builder.create().name("steel_hammer").itemStack(ItemStackHelper.fromData(IUItem.steelHammer)).prev(
                "steel").localizationItem().useItemInform().tab(steamTab).position(300, 40).build();
        Quest.Builder.create().name("block_strong_anvil").itemStack(getBlockStack(BlockStrongAnvil.block_strong_anvil)).prev(
                "steel").localizationItem().useItemInform().tab(steamTab).position(385, 110).build();
        Quest.Builder.create().name("refractory_furnace").itemStack(getBlockStack(BlockRefractoryFurnace.refractory_furnace)).prev(
                "block_strong_anvil").useItemInform().localizationItem().tab(steamTab).position(425, 110).build();
        Quest.Builder.create().name("mini_smeltery").itemStack(getBlockStack(BlockMiniSmeltery.mini_smeltery)).prev(
                "refractory_furnace").useItemInform().localizationItem().tab(steamTab).position(465, 110).build();
        Quest.Builder.create().name("primal_soldering_mechanism").itemStack(getBlockStack(BlockSolderingMechanism.primal_soldering_mechanism)).prev(
                "steel").useItemInform().localizationItem().tab(steamTab).position(280, 80).build();
        Quest.Builder.create().name("steam_converter").itemStack(getBlockStack(BlockBaseMachine3.steam_converter)).prev(
                "primal_soldering_mechanism").localizationItem().tab(steamTab).position(280, 120).build();

        Quest.Builder.create().name("steam_peat_generator").itemStack(getBlockStack(BlockBaseMachine3.steam_peat_generator)).prev(
                "steam_converter").localizationItem().tab(steamTab).position(320, 160).build();
        Quest.Builder.create().name("steam_pump").itemStack(getBlockStack(BlockBaseMachine3.steam_pump)).prev(
                "steam_converter").localizationItem().tab(steamTab).useItemInform().position(240, 160).build();

        Quest.Builder.create().name("steam_boiler_controller").itemStack(getBlockStack(BlockSteamBoiler.steam_boiler_controller)).prev(
                "steam_pump").localizationItem().tab(steamTab).position(200, 160).build();

        Quest.Builder.create().name("steam_storage").itemStack(getBlockStack(BlockBaseMachine3.steam_storage)).prev(
                "steam_boiler_controller").localizationItem().tab(steamTab).position(160, 160).build();

        Quest.Builder.create().name("steam_quarry").itemStack(getBlockStack(BlockBaseMachine3.steam_quarry),
                ModUtils.setSize(getBlockStack(BlockBaseMachine3.quarry_pipe), 32), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 508)).prev(
                "steam_storage").localizationItem().tab(steamTab).useItemInform().position(120, 160).build();

        Quest.Builder.create().name("adv_steam_quarry").itemStack(getBlockStack(BlockBaseMachine3.adv_steam_quarry), ItemStackHelper.fromData(IUItem.crafting_elements, 1, 517)).prev(
                "steam_quarry").localizationItem().tab(steamTab).useItemInform().position(80, 160).build();

        Quest.Builder.create().name("steam_crystal_charge").itemStack(getBlockStack(BlockBaseMachine3.steam_crystal_charge)).prev(
                "primal_soldering_mechanism").localizationItem().tab(steamTab).position(240, 80).build();

        Quest.Builder.create().name("steam_fluid_heater").itemStack(getBlockStack(BlockBaseMachine3.steam_fluid_heater)).prev(
                "steam_crystal_charge").localizationItem().tab(steamTab).position(240, 40).build();
        Quest.Builder.create().name("titanium_steel").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                504)).prev(
                "steam_crystal_charge").localizationItem().useItemInform().tab(steamTab).position(200, 80).build();

        Quest.Builder.create().name("electronics_assembler").itemStack(getBlockStack(BlockElectronicsAssembler.electronics_assembler)).useItemInform().prev(
                "titanium_steel").localizationItem().tab(steamTab).position(160, 80).build();
        Quest.Builder.create().name("electronic_circuit").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                272)).useItemInform().prev(
                "electronics_assembler").localizationItem().tab(steamTab).position(120, 80).build();
        Quest.Builder.create().name("machine_casing").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1,
                137)).useItemInform().prev(
                "electronic_circuit").localizationItem().tab(steamTab).position(80, 80).build();

        Quest.Builder.create().name("blast_furnace_main").itemStack(getBlockStack(BlockBlastFurnace.blast_furnace_main)).prev(
                "machine_casing").localizationItem().useItemInform().tab(steamTab).position(40, 80).build();
        GuideTab baseElectricTab = new GuideTab("baseElectricTab", ItemStackHelper.fromData(IUItem.blockResource, 1,
                8), "baseElectric");
        Quest.Builder.create().name("elemotor").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276)).localizationItem().useItemInform().tab(baseElectricTab).position(0, 0).build();
        Quest.Builder.create().name("liqued_heater").itemStack(getBlockStack(BlockBaseMachine3.fluid_heat)).localizationItem().useItemInform().tab(baseElectricTab).prev("elemotor").position(40, 0).build();
        Quest.Builder.create().name("generator").itemStack(getBlockStack(BlockBaseMachine3.generator_iu)).localizationItem().useItemInform().tab(baseElectricTab).prev("liqued_heater").position(80, 0).build();
        Quest.Builder.create().name("redstone_generator").itemStack(getBlockStack(BlockBaseMachine3.redstone_generator)).localizationItem().useItemInform().tab(baseElectricTab).prev("generator").position(80, -40).build();
        Quest.Builder.create().name("geogenerator").itemStack(getBlockStack(BlockBaseMachine3.geogenerator_iu)).localizationItem().useItemInform().tab(baseElectricTab).prev("generator").position(120, 0).build();
        Quest.Builder.create().name("solid_refrigerator").itemStack(getBlockStack(BlockBaseMachine3.solid_cooling)).localizationItem().useItemInform().tab(baseElectricTab).prev("geogenerator").position(160, 0).build();
        Quest.Builder.create().name("base_machines").itemStack(getBlockStack(BlockSimpleMachine.macerator_iu),
                getBlockStack(BlockSimpleMachine.compressor_iu), getBlockStack(BlockSimpleMachine.extractor_iu),
                getBlockStack(BlockSimpleMachine.furnace_iu), getBlockStack(BlockMoreMachine2.rolling),
                getBlockStack(BlockMoreMachine2.cutting), getBlockStack(BlockMoreMachine2.extruder)).localizationItem().useItemInform().tab(baseElectricTab).prev(
                "solid_refrigerator").position(200, 0).build();
        Quest.Builder.create().name("generator_microchip").itemStack(getBlockStack(BlockBaseMachine.generator_microchip)).localizationItem().useItemInform().tab(baseElectricTab).prev("base_machines").position(240, 0).build();
        Quest.Builder.create().name("alloy_smelter").itemStack(getBlockStack(BlockBaseMachine.alloy_smelter)).localizationItem().useItemInform().tab(baseElectricTab).prev("generator_microchip").position(280, 0).build();
        Quest.Builder.create().name("gearing").itemStack(getBlockStack(BlockMoreMachine3.gearing)).localizationItem().useItemInform().tab(baseElectricTab).prev("alloy_smelter").position(320, 0).build();
        Quest.Builder.create().name("electronic_assembler").itemStack(getBlockStack(BlockBaseMachine3.electronic_assembler)).localizationItem().useItemInform().tab(baseElectricTab).prev("gearing").position(360, 0).build();
        Quest.Builder.create().name("plastic_creator").itemStack(getBlockStack(BlockBaseMachine2.plastic_creator)).localizationItem().useItemInform().tab(baseElectricTab).prev("electronic_assembler").position(360, 40).build();
        Quest.Builder.create().name("plastic_plate_creator").itemStack(getBlockStack(BlockBaseMachine2.plastic_plate_creator)).localizationItem().useItemInform().tab(baseElectricTab).prev("plastic_creator").position(320, 40).build();
        Quest.Builder.create().name("welding").itemStack(getBlockStack(BlockBaseMachine3.welding)).localizationItem().useItemInform().tab(baseElectricTab).prev("plastic_plate_creator").position(280, 40).build();
        Quest.Builder.create().name("orewashing").itemStack(getBlockStack(BlockMoreMachine3.orewashing)).localizationItem().useItemInform().tab(baseElectricTab).prev("welding").position(240, 40).build();
        Quest.Builder.create().name("nitrate_dust").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 456),
                ItemStackHelper.fromData(IUItem.crafting_elements, 1, 460)).localizationItem().useItemInform().tab(baseElectricTab).prev(
                "orewashing").position(200, 40).build();
        Quest.Builder.create().name("item_divider").itemStack(getBlockStack(BlockBaseMachine3.item_divider)).localizationItem().useItemInform().tab(baseElectricTab).prev("nitrate_dust").position(160, 40).build();
        Quest.Builder.create().name("nitrogen").fluidStack(new FluidStack(FluidName.fluidazot.getInstance().get(), 1000)).localizationItem().useItemInform().tab(baseElectricTab).prev(
                "item_divider").position(120, 40).build();
        Quest.Builder.create().name("advanced_circuit").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 273)).localizationItem().useItemInform().tab(baseElectricTab).prev("nitrogen").position(120, 80).build();
        Quest.Builder.create().name("gas_sensor").itemStack(ItemStackHelper.fromData(IUItem.gasSensor, 1)).localizationItem().useItemInform().tab(baseElectricTab).prev("advanced_circuit").position(160, 80).build();
        Quest.Builder.create().name("gas").fluidStack(new FluidStack(FluidName.fluidgas.getInstance().get(), 1000)).localizationItem().useItemInform().tab(baseElectricTab).prev(
                "gas_sensor").position(200, 80).build();
        Quest.Builder.create().name("triple_solid_mixer").itemStack(getBlockStack(BlockBaseMachine3.triple_solid_mixer)).localizationItem().useItemInform().tab(baseElectricTab).prev("gas").position(240, 80).build();
        Quest.Builder.create().name("gas_combiner").itemStack(getBlockStack(BlockBaseMachine3.gas_combiner)).localizationItem().useItemInform().tab(baseElectricTab).prev("triple_solid_mixer").position(280, 80).build();
        Quest.Builder.create().name("nitrogenhydride").fluidStack(new FluidStack(FluidName.fluidnitrogenhydride.getInstance().get(), 1000)).localizationItem().useItemInform().tab(baseElectricTab).prev(
                "gas_combiner").position(320, 80).build();
        Quest.Builder.create().name("nitrogenoxy").fluidStack(new FluidStack(FluidName.fluidnitrogenoxy.getInstance().get(), 1000)).localizationItem().useItemInform().tab(baseElectricTab).prev(
                "nitrogenhydride").position(360, 80).build();
        Quest.Builder.create().name("nitrogendioxide").fluidStack(new FluidStack(FluidName.fluidnitrogendioxide.getInstance().get(), 1000)).localizationItem().useItemInform().tab(baseElectricTab).prev(
                "nitrogenoxy").position(400, 80).build();

        Quest.Builder.create().name("nitricacid").fluidStack(new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 1000)).localizationItem().useItemInform().tab(baseElectricTab).prev(
                "nitrogendioxide").position(400, 120).build();
        Quest.Builder.create().name("fluid_integrator").itemStack(getBlockStack(BlockBaseMachine3.fluid_integrator)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("nitricacid").position(360, 120).build();
        Quest.Builder.create().name("silver_nitrate_dust").itemStack(ItemStackHelper.fromData(IUItem.iudust, 1, 62)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("fluid_integrator").position(320, 120).build();
        Quest.Builder.create().name("crushed_uranium_ore").itemStack(ItemStackHelper.fromData(IUItem.crushed, 1, 24)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("silver_nitrate_dust").position(280, 120).build();
        Quest.Builder.create().name("radioactive_handler_ore").itemStack(getBlockStack(BlockBaseMachine3.radioactive_handler_ore)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("crushed_uranium_ore").position(240, 120).build();
        Quest.Builder.create().name("industrial_ore_purifier").itemStack(getBlockStack(BlockBaseMachine3.industrial_ore_purifier)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("radioactive_handler_ore").position(200, 120).build();
        Quest.Builder.create().name("se_sensor").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 79)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("industrial_ore_purifier").position(160, 120).build();
        Quest.Builder.create().name("se_gen").itemStack(getBlockStack(BlockSolarEnergy.se_gen)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("se_sensor").position(120, 120).build();
        Quest.Builder.create().name("gen_sunnarium_plate").itemStack(getBlockStack(BlockSunnariumMaker.gen_sunnarium_plate)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("se_gen").position(80, 120).build();
        Quest.Builder.create().name("gen_sunnarium").itemStack(getBlockStack(BlockSunnariumPanelMaker.gen_sunnarium)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("gen_sunnarium_plate").position(40, 120).build();
        Quest.Builder.create().name("calcium_carbide").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 482)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("gen_sunnarium").position(0, 120).build();
        Quest.Builder.create().name("acetylene").fluidStack(new FluidStack(FluidName.fluidacetylene.getInstance().get(), 1000)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("calcium_carbide").position(0, 160).build();
        Quest.Builder.create().name("polymerizer").itemStack(getBlockStack(BlockBaseMachine3.polymerizer)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("acetylene").position(40, 160).build();
        Quest.Builder.create().name("solid_fluid_integrator").itemStack(getBlockStack(BlockBaseMachine3.solid_fluid_integrator)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("polymerizer").position(80, 160).build();
        Quest.Builder.create().name("polyeth").fluidStack(new FluidStack(FluidName.fluidpolyeth.getInstance().get(), 1000)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("solid_fluid_integrator").position(120, 160).build();
        Quest.Builder.create().name("fluid_separator").itemStack(getBlockStack(BlockBaseMachine3.fluid_separator)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("polyeth").position(160, 160).build();
        Quest.Builder.create().name("propane").fluidStack(new FluidStack(FluidName.fluidpropane.getInstance().get(), 1000)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("fluid_separator").position(200, 160).build();
        Quest.Builder.create().name("bromine").fluidStack(new FluidStack(FluidName.fluidbromine.getInstance().get(), 1000)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("propane").position(240, 160).build();
        Quest.Builder.create().name("propylene").fluidStack(new FluidStack(FluidName.fluidpropylene.getInstance().get(), 1000)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("bromine").position(280, 160).build();
        Quest.Builder.create().name("plast").itemStack(ItemStackHelper.fromData(IUItem.plast)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("propylene").position(320, 160).build();
        Quest.Builder.create().name("plastic_plate").itemStack(ItemStackHelper.fromData(IUItem.plastic_plate)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("plast").position(360, 160).build();
        Quest.Builder.create().name("handler_ho").itemStack(getBlockStack(BlockBaseMachine1.handler_ho)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("plastic_plate").position(400, 160).build();
        Quest.Builder.create().name("refiner").itemStack(getBlockStack(BlockRefiner.refiner)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("handler_ho").position(440, 160).build();
        Quest.Builder.create().name("gen_disel").itemStack(getBlockStack(BlockBaseMachine2.gen_disel)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("refiner").position(480, 160).build();
        Quest.Builder.create().name("gen_pet").itemStack(getBlockStack(BlockBaseMachine2.gen_pet)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("refiner").position(480, 200).build();
        Quest.Builder.create().name("fluid_cooling").itemStack(getBlockStack(BlockBaseMachine3.fluid_cooling)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("refiner").position(440, 240).build();
        Quest.Builder.create().name("simple_wind_generator").itemStack(getBlockStack(BlockBaseMachine3.simple_wind_generator)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("fluid_cooling").position(480, 240).build();
        Quest.Builder.create().name("simple_water_generator").itemStack(getBlockStack(BlockBaseMachine3.simple_water_generator)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("fluid_cooling").position(440, 280).build();
        Quest.Builder.create().name("adv_alloy_smelter").itemStack(getBlockStack(BlockBaseMachine1.adv_alloy_smelter)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("fluid_cooling").position(400, 240).build();
        Quest.Builder.create().name("centrifuge").itemStack(getBlockStack(BlockMoreMachine3.centrifuge_iu)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("adv_alloy_smelter").position(360, 240).build();
        Quest.Builder.create().name("enrichment").itemStack(getBlockStack(BlockBaseMachine1.enrichment)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("centrifuge").position(320, 240).build();
        Quest.Builder.create().name("nuclear_waste_recycler").itemStack(getBlockStack(BlockBaseMachine3.nuclear_waste_recycler)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("enrichment").position(280, 240).build();
        Quest.Builder.create().name("radioactive_waste").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 443)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("nuclear_waste_recycler").position(240, 240).build();
        GuideTab improvedElectricTab = new GuideTab("improvedElectricTab", getBlockStack(BlockBaseMachine3.research_table_space),
                "improvedElectricTab");
        Quest.Builder.create().name("laser_polisher").itemStack(getBlockStack(BlockBaseMachine3.laser_polisher)).localizationItem().noDescription().tab(improvedElectricTab)
                .position(0, 0).build();
        Quest.Builder.create().name("farmer").itemStack(getBlockStack(BlockMoreMachine3.farmer)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("laser_polisher").position(40, -40).build();
        Quest.Builder.create().name("fertilizer").itemStack(ItemStackHelper.fromData(IUItem.fertilizer)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("farmer").position(80, -40).build();
        Quest.Builder.create().name("single_multi_crop").itemStack(getBlockStack(BlockBaseMachine3.single_multi_crop)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("fertilizer").position(120, -40).build();
        Quest.Builder.create().name("alkalineearthquarry").itemStack(getBlockStack(BlockBaseMachine3.alkalineearthquarry)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("laser_polisher").position(40, 0).build();
        Quest.Builder.create().name("steelMesh").itemStack(ItemStackHelper.fromData(IUItem.steelMesh)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("alkalineearthquarry").position(80, 0).build();
        Quest.Builder.create().name("lithium").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 447)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("steelMesh").position(120, 0).build();
        Quest.Builder.create().name("reBattery").itemStack(ItemStackHelper.fromData(IUItem.reBattery)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("lithium").position(160, 0).build();
        Quest.Builder.create().name("planner").itemStack(ItemStackHelper.fromData(IUItem.planner)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("reBattery").position(160, 40).build();
        Quest.Builder.create().name("oilgetter").itemStack(ItemStackHelper.fromData(IUItem.oilgetter)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("reBattery").position(200, 0).build();
        Quest.Builder.create().name("oilquarry").itemStack(ItemStackHelper.fromData(IUItem.oilquarry)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("oilgetter").position(200, 40).build();
        Quest.Builder.create().name("oiladvrefiner").itemStack(ItemStackHelper.fromData(IUItem.oiladvrefiner)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("oilgetter").position(240, 0).build();
        Quest.Builder.create().name("cokeoven").itemStack(ItemStackHelper.fromData(IUItem.cokeoven)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("oiladvrefiner").position(280, 0).build();
        Quest.Builder.create().name("dosimeter").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 40)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("cokeoven").position(320, 0).build();
        Quest.Builder.create().name("radioprotector").itemStack(ItemStackHelper.fromData(IUItem.radioprotector)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("dosimeter").position(360, -40).build();
        Quest.Builder.create().name("hazmat").itemStack(ItemStackHelper.fromData(IUItem.hazmat_helmet),
                        ItemStackHelper.fromData(IUItem.hazmat_chestplate), ItemStackHelper.fromData(IUItem.hazmat_leggings),
                        ItemStackHelper.fromData(IUItem.rubber_boots)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("dosimeter").position(320, 40).build();
        Quest.Builder.create().name("reactor_rod_factory").itemStack(getBlockStack(BlockBaseMachine3.reactor_rod_factory)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("hazmat").position(280, 40).build();
        Quest.Builder.create().name("uranium_fuel_rod").itemStack(IUItem.uranium_fuel_rod.getItemStack()).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("reactor_rod_factory").position(240, 40).build();
        Quest.Builder.create().name("leadbox").itemStack(ItemStackHelper.fromData(IUItem.leadbox)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("uranium_fuel_rod").position(200, 80).build();
        Quest.Builder.create().name("pellets").itemStack(ItemStackHelper.fromData(IUItem.pellets, 1, 8)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("uranium_fuel_rod").position(280, 80).build();
        Quest.Builder.create().name("pallet_generator").itemStack(getBlockStack(BlockBaseMachine3.pallet_generator)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("pellets").position(320, 80).build();
        Quest.Builder.create().name("radcable").itemStack(ItemStackHelper.fromData(IUItem.radcable_item)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("hazmat").position(360, 40).build();
        Quest.Builder.create().name("reactor_safety_doom").itemStack(getBlockStack(BlockBaseMachine3.reactor_safety_doom)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("radcable").position(400, 40).build();
        Quest.Builder.create().name("water_controller").itemStack(getBlockStack(BlockWaterReactors.water_controller)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("reactor_safety_doom").position(440, 40).build();
        Quest.Builder.create().name("water_controller").itemStack(getBlockStack(BlockGasReactor.gas_controller)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("reactor_safety_doom").position(440, 70).build();

        Quest.Builder.create().name("azurebrilliant").fluidStack(new FluidStack(FluidName.fluidazurebrilliant.getInstance().get(),
                        1000)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("radioprotector").position(400, -40).build();
        Quest.Builder.create().name("industrialoil").fluidStack(new FluidStack(FluidName.fluidindustrialoil.getInstance().get(),
                        1000)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("dosimeter").position(360, 0).build();
        Quest.Builder.create().name("motoroil").fluidStack(new FluidStack(FluidName.fluidmotoroil.getInstance().get(),
                        1000)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("industrialoil").position(400, 0).build();
        Quest.Builder.create().name("solardestiller").itemStack(getBlockStack(BlockBaseMachine3.solardestiller)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("motoroil").position(440, 0).build();
        Quest.Builder.create().name("single_fluid_adapter").itemStack(getBlockStack(BlockBaseMachine3.single_fluid_adapter)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("solardestiller").position(480, 0).build();
        Quest.Builder.create().name("gen_obsidian").itemStack(getBlockStack(BlockBaseMachine2.gen_obsidian)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("single_fluid_adapter").position(520, -40).build();
        Quest.Builder.create().name("fisher").itemStack(getBlockStack(BlockBaseMachine2.fisher)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("gen_obsidian").position(560, -40).build();
        Quest.Builder.create().name("gen_wither").itemStack(getBlockStack(BlockBaseMachine1.gen_wither)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("fisher").position(600, -40).build();
        Quest.Builder.create().name("cooling_mixture").itemStack(ItemStackHelper.fromData(IUItem.cooling_mixture)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("single_fluid_adapter").position(520, 0).build();
        Quest.Builder.create().name("construction_foam").fluidStack(new FluidStack(FluidName.fluidconstruction_foam.getInstance().get(),
                        1000)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("cooling_mixture").position(560, 0).build();
        Quest.Builder.create().name("sprayer").itemStack(ItemStackHelper.fromData(IUItem.sprayer)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("construction_foam").position(560, 40).build();
        Quest.Builder.create().name("reinforcedstone").itemStack(ItemStackHelper.fromData(IUItem.blockResource, 1, 7)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("sprayer").position(560, 80).build();
        Quest.Builder.create().name("generator_fluid_matter").itemStack(getBlockStack(BlockSimpleMachine.generator_matter)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("reinforcedstone").position(600, 80).build();
        Quest.Builder.create().name("scanner_iu").itemStack(getBlockStack(BlockBaseMachine3.scanner_iu)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("generator_fluid_matter").position(640, 80).build();
        Quest.Builder.create().name("pattern_storage_iu").itemStack(getBlockStack(BlockBaseMachine3.pattern_storage_iu)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("scanner_iu").position(680, 80).build();
        Quest.Builder.create().name("replicator_iu").itemStack(getBlockStack(BlockBaseMachine3.replicator_iu)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("pattern_storage_iu").position(720, 80).build();
        Quest.Builder.create().name("matter_collector").itemStack(getBlockStack(BlockBaseMachine3.matter_collector)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("replicator_iu").position(760, 80).build();
        Quest.Builder.create().name("scrapBox").itemStack(IUItem.scrapBox).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("replicator_iu").position(720, 120).build();
        Quest.Builder.create().name("assamplerscrap").itemStack(getBlockStack(BlockMoreMachine3.assamplerscrap)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("scrapBox").position(720, 160).build();
        Quest.Builder.create().name("overclockerUpgrade").itemStack(IUItem.overclockerUpgrade).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("cooling_mixture").position(520, 100).build();
        Quest.Builder.create().name("transformerUpgrade").itemStack(IUItem.transformerUpgrade).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("overclockerUpgrade").position(480, 100).build();
        Quest.Builder.create().name("mfe_iu").itemStack(getBlockStack(BlockEnergyStorage.mfe_iu)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("transformerUpgrade").position(440, 100).build();
        Quest.Builder.create().name("fluid_trash").itemStack(getBlockStack(BlockBaseMachine3.fluid_trash)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("mfe_iu").position(400, 140).build();
        Quest.Builder.create().name("energy_trash").itemStack(getBlockStack(BlockBaseMachine3.energy_trash)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("mfe_iu").position(480, 140).build();
        Quest.Builder.create().name("item_trash").itemStack(getBlockStack(BlockBaseMachine3.item_trash)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("mfe_iu").position(440, 140).build();
        Quest.Builder.create().name("electrolyzer_iu").itemStack(getBlockStack(BlockBaseMachine2.electrolyzer_iu)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("mfe_iu").position(400, 100).build();
        Quest.Builder.create().name("enchanter_books").itemStack(getBlockStack(BlockBaseMachine3.enchanter_books)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("electrolyzer_iu").position(360, 70).build();
        Quest.Builder.create().name("molecular").itemStack(getBlockStack(BlockMolecular.molecular)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("electrolyzer_iu").position(360, 100).build();
        Quest.Builder.create().name("advanced_solar_paneliu").itemStack(getBlockStack(BlockSolarPanels.advanced_solar_paneliu)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("molecular").position(280, 120).build();
        Quest.Builder.create().name("minipanel").itemStack(getBlockStack(BlockBaseMachine3.minipanel)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("advanced_solar_paneliu").position(240, 120).build();
        Quest.Builder.create().name("lightning_rod_controller").itemStack(getBlockStack(BlockLightningRod.lightning_rod_controller
                )).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("advanced_solar_paneliu").position(240, 150).build();
        Quest.Builder.create().name("matter_factory").itemStack(getBlockStack(BlockBaseMachine3.matter_factory)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("minipanel").position(200, 120).build();
        Quest.Builder.create().name("battery_factory").itemStack(getBlockStack(BlockBaseMachine3.battery_factory)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("matter_factory").position(160, 120).build();
        Quest.Builder.create().name("socket_factory").itemStack(getBlockStack(BlockBaseMachine3.socket_factory)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("battery_factory").position(120, 120).build();
        Quest.Builder.create().name("photoniy_ingot").itemStack(ItemStackHelper.fromData(IUItem.photoniy_ingot)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("molecular").position(320, 140).build();
        Quest.Builder.create().name("aircollector").itemStack(getBlockStack(BlockBaseMachine3.aircollector)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("photoniy_ingot").position(280, 180).build();
        Quest.Builder.create().name("soil_analyzer").itemStack(getBlockStack(BlockBaseMachine3.soil_analyzer)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("photoniy_ingot").position(360, 180).build();
        Quest.Builder.create().name("radiation_purifier").itemStack(getBlockStack(BlockBaseMachine3.radiation_purifier)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("soil_analyzer").position(400, 180).build();
        Quest.Builder.create().name("synthesis").itemStack(getBlockStack(BlockBaseMachine1.synthesis)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("aircollector").position(240, 180).build();
        Quest.Builder.create().name("upgrade_speed_creation").itemStack(ItemStackHelper.fromData(IUItem.upgrade_speed_creation)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("aircollector").position(280, 220).build();
        Quest.Builder.create().name("pollution_scanner").itemStack(ItemStackHelper.fromData(IUItem.pollutionDevice)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("synthesis").position(240, 220).build();
        Quest.Builder.create().name("steam_turbine_controller").itemStack(getBlockStack(BlockSteamTurbine.steam_turbine_controller)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("photoniy_ingot").position(320, 220).build();
        Quest.Builder.create().name("research_lens").itemStack(ItemStackHelper.fromData(IUItem.research_lens)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("synthesis").position(200, 180).build();
        Quest.Builder.create().name("research_table_space").itemStack(getBlockStack(BlockBaseMachine3.research_table_space)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("research_lens").position(160, 180).build();
        Quest.Builder.create().name("rocket_launch_pad").itemStack(getBlockStack(BlockBaseMachine3.rocket_launch_pad)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("research_table_space").position(120, 180).build();
        Quest.Builder.create().name("rover_assembler").itemStack(getBlockStack(BlockBaseMachine3.rover_assembler)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("rocket_launch_pad").position(80, 180).build();
        Quest.Builder.create().name("probe_assembler").itemStack(getBlockStack(BlockBaseMachine3.probe_assembler)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("rover_assembler").position(40, 140).build();
        Quest.Builder.create().name("satellite_assembler").itemStack(getBlockStack(BlockBaseMachine3.satellite_assembler)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("probe_assembler").position(0, 100).build();
        Quest.Builder.create().name("rocket_assembler").itemStack(getBlockStack(BlockBaseMachine3.rocket_assembler)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("satellite_assembler").position(-40, 60).build();
        Quest.Builder.create().name("hydrazine").fluidStack(new FluidStack(FluidName.fluidhydrazine.getInstance().get(),
                        1000)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("rover_assembler").position(40, 180).build();
        Quest.Builder.create().name("rover").itemStack(ItemStackHelper.fromData(IUItem.rover)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("hydrazine").position(0, 180).build();
        Quest.Builder.create().name("purifier_soil").itemStack(getBlockStack(BlockBaseMachine3.purifier_soil)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("rover").position(-40, 180).build();
        Quest.Builder.create().name("moon_pebble").itemStack(ItemStackHelper.fromData(IUItem.spaceItem, 1, 48)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("rover").position(0, 260).build();
        Quest.Builder.create().name("meteoric_iron").itemStack(ItemStackHelper.fromData(IUItem.iuingot, 1, 46)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("moon_pebble").position(40, 260).build();
        Quest.Builder.create().name("spaceupgrademodule_schedule").itemStack(ItemStackHelper.fromData(IUItem.spaceupgrademodule_schedule)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("meteoric_iron").position(80, 260).build();
        Quest.Builder.create().name("upgrade_rover").itemStack(getBlockStack(BlockBaseMachine3.upgrade_rover)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("spaceupgrademodule_schedule").position(120, 260).build();
        Quest.Builder.create().name("mars_pebble").itemStack(ItemStackHelper.fromData(IUItem.spaceItem, 1, 44)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("upgrade_rover").position(160, 260).build();
        Quest.Builder.create().name("adamantium").itemStack(ItemStackHelper.fromData(IUItem.iuingot, 1, 43)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("mars_pebble").position(200, 260).build();
        Quest.Builder.create().name("dimethylhydrazine").fluidStack(new FluidStack(FluidName.fluiddimethylhydrazine.getInstance().get(),
                        1000)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("adamantium").position(240, 260).build();
        Quest.Builder.create().name("adv_rover").itemStack(ItemStackHelper.fromData(IUItem.adv_rover)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("dimethylhydrazine").position(280, 260).build();
        Quest.Builder.create().name("research_lens_2").itemStack(ItemStackHelper.fromData(IUItem.research_lens, 1, 1)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("adv_rover").position(320, 260).build();
        Quest.Builder.create().name("pressure_space_sensor").itemStack(ItemStackHelper.fromData(IUItem.spaceupgrademodule, 1, 3)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("research_lens_2").position(360, 260).build();
        Quest.Builder.create().name("venus_pebble").itemStack(ItemStackHelper.fromData(IUItem.spaceItem, 1, 59)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("pressure_space_sensor").position(400, 260).build();
        Quest.Builder.create().name("mercury_pebble").itemStack(ItemStackHelper.fromData(IUItem.spaceItem, 1, 45)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("venus_pebble").position(400, 260).build();
        Quest.Builder.create().name("mithril").itemStack(ItemStackHelper.fromData(IUItem.iuingot, 1, 47)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("mercury_pebble").position(440, 260).build();
        Quest.Builder.create().name("research_lens_3").itemStack(ItemStackHelper.fromData(IUItem.research_lens, 1, 2)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("mithril").position(480, 260).build();
        Quest.Builder.create().name("deimos_pebble").itemStack(ItemStackHelper.fromData(IUItem.spaceItem, 1, 35)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("research_lens_3").position(480, 300).build();
        Quest.Builder.create().name("orichalcum").itemStack(ItemStackHelper.fromData(IUItem.iuingot, 1, 48)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("deimos_pebble").position(440, 300).build();
        Quest.Builder.create().name("tethys_pebble").itemStack(ItemStackHelper.fromData(IUItem.spaceItem, 1, 54)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("orichalcum").position(400, 300).build();
        Quest.Builder.create().name("decane").fluidStack(new FluidStack(FluidName.fluiddecane.getInstance().get(),
                        1000)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("tethys_pebble").position(360, 300).build();
        Quest.Builder.create().name("imp_rover").itemStack(ItemStackHelper.fromData(IUItem.imp_rover)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("decane").position(320, 300).build();
        Quest.Builder.create().name("mimas_pebble").itemStack(ItemStackHelper.fromData(IUItem.spaceItem, 1, 46)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("imp_rover").position(280, 300).build();
        Quest.Builder.create().name("bloodstone").itemStack(ItemStackHelper.fromData(IUItem.plate, 1, 47)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("mimas_pebble").position(240, 300).build();
        Quest.Builder.create().name("advanced_hull_machine").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 139)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("bloodstone").position(200, 300).build();
        Quest.Builder.create().name("double_transformer").itemStack(getBlockStack(BlockDoubleMolecularTransfomer.double_transformer)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("advanced_hull_machine").position(160, 300).build();
        Quest.Builder.create().name("analyzer").itemStack(getBlockStack(BlockBaseMachine2.analyzer)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("double_transformer").position(120, 300).build();
        Quest.Builder.create().name("research_lens_4").itemStack(ItemStackHelper.fromData(IUItem.research_lens, 1, 3)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("analyzer").position(80, 300).build();
        Quest.Builder.create().name("double_molecular").itemStack(ItemStackHelper.fromData(IUItem.double_molecular)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("analyzer").position(160, 340).build();
        Quest.Builder.create().name("antisoilpollution").itemStack(ItemStackHelper.fromData(IUItem.antisoilpollution)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("double_molecular").position(200, 340).build();
        Quest.Builder.create().name("antiairpollution").itemStack(ItemStackHelper.fromData(IUItem.antiairpollution)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("double_molecular").position(200, 380).build();
        GuideTab advancedElectricTab = new GuideTab("advancedElectricTab", getBlockStack(BlockBaseMachine3.imp_alloy_smelter),
                "advancedElectricTab");
        Quest.Builder.create().name("imp_alloy_smelter").itemStack(getBlockStack(BlockBaseMachine3.imp_alloy_smelter)).localizationItem().noDescription().tab(advancedElectricTab)
                .position(0, 0).build();
        Quest.Builder.create().name("fluid_item_pipe").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.item_pipes)).shape(Shape.DEFAULT).description(
                "fluid_item_pipe").position(-35,
                105).build();
        Quest.Builder.create().name("graviTool").itemStack(ItemStackHelper.fromData(IUItem.GraviTool)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("imp_alloy_smelter").position(40, -40).build();
        Quest.Builder.create().name("relocator").itemStack(ItemStackHelper.fromData(IUItem.relocator)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("graviTool").position(40, -80).build();
        Quest.Builder.create().name("cooling").itemStack(getBlockStack(BlockBaseMachine3.cooling)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("imp_alloy_smelter").position(40, 0).build();
        Quest.Builder.create().name("antiairpollution1").itemStack(ItemStackHelper.fromData(IUItem.antiairpollution1)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("cooling").position(0, 40).build();
        Quest.Builder.create().name("antisoilpollution1").itemStack(ItemStackHelper.fromData(IUItem.antisoilpollution1)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("cooling").position(80, 40).build();
        Quest.Builder.create().name("substitute").itemStack(getBlockStack(BlockBaseMachine3.substitute)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("cooling").position(80, 0).build();
        Quest.Builder.create().name("energy_remover").itemStack(getBlockStack(BlockBaseMachine3.energy_remover)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("substitute").position(80, -40).build();
        Quest.Builder.create().name("module_quickly").itemStack(ItemStackHelper.fromData(IUItem.module_quickly),
                        ItemStackHelper.fromData(IUItem.module_separate), ItemStackHelper.fromData(IUItem.module_stack),
                        ItemStackHelper.fromData(IUItem.module_storage), ItemStackHelper.fromData(IUItem.module_infinity_water)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("substitute").position(120, 0).build();
        Quest.Builder.create().name("coolupgrade").itemStack(ItemStackHelper.fromData(IUItem.coolupgrade, 1, 2)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("module_quickly").position(160, 0).build();
        Quest.Builder.create().name("autoheater").itemStack(ItemStackHelper.fromData(IUItem.autoheater)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("coolupgrade").position(200, 0).build();
        Quest.Builder.create().name("imp_refiner").itemStack(getBlockStack(BlockBaseMachine3.imp_refiner)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("autoheater").position(240, 0).build();
        Quest.Builder.create().name("adv_coke_oven_main").itemStack(getBlockStack(BlockAdvCokeOven.adv_coke_oven_main)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("imp_refiner").position(280, 0).build();
        Quest.Builder.create().name("wireless_oil_pump").itemStack(getBlockStack(BlockBaseMachine3.wireless_oil_pump)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("adv_coke_oven_main").position(280, -40).build();
        Quest.Builder.create().name("wireless_mineral_quarry").itemStack(getBlockStack(BlockBaseMachine3.wireless_mineral_quarry)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("wireless_oil_pump").position(320, -40).build();
        Quest.Builder.create().name("wireless_gas_pump").itemStack(getBlockStack(BlockBaseMachine3.wireless_gas_pump)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("wireless_mineral_quarry").position(360, -40).build();
        Quest.Builder.create().name("radiation_storage").itemStack(getBlockStack(BlockBaseMachine3.radiation_storage)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("adv_coke_oven_main").position(320, 0).build();
        Quest.Builder.create().name("automatic_mechanism").itemStack(getBlockStack(BlockBaseMachine3.automatic_mechanism)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("radiation_storage").position(360, 0).build();
        Quest.Builder.create().name("wireless_controller_reactors").itemStack(getBlockStack(BlockBaseMachine3.wireless_controller_reactors)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("automatic_mechanism").position(400, 0).build();
        Quest.Builder.create().name("entitymodules").itemStack(ItemStackHelper.fromData(IUItem.entitymodules, 1, 1)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("wireless_controller_reactors").position(440, 0).build();
        Quest.Builder.create().name("spawner").itemStack(getBlockStack(BlockBaseMachine3.spawner)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("entitymodules").position(440, -40).build();
        Quest.Builder.create().name("auto_open_box").itemStack(getBlockStack(BlockBaseMachine3.auto_open_box)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("entitymodules").position(480, 0).build();
        Quest.Builder.create().name("refrigerator_coolant").itemStack(getBlockStack(BlockBaseMachine3.refrigerator_coolant)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("auto_open_box").position(480, 100).build();
        Quest.Builder.create().name("autocrafter").itemStack(getBlockStack(BlockBaseMachine3.autocrafter)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("refrigerator_coolant").position(520, 100).build();
        Quest.Builder.create().name("autofuse").itemStack(getBlockStack(BlockBaseMachine3.autofuse)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("refrigerator_coolant").position(440, 100).build();
        Quest.Builder.create().name("graphite_handler").itemStack(getBlockStack(BlockBaseMachine3.graphite_handler)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("autofuse").position(400, 100).build();
        Quest.Builder.create().name("tesseract").itemStack(getBlockStack(BlockBaseMachine3.tesseract)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("graphite_handler").position(360, 100).build();
        Quest.Builder.create().name("imp_se_gen").itemStack(getBlockStack(BlockImpSolarEnergy.imp_se_gen)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("tesseract").position(320, 100).build();
        Quest.Builder.create().name("combiner_se_generators").itemStack(getBlockStack(BlockBaseMachine3.combiner_se_generators)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("imp_se_gen").position(280, 100).build();
        Quest.Builder.create().name("combiner_matter").itemStack(getBlockStack(BlockBaseMachine2.combiner_matter)).localizationItem().noDescription().noDescription().tab(advancedElectricTab)
                .prev("combiner_se_generators").position(240, 100).build();
        Quest.Builder.create().name("quantum_quarry").itemStack(getBlockStack(BlockBaseMachine.quantum_quarry)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("combiner_matter").position(200, 100).build();
        Quest.Builder.create().name("graphite_controller").itemStack(getBlockStack(BlocksGraphiteReactors.graphite_controller)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("quantum_quarry").position(160, 60).build();
        Quest.Builder.create().name("heat_controller").itemStack(getBlockStack(BlockHeatReactor.heat_controller)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("quantum_quarry").position(200, 140).build();
        Quest.Builder.create().name("neutron_generator").itemStack(getBlockStack(BlockBaseMachine.neutron_generator)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("quantum_quarry").position(160, 100).build();
        Quest.Builder.create().name("neutroniumingot").itemStack(ItemStackHelper.fromData(IUItem.neutroniumingot)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("neutron_generator").position(120, 100).build();
        Quest.Builder.create().name("upgrade_block").itemStack(getBlockStack(BlockUpgradeBlock.upgrade_block)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("neutroniumingot").position(80, 100).build();
        Quest.Builder.create().name("antiupgradeblock").itemStack(getBlockStack(BlockBaseMachine3.antiupgradeblock)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("upgrade_block").position(80, 140).build();
        Quest.Builder.create().name("rotor_modifier").itemStack(getBlockStack(BlockBaseMachine3.rotor_modifier)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("antiupgradeblock").position(120, 140).build();
        Quest.Builder.create().name("water_modifier").itemStack(getBlockStack(BlockBaseMachine3.water_modifier)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("rotor_modifier").position(160, 140).build();
        Quest.Builder.create().name("earth_controller").itemStack(getBlockStack(BlockEarthQuarry.earth_controller)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("upgrade_block").position(40, 100).build();
        Quest.Builder.create().name("gas_turbine_controller").itemStack(getBlockStack(BlockGasTurbine.gas_turbine_controller)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("earth_controller").position(0, 100).build();
        Quest.Builder.create().name("gas_well_controller").itemStack(getBlockStack(BlockGasWell.gas_well_controller)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("gas_turbine_controller").position(0, 180).build();
        Quest.Builder.create().name("night_transformer").itemStack(getBlockStack(BlockBaseMachine3.night_transformer)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("gas_well_controller").position(40, 180).build();
        Quest.Builder.create().name("night_converter").itemStack(getBlockStack(BlockBaseMachine3.night_converter)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("night_transformer").position(40, 220).build();
        Quest.Builder.create().name("incubator").itemStack(getBlockStack(BlockBaseMachine3.incubator)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("night_transformer").position(80, 180).build();
        Quest.Builder.create().name("insulator").itemStack(getBlockStack(BlockBaseMachine3.insulator)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("incubator").position(120, 180).build();
        Quest.Builder.create().name("rna_collector").itemStack(getBlockStack(BlockBaseMachine3.rna_collector)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("insulator").position(160, 180).build();
        Quest.Builder.create().name("mutatron").itemStack(getBlockStack(BlockBaseMachine3.mutatron)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("rna_collector").position(200, 180).build();
        Quest.Builder.create().name("genetic_stabilizer").itemStack(getBlockStack(BlockBaseMachine3.genetic_stabilizer)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("mutatron").position(240, 180).build();
        Quest.Builder.create().name("reverse_transcriptor").itemStack(getBlockStack(BlockBaseMachine3.reverse_transcriptor)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("genetic_stabilizer").position(280, 180).build();
        Quest.Builder.create().name("genetic_replicator").itemStack(getBlockStack(BlockBaseMachine3.genetic_replicator)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("reverse_transcriptor").position(320, 180).build();
        Quest.Builder.create().name("genetic_transposer").itemStack(getBlockStack(BlockBaseMachine3.genetic_transposer)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("genetic_replicator").position(360, 180).build();
        Quest.Builder.create().name("genetic_polymerizer").itemStack(getBlockStack(BlockBaseMachine3.genetic_polymerizer)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("genetic_transposer").position(400, 180).build();
        Quest.Builder.create().name("inoculator").itemStack(getBlockStack(BlockBaseMachine3.inoculator)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("genetic_polymerizer").position(440, 180).build();
        Quest.Builder.create().name("genome_extractor").itemStack(getBlockStack(BlockBaseMachine3.genome_extractor)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("inoculator").position(480, 180).build();
        Quest.Builder.create().name("geothermal_controller").itemStack(getBlockStack(BlockGeothermalPump.geothermal_controller)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("genome_extractor").position(480, 240).build();
        Quest.Builder.create().name("iodine").fluidStack(new FluidStack(FluidName.fluidiodine.getInstance().get(),
                        1000)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("geothermal_controller").position(400, 240).build();
        Quest.Builder.create().name("chemical_plant_controller").itemStack(getBlockStack(BlockChemicalPlant.chemical_plant_controller)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("iodine").position(320, 240).build();
        Quest.Builder.create().name("ariel_pebble").itemStack(ItemStackHelper.fromData(IUItem.spaceItem, 1, 30)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("chemical_plant_controller").position(260, 240).build();
        Quest.Builder.create().name("draconid").itemStack(ItemStackHelper.fromData(IUItem.iuingot, 1, 45)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("ariel_pebble").position(200, 240).build();
        Quest.Builder.create().name("quad_molecular").itemStack(ItemStackHelper.fromData(IUItem.quad_molecular)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("draconid").position(200, 280).build();
        Quest.Builder.create().name("perfect_hull_plating").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 140)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("draconid").position(100, 240).build();
        Quest.Builder.create().name("research_lens_5").itemStack(ItemStackHelper.fromData(IUItem.research_lens, 1, 4)).localizationItem().noDescription().tab(advancedElectricTab)
                .prev("perfect_hull_plating").position(0, 240).build();
        Quest.Builder.create().name("recipe_tuner").itemStack(getBlockStack(BlockBaseMachine3.recipe_tuner)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("adv_alloy_smelter").position(400, 280).build();
        Quest.Builder.create().name("recipe_schedule").itemStack(ItemStackHelper.fromData(IUItem.recipe_schedule)).localizationItem().useItemInform().tab(baseElectricTab)
                .prev("recipe_tuner").position(400, 320).build();
        Quest.Builder.create().name("tuner").itemStack(getBlockStack(BlockBaseMachine3.tuner)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("purifier_soil").position(-80, 180).build();
        Quest.Builder.create().name("privatizer").itemStack(getBlockStack(BlockBaseMachine3.privatizer)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("tuner").position(-120, 180).build();
        Quest.Builder.create().name("hologram_space").itemStack(getBlockStack(BlockBaseMachine3.hologram_space)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("rocket_launch_pad").position(120, 220).build();
        Quest.Builder.create().name("weeder").itemStack(getBlockStack(BlockBaseMachine3.weeder)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("single_multi_crop").position(160, -40).build();
        Quest.Builder.create().name("plant_fertilizer").itemStack(getBlockStack(BlockBaseMachine3.plant_fertilizer)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("weeder").position(200, -40).build();
        Quest.Builder.create().name("field_cleaner").itemStack(getBlockStack(BlockBaseMachine3.field_cleaner)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("plant_fertilizer").position(240, -40).build();
        Quest.Builder.create().name("weed_ex").fluidStack(new FluidStack(FluidName.fluidweed_ex.getInstance().get(), 1000)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("field_cleaner").position(280, -40).build();
        Quest.Builder.create().name("collector_product_bee").itemStack(getBlockStack(BlockBaseMachine3.collector_product_bee)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("steelMesh").position(80, 40).build();
        Quest.Builder.create().name("shield").itemStack(getBlockStack(BlockBaseMachine3.shield)).localizationItem().noDescription().tab(improvedElectricTab)
                .prev("gen_wither").position(640, -40).build();
        Quest.Builder.create().name("steam_generator").itemStack(getBlockStack(BlockBaseMachine3.steam_generator)).localizationItem().noDescription().tab(baseElectricTab).prev("redstone_generator").position(120, -40).build();
        Quest.Builder.create().name("bio_generator").itemStack(getBlockStack(BlockBaseMachine3.bio_generator)).localizationItem().noDescription().tab(baseElectricTab).prev("steam_generator").position(160, -40).build();
        Quest.Builder.create().name("peat_generator").itemStack(getBlockStack(BlockBaseMachine3.peat_generator)).localizationItem().noDescription().tab(baseElectricTab).prev("bio_generator").position(200, -40).build();

        Quest.Builder.create().name("gen_hyd").itemStack(getBlockStack(BlockBaseMachine2.gen_hyd)).localizationItem().noDescription().tab(baseElectricTab).prev("peat_generator").position(240, -40).build();

        Quest.Builder.create().name("electric_refractory_furnace").itemStack(getBlockStack(BlockBaseMachine3.electric_refractory_furnace)).localizationItem().noDescription().tab(baseElectricTab)
                .prev("gen_sunnarium").position(0, 80).build();

        Quest.Builder.create().name("electric_brewing").itemStack(getBlockStack(BlockBaseMachine3.electric_brewing)).localizationItem().noDescription().tab(baseElectricTab).prev("generator").position(80, 40).build();
        Quest.Builder.create().name("watergenerator").itemStack(getBlockStack(BlockBaseMachine3.watergenerator)).localizationItem().noDescription().tab(baseElectricTab)
                .prev("adv_alloy_smelter").position(360, 200).build();
        Quest.Builder.create().name("apothecary_bee").itemStack(getBlockStack(BlockBaseMachine3.apothecary_bee)).localizationItem().noDescription().tab(baseElectricTab)
                .prev("watergenerator").position(320, 200).build();
        Quest.Builder.create().name("lava_gen").itemStack(getBlockStack(BlockBaseMachine2.lava_gen)).localizationItem().noDescription().tab(baseElectricTab).prev("electronic_assembler").position(400, 0).build();
        Quest.Builder.create().name("helium_generator").itemStack(getBlockStack(BlockBaseMachine2.helium_generator)).localizationItem().noDescription().tab(baseElectricTab).prev("lava_gen").position(440, 0).build();
        Quest.Builder.create().name("gen_stone").itemStack(getBlockStack(BlockBaseMachine.gen_stone)).localizationItem().noDescription().tab(baseElectricTab).prev("gearing").position(360, -40).build();
        Quest.Builder.create().name("gen_addition_stone").itemStack(getBlockStack(BlockBaseMachine3.gen_addition_stone)).localizationItem().noDescription().tab(baseElectricTab).prev("gen_stone").position(400, -40).build();
        Quest.Builder.create().name("fluid_heater").itemStack(getBlockStack(BlockBaseMachine3.fluid_heater)).localizationItem().useItemInform().tab(baseElectricTab).prev("liqued_heater").position(40, -40).build();
        Quest.Builder.create().name("hive").localizationItem().noDescription().tab(primalTab).icon(ItemStackHelper.fromData(IUItem.hive)).position(220,
                -140).prev("squeezer").build();
        Quest.Builder.create().name("crop_stake").localizationItem().noDescription().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.crop)).position(220,
                -110).prev("squeezer").build();
        Quest.Builder.create().name("net").localizationItem().noDescription().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.net)).position(250,
                -140).prev("hive").build();
        Quest.Builder.create().name("apiary").localizationItem().noDescription().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.apiary)).position(280,
                -140).prev("net").build();
        Quest.Builder.create().name("iron_hammer").noDescription().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.ironHammer)).position(370,
                20).prev("primal_wire_insulator").build();
        Quest.Builder.create().name("barrel").localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.barrel)).position(360,
                -80).useItemInform().prev("latex").build();
        GuideTab perElectricTab = new GuideTab("perElectricTab", getBlockStack(BlockBaseMachine3.per_alloy_smelter), "perElectric");
        Quest.Builder.create().name("per_alloy_smelter").itemStack(getBlockStack(BlockBaseMachine3.per_alloy_smelter)).localizationItem().noDescription().tab(perElectricTab).position(0, 0).build();
        Quest.Builder.create().name("auto_digger").itemStack(getBlockStack(BlockBaseMachine3.auto_digger)).prev(
                "per_alloy_smelter").localizationItem().noDescription().tab(perElectricTab).position(40, 0).build();
        Quest.Builder.create().name("solid_matter").itemStack(getBlockStack(BlockSolidMatter.solidmatter),
                getBlockStack(BlockSolidMatter.aer_solidmatter), getBlockStack(BlockSolidMatter.aqua_solidmatter),
                getBlockStack(BlockSolidMatter.sun_solidmatter), getBlockStack(BlockSolidMatter.end_solidmatter),
                getBlockStack(BlockSolidMatter.nether_solidmatter), getBlockStack(BlockSolidMatter.night_solidmatter)).prev(
                "auto_digger").localizationItem().noDescription().tab(perElectricTab).position(80, 0).build();
        Quest.Builder.create().name("wind_turbine_controller").itemStack(getBlockStack(BlockWindTurbine.wind_turbine_controller)).prev(
                "auto_digger").localizationItem().noDescription().tab(perElectricTab).position(40, -40).build();
        Quest.Builder.create().name("hydro_turbine_controller").itemStack(getBlockStack(BlockHydroTurbine.hydro_turbine_controller)).prev(
                "wind_turbine_controller").localizationItem().noDescription().tab(perElectricTab).position(80, -40).build();
        Quest.Builder.create().name("sintezator").itemStack(getBlockStack(BlockSintezator.sintezator)).prev(
                "auto_digger").localizationItem().noDescription().tab(perElectricTab).position(40, 40).build();
        Quest.Builder.create().name("converter_matter").itemStack(getBlockStack(BlockConverterMatter.converter_matter)).prev(
                "solid_matter").localizationItem().noDescription().tab(perElectricTab).position(120, 0).build();
        Quest.Builder.create().name("research_lens_5").itemStack(ItemStackHelper.fromData(IUItem.research_lens, 1, 5)).prev(
                "converter_matter").localizationItem().noDescription().tab(perElectricTab).position(160, 0).build();
        Quest.Builder.create().name("crystallize").itemStack(getBlockStack(BlockBaseMachine3.crystallize)).prev(
                "research_lens_5").localizationItem().noDescription().tab(perElectricTab).position(200, 0).build();
        Quest.Builder.create().name("ender_assembler").itemStack(getBlockStack(BlockBaseMachine3.ender_assembler)).prev(
                "crystallize").localizationItem().noDescription().tab(perElectricTab).position(240, 0).build();
        Quest.Builder.create().name("aqua_assembler").itemStack(getBlockStack(BlockBaseMachine3.aqua_assembler)).prev(
                "ender_assembler").localizationItem().noDescription().tab(perElectricTab).position(280, 0).build();
        Quest.Builder.create().name("nether_assembler").itemStack(getBlockStack(BlockBaseMachine3.nether_assembler)).prev(
                "aqua_assembler").localizationItem().noDescription().tab(perElectricTab).position(320, 0).build();
        Quest.Builder.create().name("earth_assembler").itemStack(getBlockStack(BlockBaseMachine3.earth_assembler)).prev(
                "nether_assembler").localizationItem().noDescription().tab(perElectricTab).position(360, 0).build();
        Quest.Builder.create().name("aer_assembler").itemStack(getBlockStack(BlockBaseMachine3.aer_assembler)).prev(
                "earth_assembler").localizationItem().noDescription().tab(perElectricTab).position(400, 0).build();
        Quest.Builder.create().name("neutronseparator").itemStack(getBlockStack(BlockBaseMachine3.neutronseparator)).prev(
                "aer_assembler").localizationItem().noDescription().tab(perElectricTab).position(400, 100).build();
        Quest.Builder.create().name("quantum_miner").itemStack(getBlockStack(BlockBaseMachine3.quantum_miner)).prev(
                "neutronseparator").localizationItem().noDescription().tab(perElectricTab).position(320, 100).build();
        Quest.Builder.create().name("quantum_transformer").itemStack(getBlockStack(BlockBaseMachine3.quantum_transformer)).prev(
                "quantum_miner").localizationItem().noDescription().tab(perElectricTab).position(260, 100).build();
        Quest.Builder.create().name("quantum_plasma").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 646)).prev(
                "quantum_transformer").localizationItem().noDescription().tab(perElectricTab).position(210, 100).build();
        Quest.Builder.create().name("positronconverter").itemStack(getBlockStack(BlockBaseMachine3.positronconverter)).prev(
                "quantum_plasma").localizationItem().noDescription().tab(perElectricTab).position(160, 100).build();
        Quest.Builder.create().name("cyclotron_controller").itemStack(getBlockStack(BlockCyclotron.cyclotron_controller)).prev(
                "positronconverter").localizationItem().noDescription().tab(perElectricTab).position(120, 100).build();
        Quest.Builder.create().name("photon_hull_plate").itemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 623)).prev(
                "cyclotron_controller").localizationItem().noDescription().tab(perElectricTab).position(80, 100).build();
        Quest.Builder.create().name("proteus_pebble").itemStack(ItemStackHelper.fromData(IUItem.spaceItem, 1, 52)).prev(
                "photon_hull_plate").localizationItem().noDescription().tab(perElectricTab).position(40, 100).build();
        Quest.Builder.create().name("xenon").fluidStack(new FluidStack(FluidName.fluidxenon.getInstance().get(), 1000)).prev(
                "proteus_pebble").localizationItem().noDescription().tab(perElectricTab).position(0, 100).build();
        Quest.Builder.create().name("research_lens_6").itemStack(ItemStackHelper.fromData(IUItem.research_lens, 1, 6)).prev(
                "xenon").localizationItem().noDescription().tab(perElectricTab).position(-40, 100).build();
        Quest.Builder.create().name("admpanel").itemStack(getBlockStack(BlockAdminPanel.admpanel)).prev(
                "research_lens_6").localizationItem().noDescription().tab(perElectricTab).position(-80, 100).build();
    }

    public List<GuideTab> getGuideTabs() {
        return guideTabs;
    }

    public Map<GuideTab, List<Quest>> getGuideTabListMap() {
        return guideTabListMap;
    }

    public void addQuestToTab(Quest quest, final GuideTab guideTab) {
        guideTabListMap.computeIfAbsent(guideTab, k -> new ArrayList<>()).add(quest);
    }

    public Quest getPrev(String name, GuideTab guideTab) {
        List<Quest> quests = guideTabListMap.get(guideTab);
        if (quests == null || quests.isEmpty()) {
            return null;
        }
        quests = quests.stream().filter(quest -> quest.unLocalizedName.equals(name)).collect(Collectors.toList());
        return quests.get(0);
    }

    public void load(UUID uniqueID, final Player player) {
        Map<String, List<String>> map = new HashMap<>();
        for (GuideTab guideTab : guideTabs) {
            List<String> quests = new ArrayList<>();
            List<Quest> quests1 = guideTabListMap.get(guideTab);
            if (quests1 != null)
                quests1.forEach(quest -> quests.add(quest.unLocalizedName));
            map.put(guideTab.getUnLocalized(), quests);
        }
        uuidGuideMap.put(uniqueID, map);
        new PacketUpdateInformationAboutQuestsPlayer(map, player);
    }

    public void addTab(GuideTab guideTab) {
        this.guideTabs.add(guideTab);
    }

    public List<Quest> getQuests(int i) {
        GuideTab guideTab = guideTabs.get(i);
        return this.guideTabListMap.get(guideTab);
    }

    public Quest getQuests(List<Quest> quests, String name) {
        final List<Quest> findQuest = quests
                .stream()
                .filter(quest1 -> quest1.unLocalizedName.equals(name))
                .collect(Collectors.toList());
        return findQuest.get(0);
    }

    public void setData(UUID uuid, Map<String, List<String>> map) {
        uuidGuideMap.put(uuid, map);
    }

    public void remove(UUID uniqueID, String tab, String quest) {
        uuidGuideMap.get(uniqueID).get(tab).remove(quest);
    }

    public void loadOrThrow(UUID uuid) {
        Map<String, List<String>> map = uuidGuideMap.get(uuid);
        for (GuideTab guideTab : guideTabs) {
            List<String> quests = new ArrayList<>();
            List<Quest> quests1 = guideTabListMap.get(guideTab);
            if (quests1 != null)
                quests1.forEach(quest -> quests.add(quest.unLocalizedName));
            if (!map.containsKey(guideTab.getUnLocalized()))
                map.put(guideTab.getUnLocalized(), quests);
        }
    }
}
