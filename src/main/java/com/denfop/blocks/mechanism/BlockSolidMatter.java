package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.tiles.solidmatter.TileEntityAerSolidEntityMatter;
import com.denfop.tiles.solidmatter.TileEntityAquaSolidEntityMatter;
import com.denfop.tiles.solidmatter.TileEntityEarthSolidEntityMatter;
import com.denfop.tiles.solidmatter.TileEntityEndSolidEntityMatter;
import com.denfop.tiles.solidmatter.TileEntityNetherSolidEntityMatter;
import com.denfop.tiles.solidmatter.TileEntityNightSolidEntityMatter;
import com.denfop.tiles.solidmatter.TileEntitySolidEntityMatter;
import com.denfop.tiles.solidmatter.TileEntitySunSolidEntityMatter;
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
    aer_solidmatter(TileEntityAerSolidEntityMatter.class, 0),
    aqua_solidmatter(TileEntityAquaSolidEntityMatter.class, 1),
    earth_solidmatter(TileEntityEarthSolidEntityMatter.class, 2),
    end_solidmatter(TileEntityEndSolidEntityMatter.class, 3),
    solidmatter(TileEntitySolidEntityMatter.class, 4),
    nether_solidmatter(TileEntityNetherSolidEntityMatter.class, 5),
    night_solidmatter(TileEntityNightSolidEntityMatter.class, 6),
    sun_solidmatter(TileEntitySunSolidEntityMatter.class, 7),
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
