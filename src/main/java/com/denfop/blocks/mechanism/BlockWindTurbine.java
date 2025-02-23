package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.cyclotron.TileEntityCyclotronCasing;
import com.denfop.tiles.cyclotron.TileEntityCyclotronChamber;
import com.denfop.tiles.cyclotron.TileEntityCyclotronController;
import com.denfop.tiles.cyclotron.TileEntityCyclotronCoolant;
import com.denfop.tiles.cyclotron.TileEntityCyclotronCryogen;
import com.denfop.tiles.cyclotron.TileEntityCyclotronElectrostaticDeflector;
import com.denfop.tiles.cyclotron.TileEntityCyclotronParticleAccelerator;
import com.denfop.tiles.cyclotron.TileEntityCyclotronPositrons;
import com.denfop.tiles.cyclotron.TileEntityCyclotronQuantum;
import com.denfop.tiles.windturbine.TileEntityWindTurbineCasing;
import com.denfop.tiles.windturbine.TileEntityWindTurbineCasing1;
import com.denfop.tiles.windturbine.TileEntityWindTurbineCasing2;
import com.denfop.tiles.windturbine.TileEntityWindTurbineController;
import com.denfop.tiles.windturbine.TileEntityWindTurbineSocket;
import com.denfop.tiles.windturbine.TileEntityWindTurbineStabilizer;
import com.denfop.utils.ModUtils;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockWindTurbine implements IMultiTileBlock {

    wind_turbine_controller(TileEntityWindTurbineController.class, 0),
    wind_turbine_socket(TileEntityWindTurbineSocket.class, 1),
    wind_turbine_stabilizer(TileEntityWindTurbineStabilizer.class, 2),
    wind_turbine_casing_1(TileEntityWindTurbineCasing.class, 3),
    wind_turbine_casing_2(TileEntityWindTurbineCasing1.class, 4),
    wind_turbine_casing_3(TileEntityWindTurbineCasing2.class, 5)
    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("wind_turbine");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockWindTurbine(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }
    int idBlock;
    public  int getIDBlock(){
        return idBlock;
    };

    public void setIdBlock(int id){
        idBlock = id;
    };
    BlockWindTurbine(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }

    @Override
    public Material getMaterial() {
        return Material.IRON;
    }

    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockWindTurbine block : values()) {
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
        return MultiTileBlock.DefaultDrop.Self;
    }

    @Override
    public boolean allowWrenchRotating() {
        return false;
    }

    @Override
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }
}
