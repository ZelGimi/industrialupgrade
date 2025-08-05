package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.mechanism.steamturbine.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import static com.denfop.register.InitMultiBlockSystem.SteamTurbineMultiBlock;

public class MultiBlockSteamTurbine {

    public static void init() {
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.steam_turbine.getItem(5)),
                Direction.NORTH
        );

        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(0, 1, 0), ISocket.class,
                new ItemStack(IUItem.steam_turbine.getItem(2)),
                Direction.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(-1, 0, 0), ITank.class,
                new ItemStack(IUItem.steam_turbine.getItem(13)),
                Direction.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(1, 0, 0), ITank.class,
                new ItemStack(IUItem.steam_turbine.getItem(13)),
                Direction.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(-1, 1, 0), IExchanger.class,
                new ItemStack(IUItem.steam_turbine.getItem(9)),
                Direction.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(1, 1, 0), IExchanger.class,
                new ItemStack(IUItem.steam_turbine.getItem(9)),
                Direction.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(-1, 2, 0), IExchanger.class,
                new ItemStack(IUItem.steam_turbine.getItem(9)),
                Direction.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(1, 2, 0), IExchanger.class,
                new ItemStack(IUItem.steam_turbine.getItem(9)),
                Direction.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(-1, 3, 0), ICoolant.class,
                new ItemStack(IUItem.steam_turbine.getItem(17)),
                Direction.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(1, 3, 0), ICoolant.class,
                new ItemStack(IUItem.steam_turbine.getItem(17)),
                Direction.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(0, 3, 0), IPressure.class,
                new ItemStack(IUItem.steam_turbine.getItem(21)),
                Direction.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(0, 2, 0), IControllerRod.class,
                new ItemStack(IUItem.steam_turbine.getItem(4)),
                Direction.NORTH
        );
        for (int z = 1; z < 10; z++) {
            SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(0, 2, z), IRod.class,
                    new ItemStack(IUItem.steam_turbine.getItem(3)),
                    Direction.NORTH
            );
        }
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(0, 4, 0), IGlass.class,
                new ItemStack(IUItem.steam_turbine.getItem(1)),
                Direction.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(1, 4, 0), IGlass.class,
                new ItemStack(IUItem.steam_turbine.getItem(1)),
                Direction.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(-1, 4, 0), IGlass.class,
                new ItemStack(IUItem.steam_turbine.getItem(1)),
                Direction.NORTH
        );
        for (int y = 0; y < 5; y++) {
            for (int z = 1; z < 11; z++) {
                SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(-2, y, z), IGlass.class,
                        new ItemStack(IUItem.steam_turbine.getItem(1)),
                        Direction.NORTH
                );
            }
        }
        for (int y = 0; y < 5; y++) {
            for (int z = 1; z < 11; z++) {
                SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(2, y, z), IGlass.class,
                        new ItemStack(IUItem.steam_turbine.getItem(1)),
                        Direction.NORTH
                );
            }
        }
        for (int y = 0; y < 5; y++) {
            for (int x = -1; x < 2; x++) {
                SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(x, y, 11), IGlass.class,
                        new ItemStack(IUItem.steam_turbine.getItem(1)),
                        Direction.NORTH
                );
            }
        }
        for (int y = 0; y < 5; y++) {
            for (int x = -2; x < 3; x++) {
                if (x > -2 && x < 2) {
                    continue;
                }
                SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(x, y, 0), ICasing.class,
                        new ItemStack(IUItem.steam_turbine.getItem(0)),
                        Direction.NORTH
                );
            }
        }
        for (int y = 0; y < 5; y++) {
            for (int x = -2; x < 3; x++) {
                if (x > -2 && x < 2) {
                    continue;
                }
                SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(x, y, 11), ICasing.class,
                        new ItemStack(IUItem.steam_turbine.getItem(0)),
                        Direction.NORTH
                );
            }
        }
        for (int z = 0; z < 12; z++) {
            for (int x = -2; x < 3; x++) {
                SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(x, -1, z), ICasing.class,
                        new ItemStack(IUItem.steam_turbine.getItem(0)),
                        Direction.NORTH
                );
            }
        }
        for (int z = 0; z < 12; z++) {
            for (int x = -2; x < 3; x++) {
                SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().offset(x, 5, z), ICasing.class,
                        new ItemStack(IUItem.steam_turbine.getItem(0)),
                        Direction.NORTH
                );
            }
        }
    }

}
