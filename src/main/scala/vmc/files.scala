package vmc

import java.nio.file.{FileSystems,Files,Path}
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object SimpleIO {

  def cat(name: String): Array[Byte] = cat(FileSystems.getDefault.getPath(name))
  def cat(file: Path): Array[Byte] = Files.readAllBytes(file)

  def write(name: String)(image: BufferedImage): Unit =
    ImageIO.write(image, "png", FileSystems.getDefault.getPath(name).toFile)
}