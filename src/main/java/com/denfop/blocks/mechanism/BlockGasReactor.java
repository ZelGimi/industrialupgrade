package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.reactors.gas.casing.TileEntityAdvCasing;
import com.denfop.tiles.reactors.gas.casing.TileEntityImpCasing;
import com.denfop.tiles.reactors.gas.casing.TileEntityPerCasing;
import com.denfop.tiles.reactors.gas.casing.TileEntitySimpleCasing;
import com.denfop.tiles.reactors.gas.cell.TileEntityAdvCell;
import com.denfop.tiles.reactors.gas.cell.TileEntityImpCell;
import com.denfop.tiles.reactors.gas.cell.TileEntityPerCell;
import com.denfop.tiles.reactors.gas.cell.TileEntitySimpleCell;
import com.denfop.tiles.reactors.gas.chamber.TileEntityAdvChamber;
import com.denfop.tiles.reactors.gas.chamber.TileEntityImpChamber;
import com.denfop.tiles.reactors.gas.chamber.TileEntityPerChamber;
import com.denfop.tiles.reactors.gas.chamber.TileEntitySimpleChamber;
import com.denfop.tiles.reactors.gas.compressor.TileEntityAdvCompressor;
import com.denfop.tiles.reactors.gas.compressor.TileEntityImpCompressor;
import com.denfop.tiles.reactors.gas.compressor.TileEntityPerCompressor;
import com.denfop.tiles.reactors.gas.compressor.TileEntitySimpleCompressor;
import com.denfop.tiles.reactors.gas.controller.TileEntityAdvController;
import com.denfop.tiles.reactors.gas.controller.TileEntityImpController;
import com.denfop.tiles.reactors.gas.controller.TileEntityPerController;
import com.denfop.tiles.reactors.gas.controller.TileEntitySimpleController;
import com.denfop.tiles.reactors.gas.coolant.TileEntityAdvCoolant;
import com.denfop.tiles.reactors.gas.coolant.TileEntityImpCoolant;
import com.denfop.tiles.reactors.gas.coolant.TileEntityPerCoolant;
import com.denfop.tiles.reactors.gas.coolant.TileEntitySimpleCoolant;
import com.denfop.tiles.reactors.gas.intercooler.TileEntityAdvInterCooler;
import com.denfop.tiles.reactors.gas.intercooler.TileEntityImpInterCooler;
import com.denfop.tiles.reactors.gas.intercooler.TileEntityPerInterCooler;
import com.denfop.tiles.reactors.gas.intercooler.TileEntitySimpleInterCooler;
import com.denfop.tiles.reactors.gas.recirculation_pump.TileEntityAdvReCirculationPump;
import com.denfop.tiles.reactors.gas.recirculation_pump.TileEntityImpReCirculationPump;
import com.denfop.tiles.reactors.gas.recirculation_pump.TileEntityPerReCirculationPump;
import com.denfop.tiles.reactors.gas.recirculation_pump.TileEntitySimpleReCirculationPump;
import com.denfop.tiles.reactors.gas.regenerator.TileEntityAdvRegenerator;
import com.denfop.tiles.reactors.gas.regenerator.TileEntityImpRegenerator;
import com.denfop.tiles.reactors.gas.regenerator.TileEntityPerRegenerator;
import com.denfop.tiles.reactors.gas.regenerator.TileEntitySimpleRegenerator;
import com.denfop.tiles.reactors.gas.socket.TileEntityAdvSocket;
import com.denfop.tiles.reactors.gas.socket.TileEntityImpSocket;
import com.denfop.tiles.reactors.gas.socket.TileEntityPerSocket;
import com.denfop.tiles.reactors.gas.socket.TileEntitySimpleSocket;
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

public enum BlockGasReactor implements IMultiTileBlock {


    gas_cell(TileEntitySimpleCell.class, 0),
    adv_gas_cell(TileEntityAdvCell.class, 1),
    imp_gas_cell(TileEntityImpCell.class, 2),
    per_gas_cell(TileEntityPerCell.class, 3),
    gas_coolant(TileEntitySimpleCoolant.class, 4),
    adv_gas_coolant(TileEntityAdvCoolant.class, 5),
    imp_gas_coolant(TileEntityImpCoolant.class, 6),
    per_gas_coolant(TileEntityPerCoolant.class, 7),
    gas_intercooler(TileEntitySimpleInterCooler.class, 8),
    adv_gas_intercooler(TileEntityAdvInterCooler.class, 9),
    imp_gas_intercooler(TileEntityImpInterCooler.class, 10),
    per_gas_intercooler(TileEntityPerInterCooler.class, 11),
    gas_casing(TileEntitySimpleCasing.class, 12),
    adv_gas_casing(TileEntityAdvCasing.class, 13),
    imp_gas_casing(TileEntityImpCasing.class, 14),
    per_gas_casing(TileEntityPerCasing.class, 15),
    gas_compressor(TileEntitySimpleCompressor.class, 16),
    adv_gas_compressor(TileEntityAdvCompressor.class, 17),
    imp_gas_compressor(TileEntityImpCompressor.class, 18),
    per_gas_compressor(TileEntityPerCompressor.class, 19),
    gas_regenerator(TileEntitySimpleRegenerator.class, 20),
    adv_gas_regenerator(TileEntityAdvRegenerator.class, 21),
    imp_gas_regenerator(TileEntityImpRegenerator.class, 22),
    per_gas_regenerator(TileEntityPerRegenerator.class, 23),
    gas_controller(TileEntitySimpleController.class, 24),
    adv_gas_controller(TileEntityAdvController.class, 25),
    imp_gas_controller(TileEntityImpController.class, 26),
    per_gas_controller(TileEntityPerController.class, 27),
    gas_recirculation_pump(TileEntitySimpleReCirculationPump.class, 28),
    adv_gas_recirculation_pump(TileEntityAdvReCirculationPump.class, 29),
    imp_gas_recirculation_pump(TileEntityImpReCirculationPump.class, 30),
    per_gas_recirculation_pump(TileEntityPerReCirculationPump.class, 31),
    gas_socket(TileEntitySimpleSocket.class, 32),
    adv_gas_socket(TileEntityAdvSocket.class, 33),
    imp_gas_socket(TileEntityImpSocket.class, 34),
    per_gas_socket(TileEntityPerSocket.class, 35),
    gas_chamber(TileEntitySimpleChamber.class, 36),
    adv_gas_chamber(TileEntityAdvChamber.class, 37),
    imp_gas_chamber(TileEntityImpChamber.class, 38),
    per_gas_chamber(TileEntityPerChamber.class, 39),
    ;


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockType;

    ;

    BlockGasReactor(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    ;

    BlockGasReactor(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final Rarity rarity) {
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
        final ModContainer mc = ModLoadingContext.get().getActiveContainer();
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
    public void setType(RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockEntityType) {
        this.blockType = blockEntityType;
    }

    @Override
    public BlockEntityType<? extends TileEntityBlock> getBlockType() {
        return this.blockType.get();
    }

    @Override
    public String getMainPath() {
        return "gas_reactor";
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
    public CreativeModeTab getCreativeTab() {
        return IUCore.ReactorsBlockTab;
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
