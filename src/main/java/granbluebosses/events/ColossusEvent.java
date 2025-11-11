package granbluebosses.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.UpgradeShrine;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import granbluebosses.GranblueBosses;

public class ColossusEvent extends PhasedEvent {
    public final static String EVENT_ID = GranblueBosses.makeID("ColossusEvent");
    protected static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(EVENT_ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private final static String EVENT_IMAGE_URL = GranblueBosses.eventPath("act1/colossus.jpg");

    public ColossusEvent() {
        super(EVENT_ID, NAME, EVENT_IMAGE_URL);

        this.registerPhase("start", new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(OPTIONS[0]).enabledCondition(() -> !AbstractDungeon.player.masterDeck.getUpgradableCards().isEmpty(), OPTIONS[0])
                        .setOptionResult((i) -> {
                            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getUpgradableCards(), 1, OPTIONS[2], true, false, false, false);
                            transitionKey("leave");
                        }))
                .addOption(OPTIONS[1], (i) -> openMap())
        );

        this.registerPhase("leave", new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[1], (i) -> openMap())
        );
        transitionKey("start");
    }

    public void update() {
        super.update();
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = (AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            c.upgrade();
            logMetricCardUpgrade("ColossusEvent", "Upgraded", c);
            AbstractDungeon.player.bottledCardUpgradeCheck(c);
            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

    }

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        transitionKey("start");
    }
}
