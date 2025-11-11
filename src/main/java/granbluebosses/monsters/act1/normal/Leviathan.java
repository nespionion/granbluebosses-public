package granbluebosses.monsters.act1.normal;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import granbluebosses.GranblueBosses;
import granbluebosses.acts.Act1Skies;
import granbluebosses.config.ConfigMenu;
import granbluebosses.util.Sounds;

import static granbluebosses.GranblueBosses.makeID;

public class Leviathan extends CustomMonster {
    protected static final String MONSTER_NAME = "Leviathan";
    public static final String MONSTER_ID = makeID("Leviathan");
    protected static final int MONSTER_MAX_HP = 50;
    protected static final int MONSTER_MAX_HP_A_19 = 50 + 2;
    protected static final float MONSTER_HIT_BOX_X = 0;
    protected static final float MONSTER_HIT_BOX_Y = -30.0F;
    protected static final float MONSTER_HIT_BOX_WIDTH = 400.0F;
    protected static final float MONSTER_HIT_BOX_HEIGHT = 350.0F;
    protected static final String MONSTER_IMG_URL = (String) null;
    protected static final String MONSTER_ANIM_URL = MONSTER_NAME.toLowerCase();
    protected static final float MONSTER_OFF_SET_X = 0.0F;
    protected static final float MONSTER_OFF_SET_Y = 28.0F;
    protected boolean trigger = true;
    protected boolean firstTurn = true;
    protected static final MonsterStrings monsterStrings;
    public static final String CASCADE;
    public static final String AZURE_BLADE;
    protected int azureBladeDmg = 3;
    protected int azureBladeHits = 2;
    protected int cascadeStacks = 2;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public static final int AZURE_BLADE_INDEX = 0;

    public Leviathan() {
        super(MONSTER_NAME, MONSTER_ID, MONSTER_MAX_HP, MONSTER_HIT_BOX_X, MONSTER_HIT_BOX_Y, MONSTER_HIT_BOX_WIDTH, MONSTER_HIT_BOX_HEIGHT, MONSTER_IMG_URL, MONSTER_OFF_SET_X, MONSTER_OFF_SET_Y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(MONSTER_MAX_HP_A_19);
        } else {
            this.setHp(MONSTER_MAX_HP);
        }
        if (AbstractDungeon.ascensionLevel >= 17){
            this.azureBladeDmg += 1;
            this.cascadeStacks *= 1.5f;
        }

        this.damage.add(new DamageInfo(this, azureBladeDmg, DamageInfo.DamageType.NORMAL));

        this.loadAnimation(GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".atlas"), GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".json"), 1.0F);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (ConfigMenu.enableDMCAMusic){
            AbstractDungeon.getCurrRoom().playBgmInstantly(Sounds.MUSIC_ACT1_BATTLE);
        }
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0:
                this.useCascade();
                break;
            case 1:
                this.useAzureBlade();
                break;
        }
        this.prepareIntent();
    }

    public void useCascade(){
        addToBot(new AnimateSlowAttackAction(this));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, cascadeStacks, true)));
    }

    public void useAzureBlade(){
        for (int i = 0; i*2 < this.azureBladeHits; i++){
            addToBot(new AnimateSlowAttackAction(this));
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(AZURE_BLADE_INDEX), AbstractGameAction.AttackEffect.POISON));
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(AZURE_BLADE_INDEX), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        }
    }

    protected void prepareIntent() {
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.prepareIntentA17();
            return;
        }
        switch (this.nextMove) {
            case 0:
                addToBot(new SetMoveAction(this, AZURE_BLADE, (byte)1, Intent.ATTACK, this.azureBladeDmg, this.azureBladeHits, true));
                break;
            case 1:
                addToBot(new SetMoveAction(this, CASCADE, (byte)0, Intent.DEBUFF));
                break;
        }
    }

    protected void prepareIntentA17() {
        if (AbstractDungeon.aiRng.randomBoolean()){
            addToBot(new SetMoveAction(this, CASCADE, (byte)0, Intent.DEBUFF));
        } else {
            addToBot(new SetMoveAction(this, AZURE_BLADE, (byte)1, Intent.ATTACK, this.azureBladeDmg, this.azureBladeHits, true));
        }
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            if (AbstractDungeon.aiRng.randomBoolean()){
                this.setMove(CASCADE, (byte)0, Intent.DEBUFF);
            } else {
                this.setMove(AZURE_BLADE, (byte)1, Intent.ATTACK, this.azureBladeDmg, this.azureBladeHits, true);
            }
        }
    }

    @Override
    public void die() {
        super.die();
        Act1Skies.resumeMainMusic();

    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(MONSTER_ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
        CASCADE = MOVES[0];
        AZURE_BLADE = MOVES[1];
    }
}
