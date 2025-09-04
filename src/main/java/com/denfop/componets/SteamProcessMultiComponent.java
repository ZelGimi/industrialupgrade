package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.IMultiUpdateTick;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventorySteamMultiRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.sound.EnumTypeAudio;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blockentity.base.ISteamMechanism;
import com.denfop.blockentity.mechanism.EnumTypeMachines;
import com.denfop.blocks.FluidName;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.Timer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.io.IOException;
import java.util.List;

public class SteamProcessMultiComponent extends AbstractComponent implements IMultiUpdateTick {

    public final InventorySteamMultiRecipes inputSlots;
    public final InventoryOutput outputSlot;
    private final ComponentSteamEnergy steam;
    private final PressureComponent pressure;
    private final ISteamMechanism multimachine;
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
    private Timer timer1 = new Timer(0, 0, 0);
    private Timer timer = null;

    public SteamProcessMultiComponent(final ISteamMechanism parent, final EnumMultiMachine enumMultiMachine) {
        super((BlockEntityBase) parent);
        this.multimachine = parent;
        this.inputSlots = new InventorySteamMultiRecipes(
                (BlockEntityInventory) parent,
                enumMultiMachine.type.recipe,
                (IMultiUpdateTick) this,
                enumMultiMachine.sizeWorkingSlot, this
        );
        this.outputSlot = new InventoryOutput(
                (CustomWorldContainer) parent,
                enumMultiMachine.sizeWorkingSlot + (enumMultiMachine.output ? 2 : 0)
        );
        this.enumMultiMachine = enumMultiMachine;
        this.sizeWorkingSlot = enumMultiMachine.sizeWorkingSlot;
        this.progress = new short[sizeWorkingSlot];
        this.guiProgress = new double[sizeWorkingSlot];
        double coefenergy = 1;
        double speed = 1;
        this.mode = 0;
        this.defaultEnergyConsume = this.energyConsume = Math.max((int) (enumMultiMachine.usagePerTick * coefenergy), 1);
        this.defaultOperationLength = this.operationChange = this.operationLength =
                Math.max((int) (enumMultiMachine.lenghtOperation * 1D / speed), 1);
        this.output = new MachineRecipe[sizeWorkingSlot];
        this.steam = ((BlockEntityBase) parent).getComp(ComponentSteamEnergy.class);
        this.pressure = ((BlockEntityBase) parent).getComp(PressureComponent.class);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.parent.getLevel().isClientSide()) {
            inputSlots.load();
            this.getsOutputs();
        }
    }

    @Override
    public boolean onBlockActivated(final Player player, final InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem().equals(IUItem.canister.getItem())) {
            IFluidHandlerItem handler = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse((IFluidHandlerItem) stack.getItem().initCapabilities(stack, stack.getTag()));
            FluidStack fluid = handler.getFluidInTank(0);
            if (fluid != FluidStack.EMPTY && fluid.getFluid() == FluidName.fluidsteam_oil.getInstance().get() && fluid.getAmount() >= 250 && (!timer1.canWork() || timer1.getBar() == 0) && (timer == null || !timer.canWork())) {
                this.timer = new Timer(0, 0, 40);
                handler.drain(250, IFluidHandler.FluidAction.EXECUTE);
                return true;
            }
        }
        return super.onBlockActivated(player, hand);
    }

    public void setOverclockRates() {

    }

    @Override
    public void onContainerUpdate(final ServerPlayer player) {
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
            ) && (this.steam.canUseEnergy(energyConsume) && this.pressure.getEnergy() == 1)) {
                active = true;
                if (this.progress[i] == 0) {
                    if (this.operationLength > this.defaultOperationLength * 0.1) {
                        if (type == -1) {
                            this.multimachine.initiate(0);
                            type = 0;
                        }
                    }
                }
                this.steam.useEnergy(energyConsume);
                this.progress[i]++;
                this.guiProgress[i] = (double) this.progress[i] / this.operationLength;
                if (timer != null && timer.canWork()) {
                    this.progress[i]++;
                }
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
        if (this.parent.getActive() != active) {
            this.parent.setActive(active);
        }
        if (this.parent.getActive()) {
            if (this.parent.getWorld().getGameTime() % 20 == 0) {
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
            if (!this.enumMultiMachine.recipe.equals("recycler")) {
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
            if (this.inputSlots.get(slotId).isEmpty()) {
                this.output[slotId] = null;
                return null;
            }
            this.output[slotId] = this.inputSlots.process(slotId);
            return this.output[slotId];
        } else {
            if (this.inputSlots.get(slotId).isEmpty()) {
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
