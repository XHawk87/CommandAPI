package dev.jorel.commandapi;

import com.velocitypowered.api.command.CommandSource;
import dev.jorel.commandapi.arguments.Argument;

public class CommandAPICommand extends AbstractCommandAPICommand<CommandAPICommand, Argument<?>, CommandSource> implements VelocityExecutable<CommandAPICommand> {
	/**
	 * Creates a new command builder
	 *
	 * @param commandName The name of the command to create
	 */
	public CommandAPICommand(String commandName) {
		super(commandName);
	}

	/**
	 * Registers the command with a given namespace
	 *
	 * @param namespace The namespace of this command. This cannot be null or empty
	 *
	 */
	public void register(String namespace) {
		if (!namespace.isEmpty() && !CommandAPIHandler.NAMESPACE_PATTERN.matcher(namespace).matches()) {
			super.register();
			return;
		}
		super.register(namespace);
	}

	@Override
	public CommandAPICommand instance() {
		return this;
	}
}
