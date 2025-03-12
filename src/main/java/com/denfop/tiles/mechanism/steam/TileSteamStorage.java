package com.denfop.tiles.mechanism.steam;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerSteamTank;
import com.denfop.gui.GuiSteamTank;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.render.tank.DataFluid;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class TileSteamStorage extends TileEntityInventory {


    public final Fluids fluids;
    private final ComponentSteamEnergy steam;
    public FluidTank fluidTank;
    public int prev = -10;
    @SideOnly(Side.CLIENT)
    public DataFluid dataFluid;

    public TileSteamStorage() {


        this.steam = this.addComponent((new ComponentSteamEnergy(
                EnergyType.STEAM, this, 64000,

                Arrays.stream(EnumFacing.VALUES).filter(f -> f != this.getFacing()).collect(Collectors.toList()),
                Collections.singletonList(this.getFacing()),
                EnergyNetGlobal.instance.getTierFromPower(14),
                EnergyNetGlobal.instance.getTierFromPower(14), false
        )));


        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 64 * 1000, InvSlot.TypeItemSlot.INPUT,
                Fluids.fluidPredicate(FluidName.fluidsteam.getInstance())
        );
        this.steam.setFluidTank(fluidTank);
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
    public int getLightValue() {
        if (this.fluidTank.getFluid() == null || this.fluidTank.getFluid().getFluid().getBlock() == null) {
            return super.getLightValue();
        } else {
            return this.fluidTank.getFluid().getFluid().getBlock().getLightValue(this.fluidTank
                    .getFluid()
                    .getFluid()
                    .getBlock()
                    .getDefaultState());
        }
    }

    @Override
    public int getLightOpacity() {
        if (this.fluidTank.getFluid() == null || this.fluidTank
                .getFluid()
                .getFluid()
                .getBlock() == null) {
            return super.getLightOpacity();
        } else {
            final int now = this.fluidTank.getFluid().getFluid().getBlock().getLightOpacity(this.fluidTank
                    .getFluid()
                    .getFluid()
                    .getBlock()
                    .getDefaultState());
            if (this.prev != now) {
                prev = now;
                try {
                    this.getWorld().checkLight(pos);
                } catch (Exception ignored) {
                }
                ;

            }
            return now;
        }
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.steam_storage;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            fluidTank = (FluidTank) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, fluidTank);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
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


    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("fluid")) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT((NBTTagCompound) stack.getTagCompound().getTag("fluid"));

            tooltip.add(Localization.translate("iu.fluid.info") + fluidStack.getLocalizedName());
            tooltip.add(Localization.translate("iu.fluid.info1") + fluidStack.amount / 1000 + " B");

        }
        super.addInformation(stack, tooltip);
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("fluid")) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT((NBTTagCompound) stack.getTagCompound().getTag("fluid"));
            if (fluidStack != null) {
                this.fluidTank.fill(fluidStack, true);
            }
            new PacketUpdateFieldTile(this, "fluidTank", this.fluidTank);
        }
    }

    @Override
    public List<ItemStack> getWrenchDrops(final EntityPlayer player, final int fortune) {
        List<ItemStack> itemStackList = super.getWrenchDrops(player, fortune);

        if (this.fluidTank.getFluidAmount() > 0) {
            NBTTagCompound nbt = ModUtils.nbt(itemStackList.get(0));
            nbt.setTag("fluid", this.fluidTank.getFluid().writeToNBT(new NBTTagCompound()));
        }

        return itemStackList;
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        drop = super.adjustDrop(drop, wrench);
        if (drop.isItemEqual(this.getPickBlock(
                null,
                null
        )) && (wrench || this.teBlock.getDefaultDrop() == MultiTileBlock.DefaultDrop.Self)) {
            if (this.fluidTank.getFluidAmount() > 0) {
                NBTTagCompound nbt = ModUtils.nbt(drop);
                nbt.setTag("fluid", this.fluidTank.getFluid().writeToNBT(new NBTTagCompound()));
            }
        }
        return drop;
    }

    public double gaugeLiquidScaled(double i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
    }


    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, fluidTank);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            fluidTank = (FluidTank) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean needsFluid() {
        return this.getFluidTank().getFluidAmount() < this.getFluidTank().getCapacity();
    }
    public int amount;

    public void updateEntityServer() {
        super.updateEntityServer();
        if (amount != fluidTank.getFluidAmount()) {
            amount = fluidTank.getFluidAmount();
            new PacketUpdateFieldTile(this, "fluidTank", fluidTank);
        }

    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("fluidTank")) {
            try {
                this.fluidTank.setFluid(((FluidTank) DecoderHandler.decode(is)).getFluid());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    public boolean canFill() {
        return true;
    }


    public boolean canDrain() {
        return true;
    }

    public FluidTank getFluidTank() {
        return this.fluidTank;
    }


    public ContainerSteamTank getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerSteamTank(entityPlayer, this);
    }

    public int fill(FluidStack resource, boolean doFill) {
        return this.canFill() ? this.getFluidTank().fill(resource, doFill) : 0;
    }

    public FluidStack drain(int maxDrain, boolean doDrain) {

        return !this.canDrain() ? null : this.getFluidTank().drain(maxDrain, doDrain);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiSteamTank(new ContainerSteamTank(entityPlayer, this));
    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);


    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            setUpgradestat();
            this.steam.setDirections(
                    new HashSet<>(Arrays.stream(EnumFacing.VALUES)
                            .filter(facing1 -> facing1 != EnumFacing.UP && facing1 != getFacing())
                            .collect(Collectors.toList())), new HashSet<>(Collections.singletonList(this.getFacing())));
        }
    }

    public void setUpgradestat() {
    }


    public void markDirty() {
        super.markDirty();
        if (IUCore.proxy.isSimulating()) {
            setUpgradestat();
        }
    }


}
