package granbluebosses.cards.rewards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import granbluebosses.cards.BaseCard;

public class LeviathanOmega extends BaseCard {

    public static final String CARD_ID = makeID("LeviathanOmega");
    private static final int DAMAGE = 4;
    private static final int DAMAGE_UPG = 2;
    private static final int BLOCK = 0;
    private static final int BLOCK_UPG = 0;
    private static final int MAGIC = 2;
    private static final int MAGIC_UPG = 1;

    public LeviathanOmega() {
        super(
                CARD_ID,
                1,
                CardType.ATTACK,
                CardTarget.ALL_ENEMY,
                CardRarity.SPECIAL,
                CardColor.COLORLESS//,
//                cardImage
        );

        this.setDamage(DAMAGE, DAMAGE_UPG);
        this.setBlock(BLOCK, BLOCK_UPG);
        this.setMagic(MAGIC, MAGIC_UPG);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new DamageAllEnemiesAction(p, this.damage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.POISON));
        addToBot(new ApplyPowerAction(abstractMonster, p, new VulnerablePower(abstractMonster, this.magicNumber, false), this.magicNumber));

    }
}
