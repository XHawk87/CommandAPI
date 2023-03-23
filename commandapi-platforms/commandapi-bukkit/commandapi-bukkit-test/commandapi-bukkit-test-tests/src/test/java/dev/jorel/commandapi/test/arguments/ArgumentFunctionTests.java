package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.FunctionArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.test.MockPlatform;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;
import dev.jorel.commandapi.wrappers.FunctionWrapper;

/**
 * Tests for the {@link FunctionArgument}
 */
@SuppressWarnings("deprecation")
class ArgumentFunctionTests extends TestBase {

	/*********
	 * Setup *
	 *********/

	@BeforeEach
	public void setUp() {
		super.setUp();
	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}

	/*********
	 * Tests *
	 *********/

	@Test
	void executionTestWithFunctionArgument() {
		Mut<FunctionWrapper[]> results = Mut.of();
		Mut<String> sayResults = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new FunctionArgument("function"))
			.executesPlayer((player, args) -> {
				results.set((FunctionWrapper[]) args.get("function"));
			})
			.register();

		new CommandAPICommand("mysay")
			.withArguments(new GreedyStringArgument("message"))
			.executesPlayer((player, args) -> {
				sayResults.set(args.getUnchecked("message"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// Declare our functions on the server
		MockPlatform.getInstance().addFunction(new NamespacedKey("ns", "myfunc"), List.of("mysay hi"));

		// Run the /test command
		server.dispatchCommand(player, "test ns:myfunc");

		// Check that the FunctionArgument has one entry and it hasn't run the /mysay
		// command
		FunctionWrapper[] result = results.get();
		assertEquals(1, result.length);
		assertNoMoreResults(sayResults);

		// Run the function (which should run the /mysay command)
		result[0].run();

		// Check that /mysay was run successfully...
		assertEquals("hi", sayResults.get());

		assertNoMoreResults(results);
		assertNoMoreResults(sayResults);
	}

	/********************
	 * Suggestion tests *
	 ********************/

	@Test
	void suggestionTestWithFunctionArgument() {
		new CommandAPICommand("test")
			.withArguments(new FunctionArgument("function"))
			.executesPlayer(P_EXEC)
			.register();

		new CommandAPICommand("mysay")
			.withArguments(new GreedyStringArgument("message"))
			.executesPlayer(P_EXEC)
			.register();

		PlayerMock player = server.addPlayer();

		// Declare our functions on the server
		MockPlatform.getInstance().addFunction(new NamespacedKey("ns", "myfunc"), List.of("mysay hi"));
		MockPlatform.getInstance().addFunction(new NamespacedKey("mynamespace", "myotherfunc"), List.of("mysay bye"));

		// /test
		// Should suggest mynamespace:myotherfunc and ns:myfunc
		assertEquals(List.of("mynamespace:myotherfunc", "ns:myfunc"), server.getSuggestions(player, "test "));
	}

}
