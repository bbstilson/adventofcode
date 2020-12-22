package bbstilson.aoc2020

import bbstilson.implicits.ListImplicits._
import scala.annotation.tailrec

object Day21 extends aocd.Problem(2020, 21) {
  val IngredientRegex = raw"([\w ]+)\(contains ([\w, ]+)*\)".r
  case class Recipe(ingredients: Set[String], allergens: Set[String])

  def run(input: List[String]): Unit = {
    val recipes = input.map { case IngredientRegex(ingredients, allergens) =>
      Recipe(ingredients.split(" ").map(_.trim()).toSet, allergens.split(",").map(_.trim()).toSet)
    }
    val possibleAllergens = findPossibleAllergens(recipes)

    part1(recipes, possibleAllergens)
    part2(possibleAllergens)
    ()
  }

  def part1(recipes: List[Recipe], possibleAllergens: Map[String, Set[String]]): Int = part1 {
    val ingredients = recipes.flatMap(_.ingredients).toFrequencyMap
    val notPossible = ingredients -- possibleAllergens.values.flatten.toSet
    notPossible.values.sum
  }

  def part2(possibleAllergens: Map[String, Set[String]]): String = part2 {
    definiteAllergens(possibleAllergens, Map.empty).toList
      .sortBy { case (allergen, _) => allergen }
      .map { case (_, ingredient) => ingredient }
      .mkString(",")
  }

  @tailrec
  def definiteAllergens(
    possibleAllergens: Map[String, Set[String]],
    allergens: Map[String, String]
  ): Map[String, String] =
    possibleAllergens
      .collectFirst {
        case (allergen, ingredients) if ingredients.size == 1 => allergen -> ingredients.head
      } match {
      case None => allergens
      case Some((allergen, ingredient)) => {
        val nextPossibilities = possibleAllergens.collect {
          case (k, is) if k != allergen => k -> (is - ingredient)
        }
        definiteAllergens(nextPossibilities, allergens + (allergen -> ingredient))
      }
    }

  def findPossibleAllergens(recipes: List[Recipe]): Map[String, Set[String]] =
    recipes.foldLeft(Map.empty[String, Set[String]]) { case (map, recipe) =>
      recipe.allergens.foldLeft(map) { case (aMap, allergen) =>
        aMap.get(allergen) match {
          case Some(ing) => aMap + (allergen -> ing.intersect(recipe.ingredients))
          case None      => aMap + (allergen -> recipe.ingredients)
        }
      }
    }

}
