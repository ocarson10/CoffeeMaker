package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.models.enums.IngredientType;

@Entity
public class Ingredient extends DomainObject {

    @Id
    @GeneratedValue
    private Long           id;

    @Enumerated ( EnumType.STRING )
    private IngredientType ingredient;

    private Integer        amount;

    public Ingredient ( final IngredientType ingredient, final Integer amount ) {
        super();
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public Ingredient () {
        super();
    }

    public IngredientType getIngredient () {
        return ingredient;
    }

    public void setIngredient ( final IngredientType ingredient ) {
        this.ingredient = ingredient;
    }

    public Integer getAmount () {
        return amount;
    }

    public void setAmount ( final Integer amount ) {
        this.amount = amount;
    }

    public void setId ( final Long id ) {
        this.id = id;
    }

    @Override
    public Long getId () {
        return id;
    }

    @Override
    public String toString () {
        return "Ingredient [id=" + id + ", ingredient=" + ingredient + ", amount=" + amount + "]";
    }

}
