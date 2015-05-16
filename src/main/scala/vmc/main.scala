package vmc

import SimpleIO._
import Image._

object Main extends App {
  import scalaz.syntax.id._
  cat("15-102_03.53.54_VMC_Img_No_33.raw") |> toImage |> write("out.png")
}