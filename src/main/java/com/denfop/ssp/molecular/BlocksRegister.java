package com.denfop.ssp.molecular;


import com.denfop.ssp.SSPBlock;
import com.denfop.ssp.SSPOre;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlocksRegister {
	public static final Block iridiumOre = new SSPOre(Material.ROCK, "iridium_ore", 5.0F, 8.0F, "pickaxe", 3, SoundType.STONE);

	public static final Block iridium_Block = new SSPBlock(Material.ROCK, "iridium_block", 8.0F, 15.0F, "pickaxe", 2, SoundType.STONE);
	public static final Block platiumOre = new SSPOre(Material.ROCK, "platium_ore", 5.0F, 8.0F, "pickaxe", 3, SoundType.STONE);

	public static final Block platium_Block = new SSPBlock(Material.ROCK, "platium_block", 8.0F, 15.0F, "pickaxe", 2, SoundType.STONE);

	public static void register() {
		setRegister(iridiumOre);
		setRegister(iridium_Block);
		setRegister(platiumOre);
		setRegister(platium_Block);
	}

	private static void setRegister(Block block) {
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}

	public static void registerRender() {
		setRender(iridiumOre);
		setRender(iridium_Block);
		setRender(platiumOre);
		setRender(platium_Block);
	}

	private static void setRender(Block block) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
}
