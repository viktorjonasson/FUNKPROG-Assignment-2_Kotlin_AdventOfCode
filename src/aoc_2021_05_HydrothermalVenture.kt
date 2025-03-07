package adventofcode

import java.io.File

//AOC 2021, puzzle 05, part 01

//Input data mapped to strings
val coordinatesInput: Map<Int, String> = File("src/2021_05_input.txt")
        .readLines()
        .mapIndexed { index, line -> index to line }
        .toMap()

//Map values converted from String to list of Ints, AND filtered to NOT include diagonal lines
val mapWithInputCoordinates: Map<Int, List<Int>> = coordinatesInput.mapValues { (_, value) -> Regex("\\d+").findAll(value)
    .map { it.value.toInt() }
    .toList()
    }
    .filter { (_, value) -> value[0] == value[2] || value[1] == value[3]  }

//Get the size of the coming "graph" by getting the highest x and y value. Will be the size of my Map later.
fun getCoordinateBounds(input: Map<Int, List<Int>>, index1: Int, index2: Int): Int {
    return input.values.maxOf { list ->
        maxOf(list.getOrElse(index1) { 0 }, list.getOrElse(index2) { 0 })
    }
}

//Map simulating an x/y-graph, all values initialized to 0. Keys are the Y-axis length.
val mapOfCoordinateOccurrences: MutableMap<Int, MutableList<Int>> =
    (0 .. getCoordinateBounds(mapWithInputCoordinates, 1, 3))
        .associateWith { MutableList(getCoordinateBounds(mapWithInputCoordinates, 0, 2) + 1) { 0 }
}.toMutableMap()


//----------

fun main() {
    //Filling the coordinates with occurrences of lines, value corresponds to how many times a line is passing each coordinate
    populateCoordinateMap(mapWithInputCoordinates)

    //Counting how many times at least 2 lines are passing a coordinate
    println("Final result: " + countOccurrences(mapOfCoordinateOccurrences))
}

//----------

//Filling the coordinates with occurrences of lines, value corresponds to how many times a line is passing each coordinate
fun populateCoordinateMap(map: Map<Int, List<Int>>) {
    //For each entry (key/value-pair) in the input map
    for ((_, value) in map) {
        val startX = minOf(value[0], value[2])
        val endX = maxOf(value[0], value[2])
        val startY = minOf(value[1], value[3])
        val endY = maxOf(value[1], value[3])

        //Iterating over keys in the coord.map, range is start and end-Y
        for (y in startY..endY) {
            //For each key, iterate over x-axis (row), i.e. values in the list. Only targeting my start and end-x.
            mapOfCoordinateOccurrences[y]?.let { row ->
                for (x in startX..endX) {
                    if (x < row.size) row[x] += 1
                }
            }
        }
    }
}

//Counting how many times at least 2 lines are passing a coordinate
fun countOccurrences (input: Map <Int, List<Int>>): Int {
    var counter = 0
    for (entry in input) {
        for (value in entry.value) {
            if (value > 1)
                counter++
        }
    }
    return counter
}