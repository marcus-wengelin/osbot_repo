package sections;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.event.WalkingEvent;
import utils.Sleep;

import java.util.Arrays;
import java.util.List;

public final class WizardSection extends TutorialSection {

    private static final List<Position> PATH_TO_WIZARD_BUILDING = Arrays.asList(
            new Position(3122, 3101, 0),
            new Position(3125, 3097, 0),
            new Position(3127, 3093, 0),
            new Position(3129, 3088, 0),
            new Position(3135, 3087, 0),
            new Position(3141, 3086, 0)
    );

    private static final Area CHICKEN_AREA = new Area(3139, 3091, 3140, 3090);

    public WizardSection() {
        super("Magic Instructor");
    }

    @Override
    public final void onLoop() throws InterruptedException {
        if (pendingContinue()) {
            selectContinue();
            return;
        }

        if (getInstructor() == null) {
            Sleep.sleepUntil(() -> myPlayer().isAnimating(), 5000);
            getWalking().walkPath(PATH_TO_WIZARD_BUILDING);
        }

        switch (getProgress()) {
            case 620:
                talkToInstructor();
                break;
            case 630:
                getTabs().open(Tab.MAGIC);
                break;
            case 640:
                talkToInstructor();
                break;
            case 650:
                if (!CHICKEN_AREA.contains(myPosition())) {
                    walkToChickenArea();
                } else {
                    attackChicken();
                }
                break;
            case 670:
                if (getDialogues().isPendingOption()) {
                    getDialogues().selectOption("Yes.", "No, I'm not planning to do that.");
                } else {
                    talkToInstructor();
                }
                break;
        }
    }

    private boolean walkToChickenArea() {
        WalkingEvent walkingEvent = new WalkingEvent(CHICKEN_AREA);
        walkingEvent.setMinDistanceThreshold(0);
        walkingEvent.setMinDistanceThreshold(0);
        execute(walkingEvent);
        return walkingEvent.hasFinished();
    }

    private boolean attackChicken() {
        NPC chicken = getNpcs().closest("Chicken");
        if (chicken != null && getMagic().castSpellOnEntity(Spells.NormalSpells.WIND_STRIKE, chicken)) {
            Sleep.sleepUntil(() -> getProgress() != 650, 3000);
            return true;
        }
        return false;
    }
}
