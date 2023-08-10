/***************************************************************************
 *                   (C) Copyright 2003-2016 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.actions.query;

import static games.stendhal.common.constants.Actions.PROGRESS_STATUS;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import games.stendhal.server.actions.ActionListener;
import games.stendhal.server.actions.CommandCenter;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.rp.StendhalQuestSystem;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.events.ProgressStatusEvent;
//import games.stendhal.server.entity.RPEntity;
//import games.stendhal.server.entity.mapstuff.chest.PersonalChest;
//import marauroa.common.game.RPObject;
import marauroa.common.game.RPAction;

/**
 * queries the server about the progress.
 *
 * @author hendrik
 */
public class ProgressStatusQueryAction implements ActionListener {


	/**
	 * registers this action.
	 */
	public static void register() {
		CommandCenter.register(PROGRESS_STATUS, new ProgressStatusQueryAction());
	}

	/**
	 * processes the requested action.
	 *
	 * @param player the caller of the action
	 * @param action the action to be performed
	 */
	@Override
	public void onAction(final Player player, final RPAction action) {
		if (!action.has("progress_type")) {
			sendProgressTypes(player);
		} else if (!action.has("item")) {
			sendItemList(player, action.get("progress_type"));
		} else {
			sendDetails(player, action.get("progress_type"), action.get("item"));
		}

		player.notifyWorldAboutChanges();
	}

	/**
	 * sends a list of supported progress types
	 *
	 * @param player Player to sent the event to
	 */
	public List<String> sendProgressTypes(Player player) {
		List<String> list = Arrays.asList("Open Quests", "Completed Quests", "Production", "Bank Statement"); // adding the bank statement tab in travel log window
		player.addEvent(new ProgressStatusEvent(list));
		player.notifyWorldAboutChanges();
		return list;
	}
	
	/**
	 * sends a list of items in the specified progress types
	 *
	 * @param player Player to sent the event to
	 * @param progressType the type of progress the player is interested in
	 */
	public ArrayList<String> sendItemList(Player player, String progressType) {
		boolean banksComplete = false;
		ArrayList<String> banks = new ArrayList<String>();
		if (progressType.equals("Open Quests")) {
			player.addEvent(new ProgressStatusEvent(progressType,
					SingletonRepository.getStendhalQuestSystem().getOpenQuests(player)));
		} else if (progressType.equals("Completed Quests")) {
			// Send first the list of the quests that can be repeated
			player.addEvent(new ProgressStatusEvent("repeatable",
					SingletonRepository.getStendhalQuestSystem().getRepeatableQuests(player)));
			player.addEvent(new ProgressStatusEvent(progressType,
					SingletonRepository.getStendhalQuestSystem().getCompletedQuests(player)));
		} else if (progressType.equals("Production")) {
			player.addEvent(new ProgressStatusEvent(progressType,
					SingletonRepository.getProducerRegister().getWorkingProducerNames(player)));
		}else if (progressType.equals("Bank Statement")) {
			banks.add("Semos Bank");
			banks.add("Ados Bank");
			banks.add("Deniran Bank");
			banks.add("Fado Bank");
			banks.add("Nalwor Bank");
			banks.add("Zaras Chest"); // adding all the banks into a list.
			player.addEvent(new ProgressStatusEvent(progressType,banks)); //add the list to left side of the travel log board
			banksComplete = true;
		}
		player.notifyWorldAboutChanges();
		if (banksComplete == true) {
			return banks;
		}else {
			return null;
		}
	}

	/**
	 * sends details about a selected item
	 *
	 * @param player Player to sent the event to
	 * @param progressType the type of progress the player is interested in
	 * @param item name of item to get details about
	 */
	private void sendDetails(Player player, String progressType, String item) {
		StendhalQuestSystem questSystem = SingletonRepository.getStendhalQuestSystem();
		if (progressType.equals("Open Quests") || progressType.equals("Completed Quests")) {
			player.addEvent(new ProgressStatusEvent(progressType, item,
					questSystem.getQuestDescription(player, item),
					questSystem.getQuestLevelWarning(player, item),
					questSystem.getQuestProgressDetails(player, item)));
		} else if (progressType.equals("Production")) {
			player.addEvent(new ProgressStatusEvent(progressType, item,
					SingletonRepository.getProducerRegister().getProductionDescription(player, item),
					SingletonRepository.getProducerRegister().getProductionDetails(player, item)));
		}else if (progressType.equals("Bank Statement")) {
			player.addEvent(new ProgressStatusEvent(progressType, item,
					questSystem.getQuestDescription(player, item),"",
					questSystem.getBankStatementDetail(player, item))); // add the detailed bank statement to right side of travel log board.
		}
		player.notifyWorldAboutChanges();
	}
}
