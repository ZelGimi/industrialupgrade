package com.denfop.tiles.base;

import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerTank;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiTank;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
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

    public int prev = -10;
    private int old_amount;

    public TileEntityLiquedTank(int tanksize, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);

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
        if (this.fluidTank.getFluid().isEmpty() || this.fluidTank.getFluid().getFluid() == net.minecraft.world.level.material.Fluids.EMPTY) {
            return super.getLightValue();
        } else {
            return this.fluidTank.getFluid().getFluid().getFluidType().getLightLevel();
        }
    }


    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            fluidTank.setFluid(fluidTank1.getFluid());
            ;
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
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && player
                .getItemInHand(hand)
                .getCapability(
                        ForgeCapabilities.FLUID_HANDLER_ITEM,
                        null
                ).orElse((IFluidHandlerItem) player
                        .getItemInHand(hand).getItem().initCapabilities(player
                                .getItemInHand(hand), player
                                .getItemInHand(hand).getTag())) != null) {


            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(ForgeCapabilities.FLUID_HANDLER, side)
            );
        } else {

            return super.onActivated(player, hand, side, vec3);
        }
    }


    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (stack.hasTag() && stack.getTag().contains("fluid")) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT((CompoundTag) stack.getTag().get("fluid"));

            tooltip.add(Localization.translate("iu.fluid.info") + fluidStack.getDisplayName().getVisualOrderText());
            tooltip.add(Localization.translate("iu.fluid.info1") + fluidStack.getAmount() / 1000 + " B");

        }
        super.addInformation(stack, tooltip);
    }

    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        if (stack.hasTag() && stack.getTag().contains("fluid")) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT((CompoundTag) stack.getTag().get("fluid"));
            if (fluidStack != null) {
                this.fluidTank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            }
            this.old_amount = this.fluidTank.getFluidAmount();
            new PacketUpdateFieldTile(this, "fluidTank", this.fluidTank);
        }
    }

    @Override
    public List<ItemStack> getWrenchDrops(final Player player, final int fortune) {
        List<ItemStack> itemStackList = super.getWrenchDrops(player, fortune);

        if (this.fluidTank.getFluidAmount() > 0) {
            CompoundTag nbt = ModUtils.nbt(itemStackList.get(0));
            nbt.put("fluid", this.fluidTank.getFluid().writeToNBT(new CompoundTag()));
        }

        return itemStackList;
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        drop = super.adjustDrop(drop, wrench);
        if (drop.is(this.getPickBlock(
                null,
                null
        ).getItem()) && (wrench || this.teBlock.getDefaultDrop() == DefaultDrop.Self)) {
            if (this.fluidTank.getFluidAmount() > 0) {
                CompoundTag nbt = ModUtils.nbt(drop);
                nbt.put("fluid", this.fluidTank.getFluid().writeToNBT(new CompoundTag()));
            }
        }
        return drop;
    }

    public FluidTank getFluidTank() {
        return fluidTank;
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
        if (this.level.getGameTime() % 20 == 0) {
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


    public ContainerTank getGuiContainer(Player entityPlayer) {
        return new ContainerTank(entityPlayer, this);
    }


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiTank(new ContainerTank(entityPlayer, this));
    }


    public void onLoaded() {
        super.onLoaded();
        if (!this.level.isClientSide) {
            setUpgradestat();
        }
    }

    public void setUpgradestat() {
    }


    public void setChanged() {
        super.setChanged();
        if (!this.level.isClientSide) {
            setUpgradestat();
        }
    }


    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer,
                UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput, UpgradableProperty.FluidInput,
                UpgradableProperty.FluidExtract
        );
    }

}
