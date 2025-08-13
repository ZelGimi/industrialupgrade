package com.denfop.componets;

import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.IMultiUpdateTick;
import com.denfop.api.recipe.InvSlotBioMultiRecipes;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.effects.EffectsRegister;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.IBioMachine;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.EnumTypeMachines;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.List;

public class BioProcessMultiComponent extends AbstractComponent implements IMultiUpdateTick {

    public final InvSlotBioMultiRecipes inputSlots;
    public final InvSlotOutput outputSlot;
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
        this.inputSlots = new InvSlotBioMultiRecipes(
                (TileEntityInventory) parent,
                enumMultiMachine.type.recipe,
                this,
                enumMultiMachine.sizeWorkingSlot, this
        );
        this.outputSlot = new InvSlotOutput(
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
    public boolean isClient() {
        return true;
    }

    @Override
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.parent.getActive() && this.parent.getWorld().getGameTime() % 4 == 0) {
            double x = this.parent.getBlockPos().getX() + 0.5;
            double y = this.parent.getBlockPos().getY() + 1.1;
            double z = this.parent.getBlockPos().getZ() + 0.5;

            this.parent.getLevel().addParticle(
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState()),
                    x, y, z,
                    0.0, 0.1, 0.0
            );
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.parent.getLevel().isClientSide) {
            inputSlots.load();
            this.getsOutputs();
        }
    }

    @Override
    public boolean onBlockActivated(final Player player, final InteractionHand hand) {
        return false;
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
            ) && (this.bioFuel.canUseEnergy(energyConsume))) {
                active = true;
                if (this.multimachine.getTank() != null) {
                    if (this.multimachine.getTank().getFluid().isEmpty() || this.multimachine.getTank().getFluid().getAmount() < 1000) {
                        return;
                    }
                }
                if (this.multimachine.getHeat() != null) {
                    if (output.getRecipe().output.metadata.getShort("minHeat") == 0 || output.getRecipe().output.metadata.getShort(
                            "minHeat") > this.heat.getEnergy()) {
                        if (!(this).heat.buffer.need) {
                            (this).heat.buffer.need = true;
                        }
                        return;

                    } else if ((this).heat.buffer.need) {
                        (this).heat.buffer.need = false;
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
                this.multimachine.getTank().drain(1000, IFluidHandler.FluidAction.EXECUTE);
            }
            if (!this.enumMultiMachine.recipe.equals("recycler")) {
                if (this.multimachine.getTank() != null) {
                    if (this.multimachine.getTank().getFluid().isEmpty() || this.multimachine.getTank().getFluid().getAmount() < 1000) {
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
