package aoc.day09

import aoc.Aoc

import scala.collection.mutable.ArrayBuffer

object Day09 extends Aoc {

  private val printPart1 = false
  private val printPart2 = false


  override def part1(lines: List[String]): String = {
    val diskMap = lines.head.toCharArray.map(_.toInt - 48)
    if (printPart1) {
      printDiskMap(diskMap)
    }
    var checksum = 0L
    var i = 0
    var j = diskMap.length - 1 // where we're copying bytes from
    assert(j % 2 == 0)
    var jRemaining = diskMap(j)
    var pos = 0L
    while (i < j) {
      if (i % 2 == 0) {
        // data block
        val fileId = i / 2
        Range(0, diskMap(i)).foreach(r => checksum += (pos + r) * fileId)
        if (printPart1) {
          Range(0, diskMap(i)).foreach(_ => print(fileId))
        }
        pos += diskMap(i)
      } else {
        // empty block
        var blockRemaining = diskMap(i)
        while (blockRemaining >= jRemaining && blockRemaining > 0) {
          val fileId = j / 2
          Range(0, jRemaining).foreach(r => checksum += (pos + r) * fileId)
          if (printPart1) {
            Range(0, jRemaining).foreach(_ => print(fileId))
          }
          blockRemaining -= jRemaining
          pos += jRemaining
          j -= 2
          if (i >= j) {
            blockRemaining = 0
            jRemaining = 0
          } else {
            jRemaining = diskMap(j)
          }
        }
        if (blockRemaining > 0) {
          val fileId = j / 2
          Range(0, blockRemaining).foreach(r => checksum += (pos + r) * fileId)
          if (printPart1) {
            Range(0, blockRemaining).foreach(_ => print(fileId))
          }
          pos += blockRemaining
          jRemaining -= blockRemaining
        }
      }
      i += 1
    }
    if (jRemaining > 0) {
      val fileId = j / 2
      Range(0, jRemaining).foreach(r => checksum += (pos + r) * fileId)
      if (printPart1) {
        Range(0, jRemaining).foreach(_ => print(fileId))
      }
    }
    if (printPart1) {
      println
    }
    checksum.toString
  }

  override def part2(lines: List[String]): String = {
    val diskMap = lines.head.toCharArray.map(_.toInt - 48)
    val expandedDisk = expand(diskMap)
    if (printPart2) {
      printExpandedDiskMap(expandedDisk)
    }

    assert((diskMap.length - 1) % 2 == 0)
    var fileId = (diskMap.length - 1) / 2
    while (fileId > 0) {
      val size = diskMap(fileId * 2)
      if (size > 0) {
        val replace = expandedDisk.indexOf(fileId)
        var next = expandedDisk.indexOf(-1)
        while (next != -1 && next < replace) {
          val range = Range(next, next + size)
          if (range.forall(r => expandedDisk(r) == -1)) {
            range.foreach(r => expandedDisk(r) = fileId)
            Range(replace, replace + size).foreach(r => expandedDisk(r) = -1)
            next = -1
          } else {
            next = expandedDisk.indexOf(-1, next + 1)
          }
        }
      }
      if (printPart2) {
        printExpandedDiskMap(expandedDisk)
      }
      fileId -= 1
    }

    var i = 0
    var checksum = 0L
    while (i < expandedDisk.length) {
      val id = expandedDisk(i)
      if (id != -1) {
        checksum += (i * id)
      }
      i += 1
    }
    checksum.toString
  }

  private def expand(diskMap: Array[Int]): Array[Int] = {
    val expandedDisk = ArrayBuffer.empty[Int]
    var i = 0
    while (i < diskMap.length) {
      if (i % 2 == 0) {
        expandedDisk ++= Array.fill(diskMap(i))(i / 2)
      } else {
        expandedDisk ++= Array.fill(diskMap(i))(-1)
      }
      i += 1
    }
    expandedDisk.toArray
  }

  private def printDiskMap(diskMap: Array[Int]): Unit = {
    var i = 0
    while (i < diskMap.length) {
      if (i % 2 == 0) {
        // data block
        Range(0, diskMap(i)).foreach(_ => print(i / 2))
      } else {
        // empty block
        Range(0, diskMap(i)).foreach(_ => print('.'))
      }
      i += 1
    }
    println
  }

  private def printExpandedDiskMap(diskMap: Array[Int]): Unit = {
    diskMap.foreach {
      case -1 => print('.')
      case i => print(i)
    }
    println
  }
}
