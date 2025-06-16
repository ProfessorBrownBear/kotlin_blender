import kotlin.random.Random

// 1. Define the Fruit Interface
// All our fruit classes will implement this interface.
interface Fruit {
    val name: String // Every fruit has a name
    fun prepare(): String // How each fruit is prepared for blending
}

// 2. Concrete Fruit Classes (implementing the Fruit interface)
// Each class represents a specific type of fruit.

class Strawberry : Fruit {
    override val name = "Strawberry"
    override fun prepare(): String {
        return "Hull and slice the $name"
    }
}

class Cherry : Fruit {
    override val name = "Cherry"
    override fun prepare(): String {
        return "Pit and halve the $name"
    }
}

class Mango : Fruit {
    override val name = "Mango"
    override fun prepare(): String {
        return "Peel, pit, and cube the $name"
    }
}

class Raspberry : Fruit {
    override val name = "Raspberry"
    override fun prepare(): String {
        return "Gently rinse the $name"
    }
}

// You could add more fruits here following the same pattern.
// class Banana : Fruit { ... }


// 3. FruitFactory: A "Function Factory" (using a companion object for easy access)
// This object contains a factory function to create Fruit instances based on a string name.
class FruitFactory {
    companion object {
        /**
         * Creates a Fruit object based on the provided fruit name.
         * This is our "factory function" for creating fruit instances.
         * It encapsulates the logic of which specific Fruit class to instantiate.
         *
         * @param fruitName The name of the fruit (e.g., "strawberry", "mango").
         * @return An instance of a Fruit (e.g., Strawberry, Mango).
         * @throws IllegalArgumentException if the fruit name is unknown.
         */
        fun createFruit(fruitName: String): Fruit {
            return when (fruitName.lowercase()) { // Changed to lowercase() as recommended
                "strawberry" -> Strawberry()
                "cherry" -> Cherry()
                "mango" -> Mango()
                "raspberry" -> Raspberry()
                else -> throw IllegalArgumentException("Sorry, we don't have $fruitName for the smoothie.")
            }
        }
    }
}


// 4. Blender Class (A Singleton using Kotlin's 'object' keyword)
// The 'object' keyword in Kotlin automatically makes this a singleton.
// There will only ever be one instance of Blender in your program.
object Blender {
    // A mutable collection to hold the fruit objects inside the blender.
    private val contents: MutableList<Fruit> = mutableListOf()

    // Public property to observe the current contents (read-only from outside)
    val currentContents: List<Fruit>
        get() = contents.toList() // Return a copy to prevent external modification

    /**
     * Adds a fruit object to the blender's contents.
     * This simulates moving a fruit from the 'ingredients' pool into the blender.
     *
     * @param fruit The Fruit object to add.
     */
    fun addFruit(fruit: Fruit) {
        println("Adding ${fruit.name} to the blender. (${fruit.prepare()})")
        contents.add(fruit)
    }

    /**
     * Simulates the blending process.
     * It "whirls" for a specified duration and then reports the final smoothie.
     */
    fun start() {
        if (contents.isEmpty()) {
            println("The blender is empty! Can't make a smoothie without fruits.")
            return
        }

        println("\n--- Blender activated! Whirling for 30 seconds... ---")
        // Simulate a delay for 30 seconds.
        // In a real application, you'd use coroutines or threads for non-blocking UI.
        Thread.sleep(30 * 1000L) // 30 seconds in milliseconds

        println("\n--- Blending complete! ---")
        println("Your delicious smoothie is ready!")
        println("It contains:")
        contents.forEach { fruit ->
            println("- ${fruit.name}")
        }
        println("\nEnjoy your freshly blended masterpiece!\n")

        contents.clear() // Clear the blender for the next smoothie
    }

    /**
     * Checks if the blender is empty.
     */
    fun isEmpty(): Boolean = contents.isEmpty()
}


// 5. Main function: The entry point of our program
fun main() {
    // Initial array of fruit names (our "ingredients" from the basket)
    val initialFruitNames = mutableListOf("strawberry", "cherry", "mango", "raspberry", "strawberry") // Added one more strawberry for variety

    println("Welcome to the SMOOTHIE MAKER!\n")
    println("Available ingredients to pick from: $initialFruitNames\n")

    // Loop through the initial fruit names and add them to the Blender.
    // As they are processed here, they are conceptually "removed" from the 'initialFruitNames'
    // as they transition into the 'Blender's contents.
    val fruitsToAdd = initialFruitNames.toList() // Create a copy to iterate
    val removedFruits = mutableListOf<String>() // To keep track of what's added to the blender

    for (fruitName in fruitsToAdd) {
        try {
            val fruit = FruitFactory.createFruit(fruitName)
            Blender.addFruit(fruit)
            removedFruits.add(fruitName) // Track that it was added to blender
        } catch (e: IllegalArgumentException) {
            println("Error: ${e.message}")
        }
    }

    println("\nAll selected fruits are now in the blender.")
    // Demonstrating that the 'initialFruitNames' list can conceptually be thought of as "consumed"
    // by moving items to the blender. In a real app, you might explicitly remove them:
    // initialFruitNames.removeAll(removedFruits)
    // println("Remaining in basket: $initialFruitNames") // Would be empty if all processed

    println("Current blender contents: ${Blender.currentContents.map { it.name }.joinToString(", ")}\n")

    // Start the blending process
    Blender.start()

    // Verify the blender is empty after making the smoothie
    if (Blender.isEmpty()) {
        println("Blender is now clean and empty, ready for the next batch!")
    }
}
