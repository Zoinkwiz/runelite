package net.runelite.client.plugins.questhelper.steps.choice;

import net.runelite.api.widgets.WidgetInfo;

public class DialogChoiceStep extends WidgetChoiceStep
{
	public DialogChoiceStep(String choice)
	{
		super(choice, WidgetInfo.DIALOG_CHOICE.getGroupId(), WidgetInfo.DIALOG_CHOICE.getChildId());
	}

	public DialogChoiceStep(int choice)
	{
		super(choice, WidgetInfo.DIALOG_CHOICE.getGroupId(), WidgetInfo.DIALOG_CHOICE.getChildId());
	}
}
