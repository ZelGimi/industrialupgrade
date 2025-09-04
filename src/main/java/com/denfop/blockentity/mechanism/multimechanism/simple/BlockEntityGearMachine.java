package com.denfop.blockentity.mechanism.multimechanism.simple;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.recipe.IInputHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockEntityGearMachine extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityGearMachine(BlockPos pos, BlockState state) {
        super(EnumMultiMachine.Gearing.usagePerTick, EnumMultiMachine.Gearing.lenghtOperation, BlockMoreMachine3Entity.gearing, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.15));
    }

    public static void addrecipe(String input, String output) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "gearing",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 4)
                        ),
                        new RecipeOutput(null, input1.getInput(output).getInputs().get(0))
                )
        );
    }

    public static List<String> itemNames() {
        List<String> list = new ArrayList<>();
        list.add("Mikhail");//0
        list.add("Aluminium");//1
        list.add("vanadium");//2
        list.add("Tungsten");//3
        list.add("Invar");//4
        list.add("Caravky");//5
        list.add("Cobalt");//6
        list.add("Magnesium");//7
        list.add("Nickel");//8
        list.add("Platinum");//9
        list.add("Titanium");//10
        list.add("Chromium");//11
        list.add("Spinel");//12
        list.add("Electrum");//13
        list.add("Silver");//14
        list.add("Zinc");//15
        list.add("Manganese");//16
        list.add("Iridium");//17
        list.add("Germanium");//18
        return list;
    }

    public static List<String> itemNames7() {
        return Arrays.asList(
                "Arsenic",
                "Barium",
                "Bismuth",
                "Gadolinium",
                "Gallium",
                "Hafnium",
                "Yttrium",
                "Molybdenum",
                "Neodymium",
                "Niobium",
                "Palladium",
                "Polonium",
                "Strontium",
                "Thallium",
                "Zirconium"
        );
    }

    public static List<String> itemNames1() {
        List<String> list = new ArrayList<>();
        list.add("Aluminumbronze");//0
        list.add("Alumel");//1
        list.add("Redbrass");//2
        list.add("Muntsa");//3
        list.add("Nichrome");//4
        list.add("Alcled");//5
        list.add("Vanadoalumite");//6
        list.add("Vitalium");//7
        list.add("Duralumin");//8
        list.add("Ferromanganese");//9
        list.add("AluminiumSilicon");//10
        list.add("BerylliumBronze");//11
        list.add("Zeliber");//12
        list.add("StainlessSteel");//13
        list.add("Inconel");//14
        list.add("Nitenol");//15
        list.add("Stellite");//16
        list.add("HafniumBoride");//17
        list.add("Woods");//18
        list.add("Nimonic");//19
        list.add("TantalumTungstenHafnium");//20
        list.add("Permalloy");//21
        list.add("AluminiumLithium");//22
        list.add("CobaltChrome");//23
        list.add("HafniumCarbide");//24
        list.add("MolybdenumSteel");//25
        list.add("NiobiumTitanium");//26
        list.add("Osmiridium");//27
        list.add("SuperalloyHaynes");//28
        list.add("SuperalloyRene");//29
        list.add("YttriumAluminiumGarnet");//30
        list.add("GalliumArsenic");//31
        return list;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachine3Entity.gearing;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3.getBlock(getTeBlock().getId());
    }

    public void init() {
        for (int i = 0; i < itemNames().size(); i++) {

            addrecipe(
                    "forge:ingots/" + itemNames().get(i).toLowerCase(),
                    "forge:gears/" + itemNames().get(i).toLowerCase()
            );


        }
        for (int i = 0; i < itemNames7().size(); i++) {

            addrecipe(
                    "forge:ingots/" + itemNames7().get(i).toLowerCase(),
                    "forge:gears/" + itemNames7().get(i).toLowerCase()
            );


        }
        for (int i = 0; i < itemNames1().size(); i++) {

            addrecipe(
                    "forge:ingots/" + itemNames1().get(i).toLowerCase(),
                    "forge:gears/" + itemNames1().get(i).toLowerCase()
            );


        }
        addrecipe(
                "forge:ingots/Osmium".toLowerCase(),
                "forge:gears/Osmium".toLowerCase()
        );
        addrecipe(
                "forge:ingots/Tantalum".toLowerCase(),
                "forge:gears/Tantalum".toLowerCase()
        );
        addrecipe(
                "forge:ingots/Cadmium".toLowerCase(),
                "forge:gears/Cadmium".toLowerCase()
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Gearing;
    }

}
