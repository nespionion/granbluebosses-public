package granbluebosses.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BlueCandle;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.DeadBranch;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import granbluebosses.GranblueBosses;

public class FireEvent extends PhasedEvent {
    public final static String EVENT_ID = GranblueBosses.makeID("FireEvent");
    protected static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(EVENT_ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private final static String EVENT_IMAGE_URL = GranblueBosses.eventPath("act1/fire.jpg");
    private final int dmgAmt;
    private String result = "";

    public FireEvent() {
        super(EVENT_ID, NAME, EVENT_IMAGE_URL);
        if (AbstractDungeon.ascensionLevel >= 15) {
            this.dmgAmt = AbstractDungeon.player.maxHealth / 3;
        } else {
            this.dmgAmt = AbstractDungeon.player.maxHealth / 4;
        }


        this.registerPhase("start", new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(OPTIONS[0]).enabledCondition(() -> AbstractDungeon.player.getCardCount() > 0, OPTIONS[0])
                        .setOptionResult((i)->{
                            CardGroup purgableCards = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards());
                            if (!CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).isEmpty()) {
                                purgableCards.shuffle();
                                AbstractCard c = purgableCards.group.get(0);
                                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                                AbstractDungeon.player.masterDeck.removeCard(c);
                            }
                            transitionKey("card thrown");
                        }))
                .addOption(new TextPhase.OptionInfo(OPTIONS[1]).enabledCondition(() -> !AbstractDungeon.player.potions.isEmpty(), OPTIONS[2])
                        .setOptionResult((i)->{
                            this.checkForUncommonRelic();
                        }))
                .addOption(new TextPhase.OptionInfo(OPTIONS[3] + this.dmgAmt + OPTIONS[4]).enabledCondition(() -> true, OPTIONS[3] + this.dmgAmt + OPTIONS[4])
                        .setOptionResult((i)->{
                            this.result += DESCRIPTIONS[5];
                            this.checkForRareRelic(this.checkForHidingSuccess());
                            transitionKey("hide");
                        }))
                .addOption(OPTIONS[5], (i)->openMap())
        );
        this.registerPhase("card thrown", new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[5], (i)->openMap())
        );
        this.registerPhase("bottle success", new TextPhase(DESCRIPTIONS[2] + DESCRIPTIONS[3])
                .addOption(OPTIONS[5], (i)->openMap())
        );
        this.registerPhase("bottle fail", new TextPhase(DESCRIPTIONS[2] + DESCRIPTIONS[4])
                .addOption(OPTIONS[5], (i)->openMap())
        );
        this.registerPhase("hide success relic success", new TextPhase(DESCRIPTIONS[5] + DESCRIPTIONS[6] + DESCRIPTIONS[8])
                .addOption(OPTIONS[5], (i)->openMap())
        );
        this.registerPhase("hide success relic fail", new TextPhase(DESCRIPTIONS[5] + DESCRIPTIONS[6] + DESCRIPTIONS[9])
                .addOption(OPTIONS[5], (i)->openMap())
        );
        this.registerPhase("hide fail relic success", new TextPhase(DESCRIPTIONS[5] + DESCRIPTIONS[7] + DESCRIPTIONS[8])
                .addOption(OPTIONS[5], (i)->openMap())
        );
        this.registerPhase("hide fail relic fail", new TextPhase(DESCRIPTIONS[5] + DESCRIPTIONS[7] + DESCRIPTIONS[9])
                .addOption(OPTIONS[5], (i)->openMap())
        );
        transitionKey("start");
    }

//    "[Quickly! Throw something at it!] Permanently remove a #rrandom card from your deck.",
//    "[Attempt to capture it] #rLose #ra #rrandom #rpotion. 50% #gGet #gan #gUncommon #gRelic.",
//    "[Hide and wait it out] 12% #gGet #gan #gRare #gRelic. 50% #rLose #r NP",

    private void checkForUncommonRelic(){
        int r = AbstractDungeon.eventRng.random(AbstractDungeon.player.potionSlots - 1);
        AbstractDungeon.player.removePotion(AbstractDungeon.player.potions.get(r));

        if (AbstractDungeon.eventRng.randomBoolean()){
            AbstractRelic relic = new BottledFlame();
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
            transitionKey("bottle success");
        } else {
            transitionKey("bottle fail");
        }
    }

    private boolean checkForHidingSuccess(){
        if (AbstractDungeon.eventRng.randomBoolean()){
            this.result += DESCRIPTIONS[6];
            return true;
        } else {
            AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, this.dmgAmt, DamageInfo.DamageType.HP_LOSS));
            this.result += DESCRIPTIONS[7];
            return false;
        }
    }

    private void checkForRareRelic(boolean hiding){
        if (AbstractDungeon.eventRng.randomBoolean() && AbstractDungeon.eventRng.randomBoolean() && AbstractDungeon.eventRng.randomBoolean()){
            AbstractRelic relic = new DeadBranch();
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
            this.result += DESCRIPTIONS[8];
            if (hiding){
                transitionKey("hide success relic success");
            } else {
                transitionKey("hide fail relic success");
            }
        } else {
            this.result += DESCRIPTIONS[9];
            if (hiding){
                transitionKey("hide success relic fail");
            } else {
                transitionKey("hide fail relic fail");
            }
        }
    }

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        transitionKey("start");
    }
}
