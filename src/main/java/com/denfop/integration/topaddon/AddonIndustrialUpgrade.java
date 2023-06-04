package com.denfop.integration.topaddon;

import com.denfop.api.cool.ICoolSource;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IAdvConductor;
import com.denfop.api.energy.IAdvEnergyTile;
import com.denfop.api.energy.NodeStats;
import com.denfop.api.sytem.EnergyBase;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.sytem.IConductor;
import com.denfop.api.sytem.ITile;
import com.denfop.componets.AbstractComponent;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.CoolComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.HeatComponent;
import com.denfop.items.armour.ItemAdvJetpack;
import com.denfop.items.armour.ItemArmorAdvHazmat;
import com.denfop.items.armour.ItemArmorImprovemedNano;
import com.denfop.items.armour.ItemArmorImprovemedQuantum;
import com.denfop.items.armour.ItemSolarPanelHelmet;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.base.TileEntityLiquedTank;
import com.denfop.tiles.base.TileEntityMultiMatter;
import com.denfop.tiles.base.TileEntityObsidianGenerator;
import com.denfop.tiles.mechanism.TileEntityPlasticCreator;
import com.denfop.tiles.mechanism.TileEntityPlasticPlateCreator;
import com.denfop.tiles.mechanism.TileEntityPump;
import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import io.github.drmanganese.topaddons.addons.AddonBlank;
import io.github.drmanganese.topaddons.addons.AddonForge;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.reference.Colors;
import io.github.drmanganese.topaddons.reference.EnumChip;
import io.github.drmanganese.topaddons.reference.Names;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@TOPAddon(
        dependency = "industrialupgrade",
        order = 0
)
public class AddonIndustrialUpgrade extends AddonBlank {

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
                        .filledColor(ModUtils.convertRGBcolorToInt(91, 94, 98))
                        .alternateFilledColor(ModUtils.convertRGBcolorToInt(179, 180, 181))
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
                        .filledColor(ModUtils.convertRGBcolorToInt(224, 212, 18))
                        .alternateFilledColor(ModUtils.convertRGBcolorToInt(219, 156, 20))
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
                        .filledColor(ModUtils.convertRGBcolorToInt(76, 172, 32))
                        .alternateFilledColor(ModUtils.convertRGBcolorToInt(103, 212, 44))
                        .borderColor(Config.rfbarBorderColor)
                        .numberFormat(
                                NumberFormat.COMPACT)
        );
    }

    public void addFluidColors() {
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidiodine", ModUtils.convertRGBcolorToInt(41, 5, 59));
        Colors.FLUID_NAME_COLOR_MAP.put("iufluiddizel", ModUtils.convertRGBcolorToInt(149, 142, 0));
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidgas", ModUtils.convertRGBcolorToInt(62, 180, 241));
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidhelium", ModUtils.convertRGBcolorToInt(203, 203, 203));
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidpolyprop", ModUtils.convertRGBcolorToInt(178, 178, 178));
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidpolyeth", ModUtils.convertRGBcolorToInt(178, 178, 178));
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidneutron", ModUtils.convertRGBcolorToInt(5, 24, 59));
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidoxy", ModUtils.convertRGBcolorToInt(200, 173, 127));
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidco2", ModUtils.convertRGBcolorToInt(235, 235, 175));
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidchlorum", ModUtils.convertRGBcolorToInt(5, 59, 18));
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidhyd", ModUtils.convertRGBcolorToInt(127, 204, 174));
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidbromine", ModUtils.convertRGBcolorToInt(53, 5, 9));
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidbenz", ModUtils.convertRGBcolorToInt(197, 194, 127));
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidazot", ModUtils.convertRGBcolorToInt(173, 235, 231));
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidneft", ModUtils.convertRGBcolorToInt(1, 1, 1));

    }

    public void addTankNames() {
        Names.tankNamesMap.put(TileEntityObsidianGenerator.class, new String[]{"Water", "Lava"});
        Names.tankNamesMap.put(TileEntityLiquedTank.class, new String[]{"Buffer"});
        Names.tankNamesMap.put(TileEntityPlasticCreator.class, new String[]{"Input"});
        Names.tankNamesMap.put(TileEntityPlasticPlateCreator.class, new String[]{"Input"});
        Names.tankNamesMap.put(TileEntityPump.class, new String[]{"Buffer"});
        Names.tankNamesMap.put(TileEntityMultiMatter.class, new String[]{"Matter"});
    }

    public Map<Class<? extends ItemArmor>, EnumChip> getSpecialHelmets() {
        Map<Class<? extends ItemArmor>, EnumChip> map = new HashMap<>();
        map.put(ItemArmorImprovemedNano.class, EnumChip.IC2);
        map.put(ItemArmorImprovemedQuantum.class, EnumChip.IC2);
        map.put(ItemArmorAdvHazmat.class, EnumChip.IC2);
        map.put(ItemAdvJetpack.class, EnumChip.IC2);
        map.put(ItemSolarPanelHelmet.class, EnumChip.IC2);
        return map;
    }

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
        if (tile instanceof IAdvConductor) {
            final NodeStats node = EnergyNetGlobal.instance.getNodeStats((IAdvEnergyTile) tile);
            euBar(probeInfo, (int) node.getEnergyOut(), (int) ((IAdvConductor) tile).getConductorBreakdownEnergy());
        }
        if (tile instanceof IConductor) {
            IConductor conductor = (IConductor) tile;
            if (conductor.hasEnergies()) {
                for (EnergyType type : conductor.getEnergies()) {
                    com.denfop.api.sytem.NodeStats node = EnergyBase.getGlobal(type).getNodeStats((ITile) tile, world);
                    if (type == EnergyType.QUANTUM) {

                        qeBar(probeInfo, (int) node.getOut(),
                                (int) conductor.getConductorBreakdownEnergy(type)
                        );
                    } else if (type == EnergyType.SOLARIUM) {


                        seBar(probeInfo, (int) node.getOut(),
                                (int) conductor.getConductorBreakdownEnergy(type)
                        );
                    } else if (type == EnergyType.EXPERIENCE) {


                        eeBar(probeInfo, (int) node.getOut(),
                                (int) conductor.getConductorBreakdownEnergy(type)
                        );
                    }
                }
            } else {
                final EnergyType type = conductor.getEnergyType();
                com.denfop.api.sytem.NodeStats node = EnergyBase.getGlobal(type).getNodeStats((ITile) tile, world);
                if (type == EnergyType.QUANTUM) {

                    qeBar(probeInfo, (int) node.getOut(),
                            (int) conductor.getConductorBreakdownEnergy(type)
                    );
                } else if (type == EnergyType.SOLARIUM) {


                    seBar(probeInfo, (int) node.getOut(),
                            (int) conductor.getConductorBreakdownEnergy(type)
                    );
                } else if (type == EnergyType.EXPERIENCE) {


                    eeBar(probeInfo, (int) node.getOut(),
                            (int) conductor.getConductorBreakdownEnergy(type)
                    );
                }
            }
        } else if (tile instanceof TileEntityInventory) {
            TileEntityInventory tileBlock = (TileEntityInventory) tile;
            for (AbstractComponent component : tileBlock.getComponentList()) {
                if (component instanceof AdvEnergy) {
                    euBar(probeInfo, (int) ((AdvEnergy) component).getEnergy(), (int) ((AdvEnergy) component).getCapacity());
                }
                if (component instanceof Fluids) {
                    Iterator<Fluids.InternalFluidTank> tanks = ((Fluids) component).getAllTanks().iterator();

                    for (int tank = 0; tanks.hasNext(); ++tank) {
                        AddonForge.addTankElement(probeInfo, tile.getClass(), tanks.next(), tank, probeMode, entityPlayer);
                    }
                }
                if (component instanceof ComponentBaseEnergy) {
                    ComponentBaseEnergy componentBaseEnergy = (ComponentBaseEnergy) component;
                    if (componentBaseEnergy.getType() == EnergyType.QUANTUM) {

                        qeBar(probeInfo, (int) ((ComponentBaseEnergy) component).getEnergy(),
                                (int) ((ComponentBaseEnergy) component).getCapacity()
                        );
                    } else if (componentBaseEnergy.getType() == EnergyType.SOLARIUM) {


                        seBar(probeInfo, (int) ((ComponentBaseEnergy) component).getEnergy(),
                                (int) ((ComponentBaseEnergy) component).getCapacity()
                        );
                    } else if (componentBaseEnergy.getType() == EnergyType.EXPERIENCE) {


                        eeBar(probeInfo, (int) ((ComponentBaseEnergy) component).getEnergy(),
                                (int) ((ComponentBaseEnergy) component).getCapacity()
                        );
                    }

                }
                if (component instanceof CoolComponent) {
                    CoolComponent coolComponent = (CoolComponent) component;
                    boolean isRefrigerator = coolComponent.delegate instanceof ICoolSource;
                    if (!coolComponent.upgrade) {
                        if (!isRefrigerator) {
                            probeInfo.progress(
                                    (int) coolComponent.getEnergy(),
                                    (int) coolComponent.getCapacity(),
                                    probeInfo
                                            .defaultProgressStyle()
                                            .prefix(Localization.translate("iu.temperature"))
                                            .suffix("°C")
                                            .filledColor(Config.rfbarFilledColor)
                                            .alternateFilledColor(ModUtils.convertRGBcolorToInt(202, 98, 84))
                                            .borderColor(Config.rfbarBorderColor)
                                            .numberFormat(
                                                    NumberFormat.COMPACT)
                            );
                        } else {
                            probeInfo.progress(
                                    (int) coolComponent.getEnergy(),
                                    (int) coolComponent.getCapacity(),
                                    probeInfo
                                            .defaultProgressStyle()
                                            .prefix(Localization.translate("iu.temperature") + (coolComponent.getEnergy() > 0 ?
                                                    "-" : "")).suffix("°C")
                                            .filledColor(ModUtils.convertRGBcolorToInt(33, 98, 208))
                                            .alternateFilledColor(ModUtils.convertRGBcolorToInt(42, 87, 200))
                                            .borderColor(Config.rfbarBorderColor)
                                            .numberFormat(
                                                    NumberFormat.COMPACT)
                            );
                        }
                    }
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
                                    .filledColor(ModUtils.convertRGBcolorToInt(208, 61, 33))
                                    .alternateFilledColor(ModUtils.convertRGBcolorToInt(127, 21, 0))
                                    .borderColor(Config.rfbarBorderColor)
                                    .numberFormat(
                                            NumberFormat.COMPACT)
                    );
                }
            }


        }
    }

}
