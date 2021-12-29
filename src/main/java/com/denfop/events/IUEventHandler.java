package com.denfop.events;


import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.Recipes;
import com.denfop.blocks.BlockIUFluid;
import com.denfop.items.armour.ItemArmorImprovemedQuantum;
import com.denfop.items.energy.AdvancedMultiTool;
import com.denfop.items.energy.EnergyAxe;
import com.denfop.items.energy.EnergyBow;
import com.denfop.items.energy.EnergyDrill;
import com.denfop.items.energy.EnergyPickaxe;
import com.denfop.items.energy.EnergyShovel;
import com.denfop.items.energy.ItemQuantumSaber;
import com.denfop.items.energy.ItemSpectralSaber;
import com.denfop.items.modules.EnumBaseType;
import com.denfop.items.modules.EnumModule;
import com.denfop.items.modules.ItemBaseModules;
import com.denfop.items.modules.ItemEntityModule;
import com.denfop.utils.CapturedMob;
import com.denfop.utils.EnumInfoUpgradeModules;
import com.denfop.utils.ModUtils;
import ic2.api.item.ElectricItem;
import ic2.api.recipe.RecipeOutput;
import ic2.core.init.Localization;
import ic2.core.util.Util;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
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
        return item instanceof EnergyAxe
                || item instanceof EnergyDrill
                || item instanceof AdvancedMultiTool
                || item instanceof EnergyPickaxe
                || item instanceof EnergyShovel
                || item instanceof ItemArmorImprovemedQuantum
                || item instanceof EnergyBow
                || item instanceof ItemQuantumSaber
                || item instanceof ItemSpectralSaber
                ;

    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onViewRenderFogDensity(EntityViewRenderEvent.FogDensity event) {
        if (!(event.getState().getBlock() instanceof BlockIUFluid)) {
            return;
        }
        event.setCanceled(true);
        Fluid fluid = ((BlockIUFluid) event.getState().getBlock()).getFluid();
        GL11.glFogi(2917, 2048);
        event.setDensity((float) Util.map(Math.abs(fluid.getDensity()), 20000.0D, 2.0D));
    }

    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event) {
        if (event.crafting.isItemEqual(Ic2Items.toolbox)) {
            event.crafting.setItemDamage(5);
        }

    }


    @SubscribeEvent
    public void FlyUpdate(LivingEvent.LivingUpdateEvent event) {

        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();

        if (!player.capabilities.isCreativeMode) {

            NBTTagCompound nbtData = player.getEntityData();
            if (!player.capabilities.isCreativeMode) {
                if (!player.inventory.armorInventory.get(2).isEmpty()) {
                    if (player.inventory.armorInventory
                            .get(2)
                            .getItem() == IUItem.quantumBodyarmor || player.inventory.armorInventory
                            .get(2)
                            .getItem() == IUItem.NanoBodyarmor || player.inventory.armorInventory
                            .get(2)
                            .getItem() == IUItem.perjetpack) {
                        NBTTagCompound nbtData1 = ModUtils.nbt(player.inventory.armorInventory.get(2));
                        boolean jetpack = nbtData1.getBoolean("jetpack");
                        if (!jetpack) {
                            if (nbtData.getBoolean("isFlyActive")) {
                                player.capabilities.isFlying = false;
                                player.capabilities.allowFlying = false;
                                nbtData1.setBoolean("isFlyActive", false);
                                nbtData.setBoolean("isFlyActive", false);
                                if (player.getEntityWorld().isRemote) {
                                    player.capabilities.setFlySpeed((float) 0.05);
                                }
                            }
                        } else {
                            if(!player.onGround) {
                                player.capabilities.isFlying = true;
                                player.capabilities.allowFlying = true;
                                nbtData.setBoolean("isFlyActive", true);
                                nbtData1.setBoolean("isFlyActive", true);
                                int flyspeed = 0;
                                for (int i = 0; i < 4; i++) {
                                    if (nbtData1.getString("mode_module" + i).equals("flyspeed")) {
                                        flyspeed++;
                                    }

                                }
                                flyspeed = Math.min(flyspeed, EnumInfoUpgradeModules.FLYSPEED.max);

                                if (player.getEntityWorld().isRemote) {
                                    player.capabilities.setFlySpeed((float) ((float) 0.1 + 0.05 * flyspeed));
                                }
                            }else{
                                player.capabilities.isFlying = false;
                                player.capabilities.allowFlying = false;
                                if (player.getEntityWorld().isRemote) {
                                    player.capabilities.setFlySpeed((float) 0.05);
                                }
                            }
                        }
                    } else if (player.inventory.armorInventory
                            .get(2)
                            .getItem() != IUItem.quantumBodyarmor && player.inventory.armorInventory
                            .get(2)
                            .getItem() != IUItem.NanoBodyarmor && player.inventory.armorInventory
                            .get(2)
                            .getItem() != IUItem.perjetpack
                            && !player.inventory.armorInventory.get(2).isEmpty()) {
                        if (nbtData.getBoolean("isFlyActive")) {
                            player.capabilities.isFlying = false;
                            player.capabilities.allowFlying = false;
                            nbtData.setBoolean("isFlyActive", false);
                            if (player.getEntityWorld().isRemote) {
                                player.capabilities.setFlySpeed((float) 0.05);
                            }
                        }
                    }
                } else {
                    if (nbtData.getBoolean("isFlyActive")) {
                        player.capabilities.isFlying = false;
                        player.capabilities.allowFlying = false;
                        nbtData.setBoolean("isFlyActive", false);
                        if (player.getEntityWorld().isRemote) {
                            player.capabilities.setFlySpeed((float) 0.05);
                        }
                    }
                }
            } else {
                if (nbtData.getBoolean("isFlyActive")) {
                    player.capabilities.isFlying = false;
                    player.capabilities.allowFlying = false;
                    nbtData.setBoolean("isFlyActive", false);
                    if (player.getEntityWorld().isRemote) {
                        player.capabilities.setFlySpeed((float) 0.05);
                    }
                }
            }
        }

    }


    @SubscribeEvent
    public void UpdateHelmet(LivingEvent.LivingUpdateEvent event) {

        if (!(event.getEntity() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntity();
        NBTTagCompound nbtData = player.getEntityData();
        if (!player.inventory.armorInventory.get(3).isEmpty()) {
            if (player.inventory.armorInventory.get(3).getItem() == IUItem.quantumHelmet) {
                nbtData.setBoolean("isNightVision", true);
            } else if (player.inventory.armorInventory.get(3).getItem() == IUItem.NanoHelmet) {
                nbtData.setBoolean("isNightVision", true);
            } else if (player.inventory.armorInventory.get(3).getItem() == Ic2Items.nanoHelmet.getItem()) {
                nbtData.setBoolean("isNightVision", true);
            } else if (player.inventory.armorInventory.get(3).getItem() == Ic2Items.quantumHelmet.getItem()) {
                nbtData.setBoolean("isNightVision", true);
            } else if (player.inventory.armorInventory.get(3).getItem() == IUItem.advancedSolarHelmet) {
                nbtData.setBoolean("isNightVision", true);
                nbtData.setBoolean("isNightVisionEnable", true);
            } else if (player.inventory.armorInventory.get(3).getItem() == IUItem.hybridSolarHelmet) {
                nbtData.setBoolean("isNightVision", true);
                nbtData.setBoolean("isNightVisionEnable", true);
            } else if (player.inventory.armorInventory.get(3).getItem() == IUItem.spectralSolarHelmet) {
                nbtData.setBoolean("isNightVision", true);
                nbtData.setBoolean("isNightVisionEnable", true);
            } else if (player.inventory.armorInventory.get(3).getItem() == IUItem.singularSolarHelmet) {
                nbtData.setBoolean("isNightVision", true);
                nbtData.setBoolean("isNightVisionEnable", true);
            } else if (player.inventory.armorInventory.get(3).getItem() == Ic2Items.nightvisionGoggles.getItem()) {
                nbtData.setBoolean("isNightVision", true);
            } else if (player.inventory.armorInventory.get(3).getItem() == IUItem.ultimateSolarHelmet) {
                nbtData.setBoolean("isNightVision", true);
                nbtData.setBoolean("isNightVisionEnable", true);
            } else if (nbtData.getBoolean("isNightVision")) {
                nbtData.setBoolean("isNightVision", false);
                nbtData.setBoolean("isNightVisionEnable", false);

            }
        } else if (nbtData.getBoolean("isNightVision")) {
            nbtData.setBoolean("isNightVision", false);
            nbtData.setBoolean("isNightVisionEnable", false);

        }
    }

    public static int floor_double(double p_76128_0_) {
        int i = (int) p_76128_0_;
        return p_76128_0_ < (double) i ? i - 1 : i;
    }

    @SubscribeEvent
    public void UpdateNightVision(LivingEvent.LivingUpdateEvent event) {
        if (Config.nightvision) {
            if (!(event.getEntityLiving() instanceof EntityPlayer)) {
                return;
            }
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            int x = floor_double(player.posX);
            int z = floor_double(player.posZ);
            int y = floor_double(player.posY);
            int skylight = player.getEntityWorld().getLightFromNeighbors(new BlockPos(x, y, z));
            NBTTagCompound nbtData = player.getEntityData();
            if (nbtData.getBoolean("isNightVision")) {
                if (player.posY < 60 || skylight < 8 || !player.getEntityWorld().isDaytime()) {
                    player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0));
                }

            }

        }
    }

    //


    @SubscribeEvent
    public void Potion(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        NBTTagCompound nbtData = player.getEntityData();
        if (!player.inventory.armorInventory.get(0).isEmpty()
                && player.inventory.armorInventory.get(0).getItem() == IUItem.quantumBoots) {
            nbtData.setBoolean("stepHeight", true);
            player.stepHeight = 1.0F;

            nbtData.setBoolean("falldamage", true);
            player.fallDistance = 0;


        } else {
            if (nbtData.getBoolean("stepHeight")) {
                player.stepHeight = 0.5F;
                nbtData.setBoolean("stepHeight", false);
            }
            if (nbtData.getBoolean("falldamage")) {
                player.fallDistance = 1;
                nbtData.setBoolean("falldamage", false);
            }

        }
    }

    @SubscribeEvent
    public void jump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntity();
        if (!player.inventory.armorInventory.get(0).isEmpty()
                && (player.inventory.armorInventory.get(0).getItem() == IUItem.quantumBoots || player.inventory.armorInventory
                .get(0)
                .getItem() == IUItem.NanoLeggings)) {
            player.motionY = 0.8;
            ElectricItem.manager.use(player.inventory.armorInventory.get(0), 4000.0D, player);

        }
    }

    @SubscribeEvent
    public void checkinstruments(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();

        for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
            // TODO start Check inventory
            if (!player.inventory.mainInventory.get(i).isEmpty()
                    && (player.inventory.mainInventory.get(i).getItem() instanceof EnergyAxe
                    || player.inventory.mainInventory.get(i).getItem() instanceof EnergyPickaxe
                    || player.inventory.mainInventory.get(i).getItem() instanceof EnergyShovel)) {
                ItemStack input = player.inventory.mainInventory.get(i);
                NBTTagCompound nbtData = ModUtils.nbt(input);
                if (nbtData.getBoolean("create")) {
                    Map<Enchantment, Integer> enchantmentMap4 = new HashMap<>();

                    if (input.getItem() instanceof EnergyAxe) {
                        EnergyAxe drill = (EnergyAxe) input.getItem();
                        if (Config.enableefficiency) {
                            if( drill.efficienty != 0)
                            enchantmentMap4.put(
                                    Enchantments.EFFICIENCY,
                                    drill.efficienty
                            );
                            if( drill.lucky != 0)
                            enchantmentMap4.put(
                                    Enchantments.FORTUNE,
                                    drill.lucky
                            );
                            nbtData.setBoolean("create", false);
                            EnchantmentHelper.setEnchantments(enchantmentMap4, input);
                        }
                    } else if (input.getItem() instanceof EnergyPickaxe) {
                        EnergyPickaxe drill = (EnergyPickaxe) input.getItem();
                        if (Config.enableefficiency) {
                            if( drill.efficienty != 0)
                            enchantmentMap4.put(
                                    Enchantments.EFFICIENCY,
                                    drill.efficienty
                            );
                            if( drill.lucky != 0)
                            enchantmentMap4.put(
                                    Enchantments.FORTUNE,
                                    drill.lucky
                            );
                            nbtData.setBoolean("create", false);
                            EnchantmentHelper.setEnchantments(enchantmentMap4, input);
                        }
                    } else if (input.getItem() instanceof EnergyShovel) {
                        EnergyShovel drill = (EnergyShovel) input.getItem();
                        if (Config.enableefficiency) {
                            if( drill.efficienty != 0)
                            enchantmentMap4.put(
                                    Enchantments.EFFICIENCY,
                                    drill.efficienty
                            );
                            if( drill.lucky != 0)
                            enchantmentMap4.put(
                                    Enchantments.FORTUNE,
                                    drill.lucky
                            );
                            nbtData.setBoolean("create", false);
                            EnchantmentHelper.setEnchantments(enchantmentMap4, input);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void addInfoforItem(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();


        if (item.equals(IUItem.module_quickly) || item.equals(IUItem.module_stack) || (item.equals(IUItem.module7) && (stack.getItemDamage() == 4 || stack.getItemDamage() == 10))) {
            event.getToolTip().add(Localization.translate("module.wireless"));
        }
        if (item.equals(IUItem.entitymodules) || stack.getItemDamage() == 4) {
            CapturedMob capturedMob = CapturedMob.create(stack);
            if (capturedMob != null) {
                Entity entity = Objects.requireNonNull(capturedMob).getEntity(event.getEntity().getEntityWorld(), true);
                event.getToolTip().add(Objects.requireNonNull(entity).getName());
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void addInfo(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        RecipeOutput output = Recipes.matterrecipe.getOutputFor(stack, false);
        if (stack.getItem().equals(IUItem.plast)) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                event.getToolTip().add(Localization.translate("press.lshift"));
            }


            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {

                event.getToolTip().add(Localization.translate("iu.create_plastic"));
            }
        }
        if (stack.getItem().equals(IUItem.analyzermodule)) {
            event.getToolTip().add(Localization.translate("iu.analyzermodule"));
        }
        if (stack.getItem().equals(IUItem.quarrymodule)) {
            event.getToolTip().add(Localization.translate("iu.quarrymodule"));
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
        if (stack.getItem().equals(IUItem.plastic_plate)) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                event.getToolTip().add(Localization.translate("press.lshift"));
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                event.getToolTip().add(Localization.translate("iu.create_plastic_plate"));
            }
        }
        if (stack.isItemEqual(new ItemStack(IUItem.sunnarium, 1, 4))) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                event.getToolTip().add(Localization.translate("press.lshift"));
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                event.getToolTip().add(Localization.translate("iu.create_sunnarium"));
            }
        }
        if (output != null) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                event.getToolTip().add(Localization.translate("press.lshift"));
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                event.getToolTip().add(Localization.translate("clonning"));
                for (int i = 0; i < this.name.length; i++) {
                    if (output.metadata.getDouble(("quantitysolid_" + i)) != 0) {
                        event.getToolTip().add(name[i] + Localization.translate(mattertype[i]) + ": " + output.metadata.getDouble(
                                ("quantitysolid_" + i)) + Localization.translate("matternumber"));
                    }
                }
            }
        }
        if (getUpgradeItem(stack)) {
            NBTTagCompound nbt = ModUtils.nbt(stack);
            int aoe = 0;
            int energy = 0;
            int speed = 0;
            int depth = 0;
            int gennight = 0;
            int genday = 0;
            int storage = 0;
            int protect = 0;
            int free_slot = 0;
            int speedfly = 0;
            boolean moveSpeed = false;
            boolean jump = false;
            boolean fireResistance = false;
            boolean waterBreathing = false;
            int bowdamage = 0;
            int bowenergy = 0;
            int saberdamage = 0;
            int saberenergy = 0;

            int vampires = 0;
            int resistance = 0;
            int poison = 0;
            int wither = 0;
            int loot = 0;
            int fire = 0;

            int repaired = 0;
            boolean silk = false;
            boolean invisibility = false;
            for (int i = 0; i < 4; i++) {
                if (nbt.getString("mode_module" + i).equals("repaired")) {
                    repaired++;
                }
                if (nbt.getString("mode_module" + i).equals("loot")) {
                    loot++;
                }
                if (nbt.getString("mode_module" + i).equals("fire")) {
                    fire++;
                }

                if (nbt.getString("mode_module" + i).equals("saberenergy")) {
                    saberenergy++;
                }
                if (nbt.getString("mode_module" + i).equals("saberenergy")) {
                    saberenergy++;
                }
                if (nbt.getString("mode_module" + i).equals("invisibility")) {
                    invisibility = true;
                }
                if (nbt.getString("mode_module" + i).equals("silk")) {
                    silk = true;
                }
                if (nbt.getString("mode_module" + i).equals("poison")) {
                    poison++;
                }
                if (nbt.getString("mode_module" + i).equals("wither")) {
                    wither++;
                }
                if (nbt.getString("mode_module" + i).equals("vampires")) {
                    vampires++;
                }
                if (nbt.getString("mode_module" + i).equals("resistance")) {
                    resistance++;
                }
                if (nbt.getString("mode_module" + i).equals("saberdamage")) {
                    saberdamage++;
                }
                if (nbt.getString("mode_module" + i).equals("bowdamage")) {
                    bowdamage++;
                }
                if (nbt.getString("mode_module" + i).equals("bowenergy")) {
                    bowenergy++;
                }
                if (nbt.getString("mode_module" + i).equals("moveSpeed")) {
                    moveSpeed = true;
                }
                if (nbt.getString("mode_module" + i).equals("flyspeed")) {
                    speedfly++;
                }

                if (nbt.getString("mode_module" + i).equals("jump")) {
                    jump = true;
                }
                if (nbt.getString("mode_module" + i).equals("fireResistance")) {
                    fireResistance = true;
                }
                if (nbt.getString("mode_module" + i).equals("waterBreathing")) {
                    waterBreathing = true;
                }
                if (nbt.getString("mode_module" + i).equals("energy")) {
                    energy++;
                }
                if (nbt.getString("mode_module" + i).isEmpty()) {
                    free_slot++;
                }
                if (nbt.getString("mode_module" + i).equals("dig_depth")) {
                    depth++;
                }
                if (nbt.getString("mode_module" + i).equals("AOE_dig")) {
                    aoe++;
                }
                if (nbt.getString("mode_module" + i).equals("speed")) {
                    speed++;
                }
                if (nbt.getString("mode_module" + i).equals("genday")) {
                    genday++;
                }
                if (nbt.getString("mode_module" + i).equals("gennight")) {
                    gennight++;
                }
                if (nbt.getString("mode_module" + i).equals("storage")) {
                    storage++;
                }
                if (nbt.getString("mode_module" + i).equals("protect")) {
                    protect++;
                }
            }

            if (free_slot != 0) {
                event.getToolTip().add(Localization.translate("free_slot") + free_slot + Localization.translate(
                        "free_slot1"));
            } else {
                event.getToolTip().add(Localization.translate("not_free_slot"));

            }
            if (saberenergy != 0) {
                event
                        .getToolTip()
                        .add(TextFormatting.RED + Localization.translate("saberenergy") + TextFormatting.GREEN + ModUtils.getString(
                                0.15 * saberenergy * 100) + "%");
            }


            if (vampires != 0) {
                event
                        .getToolTip()
                        .add(TextFormatting.RED + Localization.translate("vampires") + TextFormatting.GREEN + ModUtils.getString(
                                vampires));
            }
            if (resistance != 0) {
                event
                        .getToolTip()
                        .add(TextFormatting.GOLD + Localization.translate("resistance") + TextFormatting.GREEN + ModUtils.getString(
                                resistance));
            }
            if (wither != 0) {
                event.getToolTip().add(TextFormatting.BLUE + Localization.translate("wither"));
            }
            if (poison != 0) {
                event.getToolTip().add(TextFormatting.GREEN + Localization.translate("poison"));
            }
            if (invisibility) {
                event.getToolTip().add(TextFormatting.WHITE + Localization.translate("invisibility"));
            }
            if (repaired != 0) {
                event
                        .getToolTip()
                        .add(TextFormatting.WHITE + Localization.translate("repaired") + TextFormatting.GREEN + 0.001 * repaired + "%");
            }
            if (loot != 0) {
                event
                        .getToolTip()
                        .add(TextFormatting.WHITE + Localization.translate("loot") + TextFormatting.GREEN + ModUtils.getString(
                                loot));
            }
            if (fire != 0) {
                event
                        .getToolTip()
                        .add(TextFormatting.WHITE + Localization.translate("fire") + TextFormatting.GREEN + ModUtils.getString(
                                fire));
            }
            if (silk) {
                event.getToolTip().add(TextFormatting.WHITE + Localization.translate("silk"));
            }


            if (saberdamage != 0) {
                event
                        .getToolTip()
                        .add(TextFormatting.DARK_AQUA + Localization.translate("saberdamage") + TextFormatting.GREEN + ModUtils.getString(
                                0.15 * saberdamage * 100) + "%");
            }


            if (bowenergy != 0) {
                event
                        .getToolTip()
                        .add(TextFormatting.RED + Localization.translate("bowenergy") + TextFormatting.GREEN + ModUtils.getString(
                                0.1 * bowenergy * 100) + "%");
            }


            if (bowdamage != 0) {
                event
                        .getToolTip()
                        .add(TextFormatting.DARK_GREEN + Localization.translate("bowdamage") + TextFormatting.GREEN + ModUtils.getString(
                                (0.25 * bowdamage) * 100) + "%");
            }


            if (speedfly != 0) {
                event
                        .getToolTip()
                        .add(TextFormatting.DARK_PURPLE + Localization.translate("speedfly") + TextFormatting.GREEN + ModUtils.getString(
                                (0.1 * speedfly / 0.2) * 100) + "%");
            }


            if (waterBreathing) {
                event.getToolTip().add(TextFormatting.GOLD + Localization.translate("waterBreathing"));
            }

            if (fireResistance) {
                event.getToolTip().add(TextFormatting.GOLD + Localization.translate("fireResistance"));
            }

            if (jump) {
                event.getToolTip().add(TextFormatting.GOLD + Localization.translate("jump"));
            }

            if (moveSpeed) {
                event.getToolTip().add(TextFormatting.GOLD + Localization.translate("moveSpeed"));
            }

            if (energy != 0) {
                event
                        .getToolTip()
                        .add(TextFormatting.RED + Localization.translate("energy_less_use") + TextFormatting.GREEN + ModUtils.getString(
                                0.25 * energy * 100) + "%");
            }

            if (depth != 0) {
                event.getToolTip().add(TextFormatting.AQUA + Localization.translate("depth") + TextFormatting.GREEN + depth);
            }

            if (aoe != 0) {
                event.getToolTip().add(TextFormatting.BLUE + Localization.translate("aoe") + TextFormatting.GREEN + aoe);
            }

            if (speed != 0) {
                event
                        .getToolTip()
                        .add(TextFormatting.LIGHT_PURPLE + Localization.translate("speed") + TextFormatting.GREEN + ModUtils.getString(
                                0.2 * speed * 100) + "%");
            }


            if (genday != 0) {
                event
                        .getToolTip()
                        .add(TextFormatting.YELLOW + Localization.translate("genday") + TextFormatting.GREEN + ModUtils.getString(
                                0.05 * genday * 100) + "%");
            }

            if (gennight != 0) {
                event
                        .getToolTip()
                        .add(TextFormatting.AQUA + Localization.translate("gennight") + TextFormatting.GREEN + ModUtils.getString(
                                0.05 * gennight * 100) + "%");
            }

            if (storage != 0) {
                event
                        .getToolTip()
                        .add(TextFormatting.BLUE + Localization.translate("storage") + TextFormatting.GREEN + ModUtils.getString(
                                0.05 * storage * 100) + "%");
            }

            if (protect != 0) {
                event
                        .getToolTip()
                        .add(TextFormatting.GOLD + Localization.translate("protect") + TextFormatting.GREEN + ModUtils.getString(
                                0.2 * protect * 100) + "%");
            }

        }
    }

    @SubscribeEvent
    public void check(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        for (int i = 0; i < 36; i++) {
            if (!player.inventory.mainInventory.get(i).isEmpty()
                    && player.inventory.mainInventory.get(i).isItemEqual(Ic2Items.toolbox))
                player.inventory.mainInventory.get(i).setItemDamage(5);

            if (!player.inventory.mainInventory.get(i).isEmpty()
                    && player.inventory.mainInventory.get(i).getItem().equals(Ic2Items.cesuUnit.getItem())) {
                int meta = player.inventory.mainInventory.get(i).getItemDamage();
                if (meta == Ic2Items.batBox.getItemDamage()) {
                    player.inventory.mainInventory.set(i, new ItemStack(IUItem.electricblock, 1, 2));
                }
                if (meta == Ic2Items.cesuUnit.getItemDamage()) {
                    player.inventory.mainInventory.set(i, new ItemStack(IUItem.electricblock, 1, 5));
                }
                if (meta == Ic2Items.mfeUnit.getItemDamage()) {
                    player.inventory.mainInventory.set(i, new ItemStack(IUItem.electricblock, 1, 3));

                }
                if (meta == Ic2Items.mfsUnit.getItemDamage()) {
                    player.inventory.mainInventory.set(i, new ItemStack(IUItem.electricblock, 1, 4));

                }
                 if (meta == Ic2Items.ChargepadbatBox.getItemDamage()) {
                    player.inventory.mainInventory.set(i, new ItemStack(IUItem.Chargepadelectricblock, 1, 2));
                }
                if (meta == Ic2Items.ChargepadcesuUnit.getItemDamage()) {
                    player.inventory.mainInventory.set(i, new ItemStack(IUItem.Chargepadelectricblock, 1, 3));
                }
                if (meta == Ic2Items.ChargepadmfeUnit.getItemDamage()) {
                    player.inventory.mainInventory.set(i, new ItemStack(IUItem.Chargepadelectricblock, 1, 4));

                }
                if (meta == Ic2Items.ChargepadmfsUnit.getItemDamage()) {
                    player.inventory.mainInventory.set(i, new ItemStack(IUItem.Chargepadelectricblock, 1, 5));

                }
            }

        }
    }

    @SubscribeEvent
    public void checkdrill(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
            // TODO start Check inventory
            if (!player.inventory.mainInventory.get(i).isEmpty()
                    && (player.inventory.mainInventory.get(i).getItem() == IUItem.ultDDrill)) {
                ItemStack input = player.inventory.mainInventory.get(i);
                NBTTagCompound nbtData = ModUtils.nbt(input);
                if (nbtData.getBoolean("create")) {
                    Map<Enchantment, Integer> enchantmentMap4 = new HashMap<>();
                    if (Config.enableefficiency) {
                        enchantmentMap4.put(
                                Enchantments.EFFICIENCY,
                                Config.efficiencylevel
                        );
                        nbtData.setBoolean("create", false);
                        EnchantmentHelper.setEnchantments(enchantmentMap4, input);
                    }

                }
            }
        }
    }

    @SubscribeEvent
    public void falling(LivingFallEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntity();
        if (!player.inventory.armorInventory.get(0).isEmpty()
                && (player.inventory.armorInventory.get(0).getItem() == IUItem.quantumBoots || player.inventory.armorInventory
                .get(0)
                .getItem() == IUItem.NanoLeggings)) {
            if (ElectricItem.manager.canUse(player.inventory.armorInventory.get(0), 500.0D)) {
                ElectricItem.manager.use(player.inventory.armorInventory.get(0), 500.0D, player);
            } else {
                ElectricItem.manager.use(player.inventory.armorInventory.get(0),
                        ElectricItem.manager.getCharge(player.inventory.armorInventory.get(0)), player
                );

            }

        }
    }
    //

}
