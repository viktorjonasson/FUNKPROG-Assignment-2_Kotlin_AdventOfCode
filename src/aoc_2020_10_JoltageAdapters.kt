package adventofcode

import java.io.File

fun main() {
    val adapterList: List<Int> = File("src/2020_10_input.txt").readLines().map { it.toInt() }.sorted()
    val fullData: List<Int> = listOf(0) + adapterList + adapterList.maxOrNull()?.plus(3)!!
    //!! är för att tvinga fram en Int list och inte Int?, dvs jag assertar att jag vet att det är ints.

    val testData3 = listOf(0, 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
        32, 33, 34, 35, 38, 39, 42, 45, 46, 47, 48, 49, 52)

    //Part 01
    val joltDiffOccurrence = dataIteratorPart1(fullData)

    println(joltDiffOccurrence)

    val joltDiff1TimesJoltDiff3 = joltDiffOccurrence[1]!! * joltDiffOccurrence[3]!!
    println("Result of part 01: $joltDiff1TimesJoltDiff3")
    //----

    //Part 02
    //println(dataIteratorPart2(testData3))
    //println(countAdapterArrangements(testData3))
    println("Result of part 02: " + countAdapterArrangements(fullData))

    //explainSolution()
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

//Chatting with claude to get to this solution. Key was that the ways to get to next position in the list,
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
    println(arrangements.contentToString())
    return arrangements.last()
}

//--------------------------------//

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

//Claudes breakdown of Ginsberg's solution
//"Let's walk through a simple example with adapters [0, 1, 3, 4, 5, 6, 9]":

fun explainSolution() {
    val adapters = listOf(0, 1, 3, 4, 5, 6, 9)

    // This map tracks the number of distinct ways to reach each adapter
    val pathsByAdapter: MutableMap<Int, Long> = mutableMapOf(0 to 1L)

    println("Initial state:")
    println("pathsByAdapter = $pathsByAdapter")

    adapters.drop(1).forEach { adapter ->
        println("\nProcessing adapter $adapter:")

        val lookBack1 = adapter - 1
        val lookBack2 = adapter - 2
        val lookBack3 = adapter - 3

        println("  Looking back at: $lookBack1, $lookBack2, $lookBack3")

        val pathsFromLookBack1 = pathsByAdapter.getOrDefault(lookBack1, 0)
        val pathsFromLookBack2 = pathsByAdapter.getOrDefault(lookBack2, 0)
        val pathsFromLookBack3 = pathsByAdapter.getOrDefault(lookBack3, 0)

        println("  Paths from $lookBack1: $pathsFromLookBack1")
        println("  Paths from $lookBack2: $pathsFromLookBack2")
        println("  Paths from $lookBack3: $pathsFromLookBack3")

        val totalPaths = pathsFromLookBack1 + pathsFromLookBack2 + pathsFromLookBack3
        pathsByAdapter[adapter] = totalPaths

        println("  Total paths to adapter $adapter: $totalPaths")
        println("  Updated pathsByAdapter = $pathsByAdapter")
    }

    println("\nFinal result: ${pathsByAdapter[adapters.last()]} distinct paths to ${adapters.last()}")
}

