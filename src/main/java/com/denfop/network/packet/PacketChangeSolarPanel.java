package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.items.block.ItemBlockTileEntity;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.tiles.base.FakePlayerSpawner;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.List;

public class PacketChangeSolarPanel implements IPacket {

    public PacketChangeSolarPanel() {

    }

    public PacketChangeSolarPanel(EnumSolarPanels typePacket, TileSolarPanel tileEntityBlock) {
        World world = tileEntityBlock.getWorld();
        BlockPos pos = tileEntityBlock.getBlockPos();
        byte facing = tileEntityBlock.facing;
        tileEntityBlock.debt = 0;
        tileEntityBlock.deptPercent = 0;
        NBTTagCompound tagCompound = tileEntityBlock.writeToNBT(new NBTTagCompound());
        final List<ItemStack> list = tileEntityBlock.pollution.getDrops();
        typePacket = typePacket.solarold;
        ItemStack stack = new ItemStack(typePacket.block, 1, typePacket.meta);
        ItemBlockTileEntity itemBlockTileEntity = (ItemBlockTileEntity) stack.getItem();

        tileEntityBlock.onUnloaded();
        FakePlayerSpawner fakePlayerSpawner = new FakePlayerSpawner(world);
        fakePlayerSpawner.setActiveHand(EnumHand.MAIN_HAND);
        tileEntityBlock.getWorld().setBlockToAir(pos);
        tileEntityBlock.getWorld().removeTileEntity(pos);
        fakePlayerSpawner.setHeldItem(EnumHand.MAIN_HAND, stack);
        itemBlockTileEntity.onItemUse(fakePlayerSpawner, world, pos,
                fakePlayerSpawner.getActiveHand(), EnumFacing.values()[facing], 0, 0, 0
        );
        TileSolarPanel tileSolarPanel = (TileSolarPanel) world.getTileEntity(pos);
        if (tileSolarPanel != null) {
            tileSolarPanel.readFromNBT(tagCompound);
            if (!list.isEmpty()) {
                fakePlayerSpawner.setHeldItem(EnumHand.MAIN_HAND, list.get(0));
                tileSolarPanel.pollution.onBlockActivated(fakePlayerSpawner, fakePlayerSpawner.getActiveHand());
            }
        }
        tileSolarPanel.onLoaded();
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        try {
            EncoderHandler.encode(buffer, pos);
            EncoderHandler.encode(buffer, tagCompound);
            buffer.writeBytes(tileSolarPanel.pollution.updateComponent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IUCore.network.getServer().sendPacket(buffer);
    }

    @Override
    public byte getId() {
        return (byte) 157;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        try {
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            NBTTagCompound tagCompound = (NBTTagCompound) DecoderHandler.decode(customPacketBuffer);
            TileSolarPanel tileSolarPanel = (TileSolarPanel) entityPlayer.getEntityWorld().getTileEntity(pos);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
