package org.bbstilson.aoc2017

final case class Node(name: String, weight: Int, children: List[Node])

object Day7 {
  type NodeMap = Map[String, Node]
  object NodeMap {
    def empty: NodeMap = Map.empty[String, Node]
  }
  type ChoiceMap = Map[String, (Int, List[String])]
  object ChoiceMap {
    def empty: ChoiceMap = Map.empty[String, (Int, List[String])]
  }

  def main(args: Array[String]): Unit = {
    val nodeMap = parseInput()
    val root = part1(nodeMap)
    println(root)
    part2(nodeMap(root))
  }

  def part1(nodeMap: NodeMap): String = {
    nodeMap.maxBy { case (_, node) => numChildren(node) }._1
  }

  def part2(root: Node): (Int, Int) = findHeaviestBranchWeight(root)

  private def findHeaviestBranchWeight(node: Node): (Int, Int) = { // 🤢
    node.children match {
      case Nil => (node.weight, 0)
      case xs => {
        val childrenWeights = xs.map(findHeaviestBranchWeight)
        val computedChildWeights = childrenWeights.map { case (mW, cW) => mW + cW }
        val childrenWeight = computedChildWeights.sum
        val totalWeight = node.weight + childrenWeight
        if (childrenWeight / xs.size == computedChildWeights.head) {
          (node.weight, childrenWeight)
        } else {
          val heaviest = childrenWeights.maxBy { case (w, cW) => w + cW }
          val heaviestSum = heaviest._1 + heaviest._2
          val smallest = childrenWeights.minBy { case (w, cW) => w + cW }
          val smallestSum = smallest._1 + smallest._2
          val diff = heaviestSum - smallestSum
          val shouldWeigh = heaviest._1 - diff

          println(shouldWeigh) // maaaasive cheat.
          (node.weight, childrenWeight)
        }
      }
    }
  }

  private def parseInput(): NodeMap = {
    val rowRegex = "(\\w+) \\((\\d+)\\) ?-?>? ?(.+)?".r
    val test = io.Source.fromResource("2017/day7/input.txt").getLines.toList
    // val test = List(
    //   "pbga (66)",
    //   "xhth (57)",
    //   "ebii (61)",
    //   "havc (66)",
    //   "ktlj (57)",
    //   "fwft (72) -> ktlj, cntj, xhth",
    //   "qoyq (66)",
    //   "padx (45) -> pbga, havc, qoyq",
    //   "tknk (41) -> ugml, padx, fwft",
    //   "jptl (61)",
    //   "ugml (68) -> gyxo, ebii, jptl",
    //   "gyxo (61)","cntj (57)"
    // )

    val choiceMap: ChoiceMap = test.map { row =>
      val rowRegex(name, weight, cs) = row
      val children = Option(cs).map(_.split(", ").toList).getOrElse(Nil)
      (name -> (weight.toInt, children))
    }.toMap

    Iterator
      .iterate((choiceMap, NodeMap.empty)) { case (cM, nM) => buildTree(cM, nM) }
      .dropWhile { case (choiceMap, _) => choiceMap.nonEmpty }
      .next
      ._2
  }

  def buildTree(choiceMap: ChoiceMap, nodeMap: NodeMap): (ChoiceMap, NodeMap) = {
    choiceMap
      .keys
      .toList
      .foldLeft((choiceMap, NodeMap.empty)) {
        case ((choiceMap, nodeMap), name) =>
          choiceMap.get(name) match {
            case Some((weight, children)) if children.isEmpty => {
              val nextNodeMap = nodeMap + (name -> Node(name, weight, Nil))
              (choiceMap - name, nextNodeMap)
            }
            case Some((weight, children)) => {
              val nextNodeMap = nodeMap + (name -> buildNode(choiceMap, nodeMap)(name))
              (choiceMap - name, nextNodeMap)
            }
            case _ => (choiceMap, nodeMap)
          }
      }
  }

  def buildNode(choiceMap: ChoiceMap, nodeMap: NodeMap)(name: String): Node = {
    nodeMap.get(name) match {
      case Some(node) => node
      case None => {
        val (weight, children) = choiceMap(name)
        Node(name, weight, children.map { child =>
          nodeMap.get(child).getOrElse(buildNode(choiceMap, nodeMap)(child))
        })
      }
    }
  }

  def numChildren(node: Node): Int = {
    node.children.size + node.children.map(numChildren).sum
  }
}
