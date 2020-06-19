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
	private final JPanel bodyPanel = new JPanel();

    public QuestStepPanel(PanelDetails panelDetails) {
		setLayout(new BorderLayout(0, 1));
		setBorder(new EmptyBorder(5, 0, 0, 0));

		JLabel headerLabel = new JLabel();
		headerLabel.setText(panelDetails.getHeader());
		headerLabel.setFont(FontManager.getRunescapeSmallFont());
		headerLabel.setForeground(Color.WHITE);
		headerLabel.setMinimumSize(new Dimension(1, headerLabel.getPreferredSize().height));

		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
		headerPanel.setBorder(new EmptyBorder(7, 7, 7, 7));
		headerPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
		headerPanel.add(Box.createRigidArea(new Dimension(TITLE_PADDING, 0)));
		headerPanel.add(headerLabel);

		bodyPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		bodyPanel.setLayout(new BorderLayout());
		bodyPanel.setBorder(new EmptyBorder(10, 5, 10, 5));

		JLabel body = new JLabel();
		body.setHorizontalAlignment(SwingConstants.LEFT);
		body.setVerticalAlignment(SwingConstants.TOP);
		StringBuilder text = new StringBuilder();

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

		for(QuestStep step : panelDetails.getSteps())
		{
			if(!text.toString().equals("")) {
				text.append("<br><br>");
			}
			text.append(step.getText());
		}
		body.setText("<html><body style = 'text-align:left'>" + text + "</body></html>");
		bodyPanel.add(body, BorderLayout.CENTER);

		add(headerPanel, BorderLayout.NORTH);
		add(bodyPanel, BorderLayout.CENTER);
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