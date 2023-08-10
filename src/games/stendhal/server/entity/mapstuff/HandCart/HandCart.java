package games.stendhal.server.entity.mapstuff.HandCart;

import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.mapstuff.block.Block;

public class HandCart extends Block {
	private boolean open = false;
	
	public HandCart() {
		super(true, "hand_cart");
		setDescription("You see a handCart.You will be able to use it to move items soon!");
	}
	
	public boolean canBePushed(Player p) {
		if (p.getAdminLevel() < 5000)
			return false;
		return true;
	}
	
	public void open() {
		this.open = true;
		put("open", "");
	}
	
	public void close() {
		this.open = false;
		if (has("open")) remove("open");
	}
	
	public boolean isOpen() {
		return open;
	}
}
