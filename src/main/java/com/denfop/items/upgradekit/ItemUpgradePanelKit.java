package com.denfop.items.upgradekit;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class ItemUpgradePanelKit extends ItemSubTypes<ItemUpgradePanelKit.Types> implements IModelRegister {

    protected static final String NAME = "upgradekitpanel";

    public ItemUpgradePanelKit() {
        super(Types.class);
        this.setCreativeTab(IUCore.UpgradeTab);
        IUCore.proxy.addIModelRegister(this);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);

    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                item,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + extraName, null)
        );
    }


    @Override
    public void addInformation(
            @Nonnull final ItemStack p_77624_1_,
            @Nullable final World p_77624_2_,
            final List<String> p_77624_3_,
            @Nonnull final ITooltipFlag p_77624_4_
    ) {
        p_77624_3_.add(Localization.translate("waring_kit"));
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);

    }

    @Nonnull
    public EnumActionResult onItemUseFirst(
            @Nonnull EntityPlayer player,
            @Nonnull World world,
            @Nonnull BlockPos pos,
            @Nonnull EnumFacing side,
            float hitX,
            float hitY,
            float hitZ,
            @Nonnull EnumHand hand
    ) {
        if (!IUCore.proxy.isSimulating()) {
            return EnumActionResult.PASS;
        } else {
            final EnumActionResult hooks = ForgeHooks.onItemRightClick(player, hand);
            if (hooks == EnumActionResult.FAIL) {
                return hooks;
            }
            ItemStack stack = player.getHeldItem(hand);
            int meta = stack.getItemDamage();
            TileEntity tileEntity = world.getTileEntity(pos);
            if ((tileEntity instanceof TileSolarPanel)) {

                TileSolarPanel tile = (TileSolarPanel) tileEntity;
                if (tile.getPanels() == null) {
                    return EnumActionResult.PASS;
                }
                EnumSolarPanels oldpanel = tile.getPanels();
                EnumSolarPanels kit = EnumSolarPanels.getFromID(meta + 1);
                if (kit.solarold != null && !kit.solarold.equals(oldpanel)) {
                    return EnumActionResult.PASS;
                }


                final EnumSolarPanelsKit kit1 = EnumSolarPanelsKit.getFromID(meta);
                final IBlockState state = world.getBlockState(pos);
                state.getBlock().removedByPlayer(state, world, pos, (EntityPlayerMP) player, true);
                state.getBlock().onBlockDestroyedByPlayer(world, pos, state);
                state.getBlock().harvestBlock(world, (EntityPlayerMP) player, pos, state, null, stack);
                List<EntityItem> items = world.getEntitiesWithinAABB(
                        EntityItem.class,
                        new AxisAlignedBB(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1,
                                pos.getY() + 1,
                                pos.getZ() + 1
                        )
                );
                for (EntityItem item : items) {
                    item.setDead();
                }
                ItemStack stack1 = new ItemStack(kit1.solarpanel_new.block, 1, kit1.solarpanel_new.meta);

                EntityItem item = new EntityItem(world);
                item.setItem(stack1);
                if (!player.getEntityWorld().isRemote) {
                    item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                    item.setPickupDelay(0);
                    world.spawnEntity(item);

                }
                List<ItemStack> list = tile.getDrop();
                EntityItem[] item1 = new EntityItem[list.size()];

                for (ItemStack stack2 : list) {
                    item1[list.indexOf(stack2)] = new EntityItem(world);
                    item1[list.indexOf(stack2)].setItem(stack2);

                    if (!player.getEntityWorld().isRemote) {
                        item1[list.indexOf(stack2)].setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                        item1[list.indexOf(stack2)].setPickupDelay(0);
                        world.spawnEntity(item1[list.indexOf(stack2)]);
                    }
                }
                stack.setCount(stack.getCount() - 1);
                return EnumActionResult.SUCCESS;


            }
        }
        return EnumActionResult.PASS;
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + extraName, null)
        );
    }

    public enum Types implements ISubEnum {
        upgradepanelkit(0),
        upgradepanelkit1(1),
        upgradepanelkit2(2),
        upgradepanelkit3(3),

        upgradepanelkit4(4),
        upgradepanelkit5(5),
        upgradepanelkit6(6),
        upgradepanelkit7(7),
        upgradepanelkit8(8),
        upgradepanelkit9(9),
        upgradepanelkit10(10),
        upgradepanelkit11(11),
        upgradepanelkit12(12),
        upgradepanelkit13(13),

        upgradepanelkit14(14),
        upgradepanelkit15(15),
        upgradepanelkit16(16),
        upgradepanelkit17(17),
        upgradepanelkit18(18),
        upgradepanelkit19(19),
        upgradepanelkit20(20),
        upgradepanelkit21(21),
        upgradepanelkit22(22),
        upgradepanelkit23(23);

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.ID;
        }
    }

    public enum EnumSolarPanelsKit {
        ADVANCED(EnumSolarPanels.ADVANCED_SOLAR_PANEL, 0, true),
        HYBRID(EnumSolarPanels.HYBRID_SOLAR_PANEL, 1, true),
        PERFECT(EnumSolarPanels.PERFECT_SOLAR_PANEL, 2, true),
        QUANTUM(EnumSolarPanels.QUANTUM_SOLAR_PANEL, 3, true),
        SPECTRAL(EnumSolarPanels.SPECTRAL_SOLAR_PANEL, 4, true),
        PROTON(EnumSolarPanels.PROTON_SOLAR_PANEL, 5, true),
        SINGULAR(EnumSolarPanels.SINGULAR_SOLAR_PANEL, 6, true),
        DIFFRACTION(EnumSolarPanels.DIFFRACTION_SOLAR_PANEL, 7, true),
        PHOTON(EnumSolarPanels.PHOTONIC_SOLAR_PANEL, 8, true),
        NEUTRONIUM(EnumSolarPanels.NEUTRONIUN_SOLAR_PANEL, 9, true),
        BARION(EnumSolarPanels.BARION_SOLAR_PANEL, 10, true),
        HADRON(EnumSolarPanels.HADRON_SOLAR_PANEL, 11, true),
        GRAVITON(EnumSolarPanels.GRAVITON_SOLAR_PANEL, 12, true),
        QUARK(EnumSolarPanels.QUARK_SOLAR_PANEL, 13, true),

        DRACONIC(EnumSolarPanels.DRACONIC_SOLAR_PANEL, 14, true),
        AWAKANED(EnumSolarPanels.AWAKENED_SOLAR_PANEL, 15, true),
        CHAOS(EnumSolarPanels.CHAOTIC_SOLAR_PANEL, 16, true),
        MANASTEEL(EnumSolarPanels.MANASTEEL_SOLAR_PANEL, 17, true),
        ELEMENTUM(EnumSolarPanels.ELEMENTUM_SOLAR_PANEL, 18, true),
        TERRASTEEL(EnumSolarPanels.TERRASTEEL_SOLAR_PANEL, 19, true),
        NEUTRON_AV(EnumSolarPanels.NEUTRONIUM_SOLAR_PANEL_AVARITIA, 20, true
        ),
        INFINITY(EnumSolarPanels.INFINITY_SOLAR_PANEL, 21, true),
        THAUM(EnumSolarPanels.THAUM_SOLAR_PANEL, 22, true),
        VOID(EnumSolarPanels.VOID_SOLAR_PANEL, 23, true),
        ;

        public final int item_meta;
        public final EnumSolarPanels solarpanel_new;
        public final boolean register;

        EnumSolarPanelsKit(EnumSolarPanels solarpanel_new, int item_meta, boolean register) {
            this.item_meta = item_meta;
            this.solarpanel_new = solarpanel_new;
            this.register = register;
        }

        public static EnumSolarPanelsKit getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public static void registerkit() {
            for (EnumSolarPanelsKit machine : EnumSolarPanelsKit.values()) {
                IUItem.map1.put(machine.item_meta, machine.solarpanel_new);

            }
        }
    }

}
