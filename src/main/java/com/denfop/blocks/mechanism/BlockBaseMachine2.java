package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.item.IMultiBlockItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileAdvPump;
import com.denfop.tiles.base.TileAnalyzer;
import com.denfop.tiles.base.TileCombinerMatter;
import com.denfop.tiles.base.TileElectrolyzer;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileFisher;
import com.denfop.tiles.base.TileImpPump;
import com.denfop.tiles.base.TileObsidianGenerator;
import com.denfop.tiles.base.TilePainting;
import com.denfop.tiles.mechanism.TilePlasticCreator;
import com.denfop.tiles.mechanism.TilePlasticPlateCreator;
import com.denfop.tiles.mechanism.exp.TileStorageExp;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileDieselGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileHydrogenGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TilePetrolGenerator;
import com.denfop.tiles.mechanism.generator.things.fluid.TileHeliumGenerator;
import com.denfop.tiles.mechanism.generator.things.fluid.TileLavaGenerator;
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

public enum BlockBaseMachine2 implements IMultiTileBlock, IMultiBlockItem {

    combiner_matter(TileCombinerMatter.class, 0),
    fisher(TileFisher.class, 1),
    analyzer(TileAnalyzer.class, 2),
    painter(TilePainting.class, 3),
    gen_disel(TileDieselGenerator.class, 4),
    gen_pet(TilePetrolGenerator.class, 5),
    adv_pump(TileAdvPump.class, 6),
    imp_pump(TileImpPump.class, 7),
    expierence_block(TileStorageExp.class, 8),
    gen_hyd(TileHydrogenGenerator.class, 9),
    gen_obsidian(TileObsidianGenerator.class, 10),
    plastic_creator(TilePlasticCreator.class, 11),
    lava_gen(TileLavaGenerator.class, 12),
    plastic_plate_creator(TilePlasticPlateCreator.class, 13),
    helium_generator(TileHeliumGenerator.class, 14),
    electrolyzer_iu(TileElectrolyzer.class, 15),

    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("basemachine2");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockBaseMachine2(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockBaseMachine2(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
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
        for (final BlockBaseMachine2 block : values()) {
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

    @Override
    public boolean hasUniqueRender(final ItemStack itemStack) {
        return false;
    }

    @Override
    public ModelResourceLocation getModelLocation(final ItemStack itemStack) {
        return null;
    }
}
