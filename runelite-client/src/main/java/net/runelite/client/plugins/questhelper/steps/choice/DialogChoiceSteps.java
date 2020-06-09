package net.runelite.client.plugins.questhelper.steps.choice;

import lombok.Getter;
import net.runelite.api.Client;

import java.util.ArrayList;
import java.util.Collections;

public class DialogChoiceSteps
{
    @Getter
    final private ArrayList<DialogChoiceStep> choices = new ArrayList<>();

	public DialogChoiceSteps(DialogChoiceStep... choices) {
        Collections.addAll(this.choices, choices);
    }

    public void addChoice(DialogChoiceStep choice) {
        choices.add(choice);
    }

    public void checkChoices(Client client) {
        if (choices.size() == 0) {
            return;
        }

        for (DialogChoiceStep currentChoice : choices) {
			currentChoice.highlightChoice(client);
        }
    }
}
