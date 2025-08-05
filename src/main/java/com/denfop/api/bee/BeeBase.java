package com.denfop.api.bee;

import com.denfop.api.agriculture.ICrop;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class BeeBase implements IBee {

    private final int weatherResistance;
    private final int maxSwarm;
    private final ICrop cropFlower;
    private final int id;
    private final List<ResourceKey<Biome>> biomes = new ArrayList<>();
    private final int offspring;
    private final AABB sizeTerritory;
    private final int tickLifecycles;
    private final double maxMortalityRate;
    private final int tickBirthRate;
    private final List<Product> products = new ArrayList<>();
    private final String name;
    private List<IBee> unCompatibleBees;
    private boolean sun;
    private boolean night;
    private int chance;

    public BeeBase(
            String name, int id, int maxSwarm,
            int tickBirthRate, int tickLifecycles, AABB sizeTerritory, int offspring,
            int chance, boolean sun, boolean night, ICrop cropFlower,
            List<IBee> unCompatibleBees, int defaultWeatherResistance, double maxMortalityRate
    ) {
        this.name = name;
        this.maxSwarm = maxSwarm;
        this.chance = chance;
        this.id = id;
        this.maxMortalityRate = maxMortalityRate;
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
        for (ResourceKey<Biome> biome : biomes)
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
    public void setUnCompatibleBees(final List<IBee> bees) {
        this.unCompatibleBees = bees;
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
    public List<ResourceKey<Biome>> getBiomes() {
        return biomes;
    }


    @Override
    public boolean canWorkInBiome(ResourceKey<Biome> biomeName) {
        return biomes.contains(biomeName);
    }

    public boolean canWorkInBiome(Biome biomeName, Level level) {
        ResourceKey<Biome> biomeKey = level.registryAccess()
                .registryOrThrow(Registries.BIOME)
                .getResourceKey(biomeName).get();
        return biomes.contains(biomeKey);
    }

    @Override
    public void addBiome(ResourceKey<Biome> biomeName) {
        biomes.add(biomeName);
    }


    @Override
    public int getOffspring() {
        return offspring;
    }


    @Override
    public AABB getSizeTerritory() {
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

        Product target = null;
        for (Product product : products) {
            if (product.getCrop().getId() == crop.getId()) {
                target = product;
                break;
            }
        }


        if (target == null) {
            target = new Product(0, crop);
            products.add(target);
        }


        double currentTotal = products.stream().mapToDouble(Product::getChance).sum();


        double availableSpace = 100.0 - currentTotal;


        double actualAdd = Math.min(percent, availableSpace);
        target.addChance(actualAdd);


        double overflow = percent - actualAdd;


        if (overflow > 0) {
            double toReduce = overflow;


            Product finalTarget = target;
            List<Product> others = products.stream()
                    .filter(p -> p != finalTarget)
                    .sorted(Comparator.comparingDouble(Product::getChance).reversed())
                    .toList();

            for (Product other : others) {
                if (toReduce <= 0) break;

                double chance = other.getChance();
                double reducible = Math.min(chance, toReduce);

                other.addChance(-reducible);
                toReduce -= reducible;
            }


            products.removeIf(p -> p.getChance() <= 0);
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
    public CustomPacketBuffer writePacket(CustomPacketBuffer o) {
        return null;
    }

}
