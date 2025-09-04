package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.reactors.gas.casing.BlockEntityAdvCasing;
import com.denfop.blockentity.reactors.gas.casing.BlockEntityImpCasing;
import com.denfop.blockentity.reactors.gas.casing.BlockEntityPerCasing;
import com.denfop.blockentity.reactors.gas.casing.BlockEntitySimpleCasing;
import com.denfop.blockentity.reactors.gas.cell.BlockEntityAdvCell;
import com.denfop.blockentity.reactors.gas.cell.BlockEntityImpCell;
import com.denfop.blockentity.reactors.gas.cell.BlockEntityPerCell;
import com.denfop.blockentity.reactors.gas.cell.BlockEntitySimpleCell;
import com.denfop.blockentity.reactors.gas.chamber.BlockEntityAdvChamber;
import com.denfop.blockentity.reactors.gas.chamber.BlockEntityImpChamber;
import com.denfop.blockentity.reactors.gas.chamber.BlockEntityPerChamber;
import com.denfop.blockentity.reactors.gas.chamber.BlockEntitySimpleChamber;
import com.denfop.blockentity.reactors.gas.compressor.BlockEntityAdvCompressor;
import com.denfop.blockentity.reactors.gas.compressor.BlockEntityImpCompressor;
import com.denfop.blockentity.reactors.gas.compressor.BlockEntityPerCompressor;
import com.denfop.blockentity.reactors.gas.compressor.BlockEntitySimpleCompressor;
import com.denfop.blockentity.reactors.gas.controller.BlockEntityAdvController;
import com.denfop.blockentity.reactors.gas.controller.BlockEntityImpController;
import com.denfop.blockentity.reactors.gas.controller.BlockEntityPerController;
import com.denfop.blockentity.reactors.gas.controller.BlockEntitySimpleController;
import com.denfop.blockentity.reactors.gas.coolant.BlockEntityAdvCoolant;
import com.denfop.blockentity.reactors.gas.coolant.BlockEntityImpCoolant;
import com.denfop.blockentity.reactors.gas.coolant.BlockEntityPerCoolant;
import com.denfop.blockentity.reactors.gas.coolant.BlockEntitySimpleCoolant;
import com.denfop.blockentity.reactors.gas.intercooler.BlockEntityAdvInterCooler;
import com.denfop.blockentity.reactors.gas.intercooler.BlockEntityImpInterCooler;
import com.denfop.blockentity.reactors.gas.intercooler.BlockEntityPerInterCooler;
import com.denfop.blockentity.reactors.gas.intercooler.BlockEntitySimpleInterCooler;
import com.denfop.blockentity.reactors.gas.recirculation_pump.BlockEntityAdvReCirculationPump;
import com.denfop.blockentity.reactors.gas.recirculation_pump.BlockEntityImpReCirculationPump;
import com.denfop.blockentity.reactors.gas.recirculation_pump.BlockEntityPerReCirculationPump;
import com.denfop.blockentity.reactors.gas.recirculation_pump.BlockEntitySimpleReCirculationPump;
import com.denfop.blockentity.reactors.gas.regenerator.BlockEntityAdvRegenerator;
import com.denfop.blockentity.reactors.gas.regenerator.BlockEntityImpRegenerator;
import com.denfop.blockentity.reactors.gas.regenerator.BlockEntityPerRegenerator;
import com.denfop.blockentity.reactors.gas.regenerator.BlockEntitySimpleRegenerator;
import com.denfop.blockentity.reactors.gas.socket.BlockEntityAdvSocket;
import com.denfop.blockentity.reactors.gas.socket.BlockEntityImpSocket;
import com.denfop.blockentity.reactors.gas.socket.BlockEntityPerSocket;
import com.denfop.blockentity.reactors.gas.socket.BlockEntitySimpleSocket;
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

public enum BlockGasReactorEntity implements MultiBlockEntity {


    gas_cell(BlockEntitySimpleCell.class, 0),
    adv_gas_cell(BlockEntityAdvCell.class, 1),
    imp_gas_cell(BlockEntityImpCell.class, 2),
    per_gas_cell(BlockEntityPerCell.class, 3),
    gas_coolant(BlockEntitySimpleCoolant.class, 4),
    adv_gas_coolant(BlockEntityAdvCoolant.class, 5),
    imp_gas_coolant(BlockEntityImpCoolant.class, 6),
    per_gas_coolant(BlockEntityPerCoolant.class, 7),
    gas_intercooler(BlockEntitySimpleInterCooler.class, 8),
    adv_gas_intercooler(BlockEntityAdvInterCooler.class, 9),
    imp_gas_intercooler(BlockEntityImpInterCooler.class, 10),
    per_gas_intercooler(BlockEntityPerInterCooler.class, 11),
    gas_casing(BlockEntitySimpleCasing.class, 12),
    adv_gas_casing(BlockEntityAdvCasing.class, 13),
    imp_gas_casing(BlockEntityImpCasing.class, 14),
    per_gas_casing(BlockEntityPerCasing.class, 15),
    gas_compressor(BlockEntitySimpleCompressor.class, 16),
    adv_gas_compressor(BlockEntityAdvCompressor.class, 17),
    imp_gas_compressor(BlockEntityImpCompressor.class, 18),
    per_gas_compressor(BlockEntityPerCompressor.class, 19),
    gas_regenerator(BlockEntitySimpleRegenerator.class, 20),
    adv_gas_regenerator(BlockEntityAdvRegenerator.class, 21),
    imp_gas_regenerator(BlockEntityImpRegenerator.class, 22),
    per_gas_regenerator(BlockEntityPerRegenerator.class, 23),
    gas_controller(BlockEntitySimpleController.class, 24),
    adv_gas_controller(BlockEntityAdvController.class, 25),
    imp_gas_controller(BlockEntityImpController.class, 26),
    per_gas_controller(BlockEntityPerController.class, 27),
    gas_recirculation_pump(BlockEntitySimpleReCirculationPump.class, 28),
    adv_gas_recirculation_pump(BlockEntityAdvReCirculationPump.class, 29),
    imp_gas_recirculation_pump(BlockEntityImpReCirculationPump.class, 30),
    per_gas_recirculation_pump(BlockEntityPerReCirculationPump.class, 31),
    gas_socket(BlockEntitySimpleSocket.class, 32),
    adv_gas_socket(BlockEntityAdvSocket.class, 33),
    imp_gas_socket(BlockEntityImpSocket.class, 34),
    per_gas_socket(BlockEntityPerSocket.class, 35),
    gas_chamber(BlockEntitySimpleChamber.class, 36),
    adv_gas_chamber(BlockEntityAdvChamber.class, 37),
    imp_gas_chamber(BlockEntityImpChamber.class, 38),
    per_gas_chamber(BlockEntityPerChamber.class, 39),
    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlockGasReactorEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    ;

    BlockGasReactorEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta, final Rarity rarity) {
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
    public BlockEntityBase getDummyTe() {
        return this.dummyTe;
    }

    @Override
    public String[] getMultiModels(final MultiBlockEntity teBlock) {
        return MultiBlockEntity.super.getMultiModels(teBlock);
    }
}
