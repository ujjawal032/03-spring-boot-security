package com.uj.restcontroller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uj.entity.Product;
import com.uj.entity.Users;
import com.uj.repo.UsersRepo;
import com.uj.request.AuthRequest;
import com.uj.util.JWTUtil;

@RestController
@RequestMapping("/product")
public class ProductsController {
	
	private List<Product> productList = Arrays.asList(new Product(1,"pen"),
            new Product(2,"book"),
            new Product(3,"bag"),
            new Product(4,"eraser"),
            new Product(5,"pencil") );

	@Autowired
	private UsersRepo repo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/welcome")
	public ResponseEntity<String> welcome(){
		return new ResponseEntity<>("==========Welcome to Products App==========",HttpStatus.OK);
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<Product>> getAllProduct(){
		
		return new ResponseEntity<>(productList,HttpStatus.OK);
	}
	
	@GetMapping("/one/{id}")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<Product> getSingleProduct(@PathVariable("id") Integer id){
		List<Product> collect = productList.stream().filter(p -> p.getPid() == id).collect(Collectors.toList());
		return new ResponseEntity<>(collect.get(0),HttpStatus.OK);
	}
	
	@PostMapping("/save")
	public String saveUser(@RequestBody Users user) {
		user.setPassword(encoder.encode(user.getPassword()));
		Users save = repo.save(user);
		if(save != null)
			return "User saved";
		else
			return "User not saved";
	}
	
	@PostMapping("/generate")
	public ResponseEntity<String> generateToken(@RequestBody AuthRequest req){
		Authentication authenticate = authenticationManager.
		                                             authenticate(new UsernamePasswordAuthenticationToken
		                                             (req.getUserName(), req.getPassword()));
		
		if(authenticate.isAuthenticated()) {
			return new ResponseEntity<>(jwtUtil.generateToken(req.getUserName()),HttpStatus.OK);
		}else {
			 throw new UsernameNotFoundException("invalid user request !");
		}
		
	}

}
