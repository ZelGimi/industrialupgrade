package com.denfop.api.storage.cell;

import com.denfop.IUCore;
import com.denfop.api.storage.StorageStack;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuAgriculturalAnalyzer;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemStackInventory;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.ModUtils;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.*;

public class ItemStackCell extends ItemStackInventory implements ICell {
    private static final String TAG_ITEMS_BY_SLOT = "ItemsBySlot";
    private static final String TAG_FLUIDS_BY_SLOT = "FluidsBySlot";
    public static CompoundTag EMPTY = new CompoundTag();
    public final int inventorySize;
    public final ItemStack itemStack1;
    protected final FluidStack[] inventory_fluid;
    private final CellInfo cellInfo;
    private final int maxCapacity;
    private final boolean isFluid;
    private final Deque<Integer> freeSlots = new ArrayDeque<>();
    private final Set<Integer> dirtyContentSlots = new HashSet<>();
    public Map<String, List<StorageStack>> storageMap = new HashMap<>();
    public Map<Integer, Integer> stacksForCraft = new HashMap<>();
    boolean save = false;
    private int storage;
    private boolean rewriteAllContentToCompound = false;

    public ItemStackCell(ItemStack stack, CellInfo cellInfo) {
        super(null, stack, cellInfo.typeCell() == TypeCell.FLUID ? 100 : 300);
        this.inventorySize = cellInfo.typeCell() == TypeCell.FLUID ? 100 : 300;
        this.inventory_fluid = new FluidStack[inventorySize];
        Arrays.fill(this.inventory_fluid, FluidStack.EMPTY);
        this.itemStack1 = stack;
        this.cellInfo = cellInfo;
        this.maxCapacity = cellInfo.capacity();
        this.isFluid = cellInfo.typeCell() == TypeCell.FLUID;

        CompoundTag nbt = ModUtils.nbt(containerStack);
        if (!nbt.contains("uid", 3)) {
            nbt.putInt("uid", IUCore.random.nextInt());
        }
        loadForStacks(nbt);

        boolean[] occupied = new boolean[this.inventorySize];

        if (this.isFluid) {
            if (nbt.contains(TAG_FLUIDS_BY_SLOT, Tag.TAG_COMPOUND)) {
                loadFluidsFromCompound(nbt.getCompound(TAG_FLUIDS_BY_SLOT), occupied);
            } else {
                ListTag contentList = nbt.getList("Fluids", Tag.TAG_COMPOUND);
                loadFluidsFromLegacyList(contentList, occupied);
                if (!contentList.isEmpty()) {
                    this.rewriteAllContentToCompound = true;
                    this.save = true;
                }
            }
        } else {
            if (nbt.contains(TAG_ITEMS_BY_SLOT, Tag.TAG_COMPOUND)) {
                loadItemsFromCompound(nbt.getCompound(TAG_ITEMS_BY_SLOT), occupied);
            } else {
                ListTag contentList = nbt.getList("Items", Tag.TAG_COMPOUND);
                loadItemsFromLegacyList(contentList, occupied);
                if (!contentList.isEmpty()) {
                    this.rewriteAllContentToCompound = true;
                    this.save = true;
                }
            }
        }

        for (int i = 0; i < this.inventorySize; i++) {
            if (!occupied[i]) {
                freeSlots.add(i);
            }
        }
    }

    private void markSlotDirty(int slot) {
        if (slot < 0 || slot >= this.inventorySize) {
            return;
        }
        this.dirtyContentSlots.add(slot);
        this.save = true;
    }

    private void loadItemsFromCompound(CompoundTag itemsTag, boolean[] occupied) {
        for (String key : itemsTag.getAllKeys()) {
            int slot;
            try {
                slot = Integer.parseInt(key);
            } catch (NumberFormatException e) {
                continue;
            }

            if (slot < 0 || slot >= this.inventory.length) {
                continue;
            }

            CompoundTag slotNbt = itemsTag.getCompound(key);
            ItemStack is = ItemStack.of(slotNbt);
            if (is.isEmpty()) {
                continue;
            }

            int fullCount = slotNbt.contains("full_count", Tag.TAG_INT) ? slotNbt.getInt("full_count") : is.getCount();
            is.setCount(fullCount);

            this.inventory[slot] = is;
            occupied[slot] = true;
            this.storage += fullCount;

            ResourceLocation rl = Registry.ITEM.getKey(is.getItem());
            List<StorageStack> list = storageMap.computeIfAbsent(rl.toString(), k -> new ArrayList<>());
            list.add(new StorageStack(is.getTag(), fullCount, slot));
        }
    }

    private void loadItemsFromLegacyList(ListTag contentList, boolean[] occupied) {
        for (int i = 0; i < contentList.size(); ++i) {
            CompoundTag slotNbt = contentList.getCompound(i);
            int slot = slotNbt.getByte("Slot");

            if (slot < 0 || slot >= this.inventory.length) {
                continue;
            }

            ItemStack is = ItemStack.of(slotNbt);
            if (is.isEmpty()) {
                continue;
            }

            occupied[slot] = true;
            is.setCount(slotNbt.getInt("full_count"));
            this.inventory[slot] = is;
            this.storage += is.getCount();

            ResourceLocation rl = Registry.ITEM.getKey(is.getItem());
            List<StorageStack> list = storageMap.computeIfAbsent(rl.toString(), k -> new ArrayList<>());
            list.add(new StorageStack(is.getTag(), is.getCount(), slot));
        }
    }

    private void loadFluidsFromCompound(CompoundTag fluidsTag, boolean[] occupied) {
        for (String key : fluidsTag.getAllKeys()) {
            int slot;
            try {
                slot = Integer.parseInt(key);
            } catch (NumberFormatException e) {
                continue;
            }

            if (slot < 0 || slot >= this.inventory_fluid.length) {
                continue;
            }

            CompoundTag slotNbt = fluidsTag.getCompound(key);
            FluidStack fs = FluidStack.loadFluidStackFromNBT(slotNbt);
            if (fs.isEmpty()) {
                continue;
            }

            int fullAmount = slotNbt.contains("full_amount", Tag.TAG_INT) ? slotNbt.getInt("full_amount") : fs.getAmount();
            fs.setAmount(fullAmount);

            this.inventory_fluid[slot] = fs;
            occupied[slot] = true;
            this.storage += fullAmount;

            ResourceLocation rl = Registry.FLUID.getKey(fs.getFluid());
            List<StorageStack> list = storageMap.computeIfAbsent(rl.toString(), k -> new ArrayList<>());
            list.add(new StorageStack(fs.getTag(), fullAmount, slot));
        }
    }

    private void loadFluidsFromLegacyList(ListTag contentList, boolean[] occupied) {
        for (int i = 0; i < contentList.size(); ++i) {
            CompoundTag slotNbt = contentList.getCompound(i);
            int slot = slotNbt.getByte("Slot");

            if (slot < 0 || slot >= this.inventory_fluid.length) {
                continue;
            }

            FluidStack fs = FluidStack.loadFluidStackFromNBT(slotNbt);
            if (fs.isEmpty()) {
                continue;
            }

            occupied[slot] = true;
            this.inventory_fluid[slot] = fs;
            this.storage += fs.getAmount();

            ResourceLocation rl = Registry.FLUID.getKey(fs.getFluid());
            List<StorageStack> list = storageMap.computeIfAbsent(rl.toString(), k -> new ArrayList<>());
            list.add(new StorageStack(fs.getTag(), fs.getAmount(), slot));
        }
    }

    private void writeAllItemsToCompound(CompoundTag root) {
        CompoundTag itemsTag = new CompoundTag();

        for (int idx = 0; idx < this.inventory.length; ++idx) {
            ItemStack stack = this.inventory[idx];
            if (!ModUtils.isEmpty(stack)) {
                CompoundTag slotNbt = new CompoundTag();
                slotNbt.putInt("full_count", stack.getCount());
                stack.save(slotNbt);
                slotNbt.putByte("Count", (byte) 127);
                slotNbt.putInt("Slot", idx);
                itemsTag.put(String.valueOf(idx), slotNbt);
            }
        }

        if (itemsTag.isEmpty()) {
            root.remove(TAG_ITEMS_BY_SLOT);
        } else {
            root.put(TAG_ITEMS_BY_SLOT, itemsTag);
        }

        root.remove("Items");
    }

    private void writeDirtyItemsToCompound(CompoundTag root) {
        CompoundTag itemsTag = root.contains(TAG_ITEMS_BY_SLOT, Tag.TAG_COMPOUND)
                ? root.getCompound(TAG_ITEMS_BY_SLOT)
                : new CompoundTag();

        for (Integer slot : dirtyContentSlots) {
            String key = String.valueOf(slot);
            ItemStack stack = this.inventory[slot];

            if (ModUtils.isEmpty(stack)) {
                itemsTag.remove(key);
                continue;
            }

            CompoundTag slotNbt = new CompoundTag();
            slotNbt.putInt("full_count", stack.getCount());
            stack.save(slotNbt);
            slotNbt.putByte("Count", (byte) 127);
            slotNbt.putInt("Slot", slot);
            itemsTag.put(key, slotNbt);
        }

        if (itemsTag.isEmpty()) {
            root.remove(TAG_ITEMS_BY_SLOT);
        } else {
            root.put(TAG_ITEMS_BY_SLOT, itemsTag);
        }
    }

    private void writeAllFluidsToCompound(CompoundTag root) {
        CompoundTag fluidsTag = new CompoundTag();

        for (int idx = 0; idx < this.inventory_fluid.length; ++idx) {
            FluidStack stack = this.inventory_fluid[idx];
            if (!stack.isEmpty()) {
                CompoundTag slotNbt = new CompoundTag();
                stack.writeToNBT(slotNbt);
                slotNbt.putInt("full_amount", stack.getAmount());
                slotNbt.putInt("Slot", idx);
                fluidsTag.put(String.valueOf(idx), slotNbt);
            }
        }

        if (fluidsTag.isEmpty()) {
            root.remove(TAG_FLUIDS_BY_SLOT);
        } else {
            root.put(TAG_FLUIDS_BY_SLOT, fluidsTag);
        }

        root.remove("Fluids");
    }

    private void writeDirtyFluidsToCompound(CompoundTag root) {
        CompoundTag fluidsTag = root.contains(TAG_FLUIDS_BY_SLOT, Tag.TAG_COMPOUND)
                ? root.getCompound(TAG_FLUIDS_BY_SLOT)
                : new CompoundTag();

        for (Integer slot : dirtyContentSlots) {
            String key = String.valueOf(slot);
            FluidStack stack = this.inventory_fluid[slot];

            if (stack == null || stack.isEmpty()) {
                fluidsTag.remove(key);
                continue;
            }

            CompoundTag slotNbt = new CompoundTag();
            stack.writeToNBT(slotNbt);
            slotNbt.putInt("full_amount", stack.getAmount());
            slotNbt.putInt("Slot", slot);
            fluidsTag.put(key, slotNbt);
        }

        if (fluidsTag.isEmpty()) {
            root.remove(TAG_FLUIDS_BY_SLOT);
        } else {
            root.put(TAG_FLUIDS_BY_SLOT, fluidsTag);
        }
    }

    @Override
    public Map<String, List<StorageStack>> getStorageStack() {
        return storageMap;
    }

    @Override
    public ItemStack[] getStacks() {
        return inventory;
    }

    @Override
    public FluidStack[] getFluids() {
        return inventory_fluid;
    }

    public FluidStack addFluid(FluidStack stack) {
        if (stack.isEmpty()) return FluidStack.EMPTY;

        ResourceLocation rl = Registry.FLUID.getKey(stack.getFluid());
        List<StorageStack> list = storageMap.get(rl.toString());
        int requestedMb = stack.getAmount();

        int freeMb = (maxCapacity - storage);
        if (freeMb <= 0) return stack.copy();

        int toInsert = Math.min(requestedMb, freeMb);

        if (list == null) {
            list = new ArrayList<>();
            int slotId = addFluidDirect(stack.getFluid(), stack.getTag(), toInsert, false);
            if (slotId < 0) return stack.copy();
            list.add(new StorageStack(stack.getTag(), toInsert, slotId));
            storageMap.put(rl.toString(), list);
        } else {
            boolean added = false;

            for (StorageStack stored : list) {
                if (ModUtils.compareNbt(stored.getTag(), stack.getTag(), true)) {
                    stored.addCount(toInsert);
                    addFluidDirect(stored.getSlot(), toInsert);
                    added = true;
                    break;
                }
            }

            if (!added) {
                int slotId = addFluidDirect(stack.getFluid(), stack.getTag(), toInsert, false);
                if (slotId < 0) return stack.copy();
                list.add(new StorageStack(stack.getTag(), toInsert, slotId));
                storageMap.put(rl.toString(), list);
            }
        }

        storage += toInsert;
        return new FluidStack(stack.getFluid(), requestedMb - toInsert);
    }

    @Override
    public int removeItemFromListCraft(SameStack key, Integer value) {
        ResourceLocation rl = Registry.ITEM.getKey(key.getStack().getItem());
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return value;
        }

        for (StorageStack stored : list) {
            if (value == 0) {
                break;
            }

            if (ModUtils.compareNbt(stored.getTag(), key.getTag(), true)) {
                int forCraft = this.stacksForCraft.getOrDefault(stored.getSlot(), 0);
                int remove = Math.min(forCraft, value);
                if (remove > 0) {
                    value -= remove;
                    int remaining = forCraft - remove;
                    if (remaining > 0) {
                        this.stacksForCraft.put(stored.getSlot(), remaining);
                    } else {
                        this.stacksForCraft.remove(stored.getSlot());
                    }
                    this.save = true;
                }
            }
        }

        return value;
    }

    @Override
    public int removeFluidFromListCraft(SameStack key, Integer value) {
        ResourceLocation rl = Registry.FLUID.getKey(key.getFluidStack().getFluid());
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return value;
        }

        for (StorageStack stored : list) {
            if (value == 0) {
                break;
            }

            if (ModUtils.compareNbt(stored.getTag(), key.getFluidStack().getTag(), true)) {
                int forCraft = this.stacksForCraft.getOrDefault(stored.getSlot(), 0);
                int remove = Math.min(forCraft, value);
                if (remove > 0) {
                    value -= remove;
                    int remaining = forCraft - remove;
                    if (remaining > 0) {
                        this.stacksForCraft.put(stored.getSlot(), remaining);
                    } else {
                        this.stacksForCraft.remove(stored.getSlot());
                    }
                    this.save = true;
                }
            }
        }

        return value;
    }

    public int removeFluid(FluidStack request) {
        if (request.isEmpty()) {
            return 0;
        }

        ResourceLocation rl = Registry.FLUID.getKey(request.getFluid());
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return 0;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack stored = it.next();
            if (ModUtils.compareNbt(stored.getTag(), request.getTag(), true)) {
                int available = stored.getCount();
                int forCraft = this.stacksForCraft.getOrDefault(stored.getSlot(), 0);
                int toExtract = Math.min(available - forCraft, request.getAmount());

                if (toExtract <= 0) {
                    return 0;
                }

                stored.addCount(-toExtract);
                storage -= toExtract;

                if (stored.getCount() <= 0) {
                    it.remove();
                }
                if (list.isEmpty()) {
                    storageMap.remove(rl.toString());
                } else {
                    storageMap.put(rl.toString(), list);
                }

                this.removeFluid(stored.getSlot(), toExtract);
                return toExtract;
            }
        }

        return 0;
    }

    @Override
    public int removeFluid(Fluid item, CompoundTag tag, int remaining) {
        if (item == Fluids.EMPTY) {
            return 0;
        }

        ResourceLocation rl = Registry.FLUID.getKey(item);
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return 0;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack stored = it.next();
            if (ModUtils.compareNbt(stored.getTag(), tag, true)) {
                int available = stored.getCount();
                int forCraft = this.stacksForCraft.getOrDefault(stored.getSlot(), 0);
                int toExtract = Math.min(available - forCraft, remaining);

                if (toExtract <= 0) {
                    return 0;
                }

                stored.addCount(-toExtract);
                storage -= toExtract;

                if (stored.getCount() <= 0) {
                    it.remove();
                }
                if (list.isEmpty()) {
                    storageMap.remove(rl.toString());
                } else {
                    storageMap.put(rl.toString(), list);
                }
                this.removeFluid(stored.getSlot(), toExtract);
                return toExtract;
            }
        }

        return 0;
    }

    @Override
    public int removeFluidCrafting(Fluid item, CompoundTag tag, int remaining) {
        if (item == Fluids.EMPTY) {
            return 0;
        }

        ResourceLocation rl = Registry.FLUID.getKey(item);
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return 0;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack stored = it.next();
            if (ModUtils.compareNbt(stored.getTag(), tag, true) && this.stacksForCraft.containsKey(stored.getSlot())) {
                int available = stored.getCount();
                int forCraft = this.stacksForCraft.get(stored.getSlot());
                available = Math.min(available, forCraft);
                int toExtract = Math.min(available, remaining);

                if (toExtract <= 0) {
                    return 0;
                }
                forCraft -= toExtract;
                if (forCraft == 0)
                    this.stacksForCraft.remove(stored.getSlot());
                else
                    this.stacksForCraft.replace(stored.getSlot(), forCraft);

                stored.addCount(-toExtract);
                storage -= toExtract;

                if (stored.getCount() <= 0) {
                    it.remove();
                }
                if (list.isEmpty()) {
                    storageMap.remove(rl.toString());
                } else {
                    storageMap.put(rl.toString(), list);
                }
                this.removeFluid(stored.getSlot(), toExtract);
                return toExtract;
            }
        }

        return 0;
    }

    private FluidStack removeFluid(int index, int amount) {
        FluidStack stack;
        if (index >= 0 && index < this.inventory_fluid.length && !this.inventory_fluid[index].isEmpty()) {
            stack = this.inventory_fluid[index];
            FluidStack ret;
            if (amount >= stack.getAmount()) {
                ret = stack;
                this.inventory_fluid[index] = FluidStack.EMPTY;
                if (!this.freeSlots.contains(index)) {
                    this.freeSlots.add(index);
                }
            } else {
                ret = ModUtils.setSize(stack, amount);
                this.inventory_fluid[index] = ModUtils.decSize(stack, amount);
            }

            markSlotDirty(index);
            return ret;
        } else {
            return FluidStack.EMPTY;
        }
    }

    public int canAddFluid(FluidStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }

        int requestMb = stack.getAmount();
        int freeMb = (int) ((maxCapacity - storage));
        if (freeMb <= 0) {
            return 0;
        }

        return Math.min(requestMb, freeMb);
    }

    public int addFluidDirect(FluidStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return -1;
        }
        int slotId = -1;

        int remaining = stack.getAmount();
        for (int i = 0; i < this.size() && remaining > 0; i++) {
            FluidStack current = this.getFluid(i);
            if (!current.isEmpty() && current.getFluid() == stack.getFluid()) {
                if ((current.getTag() == null && stack.getTag() == null) ||
                        ModUtils.checkNbtEquality(stack.getTag(), current.getTag())) {

                    int space = getInventoryStackLimit() - current.getAmount();
                    if (space > 0) {
                        int toMove = Math.min(space, remaining);
                        if (!simulate) {
                            current.grow(toMove);
                        }
                        remaining -= toMove;
                        slotId = i;
                    }
                }
            }
        }

        for (int i = 0; i < this.size() && remaining > 0; i++) {
            FluidStack current = this.getFluid(i);
            if (current.isEmpty()) {
                int toMove = Math.min(getInventoryStackLimit(), remaining);
                if (!simulate) {
                    FluidStack newStack = stack.copy();
                    newStack.setAmount(toMove);
                    this.setFluid(i, newStack);
                }
                slotId = i;
                remaining -= toMove;
            }
        }

        if (remaining > 0) {
            return -1;
        }
        save = true;
        return slotId;
    }

    public int addFluidDirect(Fluid fluid, CompoundTag tag, int amount, boolean simulate) {
        int slotId = -1;
        int remaining = amount;

        for (int i = 0; i < this.size() && remaining > 0; i++) {
            FluidStack current = this.getFluid(i);
            if (!current.isEmpty() && current.getFluid() == fluid) {
                if ((current.getTag() == null && tag == null) || ModUtils.checkNbtEquality(tag, current.getTag())) {
                    int space = getInventoryStackLimit() - current.getAmount();
                    if (space > 0) {
                        int toMove = Math.min(space, remaining);
                        if (!simulate) current.grow(toMove);
                        remaining -= toMove;
                        slotId = i;
                    }
                }
            }
        }
        if (freeSlots.isEmpty()) {
            return -1;
        }
        while (remaining > 0) {
            if (freeSlots.isEmpty()) {
                return -1;
            }
            int i = freeSlots.remove();
            int toMove = Math.min(getInventoryStackLimit(), remaining);

            if (!simulate) {
                FluidStack newStack = new FluidStack(fluid, toMove);
                if (tag != null) newStack.setTag(tag.copy());
                this.setFluid(i, newStack);

            }

            slotId = i;
            remaining -= toMove;
        }

        if (remaining > 0) return -1;
        if (!simulate) save = true;
        return slotId;
    }

    public void addFluidDirect(int slot, int amount) {
        FluidStack current = this.getFluid(slot);
        current.grow(amount);
        save = true;
    }

    private void setFluid(int slot, FluidStack stack) {
        if (stack == null || stack.isEmpty()) {
            this.inventory_fluid[slot] = FluidStack.EMPTY;
            if (!this.freeSlots.contains(slot)) {
                this.freeSlots.add(slot);
            }
        } else {
            this.inventory_fluid[slot] = stack;
            this.freeSlots.remove(slot);
        }

        markSlotDirty(slot);
    }

    @Override
    public CellInfo getCellInfo() {
        return cellInfo;
    }

    public int size() {
        return inventorySize;
    }

    public int canAdd(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        }
        if (stack.isEmpty()) {
            return 0;
        }

        int request = stack.getCount();


        if (this.storage >= this.maxCapacity) {
            return 0;
        }

        int allowedByCapacity = Math.min(request, this.maxCapacity - this.storage);
        ResourceLocation rl = Registry.ITEM.getKey(stack.getItem());
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list != null) {
            int canInsert;
            for (StorageStack storageStack : list) {
                if (ModUtils.compareNbt(storageStack.getTag(), stack.getTag(), true)) {
                    canInsert = getInventoryStackLimit() - storageStack.getCount();
                    canInsert = Math.min(canInsert, allowedByCapacity);
                    return canInsert;
                }
            }
            if (this.freeSlots.isEmpty())
                return 0;
            return allowedByCapacity;
        } else {
            if (this.freeSlots.isEmpty())
                return 0;
            return allowedByCapacity;
        }
    }

    public int canAdd(Item stack, CompoundTag tag, int amount) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        }
        if (stack == Items.AIR) {
            return 0;
        }

        int request = amount;


        if (this.storage >= this.maxCapacity) {
            return 0;
        }

        int allowedByCapacity = Math.min(request, this.maxCapacity - this.storage);
        ResourceLocation rl = Registry.ITEM.getKey(stack);
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list != null) {
            int canInsert = 0;
            for (StorageStack storageStack : list) {
                if (ModUtils.compareNbt(storageStack.getTag(), tag, true)) {
                    canInsert = getInventoryStackLimit() - storageStack.getCount();
                    canInsert = Math.min(canInsert, allowedByCapacity);
                    return canInsert;
                }
            }
            if (this.freeSlots.isEmpty())
                return 0;
            return allowedByCapacity;
        } else {
            if (this.freeSlots.isEmpty())
                return 0;
            return allowedByCapacity;
        }
    }

    public int canAdd(Item stack, CompoundTag tag, int amount, StorageStack storageStack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        }
        if (stack == Items.AIR) {
            return 0;
        }

        int request = amount;


        if (this.storage >= this.maxCapacity) {
            return 0;
        }

        int allowedByCapacity = Math.min(request, this.maxCapacity - this.storage);


        int canInsert = 0;
        if (ModUtils.compareNbt(storageStack.getTag(), tag, true)) {
            canInsert = getInventoryStackLimit() - storageStack.getCount();
            canInsert = Math.min(canInsert, allowedByCapacity);
            return canInsert;
        }
        return canInsert;
    }

    public int canAdd(Fluid stack, CompoundTag tag, int amount, StorageStack storageStack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        }
        if (stack == Fluids.EMPTY) {
            return 0;
        }

        int request = amount;


        if (this.storage >= this.maxCapacity) {
            return 0;
        }

        int allowedByCapacity = Math.min(request, this.maxCapacity - this.storage);


        int canInsert = 0;
        if (ModUtils.compareNbt(storageStack.getTag(), tag, true)) {
            canInsert = getInventoryStackLimit() - storageStack.getCount();
            canInsert = Math.min(canInsert, allowedByCapacity);
            return canInsert;
        }
        return canInsert;
    }

    @Override
    public int canAdd(FluidStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        }
        if (stack.isEmpty()) {
            return 0;
        }

        int request = stack.getAmount();


        if (this.storage >= this.maxCapacity) {
            return 0;
        }

        int allowedByCapacity = Math.min(request, this.maxCapacity - this.storage);


        ResourceLocation rl = Registry.FLUID.getKey(stack.getFluid());
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list != null) {
            int canInsert = 0;
            for (StorageStack storageStack : list) {
                if (ModUtils.compareNbt(storageStack.getTag(), stack.getTag(), true)) {
                    canInsert = getInventoryStackLimit() - storageStack.getCount();
                    canInsert = Math.min(canInsert, allowedByCapacity);
                    return canInsert;
                }
            }
            if (this.freeSlots.isEmpty())
                return 0;
            return allowedByCapacity;
        } else {
            if (this.freeSlots.isEmpty())
                return 0;
            return allowedByCapacity;
        }
    }

    @Override
    public int canAdd(Fluid stack, CompoundTag tag, int amount) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        }
        if (stack == Fluids.EMPTY) {
            return 0;
        }

        int request = amount;


        if (this.storage >= this.maxCapacity) {
            return 0;
        }

        int allowedByCapacity = Math.min(request, this.maxCapacity - this.storage);


        ResourceLocation rl = Registry.FLUID.getKey(stack);
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list != null) {
            int canInsert = 0;
            for (StorageStack storageStack : list) {
                if (ModUtils.compareNbt(storageStack.getTag(), tag, true)) {
                    canInsert = getInventoryStackLimit() - storageStack.getCount();
                    canInsert = Math.min(canInsert, allowedByCapacity);
                    return canInsert;
                }
            }
            if (this.freeSlots.isEmpty())
                return 0;
            return allowedByCapacity;
        } else {
            if (this.freeSlots.isEmpty())
                return 0;
            return allowedByCapacity;
        }
    }

    @Override
    public void setUpdate(boolean update) {
        this.save = update;
    }

    @Override
    public List<StorageStack> getStorageStackFromItem(ItemStack stack) {
        ResourceLocation rl = Registry.ITEM.getKey(stack.getItem());
        List<StorageStack> stacks = new LinkedList<>();
        List<StorageStack> storage = storageMap.get(rl.toString());
        if (storage != null) {
            for (StorageStack storageStack : storage) {
                if (ModUtils.checkNbtEquality(stack.getTag(), storageStack.getTag())) {
                    stacks.add(storageStack);
                }
            }
        }
        return stacks;
    }

    @Override
    public List<StorageStack> getStorageStackFromFluid(FluidStack stack) {
        ResourceLocation rl = Registry.FLUID.getKey(stack.getFluid());
        List<StorageStack> stacks = new LinkedList<>();
        List<StorageStack> storage = storageMap.get(rl.toString());
        if (storage != null) {
            for (StorageStack storageStack : storage) {
                if (ModUtils.checkNbtEquality(stack.getTag(), storageStack.getTag())) {
                    stacks.add(storageStack);
                }
            }
        }
        return stacks;
    }

    @Override
    public boolean needUpdate() {
        return save;
    }

    public void add(int count, int slot) {
        ItemStack current = this.get(slot);
        current.grow(count);
        markSlotDirty(slot);
    }

    public int add(ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return -1;
        }
        int slotId = -1;

        int remaining = stack.getCount();
        for (int i = 0; i < this.size() && remaining > 0; i++) {
            ItemStack current = this.get(i);
            if (!current.isEmpty() && current.getItem() == stack.getItem()) {
                if ((current.getTag() == null && stack.getTag() == null) ||
                        ModUtils.compareNbt(current.getTag(), stack.getTag(), true)) {

                    int space = getInventoryStackLimit() - current.getCount();
                    if (space > 0) {
                        int toMove = Math.min(space, remaining);
                        if (!simulate) {
                            current.grow(toMove);
                        }
                        remaining -= toMove;
                        slotId = i;
                    }
                }
            }
        }
        if (freeSlots.isEmpty()) {
            return -1;
        }
        while (remaining > 0) {
            if (freeSlots.isEmpty()) {
                return -1;
            }
            int i = freeSlots.remove();

            int toMove = Math.min(getInventoryStackLimit(), remaining);

            if (!simulate) {
                ItemStack newStack = stack.copy();
                newStack.setCount(toMove);
                this.setItem(i, newStack);

            }

            slotId = i;
            remaining -= toMove;
        }

        if (remaining > 0) {
            return -1;
        }
        save = true;
        return slotId;
    }

    public int add(Item item, CompoundTag tag, int count, boolean simulate) {
        if (item == Items.AIR) {
            return -1;
        }
        int slotId = -1;

        int remaining = count;
        for (int i = 0; i < this.size() && remaining > 0; i++) {
            ItemStack current = this.get(i);
            if (!current.isEmpty() && current.getItem() == item) {
                if ((current.getTag() == null && tag == null) ||
                        ModUtils.compareNbt(current.getTag(), tag, true)) {

                    int space = getInventoryStackLimit() - current.getCount();
                    if (space > 0) {
                        int toMove = Math.min(space, remaining);
                        if (!simulate) {
                            current.grow(toMove);
                        }
                        remaining -= toMove;
                        slotId = i;
                    }
                }
            }
        }
        if (freeSlots.isEmpty()) {
            return -1;
        }
        while (remaining > 0) {
            if (freeSlots.isEmpty()) {
                return -1;
            }
            int i = freeSlots.remove();

            int toMove = Math.min(getInventoryStackLimit(), remaining);

            if (!simulate) {
                ItemStack newStack = new ItemStack(item, count);
                newStack.setTag(tag);
                newStack.setCount(toMove);
                this.setItem(i, newStack);

            }

            slotId = i;
            remaining -= toMove;
        }

        if (remaining > 0) {
            return -1;
        }
        save = true;
        return slotId;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {

        return true;
    }

    public CompoundTag saveForStacks(CompoundTag root) {
        CompoundTag stacksTag = new CompoundTag();

        for (Map.Entry<Integer, Integer> entry : stacksForCraft.entrySet()) {
            stacksTag.putInt(String.valueOf(entry.getKey()), entry.getValue());
        }

        root.put("stacksForCraft", stacksTag);
        return root;
    }

    public int getItemsForCraft(Item stack, CompoundTag tag) {
        ResourceLocation rl = Registry.ITEM.getKey(stack);
        List<StorageStack> list = storageMap.get(rl.toString());
        int amount = 0;
        for (StorageStack stack1 : list) {
            if (ModUtils.compareNbt(stack1.getTag(), tag, true)) {
                if (this.stacksForCraft.getOrDefault(stack1.getSlot(), 0) != 0) {
                    amount += this.stacksForCraft.get(stack1.getSlot());
                }
            }
        }
        return amount;
    }

    public int getItemsForCraft(CompoundTag tag, List<StorageStack> list) {
        int amount = 0;
        for (StorageStack stack1 : list) {
            if (ModUtils.compareNbt(stack1.getTag(), tag, true)) {
                if (this.stacksForCraft.getOrDefault(stack1.getSlot(), 0) != 0) {
                    amount += this.stacksForCraft.get(stack1.getSlot());
                }
            }
        }
        return amount;
    }

    public Map<Integer, Integer> getStacksForCraft() {
        return stacksForCraft;
    }


    public void loadForStacks(CompoundTag root) {
        stacksForCraft.clear();

        if (!root.contains("stacksForCraft")) return;

        CompoundTag stacksTag = root.getCompound("stacksForCraft");

        for (String key : stacksTag.getAllKeys()) {
            int intKey = Integer.parseInt(key);
            int value = stacksTag.getInt(key);

            stacksForCraft.put(intKey, value);
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (!ModUtils.isEmpty(stack) && ModUtils.getSize(stack) > this.getInventoryStackLimit()) {
            stack = ModUtils.setSize(stack, this.getInventoryStackLimit());
        }

        if (ModUtils.isEmpty(stack)) {
            this.inventory[slot] = ModUtils.emptyStack;
            if (!this.freeSlots.contains(slot)) {
                this.freeSlots.add(slot);
            }
        } else {
            this.inventory[slot] = stack;
            this.freeSlots.remove(slot);
        }

        markSlotDirty(slot);
    }

    @Override
    public void save() {
        if (this.cleared) {
            return;
        }

        CompoundTag root = ModUtils.nbt(this.containerStack);

        saveForStacks(root);

        if (this.isFluid) {
            if (this.rewriteAllContentToCompound) {
                writeAllFluidsToCompound(root);
            } else if (!this.dirtyContentSlots.isEmpty()) {
                writeDirtyFluidsToCompound(root);
            }
        } else {
            if (this.rewriteAllContentToCompound) {
                writeAllItemsToCompound(root);
            } else if (!this.dirtyContentSlots.isEmpty()) {
                writeDirtyItemsToCompound(root);
            }
        }

        root.putInt("storage", storage);

        this.dirtyContentSlots.clear();
        this.rewriteAllContentToCompound = false;
        this.save = false;
    }


    public ItemStack addStack(ItemStack stack) {
        ResourceLocation rl = Registry.ITEM.getKey(stack.getItem());
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list == null) {
            list = new ArrayList<>();
            if (storage + stack.getCount() <= maxCapacity) {
                storage += stack.getCount();
                int slotId = this.add(stack, false);
                list.add(new StorageStack(stack.getTag(), stack.getCount(), slotId));
                storageMap.put(rl.toString(), list);
                return ItemStack.EMPTY;
            } else {
                double temp = Math.min(maxCapacity - storage, stack.getCount());
                int slotId = this.add(stack.split((int) temp), false);
                list.add(new StorageStack(stack.getTag(), (int) temp, slotId));
                storageMap.put(rl.toString(), list);

                return stack.split((int) (stack.getCount() - temp));
            }
        } else {
            for (StorageStack stack1 : list) {
                if (ModUtils.compareNbt(stack1.getTag(), stack.getTag(), true))
                    if (storage + stack.getCount() <= maxCapacity) {
                        storage += stack.getCount();

                        stack1.addCount(stack.getCount());
                        this.add(stack.getCount(), stack1.getSlot());
                        return ItemStack.EMPTY;
                    } else {
                        int temp = Math.min(maxCapacity - storage, stack.getCount());
                        stack1.addCount((int) temp);
                        this.add(temp, stack1.getSlot());
                        return stack.split((int) (stack.getCount() - temp));
                    }
            }
            if (storage + stack.getCount() <= maxCapacity) {
                storage += stack.getCount();
                int slotId = this.add(stack, false);
                list.add(new StorageStack(stack.getTag(), stack.getCount(), slotId));
                storageMap.replace(rl.toString(), list);

                return ItemStack.EMPTY;
            } else {
                double temp = Math.min(maxCapacity - storage, stack.getCount());

                int slotId = this.add(stack.split((int) temp), false);
                list.add(new StorageStack(stack.getTag(), (int) temp, slotId));
                storageMap.replace(rl.toString(), list);
                return stack.split((int) (stack.getCount() - temp));
            }
        }
    }

    @Override
    public ItemStack removeStackWithIgnoring(Item request, CompoundTag tag, int amount) {
        ResourceLocation rl = Registry.ITEM.getKey(request);
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return ItemStack.EMPTY;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack stored = it.next();


            if (ModUtils.checkNbtEquality(stored.getTag(), tag)) {
                int available = stored.getCount();
                int toExtract = Math.min(available, amount);

                if (toExtract <= 0) {
                    return ItemStack.EMPTY;
                }

                storage -= toExtract;
                stored.addCount(-toExtract);


                if (stored.getCount() <= 0) {
                    it.remove();
                }


                if (list.isEmpty()) {
                    storageMap.remove(rl.toString());
                } else {
                    storageMap.put(rl.toString(), list);
                }


                ItemStack result = new ItemStack(request, toExtract);
                result.setTag(tag);
                this.removeItem(stored.getSlot(), toExtract);
                save = true;
                return result;
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public int removeStackWithIgnoringAmount(Item request, CompoundTag tag, int amount) {
        ResourceLocation rl = Registry.ITEM.getKey(request);
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return 0;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack stored = it.next();


            if (ModUtils.checkNbtEquality(stored.getTag(), tag)) {
                int available = stored.getCount();
                int toExtract = Math.min(available, amount);

                if (toExtract <= 0) {
                    return 0;
                }

                storage -= toExtract;
                stored.addCount(-toExtract);


                if (stored.getCount() <= 0) {
                    it.remove();
                }


                if (list.isEmpty()) {
                    storageMap.remove(rl.toString());
                } else {
                    storageMap.put(rl.toString(), list);
                }


                this.removeItem(stored.getSlot(), toExtract);
                save = true;
                return toExtract;
            }
        }

        return 0;
    }

    @Override
    public int removeStackWithIgnoringAmountCrafting(Item request, CompoundTag tag, int amount) {
        ResourceLocation rl = Registry.ITEM.getKey(request);
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return 0;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack stored = it.next();


            if (ModUtils.checkNbtEquality(stored.getTag(), tag) && this.stacksForCraft.containsKey(stored.getSlot())) {
                int available = stored.getCount();
                int forCrafing = this.stacksForCraft.get(stored.getSlot());
                available = Math.min(available, forCrafing);
                int toExtract = Math.min(available, amount);

                if (toExtract <= 0) {
                    return 0;
                }

                storage -= toExtract;
                forCrafing -= toExtract;
                stored.addCount(-toExtract);
                if (forCrafing == 0) {
                    this.stacksForCraft.remove(stored.getSlot());
                } else {
                    this.stacksForCraft.replace(stored.getSlot(), forCrafing);
                }

                if (stored.getCount() <= 0) {
                    it.remove();
                }


                if (list.isEmpty()) {
                    storageMap.remove(rl.toString());
                } else {
                    storageMap.put(rl.toString(), list);
                }


                this.removeItem(stored.getSlot(), toExtract);
                save = true;
                return toExtract;
            }
        }

        return 0;
    }

    @Override
    public void addStackCrafting(Item item, CompoundTag tag, int amount) {
        ResourceLocation rl = Registry.ITEM.getKey(item);
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list == null) {
            list = new ArrayList<>();
            if (storage + amount <= maxCapacity) {
                storage += amount;
                int slotId = this.add(item, tag, amount, false);
                list.add(new StorageStack(tag, amount, slotId));
                storageMap.put(rl.toString(), list);
                this.stacksForCraft.put(slotId, amount);
                return;
            } else {
                int temp = Math.min(maxCapacity - storage, amount);
                int slotId = this.add(item, tag, temp, false);
                list.add(new StorageStack(tag, (int) temp, slotId));
                storageMap.put(rl.toString(), list);
                this.stacksForCraft.put(slotId, temp);
                return;
            }
        } else {
            boolean added = false;
            for (StorageStack stack1 : list) {
                if (ModUtils.compareNbt(stack1.getTag(), tag, true)) {
                    if (storage + amount <= maxCapacity) {
                        storage += amount;

                        stack1.addCount(amount);
                        this.add(amount, stack1.getSlot());
                        amount = 0;
                        this.stacksForCraft.merge(stack1.getSlot(), amount, Integer::sum);
                        added = true;
                        break;
                    } else {
                        int temp = Math.min(maxCapacity - storage, amount);
                        stack1.addCount((int) temp);
                        this.add(temp, stack1.getSlot());
                        this.stacksForCraft.merge(stack1.getSlot(), temp, Integer::sum);

                        added = true;
                        break;
                    }
                }
            }
            if (!added)
                if (storage + amount <= maxCapacity) {
                    storage += amount;
                    int slotId = this.add(item, tag, amount, false);
                    list.add(new StorageStack(tag, amount, slotId));
                    storageMap.replace(rl.toString(), list);
                    this.stacksForCraft.put(slotId, amount);
                } else {
                    int temp = Math.min(maxCapacity - storage, amount);

                    int slotId = this.add(item, tag, temp, false);
                    list.add(new StorageStack(tag, (int) temp, slotId));
                    storageMap.replace(rl.toString(), list);
                    this.stacksForCraft.put(slotId, temp);
                    return;
                }
        }
    }

    @Override
    public void addStack(Item item, CompoundTag tag, int amount) {
        ResourceLocation rl = Registry.ITEM.getKey(item);
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list == null) {
            list = new ArrayList<>();
            if (storage + amount <= maxCapacity) {
                storage += amount;
                int slotId = this.add(item, tag, amount, false);
                list.add(new StorageStack(tag, amount, slotId));
                storageMap.put(rl.toString(), list);
                return;
            } else {
                int temp = Math.min(maxCapacity - storage, amount);
                int slotId = this.add(item, tag, temp, false);
                list.add(new StorageStack(tag, (int) temp, slotId));
                storageMap.put(rl.toString(), list);

                return;
            }
        } else {
            boolean added = false;
            for (StorageStack stack1 : list) {
                if (ModUtils.compareNbt(stack1.getTag(), tag, true))
                    if (storage + amount <= maxCapacity) {
                        storage += amount;

                        stack1.addCount(amount);
                        this.add(amount, stack1.getSlot());
                        added = true;
                        break;
                    } else {
                        int temp = Math.min(maxCapacity - storage, amount);
                        stack1.addCount((int) temp);
                        this.add(temp, stack1.getSlot());
                        added = true;
                        break;
                    }

            }
            if (!added)
                if (storage + amount <= maxCapacity) {
                    storage += amount;
                    int slotId = this.add(item, tag, amount, false);
                    list.add(new StorageStack(tag, amount, slotId));
                    storageMap.replace(rl.toString(), list);

                } else {
                    int temp = Math.min(maxCapacity - storage, amount);

                    int slotId = this.add(item, tag, temp, false);
                    list.add(new StorageStack(tag, (int) temp, slotId));
                    storageMap.replace(rl.toString(), list);
                    return;
                }
        }
    }

    @Override
    public void addFluidCrafting(Fluid fluid, CompoundTag tag, int amount) {
        if (fluid == Fluids.EMPTY) {
            return;
        }

        ResourceLocation rl = Registry.FLUID.getKey(fluid);
        List<StorageStack> list = storageMap.get(rl.toString());
        int requestedMb = amount;


        int freeMb = (maxCapacity - storage);
        if (freeMb <= 0) {
            return;
        }

        int toInsert = Math.min(requestedMb, freeMb);

        if (list == null) {
            list = new ArrayList<>();
            int slotId = addFluidDirect(fluid, tag, toInsert, false);
            list.add(new StorageStack(tag, toInsert, slotId));
            storageMap.put(rl.toString(), list);
            this.stacksForCraft.put(slotId, toInsert);

        } else {

            for (StorageStack stored : list) {
                if (ModUtils.compareNbt(stored.getTag(), tag, true)) {
                    stored.addCount(toInsert);
                    this.stacksForCraft.merge(stored.getSlot(), toInsert, Integer::sum);
                    addFluidDirect(stored.getSlot(), toInsert);
                    break;
                }
            }


        }


        storage += toInsert;
    }

    @Override
    public void addFluid(Fluid fluid, CompoundTag tag, int amount, StorageStack stored) {
        if (fluid == Fluids.EMPTY) {
            return;
        }

        int requestedMb = amount;


        int freeMb = (maxCapacity - storage);
        if (freeMb <= 0) {
            return;
        }

        int toInsert = Math.min(requestedMb, freeMb);

        if (ModUtils.compareNbt(stored.getTag(), tag, true)) {
            stored.addCount(toInsert);
            addFluidDirect(stored.getSlot(), toInsert);
        }


        storage += toInsert;
    }

    @Override
    public void addStack(Item item, CompoundTag tag, int amount, StorageStack stack1) {
        ResourceLocation rl = Registry.ITEM.getKey(item);
        List<StorageStack> list = storageMap.get(rl.toString());

        boolean added = false;

        if (ModUtils.compareNbt(stack1.getTag(), tag, true))
            if (storage + amount <= maxCapacity) {
                storage += amount;

                stack1.addCount(amount);
                this.add(amount, stack1.getSlot());
                added = true;

            } else {
                int temp = Math.min(maxCapacity - storage, amount);
                stack1.addCount(temp);
                this.add(temp, stack1.getSlot());
                added = true;
            }

        if (!added)
            if (storage + amount <= maxCapacity) {
                storage += amount;
                int slotId = this.add(item, tag, amount, false);
                list.add(new StorageStack(tag, amount, slotId));
                storageMap.replace(rl.toString(), list);

            } else {
                int temp = Math.min(maxCapacity - storage, amount);

                int slotId = this.add(item, tag, temp, false);
                list.add(new StorageStack(tag, (int) temp, slotId));
                storageMap.replace(rl.toString(), list);
                return;
            }

    }

    @Override
    public void addFluid(Fluid fluid, CompoundTag tag, int amount) {
        if (fluid == Fluids.EMPTY) return;

        ResourceLocation rl = Registry.FLUID.getKey(fluid);
        List<StorageStack> list = storageMap.get(rl.toString());

        int freeMb = (maxCapacity - storage);
        if (freeMb <= 0) return;

        int toInsert = Math.min(amount, freeMb);

        if (list == null) {
            list = new ArrayList<>();
            int slotId = addFluidDirect(fluid, tag, toInsert, false);
            if (slotId < 0) return;
            list.add(new StorageStack(tag, toInsert, slotId));
            storageMap.put(rl.toString(), list);
        } else {
            boolean added = false;

            for (StorageStack stored : list) {
                if (ModUtils.compareNbt(stored.getTag(), tag, true)) {
                    stored.addCount(toInsert);
                    addFluidDirect(stored.getSlot(), toInsert);
                    added = true;
                    break;
                }
            }

            if (!added) {
                int slotId = addFluidDirect(fluid, tag, toInsert, false);
                if (slotId < 0) return;
                list.add(new StorageStack(tag, toInsert, slotId));
                storageMap.put(rl.toString(), list);
            }
        }

        storage += toInsert;
    }

    public ItemStack removeStack(ItemStack request) {
        ResourceLocation rl = Registry.ITEM.getKey(request.getItem());
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return ItemStack.EMPTY;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack stored = it.next();


            if (ModUtils.compareNbt(stored.getTag(), request.getTag(), true)) {
                int available = stored.getCount();
                int toExtract = Math.min(available, request.getCount());

                if (toExtract <= 0) {
                    return ItemStack.EMPTY;
                }

                storage -= toExtract;
                stored.addCount(-toExtract);


                if (stored.getCount() <= 0) {
                    it.remove();
                }


                if (list.isEmpty()) {
                    storageMap.remove(rl.toString());
                } else {
                    storageMap.put(rl.toString(), list);
                }


                this.removeItem(stored.getSlot(), toExtract);
                save = true;
                ItemStack stack = new ItemStack(request.getItem(), toExtract);
                stack.setTag(request.getTag());
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int index, int amount) {
        ItemStack stack;
        if (index >= 0 && index < this.inventory.length && !ModUtils.isEmpty(stack = this.inventory[index])) {
            ItemStack ret;
            if (amount >= ModUtils.getSize(stack)) {
                ret = stack;
                this.inventory[index] = ModUtils.emptyStack;
                if (!this.freeSlots.contains(index)) {
                    this.freeSlots.add(index);
                }
            } else {
                ret = ModUtils.setSize(stack, amount);
                this.inventory[index] = ModUtils.decSize(stack, amount);
            }

            markSlotDirty(index);
            return ret;
        } else {
            return ModUtils.emptyStack;
        }
    }

    @Override
    public int getStorage() {
        return storage;
    }

    @Override
    public int removeStack(Item item, CompoundTag requestTag, int toRemove) {
        ResourceLocation rl = Registry.ITEM.getKey(item);
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return 0;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack stored = it.next();


            if (ModUtils.compareNbt(stored.getTag(), requestTag, true)) {
                int available = stored.getCount();
                int forCraft = this.stacksForCraft.getOrDefault(stored.getSlot(), 0);
                int toExtract = Math.min(available - forCraft, toRemove);
                if (toExtract <= 0) {
                    return 0;
                }

                storage -= toExtract;
                stored.addCount(-toExtract);


                if (stored.getCount() <= 0) {
                    it.remove();
                }


                if (list.isEmpty()) {
                    storageMap.remove(rl.toString());
                } else {
                    storageMap.put(rl.toString(), list);
                }


                this.removeItem(stored.getSlot(), toExtract);
                save = true;
                return toExtract;
            }
        }

        return 0;
    }

    @Override
    public int removeStackCrafting(Item item, CompoundTag requestTag, int toRemove) {
        ResourceLocation rl = Registry.ITEM.getKey(item);
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return 0;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack stored = it.next();


            if (ModUtils.checkNbtEquality(stored.getTag(), requestTag) && this.stacksForCraft.containsKey(stored.getSlot())) {
                int available = stored.getCount();
                int forCrafting = this.stacksForCraft.get(stored.getSlot());
                available = Math.min(available, forCrafting);
                int toExtract = Math.min(available, toRemove);

                if (toExtract <= 0) {
                    return 0;
                }
                forCrafting -= toExtract;
                if (forCrafting != 0)
                    this.stacksForCraft.replace(stored.getSlot(), forCrafting);
                else
                    this.stacksForCraft.remove(stored.getSlot());
                storage -= toExtract;
                stored.addCount(-toExtract);


                if (stored.getCount() <= 0) {
                    it.remove();
                }


                if (list.isEmpty()) {
                    storageMap.remove(rl.toString());
                } else {
                    storageMap.put(rl.toString(), list);
                }


                this.removeItem(stored.getSlot(), toExtract);
                save = true;
                return toExtract;
            }
        }

        return 0;
    }

    public ItemStack removeItem(ItemStack request) {
        ResourceLocation rl = Registry.ITEM.getKey(request.getItem());
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return ItemStack.EMPTY;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack stored = it.next();


            if (ModUtils.checkNbtEquality(stored.getTag(), request.getTag())) {
                int available = stored.getCount();
                int toExtract = Math.min(available, request.getCount());

                if (toExtract <= 0) {
                    return ItemStack.EMPTY;
                }

                storage -= toExtract;
                stored.addCount(-toExtract);


                if (stored.getCount() <= 0) {
                    it.remove();
                }


                if (list.isEmpty()) {
                    storageMap.remove(rl.toString());
                } else {
                    storageMap.put(rl.toString(), list);
                }


                ItemStack result = new ItemStack(request.getItem(), toExtract);
                result.setTag(request.getTag());
                this.removeItem(stored.getSlot(), toExtract);
                save = true;
                return result;
            }
        }

        return ItemStack.EMPTY;
    }


    public ContainerMenuAgriculturalAnalyzer getGuiContainer(Player player) {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(Player player, ContainerMenuBase<?> isAdmin) {
        return null;
    }


    @Override
    public void addInventorySlot(final Inventory var1) {

    }


    public ItemStack get(int index) {
        return this.inventory[index];
    }

    public FluidStack getFluid(int index) {
        return this.inventory_fluid[index];
    }

    @Nonnull
    public String getName() {
        return "toolbox";
    }


    @Override
    public int getInventoryStackLimit() {
        return Integer.MAX_VALUE;
    }


}
