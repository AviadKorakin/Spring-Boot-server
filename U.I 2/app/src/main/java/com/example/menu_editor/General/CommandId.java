package com.example.menu_editor.General;

public class CommandId {
	private String superapp;
	private String miniapp;
	private String id;
	private String delimiter = "/";

	public CommandId() {
	}

	/**
	 * This constructor creates a CommandId object with the provided SuperApp name,
	 * MiniApp name (can be null), and an identifier.
	 * 
	 * @param superapp The name of the SuperApp this command belongs to.
	 * @param miniapp  The name of the MiniApp within the SuperApp.
	 * @param id       The unique identifier for this command.
	 */
	public CommandId(String superapp, String miniapp, String id) {
		this.superapp = superapp;
		this.miniapp = miniapp;
		this.id = id;
	}

	/**
	 * This constructor creates a CommandId object by parsing a composed string
	 * containing information about the SuperApp, MiniApp , and identifier.
	 * 
	 * The expected format of the composed string is
	 * "SuperAppName[delimiter]MiniAppName[delimiter]CommandId".
	 * 
	 * @param composedId The string containing SuperApp name, MiniApp name, and
	 *                   command identifier separated by a delimiter.
	 */
	public CommandId(String composedId) {
		String[] composed = composedId.split(delimiter);
		this.superapp = composed[0];
		this.miniapp = composed[1];
		this.id = composed[2];

	}

	public String getSuperapp() {
		return superapp;
	}

	public void setSuperapp(String superapp) {
		this.superapp = superapp;
	}

	public String getMiniapp() {
		return miniapp;
	}

	public void setMiniapp(String miniapp) {
		this.miniapp = miniapp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CommandId that = (CommandId) o;
		return superapp.equals(that.superapp) && miniapp.equals(that.miniapp) && id.equals(that.id);
	}

	@Override
	public String toString() {
		return superapp + delimiter + miniapp + delimiter + id;
	}

}
