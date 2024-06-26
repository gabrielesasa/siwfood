
package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.UserRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;

@Controller
public class AuthenticationController {
	@Autowired 
	private CredentialsRepository credentialsRepository;
	@Autowired 
	private CredentialsService credentialsService;
	@Autowired 
	private UserRepository userRepository;
	@Autowired 
	private UserService userService;
	
	@GetMapping("/generico/formRegistrazione")
	public String getRegistrazione(Model model) {
		model.addAttribute("user",new User());
		model.addAttribute("credentials",new Credentials());
		return "/generico/formRegistrazione.html";
		}
	@PostMapping("/generico/registrazione")
    public String registerUser(@ModelAttribute("user") User user,
                 @ModelAttribute("credentials") Credentials credentials,
                 Model model) {

        // se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB 
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            userService.saveUser(user);
            model.addAttribute("user", user);
            return "/generico/index.html";
    }
	@GetMapping(value = "/login") 
	public String showLoginForm (Model model) {
		return "/generico/formLogin";
	}
	@GetMapping("/success")
    public String defaultAfterLogin(Model model) {
        
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    	if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "cuoco/indexCuoco.html";
        }
        return "/generico/index.html";
    }
	@GetMapping(value = "/") 
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
	        return "/generico/index.html";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				return "cuoco/indexCuoco.html";
			}
		}
        return "/generico/index.html";
	}
}

