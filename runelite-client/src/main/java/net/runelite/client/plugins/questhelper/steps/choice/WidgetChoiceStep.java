package net.runelite.client.plugins.questhelper.steps.choice;

import java.awt.Color;
import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.widgets.JavaScriptCallback;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

public class WidgetChoiceStep
{
    @Getter
    private final String choice;

    private final int choiceById;

    private final int groupId;
    private final int childId;

    private final int TEXT_HIGHLIGHT_COLOR = Color.CYAN.darker().getRGB();

    public WidgetChoiceStep(String choice, int groupId, int childId)
    {
        this.choice = choice;
        this.choiceById = -1;
        this.groupId = groupId;
        this.childId = childId;
    }

	public WidgetChoiceStep(int choiceId, int groupId, int childId)
	{
		this.choice = null;
		this.choiceById = choiceId;
		this.groupId = groupId;
		this.childId = childId;
	}

    public void highlightChoice(Client client) {
		Widget dialogChoice = client.getWidget(groupId, childId);

		if (dialogChoice != null) {
			Widget[] choices = dialogChoice.getChildren();
			if (choices != null) {
				if (choiceById != -1 && choices[choiceById] != null)
				{
					highlightText(choices[choiceById]);
				}
				else
				{
					for (Widget currentChoice : choices)
					{
						if (currentChoice.getText().equals(choice))
						{
							highlightText(currentChoice);
							return;
						}
					}
				}
			}
		}
	}

	private void highlightText(Widget text) {
		text.setTextColor(TEXT_HIGHLIGHT_COLOR);
		text.setOnMouseLeaveListener((JavaScriptCallback) ev -> text.setTextColor(TEXT_HIGHLIGHT_COLOR));
	}
}