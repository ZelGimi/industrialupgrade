package com.denfop.tiles.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerSteamAmpereGenerator;
import com.denfop.gui.GuiSteamAmpereGenerator;
import com.denfop.invslot.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

public class TileEntitySteamAmpereGenerator extends TileElectricMachine implements IUpdatableTileEvent {


    public final ComponentBaseEnergy pressure;
    private final ComponentSteamEnergy steam;
    public FluidTank fluidTank;
    public Fluids fluids;
    public boolean work = true;
    public short maxpressure;

    public TileEntitySteamAmpereGenerator() {
        super(0, 0, 1);


        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank1", 4000, Inventory.TypeItemSlot.NONE, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance()
        ));
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSink(this, 4000));
        this.steam.setFluidTank(fluidTank);
        this.pressure = this.addComponent(ComponentBaseEnergy.asBasicSource(EnergyType.AMPERE, this, 2000));
        this.maxpressure = 0;

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
                    fluids.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
        } else {

            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public void onNeighborChange(final Block neighbor, final BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (work) {
            if (this.pos.down().distanceSq(neighborPos) == 0) {
                IBlockState blockState = world.getBlockState(this.pos.down());
                if (blockState.getMaterial() != Material.AIR) {
                    this.work =
                            blockState.getBlock() == Blocks.LAVA || blockState.getBlock() == Blocks.FLOWING_LAVA || blockState.getBlock() == FluidName.fluidpahoehoe_lava
                                    .getInstance()
                                    .getBlock();
                } else {
                    work = false;
                }
            }
        } else {
            if (this.pos.down().distanceSq(neighborPos) == 0) {
                IBlockState blockState = world.getBlockState(this.pos.down());
                if (blockState.getMaterial() != Material.AIR) {
                    this.work =
                            blockState.getBlock() == Blocks.LAVA || blockState.getBlock() == Blocks.FLOWING_LAVA || blockState.getBlock() == FluidName.fluidpahoehoe_lava
                                    .getInstance()
                                    .getBlock();
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
                this.fluidTank.readFromNBT(fluidTank1.writeToNBT(new NBTTagCompound()));
            }
            this.maxpressure = (short) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("iu.steam_info"));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.steam_ampere_generator;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
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
        if (!this.getWorld().isRemote) {
            IBlockState blockState = world.getBlockState(this.pos.down());
            if (blockState.getMaterial() != Material.AIR) {
                this.work = blockState.getBlock() == Blocks.LAVA || blockState.getBlock() == Blocks.FLOWING_LAVA || blockState.getBlock() == FluidName.fluidpahoehoe_lava
                        .getInstance()
                        .getBlock();
            } else {
                work = false;
            }

        }
    }

    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {

    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        maxpressure = nbttagcompound.getShort("maxpressure");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("maxpressure", maxpressure);
        return nbttagcompound;

    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.work) {

            if (this.steam.getEnergy() >= 1 && this.pressure.getEnergy() + 1 <= this.pressure.getCapacity()) {
                this.pressure.addEnergy(1);
                this.steam.useEnergy(1);
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
    public ContainerSteamAmpereGenerator getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerSteamAmpereGenerator(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiSteamAmpereGenerator(getGuiContainer(entityPlayer), b);
    }


    @Override
    public SoundEvent getSound() {
        return null;
    }

}
