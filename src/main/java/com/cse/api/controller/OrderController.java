package com.cse.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.cse.api.exception.ResourceNotFoundException;
import com.cse.api.model.Order;
import com.cse.api.model.Email;
import com.cse.api.repository.OrderRepository;
import com.cse.api.service.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
@Autowired
private EmailSender mailSender;
  @Autowired
  private OrderRepository orderRepository;


  @PostMapping("/orders")
  public Order creatOrder(@Valid @RequestBody Order order) throws ResourceNotFoundException {
    return orderRepository.save(order);
  }

  @GetMapping("/orders")
  public List<Order> getAllOrders() {
    return orderRepository.findAll();
  }

  @GetMapping("/orders/{id}")
  public ResponseEntity<Order> getOrderById(@PathVariable(value = "id") Long orderId)
      throws ResourceNotFoundException {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("order not found :: " + orderId));
    return ResponseEntity.ok().body(order);
  }

  @DeleteMapping("/orders/{id}")
  public Map<String, Boolean> deleteOrder(@PathVariable(value = "id") Long orderId)
      throws ResourceNotFoundException {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("order not found :: " + orderId));
  orderRepository.delete(order);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
  }
@PostMapping("/order/sendemail")
public @Valid String SendEmail(HttpServletRequest request,@Valid @RequestBody Email email) {

		String password="fljbcpaivjvzvoxf";
		String tomail=email.getToEmail();
		String subject=email.getSubject();
		String body=email.getBody();
		body=body+"\n Requested by "+request.getAttribute("email").toString();
		System.out.println("User email "+request.getAttribute("email").toString());
		mailSender.SendSimpleEmail(tomail, body, subject);
		//email.SendSimpleEmail(tomail,subject,body);
		//mailSender.SendSimpleEmail("ngabonziza@gmail.com","Hello this is Email from Springboot backend by mr. claude", "Testing Mailsender");
		//mailSender.semdEmailWithAttachement(tomail, body, subject,"D:\\2nd_Year\\Semester I\\2nd_Year\\Semester I\\CCN A\\supcfg.pdf");


	return "Email is successfully sent";
	
}
//  @PutMapping("/orders/{id}")
//  public ResponseEntity<Order> updateOrder(@PathVariable(value = "id") Long orderId,
//      @Valid @RequestBody Order orderDetails) throws ResourceNotFoundException {
//    Order order = orderRepository.findById(orderId)
//        .orElseThrow(() -> new ResourceNotFoundException("Product not found :: " + orderId));
//    order.setPr(orderDetails.getProductName());
//    order.setquantity(orderDetails.getquantity());
//    order.setReceiverCompany(orderDetails.getReceiverCompany());
//    order.setSenderCompany(orderDetails.getSenderCompany());
//    final Order updatedOrder = orderRepository.save(order);
//    return ResponseEntity.ok(updatedOrder);
//  }

}
