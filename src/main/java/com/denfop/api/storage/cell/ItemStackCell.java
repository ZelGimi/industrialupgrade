package com.denfop.api.storage.cell;

import com.denfop.api.storage.StorageStack;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuAgriculturalAnalyzer;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.StorageCellData;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemStackInventory;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.ModUtils;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ItemStackCell extends ItemStackInventory implements ICell {

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
    private StorageCellData storageCellData;

    public ItemStackCell(ItemStack stack, CellInfo cellInfo) {
        super(null, stack, cellInfo.typeCell() == TypeCell.FLUID ? 100 : 300);

        this.inventorySize = cellInfo.typeCell() == TypeCell.FLUID ? 100 : 300;

        this.inventory = makeMutableInventory(this.inventory, this.inventorySize);

        this.inventory_fluid = new FluidStack[this.inventorySize];
        Arrays.fill(this.inventory_fluid, FluidStack.EMPTY);

        this.itemStack1 = stack;
        this.cellInfo = cellInfo;
        this.maxCapacity = cellInfo.capacity();
        this.isFluid = cellInfo.typeCell() == TypeCell.FLUID;

        this.storageCellData = containerStack.get(DataComponentsInit.STORAGE_CELL);
        if (this.storageCellData == null) {
            this.storageCellData = StorageCellData.createEmpty(this.inventorySize);
            this.containerStack.set(DataComponentsInit.STORAGE_CELL, this.storageCellData);
        } else {
            this.storageCellData = this.storageCellData.normalize(this.inventorySize);
            this.containerStack.set(DataComponentsInit.STORAGE_CELL, this.storageCellData);
        }

        loadForStacks(this.storageCellData);
        loadContentFromComponents();
        rebuildFreeSlots();
    }

    private static DataComponentPatch toPatch(@Nullable DataComponentMap map) {
        if (map == null || map.isEmpty()) {
            return DataComponentPatch.EMPTY;
        }

        if (map instanceof PatchedDataComponentMap patched) {
            return patched.asPatch();
        }

        PatchedDataComponentMap patched = new PatchedDataComponentMap(DataComponentMap.EMPTY);
        patched.setAll(map);
        return patched.asPatch();
    }

    private static boolean equalComponents(@Nullable DataComponentMap first, @Nullable DataComponentMap second) {
        if ((first == null || first.isEmpty()) && (second == null || second.isEmpty())) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        return ModUtils.compareNbt(first, second, true);
    }

    private static boolean strictEqualComponents(@Nullable DataComponentMap first, @Nullable DataComponentMap second) {
        if ((first == null || first.isEmpty()) && (second == null || second.isEmpty())) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        return ModUtils.compareNbt(first, second, false);
    }

    private static ItemStack buildItemStack(Item item, int count, @Nullable DataComponentMap components) {
        if (item == Items.AIR || count <= 0) {
            return ItemStack.EMPTY;
        }
        if (components == null || components.isEmpty()) {
            return new ItemStack(item, count);
        }
        return new ItemStack(BuiltInRegistries.ITEM.wrapAsHolder(item), count, toPatch(components));
    }

    private static FluidStack buildFluidStack(Fluid fluid, int amount, @Nullable DataComponentMap components) {
        if (fluid == Fluids.EMPTY || amount <= 0) {
            return FluidStack.EMPTY;
        }
        if (components == null || components.isEmpty()) {
            return new FluidStack(fluid, amount);
        }
        return new FluidStack(BuiltInRegistries.FLUID.wrapAsHolder(fluid), amount, toPatch(components));
    }

    private static List<ItemStack> makeMutableInventory(List<ItemStack> source, int size) {
        List<ItemStack> result = new ArrayList<>(size);

        if (source != null) {
            int limit = Math.min(source.size(), size);
            for (int i = 0; i < limit; i++) {
                ItemStack stack = source.get(i);
                result.add(stack == null ? ItemStack.EMPTY : stack.copy());
            }
        }

        while (result.size() < size) {
            result.add(ItemStack.EMPTY);
        }

        return result;
    }

    private void markSlotDirty(int slot) {
        if (slot < 0 || slot >= this.inventorySize) {
            return;
        }
        this.dirtyContentSlots.add(slot);
        this.save = true;
    }

    private void loadForStacks(StorageCellData data) {
        this.stacksForCraft.clear();
        List<Integer> craft = data.stacksForCraft();

        for (int i = 0; i < Math.min(craft.size(), this.inventorySize); i++) {
            int value = craft.get(i);
            if (value > 0) {
                this.stacksForCraft.put(i, value);
            }
        }
    }

    private void loadContentFromComponents() {
        this.storageMap.clear();
        this.storage = 0;

        if (this.isFluid) {
            List<FluidStack> fluids = this.storageCellData.fluids();
            for (int slot = 0; slot < this.inventorySize; slot++) {
                FluidStack stack = slot < fluids.size() ? fluids.get(slot) : FluidStack.EMPTY;
                if (stack == null || stack.isEmpty()) {
                    this.inventory_fluid[slot] = FluidStack.EMPTY;
                    continue;
                }

                FluidStack copy = stack.copy();
                this.inventory_fluid[slot] = copy;
                this.storage += copy.getAmount();

                ResourceLocation rl = BuiltInRegistries.FLUID.getKey(copy.getFluid());
                List<StorageStack> list = this.storageMap.computeIfAbsent(rl.toString(), k -> new ArrayList<>());
                list.add(new StorageStack(copy.getComponents(), copy.getAmount(), slot));
            }
        } else {
            for (int slot = 0; slot < Math.min(this.inventory.size(), this.inventorySize); slot++) {
                ItemStack stack = this.inventory.get(slot);
                if (ModUtils.isEmpty(stack)) {
                    continue;
                }

                this.storage += stack.getCount();

                ResourceLocation rl = BuiltInRegistries.ITEM.getKey(stack.getItem());
                List<StorageStack> list = this.storageMap.computeIfAbsent(rl.toString(), k -> new ArrayList<>());
                list.add(new StorageStack(stack.getComponents(), stack.getCount(), slot));
            }
        }
    }

    private void rebuildFreeSlots() {
        this.freeSlots.clear();

        if (this.isFluid) {
            for (int i = 0; i < this.inventorySize; i++) {
                if (this.inventory_fluid[i] == null || this.inventory_fluid[i].isEmpty()) {
                    this.freeSlots.add(i);
                }
            }
        } else {
            for (int i = 0; i < Math.min(this.inventory.size(), this.inventorySize); i++) {
                if (ModUtils.isEmpty(this.inventory.get(i))) {
                    this.freeSlots.add(i);
                }
            }
        }
    }

    @Override
    public Map<String, List<StorageStack>> getStorageStack() {
        return this.storageMap;
    }

    @Override
    public ItemStack[] getStacks() {
        return this.inventory.toArray(new ItemStack[0]);
    }

    @Override
    public FluidStack[] getFluids() {
        return this.inventory_fluid;
    }

    @Override
    public CellInfo getCellInfo() {
        return this.cellInfo;
    }

    public int size() {
        return this.inventorySize;
    }

    public int canAddStack(ItemStack stack) {
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
        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(stack.getItem());
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list != null) {
            int canInsert;
            for (StorageStack storageStack : list) {
                if (ModUtils.compareNbt(storageStack.getTag(), stack.getComponents(), true)) {
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
    public int canAddStack(Item item, DataComponentMap tag, int amount) {
        if (item == null) {
            throw new NullPointerException("null ItemStack");
        }
        if (item == Items.AIR) {
            return 0;
        }

        int request = amount;


        if (this.storage >= this.maxCapacity) {
            return 0;
        }

        int allowedByCapacity = Math.min(request, this.maxCapacity - this.storage);
        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(item);
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list != null) {
            int canInsert = 0;
            for (StorageStack storageStack : list) {
                if (equalComponents(storageStack.getTag(), tag)) {
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
    public int canAddStack(Item item, DataComponentMap tag, int amount, StorageStack storageStack) {
        if (item == null) {
            throw new NullPointerException("null Item");
        }
        if (item == Items.AIR || amount <= 0) {
            return 0;
        }

        if (this.storage >= this.maxCapacity) {
            return 0;
        }

        int allowedByCapacity = Math.min(amount, this.maxCapacity - this.storage);


        int canInsert = 0;
        if (equalComponents(storageStack.getTag(), tag)) {
            canInsert = getInventoryStackLimit() - storageStack.getCount();
            canInsert = Math.min(canInsert, allowedByCapacity);
            return canInsert;
        }
        return canInsert;

    }

    @Override
    public int canAddStack(Fluid fluid, DataComponentMap tag, int amount, StorageStack storageStack) {
        if (fluid == null) {
            throw new NullPointerException("null Fluid");
        }
        if (fluid == Fluids.EMPTY || amount <= 0) {
            return 0;
        }

        if (this.storage >= this.maxCapacity) {
            return 0;
        }

        int allowedByCapacity = Math.min(amount, this.maxCapacity - this.storage);


        int canInsert = 0;
        if (equalComponents(storageStack.getTag(), tag)) {
            canInsert = getInventoryStackLimit() - storageStack.getCount();
            canInsert = Math.min(canInsert, allowedByCapacity);
            return canInsert;
        }

        return Math.min(amount, this.maxCapacity - this.storage);
    }

    @Override
    public int canAddStack(FluidStack stack) {
        if (stack == null) {
            throw new NullPointerException("null FluidStack");
        }
        if (stack.isEmpty()) {
            return 0;
        }

        if (this.storage >= this.maxCapacity) {
            return 0;
        }
        int allowedByCapacity = Math.min(stack.getAmount(), this.maxCapacity - this.storage);


        ResourceLocation rl = BuiltInRegistries.FLUID.getKey(stack.getFluid());
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list != null) {
            int canInsert = 0;
            for (StorageStack storageStack : list) {
                if (equalComponents(storageStack.getTag(), stack.getComponents())) {
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
    public int canAddStack(Fluid fluid, DataComponentMap tag, int amount) {
        if (fluid == null) {
            throw new NullPointerException("null Fluid");
        }
        if (fluid == Fluids.EMPTY || amount <= 0) {
            return 0;
        }

        if (this.storage >= this.maxCapacity) {
            return 0;
        }
        int allowedByCapacity = Math.min(amount, this.maxCapacity - this.storage);


        ResourceLocation rl = BuiltInRegistries.FLUID.getKey(fluid);
        List<StorageStack> list = storageMap.get(rl.toString());
        if (list != null) {
            int canInsert = 0;
            for (StorageStack storageStack : list) {
                if (equalComponents(storageStack.getTag(), tag)) {
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
        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(stack.getItem());
        List<StorageStack> result = new LinkedList<>();
        List<StorageStack> storage = this.storageMap.get(rl.toString());

        if (storage != null) {
            for (StorageStack entry : storage) {
                if (strictEqualComponents(stack.getComponents(), entry.getTag())) {
                    result.add(entry);
                }
            }
        }

        return result;
    }

    @Override
    public List<StorageStack> getStorageStackFromFluid(FluidStack stack) {
        ResourceLocation rl = BuiltInRegistries.FLUID.getKey(stack.getFluid());
        List<StorageStack> result = new LinkedList<>();
        List<StorageStack> storage = this.storageMap.get(rl.toString());

        if (storage != null) {
            for (StorageStack entry : storage) {
                if (strictEqualComponents(stack.getComponents(), entry.getTag())) {
                    result.add(entry);
                }
            }
        }

        return result;
    }

    @Override
    public boolean needUpdate() {
        return this.save;
    }

    public void add(int count, int slot) {
        ItemStack current = this.get(slot);
        current.grow(count);
        markSlotDirty(slot);
    }

    @Override
    public int add(ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return -1;
        }

        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(stack.getItem());
        List<StorageStack> list = this.storageMap.get(rl.toString());

        if (list != null) {
            for (StorageStack entry : list) {
                if (equalComponents(entry.getTag(), stack.getComponents())) {
                    if (!simulate) {
                        this.add(stack.getCount(), entry.getSlot());
                    }
                    return entry.getSlot();
                }
            }
        }

        if (this.freeSlots.isEmpty()) {
            return -1;
        }

        int slot = this.freeSlots.peekFirst();
        if (!simulate) {
            this.setItem(slot, stack.copy());
        }
        return slot;
    }

    public int add(Item item, DataComponentMap tag, int count, boolean simulate) {
        if (item == Items.AIR || count <= 0) {
            return -1;
        }

        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(item);
        List<StorageStack> list = this.storageMap.get(rl.toString());

        if (list != null) {
            for (StorageStack entry : list) {
                if (equalComponents(entry.getTag(), tag)) {
                    if (!simulate) {
                        this.add(count, entry.getSlot());
                    }
                    return entry.getSlot();
                }
            }
        }

        if (this.freeSlots.isEmpty()) {
            return -1;
        }

        int slot = this.freeSlots.peekFirst();
        if (!simulate) {
            this.setItem(slot, buildItemStack(item, count, tag));
        }
        return slot;
    }

    @Override
    public ItemStack addStack(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int toInsert = Math.min(stack.getCount(), Math.max(0, this.maxCapacity - this.storage));
        if (toInsert <= 0) {
            return stack.copy();
        }

        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(stack.getItem());
        List<StorageStack> list = this.storageMap.computeIfAbsent(rl.toString(), k -> new ArrayList<>());

        for (StorageStack entry : list) {
            if (equalComponents(entry.getTag(), stack.getComponents())) {
                entry.addCount(toInsert);
                this.add(toInsert, entry.getSlot());
                this.storage += toInsert;

                if (toInsert == stack.getCount()) {
                    return ItemStack.EMPTY;
                }
                return buildItemStack(stack.getItem(), stack.getCount() - toInsert, stack.getComponents());
            }
        }

        int slotId = this.add(buildItemStack(stack.getItem(), toInsert, stack.getComponents()), false);
        if (slotId < 0) {
            return stack.copy();
        }

        list.add(new StorageStack(stack.getComponents(), toInsert, slotId));
        this.storage += toInsert;

        if (toInsert == stack.getCount()) {
            return ItemStack.EMPTY;
        }

        return buildItemStack(stack.getItem(), stack.getCount() - toInsert, stack.getComponents());
    }

    @Override
    public void addStack(Item item, DataComponentMap tag, int amount) {
        if (item == Items.AIR || amount <= 0) {
            return;
        }

        int toInsert = Math.min(amount, Math.max(0, this.maxCapacity - this.storage));
        if (toInsert <= 0) {
            return;
        }

        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(item);
        List<StorageStack> list = this.storageMap.computeIfAbsent(rl.toString(), k -> new ArrayList<>());

        for (StorageStack entry : list) {
            if (equalComponents(entry.getTag(), tag)) {
                entry.addCount(toInsert);
                this.add(toInsert, entry.getSlot());
                this.storage += toInsert;
                return;
            }
        }

        int slotId = this.add(item, tag, toInsert, false);
        if (slotId < 0) {
            return;
        }

        list.add(new StorageStack(tag, toInsert, slotId));
        this.storage += toInsert;
    }

    @Override
    public void addStack(Item item, DataComponentMap tag, int amount, StorageStack stack1) {
        if (item == Items.AIR || amount <= 0) {
            return;
        }

        int toInsert = Math.min(amount, Math.max(0, this.maxCapacity - this.storage));
        if (toInsert <= 0) {
            return;
        }

        if (equalComponents(stack1.getTag(), tag)) {
            stack1.addCount(toInsert);
            this.add(toInsert, stack1.getSlot());
            this.storage += toInsert;
            return;
        }

        this.addStack(item, tag, toInsert);
    }

    @Override
    public void addStackCrafting(Item item, DataComponentMap tag, int amount) {
        if (item == Items.AIR || amount <= 0) {
            return;
        }

        int toInsert = Math.min(amount, Math.max(0, this.maxCapacity - this.storage));
        if (toInsert <= 0) {
            return;
        }

        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(item);
        List<StorageStack> list = this.storageMap.computeIfAbsent(rl.toString(), k -> new ArrayList<>());

        for (StorageStack entry : list) {
            if (equalComponents(entry.getTag(), tag)) {
                entry.addCount(toInsert);
                this.add(toInsert, entry.getSlot());
                this.stacksForCraft.merge(entry.getSlot(), toInsert, Integer::sum);
                this.storage += toInsert;
                this.save = true;
                return;
            }
        }

        int slotId = this.add(item, tag, toInsert, false);
        if (slotId < 0) {
            return;
        }

        list.add(new StorageStack(tag, toInsert, slotId));
        this.stacksForCraft.put(slotId, toInsert);
        this.storage += toInsert;
        this.save = true;
    }

    @Override
    public FluidStack addFluid(FluidStack stack) {
        if (stack.isEmpty()) {
            return FluidStack.EMPTY;
        }

        int toInsert = Math.min(stack.getAmount(), Math.max(0, this.maxCapacity - this.storage));
        if (toInsert <= 0) {
            return stack.copy();
        }

        ResourceLocation rl = BuiltInRegistries.FLUID.getKey(stack.getFluid());
        List<StorageStack> list = this.storageMap.computeIfAbsent(rl.toString(), k -> new ArrayList<>());

        for (StorageStack entry : list) {
            if (equalComponents(entry.getTag(), stack.getComponents())) {
                entry.addCount(toInsert);
                this.addFluidDirect(entry.getSlot(), toInsert);
                this.storage += toInsert;

                if (toInsert == stack.getAmount()) {
                    return FluidStack.EMPTY;
                }
                return buildFluidStack(stack.getFluid(), stack.getAmount() - toInsert, stack.getComponents());
            }
        }

        int slotId = addFluidDirect(stack.getFluid(), stack.getComponents(), toInsert, false);
        if (slotId < 0) {
            return stack.copy();
        }

        list.add(new StorageStack(stack.getComponents(), toInsert, slotId));
        this.storage += toInsert;

        if (toInsert == stack.getAmount()) {
            return FluidStack.EMPTY;
        }

        return buildFluidStack(stack.getFluid(), stack.getAmount() - toInsert, stack.getComponents());
    }

    @Override
    public void addFluid(Fluid fluid, DataComponentMap tag, int amount) {
        if (fluid == Fluids.EMPTY || amount <= 0) {
            return;
        }

        int toInsert = Math.min(amount, Math.max(0, this.maxCapacity - this.storage));
        if (toInsert <= 0) {
            return;
        }

        ResourceLocation rl = BuiltInRegistries.FLUID.getKey(fluid);
        List<StorageStack> list = this.storageMap.computeIfAbsent(rl.toString(), k -> new ArrayList<>());

        for (StorageStack entry : list) {
            if (equalComponents(entry.getTag(), tag)) {
                entry.addCount(toInsert);
                this.addFluidDirect(entry.getSlot(), toInsert);
                this.storage += toInsert;
                return;
            }
        }

        int slotId = addFluidDirect(fluid, tag, toInsert, false);
        if (slotId < 0) {
            return;
        }

        list.add(new StorageStack(tag, toInsert, slotId));
        this.storage += toInsert;
    }

    @Override
    public void addFluid(Fluid fluid, DataComponentMap tag, int amount, StorageStack stored) {
        if (fluid == Fluids.EMPTY || amount <= 0) {
            return;
        }

        int toInsert = Math.min(amount, Math.max(0, this.maxCapacity - this.storage));
        if (toInsert <= 0) {
            return;
        }

        if (equalComponents(stored.getTag(), tag)) {
            stored.addCount(toInsert);
            this.addFluidDirect(stored.getSlot(), toInsert);
            this.storage += toInsert;
            return;
        }

        this.addFluid(fluid, tag, toInsert);
    }

    @Override
    public void addFluidCrafting(Fluid fluid, DataComponentMap tag, int amount) {
        if (fluid == Fluids.EMPTY || amount <= 0) {
            return;
        }

        int toInsert = Math.min(amount, Math.max(0, this.maxCapacity - this.storage));
        if (toInsert <= 0) {
            return;
        }

        ResourceLocation rl = BuiltInRegistries.FLUID.getKey(fluid);
        List<StorageStack> list = this.storageMap.computeIfAbsent(rl.toString(), k -> new ArrayList<>());

        for (StorageStack entry : list) {
            if (equalComponents(entry.getTag(), tag)) {
                entry.addCount(toInsert);
                this.addFluidDirect(entry.getSlot(), toInsert);
                this.stacksForCraft.merge(entry.getSlot(), toInsert, Integer::sum);
                this.storage += toInsert;
                this.save = true;
                return;
            }
        }

        int slotId = addFluidDirect(fluid, tag, toInsert, false);
        if (slotId < 0) {
            return;
        }

        list.add(new StorageStack(tag, toInsert, slotId));
        this.stacksForCraft.put(slotId, toInsert);
        this.storage += toInsert;
        this.save = true;
    }

    @Override
    public int removeFluid(FluidStack request) {
        if (request.isEmpty()) {
            return 0;
        }

        ResourceLocation rl = BuiltInRegistries.FLUID.getKey(request.getFluid());
        List<StorageStack> list = this.storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return 0;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack entry = it.next();
            if (equalComponents(entry.getTag(), request.getComponents())) {
                int available = entry.getCount();
                int reserved = this.stacksForCraft.getOrDefault(entry.getSlot(), 0);
                int toExtract = Math.min(available - reserved, request.getAmount());

                if (toExtract <= 0) {
                    return 0;
                }

                entry.addCount(-toExtract);
                this.storage -= toExtract;
                this.removeFluid(entry.getSlot(), toExtract);

                if (entry.getCount() <= 0) {
                    it.remove();
                }
                if (list.isEmpty()) {
                    this.storageMap.remove(rl.toString());
                }

                return toExtract;
            }
        }

        return 0;
    }

    @Override
    public int removeFluid(Fluid item, DataComponentMap tag, int remaining) {
        if (item == Fluids.EMPTY || remaining <= 0) {
            return 0;
        }

        ResourceLocation rl = BuiltInRegistries.FLUID.getKey(item);
        List<StorageStack> list = this.storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return 0;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack entry = it.next();
            if (equalComponents(entry.getTag(), tag)) {
                int available = entry.getCount();
                int reserved = this.stacksForCraft.getOrDefault(entry.getSlot(), 0);
                int toExtract = Math.min(available - reserved, remaining);

                if (toExtract <= 0) {
                    return 0;
                }

                entry.addCount(-toExtract);
                this.storage -= toExtract;
                this.removeFluid(entry.getSlot(), toExtract);

                if (entry.getCount() <= 0) {
                    it.remove();
                }
                if (list.isEmpty()) {
                    this.storageMap.remove(rl.toString());
                }

                return toExtract;
            }
        }

        return 0;
    }

    @Override
    public int removeFluidCrafting(Fluid item, DataComponentMap tag, int remaining) {
        if (item == Fluids.EMPTY || remaining <= 0) {
            return 0;
        }

        ResourceLocation rl = BuiltInRegistries.FLUID.getKey(item);
        List<StorageStack> list = this.storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return 0;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack entry = it.next();
            if (equalComponents(entry.getTag(), tag) && this.stacksForCraft.containsKey(entry.getSlot())) {
                int reserved = this.stacksForCraft.get(entry.getSlot());
                int available = Math.min(entry.getCount(), reserved);
                int toExtract = Math.min(available, remaining);

                if (toExtract <= 0) {
                    return 0;
                }

                int leftReserved = reserved - toExtract;
                if (leftReserved <= 0) {
                    this.stacksForCraft.remove(entry.getSlot());
                } else {
                    this.stacksForCraft.put(entry.getSlot(), leftReserved);
                }

                entry.addCount(-toExtract);
                this.storage -= toExtract;
                this.removeFluid(entry.getSlot(), toExtract);

                if (entry.getCount() <= 0) {
                    it.remove();
                }
                if (list.isEmpty()) {
                    this.storageMap.remove(rl.toString());
                }

                this.save = true;
                return toExtract;
            }
        }

        return 0;
    }

    private FluidStack removeFluid(int index, int amount) {
        if (index < 0 || index >= this.inventory_fluid.length) {
            return FluidStack.EMPTY;
        }

        FluidStack stack = this.inventory_fluid[index];
        if (stack == null || stack.isEmpty()) {
            return FluidStack.EMPTY;
        }

        FluidStack ret;
        if (amount >= stack.getAmount()) {
            ret = stack.copy();
            this.inventory_fluid[index] = FluidStack.EMPTY;
            if (!this.freeSlots.contains(index)) {
                this.freeSlots.add(index);
            }
        } else {
            ret = stack.copy();
            ret.setAmount(amount);

            FluidStack left = stack.copy();
            left.shrink(amount);
            this.inventory_fluid[index] = left;
        }

        markSlotDirty(index);
        return ret;
    }

    @Override
    public int canAddFluid(FluidStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }

        int free = this.maxCapacity - this.storage;
        if (free <= 0) {
            return 0;
        }

        return Math.min(stack.getAmount(), free);
    }

    @Override
    public int addFluidDirect(FluidStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return -1;
        }

        ResourceLocation rl = BuiltInRegistries.FLUID.getKey(stack.getFluid());
        List<StorageStack> list = this.storageMap.get(rl.toString());

        if (list != null) {
            for (StorageStack entry : list) {
                if (equalComponents(entry.getTag(), stack.getComponents())) {
                    if (!simulate) {
                        this.addFluidDirect(entry.getSlot(), stack.getAmount());
                    }
                    return entry.getSlot();
                }
            }
        }

        if (this.freeSlots.isEmpty()) {
            return -1;
        }

        int slot = this.freeSlots.peekFirst();
        if (!simulate) {
            this.setFluid(slot, stack.copy());
        }

        return slot;
    }

    public int addFluidDirect(Fluid fluid, DataComponentMap tag, int amount, boolean simulate) {
        if (fluid == Fluids.EMPTY || amount <= 0) {
            return -1;
        }

        ResourceLocation rl = BuiltInRegistries.FLUID.getKey(fluid);
        List<StorageStack> list = this.storageMap.get(rl.toString());

        if (list != null) {
            for (StorageStack entry : list) {
                if (equalComponents(entry.getTag(), tag)) {
                    if (!simulate) {
                        this.addFluidDirect(entry.getSlot(), amount);
                    }
                    return entry.getSlot();
                }
            }
        }

        if (this.freeSlots.isEmpty()) {
            return -1;
        }

        int slot = this.freeSlots.peekFirst();
        if (!simulate) {
            this.setFluid(slot, buildFluidStack(fluid, amount, tag));
        }

        return slot;
    }

    public void addFluidDirect(int slot, int amount) {
        FluidStack current = this.getFluid(slot);
        current.grow(amount);
        markSlotDirty(slot);
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
    public int removeItemFromListCraft(SameStack key, Integer value) {
        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(key.getStack().getItem());
        List<StorageStack> list = this.storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return value;
        }

        for (StorageStack entry : list) {
            if (value == 0) {
                break;
            }

            if (equalComponents(entry.getTag(), key.getStack().getComponents())) {
                int reserved = this.stacksForCraft.getOrDefault(entry.getSlot(), 0);
                int remove = Math.min(reserved, value);

                if (remove > 0) {
                    value -= remove;
                    int left = reserved - remove;

                    if (left > 0) {
                        this.stacksForCraft.put(entry.getSlot(), left);
                    } else {
                        this.stacksForCraft.remove(entry.getSlot());
                    }
                    this.save = true;
                }
            }
        }

        return value;
    }

    @Override
    public int removeFluidFromListCraft(SameStack key, Integer value) {
        ResourceLocation rl = BuiltInRegistries.FLUID.getKey(key.getFluidStack().getFluid());
        List<StorageStack> list = this.storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return value;
        }

        for (StorageStack entry : list) {
            if (value == 0) {
                break;
            }

            if (equalComponents(entry.getTag(), key.getFluidStack().getComponents())) {
                int reserved = this.stacksForCraft.getOrDefault(entry.getSlot(), 0);
                int remove = Math.min(reserved, value);

                if (remove > 0) {
                    value -= remove;
                    int left = reserved - remove;

                    if (left > 0) {
                        this.stacksForCraft.put(entry.getSlot(), left);
                    } else {
                        this.stacksForCraft.remove(entry.getSlot());
                    }
                    this.save = true;
                }
            }
        }

        return value;
    }

    @Override
    public ItemStack removeStackWithIgnoring(Item request, DataComponentMap tag, int amount) {
        int extracted = this.removeStackWithIgnoringAmount(request, tag, amount);
        if (extracted <= 0) {
            return ItemStack.EMPTY;
        }
        return buildItemStack(request, extracted, tag);
    }

    @Override
    public int removeStackWithIgnoringAmount(Item request, DataComponentMap tag, int amount) {
        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(request);
        List<StorageStack> list = this.storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return 0;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack entry = it.next();
            if (strictEqualComponents(entry.getTag(), tag)) {
                int available = entry.getCount();
                int toExtract = Math.min(available, amount);

                if (toExtract <= 0) {
                    return 0;
                }

                this.storage -= toExtract;
                entry.addCount(-toExtract);
                this.removeItem(entry.getSlot(), toExtract);
                this.save = true;

                if (entry.getCount() <= 0) {
                    it.remove();
                }
                if (list.isEmpty()) {
                    this.storageMap.remove(rl.toString());
                }

                return toExtract;
            }
        }

        return 0;
    }

    @Override
    public int removeStackWithIgnoringAmountCrafting(Item request, DataComponentMap tag, int amount) {
        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(request);
        List<StorageStack> list = this.storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return 0;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack entry = it.next();
            if (strictEqualComponents(entry.getTag(), tag) && this.stacksForCraft.containsKey(entry.getSlot())) {
                int reserved = this.stacksForCraft.get(entry.getSlot());
                int available = Math.min(entry.getCount(), reserved);
                int toExtract = Math.min(available, amount);

                if (toExtract <= 0) {
                    return 0;
                }

                this.storage -= toExtract;
                entry.addCount(-toExtract);

                int leftReserved = reserved - toExtract;
                if (leftReserved <= 0) {
                    this.stacksForCraft.remove(entry.getSlot());
                } else {
                    this.stacksForCraft.put(entry.getSlot(), leftReserved);
                }

                this.removeItem(entry.getSlot(), toExtract);
                this.save = true;

                if (entry.getCount() <= 0) {
                    it.remove();
                }
                if (list.isEmpty()) {
                    this.storageMap.remove(rl.toString());
                }

                return toExtract;
            }
        }

        return 0;
    }

    @Override
    public int removeStack(Item item, DataComponentMap requestTag, int toRemove) {
        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(item);
        List<StorageStack> list = this.storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return 0;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack entry = it.next();

            if (equalComponents(entry.getTag(), requestTag)) {
                int available = entry.getCount();
                int reserved = this.stacksForCraft.getOrDefault(entry.getSlot(), 0);
                int toExtract = Math.min(available - reserved, toRemove);

                if (toExtract <= 0) {
                    return 0;
                }

                this.storage -= toExtract;
                entry.addCount(-toExtract);
                this.removeItem(entry.getSlot(), toExtract);
                this.save = true;

                if (entry.getCount() <= 0) {
                    it.remove();
                }
                if (list.isEmpty()) {
                    this.storageMap.remove(rl.toString());
                }

                return toExtract;
            }
        }

        return 0;
    }

    @Override
    public int removeStackCrafting(Item item, DataComponentMap requestTag, int toRemove) {
        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(item);
        List<StorageStack> list = this.storageMap.get(rl.toString());
        if (list == null || list.isEmpty()) {
            return 0;
        }

        for (Iterator<StorageStack> it = list.iterator(); it.hasNext(); ) {
            StorageStack entry = it.next();

            if (strictEqualComponents(entry.getTag(), requestTag) && this.stacksForCraft.containsKey(entry.getSlot())) {
                int reserved = this.stacksForCraft.get(entry.getSlot());
                int available = Math.min(entry.getCount(), reserved);
                int toExtract = Math.min(available, toRemove);

                if (toExtract <= 0) {
                    return 0;
                }

                int leftReserved = reserved - toExtract;
                if (leftReserved <= 0) {
                    this.stacksForCraft.remove(entry.getSlot());
                } else {
                    this.stacksForCraft.put(entry.getSlot(), leftReserved);
                }

                this.storage -= toExtract;
                entry.addCount(-toExtract);
                this.removeItem(entry.getSlot(), toExtract);
                this.save = true;

                if (entry.getCount() <= 0) {
                    it.remove();
                }
                if (list.isEmpty()) {
                    this.storageMap.remove(rl.toString());
                }

                return toExtract;
            }
        }

        return 0;
    }

    @Override
    public ItemStack removeStack(ItemStack request) {
        if (request.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int extracted = this.removeStack(request.getItem(), request.getComponents(), request.getCount());
        if (extracted <= 0) {
            return ItemStack.EMPTY;
        }

        return buildItemStack(request.getItem(), extracted, request.getComponents());
    }

    @Override
    public ItemStack removeItem(ItemStack request) {
        if (request.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int extracted = this.removeStackWithIgnoringAmount(request.getItem(), request.getComponents(), request.getCount());
        if (extracted <= 0) {
            return ItemStack.EMPTY;
        }

        return buildItemStack(request.getItem(), extracted, request.getComponents());
    }

    @Override
    public ItemStack removeItem(int index, int amount) {
        if (index < 0 || index >= this.inventory.size()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = this.inventory.get(index);
        if (ModUtils.isEmpty(stack)) {
            return ItemStack.EMPTY;
        }

        ItemStack ret;
        if (amount >= stack.getCount()) {
            ret = stack.copy();
            this.inventory.set(index, ItemStack.EMPTY);
            if (!this.freeSlots.contains(index)) {
                this.freeSlots.add(index);
            }
        } else {
            ret = stack.copy();
            ret.setCount(amount);

            ItemStack left = stack.copy();
            left.shrink(amount);
            this.inventory.set(index, left);
        }

        markSlotDirty(index);
        return ret;
    }

    @Override
    public int getStorage() {
        return this.storage;
    }

    public int getItemsForCraft(Item stack, DataComponentMap tag) {
        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(stack);
        List<StorageStack> list = this.storageMap.get(rl.toString());
        if (list == null) {
            return 0;
        }

        int amount = 0;
        for (StorageStack entry : list) {
            if (equalComponents(entry.getTag(), tag)) {
                amount += this.stacksForCraft.getOrDefault(entry.getSlot(), 0);
            }
        }
        return amount;
    }

    @Override
    public int getItemsForCraft(DataComponentMap tag, List<StorageStack> list) {
        int amount = 0;
        for (StorageStack entry : list) {
            if (equalComponents(entry.getTag(), tag)) {
                amount += this.stacksForCraft.getOrDefault(entry.getSlot(), 0);
            }
        }
        return amount;
    }

    @Override
    public Map<Integer, Integer> getStacksForCraft() {
        return this.stacksForCraft;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (!ModUtils.isEmpty(stack) && ModUtils.getSize(stack) > this.getInventoryStackLimit()) {
            stack = ModUtils.setSize(stack, this.getInventoryStackLimit());
        }

        if (ModUtils.isEmpty(stack)) {
            this.inventory.set(slot, ItemStack.EMPTY);
            if (!this.freeSlots.contains(slot)) {
                this.freeSlots.add(slot);
            }
        } else {
            this.inventory.set(slot, stack);
            this.freeSlots.remove(slot);
        }

        markSlotDirty(slot);
    }

    @Override
    public void save() {

        if (this.cleared) {
            return;
        }

        this.inventory = makeMutableInventory(this.inventory, this.inventorySize);
        this.containerItem = this.containerItem.updateItems(containerStack, this.inventory);

        List<FluidStack> fluids = new ArrayList<>(this.inventorySize);
        for (int i = 0; i < this.inventorySize; i++) {
            FluidStack fluid = this.inventory_fluid[i];
            fluids.add(fluid == null ? FluidStack.EMPTY : fluid.copy());
        }

        List<Integer> craft = new ArrayList<>(this.inventorySize);
        for (int i = 0; i < this.inventorySize; i++) {
            craft.add(this.stacksForCraft.getOrDefault(i, 0));
        }

        this.storageCellData = this.storageCellData.updateAll(this.containerStack, this.storage, fluids, craft);

        this.dirtyContentSlots.clear();
        this.save = false;
    }

    public ItemStack get(int index) {
        return this.inventory.get(index);
    }

    public FluidStack getFluid(int index) {
        return this.inventory_fluid[index];
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
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

    @Nonnull
    public String getName() {
        return "toolbox";
    }

    @Override
    public int getInventoryStackLimit() {
        return Integer.MAX_VALUE;
    }
}