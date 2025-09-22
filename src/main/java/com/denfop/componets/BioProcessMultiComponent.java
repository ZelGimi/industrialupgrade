package com.denfop.componets;

import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.recipe.InventoryBioMultiRecipes;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.IBioMachine;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.EnumTypeMachines;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.io.IOException;
import java.util.List;

public class BioProcessMultiComponent extends AbstractComponent implements IMultiUpdateTick {

    public final InventoryBioMultiRecipes inputSlots;
    public final InventoryOutput outputSlot;
    public final HeatComponent heat;
    public final boolean isCentrifuge;
    private final ComponentBioFuelEnergy bioFuel;
    private final IBioMachine multimachine;
    private final EnumMultiMachine enumMultiMachine;
    private final int sizeWorkingSlot;
    private final short[] progress;
    private final double[] guiProgress;
    private final double defaultEnergyConsume;
    private final int defaultOperationLength;
    private final int operationChange;
    private final MachineRecipe[] output;
    public double energyConsume;
    public int operationLength;
    private int mode;
    private int[] col;

    public BioProcessMultiComponent(final IBioMachine parent, final EnumMultiMachine enumMultiMachine) {
        super((TileEntityBlock) parent);
        this.multimachine = parent;
        this.inputSlots = new InventoryBioMultiRecipes(
                (TileEntityInventory) parent,
                enumMultiMachine.type.recipe,
                this,
                enumMultiMachine.sizeWorkingSlot, this
        );
        this.outputSlot = new InventoryOutput(
                (IAdvInventory<?>) parent,
                enumMultiMachine.sizeWorkingSlot + (enumMultiMachine.output ? 2 : 0)
        );
        this.enumMultiMachine = enumMultiMachine;
        this.sizeWorkingSlot = enumMultiMachine.sizeWorkingSlot;
        this.progress = new short[sizeWorkingSlot];
        this.guiProgress = new double[sizeWorkingSlot];
        double coefenergy = 1;
        double speed = 1;
        this.mode = 0;
        this.defaultEnergyConsume = this.energyConsume = Math.max((int) (enumMultiMachine.usagePerTick * coefenergy), 1) / 2D;
        this.defaultOperationLength = this.operationChange = this.operationLength =
                Math.max((int) (enumMultiMachine.lenghtOperation * 1D / speed), 1);
        this.output = new MachineRecipe[sizeWorkingSlot];
        this.bioFuel = ((TileEntityBlock) parent).getComp(ComponentBioFuelEnergy.class);
        this.heat = ((TileEntityBlock) parent).getComp(HeatComponent.class);
        this.isCentrifuge = enumMultiMachine.type == EnumTypeMachines.Centrifuge;

    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.parent.getWorld().isRemote) {
            inputSlots.load();
            this.getsOutputs();
        }
    }

    @Override
    public boolean onBlockActivated(final EntityPlayer player, final EnumHand hand) {
        return false;
    }

    public void setOverclockRates() {

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
    }

    @Override
    public void updateEntityServer() {


        boolean active = false;
        int type = -1;

        for (int i = 0; i < getSizeWorkingSlot(); i++) {
            MachineRecipe output = this.output[i];
            int size = 1;

            if (output != null && this.inputSlots.continue_proccess(
                    this.outputSlot,
                    i
            ) && (this.bioFuel.canUseEnergy(energyConsume))) {
                active = true;
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
                if (this.progress[i] == 0) {
                    if (this.operationLength > this.defaultOperationLength * 0.1) {
                        if (type == -1) {
                            this.multimachine.initiate(0);
                            type = 0;
                        }
                    }
                }
                this.bioFuel.useEnergy(energyConsume);
                this.progress[i]++;
                this.guiProgress[i] = (double) this.progress[i] / this.operationLength;

                if (this.progress[i] >= this.operationLength) {
                    this.guiProgress[i] = 0;
                    this.progress[i] = 0;
                    operate(i, output);
                    if (this.operationLength > this.defaultOperationLength * 0.1 || (this.multimachine.getTypeAudio() != EnumTypeAudio.VALUES[2 % EnumTypeAudio.VALUES.length])) {
                        if (type == -1) {
                            this.multimachine.initiate(2);
                            type = 2;
                        }
                    }
                }
            } else {
                if (this.progress[i] != 0 && this.parent.getActive()) {
                    if (this.operationLength > this.defaultOperationLength * 0.1 || (this.multimachine.getTypeAudio() != EnumTypeAudio.VALUES[1 % EnumTypeAudio.VALUES.length])) {
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
    }

    public MachineRecipe getRecipeOutput(int slotId) {
        return this.output[slotId];
    }

    public void setRecipeOutput(MachineRecipe output, int slotId) {
        this.output[slotId] = output;
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


    public void operate(int slotId, MachineRecipe output) {

        for (int i = 0; i < 1; i++) {

            operateOnce(slotId, output.getRecipe().output.items);
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


    @Override
    public boolean isServer() {
        return true;
    }

    public void operateOnce(int slotId, List<ItemStack> processResult) {
        for (ItemStack stack : processResult) {
            this.outputSlot.add(stack);
        }
        this.inputSlots.consume(slotId);
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

    public double getProgress(int slotId) {
        return this.progress[slotId] * 1D / this.operationLength;
    }

}
