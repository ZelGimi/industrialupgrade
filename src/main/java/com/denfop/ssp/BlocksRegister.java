package com.denfop.ssp;




import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class BlocksRegister {
  public static Block iridiumOre = new SSPOre(Material.ROCK, "iridium_ore", 5.0F, 8.0F, "pickaxe", 3, SoundType.STONE);
  
  public static Block iridium_Block = new SSPB(Material.ROCK, "iridium_block", 8.0F, 15.0F, "pickaxe", 2, SoundType.STONE);
 public static Block platiumOre = new SSPOre(Material.ROCK, "platium_ore", 5.0F, 8.0F, "pickaxe", 3, SoundType.STONE);

  public static Block platium_Block = new SSPB(Material.ROCK, "platium_block", 8.0F, 15.0F, "pickaxe", 2, SoundType.STONE);
  
  //
  public static Block chromiumore=  new SSPOre(Material.ROCK, "chromium_ore", 5.0F, 8.0F, "pickaxe", 2, SoundType.STONE);
  public static Block wolframore=  new SSPOre(Material.ROCK, "wolfram_ore", 5.0F, 8.0F, "pickaxe", 2, SoundType.STONE);
  
  public static Block magnesiumore =  new SSPOre(Material.ROCK, "magnesium_ore", 5.0F, 8.0F, "pickaxe", 2, SoundType.STONE);
  
  public static Block mikhailovore =  new SSPOre(Material.ROCK, "mikhail_ore", 5.0F, 8.0F, "pickaxe", 2, SoundType.STONE);
  
  //
  public static Block chromium_Block = new SSPB(Material.ROCK, "chromium_block", 8.0F, 15.0F, "pickaxe", 2, SoundType.STONE);
  public static Block wolfram_Block = new SSPB(Material.ROCK, "wolfram_block", 8.0F, 15.0F, "pickaxe", 2, SoundType.STONE);
  
  public static Block magnesium_Block = new SSPB(Material.ROCK, "magnesium_block", 8.0F, 15.0F, "pickaxe", 2, SoundType.STONE);
  public static Block maltsev_Block = new SSPB(Material.ROCK, "maltsev_block", 8.0F, 15.0F, "pickaxe", 2, SoundType.STONE);
  
  public static Block mikhail_Block = new SSPB(Material.ROCK, "mikhail_block", 8.0F, 15.0F, "pickaxe", 2, SoundType.STONE);
 public static void register() {
	  setRegister(iridiumOre);
	  setRegister(iridium_Block);
	  setRegister(platiumOre);
	  setRegister(platium_Block);
	  setRegister(chromiumore);
	  setRegister(wolframore);
	  setRegister(magnesiumore);
	  setRegister(mikhailovore);
	  setRegister(chromium_Block);
	  setRegister(wolfram_Block);
	  setRegister(magnesium_Block);
	  setRegister(maltsev_Block);
	  setRegister(mikhail_Block);
   
  }
  
  public static void registerRender() {
	  setRender(iridiumOre);
    setRender(iridium_Block);
    setRender(platiumOre);
    setRender(platium_Block);
    setRender(chromiumore);
    setRender(wolframore);
    setRender(magnesiumore);
    setRender(mikhailovore);
    setRender(chromium_Block);
    setRender(wolfram_Block);
    setRender(magnesium_Block);
    setRender(maltsev_Block);
    setRender(mikhail_Block);
  
  }
  
  private static void setRegister(Block block) {
	  ForgeRegistries.BLOCKS.register((Block)block);
      ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
  }
  
  private static void setRender(Block block) {
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
  }
}
