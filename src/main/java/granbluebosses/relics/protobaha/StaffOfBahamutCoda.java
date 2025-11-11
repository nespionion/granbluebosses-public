package granbluebosses.relics.protobaha;

import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import granbluebosses.GranblueBosses;
import granbluebosses.cards.BaseCard;
import granbluebosses.relics.BaseRelic;
import granbluebosses.relics.events.AzureFeather;

public class StaffOfBahamutCoda extends BaseRelic {

    public static final String RELIC_ID = GranblueBosses.makeID("StaffOfBahamutCoda");

    public StaffOfBahamutCoda() {
        super(
                RELIC_ID,                                       // ID
                "StaffOfBahamutCoda",
                AbstractCard.CardColor.COLORLESS,
                RelicTier.SPECIAL,                              // Rarity
                LandingSound.HEAVY);                            // SFX
        this.relicType = RelicType.SHARED;
    }

    @Override
    public void onEquip() {
        super.onEquip();
        AbstractDungeon.player.increaseMaxHp(4, false);
        AbstractDungeon.player.loseRelic(StaffOfBahamut.RELIC_ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.flash();
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, 2), 2));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]; // DESCRIPTIONS pulls from your localization file
    }

    @Override
    public AbstractRelic makeCopy() {
        return new StaffOfBahamutCoda();
    }
}
