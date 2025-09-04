package com.denfop.blockentity.mechanism.steamturbine.coolant;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.mechanism.steamturbine.ICoolant;
import com.denfop.blocks.FluidName;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSteamTurbineCoolant;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSteamTurbineCoolant;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityBaseSteamTurbineCoolant extends BlockEntityMultiBlockElement implements ICoolant, IUpdatableTileEvent {

    private final Fluids fluids;
    private final int blockLevel;
    private final Fluids.InternalFluidTank tank;
    double power = 0;
    Fluid currentStack = null;
    private int x = 1;

    public BlockEntityBaseSteamTurbineCoolant(int blockLevel, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.blockLevel = blockLevel;
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = this.fluids.addTankInsert("tank", 10000);
        this.getFluidsFromLevel();
    }

    private void getFluidsFromLevel() {
        List<Fluid> fluidList = new ArrayList<>();
        switch (blockLevel) {
            default:
                fluidList.add(FluidName.fluidhydrogen.getInstance().get());
                break;
            case 1:
                fluidList.add(FluidName.fluidhydrogen.getInstance().get());
                fluidList.add(FluidName.fluidnitrogen.getInstance().get());
                fluidList.add(FluidName.fluidcoolant.getInstance().get());
                break;
            case 2:
                fluidList.add(FluidName.fluidhydrogen.getInstance().get());
                fluidList.add(FluidName.fluidnitrogen.getInstance().get());
                fluidList.add(FluidName.fluidcoolant.getInstance().get());
                fluidList.add(FluidName.fluidhelium.getInstance().get());
                break;
            case 3:
                fluidList.add(FluidName.fluidhydrogen.getInstance().get());
                fluidList.add(FluidName.fluidnitrogen.getInstance().get());
                fluidList.add(FluidName.fluidcoolant.getInstance().get());
                fluidList.add(FluidName.fluidhelium.getInstance().get());
                fluidList.add(FluidName.fluidcryogen.getInstance().get());
                break;


        }
        this.tank.setAcceptedFluids(Fluids.fluidPredicate(fluidList));
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.getFluidHandler(player.getItemInHand(hand)) != null && this.getMain() != null) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(Capabilities.FluidHandler.BLOCK, side)
            );
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }


    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeInt(this.x);
        customPacketBuffer.writeDouble(this.power);
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.x = customPacketBuffer.readInt();
        this.power = customPacketBuffer.readDouble();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenSteamTurbineCoolant((ContainerMenuSteamTurbineCoolant) menu);
    }


    @Override
    public ContainerMenuSteamTurbineCoolant getGuiContainer(final Player var1) {
        return new ContainerMenuSteamTurbineCoolant(this, var1);
    }

    @Override
    public boolean hasOwnInventory() {
        return this.getMain() != null;
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        x = nbtTagCompound.getInt("coolant_x");
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putInt("coolant_x", x);
        return nbtTagCompound;
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (this.getMain() == null) {
            return;
        }
        if (var2 == 0) {
            this.x = Math.min(this.x + 1, this.blockLevel + 2);
        } else {
            this.x = Math.max(0, this.x - 1);
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.tank.getFluid() != null) {
            currentStack = this.tank.getFluid().getFluid();
            power = getPowerFromFluid();
        } else {
            currentStack = null;
            power = 0;
        }
    }

    private double getPowerFromFluid() {
        Fluid hyd = FluidName.fluidhydrogen.getInstance().get();
        Fluid hel = FluidName.fluidhelium.getInstance().get();
        Fluid coolant = FluidName.fluidcoolant.getInstance().get();
        Fluid nitrogen = FluidName.fluidnitrogen.getInstance().get();
        Fluid cryogen = FluidName.fluidcryogen.getInstance().get();
        if (this.currentStack == hyd) {
            this.power = 1;
        } else if (this.currentStack == nitrogen) {
            this.power = 1.25;
        } else if (this.currentStack == coolant) {
            this.power = 1.75;
        } else if (this.currentStack == hel) {
            this.power = 2.25;
        } else if (this.currentStack == cryogen) {
            this.power = 3;
        }
        return this.power;
    }

    @Override
    public FluidTank getCoolant() {
        return tank;
    }

    @Override
    public double getPower() {
        return this.power * this.x * 0.8d;
    }

    @Override
    public int getPressure() {
        return this.x;
    }

    @Override
    public int getBlockLevel() {
        return -1;
    }

}
