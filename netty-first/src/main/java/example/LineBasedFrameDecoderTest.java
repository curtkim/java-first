package example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static io.netty.util.ReferenceCountUtil.release;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LineBasedFrameDecoderTest {

  @Test
  public void test() {
    EmbeddedChannel ch = new EmbeddedChannel(
        new LineBasedFrameDecoder(100, true, false)
    );
    //ch.writeInbound(Unpooled.copiedBuffer("first\r\nsecond\r\n", Charset.defaultCharset()));
    ch.writeInbound(Unpooled.copiedBuffer("first\nsecond\n", Charset.defaultCharset()));

    ByteBuf buf = ch.readInbound();
    assertEquals("first", buf.toString(Charset.defaultCharset()));
    release(buf);

    buf = ch.readInbound();
    assertEquals("second", buf.toString(Charset.defaultCharset()));
    release(buf);

    ch.finish();
  }
}
