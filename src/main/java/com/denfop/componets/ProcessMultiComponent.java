package com.denfop.componets;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.IMultiUpdateTick;
import com.denfop.api.recipe.InvSlotMultiRecipes;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.base.TileMultiMachine;
import com.denfop.tiles.mechanism.EnumTypeMachines;
import com.denfop.tiles.mechanism.multimechanism.IMultiMachine;
import com.denfop.utils.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class ProcessMultiComponent extends AbstractComponent implements IMultiUpdateTick {

    public final InvSlotOutput outputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final Energy energy;
    public final InvSlotMultiRecipes inputSlots;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    final short[] progress;
    final double[] guiProgress;
    final IMultiMachine multimachine;
    private final int sizeWorkingSlot;
    private final int defaultTier;
    private final double defaultEnergyStorage;
    private final EnumMultiMachine enumMultiMachine;
    private final boolean random;
    private final int min;
    private final int max;
    private final CoolComponent cold;
    private final ComponentBaseEnergy exp;
    private final boolean isCentrifuge;
    private final HeatComponent heat;
    public double energyConsume;
    public int operationLength;
    public boolean quickly;
    public int module;
    public int[] col = new int[8];
    public MachineRecipe[] output;
    public boolean modulesize = false;
    public boolean module_infinity_water = false;
    public boolean module_separate = false;
    public boolean modulestorage = false;
    private int mode;
    private int operationsPerTick = 1;
    private int operationChange;
    private Timer timer = null;
    private Timer timer1 = new Timer(0, 0, 0);

    public ProcessMultiComponent(
            final IMultiMachine parent, EnumMultiMachine enumMultiMachine
    ) {
        super((TileEntityBlock) parent);
        this.multimachine = parent;
        this.inputSlots = new InvSlotMultiRecipes(
                (TileEntityInventory) parent,
                enumMultiMachine.type.recipe,
                this,
                enumMultiMachine.sizeWorkingSlot, this
        );
        this.outputSlot = new InvSlotOutput(
                (IAdvInventory<?>) parent,
                enumMultiMachine.sizeWorkingSlot + (enumMultiMachine.output ? 2 : 0)
        );
        this.upgradeSlot = new InvSlotUpgrade((TileEntityInventory) parent, 4);
        this.energy = ((TileEntityInventory)parent).getComp(Energy.class);
        this.enumMultiMachine = enumMultiMachine;
        this.sizeWorkingSlot = enumMultiMachine.sizeWorkingSlot;
        this.progress = new short[sizeWorkingSlot];
        this.guiProgress = new double[sizeWorkingSlot];
        double coefenergy = getcoef();
        double speed = getspeed();
        this.mode = 0;
        this.defaultEnergyConsume = this.energyConsume = Math.max((int) (enumMultiMachine.usagePerTick * coefenergy), 1);
        this.defaultOperationLength = this.operationChange = this.operationLength =
                Math.max((int) (enumMultiMachine.lenghtOperation * 1D / speed), 1);
        this.defaultTier = this.energy.getSinkTier();
        this.defaultEnergyStorage = this.defaultEnergyConsume * this.defaultOperationLength;
        this.quickly = false;
        this.module = 0;
        this.min = enumMultiMachine.getMin();
        this.max = enumMultiMachine.getMax();
        this.random = enumMultiMachine.type == EnumTypeMachines.RECYCLER;
        this.output = new MachineRecipe[sizeWorkingSlot];
        this.cold =  ((TileEntityInventory)parent).getComp(CoolComponent.class);
        this.exp =  ((TileEntityInventory)parent).getComp(ComponentBaseEnergy.class);
        this.heat =  ((TileEntityInventory)parent).getComp(HeatComponent.class);
        this.isCentrifuge = enumMultiMachine.type == EnumTypeMachines.Centrifuge;
    }

    public MachineRecipe getRecipeOutput(int slotId) {
        return this.output[slotId];
    }

    public void setRecipeOutput(MachineRecipe output, int slotId) {
        this.output[slotId] = output;
    }

    public InvSlotUpgrade getUpgradeSlot() {
        return upgradeSlot;
    }

    public int getSizeWorkingSlot() {
        return sizeWorkingSlot;
    }

    @Override
    public void onUpdate() {

    }

    public MachineRecipe getRecipeOutput() {
        return this.output[0];
    }

    public void setRecipeOutput(MachineRecipe output) {
        this.output[0] = output;
    }


    private void getsOutputs() {
        for (int i = 0; i < this.sizeWorkingSlot; i++) {
            this.output[i] = this.getOutput(i);
        }
    }

    public void cycleMode() {
        setMode((this.mode + 1) % 3);
    }

    public void operate(int slotId, MachineRecipe output, int size) {
        if (output.getRecipe().output.items.size() < 2) {
            if (size > 1) {
                int maxSize;
                int maxSize1;
                boolean recycler = this.enumMultiMachine.recipe.equals("recycler");
                if (!recycler) {
                    maxSize = this.inputSlots.get(slotId).getCount() / output.getList().get(0);
                } else {
                    maxSize = this.inputSlots.get(slotId).getCount();
                }
                maxSize1 = output.getRecipe().output.items.get(0).getMaxStackSize();

                ItemStack outputStack = output.getRecipe().output.items.get(0);

                if (recycler) {
                    outputStack = this.inputSlots.recycler_output.getRecipe().output.items.get(0);
                }
                int count = 0;
                for (int i = 0; i < outputSlot.size(); i++) {
                    final ItemStack output_stack = this.outputSlot.get(i);
                    count += output_stack.isEmpty() ? maxSize1 : output_stack.isItemEqual(outputStack) ?
                            output_stack.getMaxStackSize() - output_stack.getCount() : 0;
                }
                size = Math.min(size, count / outputStack.getCount());
                if (this.multimachine.getTank() != null) {
                    size = Math.min(size, this.multimachine.getTank().getFluidAmount() / 1000);
                    this.multimachine.getTank().drain(1000 * size, true);
                }
                if (maxSize == size && (recycler || size * output.getRecipe().input
                        .getInputs()
                        .get(0)
                        .getAmount() == this.inputSlots.get(slotId).getCount())) {
                    this.output[slotId] = null;
                }
                this.multimachine.consume(size);
                if (random) {
                    this.inputSlots.consume(slotId, size, 1);
                    size = (int) ((min * 1D / max) * size);
                    this.outputSlot.add(outputStack, size);
                } else {
                    this.inputSlots.consume(slotId, size, output.getList().get(0));
                    this.outputSlot.add(outputStack, size);
                }
            } else {
                int maxSize1;
                boolean recycler = this.enumMultiMachine.recipe.equals("recycler");
                if (!recycler) {
                    maxSize1 = output.getRecipe().output.items.get(0).getMaxStackSize();
                } else {
                    maxSize1 = 64;
                }

                if (!recycler) {
                    size = this.inputSlots.get(slotId).getCount() / output.getList().get(0);
                } else {
                    size = this.inputSlots.get(slotId).getCount();
                }
                int maxSize = size;
                size = Math.min(size, this.operationsPerTick);
                size = this.multimachine.getSize(size);
                ItemStack outputStack = output.getRecipe().output.items.get(0);
                if (recycler) {
                    outputStack = this.inputSlots.recycler_output.getRecipe().output.items.get(0);
                }
                int count = 0;
                for (int i = 0; i < sizeWorkingSlot; i++) {
                    final ItemStack output_stack = this.outputSlot.get(i);
                    count += output_stack.isEmpty() ? maxSize1 : output_stack.isItemEqual(outputStack) ?
                            output_stack.getMaxStackSize() - output_stack.getCount() : 0;
                }
                size = Math.min(size, count / outputStack.getCount());
                if (this.multimachine.getTank() != null) {
                    size = Math.min(size, this.multimachine.getTank().getFluidAmount() / 1000);
                    this.multimachine.getTank().drain(1000 * size, true);
                }
                this.multimachine.consume(size);
                if (maxSize == size && (recycler ||
                        size * output.getRecipe().input.getInputs().get(0).getAmount() == this.inputSlots
                                .get(slotId)
                                .getCount())) {
                    this.output[slotId] = null;
                }

                if (!this.random) {
                    this.inputSlots.consume(slotId, size, output.getList().get(0));
                    this.outputSlot.add(outputStack, size);
                } else {
                    Random rand = this.getParent().getWorld().rand;
                    for (int i = 0; i < size; i++) {
                        this.inputSlots.consume(slotId);
                        if (rand.nextInt(max + 1) <= min) {
                            this.outputSlot.add(outputStack);
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < Math.max(size, this.operationsPerTick); i++) {

                operateOnce(slotId, output.getRecipe().output.items, size);
                if (this.multimachine.getTank() != null) {
                    this.multimachine.getTank().drain(1000, true);
                }
                if (!this.enumMultiMachine.recipe.equals("recycler")) {
                    if (this.multimachine.getTank() != null) {
                        if (this.multimachine.getTank().getFluid() == null || this.multimachine.getTank().getFluid().amount < 1000) {
                            break;
                        }
                    }
                    if (this.inputSlots.get(slotId).isEmpty() || this.inputSlots
                            .get(slotId)
                            .getCount() < this.output[slotId].getRecipe().input
                            .getInputs()
                            .get(0).getAmount()
                            || !this.outputSlot.canAdd(output.getRecipe().output.items)) {
                        this.getOutput(slotId);
                        break;
                    } else {
                        if (this.inputSlots.get(slotId).isEmpty() || this.inputSlots
                                .get(slotId)
                                .getCount() < 1 || !this.outputSlot.canAdd(output.getRecipe().output.items)) {
                            this.getOutput(slotId);
                            break;
                        }
                    }
                }

            }
        }
    }

    public CoolComponent getCold() {
        return cold;
    }

    @Override
    public boolean isServer() {
        return true;
    }

    public void operateOnce(int slotId, List<ItemStack> processResult, int size) {

        for (int i = 0; i < size; i++) {
            if (!random) {
                boolean can = this.outputSlot.addWithoutIgnoring(processResult, true);
                if (!can) {
                    break;
                }
                this.inputSlots.consume(slotId);


                for (ItemStack stack : processResult) {
                    this.outputSlot.add(stack);
                }
                if (this.enumMultiMachine.type == EnumTypeMachines.ELECTRICFURNACE) {
                    this.exp.addEnergy(this.getRecipeOutput(slotId).getRecipe().output.metadata.getFloat("experience"));
                }
            } else {
                Random rand = this.getParent().getWorld().rand;
                this.inputSlots.consume(slotId);
                if (rand.nextInt(max + 1) <= min) {
                    this.outputSlot.add(processResult);
                }
            }

        }
        if (!(this.timer != null && this.timer.canWork())) {
            this.cold.addEnergy(0.15 * (this.isCentrifuge ? 2.5 : 1));
        }

    }

    public MachineRecipe getOutput(int slotId) {
        if (enumMultiMachine == null || (
                enumMultiMachine.type != EnumTypeMachines.COMBRECYCLER && enumMultiMachine.type != EnumTypeMachines.RECYCLER)) {
            if (this.inputSlots.isEmpty(slotId)) {
                this.output[slotId] = null;
                return null;
            }
            this.output[slotId] = this.inputSlots.process(slotId);
            return this.output[slotId];
        } else {
            if (this.inputSlots.isEmpty(slotId)) {
                this.output[slotId] = null;
                return null;
            }
            this.output[slotId] = this.inputSlots.recycler_output;
            return this.output[slotId];
        }
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        if (parent.getWorld() == null){
            tooltip.add(Localization.translate("iu.speed_canister.info"));
        }
    }

    @Override
    public boolean onBlockActivated(final EntityPlayer player, final EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem().equals(IUItem.canister)) {
            FluidStack fluid = FluidUtil.getFluidContained(stack);
            if (fluid != null && fluid.amount >= 250 && !timer1.canWork() && (timer == null || !timer.canWork())) {
                this.timer = new Timer(0, 0, 15);
                final IFluidHandlerItem handler = FluidUtil.getFluidHandler(stack);
                handler.drain(250, true);
                return true;
            }
        }
        return super.onBlockActivated(player, hand);
    }

    @Override
    public void updateEntityServer() {

        int quickly = 1;
        if (this.parent.getWorld().provider.getWorldTime() % 20 == 0) {
            if (this.module_separate && !this.inputSlots.isEmpty()) {
                if (sizeWorkingSlot > 1) {

                    for (int i = 1; i < sizeWorkingSlot; i++) {
                        ItemStack stack = this.inputSlots.get(0);
                        if (!stack.isEmpty()) {
                            if (stack.getCount() == stack.getMaxStackSize()) {
                                break;
                            }
                            ItemStack stack1 = this.inputSlots.get(i);
                            if (stack.isItemEqual(stack1)) {
                                int min = stack.getMaxStackSize() - stack.getCount();
                                min = Math.min(min, stack1.getCount());
                                if (stack1.getCount() == min) {
                                    stack.grow(min);
                                    this.inputSlots.put(i, ItemStack.EMPTY);
                                } else {
                                    stack.grow(min);
                                    stack1.shrink(min);
                                }
                            }
                        } else {
                            ItemStack stack1 = this.inputSlots.get(i);
                            this.inputSlots.put(0, stack1);
                            this.inputSlots.put(i, ItemStack.EMPTY);
                        }
                    }
                }
            }
        }

        if (this.parent.getWorld().provider.getWorldTime() % 10 == 0) {
            if (this.modulestorage && !this.inputSlots.isEmpty()) {
                final ItemStack stack = this.inputSlots.get();
                int size = 0;
                int col = 0;
                for (int i = 0; i < sizeWorkingSlot; i++) {
                    ItemStack stack1 = this.inputSlots.get(i);

                    if (stack1.isItemEqual(stack)) {
                        size += stack1.getCount();
                    }

                    if (stack1.isItemEqual(stack) || stack1.isEmpty()) {
                        col++;
                    }
                }
                int count = size / col;
                int count1 = size - (count * col);
                for (int i = 0; i < sizeWorkingSlot; i++) {
                    ItemStack stack1 = this.inputSlots.get(i);
                    if ((stack1.isItemEqual(stack)) || stack1.isEmpty()) {
                        ItemStack stack2 = stack.copy();
                        int dop = 0;
                        int prom = 64 - count;
                        if (prom > 0) {
                            if (count1 > prom) {
                                dop += prom;
                                count1 -= prom;
                            } else {
                                dop += count1;
                                count1 = 0;
                            }

                        }

                        stack2.setCount(count + dop);
                        this.inputSlots.put(i, stack2);

                    }

                }


            }
        }
        boolean active = false;
        int type = -1;

        for (int i = 0; i < sizeWorkingSlot; i++) {
            MachineRecipe output = this.output[i];

            if (this.quickly) {
                quickly = 100;
            }

            int size = 1;
            if (this.output[i] != null && !this.inputSlots.get(i).isEmpty()) {
                if (this.module_infinity_water) {
                    if (this.multimachine.getTank() != null && this.multimachine.getTank().getFluidAmount() < 32000) {
                        this.multimachine.getTank().fill(new FluidStack(FluidRegistry.WATER, 64000), true);
                    }
                }
                if (this.multimachine.getTank() != null) {
                    if (this.multimachine.getTank().getFluid() == null || this.multimachine.getTank().getFluid().amount < 1000) {
                        return;
                    }
                }
                if (this.multimachine.getHeat() != null) {
                    if (output.getRecipe().output.metadata.getShort("minHeat") == 0 || output.getRecipe().output.metadata.getShort(
                            "minHeat") > this.heat.getEnergy()) {
                        if (!(this).heat.need) {
                            (this).heat.need = true;
                        }
                        return;

                    } else if ((this).heat.need) {
                        (this).heat.need = false;
                    }
                }
                if (output != null && this.modulesize) {
                    if (!this.enumMultiMachine.recipe.equals("recycler")) {
                        size = this.inputSlots.get(i).getCount() / this.output[i].getList().get(0);
                    } else {
                        size = this.inputSlots.get(i).getCount();
                    }
                }
            }
            if (this.cold.upgrade) {
                if (this.cold.storage > 0) {
                    this.cold.storage = 0;
                }

            }
            size = this.multimachine.getSize(size);
            if (output != null && this.inputSlots.continue_proccess(
                    this.outputSlot,
                    i
            ) && (this.energy.canUseEnergy(this.energyConsume * quickly * size)) && this.multimachine.canoperate(size)) {
                active = true;
                if (this.progress[i] == 0) {
                    if (this.operationLength > this.defaultOperationLength * 0.1) {
                        if (type == -1) {
                            this.multimachine.initiate(0);
                            type = 0;
                        }
                    }
                    col[i] = this.inputSlots.get(i).getCount();
                }
                if (!this.isCentrifuge && !(this.timer != null && this.timer.canWork())) {
                    this.cold.addEnergy(0.05 * 1);
                }

                if (this.inputSlots.get(i).getCount() != col[i] && this.modulesize) {
                    this.progress[i] = (short) (col[i] * this.progress[i] / this.inputSlots.get(i).getCount());
                    col[i] = this.inputSlots.get(i).getCount();
                }
                this.energy.useEnergy(this.energyConsume * quickly * size);
                this.progress[i]++;
                if (timer != null && timer.canWork()) {
                    this.progress[i]++;
                    progress[i] = (short) Math.min(this.progress[i], operationLength);

                }
                this.guiProgress[i] = (double) this.progress[i] / this.operationLength;

                if (this.progress[i] >= this.operationLength || (this.quickly && this.operationLength < Integer.MAX_VALUE)) {
                    this.guiProgress[i] = 0;
                    this.progress[i] = 0;
                    if (this.enumMultiMachine.type == EnumTypeMachines.ELECTRICFURNACE) {


                        int exp = this.getParent().getWorld().rand.nextInt(3) + 1;
                        this.exp.addEnergy(exp);
                    }

                    operate(i, output, size);
                    if (this.operationLength > this.defaultOperationLength * 0.1 || (this.multimachine.getType() != EnumTypeAudio.values()[2 % EnumTypeAudio.values().length])) {
                        if (type == -1) {
                            this.multimachine.initiate(2);
                            type = 2;
                        }
                    }
                }
            } else {
                if (this.progress[i] != 0 && this.parent.getActive()) {
                    if (this.operationLength > this.defaultOperationLength * 0.1 || (this.multimachine.getType() != EnumTypeAudio.values()[1 % EnumTypeAudio.values().length])) {
                        if (type == -1) {
                            this.multimachine.initiate(1);
                            type = 1;
                        }
                    }
                }
                if (output == null) {
                    this.progress[i] = 0;
                }

            }

        }
        if (this.multimachine.getHeat() != null) {
            this.multimachine.getHeat().useEnergy(1);
        }
        if (this.parent.getActive() != active) {
            this.parent.setActive(active);
        }

        final double fillratio = this.cold.getFillRatio();
        operationLength = this.upgradeSlot.getOperationLength1(this.defaultOperationLength);
        if (fillratio >= 0.75 && fillratio < 1) {
            this.operationLength = this.operationLength * 2;
        }
        if (fillratio >= 1) {
            this.operationLength = Integer.MAX_VALUE;
        }
        if (fillratio >= 0.5 && fillratio < 0.75) {
            this.operationLength = (int) (this.operationLength * 1.5);
        }
        if (this.parent.getWorld().provider.getWorldTime() % 20 == 0) {
            if (this.timer != null && this.timer.canWork()) {
                this.timer.work();
                if (!this.timer.canWork()) {
                    timer1 = new Timer(0, 0, 10);
                }
            }
            if (timer1.canWork()) {
                timer1.work();
            }
        }
    }

    public void readFromNbt(NBTTagCompound nbttagcompound) {
        for (int i = 0; i < sizeWorkingSlot; i++) {
            this.progress[i] = nbttagcompound.getShort("progress" + i);
        }
        this.quickly = nbttagcompound.getBoolean("quickly");
        this.modulesize = nbttagcompound.getBoolean("modulesize");
        this.modulestorage = nbttagcompound.getBoolean("modulestorage");
        this.module_infinity_water = nbttagcompound.getBoolean("module_infinity_water");
        this.module_separate = nbttagcompound.getBoolean("module_separate");

        this.module = nbttagcompound.getInteger("module");
        this.mode = nbttagcompound.getInteger("mode");
    }

    public NBTTagCompound writeToNbt() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        for (int i = 0; i < sizeWorkingSlot; i++) {
            nbttagcompound.setShort("progress" + i, progress[i]);
        }
        nbttagcompound.setInteger("module", module);
        nbttagcompound.setInteger("mode", mode);
        nbttagcompound.setBoolean("quickly", this.quickly);
        nbttagcompound.setBoolean("modulesize", this.modulesize);
        nbttagcompound.setBoolean("modulestorage", this.modulestorage);
        nbttagcompound.setBoolean("module_infinity_water", this.module_infinity_water);
        nbttagcompound.setBoolean("module_separate", this.module_separate);
        return nbttagcompound;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            this.setOverclockRates();
            inputSlots.load();
            this.getsOutputs();
        }
    }

    public void setOverclockRates() {


        this.operationsPerTick = this.upgradeSlot.getOperationsPerTick1(this.defaultOperationLength);
        this.operationLength = this.upgradeSlot.getOperationLength1(this.defaultOperationLength);

        final double fillratio = this.cold.getFillRatio();
        if (fillratio >= 0.75 && fillratio < 1) {
            this.operationLength *= 2;
        }
        if (fillratio >= 1) {
            this.operationLength = Integer.MAX_VALUE;
        }
        if (fillratio >= 0.5 && fillratio < 0.75) {
            this.operationLength *= 1.5;
        }
        this.operationChange = this.operationLength;
        this.energyConsume = this.upgradeSlot.getEnergyDemand(this.defaultEnergyConsume);
        int tier = this.upgradeSlot.getTier(this.defaultTier);
        this.energy.setSinkTier(tier);
        ((TileMultiMachine) parent).dischargeSlot.setTier(tier);
        this.energy.setCapacity(this.upgradeSlot.getEnergyStorage(
                this.defaultEnergyStorage
        ));

    }

    public double getProgress(int slotId) {
        return this.progress[slotId] * 1D / this.operationLength;
    }

    @Override
    public void onContainerUpdate(final EntityPlayerMP player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
        buffer.writeInt(this.operationLength);
        for (int i = 0; i < sizeWorkingSlot; i++) {
            buffer.writeInt(this.progress[i]);
        }
        buffer.writeDouble(this.energyConsume);
        buffer.writeInt(this.mode);
        buffer.writeBoolean(this.quickly);
        buffer.writeBoolean(this.modulesize);
        buffer.writeBoolean(this.modulestorage);
        buffer.writeBoolean(this.module_infinity_water);
        buffer.writeBoolean(this.module_separate);

        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    @Override
    public CustomPacketBuffer updateComponent() {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
        buffer.writeInt(this.operationLength);
        for (int i = 0; i < sizeWorkingSlot; i++) {
            buffer.writeInt(this.progress[i]);
        }
        buffer.writeDouble(this.energyConsume);
        buffer.writeInt(this.mode);
        buffer.writeBoolean(this.quickly);
        buffer.writeBoolean(this.modulesize);
        buffer.writeBoolean(this.modulestorage);
        buffer.writeBoolean(this.module_infinity_water);
        buffer.writeBoolean(this.module_separate);
        return buffer;
    }

    @Override
    public void onNetworkUpdate(final CustomPacketBuffer is) throws IOException {
        super.onNetworkUpdate(is);
        this.operationLength = is.readInt();
        for (int i = 0; i < sizeWorkingSlot; i++) {
            this.progress[i] = (short) is.readInt();
        }
        this.energyConsume = is.readDouble();
        this.mode = is.readInt();
        this.quickly = is.readBoolean();
        this.modulesize = is.readBoolean();
        this.modulestorage = is.readBoolean();
        this.module_infinity_water = is.readBoolean();
        this.module_separate = is.readBoolean();
    }

    private double getspeed() {
        switch (this.sizeWorkingSlot) {
            case 2:
                return 2.2;
            case 3:
                return 3.4;
            case 4:
                return 4.5;
            case 8:
                return 6;
            default:
                return 1;
        }
    }

    private double getcoef() {
        switch (this.sizeWorkingSlot) {
            case 3:
                return 0.9;
            case 4:
                return 0.8;
            case 8:
                return 0.6;
            default:
                return 1;
        }
    }

    public void setModule(final int module) {
        this.module = module;
    }

    public void shrinkModule(final int module) {
        this.module -= module;
    }

    public void setModulesize(final boolean modulesize) {
        this.modulesize = modulesize;
    }

    public void setQuickly(final boolean quickly) {
        this.quickly = quickly;
    }

    public void setModulestorage(final boolean modulestorage) {
        this.modulestorage = modulestorage;
    }

    public boolean onActivated(ItemStack heldItem) {
        if (heldItem.getItem().equals(IUItem.module_quickly)) {
            if (!this.quickly && this.module < 2) {
                this.quickly = true;
                this.module++;
                heldItem.shrink(1);
                return true;
            }
        }
        if (heldItem.getItem().equals(IUItem.module_stack)) {
            if (!this.modulesize && this.module < 2) {
                this.modulesize = true;
                this.module++;
                heldItem.shrink(1);
                return true;
            }
        }
        if (this.multimachine.getTank() != null && heldItem.getItem().equals(IUItem.module_infinity_water)) {
            if (!this.module_infinity_water && this.module < 2) {
                this.module_infinity_water = true;
                this.module++;
                heldItem.shrink(1);
                return true;
            }
        }
        if (heldItem.getItem().equals(IUItem.module_storage)) {
            if (!this.modulestorage && this.module < 2) {
                this.modulestorage = true;
                this.module++;
                heldItem.shrink(1);
                return true;
            }
        }
        if (this.enumMultiMachine.type == EnumTypeMachines.COMPRESSOR && heldItem.getItem().equals(IUItem.module_separate)) {
            if (!this.module_separate && this.module < 2) {
                this.module_separate = true;
                this.module++;
                heldItem.shrink(1);
                return true;
            }
        }
        return false;
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode1) {
        if (this.enumMultiMachine.type == EnumTypeMachines.METALFOMER) {
            final InvSlotMultiRecipes slot = this.inputSlots;
            switch (mode1) {
                case 0:
                    slot.setNameRecipe("extruding");
                    break;
                case 1:
                    slot.setNameRecipe("rolling");
                    break;
                case 2:
                    slot.setNameRecipe("cutting");
                    break;
                default:
                    throw new RuntimeException("invalid mode: " + mode1);
            }
            this.mode = mode1;
            for (int i = 0; i < this.getSizeWorkingSlot(); i++) {
                this.setRecipeOutput(this.inputSlots.process(i), i);
            }
        }
    }

}
