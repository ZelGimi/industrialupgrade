package com.denfop.blockentity.mechanism.cooling;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.CoolComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.client.ComponentClientEffectRender;
import com.denfop.componets.client.EffectType;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuFluidCoolMachine;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenFluidCoolMachine;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BlockEntityFluidCooling extends BlockEntityElectricMachine implements IUpdatableTileEvent {


    public final Fluids.InternalFluidTank tank;
    public final InventoryFluidByList fluidSlot1;
    private final Fluids fluid;
    public CoolComponent cold;
    public int max;
    public boolean work;

    public BlockEntityFluidCooling(BlockPos pos, BlockState state) {
        super(0, 0, 1, BlockBaseMachine3Entity.fluid_cooling, pos, state);
        this.cold = this.addComponent(CoolComponent.asBasicSource(this, 4, 14));
        this.max = 4;
        this.componentClientEffectRender = new ComponentClientEffectRender(this, EffectType.REFRIGERATOR);
        this.fluid = this.addComponent(new Fluids(this));
        this.tank = this.fluid.addTankInsert("insert", 5000, Fluids.fluidPredicate(FluidName.fluidnitrogen.getInstance().get(),
                FluidName.fluidhydrogen.getInstance().get(), FluidName.fluidhelium.getInstance().get()
        ));
        this.fluidSlot1 = new InventoryFluidByList(this, 1, Arrays.asList(FluidName.fluidnitrogen.getInstance().get(),
                FluidName.fluidhydrogen.getInstance().get(), FluidName.fluidhelium.getInstance().get()
        ));

    }


    @Override
    public boolean needUpdate() {
        return true;
    }

    @Override
    public CustomPacketBuffer writeUpdatePacket() {
        final CustomPacketBuffer packet = super.writeUpdatePacket();
        try {
            EncoderHandler.encode(packet, cold, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void readUpdatePacket(final CustomPacketBuffer customPacketBuffer) {
        super.readUpdatePacket(customPacketBuffer);
        try {
            cold.onNetworkUpdate(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            max = (int) DecoderHandler.decode(customPacketBuffer);
            work = (boolean) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, max);
            EncoderHandler.encode(packet, work);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, cold, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            cold.onNetworkUpdate(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.fluid_cooling;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.max = nbttagcompound.getInt("max");
        this.work = nbttagcompound.getBoolean("work");
        this.cold.setCapacity(this.max);
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putInt("max", this.max);
        nbttagcompound.putBoolean("work", this.work);
        return nbttagcompound;

    }


    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {
        if (i == 0) {
            this.cold.setCapacity(this.max + 4);
            if (this.cold.getCapacity() > 16) {
                this.cold.setCapacity(16);

            }
            this.max = (int) this.cold.getCapacity();
        }
        if (i == 1) {
            this.cold.setCapacity(this.max - 4);
            if (this.cold.getCapacity() < 4) {
                this.cold.setCapacity(4);

            }
            this.max = (int) this.cold.getCapacity();
        }
        if (i == 2) {
            this.work = !this.work;
        }
        if (i == 10) {
            super.updateTileServer(entityPlayer, i);
        }
    }


    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }

        tooltip.add(Localization.translate("iu.fluid_colling"));
        super.addInformation(stack, tooltip);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();

    }

    public void updateEntityServer() {
        super.updateEntityServer();
        int coef = 1;
        FluidStack fluidStack = tank.getFluid();
        int time = 20;
        if (!fluidStack.isEmpty()) {
            if (fluidStack.getFluid() == FluidName.fluidhydrogen.getInstance().get()) {
                coef = max / 4;
            } else if (fluidStack.getFluid() == FluidName.fluidnitrogen.getInstance().get()) {
                coef = (max / 8) + 1;
                time = 40;
            } else {
                time = 60;
            }
        }
        MutableObject<ItemStack> output1 = new MutableObject<>();
        if (this.tank.getCapacity() - this.tank.getFluidAmount() >= 1000 && this.fluidSlot1.transferToTank(
                this.tank,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot.canAdd(output1.getValue()))) {
            this.fluidSlot1.transferToTank(this.tank, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot.add(output1.getValue());
            }
        }
        if (this.cold.buffer.allow || work) {
            if (!this.tank.getFluid().isEmpty() && this.tank.getFluidAmount() >= coef && this.cold.getEnergy() < this.cold.getCapacity()) {
                this.cold.addEnergy(1);
                this.tank.drain(coef, IFluidHandler.FluidAction.EXECUTE);
                initiate(0);
                this.setActive(true);

            }
            if (this.level.getGameTime() % 400 == 0) {
                initiate(2);
            }

            if (this.tank.getFluid().isEmpty() || this.tank.getFluidAmount() < coef) {
                initiate(2);
                this.setActive(false);
            } else {
                initiate(0);
            }

        } else {
            initiate(2);
            this.setActive(false);
        }
        if (this.level.getGameTime() % time == 0 && this.cold.getEnergy() >= 1) {
            this.cold.addEnergy(-1);
        }
    }


    @Override
    public ContainerMenuFluidCoolMachine getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuFluidCoolMachine(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenFluidCoolMachine((ContainerMenuFluidCoolMachine) menu);
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.cooling.getSoundEvent();
    }

}
