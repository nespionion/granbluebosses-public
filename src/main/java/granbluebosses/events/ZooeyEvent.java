package granbluebosses.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import granbluebosses.GranblueBosses;
import granbluebosses.relics.events.AzureFeather;

public class ZooeyEvent extends PhasedEvent {
    public final static String EVENT_ID = GranblueBosses.makeID("ZooeyEvent");
    protected static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(EVENT_ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private final static String EVENT_IMAGE_URL = GranblueBosses.eventPath("act1/zooey.png");

    public ZooeyEvent() {
        super(EVENT_ID, NAME, EVENT_IMAGE_URL);

        this.registerPhase("start", new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(OPTIONS[0]).enabledCondition(() -> true, OPTIONS[0])
                        .setOptionResult((i)->{
                            // TODO Change boss to Grand Order
                            transitionKey("boss change");
                        }))
                .addOption(new TextPhase.OptionInfo(OPTIONS[1]).enabledCondition(() -> true, OPTIONS[1])
                        .setOptionResult((i)->{
                            // TODO Change boss to Proto Baha
                            AbstractRelic relic = new AzureFeather();
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);
                            transitionKey("no boss change");
                        }))
        );
        this.registerPhase("boss change", new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[2], (i)->openMap())
        );
        this.registerPhase("no boss change", new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[2], (i)->openMap())
        );
        transitionKey("start");
    }


}
