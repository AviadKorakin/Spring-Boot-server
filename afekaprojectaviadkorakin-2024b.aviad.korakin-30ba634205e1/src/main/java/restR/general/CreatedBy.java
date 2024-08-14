package restR.general;

public class CreatedBy {
	private UserId userId;

	public CreatedBy() {
	}

	public CreatedBy(UserId userId) {
		this.userId = userId;
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CreatedBy that = (CreatedBy) o;
		return userId.equals(that.userId);
	}
}
