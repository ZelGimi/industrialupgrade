package com.denfop.utils;

import com.denfop.IUCore;
import com.denfop.Ic2Items;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.items.bags.HandHeldBags;
import com.denfop.tiles.mechanism.quarry.QuarryItem;
import ic2.core.block.TileEntityBlock;
import ic2.core.init.Localization;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModUtils {

    public static Logger log;
    public static EnumFacing[] facings = EnumFacing.values();

    public static ItemStack getCable(ItemStack stack, int insulation) {
        final NBTTagCompound nbt = nbt(stack);
        nbt.setByte("insulation", (byte) insulation);
        nbt.setByte("type", (byte) stack.getItemDamage());
        return stack;
    }

    public static List<ItemStack> get_blacklist_block() {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(Blocks.STONE));
        list.add(new ItemStack(Blocks.DIRT));
        list.add(new ItemStack(Blocks.NETHERRACK));
        list.add(new ItemStack(Blocks.END_STONE));
        list.add(new ItemStack(Blocks.STONE, 1, 1));
        list.add(new ItemStack(Blocks.STONE, 1, 2));
        list.add(new ItemStack(Blocks.STONE, 1, 3));
        list.add(new ItemStack(Blocks.STONE, 1, 4));
        list.add(new ItemStack(Blocks.STONE, 1, 5));
        list.add(new ItemStack(Blocks.STONE, 1, 6));
        list.add(new ItemStack(Blocks.DIRT, 1, 1));
        list.add(new ItemStack(Blocks.DIRT, 1, 2));
        return list;
    }

    public static void info(String message) {
        log.info(message);
    }

    public static ItemStack getCellFromFluid(String name) {
        final ItemStack cell = Ic2Items.FluidCell.copy();
        final NBTTagCompound nbt = ModUtils.nbt(cell);
        final NBTTagCompound nbt1 = ModUtils.nbt();
        nbt1.setString("FluidName", name);
        nbt1.setInteger("Amount", 1000);
        nbt.setTag("Fluid", nbt1);
        return cell;
    }

    public static ItemStack getCellFromFluid(Fluid name) {
        final ItemStack cell = Ic2Items.FluidCell.copy();
        final NBTTagCompound nbt = ModUtils.nbt(cell);
        final NBTTagCompound nbt1 = ModUtils.nbt();
        nbt1.setString("FluidName", name.getName());
        nbt1.setInteger("Amount", 1000);
        nbt.setTag("Fluid", nbt1);
        return cell;
    }

    public static List<ItemStack> getListFromModule(ItemStack stack) {
        List<ItemStack> stacks = new ArrayList<>();
        if (!stack.isEmpty()) {
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            int size = nbt.getInteger("size");
            for (int j = 0; j < size; j++) {
                String l = "number_" + j;
                String temp = ModUtils.NBTGetString(stack, l);
                stacks.addAll(OreDictionary.getOres(temp));

            }
        }
        return stacks;
    }

    public static List<QuarryItem> getQuarryListFromModule(ItemStack stack) {
        List<QuarryItem> stacks = new ArrayList<>();
        if (!stack.isEmpty()) {
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            int size = nbt.getInteger("size");
            for (int j = 0; j < size; j++) {
                String l = "number_" + j;
                String temp = ModUtils.NBTGetString(stack, l);
                stacks.add(new QuarryItem(OreDictionary.getOres(temp).get(0), temp));

            }
        }
        return stacks;
    }

    public static boolean getore(Block localBlock, int meta) {
        ItemStack stack = new ItemStack(localBlock, 1, meta);
        for (ItemStack itemstack : get_blacklist_block()) {
            if (stack.isItemEqual(itemstack)) {
                return false;
            }
        }
        return true;
    }

    public static boolean getore(Item localBlock) {
        for (ItemStack itemstack : get_blacklist_block()) {
            if (localBlock == itemstack.getItem()) {
                return false;
            }
        }
        return true;
    }

    public static boolean getore(ItemStack localBlock) {
        for (ItemStack itemstack : get_blacklist_block()) {
            if (localBlock.isItemEqual(itemstack)) {
                return false;
            }
        }
        return true;
    }

    public static boolean getore(Block stack, Block localBlock) {
        ItemStack stack1 = new ItemStack(localBlock);
        for (ItemStack itemstack : get_blacklist_block()) {
            if (stack1.isItemEqual(itemstack)) {
                return false;
            }
        }
        if (stack != localBlock) {
            return false;
        }

        if (localBlock == Blocks.LIT_REDSTONE_ORE) {
            return true;
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


    public static void SetDoubleWithoutItem(NBTTagCompound NBTTagCompound, String name, double amount) {
        if (NBTTagCompound == null) {
            NBTTagCompound = new NBTTagCompound();
        }
        NBTTagCompound.setDouble(name, amount);

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
        list.add(mode(nbt));
    }

    public static String mode(NBTTagCompound nbt) {
        String mode = nbt.getString("mode");
        if (mode.isEmpty()) {
            return Localization.translate("defaultskin");
        }
        switch (mode) {
            case "Zelen":
                return Localization.translate("camouflageskin");
            case "Demon":
                return Localization.translate("demonskin");
            case "Dark":
                return Localization.translate("Darkskin");
            case "Cold":
                return Localization.translate("Coldskin");
            case "Ender":
                return Localization.translate("Enderskin");
            case "Ukraine":
                return Localization.translate("Ukraineskin");
            case "Fire":
                return Localization.translate("Fireskin");
            case "Emerald":
                return Localization.translate("Emeraldskin");
            case "Taiga":
                return Localization.translate("Taigaskin");
            case "Desert":
                return Localization.translate("Desertskin");
            case "Snow":
                return Localization.translate("Snowskin");
        }
        return Localization.translate("defaultskin");
    }

    public static ItemStack mode(NBTTagCompound nbt, ItemStack stack) {
        String mode = nbt.getString("mode");
        ItemStack stack1 = stack.copy();
        if (mode.isEmpty()) {
            return stack1;
        }
        nbt = nbt(stack1);
        nbt.setString("mode", mode);
        return stack1;
    }

    public static String mode(int meta) {
        if (meta == 0) {
            return Localization.translate("defaultskin");
        }
        switch (meta) {
            case 3:
                return Localization.translate("camouflageskin");
            case 4:
                return Localization.translate("demonskin");
            case 6:
                return Localization.translate("Darkskin");
            case 1:
                return Localization.translate("Coldskin");
            case 7:
                return Localization.translate("Enderskin");
            case 2:
                return Localization.translate("Ukraineskin");
            case 5:
                return Localization.translate("Fireskin");
            case 11:
                return Localization.translate("Emeraldskin");
            case 8:
                return Localization.translate("Taigaskin");
            case 10:
                return Localization.translate("Desertskin");
            case 9:
                return Localization.translate("Snowskin");
        }
        return Localization.translate("defaultskin");
    }

    public static String getString(float number) {
        float gg;
        int i;

        i = (int) Math.log10(number);
        String maxstorage_2 = "0";
        if (i >= 0 && i < 3 && number <= 1000) {

            gg = number;
            maxstorage_2 = String.format("%.0f", gg);
        } else if (i >= 3 && i < 6) {
            gg = number / (1000);
            maxstorage_2 = String.format("%.2fK", gg);
        } else if (i >= 6 && i < 9) {
            gg = number / (1000000);
            maxstorage_2 = String.format("%.2fM", gg);
        } else if (i >= 9 && i < 12) {
            gg = number / (1000000000);
            maxstorage_2 = String.format("%.2fG", gg);
        }
        return maxstorage_2;

    }

    public static String getString1(double number) {
        double gg;
        int i;

        i = (int) Math.log10(number);
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

    public static String getString(double number) {
        String maxstorage_2 = "0";
        int i = (int) Math.log10(number);
        if (i < 3) {

            maxstorage_2 = String.format("%.0f", number);
        } else if (i < 6) {

            maxstorage_2 = String.format("%.2fK", number / 10E2D);
        } else if (i < 9) {

            maxstorage_2 = String.format("%.2fM", number / 10E5D);
        } else if (i < 12) {

            maxstorage_2 = String.format("%.2fG", number / 10E8D);
        } else if (i < 15) {

            maxstorage_2 = String.format("%.2fT", number / 10E11D);
        } else if (i < 18) {

            maxstorage_2 = String.format("%.2fP", number / 10E14D);
        } else if (i < 21) {

            maxstorage_2 = String.format("%.2fE", number / 10E17D);
        } else if (i < 24) {

            maxstorage_2 = String.format("%.2fZ", number / 10E20D);
        } else if (i < 27) {

            maxstorage_2 = String.format("%.2fY", number / 10E23D);
        }
        return maxstorage_2;

    }

    public static NBTTagCompound nbt() {
        return new NBTTagCompound();
    }

    public static NBTTagCompound nbt(ItemStack stack) {
        if (stack.isEmpty()) {
            return new NBTTagCompound();
        }
        NBTTagCompound NBTTagCompound = stack.getTagCompound();
        if (NBTTagCompound == null) {
            NBTTagCompound = new NBTTagCompound();
        }
        stack.setTagCompound(NBTTagCompound);
        return NBTTagCompound;
    }

    public static NBTTagCompound nbtOrNull(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return stack.getTagCompound();
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

    public static IItemHandler getItemHandler(@Nullable TileEntity tile, EnumFacing side) {
        if (tile == null) {
            return null;
        }

        IItemHandler handler = tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side) ? tile.getCapability(
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                side
        ) : null;

        if (handler == null) {
            if (side != null && tile instanceof ISidedInventory) {
                handler = new SidedInvWrapper((ISidedInventory) tile, side);
            } else if (tile instanceof IInventory) {
                handler = new InvWrapper((IInventory) tile);
            }
        }

        return handler;
    }

    public static void tick(InvSlotOutput slot, TileEntityBlock tile) {

        for (EnumFacing facing1 : facings) {
            BlockPos pos = tile.getPos().offset(facing1);
            final TileEntity tile1 = tile.getWorld().getTileEntity(pos);
            final IItemHandler handler = getItemHandler(tile1, facing1.getOpposite());
            if (handler == null) {
                continue;
            }
            final int slots = handler.getSlots();
            for (int j = 0; j < slot.size(); j++) {
                ItemStack took = slot.get(j);
                if (took.isEmpty()) {
                    continue;
                }

                if (!(handler instanceof ISidedInventory)) {
                    took = took.copy();
                    if (insertItem(handler, took, true, slots).isEmpty()) {
                        slot.put(j, ItemStack.EMPTY);
                        insertItem(handler, took, false, slots);
                    }
                } else {
                    if (insertItem1(handler, took, true, slots).isEmpty()) {
                        slot.put(j, ItemStack.EMPTY);
                        insertItem1(handler, took, false, slots);

                    }
                }

            }
        }

    }

    public static void tick(ItemStack[] slot, IItemHandler tile, HandHeldBags handHeldBags) {
        if (tile == null) {
            return;
        }
        final int slots = tile.getSlots();
        for (int i = 0; i < slot.length; i++) {
            ItemStack took = slot[i];
            if (took.isEmpty()) {
                continue;
            }

            if (!(tile instanceof ISidedInventory)) {
                took = took.copy();
                if (insertItem(tile, took, true, slots).isEmpty()) {
                    handHeldBags.put(i, ItemStack.EMPTY);
                    insertItem(tile, took, false, slots);
                }
            } else {
                if (insertItem1(tile, took, true, slots).isEmpty()) {
                    handHeldBags.put(i, ItemStack.EMPTY);
                    insertItem1(tile, took, false, slots);

                }
            }


        }

    }

    @Nonnull
    public static ItemStack insertItem1(IItemHandler dest, @Nonnull ItemStack stack, boolean simulate, int slot) {
        if (dest == null || stack.isEmpty()) {
            return stack;
        }

        for (int i = 0; i < slot; i++) {
            stack = insertItem2(dest, i, stack, simulate);
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }

        return stack;
    }

    public static boolean canItemStacksStack(@Nonnull ItemStack a, @Nonnull ItemStack b) {
        if (a.isEmpty() || !a.isItemEqual(b) || a.hasTagCompound() != b.hasTagCompound()) {
            return false;
        }

        return (!a.hasTagCompound() || a.getTagCompound().equals(b.getTagCompound()));
    }

    @Nonnull
    public static ItemStack insertItem2(IItemHandler dest, int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack stackInSlot = dest.getStackInSlot(slot);

        int m;
        if (!stackInSlot.isEmpty()) {
            int max = stackInSlot.getMaxStackSize();
            int limit = dest.getSlotLimit(slot);
            if (stackInSlot.getCount() >= Math.min(max, limit)) {
                return stack;
            }

            if (!canItemStacksStack(stack, stackInSlot)) {
                return stack;
            }


            m = Math.min(max, limit) - stackInSlot.getCount();

            if (stack.getCount() <= m) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.grow(stackInSlot.getCount());
                    ((SidedInvWrapper) dest).setStackInSlot(slot, copy);
                    return ItemStack.EMPTY;
                }

            } else {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.splitStack(m);
                    copy.grow(stackInSlot.getCount());
                    ((SidedInvWrapper) dest).setStackInSlot(slot, copy);
                    return ItemStack.EMPTY;
                }
            }
            return stack;
        } else {


            m = Math.min(stack.getMaxStackSize(), dest.getSlotLimit(slot));
            if (m < stack.getCount()) {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    ((SidedInvWrapper) dest).setStackInSlot(slot, stack.splitStack(m));
                }
                return stack;
            } else {
                if (!simulate) {
                    ((SidedInvWrapper) dest).setStackInSlot(slot, stack);
                }
                return ItemStack.EMPTY;
            }
        }

    }

    @Nonnull
    public static ItemStack insertItem(IItemHandler dest, @Nonnull ItemStack stack, boolean simulate, final int slots) {
        if (dest == null || stack.isEmpty()) {
            return stack;
        }

        for (int i = 0; i < slots; i++) {
            stack = dest.insertItem(i, stack, simulate);
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }

        return stack;
    }


    public static ItemStack setSize(ItemStack stack, int col) {
        stack = stack.copy();
        stack.setCount(col);
        return stack;
    }

}
