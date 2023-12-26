
package com.denfop.render.base;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.denfop.blocks.state.TileEntityBlockStateContainer;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelComparator {
    private static final EnumFacing[] facings;
    private static final Byte UNCACHEABLE;
    private static final ConcurrentMap<CacheKey, Byte> cache;

    public ModelComparator() {
    }

    public static boolean isEqual(IBlockState stateA, IBlockState stateB, World world, BlockPos pos) {
        assert stateA != stateB;

        byte renderMask = 0;
        EnumFacing[] var5 = EnumFacing.VALUES;

        for (EnumFacing facing : var5) {
            boolean renderA = stateA.shouldSideBeRendered(world, pos, facing);
            boolean renderB = stateB.shouldSideBeRendered(world, pos, facing);
            if (renderA != renderB) {
                return false;
            }

            if (renderA) {
                renderMask = (byte) (renderMask | 1 << facing.ordinal());
            }
        }

        CacheKey cacheKey;
        Byte cacheResult;
        if (stateA.getClass() != stateB.getClass() || stateA.getClass() != BlockStateContainer.StateImplementation.class && (!(stateA instanceof TileEntityBlockStateContainer.PropertiesStateInstance) || ((TileEntityBlockStateContainer.PropertiesStateInstance)stateA).hasExtraProperties() || ((TileEntityBlockStateContainer.PropertiesStateInstance)stateB).hasExtraProperties()) && (!(stateA instanceof IExtendedBlockState) || ((IExtendedBlockState)stateA).getClean() != stateA || ((IExtendedBlockState)stateB).getClean() != stateB)) {
            cacheKey = null;
            cacheResult = UNCACHEABLE;
        } else {
            cacheKey = new CacheKey(stateA, stateB);
            cacheResult = cache.get(cacheKey);
            if (cacheResult != null && !cacheResult.equals(UNCACHEABLE)) {
                return (cacheResult | ~renderMask) == -1;
            }
        }



        BlockRendererDispatcher renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel modelA = renderer.getModelForState(stateA);
        IBakedModel modelB = renderer.getModelForState(stateB);
        Class<?> modelCls = modelA.getClass();
        if (modelB.getClass() != modelCls) {
            if (cacheResult == null) {
                cache.putIfAbsent(cacheKey, (byte)0);
            }

            return false;
        } else {
            if (cacheResult == null && modelCls != SimpleBakedModel.class && modelCls != BasicBakedBlockModel.class && !modelCls.getName().equals("net.minecraftforge.client.model.ModelLoader$VanillaModelWrapper$1")) {


                cacheResult = UNCACHEABLE;
                cache.putIfAbsent(cacheKey, UNCACHEABLE);
            }

            long rand = MathHelper.getPositionRandom(pos);
            byte equal = 63;

            label130:
            for (EnumFacing facing : facings) {
                if (cacheResult == null || facing == null || (renderMask & 1 << facing.ordinal()) != 0) {
                    List<BakedQuad> quadsA = modelA.getQuads(stateA, facing, rand);
                    List<BakedQuad> quadsB = modelB.getQuads(stateB, facing, rand);
                    if (quadsA.size() != quadsB.size()) {
                        if (cacheResult != null) {
                            return false;
                        }

                        if (facing == null) {
                            equal = 0;
                            break;
                        }

                        equal = (byte) (equal & ~(1 << facing.ordinal()));
                    } else if (!quadsA.isEmpty()) {
                        for (int i = 0; i < quadsA.size(); ++i) {
                            if (!Arrays.equals(quadsA.get(i).getVertexData(), quadsB.get(i).getVertexData())) {
                                if (cacheResult != null) {
                                    return false;
                                }

                                if (facing == null) {
                                    equal = 0;
                                    break label130;
                                }

                                equal = (byte) (equal & ~(1 << facing.ordinal()));
                                break;
                            }
                        }
                    }
                }
            }

            if (cacheResult != null) {
                return true;
            } else {
                cache.putIfAbsent(cacheKey, equal);
                return (equal | ~renderMask) == -1;
            }
        }
    }

    public static void onReload() {
        cache.clear();
    }

    static {
        facings = new EnumFacing[]{null, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST};
        UNCACHEABLE = -1;
        cache = new ConcurrentHashMap<>();
    }

    private static class CacheKey {
        private final IBlockState stateA;
        private final IBlockState stateB;

        CacheKey(IBlockState stateA, IBlockState stateB) {
            this.stateA = stateA;
            this.stateB = stateB;
        }

        public boolean equals(Object obj) {
            if (obj != null && obj.getClass() == CacheKey.class) {
                CacheKey o = (CacheKey)obj;
                return this.stateA == o.stateA && this.stateB == o.stateB || this.stateA == o.stateB && this.stateB == o.stateA;
            } else {
                return false;
            }
        }

        public int hashCode() {
            return System.identityHashCode(this.stateA) ^ System.identityHashCode(this.stateB);
        }
    }
}
