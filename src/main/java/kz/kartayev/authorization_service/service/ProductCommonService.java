package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.commons.adapter.ProductAdapter;
import kz.kartayev.authorization_service.commons.dto.PriceDto;
import kz.kartayev.authorization_service.commons.dto.ProductBuyDto;
import kz.kartayev.authorization_service.commons.dto.ProductCreateDto;
import kz.kartayev.authorization_service.commons.dto.ProductDto;
import kz.kartayev.authorization_service.commons.dto.ProductSearchDto;
import kz.kartayev.authorization_service.commons.enums.TransactionStatusHistory;
import kz.kartayev.authorization_service.commons.enums.TransactionType;
import kz.kartayev.authorization_service.entity.Category;
import kz.kartayev.authorization_service.entity.Order;
import kz.kartayev.authorization_service.entity.Price;
import kz.kartayev.authorization_service.entity.Product;
import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserCustomer;
import kz.kartayev.authorization_service.entity.UserTransactionHistory;
import kz.kartayev.authorization_service.repository.CategoryRepository;
import kz.kartayev.authorization_service.repository.PriceRepository;
import kz.kartayev.authorization_service.repository.ProductRepository;
import kz.kartayev.authorization_service.util.error.FLCException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.service.OAuth;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductCommonService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final SpecificationService specificationService;
  private final PriceRepository priceRepository;
  private final CustomerService customerService;
  private final UserTransactionHistoryService userTransactionHistoryService;
  private final CardService cardService;
  private final UserService userService;
  private final EmailService emailService;

  private final OrderService orderService;

  public Page<ProductDto> findAll(Pageable pageable) {
    Page<Product> products = productRepository.findAll(pageable);
    return products.map(ProductAdapter::toDto);
  }

  @Transactional
  public Page<ProductDto> findAll(ProductSearchDto productSearchDto, Pageable pageable) {
    Page<Product> products = productRepository.findAll(
            specificationService.productSpecification(productSearchDto), pageable);

    return products.map(ProductAdapter::toDto);
  }

  public List<ProductDto> findAllByCustomer(UserCustomer userCustomer) {
    return productRepository.findAllByUserCustomer(userCustomer).stream()
            .map(ProductAdapter::toDto).collect(Collectors.toList());
  }

  @Transactional
  public ProductDto save(ProductCreateDto productDto) {
    Product product = new Product();
    Price price = new Price();
    validateAttributes(productDto);

    Optional<Category> category = categoryRepository.
            findCategoryByCategoryName(productDto.getCategoryDto().getCategoryName());

    category.ifPresent(product::setCategory);

    price.setPrice(productDto.getPriceDto().getPrice());
    price.setIsSale(false);
    price.setUpdatedAt(LocalDateTime.now());

    product.setProductName(productDto.getProductName());
    product.setDescription(productDto.getDescription());
    product.setQuantity(productDto.getQuantity());
    product.setCreatedDate(LocalDateTime.now());
    product.setPrice(price);
    Optional<UserCustomer> optionalUserCustomer = customerService.
            findCustomerByName(productDto.getCustomerDto().getCustomerName());
    if (optionalUserCustomer.isPresent()) {
      product.setUserCustomer(optionalUserCustomer.get());
    }
    else {
      throw new FLCException("validation.customer", "Продавец не найден!","");
    }

    productRepository.save(product);
    priceRepository.save(price);

    return ProductAdapter.toDto(product);
  }

  @Transactional
  public ProductDto update(Long id, ProductCreateDto productCreateDto) {
    Optional<Product> productOptional = productRepository.findById(id);
    if (productOptional.isPresent()) {
      Product product = productOptional.get();
      if (productCreateDto.getQuantity() != null) {
        product.setQuantity(productCreateDto.getQuantity());
      }
      if (productCreateDto.getProductName() != null) {
        product.setProductName(productCreateDto.getProductName());
      }
      if (productCreateDto.getPriceDto() != null) {
        Price price = product.getPrice();
        PriceDto priceDto = productCreateDto.getPriceDto();
        if (priceDto.getPrice() != null) {
          price.setPrice(priceDto.getPrice());
        }
        if (priceDto.getIsSale() != null) {
          price.setIsSale(priceDto.getIsSale());
        }
        priceRepository.save(price);
      }
      if (productCreateDto.getDescription() != null) {
        product.setDescription(productCreateDto.getDescription());
      }
      productRepository.save(product);
      return ProductAdapter.toDto(product);
    }
    return null;
  }

  public Optional<Product> findById(Long id) {
    return productRepository.findById(id);
  }

  @Transactional
  public void delete(Long id) {
    productRepository.deleteById(id);
  }

  public List<ProductDto> findMyProducts() {
    User user = userService.getCurrentUserEntity();
    Optional<UserCustomer> userCustomerOptional = customerService.findCustomerByUser(user);
    if (userCustomerOptional.isPresent()) {
      return findAllByCustomer(userCustomerOptional.get());
    }
    return Collections.emptyList();
  }

  @Transactional
  public void buy(Long id, ProductBuyDto productBuyDto) {
    Optional<Product> productOptional = productRepository.findById(id);
    User user = userService.getCurrentUserEntity();
    if (productOptional.isPresent()) {
      Product product = productOptional.get();
      if (productBuyDto.getQuantity() > product.getQuantity()) {
        throw new FLCException("P1", "Ошибка при покупке. Выберите корректное число товаров", "");
      }
      product.setQuantity(product.getQuantity() - productBuyDto.getQuantity());

      // TODO : Нужно чтобы была очередь в customer и сustomer аппрувнул
      UserTransactionHistory userTransactionHistory = userTransactionHistoryService.save(
              Double.valueOf(product.getPrice().getPrice()) * productBuyDto.getQuantity(),
              TransactionType.WITHDRAW, user, cardService.findById(productBuyDto.getCardId()).get());
      Order order = orderService.save(productBuyDto, user, product, userTransactionHistory);
      if (userTransactionHistory.getTransactionStatus().equals(TransactionStatusHistory.SUCCESS)) {
        productRepository.save(product);
      }
      else {
        throw new FLCException("P3", "Пополните баланс для покупки этого товара","");
      }
      String text = "Здравствуйте! Вы приобрели товар в MyMarket! Название товара: " +
              product.getProductName() + "\nПродавец : " + product.getUserCustomer().getName() +"\n"
              + "Количествo: " + productBuyDto.getQuantity();
      String subject = "Транзакция №" + userTransactionHistory.getId();
      String email = user.getEmail();
      emailService.sendMessage(email, subject, text);
      sendMessageForSeller(product.getUserCustomer(), order.getId(), product);
    }
  }

  private void sendMessageForSeller(UserCustomer userCustomer, Long orderId, Product product) {
    String email = userCustomer.getUser().getEmail();
    String subject = "Поступил заказ №" + orderId;
    String text = "Вам поступил заказ на покупку продукта " +  product.getProductName() + "Необходимо доставить товар в "
            + "течении 7 дней. Если вы не сможете доставить, проcим отменить доставку! \n"
            + "C Уважением, Служба Поддержки MyMarket";
    emailService.sendMessage(email, subject, text);
  }


  private void validateAttributes(ProductCreateDto productDto) {
    if (productDto.getPriceDto().getPrice() <= 0) {
      throw new FLCException("Цена не может быть негативным значением или нулем");
    }
    if (productDto.getQuantity() > 0) {
      throw new FLCException("Количество товара должно быть больше чем 0");
    }
  }
}
