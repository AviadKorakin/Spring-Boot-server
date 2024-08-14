package restR.logic.converters;

import org.springframework.stereotype.Component;

import restR.boundaries.MiniAppCommandBoundary;
import restR.entities.MiniAppCommandEntity;
import restR.general.CommandId;
import restR.general.InvokedBy;
import restR.general.ObjectId;
import restR.general.TargetObject;
import restR.general.UserId;

@Component
public class MiniAppCommandConverter {

	public MiniAppCommandBoundary toBoundary(MiniAppCommandEntity entity) {
		MiniAppCommandBoundary rv = new MiniAppCommandBoundary();

		rv.setCommand(entity.getCommand());
		rv.setInvocationTimestamp(entity.getInvocationTimestamp());
		rv.setCommandId(new CommandId(entity.getCommandId()));
		rv.setInvokedBy(new InvokedBy(new UserId(entity.getInvokedBy())));
		rv.setTargetObject(new TargetObject(new ObjectId(entity.getTargetObject())));
		rv.setCommandAttributes(entity.getCommandAttributes());

		return rv;
	}

	public MiniAppCommandEntity toEntity(MiniAppCommandBoundary boundary) {
		MiniAppCommandEntity rv = new MiniAppCommandEntity();

		rv.setCommand(boundary.getCommand());
		rv.setCommandAttributes(boundary.getCommandAttributes());
		rv.setCommandId(boundary.getCommandId().toString());
		rv.setMiniapp(boundary.getCommandId().getMiniapp());
		rv.setInvocationTimestamp(boundary.getInvocationTimestamp());
		rv.setInvokedBy(boundary.getInvokedBy().getUserId().toString());
		if(boundary.getTargetObject().getObjectId()==null)
			rv.setTargetObject(null);
		else
		rv.setTargetObject(boundary.getTargetObject().getObjectId().toString());

		return rv;

	}
}
