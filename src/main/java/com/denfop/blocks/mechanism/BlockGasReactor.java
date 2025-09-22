package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
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
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

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


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("gas_reactor");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    int idBlock;
    private TileEntityBlock dummyTe;

    BlockGasReactor(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }

    ;

    public int getIDBlock() {
        return idBlock;
    }

    ;

    public void setIdBlock(int id) {
        idBlock = id;
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return IUCore.ReactorsBlockTab;
    }

    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockGasReactor block : values()) {
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
