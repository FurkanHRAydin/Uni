/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package beans;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author fugy
 */
@Named(value = "bmiBean")
@RequestScoped
public class BmiBean {
    
    private String weight;
    private String height;
    /**
     * Creates a new instance of BmiBean
     */
    public BmiBean() {
    }
    
    public BmiBean(String weight, String height){
        this.weight = weight;
        this.height = height;
    }
    
    
    public String getWeight() {
        return this.weight;
    }
    
    public String getHeight() {
        return this.height;
    }
    
    
    public void setWeight(String weight) {
        this.weight=weight;
    }
    
    public void setHeight(String height) {
        this.height = height;
    }
    
    public String berechne(String weight, String height) {
            String result = "";
        try{
            float max = 0;
            float weightValue = Float.parseFloat(weight);
            float heightValue = Float.parseFloat(height);
            float BMI = weightValue / (heightValue * heightValue);
            
            String weightClass = "Normalgewicht";
            if(BMI < 19.0) {
                weightClass = "Untergewicht";
            }
            if(BMI > 25.0) {
                weightClass = "Übergewicht";
            }
            result += "Dein BMI ist: " + Math.round(BMI)
                    + " !!!! du hast " 
                    + weightClass 
                    + ".";
            
            
            if (BMI > max) {
                max = BMI;
            }
            
            result += "\n" + "Der bisher größte gemessene BMI-Wert war: " + Math.round(max);
            
        } catch (NumberFormatException e) {
            result = "Nur numerische Werte eingeben!";
        } catch (IllegalArgumentException e) {
            result = "Ungültige Werte!";
        }
        return result;
    }
}

