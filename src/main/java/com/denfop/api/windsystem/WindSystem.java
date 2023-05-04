package com.denfop.api.windsystem;

import com.denfop.api.windsystem.event.WindGeneratorEvent;
import com.denfop.tiles.mechanism.water.TileEntityBaseWaterGenerator;
import com.denfop.tiles.mechanism.wind.TileEntityWindGenerator;
import ic2.core.IC2;
import ic2.core.util.Util;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WindSystem implements IWindSystem {

    public static IWindSystem windSystem;
    public EnumTypeWind enumTypeWind = EnumTypeWind.ONE;
    public EnumWindSide windSide;
    public int tick = 12000;
    public EnumTypeWind[] enumTypeWinds = EnumTypeWind.values();
    List<IWindMechanism> mechanismList = new ArrayList<>();
    Random rand;
    Map<EnumFacing, EnumFacing> facingMap = new HashMap<>();
    private int time;
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
        if (windMechanism instanceof TileEntityWindGenerator) {
            ((TileEntityWindGenerator) windMechanism).setFacingWrench(facing, null);
            this.changeRotorSide(windMechanism, windMechanism.getFacing());
        } else {
            ((TileEntityBaseWaterGenerator) windMechanism).setFacingWrench(facing, null);
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
        if (windMechanism instanceof TileEntityWindGenerator) {
            ((TileEntityWindGenerator) windMechanism).setFacingWrench(newFacing, null);
            this.changeRotorSide(windMechanism, windMechanism.getFacing());
            IC2.network.get(true).updateTileEntityField(((TileEntityWindGenerator) windMechanism), "facing");
        } else {
            ((TileEntityBaseWaterGenerator) windMechanism).setFacingWrench(newFacing, null);
            this.changeRotorSide(windMechanism, windMechanism.getFacing());
            IC2.network.get(true).updateTileEntityField(((TileEntityBaseWaterGenerator) windMechanism), "facing");

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
        return Util.limit((this.getWind_Strength()) / (EnumTypeWind.TEN.getMax() * 1.5), 0.0D, 2.0D);
    }

    public double getSpeed(double speed) {
        return Util.limit((speed) / (EnumTypeWind.TEN.getMax() * 1.5), 0.0D, 2.0D);
    }

    @SubscribeEvent
    public void windTick(TickEvent.WorldTickEvent event) {
        if (event.world.provider.getDimension() != 0) {
            if (tick == 0) {
                windSide = EnumWindSide.getValue(this.rand.nextInt(8));
                for (IWindMechanism windMechanism : this.mechanismList) {
                    windMechanism.setCoefficient(getCoefficient(windMechanism));
                    if (windMechanism.getAuto()) {
                        this.getNewPositionOfMechanism(windMechanism);
                    }
                }
            }
            return;
        }

        tick--;
        if (tick == 0) {
            windSide = EnumWindSide.getValue(this.rand.nextInt(8));
            tick = 12000;
            for (IWindMechanism windMechanism : this.mechanismList) {
                windMechanism.setCoefficient(getCoefficient(windMechanism));
                if (windMechanism.getAuto()) {
                    this.getNewPositionOfMechanism(windMechanism);
                }
            }
        }

        World world = event.world;
        if (world.getWorldTime() % 20 == 0) {
            if (!world.isRaining()) {
                if (!world.isThundering()) {
                    if (world.getWorldInfo().getCleanWeatherTime() > 0) {
                        int time = world.getWorldInfo().getCleanWeatherTime();
                        if (time < 11000 && time >= 8000) {
                            this.time = time - 8000;
                            this.enumTypeWind = EnumTypeWind.ONE;
                        }
                        if (time < 8000 && time >= 5000) {
                            this.time = time - 5000;
                            this.enumTypeWind = EnumTypeWind.TWO;
                        }
                        if (time < 5000 && time >= 2500) {
                            this.time = time - 2500;
                            this.enumTypeWind = EnumTypeWind.THREE;
                        }
                        if (time < 2500 && time >= 1) {
                            this.time = time;
                            this.enumTypeWind = EnumTypeWind.FOUR;
                        }
                        double coef = this.enumTypeWind.getMax() - this.enumTypeWind.getMin();
                        coef *= 10;
                        this.Wind_Strength = this.enumTypeWind.getMin() + world.rand.nextInt((int) coef + 1) / 10D;
                    } else if (world.getWorldInfo().getRainTime() > 0) {
                        int time = world.getWorldInfo().getRainTime();
                        if (time > 150000) {
                            this.enumTypeWind = EnumTypeWind.ONE;
                            this.time = time - 150000;
                        } else if (time < 150000 && time >= 100000) {
                            this.enumTypeWind = EnumTypeWind.TWO;
                            this.time = time - 100000;
                        } else if (time < 100000 && time >= 60000) {
                            this.enumTypeWind = EnumTypeWind.THREE;
                            this.time = time - 60000;
                        } else if (time < 60000 && time >= 20000) {
                            this.enumTypeWind = EnumTypeWind.FOUR;
                            this.time = time - 20000;
                        } else if (time < 20000 && time >= 1) {
                            this.time = time;
                            this.enumTypeWind = EnumTypeWind.FIVE;
                        }
                        double coef = this.enumTypeWind.getMax() - this.enumTypeWind.getMin();
                        coef *= 10;
                        this.Wind_Strength = this.enumTypeWind.getMin() + world.rand.nextInt((int) coef + 1) / 10D;

                    } else if (world.getWorldInfo().getThunderTime() > 0) {
                        int time = world.getWorldInfo().getThunderTime();
                        if (time > 150000) {
                            this.enumTypeWind = EnumTypeWind.ONE;
                            this.time = time - 100000;
                        } else if (time < 150000 && time >= 100000) {
                            this.enumTypeWind = EnumTypeWind.TWO;
                            this.time = time - 100000;
                        } else if (time < 100000 && time >= 60000) {
                            this.enumTypeWind = EnumTypeWind.THREE;
                            this.time = time - 60000;
                        } else if (time < 60000 && time >= 20000) {
                            this.enumTypeWind = EnumTypeWind.FOUR;
                            this.time = time - 20000;
                        } else if (time < 20000 && time >= 1) {
                            this.enumTypeWind = EnumTypeWind.FIVE;
                            this.time = time;
                        }
                        double coef = this.enumTypeWind.getMax() - this.enumTypeWind.getMin();
                        coef *= 10;
                        this.Wind_Strength = this.enumTypeWind.getMin() + world.rand.nextInt((int) coef + 1) / 10D;


                    }
                } else {
                    int time = world.getWorldInfo().getThunderTime();
                    if (time > 20000) {
                        this.enumTypeWind = EnumTypeWind.SEVEN;
                        this.time = time - 20000;
                    }
                    if (time < 20000 && time >= 12000) {
                        this.enumTypeWind = EnumTypeWind.EIGHT;
                        this.time = time - 12000;
                    }
                    if (time < 12000 && time >= 5000) {
                        this.time = time - 5000;
                        this.enumTypeWind = EnumTypeWind.NINE;
                    }
                    if (time < 5000 && time >= 1) {
                        this.time = time;
                        this.enumTypeWind = EnumTypeWind.TEN;
                    }
                    double coef = this.enumTypeWind.getMax() - this.enumTypeWind.getMin();
                    coef *= 10;
                    this.Wind_Strength = this.enumTypeWind.getMin() + world.rand.nextInt((int) coef + 1) / 10D;

                }
            } else {

                if (world.getWorldInfo().getRainTime() > 0 && world.isRaining() && !world.isThundering()) {
                    int time = world.getWorldInfo().getRainTime();
                    if (time > 20000) {
                        this.time = time - 20000;
                        this.enumTypeWind = EnumTypeWind.FIVE;
                    }
                    if (time < 20000 && time >= 12000) {
                        this.time = time - 12000;
                        this.enumTypeWind = EnumTypeWind.SIX;
                    }
                    if (time < 12000 && time >= 5000) {
                        this.time = time - 5000;
                        this.enumTypeWind = EnumTypeWind.SEVEN;
                    }
                    if (time < 5000 && time >= 1) {
                        this.time = time;
                        this.enumTypeWind = EnumTypeWind.EIGHT;
                    }
                    double coef = this.enumTypeWind.getMax() - this.enumTypeWind.getMin();
                    coef *= 10;
                    this.Wind_Strength = this.enumTypeWind.getMin() + world.rand.nextInt((int) coef + 1) / 10D;

                } else if (world.getWorldInfo().getThunderTime() > 0) {
                    int time = world.getWorldInfo().getThunderTime();
                    if (time > 20000) {
                        this.time = time - 20000;
                        this.enumTypeWind = EnumTypeWind.SEVEN;
                    }
                    if (time < 20000 && time >= 12000) {
                        this.time = time - 12000;
                        this.enumTypeWind = EnumTypeWind.EIGHT;
                    }
                    if (time < 12000 && time >= 5000) {
                        this.time = time - 5000;
                        this.enumTypeWind = EnumTypeWind.NINE;
                    }
                    if (time < 5000 && time >= 1) {
                        this.time = time;
                        this.enumTypeWind = EnumTypeWind.TEN;
                    }
                    double coef = this.enumTypeWind.getMax() - this.enumTypeWind.getMin();
                    coef *= 10;
                    this.Wind_Strength = this.enumTypeWind.getMin() + world.rand.nextInt((int) coef + 1) / 10D;

                }
            }
            final double speed = getSpeed();
            for (IWindMechanism windMechanism : this.mechanismList) {
                if (windMechanism != null) {
                    windMechanism.setRotationSpeed((float) speed);
                }
            }
        }
    }

    public int getTime() {
        return time;
    }

    @Override
    public double getPower(final World world, final BlockPos pos, boolean min, IWindMechanism rotor) {
        if (world.provider.getDimension() != 0) {
            return 0;
        }
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
