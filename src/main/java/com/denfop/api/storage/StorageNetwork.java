package com.denfop.api.storage;

import com.denfop.IUCore;
import com.denfop.api.otherenergies.common.ISink;
import com.denfop.api.otherenergies.common.Path;
import com.denfop.api.storage.autocrafting.*;
import com.denfop.api.storage.cell.ICell;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.network.WorldData;
import com.denfop.network.packet.PacketUpdateNetworkSystem;
import com.denfop.network.packet.PacketUpdateStorageCell;
import com.denfop.network.packet.PacketUpdateStorageForCraft;
import com.denfop.utils.ModUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StorageNetwork {
    private static final int STORAGE_LIMIT = 2000000000;
    private final Controller controller;
    public List<StorageDeviceCell> storageDeviceCells = new LinkedList<>();
    public List<ICell> storageDeviceItemsCells = new LinkedList<>();
    public List<ICell> storageDeviceFluidCells = new LinkedList<>();
    public boolean reworkCell = false;
    public boolean reBuildPreCraft = false;
    public Map<PatternStack, Interface> patternStackInterfaceMap = new HashMap<>();
    public Map<String, Map<SameStack, Integer>> stacksMapCount = new HashMap<>();
    public boolean canUpdate = false;
    public boolean reBuildPatterns = false;
    List<Processor> processors = new ArrayList<>();
    Map<Integer, Processor> processorsIds = new HashMap<>();
    List<Export> exports = new ArrayList<>();
    List<IMonitor> monitors = new ArrayList<>();
    List<Import> imports = new ArrayList<>();
    List<PreCraft> preCrafts = new ArrayList<>();
    List<Interface> interfaces = new ArrayList<>();
    double consumeEnergy = 0;
    int indexProcessor = 0;
    List<PatternStack> patternStacks = new LinkedList<>();
    List<SameStack> preCraftStacks = new LinkedList<>();
    private List<SameStack> stacks;
    private List<SameStack> fluids;
    private List<SameStack> stacksCanCreate;
    private List<SameStack> fluidsCanCreate;

    public StorageNetwork(Controller controller) {
        this.controller = controller;
    }

    public void addElement(ElectricStorage electricStorage) {
        if (electricStorage instanceof Export export)
            addExport(export);
        if (electricStorage instanceof Import imp)
            addImport(imp);
        if (electricStorage instanceof Interface intf)
            addInterface(intf);
        if (electricStorage instanceof PreCraft intf)
            addPreCraft(intf);
        if (electricStorage instanceof Processor processor)
            addProcessor(processor);
        if (electricStorage instanceof StorageDeviceCell storageDeviceCell)
            addStorageDevice(storageDeviceCell);
        if (electricStorage instanceof IMonitor monitor) {
            monitor.setStorageNetwork(this);
            monitors.add(monitor);
        }
        this.consumeEnergy += electricStorage.getRequiredPower();
    }

    public Controller getController() {
        return controller;
    }

    public Tuple<List<SameStack>, List<SameStack>> getAllStoredItemStacks() {
        Map<String, Map<StorageStack, SameStack>> combined = new HashMap<>();
        SetMultimap<String, SameStack> multimap = HashMultimap.create();

        for (ICell cell : getAllItemCells()) {
            Map<String, List<StorageStack>> entries = cell.getStorageStack();
            ItemStack[] stacks = cell.getStacks();
            for (Map.Entry<String, List<StorageStack>> entry : entries.entrySet()) {
                Map<StorageStack, SameStack> map = combined.computeIfAbsent(entry.getKey(), k -> new HashMap<>());

                for (StorageStack storageStack : entry.getValue()) {
                    SameStack sameStack = map.get(storageStack);
                    if (sameStack == null) {
                        sameStack = new SameStack();
                        sameStack.setStack(stacks[storageStack.getSlot()].copy());
                        map.put(storageStack, sameStack);
                        multimap.put(entry.getKey(), sameStack);
                    } else {
                        sameStack.getStack().grow(storageStack.getCount());

                    }
                }
            }
        }

        List<SameStack> result = new ArrayList<>(combined.size() * 2);
        for (Map<StorageStack, SameStack> map : combined.values()) {
            result.addAll(map.values());
        }
        result.sort((a, b) -> Integer.compare(b.getStack().getCount(), a.getStack().getCount()));
        List<SameStack> canCreate = new LinkedList<>();
        cycle:
        for (PatternStack patternStack : getAllPatterns()) {
            for (SameStack sameStack : patternStack.output())
                if (sameStack.isItem()) {
                    Collection<SameStack> collection = multimap.get(sameStack.getKey());
                    for (SameStack stack : collection) {
                        if (stack.isCorrect(sameStack.getStack()))
                            continue cycle;
                    }
                    canCreate.add(sameStack);
                }
        }
        return new Tuple<>(result, canCreate);
    }

    public Tuple<List<SameStack>, List<SameStack>> getAllStoredFluidStacks() {
        Map<String, Map<StorageStack, SameStack>> combined = new HashMap<>();

        for (ICell cell : getAllFluidCells()) {
            Map<String, List<StorageStack>> entries = cell.getStorageStack();
            FluidStack[] stacks = cell.getFluids();
            for (Map.Entry<String, List<StorageStack>> entry : entries.entrySet()) {
                Map<StorageStack, SameStack> map = combined.computeIfAbsent(entry.getKey(), k -> new HashMap<>());

                for (StorageStack storageStack : entry.getValue()) {
                    SameStack sameStack = map.get(storageStack);
                    if (sameStack == null) {
                        sameStack = new SameStack();
                        sameStack.setFluidStack(stacks[storageStack.getSlot()].copy());
                        map.put(storageStack, sameStack);
                    } else {
                        sameStack.getFluidStack().grow(storageStack.getCount());
                    }
                }
            }
        }

        List<SameStack> result = new ArrayList<>(combined.size() * 2);
        for (Map<StorageStack, SameStack> map : combined.values()) {
            result.addAll(map.values());
        }
        result.sort((a, b) -> Integer.compare(b.getFluidStack().getAmount(), a.getFluidStack().getAmount()));

        List<SameStack> canCreate = new ArrayList<>();
        cycle:
        for (PatternStack patternStack : getAllPatterns()) {
            for (SameStack sameStack : patternStack.output())
                if (sameStack.isFluid()) {
                    for (SameStack stack : result) {
                        if (stack.isCorrect(sameStack.getFluidStack()))
                            continue cycle;
                    }
                    canCreate.add(sameStack);
                }
        }
        return new Tuple<>(result, canCreate);
    }

    private void addProcessor(Processor processor) {
        this.processors.add(processor);
        processor.setStorageNetwork(this);
        processor.setId(indexProcessor);
        this.processorsIds.put(indexProcessor, processor);
        indexProcessor++;
    }

    private void addStorageDevice(StorageDeviceCell processor) {
        this.storageDeviceCells.add(processor);
        processor.setStorageNetwork(this);
        reworkCell = true;
    }

    private void removeStorageDevice(StorageDeviceCell processor) {
        this.storageDeviceCells.remove(processor);

        processor.setStorageNetwork(null);
    }

    public void removeElement(ElectricStorage electricStorage) {
        if (electricStorage instanceof Export export)
            removeExport(export);
        if (electricStorage instanceof Import imp)
            removeImport(imp);
        if (electricStorage instanceof PreCraft imp)
            removePreCraft(imp);
        if (electricStorage instanceof Interface intf)
            removeInterface(intf);
        if (electricStorage instanceof Processor processor)
            removeProccessor(processor);
        if (electricStorage instanceof StorageDeviceCell storageDeviceCell)
            removeStorageDevice(storageDeviceCell);
        this.consumeEnergy -= electricStorage.getRequiredPower();
        if (this.consumeEnergy < 0)
            this.consumeEnergy = 0;
    }

    private void removeProccessor(Processor processor) {
        this.processors.remove(processor);
        processor.setStorageNetwork(null);
    }

    public void addExport(Export export) {
        exports.add(export);
    }

    public void addImport(Import imp) {
        imports.add(imp);
    }

    public void addPreCraft(PreCraft imp) {
        preCrafts.add(imp);
        imp.setStorageNetwork(this);
    }

    public void addInterface(Interface intf) {
        interfaces.add(intf);
        for (PatternStack patternStack : intf.getPatterns()) {
            patternStackInterfaceMap.put(patternStack, intf);
        }
        intf.setStorageNetwork(this);
    }

    public void removeExport(Export export) {
        exports.remove(export);
    }

    public void removeImport(Import imp) {
        imports.remove(imp);
    }

    public void removePreCraft(PreCraft imp) {
        preCrafts.remove(imp);
    }

    public void removeInterface(Interface intf) {
        interfaces.remove(intf);
        intf.setStorageNetwork(null);
        patternStackInterfaceMap.entrySet().removeIf(entry -> entry.getValue().equals(intf));
    }

    public boolean hasAvailableProcessors() {
        for (Processor processor : processors)
            if (processor.canAddAutoCraft())
                return true;
        return false;
    }

    public void addAutoCraft(AutoCraftSystem system) {
        for (Processor processor : processors)
            if (processor.canAddAutoCraft()) {
                processor.addAutoCraft(system);
                break;
            }
    }

    public AutoCraftSystemVisual createAutoCraftVisual(SameStack stack) {
        AutoCraftSystemVisual autoCraftSystemVisual = new AutoCraftSystemVisual(stack);
        autoCraftSystemVisual.create(getAllPatterns(), getAllItemCells(), getAllFluidCells());
        return autoCraftSystemVisual;
    }

    public List<PatternStack> getAllPatterns() {
        if (reBuildPatterns) {
            patternStacks = new ArrayList<>();
            patternStackInterfaceMap.clear();
            for (Interface intf : interfaces) {
                patternStacks.addAll(intf.getPatterns());
                for (PatternStack patternStack : new ArrayList<>(intf.getPatterns())) {
                    patternStackInterfaceMap.put(patternStack, intf);
                }
            }
            reBuildPatterns = false;
            return patternStacks;
        } else {
            return patternStacks;
        }
    }

    public List<Processor> getProcessors() {
        return processors;
    }

    public List<SameStack> getPreCraftStacks() {
        if (reBuildPreCraft) {
            preCraftStacks = new ArrayList<>();
            for (PreCraft preCraft : preCrafts) {
                preCraftStacks.addAll(preCraft.getPreCrafts());
            }
            reBuildPreCraft = false;
            return preCraftStacks;
        } else {
            return preCraftStacks;
        }
    }

    public List<ICell> getAllItemCells() {
        if (reworkCell) {
            this.storageDeviceItemsCells = new LinkedList<>();
            for (StorageDeviceCell device : storageDeviceCells) {
                storageDeviceItemsCells.addAll(device.getItemCells());
            }
            return new ArrayList<>(storageDeviceItemsCells);
        } else {
            return storageDeviceItemsCells;
        }
    }

    public Map<Integer, Processor> getProcessorsIds() {
        return processorsIds;
    }

    public List<ICell> getAll() {
        List<ICell> result = new LinkedList<>();
        for (StorageDeviceCell device : storageDeviceCells) {
            result.addAll(device.getItemCells());
            result.addAll(device.getFluidCells());
        }
        return new ArrayList<>(result);
    }

    public List<ICell> getAllFluidCells() {
        if (reworkCell) {
            this.storageDeviceFluidCells = new LinkedList<>();
            for (StorageDeviceCell device : storageDeviceCells) {
                storageDeviceFluidCells.addAll(device.getFluidCells());
            }
            return new ArrayList<>(storageDeviceFluidCells);
        } else {
            return storageDeviceFluidCells;
        }

    }

    public AutoCraftSystem createAutoCraft(AutoCraftSystemVisual autoCraftSystemVisual) {
        Map<SameStack, Integer> stackCountMap = new ConcurrentHashMap<>(autoCraftSystemVisual.getStacksForCraft());
        AutoCraftSystem system = new AutoCraftSystem(autoCraftSystemVisual.getAutoCraftOutput(), autoCraftSystemVisual.getPatternStackList());
        cycle:
        for (Map.Entry<SameStack, Integer> entry : stackCountMap.entrySet()) {
            SameStack sameStack = entry.getKey();
            if (sameStack.isItem()) {
                ItemStack stack = sameStack.getStack();
                int amountToCraft = entry.getValue();
                for (ICell cell : getAllItemCells()) {
                    if (amountToCraft == 0)
                        continue cycle;
                    ResourceLocation rl = BuiltInRegistries.ITEM.getKey(stack.getItem());
                    Map<String, List<StorageStack>> map = cell.getStorageStack();
                    List<StorageStack> list = map.get(rl.toString());
                    if (list != null)
                        for (StorageStack storageStack : list) {
                            if (ModUtils.checkNbtEquality(storageStack.getTag(), stack.getComponents())) {
                                int amount = storageStack.getCount();
                                int toCraft = cell.getStacksForCraft().getOrDefault(storageStack.getSlot(), 0);
                                amount -= toCraft;
                                if (amount != 0) {
                                    int amount2 = Math.min(amount, amountToCraft);
                                    amountToCraft -= amount2;
                                    cell.getStacksForCraft().merge(storageStack.getSlot(), amount2, Integer::sum);
                                    system.addReserve(system.getReservedItemSlots(), rl.toString(), sameStack, amount2);
                                }
                            }
                        }
                }
            }
            if (sameStack.isFluid()) {
                FluidStack stack = sameStack.getFluidStack();
                int amountToCraft = entry.getValue();
                for (ICell cell : getAllFluidCells()) {
                    if (amountToCraft == 0)
                        continue cycle;
                    ResourceLocation rl = BuiltInRegistries.FLUID.getKey(stack.getFluid());
                    Map<String, List<StorageStack>> map = cell.getStorageStack();
                    List<StorageStack> list = map.get(rl.toString());
                    if (list != null)
                        for (StorageStack storageStack : list) {
                            if (ModUtils.checkNbtEquality(storageStack.getTag(), stack.getComponents())) {
                                int amount = storageStack.getCount();
                                int toCraft = cell.getStacksForCraft().getOrDefault(storageStack.getSlot(), 0);
                                amount -= toCraft;
                                if (amount != 0) {
                                    int amount2 = Math.min(amount, amountToCraft);
                                    amountToCraft -= amount2;
                                    cell.getStacksForCraft().merge(storageStack.getSlot(), amount2, Integer::sum);
                                    system.addReserve(system.getReservedFluidSlots(), rl.toString(), sameStack, amount2);
                                }
                            }
                        }
                }
            }
        }
        return system;
    }

    public PatternStack findPatternFor(ItemStack stack) {
        for (Interface intf : interfaces) {
            for (PatternStack ps : intf.getPatterns()) {
                for (SameStack sameStack : ps.output())
                    if (sameStack.isItem() && sameStack.getStack().is(stack.getItem()) && ModUtils.compareNbt(sameStack.getStack().getComponents(), stack.getComponents(), true)) {
                        return ps;
                    }
            }
        }
        return null;
    }

    public PatternStack findPatternFor(FluidStack stack) {
        for (Interface intf : interfaces) {
            for (PatternStack ps : intf.getPatterns()) {
                for (SameStack sameStack : ps.output())
                    if (sameStack.isFluid() && FluidStack.isSameFluid(sameStack.getFluidStack(), stack)) {
                        return ps;
                    }
            }
        }
        return null;
    }

    public void tick(Level level, Energy energy, BlockPos pos) {
        if (!energy.useEnergy(this.consumeEnergy))
            return;
        if (reworkCell) {
            getAllFluidCells();
            getAllItemCells();
            reworkCell = false;
        }
        if (reBuildPatterns) {
            this.getAllPatterns();
            reBuildPatterns = false;
        }
        if (reBuildPreCraft) {
            this.getPreCraftStacks();
            reBuildPreCraft = false;
        }
        if (level.getGameTime() % 2 == 0) {
            for (Import imp : imports)
                workImport(imp);
            for (Export exp : exports)
                workExport(exp);
        }

        stacksMapCount.clear();
        this.updateStacksMapCount();
        if (level.getGameTime() % 1200 == 0) {
            for (SameStack stack : preCraftStacks) {

                int amount = stack.isFluid() ? this.canRemoveExport(stack.getFluidStack()) : this.canRemoveExport(stack.getStack());
                if (amount < stack.getAmount()) {
                    int need = stack.getAmount() - amount;
                    Iterator<Processor> it3 = processors.iterator();
                    mainCycle:
                    while (it3.hasNext()) {
                        Processor processor = it3.next();
                        Iterator<AutoCraftSystem> it = processor.getAutoCrafts().iterator();

                        cycle2:
                        while (it.hasNext()) {
                            AutoCraftSystem autoCraftSystem = it.next();
                            if (autoCraftSystem.getAutoCraftOutput().getSameStack().equals(stack)) {
                                need -= autoCraftSystem.getAutoCraftOutput().getAll();
                                if (need <= 0)
                                    break mainCycle;
                            }
                        }
                    }
                    if (need > 0) {
                        SameStack sameStack = stack.copy();
                        sameStack.setAmount(need);
                        AutoCraftSystemVisual visual = this.createAutoCraftVisual(sameStack);
                        if (visual.canAddToProcessor() && this.hasAvailableProcessors()) {
                            this.addAutoCraft(this.createAutoCraft(visual));
                        }
                    }
                }

            }
        }
        if ((level.getGameTime() % 5 == 0 && canUpdate)) {

            Tuple<List<SameStack>, List<SameStack>> tupleItem = this.getAllStoredItemStacks();
            Tuple<List<SameStack>, List<SameStack>> tupleFluid = this.getAllStoredFluidStacks();
            this.stacks = tupleItem.getA();
            this.fluids = tupleFluid.getA();
            this.stacksCanCreate = tupleItem.getB();
            this.fluidsCanCreate = tupleFluid.getB();
            new PacketUpdateStorageCell(stacks, false, false, pos, level);
            new PacketUpdateStorageCell(fluids, true, false, pos, level);
            new PacketUpdateStorageCell(stacksCanCreate, false, true, pos, level);
            new PacketUpdateStorageCell(fluidsCanCreate, true, true, pos, level);
            new PacketUpdateStorageForCraft(stacksMapCount, pos, level);
        }
        canUpdate = false;
        Iterator<Processor> it3 = processors.iterator();
        mainCycle:
        while (it3.hasNext()) {
            Processor processor = it3.next();
            Iterator<AutoCraftSystem> it = processor.getAutoCrafts().iterator();

            cycle2:
            while (it.hasNext()) {
                AutoCraftSystem autoCraftSystem = it.next();

                Iterator<AutoCraftStack> it1 = autoCraftSystem.getAutoStacks().iterator();
                cycle:
                while (it1.hasNext()) {
                    AutoCraftStack autoCraftStack = it1.next();
                    PatternStack patternStack = autoCraftStack.getPatternStack();
                    Map<SameStack, Integer> tempStorageStackMap = new HashMap<>();
                    if (autoCraftStack.getPatternStack().typeRecipe() == TypeRecipe.WORKBENCH) {

                        cycle1:
                        for (SameStack stack : patternStack.inputs()) {
                            int amount = this.findStackForWorkBench(stack);
                            if (amount == 0) {
                                continue cycle;
                            }
                            if (!stack.getStack().isEmpty()) {
                                if (stack.getStack().getCount() > amount)
                                    continue cycle;
                            }
                            if (!stack.getFluidStack().isEmpty()) {
                                if (stack.getFluidStack().getAmount() > amount)
                                    continue cycle;
                            }
                            tempStorageStackMap.put(stack, amount);
                        }


                        if (tempStorageStackMap.keySet().size() == patternStack.inputs().size()) {
                            int minAmount1 = Integer.MAX_VALUE;
                            for (Map.Entry<SameStack, Integer> storageStackList : tempStorageStackMap.entrySet()) {
                                minAmount1 = Math.min(minAmount1, storageStackList.getValue() / storageStackList.getKey().getAmount());

                            }
                            int craftableByOutput = (getDifferenceStorage()) / autoCraftStack.getSameStack().getAmount();
                            craftableByOutput = Math.min(craftableByOutput, autoCraftStack.getAll() / autoCraftStack.getSameStack().getAmount());
                            craftableByOutput = Math.min(minAmount1, craftableByOutput);


                            if (craftableByOutput > 0) {

                                for (Map.Entry<SameStack, Integer> storageStackList : tempStorageStackMap.entrySet()) {

                                    removeStackWithIgnoringCrafting(storageStackList.getKey().getStack().getItem(), storageStackList.getKey().getStack().getComponents(), craftableByOutput * storageStackList.getKey().getAmount());
                                    autoCraftSystem.removeReserve(storageStackList.getKey(), craftableByOutput * storageStackList.getKey().getAmount());
                                    this.stacksMapCount.get(storageStackList.getKey().getKey()).merge(storageStackList.getKey(), craftableByOutput * storageStackList.getKey().getAmount(), (a, b) -> a - b);
                                }

                                addStackCrafting(autoCraftStack.getSameStack().getStack().getItem(), autoCraftStack.getSameStack().getStack().getComponents(), autoCraftStack.getSameStack().getAmount() * craftableByOutput);
                                this.stacksMapCount.computeIfAbsent(autoCraftStack.getSameStack().getKey(), k -> new HashMap<>()).merge(autoCraftStack.getSameStack(), autoCraftStack.getSameStack().getAmount() * craftableByOutput, Integer::sum);

                                autoCraftSystem.addReserve(autoCraftStack.getSameStack(), autoCraftStack.getSameStack().getAmount() * craftableByOutput);

                                autoCraftStack.removeAllAmount(autoCraftStack.getSameStack().getAmount() * craftableByOutput);
                                if (autoCraftStack.getAll() == 0) {
                                    it1.remove();
                                }
                            }
                        }
                    }
                    if (autoCraftStack.getPatternStack().typeRecipe() == TypeRecipe.BLOCK) {
                        int minAmount1 = Integer.MAX_VALUE;
                        int amountInputs = 0;
                        cycle1:
                        for (SameStack stack : patternStack.inputs()) {
                            int amount = this.findStack(stack);
                            if (amount == 0) {
                                continue cycle;
                            }
                            if (!stack.getStack().isEmpty()) {
                                if (stack.getStack().getCount() > amount)
                                    continue cycle;
                            }
                            if (!stack.getFluidStack().isEmpty()) {
                                if (stack.getFluidStack().getAmount() > amount)
                                    continue cycle;
                            }
                            minAmount1 = Math.min(minAmount1, amount / stack.getAmount());
                            amountInputs += 1;
                        }
                        if (amountInputs == patternStack.inputs().size()) {
                            IMechanismInterface intf = (IMechanismInterface) this.patternStackInterfaceMap.get(autoCraftStack.getPatternStack());
                            if (intf == null) {
                                continue;
                            }
                            InterfaceSide side = intf.getSides(autoCraftStack.getPatternStack());

                            for (Direction direction : side.getDirections()) {
                                if (intf.getBlockEntity(direction) == null) {
                                    continue;
                                }
                                int craftableByOutput = (int) (this.getDifferenceStorage(autoCraftStack.getSameStack().isFluid()) / autoCraftStack.getSameStack().getAmount());
                                craftableByOutput = Math.min(craftableByOutput, (autoCraftStack.getAll() - autoCraftStack.getCreate()) / autoCraftStack.getSameStack().getAmount());
                                craftableByOutput = Math.min(minAmount1, craftableByOutput);

                                BlockEntity blockEntity = intf.getBlockEntity(direction);
                                @Nullable IItemHandler handler = blockEntity.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, intf.getPos().offset(direction.getNormal()), direction);
                                IFluidHandler fluid_handler = blockEntity.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, intf.getPos().offset(direction.getNormal()), direction);
                                if (craftableByOutput > 0 && handler != null) {

                                    Map<Integer, Tuple<ItemStack, Integer>> cachedSlots = new HashMap<>();
                                    Map<Integer, Tuple<FluidStack, Integer>> cachedTanks = new HashMap<>();
                                    Set<Integer> usedSlots = new HashSet<>();
                                    Set<Integer> usedTanks = new HashSet<>();

                                    boolean canInsertAll = true;
                                    int possibleOperations = Integer.MAX_VALUE;
                                    for (SameStack sameStack : patternStack.inputs()) {
                                        if (!sameStack.getStack().isEmpty()) {
                                            if (handler == null) {
                                                canInsertAll = false;
                                                break;
                                            }
                                            int perRecipe = sameStack.getStack().getCount();
                                            int maxNeeded = perRecipe * craftableByOutput;

                                            int remaining = maxNeeded;
                                            int insertedTotal = 0;

                                            for (int slot = 0; slot < handler.getSlots() && remaining > 0; slot++) {
                                                if (usedSlots.contains(slot)) continue;

                                                ItemStack tryStack = sameStack.getStack().copy();
                                                tryStack.setCount(Math.min(remaining, tryStack.getMaxStackSize()));

                                                ItemStack leftoverSim = handler.insertItem(slot, tryStack, true);
                                                int insertedSim = tryStack.getCount() - leftoverSim.getCount();

                                                if (insertedSim > 0) {

                                                    if (insertedSim > 0) {
                                                        remaining -= insertedSim;
                                                        insertedTotal += insertedSim;
                                                        usedSlots.add(slot);

                                                        cachedSlots.put(slot, new Tuple<>(tryStack.copy(), perRecipe));
                                                        break;
                                                    }
                                                }
                                            }

                                            possibleOperations = Math.min(possibleOperations, insertedTotal / perRecipe);

                                            if (possibleOperations <= 0) {
                                                canInsertAll = false;
                                                break;
                                            }

                                            insertedTotal = possibleOperations * perRecipe;
                                        } else if (!sameStack.getFluidStack().isEmpty()) {
                                            if (fluid_handler == null) {
                                                canInsertAll = false;
                                                break;
                                            }

                                            FluidStack fluidStack = sameStack.getFluidStack();
                                            int perRecipe = fluidStack.getAmount();
                                            int maxNeeded = perRecipe * craftableByOutput;

                                            int remaining = maxNeeded;
                                            int insertedTotal = 0;
                                            boolean placed = false;

                                            for (int tank = 0; tank < fluid_handler.getTanks() && remaining > 0; tank++) {
                                                if (usedTanks.contains(tank)) continue;

                                                FluidStack tryFluid = fluidStack.copy();
                                                tryFluid.setAmount(Math.min(remaining, tryFluid.getAmount()));

                                                int filledSim = fluid_handler.fill(tryFluid, IFluidHandler.FluidAction.SIMULATE);
                                                if (filledSim > 0) {


                                                    if (filledSim > 0) {
                                                        remaining -= filledSim;
                                                        insertedTotal += filledSim;
                                                        usedTanks.add(tank);


                                                        cachedTanks.put(tank, new Tuple<>(fluidStack.copy(), possibleOperations));
                                                        placed = true;
                                                        break;
                                                    }
                                                }
                                            }

                                            possibleOperations = Math.min(possibleOperations, insertedTotal / perRecipe);

                                            if (possibleOperations <= 0 || !placed) {
                                                canInsertAll = false;
                                                break;
                                            }

                                            insertedTotal = possibleOperations * perRecipe;
                                        }
                                    }


                                    if (canInsertAll) {
                                        autoCraftStack.addCreate(possibleOperations * autoCraftStack.getSameStack().getAmount());
                                        Map<SameStack, Integer> map = processor.getItemsForCraft(autoCraftSystem.getIndex()).computeIfAbsent(autoCraftStack.getSameStack().getKey(), k -> new HashMap<>());
                                        map.merge(autoCraftStack.getSameStack(), possibleOperations * autoCraftStack.getSameStack().getAmount(), Integer::sum);

                                        for (Map.Entry<Integer, Tuple<ItemStack, Integer>> entry : cachedSlots.entrySet()) {
                                            int slot = entry.getKey();
                                            ItemStack cached = entry.getValue().getA();
                                            cached.setCount(entry.getValue().getB() * possibleOperations);
                                            SameStack stack = new SameStack(entry.getValue().getA());
                                            autoCraftSystem.removeReserve(stack, entry.getValue().getB() * possibleOperations);
                                            this.stacksMapCount.computeIfAbsent(stack.getKey(), k -> new HashMap<>()).merge(stack, entry.getValue().getB() * possibleOperations, (a, b) -> a - b);

                                            this.removeStackCrafting(cached);
                                            handler.insertItem(slot, cached, false);
                                        }
                                        for (Map.Entry<Integer, Tuple<FluidStack, Integer>> entry : cachedTanks.entrySet()) {
                                            FluidStack cached = entry.getValue().getA();
                                            cached.setAmount(entry.getValue().getA().getAmount() * possibleOperations);
                                            this.removeFluidStackCrafting(cached);
                                            SameStack stack = new SameStack(entry.getValue().getA());
                                            autoCraftSystem.removeReserve(stack, entry.getValue().getB() * possibleOperations);
                                            this.stacksMapCount.computeIfAbsent(stack.getKey(), k -> new HashMap<>()).merge(stack, entry.getValue().getB() * possibleOperations, (a, b) -> a - b);

                                            fluid_handler.fill(cached, IFluidHandler.FluidAction.EXECUTE);
                                        }
                                        minAmount1 -= possibleOperations;
                                    }

                                }
                            }

                        }

                    }

                }

                if (autoCraftSystem.getAutoCraftOutput().getPatternStack().typeRecipe() == TypeRecipe.WORKBENCH) {
                    AutoCraftOutput autoCraftStack = autoCraftSystem.getAutoCraftOutput();
                    PatternStack patternStack = autoCraftStack.getPatternStack();
                    int minAmount1 = Integer.MAX_VALUE;
                    int amountInputs = 0;
                    cycle1:
                    for (SameStack stack : patternStack.inputs()) {
                        int amount = this.findStackForWorkBench(stack);
                        if (amount == 0) {
                            continue cycle2;
                        }
                        if (!stack.getStack().isEmpty()) {
                            if (stack.getStack().getCount() > amount)
                                continue cycle2;
                        }
                        if (!stack.getFluidStack().isEmpty()) {
                            if (stack.getFluidStack().getAmount() > amount)
                                continue cycle2;
                        }
                        minAmount1 = Math.min(minAmount1, amount / stack.getAmount());
                        amountInputs += 1;
                    }


                    if (amountInputs == patternStack.inputs().size()) {


                        int craftableByOutput = (getDifferenceStorage()) / autoCraftStack.getSameStack().getAmount();
                        craftableByOutput = Math.min(craftableByOutput, autoCraftStack.getAll() / autoCraftStack.getSameStack().getAmount());
                        craftableByOutput = Math.min(minAmount1, craftableByOutput);


                        if (craftableByOutput > 0) {
                            for (SameStack stack : patternStack.inputs()) {
                                removeStackWithIgnoringCrafting(stack.getStack().getItem(), stack.getStack().getComponents(), craftableByOutput * stack.getAmount());
                                this.stacksMapCount.get(stack.getKey()).merge(stack, craftableByOutput * stack.getAmount(), (a, b) -> a - b);
                                autoCraftSystem.removeReserve(stack, craftableByOutput * stack.getAmount());
                            }

                            addStack(autoCraftStack.getSameStack().getStack().getItem(), autoCraftStack.getSameStack().getStack().getComponents(), autoCraftStack.getSameStack().getAmount() * craftableByOutput);
                            autoCraftSystem.removeCountAutoCraftStack(craftableByOutput);
                        }
                    }
                }
                if (autoCraftSystem.getAutoCraftOutput().patternStack().typeRecipe() == TypeRecipe.BLOCK) {
                    int minAmount1 = Integer.MAX_VALUE;
                    int amountInputs = 0;
                    PatternStack patternStack = autoCraftSystem.getAutoCraftOutput().patternStack();
                    AutoCraftOutput autoCraftStack = autoCraftSystem.getAutoCraftOutput();
                    for (SameStack stack : autoCraftSystem.getAutoCraftOutput().patternStack().inputs()) {
                        int amount = this.findStack(stack);
                        if (amount == 0) {
                            continue cycle2;
                        }
                        if (!stack.getStack().isEmpty()) {
                            if (stack.getStack().getCount() > amount)
                                continue cycle2;
                        }
                        if (!stack.getFluidStack().isEmpty()) {
                            if (stack.getFluidStack().getAmount() > amount)
                                continue cycle2;
                        }
                        minAmount1 = Math.min(minAmount1, amount / stack.getAmount());
                        amountInputs += 1;
                    }
                    if (amountInputs == patternStack.inputs().size()) {
                        IMechanismInterface intf = (IMechanismInterface) this.patternStackInterfaceMap.get(autoCraftStack.getPatternStack());
                        if (intf == null) {
                            continue;
                        }
                        InterfaceSide side = intf.getSides(autoCraftStack.getPatternStack());

                        for (Direction direction : side.getDirections()) {
                            if (intf.getBlockEntity(direction) == null) {
                                continue;
                            }


                            int craftableByOutput = (int) (this.getDifferenceStorage(autoCraftStack.getSameStack().isFluid()) / autoCraftStack.getSameStack().getAmount());
                            craftableByOutput = Math.min(craftableByOutput, (autoCraftStack.getAll() - autoCraftStack.getCreate()) / autoCraftStack.getSameStack().getAmount());
                            craftableByOutput = Math.min(minAmount1, craftableByOutput);

                            BlockEntity blockEntity = intf.getBlockEntity(direction);
                            @Nullable IItemHandler handler = blockEntity.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, intf.getPos().offset(direction.getNormal()), direction);
                            IFluidHandler fluid_handler = blockEntity.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, intf.getPos().offset(direction.getNormal()), direction);
                            if (craftableByOutput > 0 && handler != null) {

                                Map<Integer, Tuple<ItemStack, Integer>> cachedSlots = new HashMap<>();
                                Map<Integer, Tuple<FluidStack, Integer>> cachedTanks = new HashMap<>();
                                Set<Integer> usedSlots = new HashSet<>();
                                Set<Integer> usedTanks = new HashSet<>();

                                boolean canInsertAll = true;
                                int possibleOperations = Integer.MAX_VALUE;
                                for (SameStack sameStack : patternStack.inputs()) {
                                    if (!sameStack.getStack().isEmpty()) {
                                        if (handler == null) {
                                            canInsertAll = false;
                                            break;
                                        }
                                        int perRecipe = sameStack.getStack().getCount();
                                        int maxNeeded = perRecipe * craftableByOutput;

                                        int remaining = maxNeeded;
                                        int insertedTotal = 0;

                                        for (int slot = 0; slot < handler.getSlots() && remaining > 0; slot++) {
                                            if (usedSlots.contains(slot)) continue;

                                            ItemStack tryStack = sameStack.getStack().copy();
                                            tryStack.setCount(Math.min(remaining, tryStack.getMaxStackSize()));

                                            ItemStack leftoverSim = handler.insertItem(slot, tryStack, true);
                                            int insertedSim = tryStack.getCount() - leftoverSim.getCount();

                                            if (insertedSim > 0) {

                                                remaining -= insertedSim;
                                                insertedTotal += insertedSim;
                                                usedSlots.add(slot);
                                                cachedSlots.put(slot, new Tuple<>(tryStack, perRecipe));
                                                break;
                                            }
                                        }

                                        possibleOperations = Math.min(possibleOperations, insertedTotal / perRecipe);

                                        if (possibleOperations <= 0) {
                                            canInsertAll = false;
                                            break;
                                        }

                                        insertedTotal = possibleOperations * perRecipe;
                                    } else if (!sameStack.getFluidStack().isEmpty()) {
                                        if (fluid_handler == null) {
                                            canInsertAll = false;
                                            break;
                                        }

                                        FluidStack fluidStack = sameStack.getFluidStack();
                                        int perRecipe = fluidStack.getAmount();
                                        int maxNeeded = perRecipe * craftableByOutput;

                                        int remaining = maxNeeded;
                                        int insertedTotal = 0;
                                        boolean placed = false;

                                        for (int tank = 0; tank < fluid_handler.getTanks() && remaining > 0; tank++) {
                                            if (usedTanks.contains(tank)) continue;

                                            FluidStack tryFluid = fluidStack.copy();
                                            tryFluid.setAmount(Math.min(remaining, tryFluid.getAmount()));

                                            int filledSim = fluid_handler.fill(tryFluid, IFluidHandler.FluidAction.SIMULATE);
                                            if (filledSim > 0) {


                                                remaining -= filledSim;
                                                insertedTotal += filledSim;
                                                usedTanks.add(tank);


                                                cachedTanks.put(tank, new Tuple<>(tryFluid, possibleOperations));
                                                placed = true;
                                                break;
                                            }
                                        }

                                        possibleOperations = Math.min(possibleOperations, insertedTotal / perRecipe);

                                        if (possibleOperations <= 0 || !placed) {
                                            canInsertAll = false;
                                            break;
                                        }

                                        insertedTotal = possibleOperations * perRecipe;
                                    }
                                }


                                if (canInsertAll) {
                                    autoCraftStack.addCreate(possibleOperations * autoCraftStack.getSameStack().getAmount());

                                    Map<SameStack, Integer> map = processor.getItemsForCraft(autoCraftSystem.getIndex()).computeIfAbsent(autoCraftStack.getSameStack().getKey(), k -> new HashMap<>());
                                    map.merge(autoCraftStack.getSameStack(), possibleOperations * autoCraftStack.getSameStack().getAmount(), Integer::sum);


                                    for (Map.Entry<Integer, Tuple<ItemStack, Integer>> entry : cachedSlots.entrySet()) {
                                        int slot = entry.getKey();
                                        ItemStack cached = entry.getValue().getA();
                                        cached.setCount(entry.getValue().getB() * possibleOperations);
                                        SameStack stack = new SameStack(entry.getValue().getA());
                                        autoCraftSystem.removeReserve(stack, entry.getValue().getB() * possibleOperations);
                                        this.stacksMapCount.computeIfAbsent(stack.getKey(), k -> new HashMap<>()).merge(stack, entry.getValue().getB() * possibleOperations, (a, b) -> a - b);
                                        this.removeStackCrafting(cached);
                                        handler.insertItem(slot, cached, false);
                                    }
                                    for (Map.Entry<Integer, Tuple<FluidStack, Integer>> entry : cachedTanks.entrySet()) {
                                        FluidStack cached = entry.getValue().getA();
                                        SameStack stack = new SameStack(entry.getValue().getA());
                                        cached.setAmount(entry.getValue().getA().getAmount() * possibleOperations);
                                        autoCraftSystem.removeReserve(stack, entry.getValue().getB() * possibleOperations);
                                        this.stacksMapCount.computeIfAbsent(stack.getKey(), k -> new HashMap<>()).merge(stack, entry.getValue().getB() * possibleOperations, (a, b) -> a - b);
                                        this.removeFluidStackCrafting(cached);
                                        fluid_handler.fill(cached, IFluidHandler.FluidAction.EXECUTE);
                                    }
                                    minAmount1 -= possibleOperations;
                                }
                            }
                        }


                    }

                }
                if (autoCraftSystem.isEnd()) {
                    processor.removeNetworkCraft(autoCraftSystem.getIndex());
                    it.remove();
                }

            }
        }


    }

    private void updateStacksMapCount() {

        for (ICell cell : getAllItemCells()) {
            Map<String, List<StorageStack>> entries = cell.getStorageStack();
            ItemStack[] stacks = cell.getStacks();
            Map<Integer, Integer> toCraft = cell.getStacksForCraft();

            for (Map.Entry<String, List<StorageStack>> entry : entries.entrySet()) {

                for (StorageStack storageStack : entry.getValue()) {
                    SameStack sameStack = new SameStack(stacks[storageStack.getSlot()]);
                    int amount = toCraft.getOrDefault(storageStack.getSlot(), 0);
                    if (amount != 0)
                        stacksMapCount.computeIfAbsent(entry.getKey(), k -> new HashMap<>()).merge(sameStack, amount, Integer::sum);
                }
            }
        }
        for (ICell cell : getAllFluidCells()) {
            Map<String, List<StorageStack>> entries = cell.getStorageStack();
            FluidStack[] stacks = cell.getFluids();
            Map<Integer, Integer> toCraft = cell.getStacksForCraft();

            for (Map.Entry<String, List<StorageStack>> entry : entries.entrySet()) {

                for (StorageStack storageStack : entry.getValue()) {
                    SameStack sameStack = new SameStack(stacks[storageStack.getSlot()]);
                    int amount = toCraft.getOrDefault(storageStack.getSlot(), 0);
                    if (amount != 0)
                        stacksMapCount.computeIfAbsent(entry.getKey(), k -> new HashMap<>()).merge(sameStack, amount, Integer::sum);
                }
            }
        }
    }

    public void addStack(ItemStack addItem) {
        DataComponentMap tag = addItem.getComponents();
        Item item = addItem.getItem();
        for (ICell cell : getAllItemCells()) {
            if (addItem.isEmpty())
                return;
            if (addItem.getCount() <= 0)
                return;
            int amount = cell.canAddStack(addItem);
            if (amount > 0) {
                addItem.shrink(amount);
                cell.addStack(item, tag, amount);
            }
        }
    }

    public void addStack(Item addItem, DataComponentMap tag, int amount) {

        for (ICell cell : getAllItemCells()) {
            if (addItem == Items.AIR)
                return;
            if (amount <= 0)
                return;
            int amount1 = cell.canAddStack(addItem, tag, amount);
            if (amount1 > 0) {
                amount -= amount1;
                cell.addStack(addItem, tag, amount1);
            }
        }
    }

    public void addStackImport(ItemStack itemStack, Item addItem, DataComponentMap tag, int amount) {
        if (addItem == null || addItem == Items.AIR) return;
        if (amount <= 0) return;

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(addItem);
        String itemKey = itemId.toString();


        SameStack requestedStack = new SameStack(itemStack);


        int totalReservedAmount = 0;
        List<Tuple<Tuple<Integer, Integer>, Map<String, Map<SameStack, Integer>>>> processorsWithReservations = new ArrayList<>();

        for (int i = 0; i < processors.size(); i++) {
            Processor processor = processors.get(i);
            for (Integer autoCraftSystems : processor.getIndexes()) {
                Map<String, Map<SameStack, Integer>> craftMap = processor.getItemsForCraft(autoCraftSystems);
                if (craftMap == null) continue;

                Map<SameStack, Integer> reservedMap = craftMap.get(itemKey);
                if (reservedMap == null || reservedMap.isEmpty()) continue;

                Integer reservedForThisStack = reservedMap.get(requestedStack);
                if (reservedForThisStack == null || reservedForThisStack <= 0) continue;

                totalReservedAmount += reservedForThisStack;
                processorsWithReservations.add(new Tuple<>(new Tuple<>(i, autoCraftSystems), craftMap));
            }
        }


        for (ICell cell : getAllItemCells()) {
            if (amount <= 0) break;

            int acceptedByCell = cell.canAddStack(addItem, tag, amount);
            if (acceptedByCell <= 0) continue;

            amount -= acceptedByCell;

            int reservedPart = 0;
            if (totalReservedAmount > 0) {

                int toConsume = acceptedByCell;

                for (Tuple<Tuple<Integer, Integer>, Map<String, Map<SameStack, Integer>>> processorMap : processorsWithReservations) {
                    if (toConsume <= 0) break;

                    Map<SameStack, Integer> reservedMap = processorMap.getB().get(itemKey);
                    if (reservedMap == null || reservedMap.isEmpty()) continue;

                    Integer reservedForStack = reservedMap.get(requestedStack);
                    if (reservedForStack == null || reservedForStack <= 0) continue;

                    int taken = Math.min(reservedForStack, toConsume);
                    toConsume -= taken;
                    reservedMap.merge(requestedStack, taken, (a, b) -> a - b);

                    reservedPart += taken;
                    cell.addStackCrafting(addItem, tag, taken);
                    AutoCraftSystem autoCraftSystem = processors.get(processorMap.getA().getA()).getAutoCraft(processorMap.getA().getB());


                    if (!autoCraftSystem.getAutoStacks().isEmpty()) {
                        Iterator<AutoCraftStack> it1 = autoCraftSystem.getAutoStacks().iterator();
                        cycle:
                        while (it1.hasNext()) {
                            AutoCraftStack autoCraftStack = it1.next();
                            if (taken == 0)
                                break;
                            if (autoCraftStack.getCreate() > 0 && autoCraftStack.getPatternStack().typeRecipe() == TypeRecipe.BLOCK && autoCraftStack.getSameStack().isItem() && autoCraftStack.getSameStack().equals(requestedStack)) {

                                int canRemove = Math.min(autoCraftStack.getCreate(), taken);
                                this.stacksMapCount.computeIfAbsent(autoCraftStack.getSameStack().getKey(), k -> new HashMap<>()).merge(autoCraftStack.getSameStack(), canRemove, Integer::sum);

                                autoCraftStack.removeAllAmount(canRemove);
                                autoCraftStack.removeCreate(canRemove);
                                autoCraftSystem.addReserve(autoCraftStack.getSameStack(), canRemove);
                                taken -= canRemove;
                                if (autoCraftStack.getAll() == 0) {
                                    it1.remove();
                                    break;
                                }
                            }
                        }
                    }
                    if (taken > 0) {
                        AutoCraftOutput autoCraftStack = autoCraftSystem.getAutoCraftOutput();
                        if (autoCraftStack.getCreate() > 0 && autoCraftStack.getPatternStack().typeRecipe() == TypeRecipe.BLOCK && autoCraftStack.getSameStack().isItem() && autoCraftStack.getSameStack().equals(requestedStack)) {
                            int canRemove = Math.min(autoCraftStack.getCreate(), taken);
                            autoCraftStack.removeAllAmount(canRemove);
                            autoCraftStack.removeCreate(canRemove);
                            cell.removeItemFromListCraft(autoCraftStack.getSameStack(), canRemove);

                            taken -= canRemove;
                            if (autoCraftStack.getAll() == 0) {
                                processors.get(processorMap.getA().getA()).removeCraft(processorMap.getA().getB());
                                break;
                            }
                        }
                    }
                }
            }


            int normalPart = acceptedByCell - reservedPart;
            if (normalPart > 0) {
                cell.addStack(addItem, tag, normalPart);
            }
        }

    }

    public void addStackCrafting(Item addItem, DataComponentMap tag, int amount) {

        for (ICell cell : getAllItemCells()) {
            if (addItem == Items.AIR)
                return;
            if (amount <= 0)
                return;
            int amount1 = cell.canAddStack(addItem, tag, amount);
            if (amount1 > 0) {
                amount -= amount1;
                cell.addStackCrafting(addItem, tag, amount1);
            }
        }
    }

    public void addStackCraftingOutput(Item addItem, DataComponentMap tag, int amount) {

        for (ICell cell : getAllItemCells()) {
            if (addItem == Items.AIR)
                return;
            if (amount <= 0)
                return;
            int amount1 = cell.canAddStack(addItem, tag, amount);
            if (amount1 > 0) {
                amount -= amount1;
                cell.addStack(addItem, tag, amount1);
            }
        }
    }

    public void addStackImport(FluidStack keyStack, Fluid fluid, DataComponentMap tag, int amount) {
        if (fluid == null || fluid == Fluids.EMPTY) return;
        if (amount <= 0) return;

        ResourceLocation rl = BuiltInRegistries.FLUID.getKey(fluid);
        String fluidKey = rl.toString();
        SameStack requestedStack = new SameStack(keyStack);
        int totalReservedAmount = 0;
        List<Tuple<Tuple<Integer, Integer>, Map<String, Map<SameStack, Integer>>>> processorsWithReservations = new ArrayList<>();

        for (int i = 0; i < processors.size(); i++) {
            Processor processor = processors.get(i);


            for (Integer autoCraftSystems : processor.getIndexes()) {
                Map<String, Map<SameStack, Integer>> craftMap = processor.getItemsForCraft(autoCraftSystems);
                if (craftMap == null) continue;

                Map<SameStack, Integer> reservedMap = craftMap.get(fluidKey);
                if (reservedMap == null || reservedMap.isEmpty()) continue;

                Integer reservedForThisStack = reservedMap.get(requestedStack);
                if (reservedForThisStack == null || reservedForThisStack <= 0) continue;

                totalReservedAmount += reservedForThisStack;
                processorsWithReservations.add(new Tuple<>(new Tuple<>(i, autoCraftSystems), craftMap));
            }
        }

        for (ICell cell : getAllFluidCells()) {
            if (amount <= 0) break;

            int acceptedByCell = cell.canAddStack(fluid, tag, amount);
            if (acceptedByCell <= 0) continue;

            amount -= acceptedByCell;

            int reservedPart = 0;

            if (totalReservedAmount > 0) {
                reservedPart = Math.min(acceptedByCell, totalReservedAmount);
                totalReservedAmount -= reservedPart;

                int toConsume = reservedPart;

                for (Tuple<Tuple<Integer, Integer>, Map<String, Map<SameStack, Integer>>> processorMap : processorsWithReservations) {
                    if (toConsume <= 0) break;

                    Map<SameStack, Integer> reservedMap = processorMap.getB().get(fluidKey);
                    if (reservedMap == null || reservedMap.isEmpty()) continue;

                    Integer reservedForStack = reservedMap.get(requestedStack);
                    if (reservedForStack == null || reservedForStack <= 0) continue;

                    int taken = Math.min(reservedForStack, toConsume);
                    toConsume -= taken;

                    int remainingReserved = reservedForStack - taken;

                    if (remainingReserved <= 0) {
                        reservedMap.remove(requestedStack);
                        if (reservedMap.isEmpty()) {
                            processorMap.getB().remove(fluidKey);
                        }
                    } else {
                        reservedMap.put(requestedStack, remainingReserved);
                    }

                    AutoCraftSystem autoCraftSystem = processors.get(processorMap.getA().getA()).getAutoCraft(processorMap.getA().getB());


                    cell.addFluidCrafting(fluid, tag, taken);
                    if (!autoCraftSystem.getAutoStacks().isEmpty()) {
                        Iterator<AutoCraftStack> it1 = autoCraftSystem.getAutoStacks().iterator();
                        cycle:
                        while (it1.hasNext()) {
                            AutoCraftStack autoCraftStack = it1.next();
                            if (taken == 0)
                                break;
                            if (autoCraftStack.getCreate() > 0 && autoCraftStack.getPatternStack().typeRecipe() == TypeRecipe.BLOCK && autoCraftStack.getSameStack().isFluid() && autoCraftStack.getSameStack().equals(requestedStack)) {
                                int canRemove = Math.min(autoCraftStack.getCreate(), taken);
                                autoCraftStack.removeAllAmount(canRemove);
                                autoCraftStack.removeCreate(canRemove);
                                this.stacksMapCount.computeIfAbsent(autoCraftStack.getSameStack().getKey(), k -> new HashMap<>()).merge(autoCraftStack.getSameStack(), canRemove, Integer::sum);

                                autoCraftSystem.addReserve(autoCraftStack.getSameStack(), canRemove);
                                taken -= canRemove;
                                if (autoCraftStack.getAll() == 0) {
                                    it1.remove();
                                    break;
                                }
                            }
                        }
                    }
                    if (taken > 0) {
                        AutoCraftOutput autoCraftStack = autoCraftSystem.getAutoCraftOutput();
                        if (autoCraftStack.getCreate() > 0 && autoCraftStack.getPatternStack().typeRecipe() == TypeRecipe.BLOCK && autoCraftStack.getSameStack().isFluid() && autoCraftStack.getSameStack().equals(requestedStack)) {
                            int canRemove = Math.min(autoCraftStack.getCreate(), taken);
                            autoCraftStack.removeAllAmount(canRemove);
                            autoCraftStack.removeCreate(canRemove);
                            cell.removeFluidFromListCraft(autoCraftStack.getSameStack(), canRemove);

                            taken -= canRemove;
                            if (autoCraftStack.getAll() == 0) {
                                processors.get(processorMap.getA().getA()).removeCraft(processorMap.getA().getB());
                                break;
                            }
                        }
                    }
                }


                if (toConsume > 0) {
                    cell.addFluid(fluid, tag, toConsume);
                }
            }


            int normalPart = acceptedByCell - reservedPart;
            if (normalPart > 0) {
                cell.addFluid(fluid, tag, normalPart);
            }
        }
    }

    public void addStack(Fluid fluid, DataComponentMap tag, int amount) {


        for (ICell cell : getAllFluidCells()) {
            if (fluid == Fluids.EMPTY)
                return;
            if (amount <= 0)
                return;
            int amount1 = cell.canAddStack(fluid, tag, amount);
            if (amount1 > 0) {
                amount -= amount1;
                cell.addFluid(fluid, tag, amount1);
            }
        }
    }

    public void addStack(FluidStack addItem) {
        DataComponentMap tag = addItem.getComponents();
        Fluid fluid = addItem.getFluid();
        for (ICell cell : getAllFluidCells()) {
            if (addItem.isEmpty())
                return;
            if (addItem.getAmount() <= 0)
                return;
            int amount = cell.canAddStack(addItem);
            if (amount > 0) {
                addItem.shrink(amount);
                cell.addFluid(fluid, tag, amount);
            }
        }
    }

    public int getDifferenceStorage() {
        long sum = 0L;

        for (ICell cell : getAllItemCells()) {
            long diff = (long) cell.getCellInfo().capacity() - cell.getStorage();

            if (diff <= 0) {
                continue;
            }


            if (diff > STORAGE_LIMIT) {
                sum = STORAGE_LIMIT;
                break;
            }


            if (sum + diff > STORAGE_LIMIT) {
                sum = STORAGE_LIMIT;
                break;
            }

            sum += diff;
        }

        return (int) sum;
    }

    public int getDifferenceStorage(boolean fluid) {
        long sum = 0L;
        Iterable<ICell> cells = fluid ? getAllFluidCells() : getAllItemCells();

        for (ICell cell : cells) {
            long diff = (long) cell.getCellInfo().capacity() - cell.getStorage();

            if (diff <= 0) {
                continue;
            }


            if (diff > STORAGE_LIMIT) {
                sum = STORAGE_LIMIT;
                break;
            }


            if (sum + diff > STORAGE_LIMIT) {
                sum = STORAGE_LIMIT;
                break;
            }

            sum += diff;
        }

        return (int) sum;
    }

    public double getItemStorage() {
        double sum = 0;
        for (ICell cell : getAllItemCells()) {
            sum += cell.getStorage();
        }
        return sum;
    }

    public double getItemMaxStorage() {
        double sum = 0;
        for (ICell cell : getAllItemCells()) {
            sum += cell.getCellInfo().capacity();
        }
        return sum;
    }

    public int findStackForWorkBench(SameStack stack) {


        Map<SameStack, Integer> map = stacksMapCount.get(stack.getKey());
        if (map == null)
            return 0;
        Integer amount = map.get(stack);
        if (amount == null || amount == 0)
            return 0;
        return amount;
    }

    public int findStack(SameStack stack) {
        ResourceLocation rl;
        if (!stack.getStack().isEmpty()) {
            rl = BuiltInRegistries.ITEM.getKey(stack.getStack().getItem());
        } else {
            rl = BuiltInRegistries.FLUID.getKey(stack.getFluidStack().getFluid());
        }
        Map<SameStack, Integer> map = stacksMapCount.get(rl.toString());
        if (map == null)
            return 0;
        Integer amount = map.get(stack);
        if (amount == null || amount == 0)
            return 0;
        return amount;
    }

    public void workImport(Import imp) {

        TypeRedstone redstone = imp.getRedstone();
        if (redstone != TypeRedstone.NONE) {
            int levelRedstone = imp.getRedstoneSignal();
            boolean can = (TypeRedstone.FULL == redstone && levelRedstone == 15) || (TypeRedstone.HALF == redstone && levelRedstone > 7);
            if (!can)
                return;
        }
        TypeStack typeStack = imp.getTypeStack();
        BlockEntity block = imp.getBlockEntityNeighbor();
        List<ItemStack> listStacks = imp.getStacks();
        List<FluidStack> listFluidStacks = imp.getFluidStacks();
        boolean isBlackList = imp.getTypeSlots() == EnumTypeSlots.BLACKLIST;

        if (block == null) return;

        if (typeStack == TypeStack.ITEM) {
            IItemHandler itemHandler = block.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, block.getBlockPos(), imp.getDirection());
            if (itemHandler == null) return;
            int differenceStorage = (int) this.getDifferenceStorage();
            if (differenceStorage > 0) {
                for (int slot = 0; slot < itemHandler.getSlots() && differenceStorage > 0; slot++) {
                    ItemStack candidate = itemHandler.extractItem(slot, differenceStorage, true);
                    if (candidate.isEmpty()) continue;

                    boolean allowed = checkItemFilter(imp, candidate, listStacks, isBlackList, slot);
                    if (!allowed) continue;

                    int canAdd = this.canAdd(candidate);
                    if (canAdd > 0) {
                        ItemStack extracted = itemHandler.extractItem(slot, canAdd, false);
                        if (!extracted.isEmpty()) {
                            this.addStackImport(extracted, extracted.getItem(), extracted.getComponents(), extracted.getCount());
                            differenceStorage = this.getDifferenceStorage();
                        }
                    }
                }
            }

        }

        if (typeStack == TypeStack.FLUID) {
            IFluidHandler fluidHandler = block.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, block.getBlockPos(), imp.getDirection());
            if (fluidHandler == null) return;


            int differenceStorage = this.getDifferenceStorage(true);
            if (differenceStorage > 0) {
                for (int slot = 0; slot < fluidHandler.getTanks() && differenceStorage > 0; slot++) {
                    FluidStack fluidStack = fluidHandler.getFluidInTank(slot);
                    FluidStack simulate = fluidHandler.drain(fluidStack.copy(), IFluidHandler.FluidAction.SIMULATE);
                    if (!simulate.isEmpty()) {
                        boolean allowed = checkFluidFilter(imp, simulate, listFluidStacks, isBlackList, slot);
                        if (!allowed) return;

                        int canAdd = this.canAdd(simulate);
                        if (canAdd > 0) {
                            simulate.setAmount(canAdd);
                            FluidStack drained = fluidHandler.drain(simulate, IFluidHandler.FluidAction.EXECUTE);
                            if (!drained.isEmpty()) {
                                this.addStackImport(drained, drained.getFluid(), drained.getComponents(), drained.getAmount());
                                differenceStorage = this.getDifferenceStorage();
                            }
                        }
                    }
                }
            }

        }

    }

    public void workExport(Export exp) {
        TypeRedstone redstone = exp.getRedstone();
        if (redstone != TypeRedstone.NONE) {
            int levelRedstone = exp.getRedstoneSignal();
            boolean can = (TypeRedstone.FULL == redstone && levelRedstone == 15) || (TypeRedstone.HALF == redstone && levelRedstone > 7);
            if (!can)
                return;
        }
        TypeStack typeStack = exp.getTypeStack();
        BlockEntity block = exp.getBlockEntityNeighbor();
        List<ItemStack> listStacks = exp.getStacks();
        List<FluidStack> listFluidStacks = exp.getFluidStacks();

        if (block == null) return;

        if (typeStack == TypeStack.ITEM) {

            if (listStacks == null || listStacks.isEmpty()) return;

            IItemHandler itemHandler = block.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, block.getBlockPos(), exp.getDirection());
            if (itemHandler == null) return;

            TypeSlot slotType = exp.getSlotMode();

            int n = listStacks.size();
            int[] order = null;

            if (slotType == TypeSlot.RANDOM) {
                order = new int[n];
                for (int i = 0; i < n; i++) order[i] = i;

                for (int i = n - 1; i > 0; i--) {
                    int j = IUCore.random.nextInt(i + 1);
                    int tmp = order[i];
                    order[i] = order[j];
                    order[j] = tmp;
                }
            }

            for (int t = 0; t < n; t++) {
                int i = (slotType == TypeSlot.QUEUE) ? t : order[t];
                ItemStack filter = listStacks.get(i);
                if (filter.isEmpty()) continue;


                int available = this.canRemoveExport(filter);
                if (available <= 0) continue;

                ItemStack toInsert = new ItemStack(BuiltInRegistries.ITEM.wrapAsHolder(filter.getItem()), Math.min(available, filter.getMaxStackSize() * 4), filter.getComponentsPatch());
                ItemStack remaining = insertIntoHandler(exp, itemHandler, toInsert, true, i);

                int inserted = Math.min(available, filter.getMaxStackSize() * 4) - remaining.getCount();
                if (inserted > 0) {

                    int extracted = this.removeStackWithIgnoring(filter.getItem(), filter.getComponents(), inserted);
                    toInsert.setCount(extracted);
                    if (extracted != 0) {
                        insertIntoHandler(exp, itemHandler, toInsert, false, i);
                    }
                }
            }
        }

        if (typeStack == TypeStack.FLUID) {

            if (listFluidStacks == null || listFluidStacks.isEmpty()) return;

            IFluidHandler fluidHandler = block.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, block.getBlockPos(), exp.getDirection());
            if (fluidHandler == null) return;


            int n = listStacks.size();
            int[] order = null;
            TypeSlot slotType = exp.getSlotMode();

            if (slotType == TypeSlot.RANDOM) {
                order = new int[n];
                for (int i = 0; i < n; i++) order[i] = i;

                for (int i = n - 1; i > 0; i--) {
                    int j = IUCore.random.nextInt(i + 1);
                    int tmp = order[i];
                    order[i] = order[j];
                    order[j] = tmp;
                }
            }
            for (int t = 0; t < listFluidStacks.size(); t++) {
                int i = (slotType == TypeSlot.QUEUE) ? t : order[t];

                FluidStack filter = listFluidStacks.get(i);
                if (filter.isEmpty()) continue;


                int available = this.canRemoveExport(filter);
                if (available <= 0) continue;
                FluidStack availableFluid = new FluidStack(BuiltInRegistries.FLUID.wrapAsHolder(filter.getFluid()), available, filter.getComponentsPatch());
                int canFill = fluidHandler.fill(availableFluid, IFluidHandler.FluidAction.SIMULATE);
                if (canFill > 0) {
                    availableFluid.setAmount(canFill);
                    this.removeStack(availableFluid);
                    fluidHandler.fill(availableFluid, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }

    public int removeStack(FluidStack request) {
        if (request == null || request.isEmpty()) {
            return 0;
        }

        int remaining = request.getAmount();
        int amount1 = 0;

        for (ICell cell : getAllFluidCells()) {
            if (remaining <= 0) break;
            request.setAmount(remaining);

            int extracted = cell.removeFluid(request);
            remaining -= extracted;
            amount1 += extracted;
        }

        return amount1;

    }


    public ItemStack insertIntoHandler(Export export, IItemHandler handler, ItemStack stack, boolean simulate, int needSlot) {
        ItemStack remaining = stack.copy();
        int index = export.getIndexFromSlot(needSlot);
        for (int slot = 0; slot < handler.getSlots() && !remaining.isEmpty(); slot++) {
            if ((index == -1 || index == slot))
                remaining = handler.insertItem(slot, remaining, simulate);
        }
        return remaining;
    }


    public boolean checkItemFilter(Import imp, ItemStack stack, List<ItemStack> filters, boolean isBlackList, int slot) {
        if (filters == null || filters.isEmpty()) {
            return isBlackList;
        }
        TypeComponent component = imp.getComponent();
        for (int i = 0; i < filters.size(); i++) {
            ItemStack filter = filters.get(i);
            int index = imp.getIndexFromSlot(i);
            if ((index == -1 || index == slot) && !filter.isEmpty() && filter.getItem() == stack.getItem()) {
                if (component == TypeComponent.IGNORE || ((filter.getComponents() == null && stack.getComponents() == null) ||
                        ModUtils.compareNbt(filter.getComponents(), stack.getComponents(), true))) {
                    return !isBlackList;
                }
            }
        }

        return isBlackList;
    }


    public boolean checkFluidFilter(Import imp, FluidStack stack, List<FluidStack> filters, boolean isBlackList, int slot) {
        if (filters == null || filters.isEmpty()) {
            return !isBlackList;
        }
        TypeComponent component = imp.getComponent();
        for (int i = 0; i < filters.size(); i++) {
            FluidStack filter = filters.get(i);
            int index = imp.getIndexFromSlot(i);
            if ((index == -1 || index == slot) && !filter.isEmpty() && filter.getFluid() == stack.getFluid()) {
                if (component == TypeComponent.IGNORE || (filter.getComponentsPatch().isEmpty() || ModUtils.compareNbt(filter.getComponents(), stack.getComponents(), true))) {
                    return !isBlackList;
                }
            }
        }

        return isBlackList;
    }


    public int canAdd(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }

        int inserted = 0;

        for (ICell cell : getAllItemCells()) {
            if (stack.isEmpty()) break;

            int accepted = cell.canAddStack(stack);
            if (accepted > 0) {
                inserted += accepted;
                stack.shrink(accepted);
            }
        }

        return inserted;
    }

    public int canAdd(FluidStack stack) {
        if (stack == null || stack.isEmpty()) return 0;

        FluidStack copy = stack.copy();
        int inserted = 0;

        for (ICell cell : getAllFluidCells()) {
            if (copy.isEmpty()) break;
            int accepted = cell.canAddStack(copy);
            if (accepted > 0) {
                inserted += accepted;
                copy.shrink(accepted);
            }
        }
        return inserted;
    }

    public int canRemove(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;

        ItemStack copy = stack.copy();
        int removable = 0;

        for (ICell cell : getAllItemCells()) {
            if (copy.isEmpty()) break;

            List<StorageStack> availableStacks = cell.getStorageStackFromItem(copy);
            for (StorageStack s : availableStacks) {
                int take = Math.min(s.getCount(), copy.getCount()) - cell.getStacksForCraft().getOrDefault(s.getSlot(), 0);
                removable += take;
                copy.shrink(take);
                if (copy.isEmpty()) break;
            }
        }
        return removable;
    }

    public int canRemoveMonitor(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;


        int removable = 0;
        int min = stack.getCount();
        for (ICell cell : getAllItemCells()) {
            List<StorageStack> availableStacks = cell.getStorageStackFromItem(stack);
            for (StorageStack s : availableStacks) {
                if (ModUtils.compareNbt(s.getTag(), stack.getComponents(), true)) {
                    int take = s.getCount();
                    removable += take;
                }
            }
        }
        ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(stack.getItem());
        Map<SameStack, Integer> map = stacksMapCount.get(resourceLocation.toString());
        if (map == null)
            return Math.min(removable, min);
        else {
            int count = map.getOrDefault(new SameStack(stack), 0);
            return Math.min(removable - count, min);
        }
    }

    public int canRemoveMonitor(FluidStack stack) {
        if (stack == null || stack.isEmpty()) return 0;


        int removable = 0;
        int min = stack.getAmount();
        for (ICell cell : getAllFluidCells()) {
            List<StorageStack> availableStacks = cell.getStorageStackFromFluid(stack);
            for (StorageStack s : availableStacks) {
                if (ModUtils.compareNbt(s.getTag(), stack.getComponents(), true)) {
                    int take = s.getCount();
                    removable += take;
                }
            }
        }
        ResourceLocation resourceLocation = BuiltInRegistries.FLUID.getKey(stack.getFluid());
        Map<SameStack, Integer> map = stacksMapCount.get(resourceLocation.toString());
        if (map == null)
            return Math.min(removable, min);
        else {
            int count = map.getOrDefault(new SameStack(stack), 0);
            return Math.min(removable - count, min);
        }
    }

    public int canRemoveExport(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;


        int removable = 0;

        for (ICell cell : getAllItemCells()) {

            List<StorageStack> availableStacks = cell.getStorageStackFromItem(stack);
            for (StorageStack s : availableStacks) {
                int take = s.getCount();
                int toCraft = cell.getStacksForCraft().getOrDefault(s.getSlot(), 0);
                removable += (take - toCraft);
            }
        }

        return removable;
    }

    public int canRemove(FluidStack stack) {
        if (stack == null || stack.isEmpty()) return 0;

        FluidStack copy = stack.copy();
        int removable = 0;

        for (ICell cell : getAllFluidCells()) {
            if (copy.isEmpty()) break;
            List<StorageStack> availableStacks = cell.getStorageStackFromFluid(copy);
            for (StorageStack s : availableStacks) {
                int reserved = cell.getStacksForCraft().getOrDefault(s.getSlot(), 0);
                int take = Math.min(s.getCount() - reserved, copy.getAmount());
                if (take > 0) {
                    removable += take;
                    copy.shrink(take);
                }
                if (copy.isEmpty()) break;
            }
        }

        return removable;
    }

    public int canRemoveExport(FluidStack stack) {
        if (stack == null || stack.isEmpty()) return 0;

        int removable = 0;

        for (ICell cell : getAllFluidCells()) {
            List<StorageStack> availableStacks = cell.getStorageStackFromFluid(stack);
            for (StorageStack s : availableStacks) {
                int take = s.getCount();
                int toCraft = cell.getStacksForCraft().getOrDefault(s.getSlot(), 0);
                removable += (take - toCraft);
            }
        }

        return removable;
    }

    public int removeStackWithIgnoring(Item request, DataComponentMap tag, int amount) {
        if (request == null || request == Items.AIR) {
            return 0;
        }

        int remaining = amount;
        int amount1 = 0;
        for (ICell cell : getAllItemCells()) {
            if (remaining <= 0) break;
            int extracted = cell.removeStackWithIgnoringAmount(request, tag, remaining);

            amount1 += extracted;
            remaining -= extracted;
        }

        return amount1;
    }

    public int removeStackWithIgnoringCrafting(Item request, DataComponentMap tag, int amount) {
        if (request == null || request == Items.AIR) {
            return 0;
        }

        int remaining = amount;
        int amount1 = 0;
        for (ICell cell : getAllItemCells()) {
            if (remaining <= 0) break;
            int extracted = cell.removeStackWithIgnoringAmountCrafting(request, tag, remaining);

            amount1 += extracted;
            remaining -= extracted;
        }

        return amount1;
    }

    public void removeStack(ItemStack request) {
        if (request == null || request.isEmpty()) {
            return;
        }

        int remaining = request.getCount();
        Item item = request.getItem();
        DataComponentMap tag = request.getComponents();
        for (ICell cell : getAllItemCells()) {
            if (remaining <= 0) break;
            int extracted = cell.removeStack(item, tag, remaining);
            remaining -= extracted;
        }

    }

    public void removeStackCrafting(ItemStack request) {
        if (request == null || request.isEmpty()) {
            return;
        }

        int remaining = request.getCount();
        Item item = request.getItem();
        DataComponentMap tag = request.getComponents();
        for (ICell cell : getAllItemCells()) {
            if (remaining <= 0) break;
            int extracted = cell.removeStackCrafting(item, tag, remaining);
            remaining -= extracted;
        }

    }

    public void removeFluidStack(FluidStack request) {
        if (request == null || request.isEmpty()) {
            return;
        }

        int remaining = request.getAmount();
        Fluid item = request.getFluid();
        DataComponentMap tag = request.getComponents();
        for (ICell cell : getAllFluidCells()) {
            if (remaining <= 0) break;
            int extracted = cell.removeFluid(item, tag, remaining);
            remaining -= extracted;
        }
    }

    public void removeFluidStackCrafting(FluidStack request) {
        if (request == null || request.isEmpty()) {
            return;
        }

        int remaining = request.getAmount();
        Fluid item = request.getFluid();
        DataComponentMap tag = request.getComponents();
        for (ICell cell : getAllFluidCells()) {
            if (remaining <= 0) break;
            int extracted = cell.removeFluidCrafting(item, tag, remaining);
            remaining -= extracted;
        }
    }

    public void reBuild(List<Path> paths, Level level) {
        this.storageDeviceCells.clear();
        this.consumeEnergy = 0;
        this.interfaces.clear();
        this.processors.clear();
        this.imports.clear();
        this.exports.clear();
        this.preCrafts.clear();
        this.indexProcessor = 0;
        monitors.clear();
        this.processorsIds.clear();
        this.patternStackInterfaceMap.clear();
        if (paths != null) {
            List<BlockPos> poses = new ArrayList<>();
            paths.forEach(path -> {
                ISink sink = path.getSink();
                BlockEntity blockEntity = level.getBlockEntity(sink.getPos());
                if (blockEntity instanceof ElectricStorage storage) {
                    poses.add(sink.getPos());
                    addElement(storage);
                }
            });
            new PacketUpdateNetworkSystem(level, poses, this.controller.getPos(), false);
        }
    }

    public void reBuild(Level level, List<BlockPos> paths) {
        this.storageDeviceCells.clear();
        this.consumeEnergy = 0;
        this.indexProcessor = 0;
        this.interfaces.clear();
        this.processors.clear();
        this.imports.clear();
        this.processorsIds.clear();
        this.exports.clear();
        this.preCrafts.clear();
        this.patternStackInterfaceMap.clear();
        monitors.clear();
        paths.forEach(path -> {
            BlockEntity blockEntity = level.getBlockEntity(path);
            if (blockEntity instanceof ElectricStorage storage)
                addElement(storage);
        });
        new PacketUpdateNetworkSystem(level, paths, this.controller.getPos(), false);
    }

    public void onUnload(Level level) {
        List<BlockPos> posList = new ArrayList<>((storageDeviceCells.size() + preCrafts.size() + interfaces.size() + this.processors.size()));
        storageDeviceCells.forEach(interf -> {
            interf.setStorageNetwork(null);
            posList.add(interf.getPos());
        });

        this.processorsIds.clear();
        this.indexProcessor = 0;
        this.storageDeviceCells.clear();
        preCrafts.forEach(interf -> {
            interf.setStorageNetwork(null);
            posList.add(interf.getPos());
        });
        this.preCrafts.clear();
        this.consumeEnergy = 0;
        interfaces.forEach(interf -> {
            interf.setStorageNetwork(null);
            posList.add(interf.getPos());
        });
        this.interfaces.clear();
        this.processors.forEach(interf -> {
            interf.setStorageNetwork(null);
            posList.add(interf.getPos());
            interf.setId(-1);

        });
        this.processors.clear();
        this.monitors.forEach(interf -> {
            interf.setStorageNetwork(null);
            posList.add(interf.getPos());
        });
        this.monitors.clear();
        this.imports.clear();
        this.exports.clear();
        this.consumeEnergy = 0;
        this.patternStackInterfaceMap.clear();
        if (!posList.isEmpty() && level != null && !level.isClientSide() && !WorldData.isStoppingOrUnloading(level) && level.getServer() != null && level.getServer().isRunning()) {
            new PacketUpdateNetworkSystem(level, posList, this.controller.getPos(), true);
        }
    }

    public List<SameStack> getFluids() {
        return fluids == null ? Collections.emptyList() : fluids;
    }

    public void setFluids(List<SameStack> stacks) {
        this.fluids = stacks;
    }

    public List<SameStack> getStacks() {
        return stacks == null ? Collections.emptyList() : stacks;
    }

    public List<SameStack> getFluidsCanCreate() {
        return fluidsCanCreate == null ? Collections.emptyList() : fluidsCanCreate;
    }

    public List<SameStack> getStacksCanCreate() {
        return stacksCanCreate == null ? Collections.emptyList() : stacksCanCreate;
    }

    public void setItems(List<SameStack> stacks) {
        this.stacks = stacks;
    }

    public void setFluidsCreate(List<SameStack> stacks) {
        this.fluidsCanCreate = stacks;
    }

    public void setItemsCreate(List<SameStack> stacks) {
        this.stacksCanCreate = stacks;
    }


    public boolean hasFreeEnergy() {
        return controller.getPower() > 0;
    }

    public void removeAutoCraft(AutoCraftSystem system) {
        Map<String, Map<SameStack, Integer>> fluidList = system.getReservedFluidSlots();
        Map<String, Map<SameStack, Integer>> itemSlots = system.getReservedItemSlots();
        if (!fluidList.isEmpty()) {
            for (ICell cell : getAllFluidCells()) {
                Iterator<Map.Entry<String, Map<SameStack, Integer>>> outerIt =
                        fluidList.entrySet().iterator();

                while (outerIt.hasNext()) {
                    Map.Entry<String, Map<SameStack, Integer>> outer = outerIt.next();
                    Map<SameStack, Integer> inner = outer.getValue();

                    Iterator<Map.Entry<SameStack, Integer>> innerIt =
                            inner.entrySet().iterator();

                    while (innerIt.hasNext()) {
                        Map.Entry<SameStack, Integer> innerEntry = innerIt.next();

                        int remove = cell.removeFluidFromListCraft(innerEntry.getKey(), innerEntry.getValue());
                        innerEntry.setValue(remove);
                        if (innerEntry.getValue() <= 0) {
                            innerIt.remove();
                        }
                    }

                    if (inner.isEmpty()) {
                        outerIt.remove();
                    }
                }
            }
        }
        if (!itemSlots.isEmpty()) {
            for (ICell cell : getAllItemCells()) {
                Iterator<Map.Entry<String, Map<SameStack, Integer>>> outerIt =
                        itemSlots.entrySet().iterator();

                while (outerIt.hasNext()) {
                    Map.Entry<String, Map<SameStack, Integer>> outer = outerIt.next();
                    Map<SameStack, Integer> inner = outer.getValue();

                    Iterator<Map.Entry<SameStack, Integer>> innerIt =
                            inner.entrySet().iterator();

                    while (innerIt.hasNext()) {
                        Map.Entry<SameStack, Integer> innerEntry = innerIt.next();

                        int remove = cell.removeItemFromListCraft(innerEntry.getKey(), innerEntry.getValue());
                        innerEntry.setValue(remove);
                        if (innerEntry.getValue() <= 0) {
                            innerIt.remove();
                        }
                    }

                    if (inner.isEmpty()) {
                        outerIt.remove();
                    }
                }
            }
        }
    }

    public double getConsumeEnergy() {
        return consumeEnergy;
    }
}
