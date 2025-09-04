package com.denfop.mixin;

import com.denfop.blockentity.base.BlockEntityBase;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {

    @Inject(
            method = "saveWithFullMetadata",
            at = @At("RETURN")
    )
    private void onSaveWithFullMetadata(HolderLookup.Provider provider,
                                        CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag tag = cir.getReturnValue();
        if ((Object) this instanceof BlockEntityBase myTile) {
            myTile.writeToNBT(tag);
        }
    }
}
