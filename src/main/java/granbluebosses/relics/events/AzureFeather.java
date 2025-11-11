package granbluebosses.relics.events;

import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import granbluebosses.GranblueBosses;
import granbluebosses.relics.BaseRelic;

public class AzureFeather extends BaseRelic {

    public static final String RELIC_ID = GranblueBosses.makeID("AzureFeather");

    private int turnCount;
    private boolean active;

    public AzureFeather() {
        super(
                RELIC_ID,       // ID
                "AzureFeather",
                AbstractCard.CardColor.COLORLESS,
                RelicTier.SPECIAL,                              // Rarity
                LandingSound.MAGICAL);                            // SFX
        this.relicType = RelicType.SHARED;
        this.turnCount = 0;
        this.active = true;
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.turnCount = 0;
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        this.turnCount++;
        if (this.turnCount <= 3){
            this.active = true;
        }
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (this.active && targetCard.type == AbstractCard.CardType.ATTACK){
            this.active = false;
            addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 1));
        }
        super.onUseCard(targetCard, useCardAction);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + 1 + DESCRIPTIONS[1]; // DESCRIPTIONS pulls from your localization file
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AzureFeather();
    }
}
