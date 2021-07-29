package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.XmlDto.SellerSeedRootDto;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.SellerRepository;
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
public class SellerServiceImpl implements SellerService {
    private static final String SELLERS_DATA_FILE_PATH  = "src/main/resources/files/xml/sellers.xml";
    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public SellerServiceImpl(SellerRepository sellerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.sellerRepository = sellerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
          return  Files.readString(Path.of(SELLERS_DATA_FILE_PATH));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        SellerSeedRootDto sellerSeedRootDto =  xmlParser.fromFile(SELLERS_DATA_FILE_PATH,SellerSeedRootDto.class);
        StringBuilder builder = new StringBuilder();
     sellerSeedRootDto.getSellers()
             .forEach(sellerSeedViewDto -> {
                 boolean valid = validationUtil.isValid(sellerSeedViewDto);
                 if (valid){
                     Seller seller = modelMapper.map(sellerSeedViewDto,Seller.class);
                     if (seller.getRating() != null){
                         builder.append(String.format("Successfully import seller %s - %s",seller.getFirstName(),seller.getEmail()));
                         sellerRepository.save(seller);

                     } else{
                         builder.append("Invalid seller");
                     }
                 } else {
                     builder.append("Invalid seller");
                 }
                 builder.append(System.lineSeparator());
             });


        return builder.toString();
    }

    @Override
    public Seller getSellerById(Long id) {
        return sellerRepository.findSellerById(id);
    }
}
