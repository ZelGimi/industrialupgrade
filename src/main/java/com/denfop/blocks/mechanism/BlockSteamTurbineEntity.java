package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.mechanism.steamturbine.*;
import com.denfop.blockentity.mechanism.steamturbine.controller.BlockEntityAdvSteamTurbineController;
import com.denfop.blockentity.mechanism.steamturbine.controller.BlockEntityImpSteamTurbineController;
import com.denfop.blockentity.mechanism.steamturbine.controller.BlockEntityPerSteamTurbineController;
import com.denfop.blockentity.mechanism.steamturbine.controller.BlockEntitySimpleSteamTurbineController;
import com.denfop.blockentity.mechanism.steamturbine.coolant.BlockEntityAdvSteamTurbineCoolant;
import com.denfop.blockentity.mechanism.steamturbine.coolant.BlockEntityImpSteamTurbineCoolant;
import com.denfop.blockentity.mechanism.steamturbine.coolant.BlockEntityPerSteamTurbineCoolant;
import com.denfop.blockentity.mechanism.steamturbine.coolant.BlockEntitySimpleSteamTurbineCoolant;
import com.denfop.blockentity.mechanism.steamturbine.exchanger.BlockEntityAdvSteamTurbineExchanger;
import com.denfop.blockentity.mechanism.steamturbine.exchanger.BlockEntityImpSteamTurbineExchanger;
import com.denfop.blockentity.mechanism.steamturbine.exchanger.BlockEntityPerSteamTurbineExchanger;
import com.denfop.blockentity.mechanism.steamturbine.exchanger.BlockEntitySimpleSteamTurbineExchanger;
import com.denfop.blockentity.mechanism.steamturbine.pressure.BlockEntityAdvSteamTurbinePressure;
import com.denfop.blockentity.mechanism.steamturbine.pressure.BlockEntityImpSteamTurbinePressure;
import com.denfop.blockentity.mechanism.steamturbine.pressure.BlockEntityPerSteamTurbinePressure;
import com.denfop.blockentity.mechanism.steamturbine.pressure.BlockEntitySimpleSteamTurbinePressure;
import com.denfop.blockentity.mechanism.steamturbine.tank.BlockEntityAdvSteamTurbineTank;
import com.denfop.blockentity.mechanism.steamturbine.tank.BlockEntityImpSteamTurbineTank;
import com.denfop.blockentity.mechanism.steamturbine.tank.BlockEntityPerSteamTurbineTank;
import com.denfop.blockentity.mechanism.steamturbine.tank.BlockEntitySimpleSteamTurbineTank;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockSteamTurbineEntity implements MultiBlockEntity {

    steam_turbine_casing(BlockEntitySteamTurbineCasing.class, 0),
    steam_turbine_casing_glass(BlockEntitySteamTurbineGlass.class, 1),
    steam_turbine_socket(BlockEntitySteamTurbineSocket.class, 2),
    steam_turbine_rod(BlockEntitySteamTurbineRod.class, 3),

    steam_turbine_controller_rod(BlockEntitySteamTurbineControllerRod.class, 4),

    steam_turbine_controller(BlockEntitySimpleSteamTurbineController.class, 5),
    steam_turbine_adv_controller(BlockEntityAdvSteamTurbineController.class, 6),
    steam_turbine_imp_controller(BlockEntityImpSteamTurbineController.class, 7),
    steam_turbine_per_controller(BlockEntityPerSteamTurbineController.class, 8),

    steam_turbine_exchanger(BlockEntitySimpleSteamTurbineExchanger.class, 9),
    steam_turbine_adv_exchanger(BlockEntityAdvSteamTurbineExchanger.class, 10),
    steam_turbine_imp_exchanger(BlockEntityImpSteamTurbineExchanger.class, 11),
    steam_turbine_per_exchanger(BlockEntityPerSteamTurbineExchanger.class, 12),

    steam_turbine_tank(BlockEntitySimpleSteamTurbineTank.class, 13),
    steam_turbine_adv_tank(BlockEntityAdvSteamTurbineTank.class, 14),
    steam_turbine_imp_tank(BlockEntityImpSteamTurbineTank.class, 15),
    steam_turbine_per_tank(BlockEntityPerSteamTurbineTank.class, 16),

    steam_turbine_coolant(BlockEntitySimpleSteamTurbineCoolant.class, 17),
    steam_turbine_adv_coolant(BlockEntityAdvSteamTurbineCoolant.class, 18),
    steam_turbine_imp_coolant(BlockEntityImpSteamTurbineCoolant.class, 19),
    steam_turbine_per_coolant(BlockEntityPerSteamTurbineCoolant.class, 20),

    steam_turbine_pressure(BlockEntitySimpleSteamTurbinePressure.class, 21),
    steam_turbine_adv_pressure(BlockEntityAdvSteamTurbinePressure.class, 22),
    steam_turbine_imp_pressure(BlockEntityImpSteamTurbinePressure.class, 23),
    steam_turbine_per_pressure(BlockEntityPerSteamTurbinePressure.class, 24),
    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlockSteamTurbineEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    ;

    BlockSteamTurbineEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta, final Rarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;


    }

    public int getIDBlock() {
        return idBlock;
    }

    public void setIdBlock(int id) {
        idBlock = id;
    }

    @Override
    public Material getMaterial() {
        return Material.METAL;
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
        return "steam_turbine";
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
