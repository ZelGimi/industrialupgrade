package com.denfop.blockentity.smeltery;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockSmelteryEntity;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSmelteryFuelTank;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSmelteryFuelTank;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class BlockEntitySmelteryFuelTank extends BlockEntityMultiBlockElement implements IFuelTank {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;
    private double speed;

    public BlockEntitySmelteryFuelTank(BlockPos pos, BlockState state) {
        super(BlockSmelteryEntity.smeltery_fuel_tank, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluids", 10000);
        this.fluidTank.setAcceptedFluids(Fluids.fluidPredicate(net.minecraft.world.level.material.Fluids.LAVA, FluidName.fluidpahoehoe_lava.getInstance().get()));
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.getFluidHandler(player.getItemInHand(hand)) != null) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(Capabilities.FluidHandler.BLOCK, side)
            );
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (!this.fluidTank.getFluid().isEmpty() && this.fluidTank
                .getFluid()
                .getFluid()
                .equals(FluidName.fluidpahoehoe_lava.getInstance().get())) {
            speed = 2;
        } else {
            speed = 1;
        }
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerMenuSmelteryFuelTank getGuiContainer(final Player var1) {
        return new ContainerMenuSmelteryFuelTank(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenSmelteryFuelTank((ContainerMenuSmelteryFuelTank) menu);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSmelteryEntity.smeltery_fuel_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.smeltery.getBlock(getTeBlock());
    }

    @Override
    public FluidTank getFuelTank() {
        return fluidTank;
    }

    @Override
    public double getSpeed() {
        return this.speed;
    }

}
