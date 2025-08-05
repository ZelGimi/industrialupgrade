package com.denfop.tiles.base;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.FluidName;
import com.denfop.componets.Fluids;
import com.denfop.componets.HeatComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerHeatMachine;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiHeatMachine;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;

public class TileBaseHeatMachine extends TileElectricMachine implements IUpdatableTileEvent {


    public final boolean hasFluid;
    public final HeatComponent heat;
    public short maxtemperature;
    public boolean auto;
    public FluidTank fluidTank;
    public Fluids fluids = null;
    public InvSlotFluid fluidSlot;
    public int coef = 1;
    public boolean work = true;

    public TileBaseHeatMachine(boolean hasFluid, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(hasFluid ? 0D : 10000D, 14, 1, block, pos, state);
        this.hasFluid = hasFluid;
        if (this.hasFluid) {
            this.fluids = this.addComponent(new Fluids(this));
            this.fluidTank = this.fluids.addTank("fluidTank", 12000, InvSlot.TypeItemSlot.INPUT, Fluids.fluidPredicate(
                    FluidName.fluidpahoehoe_lava.getInstance().get(),
                    net.minecraft.world.level.material.Fluids.LAVA, FluidName.fluiddizel.getInstance().get(),
                    FluidName.fluidbenz.getInstance().get(),
                    FluidName.fluidbiogas.getInstance().get(),
                    FluidName.fluidbiomass.getInstance().get()
            ));
            this.fluidSlot = new InvSlotFluidByList(
                    this,
                    InvSlot.TypeItemSlot.INPUT,
                    1,

                    InvSlotFluid.TypeFluidSlot.INPUT,
                    FluidName.fluidpahoehoe_lava.getInstance().get(),
                    net.minecraft.world.level.material.Fluids.LAVA, FluidName.fluiddizel.getInstance().get(),
                    FluidName.fluidbenz.getInstance().get(),
                    FluidName.fluidbiogas.getInstance().get(),
                    FluidName.fluidbiomass.getInstance().get()
            );
        }

        this.maxtemperature = 1000;
        this.heat = this.addComponent(HeatComponent
                .asBasicSource(this, 1000));
        this.auto = false;

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            auto = (boolean) DecoderHandler.decode(customPacketBuffer);
            maxtemperature = (short) DecoderHandler.decode(customPacketBuffer);
            work = (boolean) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, auto);
            EncoderHandler.encode(packet, maxtemperature);
            EncoderHandler.encode(packet, work);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.coef = (int) Math.max(Math.ceil(this.heat.storage / 2000), 1);
    }

    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {
        if (i == 0) {
            this.maxtemperature = (short) (this.maxtemperature + 1000);
            if (this.maxtemperature > 10000) {
                this.maxtemperature = 10000;
            }
            this.heat.setCapacity(this.maxtemperature);

        }
        if (i == 1) {
            this.maxtemperature = (short) (this.maxtemperature - 1000);
            if (this.maxtemperature < 1000) {
                this.maxtemperature = 1000;
            }
            this.heat.setCapacity(this.maxtemperature);
        }
        if (i == 2) {
            this.work = !this.work;
        }
    }


    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        if (this.hasFluid) {
            this.fluidTank.readFromNBT(this.provider, nbttagcompound.getCompound("fluidTank"));
        }
        this.maxtemperature = nbttagcompound.getShort("maxtemperature");
        this.auto = nbttagcompound.getBoolean("auto");
        this.work = nbttagcompound.getBoolean("work");
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (this.hasFluid) {
            CompoundTag fluidTankTag = new CompoundTag();
            this.fluidTank.writeToNBT(this.provider, fluidTankTag);
            nbttagcompound.put("fluidTank", fluidTankTag);
        }
        nbttagcompound.putShort("maxtemperature", this.maxtemperature);
        nbttagcompound.putBoolean("auto", this.auto);
        nbttagcompound.putBoolean("work", this.work);
        return nbttagcompound;

    }

    public boolean process() {
        if (!this.hasFluid) {
            if ((this.energy != null && this.energy.getEnergy() < 50)) {
                return false;
            }
        } else {
            if (this.fluidTank.getFluid().isEmpty() || this.fluidTank.getFluidAmount() == 0) {
                return false;
            }
        }
        short temp = (short) this.heat.getEnergy();
        if (temp >= this.maxtemperature) {
            return false;
        }
        if (this.heat.allow || work) {
            if (this.hasFluid) {
                if (this.getFluidTank().getFluidAmount() >= 1) {
                    this.heat.addEnergy(5);
                    this.getFluidTank().drain(this.coef, IFluidHandler.FluidAction.EXECUTE);
                    return true;
                }
            } else {
                if (this.energy.getEnergy() >= 30 * this.coef) {
                    this.heat.addEnergy(5);
                    this.energy.useEnergy(30 * this.coef);
                    return true;
                }
            }
        }
        return false;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.hasFluid) {
            MutableObject<ItemStack> output = new MutableObject<>();
            if (this.fluidTank.getFluidAmount() + 1000 <= this.fluidTank.getCapacity() && this.fluidSlot.transferToTank(
                    this.fluidTank,
                    output,
                    true
            ) && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
                this.fluidSlot.transferToTank(this.fluidTank, output, false);
                if (output.getValue() != null) {
                    this.outputSlot.add(output.getValue());
                }
            }

        }
        boolean active = process();
        if (active != this.getActive()) {
            setActive(active);
        }
        if (this.level.getGameTime() % 60 == 0) {
            if (this.heat.getEnergy() > 0) {
                this.heat.useEnergy(1);
            }
        }
    }


    public FluidTank getFluidTank() {
        return this.fluidTank;
    }


    @Override
    public ContainerHeatMachine getGuiContainer(final Player entityPlayer) {
        return new ContainerHeatMachine(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiHeatMachine((ContainerHeatMachine) menu);
    }


    @Override
    public SoundEvent getSound() {
        return null;
    }

}
