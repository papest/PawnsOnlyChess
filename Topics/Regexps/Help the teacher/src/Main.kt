fun main() {

    val report = readLine()!!
    println(report.matches("[0-9] wrong answers?".toRegex()))
}