package granbluebosses.monsters.act1.elites;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.EmpowerEffect;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;
import granbluebosses.GranblueBosses;
import granbluebosses.acts.Act1Skies;
import granbluebosses.cards.rewards.ShivaCall;
import granbluebosses.cards.rewards.TiamatOmega;
import granbluebosses.config.ConfigMenu;
import granbluebosses.powers.StanceOmen;
import granbluebosses.powers.a_monsters.PathOfDestruction;
import granbluebosses.util.Sounds;

import java.util.ArrayList;

import static granbluebosses.GranblueBosses.makeID;

public class Shiva extends CustomMonster {
    protected static final String MONSTER_NAME = "Shiva";
    public static final String MONSTER_ID = makeID("Shiva");
    protected static final String MONSTER_ANIM_URL = MONSTER_NAME.toLowerCase();
    protected static final int MONSTER_MAX_HP = 85;
    protected static final int MONSTER_MAX_HP_A_19 = 85 + 5;
    protected static final float MONSTER_HIT_BOX_X = 0;
    protected static final float MONSTER_HIT_BOX_Y = -30.0F;
    protected static final float MONSTER_HIT_BOX_WIDTH = 400.0F;
    protected static final float MONSTER_HIT_BOX_HEIGHT = 350.0F;
    protected static final String MONSTER_IMG_URL = (String) null;
    protected static final float MONSTER_OFF_SET_X = 0.0F;
    protected static final float MONSTER_OFF_SET_Y = 28.0F;
    protected boolean trigger = true;
    protected final int OMEN_MULT = 3;
    protected boolean firstTurn = true;
    protected int rudraBuffStacks = 1;
    protected int rudraDebuffStacks = 2;
    protected int sriRudramDmg = 14;
    protected int awakenInnerEyeDmg = 5;
    protected float awakenInnerEyeMult = 1.5f;
    protected static final MonsterStrings monsterStrings;
    public static final String RUDRA;
    public static final String SRI_RUDRAM;
    public static final String AWAKEN_INNER_EYE;
    public static final String ENTRY_DIALOG;
    public static final String AWAKEN_DIALOG;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public static final int SRI_RUDRAM_INDEX = 0;
    public static final int AWAKEN_INNER_EYE_INDEX = 1;
    public static final int SRI_RUDRAM_EMPOWERED_INDEX = 0;

    public Shiva() {
        super(MONSTER_NAME, MONSTER_ID, MONSTER_MAX_HP, MONSTER_HIT_BOX_X, MONSTER_HIT_BOX_Y, MONSTER_HIT_BOX_WIDTH, MONSTER_HIT_BOX_HEIGHT, MONSTER_IMG_URL, MONSTER_OFF_SET_X, MONSTER_OFF_SET_Y);
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(MONSTER_MAX_HP_A_19);
        } else {
            this.setHp(MONSTER_MAX_HP);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.awakenInnerEyeDmg += 2;
            this.awakenInnerEyeMult = 2.5f;
            this.rudraBuffStacks += 1;
        } else if (AbstractDungeon.ascensionLevel >= 3) {
            this.awakenInnerEyeDmg += 1;
            this.awakenInnerEyeMult = 2f;
        } else {
            this.awakenInnerEyeDmg -= 1;
            this.awakenInnerEyeMult -= 1.5f;
        }

        this.loadAnimation(GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".atlas"), GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".json"), 1.0F);

        this.damage.add(new DamageInfo(this, this.sriRudramDmg, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.awakenInnerEyeDmg, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, (int)(this.sriRudramDmg * awakenInnerEyeMult), DamageInfo.DamageType.NORMAL));
    }

    @Override
    public void usePreBattleAction() {
        if (ConfigMenu.enableDMCAMusic){
            AbstractDungeon.getCurrRoom().playBgmInstantly(Sounds.MUSIC_ACT1_ELITE_SHIVA);
        } else {
            AbstractDungeon.getCurrRoom().playBgmInstantly("ELITE");
        }

        StanceOmen omen = new StanceOmen(this);
        omen.setUpOmen(OMEN_MULT);
        addToBot(new ApplyPowerAction(this, this, omen));

        super.usePreBattleAction();
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0:
                this.useRudra();
                break;
            case 1:
                this.useSriRudram();
                break;
            case 2:
                this.useAwakenInnerEye();
                break;
        }
        this.prepareIntent();
    }

    protected void useRudra(){
        addToBot(new VFXAction(new EmpowerEffect(this.hb.cX, this.hb.cY)));
        this.addToBot(new SFXAction("BUFF_1"));

        addToBot(new ApplyPowerAction(this, this, new VulnerablePower(this, rudraDebuffStacks+1, true)));
        addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, rudraBuffStacks), this.rudraBuffStacks));
    }

    protected void useSriRudram(){
        float vfxSpeed = 0.1F;
        if (Settings.FAST_MODE) {
            vfxSpeed = 0.0F;
        }

        addToBot(new VFXAction(new CleaveEffect(true), vfxSpeed));
        this.addToBot(new SFXAction("ATTACK_HEAVY"));
        addToBot(new AnimateSlowAttackAction(this));
        if (this.hasPower(PathOfDestruction.POWER_ID)){
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(SRI_RUDRAM_EMPOWERED_INDEX), AbstractGameAction.AttackEffect.NONE));
            addToBot(new RemoveSpecificPowerAction(this, this, PathOfDestruction.POWER_ID));
        } else {
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(SRI_RUDRAM_INDEX), AbstractGameAction.AttackEffect.NONE));
        }
    }

    protected void useAwakenInnerEye(){

        this.addToBot(new SFXAction("ATTACK_DEFECT_BEAM"));
        this.addToBot(new VFXAction(this, new SweepingBeamEffect(this.hb.cX, this.hb.cY, this.flipHorizontal), 0.4F));
        addToBot(new AnimateSlowAttackAction(this));

        addToBot(new ShoutAction(this, AWAKEN_DIALOG));
        addToBot(new SFXAction(Sounds.SHIVA_AWAKEN_DIALOG));

        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(AWAKEN_INNER_EYE_INDEX), AbstractGameAction.AttackEffect.NONE));
        addToBot(new ApplyPowerAction(this, this, new PathOfDestruction(this)));
    }

    protected void prepareIntent() {
        if (this.currentHealth * this.OMEN_MULT <= this.maxHealth && this.trigger) {
            this.trigger = false;
            addToTop(new RemoveSpecificPowerAction(this, this, StanceOmen.POWER_ID));
            addToBot(new TextAboveCreatureAction(this, "DANGER!"));
            addToBot(new SetMoveAction(this, AWAKEN_DIALOG, (byte) 2, Intent.ATTACK_BUFF, this.awakenInnerEyeDmg, 1, false));
            return;
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.prepareIntentA17();
            return;
        }
        switch (this.nextMove) {
            case 0:
                addToBot(new SetMoveAction(this, SRI_RUDRAM, (byte) 1, Intent.ATTACK, this.sriRudramDmg, 1, false));
                break;
            case 1:
                addToBot(new SetMoveAction(this, RUDRA, (byte) 0, Intent.BUFF));
                break;
            case 2:
                addToBot(new SetMoveAction(this, SRI_RUDRAM, (byte) 1, Intent.ATTACK, (int)(this.sriRudramDmg * awakenInnerEyeMult), 1, false));
                break;
        }
    }

    protected void prepareIntentA17() {
        switch (this.nextMove) {
            case 0:
                addToBot(new SetMoveAction(this, SRI_RUDRAM, (byte) 1, Intent.ATTACK, this.sriRudramDmg, 1, false));
                break;
            case 1:
                addToBot(new SetMoveAction(this, RUDRA, (byte) 0, Intent.BUFF));
                break;
            case 2:
                addToBot(new SetMoveAction(this, SRI_RUDRAM, (byte) 1, Intent.ATTACK, (int)(this.sriRudramDmg * awakenInnerEyeMult), 1, false));
                break;
        }
    }

    @Override
    public void die() {
        if (ConfigMenu.enableExtraRewards) {
            RewardItem reward = new RewardItem(AbstractCard.CardColor.COLORLESS);
            reward.cards = new ArrayList<>();
            reward.cards.add(new ShivaCall());

            for (AbstractCard c : reward.cards) {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    r.onPreviewObtainCard(c);
                }
            }
            AbstractDungeon.getCurrRoom().rewards.add(reward);
        }
        Act1Skies.resumeMainMusic();
        super.die();
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;

            addToBot(new ShoutAction(this, ENTRY_DIALOG));
            addToBot(new SFXAction(Sounds.SHIVA_ENTRY_DIALOG));
            this.setMove(RUDRA, (byte) 0, Intent.BUFF);

        }
    }

    @Override
    public void createIntent() {
        super.createIntent();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(MONSTER_ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
        RUDRA = MOVES[0];
        SRI_RUDRAM = MOVES[1];
        AWAKEN_INNER_EYE = MOVES[2];
        ENTRY_DIALOG = DIALOG[0];
        AWAKEN_DIALOG = DIALOG[1];
    }
}