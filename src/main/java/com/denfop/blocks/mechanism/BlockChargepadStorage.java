package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IElectricBlock;
import com.denfop.tiles.base.TileEntityElectricBlock;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadAdvMFSU;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadBarMFSU;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadBatBox;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadCESU;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadGraMFSU;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadHadrMFSU;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadKvrMFSU;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadMFE;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadMFSU;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadPerMFSU;
import com.denfop.tiles.wiring.chargepad.TileEntityChargepadUltMFSU;
import ic2.core.block.ITeBlock;
import ic2.core.block.TileEntityBlock;
import ic2.core.ref.IC2Material;
import ic2.core.ref.TeBlock;
import ic2.core.util.Util;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockChargepadStorage implements ITeBlock, IElectricBlock {
    adv_mfsu_chargepad(TileEntityChargepadAdvMFSU.class, 0),
    ult_mfsu_chargepad(TileEntityChargepadUltMFSU.class, 1),
    batbox_iu_chargepad(TileEntityChargepadBatBox.class, 2),
    cesu_iu_chargepad(TileEntityChargepadCESU.class, 3),
    mfe_iu_chargepad(TileEntityChargepadMFE.class, 4),
    mfsu_iu_chargepad(TileEntityChargepadMFSU.class, 5),
    per_mfsu_chargepad(TileEntityChargepadPerMFSU.class, 6),
    bar_mfsu_chargepad(TileEntityChargepadBarMFSU.class, 7),
    had_mfsu_chargepad(TileEntityChargepadHadrMFSU.class, 8),
    gra_mfsu_chargepad(TileEntityChargepadGraMFSU.class, 9),
    qua_mfsu_chargepad(TileEntityChargepadKvrMFSU.class, 10),

    ;


    //
    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("wiring_chargepad");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockChargepadStorage(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockChargepadStorage(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
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
        for (final BlockChargepadStorage block : BlockChargepadStorage.values()) {
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
    public Material getMaterial() {
        return IC2Material.MACHINE;
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
        return BlockChargepadStorage.IDENTITY;
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
    public Set<EnumFacing> getSupportedFacings() {
        return Util.downSideFacings;
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
