package granbluebosses.monsters.act1.normal;

import basemod.BaseMod;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.red.Bludgeon;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.HeartBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import granbluebosses.GranblueBosses;
import granbluebosses.acts.Act1Skies;
import granbluebosses.cards.rewards.ColossusOmega;
import granbluebosses.cards.rewards.LeviathanOmega;
import granbluebosses.config.ConfigMenu;
import granbluebosses.events.CelesteEvent;
import granbluebosses.events.LeviathanEvent;
import granbluebosses.powers.StanceOmen;
import granbluebosses.powers.a_monsters.DebuffOnHit;
import granbluebosses.powers.a_monsters.EbbTidePower;
import granbluebosses.util.Sounds;

import java.util.ArrayList;

import static granbluebosses.GranblueBosses.makeID;

public class Leviathan2 extends CustomMonster {
    protected static final String MONSTER_NAME = "Leviathan";
    public static final String MONSTER_ID = makeID("Leviathan2");
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
    protected boolean firstTurn = true;
    protected static final MonsterStrings monsterStrings;
    public static final String CASCADE;
    public static final String AZURE_BLADE;
    public static final String PERILOUS_TIDEFALL;
    protected int azureBladeDmg = 4;
    protected int azureBladeHits = 2;
    protected int cascadeStacks = 2;
    protected int perilousTidefallDmg = 6;
    protected int perilousTidefallHits = 2;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public static final int AZURE_BLADE_INDEX = 0;
    public static final int PERILOUS_TIDEFALL_INDEX = 1;

    public Leviathan2() {
        super(MONSTER_NAME, MONSTER_ID, MONSTER_MAX_HP, MONSTER_HIT_BOX_X, MONSTER_HIT_BOX_Y, MONSTER_HIT_BOX_WIDTH, MONSTER_HIT_BOX_HEIGHT, MONSTER_IMG_URL, MONSTER_OFF_SET_X, MONSTER_OFF_SET_Y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(MONSTER_MAX_HP_A_19);
        } else {
            this.setHp(MONSTER_MAX_HP);
        }
        if (AbstractDungeon.ascensionLevel >= 2){
            this.azureBladeDmg += 1;
        }
        if (AbstractDungeon.ascensionLevel >= 17){
            this.cascadeStacks *= 1.5f;
            this.perilousTidefallDmg += 3;
        } else {

        }

        this.damage.add(new DamageInfo(this, azureBladeDmg, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this, perilousTidefallDmg, DamageInfo.DamageType.NORMAL));

        this.loadAnimation(GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".atlas"), GranblueBosses.monsterPath(MONSTER_ANIM_URL + "/" + MONSTER_ANIM_URL + ".json"), 1.0F);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();

        if (ConfigMenu.enableDMCAMusic){
            AbstractDungeon.getCurrRoom().playBgmInstantly(Sounds.MUSIC_ACT1_BATTLE);
        }

        if (AbstractDungeon.ascensionLevel >= 17){
            addToBot(new ApplyPowerAction(this, this, new DebuffOnHit(this, DebuffOnHit.AvailableDebuffs.WEAK, 2)));
        }
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0:
                this.useCascade();
                break;
            case 1:
                this.useAzureBlade();
                break;
            case 2:
                this.usePerilousTidefall();
                break;
        }
        this.prepareIntent();
    }

    public void useCascade(){
        addToBot(new AnimateSlowAttackAction(this));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, cascadeStacks, true)));
    }

    public void useAzureBlade(){
        for (int i = 0; i*2 < this.azureBladeHits; i++){
            addToBot(new AnimateSlowAttackAction(this));
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(AZURE_BLADE_INDEX), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(AZURE_BLADE_INDEX), AbstractGameAction.AttackEffect.POISON));
        }

        if (this.currentHealth * 4 <= this.maxHealth){
            addToBot(new ApplyPowerAction(this, this, new EbbTidePower(this)));
        }
    }

    public void usePerilousTidefall(){
        addToBot(new RemoveSpecificPowerAction(this, this, EbbTidePower.POWER_ID));

        for (int i = 0; i*2 < this.perilousTidefallHits; i++){

            addToBot(new AnimateSlowAttackAction(this));

            this.addToBot(new VFXAction(new WeightyImpactEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY)));
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(PERILOUS_TIDEFALL_INDEX), AbstractGameAction.AttackEffect.NONE));
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(PERILOUS_TIDEFALL_INDEX), AbstractGameAction.AttackEffect.POISON));
        }
    }

    protected void prepareIntent() {
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.prepareIntentA17();
            return;
        }
        switch (this.nextMove) {
            case 0:
                addToBot(new SetMoveAction(this, AZURE_BLADE, (byte)1, Intent.ATTACK, this.azureBladeDmg, this.azureBladeHits, true));
                break;
            case 1:
                addToBot(new SetMoveAction(this, CASCADE, (byte)0, Intent.DEBUFF));
                break;
            case 2:
                addToBot(new SetMoveAction(this, CASCADE, (byte)0, Intent.DEBUFF));
                break;
        }
    }

    protected void prepareIntentA17() {
        if (AbstractDungeon.aiRng.randomBoolean()){
            addToBot(new SetMoveAction(this, CASCADE, (byte)0, Intent.DEBUFF));
        } else {
            addToBot(new SetMoveAction(this, AZURE_BLADE, (byte)1, Intent.ATTACK, this.azureBladeDmg, this.azureBladeHits, true));
        }
    }

    public void preparePerilousTidefall(){
        if (this.hasPower(EbbTidePower.POWER_ID)) {
            this.updatePowers();

            addToBot(new SetMoveAction(this, PERILOUS_TIDEFALL, (byte)2, Intent.ATTACK, this.perilousTidefallDmg, this.perilousTidefallHits, true));
            this.createIntent();
            addToBot(new TextAboveCreatureAction(this, "DANGER!"));

        }
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            if (AbstractDungeon.aiRng.randomBoolean()){
                this.setMove(CASCADE, (byte)0, Intent.DEBUFF);
            } else {
                this.setMove(AZURE_BLADE, (byte)1, Intent.ATTACK, this.azureBladeDmg, this.azureBladeHits, true);
            }
        }
    }

    @Override
    public void die() {
        if (ConfigMenu.enableExtraRewards) {
            RewardItem reward = new RewardItem(AbstractCard.CardColor.COLORLESS);
            reward.cards = new ArrayList<>();
            reward.cards.add(new LeviathanOmega());

            for (AbstractCard c : reward.cards) {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    r.onPreviewObtainCard(c);
                }
            }
            AbstractDungeon.getCurrRoom().rewards.add(reward);
        }

        Act1Skies.resumeMainMusic();
        BaseMod.addEvent(LeviathanEvent.EVENT_ID, LeviathanEvent.class, Act1Skies.ID);

        super.die();
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(MONSTER_ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
        CASCADE = MOVES[0];
        AZURE_BLADE = MOVES[1];
        PERILOUS_TIDEFALL = MOVES[2];
    }
}
