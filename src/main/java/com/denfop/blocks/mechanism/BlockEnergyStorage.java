package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IElectricBlock;
import com.denfop.api.IStorage;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.wiring.storage.*;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

;

public enum BlockEnergyStorage implements IMultiTileBlock, IElectricBlock {
    adv_mfsu(TileElectricAdvMFSU.class, 0),

    ult_mfsu(TileElectricUltMFSU.class, 1),
    batbox_iu(TileElectricBatBox.class, 2),

    mfe_iu(TileElectricMFE.class, 3),
    mfsu_iu(TileElectricMFSU.class, 4),
    cesu_iu(TileElectricCESU.class, 5),
    per_mfsu(TileElectricPerMFSU.class, 6),
    bar_mfsu(TileElectricBarMFSU.class, 7),
    had_mfsu(TileElectricHadrMFSU.class, 8),
    gra_mfsu(TileElectricGraMFSU.class, 9),
    qua_mfsu(TileElectricKvrMFSU.class, 10),

    ;


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TileEntityBlock>> blockType;

    BlockEnergyStorage(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    ;

    BlockEnergyStorage(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final Rarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;


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
    public String getName() {
        return this.name();
    }

    @Override
    public int getId() {
        return this.itemMeta;
    }

    @Override
    public String getMainPath() {
        return "wiring_storage";
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
        return false;
    }

    @Override
    public boolean hasOtherVersion() {
        return true;
    }

    @Override
    public List<ItemStack> getOtherVersion(ItemStack stack) {
        final List<ItemStack> list = new ArrayList<>();
        stack = stack.copy();
        if (this.dummyTe == null) {
            try {
                this.dummyTe = (TileEntityBlock) this.teClass.getConstructors()[0].newInstance(BlockPos.ZERO, defaultState);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        ModUtils.nbt(stack).putDouble(
                "energy",
                ((IStorage) (((IElectricBlock) this).getDummyElec())).getEUCapacity()
        );
        list.add(stack);
        return list;
    }

    @Override
    @Nonnull
    public Set<Direction> getSupportedFacings() {
        return ModUtils.allFacings;
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
        return DefaultDrop.Self;
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
    public TileElectricBlock getDummyElec() {
        return (TileElectricBlock) this.dummyTe;
    }
}
