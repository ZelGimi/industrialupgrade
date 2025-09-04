package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityElectricLiquidTankInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockPrimalPumpEntity;
import com.denfop.componets.ComponentProgress;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.sound.EnumSound;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.List;

public class BlockEntityPrimalPump extends BlockEntityElectricLiquidTankInventory {


    public ComponentProgress componentProgress;
    private int prevAmount;

    public BlockEntityPrimalPump(BlockPos pos, BlockState state) {
        super(0, 1, 4, BlockPrimalPumpEntity.primal_pump, pos, state);
        componentProgress = this.addComponent(new ComponentProgress(this, 1, (short) 25));
        this.fluidTank.setTypeItemSlot(Inventory.TypeItemSlot.OUTPUT);
    }


    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockPrimalPumpEntity.primal_pump;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.primal_pump.getBlock();
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);


    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank1 != null) {
                this.fluidTank.readFromNBT(fluidTank1.writeToNBT(new CompoundTag()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("fluidtank")) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank1 != null) {
                    this.fluidTank.readFromNBT(fluidTank1.writeToNBT(new CompoundTag()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public boolean onSneakingActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && this.fluidTank.getFluidAmount() + 1000 <= this.fluidTank.getCapacity()) {
            if (componentProgress.getProgress() < componentProgress.getMaxValue()) {
                componentProgress.addProgress(0, (short) 4);
            } else {
                if (this.canoperate()) {
                    componentProgress.setProgress((short) 0);
                }
            }
            this.setActive(true);
        } else {
            this.setActive(false);
        }
        return super.onSneakingActivated(player, hand, side, vec3);
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.getFluidHandler(player.getItemInHand(hand)) != null) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(ForgeCapabilities.FLUID_HANDLER, side)
            );
        } else {

            return super.onActivated(player, hand, side, vec3);
        }
    }


    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();

        return packet;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        for (int i = 1; i < 6; i++) {
            tooltip.add(Localization.translate("primitive_pump.info" + i));
        }
    }


    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = super.writePacket();
        try {
            EncoderHandler.encode(customPacketBuffer, fluidTank);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.prevAmount != this.fluidTank.getFluidAmount()) {
            this.prevAmount = this.fluidTank.getFluidAmount();
            new PacketUpdateFieldTile(this, "fluidtank", this.fluidTank);
        }
    }

    public boolean canoperate() {
        return this.operate(true);
    }

    public boolean operate(boolean sim) {
        if (this.fluidTank.getFluidAmount() >= this.fluidTank.getCapacity()) {
            return false;
        }
        FluidStack liquid;
        boolean canOperate = false;
        for (int i = this.pos.getX() - 1; i <= this.pos.getX() + 1; i++) {
            for (int j = this.pos.getZ() - 1; j <= this.pos.getZ() + 1; j++) {
                for (int k = this.pos.getY() - 1; k <= this.pos.getY() - 1; k++) {

                    if (this.fluidTank.getFluidAmount() >= this.fluidTank.getCapacity()) {
                        return false;
                    }
                    liquid = this.pump(new BlockPos(i, k,
                            j
                    ), false);
                    if (liquid == null || liquid.isEmpty())
                        continue;

                    if (this.getFluidTank().fill(liquid, IFluidHandler.FluidAction.SIMULATE) > 0) {
                        this.getFluidTank().fill(liquid, IFluidHandler.FluidAction.EXECUTE);
                        canOperate = true;
                    }
                }

            }
        }


        return canOperate;
    }

    public FluidStack pump(BlockPos pos, boolean sim) {
        FluidStack ret = null;
        int freespace = this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount();

        if (freespace >= 1000) {
            BlockState block = this.getWorld().getBlockState(pos);
            if (block.liquid()) {


                if (block.getBlock() instanceof IFluidBlock) {
                    IFluidBlock liquid = (IFluidBlock) block.getBlock();
                    if ((this.fluidTank.getFluid().isEmpty() || this.fluidTank
                            .getFluid()
                            .getFluid() == liquid.getFluid()) && liquid.canDrain(
                            this.getWorld(),
                            pos
                    )) {
                        if (!sim) {
                            ret = liquid.drain(this.getWorld(), pos, IFluidHandler.FluidAction.EXECUTE);
                            this.getWorld().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                        } else {
                            ret = new FluidStack(liquid.getFluid(), 1000);
                        }
                    }
                } else {
                    if (!block.getBlock().getFluidState(block).isSource()) {
                        return FluidStack.EMPTY;
                    }

                    ret = new FluidStack(block.getBlock().getFluidState(block).getType(), 1000);
                    if (this.fluidTank.getFluid().isEmpty() || this.fluidTank
                            .getFluid()
                            .getFluid() == ret.getFluid()) {
                        if (!sim) {
                            this.getWorld().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }


        return ret;
    }


    public void onLoaded() {
        super.onLoaded();


    }


    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        if (this.energy.canUseEnergy(amount)) {
            this.energy.useEnergy(amount);
            return true;
        } else {
            return false;
        }
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.PumpOp.getSoundEvent();
    }

}
