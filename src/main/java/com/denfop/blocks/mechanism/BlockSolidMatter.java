package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.tiles.solidmatter.TileEntityAerSolidMatter;
import com.denfop.tiles.solidmatter.TileEntityAquaSolidMatter;
import com.denfop.tiles.solidmatter.TileEntityEarthSolidMatter;
import com.denfop.tiles.solidmatter.TileEntityEndSolidMatter;
import com.denfop.tiles.solidmatter.TileEntityNetherSolidMatter;
import com.denfop.tiles.solidmatter.TileEntityNightSolidMatter;
import com.denfop.tiles.solidmatter.TileEntitySolidMatter;
import com.denfop.tiles.solidmatter.TileEntitySunSolidMatter;
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

public enum BlockSolidMatter implements ITeBlock {
    aer_solidmatter(TileEntityAerSolidMatter.class, 0),
    aqua_solidmatter(TileEntityAquaSolidMatter.class, 1),
    earth_solidmatter(TileEntityEarthSolidMatter.class, 2),
    end_solidmatter(TileEntityEndSolidMatter.class, 3),
    solidmatter(TileEntitySolidMatter.class, 4),
    nether_solidmatter(TileEntityNetherSolidMatter.class, 5),
    night_solidmatter(TileEntityNightSolidMatter.class, 6),
    sun_solidmatter(TileEntitySunSolidMatter.class, 7),
    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("solid");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockSolidMatter(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockSolidMatter(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }


    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public int getId() {
        return this.itemMeta;
    }

    public static void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockSolidMatter block : BlockSolidMatter.values()) {
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
    @Nonnull
    public ResourceLocation getIdentifier() {
        return BlockSolidMatter.IDENTITY;
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
        return Util.horizontalFacings;
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
}
