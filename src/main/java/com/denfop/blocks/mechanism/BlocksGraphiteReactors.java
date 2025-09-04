package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.reactors.graphite.capacitor.BlockEntityAdvCapacitor;
import com.denfop.blockentity.reactors.graphite.capacitor.BlockEntityImpCapacitor;
import com.denfop.blockentity.reactors.graphite.capacitor.BlockEntityPerCapacitor;
import com.denfop.blockentity.reactors.graphite.capacitor.BlockEntitySimpleCapacitor;
import com.denfop.blockentity.reactors.graphite.casing.BlockEntityAdvCasing;
import com.denfop.blockentity.reactors.graphite.casing.BlockEntityImpCasing;
import com.denfop.blockentity.reactors.graphite.casing.BlockEntityPerCasing;
import com.denfop.blockentity.reactors.graphite.casing.BlockEntitySimpleCasing;
import com.denfop.blockentity.reactors.graphite.chamber.BlockEntityAdvChamber;
import com.denfop.blockentity.reactors.graphite.chamber.BlockEntityImpChamber;
import com.denfop.blockentity.reactors.graphite.chamber.BlockEntityPerChamber;
import com.denfop.blockentity.reactors.graphite.chamber.BlockEntitySimpleChamber;
import com.denfop.blockentity.reactors.graphite.controller.BlockEntityAdvController;
import com.denfop.blockentity.reactors.graphite.controller.BlockEntityImpController;
import com.denfop.blockentity.reactors.graphite.controller.BlockEntityPerController;
import com.denfop.blockentity.reactors.graphite.controller.BlockEntitySimpleController;
import com.denfop.blockentity.reactors.graphite.cooling.BlockEntityAdvCooling;
import com.denfop.blockentity.reactors.graphite.cooling.BlockEntityImpCooling;
import com.denfop.blockentity.reactors.graphite.cooling.BlockEntityPerCooling;
import com.denfop.blockentity.reactors.graphite.cooling.BlockEntitySimpleCooling;
import com.denfop.blockentity.reactors.graphite.exchanger.BlockEntityAdvExchanger;
import com.denfop.blockentity.reactors.graphite.exchanger.BlockEntityImpExchanger;
import com.denfop.blockentity.reactors.graphite.exchanger.BlockEntityPerExchanger;
import com.denfop.blockentity.reactors.graphite.exchanger.BlockEntitySimpleExchanger;
import com.denfop.blockentity.reactors.graphite.graphite_controller.BlockEntityAdvGraphiteController;
import com.denfop.blockentity.reactors.graphite.graphite_controller.BlockEntityImpGraphiteController;
import com.denfop.blockentity.reactors.graphite.graphite_controller.BlockEntityPerGraphiteController;
import com.denfop.blockentity.reactors.graphite.graphite_controller.BlockEntitySimpleGraphiteController;
import com.denfop.blockentity.reactors.graphite.reactor.BlockEntityAdvReactor;
import com.denfop.blockentity.reactors.graphite.reactor.BlockEntityImpReactor;
import com.denfop.blockentity.reactors.graphite.reactor.BlockEntityPerReactor;
import com.denfop.blockentity.reactors.graphite.reactor.BlockEntitySimpleReactor;
import com.denfop.blockentity.reactors.graphite.socket.BlockEntityAdvSocket;
import com.denfop.blockentity.reactors.graphite.socket.BlockEntityImpSocket;
import com.denfop.blockentity.reactors.graphite.socket.BlockEntityPerSocket;
import com.denfop.blockentity.reactors.graphite.socket.BlockEntitySimpleSocket;
import com.denfop.blockentity.reactors.graphite.tank.BlockEntityAdvTank;
import com.denfop.blockentity.reactors.graphite.tank.BlockEntityImpTank;
import com.denfop.blockentity.reactors.graphite.tank.BlockEntityPerTank;
import com.denfop.blockentity.reactors.graphite.tank.BlockEntitySimpleTank;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlocksGraphiteReactors implements MultiBlockEntity {


    graphite_controller(BlockEntitySimpleController.class, 0),
    graphite_adv_controller(BlockEntityAdvController.class, 1),
    graphite_imp_controller(BlockEntityImpController.class, 2),
    graphite_per_controller(BlockEntityPerController.class, 3),
    graphite_graphite_controller(BlockEntitySimpleGraphiteController.class, 4),
    graphite_adv_graphite_controller(BlockEntityAdvGraphiteController.class, 5),
    graphite_imp_graphite_controller(BlockEntityImpGraphiteController.class, 6),
    graphite_per_graphite_controller(BlockEntityPerGraphiteController.class, 7),
    graphite_exchanger(BlockEntitySimpleExchanger.class, 8),
    graphite_adv_exchanger(BlockEntityAdvExchanger.class, 9),
    graphite_imp_exchanger(BlockEntityImpExchanger.class, 10),
    graphite_per_exchanger(BlockEntityPerExchanger.class, 11),
    graphite_chamber(BlockEntitySimpleChamber.class, 12),
    graphite_adv_chamber(BlockEntityAdvChamber.class, 13),
    graphite_imp_chamber(BlockEntityImpChamber.class, 14),
    graphite_per_chamber(BlockEntityPerChamber.class, 15),
    graphite_tank(BlockEntitySimpleTank.class, 16),
    graphite_adv_tank(BlockEntityAdvTank.class, 17),
    graphite_imp_tank(BlockEntityImpTank.class, 18),
    graphite_per_tank(BlockEntityPerTank.class, 19),
    graphite_reactor(BlockEntitySimpleReactor.class, 20),
    graphite_adv_reactor(BlockEntityAdvReactor.class, 21),
    graphite_imp_reactor(BlockEntityImpReactor.class, 22),
    graphite_per_reactor(BlockEntityPerReactor.class, 23),
    graphite_casing(BlockEntitySimpleCasing.class, 24),
    graphite_adv_casing(BlockEntityAdvCasing.class, 25),
    graphite_imp_casing(BlockEntityImpCasing.class, 26),
    graphite_per_casing(BlockEntityPerCasing.class, 27),
    graphite_capacitor(BlockEntitySimpleCapacitor.class, 28),
    graphite_adv_capacitor(BlockEntityAdvCapacitor.class, 29),
    graphite_imp_capacitor(BlockEntityImpCapacitor.class, 30),
    graphite_per_capacitor(BlockEntityPerCapacitor.class, 31),
    graphite_socket(BlockEntitySimpleSocket.class, 32),
    graphite_adv_socket(BlockEntityAdvSocket.class, 33),
    graphite_imp_socket(BlockEntityImpSocket.class, 34),
    graphite_per_socket(BlockEntityPerSocket.class, 35),
    graphite_cooling(BlockEntitySimpleCooling.class, 36),
    graphite_adv_cooling(BlockEntityAdvCooling.class, 37),
    graphite_imp_cooling(BlockEntityImpCooling.class, 38),
    graphite_per_cooling(BlockEntityPerCooling.class, 39),
    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlocksGraphiteReactors(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    ;

    BlocksGraphiteReactors(final Class<? extends BlockEntityBase> teClass, final int itemMeta, final Rarity rarity) {
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
        final ModContainer mc = ModLoadingContext.get().getActiveContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        if (this.getTeClass() != null) {
            try {
                this.dummyTe = (BlockEntityBase) this.teClass.getConstructors()[0].newInstance(BlockPos.ZERO, defaultState);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void setDefaultState(BlockState blockState) {
        this.defaultState = blockState;
    }

    @Override
    public void setType(RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockEntityType) {
        this.blockType = blockEntityType;
    }

    @Override
    public BlockEntityType<? extends BlockEntityBase> getBlockType() {
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
    public Class<? extends BlockEntityBase> getTeClass() {
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
    public BlockEntityBase getDummyTe() {
        return this.dummyTe;
    }

    @Override
    public String[] getMultiModels(final MultiBlockEntity teBlock) {
        return MultiBlockEntity.super.getMultiModels(teBlock);
    }
}
