package com.denfop.items;


import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockRubWood;
import com.denfop.blocks.BlockSwampRubWood;
import com.denfop.blocks.BlockTropicalRubWood;
import com.denfop.blocks.FluidName;
import com.denfop.utils.FluidHandlerFix;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemLatexPipette extends ItemFluidContainer{

    public ItemLatexPipette() {
        super(100*20, 1);

    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, list, tooltipFlag);
        list.add(Component.literal(Localization.translate("iu.latex_pipette.info")));
    }



    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            p_41392_.add(new ItemStack(this));
        }
    }
    public static boolean attemptExtract(
            Player player,
            Level world,
            BlockPos pos,
            Direction side,
            BlockState state,
            List<ItemStack> stacks,
            ItemStack stack) {
        if (state.getBlock() != IUItem.rubWood.getBlock().get()) return false;

        BlockRubWood.RubberWoodState rwState = state.getValue(BlockRubWood.stateProperty);

        if (!rwState.isPlain() && rwState.facing == side && FluidHandlerFix.hasFluidHandler(stack) && FluidHandlerFix.getFluidHandler(stack).getFluidInTank(0).getAmount() + 100 <=  FluidHandlerFix.getFluidHandler(stack).getTankCapacity(0)) {
            if (rwState.wet) {
                if (!world.isClientSide) {
                    world.setBlock(pos, state.setValue(BlockRubWood.stateProperty, rwState.getDry()), 3);
                    if (stacks == null) {
                        FluidHandlerFix.getFluidHandler(stack).fill(new FluidStack(FluidName.fluidrawlatex.getInstance().get(),100*( world.random.nextInt(3) + 1)), IFluidHandler.FluidAction.EXECUTE);
                    }
                }
                if (world.isClientSide && player != null) {
                    player.playSound(EnumSound.Treetap.getSoundEvent(), 1F, 1F);
                }
                return true;
            } else {
                if (!world.isClientSide && world.random.nextInt(5) == 0) {
                    world.setBlock(pos, state.setValue(BlockRubWood.stateProperty, BlockRubWood.RubberWoodState.plain_y), 3);
                }

                if (world.random.nextInt(5) == 0) {
                    if (!world.isClientSide) {
                        FluidHandlerFix.getFluidHandler(stack).fill(new FluidStack(FluidName.fluidrawlatex.getInstance().get(),100), IFluidHandler.FluidAction.EXECUTE);
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public static boolean attemptTropicalExtract(
            Player player,
            Level world,
            BlockPos pos,
            Direction side,
            BlockState state,
            List<ItemStack> stacks,
            ItemStack stack
    ) {
        if (state.getBlock() != IUItem.tropicalRubWood.getBlock().get()) return false;

        BlockTropicalRubWood.RubberWoodState rwState = state.getValue(BlockTropicalRubWood.stateProperty);
        if (!rwState.isPlain() && rwState.facing == side && FluidHandlerFix.hasFluidHandler(stack) && FluidHandlerFix.getFluidHandler(stack).getFluidInTank(0).getAmount() + 100 <=  FluidHandlerFix.getFluidHandler(stack).getTankCapacity(0)) {
            if (rwState.wet) {
                if (!world.isClientSide) {
                    world.setBlock(pos, state.setValue(BlockTropicalRubWood.stateProperty, rwState.getDry()), 3);
                    if (stacks == null) {
                        FluidHandlerFix.getFluidHandler(stack).fill(new FluidStack(FluidName.fluidrawlatex.getInstance().get(),100*( world.random.nextInt(3) + 1)), IFluidHandler.FluidAction.EXECUTE);
                    }
                }
                if (world.isClientSide && player != null) {
                    player.playSound(EnumSound.Treetap.getSoundEvent(), 1F, 1F);
                }
                return true;
            } else {
                if (!world.isClientSide && world.random.nextInt(5) == 0) {
                    world.setBlock(pos, state.setValue(BlockTropicalRubWood.stateProperty, BlockTropicalRubWood.RubberWoodState.plain_y), 3);
                }

                if (world.random.nextInt(5) == 0) {
                    if (!world.isClientSide) {
                        FluidHandlerFix.getFluidHandler(stack).fill(new FluidStack(FluidName.fluidrawlatex.getInstance().get(),100), IFluidHandler.FluidAction.EXECUTE);
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public static boolean attemptSwampExtract(
            Player player,
            Level world,
            BlockPos pos,
            Direction side,
            BlockState state,
            List<ItemStack> stacks,
            ItemStack stack
    ) {
        if (state.getBlock() != IUItem.swampRubWood.getBlock().get()) return false;

        BlockSwampRubWood.RubberWoodState rwState = state.getValue(BlockSwampRubWood.stateProperty);
        if (!rwState.isPlain() && rwState.facing == side && FluidHandlerFix.hasFluidHandler(stack) && FluidHandlerFix.getFluidHandler(stack).getFluidInTank(0).getAmount() + 100 <=  FluidHandlerFix.getFluidHandler(stack).getTankCapacity(0)) {
            if (rwState.wet) {
                if (!world.isClientSide) {
                    world.setBlock(pos, state.setValue(BlockSwampRubWood.stateProperty, rwState.getDry()), 3);
                    if (stacks == null) {
                        FluidHandlerFix.getFluidHandler(stack).fill(new FluidStack(FluidName.fluidrawlatex.getInstance().get(),100*( world.random.nextInt(3) + 1)), IFluidHandler.FluidAction.EXECUTE);
                    }
                }
                if (world.isClientSide && player != null) {
                    player.playSound(EnumSound.Treetap.getSoundEvent(), 1F, 1F);
                }
                return true;
            } else {
                if (!world.isClientSide && world.random.nextInt(5) == 0) {
                    world.setBlock(pos, state.setValue(BlockSwampRubWood.stateProperty, BlockSwampRubWood.RubberWoodState.plain_y), 3);
                }

                if (world.random.nextInt(5) == 0) {
                    if (!world.isClientSide) {
                        FluidHandlerFix.getFluidHandler(stack).fill(new FluidStack(FluidName.fluidrawlatex.getInstance().get(),100), IFluidHandler.FluidAction.EXECUTE);
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public InteractionResult useOn(
            UseOnContext context
    ) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction side = context.getClickedFace();
        BlockState state = world.getBlockState(pos);
        ItemStack stack =  context.getItemInHand();
        if (state.getBlock() == IUItem.rubWood.getBlock().get()) {
            if (attemptExtract(player, world, pos, side, state, null,stack)) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }
        if (state.getBlock() == IUItem.swampRubWood.getBlock().get()) {
            if (attemptSwampExtract(player, world, pos, side, state, null,stack)) {

                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }
        if (state.getBlock() == IUItem.tropicalRubWood.getBlock().get()) {
            if (attemptTropicalExtract(player, world, pos, side, state, null,stack)) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }




    public boolean canfill(Fluid fluid) {
        return fluid == FluidName.fluidrawlatex.getInstance().get();
    }



}
