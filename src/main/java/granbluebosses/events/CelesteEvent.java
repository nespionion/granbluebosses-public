package granbluebosses.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.DarkstonePeriapt;
import granbluebosses.GranblueBosses;

public class CelesteEvent extends PhasedEvent {
    public final static String EVENT_ID = GranblueBosses.makeID("CelesteEvent");
    protected static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(EVENT_ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private final static String EVENT_IMAGE_URL = GranblueBosses.eventPath("act1/celeste.jpg");
    private final int maxHPLost;

    public CelesteEvent() {
        super(EVENT_ID, NAME, EVENT_IMAGE_URL);
        if (AbstractDungeon.ascensionLevel >= 15) {
            this.maxHPLost = 10;
        } else {
            this.maxHPLost = 7;
        }

        this.registerPhase("start", new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(OPTIONS[0] + this.maxHPLost + OPTIONS[1]).enabledCondition(() -> true, OPTIONS[0] + this.maxHPLost + OPTIONS[1])
                        .setOptionResult((i)->{
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new DarkstonePeriapt());
                            AbstractDungeon.player.decreaseMaxHealth(this.maxHPLost);
                            transitionKey("leave");
                        }))
                .addOption(OPTIONS[2], (i)->openMap())
        );

        this.registerPhase("leave", new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[2], (i)->openMap())
        );

        transitionKey("start");
    }

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        transitionKey("start");
    }
}
