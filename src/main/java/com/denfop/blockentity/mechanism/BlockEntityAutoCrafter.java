package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuAutoCrafter;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryAutoCrafter;
import com.denfop.inventory.InventoryAutoCrafting;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputItemStack;
import com.denfop.screen.ScreenAutoCrafter;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BlockEntityAutoCrafter extends BlockEntityElectricMachine implements BlockEntityUpgrade {

    public final InventoryUpgrade upgradeSlot;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final double defaultEnergyStorage;
    private final CraftingContainer crafingTable;
    private final Inventory slot;
    private final InventoryAutoCrafter autoCrafter;
    public int operationsPerTick;
    public double energyConsume;
    public ComponentProgress componentProgress;
    BaseMachineRecipe recipe;
    private boolean canRecipe = false;

    public BlockEntityAutoCrafter(BlockPos pos, BlockState state) {
        super(1000, 4, 1, BlockBaseMachine3Entity.autocrafter, pos, state);
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 18) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                ((BlockEntityAutoCrafter) this.base).checkRecipe();
                return content;
            }
        };
        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = operationsPerTick = 100;
        this.defaultTier = 4;
        this.defaultEnergyStorage = 2 * 100;
        this.autoCrafter = new InventoryAutoCrafter(this, null, 9);
        this.crafingTable = new InventoryAutoCrafting(this);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
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
    public ContainerMenuAutoCrafter getGuiContainer(final Player var1) {
        return new ContainerMenuAutoCrafter(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenAutoCrafter((ContainerMenuAutoCrafter) menu);
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
                    .filter(itemStack -> !itemStack.isEmpty())
                    .toList();


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
        if (!level.isClientSide) {
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
        if (this.getWorld().getGameTime() % 40 == 0) {
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

    public InventoryAutoCrafter getAutoCrafter() {
        return autoCrafter;
    }

    public Inventory getSlot() {
        return slot;
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.autocrafter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public void updateCraft() {
        if (!(level instanceof ServerLevel))
            return;
        ;
        RecipeManager recipeManager = ((ServerLevel) level).getRecipeManager();
        Collection<CraftingRecipe> recipes = recipeManager.getAllRecipesFor(RecipeType.CRAFTING);

        if (this.autoCrafter.isEmpty()) {
            recipe = null;
            return;
        }
        recipe = null;
        for (CraftingRecipe recipe1 : recipes) {

            if (recipe1.matches(this.crafingTable, level)) {
                final ItemStack output = recipe1.assemble(this.crafingTable);
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
    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(
                EnumBlockEntityUpgrade.Processing,
                EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage,
                EnumBlockEntityUpgrade.ItemExtract,
                EnumBlockEntityUpgrade.ItemInput
        );
    }

}
