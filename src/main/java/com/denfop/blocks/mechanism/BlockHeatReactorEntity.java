package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.reactors.heat.casing.BlockEntityAdvCasing;
import com.denfop.blockentity.reactors.heat.casing.BlockEntityImpCasing;
import com.denfop.blockentity.reactors.heat.casing.BlockEntityPerCasing;
import com.denfop.blockentity.reactors.heat.casing.BlockEntitySimpleCasing;
import com.denfop.blockentity.reactors.heat.chamber.BlockEntityAdvChamber;
import com.denfop.blockentity.reactors.heat.chamber.BlockEntityImpChamber;
import com.denfop.blockentity.reactors.heat.chamber.BlockEntityPerChamber;
import com.denfop.blockentity.reactors.heat.chamber.BlockEntitySimpleChamber;
import com.denfop.blockentity.reactors.heat.circulationpump.BlockEntityAdvCirculationPump;
import com.denfop.blockentity.reactors.heat.circulationpump.BlockEntityImpCirculationPump;
import com.denfop.blockentity.reactors.heat.circulationpump.BlockEntityPerCirculationPump;
import com.denfop.blockentity.reactors.heat.circulationpump.BlockEntitySimpleCirculationPump;
import com.denfop.blockentity.reactors.heat.controller.BlockEntityAdvController;
import com.denfop.blockentity.reactors.heat.controller.BlockEntityImpController;
import com.denfop.blockentity.reactors.heat.controller.BlockEntityPerController;
import com.denfop.blockentity.reactors.heat.controller.BlockEntitySimpleController;
import com.denfop.blockentity.reactors.heat.coolant.BlockEntityAdvCoolant;
import com.denfop.blockentity.reactors.heat.coolant.BlockEntityImpCoolant;
import com.denfop.blockentity.reactors.heat.coolant.BlockEntityPerCoolant;
import com.denfop.blockentity.reactors.heat.coolant.BlockEntitySimpleCoolant;
import com.denfop.blockentity.reactors.heat.fueltank.BlockEntityAdvFuelTank;
import com.denfop.blockentity.reactors.heat.fueltank.BlockEntityImpFuelTank;
import com.denfop.blockentity.reactors.heat.fueltank.BlockEntityPerFuelTank;
import com.denfop.blockentity.reactors.heat.fueltank.BlockEntitySimpleFuelTank;
import com.denfop.blockentity.reactors.heat.graphite_controller.BlockEntityAdvGraphiteController;
import com.denfop.blockentity.reactors.heat.graphite_controller.BlockEntityImpGraphiteController;
import com.denfop.blockentity.reactors.heat.graphite_controller.BlockEntityPerGraphiteController;
import com.denfop.blockentity.reactors.heat.graphite_controller.BlockEntitySimpleGraphiteController;
import com.denfop.blockentity.reactors.heat.pump.BlockEntityAdvPump;
import com.denfop.blockentity.reactors.heat.pump.BlockEntityImpPump;
import com.denfop.blockentity.reactors.heat.pump.BlockEntityPerPump;
import com.denfop.blockentity.reactors.heat.pump.BlockEntitySimplePump;
import com.denfop.blockentity.reactors.heat.reactor.BlockEntityAdvReactor;
import com.denfop.blockentity.reactors.heat.reactor.BlockEntityImpReactor;
import com.denfop.blockentity.reactors.heat.reactor.BlockEntityPerReactor;
import com.denfop.blockentity.reactors.heat.reactor.BlockEntitySimpleReactor;
import com.denfop.blockentity.reactors.heat.socket.BlockEntityAdvSocket;
import com.denfop.blockentity.reactors.heat.socket.BlockEntityImpSocket;
import com.denfop.blockentity.reactors.heat.socket.BlockEntityPerSocket;
import com.denfop.blockentity.reactors.heat.socket.BlockEntitySimpleSocket;
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
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.util.Set;

;

public enum BlockHeatReactorEntity implements MultiBlockEntity {


    heat_pump(BlockEntitySimplePump.class, 0),
    heat_adv_pump(BlockEntityAdvPump.class, 1),
    heat_imp_pump(BlockEntityImpPump.class, 2),
    heat_per_pump(BlockEntityPerPump.class, 3),
    heat_controller(BlockEntitySimpleController.class, 4),
    heat_adv_controller(BlockEntityAdvController.class, 5),
    heat_imp_controller(BlockEntityImpController.class, 6),
    heat_per_controller(BlockEntityPerController.class, 7),
    heat_chamber(BlockEntitySimpleChamber.class, 8),
    heat_adv_chamber(BlockEntityAdvChamber.class, 9),
    heat_imp_chamber(BlockEntityImpChamber.class, 10),
    heat_per_chamber(BlockEntityPerChamber.class, 11),
    heat_reactor(BlockEntitySimpleReactor.class, 12),
    heat_adv_reactor(BlockEntityAdvReactor.class, 13),
    heat_imp_reactor(BlockEntityImpReactor.class, 14),
    heat_per_reactor(BlockEntityPerReactor.class, 15),

    heat_coolant(BlockEntitySimpleCoolant.class, 16),
    heat_adv_coolant(BlockEntityAdvCoolant.class, 17),
    heat_imp_coolant(BlockEntityImpCoolant.class, 18),
    heat_per_coolant(BlockEntityPerCoolant.class, 19),
    heat_graphite_controller(BlockEntitySimpleGraphiteController.class, 20),
    heat_adv_graphite_controller(BlockEntityAdvGraphiteController.class, 21),
    heat_imp_graphite_controller(BlockEntityImpGraphiteController.class, 22),
    heat_per_graphite_controller(BlockEntityPerGraphiteController.class, 23),
    heat_casing(BlockEntitySimpleCasing.class, 24),
    heat_adv_casing(BlockEntityAdvCasing.class, 25),
    heat_imp_casing(BlockEntityImpCasing.class, 26),
    heat_per_casing(BlockEntityPerCasing.class, 27),
    heat_socket(BlockEntitySimpleSocket.class, 28),
    heat_adv_socket(BlockEntityAdvSocket.class, 29),
    heat_imp_socket(BlockEntityImpSocket.class, 30),
    heat_per_socket(BlockEntityPerSocket.class, 31),
    heat_fueltank(BlockEntitySimpleFuelTank.class, 32),
    heat_adv_fueltank(BlockEntityAdvFuelTank.class, 33),
    heat_imp_fueltank(BlockEntityImpFuelTank.class, 34),
    heat_per_fueltank(BlockEntityPerFuelTank.class, 35),
    heat_circulationpump(BlockEntitySimpleCirculationPump.class, 36),
    heat_adv_circulationpump(BlockEntityAdvCirculationPump.class, 37),
    heat_imp_circulationpump(BlockEntityImpCirculationPump.class, 38),
    heat_per_circulationpump(BlockEntityPerCirculationPump.class, 39),
    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlockHeatReactorEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    ;

    BlockHeatReactorEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta, final Rarity rarity) {
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
    public void setType(DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockEntityType) {
        this.blockType = blockEntityType;
    }

    @Override
    public BlockEntityType<? extends BlockEntityBase> getBlockType() {
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
