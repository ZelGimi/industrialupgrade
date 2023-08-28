package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class ItemBlueprint extends ItemSubTypes<ItemBlueprint.Types> implements IModelRegister {

    protected static final String NAME = "blueprint";

    public ItemBlueprint() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.BlueprintTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    public enum Types implements ISubEnum {
        blueprint_0(0),
        blueprint_1(1),
        blueprint_2(2),
        blueprint_3(3),
        blueprint_4(4),
        blueprint_5(5),
        blueprint_6(6),
        blueprint_7(7),
        blueprint_8(8),
        blueprint_9(9),
        blueprint_10(10),
        blueprint_11(11),
        blueprint_12(12),
        blueprint_13(13),
        blueprint_14(14),
        blueprint_15(15),
        blueprint_16(16),
        blueprint_17(17),
        blueprint_18(18),
        blueprint_19(19),
        blueprint_20(20),
        blueprint_21(21),
        blueprint_22(22),
        blueprint_23(23),
        blueprint_24(24),
        blueprint_25(25),
        blueprint_26(26),
        blueprint_27(27),
        blueprint_28(28),
        blueprint_29(29),
        blueprint_30(30),
        blueprint_31(31),
        blueprint_32(32),
        blueprint_33(33),
        blueprint_34(34),
        blueprint_35(35),
        blueprint_36(36),
        blueprint_37(37),
        blueprint_38(38),
        blueprint_39(39),
        blueprint_40(40),
        blueprint_41(41),
        blueprint_42(42),
        blueprint_43(43),
        blueprint_44(44),
        blueprint_45(45),
        blueprint_46(46),
        blueprint_47(47),
        blueprint_48(48),
        blueprint_49(49),
        blueprint_50(50),
        blueprint_51(51),
        blueprint_52(52),
        blueprint_53(53),
        blueprint_54(54),
        blueprint_55(55),
        blueprint_56(56),
        blueprint_57(57),
        blueprint_58(58),
        blueprint_59(59),
        blueprint_60(60),
        blueprint_61(61),
        blueprint_62(62),
        blueprint_63(63),
        blueprint_64(64),
        blueprint_65(65),
        blueprint_66(66),
        blueprint_67(67),
        blueprint_68(68),
        blueprint_69(69),
        blueprint_70(70),
        blueprint_71(71),
        blueprint_72(72),
        blueprint_73(73),
        blueprint_74(74),
        blueprint_75(75),
        blueprint_76(76),
        blueprint_77(77),
        blueprint_78(78),
        blueprint_79(79),
        blueprint_80(80),
        blueprint_81(81),
        blueprint_82(82),
        blueprint_83(83),
        blueprint_84(84),
        blueprint_85(85),
        blueprint_86(86),
        blueprint_87(87),
        blueprint_88(88),
        blueprint_89(89),
        blueprint_90(90),
        blueprint_91(91),
        blueprint_92(92),
        blueprint_93(93),
        blueprint_94(94),
        blueprint_95(95),
        blueprint_96(96),
        blueprint_97(97),
        blueprint_98(98),
        blueprint_99(99),
        blueprint_100(100),
        blueprint_101(101),
        blueprint_102(102),
        blueprint_103(103),
        blueprint_104(104),
        blueprint_105(105),
        blueprint_106(106),
        blueprint_107(107),
        blueprint_108(108),
        blueprint_109(109),
        blueprint_110(110),
        blueprint_111(111),
        blueprint_112(112),
        blueprint_113(113),
        blueprint_114(114),
        blueprint_115(115),
        blueprint_116(116),
        blueprint_117(117),
        blueprint_118(118),
        blueprint_119(119),
        blueprint_120(120),
        blueprint_121(121),
        blueprint_122(122),
        blueprint_123(123),
        blueprint_124(124),
        blueprint_125(125),
        blueprint_126(126),
        blueprint_127(127),
        blueprint_128(128),
        blueprint_129(129),
        blueprint_130(130),
        blueprint_131(131),
        blueprint_132(132),
        blueprint_133(133),
        blueprint_134(134),
        blueprint_135(135),
        blueprint_136(136),
        blueprint_137(137),
        blueprint_138(138),
        blueprint_139(139),
        blueprint_140(140),
        blueprint_141(141),
        blueprint_142(142),
        blueprint_143(143),
        blueprint_144(144),
        blueprint_145(145),
        blueprint_146(146),
        blueprint_147(147),
        blueprint_148(148),
        blueprint_149(149),
        blueprint_150(150),
        blueprint_151(151),
        blueprint_152(152),
        blueprint_153(153),
        blueprint_154(154),
        blueprint_155(155),
        blueprint_156(156),
        blueprint_157(157),
        blueprint_158(158),
        blueprint_159(159),
        blueprint_160(160),
        blueprint_161(161),
        blueprint_162(162),
        blueprint_163(163),
        blueprint_164(164),
        blueprint_165(165),
        blueprint_166(166),
        blueprint_167(167),
        blueprint_168(168),
        blueprint_169(169),
        blueprint_170(170),
        blueprint_171(171),
        blueprint_172(172),
        blueprint_173(173),
        blueprint_174(174),
        blueprint_175(175),
        blueprint_176(176),
        blueprint_177(177),
        blueprint_178(178),
        blueprint_179(179),
        blueprint_180(180),
        blueprint_181(181),
        blueprint_182(182),
        blueprint_183(183),
        blueprint_184(184),
        blueprint_185(185),
        blueprint_186(186),
        blueprint_187(187),
        blueprint_188(188),
        blueprint_189(189),
        blueprint_190(190),
        blueprint_191(191),
        blueprint_192(192),
        blueprint_193(193),
        blueprint_194(194),
        blueprint_195(195),
        blueprint_196(196),
        blueprint_197(197),
        blueprint_198(198),
        blueprint_199(199),
        blueprint_200(200),
        blueprint_201(201),
        blueprint_202(202),
        blueprint_203(203),
        blueprint_204(204),
        blueprint_205(205),
        blueprint_206(206),
        blueprint_207(207),
        blueprint_208(208),
        blueprint_209(209),
        blueprint_210(210),
        blueprint_211(211),
        blueprint_212(212),
        blueprint_213(213),
        blueprint_214(214),
        blueprint_215(215),
        blueprint_216(216),
        blueprint_217(217),
        blueprint_218(218),
        blueprint_219(219),
        blueprint_220(220),
        blueprint_221(221),
        blueprint_222(222),
        blueprint_223(223),
        blueprint_224(224),
        blueprint_225(225),
        blueprint_226(226),
        blueprint_227(227),
        blueprint_228(228),
        blueprint_229(229),
        blueprint_230(230),
        blueprint_231(231),
        blueprint_232(232),
        blueprint_233(233),
        blueprint_234(234),
        blueprint_235(235),
        blueprint_236(236),
        blueprint_237(237),
        blueprint_238(238),
        blueprint_239(239),
        blueprint_240(240),
        blueprint_241(241),
        blueprint_242(242),
        blueprint_243(243),
        blueprint_244(244),
        blueprint_245(245),
        blueprint_246(246),
        blueprint_247(247),
        blueprint_248(248),
        blueprint_249(249),
        blueprint_250(250),
        blueprint_251(251),
        blueprint_252(252),
        blueprint_253(253),
        blueprint_254(254),
        blueprint_255(255),
        blueprint_256(256),
        blueprint_257(257),
        blueprint_258(258),
        blueprint_259(259),
        blueprint_260(260),
        blueprint_261(261),
        blueprint_262(262),
        blueprint_263(263),
        blueprint_264(264),
        blueprint_265(265),
        blueprint_266(266),
        blueprint_267(267),
        blueprint_268(268),
        blueprint_269(269),
        blueprint_270(270),
        blueprint_271(271),
        blueprint_272(272),
        blueprint_273(273),
        blueprint_274(274),
        blueprint_275(275),
        blueprint_276(276),
        blueprint_277(277),
        blueprint_278(278),
        blueprint_279(279),
        blueprint_280(280),
        blueprint_281(281),
        blueprint_282(282),
        blueprint_283(283),
        blueprint_284(284),
        blueprint_285(285),
        blueprint_286(286),
        blueprint_287(287),
        blueprint_288(288),
        blueprint_289(289),
        blueprint_290(290),
        blueprint_291(291),
        blueprint_292(292),
        blueprint_293(293),
        blueprint_294(294),
        blueprint_295(295),
        blueprint_296(296),
        blueprint_297(297),
        blueprint_298(298),
        blueprint_299(299),
        blueprint_300(300),
        blueprint_301(301),
        blueprint_302(302),
        blueprint_303(303),
        blueprint_304(304),
        blueprint_305(305),
        blueprint_306(306),
        blueprint_307(307),
        blueprint_308(308),
        blueprint_309(309),
        blueprint_310(310),
        blueprint_311(311),
        blueprint_312(312),
        blueprint_313(313),
        blueprint_314(314),
        blueprint_315(315),
        blueprint_316(316),
        blueprint_317(317),
        blueprint_318(318),
        blueprint_319(319),
        blueprint_320(320),
        blueprint_321(321),
        blueprint_322(322),
        blueprint_323(323),
        blueprint_324(324),
        blueprint_325(325),
        blueprint_326(326),
        blueprint_327(327),
        blueprint_328(328),
        blueprint_329(329),
        blueprint_330(330),
        blueprint_331(331),
        blueprint_332(332),
        blueprint_333(333),
        blueprint_334(334),
        blueprint_335(335),
        blueprint_336(336),
        blueprint_337(337),
        blueprint_338(338),
        blueprint_339(339),
        blueprint_340(340),
        blueprint_341(341),
        blueprint_342(342),
        blueprint_343(343),
        blueprint_344(344),
        blueprint_345(345),
        blueprint_346(346),
        blueprint_347(347),
        blueprint_348(348),
        blueprint_349(349),
        blueprint_350(350),
        blueprint_351(351),
        blueprint_352(352),
        blueprint_353(353),
        blueprint_354(354),
        blueprint_355(355),
        blueprint_356(356),
        blueprint_357(357),
        blueprint_358(358),
        blueprint_359(359),
        blueprint_360(360),
        blueprint_361(361),
        blueprint_362(362),
        blueprint_363(363),
        blueprint_364(364),
        blueprint_365(365),
        blueprint_366(366),
        blueprint_367(367),
        blueprint_368(368),
        blueprint_369(369),
        blueprint_370(370),
        blueprint_371(371),
        blueprint_372(372),
        blueprint_373(373),
        blueprint_374(374),
        blueprint_375(375),
        blueprint_376(376),
        blueprint_377(377),
        blueprint_378(378),
        blueprint_379(379),
        blueprint_380(380),
        blueprint_381(381),
        blueprint_382(382),
        blueprint_383(383),
        blueprint_384(384),
        blueprint_385(385),
        blueprint_386(386),
        blueprint_387(387),
        blueprint_388(388),
        blueprint_389(389),
        blueprint_390(390),
        blueprint_391(391),
        blueprint_392(392),
        blueprint_393(393),
        blueprint_394(394),
        blueprint_395(395),
        blueprint_396(396),
        blueprint_397(397),
        blueprint_398(398),
        blueprint_399(399),
        blueprint_400(400),
        blueprint_401(401),
        blueprint_402(402),
        blueprint_403(403),
        blueprint_404(404),
        blueprint_405(405),
        blueprint_406(406),
        blueprint_407(407),
        blueprint_408(408),
        blueprint_409(409),
        blueprint_410(410),
        blueprint_411(411),
        blueprint_412(412),
        blueprint_413(413),
        blueprint_414(414),
        blueprint_415(415),
        blueprint_416(416),
        blueprint_417(417),
        blueprint_418(418),
        blueprint_419(419),
        blueprint_420(420),
        blueprint_421(421),
        blueprint_422(422),
        blueprint_423(423),
        blueprint_424(424),
        blueprint_425(425),
        blueprint_426(426),
        blueprint_427(427),
        blueprint_428(428),
        blueprint_429(429),
        blueprint_430(430),
        blueprint_431(431),
        blueprint_432(432),
        blueprint_433(433),
        blueprint_434(434),
        blueprint_435(435),
        blueprint_436(436),
        blueprint_437(437),
        blueprint_438(438),
        blueprint_439(439),
        blueprint_440(440),
        blueprint_441(441),
        blueprint_442(442),
        blueprint_443(443),
        blueprint_444(444),
        blueprint_445(445),
        blueprint_446(446),
        blueprint_447(447),
        blueprint_448(448),
        blueprint_449(449),
        blueprint_450(450),
        blueprint_451(451),
        blueprint_452(452),
        blueprint_453(453),
        blueprint_454(454),
        blueprint_455(455),
        blueprint_456(456),
        blueprint_457(457),
        blueprint_458(458),
        blueprint_459(459),
        blueprint_460(460),
        blueprint_461(461),
        blueprint_462(462),
        blueprint_463(463),
        blueprint_464(464),
        blueprint_465(465),
        blueprint_466(466),
        blueprint_467(467),
        blueprint_468(468),
        blueprint_469(469),
        blueprint_470(470),
        blueprint_471(471),
        blueprint_472(472),
        blueprint_473(473),
        blueprint_474(474),
        blueprint_475(475),
        blueprint_476(476),
        blueprint_477(477),
        blueprint_478(478),
        blueprint_479(479),
        blueprint_480(480),
        blueprint_481(481),
        blueprint_482(482),
        blueprint_483(483),
        blueprint_484(484),
        blueprint_485(485),
        blueprint_486(486),
        blueprint_487(487),
        blueprint_488(488),
        blueprint_489(489),
        blueprint_490(490),
        blueprint_491(491),
        blueprint_492(492),
        blueprint_493(493),
        blueprint_494(494),
        blueprint_495(495),
        blueprint_496(496),
        blueprint_497(497),
        blueprint_498(498),
        blueprint_499(499),
        blueprint_500(500),
        blueprint_501(501),
        blueprint_502(502),
        blueprint_503(503),
        blueprint_504(504),
        blueprint_505(505),
        blueprint_506(506),
        blueprint_507(507),
        blueprint_508(508),
        blueprint_509(509),
        blueprint_510(510),
        blueprint_511(511),
        blueprint_512(512),
        blueprint_513(513),
        blueprint_514(514),
        blueprint_515(515),
        blueprint_516(516),
        blueprint_517(517),
        blueprint_518(518),
        blueprint_519(519),
        blueprint_520(520),
        blueprint_521(521),
        blueprint_522(522),
        blueprint_523(523),
        blueprint_524(524),
        blueprint_525(525),
        blueprint_526(526),
        blueprint_527(527),
        blueprint_528(528),
        blueprint_529(529),
        blueprint_530(530),
        blueprint_531(531),
        blueprint_532(532),
        blueprint_533(533),
        blueprint_534(534),
        blueprint_535(535),
        blueprint_536(536),
        blueprint_537(537),
        blueprint_538(538),
        blueprint_539(539),
        blueprint_540(540),
        blueprint_541(541),
        blueprint_542(542),
        blueprint_543(543),
        blueprint_544(544),
        blueprint_545(545),
        blueprint_546(546),
        blueprint_547(547),
        blueprint_548(548),
        blueprint_549(549),
        blueprint_550(550),
        blueprint_551(551),
        blueprint_552(552),
        blueprint_553(553),
        blueprint_554(554),
        blueprint_555(555),
        blueprint_556(556),
        blueprint_557(557),
        blueprint_558(558),
        blueprint_559(559),
        blueprint_560(560),
        blueprint_561(561),
        blueprint_562(562),
        blueprint_563(563),
        blueprint_564(564),
        blueprint_565(565),
        blueprint_566(566),
        blueprint_567(567),
        blueprint_568(568),
        blueprint_569(569),
        blueprint_570(570),
        blueprint_571(571),
        blueprint_572(572),
        blueprint_573(573),
        blueprint_574(574),
        blueprint_575(575),
        blueprint_576(576),
        blueprint_577(577),
        blueprint_578(578),
        blueprint_579(579),
        blueprint_580(580),
        blueprint_581(581),
        blueprint_582(582),
        blueprint_583(583),
        blueprint_584(584),
        blueprint_585(585),
        blueprint_586(586),
        blueprint_587(587),
        blueprint_588(588),
        blueprint_589(589),
        blueprint_590(590),
        blueprint_591(591),
        blueprint_592(592),
        blueprint_593(593),
        blueprint_594(594),
        blueprint_595(595),
        blueprint_596(596),
        blueprint_597(597),
        blueprint_598(598),
        blueprint_599(599),
        blueprint_600(600),
        blueprint_601(601),
        blueprint_602(602),
        blueprint_603(603),
        blueprint_604(604),
        blueprint_605(605),
        blueprint_606(606),
        blueprint_607(607),
        blueprint_608(608),
        blueprint_609(609),
        blueprint_610(610),
        blueprint_611(611),
        blueprint_612(612),
        blueprint_613(613),
        blueprint_614(614),
        blueprint_615(615),
        blueprint_616(616),
        blueprint_617(617),
        blueprint_618(618),
        blueprint_619(619),
        blueprint_620(620),
        blueprint_621(621),
        blueprint_622(622),
        blueprint_623(623),
        blueprint_624(624),
        blueprint_625(625),
        blueprint_626(626),
        blueprint_627(627),
        blueprint_628(628),
        blueprint_629(629),
        blueprint_630(630),
        blueprint_631(631),
        blueprint_632(632),
        blueprint_633(633),
        blueprint_634(634),
        blueprint_635(635),
        blueprint_636(636),
        blueprint_637(637),
        blueprint_638(638),
        blueprint_639(639),
        blueprint_640(640),
        blueprint_641(641),
        blueprint_642(642),
        blueprint_643(643),
        blueprint_644(644),
        blueprint_645(645),
        blueprint_646(646),
        blueprint_647(647),
        blueprint_648(648),
        blueprint_649(649),

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
