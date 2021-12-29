package com.denfop.handler;

import com.denfop.IUItem;
import com.denfop.events.IUEventHandler;
import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientTickHandlerIU {

    public static final Minecraft mc = FMLClientHandler.instance().getClient();
    protected static final RenderItem itemRender = mc.getRenderItem();

    protected static List<ItemStack> getModules(ItemStack item, List<ItemStack> lst) {
        NBTTagCompound nbt = ModUtils.nbt(item);
        if (lst.isEmpty()) {
            lst = new ArrayList<>();
        }
        int aoe = 0;
        int energy = 0;
        int speed = 0;
        int depth = 0;
        int gennight = 0;
        int genday = 0;
        int storage = 0;
        int protect = 0;
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


            switch (nbt.getString("mode_module" + i)) {
                case "vampires":
                    vampires++;
                    break;
                case "resistance":
                    resistance++;
                    break;
                case "wither":
                    wither++;
                    break;
                case "poison":
                    poison++;
                    break;
                case "fire":
                    fire++;
                    break;
                case "loot":
                    loot++;
                    break;
                case "repaired":
                    repaired++;
                    break;
                case "silk":
                    silk = true;
                    break;
                case "invisibility":
                    invisibility = true;
                    break;
                case "saberenergy":
                    saberenergy++;
                    break;
                case "saberdamage":
                    saberdamage++;
                    break;
                case "bowdamage":
                    bowdamage++;
                    break;
                case "bowenergy":
                    bowenergy++;
                    break;
                case "moveSpeed":
                    moveSpeed = true;
                    break;
                case "flyspeed":
                    speedfly++;
                    break;
                case "jump":
                    jump = true;
                    break;
                case "fireResistance":
                    fireResistance = true;
                    break;
                case "waterBreathing":
                    waterBreathing = true;
                    break;
                case "energy":
                    energy++;
                    break;
                case "dig_depth":
                    depth++;
                    break;
                case "AOE_dig":
                    aoe++;
                    break;
                case "speed":
                    speed++;
                    break;
                case "genday":
                    genday++;
                    break;
                case "gennight":
                    gennight++;
                    break;
                case "storage":
                    storage++;
                    break;
                case "protect":
                    protect++;


            }
        }
        if (genday > 0) {
            for (int i = 0; i < genday; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 0));
            }
        }
        if (gennight > 0) {
            for (int i = 0; i < gennight; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 1));
            }
        }
        if (protect > 0) {
            for (int i = 0; i < protect; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 2));
            }
        }
        if (speed > 0) {
            for (int i = 0; i < speed; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 3));
            }
        }
        if (bowenergy > 0) {
            for (int i = 0; i < bowenergy; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 4));
            }
        }
        if (saberenergy > 0) {
            for (int i = 0; i < saberenergy; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 5));
            }
        }
        if (depth > 0) {
            for (int i = 0; i < depth; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 6));
            }
        }
        if (fireResistance) {
            lst.add(new ItemStack(IUItem.upgrademodule, 1, 7));
        }
        if (waterBreathing) {
            lst.add(new ItemStack(IUItem.upgrademodule, 1, 8));
        }
        if (moveSpeed) {
            lst.add(new ItemStack(IUItem.upgrademodule, 1, 9));
        }
        if (jump) {
            lst.add(new ItemStack(IUItem.upgrademodule, 1, 10));
        }
        if (bowdamage > 0) {
            for (int i = 0; i < bowdamage; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 11));
            }
        }
        if (saberdamage > 0) {
            for (int i = 0; i < saberdamage; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 12));
            }
        }
        if (aoe > 0) {
            for (int i = 0; i < aoe; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 13));
            }
        }
        if (speedfly > 0) {
            for (int i = 0; i < speedfly; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 14));
            }
        }
        if (storage > 0) {
            for (int i = 0; i < storage; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 15));
            }
        }
        if (energy > 0) {
            for (int i = 0; i < energy; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 16));
            }
        }
        if (vampires > 0) {
            for (int i = 0; i < vampires; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 17));
            }
        }
        if (resistance > 0) {
            for (int i = 0; i < resistance; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 18));
            }
        }
        if (poison > 0) {
            for (int i = 0; i < poison; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 19));
            }
        }
        if (wither > 0) {
            for (int i = 0; i < wither; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 20));
            }
        }
        if (silk) {
            lst.add(new ItemStack(IUItem.upgrademodule, 1, 21));
        }
        if (invisibility) {
            lst.add(new ItemStack(IUItem.upgrademodule, 1, 22));
        }
        if (loot > 0) {
            for (int i = 0; i < loot; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 23));
            }
        }
        if (fire > 0) {
            for (int i = 0; i < fire; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 24));
            }
        }
        if (repaired > 0) {
            for (int i = 0; i < repaired; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 25));
            }
        }
        return lst;
    }

    protected static List<ItemStack> getModules(ItemStack item) {
        NBTTagCompound nbt = ModUtils.nbt(item);
        List<ItemStack> lst = new ArrayList<>();
        int aoe = 0;
        int energy = 0;
        int speed = 0;
        int depth = 0;
        int gennight = 0;
        int genday = 0;
        int storage = 0;
        int protect = 0;
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
            switch (nbt.getString("mode_module" + i)) {
                case "vampires":
                    vampires++;
                    break;
                case "resistance":
                    resistance++;
                    break;
                case "wither":
                    wither++;
                    break;
                case "poison":
                    poison++;
                    break;
                case "fire":
                    fire++;
                    break;
                case "loot":
                    loot++;
                    break;
                case "repaired":
                    repaired++;
                    break;
                case "silk":
                    silk = true;
                    break;
                case "invisibility":
                    invisibility = true;
                    break;
                case "saberenergy":
                    saberenergy++;
                    break;
                case "saberdamage":
                    saberdamage++;
                    break;
                case "bowdamage":
                    bowdamage++;
                    break;
                case "bowenergy":
                    bowenergy++;
                    break;
                case "moveSpeed":
                    moveSpeed = true;
                    break;
                case "flyspeed":
                    speedfly++;
                    break;
                case "jump":
                    jump = true;
                    break;
                case "fireResistance":
                    fireResistance = true;
                    break;
                case "waterBreathing":
                    waterBreathing = true;
                    break;
                case "energy":
                    energy++;
                    break;
                case "dig_depth":
                    depth++;
                    break;
                case "AOE_dig":
                    aoe++;
                    break;
                case "speed":
                    speed++;
                    break;
                case "genday":
                    genday++;
                    break;
                case "gennight":
                    gennight++;
                    break;
                case "storage":
                    storage++;
                    break;
                case "protect":
                    protect++;


            }
        }
        if (genday > 0) {
            for (int i = 0; i < genday; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 0));
            }
        }
        if (gennight > 0) {
            for (int i = 0; i < gennight; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 1));
            }
        }
        if (protect > 0) {
            for (int i = 0; i < protect; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 2));
            }
        }
        if (speed > 0) {
            for (int i = 0; i < speed; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 3));
            }
        }
        if (bowenergy > 0) {
            for (int i = 0; i < bowenergy; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 4));
            }
        }
        if (saberenergy > 0) {
            for (int i = 0; i < saberenergy; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 5));
            }
        }
        if (depth > 0) {
            for (int i = 0; i < depth; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 6));
            }
        }
        if (fireResistance) {
            lst.add(new ItemStack(IUItem.upgrademodule, 1, 7));
        }
        if (waterBreathing) {
            lst.add(new ItemStack(IUItem.upgrademodule, 1, 8));
        }
        if (moveSpeed) {
            lst.add(new ItemStack(IUItem.upgrademodule, 1, 9));
        }
        if (jump) {
            lst.add(new ItemStack(IUItem.upgrademodule, 1, 10));
        }
        if (bowdamage > 0) {
            for (int i = 0; i < bowdamage; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 11));
            }
        }
        if (saberdamage > 0) {
            for (int i = 0; i < saberdamage; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 12));
            }
        }
        if (aoe > 0) {
            for (int i = 0; i < aoe; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 13));
            }
        }
        if (speedfly > 0) {
            for (int i = 0; i < speedfly; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 14));
            }
        }
        if (storage > 0) {
            for (int i = 0; i < storage; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 15));
            }
        }
        if (energy > 0) {
            for (int i = 0; i < energy; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 16));
            }
        }
        if (vampires > 0) {
            for (int i = 0; i < vampires; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 17));
            }
        }
        if (resistance > 0) {
            for (int i = 0; i < resistance; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 18));
            }
        }
        if (poison > 0) {
            for (int i = 0; i < poison; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 19));
            }
        }
        if (wither > 0) {
            for (int i = 0; i < wither; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 20));
            }
        }
        if (silk) {
            lst.add(new ItemStack(IUItem.upgrademodule, 1, 21));
        }
        if (invisibility) {
            lst.add(new ItemStack(IUItem.upgrademodule, 1, 22));
        }
        if (loot > 0) {
            for (int i = 0; i < loot; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 23));
            }
        }
        if (fire > 0) {
            for (int i = 0; i < fire; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 24));
            }
        }
        if (repaired > 0) {
            for (int i = 0; i < repaired; i++) {
                lst.add(new ItemStack(IUItem.upgrademodule, 1, 25));
            }
        }
        return lst;
    }

    private static Map<Integer, Integer> sort(List<ItemStack> lst) {
        Map<Integer, Integer> list = new HashMap<>();
        for (ItemStack stack : lst) {
            if (!list.containsKey(stack.getItemDamage())) {
                list.put(stack.getItemDamage(), 1);
            } else {
                int count = list.get(stack.getItemDamage());
                list.replace(stack.getItemDamage(), count, count + 1);
            }
        }

        return list;

    }

    public static void onTickRender() {
        final EntityPlayerSP entityClientPlayer = mc.player;
        if (mc.world != null && mc.inGameHasFocus && !mc.gameSettings.showDebugInfo) {
            ItemStack item = entityClientPlayer.getHeldItem(EnumHand.MAIN_HAND);
            int xPos1;
            int yPos1;
            List<Integer> lst1 = new ArrayList<>();
            List<ItemStack> lst = new ArrayList<>();

            for (ItemStack stack : entityClientPlayer.inventory.armorInventory) {
                if (stack != null && IUEventHandler.getUpgradeItem(stack)) {
                    lst = getModules(stack, lst);
                }
            }
            Map<Integer, Integer> map;
            map = sort(lst);
            for (ItemStack stack1 : lst) {
                if (lst1.isEmpty()) {
                    lst1.add(stack1.getItemDamage());
                } else if (!lst1.contains(stack1.getItemDamage())) {
                    lst1.add(stack1.getItemDamage());
                }
            }
            xPos1 = 2;
            yPos1 = 2 + mc.fontRenderer.FONT_HEIGHT;
            int i1 = 0;
            yPos1 += 5;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (!lst.isEmpty()) {
                mc.ingameGUI.drawString(mc.fontRenderer, Localization.translate("description_armor"), xPos1, yPos1 - 6,
                        ModUtils.convertRGBcolorToInt(44, 192, 224)
                );
            }

            if (!lst.isEmpty()) {
                for (int i : lst1) {
                    GL11.glPushMatrix();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                    GlStateManager.disableLighting();
                    GlStateManager.enableDepth();
                    itemRender.zLevel = 100.0F;
                    mc.ingameGUI.drawString(mc.fontRenderer, getDescription(new ItemStack(IUItem.upgrademodule, 1, i), map),
                            xPos1 + 20, yPos1 + 4 + 20 * i1, ModUtils.convertRGBcolorToInt(44, 192, 224)
                    );


                    mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

                    itemRender.renderItemAndEffectIntoGUI(new ItemStack(IUItem.upgrademodule, 1, i), xPos1, yPos1 + 1 + 20 * i1
                    );

                    GL11.glEnable(GL11.GL_LIGHTING);
                    GlStateManager.enableLighting();

                    RenderHelper.enableStandardItemLighting();
                    GL11.glColor4f(0.1F, 1, 0.1F, 1);

                    GL11.glPopMatrix();
                    i1++;
                }
            }
            int size = lst1.size();
            lst.clear();
            if (!item.isEmpty() && IUEventHandler.getUpgradeItem(item)) {
                lst = getModules(item);
            }
            map.clear();
            map = sort(lst);
            lst1.clear();
            for (ItemStack stack1 : lst) {
                if (lst1.isEmpty()) {
                    lst1.add(stack1.getItemDamage());
                } else if (!lst1.contains(stack1.getItemDamage())) {
                    lst1.add(stack1.getItemDamage());
                }
            }
            xPos1 = 2;
            yPos1 = 2 + mc.fontRenderer.FONT_HEIGHT;
            i1 = 0;
            if (!lst1.isEmpty()) {
                mc.ingameGUI.drawString(
                        mc.fontRenderer,
                        Localization.translate("description_item"),
                        xPos1,
                        yPos1 + 2 + 20 * size,
                        ModUtils.convertRGBcolorToInt(44, 192, 224)
                );
            }

            if (!lst1.isEmpty()) {
                for (int i : lst1) {
                    GL11.glPushMatrix();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);


                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                    GlStateManager.disableLighting();
                    GlStateManager.enableDepth();
                    itemRender.zLevel = 100.0F;
                    mc.ingameGUI.drawString(mc.fontRenderer, getDescription(new ItemStack(IUItem.upgrademodule, 1, i), map),
                            xPos1 + 20, yPos1 + 13 + 20 * (i1 + size), ModUtils.convertRGBcolorToInt(44, 192, 224)
                    );

                    mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

                    itemRender.renderItemAndEffectIntoGUI(
                            new ItemStack(IUItem.upgrademodule, 1, i),
                            xPos1,
                            yPos1 + 10 + 20 * (i1 + size)
                    );

                    GL11.glEnable(GL11.GL_LIGHTING);
                    GlStateManager.enableLighting();

                    RenderHelper.enableStandardItemLighting();

                    GL11.glPopMatrix();
                    i1++;


                }
            }
        }


    }

    public static String getDescription(ItemStack stack, Map<Integer, Integer> map) {

        switch (stack.getItemDamage()) {
            case 0:
                return TextFormatting.YELLOW + Localization.translate("genday") + TextFormatting.GREEN + ModUtils.getString(0.05 * map.get(
                        stack.getItemDamage()) * 100) + "%";
            case 1:
                return TextFormatting.AQUA + Localization.translate("gennight") + TextFormatting.GREEN + ModUtils.getString(0.05 * map.get(
                        stack.getItemDamage()) * 100) + "%";
            case 2:
                return TextFormatting.GOLD + Localization.translate("protect") + TextFormatting.GREEN + ModUtils.getString(0.2 * map.get(
                        stack.getItemDamage()) * 100) + "%";
            case 3:
                return TextFormatting.LIGHT_PURPLE + Localization.translate("speed") + TextFormatting.GREEN + ModUtils.getString(
                        0.2 * map.get(stack.getItemDamage()) * 100) + "%";
            case 4:
                return TextFormatting.RED + Localization.translate("bowenergy") + TextFormatting.GREEN + ModUtils.getString(0.1 * map.get(
                        stack.getItemDamage()) * 100) + "%";
            case 5:
                return TextFormatting.RED + Localization.translate("saberenergy") + TextFormatting.GREEN + ModUtils.getString(0.15 * map.get(
                        stack.getItemDamage()) * 100) + "%";
            case 6:
                return TextFormatting.AQUA + Localization.translate("depth") + TextFormatting.GREEN + map.get(stack.getItemDamage());
            case 7:
                return TextFormatting.GOLD + Localization.translate("fireResistance");
            case 8:
                return TextFormatting.GOLD + Localization.translate("waterBreathing");
            case 9:
                return TextFormatting.GOLD + Localization.translate("moveSpeed");
            case 10:
                return TextFormatting.GOLD + Localization.translate("jump");
            case 11:
                return TextFormatting.DARK_GREEN + Localization.translate("bowdamage") + TextFormatting.GREEN + ModUtils.getString(
                        (0.25 * map.get(stack.getItemDamage())) * 100) + "%";
            case 12:
                return TextFormatting.DARK_AQUA + Localization.translate("saberdamage") + TextFormatting.GREEN + ModUtils.getString(
                        0.15 * map.get(stack.getItemDamage()) * 100) + "%";
            case 13:
                return TextFormatting.BLUE + Localization.translate("aoe") + TextFormatting.GREEN + map.get(stack.getItemDamage());
            case 14:
                return TextFormatting.DARK_PURPLE + Localization.translate("speedfly") + TextFormatting.GREEN + ModUtils.getString(
                        (0.1 * map.get(stack.getItemDamage()) / 0.2) * 100) + "%";
            case 15:
                return TextFormatting.BLUE + Localization.translate("storage") + TextFormatting.GREEN + ModUtils.getString(0.05 * map.get(
                        stack.getItemDamage()) * 100) + "%";
            case 16:
                return TextFormatting.RED + Localization.translate("energy_less_use") + TextFormatting.GREEN + ModUtils.getString(
                        0.25 * map.get(stack.getItemDamage()) * 100) + "%";

            case 17:
                return TextFormatting.RED + Localization.translate("vampires") + TextFormatting.GREEN + ModUtils.getString(map.get(
                        stack.getItemDamage()));
            case 18:
                return TextFormatting.GOLD + Localization.translate("resistance") + TextFormatting.GREEN + ModUtils.getString(map.get(
                        stack.getItemDamage()));
            case 19:
                return TextFormatting.GREEN + Localization.translate("poison");
            case 20:
                return TextFormatting.BLUE + Localization.translate("wither");
            case 21:
                return TextFormatting.WHITE + Localization.translate("silk");
            case 22:
                return TextFormatting.WHITE + Localization.translate("invisibility");
            case 23:
                return TextFormatting.WHITE + Localization.translate("loot") + TextFormatting.GREEN + ModUtils.getString(map.get(
                        stack.getItemDamage()));
            case 24:
                return TextFormatting.WHITE + Localization.translate("fire") + TextFormatting.GREEN + ModUtils.getString(map.get(
                        stack.getItemDamage()));
            case 25:
                return TextFormatting.WHITE + Localization.translate("repaired") + TextFormatting.GREEN + 0.001 * map.get(stack.getItemDamage()) + "%";


        }
        return "";

    }


}
