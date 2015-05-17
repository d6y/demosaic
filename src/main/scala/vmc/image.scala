package vmc

import java.awt.image._
import java.awt.color._
import java.awt.{Point,Transparency}

object Image {

  val width  = 640
  val height = 480

  val upperLeft = new Point(0,0)
  val offsets: Array[Int] = Array(0)

  def grayscale(bytes: Array[Byte]): BufferedImage = {
    val buffer = new DataBufferByte(bytes, bytes.length)
    val raster = Raster.createInterleavedRaster(buffer, width, height, width, 1, offsets, upperLeft)
    val cs = new ICC_ColorSpace(ICC_Profile.getInstance(ColorSpace.CS_GRAY))
    val cm = new ComponentColorModel(cs, /*hasAlpha=*/false, /*isAlphaPremultiplied=*/true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE)
    new BufferedImage(cm, raster, true, null)
  }

}