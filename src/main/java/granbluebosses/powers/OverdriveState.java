package granbluebosses.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static granbluebosses.GranblueBosses.makeID;

public class OverdriveState extends BasePower {
    public static final String POWER_ID = makeID("OverdriveState");
    private static final AbstractPower.PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = false;
    private static PowerStrings getPowerStrings(String ID) {return CardCrawlGame.languagePack.getPowerStrings(ID);}
    private static PowerStrings powerStrings = getPowerStrings(POWER_ID);



    public OverdriveState(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);

        if (AbstractDungeon.ascensionLevel > 19){
            this.amount2 = 10;
        } else {
            this.amount2 = 5;
        }
    }

    public void lowerAmount(int damageAmount){
        if (this.amount > damageAmount) {
            this.amount = this.amount - damageAmount;
        } else {
            addToBot(new ApplyPowerAction(this.owner, this.owner, new StandbyState(this.owner, this.amount2)));
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        if (this.amount == 1){
            this.description = this.DESCRIPTIONS[0] + this.amount + this.DESCRIPTIONS[1];
        } else {
            this.description = this.DESCRIPTIONS[0] + this.amount + this.DESCRIPTIONS[2];
        }
    }
}