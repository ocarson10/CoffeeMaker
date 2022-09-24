package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

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
    @Transactional
    public void testNoIngredients () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );
        final Ingredient i1 = new Ingredient("Cream", -200);
//        i1.setAmount( -200 );
//        i1.setIngredient( "Cream" );

//        final Ingredient i2 = new Ingredient("Sugar", 350);
//        i2.setAmount( 350 );
//        i2.setIngredient( "Sugar" );

//        final List<Ingredient> ingredients = List.of( i1, i2 );

        try { 
//              service.saveAll( ingredients );
        	service.save(i1);
              
//              System.out.println("Ingredients saved are: " + service.);
              Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved" );
        }
        catch ( final Exception e ) {
//            Assertions.assertTrue( e instanceof ConstraintViolationException );
            Assertions.assertTrue( e instanceof IllegalArgumentException );
            assertEquals("Units of ingredient must be a positive integer", e.getMessage());
        }
        
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );

    }

}
