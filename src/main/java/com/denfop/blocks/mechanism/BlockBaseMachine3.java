package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.tiles.base.TileEntityAntiMagnet;
import com.denfop.tiles.base.TileEntityAntiUpgradeBlock;
import com.denfop.tiles.base.TileEntityAutoDigger;
import com.denfop.tiles.base.TileEntityAutoSpawner;
import com.denfop.tiles.base.TileEntityCombinerSEGenerators;
import com.denfop.tiles.base.TileEntityLimiter;
import com.denfop.tiles.base.TileEntityRadiationPurifier;
import com.denfop.tiles.mechanism.TileEntityAdvHandlerHeavyOre;
import com.denfop.tiles.mechanism.TileEntityAdvScanner;
import com.denfop.tiles.mechanism.TileEntityAnalyzerChest;
import com.denfop.tiles.mechanism.TileEntityCreatorSchedules;
import com.denfop.tiles.mechanism.TileEntityGasCombiner;
import com.denfop.tiles.mechanism.TileEntityImpHandlerHeavyOre;
import com.denfop.tiles.mechanism.TileEntityImpScanner;
import com.denfop.tiles.mechanism.TileEntityKineticGeneratorAssembler;
import com.denfop.tiles.mechanism.TileEntityPerHandlerHeavyOre;
import com.denfop.tiles.mechanism.TileEntityPerScanner;
import com.denfop.tiles.mechanism.TileEntityPrivatizer;
import com.denfop.tiles.mechanism.TileEntityProbeAssembler;
import com.denfop.tiles.mechanism.TileEntityResearchTable;
import com.denfop.tiles.mechanism.TileEntityResearchTableSpace;
import com.denfop.tiles.mechanism.TileEntityRocketAssembler;
import com.denfop.tiles.mechanism.TileEntityRocketLaunchPad;
import com.denfop.tiles.mechanism.TileEntityRodManufacturer;
import com.denfop.tiles.mechanism.TileEntityRotorAssembler;
import com.denfop.tiles.mechanism.TileEntityRotorModifier;
import com.denfop.tiles.mechanism.TileEntitySatelliteAssembler;
import com.denfop.tiles.mechanism.TileEntitySimpleScanner;
import com.denfop.tiles.mechanism.TileEntitySoilAnalyzer;
import com.denfop.tiles.mechanism.TileEntityTunerWireless;
import com.denfop.tiles.mechanism.TileEntityWaterRotorAssembler;
import com.denfop.tiles.mechanism.TileEntityWaterRotorModifier;
import com.denfop.tiles.mechanism.cooling.TileEntityCooling;
import com.denfop.tiles.mechanism.energy.TileEntityEnergyController;
import com.denfop.tiles.mechanism.energy.TileEntityEnergySubstitute;
import com.denfop.tiles.mechanism.generator.energy.redstone.TileEntityAdvRedstoneGenerator;
import com.denfop.tiles.mechanism.generator.energy.redstone.TileEntityImpRedstoneGenerator;
import com.denfop.tiles.mechanism.generator.energy.redstone.TileEntityPerRedstoneGenerator;
import com.denfop.tiles.mechanism.generator.energy.redstone.TileEntityRedstoneGenerator;
import com.denfop.tiles.mechanism.generator.things.fluid.TileEntityAirCollector;
import com.denfop.tiles.mechanism.generator.things.fluid.TileEntityWaterGenerator;
import com.denfop.tiles.mechanism.heat.TileEntityElectricHeat;
import com.denfop.tiles.mechanism.heat.TileEntityFluidHeat;
import com.denfop.tiles.mechanism.quantum_storage.TileEntityAdvQuantumStorage;
import com.denfop.tiles.mechanism.quantum_storage.TileEntityImpQuantumStorage;
import com.denfop.tiles.mechanism.quantum_storage.TileEntityPerQuantumStorage;
import com.denfop.tiles.mechanism.quantum_storage.TileEntitySimpleQuantumStorage;
import com.denfop.tiles.mechanism.replicator.TileEntityAdvReplicator;
import com.denfop.tiles.mechanism.replicator.TileEntityImpReplicator;
import com.denfop.tiles.mechanism.replicator.TileEntityPerReplicator;
import com.denfop.tiles.mechanism.replicator.TileEntityReplicator;
import com.denfop.tiles.mechanism.solardestiller.TileEntityAdvSolarDestiller;
import com.denfop.tiles.mechanism.solardestiller.TileEntityImpSolarDestiller;
import com.denfop.tiles.mechanism.solardestiller.TileEntityPerSolarDestiller;
import com.denfop.tiles.mechanism.solardestiller.TileEntitySolarDestiller;
import com.denfop.tiles.mechanism.solarium_storage.TileEntityAdvSolariumStorage;
import com.denfop.tiles.mechanism.solarium_storage.TileEntityImpSolariumStorage;
import com.denfop.tiles.mechanism.solarium_storage.TileEntityPerSolariumStorage;
import com.denfop.tiles.mechanism.solarium_storage.TileEntitySimpleSolariumStorage;
import com.denfop.tiles.mechanism.water.TileEntityAdvWaterGenerator;
import com.denfop.tiles.mechanism.water.TileEntityImpWaterGenerator;
import com.denfop.tiles.mechanism.water.TileEntityPerWaterGenerator;
import com.denfop.tiles.mechanism.water.TileEntitySimpleWaterGenerator;
import com.denfop.tiles.mechanism.wind.TileEntityAdvWindGenerator;
import com.denfop.tiles.mechanism.wind.TileEntityImpWindGenerator;
import com.denfop.tiles.mechanism.wind.TileEntityPerWindGenerator;
import com.denfop.tiles.mechanism.wind.TileEntitySimpleWindGenerator;
import com.denfop.tiles.mechanism.worlcollector.TileEntityAerAssembler;
import com.denfop.tiles.mechanism.worlcollector.TileEntityAquaAssembler;
import com.denfop.tiles.mechanism.worlcollector.TileEntityCrystallize;
import com.denfop.tiles.mechanism.worlcollector.TileEntityEarthAssembler;
import com.denfop.tiles.mechanism.worlcollector.TileEntityEnderAssembler;
import com.denfop.tiles.mechanism.worlcollector.TileEntityNetherAssembler;
import ic2.api.item.ITeBlockSpecialItem;
import ic2.core.block.ITeBlock;
import ic2.core.block.TileEntityBlock;
import ic2.core.ref.TeBlock;
import ic2.core.util.Util;
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

public enum BlockBaseMachine3 implements ITeBlock, ITeBlockSpecialItem {
    radiation_purifier(TileEntityRadiationPurifier.class, 1),
    privatizer(TileEntityPrivatizer.class, 2),
    tuner(TileEntityTunerWireless.class, 3),
    spawner(TileEntityAutoSpawner.class, 4),
    elec_heat(TileEntityElectricHeat.class, 5),
    fluid_heat(TileEntityFluidHeat.class, 6),
    antimagnet(TileEntityAntiMagnet.class, 7),
    antiupgradeblock(TileEntityAntiUpgradeBlock.class, 8),
    watergenerator(TileEntityWaterGenerator.class, 9),
    cooling(TileEntityCooling.class, 10),
    aircollector(TileEntityAirCollector.class, 11),
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
    ender_assembler(TileEntityEnderAssembler.class, 23),
    research_table(TileEntityResearchTable.class, 24),
    creator_schedules(TileEntityCreatorSchedules.class, 25),
    gas_combiner(TileEntityGasCombiner.class, 26),
    analyzer_chest(TileEntityAnalyzerChest.class, 27),
    simple_wind_generator(TileEntitySimpleWindGenerator.class, 28),
    adv_wind_generator(TileEntityAdvWindGenerator.class, 29),
    imp_wind_generator(TileEntityImpWindGenerator.class, 30),
    per_wind_generator(TileEntityPerWindGenerator.class, 31),
    energy_controller(TileEntityEnergyController.class, 32),
    substitute(TileEntityEnergySubstitute.class, 33),

    aer_assembler(TileEntityAerAssembler.class, 34),
    aqua_assembler(TileEntityAquaAssembler.class, 35),
    earth_assembler(TileEntityEarthAssembler.class, 36),
    nether_assembler(TileEntityNetherAssembler.class, 37),
    auto_digger(TileEntityAutoDigger.class, 38),
    crystallize(TileEntityCrystallize.class, 39),
    double_handlerho(TileEntityAdvHandlerHeavyOre.class, 40),
    triple_handlerho(TileEntityImpHandlerHeavyOre.class, 41),
    quad_handlerho(TileEntityPerHandlerHeavyOre.class, 42),
    water_modifier(TileEntityWaterRotorModifier.class, 43),
    water_rotor_assembler(TileEntityWaterRotorAssembler.class, 44),
    simple_water_generator(TileEntitySimpleWaterGenerator.class, 45),
    adv_water_generator(TileEntityAdvWaterGenerator.class, 46),
    imp_water_generator(TileEntityImpWaterGenerator.class, 47),
    per_water_generator(TileEntityPerWaterGenerator.class, 48),
    scanner_iu(TileEntitySimpleScanner.class, 49),
    adv_scanner(TileEntityAdvScanner.class, 50),
    imp_scanner(TileEntityImpScanner.class, 51),
    per_scanner(TileEntityPerScanner.class, 52),
    replicator_iu(TileEntityReplicator.class, 53),
    adv_replicator(TileEntityAdvReplicator.class, 54),
    imp_replicator(TileEntityImpReplicator.class, 55),
    per_replicator(TileEntityPerReplicator.class, 56),

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
    limiter(TileEntityLimiter.class, 69),
    redstone_generator(TileEntityRedstoneGenerator.class, 70),
    adv_redstone_generator(TileEntityAdvRedstoneGenerator.class, 71),
    imp_redstone_generator(TileEntityImpRedstoneGenerator.class, 72),
    per_redstone_generator(TileEntityPerRedstoneGenerator.class, 73),
    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("basemachine3");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockBaseMachine3(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockBaseMachine3(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }

    public static void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockBaseMachine3 block : values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception e) {
                    if (Util.inDev()) {
                        e.printStackTrace();
                    }
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
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    @Nonnull
    public Set<EnumFacing> getSupportedFacings() {
        return Util.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 3.0f;
    }

    @Override
    public float getExplosionResistance() {
        return 0.0f;
    }

    @Override
    @Nonnull
    public TeBlock.HarvestTool getHarvestTool() {
        return TeBlock.HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public TeBlock.DefaultDrop getDefaultDrop() {
        return TeBlock.DefaultDrop.Machine;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity() {
        return this.rarity;
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
    public boolean doesOverrideDefault(final ItemStack itemStack) {
        return false;
    }

    @Override
    public ModelResourceLocation getModelLocation(final ItemStack itemStack) {
        return null;
    }
}
