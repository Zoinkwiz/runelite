package net.runelite.client.plugins.questhelper.steps.conditional;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.NullObjectID;
import net.runelite.api.ObjectID;
import static net.runelite.api.Perspective.SCENE_SIZE;
import net.runelite.api.Point;
import net.runelite.api.Tile;

public class ObjectCondition extends ConditionForStep
{
	private int objectID;

	public ObjectCondition(int objectID) {
		this.objectID = objectID;
	}

	public boolean checkCondition(Client client)
	{
		Tile[][] tiles = client.getScene().getTiles()[client.getPlane()];

		for (int x = 0; x < SCENE_SIZE; x++)
		{
			for (int y = 0; y < SCENE_SIZE; y++)
			{
				if (tiles[x][y] == null)
				{
					continue;
				}

				GameObject[] gameObjects = tiles[x][y].getGameObjects();

				if (gameObjects != null)
				{
					for (int i = 0; i < gameObjects.length; i++)
					{
						if(gameObjects[i] != null && gameObjects[i].getId() == objectID) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}
}
