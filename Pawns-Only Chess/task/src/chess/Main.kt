package chess

object ChessBoard {
    const val SIZE = 8
    private const val TITLE = "Pawns-Only Chess"

    private val horizontalBorder = "+---".repeat(SIZE) + "+"
    private val bottomNumbers = "    ${('a' until 'a' + SIZE).joinToString("   ")}"
    val chessBoard = mutableListOf<MutableList<Char>>()

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

    private fun printChessBoard() {
        for (i in 0 until SIZE) {
            println("  $horizontalBorder")
            println("${SIZE - i} | ${chessBoard[i].joinToString(" | ")} |")
        }

        println("  $horizontalBorder")
        println(bottomNumbers)
    }

    private fun printChessBoardTitle() {
        println(TITLE)
    }

    private fun fileToInt(file: Char): Int = file - 'a'
    private fun rankToInt(rank: Char): Int = SIZE - (rank - '0')

    private fun turn(army: Army): String {
        val lastLetter = 'a' + SIZE - 1
        val regExp = "[a-$lastLetter][1-$SIZE][a-$lastLetter][1-$SIZE]".toRegex()

        while (true) {
            println("${army.userName}'s turn:")
            val turn = readln()
            if (turn == "exit") return turn
            if (turn.matches(regExp)) {
                try {
                    army.forward(
                        fileToInt(turn[0]),
                        rankToInt(turn[1]),
                        fileToInt(turn[2]),
                        rankToInt(turn[3])
                    )
                    chessBoard[rankToInt(turn[1])][fileToInt(turn[0])] = ' '
                    chessBoard[rankToInt(turn[3])][fileToInt(turn[2])] = army.symbol
                    return turn

                } catch (e: Exception) {
                    println(e.message)
                }

            } else {
                println("Invalid Input")
            }
        }
    }

    fun play() {
        printChessBoardTitle()
        readUserNames()
        printChessBoard()

        var next = 0

        while (true) {
            next %= 2
            if (turn(Army.values()[next++]) == "exit") break
            printChessBoard()
        }

        println("Bye!")
    }
}

fun readUserNames() {
    println("First Player's name:")
    Army.WHITE.userName = readln()
    println("Second Player's name:")
    Army.BLACK.userName = readln()
}

enum class Army(
    val symbol: Char,
    var userName: String,
    private val forward: Int,
    private val extraPosition: Int,
    private val extraForward: Int
) {
    WHITE('W', "", -1, 6, -2),
    BLACK('B', "", 1, 1, 2);

    fun forward(fromFile: Int, fromRank: Int, toFile: Int, toRank: Int) {
        if (ChessBoard.chessBoard[fromRank][fromFile] != symbol)
            throw Exception("No ${this.name.lowercase()} pawn at ${'a' + fromFile}${ChessBoard.SIZE - fromRank}")
        if (ChessBoard.chessBoard[toRank][toFile] != ' ') throw Exception("Invalid input")
        if (fromFile != toFile) throw Exception("Invalid input")
        if (forward == toRank - fromRank || (extraPosition == fromRank && extraForward == toRank - fromRank)) return
        else throw Exception("Invalid input")
    }
}


fun main() {
    ChessBoard.play()
}