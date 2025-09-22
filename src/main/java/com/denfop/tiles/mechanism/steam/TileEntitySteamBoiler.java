package com.denfop.tiles.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerSteamBoiler;
import com.denfop.gui.GuiSteamBoiler;
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
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

public class TileEntitySteamBoiler extends TileElectricMachine implements IUpdatableTileEvent {


    public final Fluids.InternalFluidTank fluidTank1;
    private final ComponentSteamEnergy steam;
    public FluidTank fluidTank;
    public Fluids fluids;
    public boolean work = true;

    public TileEntitySteamBoiler() {
        super(0, 0, 1);


        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 4000, Inventory.TypeItemSlot.INPUT, Fluids.fluidPredicate(
                FluidRegistry.WATER
        ));
        this.fluidTank1 = this.fluids.addTank("fluidTank1", 4000, Inventory.TypeItemSlot.NONE, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance()
        ));
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSource(this, 4000));
        this.steam.setFluidTank(fluidTank1);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("iu.steam_info"));
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
            FluidTank fluidTank2 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank2 != null) {
                this.fluidTank1.readFromNBT(fluidTank2.writeToNBT(new NBTTagCompound()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.steamboiler;
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
        if (!this.getWorld().isRemote) {
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

    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {

    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        return nbttagcompound;

    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.work) {
            if (this.getWorld().getWorldTime() % 2 == 0) {
                if (this.fluidTank.getFluid() != null && this.fluidTank.getFluid().amount >= 2 && this.steam.getEnergy() + 2 <= this.steam.getCapacity()) {
                    this.steam.addEnergy(2);
                    this.fluidTank.drain(2, true);
                    this.setActive(true);
                } else {
                    setActive(false);
                }
            }
        } else {
            setActive(false);
        }
    }


    public FluidTank getFluidTank() {
        return this.fluidTank;
    }


    @Override
    public ContainerSteamBoiler getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerSteamBoiler(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiSteamBoiler(getGuiContainer(entityPlayer), b);
    }


    @Override
    public SoundEvent getSound() {
        return null;
    }

}
