package kirill.rest.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import kirill.rest.app.models.User;
import kirill.rest.app.services.AppService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;



@RestController
@RequestMapping("/api")
public class ProxyController {

	private RestTemplate restTemplate;
	
	private AppService appService;
	
	@Autowired
	public ProxyController(RestTemplate restTemplate, AppService appService) {
		this.restTemplate = restTemplate;
		this.appService = appService;
	}
	
	@Value("${spring.host.url}")
	private String BASE_URL;

	@PostMapping("/new-user")
	public User addUser(@RequestBody User user) {
		appService.addUser(user);
		return user;
	}
	
    @GetMapping("/posts/**")
    @Cacheable("postsCache")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_POSTS_ADMIN','ROLE_POSTS_VIEWER')")
    public ResponseEntity<String> getPosts() {
        String url = BASE_URL + "/posts";
        return proxyRequest(url, HttpMethod.GET, null);
    }

    @GetMapping("/users/**")
    @Cacheable("usersCache")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USERS')")
    public ResponseEntity<String> getUsers() {
        String url = BASE_URL + "/users";
        return proxyRequest(url, HttpMethod.GET, null);
    }

    @GetMapping("/albums/**")
    @Cacheable("albumsCache")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ALBUMS')")
    public ResponseEntity<String> getAlbums() {
        String url = BASE_URL + "/albums";
        return proxyRequest(url, HttpMethod.GET, null);
    }

    @PostMapping("/posts/**")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_POSTS_ADMIN','ROLE_POSTS_EDITOR')")
    public ResponseEntity<String> createPost(@RequestBody String postBody) {
        String url = BASE_URL + "/posts";
        return proxyRequest(url, HttpMethod.POST, postBody);
    }

    
    private ResponseEntity<String> proxyRequest(String url, HttpMethod method, String requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, method, requestEntity, String.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
