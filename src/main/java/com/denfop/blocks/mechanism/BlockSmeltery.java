package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.smeltery.*;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockSmeltery implements IMultiTileBlock {

    smeltery_controller(TileEntitySmelteryController.class, 0),
    smeltery_casing(TileEntitySmelteryCasing.class, 1),
    smeltery_fuel_tank(TileEntitySmelteryFuelTank.class, 2),
    smeltery_tank(TileEntitySmelteryFluidTank.class, 3),
    smeltery_furnace(TileEntitySmelteryFurnace.class, 4),
    smeltery_casting(TileEntitySmelteryCasting.class, 5),
    ;


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockType;

    ;

    BlockSmeltery(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    ;

    BlockSmeltery(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final Rarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;


    }

    public int getIDBlock() {
        return idBlock;
    }

    public void setIdBlock(int id) {
        idBlock = id;
    }

    @Override
    public Material getMaterial() {
        return Material.METAL;
    }

    public void buildDummies() {
        final ModContainer mc = ModLoadingContext.get().getActiveContainer();
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
    public void setType(RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockEntityType) {
        this.blockType = blockEntityType;
    }

    @Override
    public BlockEntityType<? extends TileEntityBlock> getBlockType() {
        return this.blockType.get();
    }

    @Override
    public String getMainPath() {
        return "smeltery";
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
        return 1.0F;
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
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }

    @Override
    public String[] getMultiModels(final IMultiTileBlock teBlock) {
        return IMultiTileBlock.super.getMultiModels(teBlock);
    }
}
