package com.denfop.api.storage.autocrafting;

import com.denfop.api.storage.StorageStack;
import com.denfop.api.storage.cell.ICell;
import com.denfop.utils.ModUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.*;

public class AutoCraftSystemVisual {

    private final SameStack output;
    private final Map<Pair<PatternStack, PatternStack>, Boolean> cycleCache = new HashMap<>();
    private final Map<PatternStack, Boolean> patternCycleCache = new HashMap<>();
    private final Map<ResourceLocation, List<PatternStack>> patternCacheItem = new HashMap<>();
    private final Map<ResourceLocation, List<PatternStack>> patternCacheFluid = new HashMap<>();
    Map<PatternStack, AutoCraftStack> patternStackList = new HashMap<>();
    boolean canAdd = true;
    AutoCraftOutput autoCraftOutput;
    List<AutoCraftVisualStep> visualSteps = new LinkedList<>();
    Map<SameStack, Integer> stacksForCraft = new HashMap<>();
    Map<PatternStack, Set<PatternStack>> patternSource = new HashMap<>();
    Map<String, List<StorageStack>> storage;

    public AutoCraftSystemVisual(SameStack stack) {
        this.output = stack;
    }

    public static AutoCraftSystemVisual readFromNBT(CompoundTag tag, RegistryAccess access) {
        SameStack output = SameStack.readFromNBT(tag.getCompound("Output"), access);
        AutoCraftSystemVisual system = new AutoCraftSystemVisual(output);
        ListTag list = tag.getList("PatternStacks", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag stackTag = list.getCompound(i);
            AutoCraftStack stack = AutoCraftStack.readFromNBT(stackTag, access);
            system.patternStackList.put(stack.getPatternStack(), stack);
        }

        if (tag.contains("CanAdd")) {
            system.canAdd = tag.getBoolean("CanAdd");
        }
        if (tag.contains("AutoCraftOutput")) {
            system.autoCraftOutput = AutoCraftOutput.readFromNBT(tag.getCompound("AutoCraftOutput"), access);
        }
        if (tag.contains("StacksForCraft", Tag.TAG_LIST)) {
            ListTag craftList = tag.getList("StacksForCraft", Tag.TAG_COMPOUND);

            for (int i = 0; i < craftList.size(); i++) {
                CompoundTag element = craftList.getCompound(i);

                SameStack stack =
                        SameStack.readFromNBT(element.getCompound("Stack"), access);
                int count = element.getInt("Count");
                system.stacksForCraft.merge(stack, count, Integer::sum);
            }
        }
        return system;
    }

    private void buildPatternCache(List<PatternStack> patterns) {
        patternCacheItem.clear();
        patternCacheFluid.clear();

        for (PatternStack ps : patterns) {
            for (SameStack s : ps.output()) {
                if (s.isItem()) {
                    ResourceLocation rl = BuiltInRegistries.ITEM.getKey(s.getStack().getItem());
                    patternCacheItem.computeIfAbsent(rl, k -> new ArrayList<>()).add(ps);
                } else if (s.isFluid()) {
                    ResourceLocation rl = BuiltInRegistries.FLUID.getKey(s.getFluidStack().getFluid());
                    patternCacheFluid.computeIfAbsent(rl, k -> new ArrayList<>()).add(ps);
                }
            }
        }
    }

    private void putPatternSource(PatternStack owner, List<PatternStack> dependencies) {
        Set<PatternStack> current = patternSource.computeIfAbsent(owner, k -> new LinkedHashSet<>());

        if (dependencies == null || dependencies.isEmpty()) {
            return;
        }

        boolean changed = false;
        for (PatternStack dependency : dependencies) {
            if (dependency != null && current.add(dependency)) {
                changed = true;
            }
        }

        if (changed) {
            cycleCache.clear();
            patternCycleCache.clear();
        }
    }

    private boolean hasCycleCached(PatternStack start, PatternStack target) {
        Pair<PatternStack, PatternStack> key = Pair.of(start, target);
        Boolean cached = cycleCache.get(key);
        if (cached != null) {
            return cached;
        }

        boolean result = hasCycle(start, target, new HashSet<>());
        cycleCache.put(key, result);
        return result;
    }

    private boolean hasCycleForPattern(PatternStack pattern) {
        Boolean cached = patternCycleCache.get(pattern);
        if (cached != null) {
            return cached;
        }

        Set<PatternStack> dependencies = patternSource.getOrDefault(pattern, Collections.emptySet());
        for (PatternStack dep : dependencies) {
            if (hasCycleCached(dep, pattern)) {
                patternCycleCache.put(pattern, true);
                return true;
            }
        }

        patternCycleCache.put(pattern, false);
        return false;
    }

    public void create(List<PatternStack> patterns, List<ICell> cellItemStack, List<ICell> cellFluidStack) {
        buildPatternCache(patterns);
        resolveCraft(output, output.getAmount(), cellItemStack, cellFluidStack);
    }

    public boolean canAddToProcessor() {
        return canAdd;
    }

    public SameStack getOutput() {
        return output;
    }

    public CompoundTag writeToNBT(RegistryAccess access) {
        CompoundTag tag = new CompoundTag();
        CompoundTag outputTag = output.writeToNBT(access);
        tag.put("Output", outputTag);
        ListTag patternList = new ListTag();
        for (AutoCraftStack stack : patternStackList.values()) {
            patternList.add(stack.writeToNBT(access));
        }
        tag.put("PatternStacks", patternList);
        tag.putBoolean("CanAdd", canAdd);
        if (autoCraftOutput != null) {
            tag.put("AutoCraftOutput", autoCraftOutput.writeToNBT(access));
        }
        ListTag craftList = new ListTag();
        for (Map.Entry<SameStack, Integer> entry : stacksForCraft.entrySet()) {
            CompoundTag element = new CompoundTag();

            element.put("Stack", entry.getKey().writeToNBT(access));
            element.putInt("Count", entry.getValue());

            craftList.add(element);
        }
        tag.put("StacksForCraft", craftList);
        return tag;
    }

    public List<AutoCraftVisualStep> getVisualSteps() {
        return visualSteps;
    }

    public AutoCraftOutput getAutoCraftOutput() {
        return autoCraftOutput;
    }

    public List<AutoCraftStack> getPatternStackList() {
        return new ArrayList<>(patternStackList.values());
    }

    private boolean hasCycle(PatternStack start, PatternStack target, Set<PatternStack> visited) {
        if (start.equals(target)) {
            return true;
        }

        if (!visited.add(start)) {
            return false;
        }

        Set<PatternStack> nextSet = patternSource.get(start);
        if (nextSet == null || nextSet.isEmpty()) {
            return false;
        }

        for (PatternStack next : nextSet) {
            if (hasCycle(next, target, visited)) {
                return true;
            }
        }

        return false;
    }

    private void resolveCraft(SameStack target, int countMultiplier, List<ICell> cellItemStacks, List<ICell> cellFluidStacks) {
        patternSource.clear();
        cycleCache.clear();
        patternCycleCache.clear();
        Tuple<PatternStack, SameStack> pattern = findPatternFor(target);
        if (pattern == null) {
            canAdd = false;
            visualSteps.add(new AutoCraftVisualStep(target, 0, 0, target.getAmount() * countMultiplier, 0));
            return;
        }

        Set<Tuple<PatternStack, SameStack>> checkPattern = new HashSet<>();
        countMultiplier = (int) Math.ceil(countMultiplier / (1D * pattern.getB().getAmount()));
        autoCraftOutput = new AutoCraftOutput(pattern, countMultiplier);
        visualSteps.add(new AutoCraftVisualStep(target, 0, countMultiplier * pattern.getB().getAmount(), countMultiplier * pattern.getB().getAmount(), 0));
        for (SameStack sameStack : pattern.getA().output()) {
            if (!sameStack.equals(pattern.getB())) {
                visualSteps.add(new AutoCraftVisualStep(sameStack, 0, 0, 0, countMultiplier * sameStack.getAmount()));

            }
        }
        checkPattern.add(pattern);
        storage = new HashMap<>();
        findAllStacks(cellItemStacks, cellFluidStacks);
        Deque<Pair<Object, Integer>> stack = new ArrayDeque<>();
        for (SameStack sameStack : pattern.getA().inputs()) {
            if (sameStack.isItem())
                stack.push(new Pair<>(sameStack.getStack(), countMultiplier));
            else
                stack.push(new Pair<>(sameStack.getFluidStack(), countMultiplier));
            List<PatternStack> patternStacks = new LinkedList<>();
            Tuple<PatternStack, SameStack> patternInput = findPatternFor(sameStack);
            if (patternInput != null) {
                patternStacks.add(patternInput.getA());
            }
            putPatternSource(pattern.getA(), patternStacks);
        }
        cycle_main:
        while (!stack.isEmpty()) {
            Pair<Object, Integer> pair = stack.pop();
            Object current = pair.getFirst();
            int multiplier = pair.getSecond();

            if (current instanceof ItemStack currentItem) {
                Tuple<PatternStack, SameStack> ps = findPatternFor(currentItem);
                int craftsNeeded;
                if (ps == null) {
                    int availableCount = 0;


                    int required = currentItem.getCount() * multiplier;

                    Tuple<Integer, Integer> tuple = getAvailableStacks(currentItem);
                    if (tuple != null) {
                        availableCount = tuple.getA();
                    }
                    if (availableCount > 0) {
                        if (availableCount < required) {
                            canAdd = false;
                        }
                        removeAvailableStacks(currentItem, new Tuple<>(Math.min(availableCount, required), tuple.getB()));
                        visualSteps.add(new AutoCraftVisualStep(currentItem, Math.min(availableCount, required), 0, required, 0));
                    } else {
                        canAdd = false;
                        visualSteps.add(new AutoCraftVisualStep(currentItem, 0, 0, required, 0));
                    }
                    continue;
                } else {
                    if (!checkPattern.add(ps)) {
                        if (hasCycleForPattern(ps.getA())) {
                            canAdd = false;
                            visualSteps.add(new AutoCraftVisualStep(currentItem, 0, 0, currentItem.getCount() * multiplier, 0));
                            continue cycle_main;
                        }
                    }
                    int availableCount = 0;
                    int required = currentItem.getCount() * multiplier;
                    Tuple<Integer, Integer> tuple = getAvailableStacks(currentItem);

                    if (tuple != null) {
                        availableCount = tuple.getA();
                    }

                    if (required > availableCount) {
                        craftsNeeded = (int) Math.ceil((required - availableCount) / (double) ps.getB().getAmount());
                        AutoCraftStack autoCraftStack = patternStackList.get(ps.getA());
                        if (autoCraftStack == null) {
                            autoCraftStack = new AutoCraftStack(ps, craftsNeeded);
                            patternStackList.put(ps.getA(), autoCraftStack);
                        } else {
                            autoCraftStack.addAmountAll(craftsNeeded);
                        }

                        visualSteps.add(new AutoCraftVisualStep(currentItem, availableCount, (required - availableCount), required, 0));
                        for (SameStack sameStack : ps.getA().output()) {
                            if (!sameStack.equals(ps.getB())) {
                                visualSteps.add(new AutoCraftVisualStep(sameStack, 0, 0, 0, craftsNeeded * sameStack.getAmount()));

                            }
                        }
                        if (tuple != null)
                            removeAvailableStacks(currentItem, new Tuple<>(availableCount, tuple.getB()));
                    } else {
                        if (tuple != null) {
                            removeAvailableStacks(currentItem, new Tuple<>(required, tuple.getB()));
                            visualSteps.add(new AutoCraftVisualStep(currentItem, required, 0, required, 0));

                        }
                        continue;
                    }
                }

                for (SameStack input : ps.getA().inputs()) {
                    if (!input.getStack().isEmpty()) {
                        stack.push(new Pair<>(input.getStack(), craftsNeeded));
                    }
                    if (!input.getFluidStack().isEmpty()) {
                        stack.push(new Pair<>(input.getFluidStack(), craftsNeeded));
                    }
                    List<PatternStack> patternStacks = new LinkedList<>();
                    Tuple<PatternStack, SameStack> patternInput = findPatternFor(input);
                    if (patternInput != null) {
                        patternStacks.add(patternInput.getA());
                    }
                    putPatternSource(ps.getA(), patternStacks);
                }

            } else if (current instanceof FluidStack currentFluid) {
                Tuple<PatternStack, SameStack> ps = findPatternFor(currentFluid);
                int craftsNeeded;
                if (ps == null) {
                    int availableAmount = 0;
                    Tuple<Integer, Integer> tuple = getAvailableStacks(currentFluid.copy());
                    if (tuple != null) {
                        availableAmount = tuple.getA();
                    }

                    int required = currentFluid.getAmount() * multiplier;
                    if (availableAmount > 0) {
                        if (availableAmount < required) {
                            canAdd = false;
                        }
                        removeAvailableStacks(currentFluid, new Tuple<>(Math.min(availableAmount, required), tuple.getB()));
                        visualSteps.add(new AutoCraftVisualStep(currentFluid, Math.min(availableAmount, required), 0, required, 0));
                    } else {
                        canAdd = false;
                        visualSteps.add(new AutoCraftVisualStep(currentFluid, 0, 0, required - availableAmount, 0));

                    }
                    continue;
                } else {
                    if (!checkPattern.add(ps)) {
                        if (hasCycleForPattern(ps.getA())) {
                            canAdd = false;
                            visualSteps.add(new AutoCraftVisualStep(currentFluid, 0, 0, currentFluid.getAmount() * multiplier, 0));
                            continue cycle_main;
                        }
                    }
                    int availableCount = 0;
                    Tuple<Integer, Integer> tuple = getAvailableStacks(currentFluid.copy());

                    if (tuple != null) {
                        availableCount = tuple.getA();
                    }
                    int required = currentFluid.getAmount() * multiplier;
                    if (required > availableCount) {
                        craftsNeeded = (int) Math.ceil((required - availableCount) / (double) ps.getB().getAmount());
                        AutoCraftStack autoCraftStack = patternStackList.get(ps.getA());
                        if (autoCraftStack == null) {
                            autoCraftStack = new AutoCraftStack(ps, craftsNeeded);
                            patternStackList.put(ps.getA(), autoCraftStack);
                        } else {
                            autoCraftStack.addAmountAll(craftsNeeded);
                        }
                        visualSteps.add(new AutoCraftVisualStep(currentFluid, availableCount, required - availableCount, required, 0));
                        for (SameStack sameStack : ps.getA().output()) {
                            if (!sameStack.equals(ps.getB())) {
                                visualSteps.add(new AutoCraftVisualStep(sameStack, 0, 0, 0, craftsNeeded * sameStack.getAmount()));

                            }
                        }
                        if (tuple != null)
                            removeAvailableStacks(currentFluid, new Tuple<>(availableCount, tuple.getB()));
                    } else {
                        if (tuple != null) {
                            removeAvailableStacks(currentFluid, new Tuple<>(required, tuple.getB()));
                            visualSteps.add(new AutoCraftVisualStep(currentFluid, required, 0, required, 0));

                        }
                        continue;
                    }
                }

                for (SameStack input : ps.getA().inputs()) {
                    if (!input.getStack().isEmpty()) {
                        stack.push(new Pair<>(input.getStack(), craftsNeeded));
                    }
                    if (!input.getFluidStack().isEmpty()) {
                        stack.push(new Pair<>(input.getFluidStack(), craftsNeeded));
                    }
                    List<PatternStack> patternStacks = new LinkedList<>();
                    Tuple<PatternStack, SameStack> patternInput = findPatternFor(input);
                    if (patternInput != null) {
                        patternStacks.add(patternInput.getA());
                    }
                    putPatternSource(ps.getA(), patternStacks);
                }
            }
        }
        AutoCraftVisualStep step = visualSteps.remove(0);
        visualSteps = mergeDuplicateSteps(visualSteps);
        for (AutoCraftVisualStep autoCraftVisualStep : visualSteps) {
            if (autoCraftVisualStep.have() != 0)
                stacksForCraft.put(getSameStack(autoCraftVisualStep.stack()), Math.min(autoCraftVisualStep.have(), autoCraftVisualStep.need()));
        }
        visualSteps.add(0, step);
    }

    public Map<SameStack, Integer> getStacksForCraft() {
        return stacksForCraft;
    }

    private SameStack getSameStack(Object stack) {
        if (stack instanceof SameStack sameStack)
            return sameStack;
        if (stack instanceof ItemStack sameStack)
            return new SameStack(sameStack);
        else
            return new SameStack((FluidStack) stack);
    }

    private List<AutoCraftVisualStep> mergeDuplicateSteps(List<AutoCraftVisualStep> steps) {
        Map<Object, AutoCraftVisualStep> merged = new HashMap<>();

        for (AutoCraftVisualStep step : steps) {
            Object key = step.stack();
            if (key instanceof ItemStack)
                key = new SameStack((ItemStack) key);
            if (key instanceof FluidStack)
                key = new SameStack((FluidStack) key);
            merged.merge(key, step, (a, b) -> new AutoCraftVisualStep(
                    a.stack(),
                    a.have() + b.have(),
                    Math.max(0, a.create() + b.create() - a.willBeCreate() - b.willBeCreate()),
                    a.need() + b.need(), Math.max(0, a.willBeCreate() - a.create() + b.willBeCreate() - b.create())
            ));
        }

        return new ArrayList<>(merged.values());
    }

    private Tuple<Integer, Integer> getAvailableStacks(ItemStack stack) {
        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(stack.getItem());
        List<StorageStack> storages = storage.get(rl.toString());
        if (storages == null) {
            return null;
        }
        for (int i = 0; i < storages.size(); i++) {
            StorageStack storageStack = storages.get(i);
            if (storageStack.getTag() == null || storageStack.getTag().equals(stack.getComponents())) {
                return new Tuple<>(storageStack.getCount(), i);
            }
        }
        return null;
    }

    private void removeAvailableStacks(ItemStack stack, Tuple<Integer, Integer> tuple) {
        if (tuple.getA() == 0)
            return;
        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(stack.getItem());
        StorageStack storages = storage.get(rl.toString()).get(tuple.getB());
        storages.removeCount(tuple.getA());
        if (storages.getCount() <= 0) {
            storage.get(rl.toString()).remove((int) tuple.getB());
        }
    }

    private Tuple<Integer, Integer> getAvailableStacks(FluidStack stack) {
        ResourceLocation rl = BuiltInRegistries.FLUID.getKey(stack.getFluid());
        List<StorageStack> storages = storage.get(rl.toString());
        if (storages == null) {
            return null;
        }
        for (int i = 0; i < storages.size(); i++) {
            StorageStack storageStack = storages.get(i);
            if (storageStack.getTag() == null || storageStack.getTag().equals(stack.getComponents())) {
                return new Tuple<>(storageStack.getCount(), i);
            }
        }
        return null;
    }

    private void removeAvailableStacks(FluidStack stack, Tuple<Integer, Integer> tuple) {
        if (tuple.getA() == 0)
            return;
        ResourceLocation rl = BuiltInRegistries.FLUID.getKey(stack.getFluid());
        StorageStack storages = storage.get(rl.toString()).get(tuple.getB());
        storages.removeCount(tuple.getA());
        if (storages.getCount() <= 0) {
            storage.get(rl.toString()).remove((int) tuple.getB());
        }
    }

    private void findAllStacks(List<ICell> cellItemStacks, List<ICell> cellFluidStacks) {
        for (ICell cell : cellItemStacks) {
            Map<String, List<StorageStack>> cellStorage = cell.getStorageStack();
            for (Map.Entry<String, List<StorageStack>> entry : cellStorage.entrySet()) {
                if (!storage.containsKey(entry.getKey())) {
                    List<StorageStack> storageStackList = new ArrayList<>();
                    entry.getValue().forEach(storageStack -> {
                        StorageStack stack = storageStack.copy();
                        int toCraft = cell.getStacksForCraft().getOrDefault(stack.getSlot(), 0);
                        stack.removeCount(toCraft);
                        if (stack.getCount() != 0)
                            storageStackList.add(stack);
                    });
                    storage.put(entry.getKey(), storageStackList);
                } else {
                    List<StorageStack> storageStackList = storage.get(entry.getKey());
                    cycle:
                    for (StorageStack stack : entry.getValue()) {
                        for (StorageStack stack1 : storageStackList) {
                            if (stack1.equals(stack)) {
                                int toCraft = cell.getStacksForCraft().getOrDefault(stack.getSlot(), 0);
                                stack1.addCount(stack.getCount() - toCraft);
                                continue cycle;
                            }
                        }
                        int toCraft = cell.getStacksForCraft().getOrDefault(stack.getSlot(), 0);
                        StorageStack storageStack = stack.copy();
                        storageStack.removeCount(toCraft);
                        if (storageStack.getCount() != 0)
                            storageStackList.add(storageStack);
                    }

                }
            }
        }
        for (ICell cell : cellFluidStacks) {
            Map<String, List<StorageStack>> cellStorage = cell.getStorageStack();
            for (Map.Entry<String, List<StorageStack>> entry : cellStorage.entrySet()) {
                if (!storage.containsKey(entry.getKey())) {
                    List<StorageStack> storageStackList = new ArrayList<>();
                    entry.getValue().forEach(storageStack -> {
                        StorageStack stack = storageStack.copy();
                        int toCraft = cell.getStacksForCraft().getOrDefault(stack.getSlot(), 0);
                        stack.removeCount(toCraft);
                        if (stack.getCount() != 0)
                            storageStackList.add(stack);
                    });
                    storage.put(entry.getKey(), storageStackList);
                } else {
                    List<StorageStack> storageStackList = storage.get(entry.getKey());
                    cycle:
                    for (StorageStack stack : entry.getValue()) {
                        for (StorageStack stack1 : storageStackList) {
                            if (stack1.equals(stack)) {
                                int toCraft = cell.getStacksForCraft().getOrDefault(stack.getSlot(), 0);
                                stack1.addCount(stack.getCount() - toCraft);
                                continue cycle;
                            }
                        }
                        int toCraft = cell.getStacksForCraft().getOrDefault(stack.getSlot(), 0);
                        StorageStack storageStack = stack.copy();
                        storageStack.removeCount(toCraft);
                        if (storageStack.getCount() != 0)
                            storageStackList.add(storageStack);
                    }
                }
            }
        }
    }


    private Tuple<PatternStack, SameStack> findPatternFor(ItemStack stack) {
        List<PatternStack> list = patternCacheItem.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (list == null) return null;
        for (PatternStack ps : list) {
            for (SameStack sameStack : ps.output())
                if (sameStack.isItem())
                    if (ModUtils.compareNbt(sameStack.getStack().getComponents(), stack.getComponents(), true))
                        return new Tuple<>(ps, sameStack);
        }
        return null;
    }

    private Tuple<PatternStack, SameStack> findPatternFor(SameStack stack) {
        if (stack.isItem()) {
            List<PatternStack> list = patternCacheItem.get(BuiltInRegistries.ITEM.getKey(stack.getStack().getItem()));
            if (list == null) return null;
            for (PatternStack ps : list) {
                for (SameStack sameStack : ps.output())
                    if (sameStack.isItem())
                        if (sameStack.getStack().is(stack.getStack().getItem()) && ModUtils.compareNbt(sameStack.getStack().getComponents(), stack.getStack().getComponents(), true)) {
                            return new Tuple<>(ps, sameStack);
                        }
            }
        }
        if (stack.isFluid()) {
            List<PatternStack> list = patternCacheFluid.get(BuiltInRegistries.FLUID.getKey(stack.getFluidStack().getFluid()));
            if (list == null) return null;
            for (PatternStack ps : list) {
                for (SameStack sameStack : ps.output())
                    if (sameStack.isFluid())
                        if (FluidStack.isSameFluid(sameStack.getFluidStack(), stack.getFluidStack())) {
                            return new Tuple<>(ps, sameStack);
                        }
            }
        }
        return null;
    }

    private Tuple<PatternStack, SameStack> findPatternFor(FluidStack stack) {
        List<PatternStack> list = patternCacheFluid.get(BuiltInRegistries.FLUID.getKey(stack.getFluid()));
        if (list == null) return null;
        for (PatternStack ps : list) {
            for (SameStack sameStack : ps.output())
                if (sameStack.isFluid())
                    if (FluidStack.isSameFluid(sameStack.getFluidStack(), stack))
                        return new Tuple<>(ps, sameStack);
        }
        return null;
    }

}
