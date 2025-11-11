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

public class HarpOfBahamut extends BaseRelic {

    public static final String RELIC_ID = GranblueBosses.makeID("HarpOfBahamut");

    public HarpOfBahamut() {
        super(
                RELIC_ID,       // ID
                "HarpOfBahamut",
                AbstractCard.CardColor.COLORLESS,
                RelicTier.SPECIAL,                              // Rarity
                LandingSound.HEAVY);                            // SFX
        this.relicType = RelicType.SHARED;
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.flash();
        this.addToTop(new MakeTempCardInHandAction(new Miracle()));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]; // DESCRIPTIONS pulls from your localization file
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HarpOfBahamut();
    }
}