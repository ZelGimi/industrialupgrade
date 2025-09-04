package com.denfop.blocks;

import com.denfop.Constants;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityAnvil;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockAnvilEntity implements MultiBlockEntity {

    block_anvil(BlockEntityAnvil.class, 0),
    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlockAnvilEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
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
        if (this.teClass != null) {
            try {
                this.dummyTe = (BlockEntityBase) this.teClass.getConstructors()[0].newInstance(BlockPos.ZERO, defaultState);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public BlockEntityType<? extends BlockEntityBase> getBlockType() {
        return this.blockType.get();
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
    public String getMainPath() {
        return "block_anvil";
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
        return false;
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
        return HarvestTool.Pickaxe;
    }

    @Override
    @Nonnull
    public DefaultDrop getDefaultDrop() {
        return DefaultDrop.Self;
    }

    @Override
    public boolean allowWrenchRotating() {
        return false;
    }

    @Override
    public Material getMaterial() {
        return Material.METAL;
    }

    @Override
    public BlockEntityBase getDummyTe() {
        return this.dummyTe;
    }


    @Override
    public String[] getMultiModels(final MultiBlockEntity teBlock) {
        return new String[]{"1", "2", "3"};
    }
}
