package com.example.menu_editor.Boundaries;

import com.example.menu_editor.General.CommandId;
import com.example.menu_editor.General.InvokedBy;
import com.example.menu_editor.General.TargetObject;

import java.util.Date;
import java.util.Map;

public class MiniAppCommandBoundary {
    private CommandId commandId;
    private String command;
    private TargetObject targetObject;
    private Date invocationTimestamp;
    private InvokedBy invokedBy;
    private Map<String, Object> commandAttributes;

    public CommandId getCommandId() {
        return commandId;
    }

    public void setCommandId(CommandId commandId) {
        this.commandId = commandId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public TargetObject getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(TargetObject targetObject) {
        this.targetObject = targetObject;
    }

    public Date getInvocationTimestamp() {
        return invocationTimestamp;
    }

    public void setInvocationTimestamp(Date invocationTimestamp) {
        this.invocationTimestamp = invocationTimestamp;
    }

    public InvokedBy getInvokedBy() {
        return invokedBy;
    }

    public void setInvokedBy(InvokedBy invokedBy) {
        this.invokedBy = invokedBy;
    }

    public Map<String, Object> getCommandAttributes() {
        return commandAttributes;
    }

    public void setCommandAttributes(Map<String, Object> commandAttributes) {
        this.commandAttributes = commandAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MiniAppCommandBoundary that = (MiniAppCommandBoundary) o;
        return commandId.equals(that.commandId) && command.equals(that.command)
                && targetObject.equals(that.targetObject) && invocationTimestamp.equals(that.invocationTimestamp)
                && invokedBy.equals(that.invokedBy) && commandAttributes.entrySet().equals(that.commandAttributes.entrySet());
    }

}