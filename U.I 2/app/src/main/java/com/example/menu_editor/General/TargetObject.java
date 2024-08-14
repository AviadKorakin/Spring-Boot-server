package com.example.menu_editor.General;

public class TargetObject {
	private ObjectId objectId;

	public TargetObject() {
	}

	public TargetObject(ObjectId objectId) {
		this.objectId = objectId;
	}

	public ObjectId getObjectId() {
		return objectId;
	}

	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}
	
	 @Override
	  public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    TargetObject that = (TargetObject) o;
	    return objectId.equals(that.objectId);
	  }

}
