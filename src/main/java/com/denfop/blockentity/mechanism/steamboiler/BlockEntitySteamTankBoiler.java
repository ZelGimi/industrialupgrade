package com.denfop.blockentity.mechanism.steamboiler;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamBoilerEntity;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;

public class BlockEntitySteamTankBoiler extends BlockEntityMultiBlockElement implements ITank {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank tank;
    private ComponentSteamEnergy steam;
    private int amount;

    public BlockEntitySteamTankBoiler(BlockPos pos, BlockState state) {
        super(BlockSteamBoilerEntity.steam_boiler_tank, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = this.fluids.addTank("tank", 4000);
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSource(this, 4000));
        this.steam.setFluidTank(tank);
    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("fluidTank")) {
            try {
                this.tank.setFluid(((FluidTank) DecoderHandler.decode(is)).getFluid());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            this.tank.setFluid(((FluidTank) DecoderHandler.decode(customPacketBuffer)).getFluid());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomPacketBuffer writePacket() {

        CustomPacketBuffer customPacketBuffer = super.writePacket();
        try {
            EncoderHandler.encode(customPacketBuffer, tank);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            new PacketUpdateFieldTile(this, "fluidTank", tank);

        }
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (amount != tank.getFluidAmount()) {
            amount = tank.getFluidAmount();
            new PacketUpdateFieldTile(this, "fluidTank", tank);
        }

    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.getFluidHandler(player.getItemInHand(hand)) != null) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    this.getComp(Fluids.class).getCapability(ForgeCapabilities.FLUID_HANDLER, side)
            );
        }
        return super.onActivated(player, hand, side, vec3);
    }


    @Override
    public Fluids.InternalFluidTank getTank() {
        return this.tank;
    }

    @Override
    public void setSteam() {
        this.tank.setTypeItemSlot(Inventory.TypeItemSlot.NONE);
        this.steam.onLoaded();
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSteamBoilerEntity.steam_boiler_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_boiler.getBlock(getTeBlock());
    }

    @Override
    public ComponentSteamEnergy getSteam() {
        return steam;
    }

    @Override
    public void setUnloaded() {
        this.steam.onUnloaded();
    }

    @Override
    public void setMainMultiElement(final IMainMultiBlock main) {
        super.setMainMultiElement(main);
        if (main == null && steam != null) {
            steam.onUnloaded();

        }
    }

}
