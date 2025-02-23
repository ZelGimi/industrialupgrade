package com.denfop.api.windsystem;

import com.denfop.api.windsystem.event.WindGeneratorEvent;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.mechanism.water.TileBaseWaterGenerator;
import com.denfop.tiles.mechanism.wind.TileWindGenerator;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WindSystem implements IWindSystem {

    public static IWindSystem windSystem;
    public EnumTypeWind enumTypeWind;
    public EnumWindSide windSide;
    public int tick = 12000;
    public EnumTypeWind[] enumTypeWinds = EnumTypeWind.values();
    List<IWindMechanism> mechanismList = new ArrayList<>();
    Random rand;
    Map<EnumFacing, EnumFacing> facingMap = new HashMap<>();
    private double Wind_Strength;

    public WindSystem() {
        windSystem = this;
        MinecraftForge.EVENT_BUS.register(this);
        this.rand = new Random();
        this.windSide = EnumWindSide.getValue(this.rand.nextInt(8));
        facingMap.put(EnumFacing.EAST, EnumFacing.NORTH);
        facingMap.put(EnumFacing.NORTH, EnumFacing.WEST);
        facingMap.put(EnumFacing.WEST, EnumFacing.SOUTH);
        facingMap.put(EnumFacing.SOUTH, EnumFacing.EAST);
        enumTypeWind = EnumTypeWind.values()[rand.nextInt(10)];
    }

    public EnumTypeWind getEnumTypeWind() {
        return enumTypeWind;
    }

    public int getLevelWind() {
        return enumTypeWind.ordinal() + 1;
    }

    public EnumWindSide getWindSide() {
        return windSide;
    }

    public void getNewFacing(EnumFacing facing, IWindMechanism windMechanism) {
        if (facing == EnumFacing.DOWN || facing == EnumFacing.UP) {
            return;
        }
        facing = facingMap.get(facing);
        if (windMechanism instanceof TileWindGenerator) {
            ((TileWindGenerator) windMechanism).setFacingWrench(facing, null);
            new PacketUpdateFieldTile(((TileWindGenerator) windMechanism), "facing", (byte) windMechanism.getFacing().ordinal());
            this.changeRotorSide(windMechanism, windMechanism.getFacing());
        } else {
            ((TileBaseWaterGenerator) windMechanism).setFacingWrench(facing, null);
            new PacketUpdateFieldTile(
                    ((TileBaseWaterGenerator) windMechanism),
                    "facing",
                    (byte) windMechanism.getFacing().ordinal()
            );
            this.changeRotorSide(windMechanism, windMechanism.getFacing());
        }
    }

    @SubscribeEvent
    public void LoadWindMechanism(WindGeneratorEvent event) {

        final IWindMechanism windMechanism = event.getWindMechanism();
        if (event.getLoad()) {
            windMechanism.setCoefficient(getCoefficient(windMechanism));
            if (windMechanism.getAuto()) {
                this.getNewPositionOfMechanism(windMechanism);
                windMechanism.setCoefficient(getCoefficient(windMechanism));
            }
            if (!mechanismList.contains(windMechanism)) {
                mechanismList.add(windMechanism);
            }
        } else {
            mechanismList.remove(windMechanism);
        }


    }

    @SubscribeEvent
    public void EventWorldUnload(WorldEvent.Unload event) {
        if (event.getWorld().provider.getDimension() == 0) {
            this.mechanismList.clear();
        }
    }

    public double getWind_Strength() {
        return this.Wind_Strength;
    }

    public void changeRotorSide(IWindMechanism windMechanism, EnumFacing facing) {
        windMechanism.setRotorSide(getRotorSide(facing));
        windMechanism.setCoefficient(getCoefficient(windMechanism));
    }

    public void getNewPositionOfMechanism(IWindMechanism windMechanism) {
        final EnumFacing newFacing = getNewFacing();
        if (windMechanism instanceof TileWindGenerator) {
            if (windMechanism.getFacing() != newFacing) {
                ((TileWindGenerator) windMechanism).setFacingWrench(newFacing, null);
                new PacketUpdateFieldTile(
                        ((TileWindGenerator) windMechanism),
                        "facing",
                        (byte) windMechanism.getFacing().ordinal()
                );
                this.changeRotorSide(windMechanism, windMechanism.getFacing());
            }

        } else {
            if (windMechanism.getFacing() != newFacing) {
                ((TileBaseWaterGenerator) windMechanism).setFacingWrench(newFacing, null);
                new PacketUpdateFieldTile(
                        ((TileBaseWaterGenerator) windMechanism),
                        "facing",
                        (byte) windMechanism.getFacing().ordinal()
                );
                this.changeRotorSide(windMechanism, windMechanism.getFacing());
            }
        }
    }

    public EnumFacing getNewFacing() {
        switch (this.windSide) {
            case E:
                return EnumFacing.SOUTH;
            case W:
            case SW:
                return EnumFacing.NORTH;
            case N:
            case NW:
            case SE:
                return EnumFacing.EAST;
            case S:
            case NE:
                return EnumFacing.WEST;
        }
        return null;

    }

    public EnumRotorSide getRotorSide(EnumFacing facing) {
        switch (facing) {
            case EAST:
                return EnumRotorSide.E;
            case WEST:
                return EnumRotorSide.W;
            case NORTH:
                return EnumRotorSide.N;
            case SOUTH:
                return EnumRotorSide.S;
        }
        return null;
    }

    public double getCoefficient(IWindMechanism windMechanism) {
        if (windMechanism == null) {
            return 0;
        }
        final EnumHorizonSide side = this.windSide.getList().get(0);
        final EnumRotorSide side_rotor = windMechanism.getRotorSide();
        final EnumHorizonSide bad_sides = side_rotor.getBad_sides();
        final EnumHorizonSide good_side = side_rotor.getGood_sides();
        final EnumHorizonSide neutral_side = side_rotor.getNeutral_sides();
        for (EnumHorizonSide side2 : side.getEnumWindSide()) {
            for (EnumHorizonSide side1 : good_side.getEnumWindSide()) {
                if (side1 == side2) {
                    return 1;
                }
            }
            for (EnumHorizonSide side1 : neutral_side.getEnumWindSide()) {
                if (side1 == side2) {
                    return 0.75;
                }
            }
            for (EnumHorizonSide side1 : bad_sides.getEnumWindSide()) {
                if (side1 == side2) {
                    return 0.5;
                }
            }
        }
        return 0;
    }

    public double getSpeed() {
        return ModUtils.limit((this.getWind_Strength()) / (EnumTypeWind.TEN.getMax() * 1.5), 0.0D, 2.0D);
    }

    public double getSpeed(double speed) {
        return ModUtils.limit((speed) / (EnumTypeWind.TEN.getMax() * 1.5), 0.0D, 2.0D);
    }

    @SubscribeEvent
    public void windTick(TickEvent.WorldTickEvent event) {
        if (event.side == Side.CLIENT) {
            return;
        }
        if (event.phase == TickEvent.Phase.START) {
            return;
        }
        if (event.world.provider.getDimension() == 0) {
            this.tick--;
        }


        if (tick == 0) {
            windSide = EnumWindSide.getValue(this.rand.nextInt(8));

            if (this.enumTypeWind == null) {
                this.enumTypeWind = EnumTypeWind.values()[rand.nextInt(EnumTypeWind.values().length)];
            } else {
                int rand = this.rand.nextInt(100);
                switch (this.enumTypeWind.ordinal()) {
                    case 0:
                        if (rand >= 10) {
                            this.enumTypeWind = EnumTypeWind.TWO;
                        } else {
                            this.enumTypeWind = EnumTypeWind.ONE;
                        }
                        tick = 12000;
                        break;
                    case 1:
                        if (rand >= 20) {
                            this.enumTypeWind = EnumTypeWind.THREE;
                        } else {
                            if (this.rand.nextInt(100) > 50) {
                                this.enumTypeWind = EnumTypeWind.ONE;
                            }
                        }
                        tick = 12000;
                        break;
                    case 2:
                        if (rand >= 30) {
                            this.enumTypeWind = EnumTypeWind.FOUR;
                        } else {
                            if (this.rand.nextInt(100) > 50) {
                                this.enumTypeWind = EnumTypeWind.TWO;
                            }
                        }
                        tick = 13000;
                        break;
                    case 3:
                        if (rand >= 40) {
                            this.enumTypeWind = EnumTypeWind.FIVE;
                        } else {
                            if (this.rand.nextInt(100) > 50) {
                                this.enumTypeWind = EnumTypeWind.THREE;
                            }
                        }
                        tick = 14000;
                        break;
                    case 4:
                        if (rand >= 50) {
                            this.enumTypeWind = EnumTypeWind.SIX;
                        } else {
                            if (this.rand.nextInt(100) > 50) {
                                this.enumTypeWind = EnumTypeWind.FOUR;
                            }
                        }

                        tick = 15000;
                        break;
                    case 5:
                        if (rand >= 60) {
                            this.enumTypeWind = EnumTypeWind.SEVEN;
                        } else {
                            if (this.rand.nextInt(100) > 50) {
                                this.enumTypeWind = EnumTypeWind.FIVE;
                            }
                        }
                        tick = 15000;
                        break;
                    case 6:
                        if (rand >= 70) {
                            this.enumTypeWind = EnumTypeWind.EIGHT;
                        } else {
                            if (this.rand.nextInt(100) > 50) {
                                this.enumTypeWind = EnumTypeWind.SIX;
                            }
                        }
                        tick = 16000;
                        break;
                    case 7:
                        if (rand >= 80) {
                            this.enumTypeWind = EnumTypeWind.NINE;
                        } else {
                            if (this.rand.nextInt(100) > 50) {
                                this.enumTypeWind = EnumTypeWind.SEVEN;
                            }
                        }
                        tick = 15000;
                        break;
                    case 8:
                        if (rand >= 90) {
                            this.enumTypeWind = EnumTypeWind.TEN;
                        } else {
                            if (this.rand.nextInt(100) > 50) {
                                this.enumTypeWind = EnumTypeWind.EIGHT;
                            }
                        }
                        tick = 14000;
                        break;
                    case 9:
                        if (this.rand.nextInt(100) > 50) {
                            this.enumTypeWind = EnumTypeWind.NINE;
                        }

                        tick = 12000;
                        break;
                }
            }

            for (IWindMechanism windMechanism : this.mechanismList) {
                windMechanism.setCoefficient(getCoefficient(windMechanism));
                if (windMechanism.getAuto()) {
                    this.getNewPositionOfMechanism(windMechanism);
                    windMechanism.setCoefficient(getCoefficient(windMechanism));
                }
            }
        }

        World world = event.world;
        if (world.getWorldTime() % 20 == 0) {
            double coef = this.enumTypeWind.getMax() - this.enumTypeWind.getMin();
            coef *= 10;
            this.Wind_Strength = this.enumTypeWind.getMin() + world.rand.nextInt((int) coef + 1) / 10D;
            final double speed = getSpeed();
            for (IWindMechanism windMechanism : this.mechanismList) {
                if (windMechanism != null) {
                    windMechanism.setRotationSpeed((float) speed);
                }
            }
        }
    }

    public int getTime() {
        return tick;
    }

    @Override
    public double getPower(final World world, final BlockPos pos, boolean min, IWindMechanism rotor) {

        if (world.isRemote) {
            return 0;
        }
        if (rotor.getMinWind() != 0) {
            int meta = Math.min(this.enumTypeWind.ordinal() + rotor.getMinWind(), 9);
            EnumTypeWind enumTypeWinds = this.enumTypeWinds[meta];
            double coef = enumTypeWinds.getMax() - enumTypeWinds.getMin();
            coef *= 10;

            coef = enumTypeWinds.getMin() + world.rand.nextInt((int) coef + 1) / 10D;
            coef += rotor.getMinWindSpeed();
            coef = Math.min(coef, EnumTypeWind.TEN.getMax());
            int y = pos.getY();
            if (min) {
                y = 150;
            }
            if (y < 150) {
                coef = coef * (y / 150D);
            } else {
                coef = coef * (150D / y);
            }
            return coef * 27;
        } else {
            double coef = this.Wind_Strength;
            coef += rotor.getMinWindSpeed();
            int y = pos.getY();
            if (min) {
                y = 150;
            }
            if (y < 150) {
                coef = coef * (y / 150D);
            } else {
                coef = coef * (150D / y);
            }
            return coef * 27;
        }
    }

    @Override
    public double getSpeedFromPower(final BlockPos pos, final IWindMechanism rotor, double power) {


        double copy_power = power / 27;
        copy_power = copy_power / ((rotor
                .getRotor()
                .getEfficiency(rotor.getItemStack()) * (1 + rotor.getAdditionalPower())) * (rotor.getCoefficient() * (1 + rotor.getAdditionalCoefficient())));

        int y = pos.getY();
        if (rotor.getMin()) {
            y = 150;
        }
        if (y < 150) {
            copy_power = copy_power / (y / 150D);
        } else {
            copy_power = copy_power / (150D / y);
        }

        return copy_power;

    }

    @Override
    public double getSpeedFromWaterPower(final BlockPos pos, final IWindMechanism rotor, double power) {


        double copy_power = power / 27;
        copy_power = copy_power / ((rotor
                .getRotor()
                .getEfficiency(rotor.getItemStack()) * (1 + rotor.getAdditionalPower())) * (rotor.getCoefficient() * (1 + rotor.getAdditionalCoefficient())));


        return copy_power;

    }

    @Override
    public double getPowerFromWindRotor(
            final World world,
            final BlockPos pos,
            final IWindMechanism windMechanism,
            ItemStack stack
    ) {

        return this.getPower(world, pos, windMechanism.getMin(), windMechanism) * (windMechanism
                .getRotor()
                .getEfficiency(stack) * (1 + windMechanism.getAdditionalPower())) * (windMechanism.getCoefficient() * (1 + windMechanism.getAdditionalCoefficient()));
    }

    @Override
    public double getPowerFromWaterRotor(
            final World world,
            final IWindMechanism windMechanism,
            ItemStack stack
    ) {

        return (this.getPower(world, new BlockPos(0, 150, 0), windMechanism.getMin(), windMechanism) / 27D) * 25 * (windMechanism
                .getRotor()
                .getEfficiency(stack) * (1 + windMechanism.getAdditionalPower())) * (windMechanism.getCoefficient() * (1 + windMechanism.getAdditionalCoefficient()));
    }

}
