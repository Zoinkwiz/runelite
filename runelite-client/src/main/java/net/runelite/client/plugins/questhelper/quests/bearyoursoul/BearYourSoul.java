package net.runelite.client.plugins.questhelper.quests.bearyoursoul;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.runelite.api.ItemID;
import net.runelite.api.NpcID;
import net.runelite.api.ObjectID;
import net.runelite.api.Quest;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.questhelper.ItemRequirement;
import net.runelite.client.plugins.questhelper.QuestDescriptor;
import net.runelite.client.plugins.questhelper.Zone;
import net.runelite.client.plugins.questhelper.panel.PanelDetails;
import net.runelite.client.plugins.questhelper.questhelpers.BasicQuestHelper;
import net.runelite.client.plugins.questhelper.steps.ConditionalStep;
import net.runelite.client.plugins.questhelper.steps.DetailedQuestStep;
import net.runelite.client.plugins.questhelper.steps.DigStep;
import net.runelite.client.plugins.questhelper.steps.NpcTalkStep;
import net.runelite.client.plugins.questhelper.steps.ObjectStep;
import net.runelite.client.plugins.questhelper.steps.QuestStep;
import net.runelite.client.plugins.questhelper.steps.conditional.ConditionForStep;
import net.runelite.client.plugins.questhelper.steps.conditional.ItemRequirementCondition;
import net.runelite.client.plugins.questhelper.steps.conditional.ZoneCondition;

@QuestDescriptor(
	quest = Quest.BEAR_YOUR_SOUL
)
public class BearYourSoul extends BasicQuestHelper
{
	ItemRequirement spade, dustyKeyOr70AgilOrKeyMasterTeleport, damagedSoulBearer;

	ConditionForStep hasSoulBearer, inTaverleyDungeon, inKeyMaster;

	QuestStep findSoulJourneyAndRead, talkToAretha, arceuusChurchDig, goToTaverleyDungeon, enterCaveToKeyMaster, speakKeyMaster;

	Zone inTaverleyDungeonZone, inKeyMasterZone;

	@Override
	public Map<Integer, QuestStep> loadSteps()
	{
		loadZones();
		setupItemRequirements();
		setupConditions();
		setupSteps();
		Map<Integer, QuestStep> steps = new HashMap<>();

		steps.put(0, findSoulJourneyAndRead);
		steps.put(1, talkToAretha);

		ConditionalStep repairSoulBearer = new ConditionalStep(this, arceuusChurchDig);
		repairSoulBearer.addStep(inKeyMaster, speakKeyMaster);
		repairSoulBearer.addStep(inTaverleyDungeon, enterCaveToKeyMaster);
		repairSoulBearer.addStep(hasSoulBearer, goToTaverleyDungeon);

		steps.put(2, repairSoulBearer);

		return steps;
	}

	public void setupItemRequirements() {
		dustyKeyOr70AgilOrKeyMasterTeleport = new ItemRequirement("Dusty key, or another way to get into the deep Taverley Dungeon", ItemID.DUSTY_KEY);
		spade = new ItemRequirement("Spade", ItemID.SPADE);
		damagedSoulBearer = new ItemRequirement("Damaged soul bearer", ItemID.DAMAGED_SOUL_BEARER);
	}

	public void loadZones() {
		inTaverleyDungeonZone = new Zone(new WorldPoint(2816,9668,0), new WorldPoint(2973,9855,0));
		inKeyMasterZone = new Zone(new WorldPoint(1289, 1236, 0), new WorldPoint(1333, 1274, 0));
	}

	public void setupConditions() {
		inTaverleyDungeon = new ZoneCondition(inTaverleyDungeonZone);
		inKeyMaster = new ZoneCondition(inKeyMasterZone);
		hasSoulBearer = new ItemRequirementCondition(damagedSoulBearer);
	}

	public void setupSteps() {
		findSoulJourneyAndRead = new DetailedQuestStep(this, new WorldPoint(1632,3808,0), "Go to the Arceuus library and find The Soul journey book in one of the bookcases, then read it. You can ask Biblia for help locating it, or make use of the Runelite Kourend Library plugin.");

		talkToAretha = new NpcTalkStep(this, NpcID.ARETHA, new WorldPoint(1814, 3851, 0),
			"Talk to Aretha at the Soul Altar.");
		talkToAretha.addDialogStep("I've been reading your book...");
		talkToAretha.addDialogStep("Yes please.");

		arceuusChurchDig = new DigStep(this, new WorldPoint(1699, 3794, 0), "Go to the Arceuus church and dig for the Damaged soul bearer.");

		goToTaverleyDungeon = new ObjectStep(this, ObjectID.LADDER_16680, new WorldPoint(2884, 3397, 0), "Go to Taverley Dungeon, or teleport to the Key Master directly.", damagedSoulBearer, dustyKeyOr70AgilOrKeyMasterTeleport);

		enterCaveToKeyMaster = new ObjectStep(this, ObjectID.CAVE_26567, new WorldPoint(2874, 9846, 0), "Enter the cave to the Key Master.", damagedSoulBearer, dustyKeyOr70AgilOrKeyMasterTeleport);

		speakKeyMaster = new NpcTalkStep(this, NpcID.KEY_MASTER, new WorldPoint(2686, 9884, 0),
			"Talk to Key Master in the Cerberus' Lair.", damagedSoulBearer);
	}

	@Override
	public ArrayList<ItemRequirement> getItemRequirements()
	{
		ArrayList<ItemRequirement> reqs = new ArrayList<>();
		reqs.add(spade);
		reqs.add(dustyKeyOr70AgilOrKeyMasterTeleport);
		return reqs;
	}

	@Override
	public ArrayList<PanelDetails> getPanels()
	{
		ArrayList<PanelDetails> allSteps = new ArrayList<>();
		allSteps.add(new PanelDetails("Find the Soul journey book", new ArrayList<>(Arrays.asList(findSoulJourneyAndRead))));
		allSteps.add(new PanelDetails("Talk to Aretha", new ArrayList<>(Arrays.asList(talkToAretha))));
		allSteps.add(new PanelDetails("Dig up the Soul Bearer", new ArrayList<>(Arrays.asList(arceuusChurchDig)), spade));
		allSteps.add(new PanelDetails("Have the Soul Bearer repaired", new ArrayList<>(Arrays.asList(goToTaverleyDungeon, enterCaveToKeyMaster, speakKeyMaster)), dustyKeyOr70AgilOrKeyMasterTeleport));
		return allSteps;
	}

	@Override
	public String getCombatRequirements()
	{
		return null;
	}
}
