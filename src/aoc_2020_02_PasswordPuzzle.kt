package adventofcode

import java.io.File

//AOC 2020, puzzle 02, part 01 & 02

fun main() {
    val data: List<String> = File("src/2020_02_input.txt").readLines()

    val testData: List<String> = listOf(
        "14-17 s: ssssssgsssszspssb", //13 false + false
        "5-13 m: kmmctmsmmmglzxm", //7 true + false
        "4-7 s: qhrhsdbsmmlstznms", //4 true + false
        "13-15 j: jjjjljjjjjjjqjj", //13 true + true
        "5-16 n: ncnnnsvlpndnkvvrcf", //6 true + true
        "14-18 n: nnnnnnnnnnnnknnnbn" //16 true + false (n på båda platser)
        //ska vara 5 för del 1
        //ska vara 2 för del 2
        )

    //Part 01
    println(countCorrectPw(data, ::validatePwPatternRulesetPart01))
    //Part 02
    println(countCorrectPw(data, ::validatePwPatternRulesetPart02))
}

fun getCondNumbers (input: String): List<Int> = Regex("\\d+").findAll(input)
    .map { it.value.toInt() }
    .toList()

fun getCondLetter (input : String): Char? = input.filter { it.isLetter() }
    .getOrNull(0)

fun getpw (input : String): String = input.filter { it.isLetter() }
    .substring(1)

fun countCorrectPw (input: List <String>, ruleset: (String) -> Boolean): Int {
    var counter = 0
    for (element in input) {
        if (ruleset(element)) {
            counter++
        }
    }
    return counter
}

//Part 01 specific
fun validatePwPatternRulesetPart01 (input: String): Boolean {
        val stringOfCondLetters: String = getpw(input.filter { it == getCondLetter(input) })
        return (stringOfCondLetters.length >= getCondNumbers(input)[0]
                && stringOfCondLetters.length <= getCondNumbers(input)[1])
}

//Part 02 specific
fun validatePwPatternRulesetPart02 (input: String): Boolean {
    //Har ersatt .get med [] som är shorthand för att göra samma sak, alltså nå char på index inom []
    return ((getpw(input)[getCondNumbers(input)[0]-1] == getCondLetter(input))
            != (getpw(input)[getCondNumbers(input)[1]-1] == getCondLetter(input)))
}