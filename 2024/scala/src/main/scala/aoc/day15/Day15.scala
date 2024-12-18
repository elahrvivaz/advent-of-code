package aoc.day15

import aoc.Aoc

import scala.collection.mutable.ArrayBuffer

object Day15 extends Aoc {

  override def part1(lines: List[String]): Long = {
    val warehouse = new Warehouse(lines.takeWhile(_.nonEmpty).map(_.toCharArray.map(WarehouseItem.apply)).toArray)
    val moves: List[Move] = lines.dropWhile(_.nonEmpty).drop(1).flatMap(_.toCharArray.map(Move.apply))
//    if (moves.lengthCompare(20) < 0) {
//      println(warehouse)
//    }
    moves.foreach { m =>
      warehouse.move(m)
//      if (moves.lengthCompare(20) < 0) {
//        println(s"Move: $m")
//        println(warehouse)
//      }
    }
    warehouse.boxGps().sum
  }

  override def part2(lines: List[String]): Long = {
    val warehouse =
      new Warehouse2(lines.takeWhile(_.nonEmpty).map(_.toCharArray.map(WarehouseItem.apply).flatMap(_.widen)).toArray)
    val moves: List[Move] = lines.dropWhile(_.nonEmpty).drop(1).flatMap(_.toCharArray.map(Move.apply))
//    if (moves.lengthCompare(1000) > 0) {
//      println(warehouse)
//    }
    moves.foreach { m =>
      warehouse.move(m)
//      if (moves.lengthCompare(1000) > 0) {
//        println(s"Move: $m")
//        println(warehouse)
//      }
    }
    warehouse.boxGps().sum
  }

  class Warehouse(items: Array[Array[WarehouseItem]]) {

    protected var robotY: Int = items.indexWhere(_.contains(Robot))
    protected var robotX: Int = items(robotY).indexOf(Robot)

    def move(m: Move): Unit = m match {
      case Up    => moveUp()
      case Down  => moveDown()
      case Right => moveRight()
      case Left  => moveLeft()
    }

    protected def moveUp(): Unit = {
      if (robotY > 0) {
        items(robotY - 1)(robotX) match {
          case Empty =>
            items(robotY)(robotX) = Empty
            robotY -= 1
            items(robotY)(robotX) = Robot

          case Wall => // no-op
          case Box =>
            var y = robotY - 2
            while (y >= 0) {
              items(y)(robotX) match {
                case Empty =>
                  while (y < robotY) {
                    items(y)(robotX) = items(y + 1)(robotX)
                    y += 1
                  }
                  items(robotY)(robotX) = Empty
                  robotY -= 1
                  y = 0 // break out
                case Wall => y = 0 // break out
                case Box => // no-op
              }
              y -= 1
            }
        }
      }
    }

    protected def moveDown(): Unit = {
      if (robotY < items.length - 1) {
        items(robotY + 1)(robotX) match {
          case Empty =>
            items(robotY)(robotX) = Empty
            robotY += 1
            items(robotY)(robotX) = Robot
          case Wall => // no-op
          case Box =>
            var y = robotY + 2
            while (y < items.length) {
              items(y)(robotX) match {
                case Empty =>
                  while (y > robotY) {
                    items(y)(robotX) = items(y - 1)(robotX)
                    y -= 1
                  }
                  items(robotY)(robotX) = Empty
                  robotY += 1
                  y = items.length // break out
                case Wall => y = items.length // break out
                case Box => // no-op
              }
              y += 1
            }
        }
      }
    }

    protected def moveRight(): Unit = {
      if (robotX < items(robotY).length - 1) {
        items(robotY)(robotX + 1) match {
          case Empty =>
            items(robotY)(robotX) = Empty
            robotX += 1
            items(robotY)(robotX) = Robot
          case Wall => // no-op
          case Box | LeftBox | RightBox =>
            var x = robotX + 2
            while (x < items(robotY).length) {
              items(robotY)(x) match {
                case Empty =>
                  while (x > robotX) {
                    items(robotY)(x) = items(robotY)(x - 1)
                    x -= 1
                  }
                  items(robotY)(robotX) = Empty
                  robotX += 1
                  x = items(robotY).length // break out
                case Wall => x = items(robotY).length // break out
                case Box | LeftBox | RightBox => // no-op
              }
              x += 1
            }
        }
      }
    }
    protected def moveLeft(): Unit = {
      if (robotX > 0) {
        items(robotY)(robotX - 1) match {
          case Empty =>
            items(robotY)(robotX) = Empty
            robotX -= 1
            items(robotY)(robotX) = Robot
          case Wall => // no-op
          case Box | LeftBox | RightBox =>
            var x = robotX - 2
            while (x >= 0) {
              items(robotY)(x) match {
                case Empty =>
                  while (x < robotX) {
                    items(robotY)(x) = items(robotY)(x + 1)
                    x += 1
                  }
                  items(robotY)(robotX) = Empty
                  robotX -= 1
                  x = 0 // break out
                case Wall => x = 0 // break out
                case Box | LeftBox | RightBox => // no-op
              }
              x -= 1
            }
        }
      }
    }

    def boxGps(): Seq[Int] = {
      val gps = Seq.newBuilder[Int]
      var y = 0
      while (y < items.length) {
        var x = 0
        while (x < items(y).length) {
          items(y)(x) match {
            case Box | LeftBox => gps += (100 * y) + x
            case _ =>
          }
          x += 1
        }
        y += 1
      }
      gps.result()
    }

    override def toString: String = {
      val sb = new StringBuilder()
      items.foreach { row =>
        row.foreach(sb.append)
        sb.append('\n')
      }
      sb.toString()
    }
  }

  class Warehouse2(items: Array[Array[WarehouseItem]]) extends Warehouse(items) {

    // test if we can move up without being blocked
    private def tryUp(y: Int, x: Int): Seq[PossibleMove] = {
      if (y == 0 || items(y-1)(x) == Wall) { Seq.empty } else {
        items(y-1)(x) match {
          case Empty => Seq(PossibleMove(y, x))

          case LeftBox =>
            val right = tryUp(y-1, x+1)
            if (right.isEmpty) { Seq.empty } else {
              val cur = tryUp(y-1, x)
              if (cur.isEmpty) { Seq.empty } else {
                right ++ cur :+ PossibleMove(y, x)
              }
            }

          case RightBox =>
            val left = tryUp(y-1, x-1)
            if (left.isEmpty) { Seq.empty } else {
              val cur = tryUp(y-1, x)
              if (cur.isEmpty) { Seq.empty } else {
                left ++ cur :+ PossibleMove(y, x)
              }
            }
        }
      }
    }

    private def tryDown(y: Int, x: Int): Seq[PossibleMove] = {
      if (y == items.length - 1 || items(y + 1)(x) == Wall) {
        Seq.empty
      } else {
        items(y + 1)(x) match {
          case Empty => Seq(PossibleMove(y, x))

          case LeftBox =>
            val right = tryDown(y + 1, x + 1)
            if (right.isEmpty) {
              Seq.empty
            } else {
              val cur = tryDown(y + 1, x)
              if (cur.isEmpty) {
                Seq.empty
              } else {
                right ++ cur :+ PossibleMove(y, x)
              }
            }

          case RightBox =>
            val left = tryDown(y + 1, x - 1)
            if (left.isEmpty) {
              Seq.empty
            } else {
              val cur = tryDown(y + 1, x)
              if (cur.isEmpty) {
                Seq.empty
              } else {
                left ++ cur :+ PossibleMove(y, x)
              }
            }
        }
      }
    }

    override protected def moveUp(): Unit = {
      val attempts = tryUp(robotY, robotX)
      if (attempts.nonEmpty) {
        robotY -= 1
      }
      attempts.groupBy(_.fromX).foreach { case (_, moves) =>
        val sorted = moves.distinct.sortBy(_.fromY).toArray
        var i = 0
        while (i < sorted.length) {
          val m = sorted(i)
          items(m.fromY-1)(m.fromX) = items(m.fromY)(m.fromX)
          if (i == sorted.length -1 || sorted(i+1).fromY - m.fromY > 1) {
            items(m.fromY)(m.fromX) = Empty
          }
          i += 1
        }
      }
    }

    override protected def moveDown(): Unit = {
      val attempts = tryDown(robotY, robotX)
      if (attempts.nonEmpty) {
        robotY += 1
      }
      attempts.groupBy(_.fromX).foreach { case (_, moves) =>
        val sorted = moves.distinct.sortBy(_.fromY)(Ordering.Int.reverse).toArray
        var i = 0
        while (i < sorted.length) {
          val m = sorted(i)
          items(m.fromY+1)(m.fromX) = items(m.fromY)(m.fromX)
          if (i == sorted.length -1  || m.fromY - sorted(i+1).fromY > 1) {
            items(m.fromY)(m.fromX) = Empty
          }
          i += 1
        }
      }
    }
  }

  case class PossibleMove(fromY: Int, fromX: Int)

  sealed trait WarehouseItem {
    def widen: Seq[WarehouseItem]
  }
  case object Robot extends WarehouseItem {
    override def widen: Seq[WarehouseItem] = Seq(Robot, Empty)
    override def toString: String = "@"
  }
  case object Empty extends WarehouseItem {
    override def widen: Seq[WarehouseItem] = Seq(Empty, Empty)
    override def toString: String = "."
  }
  case object Wall extends WarehouseItem {
    override def widen: Seq[WarehouseItem] = Seq(Wall, Wall)
    override def toString: String = "#"
  }
  case object Box extends WarehouseItem {
    override def widen: Seq[WarehouseItem] = Seq(LeftBox, RightBox)
    override def toString: String = "O"
  }
  case object LeftBox extends WarehouseItem {
    override def widen: Seq[WarehouseItem] = ???
    override def toString: String = "["
  }
  case object RightBox extends WarehouseItem {
    override def widen: Seq[WarehouseItem] = ???
    override def toString: String = "]"
  }

  object WarehouseItem {
    def apply(w: Char): WarehouseItem = w match {
      case '@' => Robot
      case '.' => Empty
      case '#' => Wall
      case 'O' => Box
    }
  }

  sealed trait Move
  case object Left extends Move {
    override def toString: String = "<"
  }
  case object Right extends Move {
    override def toString: String = ">"
  }
  case object Up extends Move {
    override def toString: String = "^"
  }
  case object Down extends Move {
    override def toString: String = "v"
  }

  object Move {
    def apply(m: Char): Move = m match {
      case '^' => Up
      case 'v' => Down
      case '<' => Left
      case '>' => Right
    }
  }
}
