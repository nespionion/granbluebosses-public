package granbluebosses.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import granbluebosses.GranblueBosses;

public class LushEvent extends PhasedEvent {
    public final static String EVENT_ID = GranblueBosses.makeID("LushEvent");
    protected static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(EVENT_ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private final static String EVENT_IMAGE_URL = GranblueBosses.eventPath("act1/forest_lush.jpg");
    private final int HPGainedAmt;
    private final int HPLostAmt;
    private final boolean isBase3Char;

    public LushEvent() {
        super(EVENT_ID, NAME, EVENT_IMAGE_URL);
        if (AbstractDungeon.ascensionLevel >= 15) {
            this.HPGainedAmt = 4;
            this.HPLostAmt = 6;
        } else {
            this.HPGainedAmt = 4;
            this.HPLostAmt = 6;
        }

        this.isBase3Char = AbstractDungeon.player.getCardColor() == AbstractCard.CardColor.BLUE
                || AbstractDungeon.player.getCardColor() == AbstractCard.CardColor.GREEN
                || AbstractDungeon.player.getCardColor() == AbstractCard.CardColor.RED;

        this.registerPhase("start", new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(OPTIONS[0]).enabledCondition(() -> true, OPTIONS[0])
                        .setOptionResult((i)->{
                            if (this.isBase3Char){
                                this.baseGameChar(AbstractCard.CardColor.RED);
                            } else {
                                this.moddedChar();
                            }
                        }))
                .addOption(new TextPhase.OptionInfo(OPTIONS[1]).enabledCondition(() -> true, OPTIONS[1])
                        .setOptionResult((i)->{
                            if (this.isBase3Char){
                                this.baseGameChar(AbstractCard.CardColor.BLUE);
                            } else {
                                this.moddedChar();
                            }
                        }))
                .addOption(new TextPhase.OptionInfo(OPTIONS[2]).enabledCondition(() -> true, OPTIONS[2])
                        .setOptionResult((i)->{
                            if (this.isBase3Char){
                                this.baseGameChar(AbstractCard.CardColor.GREEN);
                            } else {
                                this.moddedChar();
                            }
                        }))
                .addOption(OPTIONS[3], (i)->openMap())
        );
        this.registerPhase("leave good", new TextPhase(DESCRIPTIONS[1] + DESCRIPTIONS[2])
                .addOption(OPTIONS[3], (i)->openMap())
        );
        this.registerPhase("leave bad", new TextPhase(DESCRIPTIONS[1] + DESCRIPTIONS[3])
                .addOption(OPTIONS[3], (i)->openMap())
        );
        transitionKey("start");
    }

    private void baseGameChar(AbstractCard.CardColor color) {
        if (AbstractDungeon.player.getCardColor() == AbstractCard.CardColor.PURPLE && (color == AbstractCard.CardColor.BLUE || color == AbstractCard.CardColor.RED)){
            AbstractDungeon.player.increaseMaxHp(this.HPGainedAmt, true);
            AbstractDungeon.player.heal(this.HPGainedAmt);
            transitionKey("leave good");
        } else if (AbstractDungeon.player.getCardColor() == color){
            AbstractDungeon.player.increaseMaxHp(this.HPGainedAmt, true);
            AbstractDungeon.player.heal(this.HPGainedAmt);
            transitionKey("leave good");
        } else {
            AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, this.HPLostAmt, DamageInfo.DamageType.HP_LOSS));
            AbstractDungeon.player.decreaseMaxHealth(this.HPLostAmt);
            transitionKey("leave bad");
        }
    }

    private void moddedChar(){
        if(AbstractDungeon.eventRng.randomBoolean()){
            AbstractDungeon.player.increaseMaxHp(this.HPGainedAmt, true);
            AbstractDungeon.player.heal(this.HPGainedAmt);
            transitionKey("leave good");
        } else {
            AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, this.HPLostAmt, DamageInfo.DamageType.HP_LOSS));
            AbstractDungeon.player.decreaseMaxHealth(this.HPLostAmt);
            transitionKey("leave bad");
        }
    }

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        transitionKey("start");
    }
}
