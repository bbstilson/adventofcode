import scala.io.Source

object Part1 {
  case class Timestamp(
    year: Int,
    month: Int,
    day: Int,
    hour: Int,
    minute: Int
  ) {
    override def toString: String = s"Timestamp($year/$month/$day $hour:$minute)"

    // Returns the minutes that the person was sleeping.
    def diffToFreqMap(wakeUp: Timestamp): FreqMap[Int] = {
      if (wakeUp.minute < this.minute) {
        val nextDay = (0 to wakeUp.minute).toList
        val thisDay = (this.minute to 59).toList
        FreqMap(nextDay ++ thisDay)
      } else {
        val thisDay = (this.minute to wakeUp.minute).toList
        FreqMap(thisDay)
      }
    }
  }

  object Timestamp {
    def fromDateAndTime(date: String, time: String): Timestamp = {
      val (year, month, day) = date.split('-').toList match {
        case Seq(year, month, day) => (year.toInt, month.toInt, day.toInt)
      }
      val (hour, minute) = time.split(':').toList match {
        case Seq(hour, minute) => (hour.toInt, minute.toInt)
      }
      Timestamp(year, month, day, hour, minute)
    }
  }

  sealed trait Action
  case class StartShift(guard: Guard) extends Action
  case object Sleep extends Action
  case object Wake extends Action

  case class LogLine(time: Timestamp, action: Action)

  type FreqMap[A] = Map[A, Int]
  object FreqMap {
    def empty[A]: FreqMap[A] = Map.empty[A, Int]
    def apply[A](xs: List[A]): FreqMap[A] = xs.map((_, 1)).toMap
    def merge[A](a: FreqMap[A], b: FreqMap[A]): FreqMap[A] = {
      (a.keySet ++ b.keySet).map { k =>
        (k, a.getOrElse(k, 0) + b.getOrElse(k, 0))
      }.toMap
    }
  }

  type Guard = Int
  type GuardMap = Map[Guard, FreqMap[Int]]
  object GuardMap {
    def empty: GuardMap = Map.empty[Guard, FreqMap[Int]]
  }

  def main(args: Array[String]): Unit = {
    val sortedAndParsed: Seq[LogLine] = Source.fromFile("input.txt")
      .getLines
      .toList
      .sorted
      .map(parseLine _)

    val initState = (
      GuardMap.empty,
      Option.empty[Guard],
      Option.empty[Timestamp]
    )

    val (guardMap, _, _) = sortedAndParsed.foldLeft(initState) {
      case ((guardMap, activeGuard, lastTimestamp), logLine) => {
        logLine match {
          case LogLine(ts, StartShift(guard)) =>
            (guardMap, Some(guard), Some(ts))
          case LogLine(ts, Sleep) =>
            (guardMap, activeGuard, Some(ts))
          case LogLine(ts, Wake) => {
            populateSleepSchedule(guardMap, activeGuard, lastTimestamp, ts)
          }
        }
      }
    }

    val (sleepiestGuard, minutesSlept) = guardMap
      .mapValues(_.values.sum)
      .toList
      .sortBy { case (gid, minutesSlept) => minutesSlept }
      .last

    val (mostSleptMinute, numHit) = guardMap(sleepiestGuard)
      .toList
      .sortBy { case (minute, numHit) => numHit }
      .last

    println(sleepiestGuard * mostSleptMinute)
  }

  private def parseLine(line: String): LogLine = {
    val (_date, _time, _actionTokens) = line
      .replaceAll("""\[|\]""", "") // remove brackets
      .split(' ') // split into tokens
      .toList match {
        case Seq(parts @ _*) => (parts.head, parts.tail.head, parts.tail.tail)
      }

    val timestamp = Timestamp.fromDateAndTime(_date, _time)
    val action = _actionTokens.mkString(" ") match {
      case "wakes up" => Wake
      case "falls asleep" => Sleep
      case _ => StartShift(_actionTokens(1).tail.toInt)
    }

    LogLine(timestamp, action)
  }

  private def populateSleepSchedule(
    guardMap: GuardMap,
    activeGuard: Option[Guard],
    lastTimestamp: Option[Timestamp],
    currentTimeStamp: Timestamp
  ): (GuardMap, Option[Guard], Option[Timestamp]) = {
    val activeGuardId = activeGuard.get
    val newGuardMap = guardMap.get(activeGuardId) match {
      case Some(freqMap) => {
        // guard exists; need to merge.
        // would be great to get a lib to help here.
        val newFreqMap = lastTimestamp.get.diffToFreqMap(currentTimeStamp)
        guardMap.updated(
          activeGuardId,
          FreqMap.merge(freqMap, newFreqMap)
        )
      }
      case None => {
        val freqMap = lastTimestamp.get.diffToFreqMap(currentTimeStamp)
        // new guard, so we can just add
        guardMap + (activeGuardId -> freqMap)
      }
    }

    (newGuardMap, activeGuard, None)
  }
}
