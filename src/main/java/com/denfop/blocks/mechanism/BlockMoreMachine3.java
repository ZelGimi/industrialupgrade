package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.tiles.mechanism.TileEntityAssamplerScrap;
import com.denfop.tiles.mechanism.TileEntityDoubleAssamplerScrap;
import com.denfop.tiles.mechanism.TileEntityDoubleFermer;
import com.denfop.tiles.mechanism.TileEntityFermer;
import com.denfop.tiles.mechanism.TileEntityQuadAssamplerScrap;
import com.denfop.tiles.mechanism.TileEntityQuadFermer;
import com.denfop.tiles.mechanism.TileEntityTripleAssamplerScrap;
import com.denfop.tiles.mechanism.TileEntityTripleFermer;
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

public enum BlockMoreMachine3 implements ITeBlock {
    farmer(TileEntityFermer.class, 0),
    double_farmer(TileEntityDoubleFermer.class, 1),
    triple_farmer(TileEntityTripleFermer.class, 2),
    quad_farmer(TileEntityQuadFermer.class, 3),
    assamplerscrap(TileEntityAssamplerScrap.class, 4),
    double_assamplerscrap(TileEntityDoubleAssamplerScrap.class, 5),
    triple_assamplerscrap(TileEntityTripleAssamplerScrap.class, 6),
    quad_assamplerscrap(TileEntityQuadAssamplerScrap.class, 7),

    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("moremachine3");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockMoreMachine3(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockMoreMachine3(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
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
        for (final BlockMoreMachine3 block : BlockMoreMachine3.values()) {
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
        return BlockMoreMachine3.IDENTITY;
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
        return TeBlock.DefaultDrop.Machine;
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
    @Deprecated
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }
}
