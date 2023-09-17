package com.denfop.events;


import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IItemSoon;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeItemInform;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.blocks.BlockIUFluid;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerBags;
import com.denfop.container.ContainerLeadBox;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.armour.ItemAdvJetpack;
import com.denfop.items.armour.special.EnumCapability;
import com.denfop.items.armour.special.ItemSpecialArmor;
import com.denfop.items.bags.ItemEnergyBags;
import com.denfop.items.bags.ItemLeadBox;
import com.denfop.items.block.ItemBlockIU;
import com.denfop.items.modules.EnumBaseType;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.items.modules.ItemEntityModule;
import com.denfop.items.reactors.IRadioactiveItemType;
import com.denfop.items.resource.ItemNuclearResource;
import com.denfop.network.WorldData;
import com.denfop.network.packet.PacketColorPickerAllLoggIn;
import com.denfop.tiles.transport.tiles.TileEntityCoolPipes;
import com.denfop.tiles.transport.tiles.TileEntityExpPipes;
import com.denfop.tiles.transport.tiles.TileEntityHeatColdPipes;
import com.denfop.tiles.transport.tiles.TileEntityHeatPipes;
import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import com.denfop.tiles.transport.tiles.TileEntityQCable;
import com.denfop.tiles.transport.tiles.TileEntityUniversalCable;
import com.denfop.utils.CapturedMobUtils;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class IUEventHandler {

    final TextFormatting[] name = {TextFormatting.DARK_PURPLE, TextFormatting.YELLOW, TextFormatting.BLUE,
            TextFormatting.RED, TextFormatting.GRAY, TextFormatting.GREEN, TextFormatting.DARK_AQUA, TextFormatting.AQUA};
    final String[] mattertype = {"matter.name", "sun_matter.name", "aqua_matter.name", "nether_matter.name", "night_matter.name", "earth_matter.name", "end_matter.name", "aer_matter.name"};

    public IUEventHandler() {
    }

    public static boolean getUpgradeItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof IUpgradeItem;

    }

    @SubscribeEvent
    public void loginPlayer(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player.getEntityWorld().isRemote) {
            return;
        }
        RadiationSystem.rad_system.update(event.player);
    }

    @SubscribeEvent
    public void workCutters(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote) {
            return;
        }
        ItemStack stack = event.getItemStack();
        if (stack.getItem() == IUItem.cutter) {

            final TileEntity tile = event.getWorld().getTileEntity(event.getPos());
            if (tile instanceof TileEntityMultiCable) {
                TileEntityMultiCable cable = (TileEntityMultiCable) tile;
                final List<ItemStack> drops = tile.getBlockType().getDrops(event.getWorld(), tile.getPos(), cable.getBlockState(),
                        100
                );
                if (!drops.isEmpty()) {
                    ModUtils.dropAsEntity(event.getWorld(), event.getPos(), drops.get(0));
                }
                cable.removeConductor();
            }
        }
    }

    @SubscribeEvent
    public void initiatePlayer(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player.getEntityWorld().isRemote) {
            return;
        }
        new PacketColorPickerAllLoggIn();
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        WorldData.onWorldUnload(event.getWorld());
    }


    @SubscribeEvent
    public void bag_pickup(EntityItemPickupEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        try {
            boolean isContainerBags = !(player.openContainer instanceof ContainerBags);
            boolean isContainerBox = !(player.openContainer instanceof ContainerLeadBox);

            if (isContainerBags) {
                InventoryPlayer inventory = player.inventory;

                for (int i = 0; i < inventory.mainInventory.size(); ++i) {
                    ItemStack stack = inventory.mainInventory.get(i);
                    if (stack.getItem() instanceof ItemEnergyBags) {
                        if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.BAGS, stack)) {
                            ItemEnergyBags bags = (ItemEnergyBags) stack.getItem();
                            if (!(event.getItem().getItem().getItem() instanceof ItemEnergyBags)) {
                                if (bags.canInsert(player, stack, event.getItem().getItem())) {
                                    bags.insert(player, stack, event.getItem().getItem());
                                    event.getItem().getItem().setCount(0);
                                    return;
                                }
                            }
                        }
                    }
                }

            }
            if (isContainerBox) {
                InventoryPlayer inventory = player.inventory;
                for (int i = 0; i < inventory.mainInventory.size(); ++i) {
                    ItemStack stack = inventory.mainInventory.get(i);
                    if (stack.getItem() instanceof ItemLeadBox) {
                        ItemLeadBox bags = (ItemLeadBox) stack.getItem();
                        if (!(event.getItem().getItem().getItem() instanceof ItemLeadBox)) {
                            if (bags.canInsert(player, stack, event.getItem().getItem())) {
                                bags.insert(player, stack, event.getItem().getItem());
                                event.getItem().getItem().setCount(0);
                                return;
                            }
                        }

                    }
                }

            }
        } catch (NoClassDefFoundError ignored) {

        }
    }


    public void setFly(EntityPlayer player, boolean fly, ItemStack stack) {
        player.capabilities.isFlying = fly;
        player.capabilities.allowFlying = fly;
        player.getEntityData().setBoolean("isFlyActive", fly);
        if (player.getEntityWorld().isRemote && !fly) {
            player.capabilities.setFlySpeed((float) 0.05);
            player.fallDistance = 0;
        } else {
            boolean edit = player.getEntityData().getBoolean("edit_fly");
            if (!edit) {
                int flyspeed = (UpgradeSystem.system.hasModules(
                        EnumInfoUpgradeModules.FLYSPEED,
                        stack
                ) ?
                        UpgradeSystem.system.getModules(
                                EnumInfoUpgradeModules.FLYSPEED,
                                stack
                        ).number : 0);

                if (player.getEntityWorld().isRemote) {
                    player.capabilities.setFlySpeed((float) ((float) 0.1 + 0.05 * flyspeed));
                }
            } else {
                if (player.getEntityWorld().isRemote) {
                    player.capabilities.setFlySpeed(player.getEntityData().getFloat("fly_speed"));
                }
            }
        }
    }

    public boolean canFly(ItemStack stack) {
        return stack
                .getItem() == IUItem.spectral_chestplate ||
                (stack.getItem() instanceof ItemSpecialArmor && (((ItemSpecialArmor) stack.getItem())
                        .getListCapability()
                        .contains(EnumCapability.FLY) || ((ItemSpecialArmor) stack.getItem()).getListCapability().contains(
                        EnumCapability.JETPACK_FLY))) || (stack
                .getItem() instanceof ItemAdvJetpack && UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.FLY, stack));
    }

    @SubscribeEvent
    public void FlyUpdate(LivingEvent.LivingUpdateEvent event) {

        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        NBTTagCompound nbtData = player.getEntityData();
        if (!player.capabilities.isCreativeMode) {

            if (!player.inventory.armorInventory.get(2).isEmpty()) {
                if (canFly(player.inventory.armorInventory.get(2))) {
                    NBTTagCompound nbtData1 = ModUtils.nbt(player.inventory.armorInventory.get(2));
                    boolean jetpack = nbtData1.getBoolean("jetpack");
                    if (!jetpack) {
                        if (nbtData.getBoolean("isFlyActive")) {
                            nbtData.setBoolean("hasFly", true);
                            setFly(player, false, player.inventory.armorInventory
                                    .get(2));
                        }
                    } else {
                        if (!player.onGround) {
                            if (nbtData1.getBoolean("canFly")) {
                                setFly(player, true, player.inventory.armorInventory
                                        .get(2));
                                nbtData1.setBoolean("canFly", false);
                                nbtData.setBoolean("canjump", false);
                            }
                        } else {
                            if (nbtData.getBoolean("isFlyActive")) {
                                setFly(player, false, player.inventory.armorInventory
                                        .get(2));
                            }
                        }
                    }
                } else if (!player.inventory.armorInventory.get(2).isEmpty()) {
                    if (nbtData.getBoolean("isFlyActive")) {
                        setFly(player, false, player.inventory.armorInventory
                                .get(2));
                    }
                }
            } else {
                if (nbtData.getBoolean("isFlyActive")) {
                    setFly(player, false, player.inventory.armorInventory
                            .get(2));
                }
            }
        } else {
            if (nbtData.getBoolean("isFlyActive")) {
                setFly(player, false, player.inventory.armorInventory
                        .get(2));
            }
        }


    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void addInformItem(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }

        if (item instanceof IItemSoon) {
            event.getToolTip().add(((IItemSoon) item).getDescription());
        }

        if (item instanceof IRadioactiveItemType || item instanceof ItemNuclearResource) {
            event.getToolTip().add(Localization.translate("iu.radiation.warning"));
        }

        if (item.equals(Item.getItemFromBlock(IUItem.tank))) {
            switch (stack.getItemDamage()) {
                case 1:
                    event.getToolTip().add(Localization.translate("iu.storage_fluid") + 160 + " B");
                    break;
                case 2:
                    event.getToolTip().add(Localization.translate("iu.storage_fluid") + 480 + " B");
                    break;
                case 3:
                    event.getToolTip().add(Localization.translate("iu.storage_fluid") + 2560 + " B");
                    break;
                case 0:
                    event.getToolTip().add(Localization.translate("iu.storage_fluid") + 40 + " B");
                    break;

            }
        }
        if (item.equals(IUItem.module_quickly)) {
            event.getToolTip().add(Localization.translate("iu.info.module.speed"));
        }
        if (item.equals(IUItem.module_stack)) {
            event.getToolTip().add(Localization.translate("iu.info.module.stack"));
        }
        if (item.equals(IUItem.module_storage)) {
            event.getToolTip().add(Localization.translate("iu.info.module.sorting"));
        }
        if (item.equals(IUItem.crafting_elements) && stack.getItemDamage() >= 206 && stack.getItemDamage() <= 216) {
            int meta = stack.getItemDamage() - 205;
            event
                    .getToolTip()
                    .add(Localization.translate("iu.limiter.info9") + EnergyNetGlobal.instance.getPowerFromTier(meta) + " " +
                            "EF");
        }
        if (item.equals(IUItem.module7) && stack.getItemDamage() == 9) {
            event.getToolTip().add(Localization.translate("module.wireless"));
        }
        if (item.equals(IUItem.upgrade_speed_creation) || item.equals(IUItem.autoheater) || item.equals(IUItem.coolupgrade) || item.equals(
                IUItem.module_quickly) || item.equals(
                IUItem.module_stack) || item.equals(IUItem.module_storage) || (item.equals(
                IUItem.module7) && (stack.getItemDamage() == 4 || stack.getItemDamage() == 10))) {
            event.getToolTip().add(Localization.translate("module.wireless"));
        }
        if (item.equals(IUItem.entitymodules) || stack.getItemDamage() == 4) {
            CapturedMobUtils capturedMobUtils = CapturedMobUtils.create(stack);
            if (capturedMobUtils != null) {
                Entity entity = Objects.requireNonNull(capturedMobUtils).getEntity(event.getEntity().getEntityWorld(), true);
                event.getToolTip().add(Objects.requireNonNull(entity).getName());
            }
        }
        if (item instanceof ItemBlockIU) {
            ItemBlockIU itemBlockTileEntity = (ItemBlockIU) item;
            if (itemBlockTileEntity.getBlock() instanceof BlockIUFluid) {
                BlockIUFluid blockFluid = (BlockIUFluid) itemBlockTileEntity.getBlock();
                if (blockFluid.getFluid() == FluidName.fluidgas.getInstance()) {
                    event.getToolTip().add(Localization.translate("find.gas_vein"));
                }
                if (blockFluid.getFluid() == FluidName.fluidneft.getInstance()) {
                    event.getToolTip().add(Localization.translate("find.oil_vein"));
                }

            }
        }
        if (item.equals(
                IUItem.module_quickly) || item.equals(
                IUItem.module_stack) || item.equals(IUItem.module_storage)) {
            event.getToolTip().add(Localization.translate("using_kit"));
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {

                event.getToolTip().add(ListInformationUtils.mechanism_info.get(ListInformationUtils.index));

            } else {
                for (String name : ListInformationUtils.mechanism_info) {
                    event.getToolTip().add(name);
                }
            }

        }
        if (item.equals(
                IUItem.coolupgrade)) {
            event.getToolTip().add(Localization.translate("using_kit"));
            final List<String> list = ListInformationUtils.integerListMap.get(stack.getItemDamage());
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                event.getToolTip().add(list.get(ListInformationUtils.index % list.size()));
            } else {
                for (String name : list) {
                    event.getToolTip().add(name);
                }
            }

        }
        if (item.equals(IUItem.autoheater)) {
            event.getToolTip().add(Localization.translate("using_kit"));

            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                event.getToolTip().add(ListInformationUtils.mechanism_info2.get(ListInformationUtils.index2));
            } else {
                for (String name : ListInformationUtils.mechanism_info2) {
                    event.getToolTip().add(name);
                }
            }
        }
        if (item.equals(
                IUItem.upgrade_speed_creation)) {
            event.getToolTip().add(Localization.translate("using_kit"));
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                event.getToolTip().add(ListInformationUtils.mechanism_info1.get(ListInformationUtils.index1));
            } else {
                for (String name : ListInformationUtils.mechanism_info1) {
                    event.getToolTip().add(name);
                }
            }
        }
        if (stack.getItem().equals(IUItem.analyzermodule)) {
            event.getToolTip().add(Localization.translate("iu.analyzermodule"));
        }
        if (stack.getItem().equals(IUItem.quarrymodule)) {
            event.getToolTip().add(Localization.translate("iu.quarrymodule"));
        }
        if (stack.getItem().equals(IUItem.expmodule)) {
            event.getToolTip().add(Localization.translate("iu.expierence_module.info"));
        }

        if (stack.getItem() instanceof ItemEntityModule) {
            int meta = stack.getItemDamage();
            if (meta == 0) {
                event.getToolTip().add(Localization.translate("iu.entitymodule"));
            }
            if (meta == 1) {
                event.getToolTip().add(Localization.translate("iu.entitymodule1"));
            }

        }

        if (stack.getItem() instanceof ItemBaseModules) {
            EnumModule module = EnumModule.getFromID(stack.getItemDamage());
            if (module.type.equals(EnumBaseType.PHASE)) {
                event.getToolTip().add(Localization.translate("iu.phasemodule"));

            }
            if (module.type.equals(EnumBaseType.MOON_LINSE)) {
                event.getToolTip().add(Localization.translate("iu.moonlinse"));

            }

        }
        if (getUpgradeItem(stack) && UpgradeSystem.system.hasInMap(stack)) {
            final List<UpgradeItemInform> lst = UpgradeSystem.system.getInformation(stack);
            final int col = UpgradeSystem.system.getRemaining(stack);
            if (!lst.isEmpty()) {
                for (UpgradeItemInform upgrade : lst) {
                    event.getToolTip().add(upgrade.getName());
                }
            }
            if (col != 0) {
                event.getToolTip().add(Localization.translate("free_slot") + col + Localization.translate(
                        "free_slot1"));
            } else {
                event.getToolTip().add(Localization.translate("not_free_slot"));

            }


        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void addInfo(ItemTooltipEvent event) {

        ItemStack stack = event.getItemStack();


        for (Map.Entry<ItemStack, BaseMachineRecipe> entry : IUItem.machineRecipe) {
            if (entry.getKey().isItemEqual(stack)) {
                if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    event.getToolTip().add(Localization.translate("press.lshift"));
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    event.getToolTip().add(Localization.translate("clonning"));

                    final RecipeOutput output1 = entry.getValue().output;
                    for (int i = 0; i < this.name.length; i++) {
                        if (output1.metadata.getDouble(("quantitysolid_" + i)) != 0) {
                            event
                                    .getToolTip()
                                    .add(name[i] + Localization.translate(mattertype[i]) + ": " + output1.metadata.getDouble(
                                            ("quantitysolid_" + i)) + Localization.translate("matternumber"));
                        }
                    }
                }
                break;
            }
        }
        for (Map.Entry<ItemStack, BaseMachineRecipe> entry : IUItem.fluidMatterRecipe) {
            if (entry.getKey().isItemEqual(stack)) {
                if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (!event.getToolTip().contains(Localization.translate("press.lshift"))) {
                        event.getToolTip().add(Localization.translate("press.lshift"));
                    }
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {

                    final RecipeOutput output1 = entry.getValue().output;
                    final double matter = output1.metadata.getDouble("matter");
                    String usingMatter = ModUtils.getStringBukket(matter) + Localization.translate(Constants.ABBREVIATION +
                            ".generic.text.bucketUnit");
                    event
                            .getToolTip()
                            .add(Localization.translate(Constants.ABBREVIATION + ".replicator_using_matter") + TextFormatting.DARK_PURPLE + usingMatter);

                }
                break;
            }
        }
    }

}
