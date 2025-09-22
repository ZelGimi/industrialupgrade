package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerMagnet;
import com.denfop.container.SlotInfo;
import com.denfop.gui.GuiMagnet;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.tiles.base.TileEntityAntiMagnet;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

public class TileMagnet extends TileElectricMachine implements IUpdatableTileEvent {

    public final SlotInfo slot;
    public int energyconsume;
    public boolean work;
    public String player;
    public int x = 11;
    public int y = 11;
    public int z = 11;
    List<Chunk> list = Lists.newArrayList();
    private AxisAlignedBB axisalignedbb;

    public TileMagnet() {
        super(100000, 14, 24);
        this.energyconsume = 1000;
        this.player = "";
        this.work = true;
        this.slot = new SlotInfo(this, 18, false);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            energyconsume = (int) DecoderHandler.decode(customPacketBuffer);
            x = (int) DecoderHandler.decode(customPacketBuffer);
            y = (int) DecoderHandler.decode(customPacketBuffer);
            z = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, energyconsume);
            EncoderHandler.encode(packet, x);
            EncoderHandler.encode(packet, y);
            EncoderHandler.encode(packet, z);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.magnet.getSoundEvent();
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        tooltip.add(Localization.translate("iu.magnet_work_info"));
        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }


    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine1.magnet;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine;
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (placer instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) placer;
            this.player = player.getName();


            for (int x = this.pos.getX() - this.x; x <= this.pos.getX() + this.x; x++) {
                for (int y = this.pos.getY() - this.y; y <= this.pos.getY() + this.y; y++) {
                    for (int z = this.pos.getZ() - this.z; z <= this.pos.getZ() + this.z; z++) {
                        final BlockPos pos1 = new BlockPos(x, y, z);
                        final TileEntity tileEntity = getWorld().getTileEntity(pos1);

                        if (tileEntity != null && !(pos1.equals(this.pos))) {
                            if (tileEntity instanceof TileEntityAntiMagnet) {
                                TileEntityAntiMagnet tile = (TileEntityAntiMagnet) tileEntity;
                                if (!tile.player.equals(this.player)) {
                                    this.work = false;
                                }
                            }
                        }
                    }
                }
            }


        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.player = nbttagcompound.getString("player");
        this.work = nbttagcompound.getBoolean("work");
        this.x = nbttagcompound.getInteger("x1");
        this.y = nbttagcompound.getInteger("y1");
        this.z = nbttagcompound.getInteger("z1");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setString("player", this.player);
        nbttagcompound.setBoolean("work", this.work);
        nbttagcompound.setInteger("x1", this.x);
        nbttagcompound.setInteger("y1", this.y);
        nbttagcompound.setInteger("z1", this.z);
        return nbttagcompound;
    }

    public boolean canInsertOrExtract(ItemStack stack) {
        List<ItemStack> BlackItemStacks = slot.getListBlack();
        if (BlackItemStacks.isEmpty()) {
            List<ItemStack> WhiteItemStacks = slot.getListWhite();
            if (!WhiteItemStacks.isEmpty()) {
                for (ItemStack stack1 : WhiteItemStacks) {
                    if (stack1.isItemEqual(stack)) {
                        return true;
                    }

                }
                return false;
            }
            return true;
        } else {
            for (ItemStack stack1 : BlackItemStacks) {
                if (stack1.isItemEqual(stack)) {
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        updateData();
    }

    public void updateData() {
        this.axisalignedbb = new AxisAlignedBB(
                this.pos.getX() - this.x,
                this.pos.getY() - this.y,
                this.pos.getZ() - this.z,
                this.pos.getX() + this.x,
                this.pos.getY() + this.y,
                this.pos.getZ() + this.z
        );
        int j2 = MathHelper.floor((axisalignedbb.minX - 2) / 16.0D);
        int k2 = MathHelper.ceil((axisalignedbb.maxX + 2) / 16.0D);
        int l2 = MathHelper.floor((axisalignedbb.minZ - 2) / 16.0D);
        int i3 = MathHelper.ceil((axisalignedbb.maxZ + 2) / 16.0D);
        list = Lists.newArrayList();

        for (int j3 = j2; j3 < k2; ++j3) {
            for (int k3 = l2; k3 < i3; ++k3) {
                Chunk chunk = world.getChunkFromChunkCoords(j3, k3);
                if (!list.contains(chunk)) {
                    list.add(chunk);
                }

            }
        }
    }

    public List<EntityItem> getEntitiesWithinAABB() {
        List<EntityItem> list = Lists.newArrayList();
        this.list.forEach(chunk -> chunk.getEntitiesOfTypeWithinAABB(EntityItem.class, axisalignedbb, list,
                EntitySelectors.NOT_SPECTATING
        ));

        return list;
    }

    public void updateEntityServer() {

        super.updateEntityServer();
        if (!this.work) {
            return;
        }
        boolean ret = false;
        if (this.world.provider.getWorldTime() % 4 == 0) {

            List<EntityItem> list = getEntitiesWithinAABB();
            for (EntityItem item : list) {

                if (!item.isDead) {
                    if (this.energy.canUseEnergy(energyconsume)) {
                        ItemStack stack = item.getItem();
                        if (this.outputSlot.canAdd(stack) && canInsertOrExtract(item.getItem())) {
                            item.setDead();
                            initiate(0);
                            setActive(true);
                            this.energy.useEnergy(energyconsume);
                            this.outputSlot.add(stack);
                            ret = true;
                        }


                    }
                }

            }
        }
        if (getWorld().provider.getWorldTime() % 10 == 0 && !ret && getActive()) {
            setActive(false);
            initiate(2);
        }


    }

    @Override
    public void updateTileServer(final EntityPlayer player, final double event) {
        if (event == 10) {
            super.updateTileServer(player, event);
        } else {
            if (event == 0) {
                x--;
                x = Math.max(1, x);
            }
            if (event == 1) {
                x++;
                x = Math.min(11, x);
            }
            if (event == 2) {
                y--;
                y = Math.max(1, y);
            }
            if (event == 3) {
                y++;
                y = Math.min(11, y);
            }
            if (event == 4) {
                z--;
                z = Math.max(1, z);
            }
            if (event == 5) {
                z++;
                z = Math.min(11, z);
            }
            updateData();
        }
    }

    public int getSizeInventory() {
        return 24;
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiMagnet(new ContainerMagnet(entityPlayer, this));
    }

    public ContainerMagnet getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerMagnet(entityPlayer, this);
    }


}
