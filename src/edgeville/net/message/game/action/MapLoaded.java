package edgeville.net.message.game.action;

import edgeville.io.RSBuffer;
import edgeville.model.entity.Player;
import edgeville.net.message.game.Action;
import edgeville.net.message.game.PacketInfo;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Bart Pelle on 8/23/2014.
 */
@PacketInfo(size = 0)
public class MapLoaded implements Action {

	@Override public void process(Player player) {
	}

	@Override public void decode(RSBuffer buf, ChannelHandlerContext ctx, int opcode, int size) {
	}
}