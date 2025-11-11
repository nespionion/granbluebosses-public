package granbluebosses.relics.events;

import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import granbluebosses.GranblueBosses;
import granbluebosses.relics.BaseRelic;

public class ShieldOfTenets extends BaseRelic {

    public static final String RELIC_ID = GranblueBosses.makeID("ShieldOfTenets");

    public ShieldOfTenets() {
        super(
                RELIC_ID,       // ID
                "ShieldOfTenets",
                AbstractCard.CardColor.COLORLESS,
                RelicTier.SPECIAL,                              // Rarity
                LandingSound.MAGICAL);                            // SFX
        this.relicType = RelicType.SHARED;
    }

    public void onEquip() {
        for(RewardItem reward : AbstractDungeon.combatRewardScreen.rewards) {
            if (reward.cards != null) {
                for(AbstractCard c : reward.cards) {
                    this.onPreviewObtainCard(c);
                }
            }
        }

    }

    public void onPreviewObtainCard(AbstractCard c) {
        this.onObtainCard(c);
    }

    public void onObtainCard(AbstractCard c) {
        if (c.rarity == AbstractCard.CardRarity.RARE && c.canUpgrade() && !c.upgraded) {
            c.upgrade();
        }
    }

    public AbstractRelic makeCopy() {
        return new ShieldOfTenets();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]; // DESCRIPTIONS pulls from your localization file
    }
}
