package com.denfop.tiles.mechanism.quantum_storage;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.gui.IType;
import com.denfop.api.sytem.EnergyType;
import com.denfop.blocks.BlockResource;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerQuantumStorage;
import com.denfop.gui.GuiQuantumStorage;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class TileEntityQuantumStorage extends TileEntityInventory implements IType {

    public final ComponentBaseEnergy qe;
    private final EnumTypeStyle enumTypeStyle;

    public TileEntityQuantumStorage(double maxStorage1, EnumTypeStyle enumTypeStyle) {
        this.qe = this.addComponent((new ComponentBaseEnergy(EnergyType.QUANTUM, this, maxStorage1,

                Arrays.asList(EnumFacing.values()),
                Arrays.asList(EnumFacing.values()),
                EnergyNetGlobal.instance.getTierFromPower(14),
                EnergyNetGlobal.instance.getTierFromPower(14), false
        )));
        this.enumTypeStyle = enumTypeStyle;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        tooltip.add(Localization.translate("iu.item.tooltip.Capacity") + " " + ModUtils.getString(this.qe.getCapacity()) + " " +
                "QE");

        if (energy1 != 0) {
            tooltip.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(energy1) + "/" + ModUtils.getString(
                    qe.getCapacity())
                    + " QE");
        }
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        if (!wrench) {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
                    final ComponentBaseEnergy component2 = this.qe;
                    if (component2 != null) {
                        if (component2.getEnergy() != 0) {
                            final NBTTagCompound nbt = ModUtils.nbt(drop);
                            nbt.setDouble("energy", component2.getEnergy());
                        }
                    }
                    return drop;
                case None:
                    return null;
                case Generator:
                    return new ItemStack(IUItem.basemachine2, 1, 78);
                case Machine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.machine);
                case AdvMachine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.advanced_machine);
            }
        }

        final ComponentBaseEnergy component2 = this.qe;
        if (component2 != null) {
            if (component2.getEnergy() != 0) {
                final NBTTagCompound nbt = ModUtils.nbt(drop);
                nbt.setDouble("energy", component2.getEnergy());
            }
        }

        return drop;
    }

    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            this.qe.addEnergy(energy1);
        }
    }

    @Override
    public ContainerQuantumStorage getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerQuantumStorage(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiQuantumStorage(getGuiContainer(entityPlayer));
    }


    @Override
    public EnumTypeStyle getStyle() {
        return enumTypeStyle;
    }

}
