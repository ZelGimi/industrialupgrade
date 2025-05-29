package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TileCombMacerator extends TileMultiMachine {

    public static List<String> ores = new ArrayList<>();
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileCombMacerator(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.COMB_MACERATOR.usagePerTick,
                EnumMultiMachine.COMB_MACERATOR.lenghtOperation, BlockMoreMachine1.comb_macerator, pos, state
        );
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.15));
    }

    public static void addRecipe(String input, String output) {
        ResourceLocation input1 = new ResourceLocation("forge:" + output);
        TagKey<Item> tag = ItemTags.create(input1);
        Iterable<Holder<Item>> holder = BuiltInRegistries.ITEM.getTagOrEmpty(tag);
        ItemStack stack = new ItemStack(holder.iterator().next());

        if (!stack.isEmpty()) {
            stack.setCount(3);
            IUCore.get_comb_crushed.add(stack);

            final IInputHandler input2 = com.denfop.api.Recipes.inputFactory;
            Recipes.recipes.addRecipe(
                    "comb_macerator",
                    new BaseMachineRecipe(
                            new Input(input2.getInput("forge:" + input)),
                            new RecipeOutput(null, stack)
                    )
            );
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine1.comb_macerator;
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
