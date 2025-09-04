package com.denfop.blockentity.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.base.BlockEntityElectricLiquidTankInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.componets.PressureComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSteamPump;
import com.denfop.inventory.Inventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSteamPump;
import com.denfop.sound.EnumSound;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.Keyboard;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

public class BlockEntitySteamPump extends BlockEntityElectricLiquidTankInventory {

    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final PressureComponent pressure;
    public final ComponentSteamEnergy steam;
    private final Fluids.InternalFluidTank fluidTank1;
    public double energyConsume;
    public int operationsPerTick;
    public int operationLength;
    public ComponentProgress componentProgress;

    public BlockEntitySteamPump(BlockPos pos, BlockState state) {
        super(0, 1, 10, BlockBaseMachine3Entity.steam_pump, pos, state);
        this.defaultEnergyConsume = this.energyConsume = 2;
        this.defaultOperationLength = this.operationLength = 25;
        this.defaultTier = 1;
        this.defaultEnergyStorage = this.operationLength;
        componentProgress = this.addComponent(new ComponentProgress(this, 1, (short) operationLength));

        this.fluidTank.setTypeItemSlot(Inventory.TypeItemSlot.OUTPUT);
        this.fluidTank1 = fluids.addTank("fluidTank2", 4000, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance().get()
        ), Inventory.TypeItemSlot.NONE);
        this.pressure = this.addComponent(PressureComponent.asBasicSink(this, 1));
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSink(this, 4000));
        this.steam.setFluidTank(fluidTank1);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.steam_pump;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);


    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();

        return packet;
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.defaultEnergyConsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.defaultOperationLength);
        }
        super.addInformation(stack, tooltip);

    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.steam.canUseEnergy((this.energyConsume))) {

            if (componentProgress.getProgress() < componentProgress.getMaxValue()) {
                componentProgress.addProgress(0);
                this.steam.useEnergy(energyConsume);
            } else {
                if (this.canoperate()) {
                    componentProgress.setProgress((short) 0);
                }
            }
        }


    }


    public boolean canoperate() {
        return this.operate(true);
    }

    public boolean operate(boolean sim) {
        if (this.fluidTank.getFluidAmount() >= this.fluidTank.getCapacity()) {
            return false;
        }
        FluidStack liquid = FluidStack.EMPTY;
        boolean canOperate = false;
        for (int i = this.pos.getX() - 1; i <= this.pos.getX() + 1; i++) {
            for (int j = this.pos.getZ() - 1; j <= this.pos.getZ() + 1; j++) {
                for (int k = this.pos.getY() - 3; k <= this.pos.getY() - 1; k++) {

                    if (this.fluidTank.getFluidAmount() >= this.fluidTank.getCapacity()) {
                        return false;
                    }
                    liquid = this.pump(new BlockPos(i, k,
                            j
                    ), false);
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
        FluidStack ret = FluidStack.EMPTY;
        int freespace = this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount();

        if (freespace >= 1000) {
            BlockState block = this.getWorld().getBlockState(pos);
            if (block.getMaterial().isLiquid()) {
                FluidState fluidState = block.getBlock().getFluidState(block);

                if (!fluidState.isSource()) {
                    return FluidStack.EMPTY;
                }

                ret = new FluidStack(fluidState.getType(), 1000);
                if (this.fluidTank.getFluid().isEmpty() || this.fluidTank
                        .getFluid()
                        .getFluid() == ret.getFluid()) {
                    if (!sim) {
                        this.getWorld().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
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

    public ContainerMenuSteamPump getGuiContainer(Player entityPlayer) {
        return new ContainerMenuSteamPump(entityPlayer, this);
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.hasFluidHandler(player.getItemInHand(hand))) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(ForgeCapabilities.FLUID_HANDLER, side)
            );
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenSteamPump((ContainerMenuSteamPump) menu);
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.PumpOp.getSoundEvent();
    }

}
