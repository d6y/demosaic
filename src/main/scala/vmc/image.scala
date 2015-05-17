package vmc

import java.awt.image._
import java.awt.color._
import java.awt.{Point,Transparency}
import scodec.bits.ByteVector

// Bytes on the JVM are signed.
// However, the DataBufferByte bytes are treated as unsigned, which is what we want.
object Image {

  val width  = 640
  val height = 480

  def grayscale(bytes: Array[Byte]): BufferedImage = {
    val bandOffsets: Array[Int] = Array(0)

    val buffer = new DataBufferByte(bytes, bytes.length)
    val raster = Raster.createInterleavedRaster(buffer, width, height, width, 1, bandOffsets, upperLeft)
    val cs = ColorSpace.getInstance(ColorSpace.CS_GRAY)
    val cm = new ComponentColorModel(cs, hasAlpha, isAlphaPremultiplied, Transparency.OPAQUE, DataBuffer.TYPE_BYTE)
    new BufferedImage(cm, raster, isAlphaPremultiplied, properties)
  }

  // A simple split of each pixel into a separate RGB channel (will be 50% green)
  def colour(bytes: Array[Byte]): BufferedImage = {

    // ASSUMPTION!  RAW file format pixel colour mapping
    //
    //          COLUMNS
    //         EVEN | ODD
    //        +----+----+
    //   EVEN | B  |  G |
    //   ODD  | G  |  R |
    //        +---------+

    val bv = ByteVector(bytes)

    val red   = bv and mkMask { case ( Row(Odd) , Col(Odd)  ) => on }
    val green = bv and mkMask { case ( Row(Even), Col(Odd)  ) => on
                                case ( Row(Odd) , Col(Even) ) => on }
    val blue  = bv and mkMask { case ( Row(Even), Col(Even) ) => on }

    val buffer = new DataBufferByte(Array(red.toArray,green.toArray,blue.toArray), bytes.length)

    val bandOffsets: Array[Int] = Array(0,0,0)
    val banks: Array[Int] = Array(0,1,2)
    val raster = Raster.createBandedRaster(buffer, width, height, width, banks, bandOffsets, upperLeft)

    val cs = ColorSpace.getInstance(ColorSpace.CS_sRGB)
    val cm = new ComponentColorModel(cs, hasAlpha, isAlphaPremultiplied, Transparency.OPAQUE, DataBuffer.TYPE_BYTE)
    new BufferedImage(cm, raster, isAlphaPremultiplied, properties)
  }

  private[this] lazy val zero = { case _ => 0 }: PartialFunction[(Row,Col), Byte]
  private[this] lazy val on: Byte = 255.asInstanceOf[Byte]

  private[this] case class Row(value: EvenOrOdd) extends AnyVal
  private[this] case class Col(value: EvenOrOdd) extends AnyVal

  private[this] sealed trait EvenOrOdd
  private[this] case object Even extends EvenOrOdd
  private[this] case object Odd extends EvenOrOdd

  private[this] object EvenOrOdd {
    val isEven: Int => Boolean = _ % 2 == 0
    def apply(x: Int): EvenOrOdd = if (isEven(x)) Even else Odd
  }

  private[this] lazy val checkerboard: Seq[(Row,Col)] = for {
    row <- 0 until height
    col <- 0 until width
  } yield (Row(EvenOrOdd(row)), Col(EvenOrOdd(col)))

  private[this] def mkMask(f: PartialFunction[(Row,Col), Byte]): ByteVector =
    ByteVector(checkerboard.map(f orElse zero))

  val upperLeft = new Point(0,0)
  val properties = new java.util.Hashtable()
  val hasAlpha = false
  val isAlphaPremultiplied = true
}