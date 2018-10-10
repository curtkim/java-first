package hello;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

  BufferedImage makeImage(){
    BufferedImage bufferedImage = new BufferedImage(200,200,BufferedImage.TYPE_INT_RGB);
    Graphics g = bufferedImage.getGraphics();
    g.setColor(Color.WHITE); //Then set the color to black
    g.fillRect(50, 50, 100, 100);// Finally draw a black rectangle on it
    g.dispose();
    return bufferedImage;
  }

  @GetMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)
  public @ResponseBody byte[] getImage() throws IOException {
    BufferedImage image = makeImage();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write( image, "png", baos );
    baos.flush();
    byte[] imageInByte = baos.toByteArray();
    baos.close();
    return imageInByte;
  }

}
