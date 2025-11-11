package granbluebosses.cards.protobaha.optionCards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import granbluebosses.cards.BaseCard;

public class SwordOfBahamut extends BaseCard {

    public static final String CARD_ID = makeID("SwordOfBahamut");

    public SwordOfBahamut() {
        super(
                CARD_ID,
                0,
                CardType.SKILL,
                CardTarget.SELF,
                CardRarity.SPECIAL,
                CardColor.COLORLESS//,
//                cardImage
        );
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        if (this.upgraded){
            AbstractDungeon.player.getRelic(granbluebosses.relics.protobaha.SwordOfBahamutCoda.RELIC_ID);
        } else {
            AbstractDungeon.player.getRelic(granbluebosses.relics.protobaha.SwordOfBahamut.RELIC_ID);
        }
    }
}
