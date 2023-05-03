package calculator

import java.math.BigInteger

private const val VARIABLE_OR_NUMBER = "(\\d+|[a-zA-Z]+)"
private const val COMMAND_PATTERN = "^\\s*/\\w*\\s*$"

private val signs = linkedSetOf('+', '-', '*', '/', '^')
private val scopes = linkedSetOf('(', ')')

// "^\s*[+-]?\s*(\d+|[a-zA-Z]+)\s*((?:(([+-]+\s*)*|[*/]\s*)-?\s*(\d+|[a-zA-Z]+)\s*)*)$ - деление, умножение, сложение, вычитание
// ^\s*-?(?:\s*\()*\s*-?\s*(\d+|[a-zA-Z]+)(?:\s*\))*\s*(?:([+-]+(\s*[+-]*)*|[*/])(?:\s*\()*\s*-?\s*(\d+|[a-zA-Z]+)(?:\s*\))*\s*)*$ - деление, умножение, сложение, вычитание, скобки
private const val EXPRESSION_PATTERN =
    "^-?(\\()*-?$VARIABLE_OR_NUMBER(\\))*(([+-]+([+-]*)*|[*/^])(\\()*-?$VARIABLE_OR_NUMBER(\\))*)*$"
private const val ADD_SOMETHING_PATTERN = "^.+=.+$"
private const val ADD_VARIABLE_PATTERN = "^[a-zA-Z]+=-?${VARIABLE_OR_NUMBER}$"

fun main() {
    var isAlive = true
    val variables = HashMap<String, BigInteger>()
    while (isAlive) {
        val input = readln()
        if (input.isBlank()) continue
        when {
            input.isCommand -> {
                when (input) {
                    "/exit" -> {
                        isAlive = false
                        null
                    }

                    "/help" -> "The program calculates the value of expressions with brackets and basic mathematical operations,\n" +
                            "handling (binary and unary) addition and subtraction, ignoring spaces. Supports the ability to\n" +
                            "save integer values to a variable whose name contains only latin letters"

                    else -> "Unknown command"
                }
            }

            input.isSomethingAssign -> {
                when (input.assignVariable(map = variables)) {
                    true -> null
                    false -> "Unknown variable"
                    null -> "Invalid identifier"
                }
            }

            input.isExpression -> {
                var scopes = 0

                Regex(pattern = "[)(]").findAll(input).map { it.value }.forEach {
                    if (it == "(") scopes++
                    else scopes--
                }

                if (scopes == 0) input.parseAndCountResult(map = variables)
                    ?: "Unknown variable"
                else "Invalid expression"
                /*
                if (scopes > 0) "Missing closing bracket(s)"
                else "Missing opening bracket(s)"
                 */
            }

            else -> {
                "Invalid expression"
            }
        }?.let { println(it) }
    }
    print("Bye!")
}

private val String.isCommand
    get() = matches(regex = Regex(pattern = COMMAND_PATTERN))

private val String.isExpression
    get() = replace(regex = Regex(pattern = "\\s+"), replacement = "")
        .matches(regex = Regex(pattern = EXPRESSION_PATTERN))

private val String.isSomethingAssign
    get() = replace(regex = Regex(pattern = "\\s+"), replacement = "")
        .matches(regex = Regex(pattern = ADD_SOMETHING_PATTERN))

private fun String.assignVariable(map: HashMap<String, BigInteger>) =
    with(receiver = replace(regex = Regex(pattern = "\\s+"), replacement = "")) {
        if (!matches(regex = Regex(ADD_VARIABLE_PATTERN))) null
        else split("=").run {
            (last().toBigIntegerOrNull() ?: map[last()])?.let {
                map[first()] = it
                true
            } ?: false
        }
    }

private fun String.parseAndCountResult(map: HashMap<String, BigInteger>) =
    replace(regex = Regex(pattern = "\\s+"), replacement = "")
        .toRPN(map = map)
        ?.countResult()

private fun MutableList<String>.countResult(): BigInteger {
    while (size > 1) {
        val mathSymbolIndex = indexOfFirst { it.length == 1 && signs.contains(it.first()) }
        val num0 = get(mathSymbolIndex - 2).toBigInteger()
        val num1 = get(mathSymbolIndex - 1).toBigInteger()
        set(
            index = mathSymbolIndex - 2,
            element = when (get(mathSymbolIndex).first()) {
                '+' -> {
                    num0 + num1
                }

                '-' -> {
                    num0 - num1
                }

                '*' -> {
                    num0 * num1
                }

                '/' -> {
                    num0 / num1
                }

                else -> {
                    var result = num0
                    var cycleCount = num1 - BigInteger.ONE
                    while (cycleCount > BigInteger.ZERO) {
                        result *= num0
                        cycleCount--
                    }
                    result
                }
            }
                .toString()
        )
        repeat(times = 2) { removeAt(index = mathSymbolIndex - 1) }
    }
    return get(0).toBigInteger()
}

private fun String.toRPN(map: HashMap<String, BigInteger>): MutableList<String>? {
    val resultList = mutableListOf<String>()
    val stack = mutableListOf<Char>()

    val membersOfExpression = Regex(pattern = "(-?$VARIABLE_OR_NUMBER)|\\(|\\)|\\*|\\^|/|[+-]+")
        .findAll(input = this)
        .map { it.value }
        .toMutableList()
    if (membersOfExpression.first().matches(regex = Regex(pattern = "^-.*"))) membersOfExpression.add(
        index = 0, element = "0"
    )
    var index = 1
    while (index < membersOfExpression.size) {
        if (
            membersOfExpression[index].matches(regex = Regex(pattern = "^-$VARIABLE_OR_NUMBER$")) &&
            !membersOfExpression[index - 1].matches(regex = Regex(pattern = "^[*+-/(]$"))
        ) {
            membersOfExpression.add(index = index + 1, membersOfExpression[index].substring(startIndex = 1))
            membersOfExpression[index] = "-"
        }
        index++
    }
    membersOfExpression.forEach { member ->
        fun String.simplifyMultipleMinusesAndPluses() = when {
            !matches(regex = Regex(pattern = "^[+-]+$")) -> first()
            count { it == '-' } % 2 == 0 -> '+'
            else -> '-'
        }

        when {
            member.matches(regex = Regex(pattern = "^-?\\d+$")) -> {
                resultList += member
            }

            member.matches(regex = Regex(pattern = "^-?[a-zA-Z]+$")) -> {
                map[member]?.let { value ->
                    resultList += value.toString()
                } ?: return null
            }

            member.first() == scopes.last() -> {
                while (stack.last() != scopes.first()) {
                    resultList += stack.removeLast().toString()
                }
                stack.removeLast()
            }

            stack.isEmpty() || stack.last() == scopes.first() || member.first() == scopes.first() ||
                    signs.indexOf(member.first()) / 2 > signs.indexOf(stack.last()) / 2 -> {
                stack += member.simplifyMultipleMinusesAndPluses()
            }

            signs.indexOf(member.first()) / 2 <= signs.indexOf(stack.last()) / 2 -> {
                while (
                    stack.isNotEmpty() && stack.last() != scopes.first() &&
                    signs.indexOf(member.first()) / 2 <= signs.indexOf(stack.last()) / 2
                ) {
                    resultList += stack.removeLast().toString()
                }
                stack += member.simplifyMultipleMinusesAndPluses()
            }
        }
    }
    stack.reversed().forEach { resultList += it.toString() }
    return resultList
}
