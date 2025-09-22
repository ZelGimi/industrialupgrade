package com.denfop.tiles.mechanism.radiation_storage;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.gui.IType;
import com.denfop.api.sytem.EnergyType;
import com.denfop.blocks.BlockResource;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerRadiationStorage;
import com.denfop.gui.GuiRadiationStorage;
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

public class TileEntityRadiationStorage extends TileEntityInventory implements IType {

    public final ComponentBaseEnergy radiation;
    private final EnumTypeStyle enumTypeStyle;

    public TileEntityRadiationStorage(double maxStorage1, EnumTypeStyle enumTypeStyle) {
        this.radiation = this.addComponent((new ComponentBaseEnergy(EnergyType.RADIATION, this, maxStorage1,

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

        tooltip.add(Localization.translate("iu.item.tooltip.Capacity") + " " + ModUtils.getString(this.radiation.getCapacity()) + " " +
                "☢");


    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        if (!wrench) {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
                    final ComponentBaseEnergy component2 = this.radiation;
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


        return drop;
    }

    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);

    }

    @Override
    public ContainerRadiationStorage getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerRadiationStorage(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiRadiationStorage(getGuiContainer(entityPlayer));
    }


    @Override
    public EnumTypeStyle getStyle() {
        return enumTypeStyle;
    }

}
