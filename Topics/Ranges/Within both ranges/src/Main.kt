fun main() {

    val range1 = readln().toInt()..readln().toInt()
    val range2 = readln().toInt()..readln().toInt()
    val number = readln().toInt()

    println(number in range1 && number in range2)
}