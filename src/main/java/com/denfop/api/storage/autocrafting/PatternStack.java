package com.denfop.api.storage.autocrafting;

import com.denfop.api.storage.PatternItem;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record PatternStack(List<SameStack> inputs, List<SameStack> output, TypeRecipe typeRecipe,
                           ResourceLocation location) {

    public static PatternStack readFromNBT(CompoundTag nbt) {
        List<SameStack> inputs = new ArrayList<>();
        List<SameStack> outputs = new ArrayList<>();
        if (!nbt.contains("Inputs")) {
            return null;
        }
        ListTag inputsList = nbt.getList("Inputs", 10);
        for (int i = 0; i < inputsList.size(); i++) {
            CompoundTag inputNBT = inputsList.getCompound(i);
            inputs.add(SameStack.readFromNBT(inputNBT));
        }
        ResourceLocation resourceLocation = ResourceLocation.tryParse(nbt.getString("location"));
        ListTag outputsList = nbt.getList("Outputs", 10);
        for (int i = 0; i < outputsList.size(); i++) {
            CompoundTag outputNBT = outputsList.getCompound(i);
            SameStack sameStack = SameStack.readFromNBT(outputNBT);
            if (!(sameStack.getStack().isEmpty() && sameStack.getFluidStack().isEmpty()))
                outputs.add(sameStack);
        }

        TypeRecipe typeRecipe = TypeRecipe.valueOf(nbt.getString("TypeRecipe"));

        return new PatternStack(inputs, outputs, typeRecipe, resourceLocation);
    }

    public CompoundTag writeToNBT() {
        CompoundTag nbt = new CompoundTag();

        ListTag inputsList = new ListTag();
        for (SameStack s : inputs) {
            if ((s.isItem() || s.isFluid()))
                inputsList.add(s.writeToNBT());
        }
        nbt.put("Inputs", inputsList);
        ListTag outputsList = new ListTag();
        for (SameStack s : output) {
            outputsList.add(s.writeToNBT());
        }
        nbt.put("Outputs", outputsList);
        nbt.putString("location", location.toString());

        nbt.putString("TypeRecipe", typeRecipe.name());

        return nbt;
    }

    public boolean matches(List<SameStack> remaining) {
        if (remaining.size() != inputs.size()) {
            return false;
        }


        for (SameStack required : inputs) {

            boolean matched = false;

            for (SameStack provided : new ArrayList<>(remaining)) {
                if (required.equals(provided)) {
                    matched = true;
                    remaining.remove(provided);
                    break;
                }
            }

            if (!matched) {
                return false;
            }
        }

        return true;
    }

    public ItemStack writePattern(ItemStack stack) {
        if (!(stack.getItem() instanceof PatternItem))
            return stack;
        ModUtils.nbt(stack).put("pattern", writeToNBT());
        return stack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatternStack that = (PatternStack) o;
        return Objects.equals(output, that.output) && typeRecipe == that.typeRecipe;
    }

    @Override
    public int hashCode() {
        return Objects.hash(output, typeRecipe);
    }


}
