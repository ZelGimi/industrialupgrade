package com.denfop.items.energy;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.blocks.FluidName;
import com.denfop.items.CapabilityFluidHandlerItem;
import com.denfop.items.ItemFluidContainer;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemSprayer extends ItemFluidContainer {

    public ItemSprayer() {
        super(8000, 1);
    }

    private static boolean canPlaceFoam(Level world, BlockPos pos, Target target) {
        if (target == Target.Any) {
            return world.getBlockState(pos).getMaterial().isReplaceable();
        } else {
            return false;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, list, tooltipFlag);
        list.add(Component.literal(Localization.translate("iu.sprayer.info")));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide && IUCore.keyboard.isChangeKeyDown(player)) {
            ItemStack stack = player.getItemInHand(hand);
            CompoundTag nbtData = stack.getOrCreateTag();
            int mode = nbtData.getInt("mode");
            mode = mode == 0 ? 1 : 0;
            nbtData.putInt("mode", mode);
            String sMode = mode == 0 ? "iu.tooltip.mode.normal" : "iu.tooltip.mode.single";
            player.displayClientMessage(Component.translatable("iu.tooltip.mode", sMode), true);
        }
        return super.use(world, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction side = context.getClickedFace();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        int maxFoamBlocks = 0;
        IFluidHandlerItem fs = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse((CapabilityFluidHandlerItem) this.initCapabilities(stack, stack.getTag()));
        FluidStack fluid = fs.getFluidInTank(0);
        if (!fluid.isEmpty() && fluid.getAmount() > 0) {
            maxFoamBlocks += fluid.getAmount() / this.getFluidPerFoam();
        }

        if (maxFoamBlocks == 0) {
            return InteractionResult.FAIL;
        }

        maxFoamBlocks = Math.min(maxFoamBlocks, this.getMaxFoamBlocks(stack));
        Target target = canPlaceFoam(world, pos, Target.Scaffold) ? Target.Scaffold : Target.Any;

        if (target == Target.Any) {
            pos = pos.relative(side);
        }

        Vec3 viewVec = player.getLookAngle();
        Direction playerViewFacing = Direction.getNearest(viewVec.x, viewVec.y, viewVec.z);

        int amount = this.sprayFoam(world, pos, playerViewFacing.getOpposite(), target, maxFoamBlocks);
        amount *= this.getFluidPerFoam();

        if (amount > 0) {
            fs.drain(amount, IFluidHandler.FluidAction.EXECUTE);
            player.getInventory().setItem(player.getInventory().selected, fs.getContainer());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public int sprayFoam(Level world, BlockPos pos, Direction excludedDir, Target target, int maxFoamBlocks) {
        if (!canPlaceFoam(world, pos, target)) {
            return 0;
        }

        Queue<BlockPos> toCheck = new ArrayDeque<>();
        Set<BlockPos> positions = new HashSet<>();
        toCheck.add(pos);

        BlockPos cPos;
        while ((cPos = toCheck.poll()) != null && positions.size() < maxFoamBlocks) {
            if (canPlaceFoam(world, cPos, target) && positions.add(cPos)) {
                for (Direction dir : Direction.values()) {
                    if (dir != excludedDir) {
                        toCheck.add(cPos.relative(dir));
                    }
                }
            }
        }

        int failedPlacements = 0;
        for (BlockPos targetPos : positions) {
            if (!world.setBlock(targetPos, IUItem.foam.getDefaultState(), 3)) {
                failedPlacements++;
            }
        }

        return positions.size() - failedPlacements;
    }

    protected int getMaxFoamBlocks(ItemStack stack) {
        CompoundTag nbtData = stack.getOrCreateTag();
        return nbtData.getInt("mode") == 0 ? 10 : 1;
    }

    protected int getFluidPerFoam() {
        return 100;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            p_41392_.add(new ItemStack(this));
            p_41392_.add(this.getItemStack(FluidName.fluidconstruction_foam.getInstance().get()));
        }
    }

    public boolean canfill(Fluid fluid) {
        return fluid == FluidName.fluidconstruction_foam.getInstance().get();
    }


    private enum Target {
        Any,
        Scaffold;

        Target() {
        }
    }


}
