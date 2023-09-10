package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IAdvEnergyNet;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.SunCoef;
import com.denfop.api.gui.IType;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerSolarGeneratorEnergy;
import com.denfop.gui.GuiSolarGeneratorEnergy;
import com.denfop.invslot.InvSlotGenSunarrium;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileSolarGeneratorEnergy extends TileEntityInventory implements
        IUpdatableTileEvent, IType {

    public final InvSlotGenSunarrium input;
    public final InvSlotOutput outputSlot;
    public final ItemStack itemstack = new ItemStack(IUItem.sunnarium, 1, 4);
    public final double maxSunEnergy;
    public final double cof;
    public boolean work;
    public ComponentBaseEnergy sunenergy;
    public List<Double> lst;
    public double coef_day;
    public double coef_night;
    public double update_night;
    public double generation;
    private boolean noSunWorld;
    private boolean skyIsVisible;
    private boolean sunIsUp;
    private SunCoef sunCoef;

    public TileSolarGeneratorEnergy(double cof) {

        this.maxSunEnergy = 6500;
        this.cof = cof;
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.input = new InvSlotGenSunarrium(this);
        this.lst = new ArrayList<>();
        this.lst.add(0D);
        this.lst.add(0D);
        this.lst.add(0D);
        this.sunenergy = this.addComponent(ComponentBaseEnergy
                .asBasicSource(EnergyType.SOLARIUM, this, 10000, 1));
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

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            work = (boolean) DecoderHandler.decode(customPacketBuffer);
            generation = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, work);
            EncoderHandler.encode(packet, generation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }


    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, sunenergy);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            sunenergy.readFromNbt((NBTTagCompound) DecoderHandler.decode(customPacketBuffer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.lst = this.input.coefday();
        this.noSunWorld = this.world.provider.isNether();
        this.coef_day = this.lst.get(0);
        this.coef_night = this.lst.get(1);
        this.update_night = this.lst.get(2);
        IAdvEnergyNet advEnergyNet = EnergyNetGlobal.instance;
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


    public ContainerSolarGeneratorEnergy getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerSolarGeneratorEnergy(entityPlayer, this);
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiSolarGeneratorEnergy(new ContainerSolarGeneratorEnergy(entityPlayer, this));
    }

    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        this.work = !this.work;
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
