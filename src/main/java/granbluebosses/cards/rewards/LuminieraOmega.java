package granbluebosses.cards.rewards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import granbluebosses.cards.BaseCard;

public class LuminieraOmega extends BaseCard {

    public static final String CARD_ID = makeID("LuminieraOmega");
    private static final int DAMAGE = 9;
    private static final int DAMAGE_UPG = 2;
    private static final int BLOCK = 0;
    private static final int BLOCK_UPG = 0;
    private static final int MAGIC = 2;
    private static final int MAGIC_UPG = 1;

    public LuminieraOmega() {
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
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new DamageAction(abstractMonster, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HEAVY));

        if (abstractMonster.hasPower(StrengthPower.POWER_ID) && abstractMonster.getPower(StrengthPower.POWER_ID).amount > 1){
            addToBot(new RemoveSpecificPowerAction(abstractMonster, p, StrengthPower.POWER_ID));
        }

        if (abstractMonster.hasPower(DexterityPower.POWER_ID) && abstractMonster.getPower(DexterityPower.POWER_ID).amount > 1){
            addToBot(new RemoveSpecificPowerAction(abstractMonster, p, DexterityPower.POWER_ID));
        }
    }
}
