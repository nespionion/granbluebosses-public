package granbluebosses.monsters.act1.elites;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import granbluebosses.GranblueBosses;
import granbluebosses.acts.Act1Skies;
import granbluebosses.cards.rewards.AlexielCall;
import granbluebosses.cards.rewards.TiamatOmega;
import granbluebosses.config.ConfigMenu;
import granbluebosses.powers.Marked;
import granbluebosses.powers.a_monsters.MirrorBlade2;
import granbluebosses.powers.a_monsters.MirrorBlade5;
import granbluebosses.util.Sounds;

import java.util.ArrayList;

import static granbluebosses.GranblueBosses.makeID;

public class Alexiel extends CustomMonster {
    protected static final String MONSTER_NAME = "Alexiel";
    public static final String MONSTER_ID = makeID("Alexiel");
    protected static final String MONSTER_ANIM_URL = MONSTER_NAME.toLowerCase();
    protected static final int MONSTER_MAX_HP = 80;
    protected static final int MONSTER_MAX_HP_A_19 = 80 + 5;
    protected static final float MONSTER_HIT_BOX_X = 0;
    protected static final float MONSTER_HIT_BOX_Y = -30.0F;
    protected static final float MONSTER_HIT_BOX_WIDTH = 400.0F;
    protected static final float MONSTER_HIT_BOX_HEIGHT = 350.0F;
    protected static final String MONSTER_IMG_URL = (String) null;
    protected static final float MONSTER_OFF_SET_X = 0.0F;
    protected static final float MONSTER_OFF_SET_Y = 28.0F;
    protected boolean trigger = true;
    protected boolean firstTurn = true;
    protected int uncrossableRealmBlock = 8;
    protected int mirrorBladeEruptionStacks = 2;
    protected int mirrorBladeHelixDmg = 3;
    protected int mirrorBladeHelixHits = 5;
    protected static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String UNCROSSABLE_REALM;
    public static final String MIRROR_BLADE_ERUPTION;
    public static final String MIRROR_BLADE_HELIX;
    public static final String ENTRY_DIALOG;
    public static final String HELIX_DIALOG;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public Alexiel() {
        super(MONSTER_NAME, MONSTER_ID, MONSTER_MAX_HP, MONSTER_HIT_BOX_X, MONSTER_HIT_BOX_Y, MONSTER_HIT_BOX_WIDTH, MONSTER_HIT_BOX_HEIGHT, MONSTER_IMG_URL, MONSTER_OFF_SET_X, MONSTER_OFF_SET_Y);
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(MONSTER_MAX_HP_A_19);
            this.uncrossableRealmBlock += 2;
        } else {
            this.setHp(MONSTER_MAX_HP);
        }
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.mirrorBladeHelixDmg += 1;
        }
        if (AbstractDungeon.ascensionLevel < 18) {
            this.mirrorBladeHelixHits -= 1;
        }

        this.loadAnimation(GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".atlas"), GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".json"), 1.0F);

        this.damage.add(new DamageInfo(this, this.mirrorBladeHelixDmg, DamageInfo.DamageType.NORMAL));

    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (ConfigMenu.enableDMCAMusic){
            AbstractDungeon.getCurrRoom().playBgmInstantly(Sounds.MUSIC_ACT1_ELITE_ALEX);
        } else {
            AbstractDungeon.getCurrRoom().playBgmInstantly("ELITE");
        }
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0:
                this.useUncrossableRealm();
                break;
            case 1:
                this.useMirrorBladeEruption();
                break;
            case 2:
                this.useMirrorBladeHelix();
                break;
        }
        this.prepareIntent();
    }

    protected void gainMirrorBlde(){
        AbstractPower mirrorBladePower;
        if (AbstractDungeon.ascensionLevel >= 18) {
            mirrorBladePower = new MirrorBlade5(this);
        } else {
            mirrorBladePower = new MirrorBlade2(this);
        }
        addToBot(new ApplyPowerAction(AbstractDungeon.player, this, mirrorBladePower));
    }

    protected void useUncrossableRealm(){
        this.gainMirrorBlde();
        addToBot(new GainBlockAction(this, this.uncrossableRealmBlock));
    }

    protected void useMirrorBladeEruption(){
        addToBot(new AnimateSlowAttackAction(this));

        this.gainMirrorBlde();

        addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new Marked(AbstractDungeon.player)));
    }

    protected void useMirrorBladeHelix(){
        addToBot(new AnimateSlowAttackAction(this));

        addToBot(new ShoutAction(this, HELIX_DIALOG));
        addToBot(new SFXAction(Sounds.ALEXIEL_HELIX_DIALOG));

        for (int i = 0; i < this.mirrorBladeHelixHits; i++){
            this.addToBot(new SFXAction("ATTACK_WHIRLWIND"));
            this.addToBot(new VFXAction(new WhirlwindEffect(), 0.0F));

            this.addToBot(new SFXAction("ATTACK_HEAVY"));
            this.addToBot(new VFXAction(this, new CleaveEffect(true), 0.0F));
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
        }
    }


    protected void prepareIntent() {
        if (AbstractDungeon.player.hasPower(Marked.POWER_ID)){
            addToBot(new TextAboveCreatureAction(this, "DANGER!"));
            addToBot(new SetMoveAction(this, MIRROR_BLADE_HELIX, (byte) 2, Intent.ATTACK, this.mirrorBladeHelixDmg, this.mirrorBladeHelixHits, true));
            return;
        }
        switch (this.nextMove) {
            case 0:
                addToBot(new SetMoveAction(this, MIRROR_BLADE_ERUPTION, (byte) 1, Intent.DEBUFF));
                break;
            case 1:
                addToBot(new SetMoveAction(this, UNCROSSABLE_REALM, (byte) 0, Intent.DEFEND_BUFF));
                break;
            case 2:
                addToBot(new SetMoveAction(this, UNCROSSABLE_REALM, (byte) 0, Intent.DEFEND_BUFF));
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            addToBot(new ShoutAction(this, ENTRY_DIALOG));
            addToBot(new SFXAction(Sounds.ALEXIEL_ENTRY_DIALOG));
            this.setMove(UNCROSSABLE_REALM, (byte) 0, Intent.DEFEND_DEBUFF);
        }
    }

    @Override
    public void die() {
        if (ConfigMenu.enableExtraRewards) {

            RewardItem reward = new RewardItem(AbstractCard.CardColor.COLORLESS);
            reward.cards = new ArrayList<>();
            reward.cards.add(new AlexielCall());

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

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(MONSTER_ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
        UNCROSSABLE_REALM = MOVES[0];
        MIRROR_BLADE_ERUPTION = MOVES[1];
        MIRROR_BLADE_HELIX = MOVES[2];
        ENTRY_DIALOG = DIALOG[0];
        HELIX_DIALOG = DIALOG[1];
    }
}