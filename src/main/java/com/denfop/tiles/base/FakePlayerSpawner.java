package com.denfop.tiles.base;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;


@SuppressWarnings("unused")
public class FakePlayerSpawner extends FakePlayer {


    public FakePlayerSpawner(World world) {
        super((WorldServer) world, new GameProfile(null, "lDenfop"));
    }

}
