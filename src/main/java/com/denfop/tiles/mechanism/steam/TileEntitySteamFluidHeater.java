package com.denfop.tiles.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSteamFluidHeater;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSteamFluidHeater;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.List;

public class TileEntitySteamFluidHeater extends TileElectricMachine implements IUpdatableTileEvent {


    public final Fluids.InternalFluidTank fluidTank1;
    public final Fluids.InternalFluidTank fluidTank;
    private final ComponentSteamEnergy steam;
    public Fluids fluids;
    public boolean work = true;

    public TileEntitySteamFluidHeater(BlockPos pos,BlockState state) {
        super(0, 0, 1,BlockBaseMachine3.steam_fluid_heater,pos,state);


        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 4000, InvSlot.TypeItemSlot.OUTPUT, Fluids.fluidPredicate(
                FluidName.fluidsuperheated_steam.getInstance().get()
        ));
        this.fluidTank1 = this.fluids.addTank("fluidTank1", 4000, InvSlot.TypeItemSlot.NONE, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance().get()
        ));
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSink(this, 4000));
        this.steam.setFluidTank(fluidTank1);

    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("iu.steam_info"));
        tooltip.add(Localization.translate("iu.steam_fluid_heater"));
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
    public void onNeighborChange(final BlockState neighbor, final BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (work) {
            if (this.pos.below().distSqr(neighborPos) == 0) {
                FluidState blockState = level.getFluidState(this.pos.below());
                if (blockState.getType() != net.minecraft.world.level.material.Fluids.EMPTY) {
                    this.work =
                            blockState.getType() == Fluids.LAVA;
                } else {
                    work = false;
                }
            }
        } else {
            if (this.pos.below().distSqr(neighborPos) == 0) {
                FluidState blockState = level.getFluidState(this.pos.below());
                if (blockState.getType() != net.minecraft.world.level.material.Fluids.EMPTY) {
                    this.work =
                            blockState.getType() == Fluids.LAVA;
                } else {
                    work = false;
                }
            }
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);

        try {
            FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank1 != null) {
                this.fluidTank.readFromNBT(fluidTank1.writeToNBT(new CompoundTag()));
            }
            FluidTank fluidTank2 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank2 != null) {
                this.fluidTank1.readFromNBT(fluidTank2.writeToNBT(new CompoundTag()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.steam_fluid_heater;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, fluidTank);
            EncoderHandler.encode(packet, fluidTank1);
            ;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {

            FluidState blockState = level.getFluidState(this.pos.below());
            if (blockState.getType() != net.minecraft.world.level.material.Fluids.EMPTY) {
                this.work =
                        blockState.getType() == Fluids.LAVA;
            } else {
                work = false;
            }

        }
    }


    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {

    }


    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        return nbttagcompound;

    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.work) {
            if (this.steam.getEnergy() >= 5 && this.fluidTank.getFluidAmount() + 1 <= this.fluidTank.getCapacity()) {
                this.steam.useEnergy(5);
                this.fluidTank.fill(new FluidStack(FluidName.fluidsuperheated_steam.getInstance().get(), 1), IFluidHandler.FluidAction.EXECUTE);
                this.setActive(true);
            } else {
                setActive(false);
            }
        } else {
            setActive(false);
        }
    }


    public FluidTank getFluidTank() {
        return this.fluidTank;
    }


    @Override
    public ContainerSteamFluidHeater getGuiContainer(final Player entityPlayer) {
        return new ContainerSteamFluidHeater(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiSteamFluidHeater((ContainerSteamFluidHeater) menu);
    }


    @Override
    public SoundEvent getSound() {
        return null;
    }

}
