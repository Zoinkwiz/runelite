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

import com.google.inject.Inject;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.LinkedHashMap;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.questhelper.QuestHelperPlugin;
import net.runelite.client.plugins.questhelper.questhelpers.QuestHelper;
import net.runelite.client.plugins.questhelper.steps.conditional.ConditionForStep;
import net.runelite.client.plugins.questhelper.steps.conditional.Conditions;
import net.runelite.client.plugins.questhelper.steps.conditional.OwnerStep;
import net.runelite.client.ui.overlay.components.PanelComponent;


/* Conditions are checked in the order they were added */
public class ConditionalStep extends QuestStep implements OwnerStep
{
	@Inject
	protected EventBus eventBus;

	private boolean started = false;

	private final LinkedHashMap<Conditions, QuestStep> steps;

	private QuestStep currentStep;

	public ConditionalStep(QuestHelper questHelper, QuestStep step) {
		super(questHelper, null);
		this.steps = new LinkedHashMap<>();
		this.steps.put(null, step);
	}

	public void addStep(Conditions conditions, QuestStep step) {
		this.steps.put(conditions, step);
	}

	public void addStep(ConditionForStep condition, QuestStep step) {
		this.steps.put(new Conditions(condition), step);
	}

	@Override
	public void startUp()
	{
		updateSteps();
		started = true;
	}

	@Override
	public void shutDown()
	{
		started = false;
		shutDownStep();
		currentStep = null;
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (started)
		{
			updateSteps();
		}
	}

	private void updateSteps()
	{
		for (Conditions conditions : steps.keySet())
		{
			if (conditions != null && conditions.checkCondition(client))
			{
				startUpStep(steps.get(conditions));
				return;
			}
		}

		startUpStep(steps.get(null));
	}

	protected void startUpStep(QuestStep step)
	{
		if (currentStep == null)
		{
			eventBus.register(step);
			step.startUp();
			currentStep = step;
			return;
		}

		if (!step.equals(currentStep))
		{
			shutDownStep();
			eventBus.register(step);
			step.startUp();
			currentStep = step;
		}
	}

	private void shutDownStep()
	{
		if (currentStep != null)
		{
			eventBus.unregister(currentStep);
			currentStep.shutDown();
			currentStep = null;
		}
	}

	@Override
	public void makeOverlayHint(PanelComponent panelComponent, QuestHelperPlugin plugin)
	{
		if(currentStep != null)
		{
			currentStep.makeOverlayHint(panelComponent, plugin);
		}
	}

	@Override
	public void makeWorldOverlayHint(Graphics2D graphics, QuestHelperPlugin plugin)
	{
		if(currentStep != null)
		{
			currentStep.makeWorldOverlayHint(graphics, plugin);
		}
	}

	@Override
	public QuestStep getActiveStep() {
		return currentStep.getActiveStep();
	}

	@Override
	public Collection<QuestStep> getSteps()
	{
		return steps.values();
	}
}
