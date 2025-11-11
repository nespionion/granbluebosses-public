package granbluebosses.powers.grandorder;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import granbluebosses.powers.BasePower;

import java.util.HashSet;

import static granbluebosses.GranblueBosses.makeID;

public class FieldOfMediation extends BasePower {

    public static final String POWER_ID = makeID("FieldOfMediation");
    private static final AbstractPower.PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private static PowerStrings getPowerStrings(String ID) {return CardCrawlGame.languagePack.getPowerStrings(ID);}
    private static PowerStrings powerStrings = getPowerStrings(POWER_ID);
    private boolean amountUpdated;


    public FieldOfMediation(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, null, amount);
        this.amountUpdated = false;
    }

    public FieldOfMediation(AbstractCreature owner) {
        this(owner, 1);
        this.amountUpdated = false;
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        if (!amountUpdated){
            this.updateAmount();
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount)));
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, this.amount)));
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new FocusPower(this.owner, this.amount)));
        }
    }

    private void updateAmount(){
        HashSet<AbstractCard.CardColor> colors = new HashSet<AbstractCard.CardColor>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group){
            colors.add(c.color);
        }
        int numOfColors = colors.size();
        this.amount = numOfColors - 1;
        this.amountUpdated = true;
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        this.name = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
        this.description = DESCRIPTIONS[0];
    }
}
