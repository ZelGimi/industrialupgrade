package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.reactors.heat.casing.TileEntityAdvCasing;
import com.denfop.tiles.reactors.heat.casing.TileEntityImpCasing;
import com.denfop.tiles.reactors.heat.casing.TileEntityPerCasing;
import com.denfop.tiles.reactors.heat.casing.TileEntitySimpleCasing;
import com.denfop.tiles.reactors.heat.chamber.TileEntityAdvChamber;
import com.denfop.tiles.reactors.heat.chamber.TileEntityImpChamber;
import com.denfop.tiles.reactors.heat.chamber.TileEntityPerChamber;
import com.denfop.tiles.reactors.heat.chamber.TileEntitySimpleChamber;
import com.denfop.tiles.reactors.heat.circulationpump.TileEntityAdvCirculationPump;
import com.denfop.tiles.reactors.heat.circulationpump.TileEntityImpCirculationPump;
import com.denfop.tiles.reactors.heat.circulationpump.TileEntityPerCirculationPump;
import com.denfop.tiles.reactors.heat.circulationpump.TileEntitySimpleCirculationPump;
import com.denfop.tiles.reactors.heat.controller.TileEntityAdvController;
import com.denfop.tiles.reactors.heat.controller.TileEntityImpController;
import com.denfop.tiles.reactors.heat.controller.TileEntityPerController;
import com.denfop.tiles.reactors.heat.controller.TileEntitySimpleController;
import com.denfop.tiles.reactors.heat.coolant.TileEntityAdvCoolant;
import com.denfop.tiles.reactors.heat.coolant.TileEntityImpCoolant;
import com.denfop.tiles.reactors.heat.coolant.TileEntityPerCoolant;
import com.denfop.tiles.reactors.heat.coolant.TileEntitySimpleCoolant;
import com.denfop.tiles.reactors.heat.fueltank.TileEntityAdvFuelTank;
import com.denfop.tiles.reactors.heat.fueltank.TileEntityImpFuelTank;
import com.denfop.tiles.reactors.heat.fueltank.TileEntityPerFuelTank;
import com.denfop.tiles.reactors.heat.fueltank.TileEntitySimpleFuelTank;
import com.denfop.tiles.reactors.heat.graphite_controller.TileEntityAdvGraphiteController;
import com.denfop.tiles.reactors.heat.graphite_controller.TileEntityImpGraphiteController;
import com.denfop.tiles.reactors.heat.graphite_controller.TileEntityPerGraphiteController;
import com.denfop.tiles.reactors.heat.graphite_controller.TileEntitySimpleGraphiteController;
import com.denfop.tiles.reactors.heat.pump.TileEntityAdvPump;
import com.denfop.tiles.reactors.heat.pump.TileEntityImpPump;
import com.denfop.tiles.reactors.heat.pump.TileEntityPerPump;
import com.denfop.tiles.reactors.heat.pump.TileEntitySimplePump;
import com.denfop.tiles.reactors.heat.reactor.TileEntityAdvReactor;
import com.denfop.tiles.reactors.heat.reactor.TileEntityImpReactor;
import com.denfop.tiles.reactors.heat.reactor.TileEntityPerReactor;
import com.denfop.tiles.reactors.heat.reactor.TileEntitySimpleReactor;
import com.denfop.tiles.reactors.heat.socket.TileEntityAdvSocket;
import com.denfop.tiles.reactors.heat.socket.TileEntityImpSocket;
import com.denfop.tiles.reactors.heat.socket.TileEntityPerSocket;
import com.denfop.tiles.reactors.heat.socket.TileEntitySimpleSocket;
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

public enum BlockHeatReactor implements IMultiTileBlock {


    heat_pump(TileEntitySimplePump.class, 0),
    heat_adv_pump(TileEntityAdvPump.class, 1),
    heat_imp_pump(TileEntityImpPump.class, 2),
    heat_per_pump(TileEntityPerPump.class, 3),
    heat_controller(TileEntitySimpleController.class, 4),
    heat_adv_controller(TileEntityAdvController.class, 5),
    heat_imp_controller(TileEntityImpController.class, 6),
    heat_per_controller(TileEntityPerController.class, 7),
    heat_chamber(TileEntitySimpleChamber.class, 8),
    heat_adv_chamber(TileEntityAdvChamber.class, 9),
    heat_imp_chamber(TileEntityImpChamber.class, 10),
    heat_per_chamber(TileEntityPerChamber.class, 11),
    heat_reactor(TileEntitySimpleReactor.class, 12),
    heat_adv_reactor(TileEntityAdvReactor.class, 13),
    heat_imp_reactor(TileEntityImpReactor.class, 14),
    heat_per_reactor(TileEntityPerReactor.class, 15),

    heat_coolant(TileEntitySimpleCoolant.class, 16),
    heat_adv_coolant(TileEntityAdvCoolant.class, 17),
    heat_imp_coolant(TileEntityImpCoolant.class, 18),
    heat_per_coolant(TileEntityPerCoolant.class, 19),
    heat_graphite_controller(TileEntitySimpleGraphiteController.class, 20),
    heat_adv_graphite_controller(TileEntityAdvGraphiteController.class, 21),
    heat_imp_graphite_controller(TileEntityImpGraphiteController.class, 22),
    heat_per_graphite_controller(TileEntityPerGraphiteController.class, 23),
    heat_casing(TileEntitySimpleCasing.class, 24),
    heat_adv_casing(TileEntityAdvCasing.class, 25),
    heat_imp_casing(TileEntityImpCasing.class, 26),
    heat_per_casing(TileEntityPerCasing.class, 27),
    heat_socket(TileEntitySimpleSocket.class, 28),
    heat_adv_socket(TileEntityAdvSocket.class, 29),
    heat_imp_socket(TileEntityImpSocket.class, 30),
    heat_per_socket(TileEntityPerSocket.class, 31),
    heat_fueltank(TileEntitySimpleFuelTank.class, 32),
    heat_adv_fueltank(TileEntityAdvFuelTank.class, 33),
    heat_imp_fueltank(TileEntityImpFuelTank.class, 34),
    heat_per_fueltank(TileEntityPerFuelTank.class, 35),
    heat_circulationpump(TileEntitySimpleCirculationPump.class, 36),
    heat_adv_circulationpump(TileEntityAdvCirculationPump.class, 37),
    heat_imp_circulationpump(TileEntityImpCirculationPump.class, 38),
    heat_per_circulationpump(TileEntityPerCirculationPump.class, 39),
    ;


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TileEntityBlock>> blockType;

    ;

    BlockHeatReactor(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    ;

    BlockHeatReactor(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final Rarity rarity) {
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
        return "heat_reactors";
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
