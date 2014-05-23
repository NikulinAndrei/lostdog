package services

import java.io.File
import scala.util.Random
import play.api.mvc.MultipartFormData.FilePart
import play.api.libs.Files.TemporaryFile

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
      file.ref.moveTo( new File("/tmp/images/"+filename))
      filename
    }

}
