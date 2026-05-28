package com.denfop.blockentity.mechanism.multimechanism.simple;


import com.denfop.config.ModConfig;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockRaws;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine1Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.stream.StreamSupport;

public class BlockEntityCombMacerator extends BlockEntityMultiMachine {

    public static List<String> ores = new ArrayList<>();
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityCombMacerator(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.COMB_MACERATOR.usagePerTick,
                EnumMultiMachine.COMB_MACERATOR.lenghtOperation, BlockMoreMachine1Entity.comb_macerator, pos, state
        );
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, ModConfig.mechanismDouble("combined_macerator_soil_pollution_amount", 0.1D)));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, ModConfig.mechanismDouble("combined_macerator_air_pollution_amount", 0.15D)));
    }

    public static void addRecipe(String input, String output) {
        ResourceLocation input1 = ResourceLocation.parse("c:" + output);
        TagKey<Item> tag = ItemTags.create(input1);
        Iterable<Holder<Item>> holder = BuiltInRegistries.ITEM.getTagOrEmpty(tag);
        Optional<Item> maybeItem = StreamSupport.stream(holder.spliterator(), false)
                .map(Holder::value)
                .findFirst();
        ItemStack stack = ItemStack.EMPTY;
        if (maybeItem.isPresent()) {
            stack = new ItemStack(maybeItem.get());

        }
        if (!stack.isEmpty()) {
            stack.setCount(3);
            IUCore.get_comb_crushed.add(stack);

            final IInputHandler input2 = com.denfop.api.Recipes.inputFactory;
            Recipes.recipes.addRecipe(
                    "comb_macerator",
                    new BaseMachineRecipe(
                            new Input(input2.getInput("c:" + input)),
                            new RecipeOutput(null, stack)
                    )
            );
        }
    }

    public static void addmacerator(String input, String output, int n) {
        final IInputHandler input1 = Recipes.inputFactory;
        ItemStack stack = input1.getInput(output).getInputs().get(0).copy();
        stack.setCount(n);
        com.denfop.api.Recipes.recipes.addRecipe(
                "comb_macerator",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );


    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachine1Entity.comb_macerator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base1.getBlock(getTeBlock().getId());
    }

    public void init() {
        Set<String> ores = new HashSet<>();

        BuiltInRegistries.ITEM.getTags().forEach(tag -> {
            ResourceLocation tagId = tag.getFirst().location();
            String name = tagId.getPath();

            if (name.startsWith("crushed/") && !name.startsWith("purifiedcrushed/")) {
                String name1 = name.substring("crushed".length());
                if (name1.startsWith("/uranium")) return;

                name1 = "ores" + name1;
                final String name2 = "raw_materials" + name.substring("crushed".length());

                if (isTagNotEmpty(name1) && isTagNotEmpty(name)) {
                    if (isTagNotEmpty(name2)) {
                        if (!ores.contains(name)) {
                            addRecipe(name2, name);
                            ores.add(name);
                        }
                    } else {
                        if (!ores.contains(name)) {
                            addRecipe(name1, name);
                            ores.add(name);
                        }
                    }
                }
            }
        });
        for (int i = 0; i < BlockRaws.Type.values().length; i++) {
            addmacerator("c:storage_blocks/" + BlockRaws.Type.values()[i].getName(), "c:crushed/" + BlockRaws.Type.values()[i].name(), 27);
        }
        addmacerator("c:storage_blocks/raw_iron", "c:crushed/iron", 27);
        addmacerator("c:storage_blocks/raw_copper", "c:crushed/copper", 27);
        addmacerator("c:storage_blocks/raw_gold", "c:crushed/gold", 27);
    }

    private boolean isTagNotEmpty(String tagName) {
        return !BuiltInRegistries.ITEM.getTags().filter(tag -> tag.getFirst().location().getPath().equals(tagName)).toList().isEmpty();
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.COMB_MACERATOR;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockCombMacerator.name");
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
