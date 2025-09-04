package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.mechanism.multimechanism.dual.BlockEntityDoubleCompressor;
import com.denfop.blockentity.mechanism.multimechanism.dual.BlockEntityDoubleElectricFurnace;
import com.denfop.blockentity.mechanism.multimechanism.dual.BlockEntityDoubleExtractor;
import com.denfop.blockentity.mechanism.multimechanism.dual.BlockEntityDoubleMacerator;
import com.denfop.blockentity.mechanism.multimechanism.quad.BlockEntityQuadCompressor;
import com.denfop.blockentity.mechanism.multimechanism.quad.BlockEntityQuadElectricFurnace;
import com.denfop.blockentity.mechanism.multimechanism.quad.BlockEntityQuadExtractor;
import com.denfop.blockentity.mechanism.multimechanism.quad.BlockEntityQuadMacerator;
import com.denfop.blockentity.mechanism.multimechanism.triple.BlockEntityTripleCompressor;
import com.denfop.blockentity.mechanism.multimechanism.triple.BlockEntityTripleElectricFurnace;
import com.denfop.blockentity.mechanism.multimechanism.triple.BlockEntityTripleExtractor;
import com.denfop.blockentity.mechanism.multimechanism.triple.BlockEntityTripleMacerator;
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

public enum BlockMoreMachineEntity implements MultiBlockEntity {
    double_macerator(BlockEntityDoubleMacerator.class, 0),
    triple_macerator(BlockEntityTripleMacerator.class, 1),
    quad_macerator(BlockEntityQuadMacerator.class, 2),
    double_commpressor(BlockEntityDoubleCompressor.class, 3),
    triple_commpressor(BlockEntityTripleCompressor.class, 4),
    quad_commpressor(BlockEntityQuadCompressor.class, 5),
    double_furnace(BlockEntityDoubleElectricFurnace.class, 6),
    triple_furnace(BlockEntityTripleElectricFurnace.class, 7),
    quad_furnace(BlockEntityQuadElectricFurnace.class, 8),
    double_extractor(BlockEntityDoubleExtractor.class, 9),
    triple_extractor(BlockEntityTripleExtractor.class, 10),
    quad_extractor(BlockEntityQuadExtractor.class, 11),


    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlockMoreMachineEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;


    }

    ;

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
        return "moremachine";
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
    public BlockEntityBase getDummyTe() {
        return this.dummyTe;
    }
}
