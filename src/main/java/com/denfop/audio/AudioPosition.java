//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.audio;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.ref.WeakReference;

public class AudioPosition {

    private final WeakReference<World> worldRef;
    public final float x;
    public final float y;
    public final float z;

    public static AudioPosition getFrom(Object obj, PositionSpec positionSpec) {
        if (obj instanceof AudioPosition) {
            return (AudioPosition) obj;
        } else if (obj instanceof Entity) {
            Entity e = (Entity) obj;
            return new AudioPosition(e.getEntityWorld(), (float) e.posX, (float) e.posY, (float) e.posZ);
        } else if (obj instanceof TileEntity) {
            TileEntity te = (TileEntity) obj;
            return new AudioPosition(
                    te.getWorld(),
                    (float) te.getPos().getX() + 0.5F,
                    (float) te.getPos().getY() + 0.5F,
                    (float) te.getPos().getZ() + 0.5F
            );
        } else {
            return null;
        }
    }

    public AudioPosition(World world, float x, float y, float z) {
        this.worldRef = new WeakReference(world);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public AudioPosition(World world, BlockPos pos) {
        this(world, (float) pos.getX() + 0.5F, (float) pos.getY() + 0.5F, (float) pos.getZ() + 0.5F);
    }

    public World getWorld() {
        return this.worldRef.get();
    }

}
