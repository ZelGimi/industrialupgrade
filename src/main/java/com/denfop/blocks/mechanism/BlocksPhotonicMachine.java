package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.mechanism.BlockEntityPhoCombPump;
import com.denfop.blockentity.mechanism.BlockEntityPhotonicHandlerHO;
import com.denfop.blockentity.mechanism.BlockEntityPhotonicReplicator;
import com.denfop.blockentity.mechanism.BlockEntityPhotonicScanner;
import com.denfop.blockentity.mechanism.generator.energy.coal.BlockEntityPhoGenerator;
import com.denfop.blockentity.mechanism.generator.energy.fluid.BlockEntityPhoGeoGenerator;
import com.denfop.blockentity.mechanism.generator.energy.redstone.BlockEntityPhoRedstoneGenerator;
import com.denfop.blockentity.mechanism.generator.things.matter.BlockEntityPhotonicMatter;
import com.denfop.blockentity.mechanism.multimechanism.photonic.*;
import com.denfop.blockentity.mechanism.quantum_storage.BlockEntityPhoQuantumStorage;
import com.denfop.blockentity.mechanism.solardestiller.BlockEntityPhotonicDestiller;
import com.denfop.blockentity.mechanism.solarium_storage.BlockEntityPhoSolariumStorage;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlocksPhotonicMachine implements MultiBlockEntity {
    photonic_macerator(BlockEntityPhotonicMacerator.class, 0),
    photonic_compressor(BlockEntityPhotonicCompressor.class, 1),
    photonic_extractor(BlockEntityPhotonicExtractor.class, 2),
    photonic_furnace(BlockEntityPhotonicElectricFurnace.class, 3),
    photonic_rolling(BlockEntityPhotonicRolling.class, 4),
    photonic_extruding(BlockEntityPhotonicExtruder.class, 5),
    photonic_cutting(BlockEntityPhotonicCutting.class, 6),
    photonic_fermer(BlockEntityPhotonicFermer.class, 7),
    photonic_assembler(BlockEntityPhotonicAssemblerScrap.class, 8),
    photonic_centrifuge(BlockEntityPhotonicCentrifuge.class, 9),
    photonic_orewashing(BlockEntityPhotonicOreWashing.class, 10),
    photonic_gearing(BlockEntityPhotonicGearMachine.class, 11),
    photonic_gen_matter(BlockEntityPhotonicMatter.class, 12),
    photonic_comb_mac(BlockEntityPhotonicCombMacerator.class, 13),
    photonic_scanner(BlockEntityPhotonicScanner.class, 14),
    photonic_replicator(BlockEntityPhotonicReplicator.class, 15),
    photonic_handlerho(BlockEntityPhotonicHandlerHO.class, 16),
    photonic_destiller(BlockEntityPhotonicDestiller.class, 17),
    photonic_quantum_storage(BlockEntityPhoQuantumStorage.class, 18),
    photonic_solarium_storage(BlockEntityPhoSolariumStorage.class, 19),
    photonic_comb_recycler(BlockEntityPhoCombRecycler.class, 20),
    photonic_comb_pump(BlockEntityPhoCombPump.class, 21),
    photonic_geogenerator(BlockEntityPhoGeoGenerator.class, 22),
    photonic_redstone_generator(BlockEntityPhoRedstoneGenerator.class, 23),
    photonic_generator(BlockEntityPhoGenerator.class, 24),
    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;

    ;
    private RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlocksPhotonicMachine(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
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
        final ModContainer mc = ModLoadingContext.get().getActiveContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        if (this.getTeClass() != null) {
            try {
                this.dummyTe = (BlockEntityBase) this.teClass.getConstructors()[0].newInstance(BlockPos.ZERO, defaultState);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void setDefaultState(BlockState blockState) {
        this.defaultState = blockState;
    }

    @Override
    public void setType(RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockEntityType) {
        this.blockType = blockEntityType;
    }

    @Override
    public BlockEntityType<? extends BlockEntityBase> getBlockType() {
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
    public Class<? extends BlockEntityBase> getTeClass() {
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
    public BlockEntityBase getDummyTe() {
        return this.dummyTe;
    }
}
