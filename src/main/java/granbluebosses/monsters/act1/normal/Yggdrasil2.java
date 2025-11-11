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
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.HeartBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import granbluebosses.GranblueBosses;
import granbluebosses.acts.Act1Skies;
import granbluebosses.cards.rewards.TiamatOmega;
import granbluebosses.cards.rewards.YggdrasilOmega;
import granbluebosses.config.ConfigMenu;
import granbluebosses.events.CelesteEvent;
import granbluebosses.events.YggdrasilEvent;
import granbluebosses.powers.StanceOmen;
import granbluebosses.powers.a_monsters.DebuffOnHit;
import granbluebosses.util.Sounds;

import java.util.ArrayList;

import static granbluebosses.GranblueBosses.makeID;

public class Yggdrasil2 extends CustomMonster {
    protected static final String MONSTER_NAME = "Yggdrasil";
    public static final String MONSTER_ID = makeID("Yggdrasil2");
    protected static final int MONSTER_MAX_HP = 50;
    protected static final int MONSTER_MAX_HP_A_19 = 50 + 2;
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
    protected int axisMundiDmg = 10;
    protected int luminoxGenesiStacks = 2;
    protected int songOfGraceDmg = 3;
    protected int songOfGraceHits = 1;
    protected static final MonsterStrings monsterStrings;
    public static final String LUMINOX_GENESI;
    public static final String AXIS_MUNDI;
    public static final String SONG_OF_GRACE;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public static final int AXIS_MUNDI_INDEX = 0;
    public static final int SONG_OF_GRACE_INDEX = 1;

    public Yggdrasil2() {
        super(MONSTER_NAME, MONSTER_ID, MONSTER_MAX_HP, MONSTER_HIT_BOX_X, MONSTER_HIT_BOX_Y, MONSTER_HIT_BOX_WIDTH, MONSTER_HIT_BOX_HEIGHT, MONSTER_IMG_URL, MONSTER_OFF_SET_X, MONSTER_OFF_SET_Y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(MONSTER_MAX_HP_A_19);
        } else {
            this.setHp(MONSTER_MAX_HP);
        }
        if (AbstractDungeon.ascensionLevel >= 2){
            this.axisMundiDmg += 4;
        }

        if (AbstractDungeon.ascensionLevel < 17){
            this.songOfGraceDmg = 1;
        }

        if (ConfigMenu.modestyFilter){
            this.loadAnimation(GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + "Cen.atlas"), GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + "Cen.json"), 1.0F);
        } else {
            this.loadAnimation(GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".atlas"), GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".json"), 1.0F);

        }

        this.damage.add(new DamageInfo(this, axisMundiDmg, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, songOfGraceDmg, DamageInfo.DamageType.NORMAL));

    }

    @Override
    public void usePreBattleAction() {
        StanceOmen omen = new StanceOmen(this);
        omen.setUpOmen(OMEN_MULT);
        addToTop(new ApplyPowerAction(this, this, omen));
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
            case 2:
                this.useSongOfGraceHeal();
            case 3:
                this.useSongOfGraceHurt();
        }
        this.prepareIntent();
    }

    protected void useAxisMundi(){
        addToBot(new AnimateSlowAttackAction(this));
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(AXIS_MUNDI_INDEX), AbstractGameAction.AttackEffect.SMASH));

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

    protected void useSongOfGraceHurt(){
        for (int i = 0; i < songOfGraceHits; i++){
            addToBot(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.GREEN_TEXT_COLOR, ShockWaveEffect.ShockWaveType.NORMAL), 0.3F));
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(SONG_OF_GRACE_INDEX), AbstractGameAction.AttackEffect.NONE));
        }
    }

    protected void useSongOfGraceHeal(){
        for (int i = 0; i < songOfGraceHits; i++){
            addToBot(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.GREEN_TEXT_COLOR, ShockWaveEffect.ShockWaveType.NORMAL), 0.3F));
            addToBot(new GainBlockAction(AbstractDungeon.player, this.damage.get(SONG_OF_GRACE_INDEX).output));
        }
    }

    protected void prepareIntent() {
        if (this.currentHealth * this.OMEN_MULT <= this.maxHealth && trigger){
            this.prepareSongOfGrace();
            this.trigger = false;
            addToTop(new RemoveSpecificPowerAction(this, this, StanceOmen.POWER_ID));
            return;
        }
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
            case 2:
                addToBot(new SetMoveAction(this, LUMINOX_GENESI, (byte)0, Intent.DEBUFF));
                break;
            case 3:
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

    protected void prepareSongOfGrace(){
        AbstractPlayer p = AbstractDungeon.player;
        int buffCount = 0;
        int debuffCount = 0;

        for (AbstractPower pow : p.powers){
            if (pow.type == AbstractPower.PowerType.DEBUFF){
                debuffCount++;
            } else {
                buffCount++;
            }
        }

        if (buffCount > debuffCount){
            addToBot(new SetMoveAction(this, SONG_OF_GRACE, (byte)2, Intent.DEFEND));
        } else {
            this.songOfGraceHits = debuffCount - buffCount;
            addToBot(new SetMoveAction(this, SONG_OF_GRACE, (byte)3, Intent.ATTACK, this.songOfGraceDmg, this.songOfGraceHits, true));
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
        if (ConfigMenu.enableExtraRewards) {
            RewardItem reward = new RewardItem(AbstractCard.CardColor.COLORLESS);
            reward.cards = new ArrayList<>();
            reward.cards.add(new YggdrasilOmega());

            for (AbstractCard c : reward.cards) {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    r.onPreviewObtainCard(c);
                }
            }
            AbstractDungeon.getCurrRoom().rewards.add(reward);
        }

        Act1Skies.resumeMainMusic();
        BaseMod.addEvent(YggdrasilEvent.EVENT_ID, YggdrasilEvent.class, Act1Skies.ID);

        super.die();
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(MONSTER_ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
        LUMINOX_GENESI = MOVES[0];
        AXIS_MUNDI = MOVES[1];
        SONG_OF_GRACE = MOVES[2];
    }
}
