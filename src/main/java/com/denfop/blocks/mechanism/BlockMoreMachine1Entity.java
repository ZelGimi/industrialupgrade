package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.mechanism.multimechanism.dual.BlockEntityCombDoubleMacerator;
import com.denfop.blockentity.mechanism.multimechanism.dual.BlockEntityDoubleCombRecycler;
import com.denfop.blockentity.mechanism.multimechanism.dual.BlockEntityDoubleRecycler;
import com.denfop.blockentity.mechanism.multimechanism.quad.BlockEntityCombQuadMacerator;
import com.denfop.blockentity.mechanism.multimechanism.quad.BlockEntityQuadCombRecycler;
import com.denfop.blockentity.mechanism.multimechanism.quad.BlockEntityQuadRecycler;
import com.denfop.blockentity.mechanism.multimechanism.simple.BlockEntityCombMacerator;
import com.denfop.blockentity.mechanism.multimechanism.triple.BlockEntityCombTripleMacerator;
import com.denfop.blockentity.mechanism.multimechanism.triple.BlockEntityTripleCombRecycler;
import com.denfop.blockentity.mechanism.multimechanism.triple.BlockEntityTripleRecycler;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
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

public enum BlockMoreMachine1Entity implements MultiBlockEntity {
    double_recycler(BlockEntityDoubleRecycler.class, 0),
    triple_recycler(BlockEntityTripleRecycler.class, 1),
    quad_recycler(BlockEntityQuadRecycler.class, 2),
    double_comb_recycler(BlockEntityDoubleCombRecycler.class, 3),
    triple_comb_recycler(BlockEntityTripleCombRecycler.class, 4),
    quad_comb_recycler(BlockEntityQuadCombRecycler.class, 5),
    comb_macerator(BlockEntityCombMacerator.class, 6),
    double_comb_macerator(BlockEntityCombDoubleMacerator.class, 7),
    triple_comb_macerator(BlockEntityCombTripleMacerator.class, 8),
    quad_comb_macerator(BlockEntityCombQuadMacerator.class, 9),

    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlockMoreMachine1Entity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
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
        final ModContainer mc = IUCore.instance.modContainer;
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
    public void setType(DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockEntityType) {
        this.blockType = blockEntityType;
    }

    @Override
    public BlockEntityType<? extends BlockEntityBase> getBlockType() {
        return this.blockType.get();
    }

    @Override
    public String getMainPath() {
        return "moremachine1";
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
