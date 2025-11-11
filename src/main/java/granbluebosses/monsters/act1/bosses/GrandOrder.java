package granbluebosses.monsters.act1.bosses;

import VideoTheSpire.actions.RunTopLevelEffectAction;
import VideoTheSpire.effects.SimplePlayVideoEffect;
import basemod.abstracts.CustomMonster;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.GoldenSlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import granbluebosses.GranblueBosses;
import granbluebosses.acts.Act1Skies;
import granbluebosses.cards.protobaha.optionCards.DaggerOfBahamut;
import granbluebosses.cards.protobaha.optionCards.HarpOfBahamut;
import granbluebosses.cards.protobaha.optionCards.StaffOfBahamut;
import granbluebosses.cards.protobaha.optionCards.SwordOfBahamut;
import granbluebosses.config.ConfigMenu;
import granbluebosses.powers.OverdriveState;
import granbluebosses.powers.grandorder.FieldOfMediation;
import granbluebosses.powers.grandorder.WormholePower;
import granbluebosses.relics.grandorder.CosmicSword;
import granbluebosses.relics.grandorder.ShieldOfLamentation;
import granbluebosses.util.Sounds;

import java.util.ArrayList;

import static granbluebosses.GranblueBosses.*;
import static granbluebosses.GranblueBosses.monsterPath;

public class GrandOrder extends CustomMonster {
    protected static final String MONSTER_NAME = "Grand Order";
    public static final String MONSTER_ID = makeID("GrandOrder");
    public static final String MAP_ICON = monsterPath("grandorder/grandorder_map_icon.png");
    public static final String OUTLINE = monsterPath("grandorder/grandorder_outline.png");
    protected static final int MONSTER_MAX_HP = 225;
    protected static final int MONSTER_MAX_HP_A_19 = 250;
    protected static final float MONSTER_HIT_BOX_X = 0;
    protected static final float MONSTER_HIT_BOX_Y = -30.0F;
    protected static final float MONSTER_HIT_BOX_WIDTH = 400.0F;
    protected static final float MONSTER_HIT_BOX_HEIGHT = 350.0F;
    protected static final String MONSTER_IMG_URL = (String)null;
    protected static final String MONSTER_ANIM_URL = MONSTER_NAME.toLowerCase();
    protected static final float MONSTER_OFF_SET_X = 0.0F;
    protected static final float MONSTER_OFF_SET_Y = 28.0F;
    protected int phase = 1;
    protected static final MonsterStrings monsterStrings;
    public static final String NAME;

    protected static final String BATTLE_START;
    protected static final String SPINNING_SLASH;
    protected static final String BISECTION;
    protected static final String PRISM_HALO;
    protected static final String OPPOSITION;
    protected static final String CONJUNCTION;
    protected static final String RULER_OF_FATE;
    protected static final String GAMMA_RAY;
    protected static final String LAST_WISH;
    protected static final String OUTBURST;
    protected static final String UNCHAIN;

    protected static final int SPINNING_SLASH_INDEX = 0;
    protected static final int BISECTION_INDEX = 1;
    protected static final int OPPOSITION_INDEX = 2;
    protected static final int OUTBURST_INDEX = 3;

    protected  int spinningSlashDmg;
    protected  int spinningSlashHits;
    protected  int bisectionDmg;
    protected  int bisectionStacks;
    protected  int prismHaloBlock;
    protected  int oppositionDmg;
    protected  int rulerOfFateStacks;
    protected  int rulerOfFateThreshold;
    protected  int rulerOfFateThresholdIncrease;
    protected  int arbitrationThreshold;

    protected float gammaRayDmgMult;
    protected int gammaRayThreshold;
    protected boolean gammaRay1Ready;
    protected boolean gammaRay2Ready;
    protected int lastWishStacks;
    protected int outburstDmg;
    protected int outburstHits;

    protected static final String BATTLE_START_DIALOG;
    protected static final String SPINNING_SLASH_DIALOG;
    protected static final String BISECTION1_DIALOG;
    protected static final String PRISM_HALO1_DIALOG;
    protected static final String OVERDRIVE1_DIALOG;
    protected static final String BREAK1_DIALOG;
    protected static final String PHASE2_DIALOG;
    protected static final String PHASE3_DIALOG;

    protected static final String OPPOSITION_DIALOG;
    protected static final String CONJUNCTION_DIALOG;
    protected static final String RULER_OF_FATE_DIALOG;
    protected static final String OVERDRIVE2_DIALOG;
    protected static final String BREAK2_DIALOG;
    protected static final String DEFEAT1_DIALOG;
    protected static final String PHASE4_DIALOG;

    protected static final String GAMMA_RAY_DIALOG;
    protected static final String LAST_WISH_DIALOG;
    protected static final String OUTBURST_DIALOG;
    protected static final String DEFEAT2_DIALOG;
    protected static final String BISECTION2_DIALOG;
    protected static final String PRISM_HALO2_DIALOG;

    protected boolean firstTurn = true;
    protected boolean isHL = false;
    protected boolean isDMCA = false;
    private String phase1Song = "BOSS_BOTTOM";
    private String phase2Song = "BOSS_CITY";
    private String phase3Song = "BOSS_BEYOND";



    public GrandOrder(){
        super(MONSTER_NAME, MONSTER_ID, MONSTER_MAX_HP, MONSTER_HIT_BOX_X, MONSTER_HIT_BOX_Y, MONSTER_HIT_BOX_WIDTH, MONSTER_HIT_BOX_HEIGHT, MONSTER_IMG_URL, MONSTER_OFF_SET_X, MONSTER_OFF_SET_Y);
        if (AbstractDungeon.ascensionLevel >= 10) {
            this.setHp(MONSTER_MAX_HP_A_19);
        } else {
            this.setHp(MONSTER_MAX_HP);
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.spinningSlashDmg = 10;
        } else {
            this.spinningSlashDmg = 8;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.spinningSlashHits = 2;
        } else {
            this.spinningSlashHits = 2;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.bisectionDmg = 18;
        } else {
            this.bisectionDmg = 12;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.bisectionStacks = 2;
        } else {
            this.bisectionStacks = 1;
        }

        if (AbstractDungeon.ascensionLevel >= 10) {
            this.prismHaloBlock = 12;
        } else {
            this.prismHaloBlock = 9;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.oppositionDmg = 30;
        } else {
            this.oppositionDmg = 30;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.rulerOfFateStacks = 3;
        } else {
            this.rulerOfFateStacks = 2;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.rulerOfFateThreshold = 30;
        } else if (AbstractDungeon.ascensionLevel >= 10) {
            this.rulerOfFateThreshold = 25;
        } else {
            this.rulerOfFateThreshold = 20;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.rulerOfFateThresholdIncrease = 10;
        } else {
            this.rulerOfFateThresholdIncrease = 5;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.arbitrationThreshold = 1;
        } else {
            this.arbitrationThreshold = 10;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.gammaRayDmgMult = 0.2f;
        } else {
            this.gammaRayDmgMult = 0.1f;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.gammaRayThreshold = 4;  // Change to 10 after first Gamma Ray
        } else {
            this.gammaRayThreshold = 4; // Change to 10 after first Gamma Ray
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.lastWishStacks = 3;
        } else {
            this.lastWishStacks = 2;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.outburstDmg = 2;
        } else {
            this.outburstDmg = 2;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.outburstHits = 6;
        } else {
            this.outburstHits = 5;
        }

        this.isDMCA = ConfigMenu.enableDMCAMusic;
        if (this.isDMCA){
            this.phase1Song = Sounds.MUSIC_ACT1_GRANDORDER1;
            this.phase2Song = Sounds.MUSIC_ACT1_GRANDORDER2;
            this.phase3Song = Sounds.MUSIC_ACT1_GRANDORDER3;
        }

        this.gammaRay1Ready = true; // Change to false after first Gamma Ray
        this.gammaRay2Ready = true; // Change to false after second Gamma Ray

        this.loadAnimation(GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".atlas"), GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".json"), 1.0F);
        AnimationState.TrackEntry idle1 = this.state.setAnimation(0, "idle1", true);

        this.damage.add(new DamageInfo(this, this.spinningSlashDmg));
        this.damage.add(new DamageInfo(this, this.bisectionDmg));
        this.damage.add(new DamageInfo(this, this.oppositionDmg));
        this.damage.add(new DamageInfo(this, this.outburstDmg));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.getCurrRoom().playBgmInstantly(this.phase1Song);
    }

    @Override
    public void takeTurn() {
        switch (this.phase){
            case 1:
                this.takeTurnPhase1();
                break;
            case 2:
                this.takeTurnPhase2();
                break;
            default:
                this.takeTurnPhase1();
                break;
        }
        this.prepareIntent();
    }

    private void takeTurnPhase1() {
        switch (this.nextMove) {
            case 1:
                this.usePrismHalo();
                break;
            case 2:
                this.useSpinningSlash();
                break;
            case 3:
                this.usePrismHalo();
                break;
            case 4:
                this.useBisection();
                break;
            case 5:
                this.phase2Transition();
                break;
            default:
                this.useFieldOfMeditation();
                break;
        }
    }

    private void takeTurnPhase2() {
        switch (this.nextMove) {
            case 1:
                this.useRulerOfFate();
                break;
            case 2:
                this.useConjunction();
                break;
            case 3:
                this.useOpposition();
                break;
            default:
                this.useRulerOfFate();
                break;
        }
    }

    protected void prepareIntent(){
        switch (this.phase){
            case 1:
                this.prepareIntentPhase1();
                break;
            case 2:
                this.prepareIntentPhase2();
                break;
            default:
                this.prepareIntentPhase1();
                break;
        }
    }

    protected void prepareIntentPhase1(){
        switch (this.nextMove) {
            case 1:
                addToBot(new SetMoveAction(this, SPINNING_SLASH, (byte)2, Intent.ATTACK, spinningSlashDmg, spinningSlashHits, true));
                break;
            case 2:
                addToBot(new SetMoveAction(this, PRISM_HALO, (byte)3, Intent.DEFEND_BUFF));
                break;
            case 3:
                addToBot(new SetMoveAction(this, BISECTION, (byte)4, Intent.ATTACK_DEBUFF, spinningSlashDmg, 1, false));
                break;
            case 4:
                addToBot(new SetMoveAction(this, PRISM_HALO, (byte)1, Intent.DEFEND_BUFF));
                break;
            case 5:
                addToBot(new SetMoveAction(this, SPINNING_SLASH, (byte)3, Intent.ATTACK, spinningSlashDmg, spinningSlashHits, true));
                break;
            default:
                addToBot(new SetMoveAction(this, PRISM_HALO, (byte)1, Intent.DEFEND_BUFF));
                break;
        }
    }

    protected void prepareIntentPhase2(){
        switch (this.nextMove) {
            case 1:
                addToBot(new TextAboveCreatureAction(this, "DANGER!"));
                addToBot(new SetMoveAction(this, CONJUNCTION, (byte)2, Intent.ATTACK));
                break;
            case 2:
                addToBot(new SetMoveAction(this, OPPOSITION, (byte)3, Intent.ATTACK_DEBUFF, this.oppositionDmg, 1, false));
                break;
            case 3:
                addToBot(new SetMoveAction(this, OPPOSITION, (byte)3, Intent.ATTACK_DEBUFF, this.oppositionDmg, 1, false));
                break;
            default:
                addToBot(new SetMoveAction(this, CONJUNCTION, (byte)2, Intent.ATTACK));
                break;
        }
    }


        @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            this.setMove(BATTLE_START, (byte)0, Intent.UNKNOWN);
        }
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (!this.isDying && this.hasPower(WormholePower.POWER_ID)){
            AbstractPower wormhole = this.getPower(WormholePower.POWER_ID);
            if (wormhole.amount <= info.base){
                addToBot(new RemoveSpecificPowerAction(this, this, WormholePower.POWER_ID));
                this.updatePowers();

                addToBot(new SetMoveAction(this, RULER_OF_FATE, (byte)1, Intent.DEBUFF));
                this.createIntent();
                addToBot(new TextAboveCreatureAction(this, TextAboveCreatureAction.TextType.INTERRUPTED));
            } else {
                wormhole.amount -= info.base;
                wormhole.updateDescription();
                this.updatePowers();
            }
        }

        if (!this.isDying && this.currentHealth * 2 <= this.maxHealth && this.phase == 1) {
            addToBot(new SetMoveAction(this, UNCHAIN, (byte)5, Intent.UNKNOWN));
            this.createIntent();
            addToBot(new TextAboveCreatureAction(this, TextAboveCreatureAction.TextType.INTERRUPTED));
        }

    }

    protected void phase2Transition(){
        this.phase += 1;
        this.state.setTimeScale(1.0F);

        addToBot(new RunTopLevelEffectAction(new SimplePlayVideoEffect(videoPath("grandorder/peacemaker.webm"))));

        this.state.setAnimation(0, "idle2", true);
        this.addToBot(new ApplyPowerAction(this, this, new WormholePower(this, this.rulerOfFateThreshold)));

        // TODO Test BGM Transition
        AbstractDungeon.getCurrRoom().playBgmInstantly(this.phase2Song);
        if (this.isDMCA) {
            CardCrawlGame.music.precacheTempBgm(this.phase3Song);
        }
    }

    protected void useFieldOfMeditation(){
        addToBot(new ShoutAction(this, BATTLE_START_DIALOG, 1.0F, 2.0F));
        addToBot(new SFXAction(Sounds.GO_BATTLE_START_DIALOG));

        addToBot(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.BLUE_TEXT_COLOR, ShockWaveEffect.ShockWaveType.NORMAL), 0.3F));


        addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FieldOfMediation(AbstractDungeon.player)));
    }

    protected void useSpinningSlash(){

        addToBot(new ShoutAction(this, SPINNING_SLASH_DIALOG, 1.0F, 2.0F));
        addToBot(new SFXAction(Sounds.GO_SPINNING_SLASH_DIALOG));

        for (int i = 0; i < this.spinningSlashHits; i++){
            this.state.setAnimation(0, "attack1", false);
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(SPINNING_SLASH_INDEX), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            this.state.addAnimation(0, "idle1", true, 0.0F);
        }
    }

    protected void usePrismHalo(){
        addToBot(new ShoutAction(this, PRISM_HALO1_DIALOG, 1.0F, 2.0F));
        addToBot(new SFXAction(Sounds.GO_PRISM_HALO1_DIALOG));

        if (this.phase == 1){
            this.state.setAnimation(0, "halo1", false);
            this.addToBot(new GainBlockAction(this, prismHaloBlock));
            this.state.addAnimation(0, "idle1", true, 0.0F);
        } else {
            this.state.setAnimation(0, "halo2", false);
            this.addToBot(new GainBlockAction(this, prismHaloBlock));
            this.state.addAnimation(0, "idle2", true, 0.0F);
        }
    }

    protected void useBisection(){
        addToBot(new ShoutAction(this, BISECTION1_DIALOG, 1.0F, 2.0F));
        addToBot(new SFXAction(Sounds.GO_BISECTION1_DIALOG));

        this.state.setAnimation(0, "attack1", false);
        this.addToBot(new SFXAction("ATTACK_HEAVY"));
        this.addToBot(new VFXAction(this, new CleaveEffect(true), 0.1F));

        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(BISECTION_INDEX), AbstractGameAction.AttackEffect.NONE));

        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -this.bisectionStacks), -this.bisectionStacks));

        this.state.addAnimation(0, "idle1", true, 0.0F);
    }

    protected void useOpposition(){
        float vfxSpeed = 0.1F;
        if (Settings.FAST_MODE) {
            vfxSpeed = 0.0F;
        }
        addToBot(new ShoutAction(this, OPPOSITION_DIALOG, 1.0F, 2.0F));
        addToBot(new SFXAction(Sounds.GO_OPPOSITION_DIALOG));

        this.state.setAnimation(0, "attack2", false);
        addToBot(new VFXAction(new GoldenSlashEffect(AbstractDungeon.player.hb.cX * Settings.scale, AbstractDungeon.player.hb.cY, true), vfxSpeed));
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(OPPOSITION_INDEX), AbstractGameAction.AttackEffect.LIGHTNING));
        this.state.addAnimation(0, "idle2", true, 0.0F);
    }

    protected void useConjunction(){

        this.state.setAnimation(0, "conjunction", false);

        addToBot(new ShoutAction(this, CONJUNCTION_DIALOG, 1.0F, 2.0F));
        addToBot(new SFXAction(Sounds.GO_CONJUNCTION_DIALOG));

        while (AbstractDungeon.player.currentHealth < 1){
            addToBot(new LoseHPAction(AbstractDungeon.player, this, 1));
        }

        this.state.addAnimation(0, "idle2", true, 0.0F);
    }

    protected void useRulerOfFate(){

        addToBot(new ShoutAction(this, RULER_OF_FATE_DIALOG, 1.0F, 2.0F));
        addToBot(new SFXAction(Sounds.GO_RULER_OF_FATE_DIALOG));


        this.state.setAnimation(0, "attack3", false);
        this.useRulerOfFateDebuff();
        this.rulerOfFateStacks++;
        this.state.addAnimation(0, "idle2", true, 0.0F);

        this.rulerOfFateThreshold += this.rulerOfFateThresholdIncrease;
        this.addToBot(new ApplyPowerAction(this, this, new WormholePower(this, this.rulerOfFateThreshold)));
    }

    protected void useRulerOfFateDebuff(){
        AbstractPlayer p = AbstractDungeon.player;
        /* Debuffs in order. Skips debuffs already on the player:
        1: Frail
        2: Weak
        3: Vulnerable
        4: Neg Strength
        */

        if (!p.hasPower(FrailPower.POWER_ID)){
            addToBot(new ApplyPowerAction(p, this, new FrailPower(p, rulerOfFateStacks, true)));
        } else if (!p.hasPower(WeakPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(p, this, new WeakPower(p, rulerOfFateStacks, true)));
        } else if (!p.hasPower(VulnerablePower.POWER_ID)){
            addToBot(new ApplyPowerAction(p, this, new VulnerablePower(p, rulerOfFateStacks, true)));
        } else {
            this.addToBot(new ApplyPowerAction(p, this, new StrengthPower(p, -1), -1));
        }
    }

    @Override
    public void die() {
        if (ConfigMenu.enableExtraRewards){
            RewardItem reward1 = new RewardItem(new CosmicSword());

            RewardItem reward2 = new RewardItem(reward1, RewardItem.RewardType.RELIC);
            reward2.relic = new ShieldOfLamentation();

            AbstractDungeon.getCurrRoom().rewards.add(reward1);
            AbstractDungeon.getCurrRoom().rewards.add(reward2);
        }
        Act1Skies.resumeMainMusic();
        super.die();
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(MONSTER_ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;

        SPINNING_SLASH = MOVES[0];
        BISECTION = MOVES[1];
        PRISM_HALO = MOVES[2];
        OPPOSITION = MOVES[3];
        CONJUNCTION = MOVES[4];
        RULER_OF_FATE = MOVES[5];
        GAMMA_RAY = MOVES[6];
        LAST_WISH = MOVES[7];
        OUTBURST = MOVES[8];
        BATTLE_START = MOVES[9];
        UNCHAIN = MOVES[10];

        BATTLE_START_DIALOG = DIALOG[0];
        SPINNING_SLASH_DIALOG = DIALOG[1];
        BISECTION1_DIALOG = DIALOG[2];
        PRISM_HALO1_DIALOG = DIALOG[3];
        OVERDRIVE1_DIALOG = DIALOG[4];
        BREAK1_DIALOG = DIALOG[5];
        PHASE2_DIALOG = DIALOG[6];
        PHASE3_DIALOG = DIALOG[7];

        OPPOSITION_DIALOG = DIALOG[8];
        CONJUNCTION_DIALOG = DIALOG[9];
        RULER_OF_FATE_DIALOG = DIALOG[10];
        OVERDRIVE2_DIALOG = DIALOG[11];
        BREAK2_DIALOG = DIALOG[12];
        DEFEAT1_DIALOG = DIALOG[13];
        PHASE4_DIALOG = DIALOG[14];

        GAMMA_RAY_DIALOG = DIALOG[15];
        LAST_WISH_DIALOG = DIALOG[16];
        OUTBURST_DIALOG = DIALOG[17];
        DEFEAT2_DIALOG = DIALOG[18];
        BISECTION2_DIALOG = DIALOG[19];
        PRISM_HALO2_DIALOG = DIALOG[20];
    }
}
