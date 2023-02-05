package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.IAdvEnergyNet;
import com.denfop.api.energy.SunCoef;
import com.denfop.api.gui.IType;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SEComponent;
import com.denfop.container.ContainerSolarGeneratorEnergy;
import com.denfop.gui.GuiSolarGeneratorEnergy;
import com.denfop.invslot.InvSlotGenSunarrium;
import ic2.api.energy.EnergyNet;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.init.Localization;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class TileEntitySolarGeneratorEnergy extends TileEntityInventory implements IHasGui,
        INetworkClientTileEntityEventListener, IType {

    public final InvSlotGenSunarrium input;
    public final InvSlotOutput outputSlot;
    public final ItemStack itemstack = new ItemStack(IUItem.sunnarium, 1, 4);
    public final double maxSunEnergy;
    public final double cof;
    public boolean work;
    public SEComponent sunenergy;
    public List<Double> lst;
    public double coef_day;
    public double coef_night;
    public double update_night;
    public double generation;
    private boolean noSunWorld;
    private boolean skyIsVisible;
    private boolean sunIsUp;
    private SunCoef sunCoef;

    public TileEntitySolarGeneratorEnergy(double cof) {

        this.maxSunEnergy = 6500;
        this.cof = cof;
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.input = new InvSlotGenSunarrium(this);
        this.lst = new ArrayList<>();
        this.lst.add(0D);
        this.lst.add(0D);
        this.lst.add(0D);
        this.sunenergy = this.addComponent(SEComponent
                .asBasicSource(this, 10000, 1));
        this.coef_day = 0;
        this.coef_night = 0;
        this.update_night = 0;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.solarium_energy.info"));
            tooltip.add(Localization.translate("iu.info_upgrade_energy") + this.cof);

        }
        super.addInformation(stack, tooltip, advanced);

    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("sunenergy");

        return ret;

    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        this.lst = this.input.coefday();
        this.noSunWorld = this.world.provider.isNether();
        this.coef_day = this.lst.get(0);
        this.coef_night = this.lst.get(1);
        this.update_night = this.lst.get(2);
        IAdvEnergyNet advEnergyNet = (IAdvEnergyNet) EnergyNet.instance;
        this.sunCoef = advEnergyNet.getSunCoefficient(this.world);
        updateVisibility();

    }

    public void updateVisibility() {
        this.skyIsVisible = this.world.canBlockSeeSky(this.pos.up()) &&
                (this.world.getBlockState(this.pos.up()).getMaterial().getMaterialMapColor() ==
                        MapColor.AIR) && !this.noSunWorld;
        this.sunIsUp = this.world.isDaytime();
    }

    public void updateEntityServer() {

        super.updateEntityServer();
        if (this.world.provider.getWorldTime() % 80 == 0) {
            updateVisibility();
        }
        this.generation = 0;
        if (this.skyIsVisible) {
            energy();
            if (this.sunenergy.getEnergy() >= 6500) {
                if (this.outputSlot.get().getCount() < 64 || this.outputSlot.isEmpty()) {
                    if (this.outputSlot.add(itemstack)) {
                        this.sunenergy.addEnergy(-6500);
                    }
                }
            }
        }


    }

    public void energy() {


        if (this.sunIsUp) {
            this.generation = this.sunCoef.getCoef() * 30 * this.cof * (1 + coef_day);
            this.sunenergy.addEnergy(this.generation);
        } else if (this.update_night > 0) {

            this.generation = this.sunCoef.getCoef() * 30 * this.cof * (this.update_night - 1) * (1 + this.coef_night);
            this.sunenergy.addEnergy(this.generation);

        }

    }


    @Override
    public void onGuiClosed(EntityPlayer arg0) {
    }


    public ContainerBase<? extends TileEntitySolarGeneratorEnergy> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerSolarGeneratorEnergy(entityPlayer, this);
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiSolarGeneratorEnergy(new ContainerSolarGeneratorEnergy(entityPlayer, this));
    }

    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        this.work = !this.work;
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
