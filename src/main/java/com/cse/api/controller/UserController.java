package com.cse.api.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import com.cse.api.constants.Constants;

import com.cse.api.exception.ResourceNotFoundException;

import com.cse.api.repository.UserRepository;
import com.cse.api.model.User;

import com.cse.api.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/api/v1/User")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	UserService userService;

	// get all Users
	@GetMapping("/")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	User user;

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody Map<String, Object> UserMap)
			throws javax.security.auth.message.AuthException {
		String email = (String) UserMap.get("email");
		String password = (String) UserMap.get("password");
		user = userService.validateUser(email, password);
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("User", user);
		// System.out.print(user);
		data.putAll(generateJWTToken(user));
		// String tok = token;

		userRepository.setStatusForUser(1, user.getEmail());
		// userService.UpdateUser(user);
		// System.out.println("my data are :" + data);
		// System.out.println("my token is :" + tok);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@PostMapping("/logout")

	public String logout(HttpServletRequest request)
			throws javax.security.auth.message.AuthException {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("User", user);
		// System.out.print(user);
		// data.putAll(generateJWTToken(user));
		String useremail = request.getAttribute("email").toString();
		Integer stat = userRepository.findstatusbyemail(useremail);
		if (stat == 1) {

			// String tok = token;
			// logsService.logout(user);
			User userr = userRepository.findByEmailAddress(useremail);
			userRepository.setStatusForUser(0, userr.getEmail());
			// userService.UpdateUser(user);
			// System.out.println("my data are :" + data);
			// System.out.println("my token is :" + tok);
			data.put("User", userr);
			// System.out.print(user);
			data.putAll(generateJWTToken(userr));
			return "Hello  " + userr.getFirstName() + " You Logged out successfully";
		} else {
			return "No user logged in";
		}
		// new ResponseEntity<>(data, HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<Object> signup(@RequestBody User User) throws javax.security.auth.message.AuthException {
		User UserCreated = userService.registerUser(User);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("User", UserCreated);

		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	public String token;

	private Map<String, String> generateJWTToken(User user) {
		long timestamp = System.currentTimeMillis();
		// System.out.print("current user role:" + user.getRole());
		token = Jwts.builder().signWith(SignatureAlgorithm.HS256,
				Constants.API_SECRET_KEY)
				.setIssuedAt(new Date(timestamp))
				.setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
				.claim("userId", user.getId())
				.claim("email", user.getEmail())
				.claim("firstName", user.getFirstName())
				.claim("lastName", user.getLastName())
				.claim("role", user.getRole())
				// .claim("status", user.getstatus())
				.compact();
		Map<String, String> map = new HashMap<>();
		map.put("token", token);
		return map;
	}

	@PutMapping("/setrole/{email}")
	public ResponseEntity<User> updateuser(HttpServletRequest request,
			@PathVariable(value = "email") String email,
			@Valid @RequestBody User userDetails) throws AuthException {

		String role = request.getAttribute("role").toString();
		System.out.println("role: --------  " + role);
		int i = Integer.parseInt(role);
		if (i == 4) {

			User user = userRepository.findByEmailAddress(email);
			if (user == null) {
				throw new ResourceNotFoundException("User not found :: " + email);
			}

			user.setFirstName(userDetails.getFirstName() != null ? userDetails.getFirstName() : user.getFirstName());
			user.setLastName(userDetails.getLastName() != null ? userDetails.getLastName() : user.getLastName());
			user.setEmail(userDetails.getEmail() != null ? userDetails.getEmail() : user.getEmail());
			user.setPassword(userDetails.getPassword() != null ? userDetails.getPassword() : user.getPassword());
			user.setRole(userDetails.getRole());
			user.setstatus(userDetails.getstatus() != null ? userDetails.getstatus() : user.getstatus());
			final User updatecseser = userRepository.save(user);

			return ResponseEntity.ok(updatecseser);

		} else {

			throw new AuthException("Only system administrator can set user role :: ");
		}

	}
}
