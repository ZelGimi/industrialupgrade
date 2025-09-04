package com.denfop.items.energy;

import com.denfop.IUItem;
import com.denfop.utils.ElectricItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ItemTreetapEnergy extends ItemEnergyTool {
    public ItemTreetapEnergy() {
        super(50);
        this.maxCharge = 10000;
        this.transferLimit = 100;
        this.tier = 1;
    }

    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block == IUItem.rubWood.getBlock().get() && ElectricItem.manager.canUse(context.getItemInHand(), this.operationEnergyCost)) {
            if (ItemTreetap.attemptExtract(context.getPlayer(), world, pos, context.getClickedFace(), state, null)) {
                if (!world.isClientSide) {
                    ElectricItem.manager.use(context.getItemInHand(), this.operationEnergyCost, context.getPlayer());
                }

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        } else if (block == IUItem.tropicalRubWood.getBlock().get() && ElectricItem.manager.canUse(context.getItemInHand(), this.operationEnergyCost)) {
            if (ItemTreetap.attemptTropicalExtract(context.getPlayer(), world, pos, context.getClickedFace(), state, null)) {
                if (!world.isClientSide) {
                    ElectricItem.manager.use(context.getItemInHand(), this.operationEnergyCost, context.getPlayer());
                }

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        } else if (block == IUItem.swampRubWood.getBlock().get() && ElectricItem.manager.canUse(context.getItemInHand(), this.operationEnergyCost)) {
            if (ItemTreetap.attemptSwampExtract(context.getPlayer(), world, pos, context.getClickedFace(), state, null)) {
                if (!world.isClientSide) {
                    ElectricItem.manager.use(context.getItemInHand(), this.operationEnergyCost, context.getPlayer());
                }

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return InteractionResult.PASS;
        }
    }
}
