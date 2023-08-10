package games.stendhal.client.gui;

import static org.junit.Assert.*;
import static utilities.SpeakerNPCTestHelper.getReply;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import utilities.QuestHelper;
import utilities.PlayerTestHelper;



import games.stendhal.server.actions.query.ProgressStatusQueryAction;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
//import games.stendhal.server.actions.equip.EquipAction;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.fado.bank.TellerNPC;


public class bankStatementTest extends ProgressStatusQueryAction{
	ProgressStatusQueryAction tabs = new ProgressStatusQueryAction();
	private Player arbPlayer = null;
	private Player bankPlayer = null;
	private SpeakerNPC teller = null;
	private Engine en = null;
	List<String> correctList = Arrays.asList("Open Quests","Completed Quests","Production","Bank Statement");
	List<String> correctNameList = Arrays.asList("Semos Bank","Ados Bank","Deniran Bank","Fado Bank","Nalwor Bank","Zaras Chest");
	
	@BeforeClass
	public static void init() throws Exception{
		QuestHelper.setUpBeforeClass();
	}
	
	@Before
	public void SetUp() {
		final StendhalRPZone zone = new StendhalRPZone("admin_test");
		arbPlayer = PlayerTestHelper.createPlayer("testDummy1");
		bankPlayer = PlayerTestHelper.createPlayer("bhai");
		new TellerNPC().configureZone(zone, null);
	}
	
	@Test
	public void tabTest() {
		List<String>list1 = tabs.sendProgressTypes(arbPlayer);
		assertEquals(correctList,list1);
	} // testing whether there exists a bank statement tab
	
	@Test
	public void bankListContentTest() {
		try{
			ArrayList <String>list2 = tabs.sendItemList(arbPlayer, "Bank Statement");
			assertEquals(correctNameList,list2);
		}
		catch(Exception e){
			assertEquals(0,1);
		} // Check if all the banks are under the left side of bank statement tab
	}
	
	@Test
	public void fadoBankConversationTest() {
		teller = SingletonRepository.getNPCList().get("Yance");
		en = teller.getEngine();
		
		en.step(bankPlayer, "hi");
		assertEquals("Welcome to the Fado Bank! Do you need #help or do you want some #offer?",getReply(teller));
		en.step(bankPlayer, "offer");
		assertEquals("Do you want your bank statement?",getReply(teller));
		en.step(bankPlayer, "yes");
		assertEquals("No problem! I'm getting your statement.... Reply #show to reveal.",getReply(teller));
		en.step(bankPlayer, "show");
		assertEquals("\nYour Fado Bank assets are: \n",getReply(teller));
		en.step(bankPlayer, "bye");
		assertEquals("Good to see you, have a nice day!",getReply(teller));
	} // check the conversation, if the reply contains bank statement as expected.
}
	
	
	
	
	
	
	
	
	