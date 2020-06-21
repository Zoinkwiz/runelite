/*
 * Copyright (c) 2019, Trevor <https://github.com/Trevor159>
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
import com.google.inject.Module;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.SpriteID;
import net.runelite.api.Varbits;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.questhelper.questhelpers.QuestHelper;
import static net.runelite.client.plugins.questhelper.QuestHelperOverlay.TITLED_CONTENT_COLOR;
import net.runelite.client.plugins.questhelper.QuestHelperPlugin;
import net.runelite.client.plugins.questhelper.steps.choice.DialogChoiceStep;
import net.runelite.client.plugins.questhelper.steps.choice.DialogChoiceSteps;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public abstract class QuestStep implements Module
{
	@Inject
	protected Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	SpriteManager spriteManager;

	@Setter
	@Getter
	protected String text;

	private int currentChoice = 0;

	@Getter
	protected final QuestHelper questHelper;

	@Getter
	protected DialogChoiceSteps choices = new DialogChoiceSteps();

	public QuestStep(QuestHelper questHelper, String text)
	{
		this.text = text;
		this.questHelper = questHelper;
	}

	@Override
	public void configure(Binder binder)
	{
	}

	public void startUp()
	{
	}

	public void shutDown()
	{
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		int newChoice = client.getVar(Varbits.DIALOG_CHOICE);
		if(currentChoice == 0 && newChoice == 1) {
			clientThread.invokeLater(this::highlightChoice);
		}
		currentChoice = newChoice;
	}

	public void highlightChoice() {
		choices.checkChoices(client);
	}

	public void addDialogStep(String choice)
	{
		choices.addChoice(new DialogChoiceStep(choice));
	}

	public void makeOverlayHint(PanelComponent panelComponent, QuestHelperPlugin plugin)
	{
		panelComponent.getChildren().add(TitleComponent.builder().text(questHelper.getQuest().getName()).build());

		panelComponent.getChildren().add(LineComponent.builder()
			.left(text)
			.leftColor(TITLED_CONTENT_COLOR)
			.build());
	}

	public void makeWorldOverlayHint(Graphics2D graphics, QuestHelperPlugin plugin)
	{
	}

	public QuestStep getActiveStep() {
		return this;
	}

	public BufferedImage getQuestImage()
	{
		return spriteManager.getSprite(SpriteID.TAB_QUESTS, 0);
	}
}
