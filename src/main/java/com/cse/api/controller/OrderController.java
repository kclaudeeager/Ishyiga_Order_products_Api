package com.cse.api.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.cse.api.exception.ResourceNotFoundException;
import com.cse.api.model.Order;
import com.cse.api.model.User;
import com.cse.api.model.Email;
import com.cse.api.repository.OrderRepository;
import com.cse.api.repository.UserRepository;
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
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
@Autowired
private EmailSender mailSender;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private UserRepository userRepository;


  @PostMapping("/orders")
  public Order creatOrder(HttpServletRequest request,@Valid @RequestBody Order order) throws ResourceNotFoundException {
    User user=userRepository.findByEmailAddress(request.getAttribute("email").toString());
    String client=user.getCompany();
    order.setclient(client);
   
    User supplier=userRepository.findByCompany(order.getsuplier());
    Order newOrder=new Order();


    if(supplier==null){
      new ResourceNotFoundException("No such supplier found :: ");
      
    }
    else{
      newOrder=orderRepository.save(order);;
    }
    System.out.println("the   suplier: "+supplier);
    System.out.println("Client: "+client+" Set "+order.getclient());
    return newOrder;
  }

  @GetMapping("/orders")
  public List<Order> getAllOrders(HttpServletRequest request) {
    List<Order> orders=new ArrayList<Order>();
    String role=request.getAttribute("role").toString();
    int rol=Integer.parseInt(role);
    if(rol==4){
      orders= orderRepository.findAll();
    }
    else{
      User user=userRepository.findByEmailAddress(request.getAttribute("email").toString());
       String company=user.getCompany();
      if(company!=null){
        orders=orderRepository.findByClient(company);
      }
    }
    return orders;
  }

  @GetMapping("/orders/{number}")
  public List<Order> getOrderById(HttpServletRequest request,HttpServletResponse response,@PathVariable(value = "number") Integer orderNum)
      throws ResourceNotFoundException, IOException {
    List<Order> orders = new ArrayList<Order>();
    List<Order> myorders = new ArrayList<Order>();
  
        User user=userRepository.findByEmailAddress(request.getAttribute("email").toString());
        String company=user.getCompany();

        // ResponseEntity<Order> responseEntity=null;
        orders=orderRepository.findAllCreatedAtTime(orderNum);
        if(orders==null)
        new ResourceNotFoundException("order not found :: " + orderNum);
        for(int i=0;i<orders.size();i++)
        if(orders.get(i).getclient().equals(company)|| orders.get(i).getsuplier().equals(company)){
         myorders.add(orders.get(i));

          }
      
           // response.sendError(HttpStatus.FORBIDDEN.value(), "you are not authorized to view such order");
        
          
return myorders;
  }

  @DeleteMapping("/orders/{number}")
  public Map<String, Boolean> deleteOrder(HttpServletRequest request,HttpServletResponse httpresponse,@PathVariable(value = "number") Integer orderNum)
      throws ResourceNotFoundException, IOException {
        List<Order> orders = orderRepository.findAllCreatedAtTime(orderNum);
        if(orders==null)
           new ResourceNotFoundException("order not found :: " + orderNum);
            User user=userRepository.findByEmailAddress(request.getAttribute("email").toString());
            String company=user.getCompany();
        
        Map<String, Boolean> response = new HashMap<>();
        for(Order order:orders)
        if(order.getclient().equals(company)){
          orderRepository.delete(order);
          response.put("deleted", Boolean.TRUE);
} 
 
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
