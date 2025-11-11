package granbluebosses.powers.protobaha;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import granbluebosses.powers.BasePower;
import granbluebosses.util.GeneralUtils;
import granbluebosses.util.TextureLoader;

import static granbluebosses.GranblueBosses.makeID;

public class RagnarokField extends BasePower {
    public static final String POWER_ID = makeID("RagnarokField");
    private static final AbstractPower.PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = false;
    private static PowerStrings getPowerStrings(String ID) {return CardCrawlGame.languagePack.getPowerStrings(ID);}
    private static PowerStrings powerStrings = getPowerStrings(POWER_ID);

    public RagnarokField(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, null, amount, false, false);

        this.amount = amount;

        this.updateDescription();

        this.loadSprites();

    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, amount, DamageInfo.DamageType.HP_LOSS)));
    }

    private void loadSprites() {
        String unPrefixed = "RagnarokField";
        if (this.amount == 1 || this.amount == 2 || this.amount == 3){
            unPrefixed = "RagnarokField" + this.amount;
        }

        Texture normalTexture = TextureLoader.getPowerTexture(unPrefixed);
        Texture hiDefImage = TextureLoader.getHiDefPowerTexture(unPrefixed);
        if (hiDefImage != null) {
            region128 = new TextureAtlas.AtlasRegion(hiDefImage, 0, 0, hiDefImage.getWidth(), hiDefImage.getHeight());
            if (normalTexture != null)
                region48 = new TextureAtlas.AtlasRegion(normalTexture, 0, 0, normalTexture.getWidth(), normalTexture.getHeight());
        } else {
            this.img = normalTexture;
            region48 = new TextureAtlas.AtlasRegion(normalTexture, 0, 0, normalTexture.getWidth(), normalTexture.getHeight());
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        if (this.amount > 3 || this.amount < 1){
            super.renderAmount(sb, x, y, c);
        }
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        this.name = powerStrings.NAME;
        this.description = this.DESCRIPTIONS[0] + this.amount + this.DESCRIPTIONS[1];
    }
}
