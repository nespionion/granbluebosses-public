package granbluebosses.relics.protobaha;

import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import granbluebosses.GranblueBosses;
import granbluebosses.relics.BaseRelic;
import granbluebosses.relics.events.AzureFeather;

public class SwordOfBahamut extends BaseRelic {

    public static final String RELIC_ID = GranblueBosses.makeID("SwordOfBahamut");

    public SwordOfBahamut() {
        super(
                RELIC_ID,                                                     // ID
                "SwordOfBahamut",
                AbstractCard.CardColor.COLORLESS,
                AbstractRelic.RelicTier.SPECIAL,                              // Rarity
                AbstractRelic.LandingSound.HEAVY);                            // SFX
        this.relicType = RelicType.SHARED;
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.flash();
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]; // DESCRIPTIONS pulls from your localization file
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SwordOfBahamut();
    }
}
