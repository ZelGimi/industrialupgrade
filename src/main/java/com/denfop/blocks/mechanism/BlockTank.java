package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.blocks.IIdProvider;
import com.denfop.tiles.tank.TileEntityAdvTank;
import com.denfop.tiles.tank.TileEntityImpTank;
import com.denfop.tiles.tank.TileEntityPerTank;
import com.denfop.tiles.tank.TileEntityTank;
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
import java.util.Locale;
import java.util.Set;

public enum BlockTank implements ITeBlock, ITeBlockSpecialItem {

    tank_iu(TileEntityTank.class, 0),
    adv_tank(TileEntityAdvTank.class, 1),
    imp_tank(TileEntityImpTank.class, 2),
    per_tank(TileEntityPerTank.class, 3),
    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("tank_iu");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockTank(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockTank(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
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
        for (final BlockTank block : values()) {
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

    @Override
    public boolean doesOverrideDefault(final ItemStack itemStack) {
        return true;
    }

    @Override
    public ModelResourceLocation getModelLocation(final ItemStack itemStack) {
        return new ModelResourceLocation(Constants.MOD_ID + ":" + Types.getFromID(itemStack.getItemDamage()).getName(), null);
    }
}

enum Types implements IIdProvider {
    fluid_tank_normal(0),
    fluid_tank_normal_adv(1),
    fluid_tank_normal_imp(2),
    fluid_tank_normal_per(3),
    ;

    private final String name;
    private final int ID;

    Types(final int ID) {
        this.name = this.name().toLowerCase(Locale.US);
        this.ID = ID;
    }

    public static Types getFromID(final int ID) {
        return values()[ID % values().length];
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.ID;
    }
}
