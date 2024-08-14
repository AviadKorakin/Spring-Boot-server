package restR.crud;

import org.springframework.data.jpa.repository.JpaRepository;

import restR.entities.UserEntity;

public interface UserCrud extends JpaRepository<UserEntity, String>{

}
