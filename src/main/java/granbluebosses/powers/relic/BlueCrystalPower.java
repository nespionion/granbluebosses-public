package granbluebosses.powers.relic;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import granbluebosses.powers.BasePower;

import static granbluebosses.GranblueBosses.makeID;

public class BlueCrystalPower extends BasePower {
    public static final String POWER_ID = makeID("BlueCrystalPower");
    private static final AbstractPower.PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private static PowerStrings getPowerStrings(String ID) {return CardCrawlGame.languagePack.getPowerStrings(ID);}
    private static PowerStrings powerStrings = getPowerStrings(POWER_ID);
    private boolean enableStrength;
    private boolean enableDexterity;
    private boolean enableFocus;
    private boolean enableEnergy;

    public BlueCrystalPower(AbstractCreature owner) {
        this(owner, 1);
    }

    public BlueCrystalPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        this.enableStrength = true;
        this.enableDexterity = true;
        this.enableFocus = true;
        this.enableEnergy = true;
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        this.enableStrength = true;
        this.enableDexterity = true;
        this.enableFocus = true;
        this.enableEnergy = true;
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();

    }

    @Override
    public void atEnergyGain() {
        super.atEnergyGain();
        if (this.enableEnergy){
            this.enableStrength = false;
            this.enableDexterity = false;
            this.enableFocus = false;
            this.enableEnergy = false;
            addToBot(new GainEnergyAction(1));
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        super.onApplyPower(power, target, source);
        if (enableStrength && power.ID.equals(StrengthPower.POWER_ID) && target.equals(this.owner)){
            this.enableStrength = false;
            this.enableDexterity = false;
            this.enableFocus = false;
            this.enableEnergy = false;
            addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, 1)));
            if (this.amount > 1){
                this.amount -= 1;
                this.updateDescription();
            } else {
                addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            }
        } else if (enableDexterity && power.ID.equals(DexterityPower.POWER_ID) && target.equals(this.owner)) {
            this.enableStrength = false;
            this.enableDexterity = false;
            this.enableFocus = false;
            this.enableEnergy = false;
            addToBot(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, 1)));
            if (this.amount > 1){
                this.amount -= 1;
                this.updateDescription();
            } else {
                addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            }        } else if (enableFocus && power.ID.equals(FocusPower.POWER_ID) && target.equals(this.owner)) {
            this.enableStrength = false;
            this.enableDexterity = false;
            this.enableFocus = false;
            this.enableEnergy = false;
            addToBot(new ApplyPowerAction(this.owner, this.owner, new FocusPower(this.owner, 1)));
            if (this.amount > 1){
                this.amount -= 1;
                this.updateDescription();
            } else {
                addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            }        }
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        this.name = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
        if (this.amount == 1){
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }
    }
}
