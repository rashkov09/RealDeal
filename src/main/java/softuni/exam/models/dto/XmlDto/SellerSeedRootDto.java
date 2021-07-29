package softuni.exam.models.dto.XmlDto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name= "sellers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SellerSeedRootDto {

    @XmlElement(name = "seller")
    List<SellerSeedDto> sellers;

    public SellerSeedRootDto() {
        List<SellerSeedDto> sellers = new ArrayList<>();
    }

    public List<SellerSeedDto> getSellers() {
        return sellers;
    }

    public void setSellers(List<SellerSeedDto> sellers) {
        this.sellers = sellers;
    }
}
