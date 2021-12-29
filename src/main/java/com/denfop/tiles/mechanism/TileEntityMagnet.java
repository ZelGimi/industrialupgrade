package com.denfop.tiles.mechanism;

import com.denfop.container.ContainerMagnet;
import com.denfop.gui.GUIMagnet;
import com.denfop.tiles.base.TileEntityElectricMachine;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityMagnet extends TileEntityElectricMachine
        implements IHasGui, INetworkTileEntityEventListener {

    public final int energyconsume;
    public AudioSource audioSource;

    public TileEntityMagnet() {
        super("", 100000, 14, 24);
        this.energyconsume = 1000;

    }


    public void updateEntityServer() {

        super.updateEntityServer();
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
            for (int x = this.pos.getX() - 10; x <= this.pos.getX() + 10; x++) {
                for (int y = this.pos.getY() - 10; y <= this.pos.getY() + 10; y++) {
                    for (int z = this.pos.getZ() - 10; z <= this.pos.getZ() + 10; z++) {
                        if (getWorld().getTileEntity(new BlockPos(x, y, z)) != null && !(new BlockPos(
                                x,
                                y,
                                z
                        ).equals(this.pos))) {
                            if (getWorld().getTileEntity(new BlockPos(x, y, z)) instanceof TileEntityMagnet) {
                                return;
                            }
                        }
                    }
                }
            }
        }
        if (getWorld().provider.getWorldTime() % 10 == 0) {
            for (EntityItem item : list) {


                if (this.energy.canUseEnergy(energyconsume)) {
                    ItemStack stack = item.getItem();

                    if (this.outputSlot.canAdd(stack)) {
                        setActive(true);
                        this.energy.useEnergy(energyconsume);
                        this.outputSlot.add(stack);
                        item.setDead();
                    }


                }
            }
        }
        if (getWorld().provider.getWorldTime() % 40 == 0) {
            if (getActive()) {
                setActive(false);
            }
        }


    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

    }


    public int getSizeInventory() {
        return 24;
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIMagnet(new ContainerMagnet(entityPlayer, this));
    }

    public ContainerBase<? extends TileEntityMagnet> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerMagnet(entityPlayer, this);
    }


    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    @Override
    public void onNetworkEvent(int event) {
        if (this.audioSource == null && getStartSoundFile() != null) {
            this.audioSource = IC2.audioManager.createSource(this, getStartSoundFile());
        }
        switch (event) {
            case 0:
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    if (getInterruptSoundFile() != null) {
                        IC2.audioManager.playOnce(this, getInterruptSoundFile());
                    }
                }
                break;
            case 2:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
                break;
        }
    }

    @Override
    public void onGuiClosed(EntityPlayer arg0) {
    }

    @Override
    public String getInventoryName() {
        // TODO Auto-generated method stub
        return null;
    }

}
