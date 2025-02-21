package com.denfop.tiles.mechanism.steamturbine;

import com.denfop.IUItem;
import com.denfop.api.steam.ISteamBlade;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import com.denfop.container.ContainerSteamTurbineRod;
import com.denfop.gui.GuiSteamTurbineRod;
import com.denfop.invslot.InvSlot;
import com.denfop.items.reactors.ItemDamage;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileEntitySteamTurbineRod extends TileEntityMultiBlockElement implements IRod {

    private final InvSlot slot;
    List<ISteamBlade> list = new ArrayList<>();

    public TileEntitySteamTurbineRod() {
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 4) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ISteamBlade && ((ISteamBlade) stack.getItem()).getLevel() <= getMain().getLevel();
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                update();
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
                            this.put(i, ItemStack.EMPTY);
                        } else {
                            list.add((ISteamBlade) stack.getItem());
                        }
                    }
                }
            }
        };
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_rod;
    }

    @Override
    public ContainerSteamTurbineRod getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerSteamTurbineRod(this, entityPlayer);
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
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiSteamTurbineRod(getGuiContainer(entityPlayer));
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine;
    }

    @Override
    public InvSlot getSlot() {
        return slot;
    }

    @Override
    public int getLevel() {
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
            EncoderHandler.encode(customPacketBuffer, slot.writeToNbt(new NBTTagCompound()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            NBTTagCompound tagCompound = (NBTTagCompound) DecoderHandler.decode(customPacketBuffer);
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
