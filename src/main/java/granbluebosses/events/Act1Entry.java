package granbluebosses.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import granbluebosses.GranblueBosses;
import granbluebosses.cards.rewards.*;
import granbluebosses.config.ConfigMenu;
import granbluebosses.relics.events.BlueCrystal;
import granbluebosses.relics.events.ShieldOfTenets;

import java.util.ArrayList;

public class Act1Entry extends PhasedEvent {
    public final static String EVENT_ID = GranblueBosses.makeID("Act1Entry");
    protected static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(EVENT_ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private final static String EVENT_IMAGE_URL_1 = GranblueBosses.eventPath("act1/event-act1-entry-0.png");
    private final static String EVENT_IMAGE_URL_1_CEN = GranblueBosses.eventPath("act1/event-act1-entry-0Cen.png");
    private final static String EVENT_IMAGE_URL_2_1 = GranblueBosses.eventPath("act1/event-act1-entry-1.png");
    private final static String EVENT_IMAGE_URL_2_2 = GranblueBosses.eventPath("act1/event-act1-entry-2.png");
    private final static String EVENT_IMAGE_URL_2_2_CEN = GranblueBosses.eventPath("act1/event-act1-entry-2Cen.png");
    private final static String EVENT_IMAGE_URL_3 = GranblueBosses.eventPath("act1/event-act1-entry-3.png");
    private static String reward1Text = "";
    private static String reward2Text = "";
    private static int reward1Index = -1;
    private static int reward2Index1 = -1;
    private static int reward2Index2 = -1;
    private enum SIDE{
        STARS,
        SKIES
    }
    private static SIDE currSide = null;

    public Act1Entry() {
        super(EVENT_ID, NAME, EVENT_IMAGE_URL_1);

        if (ConfigMenu.modestyFilter){
            this.imageEventText.loadImage(EVENT_IMAGE_URL_1_CEN);
        }

        this.generateRewards();
        GranblueBosses.logger.info("Entry Event rewards generated");

        this.registerPhase("start", new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(OPTIONS[1])
                        .setOptionResult((i)->{

                            currSide = SIDE.STARS;
                            this.imageEventText.loadImage(EVENT_IMAGE_URL_2_1);
                            transitionKey("stars");
                        }))
                .addOption(new TextPhase.OptionInfo(OPTIONS[0])
                        .setOptionResult((i)->{

                            currSide = SIDE.SKIES;
                            if (ConfigMenu.modestyFilter){
                                this.imageEventText.loadImage(EVENT_IMAGE_URL_2_2_CEN);
                            } else {
                                this.imageEventText.loadImage(EVENT_IMAGE_URL_2_2);
                            }
                            transitionKey("sky");
                        }))
        );

        this.registerPhase("stars", new TextPhase(DESCRIPTIONS[1])
                .addOption(new TextPhase.OptionInfo(reward1Text)
                        .setOptionResult((i)->{

                            this.obtainFirstRewards();
                            this.imageEventText.loadImage(EVENT_IMAGE_URL_3);
                            transitionKey("leave stars");
                        }))
                .addOption(new TextPhase.OptionInfo(reward2Text)
                        .setOptionResult((i)->{

                            this.obtainSecondRewards();
                            this.imageEventText.loadImage(EVENT_IMAGE_URL_3);
                            transitionKey("leave stars");
                        }))
                .addOption(new TextPhase.OptionInfo(OPTIONS[20])
                        .setOptionResult((i)->{

                            this.obtainThirdRewards();
                            this.imageEventText.loadImage(EVENT_IMAGE_URL_3);
                            transitionKey("leave stars");
                        }))
        );

        this.registerPhase("sky", new TextPhase(DESCRIPTIONS[2])
                .addOption(new TextPhase.OptionInfo(reward1Text)
                        .setOptionResult((i)->{

                            this.obtainFirstRewards();
                            this.imageEventText.loadImage(EVENT_IMAGE_URL_3);
                            transitionKey("leave sky");
                        }))
                .addOption(new TextPhase.OptionInfo(reward2Text)
                        .setOptionResult((i)->{

                            this.obtainSecondRewards();
                            this.imageEventText.loadImage(EVENT_IMAGE_URL_3);
                            transitionKey("leave sky");
                        }))
                .addOption(new TextPhase.OptionInfo(OPTIONS[21])
                        .setOptionResult((i)->{

                            this.obtainThirdRewards();
                            this.imageEventText.loadImage(EVENT_IMAGE_URL_3);
                            transitionKey("leave sky");
                        }))
        );


        this.registerPhase("leave stars", new TextPhase(DESCRIPTIONS[3] + DESCRIPTIONS[5])
                .addOption(OPTIONS[22], (i)->openMap())
        );

        this.registerPhase("leave sky", new TextPhase(DESCRIPTIONS[4] + DESCRIPTIONS[5])
                .addOption(OPTIONS[22], (i)->openMap())
        );

        transitionKey("start");

    }

    protected void generateRewards(){
        reward1Index = 6 + AbstractDungeon.eventRng.random(4);

        reward1Text = OPTIONS[reward1Index];

        reward2Index1 = 12 + AbstractDungeon.eventRng.random(2);
        reward2Index2 = 16 + AbstractDungeon.eventRng.random(2);

        reward2Text = OPTIONS[reward2Index1] + OPTIONS[reward2Index2];

    }

    protected void obtainFirstRewards(){
        switch (reward1Index){
            case 6:
                AbstractDungeon.cardRewardScreen.open(this.getOmegaRewardCards(), (RewardItem)null, CardCrawlGame.languagePack.getUIString("CardRewardScreen").TEXT[1]);
                return;
            case 7:
                AbstractCard card1 = AbstractDungeon.getCard(AbstractCard.CardRarity.RARE);
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card1, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                return;
            case 8:
                AbstractDungeon.player.increaseMaxHp(10, false);
                return;
            case 9:
                AbstractCard card2 = AbstractDungeon.player.masterDeck.getRandomCard(true);
                AbstractDungeon.player.masterDeck.removeCard(card2);
                AbstractDungeon.transformCard(card2, false, AbstractDungeon.miscRng);
                AbstractCard transCard = AbstractDungeon.getTransformedCard();
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(transCard, card2.current_x, card2.current_y));

                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                card2 = AbstractDungeon.player.masterDeck.getRandomCard(true);
                card2.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(card2);
                return;
            case 10:
                CardCrawlGame.sound.play("CARD_EXHAUST");
                AbstractCard card3 = AbstractDungeon.player.masterDeck.getRandomCard(true);
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card3, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                AbstractDungeon.player.masterDeck.removeCard(card3);

                CardCrawlGame.sound.play("CARD_EXHAUST");
                card3 = AbstractDungeon.player.masterDeck.getRandomCard(true);
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card3, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                AbstractDungeon.player.masterDeck.removeCard(card3);
                return;
        }
    }

    protected void obtainSecondRewards(){
        switch (reward2Index1){
            case 12:
                AbstractDungeon.player.decreaseMaxHealth(7);
                break;
            case 13:
                AbstractCard curse = AbstractDungeon.getCard(AbstractCard.CardRarity.CURSE);
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                break;
            case 14:
                AbstractDungeon.player.loseGold(AbstractDungeon.player.gold);
                break;
        }
        switch (reward2Index2){
            case 16:
                CardCrawlGame.sound.play("CARD_EXHAUST");
                AbstractCard card3 = AbstractDungeon.player.masterDeck.getRandomCard(true);
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card3, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                AbstractDungeon.player.masterDeck.removeCard(card3);

                CardCrawlGame.sound.play("CARD_EXHAUST");
                card3 = AbstractDungeon.player.masterDeck.getRandomCard(true);
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card3, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                AbstractDungeon.player.masterDeck.removeCard(card3);

                AbstractCard card1 = AbstractDungeon.player.masterDeck.getRandomCard(true);
                AbstractCard card2 = AbstractDungeon.player.masterDeck.getRandomCard(true);

                while (card1.equals(card2) || AbstractDungeon.player.masterDeck.size() == 1){
                    card2 = AbstractDungeon.player.masterDeck.getRandomCard(true);
                }

                AbstractDungeon.player.masterDeck.removeCard(card1);
                AbstractDungeon.transformCard(card1, false, AbstractDungeon.miscRng);
                AbstractCard transCard2 = AbstractDungeon.getTransformedCard();
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(transCard2, card1.current_x, card1.current_y));

                if (AbstractDungeon.player.masterDeck.size() > 1){
                    AbstractDungeon.player.masterDeck.removeCard(card2);
                    AbstractDungeon.transformCard(card2, false, AbstractDungeon.miscRng);
                    AbstractCard transCard = AbstractDungeon.getTransformedCard();
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(transCard, card2.current_x, card2.current_y));
                }
                return;
            case 17:
                AbstractDungeon.cardRewardScreen.open(this.getCallRewardCards(), (RewardItem)null, CardCrawlGame.languagePack.getUIString("CardRewardScreen").TEXT[1]);
                return;
            case 18:
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.BOSS));
                return;
        }
    }

    protected void obtainThirdRewards(){
        if (currSide == SIDE.STARS){
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2),new BlueCrystal());
        } else {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2),new ShieldOfTenets());
        }
    }


    public ArrayList<AbstractCard> getOmegaRewardCards() {
        ArrayList<AbstractCard> retVal = new ArrayList();

        retVal.add(new CelesteOmega());
        retVal.add(new ColossusOmega());
        retVal.add(new LeviathanOmega());
        retVal.add(new LuminieraOmega());
        retVal.add(new TiamatOmega());
        retVal.add(new YggdrasilOmega());

        while(retVal.size() > 3){
            int index = AbstractDungeon.eventRng.random(retVal.size()-1);
            retVal.remove(index);
        }

        return retVal;
    }

    public ArrayList<AbstractCard> getCallRewardCards() {
        ArrayList<AbstractCard> retVal = new ArrayList();

        retVal.add(new ShivaCall());
        retVal.add(new GrimnirCall());
        retVal.add(new EuropaCall());
        retVal.add(new AlexielCall());

        while(retVal.size() > 3){
            int index = AbstractDungeon.eventRng.random(retVal.size()-1);
            retVal.remove(index);
        }

        return retVal;
    }


    public AbstractCard.CardRarity rollRarity() {
        return NeowEvent.rng.randomBoolean(0.33F) ? AbstractCard.CardRarity.UNCOMMON : AbstractCard.CardRarity.COMMON;
    }

    /*
    Neow's Blessings:

    First Blessing
        Remove a card.
        Transform a card.
        Upgrade a card.
        Choose a card to obtain. (Choose one of 3 random cards of your character.)
        Choose an Uncommon Colorless Card to obtain.
        Obtain a random Rare Card.

    Second Blessing
        Max HP +8/6/7/7 (Ironclad / Silent / Defect / Watcher)
        Neow's Lament: Enemies in the next three combats will have one health.
        Obtain a random Common Relic.
        Receive 100 Gold.
        Obtain 3 random Potions.

    Third Blessing
        A composite of a disadvantage and a more powerful reward. It can be a combination of any disadvantage and reward below, with any exceptions noted.

        Disadvantages:
            Lose Max HP. (-8 Ironclad / -7 Silent / -7 Watcher / -7 Defect)
            Take X damage.
            X is equal to (Current HP / 10, rounded down) * 3.
            The amount of damage is also affected by AscensionAscension.png 6 and AscensionAscension.png 14.
            Obtain a Curse.
            Lose all Gold.

        Advantages:
            Remove 2 cards.
            Does not pair with Obtain a Curse.
            Transform 2 cards.
            Gain 250 Gold.
            Does not pair with Lose all Gold.
            Choose a Rare Card to obtain.
            Choose a Rare Colorless Card to obtain.
            Obtain a random Rare Relic.
            Gain Max HP. (+16 Ironclad / +12 Silent / +14 Defect / +14 Watcher)
            Does not pair with Lose max health

    * */

}
