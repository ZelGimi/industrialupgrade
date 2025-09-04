package com.denfop.utils;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.pollution.radiation.EnumCoefficient;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.mechanism.quarry.QuarryItem;
import com.denfop.inventory.Inventory;
import com.denfop.world.WorldBaseGen;
import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.VeinType;
import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.*;


public class ModUtils {

    public static final Set<String> ignoredNbtKeys = new HashSet<>(Arrays.asList("damage", "charge", "energy", "advDmg"));
    private static final Direction[] BY_2D_DATA = Arrays.stream(Direction.values()).filter((p_235685_) -> p_235685_.getAxis().isHorizontal()).sorted(Comparator.comparingInt(Direction::get2DDataValue)).toArray(Direction[]::new);
    public static Logger log;
    public static Direction[] facings = Direction.values();
    public static ItemStack emptyStack = ItemStack.EMPTY;
    public static Set<Direction> noFacings = Collections.emptySet();
    public static Set<Direction> onlyNorth;
    public static Set<Direction> horizontalFacings;
    public static Direction[] HORIZONTALS;
    public static Set<Direction> verticalFacings;
    public static Set<Direction> downSideFacings;
    public static Set<Direction> allFacings;

    static {

        onlyNorth = Collections.unmodifiableSet(EnumSet.of(Direction.NORTH));
        horizontalFacings = Collections.unmodifiableSet(EnumSet.copyOf(Arrays.asList(BY_2D_DATA)));
        HORIZONTALS = BY_2D_DATA;
        verticalFacings = Collections.unmodifiableSet(EnumSet.of(Direction.DOWN, Direction.UP));
        downSideFacings = Collections.unmodifiableSet(EnumSet.complementOf(EnumSet.of(Direction.UP)));
        allFacings = Collections.unmodifiableSet(EnumSet.allOf(Direction.class));
    }


    public static double getEnergyValue(ItemStack stack) {
        if (ModUtils.isEmpty(stack)) {
            return 0.0;
        } else if (ModUtils.checkItemEquality(stack, Items.REDSTONE)) {
            return 800.0;
        } else {
            return 0;
        }
    }

    public static int getFuelValue(ItemStack stack, boolean allowLava) {
        if (ModUtils.isEmpty(stack)) {
            return 0;
        }

        FluidStack liquid = FluidStack.EMPTY;
        try {
            Optional<FluidStack> optional = FluidUtil.getFluidContained(stack);
            if (optional != null && optional.isPresent()) {
                liquid = optional.get();
            }
        } catch (Exception e) {
            liquid = FluidStack.EMPTY;
        }

        boolean isLava = !liquid.isEmpty() && liquid.getAmount() > 0 && liquid.getFluid() == Fluids.LAVA;
        if (isLava && !allowLava) {
            return 0;
        }

        Integer ret = AbstractFurnaceBlockEntity.getFuel().get(stack.getItem());
        if (ret == null) {
            return 0;
        }

        return isLava ? ret / 10 : ret;
    }


    public static ItemStack get(Player player, InteractionHand hand) {
        return player.getItemInHand(hand);
    }

    public static ItemStack decSize(ItemStack stack) {
        return decSize(stack, 1);
    }

    public static ItemStack decSize(ItemStack stack, int amount) {
        return incSize(stack, -amount);
    }

    public static ItemStack incSize(ItemStack stack, int amount) {
        return setSize(stack, getSize(stack) + amount);
    }

    public static boolean storeInventoryItem(ItemStack stack, Player player, boolean simulate) {
        if (!simulate) {
            return player.getInventory().add(stack);
        } else {
            int sizeLeft = getSize(stack);
            int maxStackSize = Math.min(player.getInventory().getMaxStackSize(), stack.getMaxStackSize());

            for (int i = 0; i < player.getInventory().items.size() && sizeLeft > 0; ++i) {
                ItemStack invStack = player.getInventory().items.get(i);
                if (isEmpty(invStack)) {
                    sizeLeft -= maxStackSize;
                } else if (checkItemEqualityStrict(stack, invStack) && getSize(invStack) < maxStackSize) {
                    sizeLeft -= maxStackSize - getSize(invStack);
                }
            }

            return sizeLeft <= 0;
        }
    }

    public static void dropAsEntity(Level world, BlockPos pos, ItemStack stack, int count) {
        stack = stack.copy();
        stack.setCount(count);
        dropAsEntity(world, pos, stack);
    }

    public static void dropAsEntity(Level world, BlockPos pos, ItemStack stack) {
        if (!isEmpty(stack)) {
            double f = 0.7;
            double dx = (double) world.random.nextFloat() * f + (1.0 - f) * 0.5;
            double dy = (double) world.random.nextFloat() * f + (1.0 - f) * 0.5;
            double dz = (double) world.random.nextFloat() * f + (1.0 - f) * 0.5;
            ItemEntity entityItem = new ItemEntity(
                    world,
                    (double) pos.getX() + dx,
                    (double) pos.getY() + dy,
                    (double) pos.getZ() + dz,
                    stack.copy()
            );
            entityItem.setDefaultPickUpDelay();
            world.addFreshEntity(entityItem);
        }
    }


    public static boolean checkItemEquality(ItemStack a, ItemStack b) {
        return isEmpty(a) && isEmpty(b) || !isEmpty(a) && !isEmpty(b) && a.getItem() == b.getItem() && checkNbtEquality(
                a,
                b
        );
    }

    public static boolean checkItemEquality(ItemStack a, Item b) {
        return isEmpty(a) && b == null || !isEmpty(a) && b != null && a.getItem() == b;
    }

    public static boolean checkItemEqualityStrict(ItemStack a, ItemStack b) {
        return isEmpty(a) && isEmpty(b) || !isEmpty(a) && !isEmpty(b) && a.is(b.getItem()) && checkNbtEqualityStrict(a, b);
    }

    private static boolean checkNbtEquality(ItemStack a, ItemStack b) {
        return NbtUtils.compareNbt(a.getTag(), b.getTag(), true);
    }

    public static boolean checkNbtEquality(CompoundTag a, CompoundTag b) {
        if (a == b) {
            return true;
        } else {
            Set<String> keysA = a != null ? a.getAllKeys() : Collections.emptySet();
            Set<String> keysB = b != null ? b.getAllKeys() : Collections.emptySet();
            if (keysA.isEmpty() && keysB.isEmpty()) {
                return true;
            }
            Set<String> toCheck = new HashSet<>(Math.max(keysA.size(), keysB.size()));
            Iterator<String> var5 = keysA.iterator();

            String key;
            while (var5.hasNext()) {
                key = var5.next();
                if (!ignoredNbtKeys.contains(key)) {
                    if (!keysB.contains(key)) {
                        return false;
                    }

                    toCheck.add(key);
                }
            }

            var5 = keysB.iterator();

            while (var5.hasNext()) {
                key = var5.next();
                if (!ignoredNbtKeys.contains(key)) {
                    if (!keysA.contains(key)) {
                        return false;
                    }

                    toCheck.add(key);
                }
            }

            var5 = toCheck.iterator();

            do {
                if (!var5.hasNext()) {
                    return true;
                }

                key = var5.next();
            } while (a.get(key).equals(b.get(key)));

            return false;
        }
    }

    public static boolean checkNbtEqualityStrict(ItemStack a, ItemStack b) {
        CompoundTag nbtA = a.getTag();
        CompoundTag nbtB = b.getTag();
        if (nbtA == null && nbtB == null)
            return true;
        if (nbtA == nbtB) {
            return true;
        } else {
            if (nbtA == null)
                return false;
            return nbtA.equals(nbtB);
        }
    }

    public static boolean isEmpty(ItemStack stack) {
        if (stack == emptyStack || stack == null) return true;
        stack.getItem();
        return stack.getCount() <= 0;
    }

    public static boolean isEmpty(Player player, InteractionHand hand) {
        return isEmpty(player.getItemInHand(hand));
    }

    public static int getSize(ItemStack stack) {
        return isEmpty(stack) ? 0 : stack.getCount();
    }


    public static List<ItemStack> get_blacklist_block() {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(Blocks.STONE));
        list.add(new ItemStack(Blocks.DIRT));
        list.add(new ItemStack(Blocks.NETHERRACK));
        list.add(new ItemStack(Blocks.END_STONE));
        list.add(new ItemStack(Blocks.ANDESITE));
        list.add(new ItemStack(Blocks.GRANITE));
        list.add(new ItemStack(Blocks.COBBLESTONE));
        list.add(new ItemStack(Blocks.POLISHED_GRANITE));
        list.add(new ItemStack(Blocks.POLISHED_DIORITE));
        list.add(new ItemStack(Blocks.POLISHED_ANDESITE));
        list.add(new ItemStack(Blocks.COARSE_DIRT));
        list.add(new ItemStack(Blocks.PODZOL));
        return list;
    }

    public static void info(String message) {
        log.info(message);
    }

    // TODO: нужно решить
 /*   public static ItemStack getCellFromFluid(String name) {
        for (CellType cellType : CellType.values()) {
            if (cellType.getFluid() == null) {
                continue;
            }
            if (cellType.getFluid().getName().trim().equals(name.trim())) {
                return new ItemStack(IUItem.cell_all, 1, cellType.ordinal());
            }
        }
        return new ItemStack(IUItem.cell_all, 1, 0);
    }

    public static ItemStack getCellFromFluid(Fluid name) {
        for (CellType cellType : CellType.values()) {
            if (cellType.getFluid() == null) {
                continue;
            }
            if (cellType.getFluid().equals(name)) {
                return new ItemStack(IUItem.cell_all, 1, cellType.ordinal());
            }
        }
        return new ItemStack(IUItem.cell_all, 1, 0);
    }
*/
    public static List<ItemStack> getListFromModule(ItemStack stack) {
        List<ItemStack> stacks = new ArrayList<>();
        if (!stack.isEmpty()) {
            final CompoundTag nbt = ModUtils.nbt(stack);
            int size = nbt.getInt("size");
            for (int j = 0; j < size; j++) {
                String l = "number_" + j;
                String temp = ModUtils.NBTGetString(stack, l);
                TagKey<Item> tag = new TagKey<>(Registry.ITEM_REGISTRY, new ResourceLocation(temp));
                List<ItemStack> list = new Ingredient.TagValue(tag).getItems().stream().toList();
                stacks.addAll(list);

            }
        }
        return stacks;
    }

    public static List<QuarryItem> getQuarryListFromModule(ItemStack stack) {
        List<QuarryItem> stacks = new ArrayList<>();
        if (!stack.isEmpty()) {
            final CompoundTag nbt = ModUtils.nbt(stack);
            int size = nbt.getInt("size");
            for (int j = 0; j < size; j++) {
                String l = "number_" + j;
                String temp = ModUtils.NBTGetString(stack, l);
                stacks.add(new QuarryItem(temp));

            }
        }
        return stacks;
    }

    public static boolean getOre(Block localBlock) {
        ItemStack stack = new ItemStack(localBlock, 1);
        for (ItemStack itemstack : get_blacklist_block()) {
            if (stack.is(itemstack.getItem())) {
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
            if (localBlock.is(itemstack.getItem())) {
                return false;
            }
        }
        return true;
    }

    public static boolean getore(Block stack, Block localBlock) {
        ItemStack stack1 = new ItemStack(localBlock);
        for (ItemStack itemstack : get_blacklist_block()) {
            if (stack1.is(itemstack.getItem())) {
                return false;
            }
        }
        if (stack != localBlock) {
            return false;
        }


        for (ItemStack itemstack : IUCore.get_ore) {
            BlockItem blockItem = (BlockItem) itemstack.getItem();
            if (stack == blockItem.getBlock()) {
                return true;
            }
        }
        return localBlock == Blocks.REDSTONE_ORE;
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
        CompoundTag NBTTagCompound = stack.getTag();
        if (NBTTagCompound == null) {
            NBTTagCompound = new CompoundTag();
        }
        NBTTagCompound.putString(name, string);
        stack.setTag(NBTTagCompound);
    }

    public static int getsum1(List<Integer> sum) {
        int sum_sum = 0;
        for (Integer aDouble : sum) {
            sum_sum += aDouble;
        }
        return sum_sum;
    }

    public static void SetDoubleWithoutItem(CompoundTag NBTTagCompound, String name, double amount) {
        if (NBTTagCompound == null) {
            NBTTagCompound = new CompoundTag();
        }
        NBTTagCompound.putDouble(name, amount);

    }

    public static String Boolean(boolean boolean1) {
        if (boolean1) {
            return Localization.translate("iu.yes");
        } else {
            return Localization.translate("iu.no");
        }

    }

    public static void mode(ItemStack stack, List<Component> list) {
        CompoundTag nbt = nbt(stack);
        list.add(Component.literal(mode(nbt)));
    }

    public static String mode(CompoundTag nbt) {
        String mode = nbt.getString("mode");
        if (mode.isEmpty()) {
            return Localization.translate("defaultskin");
        }
        return switch (mode) {
            case "Zelen" -> Localization.translate("camouflageskin");
            case "Demon" -> Localization.translate("demonskin");
            case "Dark" -> Localization.translate("Darkskin");
            case "Cold" -> Localization.translate("Coldskin");
            case "Ender" -> Localization.translate("Enderskin");
            case "Ukraine" -> Localization.translate("Ukraineskin");
            case "Fire" -> Localization.translate("Fireskin");
            case "Emerald" -> Localization.translate("Emeraldskin");
            case "Taiga" -> Localization.translate("Taigaskin");
            case "Desert" -> Localization.translate("Desertskin");
            case "Snow" -> Localization.translate("Snowskin");
            default -> Localization.translate("defaultskin");
        };
    }

    public static ItemStack mode(CompoundTag nbt, ItemStack stack) {
        String mode = nbt.getString("mode");
        ItemStack stack1 = stack.copy();
        if (mode.isEmpty()) {
            return stack1;
        }
        nbt = nbt(stack1);
        nbt.putString("mode", mode);
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
        if (i > -3 && i < 0) {
            gg = number * 1000;
            maxstorage_2 = String.format("%.0fm", gg);
        } else if (i <= -3 && i > -6) {
            gg = number * 1000000;
            maxstorage_2 = String.format("%.2fµ", gg);
        } else if (i <= -6 && i > -9) {
            gg = number * 1000000000;
            maxstorage_2 = String.format("%.2fn", gg);
        } else if (i <= -9 && i > -12) {
            gg = number * 1000000000000F;
            maxstorage_2 = String.format("%.2fp", gg);
        } else if (i >= 0 && i < 3 && number <= 1000) {

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

    public static String getUnit(EnumCoefficient coefficient) {
        switch (coefficient) {
            case MICRO:
                return "µ";
            case MILI:
                return "m";
            case KILO:
                return "k";
            case DEFAULT:
                return "";
            default:
                return "n";
        }
    }

    public static String getString1(double number) {
        double gg;
        int i;

        i = (int) Math.log10(number);
        String maxstorage_2 = "0";
        if (i > -3 && i < 0) {
            gg = number * 1000;
            maxstorage_2 = String.format("%.0fm", gg);
        } else if (i <= -3 && i > -6) {
            gg = number * 1000000;
            maxstorage_2 = String.format("%.0fµ", gg);
        } else if (i <= -6 && i > -9) {
            gg = number * 1000000000;
            maxstorage_2 = String.format("%.0fn", gg);
        } else if (i <= -9 && i > -12) {
            gg = number * 1000000000000D;
            maxstorage_2 = String.format("%.0fp", gg);
        } else if (i >= 0 && i < 3 && number <= 1000) {

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
        double i = Math.log10(number);
        if (i > -3 && i < 0) {
            maxstorage_2 = String.format("%.0fm", number * 10E2D);
        } else if (i <= -3 && i > -6) {
            maxstorage_2 = String.format("%.0fµ", number * 10E5D);
        } else if (i <= -6 && i > -9) {
            maxstorage_2 = String.format("%.0fn", number * 10E8D);
        } else if (i <= -9 && i > -12) {
            maxstorage_2 = String.format("%.0fp", number * 10E11D);
        } else if (i < 3) {
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

    public static String getStringBukket(double number) {
        String maxstorage_2 = "0";
        double i = Math.log10(number);
        if (i > -3 && i < 0) {
            maxstorage_2 = String.format("%.0fm", number * 10E2D);
        } else if (i <= -3 && i > -6) {
            maxstorage_2 = String.format("%.0fµ", number * 10E5D);
        } else if (i <= -6 && i > -9) {
            maxstorage_2 = String.format("%.0fn", number * 10E8D);
        } else if (i <= -9 && i > -12) {
            maxstorage_2 = String.format("%.0fp", number * 10E11D);
        } else if (i < 3) {
            maxstorage_2 = String.format("%.0fm", number * 1000);
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

    public static CompoundTag nbt() {
        return new CompoundTag();
    }

    public static CompoundTag nbt(ItemStack stack) {
        if (stack.isEmpty()) {
            return new CompoundTag();
        }
        CompoundTag NBTTagCompound = stack.getTag();
        if (NBTTagCompound == null) {
            NBTTagCompound = new CompoundTag();
        }
        stack.setTag(NBTTagCompound);
        return NBTTagCompound;
    }

    public static CompoundTag nbtOrNull(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return stack.getTag();
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
        CompoundTag NBTTagCompound = nbt(stack);

        return NBTTagCompound.getString(name);

    }

    public static int NBTGetInteger(ItemStack stack, String name) {
        if (name == null) {
            return 0;
        }
        CompoundTag NBTTagCompound = stack.getTag();
        if (NBTTagCompound == null) {
            return 0;
        }

        return NBTTagCompound.getInt(name);
    }

    public static void NBTSetInteger(ItemStack stack, String name, int string) {
        if (name == null) {
            return;
        }
        CompoundTag NBTTagCompound = stack.getTag();
        if (NBTTagCompound == null) {
            NBTTagCompound = new CompoundTag();
        }
        NBTTagCompound.putInt(name, string);
        stack.setTag(NBTTagCompound);
    }

    public static int convertRGBcolorToInt(int r, int g, int b) {
        float divColor = 255.0F;
        Color tmpColor = new Color(r / divColor, g / divColor, b / divColor);
        return tmpColor.getRGB();
    }

    public static int convertRGBAcolorToInt(int r, int g, int b) {
        return ((250 & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF);
    }

    public static IItemHandler getItemHandler(@Nullable BlockEntity tile, Direction side) {
        if (tile == null) {
            return null;
        }

        @NotNull LazyOptional<IItemHandler> capability = tile.getCapability(ForgeCapabilities.ITEM_HANDLER, side);
        IItemHandler handler = capability.orElseGet(() -> null);
        if (handler == null) {
            if (side != null && tile instanceof WorldlyContainer) {
                handler = new SidedInvWrapper((WorldlyContainer) tile, side);
            } else if (tile instanceof Container) {
                handler = new InvWrapper((Container) tile);
            }
        }

        return handler;
    }

    /*  TODO: решить
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

                    ItemStack stack1;
                    if (!(handler instanceof ISidedInventory)) {
                        took = took.copy();
                        stack1 = insertItem(handler, took, true, slots);
                        if (stack1.isEmpty()) {
                            slot.set(j, ItemStack.EMPTY);
                            insertItem(handler, took, false, slots);
                        } else if (stack1 != took) {
                            slot.get(j).shrink(stack1.getCount());
                            insertItem(handler, stack1, false, slots);
                        }
                    } else {
                        stack1 = insertItem1(handler, took, true, slots);
                        if (stack1.isEmpty()) {
                            slot.set(j, ItemStack.EMPTY);
                            insertItem1(handler, took, false, slots);
                        } else if (stack1 != took) {
                            slot.get(j).shrink(stack1.getCount());
                            insertItem1(handler, stack1, false, slots);
                        }
                    }

                }
            }

        }

        public static void tick(ItemStack[] slot, IItemHandler tile, ItemStackBags handHeldBags) {
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
                    final ItemStack stack = insertItem(tile, took, true, slots);
                    if (stack.isEmpty()) {
                        handHeldBags.set(i, ItemStack.EMPTY);
                        insertItem(tile, took, false, slots);
                    } else if (stack != took) {
                        handHeldBags.get(i).shrink(stack.getCount());
                        insertItem1(tile, stack, false, slots);
                    }
                } else {
                    final ItemStack stack = insertItem1(tile, took, true, slots);
                    if (stack.isEmpty()) {
                        handHeldBags.set(i, ItemStack.EMPTY);
                        insertItem1(tile, took, false, slots);

                    } else if (stack != took) {
                        handHeldBags.get(i).shrink(stack.getCount());
                        insertItem1(tile, stack, false, slots);
                    }
                }


            }

        }

        @Nonnull
        public static ItemStack insertItem1(IItemHandler dest, @Nonnull ItemStack stack, boolean simulate, int slot) {
            if (dest == null || stack.isEmpty()) {
                return stack;
            }
            slot = Math.min(slot, dest.getSlots());
            for (int i = 0; i < slot; i++) {
                final ItemStack stack2 = insertItem2(dest, i, stack, simulate);
                if (stack.isEmpty()) {
                    return ItemStack.EMPTY;
                } else if (stack2 != stack) {
                    return stack2;
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
        public static ItemStack insertItem(IItemHandler dest, @Nonnull ItemStack stack, boolean simulate, int slots) {
            if (dest == null || stack.isEmpty()) {
                return stack;
            }
            slots = Math.min(slots, dest.getSlots());
            for (int i = 0; i < slots; i++) {
                ItemStack stack1 = dest.insertItem(i, stack, simulate);
                if (stack1.isEmpty()) {
                    return ItemStack.EMPTY;
                } else if (stack1 != stack) {
                    return stack1;
                }
            }

            return stack;
        }
    */
    public static ItemStack setSize(ItemStack stack, int col) {
        stack = stack.copy();
        stack.setCount(col);
        return stack;
    }

    public static ItemStack setSize(Item item, int col) {
        final ItemStack stack = new ItemStack(item);
        stack.setCount(col);
        return stack;
    }

    public static int limit(int value, int min, int max) {
        if (value <= min) {
            return min;
        } else {
            return Math.min(value, max);
        }
    }

    public static float limit(float value, float min, float max) {
        if (!Float.isNaN(value) && !(value <= min)) {
            return Math.min(value, max);
        } else {
            return min;
        }
    }

    public static double limit(double value, double min, double max) {
        if (!Double.isNaN(value) && !(value <= min)) {
            return Math.min(value, max);
        } else {
            return min;
        }
    }


    public static ItemStack getRecipeFromType(Level world, ItemStack stack1, RecipeType<SmeltingRecipe> type) {
        List<SmeltingRecipe> recipes = world.getRecipeManager().getAllRecipesFor(type);
        for (SmeltingRecipe recipe : recipes) {
            if (recipe.getIngredients().size() > 1)
                return ItemStack.EMPTY;
            else if (recipe.getIngredients().get(0).test(stack1))
                return recipe.getResultItem();
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    public static ItemStack insertItem(IItemHandler dest, @Nonnull ItemStack stack, boolean simulate, int slots) {
        if (dest == null || stack.isEmpty()) {
            return stack;
        }
        slots = Math.min(slots, dest.getSlots());
        for (int i = 0; i < slots; i++) {
            ItemStack stack1 = dest.insertItem(i, stack, simulate);
            if (stack1.isEmpty()) {
                return ItemStack.EMPTY;
            } else if (stack1 != stack) {
                return stack1;
            }
        }

        return stack;
    }

    public static void showFlames(Level level, BlockPos pos, Direction facing) {
        if (level.random.nextInt(8) == 0) {
            double x = pos.getX() + ((facing.getStepX() * 1.04 + 1.0) / 2.0);
            double y = pos.getY() + level.random.nextFloat() * 0.375;
            double z = pos.getZ() + ((facing.getStepZ() * 1.04 + 1.0) / 2.0);

            if (facing.getAxis() == Direction.Axis.X) {
                z += level.random.nextFloat() * 0.625 - 0.3125;
            } else {
                x += level.random.nextFloat() * 0.625 - 0.3125;
            }

            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.0, 0.0);
        }
    }

    public static ItemStack getCellFromFluid(Fluid fluid) {
        return IUItem.fluidCell.getItem().getItemStack(fluid);
    }

    public static void tick(InventoryOutput slot, BlockEntityBase tile) {

        for (Direction facing1 : facings) {
            BlockPos pos = tile.getBlockPos().offset(facing1.getNormal());
            final BlockEntity tile1 = tile.getWorld().getBlockEntity(pos);
            if (tile1 instanceof BlockEntityInventory) {
                BlockEntityInventory inventory = (BlockEntityInventory) tile1;
                for (Inventory invSlot : inventory.getInputSlots()) {
                    if (invSlot.acceptAllOrIndex()) {
                        cycle2:
                        for (int j = 0; j < slot.size(); j++) {
                            ItemStack output = slot.get(j);
                            if (output.isEmpty()) {
                                continue;
                            }
                            if (invSlot.canPlaceItem(0, output)) {
                                for (int jj = 0; jj < invSlot.size(); jj++) {
                                    if (output.isEmpty()) {
                                        continue cycle2;
                                    }
                                    ItemStack input = invSlot.get(jj);
                                    if (input.isEmpty()) {
                                        if (invSlot.add(output)) {
                                            slot.set(j, ItemStack.EMPTY);
                                            output = ItemStack.EMPTY;
                                        }
                                    } else {
                                        if (!ModUtils.checkItemEquality(input, output)) {
                                            continue;
                                        }
                                        int maxCount = Math.min(input.getMaxStackSize() - input.getCount(), output.getCount());
                                        if (maxCount > 0) {
                                            input.grow(maxCount);
                                            output.shrink(maxCount);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        cycle3:
                        for (int jj = 0; jj < slot.size(); jj++) {

                            for (int j = 0; j < invSlot.size(); j++) {
                                ItemStack output = slot.get(jj);
                                if (output.isEmpty()) {
                                    continue cycle3;
                                }
                                ItemStack input = invSlot.get(j);

                                if (input.isEmpty()) {
                                    if (invSlot.canPlaceItem(j, output)) {
                                        if (invSlot.add(output)) {
                                            slot.set(jj, ItemStack.EMPTY);
                                            output = ItemStack.EMPTY;
                                        }
                                    }
                                } else {
                                    if (!ModUtils.checkItemEquality(input, output)) {
                                        continue;
                                    }
                                    int maxCount = Math.min(input.getMaxStackSize() - input.getCount(), output.getCount());
                                    if (maxCount > 0) {
                                        input.grow(maxCount);
                                        output.shrink(maxCount);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
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

                    ItemStack stack1;
                    if (!(handler instanceof Container)) {
                        took = took.copy();
                        stack1 = insertItem(handler, took, true, slots);
                        if (stack1.isEmpty()) {
                            slot.set(j, ItemStack.EMPTY);
                            insertItem(handler, took, false, slots);
                        } else if (stack1 != took) {
                            int count = slot.get(j).getCount() - stack1.getCount();
                            slot.get(j).shrink(count);
                            stack1.setCount(count);
                            insertItem(handler, stack1, false, slots);
                        }
                    } else {
                        stack1 = insertItem1(handler, took, true, slots);
                        if (stack1.isEmpty()) {
                            slot.set(j, ItemStack.EMPTY);
                            insertItem1(handler, took, false, slots);
                        } else if (stack1 != took) {
                            int count = slot.get(j).getCount() - stack1.getCount();
                            slot.get(j).shrink(count);
                            stack1.setCount(count);
                            insertItem1(handler, stack1, false, slots);
                        }
                    }

                }
            }
        }

    }

    @Nonnull
    public static ItemStack insertItem1(IItemHandler dest, @Nonnull ItemStack stack, boolean simulate, int slot) {
        if (dest == null || stack.isEmpty()) {
            return stack;
        }
        slot = Math.min(slot, dest.getSlots());
        for (int i = 0; i < slot; i++) {
            final ItemStack stack2 = insertItem2(dest, i, stack, simulate);
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            } else if (stack2 != stack) {
                return stack2;
            }
        }

        return stack;
    }

    public static boolean canItemStacksStack(@Nonnull ItemStack a, @Nonnull ItemStack b) {
        if (a.isEmpty() || !a.is(b.getItem()) || a.hasTag() != b.hasTag()) {
            return false;
        }

        return (!a.hasTag() || a.getTag().equals(b.getTag()));
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
                    ItemStack copy = stack.split(m);
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
                    ((SidedInvWrapper) dest).setStackInSlot(slot, stack.split(m));
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


    @NotNull
    public static FluidActionResult tryFillContainer(@NotNull ItemStack container, IFluidHandler fluidSource, int maxAmount, @org.jetbrains.annotations.Nullable Player player, boolean doFill) {
        ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1);
        IFluidHandlerItem containerFluidHandler = containerCopy.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse((IFluidHandlerItem) containerCopy.getItem().initCapabilities(containerCopy, containerCopy.getTag()));
        for (int i = 0; i < fluidSource.getTanks(); i++) {
            FluidStack simulatedTransfer = tryFluidTransfer(containerFluidHandler, fluidSource, maxAmount, false, i);
            if (!simulatedTransfer.isEmpty()) {
                if (doFill) {
                    tryFluidTransfer(containerFluidHandler, fluidSource, maxAmount, true, i);
                    if (player != null) {
                        SoundEvent soundevent = simulatedTransfer.getFluid().getFluidType().getSound(simulatedTransfer, SoundActions.BUCKET_FILL);
                        player.level.playSound(null, player.getX(), player.getY() + 0.5, player.getZ(), soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                } else {
                    // We are acting on a COPY of the stack, so performing changes on the source is acceptable even if we are simulating.
                    // We need to perform the change otherwise the call to getContainer() will be incorrect.
                    containerFluidHandler.fill(simulatedTransfer, IFluidHandler.FluidAction.EXECUTE);
                }

                ItemStack resultContainer = containerFluidHandler.getContainer();
                return new FluidActionResult(resultContainer);
            }
        }
        return FluidActionResult.FAILURE;
    }

    @NotNull
    public static FluidActionResult tryFillContainerAndStow(@NotNull ItemStack container, IFluidHandler fluidSource, IItemHandler inventory, int maxAmount, @org.jetbrains.annotations.Nullable Player player, boolean doFill) {
        if (container.isEmpty()) {
            return FluidActionResult.FAILURE;
        }

        if (player != null && player.getAbilities().instabuild) {
            FluidActionResult filledReal = tryFillContainer(container, fluidSource, maxAmount, player, doFill);
            if (filledReal.isSuccess()) {
                return new FluidActionResult(container); // creative mode: item does not change
            }
        } else if (container.getCount() == 1) // don't need to stow anything, just fill the container stack
        {
            FluidActionResult filledReal = tryFillContainer(container, fluidSource, maxAmount, player, doFill);
            if (filledReal.isSuccess()) {
                return filledReal;
            }
        } else {
            FluidActionResult filledSimulated = tryFillContainer(container, fluidSource, maxAmount, player, false);
            if (filledSimulated.isSuccess()) {
                // check if we can give the itemStack to the inventory
                ItemStack remainder = ItemHandlerHelper.insertItemStacked(inventory, filledSimulated.getResult(), true);
                if (remainder.isEmpty() || player != null) {
                    FluidActionResult filledReal = tryFillContainer(container, fluidSource, maxAmount, player, doFill);
                    remainder = ItemHandlerHelper.insertItemStacked(inventory, filledReal.getResult(), !doFill);

                    // give it to the player or drop it at their feet
                    if (!remainder.isEmpty() && player != null && doFill) {
                        ItemHandlerHelper.giveItemToPlayer(player, remainder);
                    }

                    ItemStack containerCopy = container.copy();
                    containerCopy.shrink(1);
                    return new FluidActionResult(containerCopy);
                }
            }
        }

        return FluidActionResult.FAILURE;
    }

    public static boolean interactWithFluidHandler(
            @Nonnull Player player,
            @Nonnull InteractionHand hand,
            @Nonnull IFluidHandler handler
    ) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(hand);
        Preconditions.checkNotNull(handler);

        ItemStack heldItem = player.getItemInHand(hand);
        if (!heldItem.isEmpty()) {

            IItemHandler playerInventory = player.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(new InvWrapper(player.getInventory()));
            if (playerInventory != null) {
                FluidActionResult fluidActionResult = tryFillContainerAndStow(heldItem, handler, playerInventory,
                        Integer.MAX_VALUE, player, true
                );
                if (!fluidActionResult.isSuccess()) {
                    final FluidStack stack = null;
                    int sizeTanks = handler.getTanks();
                    int capacity = -1;
                    @NotNull ItemStack stack1 = ItemHandlerHelper.copyStackWithSize(heldItem, 1);
                    IFluidHandlerItem containerFluidHandler = FluidHandlerFix.getFluidHandler(stack1);
                    for (int i = 0; i < sizeTanks; i++) {
                        if ((handler.getFluidInTank(i).isEmpty() && handler.fill(containerFluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) > 0) || (!handler.getFluidInTank(i).isEmpty() && handler.getFluidInTank(i).isFluidEqual(containerFluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE)) && handler.fill(containerFluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) > 0)) {
                            capacity = handler.getTankCapacity(i) - (handler.getFluidInTank(i).isEmpty() ? 0 :
                                    handler.getFluidInTank(i).getAmount());

                        }
                    }
                    if (capacity <= 0) {
                        fluidActionResult = FluidActionResult.FAILURE;
                    } else {
                        fluidActionResult = tryEmptyContainerAndStow(heldItem, handler, playerInventory,
                                capacity, player,
                                true
                        );
                    }
                }

                if (fluidActionResult.isSuccess()) {
                    player.setItemInHand(hand, fluidActionResult.getResult());
                    return true;
                }
            }
        }
        return false;
    }

    @NotNull
    public static FluidActionResult tryEmptyContainerAndStow(@NotNull ItemStack container, IFluidHandler fluidDestination, IItemHandler inventory, int maxAmount, @org.jetbrains.annotations.Nullable Player player, boolean doDrain) {
        if (container.isEmpty()) {
            return FluidActionResult.FAILURE;
        }

        if (player != null && player.getAbilities().instabuild) {
            FluidActionResult emptiedReal = tryEmptyContainer(container, fluidDestination, maxAmount, player, doDrain);
            if (emptiedReal.isSuccess()) {
                return new FluidActionResult(container); // creative mode: item does not change
            }
        } else if (container.getCount() == 1) // don't need to stow anything, just fill and edit the container stack
        {
            FluidActionResult emptiedReal = tryEmptyContainer(container, fluidDestination, maxAmount, player, doDrain);
            if (emptiedReal.isSuccess()) {
                return emptiedReal;
            }
        } else {
            FluidActionResult emptiedSimulated = tryEmptyContainer(container, fluidDestination, maxAmount, player, false);
            if (emptiedSimulated.isSuccess()) {
                // check if we can give the itemStack to the inventory
                ItemStack remainder = ItemHandlerHelper.insertItemStacked(inventory, emptiedSimulated.getResult(), true);
                if (remainder.isEmpty() || player != null) {
                    FluidActionResult emptiedReal = tryEmptyContainer(container, fluidDestination, maxAmount, player, doDrain);
                    remainder = ItemHandlerHelper.insertItemStacked(inventory, emptiedReal.getResult(), !doDrain);

                    // give it to the player or drop it at their feet
                    if (!remainder.isEmpty() && player != null && doDrain) {
                        ItemHandlerHelper.giveItemToPlayer(player, remainder);
                    }

                    ItemStack containerCopy = container.copy();
                    containerCopy.shrink(1);
                    return new FluidActionResult(containerCopy);
                }
            }
        }

        return FluidActionResult.FAILURE;
    }

    @NotNull
    public static FluidActionResult tryEmptyContainer(@NotNull ItemStack container, IFluidHandler fluidDestination, int maxAmount, @org.jetbrains.annotations.Nullable Player player, boolean doDrain) {
        ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1); // do not modify the input
        IFluidHandlerItem containerFluidHandler;
        containerFluidHandler = FluidHandlerFix.getFluidHandler(containerCopy);
        if (containerFluidHandler != null) {
            FluidStack transfer = tryFluidTransfer(fluidDestination, containerFluidHandler, maxAmount, doDrain);
            if (transfer.isEmpty())
                return FluidActionResult.FAILURE;
            if (!doDrain) {
                // We are acting on a COPY of the stack, so performing changes on the source is acceptable even if we are simulating.
                // We need to perform the change otherwise the call to getContainer() will be incorrect.
                containerFluidHandler.drain(transfer, IFluidHandler.FluidAction.EXECUTE);
            }

            if (doDrain && player != null) {
                SoundEvent soundevent = transfer.getFluid().getFluidType().getSound(transfer, SoundActions.BUCKET_EMPTY);
                player.level.playSound(null, player.getX(), player.getY() + 0.5, player.getZ(), soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            ItemStack resultContainer = containerFluidHandler.getContainer();
            return new FluidActionResult(resultContainer);
        }
        return FluidActionResult.FAILURE;
    }

    @NotNull
    public static FluidStack tryFluidTransfer(IFluidHandler fluidDestination, IFluidHandler fluidSource, int maxAmount, boolean doTransfer, int index) {
        FluidStack stack = fluidSource.getFluidInTank(index).copy();
        if (stack.isEmpty())
            return FluidStack.EMPTY;
        stack.setAmount(Math.min(stack.getAmount(), maxAmount));
        FluidStack drainable = fluidSource.drain(stack, IFluidHandler.FluidAction.SIMULATE);
        if (!drainable.isEmpty()) {
            return tryFluidTransfer_Internal(fluidDestination, fluidSource, drainable, doTransfer);
        }
        return FluidStack.EMPTY;
    }

    @NotNull
    public static FluidStack tryFluidTransfer(IFluidHandler fluidDestination, IFluidHandler fluidSource, int maxAmount, boolean doTransfer) {
        FluidStack drainable = fluidSource.drain(maxAmount, IFluidHandler.FluidAction.SIMULATE);
        if (!drainable.isEmpty()) {
            return tryFluidTransfer_Internal(fluidDestination, fluidSource, drainable, doTransfer);
        }
        return FluidStack.EMPTY;
    }

    @NotNull
    public static FluidStack tryFluidTransfer(IFluidHandler fluidDestination, IFluidHandler fluidSource, FluidStack resource, boolean doTransfer) {
        FluidStack drainable = fluidSource.drain(resource, IFluidHandler.FluidAction.SIMULATE);
        if (!drainable.isEmpty() && resource.isFluidEqual(drainable)) {
            return tryFluidTransfer_Internal(fluidDestination, fluidSource, drainable, doTransfer);
        }
        return FluidStack.EMPTY;
    }

    private static FluidStack tryFluidTransfer_Internal(IFluidHandler fluidDestination, IFluidHandler fluidSource, FluidStack drainable, boolean doTransfer) {
        int fillableAmount = fluidDestination.fill(drainable, IFluidHandler.FluidAction.SIMULATE);
        if (fillableAmount > 0) {
            drainable.setAmount(fillableAmount);
            if (doTransfer) {
                FluidStack drained = fluidSource.drain(drainable, IFluidHandler.FluidAction.EXECUTE);
                if (!drained.isEmpty()) {
                    drained.setAmount(fluidDestination.fill(drained, IFluidHandler.FluidAction.EXECUTE));
                    return drained;
                }
            } else {
                return drainable;
            }
        }
        return FluidStack.EMPTY;
    }


    public static Direction getFacingFromTwoPositions(BlockPos fromPos, BlockPos toPos) {
        int dx = toPos.getX() - fromPos.getX();
        int dy = toPos.getY() - fromPos.getY();
        int dz = toPos.getZ() - fromPos.getZ();
        if (dx > 0) {
            return Direction.EAST;
        } else if (dx < 0) {
            return Direction.WEST;
        } else if (dy > 0) {
            return Direction.DOWN;
        } else if (dy < 0) {
            return Direction.UP;
        } else if (dz > 0) {
            return Direction.SOUTH;
        } else if (dz < 0) {
            return Direction.NORTH;
        }


        return Direction.DOWN;
    }

    public static boolean inChanceOre(VeinType veinType, BlockState state) {
        for (ChanceOre chanceOre : veinType.getOres()) {
            if (chanceOre.getBlock() == state) {
                return true;
            }
        }
        return false;
    }

    public static List<Component> getInformationFromOre(BlockState state) {
        List<Component> stringList = new ArrayList<>();
        for (VeinType vein : WorldBaseGen.veinTypes) {
            if ((vein.getHeavyOre() != null && vein.getHeavyOre().getStateMeta(vein.getMeta()) == state) || inChanceOre(
                    vein,
                    state
            )) {
                final Component s = (vein.getHeavyOre() != null ?
                        new ItemStack(vein.getHeavyOre().getBlock(), 1).getDisplayName() :
                        new ItemStack(vein.getOres().get(0).getBlock().getBlock(), 1
                        ).getDisplayName());
                stringList.add(s);
            }
        }
        return stringList;
    }
}
