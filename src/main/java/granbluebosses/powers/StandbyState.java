package granbluebosses.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import granbluebosses.powers.BasePower;
import granbluebosses.powers.protobaha.RagnarokField;

import static granbluebosses.GranblueBosses.makeID;

public class StandbyState extends BasePower{
    public static final String POWER_ID = makeID("StandbyState");
    private static final AbstractPower.PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = false;
    public boolean canLowerAmount = false;
    private static PowerStrings getPowerStrings(String ID) {return CardCrawlGame.languagePack.getPowerStrings(ID);}
    private static PowerStrings powerStrings = getPowerStrings(POWER_ID);


    public StandbyState(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);


        if (AbstractDungeon.ascensionLevel > 19){
            this.amount2 = 40;
        } else {
            this.amount2 = 30;
        }
    }

    public void lowerAmount (int damageAmount){
        if (!canLowerAmount){
            this.updateDescription();
            return;
        }
        if (this.amount > damageAmount && !this.owner.hasPower(StunMonsterPower.POWER_ID)){
            this.amount = this.amount-damageAmount;
        } else if (!this.owner.hasPower(StunMonsterPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(this.owner, this.owner, new OverdriveState(this.owner, this.amount2)));
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
