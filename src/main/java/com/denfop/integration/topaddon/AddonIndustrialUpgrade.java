package com.denfop.integration.topaddon;

import com.denfop.IUItem;
import com.denfop.api.cool.ICoolSource;
import com.denfop.api.sytem.EnergyType;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.CoolComponent;
import com.denfop.componets.HeatComponent;
import com.denfop.componets.TileEntityAdvComponent;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import io.github.drmanganese.topaddons.addons.AddonBlank;
import io.github.drmanganese.topaddons.api.TOPAddon;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@TOPAddon(
        dependency = "industrialupgrade",
        order = 0
)
public class AddonIndustrialUpgrade extends AddonBlank {

    @Override
    public void addProbeInfo(
            final ProbeMode probeMode,
            final IProbeInfo probeInfo,
            final EntityPlayer entityPlayer,
            final World world,
            final IBlockState iBlockState,
            final IProbeHitData data
    ) {
        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile instanceof TileEntityInventory) {
            TileEntityInventory tileBlock = (TileEntityInventory) tile;
            for (TileEntityAdvComponent component : tileBlock.getComponentList()) {
                if (component instanceof AdvEnergy) {
                    euBar(probeInfo, (int) ((AdvEnergy) component).getEnergy(), (int) ((AdvEnergy) component).getCapacity());
                }
                if (component instanceof ComponentBaseEnergy) {
                    ComponentBaseEnergy componentBaseEnergy = (ComponentBaseEnergy) component;
                    if (componentBaseEnergy.getType() == EnergyType.QUANTUM) {

                        qeBar(probeInfo, (int) ((ComponentBaseEnergy) component).getEnergy(),
                                (int) ((ComponentBaseEnergy) component).getCapacity()
                        );
                    }else if(componentBaseEnergy.getType() == EnergyType.SOLARIUM) {


                        seBar(probeInfo, (int) ((ComponentBaseEnergy) component).getEnergy(),
                                (int) ((ComponentBaseEnergy) component).getCapacity()
                        );
                    }else if(componentBaseEnergy.getType() == EnergyType.EXPERIENCE) {


                        eeBar(probeInfo, (int) ((ComponentBaseEnergy) component).getEnergy(),
                                (int) ((ComponentBaseEnergy) component).getCapacity()
                        );
                    }

                }
                if (component instanceof CoolComponent) {
                    CoolComponent coolComponent = (CoolComponent) component;
                    boolean isRefrigerator = coolComponent.delegate instanceof ICoolSource;
                   if(!coolComponent.upgrade)
                    if(!isRefrigerator)
                    probeInfo.progress(
                            (int) coolComponent.getEnergy(),
                            (int) coolComponent.getCapacity(),
                            probeInfo
                                    .defaultProgressStyle()
                                    .prefix(Localization.translate("iu.temperature"))
                                    .suffix("°C")
                                    .filledColor(Config.rfbarFilledColor)
                                    .alternateFilledColor(ModUtils.convertRGBcolorToInt(202,98,84))
                                    .borderColor(Config.rfbarBorderColor)
                                    .numberFormat(
                                            NumberFormat.COMPACT)
                    );
                   else
                       probeInfo.progress(
                                (int) coolComponent.getEnergy(),
                                 (int) coolComponent.getCapacity(),
                               probeInfo
                                       .defaultProgressStyle()
                                       .prefix(Localization.translate("iu.temperature") + (coolComponent.getEnergy() > 0 ?
                                               "-" : "")).suffix("°C")
                                       .filledColor(ModUtils.convertRGBcolorToInt(33,98,208))
                                       .alternateFilledColor(ModUtils.convertRGBcolorToInt(42,87,200))
                                       .borderColor(Config.rfbarBorderColor)
                                       .numberFormat(
                                               NumberFormat.COMPACT)
                       );
                }
                if (component instanceof HeatComponent) {
                    HeatComponent heatComponent = (HeatComponent) component;
                    probeInfo.progress(
                            (int) heatComponent.getEnergy(),
                            (int) heatComponent.getCapacity(),
                            probeInfo
                                    .defaultProgressStyle()
                                    .prefix(Localization.translate("iu.temperature")
                                           ).suffix("°C")
                                    .filledColor(ModUtils.convertRGBcolorToInt(208,61,33))
                                    .alternateFilledColor(ModUtils.convertRGBcolorToInt(127,21,0))
                                    .borderColor(Config.rfbarBorderColor)
                                    .numberFormat(
                                            NumberFormat.COMPACT)
                    );
                }
            }

            /*if (tileBlock.hasComponent(Fluids.class)) {
                Iterator<Fluids.InternalFluidTank> tanks = tileBlock.getComponent(Fluids.class).getAllTanks().iterator();

                for(color = 0; tanks.hasNext(); ++color) {
                    AddonForge.addTankElement(probeInfo, tile.getClass(), (FluidTank)tanks.next(), color, mode, player);
                }
            }*/
        }
    }

    public static void euBar(IProbeInfo probeInfo, int energy, int capacity) {
        probeInfo.progress(
                energy,
                capacity,
                probeInfo
                        .defaultProgressStyle()
                        .suffix("EU")
                        .filledColor(Config.rfbarFilledColor)
                        .alternateFilledColor(Config.rfbarAlternateFilledColor)
                        .borderColor(Config.rfbarBorderColor)
                        .numberFormat(
                                NumberFormat.COMPACT)
        );
    }

    public static void qeBar(IProbeInfo probeInfo, int energy, int capacity) {
        probeInfo.progress(
                energy,
                capacity,
                probeInfo
                        .defaultProgressStyle()
                        .suffix("QE")
                        .filledColor(ModUtils.convertRGBcolorToInt(91,94,98))
                        .alternateFilledColor(ModUtils.convertRGBcolorToInt(179,180,181))
                        .borderColor(Config.rfbarBorderColor)
                        .numberFormat(
                                NumberFormat.COMPACT)
        );
    }

    public static void seBar(IProbeInfo probeInfo, int energy, int capacity) {
        probeInfo.progress(
                energy,
                capacity,
                probeInfo
                        .defaultProgressStyle()
                        .suffix("SE")
                        .filledColor(ModUtils.convertRGBcolorToInt(224,212,18))
                        .alternateFilledColor(ModUtils.convertRGBcolorToInt(219,156,20))
                        .borderColor(Config.rfbarBorderColor)
                        .numberFormat(
                                NumberFormat.COMPACT)
        );
    }

    public static void eeBar(IProbeInfo probeInfo, int energy, int capacity) {
        probeInfo.progress(
                energy,
                capacity,
                probeInfo
                        .defaultProgressStyle()
                        .suffix("EE")
                        .filledColor(ModUtils.convertRGBcolorToInt(76,172,32))
                        .alternateFilledColor(ModUtils.convertRGBcolorToInt(103,212,44))
                        .borderColor(Config.rfbarBorderColor)
                        .numberFormat(
                                NumberFormat.COMPACT)
        );
    }

}
