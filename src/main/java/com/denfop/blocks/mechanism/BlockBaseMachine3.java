package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.item.IMultiBlockItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileAntiUpgradeBlock;
import com.denfop.tiles.base.TileAutoSpawner;
import com.denfop.tiles.base.TileEntityAntiMagnet;
import com.denfop.tiles.base.TileEntityAutoDigger;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileEntityCombinerSEGenerators;
import com.denfop.tiles.base.TileEntityTeleporter;
import com.denfop.tiles.base.TileLimiter;
import com.denfop.tiles.base.TileRadiationPurifier;
import com.denfop.tiles.mechanism.TileAdvHandlerHeavyOre;
import com.denfop.tiles.mechanism.TileAdvScanner;
import com.denfop.tiles.mechanism.TileCanner;
import com.denfop.tiles.mechanism.TileEntityAnalyzerChest;
import com.denfop.tiles.mechanism.TileEntityCreatorSchedules;
import com.denfop.tiles.mechanism.TileEntityFacadeBlock;
import com.denfop.tiles.mechanism.TileEntityGasCombiner;
import com.denfop.tiles.mechanism.TileEntityKineticGeneratorAssembler;
import com.denfop.tiles.mechanism.TileEntityProbeAssembler;
import com.denfop.tiles.mechanism.TileEntityResearchTable;
import com.denfop.tiles.mechanism.TileEntityResearchTableSpace;
import com.denfop.tiles.mechanism.TileEntityRocketAssembler;
import com.denfop.tiles.mechanism.TileEntityRocketLaunchPad;
import com.denfop.tiles.mechanism.TileEntityRodManufacturer;
import com.denfop.tiles.mechanism.TileEntityRotorAssembler;
import com.denfop.tiles.mechanism.TileEntityRotorModifier;
import com.denfop.tiles.mechanism.TileEntitySatelliteAssembler;
import com.denfop.tiles.mechanism.TileEntitySoilAnalyzer;
import com.denfop.tiles.mechanism.TileEntityWaterRotorAssembler;
import com.denfop.tiles.mechanism.TileEntityWaterRotorModifier;
import com.denfop.tiles.mechanism.TileImpHandlerHeavyOre;
import com.denfop.tiles.mechanism.TileImpScanner;
import com.denfop.tiles.mechanism.TilePatternStorage;
import com.denfop.tiles.mechanism.TilePerHandlerHeavyOre;
import com.denfop.tiles.mechanism.TilePerScanner;
import com.denfop.tiles.mechanism.TilePrivatizer;
import com.denfop.tiles.mechanism.TileSimplePump;
import com.denfop.tiles.mechanism.TileSimpleScanner;
import com.denfop.tiles.mechanism.TileTunerWireless;
import com.denfop.tiles.mechanism.cooling.TileCooling;
import com.denfop.tiles.mechanism.dual.heat.TileWeldingMachine;
import com.denfop.tiles.mechanism.energy.TileEnergyController;
import com.denfop.tiles.mechanism.energy.TileEnergySubstitute;
import com.denfop.tiles.mechanism.generator.energy.TileEntitySolarGenerator;
import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityGeoGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileGasGenerator;
import com.denfop.tiles.mechanism.generator.energy.redstone.TileAdvRedstoneGenerator;
import com.denfop.tiles.mechanism.generator.energy.redstone.TileImpRedstoneGenerator;
import com.denfop.tiles.mechanism.generator.energy.redstone.TilePerRedstoneGenerator;
import com.denfop.tiles.mechanism.generator.energy.redstone.TileRedstoneGenerator;
import com.denfop.tiles.mechanism.generator.things.fluid.TileAirCollector;
import com.denfop.tiles.mechanism.generator.things.fluid.TileWaterGenerator;
import com.denfop.tiles.mechanism.heat.TileElectricHeat;
import com.denfop.tiles.mechanism.heat.TileFluidHeat;
import com.denfop.tiles.mechanism.quantum_storage.TileEntityAdvQuantumStorage;
import com.denfop.tiles.mechanism.quantum_storage.TileEntityImpQuantumStorage;
import com.denfop.tiles.mechanism.quantum_storage.TileEntityPerQuantumStorage;
import com.denfop.tiles.mechanism.quantum_storage.TileEntitySimpleQuantumStorage;
import com.denfop.tiles.mechanism.replicator.TileAdvReplicator;
import com.denfop.tiles.mechanism.replicator.TileImpReplicator;
import com.denfop.tiles.mechanism.replicator.TilePerReplicator;
import com.denfop.tiles.mechanism.replicator.TileReplicator;
import com.denfop.tiles.mechanism.solardestiller.TileEntityAdvSolarDestiller;
import com.denfop.tiles.mechanism.solardestiller.TileEntityImpSolarDestiller;
import com.denfop.tiles.mechanism.solardestiller.TileEntityPerSolarDestiller;
import com.denfop.tiles.mechanism.solardestiller.TileEntitySolarDestiller;
import com.denfop.tiles.mechanism.solarium_storage.TileEntityAdvSolariumStorage;
import com.denfop.tiles.mechanism.solarium_storage.TileEntityImpSolariumStorage;
import com.denfop.tiles.mechanism.solarium_storage.TileEntityPerSolariumStorage;
import com.denfop.tiles.mechanism.solarium_storage.TileEntitySimpleSolariumStorage;
import com.denfop.tiles.mechanism.water.TileAdvWaterGenerator;
import com.denfop.tiles.mechanism.water.TileImpWaterGenerator;
import com.denfop.tiles.mechanism.water.TilePerWaterGenerator;
import com.denfop.tiles.mechanism.water.TileSimpleWaterGenerator;
import com.denfop.tiles.mechanism.wind.TileAdvWindGenerator;
import com.denfop.tiles.mechanism.wind.TileImpWindGenerator;
import com.denfop.tiles.mechanism.wind.TilePerWindGenerator;
import com.denfop.tiles.mechanism.wind.TileSimpleWindGenerator;
import com.denfop.tiles.mechanism.worlcollector.TileAerAssembler;
import com.denfop.tiles.mechanism.worlcollector.TileAquaAssembler;
import com.denfop.tiles.mechanism.worlcollector.TileCrystallize;
import com.denfop.tiles.mechanism.worlcollector.TileEarthAssembler;
import com.denfop.tiles.mechanism.worlcollector.TileEnderAssembler;
import com.denfop.tiles.mechanism.worlcollector.TileNetherAssembler;
import com.denfop.tiles.panels.entity.TileEntityMiniPanels;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockBaseMachine3 implements IMultiTileBlock, IMultiBlockItem {
    radiation_purifier(TileRadiationPurifier.class, 1),
    privatizer(TilePrivatizer.class, 2),
    tuner(TileTunerWireless.class, 3),
    spawner(TileAutoSpawner.class, 4),
    elec_heat(TileElectricHeat.class, 5),
    fluid_heat(TileFluidHeat.class, 6),
    antimagnet(TileEntityAntiMagnet.class, 7),
    antiupgradeblock(TileAntiUpgradeBlock.class, 8),
    watergenerator(TileWaterGenerator.class, 9),
    cooling(TileCooling.class, 10),
    aircollector(TileAirCollector.class, 11),
    combiner_se_generators(TileEntityCombinerSEGenerators.class, 12),
    probe_assembler(TileEntityProbeAssembler.class, 13),
    research_table_space(TileEntityResearchTableSpace.class, 14),
    rocket_assembler(TileEntityRocketAssembler.class, 15),
    rocket_launch_pad(TileEntityRocketLaunchPad.class, 16),
    rotor_assembler(TileEntityRotorAssembler.class, 17),
    rotor_modifier(TileEntityRotorModifier.class, 18),
    satellite_assembler(TileEntitySatelliteAssembler.class, 19),
    soil_analyzer(TileEntitySoilAnalyzer.class, 20),
    rods_manufacturer(TileEntityRodManufacturer.class, 21),
    kinetic_generator_assembler(TileEntityKineticGeneratorAssembler.class, 22),
    ender_assembler(TileEnderAssembler.class, 23),
    research_table(TileEntityResearchTable.class, 24),
    creator_schedules(TileEntityCreatorSchedules.class, 25),
    gas_combiner(TileEntityGasCombiner.class, 26),
    analyzer_chest(TileEntityAnalyzerChest.class, 27),
    simple_wind_generator(TileSimpleWindGenerator.class, 28),
    adv_wind_generator(TileAdvWindGenerator.class, 29),
    imp_wind_generator(TileImpWindGenerator.class, 30),
    per_wind_generator(TilePerWindGenerator.class, 31),
    energy_controller(TileEnergyController.class, 32),
    substitute(TileEnergySubstitute.class, 33),

    aer_assembler(TileAerAssembler.class, 34),
    aqua_assembler(TileAquaAssembler.class, 35),
    earth_assembler(TileEarthAssembler.class, 36),
    nether_assembler(TileNetherAssembler.class, 37),
    auto_digger(TileEntityAutoDigger.class, 38),
    crystallize(TileCrystallize.class, 39),
    double_handlerho(TileAdvHandlerHeavyOre.class, 40),
    triple_handlerho(TileImpHandlerHeavyOre.class, 41),
    quad_handlerho(TilePerHandlerHeavyOre.class, 42),
    water_modifier(TileEntityWaterRotorModifier.class, 43),
    water_rotor_assembler(TileEntityWaterRotorAssembler.class, 44),
    simple_water_generator(TileSimpleWaterGenerator.class, 45),
    adv_water_generator(TileAdvWaterGenerator.class, 46),
    imp_water_generator(TileImpWaterGenerator.class, 47),
    per_water_generator(TilePerWaterGenerator.class, 48),
    scanner_iu(TileSimpleScanner.class, 49),
    adv_scanner(TileAdvScanner.class, 50),
    imp_scanner(TileImpScanner.class, 51),
    per_scanner(TilePerScanner.class, 52),
    replicator_iu(TileReplicator.class, 53),
    adv_replicator(TileAdvReplicator.class, 54),
    imp_replicator(TileImpReplicator.class, 55),
    per_replicator(TilePerReplicator.class, 56),

    solarium_storage(TileEntitySimpleSolariumStorage.class, 57),
    adv_solarium_storage(TileEntityAdvSolariumStorage.class, 58),
    imp_solarium_storage(TileEntityImpSolariumStorage.class, 59),
    per_solarium_storage(TileEntityPerSolariumStorage.class, 60),
    quantum_storage(TileEntitySimpleQuantumStorage.class, 61),
    adv_quantum_storage(TileEntityAdvQuantumStorage.class, 62),
    imp_quantum_storage(TileEntityImpQuantumStorage.class, 63),
    per_quantum_storage(TileEntityPerQuantumStorage.class, 64),
    solardestiller(TileEntitySolarDestiller.class, 65),
    adv_solar_destiller(TileEntityAdvSolarDestiller.class, 66),
    imp_solar_destiller(TileEntityImpSolarDestiller.class, 67),
    per_solar_destiller(TileEntityPerSolarDestiller.class, 68),
    limiter(TileLimiter.class, 69),
    redstone_generator(TileRedstoneGenerator.class, 70),
    adv_redstone_generator(TileAdvRedstoneGenerator.class, 71),
    imp_redstone_generator(TileImpRedstoneGenerator.class, 72),
    per_redstone_generator(TilePerRedstoneGenerator.class, 73),
    welding(TileWeldingMachine.class, 74),
    gas_generator(TileGasGenerator.class, 76),
    pattern_storage_iu(TilePatternStorage.class, 77),
    generator_iu(TileEntityGenerator.class, 78),
    geogenerator_iu(TileEntityGeoGenerator.class, 79),
    pump_iu(TileSimplePump.class, 80),
    solar_iu(TileEntitySolarGenerator.class, 81),
    teleporter_iu(TileEntityTeleporter.class, 84),
    canner_iu(TileCanner.class, 89),

    minipanel(TileEntityMiniPanels.class, 91),
    facademechanism(TileEntityFacadeBlock.class,92);


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("basemachine3");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private TileEntityBlock dummyTe;


    BlockBaseMachine3(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockBaseMachine3(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }

    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockBaseMachine3 block : values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception e) {

                }
            }
        }

    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public int getId() {
        return this.itemMeta;
    }

    @Override
    @Nonnull
    public ResourceLocation getIdentifier() {
        return IDENTITY;
    }

    @Override
    public boolean hasItem() {
        return true;
    }

    @Override
    public Class<? extends TileEntityBlock> getTeClass() {
        return this.teClass;
    }

    @Override
    public boolean hasActive() {
        return true;
    }

    @Override
    @Nonnull
    public Set<EnumFacing> getSupportedFacings() {
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 3.0f;
    }

    @Override
    @Nonnull
    public MultiTileBlock.HarvestTool getHarvestTool() {
        return MultiTileBlock.HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public MultiTileBlock.DefaultDrop getDefaultDrop() {
        return MultiTileBlock.DefaultDrop.Machine;
    }

    @Override
    public boolean allowWrenchRotating() {
        return true;
    }

    @Override
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }

    @Override
    public boolean hasUniqueRender(final ItemStack itemStack) {
        return false;
    }

    @Override
    public ModelResourceLocation getModelLocation(final ItemStack itemStack) {
        return null;
    }
}
