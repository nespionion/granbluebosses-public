package granbluebosses.relics.grandorder;

import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import granbluebosses.GranblueBosses;
import granbluebosses.relics.BaseRelic;
import granbluebosses.relics.events.AzureFeather;

public class ShieldOfLamentation extends BaseRelic {

    public static final String RELIC_ID = GranblueBosses.makeID("ShieldOfLamentation");
    public int damage;

    public ShieldOfLamentation() {
        super(
                RELIC_ID,       // ID
                "ShieldOfLamentation",
                AbstractCard.CardColor.COLORLESS,
                RelicTier.SPECIAL,                              // Rarity
                LandingSound.HEAVY);                            // SFX
        this.relicType = RelicType.SHARED;
        this.damage = AbstractDungeon.actNum + 1;

    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (targetCard.type == AbstractCard.CardType.ATTACK){
            addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, AbstractDungeon.actNum + 1, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.NONE));
        }
        super.onUseCard(targetCard, useCardAction);
    }

    @Override
    public String getUpdatedDescription() {
        this.damage = AbstractDungeon.actNum + 1;
        this.description = this.DESCRIPTIONS[0] + this.damage + this.DESCRIPTIONS[1];
        return this.description;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ShieldOfLamentation();
    }
}
