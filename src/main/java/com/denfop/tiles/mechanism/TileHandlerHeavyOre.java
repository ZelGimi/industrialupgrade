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
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileHandlerHeavyOre extends TileBaseHandlerHeavyOre implements IHasRecipe {


    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileHandlerHeavyOre() {
        super(EnumTypeStyle.DEFAULT);
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.5));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 1));
    }

    public static void addhandlerore(ItemStack container, ItemStack[] output, short temperature, int... col) {
        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", temperature);
        for (int i = 0; i < col.length; i++) {
            nbt.setInteger("input" + i, col[i]);
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
        return IUItem.basemachine;
    }

    public void init() {
        addhandlerore(
                new ItemStack(IUItem.heavyore),
                new ItemStack[]{new ItemStack(Blocks.IRON_ORE), new ItemStack(Blocks.GOLD_ORE), new ItemStack(IUItem.ore2, 1, 4)},
                (short) 1500, 60, 25, 15
        );
        addhandlerore(new ItemStack(IUItem.heavyore, 1, 1), new ItemStack[]{new ItemStack(IUItem.ore, 1, 7),
                        new ItemStack(Blocks.GOLD_ORE), IUItem.classic_ore.getItemStack(BlockClassicOre.Type.copper)},
                (short) 3000, 28, 44, 28
        );
        addhandlerore(new ItemStack(IUItem.heavyore, 1, 2), new ItemStack[]{new ItemStack(IUItem.ore, 1, 11),
                IUItem.classic_ore.getItemStack(BlockClassicOre.Type.lead)}, (short) 5000, 13, 87);
        addhandlerore(
                new ItemStack(IUItem.heavyore, 1, 3),
                new ItemStack[]{new ItemStack(IUItem.ore, 1, 8), new ItemStack(IUItem.ore, 1, 6)},
                (short) 4000, 44, 56
        );
        addhandlerore(
                new ItemStack(IUItem.heavyore, 1, 4),
                new ItemStack[]{new ItemStack(Blocks.IRON_ORE), new ItemStack(IUItem.ore, 1, 4), IUItem.smallSulfurDust},
                (short) 2500, 80, 15, 5
        );
        addhandlerore(
                new ItemStack(IUItem.heavyore, 1, 5),
                new ItemStack[]{new ItemStack(Blocks.QUARTZ_ORE), new ItemStack(
                        IUItem.ore,
                        1, 12
                )},
                (short) 2500, 84, 16
        );
        addhandlerore(
                new ItemStack(IUItem.heavyore, 1, 6),
                new ItemStack[]{IUItem.classic_ore.getItemStack(BlockClassicOre.Type.uranium),
                        new ItemStack(IUItem.toriyore)},
                (short) 4500, 84, 16
        );
        addhandlerore(
                new ItemStack(IUItem.heavyore, 1, 7),
                new ItemStack[]{IUItem.classic_ore.getItemStack(BlockClassicOre.Type.copper), new ItemStack(Blocks.LAPIS_ORE),
                        new ItemStack(Blocks.REDSTONE_ORE)},
                (short) 2000, 55, 23, 21
        );

        addhandlerore(new ItemStack(IUItem.heavyore, 1, 8), new ItemStack[]{new ItemStack(IUItem.ore, 1, 13),
                        new ItemStack(IUItem.ore, 1, 5), new ItemStack(Blocks.IRON_ORE)}, (short) 3000
                , 44, 28, 28);
        addhandlerore(
                new ItemStack(IUItem.heavyore, 1, 9),
                new ItemStack[]{new ItemStack(IUItem.ore, 1, 4), new ItemStack(IUItem.ore, 1, 6)},
                (short) 3500, 50, 50
        );
        addhandlerore(new ItemStack(IUItem.heavyore, 1, 10),
                new ItemStack[]{new ItemStack(IUItem.ore, 1, 8),
                        new ItemStack(IUItem.toriyore), IUItem.classic_ore.getItemStack(BlockClassicOre.Type.uranium)},
                (short) 3000,
                50,
                25
                ,
                25
        );
        addhandlerore(
                new ItemStack(IUItem.crafting_elements, 1, 498),
                new ItemStack[]{new ItemStack(IUItem.crafting_elements, 1, 499)},
                (short) 3000,
                75
        );

        addhandlerore(new ItemStack(IUItem.heavyore, 1, 11), new ItemStack[]{new ItemStack(IUItem.ore, 1, 12),
                new ItemStack(Blocks.COAL_ORE)}, (short) 4000, 65, 35);

        addhandlerore(new ItemStack(IUItem.heavyore, 1, 12), new ItemStack[]{new ItemStack(IUItem.ore, 1, 8),
                new ItemStack(Blocks.IRON_ORE)}, (short) 4500, 47, 53);
        addhandlerore(new ItemStack(IUItem.heavyore, 1, 13), new ItemStack[]{new ItemStack(IUItem.ore, 1, 13),
                new ItemStack(IUItem.ore, 1, 5), new ItemStack(IUItem.ore, 1, 1)}, (short) 4000, 66, 17, 17);
        addhandlerore(new ItemStack(IUItem.heavyore, 1, 14), new ItemStack[]{new ItemStack(Blocks.IRON_ORE),
                new ItemStack(IUItem.ore, 1, 5)}, (short) 4000, 60, 40);
        addhandlerore(new ItemStack(IUItem.heavyore, 1, 15), new ItemStack[]{new ItemStack(IUItem.ore, 1, 3),
                IUItem.tinOre}, (short) 4000, 80, 20);
        addhandlerore(
                new ItemStack(IUItem.ore2, 1, 6),
                new ItemStack[]{new ItemStack(IUItem.crafting_elements, 3, 463), new ItemStack(IUItem.crafting_elements, 2, 461),
                        new ItemStack(IUItem.crafting_elements, 3, 462)},
                (short) 1000, 50, 5, 45
        );
        addhandlerore(
                new ItemStack(IUItem.ore2, 1, 7),
                new ItemStack[]{new ItemStack(IUItem.crafting_elements, 2, 481), new ItemStack(IUItem.crafting_elements, 1, 481),
                        new ItemStack(IUItem.crafting_elements, 1, 481)},
                (short) 1000, 100, 50, 25
        );
        addhandlerore(
                new ItemStack(IUItem.mineral, 1, 13),
                new ItemStack[]{new ItemStack(Blocks.DIAMOND_ORE, 1), new ItemStack(Blocks.EMERALD_ORE, 1),},
                (short) 5000, 35, 15
        );

        addhandlerore(
                new ItemStack(IUItem.mineral, 1, 0),
                new ItemStack[]{new ItemStack(IUItem.ore3, 1), new ItemStack(Blocks.IRON_ORE, 1), new ItemStack(
                        IUItem.ore,
                        1,
                        6
                )},
                (short) 3000, 55, 35, 10
        );
        addhandlerore(
                new ItemStack(IUItem.mineral, 1, 1),
                new ItemStack[]{new ItemStack(IUItem.ore3, 1, 10), new ItemStack(IUItem.ore3, 1, 11)},
                (short) 3500, 60, 40
        );
        addhandlerore(
                new ItemStack(IUItem.mineral, 1, 2),
                new ItemStack[]{new ItemStack(IUItem.ore3, 1, 7), new ItemStack(IUItem.ore, 1, 3)},
                (short) 3500, 50, 50
        );

        addhandlerore(
                new ItemStack(IUItem.mineral, 1, 3),
                new ItemStack[]{new ItemStack(IUItem.ore, 1, 15), new ItemStack(
                        IUItem.classic_ore,
                        1,
                        0
                ), new ItemStack(IUItem.ore3,
                        1, 4
                )},
                (short) 4000, 60, 20, 20
        );
        addhandlerore(
                new ItemStack(IUItem.mineral, 1, 4),
                new ItemStack[]{new ItemStack(IUItem.ore2, 1, 4), new ItemStack(IUItem.ore, 1, 13), new ItemStack(IUItem.ore3,
                        1, 9
                )},
                (short) 4000, 50, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.mineral, 1, 5),
                new ItemStack[]{new ItemStack(IUItem.ore3, 1, 13), new ItemStack(IUItem.classic_ore, 1, 2)},
                (short) 4000, 50, 50
        );
        addhandlerore(
                new ItemStack(IUItem.mineral, 1, 6),
                new ItemStack[]{new ItemStack(IUItem.ore3, 1, 6), new ItemStack(IUItem.ore3, 1, 3)},
                (short) 4000, 70, 30
        );
        addhandlerore(
                new ItemStack(IUItem.mineral, 1, 7),
                new ItemStack[]{new ItemStack(IUItem.ore2, 1, 3), new ItemStack(IUItem.ore, 1, 14)},
                (short) 4000, 65, 35
        );
        addhandlerore(
                new ItemStack(IUItem.mineral, 1, 8),
                new ItemStack[]{new ItemStack(IUItem.ore2, 1, 5), new ItemStack(IUItem.ore3, 1, 0)},
                (short) 4000, 70, 30
        );
        addhandlerore(
                new ItemStack(IUItem.mineral, 1, 9),
                new ItemStack[]{new ItemStack(IUItem.ore3, 1, 12), new ItemStack(IUItem.ore3, 1, 2), new ItemStack(
                        IUItem.ore3,
                        1,
                        0
                )},
                (short) 4000, 50, 20, 30
        );
        addhandlerore(
                new ItemStack(IUItem.mineral, 1, 10),
                new ItemStack[]{new ItemStack(IUItem.ore3, 1, 8), new ItemStack(IUItem.ore3, 1, 9)},
                (short) 4000, 70, 30
        );
        addhandlerore(
                new ItemStack(IUItem.mineral, 1, 11),
                new ItemStack[]{new ItemStack(IUItem.ore3, 1, 12), new ItemStack(IUItem.ore3, 1, 1)},
                (short) 4000, 70, 30
        );
        addhandlerore(
                new ItemStack(IUItem.mineral, 1, 12),
                new ItemStack[]{new ItemStack(IUItem.ore3, 1, 14), new ItemStack(IUItem.ore3, 1, 5)},
                (short) 4000, 70, 30
        );
        addhandlerore(
                new ItemStack(IUItem.space_ore1, 1, 5),
                new ItemStack[]{new ItemStack(IUItem.apatite, 1, 0), new ItemStack(IUItem.apatite, 1, 1)},
                (short) 4000, 50, 50
        );

        addhandlerore(
                new ItemStack(IUItem.space_ore1, 1, 3),
                new ItemStack[]{new ItemStack(IUItem.apatite, 1, 2), new ItemStack(IUItem.apatite, 1, 3),
                        new ItemStack(IUItem.apatite, 1, 4)},
                (short) 4000, 33, 33, 33
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone, 1, 0),
                new ItemStack[]{new ItemStack(IUItem.space_ore, 1, 0), new ItemStack(IUItem.spaceItem, 2, 30),
                        new ItemStack(IUItem.spaceItem, 1, 30)},
                (short) 4000, 10, 50, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone, 1, 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore, 1, 1),
                        new ItemStack(IUItem.space_ore, 1, 2), new ItemStack(IUItem.spaceItem, 2, 30),
                        new ItemStack(IUItem.spaceItem, 1, 30)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone, 1, 2),
                new ItemStack[]{new ItemStack(IUItem.space_ore, 1, 3),
                        new ItemStack(IUItem.space_ore, 1, 4), new ItemStack(IUItem.space_ore, 1, 5)},
                (short) 4000, 35, 35, 30
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone, 1, 3),
                new ItemStack[]{new ItemStack(IUItem.space_ore, 1, 6),
                        new ItemStack(IUItem.space_ore, 1, 7), new ItemStack(IUItem.spaceItem, 2, 33),
                        new ItemStack(IUItem.spaceItem, 1, 33)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone, 1, 4),
                new ItemStack[]{new ItemStack(IUItem.space_ore, 1, 8),
                        new ItemStack(IUItem.space_ore, 1, 9), new ItemStack(IUItem.spaceItem, 2, 34),
                        new ItemStack(IUItem.spaceItem, 1, 34)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone, 1, 5),
                new ItemStack[]{new ItemStack(IUItem.space_ore, 1, 10), new ItemStack(IUItem.spaceItem, 2, 35),
                        new ItemStack(IUItem.spaceItem, 1, 35)},
                (short) 4000, 10, 50, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone, 1, 6),
                new ItemStack[]{new ItemStack(IUItem.space_ore, 1, 11),
                        new ItemStack(IUItem.space_ore, 1, 12), new ItemStack(IUItem.spaceItem, 2, 36),
                        new ItemStack(IUItem.spaceItem, 1, 36)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone, 1, 7),
                new ItemStack[]{new ItemStack(IUItem.space_ore, 1, 13), new ItemStack(IUItem.spaceItem, 2, 37),
                        new ItemStack(IUItem.spaceItem, 1, 37)},
                (short) 4000, 25, 50, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone, 1, 8),
                new ItemStack[]{new ItemStack(IUItem.space_ore, 1, 14),
                        new ItemStack(IUItem.space_ore, 1, 15), new ItemStack(IUItem.spaceItem, 2, 38),
                        new ItemStack(IUItem.spaceItem, 1, 38)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone, 1, 9),
                new ItemStack[]{new ItemStack(IUItem.space_ore1, 1, 0),
                        new ItemStack(IUItem.space_ore1, 1, 1), new ItemStack(IUItem.space_ore1, 1, 4)},
                (short) 4000, 35, 35, 30
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone, 1, 10),
                new ItemStack[]{new ItemStack(IUItem.space_ore1, 1, 2),
                        new ItemStack(IUItem.space_ore1, 1, 3), new ItemStack(IUItem.spaceItem, 2, 40),
                        new ItemStack(IUItem.spaceItem, 1, 40)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone, 1, 11),
                new ItemStack[]{new ItemStack(IUItem.space_ore1, 1, 5),
                        new ItemStack(IUItem.space_ore1, 1, 6), new ItemStack(IUItem.space_ore1, 1, 7),
                        new ItemStack(IUItem.space_ore1, 1, 8)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone, 1, 12),
                new ItemStack[]{new ItemStack(IUItem.space_ore1, 1, 10),
                        new ItemStack(IUItem.space_ore1, 1, 11), new ItemStack(IUItem.spaceItem, 2, 42),
                        new ItemStack(IUItem.spaceItem, 1, 42)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone, 1, 13),
                new ItemStack[]{new ItemStack(IUItem.space_ore1, 1, 12),
                        new ItemStack(IUItem.space_ore1, 1, 13), new ItemStack(IUItem.space_ore1, 1, 14)},
                (short) 4000, 35, 35, 30
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone, 1, 14),
                new ItemStack[]{new ItemStack(IUItem.space_ore1, 1, 15), new ItemStack(IUItem.space_ore2, 1, 0),
                        new ItemStack(IUItem.spaceItem, 2, 44),
                        new ItemStack(IUItem.spaceItem, 1, 44)},
                (short) 4000, 10, 25, 25, 25
        );

        addhandlerore(
                new ItemStack(IUItem.space_stone, 1, 15),
                new ItemStack[]{new ItemStack(IUItem.space_ore2, 1, 1),
                        new ItemStack(IUItem.space_ore2, 1, 2), new ItemStack(IUItem.spaceItem, 2, 45),
                        new ItemStack(IUItem.spaceItem, 1, 45)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1, 1, 0),
                new ItemStack[]{new ItemStack(IUItem.space_ore2, 1, 3),
                        new ItemStack(IUItem.spaceItem, 2, 46),
                        new ItemStack(IUItem.spaceItem, 1, 46)},
                (short) 4000, 10, 50, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1, 1, 1),
                new ItemStack[]{new ItemStack(IUItem.space_ore2, 1, 4),
                        new ItemStack(IUItem.space_ore2, 1, 5), new ItemStack(IUItem.spaceItem, 2, 47),
                        new ItemStack(IUItem.spaceItem, 1, 47)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1, 1, 2),
                new ItemStack[]{new ItemStack(IUItem.space_ore2, 1, 6),
                        new ItemStack(IUItem.space_ore2, 1, 7), new ItemStack(IUItem.spaceItem, 2, 48),
                        new ItemStack(IUItem.spaceItem, 1, 48)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1, 1, 3),
                new ItemStack[]{new ItemStack(IUItem.space_ore2, 1, 8),
                        new ItemStack(IUItem.space_ore2, 1, 9), new ItemStack(IUItem.spaceItem, 2, 49),
                        new ItemStack(IUItem.spaceItem, 1, 49)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1, 1, 4),
                new ItemStack[]{new ItemStack(IUItem.space_ore2, 1, 10),
                        new ItemStack(IUItem.space_ore2, 1, 11), new ItemStack(IUItem.spaceItem, 2, 50),
                        new ItemStack(IUItem.spaceItem, 1, 50)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1, 1, 5),
                new ItemStack[]{new ItemStack(IUItem.space_ore2, 1, 12),
                        new ItemStack(IUItem.space_ore2, 1, 13), new ItemStack(IUItem.spaceItem, 2, 51),
                        new ItemStack(IUItem.spaceItem, 1, 51)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1, 1, 6),
                new ItemStack[]{new ItemStack(IUItem.space_ore2, 1, 14),
                        new ItemStack(IUItem.space_ore2, 1, 15), new ItemStack(IUItem.spaceItem, 2, 52),
                        new ItemStack(IUItem.spaceItem, 1, 52)},
                (short) 4000, 25, 25, 25, 25
        );

        addhandlerore(
                new ItemStack(IUItem.space_stone1, 1, 7),
                new ItemStack[]{new ItemStack(IUItem.space_ore3, 1, 0),
                        new ItemStack(IUItem.space_ore3, 1, 1), new ItemStack(IUItem.space_ore3, 1, 2)},
                (short) 4000, 35, 35, 30
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1, 1, 8),
                new ItemStack[]{new ItemStack(IUItem.space_ore3, 1, 3),
                        new ItemStack(IUItem.space_ore3, 1, 4), new ItemStack(IUItem.spaceItem, 2, 54),
                        new ItemStack(IUItem.spaceItem, 1, 54)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1, 1, 9),
                new ItemStack[]{new ItemStack(IUItem.space_ore3, 1, 5),
                        new ItemStack(IUItem.space_ore3, 1, 6), new ItemStack(IUItem.spaceItem, 2, 55),
                        new ItemStack(IUItem.spaceItem, 1, 55)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1, 1, 10),
                new ItemStack[]{new ItemStack(IUItem.space_ore3, 1, 7),
                        new ItemStack(IUItem.space_ore3, 1, 8), new ItemStack(IUItem.spaceItem, 2, 56),
                        new ItemStack(IUItem.spaceItem, 1, 56)},
                (short) 4000, 25, 25, 25, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1, 1, 11),
                new ItemStack[]{new ItemStack(IUItem.space_ore3, 1, 9),
                        new ItemStack(IUItem.space_ore3, 1, 10), new ItemStack(IUItem.spaceItem, 2, 57),
                        new ItemStack(IUItem.spaceItem, 1, 57)},
                (short) 4000, 25, 25, 25, 25
        );

        addhandlerore(
                new ItemStack(IUItem.space_stone1, 1, 12),
                new ItemStack[]{new ItemStack(IUItem.space_ore3, 1, 11),
                        new ItemStack(IUItem.spaceItem, 2, 58),
                        new ItemStack(IUItem.spaceItem, 1, 58)},
                (short) 4000, 30, 50, 25
        );
        addhandlerore(
                new ItemStack(IUItem.space_stone1, 1, 13),
                new ItemStack[]{new ItemStack(IUItem.space_ore3, 1, 12),
                        new ItemStack(IUItem.spaceItem, 2, 59),
                        new ItemStack(IUItem.spaceItem, 1, 59)},
                (short) 4000, 30, 50, 25
        );
    }


}
