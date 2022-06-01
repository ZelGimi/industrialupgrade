package com.denfop.utils;

import ic2.core.init.Localization;

import java.util.ArrayList;
import java.util.List;

public class ListInformationUtils {

    public static final List<String> panelinform = new ArrayList<>();
    public static final List<String> storageinform = new ArrayList<>();
    public static final List<String> fisherinform = new ArrayList<>();
    public static final List<String> analyzeinform = new ArrayList<>();
    public static final List<String> quarryinform = new ArrayList<>();

    public static void init() {
        quarryinform.add(Localization.translate("iu.quarryinformation1"));
        quarryinform.add(Localization.translate("iu.quarryinformation2"));
        quarryinform.add(Localization.translate("iu.quarryinformation3"));
        quarryinform.add(Localization.translate("iu.quarryinformation4"));
        quarryinform.add(Localization.translate("iu.quarryinformation5"));
        quarryinform.add(Localization.translate("iu.quarryinformation6"));
        quarryinform.add(Localization.translate("iu.quarryinformation7"));
        quarryinform.add(Localization.translate("iu.quarryinformation8"));
        quarryinform.add(Localization.translate("iu.quarryinformation9"));
        fisherinform.add(Localization.translate("iu.fisherinformation1"));
        fisherinform.add(Localization.translate("iu.fisherinformation2"));
        fisherinform.add(Localization.translate("iu.fisherinformation3"));

        panelinform.add(Localization.translate("iu.panelinformation1"));
        panelinform.add(Localization.translate("iu.panelinformation2"));
        panelinform.add(Localization.translate("iu.panelinformation3"));
        panelinform.add(Localization.translate("iu.panelinformation4"));
        panelinform.add(Localization.translate("iu.panelinformation5"));
        panelinform.add(Localization.translate("iu.panelinformation6"));
        panelinform.add(Localization.translate("iu.panelinformation7"));
        panelinform.add(Localization.translate("iu.panelinformation8"));
        panelinform.add(Localization.translate("iu.panelinformation9"));
        storageinform.add(Localization.translate("iu.electricstorageinformation1"));
        storageinform.add(Localization.translate("iu.electricstorageinformation2"));
        storageinform.add(Localization.translate("iu.electricstorageinformation3"));
        storageinform.add(Localization.translate("iu.electricstorageinformation4"));
        storageinform.add(Localization.translate("iu.electricstorageinformation5"));
        storageinform.add(Localization.translate("iu.electricstorageinformation6"));
        storageinform.add(Localization.translate("iu.electricstorageinformation7"));
        analyzeinform.add(Localization.translate("iu.analyzerinformation1"));
        analyzeinform.add(Localization.translate("iu.analyzerinformation2"));
        analyzeinform.add(Localization.translate("iu.analyzerinformation3"));
        analyzeinform.add(Localization.translate("iu.analyzerinformation4"));
        analyzeinform.add(Localization.translate("iu.analyzerinformation5"));
        analyzeinform.add(Localization.translate("iu.analyzerinformation6"));
        analyzeinform.add(Localization.translate("iu.analyzerinformation7"));
    }

}
