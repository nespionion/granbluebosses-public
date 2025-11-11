package granbluebosses.monsters.act1.bosses;

import VideoTheSpire.actions.RunTopLevelEffectAction;
import VideoTheSpire.effects.SimplePlayVideoEffect;
import basemod.abstracts.CustomMonster;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.EmpowerEffect;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;
import com.megacrit.cardcrawl.vfx.combat.OmegaFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import granbluebosses.GranblueBosses;
import granbluebosses.acts.Act1Skies;
import granbluebosses.cards.protobaha.optionCards.DaggerOfBahamut;
import granbluebosses.cards.protobaha.optionCards.HarpOfBahamut;
import granbluebosses.cards.protobaha.optionCards.StaffOfBahamut;
import granbluebosses.cards.protobaha.optionCards.SwordOfBahamut;
import granbluebosses.config.ConfigMenu;
import granbluebosses.powers.OverdriveState;
import granbluebosses.powers.StanceOmen;
import granbluebosses.powers.StandbyState;
import granbluebosses.powers.protobaha.RagnarokField;
import granbluebosses.util.Sounds;

import java.util.ArrayList;

import static granbluebosses.GranblueBosses.*;

public class ProtoBaha extends CustomMonster {
    protected static final String MONSTER_NAME = "Proto Bahamut";
    public static final String MONSTER_ID = makeID("ProtoBaha");
    public static final String MAP_ICON = monsterPath("protobaha/protobaha_map_icon.png");
    public static final String OUTLINE = monsterPath("protobaha/protobaha_outline.png");
    protected static final int MONSTER_MAX_HP = 240;
    protected static final int MONSTER_MAX_HP_A_19 = 300;
    protected static final float MONSTER_HIT_BOX_X = 0;
    protected static final float MONSTER_HIT_BOX_Y = -30.0F;
    protected static final float MONSTER_HIT_BOX_WIDTH = 400.0F;
    protected static final float MONSTER_HIT_BOX_HEIGHT = 350.0F;
    protected static final String MONSTER_IMG_URL = (String)null;
    protected static final String MONSTER_ANIM_URL = MONSTER_NAME.toLowerCase();
    protected static final float MONSTER_OFF_SET_X = 0.0F;
    protected static final float MONSTER_OFF_SET_Y = 28.0F;
    protected boolean firstTurn = true;
    protected static final String RAG_FIELD;
    protected static final String SKYFALL;
    protected static final String REGINLEIV;
    protected static final String ARCADIA;
    protected static final String ABDAK_FORCE;
    protected static final String SUPERNOVA;
    protected static final String REGINLEIV_RECIDIVE;
    protected static final String ARCADIA_KHLORON;
    protected static final String UNCHAIN;
    protected static final MonsterStrings monsterStrings;
    public static final String NAME;
    protected int ragFieldAmount;
    protected int skyfallDmg;
    protected int reginleivDmg;
    protected int reginleivHits;
    protected int reginleivRecidiveDmg;
    protected int reginleivRecidiveHits;
    protected int supernovaDmg;
    protected boolean isBeforePhase2Transition = true;
    protected final int OMEN_MULT1 = 4;
    protected final int OMEN_MULT2 = 20;
    protected boolean skyfallTrigger25 = true;
    protected boolean skyfallTrigger5 = true;
    protected boolean isHL = false;
    protected boolean isDMCA = false;
    private String phase1Song = "BOSS_BOTTOM";
    private String phase2Song = "BOSS_CITY";

    protected static final int SKYFALL_INDEX = 0;
    protected static final int REGINLEIV_INDEX = 1;
    protected static final int SUPERNOVA_INDEX = 2;
    protected static final int REGINLEIV_RECIDIVE_INDEX = 3;


    public ProtoBaha(){
        super(MONSTER_NAME, MONSTER_ID, MONSTER_MAX_HP, MONSTER_HIT_BOX_X, MONSTER_HIT_BOX_Y, MONSTER_HIT_BOX_WIDTH, MONSTER_HIT_BOX_HEIGHT, MONSTER_IMG_URL, MONSTER_OFF_SET_X, MONSTER_OFF_SET_Y);
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(MONSTER_MAX_HP_A_19);

        } else {
            this.setHp(MONSTER_MAX_HP);;
        }


        this.ragFieldAmount = Integer.max(1, (AbstractDungeon.floorNum / 10) + (AbstractDungeon.ascensionLevel / 10));

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.skyfallDmg = 9999;
        } else {
            this.skyfallDmg = 99;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.reginleivDmg = 4;
        } else {
            this.reginleivDmg = 3;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.reginleivHits = 4;
        } else {
            this.reginleivHits = 4;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.supernovaDmg = 22;
        } else {
            this.supernovaDmg = 20;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.reginleivRecidiveDmg = this.reginleivDmg + 0;
        } else {
            this.reginleivRecidiveDmg = this.reginleivDmg + 0;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.reginleivRecidiveHits = this.reginleivHits + 2;
        } else {
            this.reginleivRecidiveHits = this.reginleivHits + 2;
        }


        this.isDMCA = ConfigMenu.enableDMCAMusic;
        if (this.isDMCA){
            this.phase1Song = Sounds.MUSIC_ACT1_PROTOBAHA1;
            this.phase2Song = Sounds.MUSIC_ACT1_PROTOBAHA2;
        }

        this.damage.add(new DamageInfo(this, this.skyfallDmg));
        this.damage.add(new DamageInfo(this, this.reginleivDmg));
        this.damage.add(new DamageInfo(this, this.supernovaDmg));
        this.damage.add(new DamageInfo(this, this.reginleivDmg));

        this.loadAnimation(GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".atlas"), GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".json"), 1.0F);
        AnimationState.TrackEntry idle1 = this.state.setAnimation(0, "idle1", true);
    }

    public ProtoBaha(int initialHP){
        this();
        this.maxHealth = initialHP;
        this.currentHealth = initialHP;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(initialHP);

        } else {
            this.setHp(initialHP);;
        }
    }

    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().playBgmInstantly(this.phase1Song);

        StanceOmen omen = new StanceOmen(this);
        omen.setUpOmen(OMEN_MULT1);
        addToTop(new ApplyPowerAction(this, this, omen));
        super.usePreBattleAction();

        StandbyState standbyState = new StandbyState(this, 5);
        standbyState.canLowerAmount = true;
        addToBot(new ApplyPowerAction(this, this, standbyState));


    }

    @Override
    public void takeTurn() {
        if (isBeforePhase2Transition){
            takeTurnPhase1();
        } else {
            takeTurnPhase2();
        }
    }

    private void takeTurnPhase1() {

        switch (this.nextMove) {
            case 1:
                this.useReginleiv();
                break;
            case 2:
                this.useArcadia();
                break;
            case 3:
                this.useReginleiv();
                break;
            case 4:
                this.useAbdakForce();
                break;
            case 5:
                this.phase2Transition();
                break;
            case 6:
                this.stunTurn();
                break;
            default:
                this.useRagField();
                break;
        }
        this.prepareIntent();
    }

    private void takeTurnPhase2() {

        switch (this.nextMove) {
            case 1:
                this.useSuperNova();
                break;
            case 2:
                this.useArcadiaKhloron();
                break;
            case 3:
                this.useReginleivRecidive();
                break;
            case 4:
                this.useArcadiaKhloron();
                break;
            case 5:
                this.useSkyfall();
                break;
            case 6:
                this.stunTurn();
                break;
            default:
                this.useRagField();
                break;
        }
        this.prepareIntent();
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            this.setMove(RAG_FIELD, (byte)0, Intent.STRONG_DEBUFF);
        }
    }

    protected void prepareIntent(){
        if (isBeforePhase2Transition){
            this.prepareIntentPhase1();
        }
        else {
            this.prepareIntentPhase2();
        }
    }

    protected void prepareIntentPhase1(){
        switch (this.nextMove) {
            case 1:
                addToBot(new SetMoveAction(this, ARCADIA, (byte)2, Intent.STRONG_DEBUFF));
                break;
            case 2:
                addToBot(new SetMoveAction(this, REGINLEIV, (byte)3, Intent.ATTACK, reginleivDmg, reginleivHits + (2 * this.inOverdrive()), true));
                break;
            case 3:
                addToBot(new SetMoveAction(this, ABDAK_FORCE, (byte)4, Intent.BUFF));
                break;
            case 4:
                addToBot(new SetMoveAction(this, REGINLEIV, (byte)1, Intent.ATTACK, reginleivDmg, reginleivHits + (2 * this.inOverdrive()), true));
                break;
            case 5:
                addToBot(new SetMoveAction(this, REGINLEIV, (byte)1, Intent.ATTACK, reginleivDmg, reginleivHits + (2 * this.inOverdrive()), true));
                break;
            case 6:
                addToBot(new SetMoveAction(this, REGINLEIV, (byte)1, Intent.ATTACK, reginleivDmg, reginleivHits + (2 * this.inOverdrive()), true));
                break;
            default:
                addToBot(new SetMoveAction(this, REGINLEIV, (byte)1, Intent.ATTACK, reginleivDmg, reginleivHits + (2 * this.inOverdrive()), true));
                break;
        }
    }

    protected void prepareIntentPhase2(){
        if (this.maxHealth >= this.currentHealth * this.OMEN_MULT1 && skyfallTrigger25){
            this.skyfallTrigger25 = false;
            ((StanceOmen) this.getPower(StanceOmen.POWER_ID)).setUpOmen(OMEN_MULT2);

            addToBot(new TextAboveCreatureAction(this, "DANGER!"));
            addToBot(new SetMoveAction(this, SKYFALL, (byte)5, Intent.ATTACK, skyfallDmg, 1, false));
            return;
        }
        if (this.maxHealth >= this.currentHealth * this.OMEN_MULT2 && skyfallTrigger5){
            this.skyfallTrigger5 = false;
            addToBot(new TextAboveCreatureAction(this, "DANGER!"));
            addToBot(new SetMoveAction(this, SKYFALL, (byte)5, Intent.ATTACK, skyfallDmg, 1, false));
            return;
        }
        switch (this.nextMove) {
            case 1:
                addToBot(new SetMoveAction(this, ARCADIA_KHLORON, (byte)2, Intent.STRONG_DEBUFF));
                break;
            case 2:
                addToBot(new SetMoveAction(this, REGINLEIV_RECIDIVE, (byte)3, Intent.ATTACK, reginleivRecidiveDmg + (2 * this.inOverdrive()), reginleivRecidiveDmg, true));
                break;
            case 3:
                addToBot(new SetMoveAction(this, ARCADIA_KHLORON, (byte)4, Intent.STRONG_DEBUFF));
                break;
            case 4:
                addToBot(new SetMoveAction(this, SUPERNOVA, (byte)1, Intent.ATTACK, supernovaDmg + (3 * this.inOverdrive()), 1, false));
                break;
            case 5:
                addToBot(new SetMoveAction(this, ARCADIA_KHLORON, (byte)2, Intent.STRONG_DEBUFF));
                break;
            case 6:
                addToBot(new SetMoveAction(this, ARCADIA_KHLORON, (byte)2, Intent.STRONG_DEBUFF));
                break;
            default:
                addToBot(new SetMoveAction(this, ARCADIA_KHLORON, (byte)2, Intent.STRONG_DEBUFF));
                break;
        }
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (!this.isDying && this.hasPower(StandbyState.POWER_ID)){
            StandbyState tempPower = (StandbyState) this.getPower(StandbyState.POWER_ID);
            tempPower.lowerAmount(info.base);
        } else if (!this.isDying && this.hasPower(OverdriveState.POWER_ID)){
            OverdriveState tempPower = (OverdriveState) this.getPower(OverdriveState.POWER_ID);

            if (tempPower.amount <= info.base) {
                addToBot(new TextAboveCreatureAction(this, TextAboveCreatureAction.TextType.INTERRUPTED));
                addToBot(new SetMoveAction(this, (byte)6, Intent.STUN));
                this.createIntent();
            }
            tempPower.lowerAmount(info.base);
        }

        if (!this.isDying && this.currentHealth * 2 <= this.maxHealth && this.isBeforePhase2Transition) {
            addToBot(new TextAboveCreatureAction(this, TextAboveCreatureAction.TextType.INTERRUPTED));
            addToBot(new SetMoveAction(this, UNCHAIN, (byte)5, Intent.UNKNOWN));
            this.createIntent();
        }



    }

    protected void phase2Transition(){
        this.isBeforePhase2Transition = false;
        this.state.setTimeScale(1.0F);

        AnimationState.TrackEntry death = this.state.setAnimation(0, "death", false);
        AnimationState.TrackEntry idle2 = this.state.addAnimation(0, "idle2", true, 0.0F);

        CardCrawlGame.music.fadeAll();

        AbstractDungeon.scene.fadeOutAmbiance();

        AbstractDungeon.getCurrRoom().playBgmInstantly(this.phase2Song);
        addToBot(new SFXAction(Sounds.PBHL_PHASE_TRANS));
    }

    protected void useRagField(){
//        addToBot(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.GREEN_TEXT_COLOR, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.3F));

        AnimationState.TrackEntry rag_field = this.state.setAnimation(0, "rag_field", false);
        AnimationState.TrackEntry idle1 = this.state.addAnimation(0, "idle1", true, 0.0f);

        addToBot(new SFXAction(Sounds.PBHL_RAG_FIELD));

        addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new RagnarokField(AbstractDungeon.player, this.ragFieldAmount)));
    }

    protected void useSkyfall(){

        addToBot(new RunTopLevelEffectAction(new SimplePlayVideoEffect(videoPath("protobaha/skyfall.webm"))));

        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(SKYFALL_INDEX)));
    }

    protected void useSuperNova(){
        addToBot(new VFXAction(new OmegaFlashEffect(this.hb.cX, this.hb.cY ), 0.3F));

        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(SUPERNOVA_INDEX)));

    }

    protected void useReginleiv(){

        addToBot(new VFXAction(new LaserBeamEffect(this.hb.cX, this.hb.cY + 60.0F * Settings.scale), 1.5F));

        if (this.hasPower(makeID(OverdriveState.class.getSimpleName()))){
            for (int i = 0; i < this.reginleivHits + (2 * this.inOverdrive()); i++){
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(REGINLEIV_INDEX)));
                addToBot(new SFXAction(Sounds.PBHL_REGINLEIV));
            }
        }
    }

    protected void useReginleivRecidive(){

        addToBot(new VFXAction(new LaserBeamEffect(this.hb.cX, this.hb.cY + 60.0F * Settings.scale), 1.5F));
        if (this.hasPower(makeID(OverdriveState.class.getSimpleName()))){
            for (int i = 0; i < this.reginleivRecidiveHits + 2; i++){
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(REGINLEIV_RECIDIVE_INDEX)));
                addToBot(new SFXAction(Sounds.PBHL_REGINLEIV));
            }
        }
    }

    protected void useArcadia(){
        addToBot(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.GREEN_TEXT_COLOR, ShockWaveEffect.ShockWaveType.NORMAL), 0.3F));
        addToBot(new SFXAction(Sounds.PBHL_ARCADIA));

        this.useArcadiaDebuff(1);
        if (this.hasPower(makeID(OverdriveState.class.getSimpleName()))){
            this.useArcadiaDebuff(1);
        }
        if (AbstractDungeon.ascensionLevel >= 19){
            this.useArcadiaDebuff(1);
        }
    }

    protected void useArcadiaKhloron(){
        addToBot(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.GREEN_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.3F));
        addToBot(new SFXAction(Sounds.PBHL_ARCADIA));

        this.useArcadiaDebuff(2);
        if (this.hasPower(OverdriveState.POWER_ID)){
            this.useArcadiaDebuff(2);
        }
        if (AbstractDungeon.ascensionLevel >= 19){
            this.useArcadiaDebuff(2);
        }
    }

    private void useArcadiaDebuff(int stacks){
        AbstractPlayer p = AbstractDungeon.player;
        /* Debuffs by random num:
        0: Weak
        1: Frail
        2: Vulnerable
        3: Draw Reduction
        4: Neg Strength
        */
        int debuffToInflict = AbstractDungeon.aiRng.random(3);
        switch (debuffToInflict){
            case 0:
                addToBot(new ApplyPowerAction(p, this, new WeakPower(p, stacks, true)));
                break;
            case 1:
                addToBot(new ApplyPowerAction(p, this, new FrailPower(p, stacks, true)));
                break;
            case 2:
                addToBot(new ApplyPowerAction(p, this, new VulnerablePower(p, stacks, true)));
                break;
            case 3:
                addToBot(new MakeTempCardInDiscardAction(new Dazed(), stacks));
                break;
            default:
                this.addToBot(new ApplyPowerAction(p, this, new StrengthPower(p, -stacks), -1));
                if (!p.hasPower("Artifact")) {
                    this.addToBot(new ApplyPowerAction(p, this, new GainStrengthPower(p, stacks), stacks));
                }
                break;
        }
    }

    protected void useAbdakForce(){
        this.addToBot(new VFXAction(this, new EmpowerEffect(this.hb.cX, this.hb.cY), 0.3F));
        addToBot(new SFXAction(Sounds.PBHL_ABDAK_FORCE));

        this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 2), 2));

        if (this.hasPower(makeID(OverdriveState.class.getSimpleName()))) {
            this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));
        }
    }

    protected void stunTurn(){
        if (this.hasPower(StandbyState.POWER_ID)){
            ((StandbyState) this.getPower(StandbyState.POWER_ID)).canLowerAmount = true;
        }
    }

    @Override
    public void die() {
        if (ConfigMenu.enableExtraRewards) {
            RewardItem reward = new RewardItem(AbstractCard.CardColor.COLORLESS);
            reward.cards = new ArrayList<>();
            reward.cards.add(new SwordOfBahamut());
            reward.cards.add(new DaggerOfBahamut());
            reward.cards.add(new StaffOfBahamut());
            reward.cards.add(new HarpOfBahamut());

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

    public int inOverdrive(){
        int inOverdrive;
        if (this.hasPower(OverdriveState.POWER_ID)){
            inOverdrive = 1;
        } else {
            inOverdrive = 0;
        }
        return inOverdrive;
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(MONSTER_ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
        RAG_FIELD = MOVES[0];
        SKYFALL = MOVES[1];
        REGINLEIV = MOVES[2];
        ARCADIA = MOVES[3];
        ABDAK_FORCE = MOVES[4];
        SUPERNOVA = MOVES[5];
        REGINLEIV_RECIDIVE = MOVES[6];
        ARCADIA_KHLORON = MOVES[7];
        UNCHAIN = MOVES[8];
    }
}
