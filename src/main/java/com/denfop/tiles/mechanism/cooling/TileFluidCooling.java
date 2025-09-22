package com.denfop.tiles.mechanism.cooling;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.CoolComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.client.ComponentClientEffectRender;
import com.denfop.componets.client.EffectType;
import com.denfop.container.ContainerFluidCoolMachine;
import com.denfop.gui.GuiFluidCoolMachine;
import com.denfop.invslot.InventoryFluidByList;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TileFluidCooling extends TileElectricMachine implements IUpdatableTileEvent {


    public final Fluids.InternalFluidTank tank;
    public final InventoryFluidByList fluidSlot1;
    private final Fluids fluid;
    public CoolComponent cold;
    public int max;
    public boolean work;

    public TileFluidCooling() {
        super(0, 0, 1);
        this.cold = this.addComponent(CoolComponent.asBasicSource(this, 4, 14));
        this.max = 4;
        this.componentClientEffectRender = new ComponentClientEffectRender(this, EffectType.REFRIGERATOR);
        this.fluid = this.addComponent(new Fluids(this));
        this.tank = this.fluid.addTankInsert("insert", 5000, Fluids.fluidPredicate(FluidName.fluidazot.getInstance(),
                FluidName.fluidhyd.getInstance(), FluidName.fluidHelium.getInstance()
        ));
        this.fluidSlot1 = new InventoryFluidByList(this, 1, Arrays.asList(FluidName.fluidazot.getInstance(),
                FluidName.fluidhyd.getInstance(), FluidName.fluidHelium.getInstance()
        ));

    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
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

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.fluid_cooling;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.max = nbttagcompound.getInteger("max");
        this.work = nbttagcompound.getBoolean("work");
        this.cold.setCapacity(this.max);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("max", this.max);
        nbttagcompound.setBoolean("work", this.work);
        return nbttagcompound;

    }


    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
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
    @SideOnly(Side.CLIENT)
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
        if (fluidStack != null) {
            if (fluidStack.getFluid() == FluidName.fluidhyd.getInstance()) {
                coef = max / 4;
            } else if (fluidStack.getFluid() == FluidName.fluidazot.getInstance()) {
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
        if (this.cold.allow || work) {
            if (this.tank.getFluidAmount() >= coef && this.cold.getEnergy() < this.cold.getCapacity()) {
                this.cold.addEnergy(1);
                this.tank.drain(coef, true);
                initiate(0);
                this.setActive(true);

            }
            if (this.world.provider.getWorldTime() % 400 == 0) {
                initiate(2);
            }

            if (this.tank.getFluidAmount() < coef) {
                initiate(2);
                this.setActive(false);
            } else {
                initiate(0);
            }

        } else {
            initiate(2);
            this.setActive(false);
        }
        if (this.world.provider.getWorldTime() % time == 0 && this.cold.getEnergy() >= 1) {
            this.cold.addEnergy(-1);
        }
    }


    @Override
    public ContainerFluidCoolMachine getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerFluidCoolMachine(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiFluidCoolMachine(getGuiContainer(entityPlayer));
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.cooling.getSoundEvent();
    }

}
