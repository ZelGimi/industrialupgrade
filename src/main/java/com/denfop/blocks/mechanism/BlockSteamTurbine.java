package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.steamturbine.*;
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
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.util.Set;

;

public enum BlockSteamTurbine implements IMultiTileBlock {

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


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TileEntityBlock>> blockType;

    ;

    BlockSteamTurbine(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    ;

    BlockSteamTurbine(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final Rarity rarity) {
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
