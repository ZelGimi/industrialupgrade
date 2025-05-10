package com.denfop.tiles.base;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;


@SuppressWarnings("unused")
public class FakePlayerSpawner extends FakePlayer {


    public FakePlayerSpawner(Level world) {
        super((ServerLevel) world, new GameProfile(null, "lDenfop"));
    }

}
