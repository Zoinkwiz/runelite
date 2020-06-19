package net.runelite.client.plugins.questhelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import net.runelite.api.ItemID;

public class ItemCollections
{
	@Getter
	private static List<Integer> axes = new ArrayList<>(Arrays.asList(
		ItemID.BRONZE_AXE,
		ItemID.IRON_AXE,
		ItemID.STEEL_AXE,
		ItemID.BLACK_AXE,
		ItemID.MITHRIL_AXE,
		ItemID.ADAMANT_AXE,
		ItemID.GILDED_AXE,
		ItemID.RUNE_AXE,
		ItemID.DRAGON_AXE,
		ItemID.INFERNAL_AXE,
		ItemID._3RD_AGE_AXE,
		ItemID.CRYSTAL_AXE)
	);

	@Getter
	private static List<Integer> pickaxes = new ArrayList<>(Arrays.asList(
			ItemID.BRONZE_PICKAXE,
			ItemID.IRON_PICKAXE,
			ItemID.STEEL_PICKAXE,
			ItemID.BLACK_PICKAXE,
			ItemID.MITHRIL_PICKAXE,
			ItemID.ADAMANT_PICKAXE,
			ItemID.GILDED_PICKAXE,
			ItemID.RUNE_PICKAXE,
			ItemID.DRAGON_PICKAXE,
			ItemID.INFERNAL_PICKAXE,
			ItemID._3RD_AGE_PICKAXE,
			ItemID.CRYSTAL_PICKAXE)
	);
}
