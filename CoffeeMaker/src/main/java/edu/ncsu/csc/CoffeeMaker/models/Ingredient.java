package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.Entity;

import edu.ncsu.csc.CoffeeMaker.models.enums.IngredientType;

@Entity
public class Ingredient extends DomainObject {

    private Long           id;
    private IngredientType ingredient;
    private Integer        amount;

    @Override
    public Serializable getId () {
        // TODO Auto-generated method stub
        return null;
    }

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
    public String toString () {
        return "Ingredient [id=" + id + ", ingredient=" + ingredient + ", amount=" + amount + "]";
    }

}
