package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleAssamplerScrap;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleCentrifuge;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleFermer;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleGearMachine;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleOreWashing;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadAssamplerScrap;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadCentrifuge;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadFermer;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadGearMachine;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadOreWashing;
import com.denfop.tiles.mechanism.multimechanism.simple.TileAssamplerScrap;
import com.denfop.tiles.mechanism.multimechanism.simple.TileCentrifuge;
import com.denfop.tiles.mechanism.multimechanism.simple.TileFermer;
import com.denfop.tiles.mechanism.multimechanism.simple.TileGearMachine;
import com.denfop.tiles.mechanism.multimechanism.simple.TileOreWashing;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleAssamplerScrap;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleCentrifuge;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleFermer;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleGearMachine;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleOreWashing;
import com.denfop.utils.ModUtils;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockMoreMachine3 implements IMultiTileBlock {
    farmer(TileFermer.class, 0),
    double_farmer(TileDoubleFermer.class, 1),
    triple_farmer(TileTripleFermer.class, 2),
    quad_farmer(TileQuadFermer.class, 3),
    assamplerscrap(TileAssamplerScrap.class, 4),
    double_assamplerscrap(TileDoubleAssamplerScrap.class, 5),
    triple_assamplerscrap(TileTripleAssamplerScrap.class, 6),
    quad_assamplerscrap(TileQuadAssamplerScrap.class, 7),
    orewashing(TileOreWashing.class, 8),
    doubleorewashing(TileDoubleOreWashing.class, 9),
    tripleorewashing(TileTripleOreWashing.class, 10),
    quadorewashing(TileQuadOreWashing.class, 11),
    centrifuge_iu(TileCentrifuge.class, 12),
    doublecentrifuge(TileDoubleCentrifuge.class, 13),
    triplecentrifuge(TileTripleCentrifuge.class, 14),
    quadcentrifuge(TileQuadCentrifuge.class, 15),
    gearing(TileGearMachine.class, 16),
    doublegearing(TileDoubleGearMachine.class, 17),
    triplegearing(TileTripleGearMachine.class, 18),
    quadgearing(TileQuadGearMachine.class, 19),
    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("moremachine3");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    int idBlock;
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

    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockMoreMachine3 block : BlockMoreMachine3.values()) {
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

    public int getIDBlock() {
        return idBlock;
    }

    ;

    public void setIdBlock(int id) {
        idBlock = id;
    }

    ;

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
