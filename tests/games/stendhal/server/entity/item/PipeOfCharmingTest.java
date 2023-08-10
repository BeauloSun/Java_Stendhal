package games.stendhal.server.entity.item;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.common.Direction;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.creature.Creature;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import utilities.PlayerTestHelper;
import utilities.RPClass.CreatureTestHelper;

public class PipeOfCharmingTest {

	@BeforeClass
	public static void setUpWorld() {
		MockStendlRPWorld.get();
		CreatureTestHelper.generateRPClasses();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		PlayerTestHelper.removePlayer("player");
	}
	
	/**
	 * Test while player holds pipe, hostile creatures do not attack them
	*/
	@Test
	public void testPlayerIsNotAttackedByHostileCreatureWhileHoldingPipe() {
		
		// Create a player, a creature and a pipe item
		final Creature creature = SingletonRepository.getEntityManager().getCreature("rat");
		assertNotNull(creature);
		final Player player = PlayerTestHelper.createPlayer("player");
		assertNotNull(player);
		Item pipe = new Item("charming_pipe", "pipe", "subclass", new HashMap<String, String>());
		assertNotNull(pipe);
		
		// Add player and creature in arena
		StendhalRPZone arena = new StendhalRPZone("arena");
		arena.add(creature);
		arena.add(player);
		creature.setTarget(player);
		
		// Check creature is attacking player
		assertTrue(creature.isAttacking());
		
		// Equipe player with pipe
		player.equip("rhand", pipe);
		assertTrue(player.isEquippedItemInSlot("rhand", "charming_pipe"));
		creature.logic();
		
		// Check creature is not attacking player anymore
		assertFalse(creature.isAttacking());
		
		// Drop pipe and check that creature is attacking again
		assertTrue(player.drop(pipe));
		creature.logic();
		assertTrue(creature.isAttacking());
		
	}
	
	/**
	 * Test while player holds pipe, hostile creatures are attracted to them
	*/
	@Test
	public void testPlayerIsAttractingHostileCreatureWhileHoldingPipe() {
		
		// Create a player, a creature and a pipe item
		final Creature creature = SingletonRepository.getEntityManager().getCreature("rat");
		assertNotNull(creature);
		final Player player = PlayerTestHelper.createPlayer("player");
		assertNotNull(player);
		Item pipe = new Item("charming_pipe", "pipe", "subclass", new HashMap<String, String>());
		assertNotNull(pipe);
		
		// Add player and creature in arena
		StendhalRPZone arena = new StendhalRPZone("arena");
		arena.add(creature);
		arena.add(player);
		creature.setTarget(player);
		
		// Make creature not moving
		creature.setDirection(Direction.STOP);
		assertNotEquals(creature.getDirectionToward(player), creature.getDirection());
		
		// Equipe player with pipe
		player.equip("rhand", pipe);
		assertTrue(player.isEquippedItemInSlot("rhand", "charming_pipe"));
		creature.logic();
		
		// Check creature now goes in direction of player
		assertEquals(creature.getDirectionToward(player), creature.getDirection());	
		
	}
	
}

