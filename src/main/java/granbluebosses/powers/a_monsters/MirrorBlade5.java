package granbluebosses.powers.a_monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import granbluebosses.powers.BasePower;

import static granbluebosses.GranblueBosses.makeID;

public class MirrorBlade5 extends BasePower {

    public static final String POWER_ID = makeID("MirrorBlade5");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private static PowerStrings getPowerStrings(String ID) {return CardCrawlGame.languagePack.getPowerStrings(ID);}
    private static PowerStrings powerStrings = getPowerStrings(POWER_ID);

    public MirrorBlade5(AbstractCreature owner) {
        super(POWER_ID, TYPE, TURN_BASED, owner, null, -1);
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(this.owner, 5, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        this.name = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
        this.description = DESCRIPTIONS[0];
    }
}
