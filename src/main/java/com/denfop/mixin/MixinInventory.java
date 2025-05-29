package com.denfop.mixin;

import com.denfop.items.IItemStackInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public abstract class MixinInventory {
    @Final
    @Shadow
    public NonNullList<ItemStack> items;

    @Shadow
    public int selected;

    /**
     * @author Denfop
     * @reason remove check for items from mod stack inventory
     */
    @Overwrite
    public int findSlotMatchingItem(ItemStack p_36031_) {
        for (int i = 0; i < this.items.size(); ++i) {
            if (!this.items.get(i).isEmpty())
                if (p_36031_.getItem() instanceof IItemStackInventory && p_36031_.is(this.items.get(i).getItem()))
                    return i;
                else if (ItemStack.isSameItemSameTags(p_36031_, this.items.get(i))) {
                    return i;
                }
        }
        return -1;
    }

    @Inject(
            method = "findSlotMatchingItem(Lnet/minecraft/world/item/ItemStack;)I",
            at = @At("RETURN"),
            cancellable = true
    )
    private void injectAfterSetPickedItem(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (stack.getItem() instanceof IItemStackInventory) {
            this.items.set(selected, stack);
        }
    }
}
