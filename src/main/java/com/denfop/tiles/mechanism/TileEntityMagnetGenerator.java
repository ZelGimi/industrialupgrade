package com.denfop.tiles.mechanism;

import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerMagnetGenerator;
import com.denfop.gui.GuiMagnetGenerator;
import com.denfop.tiles.base.TileEntityElectricMachine;
import ic2.core.ContainerBase;
import ic2.core.init.Localization;
import ic2.core.ref.TeBlock;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityMagnetGenerator extends TileEntityElectricMachine {

    public int timer;

    public TileEntityMagnetGenerator() {
        super(0, 14, 1);
        this.timer = 86400;
        this.energy = this.addComponent(AdvEnergy.asBasicSource(
                this,
                3456000,
                14
        ));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        tooltip.add(Localization.translate("iu.magnet_generator.info"));
        super.addInformation(stack, tooltip, advanced);
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
            nbt.setInteger("timer", this.timer);
            nbt.setBoolean("work", true);

        }
        return drop;
    }

    public void updateEntityServer() {

        super.updateEntityServer();
        if (timer == 0) {
            if (this.getActive()) {
                initiate(2);
                setActive(false);
            }
            return;
        }
        if (this.world.provider.getWorldTime() % 20 == 0) {
            timer--;
        }
        this.energy.addEnergy(2);
        if (!getActive()) {
            setActive(true);
            initiate(0);
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
        return new GuiMagnetGenerator(new ContainerMagnetGenerator(entityPlayer, this));
    }

}
