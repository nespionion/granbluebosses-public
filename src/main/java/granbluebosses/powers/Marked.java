package granbluebosses.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static granbluebosses.GranblueBosses.makeID;

public class Marked extends BasePower{
    public static final String POWER_ID = makeID("Marked");
    private static final AbstractPower.PowerType TYPE = PowerType.DEBUFF;
    private static PowerStrings getPowerStrings(String ID) {return CardCrawlGame.languagePack.getPowerStrings(ID);}
    private static PowerStrings powerStrings = getPowerStrings(POWER_ID);
    private static final boolean TURN_BASED = false;

    public Marked(AbstractCreature owner) {
        super(POWER_ID, TYPE, TURN_BASED, owner, -1);
    }
}
