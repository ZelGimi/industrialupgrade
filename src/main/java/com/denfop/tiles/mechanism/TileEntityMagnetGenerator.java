package com.denfop.tiles.mechanism;

import com.denfop.container.ContainerMagnetGenerator;
import com.denfop.gui.GUIMagnetGenerator;
import com.denfop.tiles.base.TileEntityElectricMachine;
import ic2.core.ContainerBase;
import ic2.core.block.comp.Energy;
import ic2.core.ref.TeBlock;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMagnetGenerator extends TileEntityElectricMachine {

    public int timer;
    private boolean work;

    public TileEntityMagnetGenerator() {
        super("", 0, 14, 1);
        this.timer = 86400;
        this.energy = this.addComponent(Energy.asBasicSource(
                this,
                3456000,
                14
        ));
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.timer = nbttagcompound.getInteger("timer");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("timer", this.timer);
        return nbttagcompound;
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (!(getWorld()).isRemote) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            if (nbt.getBoolean("work")) {
                this.timer = nbt.getInteger("timer");
            }
        }
    }

    protected ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        drop = super.adjustDrop(drop, wrench);
        if (wrench || this.teBlock.getDefaultDrop() == TeBlock.DefaultDrop.Self) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(drop);
            if (nbt.getBoolean("work")) {
                nbt.setInteger("timer", this.timer);
            }
            nbt.setBoolean("work", true);

        }
        return drop;
    }

    public void updateEntityServer() {

        super.updateEntityServer();
        if (timer == 0) {
            initiate(2);
            setActive(false);
            return;
        }
        if (this.world.provider.getWorldTime() % 20 == 0) {
            timer--;
        }
        this.energy.addEnergy(2);
        setActive(true);
        initiate(0);

        if (getWorld().provider.getWorldTime() % 60 == 0) {
            initiate(2);
        }
    }

    public String getStartSoundFile() {
        return "Machines/magnet_generator.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    @Override
    public ContainerBase<?> getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerMagnetGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GUIMagnetGenerator(new ContainerMagnetGenerator(entityPlayer, this));
    }

}
