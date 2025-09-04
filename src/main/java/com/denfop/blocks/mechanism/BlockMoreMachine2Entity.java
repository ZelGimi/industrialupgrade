package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.mechanism.multimechanism.dual.BlockEntityDoubleCutting;
import com.denfop.blockentity.mechanism.multimechanism.dual.BlockEntityDoubleExtruding;
import com.denfop.blockentity.mechanism.multimechanism.dual.BlockEntityDoubleRolling;
import com.denfop.blockentity.mechanism.multimechanism.quad.BlockEntityQuadCutting;
import com.denfop.blockentity.mechanism.multimechanism.quad.BlockEntityQuadExtruding;
import com.denfop.blockentity.mechanism.multimechanism.quad.BlockEntityQuadRolling;
import com.denfop.blockentity.mechanism.multimechanism.simple.BlockEntityCutting;
import com.denfop.blockentity.mechanism.multimechanism.simple.BlockEntityExtruding;
import com.denfop.blockentity.mechanism.multimechanism.simple.BlockEntityRolling;
import com.denfop.blockentity.mechanism.multimechanism.triple.BlockEntityTripleCutting;
import com.denfop.blockentity.mechanism.multimechanism.triple.BlockEntityTripleExtruding;
import com.denfop.blockentity.mechanism.multimechanism.triple.BlockEntityTripleRolling;
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

public enum BlockMoreMachine2Entity implements MultiBlockEntity {
    rolling(BlockEntityRolling.class, 0),
    double_rolling(BlockEntityDoubleRolling.class, 1),
    triple_rolling(BlockEntityTripleRolling.class, 2),
    quad_rolling(BlockEntityQuadRolling.class, 3),
    extruder(BlockEntityExtruding.class, 4),
    double_extruder(BlockEntityDoubleExtruding.class, 5),
    triple_extruder(BlockEntityTripleExtruding.class, 6),
    quad_extruder(BlockEntityQuadExtruding.class, 7),
    cutting(BlockEntityCutting.class, 8),
    double_cutting(BlockEntityDoubleCutting.class, 9),
    triple_cutting(BlockEntityTripleCutting.class, 10),
    quad_cutting(BlockEntityQuadCutting.class, 11),

    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlockMoreMachine2Entity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
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
        return "moremachine2";
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
