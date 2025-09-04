package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.ElectricBlock;
import com.denfop.api.Storage;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.base.BlockEntityElectricBlock;
import com.denfop.blockentity.wiring.storage.*;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum BlockEnergyStorageEntity implements MultiBlockEntity, ElectricBlock {
    adv_mfsu(BlockEntityElectricAdvMFSU.class, 0),

    ult_mfsu(BlockEntityElectricUltMFSU.class, 1),
    batbox_iu(BlockEntityElectricBatBox.class, 2),

    mfe_iu(BlockEntityElectricMFE.class, 3),
    mfsu_iu(BlockEntityElectricMFSU.class, 4),
    cesu_iu(BlockEntityElectricCESU.class, 5),
    per_mfsu(BlockEntityElectricPerMFSU.class, 6),
    bar_mfsu(BlockEntityElectricBarMFSU.class, 7),
    had_mfsu(BlockEntityElectricHadrMFSU.class, 8),
    gra_mfsu(BlockEntityElectricGraMFSU.class, 9),
    qua_mfsu(BlockEntityElectricKvrMFSU.class, 10),

    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockType;

    BlockEnergyStorageEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    ;

    BlockEnergyStorageEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta, final Rarity rarity) {
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
    public Class<? extends BlockEntityBase> getTeClass() {
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
                this.dummyTe = (BlockEntityBase) this.teClass.getConstructors()[0].newInstance(BlockPos.ZERO, defaultState);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        ModUtils.nbt(stack).putDouble(
                "energy",
                ((Storage) (((ElectricBlock) this).getDummyElec())).getEUCapacity()
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
    public BlockEntityBase getDummyTe() {
        return this.dummyTe;
    }

    @Override
    public BlockEntityElectricBlock getDummyElec() {
        return (BlockEntityElectricBlock) this.dummyTe;
    }
}
