package com.denfop.ssp.fluid.neutron;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRegister {
	public static final BlockNeutronFluid
			Neutron_BLOCK = new BlockNeutronFluid(FluidRegister.Neutron);

	public static void register() {
		setRegister();
	}

	private static void setRegister() {
		ForgeRegistries.BLOCKS.register(BlockRegister.Neutron_BLOCK);
		ForgeRegistries.ITEMS.register(new ItemBlock(BlockRegister.Neutron_BLOCK).setRegistryName(BlockRegister.Neutron_BLOCK.getRegistryName()));
	}

	@SideOnly(Side.CLIENT)
	public static void registerRender() {
		setRender();
	}

	@SideOnly(Side.CLIENT)
	private static void setRender() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(BlockRegister.Neutron_BLOCK), 0, new ModelResourceLocation(BlockRegister.Neutron_BLOCK.getRegistryName(), "inventory"));
	}
}
