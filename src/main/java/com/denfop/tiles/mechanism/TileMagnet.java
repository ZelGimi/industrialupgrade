package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerMagnet;
import com.denfop.gui.GuiMagnet;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.tiles.base.TileEntityAntiMagnet;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

public class TileMagnet extends TileElectricMachine {

    public int energyconsume;
    public boolean work;
    public String player;

    public TileMagnet() {
        super(100000, 14, 24);
        this.energyconsume = 1000;
        this.player = "";
        this.work = true;

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            energyconsume = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, energyconsume);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.magnet.getSoundEvent();
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(Localization.translate("iu.magnet_work_info"));
        if (this.getComp(AdvEnergy.class) != null) {
            AdvEnergy energy = this.getComp(AdvEnergy.class);
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


            for (int x = this.pos.getX() - 10; x <= this.pos.getX() + 10; x++) {
                for (int y = this.pos.getY() - 10; y <= this.pos.getY() + 10; y++) {
                    for (int z = this.pos.getZ() - 10; z <= this.pos.getZ() + 10; z++) {
                        final TileEntity tileEntity = getWorld().getTileEntity(new BlockPos(x, y, z));

                        if (tileEntity != null && !(new BlockPos(
                                x,
                                y,
                                z
                        ).equals(this.pos))) {
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
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setString("player", this.player);
        nbttagcompound.setBoolean("work", this.work);
        return nbttagcompound;
    }

    public void updateEntityServer() {

        super.updateEntityServer();
        if (!this.work) {
            return;
        }
        boolean ret = false;
        if (this.world.provider.getWorldTime() % 4 == 0) {
            int radius = 10;
            AxisAlignedBB axisalignedbb = new AxisAlignedBB(
                    this.pos.getX() - radius,
                    this.pos.getY() - radius,
                    this.pos.getZ() - radius,
                    this.pos.getX() + radius,
                    this.pos.getY() + radius,
                    this.pos.getZ() + radius
            );
            List<EntityItem> list = this.getWorld().getEntitiesWithinAABB(EntityItem.class, axisalignedbb);


            if (getWorld().provider.getWorldTime() % 10 == 0) {
                for (EntityItem item : list) {

                    if (!item.isDead) {
                        if (this.energy.canUseEnergy(energyconsume)) {
                            ItemStack stack = item.getItem();

                            if (this.outputSlot.canAdd(stack)) {
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
        }
        if (getWorld().provider.getWorldTime() % 10 == 0 && !ret && getActive()) {
            setActive(false);
            initiate(2);
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
