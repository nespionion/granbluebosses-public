package granbluebosses.monsters.act1.normal;

import basemod.BaseMod;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.GoldenSlashEffect;
import granbluebosses.GranblueBosses;
import granbluebosses.acts.Act1Skies;
import granbluebosses.cards.rewards.ColossusOmega;
import granbluebosses.cards.rewards.TiamatOmega;
import granbluebosses.config.ConfigMenu;
import granbluebosses.events.CelesteEvent;
import granbluebosses.events.ColossusEvent;
import granbluebosses.powers.StanceOmen;
import granbluebosses.powers.a_monsters.DebuffOnHit;
import granbluebosses.util.Sounds;

import java.util.ArrayList;

import static granbluebosses.GranblueBosses.makeID;

public class Colossus2 extends CustomMonster {
    protected static final String MONSTER_NAME = "Colossus";
    public static final String MONSTER_ID = makeID("Colossus2");
    protected static final int MONSTER_MAX_HP = 70;
    protected static final int MONSTER_MAX_HP_A_19 = 70 + 4;
    protected static final float MONSTER_HIT_BOX_X = 0;
    protected static final float MONSTER_HIT_BOX_Y = -30.0F;
    protected static final float MONSTER_HIT_BOX_WIDTH = 400.0F;
    protected static final float MONSTER_HIT_BOX_HEIGHT = 350.0F;
    protected static final String MONSTER_IMG_URL = (String) null;
    protected static final String MONSTER_ANIM_URL = MONSTER_NAME.toLowerCase() + "2";
    protected static final float MONSTER_OFF_SET_X = 0.0F;
    protected static final float MONSTER_OFF_SET_Y = 28.0F;
    protected boolean trigger = true;
    protected final int OMEN_MULT = 4;
    protected boolean firstTurn = true;
    protected static final MonsterStrings monsterStrings;
    public static final String IGNITE;
    public static final String FORCE_FIELD;
    public static final String DIMENSIONAL_CLEAVER;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    protected int igniteDmg = 12;
    protected int forceFieldBlock = 6;
    protected int dimensionalCleaveDmg = 32;

    public static final int IGNITE_INDEX = 0;
    public static final int DIMENSIONAL_CLEAVER_INDEX = 1;

    public Colossus2() {
        super(MONSTER_NAME, MONSTER_ID, MONSTER_MAX_HP, MONSTER_HIT_BOX_X, MONSTER_HIT_BOX_Y, MONSTER_HIT_BOX_WIDTH, MONSTER_HIT_BOX_HEIGHT, MONSTER_IMG_URL, MONSTER_OFF_SET_X, MONSTER_OFF_SET_Y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(MONSTER_MAX_HP_A_19);
            this.forceFieldBlock += 2;
        } else {
            this.setHp(MONSTER_MAX_HP);
        }
        if (AbstractDungeon.ascensionLevel >= 2){
            this.igniteDmg += 2;
        }
        if (AbstractDungeon.ascensionLevel >= 17){
            this.igniteDmg += 1;
        } else {
            this.dimensionalCleaveDmg -= 8;
        }

        this.damage.add(new DamageInfo(this, this.igniteDmg, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.dimensionalCleaveDmg, DamageInfo.DamageType.NORMAL));

        this.loadAnimation(GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".atlas"), GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".json"), 1.0F);
    }

    @Override
    public void usePreBattleAction() {
        StanceOmen omen = new StanceOmen(this);
        omen.setUpOmen(OMEN_MULT);
        addToTop(new ApplyPowerAction(this, this, omen));
        super.usePreBattleAction();
        if (AbstractDungeon.ascensionLevel >= 17){
            addToBot(new ApplyPowerAction(this, this, new DebuffOnHit(this, DebuffOnHit.AvailableDebuffs.VULNERABLE, 2)));
        }
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
            case 2:
                this.useDimensionalCleaver();
                break;
        }
        this.prepareIntent();
    }

    public void useForceField(){
        addToBot(new GainBlockAction(this, this.forceFieldBlock));
    }

    public void useIgnite(){
        addToBot(new AnimateSlowAttackAction(this));
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(IGNITE_INDEX), AbstractGameAction.AttackEffect.FIRE));
    }

    public void useDimensionalCleaver(){
        float vfxSpeed = 0.1F;
        if (Settings.FAST_MODE) {
            vfxSpeed = 0.0F;
        }

        addToBot(new VFXAction(new GoldenSlashEffect(AbstractDungeon.player.hb.cX * Settings.scale, AbstractDungeon.player.hb.cY, true), vfxSpeed));
        addToBot(new AnimateSlowAttackAction(this));
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(DIMENSIONAL_CLEAVER_INDEX), AbstractGameAction.AttackEffect.NONE));
    }

    protected void prepareIntent() {
        if (this.currentHealth * this.OMEN_MULT <= this.maxHealth && this.nextMove == 1 && this.trigger) {
            this.trigger = false;
            addToTop(new RemoveSpecificPowerAction(this, this, StanceOmen.POWER_ID));
            addToBot(new TextAboveCreatureAction(this, "DANGER!"));
            addToBot(new SetMoveAction(this, DIMENSIONAL_CLEAVER, (byte)2, Intent.ATTACK, this.dimensionalCleaveDmg, 1, false));
            return;
        }
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
            case 2:
                addToBot(new SetMoveAction(this, FORCE_FIELD, (byte)1, Intent.DEFEND));
                break;
        }
    }

    protected void prepareIntentA17() {
        if (AbstractDungeon.aiRng.randomBoolean()) {
            addToBot(new SetMoveAction(this, FORCE_FIELD, (byte) 1, Intent.DEFEND));
        } else {
            addToBot(new SetMoveAction(this, IGNITE, (byte) 0, Intent.ATTACK, this.igniteDmg, 1, false));
        }
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            this.setMove(FORCE_FIELD, (byte)1, Intent.DEFEND);
        }
    }

    @Override
    public void die() {
        if (ConfigMenu.enableExtraRewards) {
            RewardItem reward = new RewardItem(AbstractCard.CardColor.COLORLESS);
            reward.cards = new ArrayList<>();
            reward.cards.add(new ColossusOmega());

            for (AbstractCard c : reward.cards) {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    r.onPreviewObtainCard(c);
                }
            }
            AbstractDungeon.getCurrRoom().rewards.add(reward);
        }

        Act1Skies.resumeMainMusic();
        BaseMod.addEvent(ColossusEvent.EVENT_ID, ColossusEvent.class, Act1Skies.ID);

        super.die();
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(MONSTER_ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
        IGNITE = MOVES[0];
        FORCE_FIELD = MOVES[1];
        DIMENSIONAL_CLEAVER = MOVES[2];
    }
}
