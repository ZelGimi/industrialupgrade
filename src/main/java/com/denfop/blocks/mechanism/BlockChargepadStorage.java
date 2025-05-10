package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.IElectricBlock;
import com.denfop.api.IStorage;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.wiring.chargepad.*;
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

public enum BlockChargepadStorage implements IMultiTileBlock, IElectricBlock {
    adv_mfsu_chargepad(TileChargepadAdvMFSU.class, 0),
    ult_mfsu_chargepad(TileChargepadUltMFSU.class, 1),
    batbox_iu_chargepad(TileChargepadBatBox.class, 2),
    cesu_iu_chargepad(TileChargepadCESU.class, 3),
    mfe_iu_chargepad(TileChargepadMFE.class, 4),
    mfsu_iu_chargepad(TileChargepadMFSU.class, 5),
    per_mfsu_chargepad(TileChargepadPerMFSU.class, 6),
    bar_mfsu_chargepad(TileChargepadBarMFSU.class, 7),
    had_mfsu_chargepad(TileChargepadHadrMFSU.class, 8),
    gra_mfsu_chargepad(TileChargepadGraMFSU.class, 9),
    qua_mfsu_chargepad(TileChargepadKvrMFSU.class, 10),

    ;


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockType;

    BlockChargepadStorage(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    ;

    BlockChargepadStorage(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final Rarity rarity) {
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
        return "wiring_chargepad";
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
        return ModUtils.downSideFacings;
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
