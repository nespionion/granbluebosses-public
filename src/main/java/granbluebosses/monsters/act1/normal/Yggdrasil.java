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
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.vfx.combat.HeartBuffEffect;
import granbluebosses.GranblueBosses;
import granbluebosses.acts.Act1Skies;
import granbluebosses.config.ConfigMenu;
import granbluebosses.util.Sounds;

import static granbluebosses.GranblueBosses.makeID;

public class Yggdrasil extends CustomMonster {
    protected static final String MONSTER_NAME = "Yggdrasil";
    public static final String MONSTER_ID = makeID("Yggdrasil");
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
    protected int axisMundiDmg = 8;
    protected int luminoxGenesiStacks = 2;
    protected boolean firstTurn = true;
    protected static final MonsterStrings monsterStrings;
    public static final String LUMINOX_GENESI;
    public static final String AXIS_MUNDI;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;


    public static final int AXIS_MUNDI_INDEX = 0;

    // TODO : Rework buff/debuff shenanigans
    public Yggdrasil() {
        super(MONSTER_NAME, MONSTER_ID, MONSTER_MAX_HP, MONSTER_HIT_BOX_X, MONSTER_HIT_BOX_Y, MONSTER_HIT_BOX_WIDTH, MONSTER_HIT_BOX_HEIGHT, MONSTER_IMG_URL, MONSTER_OFF_SET_X, MONSTER_OFF_SET_Y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(MONSTER_MAX_HP_A_19);
        } else {
            this.setHp(MONSTER_MAX_HP);
        }
        if (AbstractDungeon.ascensionLevel >= 17){
            this.axisMundiDmg += 4;
        }

        this.damage.add(new DamageInfo(this, axisMundiDmg, DamageInfo.DamageType.NORMAL));

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
                this.useLuminoxGenesi();
                break;
            case 1:
                this.useAxisMundi();
                break;
        }
        this.prepareIntent();
    }

    protected void useLuminoxGenesi(){
        AbstractPlayer p = AbstractDungeon.player;

        addToBot(new VFXAction(new HeartBuffEffect(p.hb.cX, p.hb.cY)));
        if (AbstractDungeon.aiRng.randomBoolean()){
            addToBot(new ApplyPowerAction(p, this, new WeakPower(p, luminoxGenesiStacks, true)));
        } else {
            addToBot(new ApplyPowerAction(p, this, new VigorPower(p, luminoxGenesiStacks)));
        }
        addToBot(new VFXAction(new HeartBuffEffect(p.hb.cX, p.hb.cY)));
        if (AbstractDungeon.aiRng.randomBoolean()){
            addToBot(new ApplyPowerAction(p, this, new FrailPower(p, luminoxGenesiStacks, true)));
        } else {
            addToBot(new GainBlockAction(p, luminoxGenesiStacks*2));
        }

        if (AbstractDungeon.ascensionLevel >= 17 && AbstractDungeon.aiRng.randomBoolean()){
            addToBot(new VFXAction(new HeartBuffEffect(p.hb.cX, p.hb.cY)));
            addToBot(new ApplyPowerAction(p, this, new VulnerablePower(p, luminoxGenesiStacks, true)));
        } else if (AbstractDungeon.ascensionLevel >= 17) {
            addToBot(new VFXAction(new HeartBuffEffect(p.hb.cX, p.hb.cY)));
            addToBot(new ApplyPowerAction(p, this, new DexterityPower(p, luminoxGenesiStacks/2)));
        }
    }

    protected void useAxisMundi(){
        addToBot(new AnimateSlowAttackAction(this));
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(AXIS_MUNDI_INDEX), AbstractGameAction.AttackEffect.SMASH));

    }

    protected void prepareIntent() {
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.prepareIntentA17();
            return;
        }
        switch (this.nextMove) {
            case 0:
                addToBot(new SetMoveAction(this, AXIS_MUNDI, (byte)1, Intent.ATTACK, this.axisMundiDmg, 1, false));
                break;
            case 1:
                addToBot(new SetMoveAction(this, LUMINOX_GENESI, (byte)0, Intent.DEBUFF));
                break;
        }
    }

    protected void prepareIntentA17() {
        if (AbstractDungeon.aiRng.randomBoolean()) {
            addToBot(new SetMoveAction(this, LUMINOX_GENESI, (byte)0, Intent.DEBUFF));
        } else {
            addToBot(new SetMoveAction(this, AXIS_MUNDI, (byte)1, Intent.ATTACK, this.axisMundiDmg, 1, false));
        }
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            if (AbstractDungeon.aiRng.randomBoolean()) {
                this.setMove(LUMINOX_GENESI, (byte)0, Intent.DEBUFF);
            } else {
                this.setMove(AXIS_MUNDI, (byte)1, Intent.ATTACK, this.axisMundiDmg, 1, false);
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
        LUMINOX_GENESI = MOVES[0];
        AXIS_MUNDI = MOVES[1];
    }
}
