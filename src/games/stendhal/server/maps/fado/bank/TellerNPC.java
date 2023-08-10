/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.maps.fado.bank;

//import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.mapstuff.chest.PersonalChest;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.EventRaiser;
import marauroa.common.game.RPObject;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.player.Player;

/**
 * Builds the bank teller NPC.
 *
 * @author timothyb89
 */
public class TellerNPC implements ZoneConfigurator {
	//
	// ZoneConfigurator
	//

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}

	//
	// IL0_TellerNPC
	//

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC bankNPC = new SpeakerNPC("Yance") {
			
			String statement = "";
			
			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(15, 3));
				nodes.add(new Node(15, 16));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Welcome to the Fado Bank! Do you need #help or do you want some #offer?");
				addJob("I am the manager for the bank.");
				addHelp("Just to the left, you can see a few chests. Open one and you can store your belongings in it.");
				addOffer("Do you want your bank statement?");
				addReply("yes","No problem! I'm getting your statement.... Reply #show to reveal.",
						new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
						getBankStatementDetail(player,statement);
					}
				});
				addGoodbye("Good to see you, have a nice day!");
			}
			
			public void getBankStatementDetail(final Player player, String statement) {
				PersonalChest a = new PersonalChest("bank_fado");
				a.setAttending(player);
				statement = "\nYour Fado Bank assets are: \n";
				for (final RPObject item: a.getBankSlot()) {
					statement += item.get("name");
					statement += "----------------------------";
					if (item.get("quantity") == null) {
						statement += "1";
					}else {
						statement += item.get("quantity");
					}
					statement += "\n";
				}
				addReply("show",statement);
				statement = "";
			}
		};

		bankNPC.setEntityClass("youngnpc");
		bankNPC.setPosition(15, 3);
		bankNPC.initHP(1000);
		bankNPC.setDescription("Yance is the Fado bank manager. He can give advice on how to use the chests.");
		zone.add(bankNPC);
	}
}
