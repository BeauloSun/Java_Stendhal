package games.stendhal.server.script;
import java.util.List;
import games.stendhal.common.MathHelper;
import games.stendhal.server.core.scripting.ScriptImpl;
import games.stendhal.server.entity.mapstuff.HandCart.HandCart;
import games.stendhal.server.entity.player.Player;

public class SummonHandCart extends ScriptImpl {

	// Expected args include zone to add item to and x, y coords
	public void add(final Player player, final List<String> args) {
		// zone x, y coords = 3 params
		if (args.size() == 3) {

			final String myZone = args.get(0);
			if ("-".equals(myZone)) {
				// set the zone as the player's current zone
				sandbox.setZone(sandbox.getZone(player));
			} else {
				if (!sandbox.setZone(myZone)) {
					sandbox.privateText(player, "Zone not found.");
					return;
				}
			}
			// parsing user input in command
			int x = 0;
			if ("-".equals(args.get(1))) {
				x = player.getX();
			} else {
				x = MathHelper.parseInt(args.get(1));
			}
			int y = 0;
			if ("-".equals(args.get(2))) {
				y = player.getY();
			} else {
				y = MathHelper.parseInt(args.get(2));
			}

			// create the hand cart and add it at x, y 
			final HandCart hc = new HandCart();
			hc.setPosition(x, y);

			sandbox.add(hc);


		} else {
			// syntax error: print help text
			sandbox.privateText(
					player,
					"This script creates, lists or removes hand carts. Syntax: \r\nSummonHandCart.class <zone> <x> <y>. The first 3 parameters can be \"-\".\r\nSummonHandCart.class list\r\nSummonHandCart.class del <n>");
		}
	}


	@Override
	public void execute(final Player admin, final List<String> args) {
		// add to zone
		add(admin, args);
	}

}