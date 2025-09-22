package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.reactors.graphite.capacitor.TileEntityAdvCapacitor;
import com.denfop.tiles.reactors.graphite.capacitor.TileEntityImpCapacitor;
import com.denfop.tiles.reactors.graphite.capacitor.TileEntityPerCapacitor;
import com.denfop.tiles.reactors.graphite.capacitor.TileEntitySimpleCapacitor;
import com.denfop.tiles.reactors.graphite.casing.TileEntityAdvCasing;
import com.denfop.tiles.reactors.graphite.casing.TileEntityImpCasing;
import com.denfop.tiles.reactors.graphite.casing.TileEntityPerCasing;
import com.denfop.tiles.reactors.graphite.casing.TileEntitySimpleCasing;
import com.denfop.tiles.reactors.graphite.chamber.TileEntityAdvChamber;
import com.denfop.tiles.reactors.graphite.chamber.TileEntityImpChamber;
import com.denfop.tiles.reactors.graphite.chamber.TileEntityPerChamber;
import com.denfop.tiles.reactors.graphite.chamber.TileEntitySimpleChamber;
import com.denfop.tiles.reactors.graphite.controller.TileEntityAdvController;
import com.denfop.tiles.reactors.graphite.controller.TileEntityImpController;
import com.denfop.tiles.reactors.graphite.controller.TileEntityPerController;
import com.denfop.tiles.reactors.graphite.controller.TileEntitySimpleController;
import com.denfop.tiles.reactors.graphite.cooling.TileEntityAdvCooling;
import com.denfop.tiles.reactors.graphite.cooling.TileEntityImpCooling;
import com.denfop.tiles.reactors.graphite.cooling.TileEntityPerCooling;
import com.denfop.tiles.reactors.graphite.cooling.TileEntitySimpleCooling;
import com.denfop.tiles.reactors.graphite.exchanger.TileEntityAdvExchanger;
import com.denfop.tiles.reactors.graphite.exchanger.TileEntityImpExchanger;
import com.denfop.tiles.reactors.graphite.exchanger.TileEntityPerExchanger;
import com.denfop.tiles.reactors.graphite.exchanger.TileEntitySimpleExchanger;
import com.denfop.tiles.reactors.graphite.graphite_controller.TileEntityAdvGraphiteController;
import com.denfop.tiles.reactors.graphite.graphite_controller.TileEntityImpGraphiteController;
import com.denfop.tiles.reactors.graphite.graphite_controller.TileEntityPerGraphiteController;
import com.denfop.tiles.reactors.graphite.graphite_controller.TileEntitySimpleGraphiteController;
import com.denfop.tiles.reactors.graphite.reactor.TileEntityAdvReactor;
import com.denfop.tiles.reactors.graphite.reactor.TileEntityImpReactor;
import com.denfop.tiles.reactors.graphite.reactor.TileEntityPerReactor;
import com.denfop.tiles.reactors.graphite.reactor.TileEntitySimpleReactor;
import com.denfop.tiles.reactors.graphite.socket.TileEntityAdvSocket;
import com.denfop.tiles.reactors.graphite.socket.TileEntityImpSocket;
import com.denfop.tiles.reactors.graphite.socket.TileEntityPerSocket;
import com.denfop.tiles.reactors.graphite.socket.TileEntitySimpleSocket;
import com.denfop.tiles.reactors.graphite.tank.TileEntityAdvTank;
import com.denfop.tiles.reactors.graphite.tank.TileEntityImpTank;
import com.denfop.tiles.reactors.graphite.tank.TileEntityPerTank;
import com.denfop.tiles.reactors.graphite.tank.TileEntitySimpleTank;
import com.denfop.utils.ModUtils;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlocksGraphiteReactors implements IMultiTileBlock {

    graphite_controller(TileEntitySimpleController.class, 0),
    graphite_adv_controller(TileEntityAdvController.class, 1),
    graphite_imp_controller(TileEntityImpController.class, 2),
    graphite_per_controller(TileEntityPerController.class, 3),
    graphite_graphite_controller(TileEntitySimpleGraphiteController.class, 4),
    graphite_adv_graphite_controller(TileEntityAdvGraphiteController.class, 5),
    graphite_imp_graphite_controller(TileEntityImpGraphiteController.class, 6),
    graphite_per_graphite_controller(TileEntityPerGraphiteController.class, 7),
    graphite_exchanger(TileEntitySimpleExchanger.class, 8),
    graphite_adv_exchanger(TileEntityAdvExchanger.class, 9),
    graphite_imp_exchanger(TileEntityImpExchanger.class, 10),
    graphite_per_exchanger(TileEntityPerExchanger.class, 11),
    graphite_chamber(TileEntitySimpleChamber.class, 12),
    graphite_adv_chamber(TileEntityAdvChamber.class, 13),
    graphite_imp_chamber(TileEntityImpChamber.class, 14),
    graphite_per_chamber(TileEntityPerChamber.class, 15),
    graphite_tank(TileEntitySimpleTank.class, 16),
    graphite_adv_tank(TileEntityAdvTank.class, 17),
    graphite_imp_tank(TileEntityImpTank.class, 18),
    graphite_per_tank(TileEntityPerTank.class, 19),
    graphite_reactor(TileEntitySimpleReactor.class, 20),
    graphite_adv_reactor(TileEntityAdvReactor.class, 21),
    graphite_imp_reactor(TileEntityImpReactor.class, 22),
    graphite_per_reactor(TileEntityPerReactor.class, 23),
    graphite_casing(TileEntitySimpleCasing.class, 24),
    graphite_adv_casing(TileEntityAdvCasing.class, 25),
    graphite_imp_casing(TileEntityImpCasing.class, 26),
    graphite_per_casing(TileEntityPerCasing.class, 27),
    graphite_capacitor(TileEntitySimpleCapacitor.class, 28),
    graphite_adv_capacitor(TileEntityAdvCapacitor.class, 29),
    graphite_imp_capacitor(TileEntityImpCapacitor.class, 30),
    graphite_per_capacitor(TileEntityPerCapacitor.class, 31),
    graphite_socket(TileEntitySimpleSocket.class, 32),
    graphite_adv_socket(TileEntityAdvSocket.class, 33),
    graphite_imp_socket(TileEntityImpSocket.class, 34),
    graphite_per_socket(TileEntityPerSocket.class, 35),
    graphite_cooling(TileEntitySimpleCooling.class, 36),
    graphite_adv_cooling(TileEntityAdvCooling.class, 37),
    graphite_imp_cooling(TileEntityImpCooling.class, 38),
    graphite_per_cooling(TileEntityPerCooling.class, 39),
    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("graphite_reactor");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    int idBlock;
    private TileEntityBlock dummyTe;

    BlocksGraphiteReactors(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
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

    @Override
    public CreativeTabs getCreativeTab() {
        return IUCore.ReactorsBlockTab;
    }

    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlocksGraphiteReactors block : values()) {
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
