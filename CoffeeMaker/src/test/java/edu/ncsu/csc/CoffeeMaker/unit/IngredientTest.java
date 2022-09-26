package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class IngredientTest {

    @Autowired
    private IngredientService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();

    }

    @Test
    @Transactional
    public void testCreateIngredient () {
        final Ingredient i1 = new Ingredient( "Matcha", 100 );
        service.save( i1 );

        assertEquals( 1, service.count() );
        Assertions.assertEquals( 100, i1.getAmount() );
        Assertions.assertEquals( "Matcha", i1.getName() );
        final Long id1 = i1.getId();
        Assertions.assertEquals( i1.getName(), service.findById( id1 ).getName() );

    }

    @Test
    // @Transactional
    public void testInvalidIngredients () {
 
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );
        Ingredient i1 = null;
        try {

            i1 = new Ingredient( "Cream", -200 );

        }
        catch ( final Exception e ) {
            // Assertions.assertTrue( e instanceof ConstraintViolationException
            // );
            Assertions.assertTrue( e instanceof IllegalArgumentException );
            assertEquals( "Units of ingredient must be a positive integer", e.getMessage() );
        }

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );
        assertNull( i1 );
    }

}
