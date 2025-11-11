package granbluebosses.monsters.act1.normal;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.vfx.combat.IntimidateEffect;
import granbluebosses.GranblueBosses;
import granbluebosses.acts.Act1Skies;
import granbluebosses.config.ConfigMenu;
import granbluebosses.util.Sounds;

import static granbluebosses.GranblueBosses.makeID;

public class Celeste extends CustomMonster {
    protected static final String MONSTER_NAME = "Celeste";
    public static final String MONSTER_ID = makeID("Celeste");
    protected static final int MONSTER_MAX_HP = 40;
    protected static final int MONSTER_MAX_HP_A_19 = 40 + 2;
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
    public static final String TRANQUIL_FOG;
    public static final String BIZARRE_FOG;
    protected int tranquilFogDmg = 3;
    protected int bizarreFogStacks = 5;

    protected static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public Celeste() {
        super(MONSTER_NAME, MONSTER_ID, MONSTER_MAX_HP, MONSTER_HIT_BOX_X, MONSTER_HIT_BOX_Y, MONSTER_HIT_BOX_WIDTH, MONSTER_HIT_BOX_HEIGHT, MONSTER_IMG_URL, MONSTER_OFF_SET_X, MONSTER_OFF_SET_Y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(MONSTER_MAX_HP_A_19);
        } else {
            this.setHp(MONSTER_MAX_HP);
        }
        if (AbstractDungeon.ascensionLevel >= 17){
            this.tranquilFogDmg += 2;
        }

        this.damage.add(new DamageInfo(this, this.tranquilFogDmg, DamageInfo.DamageType.NORMAL));

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
                this.useTranquilFog();
                break;
            case 1:
                this.useBizarreFog();
                break;
        }
        this.prepareIntent();
    }

    protected void useTranquilFog(){
        addToBot(new AnimateSlowAttackAction(this));
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.POISON));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new RegenPower(AbstractDungeon.player, bizarreFogStacks)));
    }

    protected void useBizarreFog(){
        addToBot(new VFXAction(this, new IntimidateEffect(this.hb.cX, this.hb.cY), 0.5F));

        AbstractPower playerRegen = AbstractDungeon.player.getPower(RegenPower.POWER_ID);
        if (playerRegen != null){
            addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new PoisonPower(AbstractDungeon.player, this, playerRegen.amount)));
            addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, this, RegenPower.POWER_ID));
        }

    }

    protected void prepareIntent() {
        if (AbstractDungeon.player.hasPower(RegenPower.POWER_ID)) {
            addToBot(new SetMoveAction(this, BIZARRE_FOG, (byte)1, Intent.STRONG_DEBUFF));
        } else {
            addToBot(new SetMoveAction(this, TRANQUIL_FOG, (byte)0, Intent.ATTACK_DEBUFF, this.tranquilFogDmg, 1, false));
        }
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            this.setMove(TRANQUIL_FOG, (byte)0, Intent.ATTACK_DEBUFF, this.tranquilFogDmg, 1, false);
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
        TRANQUIL_FOG = MOVES[0];
        BIZARRE_FOG = MOVES[1];
    }
}
