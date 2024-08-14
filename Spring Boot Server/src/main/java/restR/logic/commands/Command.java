package restR.logic.commands;

import java.text.ParseException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import restR.boundaries.MiniAppCommandBoundary;

public interface Command {
	public List<Object> invoke(MiniAppCommandBoundary Command) throws JsonProcessingException, ParseException;
}