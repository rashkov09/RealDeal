package softuni.exam.models.dto.XmlDto;

import org.springframework.data.annotation.AccessType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "offers")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedRootDto {

    @XmlElement(name = "offer")
    List<OfferSeedDto> offers;

    public List<OfferSeedDto> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferSeedDto> offers) {
        this.offers = offers;
    }
}
