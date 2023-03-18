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

    private fun checkStalemate(army: Army, lastTurn: String): Boolean {

        for (i in 0 until SIZE) {
            for (j in 0 until SIZE)
                if (chessBoard[i][j] == army.symbol) {
                    if (i == army.endRank) continue
                    if (chessBoard[i + army.forward][j] == ' ') return false
                    val opponentSymbol = Army.values()[(army.ordinal + 1) % 2].symbol
                    if (j - 1 > 0 && chessBoard[i][j - 1] == opponentSymbol || j + 1 < SIZE &&
                        chessBoard[i + army.forward][j + 1] == opponentSymbol
                    ) return false
                    if (rankToInt(lastTurn[3]) == i && (fileToInt(lastTurn[2]) == j - 1 ||
                                fileToInt(lastTurn[2]) == j + 1)
                        && chessBoard[rankToInt(lastTurn[3] + army.forward)][fileToInt(lastTurn[2])] == ' '
                    )
                        return false
                }
        }
        return true
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
            val army = Army.values()[next++]
            val turn = turn(army, lastTurn)
            if (turn == "exit") break
            lastTurn = turn
            printChessBoard()

            if (!checkAnyPawns(Army.values()[next % 2].symbol) || checkWinningPawn(army)) {
                println("${army.name[0].uppercase() + army.name.substring(1, army.name.length)} Wins!")
                break
            }
            if (checkStalemate(Army.values()[next % 2], lastTurn)) {
                println("Stalemate!")
                break
            }

        }

        println("Bye!")
    }

    private fun checkWinningPawn(army: Army): Boolean {
        return chessBoard[army.endRank].contains(army.symbol)
    }

    private fun checkAnyPawns(symbol: Char): Boolean {
        for (i in 0 until SIZE) {
            for (j in 0 until SIZE) {
                if (chessBoard[i][j] == symbol) return true
            }
        }
        return false
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
    val forward: Int,
    private val extraPosition: Int,
    private val extraForward: Int,
    val endRank: Int
) {
    WHITE('W', "", -1, 6, -2, 0),
    BLACK('B', "", 1, 1, 2, 7);

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