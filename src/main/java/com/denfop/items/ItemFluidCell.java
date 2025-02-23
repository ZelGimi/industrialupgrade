package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.DispenseFluidContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ItemFluidCell extends ItemFluidContainer {

    public ItemFluidCell() {
        super("fluid_cell", 1000);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseFluidContainer.getInstance());
    }


    public String getUnlocalizedName(ItemStack itemStack) {
        return "iu." + super.getUnlocalizedName().substring(5);

    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(
                this,
                0,
                new ModelResourceLocation(
                        Constants.MOD_ID + ":" + "itemcell" + "/" + "itemcell",
                        null
                )
        );

    }

    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, NBTTagCompound nbt) {
        return new CapabilityFluidHandlerItem(stack, ItemFluidCell.this.capacity) {
            public boolean canFillFluidType(FluidStack fluid) {
                return fluid != null && ItemFluidCell.this.canfill(fluid.getFluid());
            }

            public boolean canDrainFluidType(FluidStack fluid) {
                return fluid != null && ItemFluidCell.this.canfill(fluid.getFluid()) && fluid.amount >= 1000;
            }

            @Override
            public int fill(final FluidStack resource, final boolean doFill) {
                if (resource == null || resource.amount < 1000) {
                    return 0;
                }
                return super.fill(resource, doFill);
            }

            @Override
            public FluidStack drain(final int maxDrain, final boolean doDrain) {
                if (maxDrain < 1000) {
                    return null;
                }
                return super.drain(maxDrain, doDrain);
            }

            @Override
            public FluidStack drain(FluidStack resource, boolean doDrain) {
                if (resource == null || resource.amount < 1000) {
                    return null;
                }
                return super.drain(resource, doDrain);
            }

            @NotNull
            @Override
            public ItemStack getContainer() {
                return getFluid() == null ? new ItemStack(IUItem.fluidCell) : container;
            }
        };
    }

    public boolean tryPlaceContainedLiquid(FluidStack fs, @Nullable EntityPlayer player, World worldIn, BlockPos posIn) {

        Block containedBlock;
        if (fs.getFluid() == FluidRegistry.WATER) {
            containedBlock = Blocks.FLOWING_WATER;
        } else if (fs.getFluid() == FluidRegistry.LAVA) {
            containedBlock = Blocks.FLOWING_LAVA;
        } else {
            containedBlock = fs.getFluid().getBlock();
        }
        if (containedBlock == Blocks.AIR) {
            return false;
        } else {
            IBlockState iblockstate = worldIn.getBlockState(posIn);
            Material material = iblockstate.getMaterial();
            boolean flag = !material.isSolid();
            boolean flag1 = iblockstate.getBlock().isReplaceable(worldIn, posIn);

            if (!worldIn.isAirBlock(posIn) && !flag && !flag1) {
                return false;
            } else {
                if (worldIn.provider.doesWaterVaporize() && containedBlock == Blocks.FLOWING_WATER) {
                    int l = posIn.getX();
                    int i = posIn.getY();
                    int j = posIn.getZ();
                    worldIn.playSound(
                            player,
                            posIn,
                            SoundEvents.BLOCK_FIRE_EXTINGUISH,
                            SoundCategory.BLOCKS,
                            0.5F,
                            2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F
                    );

                    for (int k = 0; k < 8; ++k) {
                        worldIn.spawnParticle(
                                EnumParticleTypes.SMOKE_LARGE,
                                (double) l + Math.random(),
                                (double) i + Math.random(),
                                (double) j + Math.random(),
                                0.0D,
                                0.0D,
                                0.0D
                        );
                    }
                } else {
                    if (!worldIn.isRemote && (flag || flag1) && !material.isLiquid()) {
                        worldIn.destroyBlock(posIn, true);
                    }

                    SoundEvent soundevent = containedBlock == Blocks.FLOWING_LAVA
                            ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA
                            : SoundEvents.ITEM_BUCKET_EMPTY;
                    worldIn.playSound(player, posIn, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    worldIn.setBlockState(posIn, containedBlock.getDefaultState(), 11);
                }
                fs.amount -= 1000;
                return true;
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(
            World world, EntityPlayer player, EnumHand hand
    ) {


        RayTraceResult position = this.rayTrace(world, player, true);
        if (position == null) {
            return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(hand));
        }
        final boolean action = ForgeHooks
                .onRightClickBlock(player, hand, position.getBlockPos(), position.sideHit, position.hitVec)
                .isCanceled();
        if (action) {
            return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(hand));
        }
        if (world.isRemote) {
            return ActionResult.newResult(EnumActionResult.PASS, player.getHeldItem(hand));
        }

        if (position.typeOfHit == RayTraceResult.Type.BLOCK) {
            final BlockPos pos = position.getBlockPos();
            final ItemStack stack = player.getHeldItem(hand);
            final FluidStack fluidStack = FluidUtil.getFluidContained(stack);
            if (fluidStack != null) {
                boolean flag1 = world.getBlockState(pos).getBlock().isReplaceable(world, pos);
                BlockPos blockpos1 = flag1 && position.sideHit == EnumFacing.UP ? pos : pos.offset(position.sideHit);

                if (fluidStack.amount >= 1000 && tryPlaceContainedLiquid(fluidStack, player, world, blockpos1)) {
                    player.getHeldItem(hand).shrink(1);
                    if (!ModUtils.storeInventoryItem(new ItemStack(IUItem.fluidCell, 1),
                            player, false
                    )) {
                        ModUtils.dropAsEntity(world, pos, new ItemStack(IUItem.fluidCell, 1));
                    }
                    return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
                }

            } else {

                if (!world.canMineBlockBody(player, pos)) {
                    return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(hand));
                }

                if (!player.canPlayerEdit(pos, position.sideHit, player.getHeldItem(hand))) {
                    return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(hand));
                }
                IBlockState state = world.getBlockState(pos);
                Block block = state.getBlock();

                if (block instanceof IFluidBlock && ((IFluidBlock) block).canDrain(world, pos)) {


                    if (!world.isRemote) {

                        final ItemStack stack1 = new ItemStack(this);
                        final IFluidHandlerItem fluidDestination = FluidUtil.getFluidHandler(
                                stack1);
                        Fluid liquid = ((IFluidBlock) block).getFluid();
                        final FluidStack drainable = new FluidStack(liquid, 1000);
                        fluidDestination.fill(drainable, true);
                        world.setBlockToAir(pos);
                        stack.shrink(1);
                        if (!player.inventory.addItemStackToInventory(stack1)) {
                            ModUtils.dropAsEntity(world, pos, stack1);

                        }
                    }
                    return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));


                } else if (state.getMaterial().isLiquid()) {

                    if (!world.isRemote) {
                        final FluidStack drainable = new FluidStack(FluidRegistry.getFluid(block
                                .getUnlocalizedName()
                                .substring(5)), 1000);
                        final ItemStack stack1 = new ItemStack(this);
                        final IFluidHandlerItem fluidDestination = FluidUtil.getFluidHandler(
                                stack1);

                        fluidDestination.fill(drainable, true);
                        world.setBlockToAir(pos);
                        stack.shrink(1);
                        if (!player.inventory.addItemStackToInventory(stack1)) {
                            ModUtils.dropAsEntity(world, pos, stack1);

                        }
                    }

                    return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));

                }
            }
        }

        return ActionResult.newResult(EnumActionResult.PASS, player.getHeldItem(hand));

    }


    public boolean isRepairable() {
        return false;
    }


    public boolean canfill(Fluid fluid) {
        return true;
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            ItemStack emptyStack = new ItemStack(this);
            subItems.add(emptyStack);

            for (final Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
                if (fluid != null && fluid.getBlock() != null) {
                    ItemStack stack = this.getItemStack(fluid);
                    if (stack != null) {
                        subItems.add(stack);
                    }
                }
            }

        }
    }


}
