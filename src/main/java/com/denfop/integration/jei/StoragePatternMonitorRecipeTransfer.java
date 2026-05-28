package com.denfop.integration.jei;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerPatternMonitor;
import com.denfop.containermenu.SlotVirtualMonitor;
import com.denfop.network.packet.PacketSetFluid;
import com.denfop.network.packet.PacketUpdateMonitor;
import com.denfop.register.Register;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.recipe.transfer.IUniversalRecipeTransferHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public class StoragePatternMonitorRecipeTransfer implements IUniversalRecipeTransferHandler<ContainerMenuBase<?>> {

    private static final String PREFERRED_MODID = Constants.MOD_ID;
    private final IRecipeTransferHandlerHelper handlerHelper;

    public StoragePatternMonitorRecipeTransfer(
            IRecipeTransferHandlerHelper handlerHelper
    ) {
        this.handlerHelper = handlerHelper;
    }

    @Override
    public Class<? extends ContainerMenuBase<?>> getContainerClass() {
        return ContainerPatternMonitor.class;
    }

    @Override
    public Optional<MenuType<ContainerMenuBase<?>>> getMenuType() {
        return Optional.of(Register.containerBase.get());

    }

    @Override
    public @Nullable IRecipeTransferError transferRecipe(ContainerMenuBase<?> containerBase, Object object, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {
        ContainerPatternMonitor container = (ContainerPatternMonitor) containerBase;
        if (object instanceof CraftingRecipe recipe) {
            boolean craftingRecipe = recipe.canCraftInDimensions(3, 3);
            if (!craftingRecipe) {
                Component tooltipMessage = Component.translatable("iu.jei.recipe_transfer.error");
                return handlerHelper.createUserErrorWithTooltip(tooltipMessage);
            } else {
                if (doTransfer) {
                    List<ItemStack> list = getItems(recipeSlots);
                    List<Slot> slotVirtuals = container.slots.stream().filter(slot -> slot instanceof SlotVirtualMonitor).toList();
                    container.base.modeCraft = 0;
                    for (int i = 0; i < list.size(); i++) {
                        slotVirtuals.get(i).set(list.get(i).copy());
                    }
                    IRecipeSlotView slot = recipeSlots.getSlotViews().get(0);

                    List<ItemStack> list1 = slot.getIngredients(VanillaTypes.ITEM_STACK)
                            .filter(Objects::nonNull)
                            .toList();


                    slotVirtuals.get(36).set(pickPreferred(list1));
                    if (container.base.modeCraft != 0) {
                        new PacketUpdateMonitor(container.player, container.base.pos, 10);
                    }
                }
                return null;
            }
        } else {
            if (doTransfer) {
                if (container.base.modeCraft != 1) {
                    new PacketUpdateMonitor(container.player, container.base.pos, 10);
                }
                container.base.modeCraft = 1;
                List<ItemStack> listInputStack = getItemsInput(recipeSlots);
                List<ItemStack> listOutputStack = getItemsOutput(recipeSlots);
                List<FluidStack> listInputFluid = getFluidsInput(recipeSlots);
                List<FluidStack> listOutputFluid = getFluidsOutput(recipeSlots);
                List<Slot> slotVirtuals = container.slots.stream().filter(slot -> slot instanceof SlotVirtualMonitor).toList();
                slotVirtuals.forEach(slot -> slot.set(ItemStack.EMPTY));
                for (int i = 0; i < Math.min(36, listInputStack.size() + listInputFluid.size()); i++) {
                    if (i < listInputStack.size())
                        slotVirtuals.get(i).set(listInputStack.get(i).copy());
                    else {
                        ItemStack stack = new ItemStack(IUItem.reinforcedFluidCell.getItem());
                        ModUtils.nbt(stack).putBoolean("type_recipe", true);
                        FluidHandlerFix.getFluidHandler(stack).fill(listInputFluid.get(i - listInputStack.size()).copy(), IFluidHandler.FluidAction.EXECUTE);
                        slotVirtuals.get(i).set(stack);
                        SameStack sameStack = new SameStack(listInputFluid.get(i - listInputStack.size()));
                        sameStack.setStack(stack);
                        ((SlotVirtualMonitor) slotVirtuals.get(i)).setFluid(sameStack);

                        new PacketSetFluid(container.base, true, i, sameStack, true);
                    }

                }
                for (int i = 0; i < Math.min(36, listOutputStack.size() + listOutputFluid.size()); i++) {
                    if (i < listOutputStack.size())
                        slotVirtuals.get(36 + i).set(listOutputStack.get(i).copy());
                    else {
                        ItemStack stack = new ItemStack(IUItem.reinforcedFluidCell.getItem());
                        ModUtils.nbt(stack).putBoolean("type_recipe", true);
                        FluidHandlerFix.getFluidHandler(stack).fill(listOutputFluid.get(i - listOutputStack.size()).copy(), IFluidHandler.FluidAction.EXECUTE);
                        ((SlotVirtualMonitor) slotVirtuals.get(36 + i)).set(stack);
                        SameStack sameStack = new SameStack(listOutputFluid.get(i - listOutputStack.size()));
                        sameStack.setStack(stack);
                        ((SlotVirtualMonitor) slotVirtuals.get(36 + i)).setFluid(sameStack);

                        new PacketSetFluid(container.base, false, i, sameStack, true);
                    }

                }

            }
            return null;
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

    private List<ItemStack> getItems(IRecipeSlotsView recipeLayout) {
        @Unmodifiable List<IRecipeSlotView> recipeSlots = recipeLayout.getSlotViews();

        var result = new ArrayList<ItemStack>(9);
        for (int i = 1; i < 10; i++) {
            if (i < recipeSlots.size()) {
                IRecipeSlotView slot = recipeSlots.get(i);

                List<ItemStack> list = slot.getIngredients(VanillaTypes.ITEM_STACK)
                        .filter(Objects::nonNull)
                        .toList();

                result.add(pickPreferred(list));
            } else {
                result.add(ItemStack.EMPTY);
            }
        }

        return result;
    }


    private List<FluidStack> getFluidsOutput(IRecipeSlotsView recipeLayout) {
        @Unmodifiable List<IRecipeSlotView> recipeSlots = recipeLayout.getSlotViews(RecipeIngredientRole.OUTPUT);

        List<FluidStack> result = new LinkedList<>();
        for (IRecipeSlotView slot : recipeSlots) {

            List<FluidStack> list = slot.getIngredients(ForgeTypes.FLUID_STACK)
                    .filter(Objects::nonNull)
                    .toList();
            if (!list.isEmpty()) {
                result.add(list.get(0));
            }


        }

        return new ArrayList<>(result);
    }

    private List<FluidStack> getFluidsInput(IRecipeSlotsView recipeLayout) {
        @Unmodifiable List<IRecipeSlotView> recipeSlots = recipeLayout.getSlotViews(RecipeIngredientRole.INPUT);

        List<FluidStack> result = new LinkedList<>();
        for (IRecipeSlotView slot : recipeSlots) {

            List<FluidStack> list = slot.getIngredients(ForgeTypes.FLUID_STACK)
                    .filter(Objects::nonNull)
                    .toList();
            if (!list.isEmpty()) {
                result.add(list.get(0));
            }


        }

        return new ArrayList<>(result);
    }

    private List<ItemStack> getItemsInput(IRecipeSlotsView recipeLayout) {
        @Unmodifiable List<IRecipeSlotView> recipeSlots = recipeLayout.getSlotViews(RecipeIngredientRole.INPUT);

        List<ItemStack> result = new LinkedList<>();
        for (IRecipeSlotView slot : recipeSlots) {

            List<ItemStack> list = slot.getIngredients(VanillaTypes.ITEM_STACK)
                    .filter(Objects::nonNull)
                    .toList();
            if (!list.isEmpty()) {

                result.add(pickPreferred(list));
            }


        }

        return new ArrayList<>(result);
    }

    private List<ItemStack> getItemsOutput(IRecipeSlotsView recipeLayout) {
        @Unmodifiable List<IRecipeSlotView> recipeSlots = recipeLayout.getSlotViews(RecipeIngredientRole.OUTPUT);

        List<ItemStack> result = new LinkedList<>();
        for (IRecipeSlotView slot : recipeSlots) {

            List<ItemStack> list = slot.getIngredients(VanillaTypes.ITEM_STACK)
                    .filter(Objects::nonNull)
                    .toList();
            if (!list.isEmpty()) {
                result.add(pickPreferred(list));
            }


        }

        return new ArrayList<>(result);
    }
}