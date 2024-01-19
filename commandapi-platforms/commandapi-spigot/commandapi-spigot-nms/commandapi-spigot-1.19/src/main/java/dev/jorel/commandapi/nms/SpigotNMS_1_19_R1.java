package dev.jorel.commandapi.nms;

import io.netty.channel.Channel;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SpigotNMS_1_19_R1 extends SpigotNMS_1_19_Common {

	@Override
	protected void hookChatPreview(Plugin plugin, Player player) {
		final Channel playerChannel = ((CraftPlayer) player).getHandle().connection.connection.channel;
		if (playerChannel.pipeline().get("CommandAPI_" + player.getName()) == null) {
			playerChannel.pipeline().addBefore("packet_handler", "CommandAPI_" + player.getName(), new SpigotNMS_1_19_R1_ChatPreviewHandler(this, plugin, player));
		}
	}

	@Override
	public NMS<?> bukkitNMS() {
		return new NMS_1_19_R1();
	}
}
