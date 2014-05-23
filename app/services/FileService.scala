package services

import java.io.File
import scala.util.Random
import play.api.mvc.MultipartFormData.FilePart
import play.api.libs.Files.TemporaryFile
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.awt.Image

/**
 * Created with IntelliJ IDEA.
 * User: AndreiN
 * Date: 23.05.14
 */
class FileService {

  def persistFile( file: FilePart[TemporaryFile], pk: Long) =
    if( ! file.filename.toLowerCase().endsWith ("jpg" ))
      throw new IllegalArgumentException( "Toetame ainult JPG formaadis pilte" )
    else{
      val filename = pk +"_"+System.currentTimeMillis()+"_"+ Random.nextInt()+".jpg"


      val scaled = resizeImg( file.ref.file)
      ImageIO.write( scaled , "png", new File("/tmp/images/"+filename) )

      //file.ref.moveTo( new File("/tmp/images/"+filename))
      filename
    }

  def resizeImg( f: File ) = {
    val originalImage = ImageIO.read(f )

    val scale = determineImageScale( originalImage.getWidth, originalImage.getHeight)

    val scaled= originalImage.getScaledInstance(
      (originalImage.getWidth * scale).toInt,
      (originalImage.getHeight * scale).toInt,
      Image.SCALE_SMOOTH)

    val outImage = new BufferedImage(scaled.getWidth(null), scaled.getHeight(null), BufferedImage.TYPE_INT_RGB)

    outImage.getGraphics().drawImage(scaled, 0, 0, null)

    outImage
  }

  def determineImageScale( sourceWidth: Int, sourceHeight:Int ): Double = {
    val scalex  = 640.toDouble / sourceWidth
    val scaley = 480.toDouble / sourceHeight
    Math.min(scalex, scaley)
  }
}
