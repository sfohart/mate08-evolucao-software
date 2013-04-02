package mining.challenge.android.bugreport.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the developer database table.
 * 
 */
@Entity
public class Developer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String email;

	//bi-directional many-to-one association to Bug
	@OneToMany(mappedBy="owner")
	private List<Bug> ownedBugs;

	//bi-directional many-to-one association to Bug
	@OneToMany(mappedBy="reportedBy")
	private List<Bug> reportedBugs;

	//bi-directional many-to-one association to BugComment
	@OneToMany(mappedBy="author")
	private List<BugComment> bugComments;

	public Developer() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Bug> getOwnedBugs() {
		return this.ownedBugs;
	}

	public void setOwnedBugs(List<Bug> ownedBugs) {
		this.ownedBugs = ownedBugs;
	}

	public Bug addOwnedBug(Bug ownedBug) {
		getOwnedBugs().add(ownedBug);
		ownedBug.setOwner(this);

		return ownedBug;
	}

	public Bug removeOwnedBug(Bug ownedBug) {
		getOwnedBugs().remove(ownedBug);
		ownedBug.setOwner(null);

		return ownedBug;
	}

	public List<Bug> getReportedBugs() {
		return this.reportedBugs;
	}

	public void setReportedBugs(List<Bug> reportedBugs) {
		this.reportedBugs = reportedBugs;
	}

	public Bug addReportedBug(Bug reportedBug) {
		getReportedBugs().add(reportedBug);
		reportedBug.setReportedBy(this);

		return reportedBug;
	}

	public Bug removeReportedBug(Bug reportedBug) {
		getReportedBugs().remove(reportedBug);
		reportedBug.setReportedBy(null);

		return reportedBug;
	}

	public List<BugComment> getBugComments() {
		return this.bugComments;
	}

	public void setBugComments(List<BugComment> bugComments) {
		this.bugComments = bugComments;
	}

	public BugComment addBugComment(BugComment bugComment) {
		getBugComments().add(bugComment);
		bugComment.setAuthor(this);

		return bugComment;
	}

	public BugComment removeBugComment(BugComment bugComment) {
		getBugComments().remove(bugComment);
		bugComment.setAuthor(null);

		return bugComment;
	}

}