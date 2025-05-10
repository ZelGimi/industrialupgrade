package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.chemicalplant.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.ChemicalPlantMultiBlock;

public class MultiBlockChemicalPlant {

    public static void init() {
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.chemicalPlant.getItem(0)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(0, -1, 0), ICasing.class,
                new ItemStack(IUItem.chemicalPlant.getItem(2)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(2, -1, 0), ICasing.class,
                new ItemStack(IUItem.chemicalPlant.getItem(2)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(-2, -1, 0), ICasing.class,
                new ItemStack(IUItem.chemicalPlant.getItem(2)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(0, -1, 2), ICasing.class,
                new ItemStack(IUItem.chemicalPlant.getItem(2)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(2, -1, 2), ICasing.class,
                new ItemStack(IUItem.chemicalPlant.getItem(2)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(-2, -1, 2), ICasing.class,
                new ItemStack(IUItem.chemicalPlant.getItem(2)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(0, -1, 4), ICasing.class,
                new ItemStack(IUItem.chemicalPlant.getItem(2)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(2, -1, 4), ICasing.class,
                new ItemStack(IUItem.chemicalPlant.getItem(2)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(-2, -1, 4), ICasing.class,
                new ItemStack(IUItem.chemicalPlant.getItem(2)),
                        Direction.NORTH
                );
        for (int i = -2; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(i,1,j), ICasing.class,
                        new ItemStack(IUItem.chemicalPlant.getItem(2)),
                                Direction.NORTH
                        );
            }
        }
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(-1, 0, 0), ITransport.class,
                new ItemStack(IUItem.chemicalPlant.getItem(4)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(1, 0, 0), ITransport.class,
                new ItemStack(IUItem.chemicalPlant.getItem(4)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(-1, 0, 2), ITransport.class,
                new ItemStack(IUItem.chemicalPlant.getItem(4)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(1, 0, 2), ITransport.class,
                new ItemStack(IUItem.chemicalPlant.getItem(4)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(-1, 0, 4), ISeparate.class,
                new ItemStack(IUItem.chemicalPlant.getItem(6)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(1, 0, 4), ISeparate.class,
                new ItemStack(IUItem.chemicalPlant.getItem(6)),
                        Direction.NORTH
                );

        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(-2, 0, 1), ITransport.class,
                new ItemStack(IUItem.chemicalPlant.getItem(4)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(2, 0, 1), ITransport.class,
                new ItemStack(IUItem.chemicalPlant.getItem(4)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(-2, 0, 3), ITransport.class,
                new ItemStack(IUItem.chemicalPlant.getItem(4)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(2, 0, 3), ITransport.class,
                new ItemStack(IUItem.chemicalPlant.getItem(4)),
                        Direction.NORTH
                );

        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(-2, 0, 4), IExchanger.class,
                new ItemStack(IUItem.chemicalPlant.getItem(1)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(2, 0, 4), IExchanger.class,
                new ItemStack(IUItem.chemicalPlant.getItem(1)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(-2, 0, 0), IExchanger.class,
                new ItemStack(IUItem.chemicalPlant.getItem(1)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(2, 0, 0), IExchanger.class,
                new ItemStack(IUItem.chemicalPlant.getItem(1)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(-2, 0, 2), IGenerator.class,
                new ItemStack(IUItem.chemicalPlant.getItem(3)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(2, 0, 2), IGenerator.class,
                new ItemStack(IUItem.chemicalPlant.getItem(3)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(0, 0, 4), IWaste.class,
                new ItemStack(IUItem.chemicalPlant.getItem(5)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(0, 0, 2), ITransport.class,
                new ItemStack(IUItem.chemicalPlant.getItem(4)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(0, 0, 3), ITransport.class,
                new ItemStack(IUItem.chemicalPlant.getItem(4)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(0, 0, 1), ITransport.class,
                new ItemStack(IUItem.chemicalPlant.getItem(4)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(1, 0, 2), ITransport.class,
                new ItemStack(IUItem.chemicalPlant.getItem(4)),
                        Direction.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().offset(-1, 0, 2), ITransport.class,
                new ItemStack(IUItem.chemicalPlant.getItem(4)),
                        Direction.NORTH
                );
    }

}
