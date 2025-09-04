package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.reactors.water.casing.BlockEntityAdvCasing;
import com.denfop.blockentity.reactors.water.casing.BlockEntityImpCasing;
import com.denfop.blockentity.reactors.water.casing.BlockEntityPerCasing;
import com.denfop.blockentity.reactors.water.casing.BlockEntitySimpleCasing;
import com.denfop.blockentity.reactors.water.chamber.BlockEntityAdvChamber;
import com.denfop.blockentity.reactors.water.chamber.BlockEntityImpChamber;
import com.denfop.blockentity.reactors.water.chamber.BlockEntityPerChamber;
import com.denfop.blockentity.reactors.water.chamber.BlockEntitySimpleChamber;
import com.denfop.blockentity.reactors.water.controller.BlockEntityAdvController;
import com.denfop.blockentity.reactors.water.controller.BlockEntityImpController;
import com.denfop.blockentity.reactors.water.controller.BlockEntityPerController;
import com.denfop.blockentity.reactors.water.controller.BlockEntitySimpleController;
import com.denfop.blockentity.reactors.water.inputfluid.BlockEntityAdvInputPort;
import com.denfop.blockentity.reactors.water.inputfluid.BlockEntityImpInputPort;
import com.denfop.blockentity.reactors.water.inputfluid.BlockEntityPerInputPort;
import com.denfop.blockentity.reactors.water.inputfluid.BlockEntitySimpleInputPort;
import com.denfop.blockentity.reactors.water.levelfuel.BlockEntityAdvLevelFuel;
import com.denfop.blockentity.reactors.water.levelfuel.BlockEntityImpLevelFuel;
import com.denfop.blockentity.reactors.water.levelfuel.BlockEntityPerLevelFuel;
import com.denfop.blockentity.reactors.water.levelfuel.BlockEntitySimpleLevelFuel;
import com.denfop.blockentity.reactors.water.outputfluid.BlockEntityAdvOutputPort;
import com.denfop.blockentity.reactors.water.outputfluid.BlockEntityImpOutputPort;
import com.denfop.blockentity.reactors.water.outputfluid.BlockEntityPerOutputPort;
import com.denfop.blockentity.reactors.water.outputfluid.BlockEntitySimpleOutputPort;
import com.denfop.blockentity.reactors.water.reactor.BlockEntityAdvReactor;
import com.denfop.blockentity.reactors.water.reactor.BlockEntityImpReactor;
import com.denfop.blockentity.reactors.water.reactor.BlockEntityPerReactor;
import com.denfop.blockentity.reactors.water.reactor.BlockEntitySimpleReactor;
import com.denfop.blockentity.reactors.water.security.BlockEntityAdvSecurity;
import com.denfop.blockentity.reactors.water.security.BlockEntityImpSecurity;
import com.denfop.blockentity.reactors.water.security.BlockEntityPerSecurity;
import com.denfop.blockentity.reactors.water.security.BlockEntitySimpleSecurity;
import com.denfop.blockentity.reactors.water.socket.BlockEntityAdvSocket;
import com.denfop.blockentity.reactors.water.socket.BlockEntityImpSocket;
import com.denfop.blockentity.reactors.water.socket.BlockEntityPerSocket;
import com.denfop.blockentity.reactors.water.socket.BlockEntitySimpleSocket;
import com.denfop.blockentity.reactors.water.tank.BlockEntityAdvTank;
import com.denfop.blockentity.reactors.water.tank.BlockEntityImpTank;
import com.denfop.blockentity.reactors.water.tank.BlockEntityPerTank;
import com.denfop.blockentity.reactors.water.tank.BlockEntitySimpleTank;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.util.Set;

;

public enum BlockWaterReactorsEntity implements MultiBlockEntity {

    water_casing(BlockEntitySimpleCasing.class, 0),
    water_adv_casing(BlockEntityAdvCasing.class, 1),
    water_imp_casing(BlockEntityImpCasing.class, 2),
    water_per_casing(BlockEntityPerCasing.class, 3),

    water_chamber(BlockEntitySimpleChamber.class, 4),
    water_adv_chamber(BlockEntityAdvChamber.class, 5),
    water_imp_chamber(BlockEntityImpChamber.class, 6),
    water_per_chamber(BlockEntityPerChamber.class, 7),

    water_controller(BlockEntitySimpleController.class, 8),
    water_adv_controller(BlockEntityAdvController.class, 9),
    water_imp_controller(BlockEntityImpController.class, 10),
    water_per_controller(BlockEntityPerController.class, 11),

    water_input(BlockEntitySimpleInputPort.class, 12),
    water_adv_input(BlockEntityAdvInputPort.class, 13),
    water_imp_input(BlockEntityImpInputPort.class, 14),
    water_per_input(BlockEntityPerInputPort.class, 15),

    water_levelfuel(BlockEntitySimpleLevelFuel.class, 16),
    water_adv_levelfuel(BlockEntityAdvLevelFuel.class, 17),
    water_imp_levelfuel(BlockEntityImpLevelFuel.class, 18),
    water_per_levelfuel(BlockEntityPerLevelFuel.class, 19),

    water_output(BlockEntitySimpleOutputPort.class, 20),
    water_adv_output(BlockEntityAdvOutputPort.class, 21),
    water_imp_output(BlockEntityImpOutputPort.class, 22),
    water_per_output(BlockEntityPerOutputPort.class, 23),

    water_security(BlockEntitySimpleSecurity.class, 24),
    water_adv_security(BlockEntityAdvSecurity.class, 25),
    water_imp_security(BlockEntityImpSecurity.class, 26),
    water_per_security(BlockEntityPerSecurity.class, 27),

    water_socket(BlockEntitySimpleSocket.class, 28),
    water_adv_socket(BlockEntityAdvSocket.class, 29),
    water_imp_socket(BlockEntityImpSocket.class, 30),
    water_per_socket(BlockEntityPerSocket.class, 31),

    water_tank(BlockEntitySimpleTank.class, 32),
    water_adv_tank(BlockEntityAdvTank.class, 33),
    water_imp_tank(BlockEntityImpTank.class, 34),
    water_per_tank(BlockEntityPerTank.class, 35),
    water_reactor(BlockEntitySimpleReactor.class, 36),
    water_adv_reactor(BlockEntityAdvReactor.class, 37),
    water_imp_reactor(BlockEntityImpReactor.class, 38),
    water_per_reactor(BlockEntityPerReactor.class, 39),


    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockType;

    BlockWaterReactorsEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    ;

    BlockWaterReactorsEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta, final Rarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;


    }

    ;

    public int getIDBlock() {
        return idBlock;
    }

    public void setIdBlock(int id) {
        idBlock = id;
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
        return "water_reactors";
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
        return false;
    }

    @Override
    @Nonnull
    public Set<Direction> getSupportedFacings() {
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 3.0f;
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
        return true;
    }

    @Override
    public BlockEntityBase getDummyTe() {
        return this.dummyTe;
    }

    @Override
    public CreativeModeTab getCreativeTab() {
        return IUCore.ReactorsBlockTab;
    }

    @Override
    public String[] getMultiModels(final MultiBlockEntity teBlock) {
        if (teBlock == BlockWaterReactorsEntity.water_security || teBlock == BlockWaterReactorsEntity.water_per_security || teBlock == BlockWaterReactorsEntity.water_adv_security || teBlock == BlockWaterReactorsEntity.water_imp_security) {
            return new String[]{"stable", "unstable", "error"};
        } else {
            if (teBlock == BlockWaterReactorsEntity.water_tank || teBlock == BlockWaterReactorsEntity.water_per_tank || teBlock == BlockWaterReactorsEntity.water_adv_tank || teBlock == BlockWaterReactorsEntity.water_imp_tank) {
                return new String[]{"1", "2", "3"};
            } else {

                if (teBlock == BlockWaterReactorsEntity.water_controller || teBlock == BlockWaterReactorsEntity.water_per_controller || teBlock == BlockWaterReactorsEntity.water_adv_controller || teBlock == BlockWaterReactorsEntity.water_imp_controller) {
                    return new String[]{"active", "global"};
                } else {

                    return MultiBlockEntity.super.getMultiModels(teBlock);
                }
            }

        }
    }
}
