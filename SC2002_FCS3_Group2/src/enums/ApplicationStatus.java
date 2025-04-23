package enums;


/**
 * Represents the status of a BTO application throughout its lifecycle.
 * <ul>
 *   <li>PENDING: initial state upon submission;</li>
 *   <li>SUCCESSFUL: approved application;</li>
 *   <li>UNSUCCESSFUL: rejected application;</li>
 *   <li>BOOKED: flat booked after approval;</li>
 *   <li>WITHDRAW_REQUEST: applicant has requested a withdrawal;</li>
 *   <li>WITHDRAWN: application has been withdrawn.</li>
 * </ul>
 */
public enum ApplicationStatus {
	PENDING,
	SUCCESSFUL,
	UNSUCCESSFUL,
	BOOKED,
	WITHDRAW_REQUEST,
	WITHDRAWN
}
