import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;

public class ChatClientHandler extends
		ChannelInboundMessageHandlerAdapter<String> {

//	@Override
	public void messageReceived(ChannelHandlerContext ctx, String message)
			throws Exception {
		System.out.println(message);
	}
}
