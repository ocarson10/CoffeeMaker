package edu.ncsu.csc.CoffeeMaker.unit;

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

}
