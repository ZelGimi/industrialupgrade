package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.item.IItemIgnoringNull;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.transport.tiles.TileEntityCable;
import com.denfop.tiles.transport.types.CableType;
import com.denfop.utils.ModUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public enum BlockCable implements IMultiTileBlock, IItemIgnoringNull {

    cable_iu(TileEntityCable.class, -1),
    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("cable_iu");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private TileEntityBlock dummyTe;


    BlockCable(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }

    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockCable block : values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception e) {

                }
            }
        }
    }

    public float getHardness() {
        return 0.5F;
    }

    @Override
    public Material getMaterial() {
        return IMultiTileBlock.CABLE;
    }

    @Override
    public String[] getMultiModels() {
        List<String> stringList = new ArrayList<>();
        Arrays.stream(CableType.values).forEach(value -> stringList.add(value.name()));
        return stringList.toArray(new String[0]);
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
        return BlockCable.IDENTITY;
    }

    @Override
    public boolean hasItem() {
        return this.teClass != null && this.itemMeta != -1;
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
        return ModUtils.noFacings;
    }

    @Override
    @Nonnull
    public MultiTileBlock.HarvestTool getHarvestTool() {
        return MultiTileBlock.HarvestTool.Pickaxe;
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
}
