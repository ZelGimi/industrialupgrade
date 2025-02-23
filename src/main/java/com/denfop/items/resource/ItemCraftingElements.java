package com.denfop.items.resource;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemBaseCircuit;
import com.denfop.recipes.ScrapboxRecipeManager;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class ItemCraftingElements extends ItemSubTypes<ItemCraftingElements.Types> implements IModelRegister {

    protected static final String NAME = "crafting_elements";

    public ItemCraftingElements() {
        super(Types.class);
        this.setCreativeTab(IUCore.ElementsTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack p_77624_1_,
            @Nullable final World p_77624_2_,
            final List<String> p_77624_3_,
            final ITooltipFlag p_77624_4_
    ) {
        if (p_77624_1_.getItemDamage() == 143) {
            final NBTTagCompound nbt = ModUtils.nbt(p_77624_1_);
            NBTTagList nbtTagList = nbt.getTagList("Items", 10);
            if (!nbtTagList.hasNoTags()) {
                p_77624_3_.add(Localization.translate("multiblock." + nbt.getString("name")));
                p_77624_3_.add("Generation: " + nbt.getInteger("generation") + " EF");
                p_77624_3_.add("Radiation: " + nbt.getInteger("rad") + " ☢");

                for (int i = 0; i < nbtTagList.tagCount(); ++i) {
                    NBTTagCompound contentTag = nbtTagList.getCompoundTagAt(i);
                    ItemStack stack = new ItemStack(contentTag);
                    p_77624_3_.add(TextFormatting.GREEN + "" + stack.getCount() + "x " + stack.getDisplayName());
                }
            }
        }
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
    }

    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {

            for (final Types type : this.typeProperty.getAllowedValues()) {
                subItems.add(this.getItemStackUnchecked(type));

            }

        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(
            @Nonnull final World world,
            @Nonnull final EntityPlayer player,
            @Nonnull final EnumHand hand
    ) {
        if (!player.getHeldItem(hand).isItemEqual(IUItem.scrapBox)) {
            return super.onItemRightClick(world, player, hand);
        } else {
            int i = 0;
            ItemStack stack = player.getHeldItem(hand);
            while (i < 1) {
                if (!player.getEntityWorld().isRemote) {
                    ItemStack drop = ScrapboxRecipeManager.instance.getDrop(IUItem.scrapBox);
                    player.dropItem(drop, false);
                }
                i++;
            }
            stack.shrink(1);
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));

        }
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, final int meta, final String extraName) {
        if (meta >= 272 && meta <= 273) {
            ModelLoader.setCustomMeshDefinition(this, stack -> {
                final NBTTagCompound nbt = ModUtils.nbt(stack);
                int level = nbt.getInteger("level");
                switch (stack.getItemDamage()){
                    case 272:
                        level = level - 1;
                        break;
                    case 273:
                        level = level - 3;
                        break;
                }
                return new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types
                        .getFromID(stack.getItemDamage()).getName() + (level == 1 ? "_1" : ""),
                        null);

            });
            String[] mode = {"", "_1"};
            for (final String s : mode) {
                ModelBakery.registerItemVariants(this,
                        new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName()+s,
                                null));

            }
        } else {
            ModelLoader.setCustomModelResourceLocation(
                    this,
                    meta,
                    new ModelResourceLocation(
                            Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(),
                            null
                    )
            );
        }
    }

    public enum Types implements ISubEnum {
        crafting_0_element(),
        crafting_1_element(),
        crafting_2_element(),
        crafting_3_element(),
        crafting_4_element(),
        crafting_5_element(),
        crafting_6_element(),
        crafting_7_element(),
        crafting_8_element(),
        crafting_9_element(),
        crafting_10_element(),
        crafting_11_element(),
        crafting_12_element(),
        crafting_13_element(),
        crafting_14_element(),
        crafting_15_element(),
        crafting_16_element(),
        crafting_17_element(),
        crafting_18_element(),
        crafting_19_element(),
        crafting_20_element(),
        crafting_21_element(),
        crafting_22_element(),
        crafting_23_element(),
        crafting_24_element(),
        crafting_25_element(),
        crafting_26_element(),
        crafting_27_element(),
        crafting_28_element(),
        crafting_29_element(),
        crafting_30_element(),
        crafting_31_element(),
        crafting_32_element(),
        crafting_33_element(),
        crafting_34_element(),
        crafting_35_element(),
        crafting_36_element(),
        crafting_37_element(),
        crafting_38_element(),
        crafting_39_element(),
        crafting_40_element(),
        crafting_41_element(),
        crafting_42_element(),
        crafting_43_element(),
        crafting_44_element(),
        crafting_45_element(),
        crafting_46_element(),
        crafting_47_element(),
        crafting_48_element(),
        crafting_49_element(),
        crafting_50_element(),
        crafting_51_element(),
        crafting_52_element(),
        crafting_53_element(),
        crafting_54_element(),
        crafting_55_element(),
        crafting_56_element(),
        crafting_57_element(),
        crafting_58_element(),
        crafting_59_element(),
        crafting_60_element(),
        crafting_61_element(),
        crafting_62_element(),
        crafting_63_element(),
        crafting_64_element(),
        crafting_65_element(),
        crafting_66_element(),
        crafting_67_element(),
        crafting_68_element(),
        crafting_69_element(),
        crafting_70_element(),
        crafting_71_element(),
        crafting_72_element(),
        crafting_73_element(),
        crafting_74_element(),
        crafting_75_element(),
        crafting_76_element(),
        crafting_77_element(),
        crafting_78_element(),
        crafting_79_element(),
        crafting_80_element(),
        crafting_81_element(),
        crafting_82_element(),
        crafting_83_element(),
        crafting_84_element(),
        crafting_85_element(),
        crafting_86_element(),
        crafting_87_element(),
        crafting_88_element(),
        crafting_89_element(),
        crafting_90_element(),
        crafting_91_element(),
        crafting_92_element(),
        crafting_93_element(),
        crafting_94_element(),
        crafting_95_element(),
        crafting_96_element(),
        crafting_97_element(),
        crafting_98_element(),
        crafting_99_element(),
        crafting_100_element(),
        crafting_101_element(),
        crafting_102_element(),
        crafting_103_element(),
        crafting_104_element(),
        crafting_105_element(),
        crafting_106_element(),
        crafting_107_element(),
        crafting_108_element(),
        crafting_109_element(),
        crafting_110_element(),
        crafting_111_element(),
        crafting_112_element(),
        crafting_113_element(),
        crafting_114_element(),
        crafting_115_element(),
        crafting_116_element(),
        crafting_117_element(),
        crafting_118_element(),
        crafting_119_element(),
        crafting_120_element(),
        crafting_121_element(),
        crafting_122_element(),
        crafting_123_element(),
        crafting_124_element(),
        crafting_125_element(),
        crafting_126_element(),
        crafting_127_element(),
        crafting_128_element(),
        crafting_129_element(),
        crafting_130_element(),
        crafting_131_element(),
        crafting_132_element(),
        crafting_133_element(),
        crafting_134_element(),
        crafting_135_element(),
        crafting_136_element(),
        crafting_137_element(),
        crafting_138_element(),
        crafting_139_element(),
        crafting_140_element(),
        crafting_141_element(),
        crafting_142_element(),
        crafting_143_element(),
        crafting_144_element(),
        crafting_145_element(),
        crafting_146_element(),
        crafting_147_element(),
        crafting_148_element(),
        crafting_149_element(),
        crafting_150_element(),
        crafting_151_element(),
        crafting_152_element(),
        crafting_153_element(),
        crafting_154_element(),
        crafting_155_element(),
        crafting_156_element(),
        crafting_157_element(),
        crafting_158_element(),
        crafting_159_element(),
        crafting_160_element(),
        crafting_161_element(),
        crafting_162_element(),
        crafting_163_element(),
        crafting_164_element(),
        crafting_165_element(),
        crafting_166_element(),
        crafting_167_element(),
        crafting_168_element(),
        crafting_169_element(),
        crafting_170_element(),
        crafting_171_element(),
        crafting_172_element(),
        crafting_173_element(),
        crafting_174_element(),
        crafting_175_element(),
        crafting_176_element(),
        crafting_177_element(),
        crafting_178_element(),
        crafting_179_element(),
        crafting_180_element(),
        crafting_181_element(),
        crafting_182_element(),
        crafting_183_element(),
        crafting_184_element(),
        crafting_185_element(),
        crafting_186_element(),
        crafting_187_element(),
        crafting_188_element(),
        crafting_189_element(),
        crafting_190_element(),
        crafting_191_element(),
        crafting_192_element(),
        crafting_193_element(),
        crafting_194_element(),
        crafting_195_element(),
        crafting_196_element(),
        crafting_197_element(),
        crafting_198_element(),
        crafting_199_element(),
        crafting_200_element(),
        crafting_201_element(),
        crafting_202_element(),
        crafting_203_element(),
        crafting_204_element(),
        crafting_205_element(),
        crafting_206_element(),
        crafting_207_element(),
        crafting_208_element(),
        crafting_209_element(),
        crafting_210_element(),
        crafting_211_element(),
        crafting_212_element(),
        crafting_213_element(),
        crafting_214_element(),
        crafting_215_element(),
        crafting_216_element(),
        crafting_217_element(),
        crafting_218_element(),
        crafting_219_element(),
        crafting_220_element(),
        crafting_221_element(),
        crafting_222_element(),
        crafting_223_element(),
        crafting_224_element(),
        crafting_225_element(),
        crafting_226_element(),
        crafting_227_element(),
        crafting_228_element(),
        crafting_229_element(),
        crafting_230_element(),
        crafting_231_element(),
        crafting_232_element(),
        crafting_233_element(),
        crafting_234_element(),
        crafting_235_element(),
        crafting_236_element(),
        crafting_237_element(),
        crafting_238_element(),
        crafting_239_element(),
        crafting_240_element(),
        crafting_241_element(),
        crafting_242_element(),
        crafting_243_element(),
        crafting_244_element(),
        crafting_245_element(),
        crafting_246_element(),
        crafting_247_element(),
        crafting_248_element(),
        crafting_249_element(),
        crafting_250_element(),
        crafting_251_element(),
        crafting_252_element(),
        crafting_253_element(),
        crafting_254_element(),
        crafting_255_element(),
        crafting_256_element(),
        crafting_257_element(),
        crafting_258_element(),
        crafting_259_element(),
        crafting_260_element(),
        crafting_261_element(),
        crafting_262_element(),
        crafting_263_element(),
        crafting_264_element(),
        crafting_265_element(),
        crafting_266_element(),
        crafting_267_element(),
        crafting_268_element(),
        crafting_269_element(),
        crafting_270_element(),
        crafting_271_element(), // rubber
        crafting_272_element(), // circuit
        crafting_273_element(), // advanced_circuit
        crafting_274_element(), // alloy
        crafting_275_element(), // iridium
        crafting_276_element(), // electric_motor
        crafting_277_element(), // heat_conductor
        crafting_278_element(), // small_power_unit
        crafting_279_element(), // power_unit
        crafting_280_element(), // carbon_fibre
        crafting_281_element(), // carbon_mesh
        crafting_282_element(), // carbon_plate
        crafting_283_element(), // coal_ball
        crafting_284_element(), // copperboiler
        crafting_285_element(), // iridiumPlate
        crafting_286_element(), // coal_chunk
        crafting_287_element(), // scrap
        crafting_288_element(), // scrap_box
        crafting_289_element(), // cf_powder
        crafting_290_element(), // latex
        crafting_291_element(), // iridium shard

        crafting_292_element(), // rawcrystalmemory

        crafting_293_element(),  // biochaff

        crafting_294_element(),  // coil

        crafting_295_element(),  // plantBall

        crafting_296_element(),  // tinCan
        crafting_297_element(),   // compressedCoalBall
        crafting_298_element(), // heat_storage
        crafting_299_element(), // tri_heat_storage
        crafting_300_element(), // six_heat_storage
        crafting_301_element(),
        crafting_302_element(),
        crafting_303_element(),
        crafting_304_element(),
        crafting_305_element(),
        crafting_306_element(),
        crafting_307_element(),
        crafting_308_element(),
        crafting_309_element(),
        crafting_310_element(),
        crafting_311_element(),
        crafting_312_element(),
        crafting_313_element(),
        crafting_314_element(),
        crafting_315_element(),
        crafting_316_element(),
        crafting_317_element(),
        crafting_318_element(),
        crafting_319_element(),
        crafting_320_element(),
        crafting_321_element(),
        crafting_322_element(),
        crafting_323_element(),
        crafting_324_element(),
        crafting_325_element(),
        crafting_326_element(),
        crafting_327_element(),
        crafting_328_element(),
        crafting_329_element(),
        crafting_330_element(),
        crafting_331_element(),
        crafting_332_element(),
        crafting_333_element(),
        crafting_334_element(),
        crafting_335_element(),
        crafting_336_element(),
        crafting_337_element(),
        crafting_338_element(),
        crafting_339_element(),
        crafting_340_element(),
        crafting_341_element(),
        crafting_342_element(),
        crafting_343_element(),
        crafting_344_element(),
        crafting_345_element(),
        crafting_346_element(),
        crafting_347_element(),
        crafting_348_element(),
        crafting_349_element(),
        crafting_350_element(),
        crafting_351_element(),
        crafting_352_element(),
        crafting_353_element(),
        crafting_354_element(),
        crafting_355_element(),
        crafting_356_element(),
        crafting_357_element(),
        crafting_358_element(),
        crafting_359_element(),
        crafting_360_element(),
        crafting_361_element(),
        crafting_362_element(),
        crafting_363_element(),
        crafting_364_element(),
        crafting_365_element(),
        crafting_366_element(),
        crafting_367_element(),
        crafting_368_element(),
        crafting_369_element(),
        crafting_370_element(),
        crafting_371_element(),
        crafting_372_element(),
        crafting_373_element(),
        crafting_374_element(),
        crafting_375_element(),
        crafting_376_element(),
        crafting_377_element(),
        crafting_378_element(),
        crafting_379_element(),
        crafting_380_element(),
        crafting_381_element(),
        crafting_382_element(),
        crafting_383_element(),
        crafting_384_element(),
        crafting_385_element(),
        crafting_386_element(),
        crafting_387_element(),
        crafting_388_element(),
        crafting_389_element(),
        crafting_390_element(),
        crafting_391_element(),
        crafting_392_element(),
        crafting_393_element(),
        crafting_394_element(),
        crafting_395_element(),
        crafting_396_element(),
        crafting_397_element(),
        crafting_398_element(),
        crafting_399_element(),
        crafting_400_element(),
        crafting_401_element(),
        crafting_402_element(),
        crafting_403_element(),
        crafting_404_element(),
        crafting_405_element(),
        crafting_406_element(),
        crafting_407_element(),
        crafting_408_element(),
        crafting_409_element(),
        crafting_410_element(),
        crafting_411_element(),
        crafting_412_element(),
        crafting_413_element(),
        crafting_414_element(),
        crafting_415_element(),
        crafting_416_element(),
        crafting_417_element(),
        crafting_418_element(),
        crafting_419_element(),
        crafting_420_element(),
        crafting_421_element(),
        crafting_422_element(),
        crafting_423_element(),
        crafting_424_element(),
        crafting_425_element(),
        crafting_426_element(),
        crafting_427_element(),
        crafting_428_element(),
        crafting_429_element(),
        crafting_430_element(),
        crafting_431_element(),
        crafting_432_element(),
        crafting_433_element(),
        crafting_434_element(),
        crafting_435_element(),
        crafting_436_element(),
        crafting_437_element(),
        crafting_438_element(),
        crafting_439_element(),
        crafting_440_element(),
        crafting_441_element(),
        crafting_442_element(),
        crafting_443_element(),
        crafting_444_element(),
        crafting_445_element(),
        crafting_446_element(),
        crafting_447_element(),
        crafting_448_element(),
        crafting_449_element(),

        crafting_450_element(),
        crafting_451_element(),
        crafting_452_element(),
        crafting_453_element(),
        crafting_454_element(),
        crafting_455_element(),
        crafting_456_element(),
        crafting_457_element(),
        crafting_458_element(),
        crafting_459_element(),
        crafting_460_element(),
        crafting_461_element(),
        crafting_462_element(),
        crafting_463_element(),
        crafting_464_element(),
        crafting_465_element(),
        crafting_466_element(),
        crafting_467_element(),
        crafting_468_element(),
        crafting_469_element(),
        crafting_470_element(),
        crafting_471_element(),
        crafting_472_element(),
        crafting_473_element(),
        crafting_474_element(),
        crafting_475_element(),

        crafting_476_element(),
        crafting_477_element(),
        crafting_478_element(),
        crafting_479_element(),
        crafting_480_element(),
        crafting_481_element(),
        crafting_482_element(),
        crafting_483_element(),
        crafting_484_element(),
        crafting_485_element(),
        crafting_486_element(),
        crafting_487_element(),
        crafting_488_element(),
        crafting_489_element(),
        crafting_490_element(),
        crafting_491_element(),
        crafting_492_element(),
        crafting_493_element(),
        crafting_494_element(),
        crafting_495_element(),
        crafting_496_element(),
        crafting_497_element(),

        crafting_498_element(),
        crafting_499_element(),
        crafting_500_element(),
        crafting_501_element(),
        crafting_502_element(),
        crafting_503_element(),
        crafting_504_element(),
        crafting_505_element(),
        crafting_506_element(),
        crafting_507_element(),
        crafting_508_element(),
        crafting_509_element(),
        crafting_510_element(),
        crafting_511_element(),
        crafting_512_element(),
        crafting_513_element(),
        crafting_514_element(),
        crafting_515_element(),
        crafting_516_element(),
        crafting_517_element(),
        crafting_518_element(),
        crafting_519_element(),
        crafting_520_element(),
        crafting_521_element(),
        crafting_522_element(),
        crafting_523_element(),
        crafting_524_element(),
        crafting_525_element(),
        crafting_526_element(),
        crafting_527_element(),
        crafting_528_element(),
        crafting_529_element(),
        crafting_530_element(),
        crafting_531_element(),
        crafting_532_element(),
        crafting_533_element(),
        crafting_534_element(),
        crafting_535_element(),
        crafting_536_element(),
        crafting_537_element(),
        crafting_538_element(),
        crafting_539_element(),
        crafting_540_element(),
        crafting_541_element(),
        crafting_542_element(),
        crafting_543_element(),
        crafting_544_element(),
        crafting_545_element(),
        crafting_546_element(),
        crafting_547_element(),
        crafting_548_element(),
        crafting_549_element(),
        crafting_550_element(),
        crafting_551_element(),
        crafting_552_element(),
        crafting_553_element(),
        crafting_554_element(),
        crafting_555_element(),
        crafting_556_element(),
        crafting_557_element(),
        crafting_558_element(),
        crafting_559_element(),
        crafting_560_element(),
        crafting_561_element(),
        crafting_562_element(),
        crafting_563_element(),
        crafting_564_element(),
        crafting_565_element(),
        crafting_566_element(),
        crafting_567_element(),
        crafting_568_element(),
        crafting_569_element(),
        crafting_570_element(),
        crafting_571_element(),
        crafting_572_element(),
        crafting_573_element(),
        crafting_574_element(),
        crafting_575_element(),
        crafting_576_element(),
        crafting_577_element(),
        crafting_578_element(),
        crafting_579_element(),
        crafting_580_element(),
        crafting_581_element(),
        crafting_582_element(),
        crafting_583_element(),
        crafting_584_element(),
        crafting_585_element(),
        crafting_586_element(),
        crafting_587_element(),
        crafting_588_element(),
        crafting_589_element(),
        crafting_590_element(),
        crafting_591_element(),
        crafting_592_element(),
        crafting_593_element(),
        crafting_594_element(),
        crafting_595_element(),
        crafting_596_element(),
        crafting_597_element(),
        crafting_598_element(),
        crafting_599_element(),
        crafting_600_element(),
        crafting_601_element(),
        crafting_602_element(),
        crafting_603_element(),
        crafting_604_element(),
        crafting_605_element(),
        crafting_606_element(),
        crafting_607_element(),
        crafting_608_element(),
        crafting_609_element(),
        crafting_610_element(),
        crafting_611_element(),
        crafting_612_element(),
        crafting_613_element(),
        crafting_614_element(),
        crafting_615_element(),
        crafting_616_element(),
        crafting_617_element(),
        crafting_618_element(),
        crafting_619_element(),
        crafting_620_element(),
        crafting_621_element(),
        crafting_622_element(),
        crafting_623_element(),
        crafting_624_element(),
        crafting_625_element(),
        crafting_626_element(),
        crafting_627_element(),
        crafting_628_element(),
        crafting_629_element(),
        crafting_630_element(),
        crafting_631_element(),
        crafting_632_element(),
        crafting_633_element(),
        crafting_634_element(),
        crafting_635_element(),

        crafting_636_element(),
        crafting_637_element(),
        crafting_638_element(),
        crafting_639_element(),
        crafting_640_element(),
        crafting_641_element(),
        crafting_642_element(),
        crafting_643_element(),
        crafting_644_element(),
        crafting_645_element(),
        crafting_646_element(),
        crafting_647_element(),
        crafting_648_element(),
        crafting_649_element(),

        crafting_650_element(),
        crafting_651_element(),
        crafting_652_element(),
        crafting_653_element(),
        crafting_654_element(),
        crafting_655_element(),
        crafting_656_element(),
        crafting_657_element(),
        crafting_658_element(),
        crafting_659_element(),
        crafting_660_element(),
        crafting_661_element(),
        crafting_662_element(),
        crafting_663_element(),
        crafting_664_element(),
        crafting_665_element(),
        crafting_666_element(),
        crafting_667_element(),
        crafting_668_element(),
        crafting_669_element(),
        crafting_670_element(),
        crafting_671_element(),
        crafting_672_element(),
        crafting_673_element(),
        crafting_674_element(),
        crafting_675_element(),
        crafting_676_element(),
        crafting_677_element(),
        crafting_678_element(),
        crafting_679_element(),
        crafting_680_element(),
        crafting_681_element(),
        crafting_682_element(),
        crafting_683_element(),
        crafting_684_element(),
        crafting_685_element(),
        crafting_686_element(),
        crafting_687_element(),
        crafting_688_element(),
        crafting_689_element(),
        crafting_690_element(),
        crafting_691_element(),
        crafting_692_element(),
        crafting_693_element(),
        crafting_694_element(),
        crafting_695_element(),
        crafting_696_element(),
        crafting_697_element(),
        crafting_698_element(),
        crafting_699_element(),
        crafting_700_element(),
        crafting_701_element(),
        crafting_702_element(),
        crafting_703_element(),
        crafting_704_element(),
        crafting_705_element(),
        crafting_706_element(),
        crafting_707_element(),
        crafting_708_element(),
        crafting_709_element(),
        crafting_710_element(),
        crafting_711_element(),
        crafting_712_element(),
        crafting_713_element(),
        crafting_714_element(),
        crafting_715_element(),
        crafting_716_element(),
        crafting_717_element(),
        crafting_718_element(),
        crafting_719_element(),
        crafting_720_element(),
        crafting_721_element(),
        crafting_722_element(),
        crafting_723_element(),
        crafting_724_element(),
        crafting_725_element(),
        crafting_726_element(),
        crafting_727_element(),
        crafting_728_element(),
        crafting_729_element(),
        crafting_730_element(),
        crafting_731_element(),
        crafting_732_element(),
        crafting_733_element(),
        crafting_734_element(),
        crafting_735_element(),
        crafting_736_element(),
        crafting_737_element(),
        crafting_738_element(),
        crafting_739_element(),
        crafting_740_element(),
        crafting_741_element(),
        crafting_742_element(),
        crafting_743_element(),
        crafting_744_element(),
        crafting_745_element(),
        crafting_746_element(),
        crafting_747_element(),
        crafting_748_element(),
        crafting_749_element(),
        crafting_750_element(),
        crafting_751_element(),
        crafting_752_element(),
        crafting_753_element(),
        crafting_754_element(),
        crafting_755_element(),
        crafting_756_element(),
        crafting_757_element(),
        crafting_758_element(),
        crafting_759_element(),
        crafting_760_element(),
        crafting_761_element(),
        crafting_762_element(),
        crafting_763_element(),
        crafting_764_element(),
        crafting_765_element(),
        crafting_766_element(),
        crafting_767_element(),
        crafting_768_element(),

        ;

        private final String name;
        private final int ID;

        Types() {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = this.ordinal();
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.ID;
        }
    }

}
