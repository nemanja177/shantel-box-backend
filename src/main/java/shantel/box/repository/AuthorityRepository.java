package shantel.box.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shantel.box.model.Authority;


@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
	Authority findByName(String name);
}
