package edu.ncsu.csc.CoffeeMaker.data_gen;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
/**
 * Tests GenerateIngredients class and its functionality
 * @author csc326
 *
 */
@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateIngredients {
	/** IngredientService for GenerateIngredients*/
    @Autowired
    private IngredientService ingredientService;

    /**
     * Tests to make sure ingredients were created successfully
     */
    @Test
    @Transactional
    public void testCreateIngredients () {
        ingredientService.deleteAll();

        final Ingredient i1 = new Ingredient( "Coffee", 5 );

        ingredientService.save( i1 );

        final Ingredient i2 = new Ingredient( "Milk", 3 );

        ingredientService.save( i2 );

        Assert.assertEquals( 2, ingredientService.count() );

    }
}