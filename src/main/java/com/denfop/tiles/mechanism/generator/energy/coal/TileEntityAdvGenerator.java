package com.denfop.tiles.mechanism.generator.energy.coal;

import com.denfop.api.gui.IType;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerGenerator;
import com.denfop.gui.GuiGenerator;
import com.denfop.invslot.InvSlotConsumableFuel;
import com.denfop.tiles.mechanism.generator.energy.TileEntityBaseGenerator;
import ic2.core.block.machine.tileentity.TileEntityIronFurnace;
import ic2.core.init.Localization;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class TileEntityAdvGenerator extends TileEntityBaseGenerator implements IType {

    public final InvSlotConsumableFuel fuelSlot = new InvSlotConsumableFuel(this, "fuel", 1, false);

    private final double coef;


    public int itemFuelTime = 0;

    public TileEntityAdvGenerator(double coef, int maxstorage, int tier) {
        super(
                coef * (double) Math.round(10.0F * ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/generator")),
                tier,
                maxstorage
        );
        this.coef = coef;
    }

    @SideOnly(Side.CLIENT)
    protected void updateEntityClient() {
        super.updateEntityClient();
        if (this.getActive()) {
            TileEntityIronFurnace.showFlames(this.getWorld(), this.pos, this.getFacing());
        }

    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.info_upgrade_energy") + this.coef);
        }
        if (this.getComp(AdvEnergy.class) != null) {
            AdvEnergy energy = this.getComp(AdvEnergy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }

    }

    public int gaugeFuelScaled(int i) {
        if (this.fuel <= 0) {
            return 0;
        } else {
            if (this.itemFuelTime <= 0) {
                this.itemFuelTime = this.fuel;
            }

            return Math.min(this.fuel * i / this.itemFuelTime, i);
        }
    }

    public boolean gainFuel() {
        int fuelValue = this.fuelSlot.consumeFuel() / 4;
        if (fuelValue == 0) {
            return false;
        } else {
            this.fuel += fuelValue;
            this.itemFuelTime = fuelValue;
            return true;
        }
    }

    public int gaugeStorageScaled(int i) {
        return (int) (this.energy.getEnergy() * (double) i / this.energy.getCapacity());
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }


    public ContainerGenerator getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiGenerator(new ContainerGenerator(entityPlayer, this));
    }

    public boolean isConverting() {
        return this.fuel > 0;
    }

    public String getOperationSoundFile() {
        return "Generators/GeneratorLoop.ogg";
    }


    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.itemFuelTime = nbt.getInteger("itemFuelTime");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("itemFuelTime", this.itemFuelTime);
        return nbt;
    }


    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
