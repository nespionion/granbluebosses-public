package granbluebosses.relics.events;

import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import granbluebosses.GranblueBosses;
import granbluebosses.powers.relic.BlueCrystalPower;
import granbluebosses.relics.BaseRelic;

public class BlueCrystal extends BaseRelic {

    public static final String RELIC_ID = GranblueBosses.makeID("BlueCrystal");

    public BlueCrystal() {
        super(
                RELIC_ID,       // ID
                "BlueCrystal",
                AbstractCard.CardColor.COLORLESS,
                RelicTier.SPECIAL,                              // Rarity
                LandingSound.MAGICAL);                            // SFX
        this.relicType = RelicType.SHARED;

    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BlueCrystalPower(AbstractDungeon.player)));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]; // DESCRIPTIONS pulls from your localization file
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BlueCrystal();
    }
}
