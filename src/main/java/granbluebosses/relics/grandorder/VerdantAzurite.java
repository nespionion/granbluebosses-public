package granbluebosses.relics.grandorder;

import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import granbluebosses.GranblueBosses;
import granbluebosses.relics.BaseRelic;
import granbluebosses.relics.events.AzureFeather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class VerdantAzurite extends BaseRelic {

    public static final String RELIC_ID = GranblueBosses.makeID("VerdantAzurite");

    public VerdantAzurite() {
        super(
                RELIC_ID,       // ID
                "VerdantAzurite",
                AbstractCard.CardColor.COLORLESS,
                RelicTier.SPECIAL,                              // Rarity
                LandingSound.HEAVY);                            // SFX
        this.relicType = RelicType.SHARED;
    }

    public void onEquip() {
        ArrayList<AbstractCard> upgradableCards = new ArrayList();

        for(AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.canUpgrade() && c.rarity == AbstractCard.CardRarity.RARE) {
                upgradableCards.add(c);
            }
        }

        if (!upgradableCards.isEmpty()){
            for(AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.canUpgrade() && c.rarity == AbstractCard.CardRarity.UNCOMMON) {
                    upgradableCards.add(c);
                }
            }
        }

        if (!upgradableCards.isEmpty()){
            for(AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.canUpgrade() && c.rarity == AbstractCard.CardRarity.COMMON) {
                    upgradableCards.add(c);
                }
            }
        }

        if (!upgradableCards.isEmpty()){
            for(AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.canUpgrade() && c.rarity == AbstractCard.CardRarity.BASIC) {
                    upgradableCards.add(c);
                }
            }
        }

        Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
        if (!upgradableCards.isEmpty()) {
            if (upgradableCards.size() == 1) {
                ((AbstractCard)upgradableCards.get(0)).upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck((AbstractCard)upgradableCards.get(0));
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(((AbstractCard)upgradableCards.get(0)).makeStatEquivalentCopy()));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            } else {
                ((AbstractCard)upgradableCards.get(0)).upgrade();
                ((AbstractCard)upgradableCards.get(1)).upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck((AbstractCard)upgradableCards.get(0));
                AbstractDungeon.player.bottledCardUpgradeCheck((AbstractCard)upgradableCards.get(1));
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(((AbstractCard)upgradableCards.get(0)).makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 20.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(((AbstractCard)upgradableCards.get(1)).makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            }
        }

    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]; // DESCRIPTIONS pulls from your localization file
    }

    @Override
    public AbstractRelic makeCopy() {
        return new VerdantAzurite();
    }
}
