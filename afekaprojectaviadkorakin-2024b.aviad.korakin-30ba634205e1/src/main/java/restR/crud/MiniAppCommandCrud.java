package restR.crud;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import restR.entities.MiniAppCommandEntity;

public interface MiniAppCommandCrud extends JpaRepository<MiniAppCommandEntity, String>{
	public List<MiniAppCommandEntity> findAllByMiniapp(
			@Param("miniapp") String miniapp,
			Pageable pageable); 

}



