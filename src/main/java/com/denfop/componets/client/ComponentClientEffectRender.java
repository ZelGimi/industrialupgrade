package com.denfop.componets.client;

import com.denfop.api.windsystem.WindSystem;
import com.denfop.componets.AbstractComponent;
import com.denfop.componets.CoolComponent;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.water.TileBaseWaterGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class ComponentClientEffectRender extends AbstractComponent {

    private final EffectType effectType;

    public ComponentClientEffectRender(final TileEntityInventory parent, EffectType effectType) {
        super(parent);
        this.effectType = effectType;
    }

    @SideOnly(Side.CLIENT)
    public void render() {


        switch (effectType) {
            case HEAT:
                CoolComponent comp = parent.getComp(CoolComponent.class);
                if (comp == null || comp.upgrade) {
                    break;
                }

                Random rnd = this.parent.getWorld().rand;
                if (rnd.nextInt(8) == 0) {

                    int puffs = (int) (comp.getEnergy() / 10);

                    if (puffs > 0) {
                        puffs = rnd.nextInt(puffs);

                        int n;
                        for (n = 0; n < puffs; ++n) {
                            this.parent.getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                                    (float) this.parent.getPos().getX() + rnd.nextFloat(),
                                    (float) this.parent.getPos().getY() + 0.95F,
                                    (float) this.parent.getPos().getZ() + rnd.nextFloat(), 0.0D, 0.0D, 0.0D
                            );
                        }

                        puffs -= rnd.nextInt(4) + 3;

                        for (n = 0; n < puffs; ++n) {
                            this.parent.getWorld().spawnParticle(EnumParticleTypes.FLAME,
                                    (float) this.parent.getPos().getX() + rnd.nextFloat(),
                                    this.parent.getPos().getY() + 1,
                                    (float) this.parent.getPos().getZ() + rnd.nextFloat(), 0.0D, 0.0D, 0.0D
                            );
                        }
                    }

                }
                break;
            case WATER_GENERATOR:
                if (!(this.parent instanceof TileBaseWaterGenerator)) {
                    break;
                }
                TileBaseWaterGenerator baseWaterGenerator = (TileBaseWaterGenerator) this.parent;
                if (baseWaterGenerator.getActive()) {
                    rnd = this.parent.getWorld().rand;
                    if (WindSystem.windSystem.getLevelWind() <= 3) {
                        break;
                    }
                    if (rnd.nextInt(20 / WindSystem.windSystem.getLevelWind()) != 0) {
                        break;
                    }
                    int box = baseWaterGenerator.getRotorDiameter() / 2;
                    if (box == 0) {
                        break;
                    }
                    final BlockPos pos = baseWaterGenerator.getPos();
                    BlockPos pos1 = pos.add(baseWaterGenerator.getFacing().getDirectionVec());

                    switch (baseWaterGenerator.getFacing().getAxis()) {
                        case Y:
                            break;
                        case X:
                            for (int z = pos1.getZ() - 1; z <= pos1.getZ() + 1; z++) {
                                for (int y = pos1.getY() - 1; y <= pos1.getY() + 1; y++) {
                                    final BlockPos pos2 = new BlockPos(pos1.getX(), y, z);
                                    this.parent.getWorld().spawnParticle(EnumParticleTypes.WATER_BUBBLE,
                                            (float) pos2.getX() + rnd.nextFloat(),
                                            pos2.getY() + rnd.nextFloat(),
                                            (float) pos2.getZ() + rnd.nextFloat(),
                                            0.0D, 0.05D,
                                            -1D * (baseWaterGenerator
                                                    .getFacing()
                                                    .getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE
                                                    ? -1
                                                    : 1) * WindSystem.windSystem.getSpeed(Math.min(
                                                            24.7 + baseWaterGenerator.mind_speed,
                                                            WindSystem.windSystem.getSpeedFromPower(
                                                                    baseWaterGenerator.getBlockPos(),
                                                                    baseWaterGenerator,
                                                                    baseWaterGenerator.generation
                                                            )
                                                    ) * baseWaterGenerator.getCoefficient()
                                            ) * 2
                                    );
                                }
                            }
                            break;
                        case Z:
                            for (int x = pos1.getX() - 1; x <= pos1.getX() + 1; x++) {
                                for (int y = pos1.getY() - 1; y <= pos1.getY() + 1; y++) {
                                    final BlockPos pos2 = new BlockPos(x, y, pos1.getZ());
                                    this.parent.getWorld().spawnParticle(EnumParticleTypes.WATER_BUBBLE,
                                            (float) pos2.getX() + rnd.nextFloat(),
                                            pos2.getY() + rnd.nextFloat(),
                                            (float) pos2.getZ() + rnd.nextFloat(),
                                            -1D * (baseWaterGenerator
                                                    .getFacing()
                                                    .getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE
                                                    ? -1
                                                    : 1) * WindSystem.windSystem.getSpeed(Math.min(
                                                            24.7 + baseWaterGenerator.mind_speed,
                                                            WindSystem.windSystem.getSpeedFromPower(
                                                                    baseWaterGenerator.getBlockPos(),
                                                                    baseWaterGenerator,
                                                                    baseWaterGenerator.generation
                                                            )
                                                    ) * baseWaterGenerator.getCoefficient()
                                            ) * 2, 0.05D, 0.0D
                                    );
                                }
                            }
                            break;
                    }
                }
                break;
            case REFRIGERATOR:
                comp = parent.getComp(CoolComponent.class);
                if (comp == null) {
                    break;
                }

                rnd = this.parent.getWorld().rand;

                if (rnd.nextInt(8) == 0) {

                    int puffs = (int) (comp.getEnergy() / 4);

                    if (puffs > 0) {
                        puffs = rnd.nextInt(puffs);

                        int n;
                        for (n = 0; n < puffs; ++n) {
                            this.parent.getWorld().spawnParticle(EnumParticleTypes.SNOW_SHOVEL,
                                    (float) this.parent.getPos().getX() + rnd.nextFloat(),
                                    (float) this.parent.getPos().getY() + 1F,
                                    (float) this.parent.getPos().getZ() + rnd.nextFloat(), 0.0D, 0.0D, 0.0D
                            );
                        }
                    }

                }
                break;
        }
    }

}
