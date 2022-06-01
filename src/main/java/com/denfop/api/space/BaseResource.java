package com.denfop.api.space;

import net.minecraft.item.ItemStack;

public class BaseResource implements IBaseResource {

    private ItemStack stack;
    private int max;
    private int min;
    private IBody body;
    private int percentplanet;

    public BaseResource(ItemStack stack, int minchance, int maxchance, int percentplanet, IBody body) {
        this.stack = stack;
        this.max = maxchance;
        this.min = minchance;
        this.body = body;
        this.percentplanet = percentplanet;
        SpaceNet.instance.addResource(this);
    }

    public BaseResource(ItemStack stack, IBody body) {
        if (body instanceof IPlanet) {
            IPlanet planet = SpaceNet.instance.getPlanetList().get(SpaceNet.instance.getPlanetList().indexOf(body));
            this.stack = stack;
            for (IBaseResource resourceList : planet.getResources()) {
                if (resourceList.getItemStack().isItemEqual(stack)) {
                    this.max = resourceList.getMaxChance();
                    this.min = resourceList.getChance();
                    this.percentplanet = resourceList.getPercentPanel();
                    this.body = body;
                    break;
                }
            }
        } else if (body instanceof ISatellite) {
            ISatellite planet = SpaceNet.instance.getSatelliteList().get(SpaceNet.instance.getSatelliteList().indexOf(body));
            this.stack = stack;
            for (IBaseResource resourceList : planet.getResources()) {
                if (resourceList.getItemStack().isItemEqual(stack)) {
                    this.max = resourceList.getMaxChance();
                    this.min = resourceList.getChance();
                    this.percentplanet = resourceList.getPercentPanel();
                    this.body = body;
                    break;
                }
            }
        }
    }

    public BaseResource(ItemStack stack, int minchance, IBody body, int percentplanet) {
        this(stack, minchance, 100, percentplanet, body);

    }

    @Override
    public ItemStack getItemStack() {
        return this.stack;
    }

    @Override
    public int getChance() {
        return this.min;
    }

    @Override
    public int getMaxChance() {
        return this.max;
    }

    @Override
    public IBody getBody() {
        return this.body;
    }

    @Override
    public int getPercentPanel() {
        return this.percentplanet;
    }

}
