package org.jhoffmann.photostorybook.services;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
@Slf4j
public class AwtBicubicThumbnail implements Thumbnail {

  @Value("${photostorybook.thumbnail.width}")
  int thumbWidth;

  @Value("${photostorybook.thumbnail.height}")
  int thumbHeight;

  private static BufferedImage create( BufferedImage source,
                                       int width, int height ) {
    double thumbRatio = (double) width / height;
    double imageRatio = (double) source.getWidth() / source.getHeight();
    if ( thumbRatio < imageRatio )
      height = (int) (width / imageRatio);
    else
      width = (int) (height * imageRatio);
    BufferedImage thumb = new BufferedImage( width, height,
                                             BufferedImage.TYPE_INT_RGB );
    Graphics2D g2 = thumb.createGraphics();
    g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION,
                         RenderingHints.VALUE_INTERPOLATION_BICUBIC );
    g2.drawImage( source, 0, 0, width, height, null );
    g2.dispose();
    return thumb;
  }

  @Override
  public Future<byte[]> thumbnail( byte[] imageBytes ) {
    try ( InputStream is = new ByteArrayInputStream( imageBytes );
          ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
      ImageIO.write( create( ImageIO.read( is ), thumbWidth, thumbHeight ), "jpg", baos );
      log.info( "thumbnail" );
      return CompletableFuture.completedFuture( baos.toByteArray() );
    }
    catch ( IOException e ) {
      throw new UncheckedIOException( e );
    }
  }
}
