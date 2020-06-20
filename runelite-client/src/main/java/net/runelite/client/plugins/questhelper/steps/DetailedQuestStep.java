/*
 * Copyright (c) 2020, Zoinkwiz <https://github.com/Zoinkwiz>
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
package net.runelite.client.plugins.questhelper.steps;

import com.google.inject.Binder;
import com.google.inject.Inject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import net.runelite.api.Perspective;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.questhelper.ItemRequirement;
import net.runelite.client.plugins.questhelper.QuestHelperPlugin;
import net.runelite.client.plugins.questhelper.QuestHelperWorldMapPoint;
import net.runelite.client.plugins.questhelper.questhelpers.QuestHelper;
import net.runelite.client.plugins.questhelper.steps.choice.DialogChoiceSteps;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.worldmap.WorldMapPointManager;

public class DetailedQuestStep extends QuestStep
{
	@Inject
	ItemManager itemManager;

	@Inject
	WorldMapPointManager worldMapPointManager;

	protected WorldPoint worldPoint;
	protected List<ItemRequirement> itemRequirements = new ArrayList<>();
	protected HashMap<Integer, List<Tile>> tileHighlights = new HashMap<>();

	@Getter
	public DialogChoiceSteps choices = new DialogChoiceSteps();

	public DetailedQuestStep(QuestHelper questHelper, String text, ItemRequirement... itemRequirements)
	{
		super(questHelper, text);
		this.itemRequirements.addAll(Arrays.asList(itemRequirements));
	}

	public DetailedQuestStep(QuestHelper questHelper, WorldPoint worldPoint, String text, ItemRequirement... itemRequirements)
	{
		super(questHelper, text);
		this.worldPoint = worldPoint;
		this.itemRequirements.addAll(Arrays.asList(itemRequirements));
	}

	@Override
	public void configure(Binder binder)
	{
	}

	@Override
	public void startUp()
	{
		if (worldPoint != null)
		{
			worldMapPointManager.add(new QuestHelperWorldMapPoint(worldPoint, getQuestImage()));
			client.setHintArrow(worldPoint);
		}
		addItemTiles();
	}

	@Override
	public void shutDown()
	{
		worldMapPointManager.removeIf(QuestHelperWorldMapPoint.class::isInstance);
		tileHighlights.clear();
		client.clearHintArrow();
	}

	public void makeOverlayHint(PanelComponent panelComponent, QuestHelperPlugin plugin)
	{
		super.makeOverlayHint(panelComponent, plugin);

		if (itemRequirements.isEmpty())
		{
			return;
		}

		panelComponent.getChildren().add(LineComponent.builder().left("Required Items:").build());
		for (ItemRequirement itemRequirement : itemRequirements)
		{
			String text = itemRequirement.getQuantity() + " x " + itemRequirement.getName();
			Color color;
			if (itemRequirement.check(client))
			{
				color = Color.GREEN;
			}
			else
			{
				color = Color.RED;
			}
			panelComponent.getChildren().add(LineComponent.builder()
				.left(text)
				.leftColor(color)
				.build());
			if (itemRequirement.getTip() != null && color == Color.RED)
			{
				panelComponent.getChildren().add(LineComponent.builder()
					.left("- " + itemRequirement.getTip())
					.leftColor(Color.WHITE)
					.build());
			}
		}
	}

	@Subscribe
	public void onItemSpawned(ItemSpawned itemSpawned)
	{
		final TileItem item = itemSpawned.getItem();
		for (ItemRequirement itemRequirement : itemRequirements)
		{
			if (itemRequirement.getId() == item.getId())
			{
				tileHighlights.computeIfAbsent(item.getId(), k -> new ArrayList<>());
				if (!tileHighlights.get(item.getId()).contains(itemSpawned.getTile()))
				{
					tileHighlights.get(item.getId()).add(itemSpawned.getTile());
				}
			}
		}
	}

	@Subscribe
	public void onItemDespawned(ItemDespawned itemDespawned)
	{
		List<TileItem> items = itemDespawned.getTile().getGroundItems();
		boolean itemStillOnTile = false;
		if (items != null)
		{
			for (TileItem item : items)
			{
				if (item.getId() == itemDespawned.getItem().getId())
				{
					itemStillOnTile = true;
				}
			}
			if (!itemStillOnTile)
			{
				List<Tile> currentTile = tileHighlights.get(itemDespawned.getItem().getId());
				if (currentTile != null)
				{
					currentTile.remove(itemDespawned.getTile());
				}
			}
		}
	}

	private void addItemTiles() {
		Tile[][] squareOfTiles = client.getScene().getTiles()[client.getPlane()];
		for (Tile[] lineOfTiles : squareOfTiles)
		{
			for (Tile tile : lineOfTiles)
			{
				if(tile != null)
				{
					List<TileItem> items = tile.getGroundItems();
					if (items != null)
					{
						for (TileItem item : items)
						{
							for (ItemRequirement itemRequirement : itemRequirements)
							{
								if (item.getId() == itemRequirement.getId())
								{
									tileHighlights.computeIfAbsent(item.getId(), k -> new ArrayList<>());
									if (!tileHighlights.get(item.getId()).contains(tile))
									{
										tileHighlights.get(item.getId()).add(tile);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void makeWorldOverlayHint(Graphics2D graphics, QuestHelperPlugin plugin)
	{
		itemRequirements.forEach(itemRequirement -> {
			if (!itemRequirement.check(client))
			{
				List<Tile> tiles = tileHighlights.get(itemRequirement.getId());
				if(tiles != null)
				{
					for (Tile tile : tiles)
					{
						final LocalPoint location = tile.getLocalLocation();
						if (location == null)
						{
							return;
						}

						Polygon poly = Perspective.getCanvasTilePoly(client, location);
						if (poly == null)
						{
							return;
						}

						OverlayUtil.renderPolygon(graphics, poly, Color.CYAN);
					}
				}
			}
		});
	}
}
