package granbluebosses.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.Injury;
import com.megacrit.cardcrawl.cards.curses.Shame;
import com.megacrit.cardcrawl.cards.tempCards.Insight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BlueCandle;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import granbluebosses.GranblueBosses;

public class Shrine6Event extends PhasedEvent {
    public final static String EVENT_ID = GranblueBosses.makeID("Shrine6Event");
    protected static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(EVENT_ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private final static String EVENT_IMAGE_URL = GranblueBosses.eventPath("act1/shrine6.jpg");
    private final static String DIALOG_0 = DESCRIPTIONS[0];
    private final static String DIALOG_1 = DESCRIPTIONS[1];
    private final static String DIALOG_2 = DESCRIPTIONS[2];
    private final static String DIALOG_3 = DESCRIPTIONS[3];
    protected final int goldAmt;

    public Shrine6Event() {
        super(EVENT_ID, NAME, EVENT_IMAGE_URL);

        if (AbstractDungeon.ascensionLevel >= 15) {
            this.goldAmt = 50;
        } else {
            this.goldAmt = 70;
        }

        this.registerPhase("start", new TextPhase(DIALOG_0)
                .addOption(new TextPhase.OptionInfo(OPTIONS[0]).enabledCondition(() -> true, OPTIONS[0])
                        .setOptionResult((i)->{
                            AbstractDungeon.player.gainGold(this.goldAmt);
                            transitionKey("candle lit");
                        }))
                .addOption(new TextPhase.OptionInfo(OPTIONS[1]).enabledCondition(() -> true, OPTIONS[1])
                        .setOptionResult((i)->{
                            AbstractCard card = new Insight();
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                            transitionKey("candle unlit");
                        }))
                .addOption(new TextPhase.OptionInfo(OPTIONS[2]).enabledCondition(() -> true, OPTIONS[2])
                        .setOptionResult((i)->{
                            AbstractRelic relic = new BlueCandle();
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                            AbstractCard curse = new Shame();
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                            transitionKey("candle stolen");
                        }))
                .addOption(OPTIONS[3], (i)->openMap())
        );

        this.registerPhase("candle lit", new TextPhase(DIALOG_1).addOption(OPTIONS[3], (i)->openMap()));
        this.registerPhase("candle unlit", new TextPhase(DIALOG_2).addOption(OPTIONS[3], (i)->openMap()));
        this.registerPhase("candle stolen", new TextPhase(DIALOG_3).addOption(OPTIONS[3], (i)->openMap()));
        transitionKey("start");
    }

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        transitionKey("start");
    }
}
