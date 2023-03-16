package chess

import kotlin.math.abs

object ChessBoard {
    private const val SIZE = 8
    private const val TITLE = "Pawns-Only Chess"

    private val horizontalBorder = "+---".repeat(SIZE) + "+"
    private val bottomNumbers = "    ${('a' until 'a' + SIZE).joinToString("   ")}"
    private const val lastLetter = 'a' + SIZE - 1
    private val regExp = "[a-$lastLetter][1-$SIZE][a-$lastLetter][1-$SIZE]".toRegex()
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

    fun fileToInt(file: Char): Int = file - 'a'
    fun rankToInt(rank: Char): Int = SIZE - (rank - '0')

    private fun turn(army: Army, lastTurn: String): String {

        while (true) {
            println("${army.userName}'s turn:")
            val turn = readln()
            if (turn == "exit") return turn
            if (turn.matches(regExp)) {
                try {
                    val f1 = fileToInt(turn[0])
                    val f2 = fileToInt(turn[2])
                    val r1 = rankToInt(turn[1])
                    val r2 = rankToInt(turn[3])

                    if (chessBoard[r1][f1] != army.symbol)
                        throw Exception("No ${army.name.lowercase()} pawn at ${'a' + f1}${SIZE - r1}")

                    if (f1 != f2) {
                        if (chessBoard[r2][f2] != ' ') army.capture(f1, r1, f2, r2)
                        else {
                            army.enPassant(f1, r1, f2, r2, lastTurn)
                            chessBoard[rankToInt(lastTurn[3])][fileToInt(lastTurn[2])] = ' '
                        }

                    } else army.forward(f1, r1, f2, r2)

                    chessBoard[r1][f1] = ' '
                    chessBoard[r2][f2] = army.symbol
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
        var lastTurn = ""

        while (true) {
            next %= 2
            val turn = turn(Army.values()[next++], lastTurn)
            if (turn == "exit") break
            lastTurn = turn
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
        if (ChessBoard.chessBoard[toRank][toFile] != ' ') throw Exception("Invalid input")
        if (fromFile == toFile &&
            (forward == toRank - fromRank || (extraPosition == fromRank && extraForward == toRank - fromRank))
        ) return
        else throw Exception("Invalid input")
    }

    fun enPassant(fromFile: Int, fromRank: Int, toFile: Int, toRank: Int, lastTurn: String) {
        if (lastTurn.isNotEmpty()
            && ChessBoard.rankToInt(lastTurn[3]) == fromRank && ChessBoard.fileToInt(lastTurn[2]) == toFile
            && abs(toFile - fromFile) == 1 && (toRank - fromRank == forward)
        ) return
        else throw Exception("Invalid input")
    }

    fun capture(fromFile: Int, fromRank: Int, toFile: Int, toRank: Int) {
        if (toRank - fromRank == forward && abs(toFile - fromFile) == 1) return
        throw Exception("Invalid input")
    }
}

fun main() {
    ChessBoard.play()
}