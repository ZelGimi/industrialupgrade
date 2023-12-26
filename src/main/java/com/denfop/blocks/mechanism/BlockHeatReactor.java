package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.item.IMultiBlockItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import com.denfop.tiles.reactors.heat.controller.*;
import com.denfop.tiles.reactors.heat.graphite_controller.*;
import com.denfop.tiles.reactors.heat.reactor.*;
import com.denfop.tiles.reactors.heat.chamber.*;
import com.denfop.tiles.reactors.heat.coolant.*;
import com.denfop.tiles.reactors.heat.casing.*;
import com.denfop.tiles.reactors.heat.socket.*;
import com.denfop.tiles.reactors.heat.pump.*;
import com.denfop.tiles.reactors.heat.circulationpump.*;
import com.denfop.tiles.reactors.heat.fueltank.*;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockHeatReactor implements IMultiTileBlock, IMultiBlockItem {
    heat_pump(TileEntitySimplePump.class ,0),
    heat_adv_pump(TileEntityAdvPump.class,1),
    heat_imp_pump(TileEntityImpPump.class,2),
    heat_per_pump(TileEntityPerPump.class,3),
    heat_controller(TileEntitySimpleController.class,4),
    heat_adv_controller(TileEntityAdvController.class,5),
    heat_imp_controller(TileEntityImpController.class,6),
    heat_per_controller(TileEntityPerController.class,7),
    heat_chamber(TileEntitySimpleChamber.class,8),
    heat_adv_chamber(TileEntityAdvChamber.class,9),
    heat_imp_chamber(TileEntityImpChamber.class,10),
    heat_per_chamber(TileEntityPerChamber.class,11),
    heat_reactor(TileEntitySimpleReactor.class,12),
    heat_adv_reactor(TileEntityAdvReactor.class,13),
    heat_imp_reactor(TileEntityImpReactor.class,14),
    heat_per_reactor(TileEntityPerReactor.class,15),

    heat_coolant(TileEntitySimpleCoolant.class,16),
    heat_adv_coolant(TileEntityAdvCoolant.class,17),
    heat_imp_coolant(TileEntityImpCoolant.class,18),
    heat_per_coolant(TileEntityPerCoolant.class,19),
    heat_graphite_controller(TileEntitySimpleGraphiteController.class,20),
    heat_adv_graphite_controller(TileEntityAdvGraphiteController.class,21),
    heat_imp_graphite_controller(TileEntityImpGraphiteController.class,22),
    heat_per_graphite_controller(TileEntityPerGraphiteController.class,23),
    heat_casing(TileEntitySimpleCasing.class,24),
    heat_adv_casing(TileEntityAdvCasing.class,25),
    heat_imp_casing(TileEntityImpCasing.class,26),
    heat_per_casing(TileEntityPerCasing.class,27),
    heat_socket(TileEntitySimpleSocket.class,28),
    heat_adv_socket(TileEntityAdvSocket.class,29),
    heat_imp_socket(TileEntityImpSocket.class,30),
    heat_per_socket(TileEntityPerSocket.class,31),
    heat_fueltank(TileEntitySimpleFuelTank.class,32),
    heat_adv_fueltank(TileEntityAdvFuelTank.class,33),
    heat_imp_fueltank(TileEntityImpFuelTank.class,34),
    heat_per_fueltank(TileEntityPerFuelTank.class,35),
    heat_circulationpump(TileEntitySimpleCirculationPump.class,36),
    heat_adv_circulationpump(TileEntityAdvCirculationPump.class,37),
    heat_imp_circulationpump(TileEntityImpCirculationPump.class,38),
    heat_per_circulationpump(TileEntityPerCirculationPump.class,39),
    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("heat_reactors");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private TileEntityBlock dummyTe;


    BlockHeatReactor(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockHeatReactor(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


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
        for (final BlockHeatReactor block : values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception ignored) {

                }
            }
        }

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
        return true;
    }

    @Override
    public Class<? extends TileEntityBlock> getTeClass() {
        return this.teClass;
    }

    @Override
    public boolean hasActive() {
        return false;
    }

    @Override
    @Nonnull
    public Set<EnumFacing> getSupportedFacings() {
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 3.0f;
    }

    @Override
    @Nonnull
    public MultiTileBlock.HarvestTool getHarvestTool() {
        return MultiTileBlock.HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public MultiTileBlock.DefaultDrop getDefaultDrop() {
        return MultiTileBlock.DefaultDrop.Machine;
    }

    @Override
    public boolean allowWrenchRotating() {
        return true;
    }

    @Override
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }

    @Override
    public boolean hasUniqueRender(final ItemStack itemStack) {
        return false;
    }

    @Override
    public ModelResourceLocation getModelLocation(final ItemStack itemStack) {
        return null;
    }
}
