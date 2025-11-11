package granbluebosses.relics.protobaha;

import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import granbluebosses.GranblueBosses;
import granbluebosses.relics.BaseRelic;
import granbluebosses.relics.events.AzureFeather;

public class HarpOfBahamutCoda extends BaseRelic {

    public static final String RELIC_ID = GranblueBosses.makeID("HarpOfBahamutCoda");

    public HarpOfBahamutCoda() {
        super(
                RELIC_ID,                                                     // ID
                "HarpOfBahamutCoda",
                AbstractCard.CardColor.COLORLESS,
                AbstractRelic.RelicTier.SPECIAL,                              // Rarity
                AbstractRelic.LandingSound.HEAVY);                            // SFX
        this.relicType = RelicType.SHARED;
    }

    @Override
    public void onEquip() {
        super.onEquip();
        AbstractDungeon.player.increaseMaxHp(4, false);
        AbstractDungeon.player.loseRelic(HarpOfBahamut.RELIC_ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.flash();
        AbstractCard card = new Miracle();
        card.upgrade();
        this.addToTop(new MakeTempCardInHandAction(card));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]; // DESCRIPTIONS pulls from your localization file
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HarpOfBahamutCoda();
    }
}
