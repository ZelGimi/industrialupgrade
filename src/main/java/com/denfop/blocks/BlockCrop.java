package com.denfop.blocks;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.item.IMultiBlockItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.crop.TileEntityCrop;
import com.denfop.utils.ModUtils;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockCrop implements IMultiTileBlock, IMultiBlockItem {

    crop(TileEntityCrop.class, 0),
    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("crop");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    int idBlock;
    private TileEntityBlock dummyTe;

    BlockCrop(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockCrop(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }

    ;

    public int getIDBlock() {
        return idBlock;
    }

    ;

    public void setIdBlock(int id) {
        idBlock = id;
    }

    public void buildDummies() {
        for (final BlockCrop block : values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception ignored) {
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
        return false;
    }

    @Override
    @Nonnull
    public Set<EnumFacing> getSupportedFacings() {
        return ModUtils.noFacings;
    }

    @Override
    public float getHardness() {
        return 0.5f;
    }

    @Override
    @Nonnull
    public MultiTileBlock.HarvestTool getHarvestTool() {
        return MultiTileBlock.HarvestTool.Axe;
    }

    @Override
    @Nonnull
    public MultiTileBlock.DefaultDrop getDefaultDrop() {
        return MultiTileBlock.DefaultDrop.Self;
    }

    @Override
    public boolean allowWrenchRotating() {
        return false;
    }

    @Override
    public Material getMaterial() {
        return Material.PLANTS;
    }

    @Override
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }


    @Override
    public boolean hasUniqueRender(final ItemStack var1) {
        return true;
    }

    @Override
    public ModelResourceLocation getModelLocation(final ItemStack var1) {
        return new ModelResourceLocation(Constants.MOD_ID + ":" + "crop_block", null);
    }
}
