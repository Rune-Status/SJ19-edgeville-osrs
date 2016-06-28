package edgeville.net.message.game.action;

import edgeville.io.RSBuffer;
import edgeville.model.GroundItem;
import edgeville.model.entity.Player;
import edgeville.model.item.Item;
import edgeville.net.message.game.PacketInfo;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Bart on 5-2-2015.
 */
@PacketInfo(size = 8)
public class ItemAction5 extends ItemAction {

	@Override
	public void decode(RSBuffer buf, ChannelHandlerContext ctx, int opcode, int size) {
		slot = buf.readUShortA();
		item = buf.readULEShortA();
		hash = buf.readInt();
	}

	@Override
	protected int option() {
		return 4;
	}

	@Override
	public void process(Player player) {
		super.process(player);

		Item item = player.inventory().get(slot);
		if (item != null && item.id() == this.item && !player.locked() && !player.dead()) {
			player.inventory().set(slot, null);
			player.world().spawnGroundItem(new GroundItem(item, player.tile(), player));
			player.sound(2739, 0);
		}
	}
}