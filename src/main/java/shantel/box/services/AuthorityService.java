package shantel.box.services;

import java.util.List;

import shantel.box.model.Authority;


public interface AuthorityService {
	
	List<Authority> findById(Integer id);
	
	List<Authority> findByName(String name);
	
}
