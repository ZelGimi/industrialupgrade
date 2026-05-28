package com.denfop.villager.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Optional;

public class ProfessionHamletStructure extends Structure {

    public static final Codec<ProfessionHamletStructure> CODEC = simpleCodec(ProfessionHamletStructure::new);

    public ProfessionHamletStructure(final StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(final GenerationContext context) {
        final int startX = context.chunkPos().getMiddleBlockX() - (ProfessionHamletPiece.WIDTH / 2);
        final int startZ = context.chunkPos().getMiddleBlockZ() - (ProfessionHamletPiece.DEPTH / 2);

        final int centerX = startX + ProfessionHamletPiece.WIDTH / 2;
        final int centerZ = startZ + ProfessionHamletPiece.DEPTH / 2;

        final int y = context.chunkGenerator().getBaseHeight(
                centerX,
                centerZ,
                net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG,
                context.heightAccessor(),
                context.randomState()
        );

        if (y < 62) {
            return Optional.empty();
        }

        final BlockPos origin = new BlockPos(startX, y, startZ);
        final boolean includeNuclear = context.random().nextInt(100) < 25;

        return Optional.of(new GenerationStub(origin, builder ->
                builder.addPiece(new ProfessionHamletPiece(origin, includeNuclear))
        ));
    }

    @Override
    public net.minecraft.world.level.levelgen.structure.StructureType<?> type() {
        return ModVillageStructures.PROFESSION_HAMLET.get();
    }
}
