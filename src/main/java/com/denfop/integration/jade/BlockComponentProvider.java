package com.denfop.integration.jade;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.bee.Bee;
import com.denfop.api.crop.Crop;
import com.denfop.api.energy.interfaces.EnergyConductor;
import com.denfop.api.energy.interfaces.EnergyTile;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.api.energy.networking.NodeStats;
import com.denfop.api.otherenergies.common.EnergyBase;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.otherenergies.common.interfaces.Conductor;
import com.denfop.api.otherenergies.common.interfaces.Tile;
import com.denfop.api.otherenergies.cool.ICoolSource;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.blockentity.base.*;
import com.denfop.blockentity.bee.BlockEntityApiary;
import com.denfop.blockentity.crop.TileEntityCrop;
import com.denfop.blockentity.mechanism.*;
import com.denfop.componets.*;
import com.denfop.recipe.IInputItemStack;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.IProgressStyle;

import java.util.ArrayList;
import java.util.List;

public class BlockComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {

    public static final BlockComponentProvider INSTANCE = new BlockComponentProvider();


    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Constants.MOD_ID, "component_provider_blockentity");
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        IElementHelper elements = IElementHelper.get();
        CompoundTag data = blockAccessor.getServerData();
        if (data.contains("info")) {
            ListTag listTag = data.getList("info", 10);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag compoundTag = listTag.getCompound(i);
                writeData(compoundTag, iTooltip, blockAccessor, iPluginConfig, elements);
            }
        }
    }

    private void writeData(CompoundTag compoundTag, ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig, IElementHelper elements) {
        Object o = decode(compoundTag);
        if (o instanceof String) {
            iTooltip.add(elements.text(Component.literal((String) o)));
        } else if (o instanceof Double) {
            iTooltip.add(elements.text(Component.literal(String.valueOf((Double) o))));
        } else if (o instanceof Integer) {
            iTooltip.add(elements.text(Component.literal(String.valueOf((Integer) o))));
        } else if (o instanceof Progress) {
            Progress progressType = (Progress) o;
            float progress = progressType.getProgress() * 1f / progressType.getMax();
            Component label = Component.literal(progressType.getPrefix() + ChatFormatting.WHITE + "" + progressType.getProgress() + " " + progressType.getSuffix());
            IProgressStyle style = iTooltip.getElementHelper().progressStyle()
                    .color(ModUtils.convertRGBAcolorToInt(progressType.getR(), progressType.getG(), progressType.getB()), ModUtils.convertRGBAcolorToInt(progressType.getR(), progressType.getG(), progressType.getB()));
            iTooltip.add(elements.progress(progress, label, style, BoxStyle.DEFAULT, false));
        } else if (o instanceof ItemStack) {
            iTooltip.add(elements.item((ItemStack) o));
        }
    }

    public Object decode(CompoundTag compoundTag) {
        byte type = compoundTag.getByte("type_field");

        switch (type) {
            case 0:
                return compoundTag.getString("value_field");
            case 1:
                return compoundTag.getInt("value_field");
            case 2:
                return compoundTag.getDouble("value_field");
            case 3:
                return ItemStack.of(compoundTag.getCompound("value_field"));
            case 4:
                return compoundTag.getShort("value_field");
            case 5:
                int progress = compoundTag.getInt("value_field");
                int max = compoundTag.getInt("value_field1");
                String suffix = compoundTag.getString("value_field2");
                int r = compoundTag.getInt("value_field3");
                int g = compoundTag.getInt("value_field4");
                int b = compoundTag.getInt("value_field5");
                String prefix = compoundTag.getString("value_field7");
                return new Progress(progress, max, suffix, r, g, b, prefix);
            default:
                return null;
        }
    }


    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer entityPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BlockEntityBase) {
            ListTag listTag = new ListTag();
            BlockEntityBase te = (BlockEntityBase) blockEntity;
            if (te.wrenchCanRemove(entityPlayer)) {
                encode(ChatFormatting.WHITE + Localization.translate("iu.wrench.info"), listTag);
            }
            final ComponentProgress component = te.getComp(ComponentProgress.class);
            final ComponentProcess component1 = te.getComp(ComponentProcess.class);

            ProcessMultiComponent component2 = te.getComp(ProcessMultiComponent.class);
            if (te instanceof BlockEntityAnvil) {
                BlockEntityAnvil anvil = (BlockEntityAnvil) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                encode((ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString), listTag);
                encode((ChatFormatting.GRAY + Localization.translate("iu.primitive_anvil_durability") + " " + anvil.durability), listTag);
                encode(new Progress(anvil.progress, 100, (" / " + 100), 0x005AFFFF), listTag);


            }
            Level world = level;
            if (te instanceof EnergyConductor) {
                final NodeStats node = EnergyNetGlobal.instance.getNodeStats((EnergyTile) te, level);
                encode(new Progress((int) node.getEnergyOut(), (int) ((EnergyConductor) te).getConductorBreakdownEnergy(), ("EF"), 33, 91, 199), listTag);
            }
            if (te instanceof Conductor) {
                Conductor conductor = (Conductor) te;
                if (conductor.hasEnergies()) {
                    for (EnergyType type : conductor.getEnergies()) {
                        NodeStats node = EnergyBase.getGlobal(type).getNodeStats((Tile) te, world);
                        if (node != null) {

                            if (type == EnergyType.QUANTUM) {
                                encode(new Progress((int) node.getEnergyOut(), (int) conductor.getConductorBreakdownEnergy(type), ("QE"), 91, 94, 98), listTag);
                            } else if (type == EnergyType.SOLARIUM) {

                                encode(new Progress((int) node.getEnergyOut(), (int) conductor.getConductorBreakdownEnergy(type), ("SE"), 224, 212, 18), listTag);


                            } else if (type == EnergyType.EXPERIENCE) {

                                encode(new Progress((int) node.getEnergyOut(), (int) conductor.getConductorBreakdownEnergy(type), ("EE"), 76, 172, 32), listTag);


                            } else if (type == EnergyType.RADIATION) {


                                encode(new Progress((int) node.getEnergyOut(), (int) conductor.getConductorBreakdownEnergy(type), ("☢"), 42, 196, 45), listTag);
                            } else if (type == EnergyType.POSITRONS) {


                                encode(new Progress((int) node.getEnergyOut(), (int) conductor.getConductorBreakdownEnergy(type), ("e⁺"), 192, 0, 218), listTag);

                            }
                        }
                    }
                } else {
                    final EnergyType type = conductor.getEnergyType();
                    NodeStats node = EnergyBase.getGlobal(type).getNodeStats((Tile) te, world);
                    if (type == EnergyType.QUANTUM) {

                        encode(new Progress((int) node.getEnergyOut(), (int) conductor.getConductorBreakdownEnergy(type), ("QE"), 91, 94, 98), listTag);

                    } else if (type == EnergyType.SOLARIUM) {


                        encode(new Progress((int) node.getEnergyOut(), (int) conductor.getConductorBreakdownEnergy(type), ("SE"), 224, 212, 18), listTag);

                    } else if (type == EnergyType.EXPERIENCE) {


                        encode(new Progress((int) node.getEnergyOut(), (int) conductor.getConductorBreakdownEnergy(type), ("EE"), 76, 172, 32), listTag);

                    } else if (type == EnergyType.RADIATION) {


                        encode(new Progress((int) node.getEnergyOut(), (int) conductor.getConductorBreakdownEnergy(type), ("☢"), 42, 196, 45), listTag);

                    } else if (type == EnergyType.POSITRONS) {


                        encode(new Progress((int) node.getEnergyOut(), (int) conductor.getConductorBreakdownEnergy(type), ("e⁺"), 192, 0, 218), listTag);

                    }
                }
            }
            if (te instanceof BlockEntityStrongAnvil) {
                BlockEntityStrongAnvil anvil = (BlockEntityStrongAnvil) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                encode((ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString), listTag);
                encode(new Progress(anvil.progress, 100, (" / " + 100), 0x005AFFFF), listTag);

            }
            if (te instanceof TileEntityCrop) {
                TileEntityCrop tileEntityCrop = (TileEntityCrop) te;
                if (tileEntityCrop.getCrop() != null) {
                    Crop crop = tileEntityCrop.getCrop();


                    int tick = crop.getTick();
                    int maxTick = crop.getMaxTick();
                    encode(new Progress(tick, maxTick, " / " + maxTick + " " + Localization.translate("iu.crop.oneprobe.growth"), 0xFFFFA500), listTag);

                    ItemStack soil = crop.getSoil().getStack();
                    if (!soil.isEmpty()) {
                        encode(ChatFormatting.YELLOW + Localization.translate("iu.crop.oneprobe.soil"), listTag);
                        encode(soil, listTag);
                    }

                    if (!crop.getDrops().isEmpty()) {
                        ItemStack stack = crop.getDrops().get(0);
                        if (!stack.isEmpty()) {
                            encode(ChatFormatting.AQUA + Localization.translate("iu.crop.oneprobe.drop"), listTag);
                            encode(stack, listTag);
                        }
                    }
                    encode(ChatFormatting.GREEN + Localization.translate("iu.crop.oneprobe.using") + " " +
                            Localization.translate("iu.crop.oneprobe.fertilizer") + " " + tileEntityCrop.getPestUse() + " / " + 40, listTag);
                    encode(new ItemStack(IUItem.fertilizer.getItem()), listTag);

                    int pesticidesTime = tileEntityCrop.getTickPest();
                    int maxPesticidesTime = 7000;
                    encode(Localization.translate("iu.crop.oneprobe.pesticide_time"), listTag);
                    encode(new Progress(pesticidesTime == 0 ? pesticidesTime : maxPesticidesTime - pesticidesTime, maxPesticidesTime, (" / " + maxPesticidesTime + " t"), 0xFF00FF00), listTag);

                    int generation = crop.getGeneration();
                    encode(ChatFormatting.LIGHT_PURPLE + Localization.translate("iu.crop.oneprobe.generation") + generation, listTag);
                    encode(ChatFormatting.YELLOW + Localization.translate("iu.crop.oneprobe.genes") + tileEntityCrop
                            .getGenome()
                            .getGeneticTraitsMap()
                            .values()
                            .size(), listTag);

                    boolean isWeed = crop.getId() == 3;
                    if (isWeed) {
                        encode(ChatFormatting.RED + Localization.translate("iu.crop.oneprobe.weed_warning"), listTag);
                    }
                }
            }
            if (te instanceof BlockEntityApiary) {
                BlockEntityApiary apiary = (BlockEntityApiary) te;

                if (apiary.getQueen() != null) {

                    Bee queen = apiary.getQueen();
                    encode(ChatFormatting.GOLD + Localization.translate("iu.crop.oneprobe.queen") + ChatFormatting.BOLD + queen.getName(), listTag);
                    encode(new Progress((int) apiary.food, (int) apiary.maxFood, " / " + (int) apiary.maxFood + " " + Localization.translate("iu.crop.oneprobe.honey"), 0xFFFFA500), listTag);
                    encode(new Progress((int) apiary.royalJelly, (int) apiary.maxJelly, " / " + (int) apiary.maxJelly + " " + Localization.translate(
                            "iu.crop.oneprobe.royal_jelly"), ModUtils.convertRGBAcolorToInt(146, 146, 146)), listTag);

                    encode(ChatFormatting.GREEN + Localization.translate("iu.crop.oneprobe.workers") + apiary.workers, listTag);
                    encode(ChatFormatting.YELLOW + Localization.translate("iu.crop.oneprobe.builders") + apiary.builders, listTag);
                    encode(ChatFormatting.RED + Localization.translate("iu.crop.oneprobe.guards") + apiary.attacks, listTag);
                    encode(ChatFormatting.BLUE + Localization.translate("iu.crop.oneprobe.medics") + apiary.doctors, listTag);
                    encode(ChatFormatting.DARK_RED + Localization.translate("iu.crop.oneprobe.sick") + apiary.ill, listTag);
                    encode(ChatFormatting.LIGHT_PURPLE + Localization.translate("iu.crop.oneprobe.new_bees") + apiary.birthBeeList.size(), listTag);
                    String nameMainFlower = Localization.translate("crop." + queen.getCropFlower().getName());
                    encode(ChatFormatting.AQUA + Localization.translate("iu.crop.oneprobe.main_flower") + nameMainFlower, listTag);
                    List<ItemStack> stacks = apiary.invSlotProduct;
                    encode(ChatFormatting.GOLD + Localization.translate("iu.crop.oneprobe.products"), listTag);

                    boolean hasProducts = false;
                    for (ItemStack stack : stacks) {
                        if (!stack.isEmpty()) {
                            encode(stack, listTag);
                            hasProducts = true;
                        }
                    }
                    if (!hasProducts) {
                        encode(ChatFormatting.DARK_GRAY + Localization.translate("iu.crop.oneprobe.no_resources"), listTag);
                    }
                    encode(ChatFormatting.GOLD + Localization.translate("iu.crop.oneprobe.frames"), listTag);

                    boolean hasFrames = false;
                    for (ItemStack stack : apiary.frameSlot) {
                        if (!stack.isEmpty()) {
                            encode(stack, listTag);
                            hasFrames = true;
                        }
                    }
                    if (!hasFrames) {
                        encode(ChatFormatting.DARK_GRAY + Localization.translate("iu.crop.oneprobe.no_frames"), listTag);
                    }
                    encode(ChatFormatting.YELLOW + Localization.translate("iu.crop.oneprobe.genes_count") + apiary
                            .getGenome()
                            .getGeneticTraitsMap()
                            .values()
                            .size(), listTag);

                }
            }
            if (te instanceof BlockEntityCompressor) {
                BlockEntityCompressor anvil = (BlockEntityCompressor) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                encode((ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString), listTag);
                encode((ChatFormatting.GRAY + Localization.translate("iu.primitive_anvil_durability") + " " + anvil.durability), listTag);
                encode(new Progress(anvil.progress, 100, (" / " + 100), 0x005AFFFF), listTag);


            }
            if (te instanceof BlockEntityMacerator) {
                BlockEntityMacerator anvil = (BlockEntityMacerator) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                encode((ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString), listTag);
                encode((ChatFormatting.GRAY + Localization.translate("iu.primitive_anvil_durability") + " " + anvil.durability), listTag);
                encode(new Progress(anvil.progress, 100, (" / " + 100), 0x005AFFFF), listTag);


            }
            if (te instanceof BlockEntityPrimalWireInsulator) {
                BlockEntityPrimalWireInsulator anvil = (BlockEntityPrimalWireInsulator) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                encode((ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString), listTag);
                encode(new Progress(anvil.progress, 100, (" / " + 100), 0x005AFFFF), listTag);


            }
            if (te instanceof BlockEntityRollingMachine) {
                BlockEntityRollingMachine anvil = (BlockEntityRollingMachine) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                encode((ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString), listTag);
                encode(new Progress(anvil.progress, 100, (" / " + 100), 0x005AFFFF), listTag);


            }
            if (te instanceof BlockEntityPrimalLaserPolisher) {
                BlockEntityPrimalLaserPolisher anvil = (BlockEntityPrimalLaserPolisher) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                encode((ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString), listTag);
                encode(new Progress(anvil.progress, 100, (" / " + 100), 0x005AFFFF), listTag);


            }
            if (te instanceof BlockEntitySqueezer) {
                BlockEntitySqueezer anvil = (BlockEntitySqueezer) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                encode((ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString), listTag);
                encode(new Progress(anvil.progress, 150, (" / " + 150), 0x005AFFFF), listTag);


            }
            if (te instanceof BlockEntityDryer) {
                BlockEntityDryer anvil = (BlockEntityDryer) te;
                final Double percent = anvil.data.getOrDefault(entityPlayer.getUUID(), 0.0);
                final String percentString = String.format("%.1f", percent);
                encode((ChatFormatting.GREEN + Localization.translate("iu.primitive_master") + " " + percentString), listTag);
                encode(new Progress(anvil.progress, 100, (" / " + 100), 0x005AFFFF), listTag);


            }
            if (te instanceof IManufacturerBlock) {
                IManufacturerBlock manufacturerBlock = (IManufacturerBlock) te;
                encode(ChatFormatting.WHITE + Localization.translate("iu.manufacturer_level.info") + manufacturerBlock.getLevelMechanism() + "/" + 10, listTag);
            }
            if (component != null) {
                double progress = component.getBar();
                int percentage = (int) (progress * 100);
                encode(new Progress(component.getProgress(), component.getMaxValue(), "t", 255, 255, 255), listTag);
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
                            encode(ChatFormatting.YELLOW + Localization.translate("iu.probe.recipe.input") + " ", listTag);
                            for (IInputItemStack input : inputs) {
                                int index = (int) (level.getGameTime() % input.getInputs().size());
                                encode(input.getInputs().get(index), listTag);
                            }
                        }


                        if (!outputs.isEmpty()) {
                            encode(ChatFormatting.AQUA + Localization.translate("iu.probe.recipe.output") + " ", listTag);

                            int index = (int) (level.getGameTime() % outputs.size());
                            encode(outputs.get(index), listTag);
                        }
                    }
                }

                encode(ChatFormatting.GREEN + Localization.translate("iu.probe.recipe.progress") + " " + percentage + "%", listTag);
            }
            if (te instanceof BlockEntityInventory) {
                List<String> stringList = new ArrayList<>();
                ((BlockEntityInventory) te).addInformation(((BlockEntityInventory) te).getPickBlock(entityPlayer, null), stringList
                );
                for (String s : stringList) {
                    encode(s, listTag);
                }
                for (AbstractComponent comp : te.getComponentList()) {
                    if (comp instanceof Energy) {
                        encode(new Progress((int) ((Energy) comp).getEnergy(), (int) ((Energy) comp).getCapacity(), ("EF"), 33, 91, 199), listTag);
                    }

                    if (comp instanceof ComponentBaseEnergy) {
                        ComponentBaseEnergy componentBaseEnergy = (ComponentBaseEnergy) comp;
                        if (componentBaseEnergy.getType() == EnergyType.QUANTUM) {
                            encode(new Progress((int) ((ComponentBaseEnergy) comp).getEnergy(), (int) ((ComponentBaseEnergy) comp).getCapacity(), ("QE"), 91, 94, 98), listTag);
                        } else if (componentBaseEnergy.getType() == EnergyType.SOLARIUM) {
                            encode(new Progress((int) ((ComponentBaseEnergy) comp).getEnergy(), (int) ((ComponentBaseEnergy) comp).getCapacity(), ("SE"), 224, 212, 18), listTag);
                        } else if (componentBaseEnergy.getType() == EnergyType.EXPERIENCE) {

                            encode(new Progress((int) ((ComponentBaseEnergy) comp).getEnergy(), (int) ((ComponentBaseEnergy) comp).getCapacity(), ("EE"), 76, 172, 32), listTag);


                        } else if (componentBaseEnergy.getType() == EnergyType.RADIATION) {


                            encode(new Progress((int) ((ComponentBaseEnergy) comp).getEnergy(), (int) ((ComponentBaseEnergy) comp).getCapacity(), ("☢"), 42, 196, 45), listTag);

                        } else if (componentBaseEnergy.getType() == EnergyType.POSITRONS) {


                            encode(new Progress((int) ((ComponentBaseEnergy) comp).getEnergy(), (int) ((ComponentBaseEnergy) comp).getCapacity(), ("e⁺"), 192, 0, 218), listTag);

                        }

                    }
                    if (comp instanceof CoolComponent) {
                        CoolComponent coolComponent = (CoolComponent) comp;
                        boolean isRefrigerator = coolComponent.delegate instanceof ICoolSource;
                        if (!coolComponent.upgrade) {
                            if (!isRefrigerator) {
                                encode(new Progress((int) coolComponent.getEnergy(), (int) coolComponent.getCapacity(), ("°C"), 190, 23, 20, Localization.translate("iu.temperature")), listTag);
                            } else {
                                encode(new Progress((int) coolComponent.getEnergy(), (int) coolComponent.getCapacity(), ("°C"), 33, 98, 208, Localization.translate("iu.temperature") + (coolComponent.getEnergy() > 0 ?
                                        "-" : "")), listTag);
                            }
                        }
                    }
                    if (comp instanceof HeatComponent) {
                        HeatComponent heatComponent = (HeatComponent) comp;
                        encode(new Progress((int) (heatComponent).getEnergy(), (int) (heatComponent).getCapacity(), ("°C"), 208, 61, 33, Localization.translate("iu.temperature")), listTag);
                    }
                }
            }
            compoundTag.put("info", listTag);
        }

    }

    public void encode(Object o, ListTag listTag) {
        CompoundTag compoundTag = new CompoundTag();
        if (o instanceof String) {
            compoundTag.putByte("type_field", (byte) 0);
            compoundTag.putString("value_field", (String) o);
        } else if (o instanceof Integer) {
            compoundTag.putByte("type_field", (byte) 1);
            compoundTag.putInt("value_field", (int) o);
        } else if (o instanceof Double) {
            compoundTag.putByte("type_field", (byte) 2);
            compoundTag.putDouble("value_field", (double) o);
        } else if (o instanceof ItemStack) {
            compoundTag.putByte("type_field", (byte) 3);
            compoundTag.put("value_field", ((ItemStack) o).save(new CompoundTag()));
        } else if (o instanceof Short) {
            compoundTag.putByte("type_field", (byte) 4);
            compoundTag.putShort("value_field", (short) o);
        } else if (o instanceof Progress) {
            compoundTag.putByte("type_field", (byte) 5);
            Progress progress = (Progress) o;
            compoundTag.putInt("value_field", progress.getProgress());
            compoundTag.putInt("value_field1", progress.getMax());
            compoundTag.putString("value_field2", progress.getSuffix());
            compoundTag.putInt("value_field3", progress.getR());
            compoundTag.putInt("value_field4", progress.getG());
            compoundTag.putInt("value_field5", progress.getB());
            compoundTag.putString("value_field7", progress.getPrefix());
        }
        listTag.add(compoundTag);
    }
}
