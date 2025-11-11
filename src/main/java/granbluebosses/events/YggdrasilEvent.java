package granbluebosses.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import granbluebosses.GranblueBosses;
import granbluebosses.config.ConfigMenu;

public class YggdrasilEvent extends PhasedEvent {
    public final static String EVENT_ID = GranblueBosses.makeID("YggdrasilEvent");
    protected static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(EVENT_ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private final static String EVENT_IMAGE_URL = GranblueBosses.eventPath("act1/yggy.jpg");
    private final static String EVENT_IMAGE_URL_CEN = GranblueBosses.eventPath("act1/yggyCen.png");
    private final int hpAmt;

    public YggdrasilEvent() {
        super(EVENT_ID, NAME, EVENT_IMAGE_URL);
        if (AbstractDungeon.ascensionLevel >= 15) {
            this.hpAmt = 8;
        } else {
            this.hpAmt = 12;
        }

        if (ConfigMenu.modestyFilter){
            this.imageEventText.loadImage(EVENT_IMAGE_URL_CEN);
        }

        this.registerPhase("start", new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(OPTIONS[0] + this.hpAmt + OPTIONS[1]).enabledCondition(() -> true, OPTIONS[0])
                        .setOptionResult((i) -> {

                            AbstractDungeon.player.increaseMaxHp(this.hpAmt, false);
                            transitionKey("leave");
                        }))
                .addOption(OPTIONS[2], (i) -> openMap())
        );

        this.registerPhase("leave", new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[2], (i) -> openMap())
        );
        transitionKey("start");
    }

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        transitionKey("start");
    }
}
