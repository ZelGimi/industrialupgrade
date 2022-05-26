package aroma1997.uncomplication.enet;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class ChunkCoordinates
{
    public final int x;
    public final int z;
    public final int y;
    public ChunkCoordinates(int x,int y, int z)
    {
        this.x = x;
        this.z = z;
        this.y = y;
    }
    public BlockPos getPos(){
        return new BlockPos(x,y,z);
    }
    public ChunkCoordinates(BlockPos pos)
    {
        this.x = pos.getX();
        this.z = pos.getZ();
        this.y = pos.getY();
    }

    public int hashCode()
    {
        return this.x + this.z << 8 + this.y << 16;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof ChunkCoordinates))
        {
            return false;
        }
        else
        {
            ChunkCoordinates chunkpos = (ChunkCoordinates)p_equals_1_;
            return this.y == chunkpos.y && this.x == chunkpos.x && this.z == chunkpos.z;
        }
    }



    public BlockPos getBlock(int x, int y, int z)
    {
        return new BlockPos((this.x << 4) + x, y, (this.z << 4) + z);
    }

    public String toString()
    {
        return "[" + this.x + ", " + this.z + "]";
    }
}
