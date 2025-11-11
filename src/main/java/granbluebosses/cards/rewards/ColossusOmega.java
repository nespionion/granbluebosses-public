package granbluebosses.cards.rewards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import granbluebosses.cards.BaseCard;

public class ColossusOmega extends BaseCard {

    public static final String CARD_ID = makeID("ColossusOmega");
    private static final int DAMAGE = 4;
    private static final int DAMAGE_UPG = 2;
    private static final int BLOCK = 4;
    private static final int BLOCK_UPG = 2;
    private static final int MAGIC = 2;
    private static final int MAGIC_UPG = 1;


    public ColossusOmega() {
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
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new DamageAllEnemiesAction(abstractPlayer, this.damage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new GainBlockAction(abstractPlayer, this.block));
    }
}
