package cz.buben.sre.repository;

import cz.buben.sre.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
