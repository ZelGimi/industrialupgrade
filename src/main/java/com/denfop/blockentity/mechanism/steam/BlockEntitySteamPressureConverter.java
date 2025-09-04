package com.denfop.blockentity.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.Fluids;
import com.denfop.componets.PressureComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSteamPressureConverter;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSteamPressureConverter;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.List;

public class BlockEntitySteamPressureConverter extends BlockEntityElectricMachine implements IUpdatableTileEvent {


    public final PressureComponent pressure;
    public FluidTank fluidTank;
    public Fluids fluids;
    public boolean work = true;
    public short maxpressure;

    public BlockEntitySteamPressureConverter(BlockPos pos, BlockState state) {
        super(0, 0, 1, BlockBaseMachine3Entity.steampressureconverter, pos, state);


        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 4000, Inventory.TypeItemSlot.INPUT, Fluids.fluidPredicate(
                FluidName.fluidsuperheated_steam.getInstance().get()
        ));
        this.pressure = this.addComponent(PressureComponent.asBasicSource(this, 4));
        this.maxpressure = 0;

    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("iu.steam_info"));
        tooltip.add(Localization.translate("iu.steam_info1"));
        tooltip.add(Localization.translate("iu.steam_pressure_converter.info"));
        tooltip.add(Localization.translate("iu.pressure_steam.info"));
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.hasFluidHandler(player.getItemInHand(hand))) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(Capabilities.FluidHandler.BLOCK, side)
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
                    this.work = blockState.getType().isSame(Fluids.LAVA);
                } else {
                    work = false;
                }
            }
        } else {
            if (this.pos.below().distSqr(neighborPos) == 0) {
                FluidState blockState = level.getFluidState(this.pos.below());
                if (blockState.getType() != net.minecraft.world.level.material.Fluids.EMPTY) {
                    this.work = blockState.getType().isSame(Fluids.LAVA);
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
                this.fluidTank.readFromNBT(customPacketBuffer.registryAccess(), fluidTank1.writeToNBT(customPacketBuffer.registryAccess(), new CompoundTag()));
            }
            this.maxpressure = (short) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.steampressureconverter;
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
            EncoderHandler.encode(packet, maxpressure);

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
                this.work = blockState.getType().isSame(Fluids.LAVA);
            } else {
                work = false;
            }

        }
    }

    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {
        if (i == 0) {
            this.maxpressure = (short) (this.maxpressure + 1);
            if (this.maxpressure > 4) {
                this.maxpressure = 4;
            }
            this.pressure.setCapacity(this.maxpressure);

        }
        if (i == 1) {
            this.maxpressure = (short) (this.maxpressure - 1);
            if (this.maxpressure < 0) {
                this.maxpressure = 0;
            }
            this.pressure.setCapacity(this.maxpressure);
        }
    }


    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        maxpressure = nbttagcompound.getShort("maxpressure");
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putShort("maxpressure", maxpressure);
        return nbttagcompound;

    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (pressure.buffer.storage > maxpressure) {
            pressure.buffer.storage = maxpressure;
        }
        if (this.work) {
            if (this.getWorld().getGameTime() % 40 == 0 && maxpressure > 0) {
                if (!this.fluidTank.getFluid().isEmpty() && this.fluidTank.getFluid().getAmount() >= maxpressure && this.pressure.getEnergy() + 1 <= this.pressure.getCapacity()) {
                    this.pressure.addEnergy(1);
                    if (pressure.buffer.storage > maxpressure) {
                        pressure.buffer.storage = maxpressure;
                    }
                    this.fluidTank.drain(maxpressure, IFluidHandler.FluidAction.EXECUTE);
                    this.setActive(true);
                } else {
                    setActive(false);
                }
            }
        } else {
            setActive(false);
        }
        if (this.getWorld().getGameTime() % 400 == 0) {
            if (!this.fluidTank.getFluid().isEmpty() && this.fluidTank.getFluid().getAmount() >= maxpressure) {
                this.fluidTank.drain(maxpressure, IFluidHandler.FluidAction.EXECUTE);
            } else {
                this.pressure.useEnergy(1);
            }
        }
    }


    public FluidTank getFluidTank() {
        return this.fluidTank;
    }


    @Override
    public ContainerMenuSteamPressureConverter getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuSteamPressureConverter(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenSteamPressureConverter((ContainerMenuSteamPressureConverter) menu);
    }


    @Override
    public SoundEvent getSound() {
        return null;
    }

}
