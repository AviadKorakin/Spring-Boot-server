package restR.logic.commands;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import restR.boundaries.MiniAppCommandBoundary;
@Component
public class DefaultCommand implements Command {

	@Override
	@Transactional
	public List<Object> invoke(MiniAppCommandBoundary Command) {
		ArrayList<Object> rv=new ArrayList<>();
		rv.add(Command);
		return rv;
	}

}
