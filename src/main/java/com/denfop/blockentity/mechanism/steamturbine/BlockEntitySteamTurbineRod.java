package com.denfop.blockentity.mechanism.steamturbine;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.steam.ISteamBlade;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbineEntity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSteamTurbineRod;
import com.denfop.inventory.Inventory;
import com.denfop.items.reactors.ItemDamage;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSteamTurbineRod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlockEntitySteamTurbineRod extends BlockEntityMultiBlockElement implements IRod {

    private final Inventory slot;
    List<ISteamBlade> list = new ArrayList<>();

    public BlockEntitySteamTurbineRod(BlockPos pos, BlockState state) {
        super(BlockSteamTurbineEntity.steam_turbine_rod, pos, state);
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 4) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof ISteamBlade && ((ISteamBlade) stack.getItem()).getLevel() <= getMain().getBlockLevel();
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                update();
                return content;
            }

            @Override
            public void update() {
                super.update();
                list.clear();
                for (int i = 0; i < size(); i++) {
                    ItemStack stack = this.get(i);
                    if (!stack.isEmpty()) {
                        ItemDamage damage = (ItemDamage) stack.getItem();
                        if ((damage.getMaxCustomDamage(stack) - damage.getCustomDamage(
                                stack)) == 0) {
                            this.set(i, ItemStack.EMPTY);
                        } else {
                            list.add((ISteamBlade) stack.getItem());
                        }
                    }
                }
            }
        };
    }

    @Override
    public boolean needUpdate() {
        return true;
    }

    @Override
    public void readUpdatePacket(CustomPacketBuffer packetBuffer) {
        super.readUpdatePacket(packetBuffer);

    }

    @Override
    public CustomPacketBuffer writeUpdatePacket() {
        CustomPacketBuffer customPacketBuffer = super.writeUpdatePacket();
        return customPacketBuffer;
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSteamTurbineEntity.steam_turbine_rod;
    }

    @Override
    public ContainerMenuSteamTurbineRod getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuSteamTurbineRod(this, entityPlayer);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenSteamTurbineRod((ContainerMenuSteamTurbineRod) menu);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine.getBlock(getTeBlock());
    }

    @Override
    public Inventory getSlot() {
        return slot;
    }

    @Override
    public int getBlockLevel() {
        return -1;
    }

    @Override
    public boolean hasOwnInventory() {
        return this.getMain() != null;
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = super.writePacket();
        try {
            EncoderHandler.encode(customPacketBuffer, slot.writeToNbt(new CompoundTag()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            CompoundTag tagCompound = (CompoundTag) DecoderHandler.decode(customPacketBuffer);
            this.slot.readFromNbt(tagCompound);
            this.slot.update();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        this.slot.update();
    }

    @Override
    public List<ISteamBlade> getRods() {
        return list;
    }

    @Override
    public void updateBlades() {
        this.slot.update();
    }

}
