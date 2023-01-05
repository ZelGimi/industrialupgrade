package com.denfop.audio;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.ref.WeakReference;

public class AudioPosition {

    public final float x;
    public final float y;
    public final float z;
    private final WeakReference<World> worldRef;

    public AudioPosition(World world, float x, float y, float z) {
        this.worldRef = new WeakReference<>(world);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static AudioPosition getFrom(Object obj) {
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

    public World getWorld() {
        return this.worldRef.get();
    }

}
