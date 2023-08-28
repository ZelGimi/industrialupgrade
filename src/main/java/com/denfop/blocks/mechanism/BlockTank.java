package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.item.IMultiBlockItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.ISubEnum;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.tank.TileEntityAdvTank;
import com.denfop.tiles.tank.TileEntityImpTank;
import com.denfop.tiles.tank.TileEntityPerTank;
import com.denfop.tiles.tank.TileEntityTank;
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
import java.util.Locale;
import java.util.Set;

public enum BlockTank implements IMultiTileBlock, IMultiBlockItem {

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

    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockTank block : values()) {
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
        return false;
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
        return MultiTileBlock.DefaultDrop.Self;
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
        return true;
    }

    @Override
    public ModelResourceLocation getModelLocation(final ItemStack itemStack) {
        return new ModelResourceLocation(Constants.MOD_ID + ":" + Types.getFromID(itemStack.getItemDamage()).getName(), null);
    }
}

enum Types implements ISubEnum {
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
