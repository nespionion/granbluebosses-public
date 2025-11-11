package granbluebosses.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static granbluebosses.GranblueBosses.makeID;

public class IncantedOmen extends BasePower{
    public static final String POWER_ID = makeID("IncantedOmen");
    private static final AbstractPower.PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private static PowerStrings getPowerStrings(String ID) {return CardCrawlGame.languagePack.getPowerStrings(ID);}
    private static PowerStrings powerStrings = getPowerStrings(POWER_ID);
    protected final AbstractCard.CardRarity rarity;

    public enum OMEN_TYPE {
        DAMAGE_OMEN,
        HITS_OMEN,
        DEBUFF_OMEN,
        CARDS_OMEN,
        RARITY_OMEN
    }
    protected OMEN_TYPE omenType;

    public IncantedOmen(AbstractCreature owner, int amount, OMEN_TYPE omenType) {
        this(owner, amount, omenType, AbstractCard.CardRarity.COMMON);
    }

    public IncantedOmen(AbstractCreature owner, int amount, OMEN_TYPE omenType, AbstractCard.CardRarity rarity) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        this.omenType = omenType;
        this.rarity = rarity;
    }

    public void lowerAmount(int num){
        this.amount -= num;


        if (this.amount <= 0){
            // TODO : Cancel Omen
        }

        this.updateDescription();
    }



    @Override
    public void updateDescription() {
        super.updateDescription();
        this.name = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
        this.description = DESCRIPTIONS[0];
        switch (this.omenType){
            case DAMAGE_OMEN:
                this.setUpOmenDmg();
            case HITS_OMEN:
                this.setUpOmenHits();
            case DEBUFF_OMEN:
                this.setUpOmenDebuff();
            case CARDS_OMEN:
                this.setUpOmenCardsPlayed();
            case RARITY_OMEN:
                this.setUpOmenCardsRarity(this.rarity);
        }
    }

    public void setUpOmenDmg(){
        this.description = "This enemy is preparing a powerful attack. Deal " + this.amount + " damage to stop it.";
    }

    public void setUpOmenHits(){
        this.description = "This enemy is preparing a powerful attack. Deal damage " + this.amount + " times to stop it.";
    }

    public void setUpOmenDebuff(){
        this.description = "This enemy is preparing a powerful attack. Inflict " + this.amount + " debuffs to stop it.";
    }

    public void setUpOmenCardsPlayed(){
        this.description = "This enemy is preparing a powerful attack. Play " + this.amount + " cards to stop it.";
    }

    public void setUpOmenCardsRarity(AbstractCard.CardRarity rarity){
        String rarityText = "Common";
        if (rarity == AbstractCard.CardRarity.UNCOMMON){
            rarityText = "Uncommon";
        } else if (rarity == AbstractCard.CardRarity.RARE) {
            rarityText = "Rare";
        }

        if (this.amount == 1){
            this.description = "This enemy is preparing a powerful attack. Play " + this.amount + " " + rarityText + " card to stop it.";
        } else {
            this.description = "This enemy is preparing a powerful attack. Play " + this.amount + " " + rarityText + " cards to stop it.";
        }
    }

    private boolean checkRarity(AbstractCard usedCard){
        if (this.rarity == AbstractCard.CardRarity.UNCOMMON || this.rarity == AbstractCard.CardRarity.RARE){
            return this.rarity == usedCard.rarity;
        } else {
            return usedCard.rarity != AbstractCard.CardRarity.UNCOMMON && usedCard.rarity != AbstractCard.CardRarity.RARE;
        }
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        super.onAfterCardPlayed(usedCard);
        if ((this.omenType == OMEN_TYPE.CARDS_OMEN) || (this.omenType == OMEN_TYPE.RARITY_OMEN && this.checkRarity(usedCard))){
            this.lowerAmount(1);
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (this.omenType == OMEN_TYPE.HITS_OMEN){
            this.lowerAmount(1);
        } else if (this.omenType == OMEN_TYPE.DAMAGE_OMEN) {
            this.lowerAmount(damageAmount);
        }

        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (this.omenType ==  OMEN_TYPE.DEBUFF_OMEN && (power.type == PowerType.DEBUFF || source.equals(AbstractDungeon.player) )){
            if (power.amount > 0){
                this.lowerAmount(power.amount);
            } else {
                this.lowerAmount(1);
            }
        }
        super.onApplyPower(power, target, source);
    }

}
