package chess

object ChessBoard {
    private const val SIZE = 8
    private const val TITLE = "Pawns-Only Chess"

    private val horizontalBorder = "+---".repeat(SIZE) + "+"
    private val bottomNumbers = "    ${('a' until 'a' + SIZE).joinToString("   ")}"
    private val chessBoard = mutableListOf<MutableList<Char>>()

    init {
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
    }

    fun printChessBoard() {
        for (i in 0 until SIZE) {
            println("  $horizontalBorder")
            println("${SIZE - i} | ${chessBoard[i].joinToString(" | ")} |")
        }

        println("  $horizontalBorder")
        println(bottomNumbers)
    }

    fun printChessBoardTitle() {
        println(TITLE)
    }

    fun turn(name: String): String {
        val lastLetter = 'a' + SIZE - 1
        val regExp = "[a-$lastLetter][1-$SIZE][a-$lastLetter][1-$SIZE]".toRegex()

        while (true) {
            println("$name's turn:")
            val turn = readln()
            if (turn == "exit" || turn.matches(regExp)) return turn
            println("Invalid Input")
        }
    }
}

fun readUserNames(): MutableList<String> {
    val users = mutableListOf<String>()
    println("First Player's name:")
    users.add(readln())
    println("Second Player's name:")
    users.add(readln())
    return users
}

fun main() {
    ChessBoard.printChessBoardTitle()
    val users = readUserNames()
    ChessBoard.printChessBoard()
    var turn = ""
    var count = 0
    while (turn != "exit") {
        count = (count % 2)
        turn = ChessBoard.turn(users[count++])
    }
    println("Bye!")
}