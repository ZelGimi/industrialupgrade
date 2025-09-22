package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.chemicalplant.TileEntityChemicalPlantCasing;
import com.denfop.tiles.chemicalplant.TileEntityChemicalPlantController;
import com.denfop.tiles.chemicalplant.TileEntityChemicalPlantExchanger;
import com.denfop.tiles.chemicalplant.TileEntityChemicalPlantGenerator;
import com.denfop.tiles.chemicalplant.TileEntityChemicalPlantSeparate;
import com.denfop.tiles.chemicalplant.TileEntityChemicalPlantTransport;
import com.denfop.tiles.chemicalplant.TileEntityChemicalPlantWaste;
import com.denfop.utils.ModUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockChemicalPlant implements IMultiTileBlock {

    chemical_plant_controller(TileEntityChemicalPlantController.class, 0),
    chemical_plant_exchanger(TileEntityChemicalPlantExchanger.class, 1),
    chemical_plant_casing(TileEntityChemicalPlantCasing.class, 2),
    chemical_plant_generator(TileEntityChemicalPlantGenerator.class, 3),
    chemical_plant_transport(TileEntityChemicalPlantTransport.class, 4),
    chemical_plant_waste(TileEntityChemicalPlantWaste.class, 5),
    chemical_plant_separate(TileEntityChemicalPlantSeparate.class, 6),
    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("chemical_plant");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    int idBlock;
    private TileEntityBlock dummyTe;

    BlockChemicalPlant(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }

    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockChemicalPlant block : values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception ignored) {

                }
            }
        }
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
