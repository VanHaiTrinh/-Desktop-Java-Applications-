package libarary;

public class Book {
	
	private int id;
	private String nameBook;
	private String nameAuthor;
	private String namePublic;
	private String type;
	
	public Book(int id, String nameBook, String nameAuthor, String namePublic, String type) {
		super();
		this.id = id;
		this.nameBook = nameBook;
		this.nameAuthor = nameAuthor;
		this.namePublic = namePublic;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNameBook() {
		return nameBook;
	}

	public void setNameBook(String nameBook) {
		this.nameBook = nameBook;
	}

	public String getNameAuthor() {
		return nameAuthor;
	}

	public void setNameAuthor(String nameAuthor) {
		this.nameAuthor = nameAuthor;
	}

	public String getNamePublic() {
		return namePublic;
	}

	public void setNamePublic(String namePublic) {
		this.namePublic = namePublic;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
