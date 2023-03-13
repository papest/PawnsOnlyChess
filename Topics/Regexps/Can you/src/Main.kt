fun main() {
    val answer = readln()
    val regexp = "I can('t)? do my homework on time!".toRegex()
    println(answer.matches(regexp))
}