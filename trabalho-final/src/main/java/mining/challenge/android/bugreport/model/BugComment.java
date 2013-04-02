package mining.challenge.android.bugreport.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the bug_comment database table.
 * 
 */
@Entity
@Table(name="bug_comment")
public class BugComment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name="date_comment")
	private String dateComment;

	private String what;

	//bi-directional many-to-one association to Bug
	@ManyToOne
	private Bug bug;

	//bi-directional many-to-one association to Developer
	@ManyToOne
	private Developer author;

	public BugComment() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDateComment() {
		return this.dateComment;
	}

	public void setDateComment(String dateComment) {
		this.dateComment = dateComment;
	}

	public String getWhat() {
		return this.what;
	}

	public void setWhat(String what) {
		this.what = what;
	}

	public Bug getBug() {
		return this.bug;
	}

	public void setBug(Bug bug) {
		this.bug = bug;
	}

	public Developer getAuthor() {
		return this.author;
	}

	public void setAuthor(Developer author) {
		this.author = author;
	}

}