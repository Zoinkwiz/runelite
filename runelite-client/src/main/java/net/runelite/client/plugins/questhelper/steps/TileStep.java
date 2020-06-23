package net.runelite.client.plugins.questhelper.steps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.questhelper.ItemRequirement;
import net.runelite.client.plugins.questhelper.QuestHelperPlugin;
import net.runelite.client.plugins.questhelper.QuestHelperWorldMapPoint;
import net.runelite.client.plugins.questhelper.questhelpers.QuestHelper;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.worldmap.WorldMapPointManager;

public class TileStep extends DetailedQuestStep
{
	@Inject
	private Client client;

	@Inject
	private WorldMapPointManager worldMapPointManager;

	private WorldPoint worldPoint;

	public TileStep(QuestHelper questHelper, WorldPoint worldPoint, String text, ItemRequirement... itemRequirements)
	{
		super(questHelper, text, itemRequirements);
		this.worldPoint = worldPoint;
	}

	@Override
	public void startUp()
	{
		worldMapPointManager.add(new QuestHelperWorldMapPoint(worldPoint, getQuestImage()));
	}

	@Override
	public void shutDown()
	{
		worldMapPointManager.removeIf(QuestHelperWorldMapPoint.class::isInstance);
		clearArrow();
	}

	@Override
	public void makeWorldOverlayHint(Graphics2D graphics, QuestHelperPlugin plugin)
	{
		super.makeWorldOverlayHint(graphics, plugin);

		if (inCutscene)
		{
			return;
		}

		LocalPoint lp = LocalPoint.fromWorld(client, worldPoint);
		if (lp == null)
		{
			return;
		}

		Polygon poly = Perspective.getCanvasTilePoly(client, lp);
		if (poly == null)
		{
			return;
		}

		OverlayUtil.renderPolygon(graphics, poly, Color.cyan);
	}
}
