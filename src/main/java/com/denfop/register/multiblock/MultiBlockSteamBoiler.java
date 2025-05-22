package com.denfop.register.multiblock;

import com.denfop.IUItem;
import com.denfop.tiles.mechanism.steamboiler.ICasing;
import com.denfop.tiles.mechanism.steamboiler.IController;
import com.denfop.tiles.mechanism.steamboiler.IExchanger;
import com.denfop.tiles.mechanism.steamboiler.IHeater;
import com.denfop.tiles.mechanism.steamboiler.ITank;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static com.denfop.register.InitMultiBlockSystem.SteamBoilerMultiBlock;

public class MultiBlockSteamBoiler {

    public static void init() {
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos(), IController.class,
                new ItemStack(IUItem.steam_boiler, 1, 0),
                EnumFacing.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().add(1, 0, 1), ITank.class,
                new ItemStack(IUItem.steam_boiler, 1, 2),
                EnumFacing.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().add(-1, 0, 1), ITank.class,
                new ItemStack(IUItem.steam_boiler, 1, 2),
                EnumFacing.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().add(0, 0, 1), IHeater.class,
                new ItemStack(IUItem.steam_boiler, 1, 3),
                EnumFacing.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().add(0, -1, 1), IExchanger.class,
                new ItemStack(IUItem.steam_boiler, 1, 4),
                EnumFacing.NORTH
        );
        for (int x = -1; x < 2; x++) {
            for (int z = 0; z < 3; z++) {
                SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().add(x, -1, z), ICasing.class,
                        new ItemStack(IUItem.steam_boiler, 1, 1),
                        EnumFacing.NORTH
                );
            }
        }
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().add(-1, -1, 2), ICasing.class,
                new ItemStack(IUItem.steam_boiler, 1, 1),
                EnumFacing.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().add(1, -1, 2), ICasing.class,
                new ItemStack(IUItem.steam_boiler, 1, 1),
                EnumFacing.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().add(-1, -1, 0), ICasing.class,
                new ItemStack(IUItem.steam_boiler, 1, 1),
                EnumFacing.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().add(1, -1, 0), ICasing.class,
                new ItemStack(IUItem.steam_boiler, 1, 1),
                EnumFacing.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().add(-1, 1, 0), ICasing.class,
                new ItemStack(IUItem.steam_boiler, 1, 1),
                EnumFacing.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().add(1, 1, 0), ICasing.class,
                new ItemStack(IUItem.steam_boiler, 1, 1),
                EnumFacing.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().add(-1, 1, 1), ICasing.class,
                new ItemStack(IUItem.steam_boiler, 1, 1),
                EnumFacing.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().add(1, 1, 1), ICasing.class,
                new ItemStack(IUItem.steam_boiler, 1, 1),
                EnumFacing.NORTH
        );
        SteamBoilerMultiBlock.add(SteamBoilerMultiBlock.getPos().add(0, 1, 0), ICasing.class,
                new ItemStack(IUItem.steam_boiler, 1, 1),
                EnumFacing.NORTH
        );

    }

}
