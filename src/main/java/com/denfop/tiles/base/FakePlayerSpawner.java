package com.denfop.tiles.base;

import com.denfop.world.WorldBaseGen;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.UUID;


@SuppressWarnings("unused")
public class FakePlayerSpawner extends FakePlayer {


    public FakePlayerSpawner(Level world) {
        super((ServerLevel) world, new GameProfile(new UUID(WorldBaseGen.random.nextLong(), WorldBaseGen.random.nextLong()), "lDenfop"));
    }

}
