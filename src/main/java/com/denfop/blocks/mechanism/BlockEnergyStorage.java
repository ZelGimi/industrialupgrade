package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IElectricBlock;
import com.denfop.tiles.base.TileEntityElectricBlock;
import com.denfop.tiles.wiring.storage.TileEntityElectricAdvMFSU;
import com.denfop.tiles.wiring.storage.TileEntityElectricBarMFSU;
import com.denfop.tiles.wiring.storage.TileEntityElectricBatBox;
import com.denfop.tiles.wiring.storage.TileEntityElectricCESU;
import com.denfop.tiles.wiring.storage.TileEntityElectricGraMFSU;
import com.denfop.tiles.wiring.storage.TileEntityElectricHadrMFSU;
import com.denfop.tiles.wiring.storage.TileEntityElectricKvrMFSU;
import com.denfop.tiles.wiring.storage.TileEntityElectricMFE;
import com.denfop.tiles.wiring.storage.TileEntityElectricMFSU;
import com.denfop.tiles.wiring.storage.TileEntityElectricPerMFSU;
import com.denfop.tiles.wiring.storage.TileEntityElectricUltMFSU;
import ic2.core.block.ITeBlock;
import ic2.core.block.TileEntityBlock;
import ic2.core.ref.TeBlock;
import ic2.core.util.Util;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockEnergyStorage implements ITeBlock, IElectricBlock {
    adv_mfsu(TileEntityElectricAdvMFSU.class, 0),

    ult_mfsu(TileEntityElectricUltMFSU.class, 1),
    batbox_iu(TileEntityElectricBatBox.class, 2),

    mfe_iu(TileEntityElectricMFE.class, 3),
    mfsu_iu(TileEntityElectricMFSU.class, 4),
    cesu_iu(TileEntityElectricCESU.class, 5),
    per_mfsu(TileEntityElectricPerMFSU.class, 6),
    bar_mfsu(TileEntityElectricBarMFSU.class, 7),
    had_mfsu(TileEntityElectricHadrMFSU.class, 8),
    gra_mfsu(TileEntityElectricGraMFSU.class, 9),
    qua_mfsu(TileEntityElectricKvrMFSU.class, 10),

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

    public static void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockEnergyStorage block : BlockEnergyStorage.values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception e) {
                    if (Util.inDev()) {
                        e.printStackTrace();
                    }
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
    @Nonnull
    public Set<EnumFacing> getSupportedFacings() {
        return Util.allFacings;
    }

    @Override
    public float getHardness() {
        return 3.0f;
    }

    @Override
    public float getExplosionResistance() {
        return 0.0f;
    }

    @Override
    @Nonnull
    public TeBlock.HarvestTool getHarvestTool() {
        return TeBlock.HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public TeBlock.DefaultDrop getDefaultDrop() {
        return TeBlock.DefaultDrop.Self;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity() {
        return this.rarity;
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
    public TileEntityElectricBlock getDummyElec() {
        return (TileEntityElectricBlock) this.dummyTe;
    }
}
