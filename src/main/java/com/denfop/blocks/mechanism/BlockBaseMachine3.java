package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.tiles.base.TileEntityAirCollector;
import com.denfop.tiles.base.TileEntityAntiMagnet;
import com.denfop.tiles.base.TileEntityAntiUpgradeBlock;
import com.denfop.tiles.base.TileEntityAutoSpawner;
import com.denfop.tiles.base.TileEntityCombinerSEGenerators;
import com.denfop.tiles.base.TileEntityCooling;
import com.denfop.tiles.base.TileEntityRadiationPurifier;
import com.denfop.tiles.base.TileEntityWaterGenerator;
import com.denfop.tiles.mechanism.TileEntityCreatorSchedules;
import com.denfop.tiles.mechanism.TileEntityElectricHeat;
import com.denfop.tiles.mechanism.TileEntityEnderAssembler;
import com.denfop.tiles.mechanism.TileEntityFluidHeat;
import com.denfop.tiles.mechanism.TileEntityGasCombiner;
import com.denfop.tiles.mechanism.TileEntityKineticGeneratorAssembler;
import com.denfop.tiles.mechanism.TileEntityPrivatizer;
import com.denfop.tiles.mechanism.TileEntityProbeAssembler;
import com.denfop.tiles.mechanism.TileEntityResearchTable;
import com.denfop.tiles.mechanism.TileEntityResearchTableSpace;
import com.denfop.tiles.mechanism.TileEntityRocketAssembler;
import com.denfop.tiles.mechanism.TileEntityRocketLaunchPad;
import com.denfop.tiles.mechanism.TileEntityRodManufacturer;
import com.denfop.tiles.mechanism.TileEntityRotorAssembler;
import com.denfop.tiles.mechanism.TileEntityRotorMofifier;
import com.denfop.tiles.mechanism.TileEntitySatelliteAssembler;
import com.denfop.tiles.mechanism.TileEntitySoilAnalyzer;
import com.denfop.tiles.mechanism.TileEntityTunerWireless;
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
    rotor_modifier(TileEntityRotorMofifier.class, 18),
    satellite_assembler(TileEntitySatelliteAssembler.class, 19),
    soil_analyzer(TileEntitySoilAnalyzer.class, 20),
    rods_manufacturer(TileEntityRodManufacturer.class, 21),
    kinetic_generator_assembler(TileEntityKineticGeneratorAssembler.class, 22),
    ender_assembler(TileEntityEnderAssembler.class, 23),
    research_table(TileEntityResearchTable.class, 24),
    creator_schedules(TileEntityCreatorSchedules.class, 25),
    gas_combiner(TileEntityGasCombiner.class, 26),
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
