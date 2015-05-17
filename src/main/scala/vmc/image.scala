package vmc

import java.awt.image._
import java.awt.color._
import java.awt.{Point,Transparency}

object Image {

  val width  = 640
  val height = 480

  val upperLeft = new Point(0,0)

  def grayscale(bytes: Array[Byte]): BufferedImage = {
    val bandOffsets: Array[Int] = Array(0)
    val hasAlpha = false
    val isAlphaPremultiplied = true

    val buffer = new DataBufferByte(bytes, bytes.length)
    val raster = Raster.createInterleavedRaster(buffer, width, height, width, 1, bandOffsets, upperLeft)
    val cs = ColorSpace.getInstance(ColorSpace.CS_GRAY)
    val cm = new ComponentColorModel(cs, hasAlpha, isAlphaPremultiplied, Transparency.OPAQUE, DataBuffer.TYPE_BYTE)
    new BufferedImage(cm, raster, isAlphaPremultiplied, /*properties=*/null)
  }

}