package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.audio.AudioSource;
import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerMagnet;
import com.denfop.gui.GuiMagnet;
import com.denfop.tiles.base.TileEntityAntiMagnet;
import com.denfop.tiles.base.TileEntityElectricMachine;
import ic2.core.ContainerBase;
import ic2.core.ExplosionIC2;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityMagnet extends TileEntityElectricMachine {

    public final int energyconsume;
    public boolean work;
    public String player;
    public AudioSource audioSource;

    public TileEntityMagnet() {
        super(100000, 14, 24);
        this.energyconsume = 1000;
        this.player = "";
        this.work = true;

    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(Localization.translate("iu.magnet_work_info"));
        if (this.hasComponent(AdvEnergy.class)) {
            AdvEnergy energy = this.getComponent(AdvEnergy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }

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


        } else {
            final ExplosionIC2 explosion = new ExplosionIC2(this.world, null, pos.getX(), pos.getY(), pos.getZ(),
                    1,
                    1f
            );
            explosion.doExplosion();
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

    public ContainerBase<? extends TileEntityMagnet> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerMagnet(entityPlayer, this);
    }


    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    public String getStartSoundFile() {
        return "Machines/magnet.ogg";
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
            this.audioSource = IUCore.audioManager.createSource(this, getStartSoundFile());
        }
        switch (event) {
            case 0:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    this.audioSource.play();
                }
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    if (getStartSoundFile() != null) {
                        IUCore.audioManager.playOnce(this, getStartSoundFile());
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


}
