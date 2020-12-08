package com.denfop.ssp.integration.thaumcraft;

import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;

public class ThaumcraftMain {
	public static ResearchCategory SSPT;
	public static final Aspect Sun =  new Aspect("Sun", 16758272, new Aspect[] { Aspect.LIGHT, Aspect.AIR }, new ResourceLocation("super_solar_panels", "textures/aspects/sun.png"), 1);
	public static final Aspect Night  = new Aspect("Night", 16758273, new Aspect[] { ThaumcraftMain.Sun, Aspect.ENTROPY }, new ResourceLocation("super_solar_panels", "textures/aspects/night.png"), 1);
	public static final Aspect Energy   =  new Aspect("Energy", 16758274, new Aspect[] { Aspect.ENERGY, Aspect.MECHANISM }, new ResourceLocation("super_solar_panels", "textures/aspects/energy.png"), 1);
	public static void init() {
		SSPTab();
		Scan();
		Theory();
	}
	private static void Scan() {
		// TODO Auto-generated method stub
		
	}
	private static void Theory() {
		// TODO Auto-generated method stub
		
	}
	private static void SSPTab() {
		SSPT = ResearchCategories.registerCategory("SSPT","SSPTKEY", (new AspectList()).add(Aspect.ALCHEMY,10),new ResourceLocation("super_solar_panels", "textures/thaumcraft/ssptab.png"),new ResourceLocation("super_solar_panels", "textures/thaumcraft/ssptabbackground.png"),new ResourceLocation("super_solar_panels", "textures/thaumcraft/ssptabbackground1.png"));
		
	}
}
