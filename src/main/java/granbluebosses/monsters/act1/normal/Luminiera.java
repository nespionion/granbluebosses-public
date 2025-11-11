package granbluebosses.monsters.act1.normal;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.red.LimitBreak;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.EmpowerEffect;
import granbluebosses.GranblueBosses;
import granbluebosses.acts.Act1Skies;
import granbluebosses.config.ConfigMenu;
import granbluebosses.powers.StanceOmen;
import granbluebosses.powers.a_monsters.EbbTidePower;
import granbluebosses.util.Sounds;

import static granbluebosses.GranblueBosses.makeID;

public class Luminiera extends CustomMonster {
    protected static final String MONSTER_NAME = "Luminiera";
    public static final String MONSTER_ID = makeID("Luminiera");
    protected static final int MONSTER_MAX_HP = 30;
    protected static final int MONSTER_MAX_HP_A_19 = 30 + 3;
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
    protected int aegisMergeBlock = 15;
    protected int iliadMergeStacks = 1;
    protected int bladeOfLightDmg = 1;
    protected int bladeOfLightHits = 5;
    protected static final MonsterStrings monsterStrings;
    public static final String AEGIS_MERGE;
    public static final String ILIAD_MERGE;
    public static final String BLADE_OF_LIGHT;
    protected static final String OMEN_DESCRIPTION = "This enemy will prepare a powerful attack when it loses all its block";
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public static final int BLADE_OF_LIGHT_INDEX = 0;

    public Luminiera() {
        super(MONSTER_NAME, MONSTER_ID, MONSTER_MAX_HP, MONSTER_HIT_BOX_X, MONSTER_HIT_BOX_Y, MONSTER_HIT_BOX_WIDTH, MONSTER_HIT_BOX_HEIGHT, MONSTER_IMG_URL, MONSTER_OFF_SET_X, MONSTER_OFF_SET_Y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(MONSTER_MAX_HP_A_19);
            this.aegisMergeBlock += 5;
        } else {
            this.setHp(MONSTER_MAX_HP);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.bladeOfLightHits++;
        }

        this.damage.add(new DamageInfo(this, bladeOfLightDmg, DamageInfo.DamageType.NORMAL));

        this.loadAnimation(GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".atlas"), GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".json"), 1.0F);
    }

    @Override
    public void usePreBattleAction() {
        StanceOmen omen = new StanceOmen(this);
        omen.setUpOmen(OMEN_DESCRIPTION);
        addToTop(new ApplyPowerAction(this, this, omen));
        if (ConfigMenu.enableDMCAMusic){
            AbstractDungeon.getCurrRoom().playBgmInstantly(Sounds.MUSIC_ACT1_BATTLE);
        }
        super.usePreBattleAction();
    }


    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0:
                this.useAegisMerge();
                break;
            case 1:
                this.useIliadMerge();
                break;
            case 2:
                this.useBladeOfLight();
                break;
        }
        this.prepareIntent();
    }

    protected void useAegisMerge(){
        addToBot(new GainBlockAction(this, aegisMergeBlock));
        addToBot(new ApplyPowerAction(this, this, new BarricadePower(this)));
//        addToBot(new ApplyPowerAction(this, this, new BarricadePower(this)));
    }

    protected void useIliadMerge(){
        this.addToBot(new SFXAction("BUFF_1"));
        addToBot(new VFXAction(new EmpowerEffect(this.hb.cX, this.hb.cY)));
        addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, iliadMergeStacks)));
    }

    protected void useBladeOfLight(){
        for (int i = 0; i < bladeOfLightHits; i++){
            addToBot(new AnimateSlowAttackAction(this));
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(BLADE_OF_LIGHT_INDEX), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
    }

    protected void prepareIntent() {
        if (trigger && this.currentBlock < 1) {
            this.trigger = false;
            addToTop(new RemoveSpecificPowerAction(this, this, StanceOmen.POWER_ID));

            addToBot(new SetMoveAction(this, ILIAD_MERGE, (byte)1, Intent.BUFF));
        } else {
            addToBot(new SetMoveAction(this, BLADE_OF_LIGHT, (byte)2, Intent.ATTACK, this.bladeOfLightDmg, this.bladeOfLightHits, true));
        }
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            this.setMove(AEGIS_MERGE, (byte)0, Intent.DEFEND);
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
        AEGIS_MERGE = MOVES[0];
        ILIAD_MERGE = MOVES[1];
        BLADE_OF_LIGHT = MOVES[2];
    }
}
