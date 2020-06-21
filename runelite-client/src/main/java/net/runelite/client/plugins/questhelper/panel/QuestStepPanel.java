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
package net.runelite.client.plugins.questhelper.panel;

import java.util.ArrayList;
import java.util.HashMap;
import net.runelite.client.plugins.questhelper.ItemRequirement;
import net.runelite.client.plugins.questhelper.steps.QuestStep;
import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import net.runelite.client.ui.FontManager;

public class QuestStepPanel extends JPanel
{
	private static final int TITLE_PADDING = 5;


    private final JPanel headerPanel = new JPanel();
	private final JLabel headerLabel = new JLabel();
	private final JPanel bodyPanel = new JPanel();

	private QuestStep currentlyHighlighted = null;

	private final HashMap<QuestStep, JLabel> steps = new HashMap<>();

    public QuestStepPanel(PanelDetails panelDetails, QuestStep currentStep)
	{
		System.out.println(currentStep.getText());
		setLayout(new BorderLayout(0, 1));
		setBorder(new EmptyBorder(5, 0, 0, 0));

		headerLabel.setText(panelDetails.getHeader());
		headerLabel.setFont(FontManager.getRunescapeBoldFont());

		headerLabel.setMinimumSize(new Dimension(1, headerLabel.getPreferredSize().height));

		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
		headerPanel.setBorder(new EmptyBorder(7, 7, 7, 7));
		if (panelDetails.getSteps().contains(currentStep))
		{
			headerLabel.setForeground(Color.BLACK);
			headerPanel.setBackground(ColorScheme.BRAND_ORANGE);
		}
		else
		{
			headerLabel.setForeground(Color.WHITE);
			headerPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
		}
		headerPanel.add(Box.createRigidArea(new Dimension(TITLE_PADDING, 0)));
		headerPanel.add(headerLabel);

		bodyPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		bodyPanel.setLayout(new BorderLayout());
		bodyPanel.setBorder(new EmptyBorder(10, 5, 10, 5));

		if(!panelDetails.getItemRequirements().isEmpty()) {
			JPanel questItemRequirementsPanel = new JPanel();
			questItemRequirementsPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
			questItemRequirementsPanel.setLayout(new BorderLayout());
			questItemRequirementsPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

			JPanel questItemRequirementsHeader = new JPanel();
			questItemRequirementsHeader.setBackground(ColorScheme.DARKER_GRAY_COLOR);
			questItemRequirementsHeader.setLayout(new BorderLayout());
			questItemRequirementsHeader.setBorder(new EmptyBorder(5, 5, 5, 10));

			JLabel questItemReqsTitle = new JLabel();
			questItemReqsTitle.setForeground(Color.WHITE);
			questItemReqsTitle.setText("Bring the following items:");
			questItemReqsTitle.setMinimumSize(new Dimension(1, questItemRequirementsHeader.getPreferredSize().height));
			questItemRequirementsHeader.add(questItemReqsTitle, BorderLayout.NORTH);

			JPanel questItemRequirementsListPanel = new JPanel();
			questItemRequirementsListPanel.setLayout(new BorderLayout());
			questItemRequirementsListPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

			JLabel itemLabel = new JLabel();
			itemLabel.setForeground(Color.GRAY);
			StringBuilder itemText = new StringBuilder();
			for (ItemRequirement itemRequirement : panelDetails.getItemRequirements())
			{
				if(!itemText.toString().equals("")) {
					itemText.append("<br>");
				}
				itemText.append(itemRequirement.getQuantity()).append(" x ").append(itemRequirement.getName());
			}

			itemLabel.setText("<html><body style = 'text-align:left'>" + itemText + "</body></html>");

			questItemRequirementsListPanel.add(itemLabel);

			questItemRequirementsPanel.add(questItemRequirementsHeader, BorderLayout.NORTH);
			questItemRequirementsPanel.add(questItemRequirementsListPanel, BorderLayout.CENTER);

			bodyPanel.add(questItemRequirementsPanel, BorderLayout.NORTH);
		}

		JPanel questStepsPanel = new JPanel();
		questStepsPanel.setLayout(new BoxLayout(questStepsPanel, BoxLayout.Y_AXIS));
		questStepsPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		for(QuestStep step : panelDetails.getSteps())
		{
			JLabel questStepLabel = new JLabel();
			questStepLabel.setLayout(new BorderLayout());
			questStepLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
			questStepLabel.setHorizontalAlignment(SwingConstants.LEFT);
			questStepLabel.setVerticalAlignment(SwingConstants.TOP);

			StringBuilder text = new StringBuilder();

//			if(!text.toString().equals("")) {
//				text.append("<br><br>");
//			}
			if(step.equals(currentStep)) {
				questStepLabel.setForeground(ColorScheme.BRAND_ORANGE);
			}
			text.append(step.getText());
			questStepLabel.setText("<html><body style = 'text-align:left'>" + text + "</body></html>");

			steps.put(step, questStepLabel);
			questStepsPanel.add(questStepLabel);
		}

		bodyPanel.add(questStepsPanel, BorderLayout.CENTER);

		add(headerPanel, BorderLayout.NORTH);
		add(bodyPanel, BorderLayout.CENTER);

		if (!panelDetails.getSteps().contains(currentStep))
		{
			collapse();
		}
    }

    public ArrayList<QuestStep> getSteps() {
    	return new ArrayList<>(steps.keySet());
	}

    public void updateHighlight(QuestStep currentStep) {
    	if (currentlyHighlighted != null)
		{
			steps.get(currentlyHighlighted).setForeground(Color.WHITE);
		} else {
			headerLabel.setForeground(Color.BLACK);
			headerPanel.setBackground(ColorScheme.BRAND_ORANGE);
		}
		steps.get(currentStep).setForeground(ColorScheme.BRAND_ORANGE);
		currentlyHighlighted = currentStep;
	}

	public void removeHighlight() {
    	if (currentlyHighlighted != null) {
    		headerLabel.setForeground(Color.WHITE);
			if (isCollapsed())
			{
				applyDimmer(false, headerPanel);
			}
    		headerPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
    		steps.get(currentlyHighlighted).setForeground(Color.WHITE);
    		currentlyHighlighted = null;
		}
	}

	void collapse()
	{
		if (!isCollapsed())
		{
			bodyPanel.setVisible(false);
			applyDimmer(false, headerPanel);
		}
	}

	void expand()
	{
		if (isCollapsed())
		{
			bodyPanel.setVisible(true);
			applyDimmer(true, headerPanel);
		}
	}

	boolean isCollapsed()
	{
		return !bodyPanel.isVisible();
	}

	private void applyDimmer(boolean brighten, JPanel panel)
	{
		for (Component component : panel.getComponents())
		{
			Color color = component.getForeground();

			component.setForeground(brighten ? color.brighter() : color.darker());
		}
	}
}