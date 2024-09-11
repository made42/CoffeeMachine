package machine

import kotlin.system.exitProcess

enum class Coffee(val water: Int, val milk: Int, val beans: Int, val price: Int) {
    ESPRESSO(250, 0, 16, 4),
    LATTE(350, 75, 20, 7),
    CAPPUCCINO(200, 100, 12, 6)
}

class Machine(private var water: Int, private var milk: Int, private var beans: Int, private var cups: Int, private var money: Int) {
    private enum class State(val message: String) {
        CHOOSE_ACTION("Write action (buy, fill, take, remaining, exit):"),
        CHOOSE_TYPE("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:"),
        FILL_WATER("Write how many ml of water you want to add:"),
        FILL_MILK("Write how many ml of milk you want to add:"),
        FILL_BEANS("Write how many grams of coffee beans you want to add:"),
        FILL_CUPS("Write how many disposable cups you want to add:")
    }

    private lateinit var state: State

    init { setState(State.CHOOSE_ACTION) }

    private fun setState(newState: State) { this.state = newState; println(state.message) }

    fun input(input: String) {
        when (state) {
            State.CHOOSE_ACTION -> {
                when (input) {
                    "buy" -> { println(); setState(State.CHOOSE_TYPE) }
                    "fill" -> { println(); setState(State.FILL_WATER) }
                    "take" -> { println("I gave you $money"); money = 0; setState(State.CHOOSE_ACTION) }
                    "remaining" -> {
                        println("\nThe coffee machine has:\n$water ml of water\n$milk ml of milk\n" +
                                "$beans g of coffee beans\n$cups disposable cups\n$$money of money\n")
                        setState(State.CHOOSE_ACTION)
                    }
                    "exit" -> exitProcess(0)
                }
            }
            State.CHOOSE_TYPE -> {
                when (input.toIntOrNull()) {
                    1 -> get(Coffee.ESPRESSO)
                    2 -> get(Coffee.LATTE)
                    3 -> get(Coffee.CAPPUCCINO)
                }
                println()
                setState(State.CHOOSE_ACTION)
            }
            State.FILL_WATER -> { water += input.toInt(); setState(State.FILL_MILK) }
            State.FILL_MILK -> { milk += input.toInt(); setState(State.FILL_BEANS) }
            State.FILL_BEANS -> { beans += input.toInt(); setState(State.FILL_CUPS) }
            State.FILL_CUPS -> { cups += input.toInt(); setState(State.CHOOSE_ACTION) }
        }
    }

    private fun get(coffee: Coffee) {
        when {
            coffee.water > water -> { println("Sorry, not enough water!"); return }
            coffee.milk > milk -> { println("Sorry, not enough milk!"); return }
            coffee.beans > beans -> { println("Sorry, not enough beans!"); return }
            cups == 0 -> { println("Sorry, not enough cups!"); return }
            coffee.price > money -> { println("Sorry, not enough money!"); return }
        }
        println("I have enough resources, making you a coffee!")
        water -= coffee.water
        milk -= coffee.milk
        beans -= coffee.beans
        cups--
        money += coffee.price
    }
}

fun main() {
    val machine = Machine(400, 540, 120, 9, 550)
    while (true) machine.input(readln())
}