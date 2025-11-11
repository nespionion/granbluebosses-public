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
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.EmpowerEffect;
import com.megacrit.cardcrawl.vfx.combat.EntangleEffect;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;
import granbluebosses.GranblueBosses;
import granbluebosses.acts.Act1Skies;
import granbluebosses.cards.rewards.EuropaCall;
import granbluebosses.cards.rewards.TiamatOmega;
import granbluebosses.config.ConfigMenu;
import granbluebosses.powers.StanceOmen;
import granbluebosses.powers.a_monsters.PathOfDestruction;
import granbluebosses.util.Sounds;

import java.util.ArrayList;

import static granbluebosses.GranblueBosses.makeID;

public class Europa extends CustomMonster {
    protected static final String MONSTER_NAME = "Europa";
    public static final String MONSTER_ID = makeID("Europa");
    protected static final String MONSTER_ANIM_URL = MONSTER_NAME.toLowerCase();
    protected static final int MONSTER_MAX_HP = 90;
    protected static final int MONSTER_MAX_HP_A_19 = 90 + 5;
    protected static final float MONSTER_HIT_BOX_X = 0;
    protected static final float MONSTER_HIT_BOX_Y = -30.0F;
    protected static final float MONSTER_HIT_BOX_WIDTH = 400.0F;
    protected static final float MONSTER_HIT_BOX_HEIGHT = 350.0F;
    protected static final String MONSTER_IMG_URL = (String) null;
    protected static final float MONSTER_OFF_SET_X = 0.0F;
    protected static final float MONSTER_OFF_SET_Y = 28.0F;
    protected boolean trigger = true;
    protected final int OMEN_MULT = 4;
    protected boolean firstTurn = true;
    protected int floralPrisonStacks = 2;
    protected int manaBlastDmg = 5;
    protected int manaBlastHits = 3;
    protected int taurusBlightDmg = 5;
    protected int taurusBlightHits = 4;
    protected int taurusBlightStacks = 2;
    protected static final MonsterStrings monsterStrings;
    public static final String FLORAL_PRISON;
    public static final String MANA_BLAST;
    public static final String TAURUS_BLIGHT;
    public static final String ENTRY_DIALOG;
    public static final String TAURUS_DIALOG;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public static final int MANA_BLAST_INDEX = 0;
    public static final int TAURUS_BLIGHT_INDEX = 1;

    public Europa() {
        super(MONSTER_NAME, MONSTER_ID, MONSTER_MAX_HP, MONSTER_HIT_BOX_X, MONSTER_HIT_BOX_Y, MONSTER_HIT_BOX_WIDTH, MONSTER_HIT_BOX_HEIGHT, MONSTER_IMG_URL, MONSTER_OFF_SET_X, MONSTER_OFF_SET_Y);
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(MONSTER_MAX_HP_A_19);
        } else {
            this.setHp(MONSTER_MAX_HP);
        }
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.manaBlastDmg += 1;
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.manaBlastHits += 1;
        }
        if (AbstractDungeon.ascensionLevel < 18){
            this.taurusBlightDmg -= 1;
            this.taurusBlightHits -= 1;
        }

        this.loadAnimation(GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".atlas"), GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".json"), 1.0F);

        this.damage.add(new DamageInfo(this, this.manaBlastDmg, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, this.taurusBlightDmg, DamageInfo.DamageType.NORMAL));
    }

    @Override
    public void usePreBattleAction() {
        if (ConfigMenu.enableDMCAMusic){
            AbstractDungeon.getCurrRoom().playBgmInstantly(Sounds.MUSIC_ACT1_ELITE_EUROPA);
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
                this.useFloralPrison();
                break;
            case 1:
                this.useManaBlast();
                break;
            case 2:
                this.useTaurusBlight();
                break;
        }
        this.prepareIntent();
    }

    protected void useFloralPrison(){
        addToBot(new AnimateSlowAttackAction(this));

        addToBot(new MakeTempCardInDrawPileAction(new Dazed(), floralPrisonStacks, true, true, false));
    }

    protected void useManaBlast(){
        addToBot(new AnimateSlowAttackAction(this));

        for (int i = 0; i < this.manaBlastHits; i++){
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(MANA_BLAST_INDEX), AbstractGameAction.AttackEffect.LIGHTNING));
        }
    }

    protected void useTaurusBlight(){
        addToBot(new ShoutAction(this, TAURUS_DIALOG));
        addToBot(new SFXAction(Sounds.EUROPA_TAURUS_DIALOG));

        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(TAURUS_BLIGHT_INDEX), AbstractGameAction.AttackEffect.LIGHTNING));
        addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, this.taurusBlightStacks + 1)));
        addToBot(new ApplyPowerAction(this, this, new RegenerateMonsterPower(this, this.taurusBlightStacks)));
    }


    protected void prepareIntent() {
        if (this.currentHealth * this.OMEN_MULT <= this.maxHealth && this.trigger) {
            this.trigger = false;
            addToTop(new RemoveSpecificPowerAction(this, this, StanceOmen.POWER_ID));
            addToBot(new TextAboveCreatureAction(this, "DANGER!"));

            addToBot(new SetMoveAction(this, TAURUS_BLIGHT, (byte) 2, Intent.ATTACK_BUFF, this.taurusBlightDmg, this.taurusBlightHits, true));
            return;
        }
        switch (this.nextMove) {
            case 0:
                addToBot(new SetMoveAction(this, MANA_BLAST, (byte) 1, Intent.ATTACK, this.manaBlastDmg, this.manaBlastHits, true));
                break;
            case 1:
                addToBot(new SetMoveAction(this, FLORAL_PRISON, (byte) 0, Intent.DEBUFF));
                break;
            case 2:
                addToBot(new SetMoveAction(this, FLORAL_PRISON, (byte) 0, Intent.DEBUFF));
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            addToBot(new ShoutAction(this, ENTRY_DIALOG));
            addToBot(new SFXAction(Sounds.EUROPA_ENTRY_DIALOG));
            this.setMove(FLORAL_PRISON, (byte) 0, Intent.DEBUFF);
        }
    }

    @Override
    public void die() {
        if (ConfigMenu.enableExtraRewards) {
            RewardItem reward = new RewardItem(AbstractCard.CardColor.COLORLESS);
            reward.cards = new ArrayList<>();
            reward.cards.add(new EuropaCall());

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
        FLORAL_PRISON = MOVES[0];
        MANA_BLAST = MOVES[1];
        TAURUS_BLIGHT = MOVES[2];
        ENTRY_DIALOG = DIALOG[0];
        TAURUS_DIALOG = DIALOG[1];
    }
}