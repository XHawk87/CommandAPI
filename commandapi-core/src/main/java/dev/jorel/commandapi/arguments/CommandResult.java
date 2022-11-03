package dev.jorel.commandapi.arguments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Objects;

public record CommandResult(Command command, String[] args) {
	public void execute(CommandSender sender) {
		command.execute(sender, command.getLabel(), args);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CommandResult that = (CommandResult) o;
		return command.equals(that.command) && Arrays.equals(args, that.args);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(command);
		result = 31 * result + Arrays.hashCode(args);
		return result;
	}
}
