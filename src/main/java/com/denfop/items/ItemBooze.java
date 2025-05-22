package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.brewage.EnumBeerVariety;
import com.denfop.api.brewage.EnumTimeVariety;
import com.denfop.api.brewage.EnumWaterVariety;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemBooze extends Item implements IModelRegister {

    private final String name;
    private final String path;
    public int[] baseDuration = new int[]{300, 900, 1200, 1600, 2000, 2400};
    public float[] baseIntensity = new float[]{0.4F, 0.75F, 1.0F, 1.5F, 2.0F};

    public ItemBooze(String name) {
        super();
        this.setCreativeTab(IUCore.CropsTab);
        this.setMaxStackSize(1);

        this.name = name;
        this.path = "";
        setUnlocalizedName(name);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    public EnumAction getItemUseAction(ItemStack stack) {
        return ModUtils.nbt(stack).hasKey("beer") ? EnumAction.DRINK : EnumAction.NONE;
    }

    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            subItems.add(new ItemStack(this));
            for (EnumWaterVariety enumWaterVariety : EnumWaterVariety.values()) {
                for (EnumTimeVariety enumTimeVariety : EnumTimeVariety.values()) {
                    for (EnumBeerVariety enumBeerVariety : EnumBeerVariety.values()) {
                        ItemStack stack = new ItemStack(this);
                        final NBTTagCompound nbtTagCompound = ModUtils.nbt(stack);
                        nbtTagCompound.setBoolean("beer", true);
                        nbtTagCompound.setByte("waterVariety", (byte) enumWaterVariety.ordinal());
                        nbtTagCompound.setByte("timeVariety", (byte) enumTimeVariety.ordinal());
                        nbtTagCompound.setByte("beerVariety", (byte) enumBeerVariety.ordinal());
                        nbtTagCompound.setByte("amount", (byte) 5);
                        subItems.add(stack);
                    }
                }
            }

        }
    }

    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase living) {

        NBTTagCompound nbtTagCompound = ModUtils.nbt(stack);
        if (!nbtTagCompound.hasKey("beer")) {
            return new ItemStack(this);
        } else {
            int level;
            EnumWaterVariety waterVariety = EnumWaterVariety.values()[nbtTagCompound.getByte("waterVariety")];
            EnumTimeVariety timeVariety = EnumTimeVariety.values()[nbtTagCompound.getByte("timeVariety")];
            EnumBeerVariety beerVariety = EnumBeerVariety.values()[nbtTagCompound.getByte("beerVariety")];
            if (timeVariety == EnumTimeVariety.BLACK_STUFF || waterVariety == EnumWaterVariety.BLACK_STUFF || beerVariety == EnumBeerVariety.BLACKSTUFF) {
                return this.drinkBlackStuff(living);
            }

            int solidRatio = waterVariety.ordinal();
            level = beerVariety.ordinal();
            int duration = this.baseDuration[solidRatio];
            float intensity = this.baseIntensity[timeVariety.ordinal()];
            if (living instanceof EntityPlayer) {
                ((EntityPlayer) living).getFoodStats().addStats(5 - level, (float) solidRatio * 0.15F);
            }

            int max = (int) (intensity * (float) level * 0.5F);
            PotionEffect slow = living.getActivePotionEffect(MobEffects.MINING_FATIGUE);
            level = -1;
            if (slow != null) {
                level = slow.getAmplifier();
            }
            nbtTagCompound.setByte("amount", (byte) (nbtTagCompound.getByte("amount") - 1));
            this.amplifyEffect(living, MobEffects.MINING_FATIGUE, max, intensity, duration);
            if (level > -1) {
                this.amplifyEffect(living, MobEffects.STRENGTH, max, intensity, duration);
                if (level > 0) {
                    this.amplifyEffect(living, MobEffects.SLOWNESS, max / 2, intensity, duration);
                    if (level > 1) {
                        this.amplifyEffect(living, MobEffects.RESISTANCE, max - 1, intensity, duration);
                        if (level > 2) {
                            this.amplifyEffect(living, MobEffects.NAUSEA, 0, intensity, duration);
                            if (level > 3) {
                                living.addPotionEffect(new PotionEffect(
                                        MobEffects.INSTANT_DAMAGE,
                                        1,
                                        living.getEntityWorld().rand.nextInt(3)
                                ));
                            }
                        }
                    }
                }
            }


            return nbtTagCompound.getByte("amount") > 0 ? stack : new ItemStack(this);
        }
    }

    public void amplifyEffect(EntityLivingBase living, Potion potion, int max, float intensity, int duration) {
        PotionEffect eff = living.getActivePotionEffect(potion);
        if (eff == null) {
            living.addPotionEffect(new PotionEffect(potion, duration, 0));
        } else {
            int currentDuration = eff.getDuration();
            int maxnewdur = (int) ((float) duration * (1.0F + intensity * 2.0F) - (float) currentDuration) / 2;
            if (maxnewdur < 0) {
                maxnewdur = 0;
            }

            if (maxnewdur < duration) {
                duration = maxnewdur;
            }

            currentDuration += duration;
            int newamp = eff.getAmplifier();
            if (newamp < max) {
                ++newamp;
            }

            living.addPotionEffect(new PotionEffect(potion, currentDuration, newamp));
        }

    }

    public ItemStack drinkBlackStuff(EntityLivingBase living) {
        switch (living.getEntityWorld().rand.nextInt(6)) {
            case 1:
                living.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 1200, 0));
                break;
            case 2:
                living.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 2400, 0));
                break;
            case 3:
                living.addPotionEffect(new PotionEffect(MobEffects.POISON, 2400, 0));
                break;
            case 4:
                living.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 2));
                break;
            case 5:
                living.addPotionEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, living.getEntityWorld().rand.nextInt(4)));
        }

        return new ItemStack(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            final List<String> tooltip,
            final ITooltipFlag flagIn
    ) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        NBTTagCompound nbtTagCompound = ModUtils.nbt(stack);
        if (nbtTagCompound.hasKey("beer")) {
            EnumWaterVariety waterVariety = EnumWaterVariety.values()[nbtTagCompound.getByte("waterVariety")];
            EnumTimeVariety timeVariety = EnumTimeVariety.values()[nbtTagCompound.getByte("timeVariety")];
            EnumBeerVariety beerVariety = EnumBeerVariety.values()[nbtTagCompound.getByte("beerVariety")];
            tooltip.add(Localization.translate("iu.beer.recipe.info"));
            tooltip.add(Localization.translate("iu.beer.recipe"));
            tooltip.add(Localization.translate("iu.beer.recipe1") + " " + beerVariety.getRatioOfComponents().get(0).getSecond());
            tooltip.add(Localization.translate("iu.beer.recipe2") + " " + beerVariety.getRatioOfComponents().get(0).getFirst());
            tooltip.add(Localization.translate("iu.beer.recipe3") + " " + waterVariety.getAmount().get(0));
            tooltip.add(Localization.translate("iu.beer.recipe4") + " " + new Timer((int) (timeVariety.getTime() * 60 * 60)).getDisplay());
            tooltip.add(Localization.translate("iu.beer.recipe5") + " " + waterVariety.name() + " " + beerVariety.name() + " " + timeVariety.name());

        }
    }

    public String getUnlocalizedName() {
        return super.getUnlocalizedName() + ".name";
    }

    public String getItemStackDisplayName(ItemStack stack) {
        NBTTagCompound nbtTagCompound = ModUtils.nbt(stack);
        if (!nbtTagCompound.hasKey("beer")) {
            return Localization.translate(this.getUnlocalizedName(stack).replace("item.", "iu.") + ".name");
        } else {
            EnumWaterVariety waterVariety = EnumWaterVariety.values()[nbtTagCompound.getByte("waterVariety")];
            EnumTimeVariety timeVariety = EnumTimeVariety.values()[nbtTagCompound.getByte("timeVariety")];
            EnumBeerVariety beerVariety = EnumBeerVariety.values()[nbtTagCompound.getByte("beerVariety")];
            return waterVariety.name() + " " + beerVariety.name() + " " + timeVariety.name();
        }
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            boolean hasKey = nbt.hasKey("beer");
            if (!hasKey) {
                return new ModelResourceLocation(Constants.MOD_ID + ":" + path + this.name, null);
            } else {
                EnumTimeVariety timeVariety = EnumTimeVariety.values()[nbt.getByte("timeVariety")];
                return new ModelResourceLocation(Constants.MOD_ID + ":" + path + this.name + "_" + timeVariety
                        .name()
                        .toLowerCase(), null);
            }
        });
        ModelBakery.registerItemVariants(
                this,
                new ModelResourceLocation(Constants.MOD_ID + ":" + path + this.name, null)
        );
        for (EnumTimeVariety timeVariety : EnumTimeVariety.values()) {
            ModelBakery.registerItemVariants(
                    this,
                    new ModelResourceLocation(
                            Constants.MOD_ID + ":" + path + this.name + "_" + timeVariety.name().toLowerCase(),
                            null
                    )
            );
        }
    }

}
