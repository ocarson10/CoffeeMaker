package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService  inventoryService;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private RecipeService     recipeService;

    // private Inventory ivt;
    //
    // LinkedList<Ingredient> ingredients;

    @BeforeEach
    public void setup () {
        inventoryService.deleteAll();
    }

    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.addIngredient( new Ingredient( "Matcha", 100 ) );
        ivt.addIngredient( new Ingredient( "Milk", 100 ) );
        inventoryService.save( ivt );
        Assert.assertEquals( ivt.getIngredients().size(), 2 );

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        final Ingredient i1 = new Ingredient( "Matcha", 2 );
        final Ingredient i2 = new Ingredient( "Milk", 4 );
        ingredientService.save( i1 );
        ingredientService.save( i2 );
        recipe.addIngredient( i1 );
        recipe.addIngredient( i2 );

        recipe.setPrice( 5 );
        recipeService.save( recipe );
        Assert.assertEquals( recipe.getIngredients().size(), 2 );

        ivt.useIngredients( recipe );

        Assert.assertEquals( 98, (int) ivt.getIngredients().get( 0 ).getAmount() );
        Assert.assertEquals( 96, (int) ivt.getIngredients().get( 1 ).getAmount() );

    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.addIngredient( new Ingredient( "Matcha", 200 ) );
        ivt.addIngredient( new Ingredient( "Coffee", 200 ) );
        ivt.addIngredient( new Ingredient( "Milk", 200 ) );
        ivt.addIngredient( new Ingredient( "Sugar", 200 ) );
        inventoryService.save( ivt );
        Assert.assertEquals( ivt.getIngredients().size(), 4 );

        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values for matcha", 200,
                (int) ivt.getIngredients().get( 0 ).getAmount() );
        Assert.assertEquals( "Adding to the inventory should result incorrectly-updated values for coffee", 200,
                (int) ivt.getIngredients().get( 1 ).getAmount() );
        Assert.assertEquals( "Adding to the inventory should result incorrectly-updated values milk", 200,
                (int) ivt.getIngredients().get( 2 ).getAmount() );
        Assert.assertEquals( "Adding to the inventory should result incorrectly-updated values sugar", 200,
                (int) ivt.getIngredients().get( 3 ).getAmount() );

    }

    @Test
    @Transactional
    public void testAddInventory2 () {
        // final Inventory ivt = inventoryService.getInventory();
        final Inventory ivt = inventoryService.getInventory();
        ivt.addIngredient( new Ingredient( "Matcha", 200 ) );
        inventoryService.save( ivt );
        Assert.assertEquals( ivt.getIngredients().size(), 1 );
        try {
            ivt.setIngredient( "Matcha", -20 );
        }
        catch ( final IllegalArgumentException e ) {
            Assert.assertEquals( "Trying to update the Inventory with an invalid value should result in no changes",
                    200, (int) ivt.getIngredientsByName( "Matcha" ).getAmount() );
        }

    }

    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory ivt = inventoryService.getInventory();
        ivt.addIngredient( new Ingredient( "Matcha", 200 ) );
        ivt.addIngredient( new Ingredient( "Coffee", 200 ) );
        inventoryService.save( ivt );
        Assert.assertEquals( ivt.getIngredients().size(), 2 );
        try {
            ivt.addIngredientAmount( "Coffee", 20 );
            ivt.addIngredientAmount( "Matcha", -20 );
        }
        catch ( final IllegalArgumentException e ) {
            Assert.assertEquals( "Trying to update the Inventory with an invalid value should result in no changes",
                    200, (int) ivt.getIngredientsByName( "Matcha" ).getAmount() );
        }
        Assert.assertTrue( ivt.getIngredientsByName( "Coffee" ).getAmount().equals( 220 ) );
    }

    @Test
    @Transactional
    public void testCheckIngredientAmount () {
        final Inventory ivt = inventoryService.getInventory();
        ivt.addIngredient( new Ingredient( "Matcha", 200 ) );
        ivt.addIngredient( new Ingredient( "Coffee", 200 ) );
        inventoryService.save( ivt );
        Assert.assertEquals( ivt.getIngredients().size(), 2 );
        Assert.assertTrue( ivt.checkIngredientAmount( "230" ).equals( 230 ) );

        try {
            ivt.checkIngredientAmount( "-230" );
        }
        catch ( final IllegalArgumentException e ) {
            Assert.assertEquals( "Units of ingredient must be a positive integer", e.getMessage() );

        }

    }

    // Chitra Srinivasan (csriniv) milestone 1 individual test #3
    @Test
    @Transactional
    public void testEmptyInventory () {
        final Inventory ivt = inventoryService.getInventory();

        inventoryService.save( ivt );

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        final Ingredient i1 = new Ingredient( "Matcha", 2 );
        final Ingredient i2 = new Ingredient( "Milk", 4 );
        ingredientService.save( i1 );
        ingredientService.save( i2 );
        recipe.addIngredient( i1 );
        recipe.addIngredient( i2 );

        recipe.setPrice( 5 );
        recipeService.save( recipe );
        Assert.assertEquals( recipe.getIngredients().size(), 2 );

        Assert.assertFalse( ivt.enoughIngredients( recipe ) );

    }

    @Test
    @Transactional
    public void testCheckIsEnoughIngredients () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.addIngredient( new Ingredient( "Matcha", 100 ) );
        ivt.addIngredient( new Ingredient( "Milk", 100 ) );
        inventoryService.save( ivt );

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );

        recipe.addIngredient( new Ingredient( "Matcha", 2 ) );
        recipe.addIngredient( new Ingredient( "Milk", 4 ) );
        recipe.addIngredient( new Ingredient( "Water", 4 ) );

        recipe.setPrice( 5 );
        recipeService.save( recipe );
        Assert.assertEquals( recipe.getIngredients().size(), 3 );

        Assert.assertFalse( ivt.enoughIngredients( recipe ) );

        final Inventory ivt2 = inventoryService.getInventory();

        ivt2.addIngredient( new Ingredient( "Matcha", 100 ) );
        ivt2.addIngredient( new Ingredient( "Milk", 100 ) );
        inventoryService.save( ivt2 );

        final Recipe recipe2 = new Recipe();
        recipe2.setName( "Not-Coffee" );

        recipe2.addIngredient( new Ingredient( "Matcha", 2 ) );
        recipe2.addIngredient( new Ingredient( "Milk", 4 ) );

        recipe2.setPrice( 5 );
        recipeService.save( recipe2 );
        Assert.assertEquals( recipe2.getIngredients().size(), 2 );

        Assert.assertTrue( ivt.enoughIngredients( recipe ) );

        final Inventory ivt3 = inventoryService.getInventory();

        ivt3.addIngredient( new Ingredient( "Matcha", 100 ) );
        ivt3.addIngredient( new Ingredient( "Milk", 2 ) );
        inventoryService.save( ivt3 );

        final Recipe recipe3 = new Recipe();
        recipe3.setName( "Matcha Latte" );

        recipe3.addIngredient( new Ingredient( "Matcha", 2 ) );
        recipe3.addIngredient( new Ingredient( "Milk", 4 ) );

        recipe3.setPrice( 5 );
        recipeService.save( recipe3 );
        Assert.assertEquals( recipe3.getIngredients().size(), 2 );

        Assert.assertFalse( ivt.enoughIngredients( recipe ) );
    }

    @Test
    @Transactional
    public void testToString () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.addIngredient( new Ingredient( "Matcha", 100 ) );
        ivt.addIngredient( new Ingredient( "Milk", 100 ) );
        inventoryService.save( ivt );
        Assert.assertEquals( ivt.getIngredients().size(), 2 );
        final StringBuffer buf = new StringBuffer();
        for ( final Ingredient i : ivt.getIngredients() ) {
            buf.append( i.getName() + ": " );
            buf.append( i.getAmount() );
            buf.append( "\n" );
        }
        Assert.assertEquals( buf.toString(), ivt.toString() );
        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        final Ingredient i1 = new Ingredient( "Matcha", 2 );
        final Ingredient i2 = new Ingredient( "Milk", 4 );
        ingredientService.save( i1 );
        ingredientService.save( i2 );
        recipe.addIngredient( i1 );
        recipe.addIngredient( i2 );

        recipe.setPrice( 5 );
        recipeService.save( recipe );
        Assert.assertEquals( recipe.getIngredients().size(), 2 );

        ivt.useIngredients( recipe );

        final StringBuffer buf2 = new StringBuffer();
        for ( final Ingredient i : ivt.getIngredients() ) {
            buf2.append( i.getName() + ": " );
            buf2.append( i.getAmount() );
            buf2.append( "\n" );
        }
        Assert.assertEquals( buf2.toString(), ivt.toString() );
    }

}
