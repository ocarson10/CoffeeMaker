package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
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

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;
    

    @Autowired
    private IngredientService ingredientService;
    
    LinkedList<Ingredient> ingredients;


    @BeforeEach
    public void setup () {
    	final Inventory ivt = inventoryService.getInventory(); 
    	ingredients = new LinkedList<Ingredient>();
    	Ingredient i1 = new Ingredient("Matcha", 100);
    	Ingredient i2 = new Ingredient("Milk", 100);
    	
    	ingredients.add(i1);
    	ingredients.add(i2);

    	ingredientService.saveAll(ingredients);

    	ivt.addNewIngredients(i1);
    	ivt.addNewIngredients(i2);

    	inventoryService.save(ivt);
    	
    }

    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        Ingredient i1 = new Ingredient("Matcha", 100);
    	Ingredient i2 = new Ingredient("Milk", 100);
    	ingredientService.save(i1);
    	ingredientService.save(i2);
    	ingredients.add(i1);
    	ingredients.add(i2);
    


        recipe.setIngredient("Matcha", 2);
        recipe.setIngredient("Milk", 4);
       
        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */
        Assert.assertEquals( 98, (int) i.getIngredients().get(0).getAmount() );
        Assert.assertEquals( 96, (int) i.getIngredients().get(1).getAmount() );
       
    }

    @Test
    @Transactional
    public void testAddInventory1 () {
    	inventoryService.deleteAll();
    	ingredientService.deleteAll();
        Inventory ivt = inventoryService.getInventory();        
        
        final LinkedList<Ingredient> ingredients = new LinkedList<Ingredient>();

        final Ingredient i1 = new Ingredient( "Matcha", 100 );
        final Ingredient i2 = new Ingredient( "Coffee", 100 );
        final Ingredient i3 = new Ingredient( "Milk", 100 );
        final Ingredient i4 = new Ingredient( "Sugar", 100 );
        
        ingredients.add( i1 );
        ingredients.add( i2 );
        ingredients.add( i3 );
        ingredients.add( i4 );
        ingredientService.saveAll(ingredients);
        ivt.addNewIngredients(ingredients);

        ivt.addIngredients("Matcha", 100);      
        ivt.addIngredients("Coffee", 100);
        ivt.addIngredients("Milk", 100);
        ivt.addIngredients("Sugar", 100);
        
        ingredients.add( i1 );
        ingredients.add( i2 );
        ingredients.add( i3 );
        ingredients.add( i4 );
        ingredientService.saveAll(ingredients);
        ivt.addNewIngredients(ingredients);

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values for matcha", 200,
                (int) ivt.getIngredients().get(0).getAmount() );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values for coffee", 200,
                (int)  ivt.getIngredients().get(1).getAmount() );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values milk", 200,
                (int)  ivt.getIngredients().get(2).getAmount() );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values sugar", 200,
                (int)  ivt.getIngredients().get(3).getAmount() );

    }

    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( -5, 3, 7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee",
                    500, (int) ivt.getCoffee() );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk",
                    500, (int) ivt.getMilk() );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar",
                    500, (int) ivt.getSugar() );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate",
                    500, (int) ivt.getChocolate() );
        }
    }

    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, -3, 7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- coffee",
                    500, (int) ivt.getCoffee() );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- milk",
                    500, (int) ivt.getMilk() );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- sugar",
                    500, (int) ivt.getSugar() );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- chocolate",
                    500, (int) ivt.getChocolate() );

        }

    }

    @Test
    @Transactional
    public void testAddInventory4 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, 3, -7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- coffee",
                    500, (int) ivt.getCoffee() );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- milk",
                    500, (int) ivt.getMilk() );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- sugar",
                    500, (int) ivt.getSugar() );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- chocolate",
                    500, (int) ivt.getChocolate() );

        }

    }

    @Test
    @Transactional
    public void testAddInventory5 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, 3, 7, -2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- coffee",
                    500, (int) ivt.getCoffee() );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- milk",
                    500, (int) ivt.getMilk() );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- sugar",
                    500, (int) ivt.getSugar() );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- chocolate",
                    500, (int) ivt.getChocolate() );

        }

    }

    @Test
    @Transactional
    public void testCheckInventory () {
        final Inventory i = new Inventory( 500, 500, 500, 500 );

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.setChocolate( 10 );
        recipe.setMilk( 20 );
        recipe.setSugar( 5 );
        recipe.setCoffee( 1 );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */
        Assert.assertEquals( 490, (int) i.getChocolate() );
        Assert.assertEquals( 480, (int) i.getMilk() );
        Assert.assertEquals( 495, (int) i.getSugar() );
        Assert.assertEquals( 499, (int) i.getCoffee() );

        // checking correct values for the ingredients
        final int checkChocolateInt = i.checkChocolate( "4" );
        Assert.assertEquals( 4, checkChocolateInt );

        final int checkMilkInt = i.checkMilk( "8" );
        Assert.assertEquals( 8, checkMilkInt );

        final int checkCoffeeInt = i.checkCoffee( "12" );
        Assert.assertEquals( 12, checkCoffeeInt );

        final int checkSugarInt = i.checkSugar( "20" );
        Assert.assertEquals( 20, checkSugarInt );

        // check invalid values for the ingredients
        try {
            i.checkChocolate( "-2" );
        }
        catch ( final IllegalArgumentException iae ) {
            Assert.assertEquals( 490, (int) i.getChocolate() );
        }

        try {
            i.checkChocolate( "-two" );
        }
        catch ( final IllegalArgumentException iae ) {
            Assert.assertEquals( 490, (int) i.getChocolate() );
        }

        /*
         * Make sure that the string is printed appropriately
         */
        Assert.assertEquals( "Coffee: 499\n" + "Milk: 480\n" + "Sugar: 495\n" + "Chocolate: 490\n", i.toString() );

        Assert.assertTrue( i.enoughIngredients( recipe ) );
    }

    // Chitra Srinivasan (csriniv) milestone 1 individual test #3
    @Test
    @Transactional
    public void testEmptyInventory () {
        inventoryService.deleteAll();
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.setChocolate( 10 );
        recipe.setMilk( 20 );
        recipe.setSugar( 5 );
        recipe.setCoffee( 1 );
        recipe.setPrice( 5 );
        assertFalse( i.enoughIngredients( recipe ) );

    }
    
    @Test
    @Transactional
    public void testInventoryInvalidChecks () {
        final Inventory i = new Inventory(500, 500, 500, 500);

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.setChocolate( 10 );
        recipe.setMilk( 20 );
        recipe.setSugar( 5 );
        recipe.setCoffee( 1 );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */
        Assert.assertEquals( 490, (int) i.getChocolate() );
        Assert.assertEquals( 480, (int) i.getMilk() );
        Assert.assertEquals( 495, (int) i.getSugar() );
        Assert.assertEquals( 499, (int) i.getCoffee() );
        
        // checking correct values for the ingredients
        int checkChocolateInt = i.checkChocolate("4");
        Assert.assertEquals(4, checkChocolateInt);
        
        int checkMilkInt = i.checkMilk("8");
        Assert.assertEquals(8, checkMilkInt);
        
        int checkCoffeeInt = i.checkCoffee("12");
        Assert.assertEquals(12, checkCoffeeInt);
        
        int checkSugarInt = i.checkSugar("20");
        Assert.assertEquals(20, checkSugarInt);
        
        
        // check invalid values for the ingredients
        // invalid chocolate
        try {
        	i.checkChocolate("-2"); 
        } catch (final IllegalArgumentException iae) {
        	Assert.assertEquals(490, (int)i.getChocolate());
        }
        
        try {
        	i.checkChocolate("-two"); 
        } catch (final IllegalArgumentException iae) {
        	Assert.assertEquals(490, (int)i.getChocolate());
        }
        
        //invalid milk

        try {
        	i.checkMilk("-2"); 
        } catch (final IllegalArgumentException iae) {
        	Assert.assertEquals(480, (int)i.getMilk());
        }
        
        try {
        	i.checkMilk("-two"); 
        } catch (final IllegalArgumentException iae) {
        	Assert.assertEquals(480, (int)i.getMilk());
        }
        
        //invalid sugar

        try {
        	i.checkSugar("-2"); 
        } catch (final IllegalArgumentException iae) {
        	Assert.assertEquals(495, (int)i.getSugar());
        }
        
        try {
        	i.checkSugar("-two"); 
        } catch (final IllegalArgumentException iae) {
        	Assert.assertEquals(495, (int)i.getSugar());
        }
        
        //invalid coffee

        try {
        	i.checkCoffee("-2"); 
        } catch (final IllegalArgumentException iae) {
        	Assert.assertEquals(499, (int)i.getCoffee());
        }
        
        try {
        	i.checkCoffee("-two"); 
        } catch (final IllegalArgumentException iae) {
        	Assert.assertEquals(499, (int)i.getCoffee());
        } 
    }
    
    @Test
    @Transactional
    public void testCheckIsEnoughIngredients () {
        final Inventory ivt = new Inventory(0, 0, 0, 0);

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.setChocolate( 10 );
        recipe.setMilk( 20 );
        recipe.setSugar( 5 );
        recipe.setCoffee( 1 );

        recipe.setPrice( 5 );
        
        /*
         * makes sure that there is NOT enough ingredients for the recipe
         */
        assertFalse(ivt.enoughIngredients(recipe));

        
        final Inventory i = new Inventory(500, 500, 500, 500);
        /*
         * now there IS enough ingredients for the recipe
         */
        assertTrue(i.enoughIngredients(recipe));
        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */
        Assert.assertEquals( 490, (int) i.getChocolate() );
        Assert.assertEquals( 480, (int) i.getMilk() );
        Assert.assertEquals( 495, (int) i.getSugar() );
        Assert.assertEquals( 499, (int) i.getCoffee() );
      
    }
    
    @Test
    @Transactional
    public void testToString () {
        final Inventory i = new Inventory(500, 500, 500, 500);

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.setChocolate( 10 );
        recipe.setMilk( 20 );
        recipe.setSugar( 5 );
        recipe.setCoffee( 1 );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );
        
	    /* 
	     * Make sure that the string is printed appropriately 
	     * */
	    Assert.assertEquals("Coffee: 499\n"
	    		+ "Milk: 480\n"
	    		+ "Sugar: 495\n"
	    		+ "Chocolate: 490\n", i.toString());
	    
	    Assert.assertTrue(i.enoughIngredients(recipe));
    }

}
