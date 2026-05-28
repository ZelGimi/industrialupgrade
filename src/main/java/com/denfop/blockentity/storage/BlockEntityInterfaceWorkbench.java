package com.denfop.blockentity.storage;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.storage.EnumTypeSlots;
import com.denfop.api.storage.IWorkbenchInterface;
import com.denfop.api.storage.PatternItem;
import com.denfop.api.storage.StorageNetwork;
import com.denfop.api.storage.autocrafting.PatternStack;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.api.storage.autocrafting.TypeRecipe;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockStorageSystemEntity;
import com.denfop.componets.ComponentStorageEnergy;
import com.denfop.containermenu.ContainerInterfaceWorkbench;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.inventory.Inventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputItemStack;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenInterfaceWorkbench;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BlockEntityInterfaceWorkbench extends BlockEntityInventory implements IWorkbenchInterface {

    private static final String PREFERRED_MODID = Constants.MOD_ID;
    private final Inventory slotSlots;
    protected ComponentStorageEnergy energy;
    Map<Integer, PatternStack> patternItemHashMap = new HashMap<>();
    List<PatternStack> patternStacks = new ArrayList<>();
    private StorageNetwork network;

    public BlockEntityInterfaceWorkbench(BlockPos pos, BlockState state) {
        super(BlockStorageSystemEntity.interface_workbench, pos, state);

        this.energy = this.addComponent(ComponentStorageEnergy.asBasicSink(EnergyType.STORAGE, this, 0));
        this.slotSlots = new Inventory(this, Inventory.TypeItemSlot.INPUT, 63) {
            @Override
            public boolean canPlaceItem(int index, ItemStack stack) {
                return stack.getItem() instanceof PatternItem && ((PatternItem) stack.getItem()).hasPattern(stack) && ((PatternItem) stack.getItem()).getPattern(stack, registryAccess()).typeRecipe() == TypeRecipe.WORKBENCH;
            }

            @Override
            public ItemStack set(int i, ItemStack empty) {
                ItemStack stack = super.set(i, empty);
                reload();
                if (network != null) {
                    StorageNetwork storageNetwork = network;
                    storageNetwork.reBuildPatterns = true;
                }
                return stack;
            }
        };
        slotSlots.setStackSizeLimit(1);
    }
    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (cap == Capabilities.ItemHandler.BLOCK)
            return null;
        return super.getCapability(cap, side);
    }
    public MultiBlockEntity getTeBlock() {
        return BlockStorageSystemEntity.interface_workbench;
    }

    public BlockTileEntity getBlock() {
        return IUItem.storageSystem.getBlock(getTeBlock());
    }

    public Inventory getSlots() {
        return slotSlots;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);


    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();

        return packet;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        addStorageSystemConsumptionInformation(tooltip);
    }

    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);

    }

    @Override
    public List<ItemStack> getWrenchDrops(final Player player, final int fortune) {
        List<ItemStack> itemStackList = super.getWrenchDrops(player, fortune);


        return itemStackList;
    }

    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();

        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);

    }

    @Override
    public void onNeighborChange(BlockState neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);

    }

    public void updateEntityServer() {
        super.updateEntityServer();

    }

    public void updateField(String name, CustomPacketBuffer is) {

        super.updateField(name, is);
    }

    @Override
    public ContainerInterfaceWorkbench getGuiContainer(final Player var1) {
        return new ContainerInterfaceWorkbench(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(final Player var1, final ContainerMenuBase<?> var2) {
        return new ScreenInterfaceWorkbench((ContainerInterfaceWorkbench) var2);
    }

    public void onLoaded() {
        super.onLoaded();
        reload();
    }

    public void reload() {
        if (!(level instanceof ServerLevel))
            return;
        ;
        RecipeManager recipeManager = level.getRecipeManager();

        patternItemHashMap.clear();
        patternStacks.clear();
        for (int i = 0; i < slotSlots.size(); i++) {
            ItemStack stack = slotSlots.getItem(i);
            if (!stack.isEmpty()) {
                PatternItem cellItem = (PatternItem) stack.getItem();
                PatternStack cell = cellItem.getPattern(stack, registryAccess());
                RecipeHolder<CraftingRecipe> recipeHolder = new RecipeHolder(cell.location(), null);
                Recipe<?> recipe = recipeManager.getAllRecipesFor(RecipeType.CRAFTING).stream()
                        .filter(recipeHolder1 -> recipeHolder1.equals(recipeHolder))
                        .findFirst()
                        .map(RecipeHolder::value)
                        .orElse(null);
                if (recipe != null) {
                    List<IInputItemStack> list = new ArrayList<>();
                    for (Ingredient ingredient : recipe.getIngredients()) {
                        if (!ingredient.isEmpty()) {


                            boolean find = false;
                            for (IInputItemStack iInputItemStack : list) {
                                if (iInputItemStack.matches(ingredient.getItems()[0])) {
                                    find = true;
                                    iInputItemStack.growAmount(ingredient.getItems()[0].getCount());
                                }
                            }
                            if (!find) {
                                final IInputItemStack input = Recipes.inputFactory.getInput(ingredient);
                                list.add(input);
                            }

                        }
                    }
                    List<SameStack> stacks = new ArrayList<>();
                    for (IInputItemStack iInputItemStack : list) {
                        stacks.add(new SameStack(pickPreferred(iInputItemStack.getInputs())));
                    }
                    if (!cell.matches(stacks))
                        continue;
                    boolean find = false;
                    for (SameStack sameStack : cell.output()) {
                        if (ModUtils.checkItemEquality(recipe.getResultItem(level.registryAccess()), sameStack.getStack())) {
                            find = true;
                            break;
                        }
                    }
                    if (!find)
                        continue;
                    patternItemHashMap.put(i, cell);
                    patternStacks.add(cell);
                }
            }
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
    public void setStorageNetwork(StorageNetwork network) {
        this.network = network;
        if (network != null)
            network.reBuildPatterns = true;
    }


    @Override
    public List<PatternStack> getPatterns() {
        return patternStacks;
    }

    @Override
    public List<ItemStack> getStacks() {
        return Collections.emptyList();
    }

    @Override
    public List<FluidStack> getFluidStacks() {
        return Collections.emptyList();
    }

    @Override
    public BlockEntity getBlockEntityNeighbor() {
        return null;
    }

    @Override
    public Direction getDirection() {
        return Direction.UP;
    }

    @Override
    public EnumTypeSlots getTypeSlots() {
        return EnumTypeSlots.WHITELIST;
    }
}
