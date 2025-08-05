package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.util.Set;

;

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


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TileEntityBlock>> blockType;

    ;

    BlocksGraphiteReactors(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    ;

    BlocksGraphiteReactors(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final Rarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;


    }

    @Override
    public CreativeModeTab getCreativeTab() {
        return IUCore.ReactorsBlockTab;
    }

    public int getIDBlock() {
        return idBlock;
    }

    public void setIdBlock(int id) {
        idBlock = id;
    }

    @Override
    public MapColor getMaterial() {
        return MapColor.METAL;
    }

    public void buildDummies() {
        final ModContainer mc = IUCore.instance.modContainer;
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        if (this.getTeClass() != null) {
            try {
                this.dummyTe = (TileEntityBlock) this.teClass.getConstructors()[0].newInstance(BlockPos.ZERO, defaultState);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void setDefaultState(BlockState blockState) {
        this.defaultState = blockState;
    }

    @Override
    public void setType(DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TileEntityBlock>> blockEntityType) {
        this.blockType = blockEntityType;
    }

    @Override
    public BlockEntityType<? extends TileEntityBlock> getBlockType() {
        return this.blockType.get();
    }

    @Override
    public String getMainPath() {
        return "graphite_reactor";
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
    public Set<Direction> getSupportedFacings() {
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 1.0F;
    }

    @Override
    @Nonnull
    public HarvestTool getHarvestTool() {
        return HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public DefaultDrop getDefaultDrop() {
        return DefaultDrop.Self;
    }

    @Override
    public boolean allowWrenchRotating() {
        return false;
    }

    @Override
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }

    @Override
    public String[] getMultiModels(final IMultiTileBlock teBlock) {
        return IMultiTileBlock.super.getMultiModels(teBlock);
    }
}
