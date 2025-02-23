package com.denfop.register;

import com.denfop.IUItem;
import com.denfop.tiles.chemicalplant.ICasing;
import com.denfop.tiles.chemicalplant.IController;
import com.denfop.tiles.chemicalplant.IExchanger;
import com.denfop.tiles.chemicalplant.IGenerator;
import com.denfop.tiles.chemicalplant.ISeparate;
import com.denfop.tiles.chemicalplant.ITransport;
import com.denfop.tiles.chemicalplant.IWaste;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.ChemicalPlantMultiBlock;
import static com.denfop.register.InitMultiBlockSystem.GeoThermalPumpMultiBlock;

public class MultiBlockChemicalPlant {
    public static void init() {
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.chemicalPlant, 1, 0),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(0,-1,0), ICasing.class,
                new ItemStack(IUItem.chemicalPlant,1,2),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(2,-1,0), ICasing.class,
                new ItemStack(IUItem.chemicalPlant,1,2),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(-2,-1,0), ICasing.class,
                new ItemStack(IUItem.chemicalPlant,1,2),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(0,-1,2), ICasing.class,
                new ItemStack(IUItem.chemicalPlant,1,2),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(2,-1,2), ICasing.class,
                new ItemStack(IUItem.chemicalPlant,1,2),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(-2,-1,2), ICasing.class,
                new ItemStack(IUItem.chemicalPlant,1,2),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(0,-1,4), ICasing.class,
                new ItemStack(IUItem.chemicalPlant,1,2),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(2,-1,4), ICasing.class,
                new ItemStack(IUItem.chemicalPlant,1,2),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(-2,-1,4), ICasing.class,
                new ItemStack(IUItem.chemicalPlant,1,2),
                EnumFacing.NORTH
        );
        for (int i = -2; i < 3;i++)
            for (int j = 0; j < 5;j++)
                ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(i,1,j), ICasing.class,
                        new ItemStack(IUItem.chemicalPlant,1,2),
                        EnumFacing.NORTH
                );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(-1,0,0), ITransport.class,
                new ItemStack(IUItem.chemicalPlant,1,4),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(1,0,0), ITransport.class,
                new ItemStack(IUItem.chemicalPlant,1,4),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(-1,0,2), ITransport.class,
                new ItemStack(IUItem.chemicalPlant,1,4),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(1,0,2), ITransport.class,
                new ItemStack(IUItem.chemicalPlant,1,4),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(-1,0,4), ISeparate.class,
                new ItemStack(IUItem.chemicalPlant,1,6),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(1,0,4), ISeparate.class,
                new ItemStack(IUItem.chemicalPlant,1,6),
                EnumFacing.NORTH
        );

        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(-2,0,1), ITransport.class,
                new ItemStack(IUItem.chemicalPlant,1,4),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(2,0,1), ITransport.class,
                new ItemStack(IUItem.chemicalPlant,1,4),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(-2,0,3), ITransport.class,
                new ItemStack(IUItem.chemicalPlant,1,4),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(2,0,3), ITransport.class,
                new ItemStack(IUItem.chemicalPlant,1,4),
                EnumFacing.NORTH
        );

        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(-2,0,4), IExchanger.class,
                new ItemStack(IUItem.chemicalPlant,1,1),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(2,0,4), IExchanger.class,
                new ItemStack(IUItem.chemicalPlant,1,1),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(-2,0,0), IExchanger.class,
                new ItemStack(IUItem.chemicalPlant,1,1),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(2,0,0), IExchanger.class,
                new ItemStack(IUItem.chemicalPlant,1,1),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(-2,0,2), IGenerator.class,
                new ItemStack(IUItem.chemicalPlant,1,3),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(2,0,2), IGenerator.class,
                new ItemStack(IUItem.chemicalPlant,1,3),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(0,0,4), IWaste.class,
                new ItemStack(IUItem.chemicalPlant,1,5),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(0,0,2), ITransport.class,
                new ItemStack(IUItem.chemicalPlant,1,4),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(0,0,3), ITransport.class,
                new ItemStack(IUItem.chemicalPlant,1,4),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(0,0,1), ITransport.class,
                new ItemStack(IUItem.chemicalPlant,1,4),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(1,0,2), ITransport.class,
                new ItemStack(IUItem.chemicalPlant,1,4),
                EnumFacing.NORTH
        );
        ChemicalPlantMultiBlock.add(ChemicalPlantMultiBlock.getPos().add(-1,0,2), ITransport.class,
                new ItemStack(IUItem.chemicalPlant,1,4),
                EnumFacing.NORTH
        );
    }
}
