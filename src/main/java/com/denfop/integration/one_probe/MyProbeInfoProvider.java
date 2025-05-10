package com.denfop.integration.one_probe;

import com.denfop.Constants;
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
import com.denfop.blocks.FluidName;
import com.denfop.blocks.IDeposits;
import com.denfop.componets.*;
import com.denfop.recipe.IInputItemStack;
import com.denfop.tiles.base.*;
import com.denfop.tiles.bee.TileEntityApiary;
import com.denfop.tiles.crop.TileEntityCrop;
import com.denfop.tiles.mechanism.*;
import com.denfop.utils.ModUtils;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.config.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyProbeInfoProvider implements IProbeInfoProvider {



    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(Constants.MOD_ID,"probe");
    }


    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player entityPlayer, Level world, BlockState iBlockState, IProbeHitData data) {
        BlockEntity tile = world.getBlockEntity(data.getPos());
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
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo
                        .horizontal()
                        .text(ChatFormatting.GRAY + Localization.translate("iu.primitive_anvil_durability") + " " + anvil.durability);
                cropInfo.progress(anvil.progress, 100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 100)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntityStrongAnvil) {
                TileEntityStrongAnvil anvil = (TileEntityStrongAnvil) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo.progress(anvil.progress, 100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 100)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntityCompressor) {
                TileEntityCompressor anvil = (TileEntityCompressor) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo
                        .horizontal()
                        .text(ChatFormatting.GRAY + Localization.translate("iu.primitive_anvil_durability") + " " + anvil.durability);
                cropInfo.progress(anvil.progress, 100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 100)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntityMacerator) {
                TileEntityMacerator anvil = (TileEntityMacerator) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo
                        .horizontal()
                        .text(ChatFormatting.GRAY + Localization.translate("iu.primitive_anvil_durability") + " " + anvil.durability);
                cropInfo.progress(anvil.progress, 100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 100)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntityPrimalWireInsulator) {
                TileEntityPrimalWireInsulator anvil = (TileEntityPrimalWireInsulator) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo.progress(anvil.progress, 100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 100)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntityRollingMachine) {
                TileEntityRollingMachine anvil = (TileEntityRollingMachine) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo.progress(anvil.progress, 100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 100)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntityPrimalLaserPolisher) {
                TileEntityPrimalLaserPolisher anvil = (TileEntityPrimalLaserPolisher) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo.progress(anvil.progress, 100,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 100)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntitySqueezer) {
                TileEntitySqueezer anvil = (TileEntitySqueezer) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
                cropInfo.progress(anvil.progress, 150,
                        probeInfo.defaultProgressStyle()
                                .suffix(" / " + 150)
                                .showText(true)
                                .filledColor(0xFFFFA500)
                );

            }
            if (te instanceof TileEntityDryer) {
                TileEntityDryer anvil = (TileEntityDryer) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                IProbeInfo cropInfo = probeInfo.vertical();
                cropInfo
                        .horizontal()
                        .text(ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString);
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
                        cropInfo.horizontal().text(ChatFormatting.YELLOW + Localization.translate("iu.crop.oneprobe.soil")).item(
                                soil);
                    }

                    if (!crop.getDrops().isEmpty()) {
                        ItemStack stack = crop.getDrops().get(0);
                        if (!stack.isEmpty()) {
                            cropInfo
                                    .horizontal()
                                    .text(ChatFormatting.AQUA + Localization.translate("iu.crop.oneprobe.drop"))
                                    .item(stack);
                        }
                    }
                    cropInfo
                            .horizontal()
                            .text(ChatFormatting.GREEN + Localization.translate("iu.crop.oneprobe.using") + " " +
                                    Localization.translate("iu.crop.oneprobe.fertilizer") + " " + tileEntityCrop.getPestUse() + " / " + 40)
                            .item(new ItemStack(IUItem.fertilizer.getItem()));

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
                    cropInfo.text(ChatFormatting.LIGHT_PURPLE + Localization.translate("iu.crop.oneprobe.generation") + generation);
                    cropInfo
                            .horizontal()
                            .text(ChatFormatting.YELLOW + Localization.translate("iu.crop.oneprobe.genes") + tileEntityCrop
                                    .getGenome()
                                    .getGeneticTraitsMap()
                                    .values()
                                    .size());

                    boolean isWeed = crop.getId() == 3;
                    if (isWeed) {
                        cropInfo.text(ChatFormatting.RED + Localization.translate("iu.crop.oneprobe.weed_warning"));
                    }
                }
            }
            if (te instanceof TileEntityApiary) {
                TileEntityApiary apiary = (TileEntityApiary) te;

                if (apiary.getQueen() != null) {
                    IProbeInfo apiaryInfo = probeInfo.vertical();

                    IBee queen = apiary.getQueen();
                    apiaryInfo.text(ChatFormatting.GOLD + Localization.translate("iu.crop.oneprobe.queen") + ChatFormatting.BOLD + queen.getName());

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
                    apiaryInfo.text(ChatFormatting.GREEN + Localization.translate("iu.crop.oneprobe.workers") + apiary.workers);
                    apiaryInfo.text(ChatFormatting.YELLOW + Localization.translate("iu.crop.oneprobe.builders") + apiary.builders);
                    apiaryInfo.text(ChatFormatting.RED + Localization.translate("iu.crop.oneprobe.guards") + apiary.attacks);
                    apiaryInfo.text(ChatFormatting.BLUE + Localization.translate("iu.crop.oneprobe.medics") + apiary.doctors);
                    apiaryInfo.text(ChatFormatting.DARK_RED + Localization.translate("iu.crop.oneprobe.sick") + apiary.ill);
                    apiaryInfo.text(ChatFormatting.LIGHT_PURPLE + Localization.translate("iu.crop.oneprobe.new_bees") + apiary.birthBeeList.size());
                    String nameMainFlower = Localization.translate("crop." + queen.getCropFlower().getName());
                    apiaryInfo.text(ChatFormatting.AQUA + Localization.translate("iu.crop.oneprobe.main_flower") + nameMainFlower);
                    List<ItemStack> stacks = apiary.invSlotProduct;
                    IProbeInfo productInfo = apiaryInfo.horizontal();
                    productInfo.text(ChatFormatting.GOLD + Localization.translate("iu.crop.oneprobe.products"));

                    boolean hasProducts = false;
                    for (ItemStack stack : stacks) {
                        if (!stack.isEmpty()) {
                            productInfo.item(stack);
                            hasProducts = true;
                        }
                    }
                    if (!hasProducts) {
                        productInfo.text(ChatFormatting.DARK_GRAY + Localization.translate("iu.crop.oneprobe.no_resources"));
                    }
                    IProbeInfo framesInfo = apiaryInfo.horizontal();
                    framesInfo.text(ChatFormatting.GOLD + Localization.translate("iu.crop.oneprobe.frames"));

                    boolean hasFrames = false;
                    for (ItemStack stack : apiary.frameSlot) {
                        if (!stack.isEmpty()) {
                            framesInfo.item(stack);
                            hasFrames = true;
                        }
                    }
                    if (!hasFrames) {
                        framesInfo.text(ChatFormatting.DARK_GRAY + Localization.translate("iu.crop.oneprobe.no_frames"));
                    }
                    framesInfo = apiaryInfo.horizontal();
                    framesInfo.text(ChatFormatting.YELLOW + Localization.translate("iu.crop.oneprobe.genes_count") + apiary
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

                inputRow.text(ChatFormatting.YELLOW + Localization.translate("iu.probe.recipe.input"));
                outputRow.text(ChatFormatting.AQUA + Localization.translate("iu.probe.recipe.output"));

                for (int i = 0; i < component2.getSizeWorkingSlot(); i++) {
                    IProbeInfo inputSlot = inputRow.vertical();
                    IProbeInfo outputSlot = outputRow.vertical();

                    if (component2.getRecipeOutput(i) != null) {
                        ItemStack input = component2.inputSlots.get(i);
                        List<ItemStack> outputs = component2.getRecipeOutput(i).getRecipe().output.items;

                        if (!input.isEmpty()) {
                            inputSlot.item(input);
                        } else {
                            inputSlot.text(ChatFormatting.DARK_GRAY + Localization.translate("iu.probe.recipe.empty"));
                        }

                        if (!outputs.isEmpty()) {
                            int index = (int) (world.getGameTime() % outputs.size());
                            outputSlot.item(outputs.get(index));
                        } else {
                            outputSlot.text(ChatFormatting.DARK_GRAY + Localization.translate("iu.probe.recipe.empty"));
                        }
                    } else {
                        inputSlot.text(ChatFormatting.DARK_GRAY + Localization.translate("iu.probe.recipe.empty"));
                        outputSlot.text(ChatFormatting.DARK_GRAY + Localization.translate("iu.probe.recipe.empty"));
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
                            inputInfo.text(ChatFormatting.YELLOW + Localization.translate("iu.probe.recipe.input") + " ");
                            for (IInputItemStack input : inputs) {
                                int index = (int) (world.getGameTime() % input.getInputs().size());
                                inputInfo.item(input.getInputs().get(index));
                            }
                        }


                        if (!outputs.isEmpty()) {
                            IProbeInfo outputInfo = probeInfo.horizontal();
                            outputInfo.text(ChatFormatting.AQUA + Localization.translate("iu.probe.recipe.output") + " ");

                            int index = (int) (world.getGameTime() % outputs.size());
                            outputInfo.item(outputs.get(index));
                        }
                    }
                }

                probeInfo.text(ChatFormatting.GREEN + Localization.translate("iu.probe.recipe.progress") + " " + percentage + "%");
            }
            if (te instanceof IManufacturerBlock) {
                IManufacturerBlock manufacturerBlock = (IManufacturerBlock) te;
                probeInfo.text(Localization.translate("iu.manufacturer_level.info") + manufacturerBlock.getLevelMechanism() + "/" + 10);
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
            final List<String> stringList = deposits.getInformationFromMeta();
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
                    while (tanks.hasNext()){
                        Fluids.InternalFluidTank tank = tanks.next();
                        if (!tank.isEmpty()) {
                            FluidStack fluidStack = tank.getFluid();
                            Fluid fluid = fluidStack.getFluid();
                            int amount = fluidStack.getAmount();
                            int capacity = tank.getCapacity();
                            if (fluid == net.minecraft.world.level.material.Fluids.WATER)
                                fluid = FluidName.fluidwater.getInstance().get();
                            IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                            TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fluidStack));
                            int color = extensions.getTintColor();

                            probeInfo.horizontal().text(Localization.translate(fluid.getFluidType().getDescriptionId())+": "+String.format("§b%d / %d mB", amount, capacity));
                        } else {
                            probeInfo.text(Component.literal("§7"+Localization.translate("iu.probe.recipe.empty")));
                        }
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
    public static TextureAtlas getBlockTextureMap() {
        return (TextureAtlas) Minecraft.getInstance().getTextureManager().getTexture(InventoryMenu.BLOCK_ATLAS);
    }
}
