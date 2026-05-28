package com.denfop.api.space.dimension.worldgen;

import com.denfop.api.space.dimension.SpaceDimensionProfile;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public final class SpaceSurfaceRuleFactory {

    public static final int BEDROCK_LAYERS = 3;

    private SpaceSurfaceRuleFactory() {
    }

    public static SurfaceRules.RuleSource create(final SpaceDimensionProfile profile) {
        if (!profile.generatesTerrain()) {
            return SurfaceRules.state(Blocks.AIR.defaultBlockState());
        }

        SurfaceRules.RuleSource floor = SurfaceRules.ifTrue(
                SurfaceRules.ON_FLOOR,
                SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.steep(), SurfaceRules.state(profile.rimBlock())),
                        SurfaceRules.state(profile.topBlock())
                )
        );

        SurfaceRules.RuleSource underground = SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.state(profile.subsurfaceBlock()));
        SurfaceRules.RuleSource volcanic = SurfaceRules.ifTrue(
                SurfaceRules.yBlockCheck(net.minecraft.world.level.levelgen.VerticalAnchor.absolute(profile.seaLevel() - 8), 0),
                SurfaceRules.state(profile.cobbleBlock())
        );
        SurfaceRules.RuleSource bedrockFloor = addBedrockFloor(SurfaceRules.state(profile.subsurfaceBlock()));

        return switch (profile.generationMode()) {
            case VOLCANIC ->
                    SurfaceRules.sequence(floor, volcanic, underground, SurfaceRules.state(profile.defaultBlock()), bedrockFloor);
            case ICY_SHELL, CRYO_CHEMICAL -> SurfaceRules.sequence(
                    SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SurfaceRules.state(profile.subsurfaceBlock())),
                    floor,
                    underground,
                    SurfaceRules.state(profile.defaultBlock()),
                    bedrockFloor
            );
            default -> SurfaceRules.sequence(floor, underground, SurfaceRules.state(profile.defaultBlock()));
        };
    }

    public static SurfaceRules.RuleSource addBedrockFloor(final SurfaceRules.RuleSource baseRule) {
        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(
                        SurfaceRules.verticalGradient(
                                "iu_space_bedrock_floor",
                                VerticalAnchor.bottom(),
                                VerticalAnchor.aboveBottom(BEDROCK_LAYERS)
                        ),
                        SurfaceRules.state(Blocks.BEDROCK.defaultBlockState())
                ),
                baseRule
        );
    }
}
