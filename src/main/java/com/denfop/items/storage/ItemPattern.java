package com.denfop.items.storage;

import com.denfop.IUCore;
import com.denfop.api.storage.PatternItem;
import com.denfop.api.storage.autocrafting.PatternStack;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.api.storage.autocrafting.TypeRecipe;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.items.IProperties;
import com.denfop.tabs.IItemTab;
import com.denfop.utils.Keyboard;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ItemPattern extends Item implements PatternItem, IItemTab, IProperties {
    private String nameItem;

    public ItemPattern() {
        super(new Properties().setNoRepair().stacksTo(64));
        IUCore.proxy.addProperties(this);

    }

    @Override
    public Component getName(ItemStack pStack) {
        if (hasPattern(pStack)) {
            return super.getName(pStack);
        } else {
            return Component.translatable("iu.pattern_empty");
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String[] properties() {
        return new String[]{"written"};
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getItemProperty(ItemStack stack, ClientLevel level, LivingEntity entity, int p174679, String property) {

        if (property.trim().equals("written")) {
            if (stack.getItem() instanceof ItemPattern patternItem) {
                if (!patternItem.hasPattern(stack)) {
                    return 0.0F;
                }
                return Screen.hasShiftDown() ? 1.0F : 0.5F;
            }

        }

        return 0.0F;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            p_41392_.add(new ItemStack(this, 1));
        }
    }


    public ItemStack getPreviewStack(ItemStack stack, HolderLookup.Provider provider) {
        if (!hasPattern(stack)) {
            return ItemStack.EMPTY;
        }

        PatternStack pattern = getPattern(stack, provider);
        if (pattern == null || pattern.output() == null || pattern.output().isEmpty()) {
            return ItemStack.EMPTY;
        }

        SameStack first = pattern.output().get(0);
        if (first == null || !first.isItem() || first.getStack().isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack preview = first.getStack().copy();
        return preview;
    }

    public FluidStack getPreviewFluid(ItemStack stack, HolderLookup.Provider provider) {
        if (!hasPattern(stack)) {
            return FluidStack.EMPTY;
        }

        PatternStack pattern = getPattern(stack, provider);
        if (pattern == null || pattern.output() == null || pattern.output().isEmpty()) {
            return FluidStack.EMPTY;
        }

        SameStack first = pattern.output().get(0);
        if (first == null || first.isItem()) {
            return FluidStack.EMPTY;
        }

        FluidStack fluid = first.getFluidStack();
        if (fluid == null || fluid.isEmpty()) {
            return FluidStack.EMPTY;
        }

        return fluid.copy();
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "iu." + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    private Component joinComponents(List<Component> list) {
        MutableComponent result = Component.empty();

        for (int i = 0; i < list.size(); i++) {
            result.append(list.get(i));

            if (i < list.size() - 1) {
                result.append(", ");
            }
        }

        return result;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext p_339594_, List<Component> tooltip, TooltipFlag p_41424_) {
        super.appendHoverText(stack, p_339594_, tooltip, p_41424_);
        PatternStack pattern = getPattern(stack, p_339594_.registries());
        if (pattern == null) return;


        tooltip.add(Component.translatable("iu.pattern_type")
                .append(": ")
                .append(Component.translatable("iu.pattern_type_" + pattern.typeRecipe().name().toLowerCase())));


        if (!pattern.output().isEmpty()) {

            List<Component> parts = new ArrayList<>();

            for (SameStack s : pattern.output()) {
                if (!s.getStack().isEmpty()) {
                    parts.add(Component.literal("" + s.getStack().getCount() + " ")
                            .append(s.getStack().getHoverName()));
                }

                if (!s.getFluidStack().isEmpty()) {
                    parts.add(Component.literal("" + s.getFluidStack().getAmount() + "mB ")
                            .append(Component.translatable(s.getFluidStack().getDescriptionId())));
                }
            }

            tooltip.add(Component.translatable("iu.pattern_output")
                    .append("")
                    .append(joinComponents(parts)));
        }


        if (!pattern.inputs().isEmpty() && pattern.typeRecipe() != TypeRecipe.WORKBENCH) {

            List<Component> parts = new ArrayList<>();

            for (SameStack s : pattern.inputs()) {
                if (!s.getStack().isEmpty()) {
                    parts.add(Component.literal("" + s.getStack().getCount() + " ")
                            .append(s.getStack().getHoverName()));
                }

                if (!s.getFluidStack().isEmpty()) {
                    parts.add(Component.literal("" + s.getFluidStack().getAmount() + "mB ")
                            .append(Component.translatable(s.getFluidStack().getDescriptionId())));
                }
            }

            tooltip.add(Component.translatable("iu.pattern_input")
                    .append("")
                    .append(joinComponents(parts)));
        }


        stack.set(DataComponentsInit.MODE, Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 1 : 0);
    }


    @Override
    public PatternStack getPattern(ItemStack stack, HolderLookup.Provider provider) {
        return PatternStack.readFromComponent(stack, provider);
    }

    @Override
    public boolean hasPattern(ItemStack stack) {
        return PatternStack.hasPatternComponent(stack);
    }
}
