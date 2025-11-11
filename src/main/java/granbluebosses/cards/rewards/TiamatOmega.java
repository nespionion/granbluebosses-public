package granbluebosses.cards.rewards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import granbluebosses.cards.BaseCard;
import granbluebosses.config.ConfigMenu;

import static granbluebosses.util.GeneralUtils.removePrefix;
import static granbluebosses.util.TextureLoader.getCardTextureString;

public class TiamatOmega extends BaseCard {

    public static final String CARD_ID = makeID("TiamatOmega");

    private static final int DAMAGE = 4;
    private static final int DAMAGE_UPG = 2;
    private static final int BLOCK = 0;
    private static final int BLOCK_UPG = 0;
    private static final int MAGIC = 2;
    private static final int MAGIC_UPG = 1;

    public TiamatOmega() {
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
        addToBot(new VFXAction(new WhirlwindEffect()));
        addToBot(new DamageAllEnemiesAction(abstractPlayer, this.damage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE));
        addToBot(new ApplyPowerAction(abstractMonster, abstractPlayer, new WeakPower(abstractMonster, this.magicNumber, false), this.magicNumber));
    }

    @Override
    public void render(SpriteBatch sb) {
        if (ConfigMenu.modestyFilter){
            String img = getCardTextureString(removePrefix(makeID("TiamatOmegaCen")), this.type);
            this.textureImg = img;
            if (img != null) {
                this.loadCardImage(img);
            }
        }
        super.render(sb);
    }
}
