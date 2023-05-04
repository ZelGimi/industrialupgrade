package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.tiles.transformer.TileEntityHEEVTransformer;
import com.denfop.tiles.transformer.TileEntityTransformerEV;
import com.denfop.tiles.transformer.TileEntityTransformerHV;
import com.denfop.tiles.transformer.TileEntityTransformerLV;
import com.denfop.tiles.transformer.TileEntityTransformerMV;
import com.denfop.tiles.transformer.TileEntityUEVTransformer;
import com.denfop.tiles.transformer.TileEntityUHEVTransformer;
import com.denfop.tiles.transformer.TileEntityUHVTransformer;
import com.denfop.tiles.transformer.TileEntityUMEVTransformer;
import com.denfop.tiles.transformer.TileEntityUMHVTransformer;
import com.denfop.tiles.transformer.TileEntityUMVTransformer;
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

public enum BlockTransformer implements ITeBlock {

    umv(TileEntityUMVTransformer.class, 0),
    uhv(TileEntityUHVTransformer.class, 1),
    uev(TileEntityUEVTransformer.class, 2),
    umhv(TileEntityUMHVTransformer.class, 3),
    umev(TileEntityUMEVTransformer.class, 4),
    uhev(TileEntityUHEVTransformer.class, 5),
    heev(TileEntityHEEVTransformer.class, 6),
    lv(TileEntityTransformerLV.class, 7),
    mv(TileEntityTransformerMV.class, 8),
    hv(TileEntityTransformerHV.class, 9),
    ev(TileEntityTransformerEV.class, 10),
    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("transformer_iu");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockTransformer(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockTransformer(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
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
        for (final BlockTransformer block : values()) {
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
        return IDENTITY;
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
}
