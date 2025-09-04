package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.*;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuWorldCollector;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.inventory.InventoryWorldCollector;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenWolrdCollector;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityBaseWorldCollector extends BlockEntityElectricMachine implements IUpdateTick, IUpgradableBlock,
        IHasRecipe {

    public final EnumTypeCollector enumTypeCollector;
    public final InventoryRecipes inputSlot;
    public final InventoryUpgrade upgradeSlot;
    public final InventoryWorldCollector MatterSlot;
    public final double max_matter_energy;
    private final double defaultEnergyConsume;
    private final int defaultOperationLength;
    public double matter_energy;
    public double need_matter;
    public double guiProgress;
    protected MachineRecipe machineRecipe;
    private double energyConsume;
    private int operationLength;
    private int operationsPerTick;
    private boolean canWork = false;
    private int progress;


    public BlockEntityBaseWorldCollector(EnumTypeCollector enumTypeCollector1, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(0, 1, 1, block, pos, state);
        enumTypeCollector = enumTypeCollector1;
        this.MatterSlot = new InventoryWorldCollector(this);
        this.inputSlot = new InventoryRecipes(this, enumTypeCollector1.name().toLowerCase() + "collector", this);
        this.machineRecipe = null;
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.defaultEnergyConsume = this.energyConsume = 40;
        this.defaultOperationLength = this.operationLength = 800;
        this.operationsPerTick = 1;
        this.matter_energy = 0;
        this.max_matter_energy = 10000;
        Recipes.recipes.addInitRecipes(this);
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("iu.need_info") + new ItemStack(
                IUItem.matter.getStack(enumTypeCollector.getMeta()),
                1
        ).getDisplayName().getString());
        super.addInformation(stack, tooltip);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            guiProgress = (double) DecoderHandler.decode(customPacketBuffer);
            need_matter = (double) DecoderHandler.decode(customPacketBuffer);
            matter_energy = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, guiProgress);
            EncoderHandler.encode(packet, need_matter);
            EncoderHandler.encode(packet, matter_energy);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    protected void addRecipe(ItemStack input, double need, ItemStack output) {
        final IInputHandler input_recipe = com.denfop.api.Recipes.inputFactory;
        final CompoundTag nbt = ModUtils.nbt();
        nbt.putDouble("need", need);
        Recipes.recipes.addRecipe(
                enumTypeCollector.name().toLowerCase() + "collector",
                new BaseMachineRecipe(new Input(input_recipe.getInput(input)), new RecipeOutput(nbt, output))
        );
    }

    protected void addRecipe(String input, ItemStack output, double need) {
        final IInputHandler input_recipe = com.denfop.api.Recipes.inputFactory;
        final CompoundTag nbt = ModUtils.nbt();
        nbt.putDouble("need", getMatterFromEnergy(need));
        Recipes.recipes.addRecipe(
                enumTypeCollector.name().toLowerCase() + "collector",
                new BaseMachineRecipe(new Input(input_recipe.getInput(input)), new RecipeOutput(nbt, output))
        );
    }

    public MachineRecipe getOutput() {
        this.machineRecipe = this.inputSlot.process();

        return this.machineRecipe;
    }

    protected double getMatterFromEnergy(double energy) {
        return energy / 250000D;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            inputSlot.load();
            this.setOverclockRates();
            this.getOutput();
            if (this.machineRecipe != null) {
                this.getrequiredmatter(machineRecipe.getRecipe().getOutput());
            } else {
                this.getrequiredmatter(null);
            }
            this.MatterSlot.getmatter();
        }

    }

    public void operate(MachineRecipe output) {
        for (int i = 0; i < this.operationsPerTick; i++) {

            operateOnce(output.getRecipe().output.items);
            if (this.matter_energy < this.need_matter) {
                break;
            }
            if (this.inputSlot.get(0).isEmpty() || this.inputSlot
                    .get(0)
                    .getCount() < this.machineRecipe.getRecipe().input
                    .getInputs()
                    .get(0)
                    .getInputs()
                    .get(0)
                    .getCount() || !this.outputSlot.canAdd(output.getRecipe().output.items)) {
                this.getOutput();
                break;
            } else {
                if (this.matter_energy < this.need_matter && this.inputSlot.get(0).isEmpty() || this.inputSlot
                        .get(0)
                        .getCount() < 1 || !this.outputSlot.canAdd(output.getRecipe().output.items)) {
                    this.getOutput();
                    break;
                }
            }


        }


    }

    public void operateOnce(List<ItemStack> processResult) {

        this.matter_energy -= this.need_matter;
        this.inputSlot.consume();
        this.outputSlot.add(processResult);
        this.getrequiredmatter(this.machineRecipe.getRecipe().getOutput());


    }

    public void setOverclockRates() {
        this.operationsPerTick = this.upgradeSlot.getOperationsPerTick(this.defaultOperationLength);
        this.operationLength = this.upgradeSlot.getOperationLength(this.defaultOperationLength);
        this.energyConsume = this.upgradeSlot.getEnergyDemand(this.defaultEnergyConsume);


    }

    public boolean useEnergy(double amount, boolean consume) {
        if (this.energy.canUseEnergy(amount)) {
            if (consume) {
                this.energy.useEnergy(amount);
            }
            return true;
        }
        return false;
    }

    public double getGuiProgress() {
        return guiProgress;
    }

    public double getMatter_energy() {
        return matter_energy;
    }

    public double getMax_matter_energy() {
        return max_matter_energy;
    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.matter_energy = nbttagcompound.getDouble("matter_energy");
        this.progress = nbttagcompound.getInt("progress");
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        final CompoundTag nbt = super.writeToNBT(nbttagcompound);
        nbt.putDouble("matter_energy", this.matter_energy);
        nbt.putInt("progress", this.progress);
        return nbt;
    }

    public void updateEntityServer() {

        super.updateEntityServer();


        MachineRecipe output = this.machineRecipe;
        if (this.level.getGameTime() % 20 == 0 && output != null && this.matter_energy + 200 < this.max_matter_energy) {
            this.MatterSlot.getmatter();
        }
        if (output != null && this.outputSlot.canAdd(this.machineRecipe.getRecipe().output.items) && !this.inputSlot.isEmpty() && this.canWork && this.need_matter <= this.matter_energy) {
            if (!this.getActive()) {
                setActive(true);
            }

            this.progress++;

            double p = (this.progress * 1D / operationLength);


            if (p <= 1) {
                this.guiProgress = p;
            }
            if (p > 1) {
                this.guiProgress = 1;
            }
            if (progress >= operationLength) {

                operate(output);
                this.progress = 0;


            }
        } else {
            if (progress != 0) {
                progress = 0;
            }
            this.guiProgress = 0;
            if (this.getActive()) {
                setActive(false);
            }
        }
        if (this.upgradeSlot.tickNoMark()) {
            setOverclockRates();
        }

    }

    @Override
    public ContainerMenuWorldCollector getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuWorldCollector(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenWolrdCollector((ContainerMenuWorldCollector) menu);
    }

    @Override
    public void onUpdate() {

        if (this.machineRecipe == null) {
            getrequiredmatter(null);
        } else {
            getrequiredmatter(this.machineRecipe.getRecipe().getOutput());
        }
        this.MatterSlot.getmatter();
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.machineRecipe;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.machineRecipe = output;
    }

    public void getrequiredmatter(RecipeOutput output) {
        if (output == null) {
            this.need_matter = 0;
            this.canWork = false;
            return;
        }
        this.canWork = true;
        this.need_matter = output.metadata.getDouble("need");
    }

    @Override
    public void init() {

    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

}
