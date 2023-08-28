package com.denfop.integration.topaddon;

import com.denfop.Localization;
import com.denfop.api.cool.ICoolSource;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IEnergyConductor;
import com.denfop.api.energy.IEnergyTile;
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
import com.denfop.tiles.base.TileMultiMatter;
import com.denfop.tiles.base.TileObsidianGenerator;
import com.denfop.tiles.mechanism.TilePlasticCreator;
import com.denfop.tiles.mechanism.TilePlasticPlateCreator;
import com.denfop.tiles.mechanism.TilePump;
import com.denfop.utils.ModUtils;
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
                        .suffix("EF")
                        .filledColor(ModUtils.convertRGBcolorToInt(33, 91, 199))
                        .alternateFilledColor(ModUtils.convertRGBcolorToInt(70, 125, 227))
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
        Colors.FLUID_NAME_COLOR_MAP.put("iufluiduu_matter", -12909261);
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidconstruction_foam", -14671840);
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidcoolant", -15443350);
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidhot_coolant", -4904908);
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidpahoehoe_lava", -8686484);
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidbiomass", -13144283);
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidbiogas", -5793716);
        Colors.FLUID_NAME_COLOR_MAP.put("iufluiddistilled_water", -12364043);
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidsuperheated_steam", -3485231);
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidsteam", -4408132);
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidhot_water", -12132609);
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidweed_ex", -16298220);
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidair", -2302756);
        Colors.FLUID_NAME_COLOR_MAP.put("iufluidheavy_water", -12364043);
    }

    public void addTankNames() {
        Names.tankNamesMap.put(TileObsidianGenerator.class, new String[]{"Water", "Lava"});
        Names.tankNamesMap.put(TileEntityLiquedTank.class, new String[]{"Buffer"});
        Names.tankNamesMap.put(TilePlasticCreator.class, new String[]{"Input"});
        Names.tankNamesMap.put(TilePlasticPlateCreator.class, new String[]{"Input"});
        Names.tankNamesMap.put(TilePump.class, new String[]{"Buffer"});
        Names.tankNamesMap.put(TileMultiMatter.class, new String[]{"Matter"});
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
        if (tile instanceof IEnergyConductor) {
            final NodeStats node = EnergyNetGlobal.instance.getNodeStats((IEnergyTile) tile);
            euBar(probeInfo, (int) node.getEnergyOut(), (int) ((IEnergyConductor) tile).getConductorBreakdownEnergy());
        }
        if (tile instanceof IConductor) {
            IConductor conductor = (IConductor) tile;
            if (conductor.hasEnergies()) {
                for (EnergyType type : conductor.getEnergies()) {
                    NodeStats node = EnergyBase.getGlobal(type).getNodeStats((ITile) tile, world);
                    if (type == EnergyType.QUANTUM) {

                        qeBar(probeInfo, (int) node.getEnergyOut(),
                                (int) conductor.getConductorBreakdownEnergy(type)
                        );
                    } else if (type == EnergyType.SOLARIUM) {


                        seBar(probeInfo, (int) node.getEnergyOut(),
                                (int) conductor.getConductorBreakdownEnergy(type)
                        );
                    } else if (type == EnergyType.EXPERIENCE) {


                        eeBar(probeInfo, (int) node.getEnergyOut(),
                                (int) conductor.getConductorBreakdownEnergy(type)
                        );
                    }
                }
            } else {
                final EnergyType type = conductor.getEnergyType();
                NodeStats node = EnergyBase.getGlobal(type).getNodeStats((ITile) tile, world);
                if (type == EnergyType.QUANTUM) {

                    qeBar(probeInfo, (int) node.getEnergyOut(),
                            (int) conductor.getConductorBreakdownEnergy(type)
                    );
                } else if (type == EnergyType.SOLARIUM) {


                    seBar(probeInfo, (int) node.getEnergyOut(),
                            (int) conductor.getConductorBreakdownEnergy(type)
                    );
                } else if (type == EnergyType.EXPERIENCE) {


                    eeBar(probeInfo, (int) node.getEnergyOut(),
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
