package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockClassicOre;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileBaseHandlerHeavyOre;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class TileHandlerHeavyOre extends TileBaseHandlerHeavyOre implements IHasRecipe {


    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileHandlerHeavyOre(BlockPos pos, BlockState state) {
        super(EnumTypeStyle.DEFAULT, BlockBaseMachine1.handler_ho, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.5));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 1));
    }

    public static void addhandlerore(ItemStack container, ItemStack[] output, short temperature, int... col) {
        CompoundTag nbt = ModUtils.nbt();
        nbt.putShort("temperature", temperature);
        for (int i = 0; i < col.length; i++) {
            nbt.putInt("input" + i, col[i]);
        }
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "handlerho",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(container)
                        ),
                        new RecipeOutput(nbt, output)
                )
        );


    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine1.handler_ho;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine.getBlock(getTeBlock().getId());
    }

    public void init() {
        addhandlerore(
                new ItemStack(IUItem.heavyore.getItem(0)),
                new ItemStack[]{new ItemStack(Blocks.IRON_ORE), new ItemStack(Blocks.GOLD_ORE), new ItemStack(IUItem.ore2.getItem(4))},
                (short) 1500, 60, 25, 15
        );
        addhandlerore(new ItemStack(IUItem.heavyore.getItem(1)), new ItemStack[]{new ItemStack(IUItem.ore.getItem(7)),
                        new ItemStack(Blocks.GOLD_ORE), new ItemStack(Blocks.COPPER_ORE)},
                (short) 3000, 28, 44, 28
        );
        addhandlerore(new ItemStack(IUItem.heavyore.getItem(2)), new ItemStack[]{new ItemStack(IUItem.ore.getItem(11)),
                IUItem.classic_ore.getItemStack(BlockClassicOre.Type.lead)}, (short) 5000, 13, 87);
        addhandlerore(
                new ItemStack(IUItem.heavyore.getItem(3)),
                new ItemStack[]{new ItemStack(IUItem.ore.getItem(8)), new ItemStack(IUItem.ore.getItem(6))},
                (short) 4000, 44, 56
        );
        addhandlerore(
                new ItemStack(IUItem.heavyore.getItem(4)),
                new ItemStack[]{new ItemStack(Blocks.IRON_ORE), new ItemStack(IUItem.ore.getItem(4)), IUItem.smallSulfurDust},
                (short) 2500, 80, 15, 5
        );
        addhandlerore(
                new ItemStack(IUItem.heavyore.getItem(5)),
                new ItemStack[]{new ItemStack(Blocks.NETHER_QUARTZ_ORE), new ItemStack(
                        IUItem.ore.getItem(12),
                        1
                )},
                (short) 2500, 84, 16
        );
        addhandlerore(
                new ItemStack(IUItem.heavyore.getItem(6)),
                new ItemStack[]{IUItem.classic_ore.getItemStack(BlockClassicOre.Type.uranium),
                        new ItemStack(IUItem.toriyore.getItem(0))},
                (short) 4500, 84, 16
        );
        addhandlerore(
                new ItemStack(IUItem.heavyore.getItem(7)),
                new ItemStack[]{new ItemStack(Blocks.COPPER_ORE), new ItemStack(Blocks.LAPIS_ORE),
                        new ItemStack(Blocks.REDSTONE_ORE)},
                (short) 2000, 55, 23, 21
        );

        addhandlerore(new ItemStack(IUItem.heavyore.getItem(8)), new ItemStack[]{new ItemStack(IUItem.ore.getItem(13)),
                        new ItemStack(IUItem.ore.getItem(5)), new ItemStack(Blocks.IRON_ORE)}, (short) 3000
                , 44, 28, 28);
        addhandlerore(
                new ItemStack(IUItem.heavyore.getItem(9)),
                new ItemStack[]{new ItemStack(IUItem.ore.getItem(4)), new ItemStack(IUItem.ore.getItem(6))},
                (short) 3500, 50, 50
        );
        addhandlerore(new ItemStack(IUItem.heavyore.getItem(10)),
                new ItemStack[]{new ItemStack(IUItem.ore.getItem(8)),
                        new ItemStack(IUItem.toriyore.getItem(0)), IUItem.classic_ore.getItemStack(BlockClassicOre.Type.uranium)},
                (short) 3000,
                50,
                25
                ,
                25
        );
        addhandlerore(
                new ItemStack(IUItem.crafting_elements.getStack(498)),
                new ItemStack[]{new ItemStack(IUItem.crafting_elements.getStack(499))},
                (short) 3000,
                75
        );

        addhandlerore(new ItemStack(IUItem.heavyore.getItem(11)), new ItemStack[]{new ItemStack(IUItem.ore.getItem(12)),
                new ItemStack(Blocks.COAL_ORE)}, (short) 4000, 65, 35);

        addhandlerore(new ItemStack(IUItem.heavyore.getItem(12)), new ItemStack[]{new ItemStack(IUItem.ore.getItem(8)),
                new ItemStack(Blocks.IRON_ORE)}, (short) 4500, 47, 53);
        addhandlerore(new ItemStack(IUItem.heavyore.getItem(13)), new ItemStack[]{new ItemStack(IUItem.ore.getItem(13)),
                new ItemStack(IUItem.ore.getItem(5)), new ItemStack(IUItem.ore.getItem(1))}, (short) 4000, 66, 17, 17);
        addhandlerore(new ItemStack(IUItem.heavyore.getItem(14)), new ItemStack[]{new ItemStack(Blocks.IRON_ORE),
                new ItemStack(IUItem.ore.getItem(5))}, (short) 4000, 60, 40);
        addhandlerore(new ItemStack(IUItem.heavyore.getItem(15)), new ItemStack[]{new ItemStack(IUItem.ore.getItem(3)),
                IUItem.tinOre}, (short) 4000, 80, 20);
        addhandlerore(
                new ItemStack(IUItem.ore2.getItem(6)),
                new ItemStack[]{new ItemStack(IUItem.crafting_elements.getStack(463)), new ItemStack(IUItem.crafting_elements.getStack(461)),
                        new ItemStack(IUItem.crafting_elements.getStack(462))},
                (short) 1000, 50, 5, 45
        );
        addhandlerore(
                new ItemStack(IUItem.ore2.getItem(7)),
                new ItemStack[]{new ItemStack(IUItem.crafting_elements.getStack(481), 2), new ItemStack(IUItem.crafting_elements.getStack(481)),
                        new ItemStack(IUItem.crafting_elements.getStack(481))},
                (short) 1000, 100, 50, 25
        );
        addhandlerore(
                new ItemStack(IUItem.mineral.getItem(13)),
                new ItemStack[]{new ItemStack(Blocks.DIAMOND_ORE, 1), new ItemStack(Blocks.EMERALD_ORE, 1),},
                (short) 5000, 35, 15
        );

        addhandlerore(
                new ItemStack(IUItem.mineral.getItem(0)),
                new ItemStack[]{new ItemStack(IUItem.ore3.getItem(0), 1), new ItemStack(Blocks.IRON_ORE, 1), new ItemStack(
                        IUItem.ore.getItem(6),
                        1
                )},
                (short) 3000, 55, 35, 10
        );
        addhandlerore(
                new ItemStack(IUItem.mineral.getItem(1)),
                new ItemStack[]{new ItemStack(IUItem.ore3.getItem(10)), new ItemStack(IUItem.ore3.getItem(11))},
                (short) 3500, 60, 40
        );
        addhandlerore(
                new ItemStack(IUItem.mineral.getItem(2)),
                new ItemStack[]{new ItemStack(IUItem.ore3.getItem(7)), new ItemStack(IUItem.ore.getItem(3))},
                (short) 3500, 50, 50
        );

        addhandlerore(
                new ItemStack(IUItem.mineral.getItem(3)),
                new ItemStack[]{new ItemStack(IUItem.ore.getItem(15)), new ItemStack(
                        Blocks.COPPER_ORE
                ), new ItemStack(IUItem.ore3.getItem(4)
                )},
                (short) 4000, 60, 20, 20
        );
        addhandlerore(
                new ItemStack(IUItem.mineral.getItem(4)),
                new ItemStack[]{new ItemStack(IUItem.ore2.getItem(4)), new ItemStack(IUItem.ore.getItem(13)), new ItemStack(IUItem.ore3.getItem(9))},
                (short) 4000, 50, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.mineral.getItem(5)),
                new ItemStack[]{new ItemStack(IUItem.ore3.getItem(13)), new ItemStack(IUItem.classic_ore.getItem(2))},
                (short) 4000, 50, 50
        );
        addhandlerore(
                new ItemStack(IUItem.mineral.getItem(6)),
                new ItemStack[]{new ItemStack(IUItem.ore3.getItem(6)), new ItemStack(IUItem.ore3.getItem(3))},
                (short) 4000, 70, 30
        );
        addhandlerore(
                new ItemStack(IUItem.mineral.getItem(7)),
                new ItemStack[]{new ItemStack(IUItem.ore2.getItem(3)), new ItemStack(IUItem.ore.getItem(14))},
                (short) 4000, 65, 35
        );
        addhandlerore(
                new ItemStack(IUItem.mineral.getItem(8)),
                new ItemStack[]{new ItemStack(IUItem.ore2.getItem(5)), new ItemStack(IUItem.ore3.getItem(0))},
                (short) 4000, 70, 30
        );
        addhandlerore(
                new ItemStack(IUItem.mineral.getItem(9)),
                new ItemStack[]{new ItemStack(IUItem.ore3.getItem(12)), new ItemStack(IUItem.ore3.getItem(2)), new ItemStack(
                        IUItem.ore3.getItem(
                                0)
                )},
                (short) 4000, 50, 20, 30
        );
        addhandlerore(
                new ItemStack(IUItem.mineral.getItem(10)),
                new ItemStack[]{new ItemStack(IUItem.ore3.getItem(8)), new ItemStack(IUItem.ore3.getItem(9))},
                (short) 4000, 70, 30
        );
        addhandlerore(
                new ItemStack(IUItem.mineral.getItem(11)),
                new ItemStack[]{new ItemStack(IUItem.ore3.getItem(12)), new ItemStack(IUItem.ore3.getItem(1))},
                (short) 4000, 70, 30
        );
        addhandlerore(
                new ItemStack(IUItem.mineral.getItem(12)),
                new ItemStack[]{new ItemStack(IUItem.ore3.getItem(14)), new ItemStack(IUItem.ore3.getItem(5))},
                (short) 4000, 70, 30
        );

        addhandlerore(
                new ItemStack(IUItem.space_ore1.getItem(5), 1),
                new ItemStack[]{new ItemStack(IUItem.apatite.getItem(0), 1), new ItemStack(IUItem.apatite.getItem(1), 1)},
                (short) 4000, 50, 50
        );

        addhandlerore(
                new ItemStack(IUItem.space_ore1.getItem(3), 1),
                new ItemStack[]{new ItemStack(IUItem.apatite.getItem(2), 1), new ItemStack(IUItem.apatite.getItem(3), 1),
                        new ItemStack(IUItem.apatite.getItem(4), 1)},
                (short) 4000, 33, 33, 33
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone.getItem(), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore.getItem(), 1), new ItemStack(IUItem.spaceItem.getStack(30), 2),
                        new ItemStack(IUItem.spaceItem.getStack(30), 1)},
                (short) 4000, 10, 50, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone.getItem(1), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore.getItem(1), 1),
                        new ItemStack(IUItem.space_ore.getItem(2), 1), new ItemStack(IUItem.spaceItem.getStack(30), 2),
                        new ItemStack(IUItem.spaceItem.getStack(30), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone.getItem(2), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore.getItem(3), 1),
                        new ItemStack(IUItem.space_ore.getItem(4), 1), new ItemStack(IUItem.space_ore.getItem(5), 1)},
                (short) 4000, 35, 35, 30
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone.getItem(3), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore.getItem(6), 1),
                        new ItemStack(IUItem.space_ore.getItem(7), 1), new ItemStack(IUItem.spaceItem.getStack(33), 2),
                        new ItemStack(IUItem.spaceItem.getStack(33), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone.getItem(4), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore.getItem(8), 1),
                        new ItemStack(IUItem.space_ore.getItem(9), 1), new ItemStack(IUItem.spaceItem.getStack(34), 2),
                        new ItemStack(IUItem.spaceItem.getStack(34), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone.getItem(5), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore.getItem(10), 1), new ItemStack(IUItem.spaceItem.getStack(35), 2),
                        new ItemStack(IUItem.spaceItem.getStack(35), 1)},
                (short) 4000, 10, 50, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone.getItem(6), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore.getItem(11), 1),
                        new ItemStack(IUItem.space_ore.getItem(12), 1), new ItemStack(IUItem.spaceItem.getStack(36), 2),
                        new ItemStack(IUItem.spaceItem.getStack(36), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone.getItem(7), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore.getItem(13), 1), new ItemStack(IUItem.spaceItem.getStack(37), 2),
                        new ItemStack(IUItem.spaceItem.getStack(37), 1)},
                (short) 4000, 25, 50, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone.getItem(8), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore.getItem(14), 1),
                        new ItemStack(IUItem.space_ore.getItem(15), 1), new ItemStack(IUItem.spaceItem.getStack(38), 2),
                        new ItemStack(IUItem.spaceItem.getStack(38), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone.getItem(9), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore1.getItem(), 1),
                        new ItemStack(IUItem.space_ore1.getItem(1), 1), new ItemStack(IUItem.space_ore1.getItem(2), 1)},
                (short) 4000, 35, 35, 30
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone.getItem(10), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore1.getItem(4), 1),
                        new ItemStack(IUItem.space_ore1.getItem(3), 1), new ItemStack(IUItem.spaceItem.getStack(40), 2),
                        new ItemStack(IUItem.spaceItem.getStack(40), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone.getItem(11), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore1.getItem(5), 1),
                        new ItemStack(IUItem.space_ore1.getItem(6), 1), new ItemStack(IUItem.space_ore1.getItem(7), 1),
                        new ItemStack(IUItem.space_ore1.getItem(8), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone.getItem(12), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore1.getItem(10), 1),
                        new ItemStack(IUItem.space_ore1.getItem(11), 1), new ItemStack(IUItem.spaceItem.getStack(42), 2),
                        new ItemStack(IUItem.spaceItem.getStack(42), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone.getItem(13), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore1.getItem(12), 1),
                        new ItemStack(IUItem.space_ore1.getItem(13), 1), new ItemStack(IUItem.space_ore1.getItem(14), 1)},
                (short) 4000, 35, 35, 30
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone.getItem(14), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore1.getItem(15), 1), new ItemStack(IUItem.space_ore2.getItem(), 1),
                        new ItemStack(IUItem.spaceItem.getStack(44), 2),
                        new ItemStack(IUItem.spaceItem.getStack(44), 1)},
                (short) 4000, 10, 25, 25, 25
        );

        addhandlerore(
                new ItemStack(IUItem.space_stone.getItem(15), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore2.getItem(1), 1),
                        new ItemStack(IUItem.space_ore2.getItem(2), 1), new ItemStack(IUItem.spaceItem.getStack(45), 2),
                        new ItemStack(IUItem.spaceItem.getStack(45), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1.getItem(0), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore2.getItem(3), 1),
                        new ItemStack(IUItem.spaceItem.getStack(46), 2),
                        new ItemStack(IUItem.spaceItem.getStack(46), 1)},
                (short) 4000, 10, 50, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1.getItem(1), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore2.getItem(4), 1),
                        new ItemStack(IUItem.space_ore2.getItem(5), 1), new ItemStack(IUItem.spaceItem.getStack(47), 2),
                        new ItemStack(IUItem.spaceItem.getStack(47), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1.getItem(2), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore2.getItem(6), 1),
                        new ItemStack(IUItem.space_ore2.getItem(7), 1), new ItemStack(IUItem.spaceItem.getStack(48), 2),
                        new ItemStack(IUItem.spaceItem.getStack(48), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1.getItem(3), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore2.getItem(8), 1),
                        new ItemStack(IUItem.space_ore2.getItem(9), 1), new ItemStack(IUItem.spaceItem.getStack(49), 2),
                        new ItemStack(IUItem.spaceItem.getStack(49), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1.getItem(4), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore2.getItem(10), 1),
                        new ItemStack(IUItem.space_ore2.getItem(11), 1), new ItemStack(IUItem.spaceItem.getStack(50), 2),
                        new ItemStack(IUItem.spaceItem.getStack(50), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1.getItem(5), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore2.getItem(12), 1),
                        new ItemStack(IUItem.space_ore2.getItem(13), 1), new ItemStack(IUItem.spaceItem.getStack(51), 2),
                        new ItemStack(IUItem.spaceItem.getStack(51), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1.getItem(6), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore2.getItem(14), 1),
                        new ItemStack(IUItem.space_ore2.getItem(15), 1), new ItemStack(IUItem.spaceItem.getStack(52), 2),
                        new ItemStack(IUItem.spaceItem.getStack(52), 1)},
                (short) 4000, 25, 25, 25, 25
        );

        addhandlerore(
                new ItemStack(IUItem.space_stone1.getItem(7), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore3.getItem(), 1),
                        new ItemStack(IUItem.space_ore3.getItem(1), 1), new ItemStack(IUItem.space_ore1.getItem(2), 1)},
                (short) 4000, 35, 35, 30
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1.getItem(8), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore3.getItem(3), 1),
                        new ItemStack(IUItem.space_ore3.getItem(4), 1), new ItemStack(IUItem.spaceItem.getStack(54), 2),
                        new ItemStack(IUItem.spaceItem.getStack(54), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1.getItem(9), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore3.getItem(5), 1),
                        new ItemStack(IUItem.space_ore3.getItem(6), 1), new ItemStack(IUItem.spaceItem.getStack(55), 2),
                        new ItemStack(IUItem.spaceItem.getStack(55), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1.getItem(10), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore3.getItem(7), 1),
                        new ItemStack(IUItem.space_ore3.getItem(8), 1), new ItemStack(IUItem.spaceItem.getStack(56), 2),
                        new ItemStack(IUItem.spaceItem.getStack(56), 1)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1.getItem(11), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore3.getItem(9), 1),
                        new ItemStack(IUItem.space_ore3.getItem(10), 1), new ItemStack(IUItem.spaceItem.getStack(57), 2),
                        new ItemStack(IUItem.spaceItem.getStack(57), 1)},
                (short) 4000, 25, 25, 25, 25
        );

        addhandlerore(
                new ItemStack(IUItem.space_stone1.getItem(12), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore3.getItem(11), 1),
                        new ItemStack(IUItem.spaceItem.getStack(58), 2),
                        new ItemStack(IUItem.spaceItem.getStack(58), 1)},
                (short) 4000, 30, 50, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1.getItem(13), 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore3.getItem(12), 1),
                        new ItemStack(IUItem.spaceItem.getStack(59), 2),
                        new ItemStack(IUItem.spaceItem.getStack(59), 1)},
                (short) 4000, 30, 50, 25
        );
    }


}
