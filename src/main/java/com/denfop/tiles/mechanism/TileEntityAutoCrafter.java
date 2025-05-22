package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerAutoCrafter;
import com.denfop.gui.GuiAutoCrafter;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotAutoCrafter;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.invslot.InventoryAutoCrafting;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputItemStack;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TileEntityAutoCrafter extends TileElectricMachine implements IUpgradableBlock {

    public final InvSlotUpgrade upgradeSlot;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    private final InventoryCrafting crafingTable;
    private final InvSlot slot;
    private final InvSlotAutoCrafter autoCrafter;
    public int operationsPerTick;
    public double energyConsume;
    public ComponentProgress componentProgress;
    BaseMachineRecipe recipe;
    private boolean canRecipe = false;

    public TileEntityAutoCrafter() {
        super(1000, 4, 1);
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 18) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                ((TileEntityAutoCrafter) this.base).checkRecipe();
            }
        };
        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = operationsPerTick = 100;
        this.defaultTier = 4;
        this.defaultEnergyStorage = 2 * 100;
        this.autoCrafter = new InvSlotAutoCrafter(this, null, 9);
        this.crafingTable = new InventoryAutoCrafting(this);
        this.upgradeSlot = new InvSlotUpgrade(this, 4);
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1, (short) defaultOperationLength));

    }


    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.energyConsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.operationsPerTick);
        }
        super.addInformation(stack, tooltip);

    }

    @Override
    public ContainerAutoCrafter getGuiContainer(final EntityPlayer var1) {
        return new ContainerAutoCrafter(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiAutoCrafter(getGuiContainer(var1));
    }

    public BaseMachineRecipe getRecipe() {
        return recipe;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        boolean hasRecipe = customPacketBuffer.readBoolean();
        if (hasRecipe) {
            try {
                this.recipe = (BaseMachineRecipe) DecoderHandler.decode(customPacketBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.recipe = null;
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(recipe != null);
        if (recipe != null) {
            try {
                EncoderHandler.encode(customPacketBuffer, this.recipe);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return customPacketBuffer;
    }

    private void checkRecipe() {
        if (recipe == null) {
            canRecipe = false;
        } else {
            canRecipe = false;

            final List<ItemStack> list = this.slot.stream()
                    .filter(itemStack -> !itemStack.isEmpty()).collect(Collectors.toList());


            final List<IInputItemStack> input = recipe.input.getInputs();


            for (IInputItemStack needed : input) {
                int totalFound = 0;


                for (ItemStack stack : list) {
                    if (needed.matches(stack)) {
                        totalFound += stack.getCount();

                        if (totalFound >= needed.getAmount()) {
                            break;
                        }
                    }
                }


                if (totalFound < needed.getAmount()) {
                    return;
                }
            }


            canRecipe = true;
        }
    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            this.setOverclockRates();
            updateCraft();
            this.checkRecipe();
        }


    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (recipe != null && this.energy.getEnergy() >= this.energyConsume && outputSlot.canAdd(recipe.getOutput().items) && canRecipe) {
            if (!this.getActive()) {
                setActive(true);
            }
            this.componentProgress.addProgress(0);
            this.energy.useEnergy(this.energyConsume);

            if (this.componentProgress.getProgress() >= this.componentProgress.getMaxValue()) {
                operate(recipe);
                this.componentProgress.setProgress((short) 0);
            }
        } else {
            if (recipe == null && this.getActive()) {
                this.componentProgress.setProgress((short) 0);
            }
            if (this.getActive()) {
                setActive(false);
            }
        }
        if (this.upgradeSlot.tickNoMark()) {
            setOverclockRates();
        }
        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            this.checkRecipe();
        }
    }

    public void setOverclockRates() {
        this.operationsPerTick = this.upgradeSlot.getOperationsPerTick(this.defaultOperationLength);
        this.componentProgress.setMaxValue((short) this.upgradeSlot.getOperationLength(this.defaultOperationLength));
        this.energyConsume = this.upgradeSlot.getEnergyDemand(this.defaultEnergyConsume);
        int tier = this.upgradeSlot.getTier(this.defaultTier);
        this.energy.setSinkTier(tier);
        this.energy.setCapacity(this.upgradeSlot.getEnergyStorage(
                this.defaultEnergyStorage
        ));
        this.operationsPerTick = Math.max(1, Math.min(64, this.operationsPerTick));
    }

    public void operate(BaseMachineRecipe output) {
        for (int i = 0; i < this.operationsPerTick && this.canRecipe; i++) {
            List<ItemStack> processResult = output.output.items;
            operateOnce(processResult);
            this.checkRecipe();
            if (!this.outputSlot.canAdd(recipe.getOutput().items)) {
                break;
            }
        }
    }

    private void operateOnce(List<ItemStack> processResult) {
        final List<IInputItemStack> input = recipe.input.getInputs();
        final List<ItemStack> list = this.slot.stream()
                .filter(itemStack -> !itemStack.isEmpty())
                .collect(Collectors.toList());

        for (IInputItemStack needed : input) {
            int remaining = needed.getAmount();

            for (ItemStack stack : list) {
                if (needed.matches(stack) && remaining > 0) {
                    int available = stack.getCount();
                    if (available >= remaining) {

                        stack.shrink(remaining);
                        remaining = 0;
                        break;
                    } else {

                        stack.shrink(available);
                        remaining -= available;

                    }
                }
            }

            if (remaining > 0) {

                return;
            }
        }

        this.outputSlot.add(processResult);
    }
    public InvSlotAutoCrafter getAutoCrafter() {
        return autoCrafter;
    }

    public InvSlot getSlot() {
        return slot;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.autocrafter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public void updateCraft() {
        final Collection<IRecipe> recipes = ForgeRegistries.RECIPES.getValuesCollection();
        if (this.autoCrafter.isEmpty()) {
            recipe = null;
            return;
        }
        recipe = null;
        for (IRecipe recipe1 : recipes) {

            if (recipe1.matches(this.crafingTable, world)) {
                final ItemStack output = recipe1.getCraftingResult(this.crafingTable);
                List<IInputItemStack> list = new ArrayList<>();
                for (ItemStack stack : this.autoCrafter) {
                    if (!stack.isEmpty()) {

                        boolean find = false;
                        for (IInputItemStack iInputItemStack : list) {
                            if (iInputItemStack.matches(stack)) {
                                find = true;
                                iInputItemStack.growAmount(stack.getCount());
                            }
                        }
                        if (!find) {
                            final IInputItemStack input = Recipes.inputFactory.getInput(stack.copy());
                            list.add(input);
                        }
                    }
                }
                this.recipe = new BaseMachineRecipe(new Input(list), new RecipeOutput(null, output.copy()));
                break;
            }
        }
        checkRecipe();
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }

}
