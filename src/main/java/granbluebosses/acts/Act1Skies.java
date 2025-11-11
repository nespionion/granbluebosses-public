package granbluebosses.acts;

import actlikeit.dungeons.CustomDungeon;
import basemod.BaseMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import granbluebosses.GranblueBosses;
import granbluebosses.config.ConfigMenu;
import granbluebosses.events.*;
import granbluebosses.monsters.act1.bosses.GrandOrder;
import granbluebosses.monsters.act1.bosses.ProtoBaha;
import granbluebosses.monsters.act1.elites.Alexiel;
import granbluebosses.monsters.act1.elites.Europa;
import granbluebosses.monsters.act1.elites.Grimnir;
import granbluebosses.monsters.act1.elites.Shiva;
import granbluebosses.monsters.act1.normal.*;
import granbluebosses.util.Sounds;

import java.util.ArrayList;

public class Act1Skies extends CustomDungeon {
    public static final String ID = GranblueBosses.makeID("Act1Skies"); //From the main mod file for best practices
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];

    public Act1Skies() {
        super(NAME, ID, "images/ui/event/panel.png", false, 3, 6,6);

        this.setMainMusic(Sounds.MUSIC_ACT1_MAIN);

        this.defineWeakMonster();
        GranblueBosses.logger.info("Generated Weak Encounters");
        this.defineStrongMonster();
        GranblueBosses.logger.info("Generated Strong Encounters");
        this.defineEliteMonster();
        GranblueBosses.logger.info("Generated Elite Encounters");
        this.defineBossEnemies();
        GranblueBosses.logger.info("Generated Boss Encounters");

//        this.generateWeakEnemies(3);
//        this.generateStrongEnemies(6);
//        this.generateElites(4);

        if (ConfigMenu.enableDMCAMusic){
            this.setMainMusic(Sounds.MUSIC_ACT1_MAIN);
        } else {
            this.setMainMusic(Exordium.id);
        }

        if (ConfigMenu.enableStartOfActEvents){
            if (ConfigMenu.modestyFilter){
                this.onEnterEvent(Act1Entry.class);
            } else {
                this.onEnterEvent(Act1Entry.class);
            }
        }

        this.addTempMusic(Sounds.MUSIC_ACT1_BATTLE, Sounds.MUSIC_ACT1_BATTLE);
        this.addTempMusic(Sounds.MUSIC_ACT1_ELITE, Sounds.MUSIC_ACT1_ELITE);
        this.addTempMusic(Sounds.MUSIC_ACT1_ELITE_SHIVA, Sounds.MUSIC_ACT1_ELITE_SHIVA);
        this.addTempMusic(Sounds.MUSIC_ACT1_ELITE_EUROPA, Sounds.MUSIC_ACT1_ELITE_EUROPA);
        this.addTempMusic(Sounds.MUSIC_ACT1_ELITE_GRIMNIR, Sounds.MUSIC_ACT1_ELITE_GRIMNIR);
        this.addTempMusic(Sounds.MUSIC_ACT1_ELITE_ALEX, Sounds.MUSIC_ACT1_ELITE_ALEX);
        this.addTempMusic(Sounds.MUSIC_ACT1_PROTOBAHA1, Sounds.MUSIC_ACT1_PROTOBAHA1);
        this.addTempMusic(Sounds.MUSIC_ACT1_PROTOBAHA2, Sounds.MUSIC_ACT1_PROTOBAHA2);
        this.addTempMusic(Sounds.MUSIC_ACT1_GRANDORDER1, Sounds.MUSIC_ACT1_GRANDORDER1);
        this.addTempMusic(Sounds.MUSIC_ACT1_GRANDORDER2, Sounds.MUSIC_ACT1_GRANDORDER2);
        this.addTempMusic(Sounds.MUSIC_ACT1_GRANDORDER3, Sounds.MUSIC_ACT1_GRANDORDER3);
    }

    public Act1Skies(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd, p, emptyList);
    }
    public Act1Skies(CustomDungeon cd, AbstractPlayer p, SaveFile sf) {
        super(cd, p, sf);
    }

    protected void defineWeakMonster(){
//        if (AbstractDungeon.mapRng.randomBoolean()){
//            addMonster(GranblueBosses.makeID("Colossus"), () -> new Colossus());
//            addMonster(GranblueBosses.makeID("Tiamat"), () -> new Tiamat());
//            addMonster(GranblueBosses.makeID("Luminiera"), () -> new Luminiera());
//        } else {
//            addMonster(GranblueBosses.makeID("Celeste"), () -> new Celeste());
//            addMonster(GranblueBosses.makeID("Leviathan"), () -> new Leviathan());
//            addMonster(GranblueBosses.makeID("Yggdrasil"), () -> new Yggdrasil());
//        }

            addMonster(GranblueBosses.makeID("Colossus"), () -> new Colossus());
            addMonster(GranblueBosses.makeID("Tiamat"), () -> new Tiamat());
            addMonster(GranblueBosses.makeID("Luminiera"), () -> new Luminiera());
            addMonster(GranblueBosses.makeID("Celeste"), () -> new Celeste());
            addMonster(GranblueBosses.makeID("Leviathan"), () -> new Leviathan());
            addMonster(GranblueBosses.makeID("Yggdrasil"), () -> new Yggdrasil());
    }

    protected void defineStrongMonster(){
        addStrongMonster(GranblueBosses.makeID("Celeste2"), () -> new Celeste2());
        addStrongMonster(GranblueBosses.makeID("Colossus2"), () -> new Colossus2());
        addStrongMonster(GranblueBosses.makeID("Leviathan2"), () -> new Leviathan2());
        addStrongMonster(GranblueBosses.makeID("Luminiera2"), () -> new Luminiera2());
        addStrongMonster(GranblueBosses.makeID("Tiamat2"), () -> new Tiamat2());
        addStrongMonster(GranblueBosses.makeID("Yggdrasil2"), () -> new Yggdrasil2());
    }

    protected void defineEliteMonster(){
        addEliteEncounter(GranblueBosses.makeID("Alexiel"), () -> new Alexiel());
        addEliteEncounter(GranblueBosses.makeID("Grimnir"), () -> new Grimnir());
        addEliteEncounter(GranblueBosses.makeID("Europa"), () -> new Europa());
        addEliteEncounter(GranblueBosses.makeID("Shiva"), () -> new Shiva());
    }

    protected void generateMonsters() {
        this.generateWeakEnemies(this.weakpreset);
        this.generateStrongEnemies(this.strongpreset);
        this.generateElites(this.elitepreset);
    }

    @Override
    protected void makeMap() {
        super.makeMap();
    }

    public void defineBossEnemies(){
        addBoss(GranblueBosses.makeID("ProtoBahamut"), "Proto Bahamut", () -> new ProtoBaha(), ProtoBaha.MAP_ICON, ProtoBaha.OUTLINE);
        addBoss(GranblueBosses.makeID("GrandOrder"), "Grand Order", () -> new GrandOrder(), GrandOrder.MAP_ICON, GrandOrder.OUTLINE);
    }

    protected void generateWeakEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList();
        if (AbstractDungeon.mapRng.randomBoolean()){
            monsters.add(new MonsterInfo(GranblueBosses.makeID("Colossus"), 10.0F));
            monsters.add(new MonsterInfo(GranblueBosses.makeID("Tiamat"), 10.0F));
            monsters.add(new MonsterInfo(GranblueBosses.makeID("Luminiera"), 8.0F));
        } else {
            monsters.add(new MonsterInfo(GranblueBosses.makeID("Leviathan"), 10.0F));
            monsters.add(new MonsterInfo(GranblueBosses.makeID("Yggdrasil"), 10.0F));
            monsters.add(new MonsterInfo(GranblueBosses.makeID("Celeste"), 8.0F));
        }
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, false);
    }

    protected void generateStrongEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList();
        monsters.add(new MonsterInfo(GranblueBosses.makeID("Yggdrasil2"), 10.0F));
        monsters.add(new MonsterInfo(GranblueBosses.makeID("Tiamat2"), 10.0F));
        monsters.add(new MonsterInfo(GranblueBosses.makeID("Leviathan2"), 10.0F));
        monsters.add(new MonsterInfo(GranblueBosses.makeID("Colossus2"), 10.0F));
        monsters.add(new MonsterInfo(GranblueBosses.makeID("Celeste2"), 8.0F));
        monsters.add(new MonsterInfo(GranblueBosses.makeID("Luminiera2"), 8.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateFirstStrongEnemy(monsters, this.generateExclusions());
        this.populateMonsterList(monsters, count, false);
    }

    protected void generateElites(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList();
        monsters.add(new MonsterInfo(GranblueBosses.makeID("Alexiel"), 10.0F));
        monsters.add(new MonsterInfo(GranblueBosses.makeID("Grimnir"), 10.0F));
        monsters.add(new MonsterInfo(GranblueBosses.makeID("Europa"), 10.0F));
        monsters.add(new MonsterInfo(GranblueBosses.makeID("Shiva"), 10.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, true);
    }

    @Override
    protected void initializeEventList() {
        super.initializeEventList();

        BaseMod.addEvent(FireEvent.EVENT_ID, FireEvent.class, this.id);
        BaseMod.addEvent(LushEvent.EVENT_ID, LushEvent.class, this.id);
        BaseMod.addEvent(RiverEvent.EVENT_ID, RiverEvent.class, this.id);
        BaseMod.addEvent(RuinsEvent.EVENT_ID, RuinsEvent.class, this.id);
        BaseMod.addEvent(Shrine6Event.EVENT_ID, Shrine6Event.class, this.id);
        BaseMod.addEvent(Shrine10Event.EVENT_ID, Shrine10Event.class, this.id);

        GranblueBosses.logger.info("Generated Normal Events");

//        BaseMod.addEvent(new AddEventParams.Builder(ZooeyEvent.EVENT_ID, ZooeyEvent.class)
//                .dungeonID(this.id)
//                .spawnCondition(() -> !AbstractDungeon.bossKey.equals(GrandOrder.MONSTER_ID)
//                                    && !AbstractDungeon.bossKey.equals(GranblueBosses.makeID("GrandOrder"))
//                                    && !AbstractDungeon.bossKey.equals("Grand Order"))
//                .create());
//        GranblueBosses.logger.info("Generated Boss Change");
    }

    @Override
    protected void initializeShrineList() {}


    @Override
    public AbstractScene DungeonScene() {
        return new Act1SkiesScene();
    }

    public String getBodyText() {
        return TEXT[2];
    }

    public String getOptionText() {
        return TEXT[3];
    }

    public String getAfterSelectText() {
        return "A warm light envelops you, and you suddenly find yourself floating. Above you is a clear night sky. Bellow you is a sea of clouds.";
    }

    @Override
    protected void initializeLevelSpecificChances() {
        shopRoomChance = 0.05F;
        restRoomChance = 0.12F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 0.22F;
        eliteRoomChance = 0.08F;
        smallChestChance = 50;
        mediumChestChance = 33;
        largeChestChance = 17;
        commonRelicChance = 50;
        uncommonRelicChance = 33;
        rareRelicChance = 17;
        colorlessRareChance = 0.3F;
        if (AbstractDungeon.ascensionLevel >= 12) {
            cardUpgradedChance = 0.0F;
        } else {
            cardUpgradedChance = 0.0F;
        }

    }
}
