package granbluebosses.monsters.act1.normal;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import granbluebosses.GranblueBosses;
import granbluebosses.acts.Act1Skies;
import granbluebosses.config.ConfigMenu;
import granbluebosses.util.Sounds;

import static granbluebosses.GranblueBosses.makeID;

public class Colossus extends CustomMonster {
    protected static final String MONSTER_NAME = "Colossus";
    public static final String MONSTER_ID = makeID("Colossus");
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
    public static final String IGNITE;
    public static final String FORCE_FIELD;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    protected int igniteDmg = 12;
    protected int forceFieldBlock = 6;

    public static final int IGNITE_INDEX = 0;

    public Colossus() {
        super(MONSTER_NAME, MONSTER_ID, MONSTER_MAX_HP, MONSTER_HIT_BOX_X, MONSTER_HIT_BOX_Y, MONSTER_HIT_BOX_WIDTH, MONSTER_HIT_BOX_HEIGHT, MONSTER_IMG_URL, MONSTER_OFF_SET_X, MONSTER_OFF_SET_Y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(MONSTER_MAX_HP_A_19);
            this.forceFieldBlock += 2;
        } else {
            this.setHp(MONSTER_MAX_HP);
        }
        if (AbstractDungeon.ascensionLevel >= 17){
            this.igniteDmg += 3;
        }

        this.damage.add(new DamageInfo(this, this.igniteDmg, DamageInfo.DamageType.NORMAL));

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
                this.useIgnite();
                break;
            case 1:
                this.useForceField();
                break;
        }
        this.prepareIntent();
    }

    public void useForceField(){
        addToBot(new GainBlockAction(this, this.forceFieldBlock));
    }

    public void useIgnite(){
        addToBot(new AnimateSlowAttackAction(this));
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
    }

    protected void prepareIntent() {
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.prepareIntentA17();
            return;
        }
        switch (this.nextMove) {
            case 0:
                addToBot(new SetMoveAction(this, FORCE_FIELD, (byte)1, Intent.DEFEND));
                break;
            case 1:
                addToBot(new SetMoveAction(this, IGNITE, (byte)0, Intent.ATTACK, this.igniteDmg, 1, false));
                break;
        }
    }

    protected void prepareIntentA17() {
        if (AbstractDungeon.aiRng.randomBoolean()){
            addToBot(new SetMoveAction(this, FORCE_FIELD, (byte)1, Intent.DEFEND));
        } else {
            addToBot(new SetMoveAction(this, IGNITE, (byte)0, Intent.ATTACK, this.igniteDmg, 1, false));
        }
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            this.setMove((byte)1, Intent.DEFEND);
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
        IGNITE = MOVES[0];
        FORCE_FIELD = MOVES[1];
    }
}
