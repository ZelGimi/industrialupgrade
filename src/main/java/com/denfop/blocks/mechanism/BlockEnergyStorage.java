package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IElectricBlock;
import com.denfop.api.IStorage;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.wiring.storage.TileElectricAdvMFSU;
import com.denfop.tiles.wiring.storage.TileElectricBarMFSU;
import com.denfop.tiles.wiring.storage.TileElectricBatBox;
import com.denfop.tiles.wiring.storage.TileElectricCESU;
import com.denfop.tiles.wiring.storage.TileElectricGraMFSU;
import com.denfop.tiles.wiring.storage.TileElectricHadrMFSU;
import com.denfop.tiles.wiring.storage.TileElectricKvrMFSU;
import com.denfop.tiles.wiring.storage.TileElectricMFE;
import com.denfop.tiles.wiring.storage.TileElectricMFSU;
import com.denfop.tiles.wiring.storage.TileElectricPerMFSU;
import com.denfop.tiles.wiring.storage.TileElectricUltMFSU;
import com.denfop.utils.ModUtils;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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


    //
    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("wiring_storage");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockEnergyStorage(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockEnergyStorage(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }

    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockEnergyStorage block : BlockEnergyStorage.values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception e) {

                }
            }
        }
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
    @Nonnull
    public ResourceLocation getIdentifier() {
        return BlockEnergyStorage.IDENTITY;
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
                this.dummyTe = this.teClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        ModUtils.nbt(stack).setDouble(
                "energy",
                ((IStorage) (((IElectricBlock) this).getDummyElec())).getEUCapacity()
        );
        list.add(stack);
        return list;
    }

    @Override
    @Nonnull
    public Set<EnumFacing> getSupportedFacings() {
        return ModUtils.allFacings;
    }

    @Override
    public float getHardness() {
        return 3.0f;
    }

    @Override
    @Nonnull
    public MultiTileBlock.HarvestTool getHarvestTool() {
        return MultiTileBlock.HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public MultiTileBlock.DefaultDrop getDefaultDrop() {
        return MultiTileBlock.DefaultDrop.Self;
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
