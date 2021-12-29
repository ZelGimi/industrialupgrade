package com.denfop.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;


@SuppressWarnings("unused")
public class FakePlayerSpawner extends FakePlayer {

    public int fireAspect = 0;

    public int loot = 0;

    public FakePlayerSpawner(World world) {
        super((WorldServer) world, new GameProfile(null, "lDenfop"));
    }

}
