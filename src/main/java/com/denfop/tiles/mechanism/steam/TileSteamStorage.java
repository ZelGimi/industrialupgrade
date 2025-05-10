package com.denfop.tiles.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSteamTank;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSteamTank;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

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
    public int amount;

    public TileSteamStorage(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.steam_storage,pos,state);

        this.steam = this.addComponent((new ComponentSteamEnergy(
                EnergyType.STEAM, this, 64000,

                Arrays.stream(Direction.values()).filter(f -> f != this.getFacing()).collect(Collectors.toList()),
                Collections.singletonList(this.getFacing()),
                EnergyNetGlobal.instance.getTierFromPower(14),
                EnergyNetGlobal.instance.getTierFromPower(14), false
        )));


        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 64 * 1000, InvSlot.TypeItemSlot.NONE,
                Fluids.fluidPredicate(FluidName.fluidsteam.getInstance().get())
        );
        this.steam.setFluidTank(fluidTank);
    }



    @Override
    public int getLightValue() {
        if (this.fluidTank.getFluid().isEmpty() || this.fluidTank.getFluid().getFluid().getFluidType() == null) {
            return super.getLightValue();
        } else {
            return this.fluidTank.getFluid().getFluid().getFluidType().getLightLevel();
        }
    }



    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(BlockBaseMachine3.steam_storage);
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
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (stack.hasTag() && stack.getTag().contains("fluid")) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT((CompoundTag) stack.getTag().get("fluid"));

            tooltip.add(Localization.translate("iu.fluid.info") + fluidStack.getDisplayName().getString());
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


    public ContainerSteamTank getGuiContainer(Player entityPlayer) {
        return new ContainerSteamTank(entityPlayer, this);
    }



    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiSteamTank((ContainerSteamTank) menu);
    }


    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            setUpgradestat();
            this.steam.setDirections(
                    new HashSet<>(Arrays.stream(Direction.values())
                            .filter(facing1 -> facing1 != Direction.UP && facing1 != getFacing())
                            .collect(Collectors.toList())), new HashSet<>(Collections.singletonList(this.getFacing())));
        }
    }

    public void setUpgradestat() {
    }


    public void setChanged() {
        super.setChanged();
        if (!level.isClientSide) {
            setUpgradestat();
        }
    }


}
