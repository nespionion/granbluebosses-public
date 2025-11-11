package granbluebosses.monsters.act1.normal;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import granbluebosses.GranblueBosses;
import granbluebosses.acts.Act1Skies;
import granbluebosses.config.ConfigMenu;
import granbluebosses.util.Sounds;

import static granbluebosses.GranblueBosses.makeID;


public class Tiamat extends CustomMonster {
    protected static final String MONSTER_NAME = "Tiamat";
    public static final String MONSTER_ID = makeID("Tiamat");
    protected static final int MONSTER_MAX_HP = 50;
    protected static final int MONSTER_MAX_HP_A_19 = 52;
    protected static final float MONSTER_HIT_BOX_X = 0;
    protected static final float MONSTER_HIT_BOX_Y = -30.0F;
    protected static final float MONSTER_HIT_BOX_WIDTH = 400.0F;
    protected static final float MONSTER_HIT_BOX_HEIGHT = 350.0F;
    protected static final String MONSTER_IMG_URL = (String)null;
    protected static final String MONSTER_ANIM_URL = MONSTER_NAME.toLowerCase();
    protected static final float MONSTER_OFF_SET_X = 0.0F;
    protected static final float MONSTER_OFF_SET_Y = 28.0F;
    protected boolean firstTurn = true;
    protected static final MonsterStrings monsterStrings;
    public static final String CRIPPLING_STORM;
    public static final String WIND_TORRENT;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    protected int cripplingStormStacks = 2;
    protected int windTorrentDmg = 7;


    public static final int WIND_TORRENT_INDEX = 0;

    public Tiamat() {
        super(MONSTER_NAME, MONSTER_ID, MONSTER_MAX_HP, MONSTER_HIT_BOX_X, MONSTER_HIT_BOX_Y, MONSTER_HIT_BOX_WIDTH, MONSTER_HIT_BOX_HEIGHT, MONSTER_IMG_URL, MONSTER_OFF_SET_X, MONSTER_OFF_SET_Y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(MONSTER_MAX_HP_A_19);

        } else {
            this.setHp(MONSTER_MAX_HP);;
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.cripplingStormStacks *= 1.5f;
        }

        this.damage.add(new DamageInfo(this, windTorrentDmg, DamageInfo.DamageType.NORMAL));

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
                this.useWindTorrent();
                break;
            case 1:
                this.useCripplingStorm();
                break;
        }
        this.prepareIntent();
    }

    public void useCripplingStorm(){
        addToBot(new VFXAction(new WhirlwindEffect(), 0.0F));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, cripplingStormStacks, true)));
    }

    public void useWindTorrent(){
        addToBot(new VFXAction(new WhirlwindEffect(), 0.0F));
        addToBot(new AnimateSlowAttackAction(this));
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(WIND_TORRENT_INDEX)));
    }

    protected  void prepareIntent(){
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.prepareIntentA17();
            return;
        }
        if (AbstractDungeon.aiRng.randomBoolean()) {
            addToBot(new SetMoveAction(this, WIND_TORRENT, (byte)0, Intent.ATTACK, windTorrentDmg));
        } else {
            addToBot(new SetMoveAction(this, CRIPPLING_STORM, (byte)1, Intent.DEBUFF));
        }
    }

    protected  void prepareIntentA17(){
        if (AbstractDungeon.aiRng.randomBoolean() || AbstractDungeon.aiRng.randomBoolean()) {
            addToBot(new SetMoveAction(this, WIND_TORRENT, (byte)0, Intent.ATTACK, windTorrentDmg));
        } else {
            addToBot(new SetMoveAction(this, CRIPPLING_STORM, (byte)1, Intent.DEBUFF));
        }
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            if (AbstractDungeon.aiRng.randomBoolean()) {
                this.setMove(WIND_TORRENT, (byte) 0, Intent.ATTACK, windTorrentDmg);
            } else {
                this.setMove(CRIPPLING_STORM, (byte) 1, Intent.DEBUFF);
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
        CRIPPLING_STORM = MOVES[0];
        WIND_TORRENT = MOVES[1];
    }
}
