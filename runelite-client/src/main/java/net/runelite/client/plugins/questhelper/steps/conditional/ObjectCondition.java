/*
 * Copyright (c) 2020, Zoinkwiz
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
