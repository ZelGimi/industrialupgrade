package com.denfop.items.resource;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ItemCraftingElements extends ItemMulti<ItemCraftingElements.Types> implements IModelRegister {

    protected static final String NAME = "crafting_elements";

    public ItemCraftingElements() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.ElementsTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public void registerModels() {
        registerModels(null);
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {

            for (final Types type : this.typeProperty.getShownValues()) {
                subItems.add(this.getItemStackUnchecked(type));

            }

        }
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final ItemName name, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    public enum Types implements IIdProvider {
        crafting_0_element(0),
        crafting_1_element(1),
        crafting_2_element(2),
        crafting_3_element(3),
        crafting_4_element(4),
        crafting_5_element(5),
        crafting_6_element(6),
        crafting_7_element(7),
        crafting_8_element(8),
        crafting_9_element(9),
        crafting_10_element(10),
        crafting_11_element(11),
        crafting_12_element(12),
        crafting_13_element(13),
        crafting_14_element(14),
        crafting_15_element(15),
        crafting_16_element(16),
        crafting_17_element(17),
        crafting_18_element(18),
        crafting_19_element(19),
        crafting_20_element(20),
        crafting_21_element(21),
        crafting_22_element(22),
        crafting_23_element(23),
        crafting_24_element(24),
        crafting_25_element(25),
        crafting_26_element(26),
        crafting_27_element(27),
        crafting_28_element(28),
        crafting_29_element(29),
        crafting_30_element(30),
        crafting_31_element(31),
        crafting_32_element(32),
        crafting_33_element(33),
        crafting_34_element(34),
        crafting_35_element(35),
        crafting_36_element(36),
        crafting_37_element(37),
        crafting_38_element(38),
        crafting_39_element(39),
        crafting_40_element(40),
        crafting_41_element(41),
        crafting_42_element(42),
        crafting_43_element(43),
        crafting_44_element(44),
        crafting_45_element(45),
        crafting_46_element(46),
        crafting_47_element(47),
        crafting_48_element(48),
        crafting_49_element(49),
        crafting_50_element(50),
        crafting_51_element(51),
        crafting_52_element(52),
        crafting_53_element(53),
        crafting_54_element(54),
        crafting_55_element(55),
        crafting_56_element(56),
        crafting_57_element(57),
        crafting_58_element(58),
        crafting_59_element(59),
        crafting_60_element(60),
        crafting_61_element(61),
        crafting_62_element(62),
        crafting_63_element(63),
        crafting_64_element(64),
        crafting_65_element(65),
        crafting_66_element(66),
        crafting_67_element(67),
        crafting_68_element(68),
        crafting_69_element(69),
        crafting_70_element(70),
        crafting_71_element(71),
        crafting_72_element(72),
        crafting_73_element(73),
        crafting_74_element(74),
        crafting_75_element(75),
        crafting_76_element(76),
        crafting_77_element(77),
        crafting_78_element(78),
        crafting_79_element(79),
        crafting_80_element(80),
        crafting_81_element(81),
        crafting_82_element(82),
        crafting_83_element(83),
        crafting_84_element(84),
        crafting_85_element(85),
        crafting_86_element(86),
        crafting_87_element(87),
        crafting_88_element(88),
        crafting_89_element(89),
        crafting_90_element(90),
        crafting_91_element(91),
        crafting_92_element(92),
        crafting_93_element(93),
        crafting_94_element(94),
        crafting_95_element(95),
        crafting_96_element(96),
        crafting_97_element(97),
        crafting_98_element(98),
        crafting_99_element(99),
        crafting_100_element(100),
        crafting_101_element(101),
        crafting_102_element(102),
        crafting_103_element(103),
        crafting_104_element(104),
        crafting_105_element(105),
        crafting_106_element(106),
        crafting_107_element(107),
        crafting_108_element(108),
        crafting_109_element(109),
        crafting_110_element(110),
        crafting_111_element(111),
        crafting_112_element(112),
        crafting_113_element(113),
        crafting_114_element(114),
        crafting_115_element(115),
        crafting_116_element(116),
        crafting_117_element(117),
        crafting_118_element(118),
        crafting_119_element(119),
        crafting_120_element(120),
        crafting_121_element(121),
        crafting_122_element(122),
        crafting_123_element(123),
        crafting_124_element(124),
        crafting_125_element(125),
        crafting_126_element(126),
        crafting_127_element(127),
        crafting_128_element(128),
        crafting_129_element(129),
        crafting_130_element(130),
        crafting_131_element(131),
        crafting_132_element(132),
        crafting_133_element(133),
        crafting_134_element(134),
        crafting_135_element(135),
        crafting_136_element(136),
        crafting_137_element(137),
        crafting_138_element(138),
        crafting_139_element(139),
        crafting_140_element(140),
        crafting_141_element(141),
        crafting_142_element(142),
        crafting_143_element(143),
        crafting_144_element(144),
        crafting_145_element(145),
        crafting_146_element(146),
        crafting_147_element(147),
        crafting_148_element(148),
        crafting_149_element(149),
        crafting_150_element(150),
        crafting_151_element(151),
        crafting_152_element(152),
        crafting_153_element(153),
        crafting_154_element(154),
        crafting_155_element(155),
        crafting_156_element(156),
        crafting_157_element(157),
        crafting_158_element(158),
        crafting_159_element(159),
        crafting_160_element(160),
        crafting_161_element(161),
        crafting_162_element(162),
        crafting_163_element(163),
        crafting_164_element(164),
        crafting_165_element(165),
        crafting_166_element(166),
        crafting_167_element(167),
        crafting_168_element(168),
        crafting_169_element(169),
        crafting_170_element(170),
        crafting_171_element(171),
        crafting_172_element(172),
        crafting_173_element(173),
        crafting_174_element(174),
        crafting_175_element(175),
        crafting_176_element(176),
        crafting_177_element(177),
        crafting_178_element(178),
        crafting_179_element(179),
        crafting_180_element(180),
        crafting_181_element(181),
        crafting_182_element(182),
        crafting_183_element(183),
        crafting_184_element(184),
        crafting_185_element(185),
        crafting_186_element(186),
        crafting_187_element(187),
        crafting_188_element(188),
        crafting_189_element(189),
        crafting_190_element(190),
        crafting_191_element(191),
        crafting_192_element(192),
        crafting_193_element(193),
        crafting_194_element(194),
        crafting_195_element(195),
        crafting_196_element(196),
        crafting_197_element(197),
        crafting_198_element(198),
        crafting_199_element(199),
        crafting_200_element(200),
        crafting_201_element(201),
        crafting_202_element(202),
        crafting_203_element(203),
        crafting_204_element(204),
        crafting_205_element(205),
        crafting_206_element(206),
        crafting_207_element(207),
        crafting_208_element(208),
        crafting_209_element(209),
        crafting_210_element(210),
        crafting_211_element(211),
        crafting_212_element(212),
        crafting_213_element(213),
        crafting_214_element(214),
        crafting_215_element(215),
        crafting_216_element(216),
        crafting_217_element(217),
        crafting_218_element(218),
        crafting_219_element(219),
        crafting_220_element(220),
        crafting_221_element(221),
        crafting_222_element(222),
        crafting_223_element(223),
        crafting_224_element(224),
        crafting_225_element(225),
        crafting_226_element(226),
        crafting_227_element(227),
        crafting_228_element(228),
        crafting_229_element(229),
        crafting_230_element(230),
        crafting_231_element(231),
        crafting_232_element(232),
        crafting_233_element(233),
        crafting_234_element(234),
        crafting_235_element(235),
        crafting_236_element(236),
        crafting_237_element(237),
        crafting_238_element(238),
        crafting_239_element(239),
        crafting_240_element(240),
        crafting_241_element(241),
        crafting_242_element(242),
        crafting_243_element(243),
        crafting_244_element(244),
        crafting_245_element(245),
        crafting_246_element(246),
        crafting_247_element(247),
        crafting_248_element(248),
        crafting_249_element(249),
        crafting_250_element(250),
        crafting_251_element(251),
        crafting_252_element(252),
        crafting_253_element(253),
        crafting_254_element(254),
        crafting_255_element(255),
        crafting_256_element(256),
        crafting_257_element(257),
        crafting_258_element(258),
        crafting_259_element(259),
        crafting_260_element(260),
        crafting_261_element(261),
        ;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
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
