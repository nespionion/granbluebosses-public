package granbluebosses.acts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import granbluebosses.GranblueBosses;
import granbluebosses.monsters.act1.bosses.GrandOrder;
import granbluebosses.monsters.act1.bosses.ProtoBaha;
import granbluebosses.monsters.act1.elites.Alexiel;
import granbluebosses.monsters.act1.elites.Europa;
import granbluebosses.monsters.act1.elites.Grimnir;
import granbluebosses.monsters.act1.elites.Shiva;
import granbluebosses.monsters.act1.normal.*;

public class Act1SkiesScene extends AbstractScene {
    private static final String ATLAS_URL = GranblueBosses.imagePath("scenes/act1/act1skies.atlas");
    private final TextureAtlas.AtlasRegion bg;
    private final TextureAtlas.AtlasRegion celesteFight;
    private final TextureAtlas.AtlasRegion colosusFight;
    private final TextureAtlas.AtlasRegion leviathanFight;
    private final TextureAtlas.AtlasRegion luminieraFight;
    private final TextureAtlas.AtlasRegion tiamatFight;
    private final TextureAtlas.AtlasRegion yggdrasilFight;
    private final TextureAtlas.AtlasRegion protoBaha1Fight;
    private final TextureAtlas.AtlasRegion protoBaha2Fight;
    private final TextureAtlas.AtlasRegion grandOrder1Fight;
    private final TextureAtlas.AtlasRegion grandOrder2Fight;
    private final TextureAtlas.AtlasRegion grandOrder3Fight;
    private final TextureAtlas.AtlasRegion alexFight;
    private final TextureAtlas.AtlasRegion europaFight;
    private final TextureAtlas.AtlasRegion grimnirFight;
    private final TextureAtlas.AtlasRegion shivaFight;
    private final TextureAtlas.AtlasRegion campfireBg;
    private final TextureAtlas.AtlasRegion campfireGlow;
    private final TextureAtlas.AtlasRegion campfireKindling;
    private TextureAtlas.AtlasRegion battleRoom;
    private Color overlayColor;
    private Color tmpColor;
    private Color whiteColor;
    private AbstractRoom currRoom = null;
    private String currMonsterId = null;

    public Act1SkiesScene() {
        super(ATLAS_URL);
        this.overlayColor = new Color(1.0F, 1.0F, 1.0F, 0.2F);
        this.tmpColor = new Color(1.0F, 1.0F, 1.0F, 1.0F);
        this.whiteColor = new Color(1.0F, 1.0F, 1.0F, 1.0F);
        this.bg = this.atlas.findRegion("bg");
        this.campfireBg = this.atlas.findRegion("campfire");

        TextureAtlas campfireAtlas = new TextureAtlas(Gdx.files.internal("bottomScene/scene.atlas"));
        this.campfireGlow = campfireAtlas.findRegion("mod/campfireGlow");
        this.campfireKindling = campfireAtlas.findRegion("mod/campfireKindling");

        this.celesteFight = this.atlas.findRegion("fight-cropped-celeste");
        this.colosusFight = this.atlas.findRegion("fight-cropped-colossus");
        this.leviathanFight = this.atlas.findRegion("fight-cropped-leviathan");
        this.luminieraFight = this.atlas.findRegion("fight-cropped-luminiera");
        this.tiamatFight = this.atlas.findRegion("fight-cropped-tiamat");
        this.yggdrasilFight = this.atlas.findRegion("fight-cropped-yggy");
        this.protoBaha1Fight = this.atlas.findRegion("fight-cropped-protobaha1");
        this.protoBaha2Fight = this.atlas.findRegion("fight-cropped-protobaha2");
        this.grandOrder1Fight = this.atlas.findRegion("fight-cropped-grandorder1");
        this.grandOrder2Fight = this.atlas.findRegion("fight-cropped-grandorder2");
        this.grandOrder3Fight = this.atlas.findRegion("fight-cropped-grandorder3");
        this.alexFight = this.atlas.findRegion("fight-cropped-alex");
        this.europaFight = this.atlas.findRegion("fight-cropped-europa");
        this.grimnirFight = this.atlas.findRegion("fight-cropped-grimnir");
        this.shivaFight = this.atlas.findRegion("fight-cropped-shiva");


        this.ambianceName = "AMBIANCE_BEYOND";
        this.fadeInAmbiance();
    }

    @Override
    public void renderCombatRoomBg(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        this.randomizeScene();
        this.renderAtlasRegionIf(sb, this.bg, true);
        this.renderAtlasRegionIf(sb, this.battleRoom, true);

    }

    @Override
    public void nextRoom(AbstractRoom room) {
        this.currMonsterId = null;
        super.nextRoom(room);
    }

    @Override
    public void renderCombatRoomFg(SpriteBatch sb) {

    }

    public void renderSpecificForeground(SpriteBatch sb, String id){
        this.renderAtlasRegionIf(sb, this.atlas.findRegion(id), true);
    }

    @Override
    public void renderCampfireRoom(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        this.renderAtlasRegionIf(sb, this.campfireBg, true);
        sb.setBlendFunction(770, 1);
        this.whiteColor.a = MathUtils.cosDeg((float)(System.currentTimeMillis() / 3L % 360L)) / 10.0F + 0.8F;
        sb.setColor(this.whiteColor);
        this.renderQuadrupleSize(sb, this.campfireGlow, !CampfireUI.hidden);
        sb.setBlendFunction(770, 771);
        sb.setColor(Color.WHITE);
        this.renderAtlasRegionIf(sb, this.campfireKindling, true);
    }

    @Override
    public void randomizeScene() {
        if (this.currRoom == null || !this.isMonsterPresent(AbstractDungeon.getCurrRoom(), this.currMonsterId)){
            this.currRoom = AbstractDungeon.getCurrRoom();
        }
        if (this.currMonsterId != null && this.isMonsterPresent(this.currRoom, this.currMonsterId)){
            return;
        }
        if (!(this.currRoom instanceof MonsterRoom)){
            this.battleRoom = this.bg;
        } else if (this.currRoom instanceof MonsterRoom && !(this.currRoom instanceof MonsterRoomBoss)){
            if (this.isMonsterPresent(this.currRoom, Celeste.MONSTER_ID)
                    || this.isMonsterPresent(this.currRoom, Celeste2.NAME) ){
                this.battleRoom = this.celesteFight;
            } else if (this.isMonsterPresent(this.currRoom, Colossus.MONSTER_ID)
                    || this.isMonsterPresent(this.currRoom, Colossus2.MONSTER_ID) ){
                this.battleRoom = this.colosusFight;
            } else if (this.isMonsterPresent(this.currRoom, Leviathan.MONSTER_ID)
                    || this.isMonsterPresent(this.currRoom, Leviathan2.MONSTER_ID) ){
                this.battleRoom = this.leviathanFight;
            } else if (this.isMonsterPresent(this.currRoom, Luminiera.MONSTER_ID)
                    || this.isMonsterPresent(this.currRoom, Luminiera2.MONSTER_ID) ){
                this.battleRoom = this.luminieraFight;
            } else if (this.isMonsterPresent(this.currRoom, Tiamat.MONSTER_ID)
                    || this.isMonsterPresent(this.currRoom, Tiamat2.MONSTER_ID) ){
                this.battleRoom = this.tiamatFight;
            } else if (this.isMonsterPresent(this.currRoom, Yggdrasil.MONSTER_ID)
                    || this.isMonsterPresent(this.currRoom, Yggdrasil2.MONSTER_ID) ){
                this.battleRoom = this.yggdrasilFight;
            } else if (this.isMonsterPresent(this.currRoom, Shiva.MONSTER_ID) ){
                this.battleRoom = this.shivaFight;
            } else if (this.isMonsterPresent(this.currRoom, Grimnir.MONSTER_ID) ){
                this.battleRoom = this.grimnirFight;
            } else if (this.isMonsterPresent(this.currRoom, Alexiel.MONSTER_ID) ){
                this.battleRoom = this.alexFight;
            } else if (this.isMonsterPresent(this.currRoom, Europa.MONSTER_ID) ){
                this.battleRoom = this.europaFight;
            } else {
                this.battleRoom = this.bg;
            }

            if (!AbstractDungeon.getCurrRoom().monsters.monsters.isEmpty()){
                this.currMonsterId = AbstractDungeon.getCurrRoom().monsters.monsters.get(0).id;
            }
        } else if (this.currRoom instanceof MonsterRoomBoss){
             if (this.isMonsterPresent(this.currRoom, ProtoBaha.MONSTER_ID) ){
                this.battleRoom = this.protoBaha1Fight;
             } else if (this.isMonsterPresent(this.currRoom, GrandOrder.MONSTER_ID) ){
                 this.battleRoom = this.grandOrder1Fight;
             } else {
                 this.battleRoom = this.bg;
             }

            if (!AbstractDungeon.getCurrRoom().monsters.monsters.isEmpty()){
                this.currMonsterId = AbstractDungeon.getCurrRoom().monsters.monsters.get(0).id;
            }
        } else {
            this.battleRoom = this.bg;
        }

        if (this.currRoom == null){
            this.battleRoom = this.bg;
        }
    }

    private enum ColumnConfig {
        OPEN,
        SMALL_ONLY,
        SMALL_PLUS_LEFT,
        SMALL_PLUS_RIGHT;
    }

    public boolean isMonsterPresent(AbstractRoom room, String id) {
        if (id == null || room == null || room.monsters == null || room.monsters.monsters.isEmpty()){
            return false;
        }
        for (String monsterId : room.monsters.getMonsterNames()){
            if (monsterId.equals(id)){
                return true;
            }
        }
        return false;
//        for(AbstractMonster m : room.monsters.monsters) {
//            if (m.id.equals(id)) {
//                return m;
//            }
//        }
//
//        return null;
    }
}
