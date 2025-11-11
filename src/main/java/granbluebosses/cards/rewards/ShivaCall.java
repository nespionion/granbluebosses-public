package granbluebosses.cards.rewards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import granbluebosses.cards.BaseCard;

public class ShivaCall extends BaseCard {

    public static final String CARD_ID = makeID("ShivaCall");

    private static final int DAMAGE = 10;
    private static final int DAMAGE_UPG = 2;
    private static final int BLOCK = 0;
    private static final int BLOCK_UPG = 0;
    private static final int MAGIC = 8;
    private static final int MAGIC_UPG = 2;

    public ShivaCall() {
        super(
                CARD_ID,
                1,
                CardType.ATTACK,
                CardTarget.ENEMY,
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
        addToBot(new DamageAction(abstractMonster, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));

        addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber)));
    }
}
