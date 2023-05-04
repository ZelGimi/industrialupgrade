package com.denfop.items.upgradekit;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.IIdProvider;
import com.denfop.integration.avaritia.TileEntityInfinitySolarPanel;
import com.denfop.integration.botania.TileEntityElementumSolarPanel;
import com.denfop.integration.botania.TileEntityManasteelSolarPanel;
import com.denfop.integration.botania.TileEntityTerrasteelSolarPanel;
import com.denfop.integration.de.TileEntityAwakenedSolarPanel;
import com.denfop.integration.de.TileEntityChaoticSolarPanel;
import com.denfop.integration.de.TileEntityDraconianSolarPanel;
import com.denfop.integration.thaumcraft.TileEntityThaumSolarPanel;
import com.denfop.integration.thaumcraft.TileEntityVoidSolarPanel;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileEntitySolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityBarionSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityDiffractionSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityGravitonSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityHadronSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityHybridSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityNeutronSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityPhotonicSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityProtonSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityQuantumSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityQuarkSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntitySingularSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntitySpectralSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityUltimateSolarPanel;
import ic2.core.IC2;
import ic2.core.block.generator.tileentity.TileEntitySolarGenerator;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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

public class ItemUpgradePanelKit extends ItemMulti<ItemUpgradePanelKit.Types> implements IModelRegister {

    protected static final String NAME = "upgradekitpanel";

    public ItemUpgradePanelKit() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.UpgradeTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public void registerModels() {
        registerModels(null);
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
        if (!IC2.platform.isSimulating()) {
            return EnumActionResult.PASS;
        } else {
            final EnumActionResult hooks = ForgeHooks.onItemRightClick(player, hand);
            if (hooks == EnumActionResult.FAIL) {
                return hooks;
            }
            ItemStack stack = player.getHeldItem(hand);
            int meta = stack.getItemDamage();
            TileEntity tileEntity = world.getTileEntity(pos);
            if ((tileEntity instanceof TileEntitySolarPanel) && stack.getItemDamage() > 0) {

                TileEntitySolarPanel tile = (TileEntitySolarPanel) tileEntity;
                if (tile.getPanels() == null) {
                    return EnumActionResult.PASS;
                }
                EnumSolarPanels oldpanel = tile.getPanels();
                EnumSolarPanels kit = EnumSolarPanels.getFromID(meta);
                if (!kit.solarold.equals(oldpanel)) {
                    return EnumActionResult.PASS;
                }


                final EnumSolarPanelsKit kit1 = EnumSolarPanelsKit.getFromID(meta - 1);
                world.removeTileEntity(pos);
                world.setBlockToAir(pos);
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


            } else if (tileEntity instanceof TileEntitySolarGenerator) {
                if (stack.getItemDamage() == 0 && world.getTileEntity(pos) instanceof TileEntitySolarGenerator) {
                    EnumSolarPanels kit = EnumSolarPanels.getFromID(meta);
                    world.removeTileEntity(pos);
                    world.setBlockToAir(pos);
                    ItemStack stack1 = new ItemStack(kit.block, 1, kit.meta);

                    EntityItem item = new EntityItem(world);
                    item.setItem(stack1);
                    if (!player.getEntityWorld().isRemote) {
                        item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                        item.setPickupDelay(0);
                        world.spawnEntity(item);

                    }
                    stack.setCount(stack.getCount() - 1);
                    return EnumActionResult.SUCCESS;


                }
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final ItemName name, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    public enum Types implements IIdProvider {
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
        HYBRID(EnumSolarPanels.HYBRID_SOLAR_PANEL, 1, true, new TileEntityHybridSolarPanel()),
        PERFECT(EnumSolarPanels.PERFECT_SOLAR_PANEL, 2, true, new TileEntityUltimateSolarPanel()),
        QUANTUM(EnumSolarPanels.QUANTUM_SOLAR_PANEL, 3, true, new TileEntityQuantumSolarPanel()),
        SPECTRAL(EnumSolarPanels.SPECTRAL_SOLAR_PANEL, 4, true, new TileEntitySpectralSolarPanel()),
        PROTON(EnumSolarPanels.PROTON_SOLAR_PANEL, 5, true, new TileEntityProtonSolarPanel()),
        SINGULAR(EnumSolarPanels.SINGULAR_SOLAR_PANEL, 6, true, new TileEntitySingularSolarPanel()),
        DIFFRACTION(EnumSolarPanels.DIFFRACTION_SOLAR_PANEL, 7, true, new TileEntityDiffractionSolarPanel()),
        PHOTON(EnumSolarPanels.PHOTONIC_SOLAR_PANEL, 8, true, new TileEntityPhotonicSolarPanel()),
        NEUTRONIUM(EnumSolarPanels.NEUTRONIUN_SOLAR_PANEL, 9, true, new TileEntityNeutronSolarPanel()),
        BARION(EnumSolarPanels.BARION_SOLAR_PANEL, 10, true, new TileEntityBarionSolarPanel()),
        HADRON(EnumSolarPanels.HADRON_SOLAR_PANEL, 11, true, new TileEntityHadronSolarPanel()),
        GRAVITON(EnumSolarPanels.GRAVITON_SOLAR_PANEL, 12, true, new TileEntityGravitonSolarPanel()),
        QUARK(EnumSolarPanels.QUARK_SOLAR_PANEL, 13, true, new TileEntityQuarkSolarPanel()),

        DRACONIC(EnumSolarPanels.DRACONIC_SOLAR_PANEL, 14, true, new TileEntityDraconianSolarPanel()),
        AWAKANED(EnumSolarPanels.AWAKENED_SOLAR_PANEL, 15, true, new TileEntityAwakenedSolarPanel()),
        CHAOS(EnumSolarPanels.CHAOTIC_SOLAR_PANEL, 16, true, new TileEntityChaoticSolarPanel()),
        MANASTEEL(EnumSolarPanels.MANASTEEL_SOLAR_PANEL, 17, true, new TileEntityManasteelSolarPanel()),
        ELEMENTUM(EnumSolarPanels.ELEMENTUM_SOLAR_PANEL, 18, true, new TileEntityElementumSolarPanel()),
        TERRASTEEL(EnumSolarPanels.TERRASTEEL_SOLAR_PANEL, 19, true, new TileEntityTerrasteelSolarPanel()),
        NEUTRON_AV(EnumSolarPanels.NEUTRONIUM_SOLAR_PANEL_AVARITIA, 20, true,
                new com.denfop.integration.avaritia.TileEntityNeutronSolarPanel()
        ),
        INFINITY(EnumSolarPanels.INFINITY_SOLAR_PANEL, 21, true, new TileEntityInfinitySolarPanel()),
        THAUM(EnumSolarPanels.THAUM_SOLAR_PANEL, 22, true, new TileEntityThaumSolarPanel()),
        VOID(EnumSolarPanels.VOID_SOLAR_PANEL, 23, true, new TileEntityVoidSolarPanel()),
        ;

        public final int item_meta;
        public final EnumSolarPanels solarpanel_new;
        public final boolean register;
        public final TileEntitySolarPanel tile;

        EnumSolarPanelsKit(EnumSolarPanels solarpanel_new, int item_meta, boolean register, TileEntitySolarPanel tile) {
            this.item_meta = item_meta;
            this.solarpanel_new = solarpanel_new;
            this.register = register;
            this.tile = tile;
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
