package example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static io.netty.util.ReferenceCountUtil.release;
import static io.netty.util.ReferenceCountUtil.releaseLater;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class DelimiterBasedFrameDecoderTest {

  public void testJsonByZero() {
    EmbeddedChannel ch = new EmbeddedChannel(new DelimiterBasedFrameDecoder(
        1024*1024,
        true,
        Unpooled.wrappedBuffer(new byte[] { '\0' }))
    );
    ch.writeInbound(Unpooled.copiedBuffer("{}\0{}", Charset.defaultCharset()));

    ByteBuf buf = ch.readInbound();
    assertEquals("{}", buf.toString(Charset.defaultCharset()));
    release(buf);

    buf = ch.readInbound();
    assertEquals("{}", buf.toString(Charset.defaultCharset()));
    release(buf);

    ch.finish();
  }

  @Test
  public void testMultipleLinesStrippedDelimiters() {
    EmbeddedChannel ch = new EmbeddedChannel(new DelimiterBasedFrameDecoder(
        8192,
        true,
        Unpooled.wrappedBuffer(new byte[] { 'a' }),
        Unpooled.wrappedBuffer(new byte[] { 'g' }))
    );
    ch.writeInbound(Unpooled.copiedBuffer("firstgasecondga", Charset.defaultCharset()));

    ByteBuf buf = ch.readInbound();
    assertEquals("first", buf.toString(Charset.defaultCharset()));
    release(buf);

    buf = ch.readInbound();
    assertEquals("", buf.toString(Charset.defaultCharset()));
    release(buf);

    buf = ch.readInbound();
    assertEquals("second", buf.toString(Charset.defaultCharset()));
    release(buf);

    buf = ch.readInbound();
    assertEquals("", buf.toString(Charset.defaultCharset()));
    release(buf);

    ch.finish();
  }

}