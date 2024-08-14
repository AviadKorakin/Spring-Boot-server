package restR.boundaries;

import io.swagger.v3.oas.annotations.media.Schema;
import restR.entities.RoleEnum;

public class NewUserBoundary {
	@Schema(example = "example@example.example")
	private String email;
	private RoleEnum role;
	private String username;
	private String avatar;

	public NewUserBoundary() {

	}

	public NewUserBoundary(String email, RoleEnum role, String username, String avatar) {
		this.email = email;
		this.role = role;
		this.username = username;
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		NewUserBoundary that = (NewUserBoundary) o;
		return email.equals(that.email) && role == that.role && username.equals(that.username)
				&& avatar.equals(that.avatar);
	}

	@Override
	public String toString() {
		return "NewUserBoundary [email=" + email + ", role=" + role + ", username=" + username + ", avatar=" + avatar
				+ "]";
	}

}
