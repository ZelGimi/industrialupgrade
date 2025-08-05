package com.denfop.items;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.KeyboardIU;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemReinforcedFluidCell extends ItemFluidContainer implements IItemTab {
    public ItemReinforcedFluidCell() {
        super(new Properties().setNoRepair().stacksTo(1),10000);
    }

    public boolean canfill(Fluid fluid) {
        return true;
    }
    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.fluidCellTab;
    }
    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            p_41392_.addAll(getAllStacks());

        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, list, tooltipFlag);

    }

    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, CompoundTag nbt) {
        return new CapabilityFluidHandlerItem(stack, 10000) {
            public boolean canFillFluidType(FluidStack fluid) {
                return fluid != null && ItemReinforcedFluidCell.this.canfill(fluid.getFluid());
            }

            public boolean canDrainFluidType(FluidStack fluid) {
                return fluid != null && ItemReinforcedFluidCell.this.canfill(fluid.getFluid());
            }

            @Override
            public int fill(FluidStack resource, FluidAction doFill) {
                if (resource == null) {
                    return 0;
                }
                return super.fill(resource, doFill);
            }

            @Override
            public @NotNull FluidStack drain(int maxDrain, FluidAction doDrain) {

                return super.drain(maxDrain, doDrain);
            }

            @Override
            public @NotNull FluidStack drain(FluidStack resource, FluidAction doDrain) {
                if (resource == FluidStack.EMPTY) {
                    return FluidStack.EMPTY;
                }
                return super.drain(resource, doDrain);
            }

            @NotNull
            @Override
            public ItemStack getContainer() {
                return getFluid() == FluidStack.EMPTY ? new ItemStack(container.getItem()) : container;
            }
        };
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {

        ItemStack itemstack = player.getItemInHand(hand);
        IFluidHandlerItem fs = itemstack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
        if (fs == null)
            fs = (IFluidHandlerItem) initCapabilities(itemstack,itemstack.getOrCreateTag());
        BlockHitResult blockhitresult = getPlayerPOVHitResult(world, player, fs.getFluidInTank(0).getFluid() == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            BlockPos blockpos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos blockpos1 = blockpos.relative(direction);
            if (world.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos1, direction, itemstack)) {
                if (fs.getFluidInTank(0).getFluid() != Fluids.EMPTY && fs.getFluidInTank(0).getAmount() >= 1000) {
                    Fluid fluid = fs.getFluidInTank(0).getFluid();
                    boolean flag1 = world.getBlockState(blockpos).canBeReplaced();
                    BlockPos blockpos2 = flag1 && blockhitresult.getDirection() == Direction.UP ? blockpos : blockpos.offset(blockhitresult.getDirection().getNormal());
                    if (tryPlaceContainedLiquid(new FluidStack(fluid, 1000), player, world, blockpos2)) {
                        fs.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                        return InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide);
                    }

                } else {
                    BlockState block = world.getBlockState(blockpos);
                    if (block.liquid()) {
                        FluidState fluidState = block.getBlock().getFluidState(block);

                        if (!fluidState.isSource()) {
                            return InteractionResultHolder.fail(itemstack);
                        }

                        FluidStack ret = new FluidStack(fluidState.getType(), 1000);
                        ItemStack stack = new ItemStack(this);
                        FluidHandlerFix.getFluidHandler(stack).fill(ret, IFluidHandler.FluidAction.EXECUTE);

                        stack = stack.copy();
                        if (!ModUtils.storeInventoryItem(stack, player, false)) {

                            if (!world.isClientSide()) {
                                ModUtils.dropAsEntity(world, player.blockPosition(), stack);
                            }

                        }
                        world.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 3);
                        itemstack.shrink(1);
                        return InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide);
                    }
                }

            }
        }
        return InteractionResultHolder.fail(itemstack);
    }


    public boolean tryPlaceContainedLiquid(FluidStack fs, @Nullable Player player, Level worldIn, BlockPos posIn) {

        Block containedBlock;
        if (fs.getFluid() == Fluids.WATER) {
            containedBlock = Blocks.WATER;
        } else if (fs.getFluid() == Fluids.LAVA) {
            containedBlock = Blocks.LAVA;
        } else {
            containedBlock = fs.getFluid().defaultFluidState().createLegacyBlock().getBlock();
        }
        if (containedBlock == Blocks.AIR) {
            return false;
        } else {
            BlockState iblockstate = worldIn.getBlockState(posIn);
            boolean flag1 = iblockstate.canBeReplaced();
            if (iblockstate.liquid())
                return false;
            if (!iblockstate.isAir() && !flag1) {
                return false;
            } else {
                if (worldIn.dimension() == Level.NETHER && containedBlock == Blocks.WATER) {
                    int l = posIn.getX();
                    int i = posIn.getY();
                    int j = posIn.getZ();
                    worldIn.playSound(
                            player,
                            posIn,
                            SoundEvents.FIRE_EXTINGUISH,
                            SoundSource.BLOCKS,
                            0.5F,
                            2.6F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.8F
                    );

                    for (int k = 0; k < 8; ++k) {
                        worldIn.addParticle(
                                ParticleTypes.LARGE_SMOKE,
                                (double) l + Math.random(),
                                (double) i + Math.random(),
                                (double) j + Math.random(),
                                0.0D,
                                0.0D,
                                0.0D
                        );
                    }
                } else {
                    if (!worldIn.isClientSide && ( flag1) && !iblockstate.liquid()) {
                        worldIn.destroyBlock(posIn, true);
                    }

                    SoundEvent soundevent = containedBlock == Blocks.LAVA
                            ? SoundEvents.BUCKET_EMPTY_LAVA
                            : SoundEvents.BUCKET_EMPTY;
                    worldIn.playSound(player, posIn, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
                    worldIn.setBlock(posIn, fs.getFluid().defaultFluidState().createLegacyBlock(), 11);
                }
                fs.grow(-1000);
                ;
                return true;
            }
        }
    }
}
