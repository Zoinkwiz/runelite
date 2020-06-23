package net.runelite.client.plugins.questhelper.steps;

import com.google.common.primitives.Ints;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.ObjectComposition;
import static net.runelite.api.Perspective.SCENE_SIZE;
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
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.questhelper.ItemRequirement;
import net.runelite.client.plugins.questhelper.QuestHelperPlugin;
import static net.runelite.client.plugins.questhelper.QuestHelperWorldOverlay.CLICKBOX_BORDER_COLOR;
import static net.runelite.client.plugins.questhelper.QuestHelperWorldOverlay.CLICKBOX_FILL_COLOR;
import static net.runelite.client.plugins.questhelper.QuestHelperWorldOverlay.CLICKBOX_HOVER_BORDER_COLOR;
import net.runelite.client.plugins.questhelper.questhelpers.QuestHelper;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.ImageUtil;

public class ObjectStep extends DetailedQuestStep
{
	private final int objectID;
	private TileObject object;
	private boolean highlightAll;

	private final List<TileObject> objects = new ArrayList<>();

	public ObjectStep(QuestHelper questHelper, int objectID, WorldPoint worldPoint, String text, ItemRequirement... itemRequirements)
	{
		super(questHelper, worldPoint, text, itemRequirements);
		this.objectID = objectID;
		this.highlightAll = false;
	}

	public ObjectStep(QuestHelper questHelper, int objectID, WorldPoint worldPoint, String text, boolean highlightAll, ItemRequirement... itemRequirements)
	{
		super(questHelper, worldPoint, text, itemRequirements);
		this.highlightAll = highlightAll;
		this.objectID = objectID;
	}

	@Override
	public void startUp()
	{
		super.startUp();

		if (worldPoint != null)
		{
			Collection<WorldPoint> localWorldPoints = WorldPoint.toLocalInstance(client, worldPoint);

			for (WorldPoint point : localWorldPoints)
			{
				LocalPoint localPoint = LocalPoint.fromWorld(client, point);
				if (localPoint == null)
				{
					return;
				}

				Tile tile = client.getScene().getTiles()[client.getPlane()][localPoint.getSceneX()][localPoint.getSceneY()];
				if (tile != null)
				{
					for (GameObject object : tile.getGameObjects())
					{
						handleObjects(object);
					}

					handleObjects(tile.getDecorativeObject());
					handleObjects(tile.getGroundObject());
					handleObjects(tile.getWallObject());
				}
			}
		}
	}


	@Override
	public void shutDown() {
		super.shutDown();
		this.objects.clear();
	}

	@Subscribe
	@Override
	public void onGameStateChanged(GameStateChanged event)
	{
		super.onGameStateChanged(event);
		if (event.getGameState() == GameState.LOADING)
		{
			object = null;
			objects.clear();
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

	@Subscribe
	public void onWallObjectSpawned(WallObjectSpawned event)
	{
		handleObjects(event.getWallObject());
	}

	@Subscribe
	public void onWallObjectDespawned(WallObjectDespawned event)
	{
		if (event.getWallObject().equals(object))
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
			if (tileObject.getPlane() == client.getPlane())
			{
				OverlayUtil.renderHoverableArea(graphics, tileObject.getClickbox(), mousePosition,
					CLICKBOX_FILL_COLOR, CLICKBOX_BORDER_COLOR, CLICKBOX_HOVER_BORDER_COLOR);
			}
		}

		if (iconItemID != -1 && object != null) {
			LocalPoint lp = LocalPoint.fromWorld(client, object.getWorldLocation());
			if (lp != null)
			{
				System.out.println("IN THE THING");
				addItemImageToLocation(graphics, lp);
			}
		}
	}

	private void handleObjects(TileObject object)
	{
		if (object == null)
		{
			return;
		}

		Collection<WorldPoint> localWorldPoints = null;
		if (worldPoint != null)
		{
			localWorldPoints = WorldPoint.toLocalInstance(client, worldPoint);
		}

		if (object.getId() == objectID)
		{
			if (localWorldPoints != null && localWorldPoints.contains(object.getWorldLocation()))
			{
				this.object = object;
				this.objects.add(object);
				client.setHintArrow(object.getWorldLocation());
				return;
			}
			if (highlightAll)
			{
				this.objects.add(object);
			}
		}

		// Check impostors
		final ObjectComposition comp = client.getObjectDefinition(object.getId());
		final int[] impostorIds = comp.getImpostorIds();

		if (impostorIds != null && Ints.contains(comp.getImpostorIds(), objectID))
		{
			if (localWorldPoints.contains(object.getWorldLocation()))
			{
				this.object = object;
				client.setHintArrow(object.getWorldLocation());    //TODO: better object arrows, probably hydrox's thing
			}
			if (highlightAll)
			{
				this.objects.add(object);
			}
		}
	}
}
