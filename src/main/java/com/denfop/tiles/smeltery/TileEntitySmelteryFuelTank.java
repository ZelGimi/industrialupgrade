package com.denfop.tiles.smeltery;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockSmeltery;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSmelteryFuelTank;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSmelteryFuelTank;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
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

public class TileEntitySmelteryFuelTank extends TileEntityMultiBlockElement implements IFuelTank {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;
    private double speed;

    public TileEntitySmelteryFuelTank(BlockPos pos, BlockState state) {
        super(BlockSmeltery.smeltery_fuel_tank, pos, state);
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
    public ContainerSmelteryFuelTank getGuiContainer(final Player var1) {
        return new ContainerSmelteryFuelTank(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiSmelteryFuelTank((ContainerSmelteryFuelTank) menu);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSmeltery.smeltery_fuel_tank;
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
