/* This file is automatically generated. Do not edit. */
package net.runelite.client.ui.overlay.worldmap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum of the index and text of each map area of the world map as it appears in the world map area selection dropdown.
 */
@RequiredArgsConstructor
@Getter
public enum WorldMapArea
{
	ANY("Any"),
	MAIN("Gielinor Surface"),
	ANCIENT_CAVERN("Ancient Cavern"),
	ARDOUGNE_UNDERGROUND("Ardougne Underground"),
	ASGARNIA_ICE_DUNGEON("Asgarnia Ice Cave"),
	BRAINDEATH_ISLAND("Braindeath Island"),
	DORGESHKAAN("Dorgesh-Kaan"),
	DWARVEN_MINES("Dwarven Mines"),
	GODWARS("God Wars Dungeon"),
	GHORROCK_PRISON("Ghorrock Prison"),
	KARAMJA_UNDERGROUND("Karamja Underground"),
	KELDAGRIM("Keldagrim"),
	MISCELLANIA_UNDERGROUND("Miscellania Underground"),
	MISTHALIN_UNDERGROUND("Misthalin Underground"),
	MOLE("Mole Hole"),
	MORYTANIA_UNDERGROUND("Morytania Underground"),
	MOSLEHARMLESS_CAVE("Mos Le'Harmless Cave"),
	OURANIA("Ourania Altar"),
	SLAYER_CAVE("Fremennik Slayer Cave"),
	SOS("Stronghold of Security"),
	STRONGHOLD_UNDERGROUND("Stronghold Underground"),
	TAVERLEY_UNDERGROUND("Taverley Underground"),
	TOLNA("Tolna's Rift"),
	TROLL_STRONGHOLD("Troll Stronghold"),
	TZHAAR_AREA("Mor Ul Rek"),
	UNDEAD_DUNGEON("Lair of Tarn Razorlor"),
	WATERBIRTH("Waterbirth Dungeon"),
	WILDERNESS_DUNGEONS("Wilderness Dungeons"),
	YANILLE_UNDERGROUND("Yanille Underground"),
	ZANARIS("Zanaris"),
	PRIFDDINAS("Prifddinas"),
	FOSSIL_UNDERGROUND("Fossil Island Underground"),
	FELDIP_UNDERGROUND("Feldip Hills Underground"),
	KOUREND_UNDERGROUND("Kourend Underground"),
	KEBOS_UNDERGROUND("Kebos Underground"),
	PRIFDDINAS_UNDERGROUND("Prifddinas Underground"),
	GRAND_LIBRARY("Prifddinas Grand Library"),
	BR_DEFAULT("LMS Desert Island"),
	TUTORIAL_2("Tutorial Island"),
	BR_DARK_VARROCK("LMS Wild Varrock"),
	CAMDOZAAL("Ruins of Camdozaal"),
	THE_ABYSS("The Abyss"),
	LASSAR_UNDERCITY("Lassar Undercity"),
	DESERT_UNDERGROUND("Kharidian Desert Underground"),
	VARLAMORE_UNDERGROUND("Varlamore Underground"),
	CAM_TORUM("Cam Torum"),
	NEYPOTZLI("Neypotzli"),
	;

	private static final WorldMapArea[] AREAS = values();

	private final String name;

	public static WorldMapArea fromId(int areaId)
	{
		if (areaId < 0 || areaId >= AREAS.length) return ANY;
		return AREAS[areaId];
	}

	public static WorldMapArea fromName(String name)
	{
		for (WorldMapArea area : AREAS)
		{
			if (area.getName().equals(name))
			{
				return area;
			}
		}
		return ANY;
	}
	/* This file is automatically generated. Do not edit. */
}
