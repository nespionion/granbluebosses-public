package granbluebosses.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import granbluebosses.GranblueBosses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RiverEvent extends PhasedEvent {
    public final static String EVENT_ID = GranblueBosses.makeID("RiverEvent");
    protected static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(EVENT_ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private final static String EVENT_IMAGE_URL = GranblueBosses.eventPath("act1/river.jpg");
    protected final int healAmt;

    public RiverEvent() {
        super(EVENT_ID, NAME, EVENT_IMAGE_URL);
        if (AbstractDungeon.ascensionLevel >= 15) {
            this.healAmt = AbstractDungeon.player.maxHealth / 6;
        } else {
            this.healAmt = AbstractDungeon.player.maxHealth / 8;
        }

        this.registerPhase("start", new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(OPTIONS[0]).enabledCondition(() -> true, OPTIONS[0])
                        .setOptionResult((i)->{
                            if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0) {
                                AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[7], false, false, false, true);
                            }
                            AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[7], false, false, false, true);
                            transitionKey("leave");
                        }))
                .addOption(new TextPhase.OptionInfo(OPTIONS[1] + this.healAmt + OPTIONS[2]).enabledCondition(() -> true, OPTIONS[1] + this.healAmt + OPTIONS[2])
                        .setOptionResult((i)->{
                            AbstractDungeon.player.heal(this.healAmt);
                            transitionKey("leave");
                        }))
                .addOption(new TextPhase.OptionInfo(OPTIONS[3]).enabledCondition(() -> true, OPTIONS[0])
                        .setOptionResult((i)->{
                            ArrayList<AbstractCard> upgradableCards = new ArrayList();

                            for(AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                                if (c.canUpgrade()) {
                                    upgradableCards.add(c);
                                }
                            }

                            Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
                            if (!upgradableCards.isEmpty()) {
                                if (upgradableCards.size() == 1) {
                                    ((AbstractCard)upgradableCards.get(0)).upgrade();
                                    AbstractDungeon.player.bottledCardUpgradeCheck((AbstractCard)upgradableCards.get(0));
                                    AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(((AbstractCard)upgradableCards.get(0)).makeStatEquivalentCopy()));
                                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                                } else {
                                    ((AbstractCard)upgradableCards.get(0)).upgrade();
                                    ((AbstractCard)upgradableCards.get(1)).upgrade();
                                    AbstractDungeon.player.bottledCardUpgradeCheck((AbstractCard)upgradableCards.get(0));
                                    AbstractDungeon.player.bottledCardUpgradeCheck((AbstractCard)upgradableCards.get(1));
                                    AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(((AbstractCard)upgradableCards.get(0)).makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 20.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F));
                                    AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(((AbstractCard)upgradableCards.get(1)).makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F));
                                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                                }
                            }
                            transitionKey("leave");
                        }))
                .addOption(OPTIONS[4], (i)->openMap())
        );
        this.registerPhase("leave", new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[2], (i)->openMap())
        );
        transitionKey("start");
    }

    public void update() {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = (AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
            AbstractDungeon.player.masterDeck.removeCard(c);
            AbstractDungeon.gridSelectScreen.selectedCards.remove(c);

        }

    }

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        transitionKey("start");
    }
}
