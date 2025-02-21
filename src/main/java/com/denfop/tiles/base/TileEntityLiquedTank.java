package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerTank;
import com.denfop.gui.GuiTank;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.render.tank.DataFluid;
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
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityLiquedTank extends TileEntityInventory implements IUpgradableBlock {

    public final InvSlotUpgrade upgradeSlot;


    public final InvSlotFluidByList containerslot;
    public final InvSlotFluidByList containerslot1;
    public final Fluids fluids;
    public final InvSlotOutput outputSlot;
    public FluidTank fluidTank;
    @SideOnly(Side.CLIENT)
    public DataFluid dataFluid;
    public int prev = -10;
    private int old_amount;

    public TileEntityLiquedTank(int tanksize) {


        this.containerslot = new InvSlotFluidByList(this,
                InvSlot.TypeItemSlot.INPUT, 1, InvSlotFluid.TypeFluidSlot.OUTPUT
        );
        this.containerslot.setUsually(true);
        this.containerslot1 = new InvSlotFluidByList(this,
                InvSlot.TypeItemSlot.INPUT, 1, InvSlotFluid.TypeFluidSlot.INPUT
        );
        this.containerslot1.setUsually(true);

        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", tanksize * 1000);
        this.outputSlot = new InvSlotOutput(this, 1);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);

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
            this.old_amount = this.fluidTank.getFluidAmount();
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


    public void updateEntityServer() {
        super.updateEntityServer();
        boolean needsInvUpdate = false;
        if (this.world.provider.getWorldTime() % 20 == 0) {
            boolean need = false;
            if (this.fluidTank.getFluidAmount() != this.old_amount) {
                this.old_amount = this.fluidTank.getFluidAmount();
                need = true;
            }
            if (need) {
                new PacketUpdateFieldTile(this, "fluidTank", this.fluidTank);
            }
        }


        MutableObject<ItemStack> output = new MutableObject<>();
        if (this.containerslot.transferFromTank(this.fluidTank, output, true)
                && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
            this.containerslot.transferFromTank(this.fluidTank, output, false);
            if (output.getValue() != null) {
                this.outputSlot.add(output.getValue());
            }
        }
        if (this.needsFluid()) {
            output = new MutableObject<>();
            if (this.fluidTank.getFluidAmount() + 1000 <= this.fluidTank.getCapacity() && this.containerslot1.transferToTank(
                    this.fluidTank,
                    output,
                    true
            ) && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
                needsInvUpdate = this.containerslot1.transferToTank(this.fluidTank, output, false);
                if (output.getValue() != null) {
                    this.outputSlot.add(output.getValue());
                }
            }
        }
        if (this.upgradeSlot.tickNoMark() && needsInvUpdate) {
            this.setUpgradestat();
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


    public ContainerTank getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerTank(entityPlayer, this);
    }

    public int fill(FluidStack resource, boolean doFill) {
        return this.canFill() ? this.getFluidTank().fill(resource, doFill) : 0;
    }

    public FluidStack drain(int maxDrain, boolean doDrain) {

        return !this.canDrain() ? null : this.getFluidTank().drain(maxDrain, doDrain);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiTank(new ContainerTank(entityPlayer, this));
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

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer,
                UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput, UpgradableProperty.FluidInput,
                UpgradableProperty.FluidExtract
        );
    }

}
