package com.denfop.integration.topaddon;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.agriculture.ICrop;
import com.denfop.api.bee.IBee;
import com.denfop.api.cool.ICoolSource;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IEnergyConductor;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.energy.NodeStats;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.sytem.EnergyBase;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.sytem.IConductor;
import com.denfop.api.sytem.ITile;
import com.denfop.blocks.IDeposits;
import com.denfop.componets.AbstractComponent;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.CoolComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.componets.HeatComponent;
import com.denfop.componets.ProcessMultiComponent;
import com.denfop.items.armour.ItemAdvJetpack;
import com.denfop.items.armour.ItemArmorAdvHazmat;
import com.denfop.items.armour.ItemSolarPanelHelmet;
import com.denfop.items.armour.special.ItemSpecialArmor;
import com.denfop.recipe.IInputItemStack;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileEntityAnvil;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.base.TileEntityLiquedTank;
import com.denfop.tiles.base.TileEntityStrongAnvil;
import com.denfop.tiles.base.TileMultiMatter;
import com.denfop.tiles.base.TileObsidianGenerator;
import com.denfop.tiles.bee.TileEntityApiary;
import com.denfop.tiles.crop.TileEntityCrop;
import com.denfop.tiles.mechanism.TileEntityCompressor;
import com.denfop.tiles.mechanism.TileEntityDryer;
import com.denfop.tiles.mechanism.TileEntityMacerator;
import com.denfop.tiles.mechanism.TileEntityPrimalLaserPolisher;
import com.denfop.tiles.mechanism.TileEntityPrimalWireInsulator;
import com.denfop.tiles.mechanism.TileEntityRollingMachine;
import com.denfop.tiles.mechanism.TileEntitySqueezer;
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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
                        .alternateFilledColor(ModUtils.convertRGBcolorToInt(33, 91, 199))
                        .numberFormat(NumberFormat.COMPACT)
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
                        .alternateFilledColor(ModUtils.convertRGBcolorToInt(91, 94, 98))
                        .borderColor(Config.rfbarBorderColor)
                        .numberFormat(
                                NumberFormat.COMPACT)
        );
    }

    public static void radBar(IProbeInfo probeInfo, int energy, int capacity) {
        probeInfo.progress(
                energy,
                capacity,
                probeInfo
                        .defaultProgressStyle()
                        .suffix("☢")
                        .filledColor(ModUtils.convertRGBcolorToInt(42, 196, 45))
                        .alternateFilledColor(ModUtils.convertRGBcolorToInt(42, 196, 45))
                        .borderColor(Config.rfbarBorderColor)
                        .numberFormat(
                                NumberFormat.COMPACT)
        );
    }

    public static void posBar(IProbeInfo probeInfo, int energy, int capacity) {
        probeInfo.progress(
                energy,
                capacity,
                probeInfo
                        .defaultProgressStyle()
                        .suffix("e⁺")
                        .filledColor(ModUtils.convertRGBcolorToInt(192, 0, 218))
                        .alternateFilledColor(ModUtils.convertRGBcolorToInt(192, 0, 218))
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
                        .alternateFilledColor(ModUtils.convertRGBcolorToInt(224, 212, 18))
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
                        .alternateFilledColor(ModUtils.convertRGBcolorToInt(76, 172, 32))
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
        map.put(ItemSpecialArmor.class, EnumChip.STANDARD);
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
        if (!com.denfop.Config.enableProbe) {
            return;
        }

        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile instanceof TileEntityBlock) {
            TileEntityBlock te = (TileEntityBlock) tile;
            if (te.wrenchCanRemove(entityPlayer)) {
                probeInfo.text(Localization.translate("iu.wrench.info"));
            }
            final ComponentProgress component = te.getComp(ComponentProgress.class);
            final ComponentProcess component1 = te.getComp(ComponentProcess.class);

            ProcessMultiComponent component2 = te.getComp(ProcessMultiComponent.class);
            if (te instanceof TileEntityAnvil) {
                TileEntityAnvil anvil = (TileEntityAnvil) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUniqueID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(TextFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo
                        .horizontal()
                        .text(TextFormatting.GRAY + Localization.translate("iu.primitive_anvil_durability") + " " + anvil.durability);
                cropInfo.progress(anvil.progress, 100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 100)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntityStrongAnvil) {
                TileEntityStrongAnvil anvil = (TileEntityStrongAnvil) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUniqueID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(TextFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo.progress(anvil.progress, 100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 100)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntityCompressor) {
                TileEntityCompressor anvil = (TileEntityCompressor) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUniqueID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(TextFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo
                        .horizontal()
                        .text(TextFormatting.GRAY + Localization.translate("iu.primitive_anvil_durability") + " " + anvil.durability);
                cropInfo.progress(anvil.progress, 100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 100)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntityMacerator) {
                TileEntityMacerator anvil = (TileEntityMacerator) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUniqueID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(TextFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo
                        .horizontal()
                        .text(TextFormatting.GRAY + Localization.translate("iu.primitive_anvil_durability") + " " + anvil.durability);
                cropInfo.progress(anvil.progress, 100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 100)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntityPrimalWireInsulator) {
                TileEntityPrimalWireInsulator anvil = (TileEntityPrimalWireInsulator) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUniqueID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(TextFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo.progress(anvil.progress, 100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 100)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntityRollingMachine) {
                TileEntityRollingMachine anvil = (TileEntityRollingMachine) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUniqueID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(TextFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo.progress(anvil.progress, 100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 100)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntityPrimalLaserPolisher) {
                TileEntityPrimalLaserPolisher anvil = (TileEntityPrimalLaserPolisher) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUniqueID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(TextFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo.progress(anvil.progress, 100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 100)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntitySqueezer) {
                TileEntitySqueezer anvil = (TileEntitySqueezer) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUniqueID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(TextFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo.progress(anvil.progress, 150,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 150)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntityDryer) {
                TileEntityDryer anvil = (TileEntityDryer) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUniqueID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(TextFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo.progress(anvil.progress, 100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 100)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntityCrop) {
                TileEntityCrop tileEntityCrop = (TileEntityCrop) te;
                if (tileEntityCrop.getCrop() != null) {
                    ICrop crop = tileEntityCrop.getCrop();
                    IProbeInfo cropInfo = probeInfo.vertical();


                    int tick = crop.getTick();
                    int maxTick = crop.getMaxTick();
                    cropInfo.progress(tick, maxTick,
                            probeInfo.defaultProgressStyle()
                                    .suffix(" / " + maxTick + " " + Localization.translate("iu.crop.oneprobe.growth"))
                                    .showText(true)
                                    .filledColor(0xFFFFA500)
                    );


                    ItemStack soil = crop.getSoil().getStack();
                    if (!soil.isEmpty()) {
                        cropInfo.horizontal().text(TextFormatting.YELLOW + Localization.translate("iu.crop.oneprobe.soil")).item(
                                soil);
                    }

                    if (!crop.getDrops().isEmpty()) {
                        ItemStack stack = crop.getDrops().get(0);
                        if (!stack.isEmpty()) {
                            cropInfo
                                    .horizontal()
                                    .text(TextFormatting.AQUA + Localization.translate("iu.crop.oneprobe.drop"))
                                    .item(stack);
                        }
                    }
                    cropInfo
                            .horizontal()
                            .text(TextFormatting.GREEN + Localization.translate("iu.crop.oneprobe.using") + " " +
                                    Localization.translate("iu.crop.oneprobe.fertilizer") + " " + tileEntityCrop.getPestUse() + " / " + 40)
                            .item(new ItemStack(IUItem.fertilizer));

                    int pesticidesTime = tileEntityCrop.getTickPest();
                    int maxPesticidesTime = 7000;
                    cropInfo.text(Localization.translate("iu.crop.oneprobe.pesticide_time")).progress(
                            pesticidesTime == 0 ? pesticidesTime : maxPesticidesTime - pesticidesTime,
                            maxPesticidesTime,
                            probeInfo.defaultProgressStyle()
                                    .suffix(" / " + maxPesticidesTime + " t")
                                    .showText(true)
                                    .filledColor(0xFF00FF00)
                    );

                    int generation = crop.getGeneration();
                    cropInfo.text(TextFormatting.LIGHT_PURPLE + Localization.translate("iu.crop.oneprobe.generation") + generation);
                    cropInfo
                            .horizontal()
                            .text(TextFormatting.YELLOW + Localization.translate("iu.crop.oneprobe.genes") + tileEntityCrop
                                    .getGenome()
                                    .getGeneticTraitsMap()
                                    .values()
                                    .size());

                    boolean isWeed = crop.getId() == 3;
                    if (isWeed) {
                        cropInfo.text(TextFormatting.RED + Localization.translate("iu.crop.oneprobe.weed_warning"));
                    }
                }
            }
            if (te instanceof TileEntityApiary) {
                TileEntityApiary apiary = (TileEntityApiary) te;

                if (apiary.getQueen() != null) {
                    IProbeInfo apiaryInfo = probeInfo.vertical();

                    IBee queen = apiary.getQueen();
                    apiaryInfo.text(TextFormatting.GOLD + Localization.translate("iu.crop.oneprobe.queen") + TextFormatting.BOLD + queen.getName());

                    apiaryInfo.progress((int) apiary.food, (int) apiary.maxFood,
                            probeInfo.defaultProgressStyle()
                                    .suffix(" / " + (int) apiary.maxFood + " " + Localization.translate("iu.crop.oneprobe.honey"))
                                    .showText(true).filledColor(0xFFFFA500).alternateFilledColor(0xFFFFA500)
                    );
                    apiaryInfo.progress((int) apiary.royalJelly, (int) apiary.maxJelly,
                            probeInfo.defaultProgressStyle()
                                    .suffix(" / " + (int) apiary.maxJelly + " " + Localization.translate(
                                            "iu.crop.oneprobe.royal_jelly"))
                                    .showText(true)
                    );
                    apiaryInfo.text(TextFormatting.GREEN + Localization.translate("iu.crop.oneprobe.workers") + apiary.workers);
                    apiaryInfo.text(TextFormatting.YELLOW + Localization.translate("iu.crop.oneprobe.builders") + apiary.builders);
                    apiaryInfo.text(TextFormatting.RED + Localization.translate("iu.crop.oneprobe.guards") + apiary.attacks);
                    apiaryInfo.text(TextFormatting.BLUE + Localization.translate("iu.crop.oneprobe.medics") + apiary.doctors);
                    apiaryInfo.text(TextFormatting.DARK_RED + Localization.translate("iu.crop.oneprobe.sick") + apiary.ill);
                    apiaryInfo.text(TextFormatting.LIGHT_PURPLE + Localization.translate("iu.crop.oneprobe.new_bees") + apiary.birthBeeList.size());
                    String nameMainFlower = Localization.translate("crop." + queen.getCropFlower().getName());
                    apiaryInfo.text(TextFormatting.AQUA + Localization.translate("iu.crop.oneprobe.main_flower") + nameMainFlower);
                    List<ItemStack> stacks = apiary.invSlotProduct.getContents();
                    IProbeInfo productInfo = apiaryInfo.horizontal();
                    productInfo.text(TextFormatting.GOLD + Localization.translate("iu.crop.oneprobe.products"));

                    boolean hasProducts = false;
                    for (ItemStack stack : stacks) {
                        if (!stack.isEmpty()) {
                            productInfo.item(stack);
                            hasProducts = true;
                        }
                    }
                    if (!hasProducts) {
                        productInfo.text(TextFormatting.DARK_GRAY + Localization.translate("iu.crop.oneprobe.no_resources"));
                    }
                    IProbeInfo framesInfo = apiaryInfo.horizontal();
                    framesInfo.text(TextFormatting.GOLD + Localization.translate("iu.crop.oneprobe.frames"));

                    boolean hasFrames = false;
                    for (ItemStack stack : apiary.frameSlot.gets()) {
                        if (!stack.isEmpty()) {
                            framesInfo.item(stack);
                            hasFrames = true;
                        }
                    }
                    if (!hasFrames) {
                        framesInfo.text(TextFormatting.DARK_GRAY + Localization.translate("iu.crop.oneprobe.no_frames"));
                    }
                    framesInfo = apiaryInfo.horizontal();
                    framesInfo.text(TextFormatting.YELLOW + Localization.translate("iu.crop.oneprobe.genes_count") + apiary
                            .getGenome()
                            .getGeneticTraitsMap()
                            .values()
                            .size());

                }
            }


            if (component2 != null) {
                int slotsPerRow = 5;
                IProbeInfo inputRow = probeInfo.horizontal();
                IProbeInfo outputRow = probeInfo.horizontal();

                inputRow.text(TextFormatting.YELLOW + Localization.translate("iu.probe.recipe.input"));
                outputRow.text(TextFormatting.AQUA + Localization.translate("iu.probe.recipe.output"));

                for (int i = 0; i < component2.getSizeWorkingSlot(); i++) {
                    IProbeInfo inputSlot = inputRow.vertical();
                    IProbeInfo outputSlot = outputRow.vertical();

                    if (component2.getRecipeOutput(i) != null) {
                        ItemStack input = component2.inputSlots.get(i);
                        List<ItemStack> outputs = component2.getRecipeOutput(i).getRecipe().output.items;

                        if (!input.isEmpty()) {
                            inputSlot.item(input);
                        } else {
                            inputSlot.text(TextFormatting.DARK_GRAY + Localization.translate("iu.probe.recipe.empty"));
                        }

                        if (!outputs.isEmpty()) {
                            int index = (int) (world.getTotalWorldTime() % outputs.size());
                            outputSlot.item(outputs.get(index));
                        } else {
                            outputSlot.text(TextFormatting.DARK_GRAY + Localization.translate("iu.probe.recipe.empty"));
                        }
                    } else {
                        inputSlot.text(TextFormatting.DARK_GRAY + Localization.translate("iu.probe.recipe.empty"));
                        outputSlot.text(TextFormatting.DARK_GRAY + Localization.translate("iu.probe.recipe.empty"));
                    }

                    if ((i + 1) % slotsPerRow == 0) {
                        inputRow = probeInfo.horizontal();
                        outputRow = probeInfo.horizontal();
                    }
                }
            }


            if (component != null) {
                double progress = component.getBar();
                int percentage = (int) (progress * 100);

                probeInfo.progress((int) component.getProgress(), component.getMaxValue(),
                        probeInfo.defaultProgressStyle()
                                .suffix("t")
                                .showText(true)
                );
                if (component1 != null) {
                    IUpdateTick updateTick = (IUpdateTick) component1.getParent();
                    if (updateTick.getRecipeOutput() != null) {
                        final List<IInputItemStack> inputs = updateTick
                                .getRecipeOutput()
                                .getRecipe().input.getInputs();
                        final List<ItemStack> outputs = updateTick
                                .getRecipeOutput()
                                .getRecipe().output.items;
                        if (!inputs.isEmpty()) {
                            IProbeInfo inputInfo = probeInfo.horizontal();
                            inputInfo.text(TextFormatting.YELLOW + Localization.translate("iu.probe.recipe.input") + " ");
                            for (IInputItemStack input : inputs) {
                                int index = (int) (world.getTotalWorldTime() % input.getInputs().size());
                                inputInfo.item(input.getInputs().get(index));
                            }
                        }


                        if (!outputs.isEmpty()) {
                            IProbeInfo outputInfo = probeInfo.horizontal();
                            outputInfo.text(TextFormatting.AQUA + Localization.translate("iu.probe.recipe.output") + " ");

                            int index = (int) (world.getTotalWorldTime() % outputs.size());
                            outputInfo.item(outputs.get(index));
                        }
                    }
                }

                probeInfo.text(TextFormatting.GREEN + Localization.translate("iu.probe.recipe.progress") + " " + percentage + "%");
            }
            if (te instanceof IManufacturerBlock) {
                IManufacturerBlock manufacturerBlock = (IManufacturerBlock) te;
                probeInfo.text(Localization.translate("iu.manufacturer_level.info") + manufacturerBlock.getLevel() + "/" + 10);
            }
        }
        if (tile instanceof IEnergyConductor) {
            final NodeStats node = EnergyNetGlobal.instance.getNodeStats((IEnergyTile) tile);
            euBar(probeInfo, (int) node.getEnergyOut(), (int) ((IEnergyConductor) tile).getConductorBreakdownEnergy());
        }
        if (tile instanceof TileEntityInventory) {
            List<String> stringList = new ArrayList<>();
            ((TileEntityInventory) tile).addInformation(((TileEntityInventory) tile).getPickBlock(entityPlayer, null), stringList
            );
            for (String s : stringList) {
                probeInfo.text(s);
            }
        }
        if (iBlockState.getBlock() instanceof IDeposits) {
            IDeposits deposits = (IDeposits) iBlockState.getBlock();
            int meta = iBlockState.getBlock().getMetaFromState(iBlockState);
            final List<String> stringList = deposits.getInformationFromMeta(meta);
            for (String s : stringList) {
                probeInfo.text(s);
            }
        }
        if (tile instanceof IConductor) {
            IConductor conductor = (IConductor) tile;
            if (conductor.hasEnergies()) {
                for (EnergyType type : conductor.getEnergies()) {
                    NodeStats node = EnergyBase.getGlobal(type).getNodeStats((ITile) tile, world);
                    if (node != null) {
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
                        } else if (type == EnergyType.RADIATION) {


                            radBar(probeInfo, (int) node.getEnergyOut(),
                                    (int) conductor.getConductorBreakdownEnergy(type)
                            );
                        } else if (type == EnergyType.POSITRONS) {


                            posBar(probeInfo, (int) node.getEnergyOut(),
                                    (int) conductor.getConductorBreakdownEnergy(type)
                            );
                        }
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
                } else if (type == EnergyType.RADIATION) {


                    radBar(probeInfo, (int) node.getEnergyOut(),
                            (int) conductor.getConductorBreakdownEnergy(type)
                    );
                } else if (type == EnergyType.POSITRONS) {


                    posBar(probeInfo, (int) node.getEnergyOut(),
                            (int) conductor.getConductorBreakdownEnergy(type)
                    );
                }
            }
        } else if (tile instanceof TileEntityInventory) {
            TileEntityInventory tileBlock = (TileEntityInventory) tile;
            for (AbstractComponent component : tileBlock.getComponentList()) {
                if (component instanceof Energy) {
                    euBar(probeInfo, (int) ((Energy) component).getEnergy(), (int) ((Energy) component).getCapacity());
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
                    } else if (componentBaseEnergy.getType() == EnergyType.RADIATION) {


                        radBar(probeInfo, (int) ((ComponentBaseEnergy) component).getEnergy(),
                                (int) ((ComponentBaseEnergy) component).getCapacity()
                        );
                    } else if (componentBaseEnergy.getType() == EnergyType.POSITRONS) {


                        posBar(probeInfo, (int) ((ComponentBaseEnergy) component).getEnergy(),
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
                                            .alternateFilledColor(Config.rfbarFilledColor)
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
                                            .alternateFilledColor(ModUtils.convertRGBcolorToInt(33, 98, 208))
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
                                    .alternateFilledColor(ModUtils.convertRGBcolorToInt(208, 61, 33))
                                    .borderColor(Config.rfbarBorderColor)
                                    .numberFormat(
                                            NumberFormat.COMPACT)
                    );
                }
            }


        }

    }


}
