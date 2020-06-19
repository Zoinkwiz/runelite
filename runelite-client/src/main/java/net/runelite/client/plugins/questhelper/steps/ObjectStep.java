package net.runelite.client.plugins.questhelper.steps;

import com.google.common.primitives.Ints;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Point;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.DecorativeObjectDespawned;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.questhelper.ItemRequirement;
import net.runelite.client.plugins.questhelper.QuestHelperPlugin;
import static net.runelite.client.plugins.questhelper.QuestHelperWorldOverlay.CLICKBOX_BORDER_COLOR;
import static net.runelite.client.plugins.questhelper.QuestHelperWorldOverlay.CLICKBOX_FILL_COLOR;
import static net.runelite.client.plugins.questhelper.QuestHelperWorldOverlay.CLICKBOX_HOVER_BORDER_COLOR;
import net.runelite.client.plugins.questhelper.questhelpers.QuestHelper;
import net.runelite.client.ui.overlay.OverlayUtil;

public class ObjectStep extends DetailedQuestStep
{
	private final int objectID;
	private TileObject object;

	private int iconItemID = -1;

	private final List<TileObject> objects = new ArrayList<>();

	public ObjectStep(QuestHelper questHelper, int objectID, WorldPoint worldPoint, String text, ItemRequirement... itemRequirements)
	{
		super(questHelper, worldPoint, text, itemRequirements);
		this.objectID = objectID;
	}

	@Override
	public void startUp()
	{
		super.startUp();

		LocalPoint localPoint = LocalPoint.fromWorld(client, worldPoint);
		if (localPoint == null)
		{
			return;
		}

		Tile tile = client.getScene().getTiles()[client.getPlane()][localPoint.getSceneX()][localPoint.getSceneY()];
		for (GameObject object : tile.getGameObjects())
		{
			handleObjects(object);
		}

		handleObjects(tile.getDecorativeObject());
		handleObjects(tile.getGroundObject());
	}

	public void addIcon(int iconItemID) {
		this.iconItemID = iconItemID;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOADING)
		{
			object = null;
		}
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		handleObjects(event.getGameObject());
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		if (event.getGameObject().equals(object))
		{
			object = null;
			client.clearHintArrow();
		}
	}

	@Subscribe
	public void onGroundObjectSpawned(GroundObjectSpawned event)
	{
		handleObjects(event.getGroundObject());
	}

	@Subscribe
	public void onGroundObjectDespawned(GroundObjectDespawned event)
	{
		if (event.getGroundObject().equals(object))
		{
			object = null;
			client.clearHintArrow();
		}
	}

	@Subscribe
	public void onDecorativeObjectSpawned(DecorativeObjectSpawned event)
	{
		handleObjects(event.getDecorativeObject());
	}

	@Subscribe
	public void onDecorativeObjectDespawned(DecorativeObjectDespawned event)
	{
		if (event.getDecorativeObject().equals(object))
		{
			object = null;
			client.clearHintArrow();
		}
	}

	@Override
	public void makeWorldOverlayHint(Graphics2D graphics, QuestHelperPlugin plugin)
	{
		super.makeWorldOverlayHint(graphics, plugin);
		if (objects == null)
		{
			return;
		}
		Point mousePosition = client.getMouseCanvasPosition();
		for (TileObject tileObject : objects)
		{
			OverlayUtil.renderHoverableArea(graphics, tileObject.getClickbox(), mousePosition,
				CLICKBOX_FILL_COLOR, CLICKBOX_BORDER_COLOR, CLICKBOX_HOVER_BORDER_COLOR);
//			if(this.iconItemID != -1) {
//				LocalPoint lp = LocalPoint.fromWorld(client, worldPoint);
//				if(lp == null) {
//					return;
//				}
//				OverlayUtil.renderTileOverlay(client, graphics, lp, itemManager.getImage(this.iconItemID), Color.CYAN);
//			}
		}
	}

	private void handleObjects(TileObject object)
	{
		if (object == null)
		{
			return;
		}

		if (object.getId() == objectID)
		{
			if (object.getWorldLocation().equals(worldPoint))
			{
				this.object = object;
				this.objects.add(object);
				client.setHintArrow(worldPoint);
				return;
			}
			this.objects.add(object);
		}

		// Check impostors
		final ObjectComposition comp = client.getObjectDefinition(object.getId());
		final int[] impostorIds = comp.getImpostorIds();

		if (impostorIds != null && Ints.contains(comp.getImpostorIds(), objectID))
		{
			if (object.getWorldLocation().equals(worldPoint))
			{
				this.object = object;
				client.setHintArrow(worldPoint);    //TODO: better object arrows, probably hydrox's thing
			}
			this.objects.add(object);
		}
	}
}
