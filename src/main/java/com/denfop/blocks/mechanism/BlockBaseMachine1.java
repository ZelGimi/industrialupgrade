package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.item.IMultiBlockItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.TileHandlerHeavyOre;
import com.denfop.tiles.mechanism.TileMagnet;
import com.denfop.tiles.mechanism.TileMagnetGenerator;
import com.denfop.tiles.mechanism.TileWitherMaker;
import com.denfop.tiles.mechanism.dual.TileEnrichment;
import com.denfop.tiles.mechanism.dual.TileSynthesis;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityAdvGeoGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityImpGeoGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityPerGeoGenerator;
import com.denfop.tiles.mechanism.triple.heat.TileAdvAlloySmelter;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockBaseMachine1 implements IMultiTileBlock, IMultiBlockItem {

    adv_alloy_smelter(TileAdvAlloySmelter.class, 3),
    adv_geo(TileEntityAdvGeoGenerator.class, 4),
    imp_geo(TileEntityImpGeoGenerator.class, 5),
    per_geo(TileEntityPerGeoGenerator.class, 6),
    enrichment(TileEnrichment.class, 10),
    synthesis(TileSynthesis.class, 11),
    handler_ho(TileHandlerHeavyOre.class, 12),
    gen_wither(TileWitherMaker.class, 13),
    magnet(TileMagnet.class, 14),
    magnet_generator(TileMagnetGenerator.class, 15),

    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("basemachine1");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    int idBlock;
    private TileEntityBlock dummyTe;

    BlockBaseMachine1(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockBaseMachine1(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }

    public int getIDBlock() {
        return idBlock;
    }

    ;

    public void setIdBlock(int id) {
        idBlock = id;
    }

    ;

    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockBaseMachine1 block : values()) {
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
        return true;
    }

    @Override
    @Nonnull
    public Set<EnumFacing> getSupportedFacings() {
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 1.0F;
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

    @Override
    public boolean hasUniqueRender(final ItemStack itemStack) {
        return false;
    }

    @Override
    public ModelResourceLocation getModelLocation(final ItemStack itemStack) {
        return null;
    }
}
