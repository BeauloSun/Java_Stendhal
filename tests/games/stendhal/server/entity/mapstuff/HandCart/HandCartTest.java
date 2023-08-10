package games.stendhal.server.entity.mapstuff.HandCart;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import games.stendhal.common.Direction;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.mapstuff.area.AreaEntity;
import games.stendhal.server.entity.mapstuff.block.Block;
import games.stendhal.server.entity.mapstuff.block.BlockTarget;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import marauroa.common.game.RPClass;
import utilities.PlayerTestHelper;
import utilities.RPClass.EntityTestHelper;



public class HandCartTest {
	
  	@BeforeClass
	public static void beforeClass() {
  	  // Init RP classes
      generateRPClasses();
      PlayerTestHelper.generatePlayerRPClasses();
      MockStendlRPWorld.get();
    }
  	
  	// Generate appropriate RP classes that block uses
    public static void generateRPClasses() {
        EntityTestHelper.generateRPClasses();
        if(!RPClass.hasRPClass("area")) AreaEntity.generateRPClass();
        if(!RPClass.hasRPClass("block")) Block.generateRPClass();
        if(!RPClass.hasRPClass("blocktarget")) BlockTarget.generateRPClass();
      }
    
      
    @Test
    public void testPush() {
      // Instantiate a hand cart
      HandCart hc = new HandCart();
      hc.setPosition(0, 0);
      // Currently restricted to admins
      StendhalRPZone zone = new StendhalRPZone("admin_test", 10, 10);
      zone.add(hc);
      // Create a player that will push the hand cart
      Player p = PlayerTestHelper.createPlayer("pusher");

      // Cart should be at starting coords of 0,0
      assertThat(Integer.valueOf(hc.getX()), is(Integer.valueOf(0)));
      assertThat(Integer.valueOf(hc.getY()), is(Integer.valueOf(0)));

      // Pushing the cart eastwards coords should be 1, 0
      hc.push(p, Direction.RIGHT);
      assertThat(Integer.valueOf(hc.getX()), is(Integer.valueOf(1)));
      assertThat(Integer.valueOf(hc.getY()), is(Integer.valueOf(0)));

      // Pushing the cart westwards coords should be 0,0 starting at 1,0 and so on
      hc.push(p, Direction.LEFT);
      assertThat(Integer.valueOf(hc.getX()), is(Integer.valueOf(0)));
      assertThat(Integer.valueOf(hc.getY()), is(Integer.valueOf(0)));


      hc.push(p, Direction.DOWN);
      assertThat(Integer.valueOf(hc.getX()), is(Integer.valueOf(0)));
      assertThat(Integer.valueOf(hc.getY()), is(Integer.valueOf(1)));


      hc.push(p, Direction.UP);
      assertThat(Integer.valueOf(hc.getX()), is(Integer.valueOf(0)));
      assertThat(Integer.valueOf(hc.getY()), is(Integer.valueOf(0)));
    }
    
	@Test
	public void testCoordinatesAfterPush() {
		// Create new hand cart and set to 0,0
		HandCart hc = new HandCart();
		hc.setPosition(0, 0);
		// Examine if coords after pushing correspond to given directions
		assertThat(Integer.valueOf(hc.getXAfterPush(Direction.UP)), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(hc.getYAfterPush(Direction.UP)), is(Integer.valueOf(-1)));

		assertThat(Integer.valueOf(hc.getXAfterPush(Direction.DOWN)), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(hc.getYAfterPush(Direction.DOWN)), is(Integer.valueOf(1)));

		assertThat(Integer.valueOf(hc.getXAfterPush(Direction.LEFT)), is(Integer.valueOf(-1)));
		assertThat(Integer.valueOf(hc.getYAfterPush(Direction.LEFT)), is(Integer.valueOf(0)));

		assertThat(Integer.valueOf(hc.getXAfterPush(Direction.RIGHT)), is(Integer.valueOf(1)));
		assertThat(Integer.valueOf(hc.getYAfterPush(Direction.RIGHT)), is(Integer.valueOf(0)));
	}
    
    
  	@Test
	public final void entityRestrictions() {
  		// Create new hand cart object
  		HandCart hc = new HandCart();
  		final String playername = "dummyplayer";
		Player p = PlayerTestHelper.createPlayer(playername);
	    p.setAdminLevel(5000);
	    // Check if an admin with lvl 5k can interact with a hand cart
	    assertThat(p.getAdminLevel(), is(Integer.valueOf(5000)));
	    assertTrue(hc.canBePushed(p) == true);
	  }
}