package granbluebosses.monsters.act1.elites;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import granbluebosses.GranblueBosses;
import granbluebosses.acts.Act1Skies;
import granbluebosses.cards.rewards.GrimnirCall;
import granbluebosses.config.ConfigMenu;
import granbluebosses.powers.StanceOmen;
import granbluebosses.util.Sounds;

import java.util.ArrayList;

import static granbluebosses.GranblueBosses.makeID;

public class Grimnir extends CustomMonster {
    protected static final String MONSTER_NAME = "Grimnir";
    public static final String MONSTER_ID = makeID("Grimnir");
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
    protected int divineWindDmg = 2;
    protected int divineWindHits = 3;
    protected int holyRayOfPurificationDmg = 48;
    protected boolean trigger = true;
    protected final int OMEN_MULT = 4;
    protected boolean firstTurn = true;

    protected static final MonsterStrings monsterStrings;
    public static final String RAMBLING;
    public static final String DIVINE_WIND;
    public static final String HOLY_RAY_OF_PURIFICATION;
    public static final String RUN_AWAY;
    public static final String ENTRY_DIALOG;
    public static final String HOLY_RAY_DIALOG;
    public static final String RUN_AWAY_DIALOG;
    public static final String RAMBLING_DIALOG1;
    public static final String RAMBLING_DIALOG2;
    public static final String RAMBLING_DIALOG3;
    public static final String RAMBLING_DIALOG4;
    public static final String RAMBLING_DIALOG5;
    public static String[] RAMBLING_DIALOG;
    public static String[] RAMBLING_AUDIO;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;


    public static final int DIVINE_WIND_INDEX = 0;
    public static final int HOLY_RAY_OF_PURIFICATION_INDEX = 1;

    public Grimnir() {
        super(MONSTER_NAME, MONSTER_ID, MONSTER_MAX_HP, MONSTER_HIT_BOX_X, MONSTER_HIT_BOX_Y, MONSTER_HIT_BOX_WIDTH, MONSTER_HIT_BOX_HEIGHT, MONSTER_IMG_URL, MONSTER_OFF_SET_X, MONSTER_OFF_SET_Y);
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(MONSTER_MAX_HP_A_19);
        } else {
            this.setHp(MONSTER_MAX_HP);
        }
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.divineWindDmg += 2;
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            divineWindHits += 1;
        }

        if (AbstractDungeon.ascensionLevel < 18) {
            this.holyRayOfPurificationDmg = holyRayOfPurificationDmg / 2;
        }

        this.loadAnimation(GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".atlas"), GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".json"), 1.0F);

        this.damage.add(new DamageInfo(this, divineWindDmg, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, holyRayOfPurificationDmg, DamageInfo.DamageType.NORMAL));

    }

    @Override
    public void usePreBattleAction() {
        if (ConfigMenu.enableDMCAMusic){
            AbstractDungeon.getCurrRoom().playBgmInstantly(Sounds.MUSIC_ACT1_ELITE_GRIMNIR);
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
                this.useRambling();
                break;
            case 1:
                this.useDivineWind();
                break;
            case 2:
                this.useHolyRayOfPurification();
                break;
            case 3:
                this.useRunAway();
                break;
        }
        this.prepareIntent();
    }

    protected void useRambling(){
        int index = AbstractDungeon.aiRng.random(4);

        addToBot(new SFXAction(RAMBLING_AUDIO[index]));

        addToBot(new ShoutAction(this, RAMBLING_DIALOG[index]));
    }

    protected void useDivineWind(){
        
        this.addToBot(new SFXAction("ATTACK_WHIRLWIND"));
        addToBot(new VFXAction(new WhirlwindEffect(), 0.0F));

        for (int i = 0; i < divineWindHits; i++){
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(DIVINE_WIND_INDEX), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }

        addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -1)));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -1)));
    }

    protected void useHolyRayOfPurification(){
        this.addToBot(new SFXAction("ATTACK_HEAVY"));
        addToBot(new ShoutAction(this, HOLY_RAY_DIALOG));
        addToBot(new SFXAction(Sounds.GRIMNIR_HOLY_RAY_DIALOG));

        this.addToBot(new VFXAction(this, new MindblastEffect(this.dialogX, this.dialogY, this.flipHorizontal), 0.1F));
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(HOLY_RAY_OF_PURIFICATION_INDEX), AbstractGameAction.AttackEffect.FIRE));
    }
    
    protected void useRunAway(){
        addToBot(new EscapeAction(this));
        
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (!this.isDying && this.currentHealth * 10 < this.maxHealth){
            addToBot(new SetMoveAction(this, (byte) 3, Intent.ESCAPE));
            this.createIntent();

            addToBot(new SFXAction(Sounds.GRIMNIR_RUN_AWAY_DIALOG));
            addToBot(new ShoutAction(this, RUN_AWAY_DIALOG));
            addToBot(new TextAboveCreatureAction(this, TextAboveCreatureAction.TextType.INTERRUPTED));
        }

    }

    protected void prepareIntent() {
        if (this.currentHealth * this.OMEN_MULT <= this.maxHealth && this.trigger) {
            this.trigger = false;
            addToTop(new RemoveSpecificPowerAction(this, this, StanceOmen.POWER_ID));
            addToBot(new TextAboveCreatureAction(this, "DANGER!"));
            addToBot(new SetMoveAction(this, HOLY_RAY_OF_PURIFICATION, (byte) 2, Intent.ATTACK_BUFF, this.holyRayOfPurificationDmg, 1, false));
            return;
        }
        switch (this.nextMove) {
            case 0:
                addToBot(new SetMoveAction(this, DIVINE_WIND, (byte) 1, Intent.ATTACK, this.divineWindDmg, this.divineWindHits, true));
                break;
            case 1:
                addToBot(new SetMoveAction(this, RAMBLING, (byte) 0, Intent.UNKNOWN));
                break;
            case 2:
                addToBot(new SetMoveAction(this, RAMBLING, (byte) 0, Intent.UNKNOWN));
                break;
            case 3:
                addToBot(new SetMoveAction(this, RUN_AWAY, (byte) 3, Intent.ESCAPE));
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;

            addToBot(new ShoutAction(this, ENTRY_DIALOG));
            addToBot(new SFXAction(Sounds.GRIMNIR_ENTRY_DIALOG));
            this.setMove(RAMBLING, (byte) 0, Intent.BUFF);
        }
    }

    public void escape() {
        if (ConfigMenu.enableExtraRewards) {
            RewardItem reward = new RewardItem(AbstractCard.CardColor.COLORLESS);
            reward.cards = new ArrayList<>();
            reward.cards.add(new GrimnirCall());

            for (AbstractCard c : reward.cards) {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    r.onPreviewObtainCard(c);
                }
            }
            AbstractDungeon.getCurrRoom().rewards.add(reward);
        }
        Act1Skies.resumeMainMusic();
        super.escape();
    }

    @Override
    public void die() {
        if (ConfigMenu.enableExtraRewards) {
            RewardItem reward = new RewardItem(AbstractCard.CardColor.COLORLESS);
            reward.cards = new ArrayList<>();
            reward.cards.add(new GrimnirCall());

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
        RAMBLING = MOVES[0];
        DIVINE_WIND = MOVES[1];
        HOLY_RAY_OF_PURIFICATION = MOVES[2];
        RUN_AWAY = MOVES[3];
        ENTRY_DIALOG = DIALOG[0];
        HOLY_RAY_DIALOG = DIALOG[1];
        RUN_AWAY_DIALOG = DIALOG[2];
        RAMBLING_DIALOG1 = DIALOG[3];
        RAMBLING_DIALOG2 = DIALOG[4];
        RAMBLING_DIALOG3 = DIALOG[5];
        RAMBLING_DIALOG4 = DIALOG[6];
        RAMBLING_DIALOG5 = DIALOG[7];
        RAMBLING_AUDIO = new String[]{Sounds.GRIMNIR_RAMBLING_DIALOG1, Sounds.GRIMNIR_RAMBLING_DIALOG2, Sounds.GRIMNIR_RAMBLING_DIALOG3, Sounds.GRIMNIR_RAMBLING_DIALOG4, Sounds.GRIMNIR_RAMBLING_DIALOG5};
        RAMBLING_DIALOG = new String[]{RAMBLING_DIALOG1, RAMBLING_DIALOG2, RAMBLING_DIALOG3, RAMBLING_DIALOG4, RAMBLING_DIALOG5};
    }
}