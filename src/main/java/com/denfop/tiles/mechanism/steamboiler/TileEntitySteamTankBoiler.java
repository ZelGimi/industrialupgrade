package com.denfop.tiles.mechanism.steamboiler;

import com.denfop.IUItem;
import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamBoiler;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.invslot.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.render.tank.DataFluid;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class TileEntitySteamTankBoiler extends TileEntityMultiBlockElement implements ITank {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank tank;
    @SideOnly(Side.CLIENT)
    public DataFluid dataFluid;
    private ComponentSteamEnergy steam;
    private int amount;

    public TileEntitySteamTankBoiler() {
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
        if (!this.getWorld().isRemote) {
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
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (!this.getWorld().isRemote && player
                .getHeldItem(hand)
                .hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    this.getComp(Fluids.class).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
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
    public IMultiTileBlock getTeBlock() {
        return BlockSteamBoiler.steam_boiler_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_boiler;
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
