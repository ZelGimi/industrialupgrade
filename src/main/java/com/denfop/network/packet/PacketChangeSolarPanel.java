package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.blockentity.base.FakePlayerSpawner;
import com.denfop.blockentity.panels.entity.BlockEntitySolarPanel;
import com.denfop.blockentity.panels.entity.EnumSolarPanels;
import com.denfop.blocks.blockitem.ItemBlockTileEntity;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;
import java.util.List;

public class PacketChangeSolarPanel implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketChangeSolarPanel() {

    }

    public PacketChangeSolarPanel(EnumSolarPanels typePacket, BlockEntitySolarPanel tileEntityBlock) {
        Level world = tileEntityBlock.getWorld();
        BlockPos pos = tileEntityBlock.getBlockPos();
        byte facing = tileEntityBlock.facing;
        tileEntityBlock.debt = 0;
        tileEntityBlock.deptPercent = 0;
        CompoundTag tagCompound = tileEntityBlock.writeToNBT(new CompoundTag());
        final List<ItemStack> list = tileEntityBlock.pollution.getDrops();
        typePacket = typePacket.solarold;
        ItemStack stack = new ItemStack(typePacket.block.getItem(typePacket.meta), 1);
        ItemBlockTileEntity itemBlockTileEntity = (ItemBlockTileEntity) stack.getItem();

        tileEntityBlock.onUnloaded();
        FakePlayerSpawner fakePlayerSpawner = new FakePlayerSpawner(world);
        tileEntityBlock.getWorld().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        tileEntityBlock.getWorld().removeBlockEntity(pos);
        fakePlayerSpawner.startUsingItem(InteractionHand.MAIN_HAND);
        fakePlayerSpawner.setItemInHand(InteractionHand.MAIN_HAND, stack);
        UseOnContext context = new UseOnContext(fakePlayerSpawner, fakePlayerSpawner.getUsedItemHand(), new BlockHitResult(new Vec3(0, 0, 0),
                Direction.values()[facing], pos, false));
        itemBlockTileEntity.useOn(context);
        BlockEntitySolarPanel tileSolarPanel = (BlockEntitySolarPanel) world.getBlockEntity(pos);
        if (tileSolarPanel != null) {
            tileSolarPanel.readFromNBT(tagCompound);
            if (!list.isEmpty()) {
                fakePlayerSpawner.setItemInHand(InteractionHand.MAIN_HAND, list.get(0));
                tileSolarPanel.pollution.onBlockActivated(fakePlayerSpawner, fakePlayerSpawner.getUsedItemHand());
            }
        }
        tileSolarPanel.onLoaded();
        CustomPacketBuffer buffer = new CustomPacketBuffer(tileEntityBlock.getLevel().registryAccess());
        buffer.writeByte(this.getId());
        try {
            EncoderHandler.encode(buffer, pos);
            EncoderHandler.encode(buffer, tagCompound);
            buffer.writeBytes(tileSolarPanel.pollution.updateComponent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.buffer = buffer;
        IUCore.network.getServer().sendPacket(this);
    }

    @Override
    public CustomPacketBuffer getPacketBuffer() {
        return buffer;
    }

    @Override
    public void setPacketBuffer(CustomPacketBuffer customPacketBuffer) {
        buffer = customPacketBuffer;
    }

    @Override
    public byte getId() {
        return (byte) 157;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            CompoundTag tagCompound = (CompoundTag) DecoderHandler.decode(customPacketBuffer);
            BlockEntitySolarPanel tileSolarPanel = (BlockEntitySolarPanel) entityPlayer.level().getBlockEntity(pos);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
