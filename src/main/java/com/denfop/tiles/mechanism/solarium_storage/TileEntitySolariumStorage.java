package com.denfop.tiles.mechanism.solarium_storage;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.gui.IType;
import com.denfop.api.sytem.EnergyType;
import com.denfop.blocks.BlockResource;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerSolariumStorage;
import com.denfop.gui.GuiSolariumStorage;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class TileEntitySolariumStorage extends TileEntityInventory implements IType {

    public final ComponentBaseEnergy se;
    private final EnumTypeStyle enumTypeStyle;

    public TileEntitySolariumStorage(double maxStorage1, EnumTypeStyle enumTypeStyle) {
        this.se = this.addComponent((new ComponentBaseEnergy(EnergyType.SOLARIUM, this, maxStorage1,

                Arrays.stream(EnumFacing.VALUES).filter(f -> f != this.getFacing()).collect(Collectors.toList()),
                Collections.singletonList(this.getFacing()),
                EnergyNetGlobal.instance.getTierFromPower(14),
                EnergyNetGlobal.instance.getTierFromPower(14), false
        )));
        this.enumTypeStyle = enumTypeStyle;
    }
    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            this.se.setDirections(
                    new HashSet<>(Arrays.stream(EnumFacing.VALUES)
                            .filter(facing1 -> facing1 != EnumFacing.UP && facing1 != getFacing())
                            .collect(Collectors.toList())), new HashSet<>(Collections.singletonList(this.getFacing())));
        }
    }

    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        tooltip.add(Localization.translate("iu.item.tooltip.Capacity") + " " + ModUtils.getString(this.se.getCapacity()) + " " +
                "SE");
        if (energy1 != 0) {
            tooltip.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(energy1) + "/" + ModUtils.getString(
                    se.getCapacity())
                    + " SE");
        }
    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        if (!wrench) {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
                    final ComponentBaseEnergy component2 = this.se;
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

        final ComponentBaseEnergy component2 = this.se;
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
    public EnumTypeStyle getStyle() {
        return enumTypeStyle;
    }

}
