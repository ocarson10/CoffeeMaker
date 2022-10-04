package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
/**
 * Tests the APICoffee class to ensure that all its methods are implemented correctly
 * @author csc326 staff
 *
 */
@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APICoffeeTest {
	/** mock model view controller for APICoffeeTest*/
    @Autowired
    private MockMvc           mvc;
    /** RecipeService for APICoffeeTest*/
    @Autowired
    private RecipeService     recipeService;
    /** InventoryService for APICoffeeTest*/
    @Autowired
    private InventoryService  iService;
    /** IngredientService for APICoffeeTest*/
    @Autowired
    private IngredientService ingredientService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {

        final Inventory ivt = iService.getInventory();

        ivt.addIngredient( new Ingredient( "Matcha", 5 ) );
        ivt.addIngredient( new Ingredient( "Milk", 5 ) );
        iService.save( ivt );
        Assert.assertEquals( ivt.getIngredients().size(), 2 );

        final Recipe recipe = new Recipe();
        recipe.setName( "Coffee" );
        final Ingredient i1 = new Ingredient( "Matcha", 2 );
        final Ingredient i2 = new Ingredient( "Milk", 4 );
        ingredientService.save( i1 );
        ingredientService.save( i2 );
        recipe.addIngredient( i1 );
        recipe.addIngredient( i2 );
        recipe.setPrice( 50 );
        recipeService.save( recipe );

    }

    /**
     * Tests if coffee was purchased successfully
     * @throws Exception if an error occurs
     */
    @Test
    @Transactional
    public void testPurchaseBeverage1 () throws Exception {

        final String name = "Coffee";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 60 ) ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.message" ).value( 10 ) );

    }

    /**
     * Tests if there's not enough money to purchase coffee
     * @throws Exception if an error occurs
     */
    @Test
    @Transactional
    public void testPurchaseBeverage2 () throws Exception {
        /* Insufficient amount paid */

        final String name = "Coffee";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 10 ) ) ).andExpect( status().is4xxClientError() )
                .andExpect( jsonPath( "$.message" ).value( "Not enough money paid" ) );

    }
    
    /**
     * Tests if there's not enough ingredients in the inventory
     * @throws Exception if an error occurs
     */
    @Test
    @Transactional
    public void testPurchaseBeverage3 () throws Exception {
        /* Insufficient inventory */

        final Inventory ivt = iService.getInventory();
        ivt.addIngredient( new Ingredient( "Matcha", 0 ) );
        ivt.addIngredient( new Ingredient( "Milk", 0 ) );
        iService.save( ivt );

        final String name = "Coffee";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 50 ) ) ).andExpect( status().is4xxClientError() )
                .andExpect( jsonPath( "$.message" ).value( "Not enough inventory" ) );

    }

}
