package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multimechanism.dual.TileCombDoubleMacerator;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleCombRecycler;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleRecycler;
import com.denfop.tiles.mechanism.multimechanism.quad.TileCombQuadMacerator;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadCombRecycler;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadRecycler;
import com.denfop.tiles.mechanism.multimechanism.simple.TileCombMacerator;
import com.denfop.tiles.mechanism.multimechanism.triple.TileCombTripleMacerator;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleCombRecycler;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleRecycler;
import com.denfop.utils.ModUtils;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockMoreMachine1 implements IMultiTileBlock {
    double_recycler(TileDoubleRecycler.class, 0),
    triple_recycler(TileTripleRecycler.class, 1),
    quad_recycler(TileQuadRecycler.class, 2),
    double_comb_recycler(TileDoubleCombRecycler.class, 3),
    triple_comb_recycler(TileTripleCombRecycler.class, 4),
    quad_comb_recycler(TileQuadCombRecycler.class, 5),
    comb_macerator(TileCombMacerator.class, 6),
    double_comb_macerator(TileCombDoubleMacerator.class, 7),
    triple_comb_macerator(TileCombTripleMacerator.class, 8),
    quad_comb_macerator(TileCombQuadMacerator.class, 9),

    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("moremachine1");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockMoreMachine1(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockMoreMachine1(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }
    int idBlock;
    public  int getIDBlock(){
        return idBlock;
    };

    public void setIdBlock(int id){
        idBlock = id;
    };
    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockMoreMachine1 block : BlockMoreMachine1.values()) {
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
        return BlockMoreMachine1.IDENTITY;
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
        return ModUtils.horizontalFacings;
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
        return MultiTileBlock.DefaultDrop.Machine;
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
