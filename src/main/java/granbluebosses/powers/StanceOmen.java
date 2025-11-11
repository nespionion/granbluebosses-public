package granbluebosses.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static granbluebosses.GranblueBosses.makeID;

public class StanceOmen extends BasePower{
    public static final String POWER_ID = makeID("StanceOmen");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private static PowerStrings getPowerStrings(String ID) {return CardCrawlGame.languagePack.getPowerStrings(ID);}
    private static PowerStrings powerStrings = getPowerStrings(POWER_ID);

    public StanceOmen(AbstractCreature owner) {
        super(POWER_ID, TYPE, TURN_BASED, owner, -1);
    }

    public void setUpOmen(int num){
        int percentage = (100/num);
        this.description = "This enemy will prepare a powerful attack when under " + percentage + "% health.";
    }

    public void setUpOmen(String newDescription){
        this.description = newDescription;
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        this.name = powerStrings.NAME;
    }
}
