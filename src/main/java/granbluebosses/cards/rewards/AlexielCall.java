package granbluebosses.cards.rewards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import granbluebosses.cards.BaseCard;

public class AlexielCall extends BaseCard {

    public static final String CARD_ID = makeID("AlexielCall");

    private static final int DAMAGE = 0;
    private static final int DAMAGE_UPG = 0;
    private static final int BLOCK = 10;
    private static final int BLOCK_UPG = 5;
    private static final int MAGIC = 1;
    private static final int MAGIC_UPG = 1;

    public AlexielCall() {
        super(
                CARD_ID,
                1,
                CardType.SKILL,
                CardTarget.SELF,
                CardRarity.SPECIAL,
                CardColor.COLORLESS//,
//                cardImage
        );

        this.setDamage(DAMAGE, DAMAGE_UPG);
        this.setBlock(BLOCK, BLOCK_UPG);
        this.setMagic(MAGIC, MAGIC_UPG);
        this.setExhaust(true, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, magicNumber)));

        addToBot(new GainBlockAction(p, block));
    }
}
