package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.item.IMultiBlockItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.steamturbine.TileEntitySteamTurbineCasing;
import com.denfop.tiles.mechanism.steamturbine.TileEntitySteamTurbineControllerRod;
import com.denfop.tiles.mechanism.steamturbine.TileEntitySteamTurbineGlass;
import com.denfop.tiles.mechanism.steamturbine.TileEntitySteamTurbineRod;
import com.denfop.tiles.mechanism.steamturbine.TileEntitySteamTurbineSocket;
import com.denfop.tiles.mechanism.steamturbine.controller.TileEntityAdvSteamTurbineController;
import com.denfop.tiles.mechanism.steamturbine.controller.TileEntityImpSteamTurbineController;
import com.denfop.tiles.mechanism.steamturbine.controller.TileEntityPerSteamTurbineController;
import com.denfop.tiles.mechanism.steamturbine.controller.TileEntitySimpleSteamTurbineController;
import com.denfop.tiles.mechanism.steamturbine.coolant.TileEntityAdvSteamTurbineCoolant;
import com.denfop.tiles.mechanism.steamturbine.coolant.TileEntityImpSteamTurbineCoolant;
import com.denfop.tiles.mechanism.steamturbine.coolant.TileEntityPerSteamTurbineCoolant;
import com.denfop.tiles.mechanism.steamturbine.coolant.TileEntitySimpleSteamTurbineCoolant;
import com.denfop.tiles.mechanism.steamturbine.exchanger.TileEntityAdvSteamTurbineExchanger;
import com.denfop.tiles.mechanism.steamturbine.exchanger.TileEntityImpSteamTurbineExchanger;
import com.denfop.tiles.mechanism.steamturbine.exchanger.TileEntityPerSteamTurbineExchanger;
import com.denfop.tiles.mechanism.steamturbine.exchanger.TileEntitySimpleSteamTurbineExchanger;
import com.denfop.tiles.mechanism.steamturbine.pressure.TileEntityAdvSteamTurbinePressure;
import com.denfop.tiles.mechanism.steamturbine.pressure.TileEntityImpSteamTurbinePressure;
import com.denfop.tiles.mechanism.steamturbine.pressure.TileEntityPerSteamTurbinePressure;
import com.denfop.tiles.mechanism.steamturbine.pressure.TileEntitySimpleSteamTurbinePressure;
import com.denfop.tiles.mechanism.steamturbine.tank.TileEntityAdvSteamTurbineTank;
import com.denfop.tiles.mechanism.steamturbine.tank.TileEntityImpSteamTurbineTank;
import com.denfop.tiles.mechanism.steamturbine.tank.TileEntityPerSteamTurbineTank;
import com.denfop.tiles.mechanism.steamturbine.tank.TileEntitySimpleSteamTurbineTank;
import com.denfop.tiles.reactors.water.inputfluid.TileEntityAdvInputPort;
import com.denfop.tiles.reactors.water.inputfluid.TileEntityImpInputPort;
import com.denfop.tiles.reactors.water.inputfluid.TileEntityPerInputPort;
import com.denfop.tiles.reactors.water.inputfluid.TileEntitySimpleInputPort;
import com.denfop.tiles.reactors.water.levelfuel.TileEntityAdvLevelFuel;
import com.denfop.tiles.reactors.water.levelfuel.TileEntityImpLevelFuel;
import com.denfop.tiles.reactors.water.levelfuel.TileEntityPerLevelFuel;
import com.denfop.tiles.reactors.water.levelfuel.TileEntitySimpleLevelFuel;
import com.denfop.tiles.reactors.water.outputfluid.TileEntityAdvOutputPort;
import com.denfop.tiles.reactors.water.outputfluid.TileEntityImpOutputPort;
import com.denfop.tiles.reactors.water.outputfluid.TileEntityPerOutputPort;
import com.denfop.tiles.reactors.water.outputfluid.TileEntitySimpleOutputPort;
import com.denfop.tiles.reactors.water.reactor.TileEntityAdvReactor;
import com.denfop.tiles.reactors.water.reactor.TileEntityImpReactor;
import com.denfop.tiles.reactors.water.reactor.TileEntityPerReactor;
import com.denfop.tiles.reactors.water.reactor.TileEntitySimpleReactor;
import com.denfop.tiles.reactors.water.security.TileEntityAdvSecurity;
import com.denfop.tiles.reactors.water.security.TileEntityImpSecurity;
import com.denfop.tiles.reactors.water.security.TileEntityPerSecurity;
import com.denfop.tiles.reactors.water.security.TileEntitySimpleSecurity;
import com.denfop.tiles.reactors.water.socket.TileEntityAdvSocket;
import com.denfop.tiles.reactors.water.socket.TileEntityImpSocket;
import com.denfop.tiles.reactors.water.socket.TileEntityPerSocket;
import com.denfop.tiles.reactors.water.socket.TileEntitySimpleSocket;
import com.denfop.tiles.reactors.water.tank.TileEntityAdvTank;
import com.denfop.tiles.reactors.water.tank.TileEntityImpTank;
import com.denfop.tiles.reactors.water.tank.TileEntityPerTank;
import com.denfop.tiles.reactors.water.tank.TileEntitySimpleTank;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockSteamTurbine implements IMultiTileBlock, IMultiBlockItem {
    steam_turbine_casing(TileEntitySteamTurbineCasing.class, 0),
    steam_turbine_casing_glass(TileEntitySteamTurbineGlass.class, 1),
    steam_turbine_socket(TileEntitySteamTurbineSocket.class, 2),
    steam_turbine_rod(TileEntitySteamTurbineRod.class, 3),

    steam_turbine_controller_rod(TileEntitySteamTurbineControllerRod.class, 4),

    steam_turbine_controller(TileEntitySimpleSteamTurbineController.class, 5),
    steam_turbine_adv_controller(TileEntityAdvSteamTurbineController.class, 6),
    steam_turbine_imp_controller(TileEntityImpSteamTurbineController.class, 7),
    steam_turbine_per_controller(TileEntityPerSteamTurbineController.class, 8),

    steam_turbine_exchanger(TileEntitySimpleSteamTurbineExchanger.class, 9),
    steam_turbine_adv_exchanger(TileEntityAdvSteamTurbineExchanger.class, 10),
    steam_turbine_imp_exchanger(TileEntityImpSteamTurbineExchanger.class, 11),
    steam_turbine_per_exchanger(TileEntityPerSteamTurbineExchanger.class, 12),

    steam_turbine_tank(TileEntitySimpleSteamTurbineTank.class, 13),
    steam_turbine_adv_tank(TileEntityAdvSteamTurbineTank.class, 14),
    steam_turbine_imp_tank(TileEntityImpSteamTurbineTank.class, 15),
    steam_turbine_per_tank(TileEntityPerSteamTurbineTank.class, 16),

    steam_turbine_coolant(TileEntitySimpleSteamTurbineCoolant.class, 17),
    steam_turbine_adv_coolant(TileEntityAdvSteamTurbineCoolant.class, 18),
    steam_turbine_imp_coolant(TileEntityImpSteamTurbineCoolant.class, 19),
    steam_turbine_per_coolant(TileEntityPerSteamTurbineCoolant.class, 20),

    steam_turbine_pressure(TileEntitySimpleSteamTurbinePressure.class, 21),
    steam_turbine_adv_pressure(TileEntityAdvSteamTurbinePressure.class, 22),
    steam_turbine_imp_pressure(TileEntityImpSteamTurbinePressure.class, 23),
    steam_turbine_per_pressure(TileEntityPerSteamTurbinePressure.class, 24),


    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("steam_turbine");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private TileEntityBlock dummyTe;


    BlockSteamTurbine(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }
    int idBlock;
    public  int getIDBlock(){
        return idBlock;
    };

    public void setIdBlock(int id){
        idBlock = id;
    };
    BlockSteamTurbine(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }

    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockSteamTurbine block : values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception ignored) {

                }
            }
        }

    }


    @Override
    public String[] getMultiModels(final IMultiTileBlock teBlock) {


                    return IMultiTileBlock.super.getMultiModels(teBlock);
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
        return 1.0f;
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
        return false;
    }

    @Override
    public ModelResourceLocation getModelLocation(final ItemStack itemStack) {
        return null;
    }
}
