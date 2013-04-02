package mining.challenge.android.bugreport.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the bug database table.
 * 
 */
@Entity
public class Bug implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	@Column(name="closed_on")
	private Timestamp closedOn;

	private String component;

	private String description;

	@Column(name="opened_date")
	private Timestamp openedDate;

	private String priority;

	private BigDecimal stars;

	private String status;

	private String title;

	@Column(name="type_bug")
	private String typeBug;

	//bi-directional many-to-one association to Developer
	@ManyToOne
	private Developer owner;

	//bi-directional many-to-one association to Developer
	@ManyToOne
	private Developer reportedBy;

	//bi-directional many-to-one association to BugComment
	@OneToMany(mappedBy="bug")
	@OrderBy("dateComment")
	private List<BugComment> comments;

	public Bug() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getClosedOn() {
		return this.closedOn;
	}

	public void setClosedOn(Timestamp closedOn) {
		this.closedOn = closedOn;
	}

	public String getComponent() {
		return this.component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getOpenedDate() {
		return this.openedDate;
	}

	public void setOpenedDate(Timestamp openedDate) {
		this.openedDate = openedDate;
	}

	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public BigDecimal getStars() {
		return this.stars;
	}

	public void setStars(BigDecimal stars) {
		this.stars = stars;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTypeBug() {
		return this.typeBug;
	}

	public void setTypeBug(String typeBug) {
		this.typeBug = typeBug;
	}

	public Developer getOwner() {
		return this.owner;
	}

	public void setOwner(Developer owner) {
		this.owner = owner;
	}

	public Developer getReportedBy() {
		return this.reportedBy;
	}

	public void setReportedBy(Developer reportedBy) {
		this.reportedBy = reportedBy;
	}

	public List<BugComment> getComments() {
		return this.comments;
	}

	public void setComments(List<BugComment> comments) {
		this.comments = comments;
	}

	public BugComment addComment(BugComment comment) {
		getComments().add(comment);
		comment.setBug(this);

		return comment;
	}

	public BugComment removeComment(BugComment comment) {
		getComments().remove(comment);
		comment.setBug(null);

		return comment;
	}

}