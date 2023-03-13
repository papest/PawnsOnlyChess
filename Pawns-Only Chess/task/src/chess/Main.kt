package chess

const val SIZE = 8
const val TITLE = "Pawns-Only Chess"

fun main() {
    val horizontalBorder = "+---".repeat(SIZE) + "+"
    val bottomNumbers = "    ${('a' until 'a' + SIZE).joinToString("   ")}"
    val chessBoard = mutableListOf<MutableList<Char>>()

    for (i in 0 until SIZE) {
        for (j in 0 until SIZE) {
            chessBoard.add(mutableListOf())
            chessBoard[i].add(
                when (i) {
                    1 -> 'B'
                    SIZE - 2 -> 'W'
                    else -> ' '
                }
            )
        }
    }

    println(TITLE)

    for (i in 0 until SIZE) {
        println("  $horizontalBorder")
        println("${SIZE - i} | ${chessBoard[i].joinToString(" | ")} |")
    }

    println("  $horizontalBorder")
    println(bottomNumbers)

}