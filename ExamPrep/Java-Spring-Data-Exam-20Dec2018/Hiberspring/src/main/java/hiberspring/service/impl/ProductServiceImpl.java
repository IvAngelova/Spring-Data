package hiberspring.service.impl;

import hiberspring.domain.dtos.ProductSeedRootDto;
import hiberspring.domain.entities.Product;
import hiberspring.repository.ProductRepository;
import hiberspring.service.BranchService;
import hiberspring.service.ProductService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

import static hiberspring.common.Constants.*;

@Service
public class ProductServiceImpl implements ProductService {
    private static final String PRODUCTS_FILE_NAME = "products.xml";
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final BranchService branchService;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, FileUtil fileUtil, BranchService branchService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.branchService = branchService;
    }

    @Override
    public Boolean productsAreImported() {
        return productRepository.count() > 0;
    }

    @Override
    public String readProductsXmlFile() throws IOException {
        return fileUtil.readFile(PATH_TO_FILES + PRODUCTS_FILE_NAME);
    }

    @Override
    public String importProducts() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        ProductSeedRootDto productSeedRootDto = xmlParser
                .parseXml(ProductSeedRootDto.class, PATH_TO_FILES + PRODUCTS_FILE_NAME);
        productSeedRootDto.getProducts()
                .stream()
                .filter(productSeedDto -> {
                    boolean isValid = validationUtil.isValid(productSeedDto);
                    sb.append(isValid
                                    ? String.format(SUCCESSFUL_IMPORT_MESSAGE, "Product", productSeedDto.getName())
                                    : INCORRECT_DATA_MESSAGE)
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(productSeedDto -> {
                    Product product = modelMapper.map(productSeedDto, Product.class);
                    product.setBranch(branchService.findBranchByName(productSeedDto.getBranch()));
                    return product;
                })
                .forEach(productRepository::save);

        return sb.toString();
    }
}
