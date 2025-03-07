package adventofcode

import java.io.File

//AOC 2020, puzzle 10, part 01 & 02

fun main() {
    val adapterList: List<Int> = File("src/2020_10_input.txt").readLines().map { it.toInt() }.sorted()
    val fullData: List<Int> = listOf(0) + adapterList + adapterList.maxOrNull()?.plus(3)!!

    //Part 01
    val joltDiffOccurrence = dataIteratorPart1(fullData)
    println(joltDiffOccurrence)

    val joltDiff1TimesJoltDiff3 = joltDiffOccurrence[1]!! * joltDiffOccurrence[3]!!
    println("Result of part 01: $joltDiff1TimesJoltDiff3")
    //----

    //Part 02
    println("Result of part 02: " + countAdapterArrangements(fullData))
    //----
}

//Part 01
fun dataIteratorPart1(input: List<Int>): Map<Int, Int> {
    val joltDiffMap = mutableMapOf(1 to 0, 2 to 0, 3 to 0)

    for (i in 0 until input.size - 1) {
        val joltDiff = input[i + 1] - input[i]
        joltDiffMap[joltDiff] = (joltDiffMap[joltDiff] ?: 0) + 1
        //[] gör att jag når index av mappens key. ?:0 är defaultvärde om värde inte finns på nyckeln, sen +1
    }
    return joltDiffMap
}

//---------------------------------------------------------------//
//Part 02
//Trying own solution, but got the brief wrong.
fun dataIteratorPart2(input: List<Int>): Long {
    var joltDiffCounter = 1L
    var localList = input.toMutableList()

    while (localList.size >= 3) {
        for (i in 0 until input.size - 3) {
            if (input[i] - input[i + 3] == 3)
                joltDiffCounter += 3
            else if (input[i] - input[i + 2] == 2)
                joltDiffCounter += 2
        }
        localList.removeAt(0)
    }
    return joltDiffCounter
}

//Chatting with claude to get to this solution, how I realised I got the brief wrong.
// The key was that the ways to get to next position in the list,
// was the ways to get to the previous adapters within range's TOTAL ways, not number of steps back as I thought.
fun countAdapterArrangements(input: List<Int>): Long {
    //Memorization array
    val arrangements = LongArray(input.size) { 0 }
    arrangements[0] = 1 // There's one way to reach the first adapter, starting point. Needed for the iteration to work.

    // Calculate the number of arrangements for each adapter
    for (i in 1 until input.size) {
        for (j in 0 until i) {
            // Check if we can reach this adapter from a previous one
            if (input[i] - input[j] <= 3) {
                arrangements[i] += arrangements[j]
            }
        }
    }
    // Return the number of arrangements to the last adapter
    return arrangements.last()
}

//---------------For educational purposes-----------------//
//Ginsberg's solution
fun solvePart2(): Long {
    //Mockup-lista av mig
    val adapters = listOf(0, 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
        32, 33, 34, 35, 38, 39, 42, 45, 46, 47, 48, 49, 52)

    val pathsByAdapter: MutableMap<Int,Long> = mutableMapOf(0 to 1L)

    adapters.drop(1).forEach { adapter ->
        pathsByAdapter[adapter] = (1 .. 3).map { lookBack ->
            pathsByAdapter.getOrDefault(adapter - lookBack, 0)
        }.sum()
    }

    return pathsByAdapter.getValue(adapters.last())
}