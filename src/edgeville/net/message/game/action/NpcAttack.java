package edgeville.net.message.game.action;

import edgeville.aquickaccess.combat.Combat;
import edgeville.aquickaccess.combat.PvMCombat;
import edgeville.io.RSBuffer;
import edgeville.model.AttributeKey;
import edgeville.model.World;
import edgeville.model.entity.Npc;
import edgeville.model.entity.PathQueue;
import edgeville.model.entity.Player;
import edgeville.net.message.game.Action;
import edgeville.net.message.game.PacketInfo;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Tom on 9/26/2015.
 * Modified by Sky on 1/3/2016.
 */
@PacketInfo(size = 3)
public class NpcAttack implements Action {

    private int size = -1;
    private int opcode = -1;
    private boolean run;
    private int index;

    @Override
    public void decode(RSBuffer buf, ChannelHandlerContext ctx, int opcode, int size) {
        run = buf.readByteA() == 1;
        index = buf.readUShortA();
    }

    @Override
    public void process(Player player) {
        player.stopActions(true);

        player.message("npc attack --- npcindex:" + index + " run:" + run);
        Npc other = player.world().npcs().get(index);
        player.message("npcid:" + other.id() + " run:" + run);

        if (other == null) {
            player.message("Unable to find npc.");
        } else {
            if (!player.locked() && !player.dead() && !other.dead()) {
                player.stepTowards(other, 20);
                player.face(other);

                player.putattrib(AttributeKey.TARGET_TYPE, 1);
                player.putattrib(AttributeKey.TARGET, /*index*/ other);

                //player.world().server().scriptExecutor().executeScript(player, PlayerCombat.script);
                new PvMCombat(player, other).start();

            }
        }
    }
}