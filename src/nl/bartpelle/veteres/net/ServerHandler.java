package nl.bartpelle.veteres.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.AttributeKey;
import nl.bartpelle.veteres.GameServer;
import nl.bartpelle.veteres.crypto.IsaacRand;
import nl.bartpelle.veteres.io.RSBuffer;
import nl.bartpelle.veteres.model.Tile;
import nl.bartpelle.veteres.model.entity.Player;
import nl.bartpelle.veteres.net.future.ClosingChannelFuture;
import nl.bartpelle.veteres.net.message.*;
import nl.bartpelle.veteres.net.message.game.Action;
import nl.bartpelle.veteres.net.message.game.DisplayMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by Bart on 8/4/2014.
 */
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger logger = LogManager.getLogger(ServerHandler.class);

	/**
	 * The attribute key for the Player attachment of the channel.
	 */
	public static final AttributeKey<Player> ATTRIB_PLAYER = AttributeKey.valueOf("player");

	/**
	 * A reference to the server instance.
	 */
	private GameServer server;

	public ServerHandler(GameServer server) {
		this.server = server;
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);

		logger.trace("A new client has connected: {}", ctx.channel());
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);

		logger.trace("A client has disconnected: {}", ctx.channel());

		if (ctx.channel().attr(ATTRIB_PLAYER).get() != null) {
			ctx.channel().attr(ATTRIB_PLAYER).get().putattrib(nl.bartpelle.veteres.model.AttributeKey.LOGOUT, true);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause.getStackTrace()[0].getMethodName().equals("read0"))
			return;

		if (cause instanceof ReadTimeoutException) {
			logger.info("Channel disconnected due to read timeout (30s): {}.", ctx.channel());
			ctx.channel().close();
		} else {
			logger.error("An exception has been caused in the pipeline: ", cause);
		}
	}

}
