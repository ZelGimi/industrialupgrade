package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.tiles.base.*;
import com.denfop.tiles.mechanism.TileEntityPlasticCreator;
import com.denfop.tiles.mechanism.TileEntityPlasticPlateCreator;
import com.denfop.tiles.mechanism.exp.TileEntityStorageExp;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityDieselGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityHydrogenGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityPetrolGenerator;
import com.denfop.tiles.mechanism.generator.things.fluid.TileEntityHeliumGenerator;
import com.denfop.tiles.mechanism.generator.things.fluid.TileEntityLavaGenerator;
import ic2.api.item.ITeBlockSpecialItem;
import ic2.core.block.ITeBlock;
import ic2.core.block.TileEntityBlock;
import ic2.core.ref.IC2Material;
import ic2.core.ref.TeBlock;
import ic2.core.util.Util;
import net.minecraft.block.material.Material;
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

public enum BlockBaseMachine2 implements ITeBlock, ITeBlockSpecialItem {

    combiner_matter(TileEntityCombinerMatter.class, 0),
    fisher(TileEntityFisher.class, 1),
    analyzer(TileEntityAnalyzer.class, 2),
    painter(TileEntityPainting.class, 3),
    gen_disel(TileEntityDieselGenerator.class, 4),
    gen_pet(TileEntityPetrolGenerator.class, 5),
    adv_pump(TileEntityAdvPump.class, 6),
    imp_pump(TileEntityImpPump.class, 7),
    expierence_block(TileEntityStorageExp.class, 8),
    gen_hyd(TileEntityHydrogenGenerator.class, 9),
    gen_obsidian(TileEntityObsidianGenerator.class, 10),
    plastic_creator(TileEntityPlasticCreator.class, 11),
    lava_gen(TileEntityLavaGenerator.class, 12),
    plastic_plate_creator(TileEntityPlasticPlateCreator.class, 13),
    helium_generator(TileEntityHeliumGenerator.class, 14),
    electrolyzer_iu(TileEntityElectrolyzer.class, 15),

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

    public static void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockBaseMachine2 block : values()) {
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
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }

    @Override
    public boolean doesOverrideDefault(final ItemStack itemStack) {
        return false;
    }

    @Override
    public ModelResourceLocation getModelLocation(final ItemStack itemStack) {
        return null;
    }
}
