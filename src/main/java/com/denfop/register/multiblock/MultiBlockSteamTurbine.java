package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.mechanism.steamturbine.ICasing;
import com.denfop.tiles.mechanism.steamturbine.IController;
import com.denfop.tiles.mechanism.steamturbine.IControllerRod;
import com.denfop.tiles.mechanism.steamturbine.ICoolant;
import com.denfop.tiles.mechanism.steamturbine.IExchanger;
import com.denfop.tiles.mechanism.steamturbine.IGlass;
import com.denfop.tiles.mechanism.steamturbine.IPressure;
import com.denfop.tiles.mechanism.steamturbine.IRod;
import com.denfop.tiles.mechanism.steamturbine.ISocket;
import com.denfop.tiles.mechanism.steamturbine.ITank;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.SteamTurbineMultiBlock;

public class MultiBlockSteamTurbine {

    public static void init() {
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.steam_turbine, 1, 5),
                EnumFacing.NORTH
        );

        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(0, 1, 0), ISocket.class,
                new ItemStack(IUItem.steam_turbine, 1, 2),
                EnumFacing.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(-1, 0, 0), ITank.class,
                new ItemStack(IUItem.steam_turbine, 1, 13),
                EnumFacing.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(1, 0, 0), ITank.class,
                new ItemStack(IUItem.steam_turbine, 1, 13),
                EnumFacing.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(-1, 1, 0), IExchanger.class,
                new ItemStack(IUItem.steam_turbine, 1, 9),
                EnumFacing.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(1, 1, 0), IExchanger.class,
                new ItemStack(IUItem.steam_turbine, 1, 9),
                EnumFacing.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(-1, 2, 0), IExchanger.class,
                new ItemStack(IUItem.steam_turbine, 1, 9),
                EnumFacing.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(1, 2, 0), IExchanger.class,
                new ItemStack(IUItem.steam_turbine, 1, 9),
                EnumFacing.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(-1, 3, 0), ICoolant.class,
                new ItemStack(IUItem.steam_turbine, 1, 17),
                EnumFacing.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(1, 3, 0), ICoolant.class,
                new ItemStack(IUItem.steam_turbine, 1, 17),
                EnumFacing.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(0, 3, 0), IPressure.class,
                new ItemStack(IUItem.steam_turbine, 1, 21),
                EnumFacing.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(0, 2, 0), IControllerRod.class,
                new ItemStack(IUItem.steam_turbine, 1, 4),
                EnumFacing.NORTH
        );
        for (int z = 1; z < 10; z++) {
            SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(0, 2, z), IRod.class,
                    new ItemStack(IUItem.steam_turbine, 1, 3),
                    EnumFacing.NORTH
            );
        }
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(0, 4, 0), IGlass.class,
                new ItemStack(IUItem.steam_turbine, 1, 1),
                EnumFacing.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(1, 4, 0), IGlass.class,
                new ItemStack(IUItem.steam_turbine, 1, 1),
                EnumFacing.NORTH
        );
        SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(-1, 4, 0), IGlass.class,
                new ItemStack(IUItem.steam_turbine, 1, 1),
                EnumFacing.NORTH
        );
        for (int y = 0; y < 5; y++) {
            for (int z = 1; z < 11; z++) {
                SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(-2, y, z), IGlass.class,
                        new ItemStack(IUItem.steam_turbine, 1, 1),
                        EnumFacing.NORTH
                );
            }
        }
        for (int y = 0; y < 5; y++) {
            for (int z = 1; z < 11; z++) {
                SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(2, y, z), IGlass.class,
                        new ItemStack(IUItem.steam_turbine, 1, 1),
                        EnumFacing.NORTH
                );
            }
        }
        for (int y = 0; y < 5; y++) {
            for (int x = -1; x < 2; x++) {
                SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(x, y, 11), IGlass.class,
                        new ItemStack(IUItem.steam_turbine, 1, 1),
                        EnumFacing.NORTH
                );
            }
        }
        for (int y = 0; y < 5; y++) {
            for (int x = -2; x < 3; x++) {
                if (x > -2 && x < 2) {
                    continue;
                }
                SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(x, y, 0), ICasing.class,
                        new ItemStack(IUItem.steam_turbine, 1, 0),
                        EnumFacing.NORTH
                );
            }
        }
        for (int y = 0; y < 5; y++) {
            for (int x = -2; x < 3; x++) {
                if (x > -2 && x < 2) {
                    continue;
                }
                SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(x, y, 11), ICasing.class,
                        new ItemStack(IUItem.steam_turbine, 1, 0),
                        EnumFacing.NORTH
                );
            }
        }
        for (int z = 0; z < 12; z++) {
            for (int x = -2; x < 3; x++) {
                SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(x, -1, z), ICasing.class,
                        new ItemStack(IUItem.steam_turbine, 1, 0),
                        EnumFacing.NORTH
                );
            }
        }
        for (int z = 0; z < 12; z++) {
            for (int x = -2; x < 3; x++) {
                SteamTurbineMultiBlock.add(SteamTurbineMultiBlock.getPos().add(x, 5, z), ICasing.class,
                        new ItemStack(IUItem.steam_turbine, 1, 0),
                        EnumFacing.NORTH
                );
            }
        }
    }

}
