package com.denfop.items;

import com.denfop.IUItem;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ItemFluidCell extends ItemFluidContainer {
    public ItemFluidCell() {
        super(1000);
    }

    public boolean canfill(Fluid fluid) {
        return true;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            p_41392_.addAll(getAllStacks());

        }
    }

    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, CompoundTag nbt) {
        return new CapabilityFluidHandlerItem(stack, 1000) {
            public boolean canFillFluidType(FluidStack fluid) {
                return fluid != null && ItemFluidCell.this.canfill(fluid.getFluid());
            }

            public boolean canDrainFluidType(FluidStack fluid) {
                return fluid != null && ItemFluidCell.this.canfill(fluid.getFluid()) && fluid.getAmount() >= 1000;
            }

            @Override
            public int fill(FluidStack resource, FluidAction doFill) {
                if (resource == null || resource.getAmount() < 1000) {
                    return 0;
                }
                return super.fill(resource, doFill);
            }

            @Override
            public @NotNull FluidStack drain(int maxDrain, FluidAction doDrain) {
                if (maxDrain < 1000) {
                    return FluidStack.EMPTY;
                }
                return super.drain(maxDrain, doDrain);
            }

            @Override
            public @NotNull FluidStack drain(FluidStack resource, FluidAction doDrain) {
                if (resource == FluidStack.EMPTY || resource.getAmount() < 1000) {
                    return FluidStack.EMPTY;
                }
                return super.drain(resource, doDrain);
            }

            @NotNull
            @Override
            public ItemStack getContainer() {
                return getFluid() == FluidStack.EMPTY ? new ItemStack(IUItem.fluidCell.getItem()) : container;
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
                if (fs.getFluidInTank(0).getFluid() != Fluids.EMPTY) {
                    Fluid fluid = fs.getFluidInTank(0).getFluid();
                    boolean flag1 = world.getBlockState(blockpos).getMaterial().isReplaceable();
                    BlockPos blockpos2 = flag1 && blockhitresult.getDirection() == Direction.UP ? blockpos : blockpos.offset(blockhitresult.getDirection().getNormal());
                    if (tryPlaceContainedLiquid(new FluidStack(fluid, 1000), player, world, blockpos2)) {
                        player.getItemInHand(hand).shrink(1);
                        if (!ModUtils.storeInventoryItem(new ItemStack(this, 1),
                                player, false
                        )) {
                            if (!world.isClientSide()) {
                                ModUtils.dropAsEntity(world, blockpos, new ItemStack(this, 1));
                            }
                        }
                        return InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide);
                    }

                } else {
                    if (world.getBlockEntity(blockpos) == null) {
                        BlockState state = world.getBlockState(blockpos);
                        Block block = state.getBlock();
                        if (block instanceof IFluidBlock && ((IFluidBlock) block).canDrain(world, blockpos)) {
                            Fluid liquid = ((IFluidBlock) block).getFluid();
                            ItemStack stack = this.getItemStack(liquid);
                            world.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 11);
                            itemstack.shrink(1);
                            if (!ModUtils.storeInventoryItem(stack, player, false)) {
                                if (!world.isClientSide()) {
                                    ModUtils.dropAsEntity(world, blockpos, stack);
                                }

                            }
                            return InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide);


                        } else if (block == Blocks.WATER || block == Blocks.LAVA) {
                            Fluid liquid;
                            if (block == Blocks.WATER) {
                                liquid = Fluids.WATER;
                            } else {
                                liquid = Fluids.LAVA;
                            }
                            ItemStack stack = this.getItemStack(liquid);
                            world.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 11);
                            itemstack.shrink(1);
                            if (!ModUtils.storeInventoryItem(stack, player, false)) {
                                if (!world.isClientSide()) {
                                    ModUtils.dropAsEntity(world, blockpos, stack);
                                }

                            }
                            return InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide);

                        }
                    } else {
                        BlockEntity blockEntity = world.getBlockEntity(blockpos);
                        if (blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null) != null) {
                            IFluidHandler handler = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
                            if (!handler.drain(1000, IFluidHandler.FluidAction.SIMULATE).isEmpty()) {
                                FluidStack fluidStack = handler.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                                fs.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                            }
                        }
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
            Material material = iblockstate.getMaterial();
            boolean flag = !material.isSolid();
            boolean flag1 = iblockstate.getMaterial().isReplaceable();
            if (material.isLiquid())
                return false;
            if (iblockstate.isAir() && !flag && !flag1) {
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
                    if (!worldIn.isClientSide && (flag || flag1) && !material.isLiquid()) {
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
