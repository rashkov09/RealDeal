package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class CarSeedDto {

    /*
                "make": "BMW",
                "model": "750",
                "kilometers": 166235,
                "registeredOn": "04/04/2016"

     */
    @Expose
    private String make;

    @Expose
    private String model;

    @Expose
    private Long kilometers;

    @Expose
    private String registeredOn;


    public CarSeedDto() {
    }

    @Size(min = 2,max = 19)
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    @Size(min = 2,max = 19)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


    @Positive
    public Long getKilometers() {
        return kilometers;
    }

    public void setKilometers(Long kilometers) {
        this.kilometers = kilometers;
    }


    @DateTimeFormat(pattern = "dd/mm/yyyy")
    public String getRegisteredOn() {
     return     registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }
}
