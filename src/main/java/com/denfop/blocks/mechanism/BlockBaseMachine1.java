package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.tiles.mechanism.TileEntityHandlerHeavyOre;
import com.denfop.tiles.mechanism.TileEntityMagnet;
import com.denfop.tiles.mechanism.TileEntityMagnetGenerator;
import com.denfop.tiles.mechanism.TileEntityWitherMaker;
import com.denfop.tiles.mechanism.dual.TileEntityEnrichment;
import com.denfop.tiles.mechanism.dual.TileEntitySynthesis;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityAdvGeoGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityImpGeoGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityPerGeoGenerator;
import com.denfop.tiles.mechanism.triple.heat.TileEntityAdvAlloySmelter;
import com.denfop.tiles.reactors.TileEntityAdvNuclearReactorElectric;
import com.denfop.tiles.reactors.TileEntityImpNuclearReactor;
import com.denfop.tiles.reactors.TileEntityPerNuclearReactor;
import ic2.api.item.ITeBlockSpecialItem;
import ic2.core.block.ITeBlock;
import ic2.core.block.TileEntityBlock;
import ic2.core.ref.TeBlock;
import ic2.core.util.Util;
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

public enum BlockBaseMachine1 implements ITeBlock, ITeBlockSpecialItem {

    adv_alloy_smelter(TileEntityAdvAlloySmelter.class, 3),
    adv_geo(TileEntityAdvGeoGenerator.class, 4),
    imp_geo(TileEntityImpGeoGenerator.class, 5),
    per_geo(TileEntityPerGeoGenerator.class, 6),
    adv_reactor(TileEntityAdvNuclearReactorElectric.class, 7),
    imp_reactor(TileEntityImpNuclearReactor.class, 8),
    per_reactor(TileEntityPerNuclearReactor.class, 9),
    enrichment(TileEntityEnrichment.class, 10),
    synthesis(TileEntitySynthesis.class, 11),
    handler_ho(TileEntityHandlerHeavyOre.class, 12),
    gen_wither(TileEntityWitherMaker.class, 13),
    magnet(TileEntityMagnet.class, 14),
    magnet_generator(TileEntityMagnetGenerator.class, 15),

    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("basemachine1");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockBaseMachine1(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockBaseMachine1(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
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
        for (final BlockBaseMachine1 block : values()) {
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
