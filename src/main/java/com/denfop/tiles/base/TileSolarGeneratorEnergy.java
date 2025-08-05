package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IAdvEnergyNet;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.SunCoef;
import com.denfop.api.gui.IType;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSolarGeneratorEnergy;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSolarGeneratorEnergy;
import com.denfop.invslot.InvSlotGenSunarrium;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.Keyboard;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileSolarGeneratorEnergy extends TileEntityInventory implements
        IUpdatableTileEvent, IType {

    public final InvSlotGenSunarrium input;
    public final InvSlotOutput outputSlot;
    public final ItemStack itemstack = new ItemStack(IUItem.sunnarium.getItemFromMeta(4), 1);
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

    public TileSolarGeneratorEnergy(double cof, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.maxSunEnergy = 6500;
        this.cof = cof;
        this.outputSlot = new InvSlotOutput(this, 1);
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
    public int getContainerSize() {
        return 1;
    }


    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.solarium_energy.info"));
            tooltip.add(Localization.translate("iu.solarium_generator.info"));
            tooltip.add(Localization.translate("iu.info_upgrade_energy") + this.cof);

        }
        super.addInformation(stack, tooltip);

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


    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, sunenergy, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            sunenergy.onNetworkUpdate(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.lst = this.input.coefday();
        this.noSunWorld = this.level.dimension() == Level.NETHER;
        this.coef_day = this.lst.get(0);
        this.coef_night = this.lst.get(1);
        this.update_night = this.lst.get(2);
        IAdvEnergyNet advEnergyNet = EnergyNetGlobal.instance;
        this.sunCoef = advEnergyNet.getSunCoefficient(this.getWorld());
        updateVisibility();

    }

    public void updateVisibility() {
        this.skyIsVisible = this.level.canSeeSky(this.worldPosition.above()) &&
                (this.level.getBlockState(this.worldPosition.above()).getMapColor(this.level, this.worldPosition.above()) ==
                        MapColor.NONE) && !this.noSunWorld;
        this.sunIsUp = this.level.isDay();
        this.coef_day = this.lst.get(0);
        this.coef_night = this.lst.get(1);
        this.update_night = this.lst.get(2);
    }

    public void updateEntityServer() {

        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 80 == 0) {
            updateVisibility();
        }
        this.generation = 0;
        if (this.skyIsVisible) {
            energy();
            if (this.sunenergy.getEnergy() >= 6500) {
                if (this.outputSlot.get(0).getCount() < 64 || this.outputSlot.isEmpty()) {
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


    public ContainerSolarGeneratorEnergy getGuiContainer(Player entityPlayer) {
        return new ContainerSolarGeneratorEnergy(entityPlayer, this);
    }


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiSolarGeneratorEnergy((ContainerSolarGeneratorEnergy) menu);
    }

    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {
        this.work = !this.work;
    }

    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
