package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.api.recipe.IMultiUpdateTick;
import com.denfop.api.recipe.InvSlotMultiRecipes;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import com.denfop.tiles.mechanism.EnumTypeMachines;
import ic2.core.IC2;
import ic2.core.network.GrowingBuffer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInput;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class ProcessMultiComponent extends AbstractComponent implements IMultiUpdateTick {

    public final InvSlotOutput outputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final AdvEnergy energy;
    public final InvSlotMultiRecipes inputSlots;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    private final int sizeWorkingSlot;
    private final short[] progress;
    private final double[] guiProgress;
    private final TileEntityMultiMachine multimachine;
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
    public int[] col = new int[4];
    public MachineRecipe[] output;
    public boolean modulesize = false;
    public boolean modulestorage = false;
    private int mode;
    private int operationsPerTick = 1;
    private int operationChange;

    public ProcessMultiComponent(
            final TileEntityMultiMachine parent, EnumMultiMachine enumMultiMachine
    ) {
        super(parent);
        this.multimachine = parent;
        this.inputSlots = new InvSlotMultiRecipes(
                parent,
                enumMultiMachine.type.recipe,
                this,
                enumMultiMachine.sizeWorkingSlot, this
        );
        this.outputSlot = new InvSlotOutput(parent, "output",
                enumMultiMachine.sizeWorkingSlot + (enumMultiMachine.output ? 2 : 0)
        );
        this.upgradeSlot = new InvSlotUpgrade(parent, "upgrade", 4);
        this.energy = parent.getComp(AdvEnergy.class);
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
        this.cold = parent.getComp(CoolComponent.class);
        this.exp = parent.getComp(ComponentBaseEnergy.class);
        this.heat = parent.getComp(HeatComponent.class);
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
        for (int i = 0; i < this.operationsPerTick; i++) {

            operateOnce(slotId, output.getRecipe().output.items, size);
            if (this.multimachine.tank != null) {
                this.multimachine.tank.drain(1000, true);
            }
            if (!this.enumMultiMachine.recipe.equals("recycler")) {
                if (this.multimachine.tank != null) {
                    if (this.multimachine.tank.getFluid() == null || this.multimachine.tank.getFluid().amount < 1000) {
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
        this.cold.addEnergy(0.15 * (this.isCentrifuge ? 2.5 : 1));

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
            if (this.outputSlot.canAdd(output[slotId].getRecipe().output.items)) {
                return output[slotId];
            }
            return null;
        }
    }


    @Override
    public void updateEntityServer() {

        int quickly = 1;

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
                if (this.multimachine.tank != null) {
                    if (this.multimachine.tank.getFluid() == null || this.multimachine.tank.getFluid().amount < 1000) {
                        return;
                    }
                }
                if (this.multimachine.heat != null) {
                    if (output.getRecipe().output.metadata.getShort("minHeat") == 0 || output.getRecipe().output.metadata.getShort(
                            "minHeat") > this.heat.getEnergy()) {
                        if (!(this).heat.need) {
                            (this).heat.need = true;
                        }
                        return;

                    } else if ((this).heat.need) {
                        (this).heat.need = false;
                    }
                    (this).heat.storage--;
                }
                if (output != null && this.modulesize) {
                    size = this.output[i].getRecipe().input.getInputs().get(0).getAmount();
                    size = (int) Math.floor((float) this.inputSlots.get(i).getCount() / size);
                    int size1 = 0;

                    for (int ii = 0; ii < sizeWorkingSlot; ii++) {
                        if (!this.outputSlot.get(ii).isEmpty()) {
                            size1 += (64 - this.outputSlot.get(ii).getCount());
                        } else {
                            size1 += 64;
                        }
                    }
                    size1 = size1 / output.getRecipe().output.items.get(0).getCount();
                    size = Math.min(size1, size);
                }
            }
            if (output != null && this.inputSlots.continue_proccess(
                    this.outputSlot,
                    i
            ) && (this.energy.canUseEnergy(this.energyConsume * quickly * size))) {
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
                this.cold.addEnergy(0.05 * (this.isCentrifuge ? 2.5 : 1));

                if (this.inputSlots.get(i).getCount() != col[i] && this.modulesize) {
                    this.progress[i] = (short) (col[i] * this.progress[i] / this.inputSlots.get(i).getCount());
                    col[i] = this.inputSlots.get(i).getCount();
                }
                if (this.energy.getEnergy() >= this.energyConsume * quickly * size) {
                    this.energy.useEnergy(this.energyConsume * quickly * size);
                } else {
                    return;
                }
                this.progress[i]++;

                this.guiProgress[i] = (double) this.progress[i] / this.operationLength;

                if (this.progress[i] >= this.operationLength || this.quickly) {
                    this.guiProgress[i] = 0;
                    this.progress[i] = 0;
                    if (this.enumMultiMachine.type == EnumTypeMachines.ELECTRICFURNACE) {


                        int exp = this.getParent().getWorld().rand.nextInt(3) + 1;
                        this.exp.addEnergy(exp);
                    }

                    operate(i, output, size);
                    if (this.operationLength > this.defaultOperationLength * 0.1 || (this.multimachine.getType() != this.multimachine.valuesAudio[2 % this.multimachine.valuesAudio.length])) {
                        if (type == -1) {
                            this.multimachine.initiate(2);
                            type = 2;
                        }
                    }
                }
            } else {
                if (this.progress[i] != 0 && this.parent.getActive()) {
                    if (this.operationLength > this.defaultOperationLength * 0.1 || (this.multimachine.getType() != this.multimachine.valuesAudio[1 % this.multimachine.valuesAudio.length])) {
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

        if (this.multimachine.heat != null) {
            this.multimachine.heat.useEnergy(1);
        }

    }

    public void readFromNbt(NBTTagCompound nbttagcompound) {
        for (int i = 0; i < sizeWorkingSlot; i++) {
            this.progress[i] = nbttagcompound.getShort("progress" + i);
        }
        this.quickly = nbttagcompound.getBoolean("quickly");
        this.modulesize = nbttagcompound.getBoolean("modulesize");
        this.modulestorage = nbttagcompound.getBoolean("modulestorage");
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
        return nbttagcompound;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
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
        ((TileEntityMultiMachine) parent).dischargeSlot.setTier(tier);
        this.energy.setCapacity(this.upgradeSlot.getEnergyStorage(
                this.defaultEnergyStorage
        ));

    }

    public double getProgress(int slotId) {
        return this.progress[slotId] * 1D / this.operationLength;
    }

    @Override
    public void onContainerUpdate(final EntityPlayerMP player) {
        GrowingBuffer buffer = new GrowingBuffer(16);
        buffer.writeInt(this.operationLength);
        for (int i = 0; i < sizeWorkingSlot; i++) {
            buffer.writeInt(this.progress[i]);
        }
        buffer.writeDouble(this.energyConsume);
        buffer.writeInt(this.mode);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    @Override
    public void onNetworkUpdate(final DataInput is) throws IOException {
        super.onNetworkUpdate(is);
        this.operationLength = is.readInt();
        for (int i = 0; i < sizeWorkingSlot; i++) {
            this.progress[i] = (short) is.readInt();
        }
        this.energyConsume = is.readDouble();
        this.mode = is.readInt();
    }

    private double getspeed() {
        switch (this.sizeWorkingSlot) {
            case 2:
                return 2.2;
            case 3:
                return 3.4;
            case 4:
                return 4.5;
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
        if (heldItem.getItem().equals(IUItem.module_storage)) {
            if (!this.modulestorage && this.module < 2) {
                this.modulestorage = true;
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
