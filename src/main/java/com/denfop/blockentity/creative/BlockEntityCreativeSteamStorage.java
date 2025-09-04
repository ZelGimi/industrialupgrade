package com.denfop.blockentity.creative;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockCreativeBlocksEntity;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuCreativeSteamTank;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.screen.ScreenCreativeSteamTank;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class BlockEntityCreativeSteamStorage extends BlockEntityInventory {


    public final Fluids fluids;
    protected final ComponentSteamEnergy steam;
    public FluidTank fluidTank;
    public int prev = -10;
    public int amount;

    public BlockEntityCreativeSteamStorage(BlockPos pos, BlockState state) {
        super(BlockCreativeBlocksEntity.creative_steam_storage, pos, state);

        this.steam = this.addComponent((new ComponentSteamEnergy(
                EnergyType.STEAM, this, 1024 * 1000,

                Arrays.stream(Direction.values()).filter(f -> f != this.getFacing()).collect(Collectors.toList()),
                Collections.singletonList(this.getFacing()),
                EnergyNetGlobal.instance.getTierFromPower(14),
                EnergyNetGlobal.instance.getTierFromPower(14), false
        )));


        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 1024 * 1000, Inventory.TypeItemSlot.NONE,
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
        return IUItem.creativeBlock.getBlock(BlockCreativeBlocksEntity.creative_steam_storage);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockCreativeBlocksEntity.creative_steam_storage;
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

        tooltip.add(Localization.translate("iu.steam_storage.info"));
        if (stack.has(DataComponentsInit.DATA) && stack.get(DataComponentsInit.DATA).contains("fluid")) {
            FluidStack fluidStack = FluidStack.parseOptional(level.registryAccess(), (CompoundTag) stack.get(DataComponentsInit.DATA).get("fluid"));

            tooltip.add(Localization.translate("iu.fluid.info") + fluidStack.getHoverName().getString());
            tooltip.add(Localization.translate("iu.fluid.info1") + fluidStack.getAmount() / 1000 + " B");

        }
        super.addInformation(stack, tooltip);
    }

    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        if (stack.has(DataComponentsInit.DATA) && stack.get(DataComponentsInit.DATA).contains("fluid")) {
            FluidStack fluidStack = FluidStack.parseOptional(placer.registryAccess(), (CompoundTag) stack.get(DataComponentsInit.DATA).get("fluid"));
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
            nbt.put("fluid", this.fluidTank.getFluid().save(player.registryAccess(), new CompoundTag()));
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
                nbt.put("fluid", this.fluidTank.getFluid().save(level.registryAccess(), new CompoundTag()));
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
        this.steam.addEnergy(this.steam.getCapacity());
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


    public ContainerMenuCreativeSteamTank getGuiContainer(Player entityPlayer) {
        return new ContainerMenuCreativeSteamTank(entityPlayer, this);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenCreativeSteamTank((ContainerMenuCreativeSteamTank) menu);
    }


    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            setUpgradestat();
            this.steam.setDirections(
                    new HashSet<>(Arrays.stream(Direction.values())
                            .filter(facing1 -> facing1 != getFacing())
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
