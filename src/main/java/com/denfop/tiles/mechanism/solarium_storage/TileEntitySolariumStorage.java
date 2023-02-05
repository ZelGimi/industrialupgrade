package com.denfop.tiles.mechanism.solarium_storage;

import com.denfop.api.gui.IType;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SEComponent;
import com.denfop.container.ContainerSolariumStorage;
import com.denfop.gui.GuiSolariumStorage;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import ic2.api.energy.EnergyNet;
import ic2.core.IHasGui;
import ic2.core.block.type.ResourceBlock;
import ic2.core.init.Localization;
import ic2.core.ref.BlockName;
import ic2.core.ref.TeBlock;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class TileEntitySolariumStorage extends TileEntityInventory implements IHasGui, IType {

    public final SEComponent se;
    private final EnumTypeStyle enumTypeStyle;

    public TileEntitySolariumStorage(double maxStorage1, EnumTypeStyle enumTypeStyle) {
        this.se = this.addComponent((new SEComponent(this, maxStorage1,
                new HashSet<>(
                        Arrays.asList(EnumFacing.values())), new HashSet<>(
                Arrays.asList(EnumFacing.values())),
                EnergyNet.instance.getTierFromPower(14),
                EnergyNet.instance.getTierFromPower(14), false
        )));
        this.enumTypeStyle = enumTypeStyle;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        tooltip.add(Localization.translate("ic2.item.tooltip.Capacity") + " " + ModUtils.getString(this.se.getCapacity()) + " " +
                "SE");
        if (energy1 != 0) {
            tooltip.add(Localization.translate("ic2.item.tooltip.Store") + " " + ModUtils.getString(energy1) + "/" + ModUtils.getString(
                    se.getCapacity())
                    + " SE");
        }
    }

    protected ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        if (!wrench) {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
                    final SEComponent component2 = this.se;
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
                    return BlockName.te.getItemStack(TeBlock.generator);
                case Machine:
                    return BlockName.resource.getItemStack(ResourceBlock.machine);
                case AdvMachine:
                    return BlockName.resource.getItemStack(ResourceBlock.advanced_machine);
            }
        }

        final SEComponent component2 = this.se;
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
            this.se.addEnergy(energy1);
        }
    }

    @Override
    public ContainerSolariumStorage getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerSolariumStorage(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiSolariumStorage(getGuiContainer(entityPlayer));
    }

    @Override
    public void onGuiClosed(final EntityPlayer entityPlayer) {

    }

    @Override
    public EnumTypeStyle getStyle() {
        return enumTypeStyle;
    }

}
