package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.geothermalpump.TileEntityGeothermalCasing;
import com.denfop.tiles.geothermalpump.TileEntityGeothermalController;
import com.denfop.tiles.geothermalpump.TileEntityGeothermalExchanger;
import com.denfop.tiles.geothermalpump.TileEntityGeothermalGenerator;
import com.denfop.tiles.geothermalpump.TileEntityGeothermalRigDrill;
import com.denfop.tiles.geothermalpump.TileEntityGeothermalTransport;
import com.denfop.tiles.geothermalpump.TileEntityGeothermalWaste;
import com.denfop.utils.ModUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockGeothermalPump implements IMultiTileBlock {

    geothermal_controller(TileEntityGeothermalController.class, 0),
    geothermal_exchanger(TileEntityGeothermalExchanger.class, 1),
    geothermal_casing(TileEntityGeothermalCasing.class, 2),
    geothermal_rig(TileEntityGeothermalRigDrill.class, 3),
    geothermal_generator(TileEntityGeothermalGenerator.class, 4),
    geothermal_transport(TileEntityGeothermalTransport.class, 5),
    geothermal_waste(TileEntityGeothermalWaste.class, 6);


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("geothermalpump");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    int idBlock;
    private TileEntityBlock dummyTe;

    BlockGeothermalPump(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
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
        for (final BlockGeothermalPump block : values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception ignored) {

                }
            }
        }
    }

    @Override
    public Material getMaterial() {
        return IMultiTileBlock.MACHINE;
    }

    public float getHardness() {
        return 0.5F;
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
        return ModUtils.horizontalFacings;
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
