package com.denfop.tiles.mechanism.steamturbine;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.steam.ISteamBlade;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSteamTurbineRod;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSteamTurbineRod;
import com.denfop.invslot.InvSlot;
import com.denfop.items.reactors.ItemDamage;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
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

public class TileEntitySteamTurbineRod extends TileEntityMultiBlockElement implements IRod {

    private final InvSlot slot;
    List<ISteamBlade> list = new ArrayList<>();

    public TileEntitySteamTurbineRod(BlockPos pos, BlockState state) {
        super(BlockSteamTurbine.steam_turbine_rod,pos,state);
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 4) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
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
        CustomPacketBuffer customPacketBuffer =  super.writeUpdatePacket();
        return customPacketBuffer;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_rod;
    }

    @Override
    public ContainerSteamTurbineRod getGuiContainer(final Player entityPlayer) {
        return new ContainerSteamTurbineRod(this, entityPlayer);
    }



    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiSteamTurbineRod((ContainerSteamTurbineRod) menu);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine.getBlock(getTeBlock());
    }

    @Override
    public InvSlot getSlot() {
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
