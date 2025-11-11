package granbluebosses.relics.rosequeen;

import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import granbluebosses.GranblueBosses;
import granbluebosses.relics.BaseRelic;
import granbluebosses.relics.events.AzureFeather;

public class RoseShield extends BaseRelic {

    public static final String RELIC_ID = GranblueBosses.makeID("RoseShield");

    public RoseShield() {
        super(
                RELIC_ID,       // ID
                "RoseShield",
                AbstractCard.CardColor.COLORLESS,
                RelicTier.SPECIAL,                              // Rarity
                LandingSound.HEAVY);                            // SFX
        this.relicType = RelicType.SHARED;
    }

    public int onLoseHpLast(int damageAmount) {
        if (damageAmount > 0) {
            this.flash();
            return damageAmount - 1;
        } else {
            return damageAmount;
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RoseShield();
    }
}
