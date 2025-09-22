package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.pollution.ChunkLevel;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityPurifierSoil extends TileEntityInventory implements IManufacturerBlock {

    public final Energy energy;
    public ChunkLevel chunkLevel;
    public ChunkPos chunkPos;
    public int level;

    public TileEntityPurifierSoil() {
        energy = this.addComponent(Energy.asBasicSink(this, 10000));
        this.chunkLevel = null;
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
        if (level < 10) {
            ItemStack stack = player.getHeldItem(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation)) {
                return super.onActivated(player, hand, side, hitX, hitY, hitZ);
            } else {
                stack.shrink(1);
                this.level++;
                return true;
            }
        } else {

            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    public List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.level != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation, this.level));
            this.level = 0;
        }
        return ret;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.level = nbttagcompound.getInteger("level");
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("level", this.level);
        return nbttagcompound;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(final int level) {
        this.level = level;
    }

    @Override
    public void removeLevel(final int level) {
        this.level -= level;
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
    public void onLoaded() {
        super.onLoaded();
        this.chunkPos = new ChunkPos(this.pos);
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.chunkLevel != null);
        if (this.chunkLevel != null) {
            customPacketBuffer.writeBytes(this.chunkLevel.writePacket());
        }
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        boolean exist = customPacketBuffer.readBoolean();
        if (exist) {
            this.chunkLevel = new ChunkLevel(customPacketBuffer);
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.world.provider.getWorldTime() % 20 == 0 && this.energy.getEnergy() > 100) {
            this.chunkLevel = PollutionManager.pollutionManager.getChunkLevelSoil(this.chunkPos);
            if (this.chunkLevel != null) {
                final ChunkLevel chunkLevel1 = PollutionManager.pollutionManager.getChunkLevelAir(this.chunkPos);
                if (this.chunkLevel.removePollution(10 + level * 50)) {
                    chunkLevel1.addPollution(25 + level * 25);
                    this.energy.useEnergy(100);
                    this.setActive(true);
                } else {
                    this.setActive(false);
                }
            } else {
                this.setActive(false);
            }
        }
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.purifier_soil;
    }

}
