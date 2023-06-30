package com.example.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.restfulwebservices.jpa.PostRepository;
import com.example.restfulwebservices.jpa.UserRepository;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {

	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PostRepository postRepository;

	@GetMapping("/jpa/users")
	public List<User> getAllUsers(){
		return repository.findAll();
		
	}
	
	@GetMapping("/jpa/users/{id}")
	public EntityModel<User> getUserById(@PathVariable int id){
		Optional<User> user = repository.findById(id);
		
		if(user.isEmpty())
			throw new UserNotFoundException("id:"+id);
		
		EntityModel<User> entityModel = EntityModel.of(user.get());
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).getAllUsers());
		entityModel.add(link.withRel("all-users"));
		return entityModel;
	}
	
	@PostMapping("/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User savedUser = repository.save(user);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location ).build();
	}
	
	@DeleteMapping("/jpa/users/{id}")
	public void deleteUserById(@PathVariable int id){
		repository.deleteById(id);
		
	}
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrievePostsForUser(@PathVariable int id){
		
		Optional<User> user = repository.findById(id);
		if(user.isEmpty())
			throw new UserNotFoundException("id:"+id);
		
		return user.get().getPosts();
		
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Object> createPostsForUser(@PathVariable int id, @Valid @RequestBody Post post){
		
		Optional<User> user = repository.findById(id);
		if(user.isEmpty())
			throw new UserNotFoundException("id:"+id);
		
		post.setUser(user.get());
		Post savedPost = postRepository.save(post);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedPost.getId())
				.toUri();
		return ResponseEntity.created(location ).build();
		
	}
	
	//http://localhost:8080/jpa/users/10001/posts/1
	
	@GetMapping("/jpa/users/{uId}/posts/{pId}")
	public ResponseEntity<Post> getUserPostById(@PathVariable int uId, @PathVariable int pId){
		Optional<User> user = repository.findById(uId);
		
		if(user.isEmpty())
			throw new UserNotFoundException("id:"+uId);
		
		
		User returnedUser = user.get();
		System.out.print(returnedUser.getName());
        Optional<Post> postOptional = returnedUser.getPosts().stream()
                .filter(post -> post.getId().equals(pId))
                .findFirst();
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            return ResponseEntity.ok(post);
        } else {
        	return ResponseEntity.notFound().build();
        }
	}
}
