package granbluebosses.config;

import basemod.EasyConfigPanel;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import granbluebosses.GranblueBosses;

public class ConfigMenu extends EasyConfigPanel {

    public static boolean enableDMCAMusic = true;
    public static boolean modestyFilter = false;
    public static boolean enableExtraRewards = true;
    public static boolean enableStartOfActEvents = true;

    public ConfigMenu(){
        super(GranblueBosses.modID, GranblueBosses.makeID("ConfigMenu"));


    }


}
