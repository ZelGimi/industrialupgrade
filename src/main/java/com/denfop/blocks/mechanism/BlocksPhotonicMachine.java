package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.TileEntityPhoCombPump;
import com.denfop.tiles.mechanism.TilePhotonicHandlerHO;
import com.denfop.tiles.mechanism.TilePhotonicReplicator;
import com.denfop.tiles.mechanism.TilePhotonicScanner;
import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityPhoGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityPhoGeoGenerator;
import com.denfop.tiles.mechanism.generator.energy.redstone.TileEntityPhoRedstoneGenerator;
import com.denfop.tiles.mechanism.generator.things.matter.TilePhotonicMatter;
import com.denfop.tiles.mechanism.multimechanism.photonic.*;
import com.denfop.tiles.mechanism.quantum_storage.TileEntityPhoQuantumStorage;
import com.denfop.tiles.mechanism.solardestiller.TilePhotonicDestiller;
import com.denfop.tiles.mechanism.solarium_storage.TileEntityPhoSolariumStorage;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.util.Set;

;

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


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;

    ;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TileEntityBlock>> blockType;

    ;

    BlocksPhotonicMachine(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;


    }

    public int getIDBlock() {
        return idBlock;
    }

    public void setIdBlock(int id) {
        idBlock = id;
    }

    public void buildDummies() {
        final ModContainer mc = IUCore.instance.modContainer;
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        if (this.getTeClass() != null) {
            try {
                this.dummyTe = (TileEntityBlock) this.teClass.getConstructors()[0].newInstance(BlockPos.ZERO, defaultState);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void setDefaultState(BlockState blockState) {
        this.defaultState = blockState;
    }

    @Override
    public void setType(DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TileEntityBlock>> blockEntityType) {
        this.blockType = blockEntityType;
    }

    @Override
    public BlockEntityType<? extends TileEntityBlock> getBlockType() {
        return this.blockType.get();
    }

    @Override
    public String getMainPath() {
        return "photonic_machine";
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
    public Set<Direction> getSupportedFacings() {
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 3.0f;
    }

    @Override
    @Nonnull
    public HarvestTool getHarvestTool() {
        return HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public DefaultDrop getDefaultDrop() {
        return DefaultDrop.Machine;
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
