package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.XmlDto.OfferSeedRootDto;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.OfferService;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {
    private final static String OFFER_DATA_FILE_PATH = "src/main/resources/files/xml/offers.xml";
    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final CarService carService;
    private final SellerService sellerService;

    public OfferServiceImpl(OfferRepository offerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, CarService carService, SellerService sellerService) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.carService = carService;
        this.sellerService = sellerService;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFER_DATA_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        OfferSeedRootDto offerSeedRootDto = xmlParser.fromFile(OFFER_DATA_FILE_PATH,OfferSeedRootDto.class);
        StringBuilder builder = new StringBuilder();

        offerSeedRootDto.getOffers()
                .forEach(offerSeedDto -> {
                    boolean valid = validationUtil.isValid(offerSeedDto);
                    if (valid){
                        Offer offer = modelMapper.map(offerSeedDto,Offer.class);
                        offer.setCar(carService.getCarById(offerSeedDto.getCar().getId()));
                        offer.setSeller(sellerService.getSellerById(offerSeedDto.getSeller().getId()));
                        builder.append(String.format("Successfully import offer %s - %s",offer.getAddedOn(),offer.getHasGoldStatus()));
                        offerRepository.save(offer);

                    }else{
                        builder.append("Invalid offer");
                    }
                    builder.append(System.lineSeparator());
                });


        return builder.toString();
    }
}
