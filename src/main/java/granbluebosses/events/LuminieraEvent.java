package granbluebosses.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import granbluebosses.GranblueBosses;

public class LuminieraEvent extends PhasedEvent {
    public final static String EVENT_ID = GranblueBosses.makeID("LuminieraEvent");
    protected static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(EVENT_ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private final static String EVENT_IMAGE_URL = GranblueBosses.eventPath("act1/luminiera.jpg");

    public LuminieraEvent() {
        super(EVENT_ID, NAME, EVENT_IMAGE_URL);

        this.registerPhase("start", new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(OPTIONS[0]).enabledCondition(() -> !AbstractDungeon.player.masterDeck.getCardsOfType(AbstractCard.CardType.CURSE).isEmpty(), OPTIONS[0])
                        .setOptionResult((i) -> {
                            AbstractCard c = AbstractDungeon.player.masterDeck.getRandomCard(AbstractCard.CardType.CURSE, true);
                            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                            AbstractDungeon.player.masterDeck.removeCard(c);

                            transitionKey("leave");
                        }))
                .addOption(OPTIONS[1], (i) -> openMap())
        );

        this.registerPhase("leave", new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[1], (i) -> openMap())
        );
        transitionKey("start");
    }

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        transitionKey("start");
    }
}
