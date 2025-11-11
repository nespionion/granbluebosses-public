package granbluebosses.cards.rewards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import granbluebosses.cards.BaseCard;
import granbluebosses.config.ConfigMenu;

import static granbluebosses.util.GeneralUtils.removePrefix;
import static granbluebosses.util.TextureLoader.getCardTextureString;

public class YggdrasilOmega extends BaseCard {

    public static final String CARD_ID = makeID("YggdrasilOmega");
    private static final int DAMAGE = 4;
    private static final int DAMAGE_UPG = 2;
    private static final int BLOCK = 0;
    private static final int BLOCK_UPG = 0;
    private static final int MAGIC = 2;
    private static final int MAGIC_UPG = 1;

    public YggdrasilOmega() {
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
        addToBot(new DamageAllEnemiesAction(p, this.damage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        for (int i = 0; i < this.magicNumber; i++){
            if (AbstractDungeon.cardRng.randomBoolean()){
                addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 1), 1));
                addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, 1), 1));
            } else {
                addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, 1), 1));
                addToBot(new ApplyPowerAction(p, p, new LoseDexterityPower(p, 1), 1));
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (ConfigMenu.modestyFilter){
            String img = getCardTextureString(removePrefix(makeID("YggdrasilOmegaCen")), this.type);
            this.textureImg = img;
            if (img != null) {
                this.loadCardImage(img);
            }
        }
        super.render(sb);
    }
}
