package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.item.IMultiBlockItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.reactors.water.casing.TileEntityAdvCasing;
import com.denfop.tiles.reactors.water.casing.TileEntityImpCasing;
import com.denfop.tiles.reactors.water.casing.TileEntityPerCasing;
import com.denfop.tiles.reactors.water.casing.TileEntitySimpleCasing;
import com.denfop.tiles.reactors.water.chamber.TileEntityAdvChamber;
import com.denfop.tiles.reactors.water.chamber.TileEntityImpChamber;
import com.denfop.tiles.reactors.water.chamber.TileEntityPerChamber;
import com.denfop.tiles.reactors.water.chamber.TileEntitySimpleChamber;
import com.denfop.tiles.reactors.water.controller.TileEntityAdvController;
import com.denfop.tiles.reactors.water.controller.TileEntityImpController;
import com.denfop.tiles.reactors.water.controller.TileEntityPerController;
import com.denfop.tiles.reactors.water.controller.TileEntitySimpleController;
import com.denfop.tiles.reactors.water.inputfluid.TileEntityAdvInputPort;
import com.denfop.tiles.reactors.water.inputfluid.TileEntityImpInputPort;
import com.denfop.tiles.reactors.water.inputfluid.TileEntityPerInputPort;
import com.denfop.tiles.reactors.water.inputfluid.TileEntitySimpleInputPort;
import com.denfop.tiles.reactors.water.levelfuel.TileEntityAdvLevelFuel;
import com.denfop.tiles.reactors.water.levelfuel.TileEntityImpLevelFuel;
import com.denfop.tiles.reactors.water.levelfuel.TileEntityPerLevelFuel;
import com.denfop.tiles.reactors.water.levelfuel.TileEntitySimpleLevelFuel;
import com.denfop.tiles.reactors.water.outputfluid.TileEntityAdvOutputPort;
import com.denfop.tiles.reactors.water.outputfluid.TileEntityImpOutputPort;
import com.denfop.tiles.reactors.water.outputfluid.TileEntityPerOutputPort;
import com.denfop.tiles.reactors.water.outputfluid.TileEntitySimpleOutputPort;
import com.denfop.tiles.reactors.water.reactor.TileEntityAdvReactor;
import com.denfop.tiles.reactors.water.reactor.TileEntityImpReactor;
import com.denfop.tiles.reactors.water.reactor.TileEntityPerReactor;
import com.denfop.tiles.reactors.water.reactor.TileEntitySimpleReactor;
import com.denfop.tiles.reactors.water.security.TileEntityAdvSecurity;
import com.denfop.tiles.reactors.water.security.TileEntityImpSecurity;
import com.denfop.tiles.reactors.water.security.TileEntityPerSecurity;
import com.denfop.tiles.reactors.water.security.TileEntitySimpleSecurity;
import com.denfop.tiles.reactors.water.socket.TileEntityAdvSocket;
import com.denfop.tiles.reactors.water.socket.TileEntityImpSocket;
import com.denfop.tiles.reactors.water.socket.TileEntityPerSocket;
import com.denfop.tiles.reactors.water.socket.TileEntitySimpleSocket;
import com.denfop.tiles.reactors.water.tank.TileEntityAdvTank;
import com.denfop.tiles.reactors.water.tank.TileEntityImpTank;
import com.denfop.tiles.reactors.water.tank.TileEntityPerTank;
import com.denfop.tiles.reactors.water.tank.TileEntitySimpleTank;
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

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockWaterReactors implements IMultiTileBlock, IMultiBlockItem {
    water_casing(TileEntitySimpleCasing.class, 0),
    water_adv_casing(TileEntityAdvCasing.class, 1),
    water_imp_casing(TileEntityImpCasing.class, 2),
    water_per_casing(TileEntityPerCasing.class, 3),

    water_chamber(TileEntitySimpleChamber.class, 4),
    water_adv_chamber(TileEntityAdvChamber.class, 5),
    water_imp_chamber(TileEntityImpChamber.class, 6),
    water_per_chamber(TileEntityPerChamber.class, 7),

    water_controller(TileEntitySimpleController.class, 8),
    water_adv_controller(TileEntityAdvController.class, 9),
    water_imp_controller(TileEntityImpController.class, 10),
    water_per_controller(TileEntityPerController.class, 11),

    water_input(TileEntitySimpleInputPort.class, 12),
    water_adv_input(TileEntityAdvInputPort.class, 13),
    water_imp_input(TileEntityImpInputPort.class, 14),
    water_per_input(TileEntityPerInputPort.class, 15),

    water_levelfuel(TileEntitySimpleLevelFuel.class, 16),
    water_adv_levelfuel(TileEntityAdvLevelFuel.class, 17),
    water_imp_levelfuel(TileEntityImpLevelFuel.class, 18),
    water_per_levelfuel(TileEntityPerLevelFuel.class, 19),

    water_output(TileEntitySimpleOutputPort.class, 20),
    water_adv_output(TileEntityAdvOutputPort.class, 21),
    water_imp_output(TileEntityImpOutputPort.class, 22),
    water_per_output(TileEntityPerOutputPort.class, 23),

    water_security(TileEntitySimpleSecurity.class, 24),
    water_adv_security(TileEntityAdvSecurity.class, 25),
    water_imp_security(TileEntityImpSecurity.class, 26),
    water_per_security(TileEntityPerSecurity.class, 27),

    water_socket(TileEntitySimpleSocket.class, 28),
    water_adv_socket(TileEntityAdvSocket.class, 29),
    water_imp_socket(TileEntityImpSocket.class, 30),
    water_per_socket(TileEntityPerSocket.class, 31),

    water_tank(TileEntitySimpleTank.class, 32),
    water_adv_tank(TileEntityAdvTank.class, 33),
    water_imp_tank(TileEntityImpTank.class, 34),
    water_per_tank(TileEntityPerTank.class, 35),
    water_reactor(TileEntitySimpleReactor.class, 36),
    water_adv_reactor(TileEntityAdvReactor.class, 37),
    water_imp_reactor(TileEntityImpReactor.class, 38),
    water_per_reactor(TileEntityPerReactor.class, 39),
    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("water_reactors");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private TileEntityBlock dummyTe;


    BlockWaterReactors(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockWaterReactors(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }
    int idBlock;
    public  int getIDBlock(){
        return idBlock;
    };

    public void setIdBlock(int id){
        idBlock = id;
    };
    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockWaterReactors block : values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception ignored) {

                }
            }
        }

    }

    @Override
    public CreativeTabs getCreativeTab() {
        return IUCore.ReactorsBlockTab;
    }

    @Override
    public String[] getMultiModels(final IMultiTileBlock teBlock) {
        if (teBlock == BlockWaterReactors.water_security || teBlock == BlockWaterReactors.water_per_security || teBlock == BlockWaterReactors.water_adv_security || teBlock == BlockWaterReactors.water_imp_security) {
            return new String[]{"stable", "unstable", "error"};
        } else {
            if (teBlock == BlockWaterReactors.water_tank || teBlock == BlockWaterReactors.water_per_tank || teBlock == BlockWaterReactors.water_adv_tank || teBlock == BlockWaterReactors.water_imp_tank) {
                return new String[]{"1", "2", "3"};
            } else {

                if (teBlock == BlockWaterReactors.water_controller || teBlock == BlockWaterReactors.water_per_controller || teBlock == BlockWaterReactors.water_adv_controller || teBlock == BlockWaterReactors.water_imp_controller) {
                    return new String[]{"active","global"};
                } else {

                    return IMultiTileBlock.super.getMultiModels(teBlock);
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
        return true;
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

    @Override
    public boolean hasUniqueRender(final ItemStack itemStack) {
        return false;
    }

    @Override
    public ModelResourceLocation getModelLocation(final ItemStack itemStack) {
        return null;
    }
}
