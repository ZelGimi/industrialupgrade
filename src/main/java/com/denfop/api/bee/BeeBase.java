package com.denfop.api.bee;

import com.denfop.api.agriculture.ICrop;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BeeBase implements IBee {

    private final int weatherResistance;
    private final int maxSwarm;
    private final ICrop cropFlower;
    private List<IBee> unCompatibleBees;
    private boolean sun;
    private boolean night;
    private int chance;
    private final int id;
    private final List<Biome> biomes = new ArrayList<>();
    private final int offspring;
    private final AxisAlignedBB sizeTerritory;
    private final int tickLifecycles;
    private final double maxMortalityRate;
    private final int tickBirthRate;
    private final List<Product> products = new ArrayList<>();
    private final String name;

    public BeeBase(
            String name, int id, int maxSwarm,
            int tickBirthRate, int tickLifecycles, AxisAlignedBB sizeTerritory, int offspring,
            int chance,  boolean sun, boolean night, ICrop cropFlower,
            List<IBee> unCompatibleBees, int defaultWeatherResistance, double maxMortalityRate
    ) {
        this.name = name;
        this.maxSwarm = maxSwarm;
        this.chance = chance;
        this.id = id;
        this.maxMortalityRate=maxMortalityRate;
        this.weatherResistance = defaultWeatherResistance;
        this.tickBirthRate = tickBirthRate;
        this.tickLifecycles = tickLifecycles;
        this.sizeTerritory = sizeTerritory;
        this.sun = sun;
        this.night = night;
        this.unCompatibleBees = unCompatibleBees;
        this.cropFlower = cropFlower;
        this.offspring = offspring;
        BeeNetwork.instance.addBee(this);
    }

    @Override
    public int getMaxSwarm() {
        return maxSwarm;
    }



    @Override
    public BeeBase copy() {
        BeeBase bee = new BeeBase(
                this.name,
                this.id,
                this.maxSwarm,
                this.tickBirthRate,
                this.tickLifecycles,
                this.sizeTerritory,
                this.offspring,
                this.chance,
                this.sun,
                this.night,
                this.cropFlower,
                new ArrayList<>(this.unCompatibleBees),
                weatherResistance,
                maxMortalityRate
        );
        for (Biome biome : biomes)
        bee.addBiome(biome);
        return bee;
    }


    @Override
    public ICrop getCropFlower() {
        return cropFlower;
    }




    @Override
    public List<IBee> getUnCompatibleBees() {
        return unCompatibleBees;
    }

    @Override
    public boolean isSun() {
        return sun;
    }


    @Override
    public boolean isNight() {
        return night;
    }


    @Override
    public int getWeatherResistance() {
        return weatherResistance;
    }
    @Override
    public void setUnCompatibleBees(final List<IBee> bees) {
        this.unCompatibleBees = bees;
    }



    @Override
    public int getChance() {
        return chance;
    }

    @Override
    public void setChance(int chance) {
        this.chance = chance;
    }


    @Override
    public int getId() {
        return id;
    }


    @Override
    public List<Biome> getBiomes() {
        return biomes;
    }



    @Override
    public boolean canWorkInBiome(Biome biomeName) {
        return biomes.contains(biomeName);
    }

    @Override
    public void addBiome(Biome biomeName) {
        biomes.add(biomeName);
    }



    @Override
    public int getOffspring() {
        return offspring;
    }


    @Override
    public AxisAlignedBB getSizeTerritory() {
        return sizeTerritory;
    }


    @Override
    public int getTickLifecycles() {
        return tickLifecycles;
    }


    @Override
    public double getMaxMortalityRate() {
        return maxMortalityRate;
    }

     @Override
    public int getTickBirthRate() {
        return tickBirthRate;
    }

    @Override
    public List<Product> getProduct() {
        return products;
    }

    @Override
    public void addPercentProduct(ICrop crop, double percent) {
        double totalChance = products.stream().mapToDouble(Product::getChance).sum();
        boolean find = false;
        if (totalChance <= 99.5) {
            for (Product product : products) {
                if (product.equals(crop)) {
                    product.addChance(0.1D);
                    find = true;
                    break;
                }
            }
            if (!find) {
                products.add(new Product(0.5, crop));
            }
        } else {
            Product product1 = null;
            for (Product product : products) {
                if (product.equals(crop)) {
                    product1 = product;
                    find = true;
                    break;
                }
            }
            if (product1 != null && products.size() == 1)
                return;
            products.sort((p1, p2) -> Double.compare(p2.getChance(), p1.getChance()));
            final Iterator<Product> iter = products.iterator();
            if (find) {
                double total = 0.5D / (products.size() - 1);
                int count = 0;
                while (iter.hasNext()) {
                    Product product = iter.next();
                    if (!product.equals(crop)) {
                        if (product.getChance() < total) {
                            double total1 = product.getChance() - total;
                            total += total1 / ((products.size() - 1) - count);
                        }
                    }
                    if (product.getChance() <= 0) {
                        iter.remove();
                    }
                }
                product1.addChance(0.5);
            } else {
                double total = 0.5D / (products.size());
                int count = 0;
                while (iter.hasNext()) {
                    Product product = iter.next();
                    if (product.getChance() < total) {
                        double total1 = product.getChance() - total;
                        total += total1 / ((products.size()) - count);
                    }
                    if (product.getChance() <= 0) {
                        iter.remove();
                    }
                }
                products.add(new Product(0.5, crop));
            }

        }
    }

    @Override
    public void removeAllPercent(double range) {
        final Iterator<Product> iter = products.iterator();
        while (iter.hasNext()) {
            Product product = iter.next();
            product.removeChance(range);
            if (product.getChance() <= 0) {
                iter.remove();
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }



    @Override
    public void readPacket(CustomPacketBuffer buffer) { /* Implementation logic */ }

    @Override
    public CustomPacketBuffer writePacket() {
        return null;
    }

}
