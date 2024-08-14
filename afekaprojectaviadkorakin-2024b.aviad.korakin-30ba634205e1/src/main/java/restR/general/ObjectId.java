package restR.general;

public class ObjectId {
	private String superapp;
	private String id;
	private String delimiter = "/";

	public ObjectId() {

	}

	/**
	 * This constructor creates an ObjectId object with the provided SuperApp name
	 * and identifier.
	 * 
	 * @param superapp The name of the SuperApp this object belongs.
	 * @param id       The unique identifier for this object.
	 */
	public ObjectId(String superapp, String id) {
		this.superapp = superapp;
		this.id = id;
	}

	/**
	 * This constructor creates an ObjectId object by parsing a composed string
	 * containing information about the SuperApp and identifier.
	 * 
	 * The expected format of the composed string is
	 * "SuperAppName[delimiter]ObjectId" where each element is separated by a
	 * delimiter).
	 * 
	 * @param composedId The string containing SuperApp name and identifier
	 *                   separated by a delimiter ).
	 */
	public ObjectId(String composedId) {
		String[] composed = composedId.split(delimiter);
		this.superapp = composed[0];
		this.id = composed[1];

	}

	public String getSuperapp() {
		return superapp;
	}

	public void setSuperapp(String superapp) {
		this.superapp = superapp;
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
		ObjectId that = (ObjectId) o;
		return superapp.equals(that.superapp) && id.equals(that.id);
	}

	@Override
	public String toString() {
		return superapp + delimiter + id;
	}

}
