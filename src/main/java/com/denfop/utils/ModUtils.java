package com.denfop.utils;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.radiationsystem.EnumCoefficient;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.invslot.InvSlot;
import com.denfop.items.bags.ItemStackBags;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.quarry.QuarryItem;
import com.denfop.world.WorldBaseGen;
import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.VeinType;
import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ModUtils {

    public static final Set<String> ignoredNbtKeys = new HashSet<>(Arrays.asList("damage", "charge", "energy", "advDmg"));

    public static Logger log;
    public static EnumFacing[] facings = EnumFacing.values();
    public static ItemStack emptyStack = ItemStack.EMPTY;
    public static Set<EnumFacing> noFacings = Collections.emptySet();
    public static Set<EnumFacing> onlyNorth;
    public static Set<EnumFacing> horizontalFacings;
    public static Set<EnumFacing> verticalFacings;
    public static Set<EnumFacing> downSideFacings;
    public static Set<EnumFacing> allFacings;

    static {
        onlyNorth = Collections.unmodifiableSet(EnumSet.of(EnumFacing.NORTH));
        horizontalFacings = Collections.unmodifiableSet(EnumSet.copyOf(Arrays.asList(EnumFacing.HORIZONTALS)));
        verticalFacings = Collections.unmodifiableSet(EnumSet.of(EnumFacing.DOWN, EnumFacing.UP));
        downSideFacings = Collections.unmodifiableSet(EnumSet.complementOf(EnumSet.of(EnumFacing.UP)));
        allFacings = Collections.unmodifiableSet(EnumSet.allOf(EnumFacing.class));
    }

    public static EnumFacing getFacingFromTwoPositions(BlockPos fromPos, BlockPos toPos) {
        int dx = toPos.getX() - fromPos.getX();
        int dy = toPos.getY() - fromPos.getY();
        int dz = toPos.getZ() - fromPos.getZ();
        if (dx > 0) {
            return EnumFacing.EAST;
        } else if (dx < 0) {
            return EnumFacing.WEST;
        } else if (dy > 0) {
            return EnumFacing.DOWN;
        } else if (dy < 0) {
            return EnumFacing.UP;
        } else if (dz > 0) {
            return EnumFacing.SOUTH;
        } else if (dz < 0) {
            return EnumFacing.NORTH;
        }


        return EnumFacing.DOWN;
    }

    public static boolean inChanceOre(VeinType veinType, IBlockState state) {
        for (ChanceOre chanceOre : veinType.getOres()) {
            if (chanceOre.getBlock() == state) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getInformationFromOre(IBlockState state) {
        List<String> stringList = new ArrayList<>();
        for (VeinType vein : WorldBaseGen.veinTypes) {
            if ((vein.getHeavyOre() != null && vein.getHeavyOre().getStateMeta(vein.getMeta()) == state) || inChanceOre(
                    vein,
                    state
            )) {
                final String s = (vein.getHeavyOre() != null ?
                        new ItemStack(vein.getHeavyOre().getBlock(), 1, vein.getMeta()).getDisplayName() :
                        new ItemStack(vein.getOres().get(0).getBlock().getBlock(), 1,
                                vein.getOres().get(0).getMeta()
                        ).getDisplayName());
                stringList.add(s);
            }
        }
        return stringList;
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
        } else {
            FluidStack liquid = FluidUtil.getFluidContained(stack);
            boolean isLava = liquid != null && liquid.amount > 0 && liquid.getFluid() == FluidRegistry.LAVA;
            if (isLava && !allowLava) {
                return 0;
            } else {
                int ret = TileEntityFurnace.getItemBurnTime(stack);
                return isLava ? ret / 10 : ret;
            }
        }
    }

    public static boolean interactWithFluidHandler(
            @Nonnull EntityPlayer player,
            @Nonnull EnumHand hand,
            @Nonnull IFluidHandler handler
    ) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(hand);
        Preconditions.checkNotNull(handler);

        ItemStack heldItem = player.getHeldItem(hand);
        if (!heldItem.isEmpty()) {
            IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (playerInventory != null) {
                FluidActionResult fluidActionResult = FluidUtil.tryFillContainerAndStow(heldItem, handler, playerInventory,
                        Integer.MAX_VALUE, player, true
                );
                if (!fluidActionResult.isSuccess()) {
                    final FluidStack stack = null;
                    final IFluidTankProperties[] tanks = handler.getTankProperties();
                    int capacity = -1;
                    IFluidHandlerItem containerFluidHandler =
                            FluidUtil.getFluidHandler(ItemHandlerHelper.copyStackWithSize(heldItem, 1));
                    for (IFluidTankProperties fluidTankProperties : tanks) {
                        if ((fluidTankProperties.getContents() == null && fluidTankProperties.canFill() && fluidTankProperties.canFillFluidType(
                                containerFluidHandler.drain(Integer.MAX_VALUE,
                                        false))) || (fluidTankProperties.getContents() != null && fluidTankProperties.canFill() && fluidTankProperties.canFillFluidType(
                                containerFluidHandler.drain(Integer.MAX_VALUE, false)))) {
                            capacity = fluidTankProperties.getCapacity() - (fluidTankProperties.getContents() == null ? 0 :
                                    fluidTankProperties.getContents().amount);

                        }
                    }
                    if (capacity <= 0) {
                        fluidActionResult = FluidActionResult.FAILURE;
                    } else {
                        fluidActionResult = FluidUtil.tryEmptyContainerAndStow(heldItem, handler, playerInventory,
                                capacity, player,
                                true
                        );
                    }
                }

                if (fluidActionResult.isSuccess()) {
                    player.setHeldItem(hand, fluidActionResult.getResult());
                    return true;
                }
            }
        }
        return false;
    }

    public static ItemStack get(EntityPlayer player, EnumHand hand) {
        return player.getHeldItem(hand);
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

    public static boolean storeInventoryItem(ItemStack stack, EntityPlayer player, boolean simulate) {
        if (!simulate) {
            return player.inventory.addItemStackToInventory(stack);
        } else {
            int sizeLeft = getSize(stack);
            int maxStackSize = Math.min(player.inventory.getInventoryStackLimit(), stack.getMaxStackSize());

            for (int i = 0; i < player.inventory.mainInventory.size() && sizeLeft > 0; ++i) {
                ItemStack invStack = player.inventory.mainInventory.get(i);
                if (isEmpty(invStack)) {
                    sizeLeft -= maxStackSize;
                } else if (checkItemEqualityStrict(stack, invStack) && getSize(invStack) < maxStackSize) {
                    sizeLeft -= maxStackSize - getSize(invStack);
                }
            }

            return sizeLeft <= 0;
        }
    }


    public static void dropAsEntity(World world, BlockPos pos, ItemStack stack) {
        if (!isEmpty(stack)) {
            double f = 0.7;
            double dx = (double) world.rand.nextFloat() * f + (1.0 - f) * 0.5;
            double dy = (double) world.rand.nextFloat() * f + (1.0 - f) * 0.5;
            double dz = (double) world.rand.nextFloat() * f + (1.0 - f) * 0.5;
            EntityItem entityItem = new EntityItem(
                    world,
                    (double) pos.getX() + dx,
                    (double) pos.getY() + dy,
                    (double) pos.getZ() + dz,
                    stack.copy()
            );
            entityItem.setDefaultPickupDelay();
            world.spawnEntity(entityItem);
        }
    }

    public static void dropAsEntity(World world, BlockPos pos, ItemStack stack, int count) {
        if (!isEmpty(stack)) {
            double f = 0.7;
            double dx = (double) world.rand.nextFloat() * f + (1.0 - f) * 0.5;
            double dy = (double) world.rand.nextFloat() * f + (1.0 - f) * 0.5;
            double dz = (double) world.rand.nextFloat() * f + (1.0 - f) * 0.5;
            final ItemStack stack1 = stack.copy();
            stack1.setCount(count);
            EntityItem entityItem = new EntityItem(
                    world,
                    (double) pos.getX() + dx,
                    (double) pos.getY() + dy,
                    (double) pos.getZ() + dz,
                    stack1
            );
            entityItem.setDefaultPickupDelay();
            world.spawnEntity(entityItem);
        }
    }

    public static boolean checkItemEquality(ItemStack a, ItemStack b) {
        return isEmpty(a) && isEmpty(b) || !isEmpty(a) && !isEmpty(b) && a.getItem() == b.getItem() && (!a.getHasSubtypes() || a.getMetadata() == b.getMetadata()) && checkNbtEquality(
                a,
                b
        );
    }

    public static boolean checkItemEquality(ItemStack a, Item b) {
        return isEmpty(a) && b == null || !isEmpty(a) && b != null && a.getItem() == b;
    }

    public static boolean checkItemEqualityStrict(ItemStack a, ItemStack b) {
        return isEmpty(a) && isEmpty(b) || !isEmpty(a) && !isEmpty(b) && a.isItemEqual(b) && checkNbtEqualityStrict(a, b);
    }

    private static boolean checkNbtEquality(ItemStack a, ItemStack b) {
        return checkNbtEquality(a.getTagCompound(), b.getTagCompound());
    }

    public static boolean checkNbtEquality(NBTTagCompound a, NBTTagCompound b) {
        if (a == b) {
            return true;
        } else {
            Set<String> keysA = a != null ? a.getKeySet() : Collections.emptySet();
            Set<String> keysB = b != null ? b.getKeySet() : Collections.emptySet();
            if (keysA.isEmpty() && keysB.isEmpty()) {
                return true;
            }
            Set<String> toCheck = new HashSet(Math.max(keysA.size(), keysB.size()));
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
            } while (a.getTag(key).equals(b.getTag(key)));

            return false;
        }
    }

    public static boolean checkNbtEqualityStrict(ItemStack a, ItemStack b) {
        NBTTagCompound nbtA = a.getTagCompound();
        NBTTagCompound nbtB = b.getTagCompound();
        if (nbtA == nbtB) {
            return true;
        } else {
            return nbtA != null && nbtA.equals(nbtB);
        }
    }

    public static boolean isEmpty(ItemStack stack) {
        return stack == emptyStack || stack == null || stack.getItem() == null || stack.getCount() <= 0;
    }

    public static boolean isEmpty(EntityPlayer player, EnumHand hand) {
        return isEmpty(player.getHeldItem(hand));
    }

    public static int getSize(ItemStack stack) {
        return isEmpty(stack) ? 0 : stack.getCount();
    }

    public static void setRawMeta(ItemStack stack, int meta) {
        if (meta < 0) {
            throw new IllegalArgumentException("negative meta");
        } else {
            Items.DYE.setDamage(stack, meta);
        }
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
        final ItemStack stack = new ItemStack(IUItem.fluidCell, 1, 0);
        final IFluidHandlerItem fluidDestination = FluidUtil.getFluidHandler(
                stack);
        Fluid liquid = FluidRegistry.getFluid(name);
        final FluidStack drainable = new FluidStack(liquid, 1000);
        fluidDestination.fill(drainable, true);
        return stack;
    }

    public static ItemStack getCellFromFluid(Fluid liquid) {
        final ItemStack stack = new ItemStack(IUItem.fluidCell, 1, 0);
        final IFluidHandlerItem fluidDestination = FluidUtil.getFluidHandler(
                stack);
        final FluidStack drainable = new FluidStack(liquid, 1000);
        fluidDestination.fill(drainable, true);
        return stack;
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
            if (tile1 instanceof TileEntityInventory) {
                TileEntityInventory inventory = (TileEntityInventory) tile1;
                for (InvSlot invSlot : inventory.getInputSlots()) {
                    if (invSlot.acceptAllOrIndex()) {
                        cycle2:
                        for (int j = 0; j < slot.size(); j++) {
                            ItemStack output = slot.get(j);
                            if (output.isEmpty()) {
                                continue;
                            }
                            if (invSlot.accepts(output, 0)) {
                                for (int jj = 0; jj < invSlot.size(); jj++) {
                                    if (output.isEmpty()) {
                                        continue cycle2;
                                    }
                                    ItemStack input = invSlot.get(jj);
                                    if (input.isEmpty()) {
                                        if (invSlot.add(output)) {
                                            slot.put(j, ItemStack.EMPTY);
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
                                    if (invSlot.accepts(output, j)) {
                                        if (invSlot.add(output)) {
                                            slot.put(jj, ItemStack.EMPTY);
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
                    if (!(handler instanceof ISidedInventory)) {
                        took = took.copy();
                        stack1 = insertItem(handler, took, true, slots);
                        if (stack1.isEmpty()) {
                            slot.put(j, ItemStack.EMPTY);
                            insertItem(handler, took, false, slots);
                        } else if (stack1 != took) {
                            slot.get(j).shrink(stack1.getCount());
                            insertItem(handler, stack1, false, slots);
                        }
                    } else {
                        stack1 = insertItem1(handler, took, true, slots);
                        if (stack1.isEmpty()) {
                            slot.put(j, ItemStack.EMPTY);
                            insertItem1(handler, took, false, slots);
                        } else if (stack1 != took) {
                            slot.get(j).shrink(stack1.getCount());
                            insertItem1(handler, stack1, false, slots);
                        }
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
                    handHeldBags.put(i, ItemStack.EMPTY);
                    insertItem(tile, took, false, slots);
                } else if (stack != took) {
                    handHeldBags.get(i).shrink(stack.getCount());
                    insertItem1(tile, stack, false, slots);
                }
            } else {
                final ItemStack stack = insertItem1(tile, took, true, slots);
                if (stack.isEmpty()) {
                    handHeldBags.put(i, ItemStack.EMPTY);
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


    public static ResourceLocation getName(Block block) {
        return Block.REGISTRY.getNameForObject(block);
    }

    public static Item getItem(String name) {
        if (name == null) {
            throw new NullPointerException("null name");
        } else {
            return getItem(new ResourceLocation(name));
        }
    }

    public static Item getItem(ResourceLocation loc) {
        return Item.REGISTRY.getObject(loc);
    }

    public static ResourceLocation getName(Item item) {
        return Item.REGISTRY.getNameForObject(item);
    }


    public static boolean matchesNBT(NBTTagCompound subject, NBTTagCompound target) {
        if (subject == null) {
            return target == null || target.hasNoTags();
        } else if (target == null) {
            return true;
        } else {
            Iterator<String> var2 = target.getKeySet().iterator();
            Set<String> var3 = subject.getKeySet();

            NBTBase targetNBT;
            NBTBase subjectNBT;
            do {
                if (!var2.hasNext()) {
                    return true;
                }

                String key = var2.next();
                targetNBT = target.getTag(key);
                if (!subject.hasKey(key) || targetNBT.getId() != subject.getTagId(key)) {
                    return false;
                }

                subjectNBT = subject.getTag(key);
            } while (targetNBT.equals(subjectNBT));

            return false;
        }
    }

    public static void showFlames(World world, BlockPos pos, EnumFacing facing) {
        if (world.rand.nextInt(8) == 0) {
            double x = (double) pos.getX() + ((double) facing.getFrontOffsetX() * 1.04 + 1.0) / 2.0;
            double y = (double) pos.getY() + (double) world.rand.nextFloat() * 0.375;
            double z = (double) pos.getZ() + ((double) facing.getFrontOffsetZ() * 1.04 + 1.0) / 2.0;
            if (facing.getAxis() == EnumFacing.Axis.X) {
                z += (double) world.rand.nextFloat() * 0.625 - 0.3125;
            } else {
                x += (double) world.rand.nextFloat() * 0.625 - 0.3125;
            }

            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0, 0.0, 0.0);
            world.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0.0, 0.0, 0.0);
        }
    }

    public static void dropAsEntity(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        if (!isEmpty(stack)) {
            double f = 0.7;
            double dx = (double) world.rand.nextFloat() * f + (1.0 - f) * 0.5;
            double dy = (double) world.rand.nextFloat() * f + (1.0 - f) * 0.5;
            double dz = (double) world.rand.nextFloat() * f + (1.0 - f) * 0.5;
            EntityItem entityItem = new EntityItem(
                    world,
                    (double) pos.getX() + dx,
                    (double) pos.getY() + dy,
                    (double) pos.getZ() + dz,
                    stack.copy()
            );
            entityItem.setDefaultPickupDelay();
            world.spawnEntity(entityItem);
            if (!player.world.isRemote) {
                ((EntityPlayerMP) player).connection.sendPacket(new SPacketEntityTeleport(entityItem));
            }
            entityItem.setPickupDelay(0);
        }
    }

}
