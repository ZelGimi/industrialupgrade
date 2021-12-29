package com.denfop.utils;

import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.denfop.Constants;
import com.denfop.IUCore;
import ic2.core.init.Localization;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.FMLRelaunchLog;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModUtils {

    public static List<ItemStack> blacklist_block() {
        List<ItemStack> list = new ArrayList<>();
        list.add( new ItemStack(Blocks.STONE));
        list.add( new ItemStack(Blocks.DIRT));
        list.add( new ItemStack(Blocks.NETHERRACK));
        list.add( new ItemStack(Blocks.END_STONE));
        list.add( new ItemStack(Blocks.STONE,1,1));
        list.add( new ItemStack(Blocks.STONE,1,2));
        list.add( new ItemStack(Blocks.STONE,1,3));
        list.add( new ItemStack(Blocks.STONE,1,4));
        list.add( new ItemStack(Blocks.STONE,1,5));
        list.add( new ItemStack(Blocks.STONE,1,6));
        list.add( new ItemStack(Blocks.DIRT,1,1));
        list.add( new ItemStack(Blocks.DIRT,1,2));
        list.add( new ItemStack(Blocks.DIRT,1,3));
        return list;
    }

    public static boolean getore(Block localBlock, int meta) {
        ItemStack stack = new ItemStack(localBlock,1,meta);
        for (ItemStack itemstack : blacklist_block()) {
            if (stack.isItemEqual(itemstack)) {
                return false;
            }
        }
        return true;
    }

    public static boolean getore(Item localBlock) {
        for (ItemStack itemstack : blacklist_block()) {
            if (localBlock == itemstack.getItem()) {
                return false;
            }
        }
        return true;
    }
    public static boolean getore(ItemStack localBlock) {
        for (ItemStack itemstack : blacklist_block()) {
            if (localBlock.isItemEqual(itemstack)) {
                return false;
            }
        }
        return true;
    }
    public static boolean getore(Block stack, Block localBlock) {
        ItemStack stack1 = new ItemStack(localBlock);
        for (ItemStack itemstack : blacklist_block()) {
            if (stack1.isItemEqual(itemstack)) {
                return false;
            }
        }
        if (stack != localBlock) {
            return false;
        }
        for (ItemStack itemstack : IUCore.get_ore) {
            if (stack == Block.getBlockFromItem(itemstack.getItem())) {
                return true;
            }
        }
        return false;

    }

    public static List<Double> Time(double time) {
        List<Double> list = new ArrayList<>();

        double temp = 0;

        if (time / 3600 >= 1) {
            temp = (time / (double) 3600);
        }
        temp = Math.floor(temp);
        list.add(Math.floor(temp));
        double temp1 = 0;


        if (((time - temp * 3600) / 60) >= 1) {
            temp1 = ((time - temp * 3600) / 60);
        }
        temp1 = Math.floor(temp1);
        list.add(Math.floor(temp1));
        double temp2;

        temp2 = (time - (temp * 3600 + temp1 * 60));

        list.add(Math.floor(temp2));
        return list;
    }

    public static void NBTSetString(ItemStack stack, String name, String string) {
        if (string == null) {
            return;
        }
        NBTTagCompound NBTTagCompound = stack.getTagCompound();
        if (NBTTagCompound == null) {
            NBTTagCompound = new NBTTagCompound();
        }
        NBTTagCompound.setString(name, string);
        stack.setTagCompound(NBTTagCompound);
    }

    public static int getsum1(List<Integer> sum) {
        int sum_sum = 0;
        for (Integer aDouble : sum) {
            sum_sum += aDouble;
        }
        return sum_sum;
    }

    public static boolean Boolean(List<Boolean> boolean1) {

        for (Boolean aBoolean : boolean1) {
            if (aBoolean) {
                return true;
            }
        }

        return false;
    }

    public static void info(String message) {
        FMLRelaunchLog.log(Constants.MOD_NAME, Level.INFO, message);
    }

    public static void SetDoubleWithoutItem(NBTTagCompound NBTTagCompound, String name, double amount) {
        if (NBTTagCompound == null)
            NBTTagCompound = new NBTTagCompound();
        NBTTagCompound.setDouble(name, amount);

    }

    public static double NBTGetDouble(ItemStack stack, String name) {
        if (name == null)
            return 0;
        NBTTagCompound NBTTagCompound = stack.getTagCompound();
        if (NBTTagCompound == null)
            return 0;
        return NBTTagCompound.getDouble(name);

    }
    public static String Boolean(boolean boolean1) {
        if (boolean1) {
            return Localization.translate("iu.yes");
        } else {
            return Localization.translate("iu.no");
        }

    }
    public static void mode(ItemStack stack, List<String> list) {
        NBTTagCompound nbt = nbt(stack);
        String mode = nbt.getString("mode");
        if (mode.isEmpty())
            list.add(Localization.translate("defaultskin"));
        switch (mode) {
            case "Zelen":
                list.add(Localization.translate("camouflageskin"));
                break;
            case "Demon":
                list.add(Localization.translate("demonskin"));
                break;
            case "Dark":
                list.add(Localization.translate("Darkskin"));
                break;
            case "Cold":
                list.add(Localization.translate("Coldskin"));
                break;
            case "Ender":
                list.add(Localization.translate("Enderskin"));
                break;
        }
    }

    public String getEntityString(ItemStack stack) {
        return ItemNBTHelper.getString(stack, "EntityName", "Pig");
    }

    @Nullable
    public NBTTagCompound getEntityData(ItemStack stack) {
        NBTTagCompound compound = ItemNBTHelper.getCompound(stack);
        return compound.hasKey("EntityData") ? compound.getCompoundTag("EntityData") : null;
    }


    public static String getString(float number) {
        float g = number;
        float gg;
        int i = 0;
        for (; g >= 10; i++) {
            g = g / 10;

        }
        String maxstorage_2 = "0";
        if (i >= 0 && i < 3 && number <= 1000) {

            gg = number;
            maxstorage_2 = String.format("%.0f", gg);
        } else if (i >= 3 && i < 6 && number >= 1000 && number < 1000000) {
            gg = number / (1000);
            maxstorage_2 = String.format("%.2fK", gg);
        } else if (i >= 6 && i < 9 && number >= 1000000 && number < 1000000000) {
            gg = number / (1000000);
            maxstorage_2 = String.format("%.2fM", gg);
        } else if (i >= 9 && i < 12 && number >= 1000000000 && number < 2100000000) {
            gg = number / (1000000000);
            maxstorage_2 = String.format("%.2fG", gg);
        }
        return maxstorage_2;

    }

    public static String getString1(double number) {
        String maxstorage_2 = "0";
        if (number <= 1000) {

            maxstorage_2 = String.format("%.2f", number);
        } else if (number >= 10E2D && number < 10E5D) {

            maxstorage_2 = String.format("%.0fK", number / 10E2D);
        }
        return maxstorage_2;
    }

    public static String getString(double number) {
        String maxstorage_2 = "0";
        if (number <= 1000) {

            maxstorage_2 = String.format("%.0f", number);
        } else if (number >= 10E2D && number < 10E5D) {

            maxstorage_2 = String.format("%.2fK", number / 10E2D);
        } else if (number >= 10E5D && number < 10E8D) {

            maxstorage_2 = String.format("%.2fM", number / 10E5D);
        } else if (number >= 10E8D && number < 10E11D) {

            maxstorage_2 = String.format("%.2fG", number / 10E8D);
        } else if (number >= 10E11D && number < 10E14D) {

            maxstorage_2 = String.format("%.2fT", number / 10E11D);
        } else if (number >= 10E14D && number < 10E17D) {

            maxstorage_2 = String.format("%.2fP", number / 10E14D);
        } else if (number >= 10E17D && number < 10E20D) {

            maxstorage_2 = String.format("%.2fE", number / 10E17D);
        } else if (number >= 10E20D && number < 10E23D) {

            maxstorage_2 = String.format("%.2fZ", number / 10E20D);
        } else if (number >= 10E23D && number < 10E26D) {

            maxstorage_2 = String.format("%.2fY", number / 10E23D);
        }
        return maxstorage_2;

    }

    public static NBTTagCompound nbt() {
        return new NBTTagCompound();
    }

    public static NBTTagCompound nbt(ItemStack stack) {
        if (stack == null) {
            return null;
        }
        NBTTagCompound NBTTagCompound = stack.getTagCompound();
        if (NBTTagCompound == null) {
            NBTTagCompound = new NBTTagCompound();
        }
        stack.setTagCompound(NBTTagCompound);
        return NBTTagCompound;
    }

    public static int slot(List<Integer> list) {
        int meta = 0;
        for (Integer integer : list) {

            if (integer != 0) {
                meta = integer;
            }

        }
        return meta;
    }

    public static String NBTGetString(ItemStack stack, String name) {
        if (name == null) {
            return "";
        }
        if (stack == null) {
            return "";
        }
        NBTTagCompound NBTTagCompound = nbt(stack);

        return NBTTagCompound.getString(name);

    }

    public static int NBTGetInteger(ItemStack stack, String name) {
        if (name == null) {
            return 0;
        }
        NBTTagCompound NBTTagCompound = stack.getTagCompound();
        if (NBTTagCompound == null) {
            return 0;
        }

        return NBTTagCompound.getInteger(name);
    }

    public static void NBTSetInteger(ItemStack stack, String name, int string) {
        if (name == null) {
            return;
        }
        NBTTagCompound NBTTagCompound = stack.getTagCompound();
        if (NBTTagCompound == null) {
            NBTTagCompound = new NBTTagCompound();
        }
        NBTTagCompound.setInteger(name, string);
        stack.setTagCompound(NBTTagCompound);
    }

    public static int convertRGBcolorToInt(int r, int g, int b) {
        float divColor = 255.0F;
        Color tmpColor = new Color(r / divColor, g / divColor, b / divColor);
        return tmpColor.getRGB();
    }

}
