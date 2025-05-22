package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.TileEntityPhoCombPump;
import com.denfop.tiles.mechanism.TilePhotonicHandlerHO;
import com.denfop.tiles.mechanism.TilePhotonicReplicator;
import com.denfop.tiles.mechanism.TilePhotonicScanner;
import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityPhoGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityPhoGeoGenerator;
import com.denfop.tiles.mechanism.generator.energy.redstone.TileEntityPhoRedstoneGenerator;
import com.denfop.tiles.mechanism.generator.things.matter.TilePhotonicMatter;
import com.denfop.tiles.mechanism.multimechanism.photonic.TileEntityPhoCombRecycler;
import com.denfop.tiles.mechanism.multimechanism.photonic.TilePhotonicAssemblerScrap;
import com.denfop.tiles.mechanism.multimechanism.photonic.TilePhotonicCentrifuge;
import com.denfop.tiles.mechanism.multimechanism.photonic.TilePhotonicCombMacerator;
import com.denfop.tiles.mechanism.multimechanism.photonic.TilePhotonicCompressor;
import com.denfop.tiles.mechanism.multimechanism.photonic.TilePhotonicCutting;
import com.denfop.tiles.mechanism.multimechanism.photonic.TilePhotonicElectricFurnace;
import com.denfop.tiles.mechanism.multimechanism.photonic.TilePhotonicExtractor;
import com.denfop.tiles.mechanism.multimechanism.photonic.TilePhotonicExtruder;
import com.denfop.tiles.mechanism.multimechanism.photonic.TilePhotonicFermer;
import com.denfop.tiles.mechanism.multimechanism.photonic.TilePhotonicGearMachine;
import com.denfop.tiles.mechanism.multimechanism.photonic.TilePhotonicMacerator;
import com.denfop.tiles.mechanism.multimechanism.photonic.TilePhotonicOreWashing;
import com.denfop.tiles.mechanism.multimechanism.photonic.TilePhotonicRolling;
import com.denfop.tiles.mechanism.quantum_storage.TileEntityPhoQuantumStorage;
import com.denfop.tiles.mechanism.solardestiller.TilePhotonicDestiller;
import com.denfop.tiles.mechanism.solarium_storage.TileEntityPhoSolariumStorage;
import com.denfop.utils.ModUtils;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlocksPhotonicMachine implements IMultiTileBlock {
    photonic_macerator(TilePhotonicMacerator.class, 0),
    photonic_compressor(TilePhotonicCompressor.class, 1),
    photonic_extractor(TilePhotonicExtractor.class, 2),
    photonic_furnace(TilePhotonicElectricFurnace.class, 3),
    photonic_rolling(TilePhotonicRolling.class, 4),
    photonic_extruding(TilePhotonicExtruder.class, 5),
    photonic_cutting(TilePhotonicCutting.class, 6),
    photonic_fermer(TilePhotonicFermer.class, 7),
    photonic_assembler(TilePhotonicAssemblerScrap.class, 8),
    photonic_centrifuge(TilePhotonicCentrifuge.class, 9),
    photonic_orewashing(TilePhotonicOreWashing.class, 10),
    photonic_gearing(TilePhotonicGearMachine.class, 11),
    photonic_gen_matter(TilePhotonicMatter.class, 12),
    photonic_comb_mac(TilePhotonicCombMacerator.class, 13),
    photonic_scanner(TilePhotonicScanner.class, 14),
    photonic_replicator(TilePhotonicReplicator.class, 15),
    photonic_handlerho(TilePhotonicHandlerHO.class, 16),
    photonic_destiller(TilePhotonicDestiller.class, 17),
    photonic_quantum_storage(TileEntityPhoQuantumStorage.class, 18),
    photonic_solarium_storage(TileEntityPhoSolariumStorage.class, 19),
    photonic_comb_recycler(TileEntityPhoCombRecycler.class, 20),
    photonic_comb_pump(TileEntityPhoCombPump.class, 21),
    photonic_geogenerator(TileEntityPhoGeoGenerator.class, 22),
    photonic_redstone_generator(TileEntityPhoRedstoneGenerator.class, 23),
    photonic_generator(TileEntityPhoGenerator.class, 24),
    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("photonic_machine");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    int idBlock;
    private TileEntityBlock dummyTe;

    BlocksPhotonicMachine(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlocksPhotonicMachine(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }

    ;

    public int getIDBlock() {
        return idBlock;
    }

    ;

    public void setIdBlock(int id) {
        idBlock = id;
    }

    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlocksPhotonicMachine block : BlocksPhotonicMachine.values()) {
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
        return BlocksPhotonicMachine.IDENTITY;
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
    @Deprecated
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }
}
