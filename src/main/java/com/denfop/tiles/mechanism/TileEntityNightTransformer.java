package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerNightTransformer;
import com.denfop.gui.GuiNightTransformer;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityNightTransformer extends TileEntityInventory implements IManufacturerBlock {

    public final ComponentBaseEnergy ne;
    public final ComponentBaseEnergy se;
    public final ComponentBaseEnergy qe;
    public int level;

    public TileEntityNightTransformer() {
        this.ne = this.addComponent(ComponentBaseEnergy.asBasicSource(EnergyType.NIGHT, this, 10000));
        this.se = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.SOLARIUM, this, 20000));
        this.qe = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 1000));
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
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiNightTransformer(getGuiContainer(var1));
    }

    @Override
    public ContainerNightTransformer getGuiContainer(final EntityPlayer var1) {
        return new ContainerNightTransformer(this, var1);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.qe.getEnergy() >= 5 && this.se.getEnergy() >= 2 && this.ne.getEnergy() + 1 <= this.ne.getCapacity()) {
            int max = (int) Math.min((level + 1)*5, qe.getEnergy() / ((level + 1) * 5));
            max = (int) Math.min(max, se.getEnergy() / ((level + 1) * 2));
            max = (int) Math.min(max, (this.ne.getCapacity() - ne.getEnergy()) / ((level + 1)));
            this.qe.useEnergy(max * 5);
            this.se.useEnergy(max * 2);
            this.ne.addEnergy(max);
        }
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.night_transformer;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
