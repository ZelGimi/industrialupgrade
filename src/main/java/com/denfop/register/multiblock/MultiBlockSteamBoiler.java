package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.steamboiler.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.SteamBoilerMultiBlock;

public class MultiBlockSteamBoiler {

    public static void init() {
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.steam_boiler.getItem(0)),
                Direction.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().offset(1, 0, 1), ITank.class,
                new ItemStack(IUItem.steam_boiler.getItem(2)),
                Direction.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().offset(-1, 0, 1), ITank.class,
                new ItemStack(IUItem.steam_boiler.getItem(2)),
                Direction.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().offset(0, 0, 1), IHeater.class,
                new ItemStack(IUItem.steam_boiler.getItem(3)),
                Direction.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().offset(0, -1, 1), IExchanger.class,
                new ItemStack(IUItem.steam_boiler.getItem(4)),
                Direction.NORTH
        );
        for (int x = -1; x < 2; x++) {
            for (int z = 0; z < 3; z++) {
                SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().offset(x, -1, z), ICasing.class,
                        new ItemStack(IUItem.steam_boiler.getItem(1)),
                        Direction.NORTH
                );
            }
        }
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().offset(-1, -1, 2), ICasing.class,
                new ItemStack(IUItem.steam_boiler.getItem(1)),
                Direction.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().offset(1, -1, 2), ICasing.class,
                new ItemStack(IUItem.steam_boiler.getItem(1)),
                Direction.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().offset(-1, -1, 0), ICasing.class,
                new ItemStack(IUItem.steam_boiler.getItem(1)),
                Direction.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().offset(1, -1, 0), ICasing.class,
                new ItemStack(IUItem.steam_boiler.getItem(1)),
                Direction.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().offset(-1, 1, 0), ICasing.class,
                new ItemStack(IUItem.steam_boiler.getItem(1)),
                Direction.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().offset(1, 1, 0), ICasing.class,
                new ItemStack(IUItem.steam_boiler.getItem(1)),
                Direction.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().offset(-1, 1, 1), ICasing.class,
                new ItemStack(IUItem.steam_boiler.getItem(1)),
                Direction.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().offset(1, 1, 1), ICasing.class,
                new ItemStack(IUItem.steam_boiler.getItem(1)),
                Direction.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().offset(0, 1, 0), ICasing.class,
                new ItemStack(IUItem.steam_boiler.getItem(1)),
                Direction.NORTH
        );

    }

}
