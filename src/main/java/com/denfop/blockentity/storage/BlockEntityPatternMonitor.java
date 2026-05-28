package com.denfop.blockentity.storage;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.storage.IMonitor;
import com.denfop.api.storage.PatternItem;
import com.denfop.api.storage.StorageNetwork;
import com.denfop.api.storage.autocrafting.PatternStack;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.api.storage.autocrafting.TypeRecipe;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockStorageSystemEntity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentStorageEnergy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerPatternMonitor;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryPatternOutput;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputItemStack;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenPatternMonitor;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockEntityPatternMonitor extends BlockEntityInventory implements IMonitor, IUpdatableTileEvent {


    private static final String PREFERRED_MODID = Constants.MOD_ID;
    public final InventoryPatternOutput inputItems;
    public final InventoryPatternOutput outputItems;
    public final Inventory patternItem;
    public final InventoryOutput patternItemOutput;
    private final ComponentBaseEnergy energy;
    public StorageNetwork network;
    public int sizeMode = 0;
    public int prevSizeMode = 0;
    public int decreasing = 0;
    public int viewMode = 0;
    public int modeCraft = 0;
    public int value1;
    public int value;
    public int fieldMode = 0;
    public String fieldString = "";
    public boolean checkField = true;
    private BaseMachineRecipe recipe;
    private ResourceLocation location;

    public BlockEntityPatternMonitor(BlockPos pos, BlockState state) {
        super(BlockStorageSystemEntity.pattern_monitor, pos, state);
        this.energy = this.addComponent(ComponentStorageEnergy.asBasicSink(EnergyType.STORAGE, this, 0));
        this.inputItems = new InventoryPatternOutput(this, 36) {
            @Override
            public ItemStack set(int index, ItemStack content) {
                ItemStack stack = super.set(index, content);
                updateCraft();
                return stack;
            }
        };
        this.outputItems = new InventoryPatternOutput(this, 36) {
            @Override
            public boolean canPlaceItem(int index, ItemStack stack) {

                return super.canPlaceItem(index, stack) && modeCraft != 0;
            }
        };
        this.patternItem = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(int index, ItemStack stack) {
                return stack.getItem() instanceof PatternItem && !((PatternItem) stack.getItem()).hasPattern(stack);
            }
        };
        this.patternItemOutput = new InventoryOutput(this, 1);
    }
    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (cap == Capabilities.ItemHandler.BLOCK)
            return null;
        return super.getCapability(cap, side);
    }
    public MultiBlockEntity getTeBlock() {
        return BlockStorageSystemEntity.pattern_monitor;
    }

    public BlockTileEntity getBlock() {
        return IUItem.storageSystem.getBlock(getTeBlock());
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        addStorageSystemConsumptionInformation(tooltip);
    }


    private void addStorageSystemConsumptionInformation(final List<String> tooltip) {
        tooltip.add("§8" + Localization.translate("iu.storage_system.tooltip.title"));
        tooltip.add("§7" + Localization.translate("iu.storage_system.tooltip.consumption") + ": §b" +
                formatStorageSystemPower(this.getRequiredPower()) + " §7" +
                Localization.translate("iu.storage_system.tooltip.energy_per_tick"));
        tooltip.add("§8" + Localization.translate("iu.storage_system.tooltip.description"));
    }

    private static String formatStorageSystemPower(final double value) {
        if (value == (long) value) {
            return Long.toString((long) value);
        }
        return Double.toString(value);
    }

    @Override
    public double getRequiredPower() {
        return 1;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        updateCraft();
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (checkField) {
            if (this.fieldMode < 2)
                this.fieldString = "";
        }
        this.checkField = true;
    }

    public void updateCraft() {
        if (!(level instanceof ServerLevel))
            return;
        ;
        if (this.modeCraft == 0) {
            RecipeManager recipeManager = level.getRecipeManager();
            List<RecipeHolder<CraftingRecipe>> recipes = recipeManager.getAllRecipesFor(RecipeType.CRAFTING);

            if (this.inputItems.isEmpty()) {
                recipe = null;
                this.outputItems.clear();
                return;
            }
            recipe = null;
            List<ItemStack> subList = inputItems.subList(0, 9);
            CraftingInput crafingTable = CraftingInput.of(3, 3, subList);
            for (RecipeHolder<CraftingRecipe> recipeHolder : recipes) {
                CraftingRecipe recipe1 = recipeHolder.value();
                if (recipe1.matches(crafingTable, level)) {
                    final ItemStack output = recipe1.assemble(crafingTable, this.getLevel().registryAccess());
                    List<IInputItemStack> list = new ArrayList<>();
                    for (int i = 0; i < 9; i++) {
                        ItemStack stack = inputItems.get(i);
                        if (!stack.isEmpty()) {
                            if (stack.getCount() > 1)
                                stack.setCount(1);
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
                    this.outputItems.clear();
                    this.outputItems.set(0, output.copy());
                    this.location = recipeHolder.id();
                    break;
                }
            }
            if (recipe == null) {
                this.outputItems.clear();
            }
        } else {
            this.recipe = null;
        }
    }

    private ItemStack pickPreferred(List<ItemStack> list) {
        if (list == null || list.isEmpty()) return ItemStack.EMPTY;


        for (ItemStack s : list) {
            if (s == null || s.isEmpty()) continue;
            var key = BuiltInRegistries.ITEM.getKey(s.getItem());
            if (key != null && PREFERRED_MODID.equals(key.getNamespace())) {
                return s;
            }
        }


        for (ItemStack s : list) {
            if (s != null && !s.isEmpty()) return s;
        }

        return list.get(0);
    }

    @Override
    public ContainerPatternMonitor getGuiContainer(Player var1) {

        return new ContainerPatternMonitor(this, var1);
    }

    @Override
    public void updateTileServer(Player var1, double var2) {

        if (modeCraft == 0 && !this.patternItem.isEmpty() && this.recipe != null) {
            List<SameStack> stacks = new ArrayList<>();
            for (IInputItemStack iInputItemStack : this.recipe.input.getInputs()) {
                stacks.add(new SameStack(pickPreferred(iInputItemStack.getInputs())));
            }
            PatternStack patternStack = new PatternStack(stacks, Collections.singletonList(new SameStack(recipe.output.items.get(0))), TypeRecipe.WORKBENCH, location);
            ItemStack stack = this.patternItem.get(0).copy();
            stack.setCount(1);
            stack = patternStack.writePattern(stack, var1.registryAccess());
            if (this.patternItemOutput.add(stack)) {
                this.patternItem.get(0).shrink(1);
            }
        } else {
            if (!this.patternItem.isEmpty()) {
                List<SameStack> sameStackList = new ArrayList<>();
                List<SameStack> sameStackListOutput = new ArrayList<>();
                for (SameStack stack : this.inputItems.sameStackList) {
                    if (stack.isItem() || stack.isFluid()) {
                        if (stack.isFluid())
                            sameStackList.add(new SameStack(stack.getFluidStack()));
                        else if (stack.isItem())
                            sameStackList.add(new SameStack(stack.getStack()));
                    }
                }
                for (SameStack stack : this.outputItems.sameStackList) {
                    if (stack.isItem() || stack.isFluid()) {
                        if (stack.isFluid())
                            sameStackListOutput.add(new SameStack(stack.getFluidStack()));
                        else if (stack.isItem())
                            sameStackListOutput.add(new SameStack(stack.getStack()));
                    }
                }
                if (!sameStackList.isEmpty() && !sameStackListOutput.isEmpty()) {
                    PatternStack patternStack = new PatternStack(sameStackList, sameStackListOutput, TypeRecipe.BLOCK, ResourceLocation.tryParse("minecraft:minecraft"));
                    ItemStack stack = this.patternItem.get(0).copy();
                    stack.setCount(1);
                    stack = patternStack.writePattern(stack, var1.registryAccess());
                    if (this.patternItemOutput.add(stack)) {
                        this.patternItem.get(0).shrink(1);
                    }
                }
            }
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer packetBuffer = super.writeContainerPacket();
        packetBuffer.writeInt(sizeMode);
        packetBuffer.writeInt(prevSizeMode);
        packetBuffer.writeInt(viewMode);
        packetBuffer.writeInt(decreasing);
        packetBuffer.writeInt(modeCraft);
        packetBuffer.writeInt(value1);
        packetBuffer.writeInt(value);
        packetBuffer.writeInt(fieldMode);
        packetBuffer.writeString(fieldString);
        return packetBuffer;
    }

    @Override
    public void readContainerPacket(CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        sizeMode = customPacketBuffer.readInt();
        prevSizeMode = customPacketBuffer.readInt();
        viewMode = customPacketBuffer.readInt();
        decreasing = customPacketBuffer.readInt();
        modeCraft = customPacketBuffer.readInt();
        value = customPacketBuffer.readInt();
        value1 = customPacketBuffer.readInt();
        fieldMode = customPacketBuffer.readInt();
        fieldString = customPacketBuffer.readString();
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt) {
        CompoundTag tag = super.writeToNBT(nbt);
        tag.putInt("sizeMode", sizeMode);
        tag.putInt("prevSizeMode", prevSizeMode);
        tag.putInt("viewMode", viewMode);
        tag.putInt("decreasing", decreasing);
        tag.putInt("value1", value1);
        tag.putInt("value", value);
        tag.putInt("modeCraft", modeCraft);
        tag.putInt("fieldMode", fieldMode);
        tag.putString("fieldString", fieldString);
        return tag;
    }

    @Override
    public void readFromNBT(CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        sizeMode = nbtTagCompound.getInt("sizeMode");
        prevSizeMode = nbtTagCompound.getInt("prevSizeMode");
        viewMode = nbtTagCompound.getInt("viewMode");
        decreasing = nbtTagCompound.getInt("decreasing");
        modeCraft = nbtTagCompound.getInt("modeCraft");
        value = nbtTagCompound.getInt("value");
        value1 = nbtTagCompound.getInt("value1");
        fieldMode = nbtTagCompound.getInt("fieldMode");
        fieldString = nbtTagCompound.getString("fieldString");
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenPatternMonitor((ContainerPatternMonitor) isAdmin);
    }

    @Override
    public void setStorageNetwork(StorageNetwork network) {
        this.network = network;
    }
}
