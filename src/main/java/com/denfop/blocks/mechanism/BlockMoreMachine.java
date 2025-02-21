package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleCompressor;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleElectricFurnace;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleExtractor;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleMacerator;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadCompressor;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadElectricFurnace;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadExtractor;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadMacerator;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleCompressor;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleElectricFurnace;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleExtractor;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleMacerator;
import com.denfop.utils.ModUtils;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockMoreMachine implements IMultiTileBlock {
    double_macerator(TileDoubleMacerator.class, 0),
    triple_macerator(TileTripleMacerator.class, 1),
    quad_macerator(TileQuadMacerator.class, 2),
    double_commpressor(TileDoubleCompressor.class, 3),
    triple_commpressor(TileTripleCompressor.class, 4),
    quad_commpressor(TileQuadCompressor.class, 5),
    double_furnace(TileDoubleElectricFurnace.class, 6),
    triple_furnace(TileTripleElectricFurnace.class, 7),
    quad_furnace(TileQuadElectricFurnace.class, 8),
    double_extractor(TileDoubleExtractor.class, 9),
    triple_extractor(TileTripleExtractor.class, 10),
    quad_extractor(TileQuadExtractor.class, 11),


    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("moremachine");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockMoreMachine(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockMoreMachine(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
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
        for (final BlockMoreMachine block : BlockMoreMachine.values()) {
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
        return BlockMoreMachine.IDENTITY;
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
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }
}
