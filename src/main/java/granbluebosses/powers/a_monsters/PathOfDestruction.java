package granbluebosses.powers.a_monsters;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import granbluebosses.monsters.act1.normal.Leviathan2;
import granbluebosses.powers.BasePower;

import static granbluebosses.GranblueBosses.makeID;

public class PathOfDestruction extends BasePower {

    public static final String POWER_ID = makeID("PathOfDestruction");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private static PowerStrings getPowerStrings(String ID) {return CardCrawlGame.languagePack.getPowerStrings(ID);}
    private static PowerStrings powerStrings = getPowerStrings(POWER_ID);

    public PathOfDestruction(AbstractCreature owner) {
        super(POWER_ID, TYPE, TURN_BASED, owner, null, -1);
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        this.name = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
        this.description = DESCRIPTIONS[0];
    }
}
