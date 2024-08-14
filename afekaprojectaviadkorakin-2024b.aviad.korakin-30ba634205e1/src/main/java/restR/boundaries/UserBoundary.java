package restR.boundaries;

import restR.entities.*;
import restR.general.UserId;

public class UserBoundary {
	private UserId userId;
	private String avatar;
	private RoleEnum role;
	private String username;

	public UserBoundary() {
	}

	public UserBoundary(UserId userId, String avatar, RoleEnum role, String username) {
		this.userId = userId;
		this.avatar = avatar;
		this.role = role;
		this.username = username;
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public RoleEnum getRole() {
		return role;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserBoundary that = (UserBoundary) o;
		return userId.equals(that.userId) && avatar.equals(that.avatar) && role.equals(that.role)
				&& username.equals(that.username);
	}


	@Override
	public String toString() {
		return "UserBoundary [userId=" + userId + ", avatar=" + avatar + ", role=" + role + ", username=" + username
				+ "]";
	}

}
