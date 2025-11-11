package granbluebosses.relics.rosequeen;

import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import granbluebosses.GranblueBosses;
import granbluebosses.relics.BaseRelic;
import granbluebosses.relics.events.AzureFeather;

public class CrystalRose extends BaseRelic {

    public static final String RELIC_ID = GranblueBosses.makeID("CrystalRose");

    public CrystalRose() {
        super(
                RELIC_ID,       // ID
                "CrystalRose",
                AbstractCard.CardColor.COLORLESS,
                RelicTier.SPECIAL,                              // Rarity
                LandingSound.HEAVY);                            // SFX
        this.relicType = RelicType.SHARED;
    }

    @Override
    public void onEquip() {
        super.onEquip();
        AbstractPlayer p = AbstractDungeon.player;
        if (!p.relics.isEmpty() && !p.relics.get(0).relicId.equals(RELIC_ID)){
            p.getRelic(p.relics.get(0).relicId);
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CrystalRose();
    }
}
