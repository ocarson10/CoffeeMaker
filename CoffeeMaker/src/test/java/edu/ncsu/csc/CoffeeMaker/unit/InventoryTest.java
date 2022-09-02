package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertFalse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @Before
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.setChocolate( 500 );
        ivt.setCoffee( 500 );
        ivt.setMilk( 500 );
        ivt.setSugar( 500 );

        inventoryService.save( ivt );
    }

    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

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
    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();

        ivt.addIngredients( 5, 3, 7, 2 );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values for coffee", 505,
                (int) ivt.getCoffee() );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values for milk", 503,
                (int) ivt.getMilk() );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values sugar", 507,
                (int) ivt.getSugar() );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values chocolate", 502,
                (int) ivt.getChocolate() );

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
